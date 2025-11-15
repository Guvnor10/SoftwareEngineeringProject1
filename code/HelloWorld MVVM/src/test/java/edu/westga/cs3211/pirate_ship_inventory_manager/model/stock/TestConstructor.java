package edu.westga.cs3211.pirate_ship_inventory_manager.model.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestStock {

    @Test
    void testValidStockCreation() {
        Set<StockAttributes> attributes = EnumSet.of(
                StockAttributes.FLAMMABLE,
                StockAttributes.LIQUID);

        LocalDate expiration = LocalDate.of(2025, 12, 31);

        Stock stock = new Stock("Gunpowder", 10, 5.0,
                attributes, "Dry and sealed", expiration);

        assertEquals("Gunpowder", stock.getName());
        assertEquals(10, stock.getQuantity());
        assertEquals(5.0, stock.getSize(), 0.0001);
        assertEquals("Dry and sealed", stock.getCondition());
        assertEquals(expiration, stock.getExpirationDate());

        assertTrue(stock.isFlammable());
        assertFalse(stock.isPerishable());
        assertTrue(stock.isLiquid());
    }

    @Test
    void testNameNullThrowsException() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            new Stock(null, 1, 1.0, attributes, "ok", null);
        });
    }

    @Test
    void testNameBlankThrowsException() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("   ", 1, 1.0, attributes, "ok", null);
        });
    }

    @Test
    void testQuantityZeroThrowsException() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("Rope", 0, 1.0, attributes, "ok", null);
        });
    }

    @Test
    void testQuantityNegativeThrowsException() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("Rope", -5, 1.0, attributes, "ok", null);
        });
    }

    @Test
    void testSizeNegativeThrowsException() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("Rope", 5, -1.0, attributes, "ok", null);
        });
    }

    @Test
    void testAttributesNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("Food", 5, 1.0, null, "ok", null);
        });
    }

    @Test
    void testConditionNullThrowsException() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("Food", 5, 1.0, attributes, null, null);
        });
    }

    @Test
    void testEmptyAttributesIsAllowed() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        Stock stock = new Stock("Crates", 3, 2.5, attributes, "good", null);

        assertFalse(stock.isFlammable());
        assertFalse(stock.isPerishable());
        assertFalse(stock.isLiquid());
        assertTrue(stock.getAttributes().isEmpty());
    }

    @Test
    void testGetAttributesIsUnmodifiable() {
        Set<StockAttributes> attributes = EnumSet.of(StockAttributes.FLAMMABLE);

        Stock stock = new Stock("Oil", 2, 1.0, attributes, "ok", null);

        Set<StockAttributes> fromGetter = stock.getAttributes();

        assertThrows(UnsupportedOperationException.class, () -> {
            fromGetter.add(StockAttributes.LIQUID);
        });
    }

    @Test
    void testEqualsAndHashCodeForEqualObjects() {
        Set<StockAttributes> attributes = EnumSet.of(StockAttributes.PERISHABLE);
        LocalDate expiration = LocalDate.of(2025, 1, 1);

        Stock stock1 = new Stock("Food", 5, 3.0, attributes, "fresh", expiration);
        Stock stock2 = new Stock("Food", 5, 3.0, attributes, "fresh", expiration);

        assertEquals(stock1, stock2);
        assertEquals(stock1.hashCode(), stock2.hashCode());
    }

    @Test
    void testEqualsReturnsFalseForDifferentObjects() {
        Set<StockAttributes> attributes = EnumSet.noneOf(StockAttributes.class);

        Stock stock1 = new Stock("Food", 5, 3.0, attributes, "fresh", null);
        Stock stock2 = new Stock("Water", 5, 3.0, attributes, "fresh", null);

        assertNotEquals(stock1, stock2);
    }
}
