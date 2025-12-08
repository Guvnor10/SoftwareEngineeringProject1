
package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.InputBundle;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockCondition;
import edu.westga.cs3211.pirate_ship_inventory_manager.persistance.StockChangesPersistance;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Validates input, finds compatible compartments, adds stock, persists
 * the change to CSV, and updates UI feedback. Functionality unchanged;
 * refactored to satisfy Checkstyle.
 * 
 * @author guv
 * @version Fall 2025
 */
public class AddStockChangesPageCodeBehind {

    public static final List<StockChangeEntry> CHANGE_LOG = new ArrayList<>();

    private static final Inventory INVENTORY = Inventory.getSharedInventory();

    @FXML private TextField nameTextField;
    @FXML private TextField quantityTextField;
    @FXML private TextField sizeTextField;

    @FXML private CheckBox flammableCheckBox;
    @FXML private CheckBox perishableCheckBox;
    @FXML private CheckBox liquidCheckBox;

    @FXML private CheckBox perfectConditionCheckBox;
    @FXML private CheckBox usableConditionCheckBox;
    @FXML private CheckBox unusableConditionCheckBox;

    @FXML private TextArea conditionTextArea;
    @FXML private DatePicker expirationDatePicker;

    @FXML private ComboBox<Compartment> compartmentComboBox;

    @FXML private Label statusLabel;

    @FXML private Button addStockButton;
    @FXML private Button logoutButton;
    @FXML private Button homeButton;

    /**
     * Populate compartments and clear status.
     */
    @FXML
    public void initialize() {
        this.compartmentComboBox.setItems(FXCollections.observableArrayList(INVENTORY.getCompartments()));
        this.compartmentComboBox.setPromptText("Select compartment");
        this.statusLabel.setText("");
    }

    /**
     * Orchestrates add-stock flow using helpers (≤60 lines).
     *
     * @param event click event
     */
    @FXML
    private void handleAddStock(ActionEvent event) {
        this.statusLabel.setText("");

        InputBundle inputs = this.collectInputs();
        boolean inputsAreValid = this.validateCoreInputs(inputs);
        if (!inputsAreValid) {
            return;
        }

        String conditionText = this.buildConditionText(inputs);
        EnumSet<StockAttributes> attributeSet = this.buildAttributes();

        boolean perishableNeedsDate = attributeSet.contains(StockAttributes.PERISHABLE) && inputs.getExpirationDate() == null;
        if (perishableNeedsDate) {
            this.statusLabel.setText("Perishable items MUST include an expiration date.");
            return;
        }

        Stock stockItem = this.createStockOrShowError(inputs, conditionText, attributeSet);
        if (stockItem == null) {
            return;
        }

        List<Compartment> compatibleCompartments = this.findCompatibleCompartments(stockItem, attributeSet);
        if (compatibleCompartments == null) {
            return;
        }

        Compartment selectedCompartment = this.requireCompartmentSelection(compatibleCompartments);
        if (selectedCompartment == null) {
            return;
        }

        this.addAndPersist(stockItem, selectedCompartment);
        this.resetForm();
    }

    /**
     * Navigate to landing page (role-aware).
     *
     * @param event the event
     * @throws IOException if FXML load fails
     */
    @FXML
    private void backToLandingPage(ActionEvent event) throws IOException {
        boolean roleMissing = LoginViewModel.getLoggedInRole() == null;
        String targetFxml;
        if (roleMissing) {
            targetFxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml";
        } else {
            switch (LoginViewModel.getLoggedInRole()) {
                case CREWMATE:
                    targetFxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml";
                    break;
                case QUARTERMASTER:
                    targetFxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml";
                    break;
                default:
                    targetFxml = "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml";
                    break;
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource(targetFxml));
        Stage stage = (Stage) this.homeButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Navigate back to login page.
     *
     * @param event the event
     * @throws IOException if FXML load fails
     */
    @FXML
    private void backToLoginPage(ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(
            getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));
        Stage stage = (Stage) this.logoutButton.getScene().getWindow();
        stage.setScene(new Scene(loginRoot));
        stage.show();
    }

    // ---------------------------------------------------------------------
    // Helpers: input & validation
    // ---------------------------------------------------------------------

    /**
     * Collect raw inputs and selected condition into an {@link InputBundle}.
     *
     * @return populated input bundle (never {@code null})
     */
    private InputBundle collectInputs() {
        InputBundle bundle = new InputBundle();
        bundle.setItemName(this.nameTextField.getText());
        bundle.setQuantityText(this.quantityTextField.getText());
        bundle.setSizeText(this.sizeTextField.getText());
        bundle.setConditionNotes(this.conditionTextArea.getText());
        bundle.setExpirationDate(this.expirationDatePicker.getValue());
        bundle.setSelectedCondition(this.determineConditionFromCheckBoxes());
        return bundle;
    }

    /**
     * Validate item name, quantity, size, and condition selection.
     * Populates parsed quantity/size in the bundle.
     *
     * @param inputs collected inputs
     * @return {@code true} if valid; {@code false} otherwise
     */
    private boolean validateCoreInputs(InputBundle inputs) {
        boolean itemNameMissing = inputs.getItemName() == null || inputs.getItemName().isBlank();
        if (itemNameMissing) {
            this.statusLabel.setText("Item name is required.");
            return false;
        }

        try {
            inputs.setQuantityValue(Integer.parseInt(inputs.getQuantityText()));
        } catch (NumberFormatException parseError) {
            this.statusLabel.setText("Quantity must be a whole number.");
            return false;
        }

        boolean sizeMissing = inputs.getSizeText() == null || inputs.getSizeText().isBlank();
        if (sizeMissing) {
            inputs.setSizeValue(0.0);
        } else {
            try {
                inputs.setSizeValue(Double.parseDouble(inputs.getSizeText()));
            } catch (NumberFormatException parseError) {
                this.statusLabel.setText("Size must be a numeric value.");
                return false;
            }
        }

        boolean invalidConditionSelection = inputs.getSelectedCondition() == null;
        if (invalidConditionSelection) {
            this.statusLabel.setText("Select EXACTLY ONE condition: Perfect, Usable, or Unusable.");
            return false;
        }

        return true;
    }

    /**
     * Build final condition text from enum + optional notes.
     *
     * @param inputs collected inputs
     * @return non-null condition string
     */
    private String buildConditionText(InputBundle inputs) {
        String conditionText = inputs.getSelectedCondition().name();
        boolean hasNotes = inputs.getConditionNotes() != null && !inputs.getConditionNotes().isBlank();
        if (hasNotes) {
            conditionText = conditionText + " - " + inputs.getConditionNotes().trim();
        }
        return conditionText;
    }

    /**
     * Build attributes from checkbox selections.
     *
     * @return attribute set (never {@code null})
     */
    private EnumSet<StockAttributes> buildAttributes() {
        EnumSet<StockAttributes> attributeSet = EnumSet.noneOf(StockAttributes.class);

        boolean flammableSelected = this.flammableCheckBox.isSelected();
        if (flammableSelected) {
            attributeSet.add(StockAttributes.FLAMMABLE);
        }

        boolean perishableSelected = this.perishableCheckBox.isSelected();
        if (perishableSelected) {
            attributeSet.add(StockAttributes.PERISHABLE);
        }

        boolean liquidSelected = this.liquidCheckBox.isSelected();
        if (liquidSelected) {
            attributeSet.add(StockAttributes.LIQUID);
        }

        return attributeSet;
    }

    /**
     * Create {@link Stock} and show validation message on failure.
     *
     * @param inputs collected inputs
     * @param conditionText final condition text
     * @param attributes attribute set
     * @return created stock, or {@code null} if invalid
     */
    private Stock createStockOrShowError(InputBundle inputs, String conditionText, EnumSet<StockAttributes> attributes) {
        try {
            return new Stock(
                inputs.getItemName(),
                inputs.getQuantityValue(),
                inputs.getSizeValue(),
                attributes,
                conditionText,
                inputs.getExpirationDate()
            );
        } catch (IllegalArgumentException invalidStock) {
            this.statusLabel.setText(invalidStock.getMessage());
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // Helpers: compartment selection
    // ---------------------------------------------------------------------

    /**
     * Find compartments that support qualities and have capacity; update UI for failure cases.
     *
     * @param stockItem the stock to store
     * @param attributes selected attributes
     * @return compatible compartments, or {@code null} if none are suitable
     */
    private List<Compartment> findCompatibleCompartments(Stock stockItem, EnumSet<StockAttributes> attributes) {
        List<Compartment> allCompartments = INVENTORY.getCompartments();

        List<Compartment> qualityMatches = new ArrayList<>();
        for (Compartment compartment : allCompartments) {
            boolean qualitiesSupported = compartment.getSpecialQualities().containsAll(stockItem.getAttributes());
            if (qualitiesSupported) {
                qualityMatches.add(compartment);
            }
        }

        boolean hasAttributes = !attributes.isEmpty();
        boolean noQualityMatches = qualityMatches.isEmpty();
        if (hasAttributes && noQualityMatches) {
            this.statusLabel.setText("No compartment supports qualities: " + attributes);
            return null;
        }

        List<Compartment> baseCandidates;
        if (noQualityMatches) {
            baseCandidates = allCompartments;
        } else {
            baseCandidates = qualityMatches;
        }

        List<Compartment> compatibleCompartments = new ArrayList<>();
        for (Compartment compartment : baseCandidates) {
            boolean canStoreStock = compartment.canStore(stockItem);
            if (canStoreStock) {
                compatibleCompartments.add(compartment);
            }
        }

        boolean noneCompatible = compatibleCompartments.isEmpty();
        if (noneCompatible) {
            List<Compartment> hasSomeSpaceList = new ArrayList<>();
            for (Compartment compartment : baseCandidates) {
                boolean hasAnySpace = compartment.getFreeSpace() > 0;
                if (hasAnySpace) {
                    hasSomeSpaceList.add(compartment);
                }
            }

            boolean noSpaceAnywhere = hasSomeSpaceList.isEmpty();
            if (noSpaceAnywhere) {
                this.statusLabel.setText("No compartment has ANY capacity for this item.");
            } else {
                this.compartmentComboBox.setItems(FXCollections.observableArrayList(hasSomeSpaceList));
                this.compartmentComboBox.setPromptText("Reduce quantity; select a compartment.");
                this.statusLabel.setText("Quantity too large. Reduce quantity to fit available space.");
            }
            return null;
        }

        this.compartmentComboBox.setItems(FXCollections.observableArrayList(compatibleCompartments));
        return compatibleCompartments;
    }

    /**
     * Require a valid selection from the compatible list.
     *
     * @param compatibleCompartments compatible options
     * @return the selected compartment, or {@code null} if not chosen/invalid
     */
    private Compartment requireCompartmentSelection(List<Compartment> compatibleCompartments) {
        Compartment selectedCompartment = this.compartmentComboBox.getValue();
        boolean noSelection = selectedCompartment == null;
        boolean notCompatible = !noSelection && !compatibleCompartments.contains(selectedCompartment);
        if (noSelection || notCompatible) {
            this.statusLabel.setText("Please select a compartment from the list before adding stock.");
            this.compartmentComboBox.requestFocus();
            return null;
        }
        return selectedCompartment;
    }

    // ---------------------------------------------------------------------
    // Helpers: persistence & feedback
    // ---------------------------------------------------------------------

    /**
     * Add stock, create entry, persist to CSV (unchanged call), and show user feedback.
     *
     * @param stockItem the created stock
     * @param selectedCompartment the chosen compartment
     */
    private void addAndPersist(Stock stockItem, Compartment selectedCompartment) {
        selectedCompartment.addStock(stockItem);

        String loggedInCrewmate = LoginViewModel.getLoggedInUser();
        boolean userNameMissing = loggedInCrewmate == null || loggedInCrewmate.isBlank();
        if (userNameMissing) {
            loggedInCrewmate = "Crewmate";
        }

        LocalDateTime eventTimestamp = LocalDateTime.now();
        StockChangeEntry changeEntry = new StockChangeEntry(stockItem, loggedInCrewmate, eventTimestamp, selectedCompartment);
        CHANGE_LOG.add(changeEntry);

        // **File saving unchanged**
        try {
            new StockChangesPersistance().append(changeEntry);
        } catch (IOException ioException) {
            String failureMessage = "Stock added, but failed to save to CSV: " + ioException.getMessage();
            this.statusLabel.setText(failureMessage);
        }

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Stock Added Successfully!\n\n");
        messageBuilder.append("Item: ").append(stockItem.getName()).append("\n");
        messageBuilder.append("Qty: ").append(stockItem.getQuantity()).append("\n");
        messageBuilder.append("Condition: ").append(stockItem.getCondition()).append("\n");
        messageBuilder.append("Attributes: ").append(stockItem.getAttributes()).append("\n");
        messageBuilder.append("Compartment: ").append(selectedCompartment.toString()).append("\n");
        messageBuilder.append("Remaining Capacity: ").append(selectedCompartment.getFreeSpace()).append("\n");
        messageBuilder.append("Added By: ").append(loggedInCrewmate).append("\n");
        messageBuilder.append("Time: ").append(eventTimestamp);

        this.statusLabel.setText(messageBuilder.toString());
    }

    /**
     * Reset UI controls to defaults after success.
     */
    private void resetForm() {
        this.nameTextField.clear();
        this.quantityTextField.clear();
        this.sizeTextField.clear();
        this.conditionTextArea.clear();
        this.expirationDatePicker.setValue(null);

        this.flammableCheckBox.setSelected(false);
        this.perishableCheckBox.setSelected(false);
        this.liquidCheckBox.setSelected(false);

        this.perfectConditionCheckBox.setSelected(false);
        this.usableConditionCheckBox.setSelected(false);
        this.unusableConditionCheckBox.setSelected(false);

        this.compartmentComboBox.setValue(null);
        this.compartmentComboBox.setItems(FXCollections.observableArrayList(INVENTORY.getCompartments()));
        this.compartmentComboBox.setPromptText("Select compartment");
    }

    /**
     * Determine selected condition; {@code null} unless exactly one is chosen.
     *
     * @return the selected {@link StockCondition}, or {@code null} if invalid selection
     */
    private StockCondition determineConditionFromCheckBoxes() {
        boolean perfectSelected = this.perfectConditionCheckBox.isSelected();
        boolean usableSelected = this.usableConditionCheckBox.isSelected();
        boolean unusableSelected = this.unusableConditionCheckBox.isSelected();

        int selectionCount = 0;
        if (perfectSelected) {
            selectionCount = selectionCount + 1;
        }
        if (usableSelected) {
            selectionCount = selectionCount + 1;
        }
        if (unusableSelected) {
            selectionCount = selectionCount + 1;
        }

        boolean isExclusiveSelection = selectionCount == 1;
        if (!isExclusiveSelection) {
            return null;
        }

        if (perfectSelected) {
            return StockCondition.PERFECT;
        }
        if (usableSelected) {
            return StockCondition.USABLE;
        }
        return StockCondition.UNUSABLE;
    }
}