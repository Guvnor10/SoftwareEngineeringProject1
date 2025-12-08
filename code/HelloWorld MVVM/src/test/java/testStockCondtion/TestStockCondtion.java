package testStockCondtion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockCondition;

public class TestStockCondtion {

    @ParameterizedTest
    @EnumSource(StockCondition.class)
    @DisplayName("Enum constants exist and valueOf works")
    void testEnumValues(StockCondition condition) {
        // name() returns the enum name
        String name = condition.name();

        // valueOf should return the same constant
        StockCondition fromValueOf = StockCondition.valueOf(name);

        assertEquals(condition, fromValueOf);
    }

    @ParameterizedTest
    @EnumSource(StockCondition.class)
    @DisplayName("Enum values() array contains all constants")
    void testValuesArrayContainsConstant(StockCondition condition) {
        boolean found = false;

        for (StockCondition c : StockCondition.values()) {
            if (c == condition) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }
}
