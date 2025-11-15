package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Code-behind for the AddStockChangesPage.
 *
 * Used by both Crewmate and Quartermaster to add stock items.
 */
public class AddStockChangesPageCodeBehind {

    // A simple shared inventory for this application.
    // (You could inject this via a ViewModel instead.)
    private static final Inventory INVENTORY = new Inventory();

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addStockButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TextField sizeTextField;

    @FXML
    private CheckBox flammableCheckBox;

    @FXML
    private CheckBox perishableCheckBox;

    @FXML
    private CheckBox liquidCheckBox;

    @FXML
    private TextArea conditionTextArea;

    @FXML
    private DatePicker expirationDatePicker;

    @FXML
    private Label statusLabel;

    // We keep track of who came here last, to know which Home to go back to.
    private static String lastRole = "CREWMATE"; // default

    /**
     * Called by landing pages before loading this view, to remember the role.
     */
    public static void setLastRole(String role) {
        lastRole = role;
    }

    @FXML
    private void initialize() {
        if (this.statusLabel != null) {
            this.statusLabel.setText("");
        }
    }

    @FXML
    private void handleAddStock() {
        this.statusLabel.setText("");

        String name = this.nameTextField.getText();
        String quantityText = this.quantityTextField.getText();
        String sizeText = this.sizeTextField.getText();
        String condition = this.conditionTextArea.getText();
        LocalDate expirationDate = this.expirationDatePicker.getValue();

        // Basic validation
        if (name == null || name.isBlank()) {
            this.statusLabel.setText("Item name is required.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException ex) {
            this.statusLabel.setText("Quantity must be a whole number.");
            return;
        }

        double size;
        try {
            if (sizeText == null || sizeText.isBlank()) {
                size = 0.0;
            } else {
                size = Double.parseDouble(sizeText);
            }
        } catch (NumberFormatException ex) {
            this.statusLabel.setText("Size must be a number.");
            return;
        }

        if (condition == null) {
            condition = "";
        }

        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);
        if (this.flammableCheckBox.isSelected()) {
            attributes.add(StockAttributes.FLAMMABLE);
        }
        if (this.perishableCheckBox.isSelected()) {
            attributes.add(StockAttributes.PERISHABLE);
        }
        if (this.liquidCheckBox.isSelected()) {
            attributes.add(StockAttributes.LIQUID);
        }

        try {
            Stock stock = new Stock(name, quantity, size,
                    attributes, condition, expirationDate);

            INVENTORY.addStock(stock);

            this.statusLabel.setText("Stock added successfully.");

            // clear fields
            this.nameTextField.clear();
            this.quantityTextField.clear();
            this.sizeTextField.clear();
            this.conditionTextArea.clear();
            this.flammableCheckBox.setSelected(false);
            this.perishableCheckBox.setSelected(false);
            this.liquidCheckBox.setSelected(false);
            this.expirationDatePicker.setValue(null);

        } catch (IllegalArgumentException ex) {
            this.statusLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void backToLandingPage() throws IOException {
        Stage stage = (Stage) this.homeButton.getScene().getWindow();
        Parent root;

        if ("QUARTERMASTER".equalsIgnoreCase(lastRole)) {
            root = FXMLLoader.load(getClass().getResource(
                "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml"));
        } else {
            root = FXMLLoader.load(getClass().getResource(
                "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml"));
        }

        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void backToLoginPage() throws IOException {
        Stage stage = (Stage) this.logoutButton.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource(
            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Exposes inventory for other parts of the app (e.g. View Stock Changes page).
     */
    public static Inventory getInventory() {
        return INVENTORY;
    }
}

