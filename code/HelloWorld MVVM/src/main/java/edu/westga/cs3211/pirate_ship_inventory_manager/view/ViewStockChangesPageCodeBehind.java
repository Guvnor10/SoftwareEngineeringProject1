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

public class ViewStockChangesPageCodeBehind {

	// ========= UI FIELDS =========

	@FXML
	private Button homeButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button applyFiltersButton;
	@FXML
	private Button clearFiltersButton;
	@FXML
	private Button refreshButton;

	@FXML
	private Label statusLabel;

	// Table + columns
	@FXML
	private TableView<StockChangeEntry> stockTableView;

	@FXML
	private TableColumn<StockChangeEntry, String> itemNameColumn;
	@FXML
	private TableColumn<StockChangeEntry, Integer> quantityColumn;
	@FXML
	private TableColumn<StockChangeEntry, Double> sizeColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> attributesColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> conditionColumn;
	@FXML
	private TableColumn<StockChangeEntry, LocalDate> expirationDateColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> compartmentColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> addedByColumn;
	@FXML
	private TableColumn<StockChangeEntry, LocalDateTime> timeAddedColumn;

	// Filters
	@FXML
	private CheckBox flammableFilterCheckBox;
	@FXML
	private CheckBox perishableFilterCheckBox;
	@FXML
	private CheckBox liquidFilterCheckBox;

	@FXML
	private ComboBox<String> crewMateFilterComboBox;

	@FXML
	private DatePicker startDatePicker;
	@FXML
	private DatePicker endDatePicker;

	// ========= INITIALIZE =========

	@FXML
	private void initialize() {
		this.configureTableColumns();
		this.populateCrewMateFilterOptions();
		this.refreshTableFromLog();
		this.statusLabel.setText("");
	}

	// ========= TABLE SETUP =========
	@FXML
	private void configureTableColumns() {
		// For easier reading
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

	@FXML
	private void refreshTableFromLog() {
		// Copy and sort CHANGE_LOG (most recent first)
		List<StockChangeEntry> sorted = new ArrayList<>(AddStockChangesPageCodeBehind.CHANGE_LOG);
		sorted.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());

		ObservableList<StockChangeEntry> items = FXCollections.observableArrayList(sorted);
		this.stockTableView.setItems(items);
	}

	@FXML
	private void populateCrewMateFilterOptions() {
		// Unique "addedBy" values from the log
		List<String> crewNames = AddStockChangesPageCodeBehind.CHANGE_LOG.stream().map(StockChangeEntry::getAddedBy)
				.distinct().sorted().collect(Collectors.toList());

		this.crewMateFilterComboBox.setItems(FXCollections.observableArrayList(crewNames));
		this.crewMateFilterComboBox.setPromptText("All crewmates");
	}

	// ========= FILTER LOGIC =========

	@FXML
	private void handleApplyFilters(ActionEvent event) {
		this.statusLabel.setText("");

		List<StockChangeEntry> entries = new ArrayList<>(AddStockChangesPageCodeBehind.CHANGE_LOG);

		// --- Filter by Special Qualities (Alternative Flow #1) ---
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

		// --- Filter by Crewmate (Alternative Flow #2) ---
		String selectedCrew = this.crewMateFilterComboBox.getValue();
		if (selectedCrew != null && !selectedCrew.isBlank()) {
			entries = entries.stream().filter(entry -> selectedCrew.equals(entry.getAddedBy()))
					.collect(Collectors.toList());
		}

		// --- Filter by Time Range (Alternative Flows #3 & #4) ---
		LocalDate startDate = this.startDatePicker.getValue();
		LocalDate endDate = this.endDatePicker.getValue();

		if (startDate != null || endDate != null) {

			// Validate time range (Alternative Flow: Invalid Time Range)
			if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
				this.statusLabel.setText("End date must be AFTER start date.");
				return; // keep previous table contents
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

		// --- Apply sort: most recent first (Primary Flow) ---
		entries.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());

		this.stockTableView.setItems(FXCollections.observableArrayList(entries));
		this.statusLabel.setText("Filters applied. Showing " + entries.size() + " change(s).");
	}

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

	@FXML
	private void handleRefresh(ActionEvent event) {
		this.populateCrewMateFilterOptions();
		this.refreshTableFromLog();
		this.statusLabel.setText("Refreshed from log.");
	}

	// ========= NAVIGATION (same style as you requested) =========

	@FXML
	private void backToLandingPage(ActionEvent event) throws IOException {
		// View Stock Changes is quartermaster-only, so we go back to Quartermaster
		// landing.
		Parent root = FXMLLoader.load(getClass()
				.getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml"));

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
