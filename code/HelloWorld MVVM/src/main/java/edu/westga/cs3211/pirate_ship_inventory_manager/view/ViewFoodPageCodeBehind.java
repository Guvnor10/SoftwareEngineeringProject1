
package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
=======
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
>>>>>>> b63b5cd1e499732b37afa6d70a372ea310467108
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
<<<<<<< HEAD
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
=======
>>>>>>> b63b5cd1e499732b37afa6d70a372ea310467108
import javafx.stage.Stage;

<<<<<<< HEAD
/**
 * Loads persisted StockChangeEntry rows from CSV and shows item fields,
 * compartment, and timestamp
 * 
 * @author jess
 * @version Fall 2025
=======
/** class
 * The Class ViewFoodPageCodeBehind.
 * @version f25
 * @author gn00021
>>>>>>> featureRemoveStockButton
 */
public class ViewFoodPageCodeBehind {
    private final StockChangesPersistance stockChangesPersister = new StockChangesPersistance();

<<<<<<< HEAD
    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TableColumn<StockChangeEntry, String> attributesColumn;
    @FXML private TableColumn<StockChangeEntry, String> compartmentColumn;
    @FXML private TableColumn<StockChangeEntry, String> conditionColumn;

    @FXML private DatePicker endDatePicker;
    @FXML private TableColumn<StockChangeEntry, LocalDate> expirationDateColumn;

    @FXML private CheckBox flammableFilterCheckBox;
    @FXML private Button homeButton;

    @FXML private TableColumn<StockChangeEntry, String> itemNameColumn;
    @FXML private CheckBox liquidFilterCheckBox;
    @FXML private Button logoutButton;

    @FXML private CheckBox perishableFilterCheckBox;
    @FXML private TableColumn<StockChangeEntry, Integer> quantityColumn;

    @FXML private TableColumn<StockChangeEntry, Double> sizeColumn;
    @FXML private DatePicker startDatePicker;

    @FXML private Label statusLabel;

    @FXML private TableView<StockChangeEntry> stockTableView;

    @FXML private TableColumn<StockChangeEntry, LocalDateTime> timeAddedColumn;

    @FXML private Button refreshButton;
    @FXML private Button clearFiltersButton;
    @FXML private Button applyFiltersButton;

    @FXML private Button getFoodButton;

    @FXML
    void backToLandingPage(ActionEvent event) {
        this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CookLandingPage.fxml");
    }

    @FXML
    void backToLoginPage(ActionEvent event) {
        this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml");
    }

<<<<<<< HEAD
=======
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
>>>>>>> featureRemoveStockButton
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
<<<<<<< HEAD

	private ObservableList<Stock> stockList = FXCollections.observableArrayList();

	@FXML
	void backToLandingPage(ActionEvent event) {
		switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CookLandingPage.fxml");
=======
	
	/**
	 * Back to landing page.
	 *
	 * @param event the event
	 */
	@FXML
	void backToLandingPage(ActionEvent event) {
		this.switchScene(event, "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml");
>>>>>>> featureRemoveStockButton
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
<<<<<<< HEAD

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

=======
	
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
>>>>>>> featureRemoveStockButton
	private void configureTableColumns() {
		this.itemNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
		this.quantityColumn
				.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());
		this.sizeColumn
				.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getSize())));
		this.attributesColumn
				.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAttributes().toString()));
		this.conditionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCondition()));
		this.expirationDateColumn
				.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getExpirationDate()));

		this.compartmentColumn.setCellValueFactory(cell -> {
			StockChangeEntry entry = getStockChangeEntryForStock(cell.getValue());
			return new SimpleStringProperty(entry != null ? entry.getCompartment().toString() : "Unknown");
		});

		this.timeAddedColumn.setCellValueFactory(cell -> {
			StockChangeEntry entry = getStockChangeEntryForStock(cell.getValue());
			return new SimpleObjectProperty<>(entry != null ? entry.getTimestamp() : null);
		});
	}

	private StockChangeEntry getStockChangeEntryForStock(Stock stock) {
		return AddStockChangesPageCodeBehind.CHANGE_LOG.stream().filter(entry -> entry.getStock().equals(stock))
				.findFirst().orElse(null);
	}

	/**
	 * Handle apply filters.
	 *
	 * @param event the event
	 */
	@FXML
	void handleApplyFilters(ActionEvent event) {
		List<Stock> filteredStock = stockList.stream().filter(stock -> filterByAttributes(stock))
				.filter(stock -> filterByDate(stock)).collect(Collectors.toList());

		this.stockTableView.setItems(FXCollections.observableArrayList(filteredStock));
		this.statusLabel.setText("Filters applied.");
	}

	private boolean filterByAttributes(Stock stock) {
		boolean matchesFilters = true;
		if (flammableFilterCheckBox.isSelected()) {
			matchesFilters &= stock.isFlammable();
		}
		if (perishableFilterCheckBox.isSelected()) {
			matchesFilters &= stock.isPerishable();
		}
		if (liquidFilterCheckBox.isSelected()) {
			matchesFilters &= stock.isLiquid();
		}
		return matchesFilters;
	}

	private boolean filterByDate(Stock stock) {
		LocalDate startDate = startDatePicker.getValue();
		LocalDate endDate = endDatePicker.getValue();
		LocalDate expirationDate = stock.getExpirationDate();

		if (startDate != null && expirationDate.isBefore(startDate)) {
			return false;
		}
		if (endDate != null && expirationDate.isAfter(endDate)) {
			return false;
		}
		return true;
	}

	/**
	 * Handle clear filters.
	 *
	 * @param event the event
	 */
	@FXML
	void handleClearFilters(ActionEvent event) {
		this.flammableFilterCheckBox.setSelected(false);
		this.perishableFilterCheckBox.setSelected(false);
		this.liquidFilterCheckBox.setSelected(false);
		this.startDatePicker.setValue(null);
		this.endDatePicker.setValue(null);
		this.statusLabel.setText("");

		this.stockTableView.setItems(stockList);
	}

	/**
	 * Handle refresh.
	 *
	 * @param event the event
	 */
	@FXML
	void handleRefresh(ActionEvent event) {
		this.statusLabel.setText("Refreshed.");
		this.refreshStockList();
	}

	private void refreshStockList() {
		List<Stock> updatedStockList = AddStockChangesPageCodeBehind.CHANGE_LOG.stream().map(StockChangeEntry::getStock)
				.collect(Collectors.toList());

		this.stockList.setAll(updatedStockList);
		this.stockTableView.setItems(stockList);
	}

	/**
	 * Initialize.
	 */
	@FXML
	void initialize() {
<<<<<<< HEAD
		configureTableColumns();
		this.refreshStockList();

		assert attributesColumn != null
=======
		 this.configureTableColumns();
		
		assert this.attributesColumn != null
>>>>>>> featureRemoveStockButton
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
=======
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
        this.statusLabel.setText("");
        assert this.attributesColumn != null : "fx:id=\"attributesColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.compartmentColumn != null : "fx:id=\"compartmentColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.conditionColumn != null : "fx:id=\"conditionColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.expirationDateColumn != null : "fx:id=\"expirationDateColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.flammableFilterCheckBox != null : "fx:id=\"flammableFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.getFoodButton != null : "fx:id=\"getFoodButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.itemNameColumn != null : "fx:id=\"itemNameColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.liquidFilterCheckBox != null : "fx:id=\"liquidFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.perishableFilterCheckBox != null : "fx:id=\"perishableFilterCheckBox\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.quantityColumn != null : "fx:id=\"quantityColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.sizeColumn != null : "fx:id=\"sizeColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.stockTableView != null : "fx:id=\"stockTableView\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
        assert this.timeAddedColumn != null : "fx:id=\"timeAddedColumn\" was not injected: check your FXML file 'ViewFoodPage.fxml'.";
    }

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
>>>>>>> b63b5cd1e499732b37afa6d70a372ea310467108

        this.compartmentColumn.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getCompartment().toString()));

        this.timeAddedColumn.setCellValueFactory(cell ->
            new SimpleObjectProperty<>(cell.getValue().getTimestamp()));
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
            this.statusLabel.setText("Loaded " + entries.size() + " item(s) from StockChanges.csv.");
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
            source = source.stream()
                    .filter(e -> {
                        Set<StockAttributes> attrs = e.getStock().getAttributes();
                        boolean okFlam = !flammable || attrs.contains(StockAttributes.FLAMMABLE);
                        boolean okPeri = !perishable || attrs.contains(StockAttributes.PERISHABLE);
                        boolean okLiq  = !liquid    || attrs.contains(StockAttributes.LIQUID);
                        return okFlam && okPeri && okLiq;
                    })
                    .collect(Collectors.toList());
        }

        LocalDate start = this.startDatePicker.getValue();
        LocalDate end   = this.endDatePicker.getValue();

        if (start != null && end != null && !end.isAfter(start)) {
            this.statusLabel.setText("End date must be AFTER start date.");
            return;
        }

        if (start != null || end != null) {
            final LocalDate s = start;
            final LocalDate e = end;
            source = source.stream()
                    .filter(entry -> {
                        LocalDate date = entry.getTimestamp().toLocalDate();
                        if (s != null && date.isBefore(s)) {
							return false;
						}
                        if (e != null && date.isAfter(e)) {
							return false;
						}
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        source.sort(Comparator.comparing(StockChangeEntry::getTimestamp).reversed());
        this.stockTableView.setItems(FXCollections.observableArrayList(source));
        this.statusLabel.setText("Filters applied. Showing " + source.size() + " item(s).");
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
         this.stockTableView.getItems().remove(selected);
         try {
             List<StockChangeEntry> all = this.stockChangesPersister.loadAll();
             List<StockChangeEntry> remaining = all.stream()
                     .filter(entry -> !this.isSameEntry(entry, selected))
                     .collect(java.util.stream.Collectors.toList());

             this.stockChangesPersister.overwriteAll(remaining);

             this.statusLabel.setText("Removed & saved: "
                     + selected.getStock().getName()
                     + " (qty " + selected.getStock().getQuantity() + ")");
         } catch (IOException io) {
             this.statusLabel.setText("Failed to update CSV: " + io.getMessage());
             this.refreshTableFromCsv();
         }
     }

     /**
      * Compares two StockChangeEntry objects for identity in the CSV context.
      * Matches on timestamp, stock core fields, and compartment characteristics.
      * 
      * @param a1 stock a
      * @param b1 stock b
      * @return matching compartment
      */
     private boolean isSameEntry(StockChangeEntry a1, StockChangeEntry b1) {
         if (a1 == b1) {
             return true;
         }
         if (a1 == null || b1 == null) {
             return false;
         }
         if (!java.util.Objects.equals(a1.getTimestamp(), b1.getTimestamp())) {
             return false;
         }
         Stock sa = a1.getStock();
         Stock sb = b1.getStock();
         if (sa == null || sb == null) {
             return false;
         }
         boolean stockMatches =
                 java.util.Objects.equals(sa.getName(), sb.getName())
                 && sa.getQuantity() == sb.getQuantity()
                 && Double.compare(sa.getSize(), sb.getSize()) == 0
                 && java.util.Objects.equals(sa.getCondition(), sb.getCondition())
                 && java.util.Objects.equals(sa.getExpirationDate(), sb.getExpirationDate())
                 && java.util.Objects.equals(sa.getAttributes(), sb.getAttributes());

         if (!stockMatches) {
             return false;
         }
         boolean compartmentMatches =
                 a1.getCompartment().getCapacity() == b1.getCompartment().getCapacity()
                 && java.util.Objects.equals(
                         a1.getCompartment().getSpecialQualities(),
                         b1.getCompartment().getSpecialQualities()
                    );

         return compartmentMatches;
     }

}