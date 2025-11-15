package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import edu.westga.cs3211.pirate_ship_inventory_manager.Main;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockCondition;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStockChangesPageCodeBehind {

	// ======= INVENTORY + CHANGE LOG (static references) =======
	private static final Inventory INVENTORY = Inventory.getSharedInventory();
	public static final List<StockChangeEntry> CHANGE_LOG = new ArrayList<>();

	// ======= UI FIELDS =======
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField quantityTextField;
	@FXML
	private TextField sizeTextField;

	// Special qualities checkboxes
	@FXML
	private CheckBox flammableCheckBox;
	@FXML
	private CheckBox perishableCheckBox;
	@FXML
	private CheckBox liquidCheckBox;

	// Condition checkboxes
	@FXML
	private CheckBox perfectConditionCheckBox;
	@FXML
	private CheckBox usableConditionCheckBox;
	@FXML
	private CheckBox unusableConditionCheckBox;

	@FXML
	private TextArea conditionTextArea;

	@FXML
	private DatePicker expirationDatePicker;

	@FXML
	private ComboBox<Compartment> compartmentComboBox;

	@FXML
	private Label statusLabel;

	@FXML
	private Button addStockButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button homeButton;

	// ======= Helper Method: Ensure only ONE condition is checked =======
	private StockCondition determineConditionFromCheckBoxes() {
		boolean perfect = this.perfectConditionCheckBox.isSelected();
		boolean usable = this.usableConditionCheckBox.isSelected();
		boolean unusable = this.unusableConditionCheckBox.isSelected();

		int count = (perfect ? 1 : 0) + (usable ? 1 : 0) + (unusable ? 1 : 0);
		if (count != 1) {
			return null; // either none or more than 1 chosen
		}

		if (perfect)
			return StockCondition.PERFECT;
		if (usable)
			return StockCondition.USABLE;
		return StockCondition.UNUSABLE;
	}

	// ======= INITIALIZE =======
	@FXML
	public void initialize() {
		this.compartmentComboBox.setItems(FXCollections.observableArrayList(INVENTORY.getCompartments()));

		this.compartmentComboBox.setPromptText("Select compartment");
		this.statusLabel.setText("");
	}

	// ======= MAIN USE CASE LOGIC =======
	@FXML
	private void handleAddStock(ActionEvent event) {

		this.statusLabel.setText("");

		// === Read user input ===
		String name = this.nameTextField.getText();
		String quantityText = this.quantityTextField.getText();
		String sizeText = this.sizeTextField.getText();
		String conditionNotes = this.conditionTextArea.getText();
		LocalDate expirationDate = this.expirationDatePicker.getValue();

		// === Validate required fields ===
		if (name == null || name.isBlank()) {
			this.statusLabel.setText("Item name is required.");
			return;
		}

		int quantity;
		try {
			quantity = Integer.parseInt(quantityText);
		} catch (Exception ex) {
			this.statusLabel.setText("Quantity must be a whole number.");
			return;
		}

		double size;
		try {
			size = sizeText.isBlank() ? 0.0 : Double.parseDouble(sizeText);
		} catch (Exception ex) {
			this.statusLabel.setText("Size must be a numeric value.");
			return;
		}

		// === Determine condition (must be exactly ONE) ===
		StockCondition conditionEnum = determineConditionFromCheckBoxes();
		if (conditionEnum == null) {
			this.statusLabel.setText("Select EXACTLY ONE condition: Perfect, Usable, or Unusable.");
			return;
		}

		// Combine enum + user notes
		String fullCondition = conditionEnum.name();
		if (conditionNotes != null && !conditionNotes.isBlank()) {
			fullCondition += " - " + conditionNotes.trim();
		}

		// === Special qualities ===
		EnumSet<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);
		if (this.flammableCheckBox.isSelected())
			attributes.add(StockAttributes.FLAMMABLE);
		if (this.perishableCheckBox.isSelected())
			attributes.add(StockAttributes.PERISHABLE);
		if (this.liquidCheckBox.isSelected())
			attributes.add(StockAttributes.LIQUID);

		// Perishable must have expiration date
		if (attributes.contains(StockAttributes.PERISHABLE) && expirationDate == null) {
			this.statusLabel.setText("Perishable items MUST include an expiration date.");
			return;
		}

		// === Construct the Stock object ===
		final Stock stock;
		try {
			stock = new Stock(name, quantity, size, attributes, fullCondition, expirationDate);
		} catch (IllegalArgumentException ex) {
			this.statusLabel.setText(ex.getMessage());
			return;
		}

		// === Step 4: Determine compatible compartments ===
		List<Compartment> compartments = INVENTORY.getCompartments();

		// First filter: compartments with required qualities
		List<Compartment> qualityMatches = new ArrayList<>();
		for (Compartment comp : compartments) {
			if (comp.getSpecialQualities().containsAll(stock.getAttributes())) {
				qualityMatches.add(comp);
			}
		}

		// If special qualities required but no compatible compartment
		if (!attributes.isEmpty() && qualityMatches.isEmpty()) {
			this.statusLabel.setText("No compartment supports qualities: " + attributes);
			return;
		}

		// Next filter: capacity check
		List<Compartment> compatible = new ArrayList<>();
		List<Compartment> baseList = qualityMatches.isEmpty() ? compartments : qualityMatches;

		for (Compartment comp : baseList) {
			if (comp.canStore(stock)) {
				compatible.add(comp);
			}
		}

		// Alternative flow: no compartment has enough space
		if (compatible.isEmpty()) {

			List<Compartment> hasSomeSpace = new ArrayList<>();
			for (Compartment comp : baseList) {
				if (comp.getFreeSpace() > 0) {
					hasSomeSpace.add(comp);
				}
			}

			if (hasSomeSpace.isEmpty()) {
				this.statusLabel.setText("No compartment has ANY capacity for this item.");
			} else {
				this.compartmentComboBox.setItems(FXCollections.observableArrayList(hasSomeSpace));
				this.statusLabel.setText("Quantity too large. Reduce quantity to fit available space.");
			}
			return;
		}

		// Present list of compatible compartments
		this.compartmentComboBox.setItems(FXCollections.observableArrayList(compatible));

		Compartment selected = this.compartmentComboBox.getValue();
		if (selected == null) {
			selected = compatible.get(0);
			this.compartmentComboBox.setValue(selected);
		}

		// === Add stock to compartment ===
		selected.addStock(stock);

		// === Log the stock change ===
		String crewMateId = LoginViewModel.getLoggedInUser();
		if (crewMateId == null) {
			crewMateId = "Crewmate";
		}

		LocalDateTime timestamp = LocalDateTime.now();

		CHANGE_LOG.add(new StockChangeEntry(stock, crewMateId, timestamp, selected));

		// === Confirmation message ===
		String message = "Stock Added Successfully!\n\n" + "Item: " + stock.getName() + "\n" + "Qty: "
				+ stock.getQuantity() + "\n" + "Condition: " + stock.getCondition() + "\n" + "Attributes: "
				+ stock.getAttributes() + "\n" + "Compartment: " + selected.toString() + "\n" + "Remaining Capacity: "
				+ selected.getFreeSpace() + "\n" + "Added By: " + crewMateId + "\n" + "Time: " + timestamp;

		this.statusLabel.setText(message);

		// === Reset form ===
		this.nameTextField.clear();
		this.quantityTextField.clear();
		this.sizeTextField.clear();
		this.conditionTextArea.clear();
		this.expirationDatePicker.setValue(null);

		this.flammableCheckBox.setSelected(false);
		this.perishableCheckBox.setSelected(false);
		this.liquidCheckBox.setSelected(false);

		this.perfectConditionCheckBox.setSelected(false);
		this.usableConditionCheckBox.setSelected(false);
		this.unusableConditionCheckBox.setSelected(false);

		this.compartmentComboBox.setValue(null);

		// Refresh compartment list
		this.compartmentComboBox.setItems(FXCollections.observableArrayList(INVENTORY.getCompartments()));
		this.compartmentComboBox.setPromptText("Select compartment");
	}
	
	@FXML
	private void backToLandingPage(ActionEvent event) throws IOException {
		if (LoginViewModel.getLoggedInRole() == null) {
	        // If somehow null → send to login
	        Parent root = FXMLLoader.load(getClass().getResource(
	            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));
	        Stage stage = (Stage) this.homeButton.getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.show();
	        return;
	    }

	    String fxml;

	    switch (LoginViewModel.getLoggedInRole()) {
	        case CREWMATE:
	            fxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml";
	            break;

	        case QUARTERMASTER:
	            fxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml";
	            break;

	        default:
	            fxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml";
	            break;
	    }

	    Parent root = FXMLLoader.load(getClass().getResource(fxml));
	    Stage stage = (Stage) this.homeButton.getScene().getWindow();
	    stage.setScene(new Scene(root));
	    stage.show();
	}

	@FXML
	private void backToLoginPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

		Stage stage = (Stage) this.logoutButton.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
	}
}
