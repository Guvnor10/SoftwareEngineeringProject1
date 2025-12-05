package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ViewFoodPageCodeBehind {

	@FXML
	private TableColumn<?, ?> attributesColumn;

	@FXML
	private TableColumn<?, ?> compartmentColumn;

	@FXML
	private TableColumn<?, ?> conditionColumn;

	@FXML
	private DatePicker endDatePicker;

	@FXML
	private TableColumn<?, ?> expirationDateColumn;

	@FXML
	private CheckBox flammableFilterCheckBox;

	@FXML
	private Button homeButton;

	@FXML
	private TableColumn<?, ?> itemNameColumn;

	@FXML
	private CheckBox liquidFilterCheckBox;

	@FXML
	private Button logoutButton;

	@FXML
	private CheckBox perishableFilterCheckBox;

	@FXML
	private TableColumn<?, ?> quantityColumn;

	@FXML
	private TableColumn<?, ?> sizeColumn;

	@FXML
	private DatePicker startDatePicker;

	@FXML
	private Label statusLabel;

	@FXML
	private TableView<?> stockTableView;

	@FXML
	private TableColumn<?, ?> timeAddedColumn;

	@FXML
	void backToLandingPage(ActionEvent event) {

	}

	@FXML
	void backToLoginPage(ActionEvent event) {

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

}
