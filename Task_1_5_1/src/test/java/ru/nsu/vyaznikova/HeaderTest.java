package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Header class.
 * These tests verify the functionality of creating and formatting Markdown headers
 * with different levels (1-6) and content combinations.
 */
public class HeaderTest {

    /**
     * Helper class for testing Header with simple text content.
     * This class provides a predictable Markdown output for testing purposes.
     */
    private static class TestElement extends Element {
        private final String content;

        public TestElement(String content) {
            this.content = content;
        }

        @Override
        public String toMarkdown() {
            return content;
        }
    }

    /**
     * Tests the creation of a basic level 1 header with a single content element.
     * Verifies that:
     * 1. The header level is correctly represented with a single '#'
     * 2. There is a space between '#' and content
     * 3. The content is correctly included in the output
     */
    @Test
    public void testBasicHeaderLevel1() {
        Header header = new Header.Builder()
            .setLevel(1)
            .addContent(new TestElement("Basic Header"))
            .build();

        assertEquals("# Basic Header", header.toMarkdown(),
            "Level 1 header should be prefixed with a single '#' and a space");
    }

    /**
     * Tests headers of all valid levels (1-6).
     * For each level, verifies:
     * 1. The correct number of '#' symbols is used
     * 2. Proper spacing between '#' symbols and content
     * 3. Content is preserved correctly
     */
    @Test
    public void testAllHeaderLevels() {
        String content = "Test Header";
        for (int level = 1; level <= 6; level++) {
            Header header = new Header.Builder()
                .setLevel(level)
                .addContent(new TestElement(content))
                .build();

            String expected = "#".repeat(level) + " " + content;
            assertEquals(expected, header.toMarkdown(),
                String.format("Level %d header should have %d '#' symbols", level, level));
        }
    }

    /**
     * Tests header with multiple content elements.
     * Verifies that:
     * 1. Multiple content elements are properly space-separated
     * 2. The order of elements is preserved
     * 3. The header level formatting is correct
     */
    @Test
    public void testHeaderWithMultipleContent() {
        Header header = new Header.Builder()
            .setLevel(2)
            .addContent(
                new TestElement("First"),
                new TestElement("Second"),
                new TestElement("Third")
            )
            .build();

        assertEquals("## First Second Third", header.toMarkdown(),
            "Multiple content elements should be space-separated");
    }

    /**
     * Tests validation of header levels.
     * Verifies that:
     * 1. Level 0 is rejected (below minimum)
     * 2. Level 7 is rejected (above maximum)
     * 3. Appropriate exception messages are provided
     */
    @Test
    public void testInvalidHeaderLevels() {
        Header.Builder builder = new Header.Builder();

        Exception tooLow = assertThrows(IllegalArgumentException.class,
            () -> builder.setLevel(0),
            "Should throw exception for level 0");
        assertTrue(tooLow.getMessage().contains("must be between 1 and 6"));

        Exception tooHigh = assertThrows(IllegalArgumentException.class,
            () -> builder.setLevel(7),
            "Should throw exception for level 7");
        assertTrue(tooHigh.getMessage().contains("must be between 1 and 6"));
    }

    /**
     * Tests header creation with empty or null content.
     * Verifies that:
     * 1. Building without content throws IllegalStateException
     * 2. Adding null content throws NullPointerException
     * 3. Adding null content array throws NullPointerException
     */
    @Test
    public void testInvalidContent() {
        Header.Builder builder = new Header.Builder();

        assertThrows(IllegalStateException.class,
            () -> builder.build(),
            "Should throw exception when building header without content");

        assertThrows(NullPointerException.class,
            () -> builder.addContent((Element) null),
            "Should throw exception when adding null element");

        assertThrows(NullPointerException.class,
            () -> builder.addContent((Element[]) null),
            "Should throw exception when adding null elements array");
    }

    /**
     * Tests equality comparison between headers.
     * Verifies that headers are equal when they have:
     * 1. The same level
     * 2. The same content in the same order
     * And headers are not equal when they have:
     * 1. Different levels
     * 2. Different content
     * 3. Same content in different order
     */
    @Test
    public void testHeaderEquality() {
        Header header1 = new Header.Builder()
            .setLevel(2)
            .addContent(new TestElement("Test Content"))
            .build();

        Header header2 = new Header.Builder()
            .setLevel(2)
            .addContent(new TestElement("Test Content"))
            .build();

        assertEquals(header1, header2,
            "Headers with same level and content should be equal");
        assertEquals(header1.hashCode(), header2.hashCode(),
            "Equal headers should have same hash code");

        Header differentLevel = new Header.Builder()
            .setLevel(3)
            .addContent(new TestElement("Test Content"))
            .build();

        assertNotEquals(header1, differentLevel,
            "Headers with different levels should not be equal");

        final Header differentContent = new Header.Builder()
            .setLevel(2)
            .addContent(new TestElement("Different Content"))
            .build();

        assertNotEquals(header1, differentContent,
            "Headers with different content should not be equal");
    }

    /**
     * Tests that default header level is 1 when not explicitly set.
     * Verifies that:
     * 1. Header created without setLevel() has level 1
     * 2. The Markdown output correctly shows level 1 formatting
     */
    @Test
    public void testDefaultHeaderLevel() {
        Header header = new Header.Builder()
            .addContent(new TestElement("Default Level"))
            .build();

        assertEquals("# Default Level", header.toMarkdown(),
            "Header should default to level 1 if not specified");
    }
}
