package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * Code-behind for the View Stock Changes page.
 * Quartermasters use this to see all stock in the inventory.
 */
public class ViewStockChangesPageCodeBehind {

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<Stock> stockTableView;

    @FXML
    private TableColumn<Stock, String> nameColumn;

    @FXML
    private TableColumn<Stock, Integer> quantityColumn;

    @FXML
    private TableColumn<Stock, Double> sizeColumn;

    @FXML
    private TableColumn<Stock, String> attributesColumn;

    @FXML
    private TableColumn<Stock, String> conditionColumn;

    @FXML
    private TableColumn<Stock, LocalDate> expirationDateColumn;

    @FXML
    private void initialize() {
        // Basic fx:id checks
        assert this.stockTableView != null : "fx:id=\"stockTableView\" was not injected: check your FXML file 'ViewStockChangesPage.fxml'.";
        assert this.nameColumn != null : "fx:id=\"nameColumn\" was not injected.";
        assert this.quantityColumn != null : "fx:id=\"quantityColumn\" was not injected.";
        assert this.sizeColumn != null : "fx:id=\"sizeColumn\" was not injected.";
        assert this.attributesColumn != null : "fx:id=\"attributesColumn\" was not injected.";
        assert this.conditionColumn != null : "fx:id=\"conditionColumn\" was not injected.";
        assert this.expirationDateColumn != null : "fx:id=\"expirationDateColumn\" was not injected.";

        // Bind columns to Stock properties
        this.nameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));

        this.quantityColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        this.sizeColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSize()).asObject());

        this.attributesColumn.setCellValueFactory(cellData -> {
            String attrs = cellData.getValue().getAttributes()
                    .stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(attrs);
        });

        this.conditionColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCondition()));

        this.expirationDateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getExpirationDate()));

        if (this.statusLabel != null) {
            this.statusLabel.setText("");
        }

        loadStockItems();
    }

    @FXML
    private void handleRefresh() {
        loadStockItems();
    }

    private void loadStockItems() {
        Inventory inventory = AddStockChangesPageCodeBehind.getInventory();

        ObservableList<Stock> items =
                FXCollections.observableArrayList(inventory.getAllStock());

        this.stockTableView.setItems(items);

        if (this.statusLabel != null) {
            this.statusLabel.setText(items.size() + " item(s) loaded.");
        }
    }

    @FXML
    private void backToLandingPage() throws IOException {
        // View Stock Changes is a Quartermaster feature,
        // so Home goes back to QuarterMasterLandingPage.
        Parent root = FXMLLoader.load(getClass().getResource(
            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml"));

        Stage stage = (Stage) this.homeButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void backToLoginPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(
            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

        Stage stage = (Stage) this.logoutButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
