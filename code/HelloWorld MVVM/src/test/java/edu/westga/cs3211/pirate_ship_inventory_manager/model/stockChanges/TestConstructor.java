package edu.westga.cs3211.pirate_ship_inventory_manager.model.stockChanges;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;

class TestConstructor {

	@Test
    void constructorShouldStoreParameters() {
        Stock stock = new Stock("Rope", 5, 1.0,
                EnumSet.noneOf(StockAttributes.class), "ok", null);
        String addedBy = "jack";
        LocalDateTime time = LocalDateTime.now();
        Compartment compartment = new Compartment(100,
                EnumSet.noneOf(StockAttributes.class));

        StockChangeEntry entry = new StockChangeEntry(stock, addedBy, time, compartment);

        assertSame(stock, entry.getStock());
        assertEquals(addedBy, entry.getAddedBy());
        assertEquals(time, entry.getTimestamp());
        assertSame(compartment, entry.getCompartment());
    }

    @Test
    void constructorShouldRejectNullArguments() {
        Stock stock = new Stock("Rope", 5, 1.0,
                EnumSet.noneOf(StockAttributes.class), "ok", null);
        String addedBy = "jack";
        LocalDateTime time = LocalDateTime.now();
        Compartment compartment = new Compartment(100,
                EnumSet.noneOf(StockAttributes.class));

        assertThrows(IllegalArgumentException.class,
                () -> new StockChangeEntry(null, addedBy, time, compartment));
        assertThrows(IllegalArgumentException.class,
                () -> new StockChangeEntry(stock, null, time, compartment));
        assertThrows(IllegalArgumentException.class,
                () -> new StockChangeEntry(stock, addedBy, null, compartment));
        assertThrows(IllegalArgumentException.class,
                () -> new StockChangeEntry(stock, addedBy, time, null));
    }

}
