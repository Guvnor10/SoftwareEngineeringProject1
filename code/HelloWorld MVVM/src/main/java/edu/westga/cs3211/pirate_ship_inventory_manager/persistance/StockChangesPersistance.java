
package edu.westga.cs3211.pirate_ship_inventory_manager.persistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Compartment;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Inventory;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Stock;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockAttributes;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.StockChangeEntry;

/**
 * Appends new records to a CSV file and loads them
 * 
 * @author Yeni
 * @version Fall 2025
 */
public class StockChangesPersistance {

    /** CSV header text used to initialize the file. */
    private static final String CSV_HEADER =
        "timestamp,itemName,quantity,size,attributes,condition,expirationDate,compartmentCapacity,compartmentQualities,addedBy";

    /** Name of the CSV file (relative path). */
    private static final String CSV_FILENAME = "StockChanges.csv";

    /** Path to the CSV file. */
    private final Path csvPath;

    /**
     * Creates a new persister targeting the default file path {@code StockChanges.csv}.
     */
    public StockChangesPersistance() {
        this.csvPath = Path.of(CSV_FILENAME);
    }

    /**
     * Appends a single {@link StockChangeEntry} to the CSV file.
     *
     * @param entry the entry to write; must not be {@code null}
     * @throws IOException if the file cannot be created or written
     * @throws IllegalArgumentException if {@code entry} is {@code null}
     */
    public void append(StockChangeEntry entry) throws IOException {
        if (entry == null) {
            throw new IllegalArgumentException("StockChangeEntry cannot be null.");
        }

        this.ensureFileHasHeader();

        String csvLine = this.serializeEntry(entry);

        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(
                this.csvPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND
            );
            writer.write(csvLine);
            writer.newLine();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Loads all persisted {@link StockChangeEntry} records from the CSV file.
     * If the file does not exist or is empty, returns an empty list.
     *
     * @return a list of parsed {@link StockChangeEntry} objects (never {@code null})
     * @throws IOException if the file exists but cannot be read
     */
    public List<StockChangeEntry> loadAll() throws IOException {
        List<StockChangeEntry> entries = new ArrayList<>();

        boolean fileExists = Files.exists(this.csvPath);
        if (!fileExists) {
            return entries;
        }

        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(this.csvPath, StandardCharsets.UTF_8);

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return entries;
            }

            String recordLine = reader.readLine();
            while (recordLine != null) {
                StockChangeEntry parsedEntry = this.parseEntry(recordLine);
                if (parsedEntry != null) {
                    entries.add(parsedEntry);
                }
                recordLine = reader.readLine();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return entries;
    }

    /**
     * Ensures the CSV file exists and contains the header row.
     *
     * @throws IOException if the file cannot be created or written
     */
    private void ensureFileHasHeader() throws IOException {
        boolean fileExists = Files.exists(this.csvPath);
        if (!fileExists) {
            BufferedWriter writer = null;
            try {
                writer = Files.newBufferedWriter(
                    this.csvPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
                );
                writer.write(CSV_HEADER);
                writer.newLine();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            return;
        }

        long fileSize = Files.size(this.csvPath);
        boolean isEmpty = fileSize == 0L;
        if (isEmpty) {
            BufferedWriter writer = null;
            try {
                writer = Files.newBufferedWriter(
                    this.csvPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND
                );
                writer.write(CSV_HEADER);
                writer.newLine();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    /**
     * Serializes a {@link StockChangeEntry} to a single CSV line.
     *
     * @param entry the source entry
     * @return CSV line text
     */
    private String serializeEntry(StockChangeEntry entry) {
        // Timestamp
        String timestampText = entry.getTimestamp().toString();

        // Stock core fields
        Stock stock = entry.getStock();
        String itemNameText = this.escapeCsv(stock.getName());
        String quantityText = Integer.toString(stock.getQuantity());
        String sizeText = Double.toString(stock.getSize());
        String attributesText = this.serializeAttributes(stock.getAttributes());
        String conditionText = this.escapeCsv(stock.getCondition());

        // Expiration date (avoid ternary)
        LocalDate expirationDate = stock.getExpirationDate();
        String expirationText;
        boolean hasNoExpiration = expirationDate == null;
        if (hasNoExpiration) {
            expirationText = "";
        } else {
            expirationText = expirationDate.toString();
        }

        // Compartment (capacity + qualities)
        Compartment compartment = entry.getCompartment();
        String compartmentCapacityText = Integer.toString(compartment.getCapacity());
        String compartmentQualitiesText = this.serializeAttributes(compartment.getSpecialQualities());

        // Added by
        String addedByText = this.escapeCsv(entry.getAddedBy());

        // Assemble in column order
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(timestampText).append(",");
        lineBuilder.append(itemNameText).append(",");
        lineBuilder.append(quantityText).append(",");
        lineBuilder.append(sizeText).append(",");
        lineBuilder.append(attributesText).append(",");
        lineBuilder.append(conditionText).append(",");
        lineBuilder.append(expirationText).append(",");
        lineBuilder.append(compartmentCapacityText).append(",");
        lineBuilder.append(compartmentQualitiesText).append(",");
        lineBuilder.append(addedByText);

        return lineBuilder.toString();
    }

    /**
     * Escapes a field for CSV by quoting if needed and doubling embedded quotes.
     *
     * @param rawValue the input string (may be {@code null})
     * @return CSV-safe string (never {@code null})
     */
    private String escapeCsv(String rawValue) {
        if (rawValue == null) {
            return "";
        }

        boolean containsComma = rawValue.contains(",");
        boolean containsQuote = rawValue.contains("\"");
        boolean containsNewline = rawValue.contains("\n");
        boolean containsCarriageReturn = rawValue.contains("\r");

        boolean containsSpecial = containsComma || containsQuote || containsNewline || containsCarriageReturn;
        if (!containsSpecial) {
            return rawValue;
        }

        String doubledQuotes = rawValue.replace("\"", "\"\"");
        return "\"" + doubledQuotes + "\"";
    }

    /**
     * Serializes a set of {@link StockAttributes} as a semicolon-separated list.
     *
     * @param attributes the attribute set (may be empty)
     * @return semicolon-separated string (no spaces)
     */
    private String serializeAttributes(Set<StockAttributes> attributes) {
        StringBuilder builder = new StringBuilder();

        boolean isFirst = true;
        for (StockAttributes attribute : attributes) {
            if (!isFirst) {
                builder.append(";");
            }
            builder.append(attribute.name());
            isFirst = false;
        }

        return builder.toString();
    }

    /**
     * Parses a CSV line into a {@link StockChangeEntry}.
     * Invalid lines are ignored and return {@code null}.
     *
     * @param csvLine the raw CSV line
     * @return parsed {@link StockChangeEntry} or {@code null} if invalid
     */
    private StockChangeEntry parseEntry(String csvLine) {
        if (csvLine == null) {
            return null;
        }

        List<String> columns = CsvLineSplitter.split(csvLine);
        int expectedColumnCount = 10;
        boolean hasExpectedColumns = columns.size() == expectedColumnCount;
        if (!hasExpectedColumns) {
            return null;
        }

        // Extract text columns
        String timestampText = columns.get(0);
        String itemNameText = columns.get(1);
        String quantityText = columns.get(2);
        String sizeText = columns.get(3);
        String attributesText = columns.get(4);
        String conditionText = columns.get(5);
        String expirationText = columns.get(6);
        String compartmentCapacityText = columns.get(7);
        String compartmentQualitiesText = columns.get(8);
        String addedByText = columns.get(9);

        // Parse primitives
        LocalDateTime timestamp = this.parseTimestampOrNull(timestampText);
        if (timestamp == null) {
            return null;
        }

        Integer quantity = this.parseIntegerOrNull(quantityText);
        if (quantity == null) {
            return null;
        }

        Double size = this.parseDoubleOrNull(sizeText);
        if (size == null) {
            return null;
        }

        // Build Stock
        Stock stockItem = this.buildStockOrNull(itemNameText, quantity, size, attributesText, conditionText, expirationText);
        if (stockItem == null) {
            return null;
        }

        // Build Compartment
        Compartment compartment = this.buildCompartmentOrDefault(compartmentCapacityText, compartmentQualitiesText);

        // Build final entry
        StockChangeEntry entry;
        try {
            entry = new StockChangeEntry(stockItem, addedByText, timestamp, compartment);
        } catch (IllegalArgumentException invalidEntry) {
            return null;
        }

        return entry;
    }

    /**
     * Parses a timestamp text into {@link LocalDateTime}, returning {@code null} on failure.
     *
     * @param text the timestamp text
     * @return parsed {@link LocalDateTime}, or {@code null} if invalid
     */
    private LocalDateTime parseTimestampOrNull(String text) {
        try {
            return LocalDateTime.parse(text);
        } catch (Exception parseError) {
            return null;
        }
    }

    /**
     * Parses an integer text into {@link Integer}, returning {@code null} on failure.
     *
     * @param text the integer text
     * @return parsed {@link Integer}, or {@code null} if invalid
     */
    private Integer parseIntegerOrNull(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception parseError) {
            return null;
        }
    }

    /**
     * Parses a double text into {@link Double}, returning {@code null} on failure.
     *
     * @param text the double text
     * @return parsed {@link Double}, or {@code null} if invalid
     */
    private Double parseDoubleOrNull(String text) {
        try {
            return Double.parseDouble(text);
        } catch (Exception parseError) {
            return null;
        }
    }

    /**
     * Builds a {@link Stock} instance from column texts.
     *
     * @param itemNameText item name text (may be quoted)
     * @param quantity quantity value (non-null)
     * @param size size value (non-null)
     * @param attributesText semicolon-separated attributes
     * @param conditionText condition text (may be quoted)
     * @param expirationText ISO-8601 local date (may be empty)
     * @return the constructed {@link Stock}, or {@code null} if invalid
     */
    private Stock buildStockOrNull(
            String itemNameText,
            Integer quantity,
            Double size,
            String attributesText,
            String conditionText,
            String expirationText) {

        EnumSet<StockAttributes> stockAttributes = this.parseAttributes(attributesText);
        LocalDate expirationDate = this.parseDateOrNull(expirationText);

        String unescapedCondition = "";
        boolean conditionIsNull = conditionText == null;
        if (!conditionIsNull) {
            unescapedCondition = this.unescapeCsv(conditionText);
        }

        String unescapedItemName = "";
        boolean itemNameIsNull = itemNameText == null;
        if (!itemNameIsNull) {
            unescapedItemName = this.unescapeCsv(itemNameText);
        }

        try {
            return new Stock(
                unescapedItemName,
                quantity.intValue(),
                size.doubleValue(),
                stockAttributes,
                unescapedCondition,
                expirationDate
            );
        } catch (IllegalArgumentException invalidStock) {
            return null;
        }
    }

    /**
     * Builds a {@link Compartment} instance by matching shared inventory or creating a default.
     *
     * @param capacityText capacity text
     * @param qualitiesText semicolon-separated qualities
     * @return a matching or default {@link Compartment} (never {@code null})
     */
    private Compartment buildCompartmentOrDefault(
            String capacityText,
            String qualitiesText) {

        int capacityValue = this.parseCapacityOrDefault(capacityText);
        EnumSet<StockAttributes> compartmentQualities = this.parseAttributes(qualitiesText);

        Inventory sharedInventory = Inventory.getSharedInventory();
        List<Compartment> compartments = sharedInventory.getCompartments();

        for (Compartment existing : compartments) {
            boolean capacityMatches = existing.getCapacity() == capacityValue;
            boolean qualitiesMatch = existing.getSpecialQualities().equals(compartmentQualities);
            if (capacityMatches && qualitiesMatch) {
                return existing;
            }
        }

        int finalCapacity = capacityValue;
        boolean capacityIsInvalid = finalCapacity <= 0;
        if (capacityIsInvalid) {
            finalCapacity = 1;
        }

        try {
            return new Compartment(finalCapacity, compartmentQualities);
        } catch (IllegalArgumentException invalidCompartment) {
            return new Compartment(1, EnumSet.noneOf(StockAttributes.class));
        }
    }

    /**
     * Parses capacity text into a positive int, returning 0 if invalid.
     *
     * @param capacityText capacity text
     * @return parsed capacity (0 if invalid)
     */
    private int parseCapacityOrDefault(String capacityText) {
        try {
            return Integer.parseInt(capacityText);
        } catch (Exception parseError) {
            return 0;
        }
    }

    /**
     * Parses a semicolon-separated list of {@link StockAttributes} into an {@link EnumSet}.
     *
     * @param text the input text
     * @return parsed attributes as {@link EnumSet}; empty set if none
     */
    private EnumSet<StockAttributes> parseAttributes(String text) {
        EnumSet<StockAttributes> result = EnumSet.noneOf(StockAttributes.class);

        if (text == null) {
            return result;
        }

        String trimmed = text.trim();
        boolean hasContent = trimmed.length() > 0;
        if (!hasContent) {
            return result;
        }

        String[] tokens = trimmed.split(";");
        for (String token : tokens) {
            if (token == null) {
                continue;
            }
            String attributeName = token.trim();
            boolean hasName = attributeName.length() > 0;
            if (!hasName) {
                continue;
            }
            try {
                StockAttributes attribute = StockAttributes.valueOf(attributeName);
                result.add(attribute);
            } catch (IllegalArgumentException unknown) {
                // Unknown attribute names are ignored gracefully.
            }
        }

        return result;
    }

    /**
     * Parses an ISO-8601 {@link LocalDate} string, returning {@code null} if empty or invalid.
     *
     * @param text the input text
     * @return parsed {@link LocalDate} or {@code null}
     */
    private LocalDate parseDateOrNull(String text) {
        if (text == null) {
            return null;
        }

        String trimmed = text.trim();
        boolean hasContent = trimmed.length() > 0;
        if (!hasContent) {
            return null;
        }

        try {
            return LocalDate.parse(trimmed);
        } catch (Exception parseError) {
            return null;
        }
    }

    /**
     * Converts a CSV-escaped field back to a raw string by removing surrounding quotes
     * and unescaping doubled quotes.
     *
     * @param value the CSV field value (may be quoted)
     * @return raw string without CSV quoting
     */
    private String unescapeCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean startsWithQuote = value.startsWith("\"");
        boolean endsWithQuote = value.endsWith("\"");

        boolean isQuotedField = startsWithQuote && endsWithQuote;
        if (!isQuotedField) {
            return value;
        }

        String inner = value.substring(1, value.length() - 1);
        String restored = inner.replace("\"\"", "\"");
        return restored;
    }
}