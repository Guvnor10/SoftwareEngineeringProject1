package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/** Class
 * @author gn00021
 * Represents one type of stock item in the ship's inventory.
 * @version Fall2025
 */
public class Stock {

	/** The name. */
	private String name;
	
	/** The quantity. */
	private int quantity;
	
	/** The size. */
	private double size;
	
	/** The attributes. */
	private Set<StockAttributes> attributes;
	
	/** The condition. */
	private String condition;
	
	/** The expiration date. */
	private LocalDate expirationDate;

	/**
	 * Creates a new Stock item.
	 *
	 * @param name           the name of the item
	 * @param quantity       the quantity of the item (must be > 0)
	 * @param size           the size/volume of the item (must be >= 0)
	 * @param attributes     the special attributes (may be empty but not null)
	 * @param condition      the condition/description (may be empty but not null)
	 * @param expirationDate the expiration date (may be null if not perishable)
	 */
	public Stock(String name, int quantity, double size, Set<StockAttributes> attributes, String condition,
			LocalDate expirationDate) {

		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or empty.");
		}
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero.");
		}
		if (size < 0) {
			throw new IllegalArgumentException("Size cannot be negative.");
		}
		if (attributes == null) {
			throw new IllegalArgumentException("Attributes set cannot be null.");
		}
		if (condition == null) {
			throw new IllegalArgumentException("Condition cannot be null.");
		}

		this.name = name;
		this.quantity = quantity;
		this.size = size;
		this.attributes = EnumSet.copyOf(attributes);
		this.condition = condition;
		this.expirationDate = expirationDate;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public double getSize() {
		return this.size;
	}

	/**
	 * Returns an unmodifiable view of the attributes.
	 *
	 * @return the attributes
	 */
	public Set<StockAttributes> getAttributes() {
		return Collections.unmodifiableSet(this.attributes);
	}

	/**
	 * Gets the condition.
	 *
	 * @return the condition
	 */
	public String getCondition() {
		return this.condition;
	}

	/**
	 * Gets the expiration date.
	 *
	 * @return the expiration date
	 */
	public LocalDate getExpirationDate() {
		return this.expirationDate;
	}

	/**
	 * Checks if is flammable.
	 *
	 * @return true, if is flammable
	 */
	public boolean isFlammable() {
		return this.attributes.contains(StockAttributes.FLAMMABLE);
	}

	/**
	 * Checks if is perishable.
	 *
	 * @return true, if is perishable
	 */
	public boolean isPerishable() {
		return this.attributes.contains(StockAttributes.PERISHABLE);
	}
	
	/**
	 * Checks if is liquid.
	 *
	 * @return true, if is liquid
	 */
	public boolean isLiquid() {
		return this.attributes.contains(StockAttributes.LIQUID);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Stock{name='" + this.name + '\'' + ", quantity=" + this.quantity + ", size=" + this.size
				+ ", attributes=" + this.attributes + ", condition='" + this.condition + '\'' + ", expirationDate="
				+ this.expirationDate + '}';
	}

	/**
	 * Equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Stock)) {
			return false;
		}
		Stock that = (Stock) other;
		return Objects.equals(this.name, that.name) && this.quantity == that.quantity
				&& Double.compare(this.size, that.size) == 0 && Objects.equals(this.attributes, that.attributes)
				&& Objects.equals(this.condition, that.condition)
				&& Objects.equals(this.expirationDate, that.expirationDate);
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.quantity, this.size, this.attributes, this.condition, this.expirationDate);
	}
}
