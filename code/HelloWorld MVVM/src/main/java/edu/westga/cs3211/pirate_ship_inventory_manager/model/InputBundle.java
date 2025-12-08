
package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.time.LocalDate;

/**
 * Encapsulates user-entered values from the Add Stock page in a
 * type-safe container with private fields and accessors, satisfying
 * Checkstyle rules.
 * 
 * @author Yeni
 * @version Fall 2025
 */
public final class InputBundle {

    private String itemName;
    private String quantityText;
    private String sizeText;
    private String conditionNotes;
    private LocalDate expirationDate;
    private StockCondition selectedCondition;

    private int quantityValue;
    private double sizeValue;

    /**
     * Gets the item name text.
     *
     * @return the item name
     */
    public String getItemName() {
        return this.itemName;
    }

    /**
     * Sets the item name text.
     *
     * @param itemName the item name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Gets the quantity text (raw).
     *
     * @return the quantity text
     */
    public String getQuantityText() {
        return this.quantityText;
    }

    /**
     * Sets the quantity text (raw).
     *
     * @param quantityText the quantity text
     */
    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    /**
     * Gets the size text (raw).
     *
     * @return the size text
     */
    public String getSizeText() {
        return this.sizeText;
    }

    /**
     * Sets the size text (raw).
     *
     * @param sizeText the size text
     */
    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    /**
     * Gets additional condition notes.
     *
     * @return the condition notes
     */
    public String getConditionNotes() {
        return this.conditionNotes;
    }

    /**
     * Sets additional condition notes.
     *
     * @param conditionNotes the condition notes
     */
    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    /**
     * Gets expiration date.
     *
     * @return the expiration date, or {@code null} if none
     */
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    /**
     * Sets expiration date.
     *
     * @param expirationDate the expiration date (nullable)
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the selected stock condition.
     *
     * @return the selected condition, or {@code null} if not set
     */
    public StockCondition getSelectedCondition() {
        return this.selectedCondition;
    }

    /**
     * Sets the selected stock condition.
     *
     * @param selectedCondition the selected condition
     */
    public void setSelectedCondition(StockCondition selectedCondition) {
        this.selectedCondition = selectedCondition;
    }

    /**
     * Gets parsed quantity value.
     *
     * @return the parsed quantity value
     */
    public int getQuantityValue() {
        return this.quantityValue;
    }

    /**
     * Sets parsed quantity value.
     *
     * @param quantityValue the parsed quantity value
     */
    public void setQuantityValue(int quantityValue) {
        this.quantityValue = quantityValue;
    }

    /**
     * Gets parsed size value.
     *
     * @return the parsed size value
     */
    public double getSizeValue() {
        return this.sizeValue;
    }

    /**
     * Sets parsed size value.
     *
     * @param sizeValue the parsed size value
     */
    public void setSizeValue(double sizeValue) {
        this.sizeValue = sizeValue;
    }
}