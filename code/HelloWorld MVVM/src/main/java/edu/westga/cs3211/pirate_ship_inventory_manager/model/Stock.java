package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.time.LocalDate;

public class Stock {

	private String name;
	private int size;
	private String condition;
	private LocalDate expirationDate;
	private boolean liquid;
	private boolean flammable;
	
	public Stock(String name, int size, String condition, LocalDate expirationDate, boolean liquid, boolean flammable)
	{
		this.name = name;
		this.size = size;
		this.condition = condition;
		this.expirationDate = expirationDate;
		this.liquid = liquid;
		this.flammable = flammable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isLiquid() {
		return liquid;
	}

	public void setLiquid(boolean liquid) {
		this.liquid = liquid;
	}

	public boolean isFlammable() {
		return flammable;
	}

	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}
	
}
