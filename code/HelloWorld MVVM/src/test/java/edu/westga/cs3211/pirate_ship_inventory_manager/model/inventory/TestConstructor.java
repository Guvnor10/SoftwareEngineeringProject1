package edu.westga.cs3211.pirate_ship_inventory_manager.model.inventory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestInventory {

	@Test
	void constructorCreatesEmptyInventory() {
		Inventory inventory = new Inventory();

		assertTrue(inventory.hasFreeSpace());
		assertTrue(inventory.getAllStock().isEmpty());
	}

	@Test
	void addStockShouldRejectNull() {
		Inventory inventory = new Inventory();

		assertThrows(IllegalArgumentException.class, () -> inventory.addStock(null));
	}

	@Test
	void addStockShouldAddItemToInventory() {
		Inventory inventory = new Inventory();
		Stock s = new Stock("Rope", 5, 1.0, EnumSet.noneOf(StockAttributes.class), "ok", null);

		inventory.addStock(s);

		List<Stock> items = inventory.getAllStock();
		assertEquals(1, items.size());
		assertSame(s, items.get(0));
	}

	

	@Test
	void getAllStockShouldReturnUnmodifiableList() {
		Inventory inventory = new Inventory();
		Stock s = new Stock("Rope", 5, 1.0, EnumSet.noneOf(StockAttributes.class), "ok", null);

		inventory.addStock(s);
		List<Stock> items = inventory.getAllStock();

		assertThrows(UnsupportedOperationException.class, () -> items.add(s));
	}
}
