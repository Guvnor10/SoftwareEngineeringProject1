package edu.westga.cs3211.pirate_ship_inventory_manager.model.compartment;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestCompartmentFullParameterized {

    private Stock makeStock(String name, int qty, double size, Set<StockAttributes> attrs) {
        return new Stock(
            name,
            qty,
            size,
            attrs,
            "good",
            LocalDate.now().plusDays(3)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = { -10, -1, 0 })
    @DisplayName("Constructor rejects zero or negative capacity")
    void testConstructorInvalidCapacity(int cap) {
        assertThrows(IllegalArgumentException.class,
                () -> new Compartment(cap, EnumSet.of(StockAttributes.FLAMMABLE)));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Constructor rejects null special qualities")
    void testConstructorNullQualities(Set<StockAttributes> qualities) {
        assertThrows(IllegalArgumentException.class,
                () -> new Compartment(10, qualities));
    }

    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("Constructor accepts valid single qualities")
    void testConstructorValid(StockAttributes attr) {
        Compartment comp = new Compartment(50, EnumSet.of(attr));
        assertEquals(50, comp.getCapacity());
        assertTrue(comp.getSpecialQualities().contains(attr));
    }

    @ParameterizedTest
    @CsvSource({
        "10, 5, true",
        "10, 10, true",
        "10, 11, false"
    })
    @DisplayName("canStore handles quantity vs free space correctly")
    void testCanStoreCapacityCases(int capacity, int quantity, boolean expected) {
        Compartment comp = new Compartment(capacity, EnumSet.of(StockAttributes.FLAMMABLE));

        Stock s = makeStock("Barrel", quantity, 1.0, EnumSet.of(StockAttributes.FLAMMABLE));

        assertEquals(expected, comp.canStore(s));
    }

    @ParameterizedTest
    @EnumSource(value = StockAttributes.class, names = { "PERISHABLE", "LIQUID" })
    @DisplayName("canStore returns false when special qualities mismatch")
    void testCanStoreWrongQuality(StockAttributes wrongAttr) {
        Compartment comp = new Compartment(50, EnumSet.of(StockAttributes.FLAMMABLE));

        Stock s = makeStock("Item", 1, 1.0, EnumSet.of(wrongAttr));

        assertFalse(comp.canStore(s));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("canStore(null) returns false")
    void testCanStoreNull(Stock nullStock) {
        Compartment comp = new Compartment(10, EnumSet.of(StockAttributes.FLAMMABLE));
        assertFalse(comp.canStore(nullStock));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("addStock rejects null")
    void testAddStockRejectsNull(Stock nullStock) {
        Compartment comp = new Compartment(30, EnumSet.of(StockAttributes.FLAMMABLE));
        assertThrows(IllegalArgumentException.class, () -> comp.addStock(nullStock));
    }

    @ParameterizedTest
    @EnumSource(value = StockAttributes.class, names = { "PERISHABLE", "LIQUID" })
    @DisplayName("addStock rejects mismatched qualities")
    void testAddStockRejectsBadAttr(StockAttributes wrongAttr) {
        Compartment comp = new Compartment(50, EnumSet.of(StockAttributes.FLAMMABLE));
        Stock s = makeStock("Item", 1, 1.0, EnumSet.of(wrongAttr));

        assertThrows(IllegalArgumentException.class, () -> comp.addStock(s));
    }

    @ParameterizedTest
    @CsvSource({
        "10, 11",
        "5, 6",
        "1, 2"
    })
    @DisplayName("addStock rejects when quantity exceeds free space")
    void testAddStockRejectsOverCapacity(int capacity, int qty) {
        Compartment comp = new Compartment(capacity, EnumSet.of(StockAttributes.FLAMMABLE));
        Stock s = makeStock("Box", qty, 1.0, EnumSet.of(StockAttributes.FLAMMABLE));

        assertThrows(IllegalArgumentException.class, () -> comp.addStock(s));
    }

    @ParameterizedTest
    @CsvSource({
        "10, 3",
        "20, 5"
    })
    @DisplayName("addStock stores item and updates free space properly")
    void testAddStockSuccess(int capacity, int qty) {
        Compartment comp = new Compartment(capacity, EnumSet.of(StockAttributes.FLAMMABLE));

        Stock s = makeStock("Oil", qty, 1.0, EnumSet.of(StockAttributes.FLAMMABLE));
        comp.addStock(s);

        assertEquals(capacity - qty, comp.getFreeSpace());
        assertEquals(1, comp.getStockItems().size());
        assertEquals(s, comp.getStockItems().get(0));
    }

    @ParameterizedTest
    @CsvSource({
        "30, 10, 5, 15",
        "50, 20, 20, 10"
    })
    @DisplayName("getFreeSpace correct after multiple adds")
    void testGetFreeSpace(int cap, int q1, int q2, int expected) {
        Compartment comp = new Compartment(cap, EnumSet.of(StockAttributes.FLAMMABLE));

        comp.addStock(makeStock("a", q1, 1.0, EnumSet.of(StockAttributes.FLAMMABLE)));
        comp.addStock(makeStock("b", q2, 1.0, EnumSet.of(StockAttributes.FLAMMABLE)));

        assertEquals(expected, comp.getFreeSpace());
    }

    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("toString includes capacity, free space, and qualities")
    void testToString(StockAttributes attr) {
        Compartment comp = new Compartment(40, EnumSet.of(attr));
        String text = comp.toString();

        assertTrue(text.contains("Cap:40"));
        assertTrue(text.contains("Free:40"));
        assertTrue(text.contains(attr.toString()));
    }
}
