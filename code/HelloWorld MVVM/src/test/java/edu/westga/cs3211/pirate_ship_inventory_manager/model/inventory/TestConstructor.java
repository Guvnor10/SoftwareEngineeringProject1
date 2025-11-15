package edu.westga.cs3211.pirate_ship_inventory_manager.model.inventory;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestInventory {

	private Stock createSampleStock() {
		return new Stock("Rope", 5, 1.5, EnumSet.noneOf(StockAttributes.class), "Good condition",
				LocalDate.of(2025, 1, 1));
	}

	@Test
	void testNewInventoryIsEmpty() {
		Inventory inventory = new Inventory();

		assertTrue(inventory.hasFreeSpace(), "New inventory should be empty");
		assertEquals(0, inventory.getAllStock().size(), "New inventory should have 0 items");
	}

	@Test
	void testAddStockAddsItem() {
		Inventory inventory = new Inventory();
		Stock stock = createSampleStock();

		inventory.addStock(stock);

		List<Stock> items = inventory.getAllStock();
		assertFalse(inventory.hasFreeSpace(), "Inventory should not be empty after adding stock");
		assertEquals(1, items.size(), "Inventory should contain 1 item");
		assertEquals(stock, items.get(0), "The added stock should be stored in the inventory");
	}

	@Test
	void testAddNullStockThrowsException() {
		Inventory inventory = new Inventory();

		assertThrows(IllegalArgumentException.class, () -> {
			inventory.addStock(null);
		}, "Adding null stock should throw IllegalArgumentException");
	}

	@Test
	void testGetAllStockIsUnmodifiable() {
		Inventory inventory = new Inventory();
		Stock stock = createSampleStock();
		inventory.addStock(stock);

		List<Stock> items = inventory.getAllStock();

		assertThrows(UnsupportedOperationException.class, () -> {
			items.add(createSampleStock());
		}, "Modifying the returned stock list should throw UnsupportedOperationException");
	}

	@Test
	void testIsEmptyAfterAddingAndNotEmpty() {
		Inventory inventory = new Inventory();

		assertTrue(inventory.hasFreeSpace(), "Inventory should start empty");

		inventory.addStock(createSampleStock());

		assertFalse(inventory.hasFreeSpace(), "Inventory should not be empty after adding an item");
	}
}
