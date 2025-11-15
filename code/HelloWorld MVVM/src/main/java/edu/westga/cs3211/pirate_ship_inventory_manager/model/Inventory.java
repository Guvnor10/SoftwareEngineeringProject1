package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

// TODO: Auto-generated Javadoc
/** Class
 * @author gn00021
 * Represents the ship's inventory of compartments.
 * @version Fall25
 */
public class Inventory {

	/** The Constant SHARED. */
	private static final Inventory SHARED = new Inventory();

	/** The compartments. */
	private final List<Compartment> compartments;

	/** Constructor
	 * Instantiates a new inventory.
	 */
	public Inventory() {
		this.compartments = new ArrayList<>();

		// Create example compartments (you can modify these)
		this.compartments.add(new Compartment(100, EnumSet.of(StockAttributes.FLAMMABLE)));
		this.compartments.add(new Compartment(200, EnumSet.of(StockAttributes.PERISHABLE)));
		this.compartments.add(new Compartment(300, EnumSet.noneOf(StockAttributes.class)));
	}
	
	/**
	 * Gets the shared inventory.
	 *
	 * @return the shared inventory
	 */
	public static Inventory getSharedInventory() {
		return SHARED;
	}	

	/**
	 * Gets the compartments.
	 *
	 * @return the compartments
	 */
	public List<Compartment> getCompartments() {
		return Collections.unmodifiableList(this.compartments);
	}

	/**
	 * Adds a stock item to the inventory. This method looks for the first
	 * compartment that can store the stock (based on free space and special
	 * qualities) and adds it there.
	 *
	 * @param stock the stock to add
	 * @throws IllegalArgumentException if stock is null
	 * @throws IllegalStateException    if no compartment can store the stock
	 */
	public void addStock(Stock stock) {
		if (stock == null) {
			throw new IllegalArgumentException("Stock cannot be null.");
		}

		for (Compartment compartment : this.compartments) {
			if (compartment.canStore(stock)) {
				compartment.addStock(stock);
				return;
			}
		}

		// If we reach here, nothing could store it
		throw new IllegalStateException("No compartment can store this stock item.");
	}

	/**
	 * Returns a flattened list of all stock items from all compartments. This is
	 * useful for your 'View Stock Changes' page.
	 *
	 * @return an unmodifiable list of all stock items
	 */
	public List<Stock> getAllStock() {
		List<Stock> all = new ArrayList<>();
		for (Compartment compartment : this.compartments) {
			all.addAll(compartment.getStockItems());
		}
		return Collections.unmodifiableList(all);
	}

	/**
	 * Returns true if the inventory currently has any free space across any
	 * compartment.
	 *
	 * @return true if at least one compartment has free space; false otherwise
	 */
	public boolean hasFreeSpace() {
		for (Compartment compartment : this.compartments) {
			if (compartment.getFreeSpace() > 0) {
				return true;
			}
		}
		return false;
	}
}
