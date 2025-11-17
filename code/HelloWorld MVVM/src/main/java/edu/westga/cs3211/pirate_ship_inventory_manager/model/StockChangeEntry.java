package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.time.LocalDateTime;

/** Class
 * @author gn00021
 * The Class StockChangeEntry.
 * @version Fall 2025
 */
public class StockChangeEntry {

	/** The stock. */
	private final Stock stock;
	
	/** The added by. */
	private final String addedBy;
	
	/** The timestamp. */
	private final LocalDateTime timestamp;
	
	/** The compartment. */
	private final Compartment compartment;

	/**
	 * Instantiates a new stock change entry.
	 *
	 * @param stock the stock
	 * @param addedBy the added by
	 * @param timestamp the timestamp
	 * @param compartment the compartment
	 */
	public StockChangeEntry(Stock stock, String addedBy, LocalDateTime timestamp, Compartment compartment) {
		this.stock = stock;
		this.addedBy = addedBy;
		this.timestamp = timestamp;
		this.compartment = compartment;
		
		if (stock == null) {
            throw new IllegalArgumentException("stock cannot be null");
        }
        if (addedBy == null || addedBy.isBlank()) {
            throw new IllegalArgumentException("addedBy cannot be null/blank");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        if (compartment == null) {
            throw new IllegalArgumentException("compartment cannot be null");
        }
	}

	/**
	 * Gets the stock.
	 *
	 * @return the stock
	 */
	public Stock getStock() {
		return this.stock;
	}

	/**
	 * Gets the added by.
	 *
	 * @return the added by
	 */
	public String getAddedBy() {
		return this.addedBy;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Gets the compartment.
	 *
	 * @return the compartment
	 */
	public Compartment getCompartment() {
		return this.compartment;
	}
}
