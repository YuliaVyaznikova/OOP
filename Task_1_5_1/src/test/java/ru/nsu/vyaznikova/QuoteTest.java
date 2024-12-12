package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Unit tests for the Quote class, which represents a Markdown quote element.
 */
public class QuoteTest {

    /**
     * Tests the creation of a single-line quote and its Markdown serialization.
     */
    @Test
    public void testSingleLineQuote() {
        Quote quote = new Quote.Builder().addContent(new Text.Builder().setContent("This is a single line quote.").build()).build();
        assertEquals("&gt; This is a single line quote.", quote.toMarkdown(), "Single line quote should be prefixed with '>' in Markdown.");
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
        assertEquals("&gt; Line 1\n&gt; Line 2", quote.toMarkdown(), "Each line of a multi-line quote should be prefixed with '>' in Markdown.");
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
}
