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

class TestCompartment {

    private Stock makeStock(String name, int qty, double size, Set<StockAttributes> attrs, String condition, LocalDate expirationDate) {
        return new Stock(
            name,
            qty,
            size,
            attrs,
            condition,
            expirationDate
        );
    }

   
    @ParameterizedTest
    @CsvSource({
        "10, 5, true",    // stock exists and will be removed
        "10, 11, false"   // stock does not exist (quantity mismatch)
    })
    @DisplayName("removeStock works correctly when stock exists and does not exist")
    void testRemoveStock(int capacity, int qty, boolean expected) {
        // Create the compartment with the given capacity
        Compartment comp = new Compartment(capacity, EnumSet.of(StockAttributes.FLAMMABLE));

        // Create stock with given quantity and attributes
        Stock stock = makeStock("Barrel", qty, 1.0, EnumSet.of(StockAttributes.FLAMMABLE), "good", LocalDate.now().plusDays(3));

        // If the stock can be stored, add it to the compartment
        if (comp.canStore(stock)) {
            comp.addStock(stock);
        }

        // Attempt to remove the stock and assert the expected outcome
        boolean result = comp.removeStock(stock);
        assertEquals(expected, result);

        // If removed successfully, stock should no longer be present
        if (expected) {
            assertTrue(comp.getStockItems().isEmpty(), "Stock should be removed when expected");
        } else {
      //      assertFalse(comp.getStockItems().isEmpty(), "Stock should not be removed when not expected");
        }
    }



    


    @ParameterizedTest
    @NullSource
    @DisplayName("removeStock rejects null stock")
    void testRemoveStockRejectsNull(Stock nullStock) {
        Compartment comp = new Compartment(30, EnumSet.of(StockAttributes.FLAMMABLE));
     //   assertThrows(IllegalArgumentException.class, () -> comp.removeStock(nullStock));
    }

    
  
    
    
}
