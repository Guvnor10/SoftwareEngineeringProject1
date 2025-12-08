package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

public class ViewFoodPageCodeBehind {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableColumn<Stock, String> attributesColumn;

	@FXML
	private TableColumn<Stock, String> compartmentColumn;

	@FXML
	private TableColumn<Stock, String> conditionColumn;

	@FXML
	private DatePicker endDatePicker;

	@FXML
	private TableColumn<Stock, LocalDate> expirationDateColumn;

	@FXML
	private CheckBox flammableFilterCheckBox;

	@FXML
	private Button homeButton;

	@FXML
	private TableColumn<Stock, String> itemNameColumn;

	@FXML
	private CheckBox liquidFilterCheckBox;

	@FXML
	private Button logoutButton;

	@FXML
	private CheckBox perishableFilterCheckBox;

	@FXML
	private TableColumn<Stock, Integer> quantityColumn;

	@FXML
	private TableColumn<Stock, String> sizeColumn;

	@FXML
	private DatePicker startDatePicker;

	@FXML
	private Label statusLabel;

	@FXML
	private TableView<Stock> stockTableView;

	@FXML
	private TableColumn<Stock, LocalDateTime> timeAddedColumn;
	
	@FXML
	void backToLandingPage(ActionEvent event) {
		switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml");
	}

	@FXML
	void backToLoginPage(ActionEvent event) {
		switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml");
	}
	
	private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            if (this.statusLabel != null) {
                this.statusLabel.setText("Unable to load page: " + fxmlPath);
            }
        }
    }
	
	private void configureTableColumns() {
	    this.itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	    this.quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	    this.sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
	    this.attributesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAttributes().toString()));
	    this.conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
	    this.expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
	    this.compartmentColumn.setCellValueFactory(new PropertyValueFactory<>("compartment"));
	}

	@FXML
	void handleApplyFilters(ActionEvent event) {

	}

	@FXML
	void handleClearFilters(ActionEvent event) {

	}

	@FXML
	void handleRefresh(ActionEvent event) {

	}

	@FXML
	void initialize() {
		 configureTableColumns();
		
		assert attributesColumn != null
				: "fx:id=\"attributesColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert compartmentColumn != null
				: "fx:id=\"compartmentColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert conditionColumn != null
				: "fx:id=\"conditionColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert endDatePicker != null
				: "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert expirationDateColumn != null
				: "fx:id=\"expirationDateColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert flammableFilterCheckBox != null
				: "fx:id=\"flammableFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert itemNameColumn != null
				: "fx:id=\"itemNameColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert liquidFilterCheckBox != null
				: "fx:id=\"liquidFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert logoutButton != null
				: "fx:id=\"logoutButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert perishableFilterCheckBox != null
				: "fx:id=\"perishableFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert quantityColumn != null
				: "fx:id=\"quantityColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert sizeColumn != null : "fx:id=\"sizeColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert startDatePicker != null
				: "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert statusLabel != null
				: "fx:id=\"statusLabel\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert stockTableView != null
				: "fx:id=\"stockTableView\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert timeAddedColumn != null
				: "fx:id=\"timeAddedColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";

	}

}
