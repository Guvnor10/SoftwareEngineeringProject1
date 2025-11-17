package edu.westga.cs3211.pirate_ship_inventory_manager.model.compartment;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestConstructor {

	private Stock makeStock(String name, int qty, double size, StockAttributes... attrs) {
		return new Stock(name, qty, size, EnumSet.of(attrs.length > 0 ? attrs[0] : StockAttributes.FLAMMABLE), "good",
				LocalDate.now().plusDays(5));
	}

	@Test
	@DisplayName("Constructor should create a valid compartment")
	void testConstructorCreatesCompartment() {
		Compartment comp = new Compartment(100, EnumSet.of(StockAttributes.FLAMMABLE));

		assertNotNull(comp);
		assertEquals(100, comp.getCapacity());
		assertEquals(100, comp.getFreeSpace());
		assertTrue(comp.getSpecialQualities().contains(StockAttributes.FLAMMABLE));
	}

	@Test
	@DisplayName("Constructor should reject negative capacity")
	void testConstructorRejectsNegativeCapacity() {
		assertThrows(IllegalArgumentException.class, () -> new Compartment(-10, EnumSet.of(StockAttributes.FLAMMABLE)));
	}

	@Test
	@DisplayName("Constructor should reject null special qualities set")
	void testConstructorRejectsNullSpecialQualities() {
		assertThrows(IllegalArgumentException.class, () -> new Compartment(50, null));
	}

	@Test
	@DisplayName("canStore returns true when qualities match and space is enough")
	void testCanStoreTrue() {
		Compartment comp = new Compartment(50, EnumSet.of(StockAttributes.FLAMMABLE));
		Stock s = makeStock("Oil", 2, 10.0, StockAttributes.FLAMMABLE);

		assertTrue(comp.canStore(s));
	}

	@Test
	@DisplayName("canStore returns false when qualities do NOT match")
	void testCanStoreFalseDueToQualities() {
		Compartment comp = new Compartment(50, EnumSet.of(StockAttributes.FLAMMABLE));
		Stock s = makeStock("Milk", 2, 10.0, StockAttributes.PERISHABLE);

		assertFalse(comp.canStore(s));
	}

	@Test
	@DisplayName("canStore returns false when capacity is too small")
	void testCanStoreFalseDueToCapacity() {
		Compartment comp = new Compartment(10, EnumSet.noneOf(StockAttributes.class));
		Stock s = makeStock("LargeBox", 1, 20.0);

		assertFalse(comp.canStore(s));
	}

	@Test
	@DisplayName("addStock should reject stock that doesn't fit special qualities")
	void testAddStockRejectsDueToSpecialQualities() {
		Compartment comp = new Compartment(100, EnumSet.of(StockAttributes.FLAMMABLE));
		Stock s = makeStock("Medicine", 1, 5.0, StockAttributes.PERISHABLE);

		assertThrows(IllegalArgumentException.class, () -> comp.addStock(s));
	}

	@Test
	@DisplayName("addStock should reject stock that exceeds space")
	void testAddStockRejectsDueToSize() {
		Compartment comp = new Compartment(10, EnumSet.noneOf(StockAttributes.class));
		Stock s = makeStock("Crate", 1, 20.0);

		assertThrows(IllegalArgumentException.class, () -> comp.addStock(s));
	}

	@Test
	@DisplayName("addStock should reject null")
	void testAddStockRejectsNull() {
		Compartment comp = new Compartment(30, EnumSet.noneOf(StockAttributes.class));

		assertThrows(IllegalArgumentException.class, () -> comp.addStock(null));
	}
}
