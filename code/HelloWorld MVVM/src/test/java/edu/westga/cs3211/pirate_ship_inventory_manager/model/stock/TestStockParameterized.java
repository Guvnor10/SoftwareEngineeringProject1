package edu.westga.cs3211.pirate_ship_inventory_manager.model.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;

class TestStockParameterized {

    private Stock makeStock(String name, int qty, double size, Set<StockAttributes> attrs, String cond, LocalDate exp) {
        return new Stock(name, qty, size, attrs, cond, exp);
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "  " })
    @DisplayName("Constructor rejects null/blank name")
    void testConstructorRejectsInvalidName(String badName) {
        assertThrows(IllegalArgumentException.class, () ->
            makeStock(badName, 1, 1.0, EnumSet.noneOf(StockAttributes.class), "good", null));
    }

    @ParameterizedTest
    @ValueSource(ints = { -5, 0 })
    @DisplayName("Constructor rejects invalid quantities")
    void testConstructorRejectsInvalidQuantities(int qty) {
        assertThrows(IllegalArgumentException.class, () ->
            makeStock("Item", qty, 1.0, EnumSet.noneOf(StockAttributes.class), "good", null));
    }

    @ParameterizedTest
    @ValueSource(doubles = { -1.0, -5.5 })
    @DisplayName("Constructor rejects negative size")
    void testConstructorRejectsNegativeSize(double size) {
        assertThrows(IllegalArgumentException.class, () ->
            makeStock("Item", 1, size, EnumSet.noneOf(StockAttributes.class), "good", null));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Constructor rejects null attributes set")
    void testConstructorRejectsNullAttributes(Set<StockAttributes> attrs) {
        assertThrows(IllegalArgumentException.class, () ->
            makeStock("Item", 1, 1.0, attrs, "good", null));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Constructor rejects null condition")
    void testConstructorRejectsNullCondition(String cond) {
        assertThrows(IllegalArgumentException.class, () ->
            makeStock("Item", 1, 1.0, EnumSet.noneOf(StockAttributes.class), cond, null));
    }

   

    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("Constructor correctly sets all fields")
    void testConstructorValid(StockAttributes attr) {
        LocalDate date = LocalDate.now().plusDays(2);
        Stock s = makeStock("Water", 5, 3.0, EnumSet.of(attr), "good", date);

        assertEquals("Water", s.getName());
        assertEquals(5, s.getQuantity());
        assertEquals(3.0, s.getSize(), 0.001);
        assertEquals("good", s.getCondition());
        assertEquals(date, s.getExpirationDate());
        assertTrue(s.getAttributes().contains(attr));
    }


    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("Attribute helper methods return correct values")
    void testAttributeMethods(StockAttributes attr) {
        Stock s = makeStock("Test", 1, 1.0, EnumSet.of(attr), "ok", null);

        assertEquals(attr == StockAttributes.FLAMMABLE, s.isFlammable());
        assertEquals(attr == StockAttributes.PERISHABLE, s.isPerishable());
        assertEquals(attr == StockAttributes.LIQUID, s.isLiquid());
    }

    @ParameterizedTest
    @ValueSource(strings = { "no attributes" })
    @DisplayName("Attribute helpers return false when no attributes present")
    void testAttributeHelpersNone(String ignore) {
        Stock s = makeStock("Test", 1, 1.0, EnumSet.noneOf(StockAttributes.class), "ok", null);

        assertFalse(s.isFlammable());
        assertFalse(s.isPerishable());
        assertFalse(s.isLiquid());
    }



    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("equals returns true for identical Stock objects")
    void testEqualsIdentical(StockAttributes attr) {
        LocalDate date = LocalDate.now();
        Stock s1 = makeStock("Item", 2, 1.5, EnumSet.of(attr), "cond", date);
        Stock s2 = makeStock("Item", 2, 1.5, EnumSet.of(attr), "cond", date);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @ParameterizedTest
    @ValueSource(strings = { "name", "quantity", "size", "attributes", "condition", "expiration" })
    @DisplayName("equals returns false when fields differ")
    void testEqualsDifferent(String differingField) {
        LocalDate date = LocalDate.now();
        Stock base = makeStock("Item", 2, 1.5, EnumSet.of(StockAttributes.FLAMMABLE), "cond", date);
        Stock modified;

        switch (differingField) {
            case "name":
                modified = makeStock("Other", 2, 1.5, EnumSet.of(StockAttributes.FLAMMABLE), "cond", date);
                break;
            case "quantity":
                modified = makeStock("Item", 3, 1.5, EnumSet.of(StockAttributes.FLAMMABLE), "cond", date);
                break;
            case "size":
                modified = makeStock("Item", 2, 9.9, EnumSet.of(StockAttributes.FLAMMABLE), "cond", date);
                break;
            case "attributes":
                modified = makeStock("Item", 2, 1.5, EnumSet.of(StockAttributes.LIQUID), "cond", date);
                break;
            case "condition":
                modified = makeStock("Item", 2, 1.5, EnumSet.of(StockAttributes.FLAMMABLE), "bad", date);
                break;
            case "expiration":
                modified = makeStock("Item", 2, 1.5, EnumSet.of(StockAttributes.FLAMMABLE), "cond", date.plusDays(1));
                break;
            default:
                modified = base;
        }

        assertNotEquals(base, modified);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"not a stock"})
    @DisplayName("equals returns false for null or non-Stock objects")
    void testEqualsInvalid(Object other) {
        Stock s = makeStock("X", 1, 1.0, EnumSet.of(StockAttributes.FLAMMABLE), "ok", null);

        assertNotEquals(s, other);
    }


    @ParameterizedTest
    @EnumSource(StockAttributes.class)
    @DisplayName("toString includes all relevant fields")
    void testToString(StockAttributes attr) {
        LocalDate date = LocalDate.now();
        Stock s = makeStock("Oil", 10, 2.5, EnumSet.of(attr), "cond", date);

        String text = s.toString();

        assertTrue(text.contains("Oil"));
        assertTrue(text.contains("10"));
        assertTrue(text.contains("2.5"));
        assertTrue(text.contains(attr.toString()));
        assertTrue(text.contains("cond"));
        assertTrue(text.contains(date.toString()));
    }
}
