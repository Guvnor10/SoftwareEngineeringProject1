package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/** Class
 * @author gn00021
 * Represents a storage compartment on the ship that can hold stock items.
 * Each compartment has a capacity (in quantity units) and may only
 * store stock that matches its special qualities (StockAttributes).
 * @version Fall 2025r
 */
public class Compartment {

    private final List<Stock> stockItems;
    private final EnumSet<StockAttributes> specialQualities;
    private final int capacity;  
    /**
     * Creates a new compartment.
     *
     * @param capacity        the maximum quantity units this compartment can hold (must be > 0)
     * @param specialQualities the allowed special qualities for stock in this compartment
     */
    
    public Compartment(int capacity, Set<StockAttributes> specialQualities) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (specialQualities == null) {
            throw new IllegalArgumentException("Special qualities set cannot be null.");
        }

        this.capacity = capacity;
        this.specialQualities = EnumSet.copyOf(specialQualities);
        this.stockItems = new ArrayList<>();
    }

    /**
     * Gets all stock items stored in this compartment.
     *
     * @return an unmodifiable list of stock items
     */
    public List<Stock> getStockItems() {
        return Collections.unmodifiableList(this.stockItems);
    }

    /**
     * Gets this compartment's capacity (in quantity units).
     *
     * @return the capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Gets the special qualities (allowed StockAttributes) for this compartment.
     *
     * @return the set of allowed attributes
     */
    public Set<StockAttributes> getSpecialQualities() {
        return Collections.unmodifiableSet(this.specialQualities);
    }

    /**
     * Computes how much free capacity remains in this compartment.
     * This is based on the sum of the quantities of all stock items stored here.
     *
     * @return remaining free capacity (capacity - used quantity)
     */
    public int getFreeSpace() {
        int used = 0;
        for (Stock stock : this.stockItems) {
            used += stock.getQuantity();
        }
        return this.capacity - used;
    }

    /**
     * Determines whether this compartment can store the given stock item,
     * based on both free space and special qualities.
     *
     * @param stock the stock to test
     * @return true if the stock can be stored here; false otherwise
     */
    public boolean canStore(Stock stock) {
        if (stock == null) {
            return false;
        }

        if (!this.specialQualities.containsAll(stock.getAttributes())) {
            return false;
        }

        return stock.getQuantity() <= this.getFreeSpace();
    }

    /**
     * Adds the given stock to this compartment.
     *
     * @param stock the stock to add
     * @throws IllegalArgumentException if the stock cannot be stored here
     */
    public void addStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null.");
        }
        if (!this.canStore(stock)) {
            throw new IllegalArgumentException("Compartment cannot store this stock item.");
        }
        this.stockItems.add(stock);
    }
    
    @Override
    public String toString() {
        return "Cap:" + this.capacity
            + ", Free:" + this.getFreeSpace()
            + ", Qualities:" + this.specialQualities;
    }
}
