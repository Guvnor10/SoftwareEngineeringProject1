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
import edu.westga.cs3211.pirate_ship_inventory_manager.persistance.StockChangesPersistance;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * <p><b>View Stock Changes Page - Code Behind</b></p>
 *
 * <p>Loads and displays persisted {@link StockChangeEntry} rows from CSV
 * and provides filtering by attributes, crewmate, and date range.
 * Falls back to the in-memory change log if CSV cannot be read.</p>
 *
 * @author gn00021
 * @version Fall 2025
 */
public class ViewStockChangesPageCodeBehind {

    /** CSV persister instance reused by this page. */
    private final StockChangesPersistance stockChangesPersister = new StockChangesPersistance();

    // ---------------------------------------------------------------------
    // FXML-CONTROLS
    // ---------------------------------------------------------------------

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

    // ---------------------------------------------------------------------
    // INITIALIZATION
    // ---------------------------------------------------------------------

    /**
     * Initializes column cell factories, populates filter options, and loads table data.
     */
    @FXML
    private void initialize() {
        this.configureTableColumns();
        this.populateCrewMateFilterOptions();
        this.refreshTableFromLog();
        this.statusLabel.setText("");
    }

    /**
     * Configures table columns with cell value factories for the {@link TableView}.
     */
    @FXML
    private void configureTableColumns() {
        this.itemNameColumn.setCellValueFactory(cellData -> {
            Stock stock = cellData.getValue().getStock();
            return new SimpleStringProperty(stock.getName());
        });

        this.quantityColumn.setCellValueFactory(cellData -> {
            Stock stock = cellData.getValue().getStock();
            return new SimpleIntegerProperty(stock.getQuantity()).asObject();
        });

        this.sizeColumn.setCellValueFactory(cellData -> {
            Stock stock = cellData.getValue().getStock();
            return new SimpleDoubleProperty(stock.getSize()).asObject();
        });

        this.attributesColumn.setCellValueFactory(cellData -> {
            Set<StockAttributes> attrs = cellData.getValue().getStock().getAttributes();
            return new SimpleStringProperty(attrs.toString());
        });

        this.conditionColumn.setCellValueFactory(cellData -> {
            Stock stock = cellData.getValue().getStock();
            return new SimpleStringProperty(stock.getCondition());
        });

        this.expirationDateColumn.setCellValueFactory(cellData -> {
            Stock stock = cellData.getValue().getStock();
            return new SimpleObjectProperty<>(stock.getExpirationDate());
        });

        this.compartmentColumn.setCellValueFactory(cellData -> {
            String compText = cellData.getValue().getCompartment().toString();
            return new SimpleStringProperty(compText);
        });

        this.addedByColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddedBy()));

        this.timeAddedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTimestamp()));
    }

    // ---------------------------------------------------------------------
    // DATA LOADING
    // ---------------------------------------------------------------------

    /**
     * Loads entries from CSV and populates the table. Falls back to in-memory log on failure.
     */
    @FXML
    private void refreshTableFromLog() {
        try {
            List<StockChangeEntry> persistedEntries = this.stockChangesPersister.loadAll();
            persistedEntries.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());
            this.stockTableView.setItems(FXCollections.observableArrayList(persistedEntries));
            String loadedMessage = "Loaded " + persistedEntries.size() + " change(s) from StockChanges.csv.";
            this.statusLabel.setText(loadedMessage);
        } catch (IOException ioException) {
            List<StockChangeEntry> fallbackEntries = new ArrayList<>(AddStockChangesPageCodeBehind.CHANGE_LOG);
            fallbackEntries.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());
            this.stockTableView.setItems(FXCollections.observableArrayList(fallbackEntries));
            String failureMessage = "Failed to load StockChanges.csv; showing in-memory log. " + ioException.getMessage();
            this.statusLabel.setText(failureMessage);
        }
    }

    /**
     * Populates the CrewMate filter combo box from CSV, falling back to in-memory if needed.
     */
    @FXML
    private void populateCrewMateFilterOptions() {
        try {
            List<StockChangeEntry> persistedEntries = this.stockChangesPersister.loadAll();
            List<String> crewNames = persistedEntries.stream()
                                                     .map(StockChangeEntry::getAddedBy)
                                                     .distinct()
                                                     .sorted()
                                                     .collect(Collectors.toList());
            this.crewMateFilterComboBox.setItems(FXCollections.observableArrayList(crewNames));
            this.crewMateFilterComboBox.setPromptText("All crewmates");
        } catch (IOException ioException) {
            List<String> crewNames = AddStockChangesPageCodeBehind.CHANGE_LOG.stream()
                                                                             .map(StockChangeEntry::getAddedBy)
                                                                             .distinct()
                                                                             .sorted()
                                                                             .collect(Collectors.toList());
            this.crewMateFilterComboBox.setItems(FXCollections.observableArrayList(crewNames));
            String failureMessage = "Failed to read StockChanges.csv; crew list from memory.";
            this.statusLabel.setText(failureMessage);
        }
    }

    // ---------------------------------------------------------------------
    // FILTERING
    // ---------------------------------------------------------------------

    /**
     * Applies filters based on attributes, crewmate selection, and date range.
     *
     * @param event the button or control event
     */
    @FXML
    private void handleApplyFilters(ActionEvent event) {
        this.statusLabel.setText("");

        List<StockChangeEntry> sourceEntries = new ArrayList<>();
        boolean loadedCsvSuccessfully = true;

        try {
            sourceEntries = this.stockChangesPersister.loadAll();
        } catch (IOException ioException) {
            loadedCsvSuccessfully = false;
            sourceEntries = new ArrayList<>(AddStockChangesPageCodeBehind.CHANGE_LOG);
            String failureMessage = "Failed to load StockChanges.csv; filtering in-memory log. " + ioException.getMessage();
            this.statusLabel.setText(failureMessage);
        }

        boolean filterFlammable = this.flammableFilterCheckBox.isSelected();
        boolean filterPerishable = this.perishableFilterCheckBox.isSelected();
        boolean filterLiquid = this.liquidFilterCheckBox.isSelected();

        if (filterFlammable || filterPerishable || filterLiquid) {
            sourceEntries = sourceEntries.stream()
                .filter(entry -> {
                    Set<StockAttributes> attrs = entry.getStock().getAttributes();
                    boolean passesFlammable = !filterFlammable || attrs.contains(StockAttributes.FLAMMABLE);
                    boolean passesPerishable = !filterPerishable || attrs.contains(StockAttributes.PERISHABLE);
                    boolean passesLiquid = !filterLiquid || attrs.contains(StockAttributes.LIQUID);
                    boolean passesAllSelected = passesFlammable && passesPerishable && passesLiquid;
                    return passesAllSelected;
                })
                .collect(Collectors.toList());
        }

        String selectedCrewmate = this.crewMateFilterComboBox.getValue();
        boolean hasCrewmateFilter = selectedCrewmate != null && !selectedCrewmate.isBlank();
        if (hasCrewmateFilter) {
            sourceEntries = sourceEntries.stream()
                .filter(entry -> selectedCrewmate.equals(entry.getAddedBy()))
                .collect(Collectors.toList());
        }

        LocalDate startDate = this.startDatePicker.getValue();
        LocalDate endDate = this.endDatePicker.getValue();

        boolean bothDatesProvided = startDate != null && endDate != null;
        if (bothDatesProvided) {
            boolean endAfterStart = endDate.isAfter(startDate);
            if (!endAfterStart) {
                this.statusLabel.setText("End date must be AFTER start date.");
                return;
            }
        }

        boolean startProvided = startDate != null;
        boolean endProvided = endDate != null;
        if (startProvided || endProvided) {
            sourceEntries = sourceEntries.stream()
                .filter(entry -> {
                    LocalDate entryDate = entry.getTimestamp().toLocalDate();
                    boolean beforeStart = startProvided && entryDate.isBefore(startDate);
                    if (beforeStart) {
                        return false;
                    }
                    boolean afterEnd = endProvided && entryDate.isAfter(endDate);
                    if (afterEnd) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        }

        sourceEntries.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());
        this.stockTableView.setItems(FXCollections.observableArrayList(sourceEntries));

        int displayedCount = sourceEntries.size();
        String completionMessage;
        if (loadedCsvSuccessfully) {
            completionMessage = "Filters applied. Showing " + displayedCount + " change(s) from CSV.";
        } else {
            completionMessage = "Filters applied. Showing " + displayedCount + " change(s) from in-memory log.";
        }
        this.statusLabel.setText(completionMessage);
    }

    /**
     * Clears all filters and reloads table data from CSV (or in-memory on failure).
     *
     * @param event the button event
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
     * Refreshes crew list and table from the persisted CSV.
     *
     * @param event the button event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.populateCrewMateFilterOptions();
        this.refreshTableFromLog();
        this.statusLabel.setText("Refreshed from log.");
    }

    // ---------------------------------------------------------------------
    // NAVIGATION
    // ---------------------------------------------------------------------

    /**
     * Navigates back to the Quartermaster landing page.
     *
     * @param event the button event
     * @throws IOException if the FXML cannot be loaded
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
     * Navigates back to the login page.
     *
     * @param event the button event
     * @throws IOException if the FXML cannot be loaded
     */
    @FXML
    private void backToLoginPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass()
                .getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));
        Stage stage = (Stage) this.logoutButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}