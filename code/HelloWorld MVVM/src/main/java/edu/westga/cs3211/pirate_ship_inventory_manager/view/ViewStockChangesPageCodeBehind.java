package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * Class
 * 
 * @author gn00021 The Class ViewStockChangesPageCodeBehind.
 * @version Fall2025
 */
public class ViewStockChangesPageCodeBehind {

	/** The home button. */
	@FXML
	private Button homeButton;

	/** The logout button. */
	@FXML
	private Button logoutButton;

	/** The apply filters button. */
	@FXML
	private Button applyFiltersButton;

	/** The clear filters button. */
	@FXML
	private Button clearFiltersButton;

	/** The refresh button. */
	@FXML
	private Button refreshButton;

	/** The status label. */
	@FXML
	private Label statusLabel;

	/** The stock table view. */
	@FXML
	private TableView<StockChangeEntry> stockTableView;

	/** The item name column. */
	@FXML
	private TableColumn<StockChangeEntry, String> itemNameColumn;

	/** The quantity column. */
	@FXML
	private TableColumn<StockChangeEntry, Integer> quantityColumn;

	/** The size column. */
	@FXML
	private TableColumn<StockChangeEntry, Double> sizeColumn;

	/** The attributes column. */
	@FXML
	private TableColumn<StockChangeEntry, String> attributesColumn;

	/** The condition column. */
	@FXML
	private TableColumn<StockChangeEntry, String> conditionColumn;

	/** The expiration date column. */
	@FXML
	private TableColumn<StockChangeEntry, LocalDate> expirationDateColumn;

	/** The compartment column. */
	@FXML
	private TableColumn<StockChangeEntry, String> compartmentColumn;

	/** The added by column. */
	@FXML
	private TableColumn<StockChangeEntry, String> addedByColumn;

	/** The time added column. */
	@FXML
	private TableColumn<StockChangeEntry, LocalDateTime> timeAddedColumn;

	/** The flammable filter check box. */
	// Filters
	@FXML
	private CheckBox flammableFilterCheckBox;

	/** The perishable filter check box. */
	@FXML
	private CheckBox perishableFilterCheckBox;

	/** The liquid filter check box. */
	@FXML
	private CheckBox liquidFilterCheckBox;

	/** The crew mate filter combo box. */
	@FXML
	private ComboBox<String> crewMateFilterComboBox;

	/** The start date picker. */
	@FXML
	private DatePicker startDatePicker;

	/** The end date picker. */
	@FXML
	private DatePicker endDatePicker;

	/**
	 * Initialize.
	 */
	@FXML
	private void initialize() {
		this.configureTableColumns();
		this.populateCrewMateFilterOptions();
		this.refreshTableFromLog();
		this.statusLabel.setText("");
	}

	/**
	 * Configure table columns.
	 */
	@FXML
	private void configureTableColumns() {

		this.itemNameColumn.setCellValueFactory(cell -> {
			Stock stock = cell.getValue().getStock();
			return new SimpleStringProperty(stock.getName());
		});

		this.quantityColumn.setCellValueFactory(cell -> {
			Stock stock = cell.getValue().getStock();
			return new SimpleIntegerProperty(stock.getQuantity()).asObject();
		});

		this.sizeColumn.setCellValueFactory(cell -> {
			Stock stock = cell.getValue().getStock();
			return new SimpleDoubleProperty(stock.getSize()).asObject();
		});

		this.attributesColumn.setCellValueFactory(cell -> {
			Set<StockAttributes> attrs = cell.getValue().getStock().getAttributes();
			return new SimpleStringProperty(attrs.toString());
		});

		this.conditionColumn.setCellValueFactory(cell -> {
			Stock stock = cell.getValue().getStock();
			return new SimpleStringProperty(stock.getCondition());
		});

		this.expirationDateColumn.setCellValueFactory(cell -> {
			Stock stock = cell.getValue().getStock();
			return new SimpleObjectProperty<>(stock.getExpirationDate());
		});

		this.compartmentColumn.setCellValueFactory(cell -> {
			String compText = cell.getValue().getCompartment().toString();
			return new SimpleStringProperty(compText);
		});

		this.addedByColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddedBy()));

		this.timeAddedColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getTimestamp()));
	}

	/**
	 * Refresh table from log.
	 */
	@FXML
	private void refreshTableFromLog() {

		List<StockChangeEntry> sorted = new ArrayList<>(AddStockChangesPageCodeBehind.CHANGE_LOG);
		sorted.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());

		ObservableList<StockChangeEntry> items = FXCollections.observableArrayList(sorted);
		this.stockTableView.setItems(items);
	}

	/**
	 * Populate crew mate filter options.
	 */
	@FXML
	private void populateCrewMateFilterOptions() {

		List<String> crewNames = AddStockChangesPageCodeBehind.CHANGE_LOG.stream().map(StockChangeEntry::getAddedBy)
				.distinct().sorted().collect(Collectors.toList());

		this.crewMateFilterComboBox.setItems(FXCollections.observableArrayList(crewNames));
		this.crewMateFilterComboBox.setPromptText("All crewmates");
	}

	/**
	 * Handle apply filters.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleApplyFilters(ActionEvent event) {
		this.statusLabel.setText("");

		List<StockChangeEntry> entries = new ArrayList<>(AddStockChangesPageCodeBehind.CHANGE_LOG);

		boolean filterFlammable = this.flammableFilterCheckBox.isSelected();
		boolean filterPerishable = this.perishableFilterCheckBox.isSelected();
		boolean filterLiquid = this.liquidFilterCheckBox.isSelected();

		if (filterFlammable || filterPerishable || filterLiquid) {
			entries = entries.stream().filter(entry -> {
				Set<StockAttributes> attrs = entry.getStock().getAttributes();
				boolean ok = true;

				if (filterFlammable) {
					ok = ok && attrs.contains(StockAttributes.FLAMMABLE);
				}
				if (filterPerishable) {
					ok = ok && attrs.contains(StockAttributes.PERISHABLE);
				}
				if (filterLiquid) {
					ok = ok && attrs.contains(StockAttributes.LIQUID);
				}
				return ok;
			}).collect(Collectors.toList());
		}

		String selectedCrew = this.crewMateFilterComboBox.getValue();
		if (selectedCrew != null && !selectedCrew.isBlank()) {
			entries = entries.stream().filter(entry -> selectedCrew.equals(entry.getAddedBy()))
					.collect(Collectors.toList());
		}

		LocalDate startDate = this.startDatePicker.getValue();
		LocalDate endDate = this.endDatePicker.getValue();

		if (startDate != null || endDate != null) {

			if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
				this.statusLabel.setText("End date must be AFTER start date.");
				return;
			}
			entries = entries.stream().filter(entry -> {
				LocalDate entryDate = entry.getTimestamp().toLocalDate();

				if (startDate != null && entryDate.isBefore(startDate)) {
					return false;
				}
				if (endDate != null && entryDate.isAfter(endDate)) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());
		}

		entries.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());

		this.stockTableView.setItems(FXCollections.observableArrayList(entries));
		this.statusLabel.setText("Filters applied. Showing " + entries.size() + " change(s).");
	}

	/**
	 * Handle clear filters.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleClearFilters(ActionEvent event) {
		this.flammableFilterCheckBox.setSelected(false);
		this.perishableFilterCheckBox.setSelected(false);
		this.liquidFilterCheckBox.setSelected(false);
		this.crewMateFilterComboBox.setValue(null);
		this.startDatePicker.setValue(null);
		this.endDatePicker.setValue(null);
		this.statusLabel.setText("");

		this.refreshTableFromLog();
	}

	/**
	 * Handle refresh.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleRefresh(ActionEvent event) {
		this.populateCrewMateFilterOptions();
		this.refreshTableFromLog();
		this.statusLabel.setText("Refreshed from log.");
	}

	/**
	 * Back to landing page.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	private void backToLandingPage(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass()
				.getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml"));

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