package TestPersistance;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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
   

    @ParameterizedTest
    @ValueSource(strings = { "" })
    @DisplayName("split(\"\") returns single empty field")
    void testSplitEmptyString(String input) {
        List<String> result = CsvLineSplitter.split(input);
        assertEquals(1, result.size());
        assertEquals("", result.get(0));
    }

    @Test
    @DisplayName("Basic unquoted CSV is split on commas")
    void testBasicUnquoted() {
        List<String> result = CsvLineSplitter.split("a,b,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }

    @Test
    @DisplayName("Quoted field with comma is kept intact")
    void testQuotedWithComma() {
        List<String> result = CsvLineSplitter.split("\"a,b\",c");
        assertEquals(2, result.size());
        assertEquals("a,b", result.get(0));
        assertEquals("c", result.get(1));
    }

    @Test
    @DisplayName("Escaped quotes inside quoted field are unescaped correctly")
    void testEscapedQuotes() {
        // Field: "He said ""Hi"""
        String line = "\"He said \"\"Hi\"\"\",X";
        List<String> result = CsvLineSplitter.split(line);

        assertEquals(2, result.size());
        assertEquals("He said \"Hi\"", result.get(0));
        assertEquals("X", result.get(1));
    }

    @Test
    @DisplayName("Trailing comma produces empty last field")
    void testTrailingComma() {
        List<String> result = CsvLineSplitter.split("a,b,");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("", result.get(2));
    }

    
}

