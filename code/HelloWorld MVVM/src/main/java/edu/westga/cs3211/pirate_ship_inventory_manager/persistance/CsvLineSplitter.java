package edu.westga.cs3211.pirate_ship_inventory_manager.persistance;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits a single CSV line into columns while honoring:
 * 	Quoted fields
 *  Escaped quotes inside quoted fields (two quotes become one)
 *  Commas as separators only when outside quotes
 *  
 *  @author Yeni
 *  @version Fall 2025
 */
public final class CsvLineSplitter {

    /**
     * Utility class; prevent instantiation.
     */
    private CsvLineSplitter() {
        // no-op
    }

    /**
     * Splits a CSV line into columns while honoring quotes and escaped quotes.
     *
     * @param line the raw CSV line (may be {@code null})
     * @return ordered list of column strings (untrimmed); never {@code null}
     */
    public static List<String> split(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inQuotes = false;
        int index = 0;

        int length;
        if (line == null) {
            length = 0;
        } else {
            length = line.length();
        }

        while (index < length) {
            char ch = line.charAt(index);

            if (inQuotes) {
                InQuotesOutcome outcome = processCharInQuotes(line, length, index, ch, current);
                if (outcome.hasConsumedExtraChar()) {
                    index = index + 1;
                }
                if (outcome.shouldToggleQuotedState()) {
                    inQuotes = false;
                }
            } else {
                OutQuotesOutcome outcome = processCharOutOfQuotes(ch, current, columns);
                if (outcome.shouldEnterQuotedMode()) {
                    inQuotes = true;
                }
            }

            index = index + 1;
        }

        columns.add(current.toString());
        return columns;
    }

    /**
     * Outcome for handling a character while in quoted mode.
     * Encapsulates flags to satisfy Checkstyle's accessor rules.
     */
    private static final class InQuotesOutcome {
        private final boolean consumedExtraChar;
        private final boolean toggleQuotedState;

        /**
         * Constructs an outcome for in-quote processing.
         *
         * @param consumedExtraChar {@code true} if the next character was consumed (escaped quote)
         * @param toggleQuotedState {@code true} if the caller should toggle quoted state (closing quote)
         */
        InQuotesOutcome(boolean consumedExtraChar, boolean toggleQuotedState) {
            this.consumedExtraChar = consumedExtraChar;
            this.toggleQuotedState = toggleQuotedState;
        }

        /**
         * Indicates whether an extra character was consumed (escaped quote).
         *
         * @return {@code true} if the next character was consumed
         */
        boolean hasConsumedExtraChar() {
            return this.consumedExtraChar;
        }

        /**
         * Indicates whether the caller should toggle quoted state (closing quote).
         *
         * @return {@code true} if quoted state should be toggled
         */
        boolean shouldToggleQuotedState() {
            return this.toggleQuotedState;
        }
    }

    /**
     * Outcome for handling a character while outside quotes.
     * Encapsulates flags to satisfy Checkstyle's accessor rules.
     */
    private static final class OutQuotesOutcome {
        private final boolean enterQuotedMode;

        /**
         * Constructs an outcome for out-of-quote processing.
         *
         * @param enterQuotedMode {@code true} if the parser should enter quoted mode
         */
        OutQuotesOutcome(boolean enterQuotedMode) {
            this.enterQuotedMode = enterQuotedMode;
        }

        /**
         * Indicates whether the parser should enter quoted mode.
         *
         * @return {@code true} if entering quoted mode
         */
        boolean shouldEnterQuotedMode() {
            return this.enterQuotedMode;
        }
    }

    /**
     * Processes one character while inside a quoted CSV field with flat branching.
     * <ul>
     *   <li>Normal char: append to {@code current}</li>
     *   <li>Escaped quote ({@code \"\"}): append one quote and mark extra char as consumed</li>
     *   <li>Closing quote: signal caller to toggle quoted state</li>
     * </ul>
     *
     * @param line the full CSV line
     * @param length the line length
     * @param index the current index
     * @param ch the current character
     * @param current the current field buffer
     * @return an {@link InQuotesOutcome} indicating what happened
     */
    private static InQuotesOutcome processCharInQuotes(
            String line,
            int length,
            int index,
            char ch,
            StringBuilder current) {

        boolean isQuoteChar = (ch == '\"');
        if (!isQuoteChar) {
            current.append(ch);
            return new InQuotesOutcome(false, false);
        }

        boolean hasNext = (index + 1) < length;
        boolean isEscapedQuote = hasNext && line.charAt(index + 1) == '\"';
        if (isEscapedQuote) {
            current.append('\"');
            return new InQuotesOutcome(true, false);
        }

        return new InQuotesOutcome(false, true);
    }

    /**
     * Processes one character while outside quotes with flat branching.
     * <ul>
     *   <li>Opening quote: signal entering quoted mode</li>
     *   <li>Comma: end the current column</li>
     *   <li>Otherwise: append to {@code current}</li>
     * </ul>
     *
     * @param ch the current character
     * @param current the current field buffer
     * @param columns the list of parsed columns
     * @return an {@link OutQuotesOutcome} indicating whether we entered quoted mode
     */
    private static OutQuotesOutcome processCharOutOfQuotes(
            char ch,
            StringBuilder current,
            List<String> columns) {

        boolean isQuoteChar = (ch == '\"');
        if (isQuoteChar) {
            return new OutQuotesOutcome(true);
        }

        boolean isComma = (ch == ',');
        if (isComma) {
            columns.add(current.toString());
            current.setLength(0);
            return new OutQuotesOutcome(false);
        }

        current.append(ch);
        return new OutQuotesOutcome(false);
    }
}