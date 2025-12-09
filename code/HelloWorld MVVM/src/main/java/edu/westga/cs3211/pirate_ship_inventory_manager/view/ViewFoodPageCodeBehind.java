package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
import edu.westga.cs3211.pirate_ship_inventory_manager.persistance.StockChangesPersistance;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;

/**
 * ViewFoodPageCodeBehind
 *
 * Shows stock change entries from StockChanges.csv for the Cook, supports
 * filtering, and lets the cook "get food" (remove an entry).
 *
 * @author gn00021
 * @version Fall 2025
 */
public class ViewFoodPageCodeBehind {

	private final StockChangesPersistance stockChangesPersister = new StockChangesPersistance();

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private TableColumn<StockChangeEntry, String> attributesColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> compartmentColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> conditionColumn;
	@FXML
	private TableColumn<StockChangeEntry, String> itemNameColumn;

	@FXML
	private TableColumn<StockChangeEntry, Integer> quantityColumn;
	@FXML
	private TableColumn<StockChangeEntry, Double> sizeColumn;

	@FXML
	private TableColumn<StockChangeEntry, LocalDate> expirationDateColumn;
	@FXML
	private TableColumn<StockChangeEntry, LocalDateTime> timeAddedColumn;

	@FXML
	private TableView<StockChangeEntry> stockTableView;

	@FXML
	private DatePicker startDatePicker;
	@FXML
	private DatePicker endDatePicker;

	@FXML
	private CheckBox flammableFilterCheckBox;
	@FXML
	private CheckBox perishableFilterCheckBox;
	@FXML
	private CheckBox liquidFilterCheckBox;

	@FXML
	private Button homeButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button refreshButton;
	@FXML
	private Button clearFiltersButton;
	@FXML
	private Button applyFiltersButton;
	@FXML
	private Button getFoodButton;

	@FXML
	private Label statusLabel;

	@FXML
	void backToLandingPage(ActionEvent event) {
		// Cook goes back to Cook landing page
		this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CookLandingPage.fxml");
	}

	@FXML
	void backToLoginPage(ActionEvent event) {
		this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml");
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

	@FXML
	void initialize() {
		this.configureTableColumns();
		this.refreshTableFromCsv();
		if (this.statusLabel != null) {
			this.statusLabel.setText("");
		}

		// FXML sanity checks
		assert this.attributesColumn != null : "fx:id=\"attributesColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.compartmentColumn != null : "fx:id=\"compartmentColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.conditionColumn != null : "fx:id=\"conditionColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.itemNameColumn != null : "fx:id=\"itemNameColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.quantityColumn != null : "fx:id=\"quantityColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.sizeColumn != null : "fx:id=\"sizeColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.expirationDateColumn != null
				: "fx:id=\"expirationDateColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.timeAddedColumn != null : "fx:id=\"timeAddedColumn\" not injected: check ViewFoodPage.fxml.";
		assert this.stockTableView != null : "fx:id=\"stockTableView\" not injected: check ViewFoodPage.fxml.";
	}

	private void configureTableColumns() {
		// Each row is a StockChangeEntry; columns pull fields from the Stock inside it.

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

		this.compartmentColumn
				.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCompartment().toString()));

		this.timeAddedColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getTimestamp()));
	}

	@FXML
	void handleRefresh(ActionEvent event) {
		this.refreshTableFromCsv();
		this.statusLabel.setText("Refreshed from CSV.");
	}

	private void refreshTableFromCsv() {
		try {
			List<StockChangeEntry> entries = this.stockChangesPersister.loadAll();
			entries.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());
			this.stockTableView.setItems(FXCollections.observableArrayList(entries));
			this.statusLabel.setText("Loaded " + entries.size() + " change(s) from StockChanges.csv.");
		} catch (IOException io) {
			this.stockTableView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
			this.statusLabel.setText("Failed to load StockChanges.csv: " + io.getMessage());
		}
	}

	@FXML
	void handleApplyFilters(ActionEvent event) {
		this.statusLabel.setText("");

		List<StockChangeEntry> source;
		try {
			source = this.stockChangesPersister.loadAll();
		} catch (IOException io) {
			source = new ArrayList<>();
		}

		boolean flammable = this.flammableFilterCheckBox.isSelected();
		boolean perishable = this.perishableFilterCheckBox.isSelected();
		boolean liquid = this.liquidFilterCheckBox.isSelected();

		if (flammable || perishable || liquid) {
			source = source.stream().filter(e -> {
				Set<StockAttributes> attrs = e.getStock().getAttributes();
				boolean okFlam = !flammable || attrs.contains(StockAttributes.FLAMMABLE);
				boolean okPeri = !perishable || attrs.contains(StockAttributes.PERISHABLE);
				boolean okLiq = !liquid || attrs.contains(StockAttributes.LIQUID);
				return okFlam && okPeri && okLiq;
			}).collect(Collectors.toList());
		}

		LocalDate start = this.startDatePicker.getValue();
		LocalDate end = this.endDatePicker.getValue();

		if (start != null && end != null && !end.isAfter(start)) {
			this.statusLabel.setText("End date must be AFTER start date.");
			return;
		}

		if (start != null || end != null) {
			final LocalDate s = start;
			final LocalDate e = end;
			source = source.stream().filter(entry -> {
				LocalDate date = entry.getTimestamp().toLocalDate();
				if (s != null && date.isBefore(s)) {
					return false;
				}
				if (e != null && date.isAfter(e)) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());
		}

		source.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());
		this.stockTableView.setItems(FXCollections.observableArrayList(source));
		this.statusLabel.setText("Filters applied. Showing " + source.size() + " change(s).");
	}

	@FXML
	void handleClearFilters(ActionEvent event) {
		this.flammableFilterCheckBox.setSelected(false);
		this.perishableFilterCheckBox.setSelected(false);
		this.liquidFilterCheckBox.setSelected(false);
		this.startDatePicker.setValue(null);
		this.endDatePicker.setValue(null);
		this.refreshTableFromCsv();
		this.statusLabel.setText("Filters cleared.");
	}

	@FXML
	void handleGetFood(ActionEvent event) {
		StockChangeEntry selected = this.stockTableView.getSelectionModel().getSelectedItem();

		if (selected == null) {
			this.statusLabel.setText("Select a row first.");
			return;
		}

		// Optimistically remove from UI
		this.stockTableView.getItems().remove(selected);

		try {
			List<StockChangeEntry> all = this.stockChangesPersister.loadAll();
			List<StockChangeEntry> remaining = all.stream().filter(entry -> !this.isSameEntry(entry, selected))
					.collect(Collectors.toList());

			this.stockChangesPersister.overwriteAll(remaining);

			this.statusLabel.setText("Removed & saved: " + selected.getStock().getName() + " (qty "
					+ selected.getStock().getQuantity() + ")");
		} catch (IOException io) {
			this.statusLabel.setText("Failed to update CSV: " + io.getMessage());
			this.refreshTableFromCsv();
		}
	}

	/**
	 * Checks if is same entry.
	 *
	 * @param aTwo b
	 * Determines whether two StockChangeEntry instances represent the same record
	 * in the CSV (matches on timestamp, stock fields, and compartment properties).
	 * @param bOne the b
	 * @return true, if is same entry
	 */
	private boolean isSameEntry(StockChangeEntry aTwo, StockChangeEntry bOne) {
		if (aTwo == bOne) {
			return true;
		}
		if (aTwo == null || bOne == null) {
			return false;
		}

		if (!java.util.Objects.equals(aTwo.getTimestamp(), bOne.getTimestamp())) {
			return false;
		}

		Stock sa = aTwo.getStock();
		Stock sb = bOne.getStock();
		if (sa == null || sb == null) {
			return false;
		}

		boolean stockMatches = java.util.Objects.equals(sa.getName(), sb.getName())
				&& sa.getQuantity() == sb.getQuantity() && Double.compare(sa.getSize(), sb.getSize()) == 0
				&& java.util.Objects.equals(sa.getCondition(), sb.getCondition())
				&& java.util.Objects.equals(sa.getExpirationDate(), sb.getExpirationDate())
				&& java.util.Objects.equals(sa.getAttributes(), sb.getAttributes());

		if (!stockMatches) {
			return false;
		}

		boolean compartmentMatches = aTwo.getCompartment().getCapacity() == bOne.getCompartment().getCapacity()
				&& java.util.Objects.equals(aTwo.getCompartment().getSpecialQualities(),
						bOne.getCompartment().getSpecialQualities());

		return compartmentMatches;
	}
}
