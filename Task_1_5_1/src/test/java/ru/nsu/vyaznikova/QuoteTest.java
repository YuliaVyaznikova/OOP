package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the Quote class.
 *
 * <p>These tests verify the functionality of quote creation and validation in Markdown format.
 * Tests include basic quotes, nested quotes, and handling of invalid inputs.</p>
 */
public class QuoteTest {

    /**
     * Tests the creation of a single-line quote and its Markdown serialization.
     */
    @Test
    public void testSingleLineQuote() {
        Quote quote = new Quote.Builder().addContent(new Text.Builder().setContent("This is a single line quote.").build()).build();
        assertEquals("> This is a single line quote.", quote.toMarkdown(), "Single line quote should be prefixed with '>' in Markdown.");
    }

    /**
     * Tests the creation of a multi-line quote and its Markdown serialization.
     */
    @Test
    public void testMultiLineQuote() {
        Quote quote = new Quote.Builder()
            .addContent(new Text.Builder().setContent("Line 1").build())
            .addContent(new Text.Builder().setContent("Line 2").build())
            .build();
        assertEquals("> Line 1\n> Line 2", quote.toMarkdown(), "Each line of a multi-line quote should be prefixed with '>' in Markdown.");
    }

    /**
     * Tests the equality method for Quote objects.
     */
    @Test
    public void testQuoteEquality() {
        Quote quote1 = new Quote.Builder().addContent(new Text.Builder().setContent("Equal quote").build()).build();
        Quote quote2 = new Quote.Builder().addContent(new Text.Builder().setContent("Equal quote").build()).build();
        Quote quote3 = new Quote.Builder().addContent(new Text.Builder().setContent("Different quote").build()).build();

        assertEquals(quote1, quote2, "Quotes with the same content should be equal.");
        assertNotEquals(quote1, quote3, "Quotes with different content should not be equal.");
    }

    /**
     * Tests that creating a Quote with null content throws an exception.
     */
    @Test
    public void testNullContent() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Quote.Builder().addContent((Element) null).build();
        }, "Creating a Quote with null content should throw IllegalArgumentException.");
    }

    /**
     * Tests that creating a Quote with an empty content throws an exception.
     */
    @Test
    public void testEmptyContent() {
        assertThrows(IllegalStateException.class, () -> {
            new Quote.Builder().build();
        }, "Creating a Quote with empty content should throw IllegalStateException.");
    }

    /**
     * Tests the creation of a quote with nested formatted text elements.
     */
    @Test
    public void testNestedFormattedText() {
        Quote quote = new Quote.Builder()
            .addContent(new Text.Builder().setContent("Bold").setBold(true).build())
            .addContent(new Text.Builder().setContent("Italic").setItalic(true).build())
            .build();
        assertEquals("> **Bold**\n> *Italic*", quote.toMarkdown(), "Nested formatted text should be correctly serialized in Markdown.");
    }
}
