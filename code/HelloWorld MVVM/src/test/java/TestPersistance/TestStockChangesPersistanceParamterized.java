package TestPersistance;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;
import edu.westga.cs3211.pirate_ship_inventory_manager.persistance.StockChangesPersistance;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStockChangesPersistanceParamterized {

    private static final Path CSV = Path.of("StockChanges.csv");

   
    private Object invokePrivate(String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        StockChangesPersistance p = new StockChangesPersistance();
        Method m = StockChangesPersistance.class.getDeclaredMethod(methodName, paramTypes);
        m.setAccessible(true);
        return m.invoke(p, args);
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

        Stock stock = new Stock(name, qty, size, attrs, condition, expiration);
        return new StockChangeEntry(stock, addedBy, ts, compartment);
    }

    @BeforeEach
    void cleanBefore() throws IOException {
        Files.deleteIfExists(CSV);
    }

    @AfterEach
    void cleanAfter() throws IOException {
        Files.deleteIfExists(CSV);
    }

    @Test
    @Order(1)
    @DisplayName("loadAll returns empty list when file does not exist")
    void testLoadAllWhenFileDoesNotExistReturnsEmpty() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();
        List<StockChangeEntry> entries = p.loadAll();
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("append(null) throws IllegalArgumentException")
    void testAppendNullThrows() {
        StockChangesPersistance p = new StockChangesPersistance();
        assertThrows(IllegalArgumentException.class, () -> p.append(null));
    }

    @Test
    @Order(3)
    @DisplayName("append creates header, writes line, loadAll parses it back (with quoting)")
    void testAppendCreatesHeaderAndWritesLineWithQuotingThenLoadAllParsesBack() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();

        Compartment general = Inventory.getSharedInventory().getCompartments().get(5); // 300, none
        String itemWithCommaAndQuote = "Oil, \"Lamp\"";
        String conditionWithCommaAndQuote = "USABLE - fixed, contains \"note\"";

        StockChangeEntry entry = newEntry(
                itemWithCommaAndQuote, 5, 2.5,
                EnumSet.of(StockAttributes.FLAMMABLE, StockAttributes.LIQUID),
                conditionWithCommaAndQuote,
                null,
                general,
                "crew1",
                LocalDateTime.now()
        );

        p.append(entry);

        List<String> lines = Files.readAllLines(CSV, StandardCharsets.UTF_8);
        assertFalse(lines.isEmpty());
        assertEquals(
                "timestamp,itemName,quantity,size,attributes,condition,expirationDate,compartmentCapacity,compartmentQualities,addedBy",
                lines.get(0)
        );

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
        assertEquals(general.getCapacity(), e.getCompartment().getCapacity());
        assertEquals(general.getSpecialQualities(), e.getCompartment().getSpecialQualities());
    }

    @Test
    @Order(4)
    @DisplayName("append and load perishable stock with expiration and matching compartment")
    void testAppendAndLoadPerishableWithExpirationAndMatchingCompartment() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();

        Compartment perishable = Inventory.getSharedInventory().getCompartments().get(1); // 200, PERISHABLE
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
    @DisplayName("ensureFileHasHeader writes header for existing empty file")
    void testEnsureHeaderWrittenForExistingEmptyFile() throws IOException {
        Files.write(CSV, new byte[0]);
        assertTrue(Files.exists(CSV));
        assertEquals(0L, Files.size(CSV));

        StockChangesPersistance p = new StockChangesPersistance();
        Compartment general = Inventory.getSharedInventory().getCompartments().get(5);

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
    @DisplayName("loadAll ignores invalid lines and parses valid ones")
    void testLoadAllIgnoresInvalidLinesAndParsesValidOnes() throws IOException {
        String header = "timestamp,itemName,quantity,size,attributes,condition,expirationDate,compartmentCapacity,compartmentQualities,addedBy";
        String validTs = LocalDateTime.now().toString();

        String badColumns = "a,b";
        String badTimestamp = "not-a-timestamp,Bread,5,1.0,PERISHABLE,USABLE,2026-05-01,200,PERISHABLE,crewX";
        String badQty = validTs + ",Salt,abc,1.0,FLAMMABLE,USABLE,,100,FLAMMABLE,crewY";
        String badSize = validTs + ",Salt,1,not-a-double,FLAMMABLE,USABLE,,100,FLAMMABLE,crewY";
        String invalidStock = validTs + ",,1,0.0,,USABLE,,,,"; 
        String capNonNumeric = validTs + ",\"Oil, Lamp\",1,0.0,UNKNOWN;LIQUID,USABLE,,NaN,LIQUID,tester";

        Files.write(
                CSV,
                List.of(header, badColumns, badTimestamp, badQty, badSize, invalidStock, capNonNumeric),
                StandardCharsets.UTF_8
        );

        StockChangesPersistance p = new StockChangesPersistance();
        List<StockChangeEntry> loaded = p.loadAll();

        assertEquals(1, loaded.size());
        StockChangeEntry e = loaded.get(0);

        assertEquals("Oil, Lamp", e.getStock().getName());
        assertEquals(EnumSet.of(StockAttributes.LIQUID), e.getStock().getAttributes());
        assertEquals(1, e.getCompartment().getCapacity());
        assertEquals(EnumSet.of(StockAttributes.LIQUID),
                     EnumSet.copyOf(e.getCompartment().getSpecialQualities()));
    }

    @Test
    @Order(7)
    @DisplayName("Multiple entries survive round trip")
    void testRoundTripWithMultipleEntries() throws IOException {
        StockChangesPersistance p = new StockChangesPersistance();

        Compartment flammable = Inventory.getSharedInventory().getCompartments().get(0); // 100, FLAMMABLE
        Compartment general = Inventory.getSharedInventory().getCompartments().get(5);    // 300, none

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

        assertTrue(loaded.stream().anyMatch(e -> e.getStock().getName().equals("Pitch")
                && e.getStock().getAttributes().contains(StockAttributes.FLAMMABLE)));
        assertTrue(loaded.stream().anyMatch(e -> e.getStock().getName().equals("Barrel")
                && e.getStock().getAttributes().isEmpty()));
    }

   

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "10.5", "xyz", " 3.0 " })
    @DisplayName("parseDoubleOrNull handles valid, invalid, and null")
    void testParseDoubleOrNull(String text) throws Exception {
        Double result = (Double) invokePrivate(
                "parseDoubleOrNull",
                new Class<?>[] { String.class },
                text);

        if (text == null || text.trim().equals("xyz")) {
            assertNull(result);
        } else {
            assertEquals(Double.valueOf(text.trim()), result, 0.0001);
        }
    }



    @ParameterizedTest
    @CsvSource({
        " ,null",
        "   ,null",
        "2026-05-01,2026-05-01",
        "bad,null"
    })
    @DisplayName("parseDateOrNull handles empty, valid, and invalid dates")
    void testParseDateOrNull(String text, String expected) throws Exception {
        LocalDate result = (LocalDate) invokePrivate(
                "parseDateOrNull",
                new Class<?>[] { String.class },
                text);

        if ("null".equals(expected)) {
            assertNull(result);
        } else {
            assertNotNull(result);
            assertEquals(LocalDate.parse(expected), result);
        }
    }



    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "   ", "FLAMMABLE;LIQUID", "UNKNOWN;PERISHABLE;   " })
    @DisplayName("parseAttributes handles null, empty, valid, and unknown tokens")
    void testParseAttributes(String text) throws Exception {
        @SuppressWarnings("unchecked")
        EnumSet<StockAttributes> attrs = (EnumSet<StockAttributes>) invokePrivate(
                "parseAttributes",
                new Class<?>[] { String.class },
                text);

        if (text == null || text.trim().isEmpty()) {
            assertTrue(attrs.isEmpty());
        } else if (text.equals("FLAMMABLE;LIQUID")) {
            assertEquals(EnumSet.of(StockAttributes.FLAMMABLE, StockAttributes.LIQUID), attrs);
        } else if (text.equals("UNKNOWN;PERISHABLE;   ")) {
            assertEquals(EnumSet.of(StockAttributes.PERISHABLE), attrs);
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
        "plain",
        "\"He said \"\"Hi\"\"\""
    })
    @DisplayName("unescapeCsv handles null, plain, and quoted with escaped quotes")
    void testUnescapeCsv(String value) throws Exception {
        String result = (String) invokePrivate(
                "unescapeCsv",
                new Class<?>[] { String.class },
                value);

        if (value == null) {
            assertEquals("", result);
        } else if (value.equals("plain")) {
            assertEquals("plain", result);
        } else if (value.equals("\"He said \"\"Hi\"\"\"")) {
            assertEquals("He said \"Hi\"", result);
        }
    }
}
