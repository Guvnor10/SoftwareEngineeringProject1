package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the ship's inventory of stock items.
 */
public class Inventory {

    private final List<Stock> stockItems;

    /**
     * Creates an empty inventory.
     */
    public Inventory() {
        this.stockItems = new ArrayList<>();
    }

    /**
     * Adds a stock item to the inventory.
     *
     * @param stock the stock to add
     */
    public void addStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null.");
        }
        this.stockItems.add(stock);
    }

    /**
     * Returns all stock items in the inventory.
     *
     * @return an unmodifiable list of stock items
     */
    public List<Stock> getAllStock() {
        return Collections.unmodifiableList(this.stockItems);
    }

    /**
     * Returns true if the inventory currently has no stock items.
     *
     * @return true if empty; false otherwise
     */
    public boolean isEmpty() {
        return this.stockItems.isEmpty();
    }
}
