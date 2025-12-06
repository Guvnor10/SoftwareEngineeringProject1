package testInputBundle;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.InputBundle;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockCondition;

public class TestInputBundle {

    @Test
    void testAllGettersAndSettersRoundTrip() {
        InputBundle bundle = new InputBundle();

        String itemName = "Cask of Rum";
        String qtyText = "42";
        String sizeText = "3.5";
        String notes = "Stored near aft deck";
        LocalDate exp = LocalDate.of(2026, 1, 15);

        bundle.setItemName(itemName);
        bundle.setQuantityText(qtyText);
        bundle.setSizeText(sizeText);
        bundle.setConditionNotes(notes);
        bundle.setExpirationDate(exp);
        bundle.setSelectedCondition(StockCondition.USABLE);
        bundle.setQuantityValue(42);
        bundle.setSizeValue(3.5);

        assertEquals(itemName, bundle.getItemName());
        assertEquals(qtyText, bundle.getQuantityText());
        assertEquals(sizeText, bundle.getSizeText());
        assertEquals(notes, bundle.getConditionNotes());
        assertEquals(exp, bundle.getExpirationDate());
        assertEquals(StockCondition.USABLE, bundle.getSelectedCondition());
        assertEquals(42, bundle.getQuantityValue());
        assertEquals(3.5, bundle.getSizeValue());
    }

    @Test
    void testNullableFields() {
        InputBundle bundle = new InputBundle();
        assertNull(bundle.getExpirationDate());
        assertNull(bundle.getSelectedCondition());

        bundle.setExpirationDate(null);
        bundle.setSelectedCondition(null);

        assertNull(bundle.getExpirationDate());
        assertNull(bundle.getSelectedCondition());
    }
}

