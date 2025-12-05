package edu.westga.cs3211.pirate_ship_inventory_manager.model.inventory;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestInventoryParameterized {

    private Stock makeStock(String name, int qty, double size, StockAttributes attr) {
        return new Stock(
                name,
                qty,
                size,
                EnumSet.of(attr),
                "good",
                LocalDate.now().plusDays(2)
        );
    }

    private Stock makeGenericStock(String name, int qty) {
        return new Stock(
                name,
                qty,
                1.0,
                EnumSet.noneOf(StockAttributes.class),
                "ok",
                null
        );
    }

   

    @ParameterizedTest
    @ValueSource(ints = { 4 })  
    @DisplayName("getCompartments returns correct number and is unmodifiable")
    void testGetCompartments(int expectedSize) {
        Inventory inv = new Inventory();
        assertEquals(expectedSize, inv.getCompartments().size());

        assertThrows(UnsupportedOperationException.class,
            () -> inv.getCompartments().clear());
    }

    @ParameterizedTest
    @ValueSource(strings = { "shared" })
    @DisplayName("getSharedInventory returns the same singleton instance")
    void testGetSharedInventory(String ignore) {
        Inventory inv1 = Inventory.getSharedInventory();
        Inventory inv2 = Inventory.getSharedInventory();

        assertNotNull(inv1);
        assertSame(inv1, inv2);  
    }
   

    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("addStock stores item in correct compartment based on attribute")
    void testAddStockRoutesToCorrectCompartment(StockAttributes attr) {
        Inventory inv = new Inventory();
        Stock s = makeStock("Test", 1, 1.0, attr);

        inv.addStock(s);

        boolean found = inv.getCompartments().stream()
                .anyMatch(c -> c.getStockItems().contains(s));

        assertTrue(found);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("addStock rejects null stock")
    void testAddStockRejectsNull(Stock stock) {
        Inventory inv = new Inventory();
        assertThrows(IllegalArgumentException.class, () -> inv.addStock(stock));
    }

    @ParameterizedTest
    @CsvSource({
        "1000, FLAMMABLE",
        "1000, PERISHABLE",
        "1000, LIQUID"
    })
    @DisplayName("addStock throws IllegalStateException when no compartment can store it")
    void testAddStockFailsWhenNoCompartmentCanStore(int qty, StockAttributes attr) {
        Inventory inv = new Inventory();
        Stock tooLarge = makeStock("TooBig", qty, 1.0, attr);

        assertThrows(IllegalStateException.class,
                () -> inv.addStock(tooLarge));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 10, 50 })
    @DisplayName("addStock generic (no attributes) routes to FIRST compartment with space")
    void testAddStockGenericRoutesToFirstAvailable(int qty) {
        Inventory inv = new Inventory();
        Stock s = makeGenericStock("Generic", qty);

        inv.addStock(s);

        
        assertTrue(inv.getCompartments().get(0).getStockItems().contains(s));
    }

    @ParameterizedTest
    @CsvSource({
        "FLAMMABLE",
        "PERISHABLE",
        "LIQUID"
    })
    @DisplayName("getAllStock returns all items from compartments")
    void testGetAllStock(StockAttributes attr) {
        Inventory inv = new Inventory();
        Stock s = makeStock("Item", 1, 1.0, attr);

        inv.addStock(s);

        assertTrue(inv.getAllStock().contains(s));
    }

    @ParameterizedTest
    @ValueSource(strings = { "read-only check" })
    @DisplayName("getAllStock returns unmodifiable list")
    void testGetAllStockUnmodifiable(String ignore) {
        Inventory inv = new Inventory();
        Stock s = makeGenericStock("X", 1);
        inv.addStock(s);

        assertThrows(UnsupportedOperationException.class,
                () -> inv.getAllStock().clear());
    }


    @ParameterizedTest
    @ValueSource(strings = { "initial state" })
    @DisplayName("hasFreeSpace returns true on a fresh inventory")
    void testHasFreeSpaceDefault(String ignore) {
        Inventory inv = new Inventory();
        assertTrue(inv.hasFreeSpace());
    }

    @ParameterizedTest
    @ValueSource(strings = { "fill everything" })
    @DisplayName("hasFreeSpace returns false when all compartments are full")
    void testHasFreeSpaceFalse(String ignore) {
        Inventory inv = new Inventory();

        
        inv.getCompartments().get(0).addStock(makeStock("A", 100, 1, StockAttributes.FLAMMABLE));
        inv.getCompartments().get(1).addStock(makeStock("B", 200, 1, StockAttributes.PERISHABLE));
        inv.getCompartments().get(2).addStock(makeStock("C", 200, 1, StockAttributes.LIQUID));
        inv.getCompartments().get(3).addStock(makeGenericStock("D", 300));

        assertFalse(inv.hasFreeSpace());
    }
}
