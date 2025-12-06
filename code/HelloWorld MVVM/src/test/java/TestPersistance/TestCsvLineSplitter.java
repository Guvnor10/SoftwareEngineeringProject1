package TestPersistance;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.persistance.CsvLineSplitter;

public class TestCsvLineSplitter {

    @Test
    void testNullLineReturnsSingleEmptyColumn() {
        List<String> cols = CsvLineSplitter.split(null);
        assertEquals(1, cols.size());
        assertEquals("", cols.get(0));
    }

    @Test
    void testSimpleCommaSplitting() {
        List<String> cols = CsvLineSplitter.split("a,b,c");
        assertEquals(List.of("a", "b", "c"), cols);
    }

    @Test
    void testQuotedFieldWithComma() {
        List<String> cols = CsvLineSplitter.split("\"hello, world\",x");
        assertEquals(2, cols.size());
        assertEquals("hello, world", cols.get(0));
        assertEquals("x", cols.get(1));
    }

    @Test
    void testEscapedQuotesInsideQuotedField() {
        List<String> cols = CsvLineSplitter.split("\"he\"\"llo\",y");
        assertEquals(2, cols.size());
        assertEquals("he\"llo", cols.get(0)); // "" -> "
        assertEquals("y", cols.get(1));
    }

    @Test
    void testTrailingCommaCreatesEmptyColumn() {
        List<String> cols = CsvLineSplitter.split("a,");
        assertEquals(2, cols.size());
        assertEquals("a", cols.get(0));
        assertEquals("", cols.get(1));
    }

    @Test
    void testSpacesAreUntrimmed() {
        List<String> cols = CsvLineSplitter.split("  a  ,  b  ");
        assertEquals(2, cols.size());
        assertEquals("  a  ", cols.get(0));
        assertEquals("  b  ", cols.get(1));
    }
}

