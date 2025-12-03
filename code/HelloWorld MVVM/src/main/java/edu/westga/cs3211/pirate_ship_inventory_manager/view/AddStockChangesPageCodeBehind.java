package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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

/** Class
 * @author gn00021
 * The Class AddStockChangesPageCodeBehind.
 * @version Fall2025
 */
public class AddStockChangesPageCodeBehind {

	/** The Constant CHANGE_LOG. */
	public static final List<StockChangeEntry> CHANGE_LOG = new ArrayList<>();

	/** The Constant INVENTORY. */
	private static final Inventory INVENTORY = Inventory.getSharedInventory();

	/** The name text field. */
	@FXML
	private TextField nameTextField;

	/** The quantity text field. */
	@FXML
	private TextField quantityTextField;

	/** The size text field. */
	@FXML
	private TextField sizeTextField;

	/** The flammable check box. */
	@FXML
	private CheckBox flammableCheckBox;

	/** The perishable check box. */
	@FXML
	private CheckBox perishableCheckBox;

	/** The liquid check box. */
	@FXML
	private CheckBox liquidCheckBox;

	/** The perfect condition check box. */
	@FXML
	private CheckBox perfectConditionCheckBox;

	/** The usable condition check box. */
	@FXML
	private CheckBox usableConditionCheckBox;

	/** The unusable condition check box. */
	@FXML
	private CheckBox unusableConditionCheckBox;

	/** The condition text area. */
	@FXML
	private TextArea conditionTextArea;

	/** The expiration date picker. */
	@FXML
	private DatePicker expirationDatePicker;

	/** The compartment combo box. */
	@FXML
	private ComboBox<Compartment> compartmentComboBox;

	/** The status label. */
	@FXML
	private Label statusLabel;

	/** The add stock button. */
	@FXML
	private Button addStockButton;

	/** The logout button. */
	@FXML
	private Button logoutButton;

	/** The home button. */
	@FXML
	private Button homeButton;

	/**
	 * Determine condition from check boxes.
	 *
	 * @return the stock condition
	 */
	private StockCondition determineConditionFromCheckBoxes() {
		boolean perfect = this.perfectConditionCheckBox.isSelected();
		boolean usable = this.usableConditionCheckBox.isSelected();
		boolean unusable = this.unusableConditionCheckBox.isSelected();

		int count = (perfect ? 1 : 0) + (usable ? 1 : 0) + (unusable ? 1 : 0);
		if (count != 1) {
			return null;
		}

		if (perfect) {
			return StockCondition.PERFECT;
		}
		if (usable) {
			return StockCondition.USABLE;
		}
		return StockCondition.UNUSABLE;
	}

	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {
		this.compartmentComboBox.setItems(FXCollections.observableArrayList(INVENTORY.getCompartments()));

		this.compartmentComboBox.setPromptText("Select compartment");
		this.statusLabel.setText("");
	}

	/**
	 * Handle add stock.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleAddStock(ActionEvent event) {

	    this.statusLabel.setText("");

	    String name = this.nameTextField.getText();
	    String quantityText = this.quantityTextField.getText();
	    String sizeText = this.sizeTextField.getText();
	    String conditionNotes = this.conditionTextArea.getText();
	    LocalDate expirationDate = this.expirationDatePicker.getValue();

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
	        size = (sizeText == null || sizeText.isBlank()) ? 0.0 : Double.parseDouble(sizeText);
	    } catch (Exception ex) {
	        this.statusLabel.setText("Size must be a numeric value.");
	        return;
	    }

	    // --- condition from check boxes ---
	    StockCondition conditionEnum = this.determineConditionFromCheckBoxes();
	    if (conditionEnum == null) {
	        this.statusLabel.setText("Select EXACTLY ONE condition: Perfect, Usable, or Unusable.");
	        return;
	    }

	    String fullCondition = conditionEnum.name();
	    if (conditionNotes != null && !conditionNotes.isBlank()) {
	        fullCondition += " - " + conditionNotes.trim();
	    }

	    // --- attributes from check boxes ---
	    EnumSet<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);
	    if (this.flammableCheckBox.isSelected()) {
	        attributes.add(StockAttributes.FLAMMABLE);
	    }
	    if (this.perishableCheckBox.isSelected()) {
	        attributes.add(StockAttributes.PERISHABLE);
	    }
	    if (this.liquidCheckBox.isSelected()) {
	        attributes.add(StockAttributes.LIQUID);
	    }

	    if (attributes.contains(StockAttributes.PERISHABLE) && expirationDate == null) {
	        this.statusLabel.setText("Perishable items MUST include an expiration date.");
	        return;
	    }

	    final Stock stock;
	    try {
	        stock = new Stock(name, quantity, size, attributes, fullCondition, expirationDate);
	    } catch (IllegalArgumentException ex) {
	        this.statusLabel.setText(ex.getMessage());
	        return;
	    }

	    // ---------- compartment capacity / qualities logic ----------
	    List<Compartment> compartments = INVENTORY.getCompartments();

	    // compartments that match all special qualities
	    List<Compartment> qualityMatches = new ArrayList<>();
	    for (Compartment comp : compartments) {
	        if (comp.getSpecialQualities().containsAll(stock.getAttributes())) {
	            qualityMatches.add(comp);
	        }
	    }

	    if (!attributes.isEmpty() && qualityMatches.isEmpty()) {
	        this.statusLabel.setText("No compartment supports qualities: " + attributes);
	        return;
	    }

	    List<Compartment> baseList = qualityMatches.isEmpty() ? compartments : qualityMatches;

	    // compartments that actually have enough space
	    List<Compartment> compatible = new ArrayList<>();
	    for (Compartment comp : baseList) {
	        if (comp.canStore(stock)) {
	            compatible.add(comp);
	        }
	    }

	    if (compatible.isEmpty()) {
	        // show compartments that at least have some space, so user can adjust quantity
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
	            this.compartmentComboBox.setPromptText("Reduce quantity; select a compartment.");
	            this.statusLabel.setText("Quantity too large. Reduce quantity to fit available space.");
	        }
	        return;
	    }

	    // show only the compatible compartments in the combo box
	    this.compartmentComboBox.setItems(FXCollections.observableArrayList(compatible));

	    // ---------- NEW REQUIREMENT: user MUST choose a compartment ----------
	    Compartment selected = this.compartmentComboBox.getValue();
	    if (selected == null || !compatible.contains(selected)) {
	        this.statusLabel.setText("Please select a compartment from the list before adding stock.");
	        this.compartmentComboBox.requestFocus();
	        return;
	    }

	    // ---------- actually add the stock ----------
	    selected.addStock(stock);

	    String crewMateId = LoginViewModel.getLoggedInUser();
	    if (crewMateId == null) {
	        crewMateId = "Crewmate";
	    }

	    LocalDateTime timestamp = LocalDateTime.now();
	    CHANGE_LOG.add(new StockChangeEntry(stock, crewMateId, timestamp, selected));

	    String message = "Stock Added Successfully!\n\n"
	            + "Item: " + stock.getName() + "\n"
	            + "Qty: " + stock.getQuantity() + "\n"
	            + "Condition: " + stock.getCondition() + "\n"
	            + "Attributes: " + stock.getAttributes() + "\n"
	            + "Compartment: " + selected.toString() + "\n"
	            + "Remaining Capacity: " + selected.getFreeSpace() + "\n"
	            + "Added By: " + crewMateId + "\n"
	            + "Time: " + timestamp;

	    this.statusLabel.setText(message);

	    // ---------- reset UI ----------
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
	    this.compartmentComboBox.setItems(FXCollections.observableArrayList(INVENTORY.getCompartments()));
	    this.compartmentComboBox.setPromptText("Select compartment");
	}


	/**
	 * Back to landing page.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	private void backToLandingPage(ActionEvent event) throws IOException {
		if (LoginViewModel.getLoggedInRole() == null) {

			Parent root = FXMLLoader.load(
					getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));
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

	/**
	 * Back to login page.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	private void backToLoginPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

		Stage stage = (Stage) this.logoutButton.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
	}
}
