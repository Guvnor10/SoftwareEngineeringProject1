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

/** class
 * The Class ViewFoodPageCodeBehind.
 * @version f25
 * @author gn00021
 */
public class ViewFoodPageCodeBehind {

	/** The resources. */
	@FXML
	private ResourceBundle resources;

	/** The location. */
	@FXML
	private URL location;

	/** The attributes column. */
	@FXML
	private TableColumn<Stock, String> attributesColumn;

	/** The compartment column. */
	@FXML
	private TableColumn<Stock, String> compartmentColumn;

	/** The condition column. */
	@FXML
	private TableColumn<Stock, String> conditionColumn;

	/** The end date picker. */
	@FXML
	private DatePicker endDatePicker;

	/** The expiration date column. */
	@FXML
	private TableColumn<Stock, LocalDate> expirationDateColumn;

	/** The flammable filter check box. */
	@FXML
	private CheckBox flammableFilterCheckBox;

	/** The home button. */
	@FXML
	private Button homeButton;

	/** The item name column. */
	@FXML
	private TableColumn<Stock, String> itemNameColumn;

	/** The liquid filter check box. */
	@FXML
	private CheckBox liquidFilterCheckBox;

	/** The logout button. */
	@FXML
	private Button logoutButton;

	/** The perishable filter check box. */
	@FXML
	private CheckBox perishableFilterCheckBox;

	/** The quantity column. */
	@FXML
	private TableColumn<Stock, Integer> quantityColumn;

	/** The size column. */
	@FXML
	private TableColumn<Stock, String> sizeColumn;

	/** The start date picker. */
	@FXML
	private DatePicker startDatePicker;

	/** The status label. */
	@FXML
	private Label statusLabel;

	/** The stock table view. */
	@FXML
	private TableView<Stock> stockTableView;

	/** The time added column. */
	@FXML
	private TableColumn<Stock, LocalDateTime> timeAddedColumn;
	
	/**
	 * Back to landing page.
	 *
	 * @param event the event
	 */
	@FXML
	void backToLandingPage(ActionEvent event) {
		this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml");
	}

	/**
	 * Back to login page.
	 *
	 * @param event the event
	 */
	@FXML
	void backToLoginPage(ActionEvent event) {
		this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml");
	}
	
	/**
	 * Switch scene.
	 *
	 * @param event the event
	 * @param fxmlPath the fxml path
	 */
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
	
	/**
	 * Configure table columns.
	 */
	private void configureTableColumns() {
	    this.itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	    this.quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	    this.sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
	    this.attributesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAttributes().toString()));
	    this.conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
	    this.expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
	    this.compartmentColumn.setCellValueFactory(new PropertyValueFactory<>("compartment"));
	}

	/**
	 * Handle apply filters.
	 *
	 * @param event the event
	 */
	@FXML
	void handleApplyFilters(ActionEvent event) {

	}

	/**
	 * Handle clear filters.
	 *
	 * @param event the event
	 */
	@FXML
	void handleClearFilters(ActionEvent event) {

	}

	/**
	 * Handle refresh.
	 *
	 * @param event the event
	 */
	@FXML
	void handleRefresh(ActionEvent event) {

	}

	/**
	 * Initialize.
	 */
	@FXML
	void initialize() {
		 this.configureTableColumns();
		
		assert this.attributesColumn != null
				: "fx:id=\"attributesColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.compartmentColumn != null
				: "fx:id=\"compartmentColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.conditionColumn != null
				: "fx:id=\"conditionColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.endDatePicker != null
				: "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.expirationDateColumn != null
				: "fx:id=\"expirationDateColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.flammableFilterCheckBox != null
				: "fx:id=\"flammableFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.itemNameColumn != null
				: "fx:id=\"itemNameColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.liquidFilterCheckBox != null
				: "fx:id=\"liquidFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.logoutButton != null
				: "fx:id=\"logoutButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.perishableFilterCheckBox != null
				: "fx:id=\"perishableFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.quantityColumn != null
				: "fx:id=\"quantityColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.sizeColumn != null : "fx:id=\"sizeColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.startDatePicker != null
				: "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.statusLabel != null
				: "fx:id=\"statusLabel\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.stockTableView != null
				: "fx:id=\"stockTableView\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
		assert this.timeAddedColumn != null
				: "fx:id=\"timeAddedColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";

	}

}
