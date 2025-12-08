package TestPersistance;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
import edu.westga.cs3211.pirate_ship_inventory_manager.persistance.StockChangesPersistance;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStockChangesPersistance {

    private static final Path CSV = Path.of("StockChanges.csv");

    @BeforeEach
    void cleanBefore() throws IOException {
        Files.deleteIfExists(CSV);
    }

    @AfterEach
    void cleanAfter() throws IOException {
        Files.deleteIfExists(CSV);
    }

    private StockChangeEntry newEntry(
            String name,
            int qty,
            double size,
            EnumSet<StockAttributes> attrs,
            String condition,
            LocalDate expiration,
            Compartment compartment,
            String addedBy,
            LocalDateTime ts) {

        Stock stock = new Stock(name, qty, size, attrs, condition, expiration); // validates; throws if invalid
        return new StockChangeEntry(stock, addedBy, ts, compartment);
    }

    @Test
    @Order(1)
    void testLoadAllWhenFileDoesNotExistReturnsEmpty() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();
        List<StockChangeEntry> entries = p.loadAll();
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }

    @Test
    @Order(2)
    void testAppendNullThrows() {
        StockChangesPersistance p = new StockChangesPersistance();
        assertThrows(IllegalArgumentException.class, () -> p.append(null));
    }

    @Test
    @Order(3)
    void testAppendCreatesHeaderAndWritesLineWithQuotingThenLoadAllParsesBack() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();

        // choose a real compartment to ensure "match on load"
        // using the shared inventory: 300 capacity with NO special qualities
        Compartment general = Inventory.getSharedInventory().getCompartments().get(3); // Cap=300, Qualities=none
        // item & condition with comma and quotes to exercise CSV quoting/unquoting
        String itemWithCommaAndQuote = "Oil, \"Lamp\"";
        String conditionWithCommaAndQuote = "USABLE - fixed, contains \"note\"";

        StockChangeEntry entry = newEntry(
                itemWithCommaAndQuote, 5, 2.5,
                EnumSet.of(StockAttributes.FLAMMABLE, StockAttributes.LIQUID),
                conditionWithCommaAndQuote,
                null, // expiration empty when not perishable
                general,
                "crew1",
                LocalDateTime.now()
        );

        p.append(entry);

        // header must exist
        List<String> lines = Files.readAllLines(CSV, StandardCharsets.UTF_8);
        assertFalse(lines.isEmpty());
        assertEquals(
                "timestamp,itemName,quantity,size,attributes,condition,expirationDate,compartmentCapacity,compartmentQualities,addedBy",
                lines.get(0)
        );

        // reload and verify fields
        List<StockChangeEntry> loaded = p.loadAll();
        assertEquals(1, loaded.size());

        StockChangeEntry e = loaded.get(0);
        assertEquals(entry.getAddedBy(), e.getAddedBy());
        assertEquals(entry.getStock().getName(), e.getStock().getName());
        assertEquals(entry.getStock().getQuantity(), e.getStock().getQuantity());
        assertEquals(entry.getStock().getSize(), e.getStock().getSize(), 0.0001);
        assertEquals(entry.getStock().getAttributes(), e.getStock().getAttributes());
        assertEquals(entry.getStock().getCondition(), e.getStock().getCondition());
        assertNull(e.getStock().getExpirationDate());
        // compartment match (capacity + qualities)
        assertEquals(general.getCapacity(), e.getCompartment().getCapacity());
        assertEquals(general.getSpecialQualities(), e.getCompartment().getSpecialQualities());
    }

    @Test
    @Order(4)
    void testAppendAndLoadPerishableWithExpirationAndMatchingCompartment() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();

        Compartment perishable = Inventory.getSharedInventory().getCompartments().get(1); // Cap=200, Qualities=PERISHABLE
        LocalDate exp = LocalDate.of(2026, 5, 1);

        StockChangeEntry entry = newEntry(
                "Bread",
                10,
                0.0,
                EnumSet.of(StockAttributes.PERISHABLE),
                "PERFECT",
                exp,
                perishable,
                "quarter1",
                LocalDateTime.now()
        );

        p.append(entry);

        List<StockChangeEntry> loaded = p.loadAll();
        assertEquals(1, loaded.size());
        assertEquals(exp, loaded.get(0).getStock().getExpirationDate());
        assertEquals(perishable.getCapacity(), loaded.get(0).getCompartment().getCapacity());
        assertEquals(perishable.getSpecialQualities(), loaded.get(0).getCompartment().getSpecialQualities());
    }

    @Test
    @Order(5)
    void testEnsureHeaderWrittenForExistingEmptyFile() throws IOException {
        // Create empty file manually (size == 0)
        Files.write(CSV, new byte[0]);
        assertTrue(Files.exists(CSV));
        assertEquals(0L, Files.size(CSV));

        StockChangesPersistance p = new StockChangesPersistance();
        Compartment general = Inventory.getSharedInventory().getCompartments().get(3);

        StockChangeEntry entry = newEntry(
                "Rope", 3, 1.0, EnumSet.noneOf(StockAttributes.class),
                "USABLE", null, general, "crew2", LocalDateTime.now());

        p.append(entry);

        List<String> lines = Files.readAllLines(CSV, StandardCharsets.UTF_8);
        assertTrue(lines.size() >= 2);
        assertEquals(
                "timestamp,itemName,quantity,size,attributes,condition,expirationDate,compartmentCapacity,compartmentQualities,addedBy",
                lines.get(0)
        );
    }

    @Test
    @Order(6)
    void testLoadAllIgnoresInvalidLinesAndParsesValidOnes() throws IOException {
        // Write header + multiple lines (some invalid)
        String header = "timestamp,itemName,quantity,size,attributes,condition,expirationDate,compartmentCapacity,compartmentQualities,addedBy";
        String validTs = LocalDateTime.now().toString();

        // 1) wrong column count
        String badColumns = "a,b";

        // 2) bad timestamp (not ISO-8601)
        String badTimestamp = "not-a-timestamp,Bread,5,1.0,PERISHABLE,USABLE,2026-05-01,200,PERISHABLE,crewX";

        // 3) bad integer quantity
        String badQty = validTs + ",Salt,abc,1.0,FLAMMABLE,USABLE,,100,FLAMMABLE,crewY";

        // 4) bad double size
        String badSize = validTs + ",Salt,1,not-a-double,FLAMMABLE,USABLE,,100,FLAMMABLE,crewY";

        // 5) invalid stock (empty name -> Stock constructor throws)
        String invalidStock = validTs + ",,1,0.0,,USABLE,,,,"; // still 10 columns

        // 6) capacity non-numeric -> fallback capacity=1; attributes parse with UNKNOWN token ignored
        String capNonNumeric = validTs + ",\"Oil, Lamp\",1,0.0,UNKNOWN;LIQUID,USABLE,,NaN,LIQUID,tester";

        Files.write(
            CSV,
            List.of(header, badColumns, badTimestamp, badQty, badSize, invalidStock, capNonNumeric),
            StandardCharsets.UTF_8
        );

        StockChangesPersistance p = new StockChangesPersistance();
        List<StockChangeEntry> loaded = p.loadAll();

        // Only #6 should produce a valid entry
        assertEquals(1, loaded.size());
        StockChangeEntry e = loaded.get(0);

        // Item name was quoted with comma -> unescape worked
        assertEquals("Oil, Lamp", e.getStock().getName());
        // Attributes unknown ignored -> only LIQUID remains
        assertEquals(EnumSet.of(StockAttributes.LIQUID), e.getStock().getAttributes());
        // Capacity non-numeric -> parse error -> capacity=0 -> created compartment has capacity 1
        assertEquals(1, e.getCompartment().getCapacity());
        assertEquals(EnumSet.of(StockAttributes.LIQUID), EnumSet.copyOf(e.getCompartment().getSpecialQualities()));
    }

    @Test
    @Order(7)
    void testRefreshRoundTripWithMultipleEntries() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();

        Compartment flammable = Inventory.getSharedInventory().getCompartments().get(0); // 100, FLAMMABLE
        Compartment general = Inventory.getSharedInventory().getCompartments().get(3);    // 300, none

        StockChangeEntry e1 = newEntry(
                "Pitch", 50, 10.0,
                EnumSet.of(StockAttributes.FLAMMABLE),
                "USABLE - viscous", null,
                flammable, "crewA", LocalDateTime.now()
        );
        StockChangeEntry e2 = newEntry(
                "Barrel", 2, 55.0,
                EnumSet.noneOf(StockAttributes.class),
                "PERFECT", null,
                general, "crewB", LocalDateTime.now()
        );

        p.append(e1);
        p.append(e2);

        List<StockChangeEntry> loaded = p.loadAll();
        assertEquals(2, loaded.size());

        // Check both entries exist by names and attributes
        assertTrue(loaded.stream().anyMatch(e -> e.getStock().getName().equals("Pitch")
                && e.getStock().getAttributes().contains(StockAttributes.FLAMMABLE)));
        assertTrue(loaded.stream().anyMatch(e -> e.getStock().getName().equals("Barrel")
                && e.getStock().getAttributes().isEmpty()));
    }
}

