package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Lst class.
 * 
 * <p>These tests verify the functionality of list creation and validation in Markdown format.
 * Tests include basic list creation, nested lists, and handling of invalid inputs.</p>
 */
public class LstTest {

    /**
     * Helper class for testing Lst with simple text content.
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
     * Tests the creation of a basic unordered list.
     */
    @Test
    public void testBasicUnorderedList() {
        Lst list = new Lst.Builder(false)
            .addItem(new TestElement("First item"))
            .addItem(new TestElement("Second item"))
            .addItem(new TestElement("Third item"))
            .build();

        String expected = "- First item\n" +
                         "- Second item\n" +
                         "- Third item";
        assertEquals(expected, list.toMarkdown(),
            "Unordered list should be formatted with dashes");
    }

    /**
     * Tests the creation of a basic ordered list.
     */
    @Test
    public void testBasicOrderedList() {
        Lst list = new Lst.Builder(true)
            .addItem(new TestElement("First item"))
            .addItem(new TestElement("Second item"))
            .addItem(new TestElement("Third item"))
            .build();

        String expected = "1. First item\n" +
                         "2. Second item\n" +
                         "3. Third item";
        assertEquals(expected, list.toMarkdown(),
            "Ordered list should be formatted with numbers");
    }

    /**
     * Tests nested unordered lists.
     * Verifies that:
     * 1. Nested lists are properly indented
     * 2. Parent-child relationships are preserved
     * 3. Indentation is consistent
     */
    @Test
    public void testNestedUnorderedLists() {
        Lst nestedList = new Lst.Builder(false)
            .addItem(new TestElement("Nested item 1"))
            .addItem(new TestElement("Nested item 2"))
            .build();

        Lst mainList = new Lst.Builder(false)
            .addItem(new TestElement("Main item 1"))
            .addNestedList(nestedList)
            .addItem(new TestElement("Main item 2"))
            .build();

        String expected = "- Main item 1\n" +
                         "  - Nested item 1\n" +
                         "  - Nested item 2\n" +
                         "- Main item 2";
        assertEquals(expected, mainList.toMarkdown(),
            "Nested unordered lists should be properly indented");
    }

    /**
     * Tests nested ordered lists.
     * Verifies that:
     * 1. Both parent and nested lists are properly numbered
     * 2. Indentation is correct
     * 3. Numbering restarts for each nested list
     */
    @Test
    public void testNestedOrderedLists() {
        Lst nestedList = new Lst.Builder(true)
            .addItem(new TestElement("Nested item 1"))
            .addItem(new TestElement("Nested item 2"))
            .build();

        Lst mainList = new Lst.Builder(true)
            .addItem(new TestElement("Main item 1"))
            .addNestedList(nestedList)
            .addItem(new TestElement("Main item 2"))
            .build();

        String expected = "1. Main item 1\n" +
                         "  1. Nested item 1\n" +
                         "  2. Nested item 2\n" +
                         "2. Main item 2";
        assertEquals(expected, mainList.toMarkdown(),
            "Nested ordered lists should maintain proper numbering");
    }

    /**
     * Tests mixing ordered and unordered nested lists.
     * Verifies that:
     * 1. Different list types can be nested
     * 2. Each list maintains its proper formatting
     * 3. Indentation is correct
     */
    @Test
    public void testMixedNestedLists() {
        Lst nestedOrdered = new Lst.Builder(true)
            .addItem(new TestElement("Ordered nested 1"))
            .addItem(new TestElement("Ordered nested 2"))
            .build();

        Lst mainUnordered = new Lst.Builder(false)
            .addItem(new TestElement("Unordered main 1"))
            .addNestedList(nestedOrdered)
            .addItem(new TestElement("Unordered main 2"))
            .build();

        String expected = "- Unordered main 1\n" +
                         "  1. Ordered nested 1\n" +
                         "  2. Ordered nested 2\n" +
                         "- Unordered main 2";
        assertEquals(expected, mainUnordered.toMarkdown(),
            "Mixed nested lists should maintain their respective formats");
    }

    /**
     * Tests validation of required items.
     * Verifies that:
     * 1. Empty list throws IllegalStateException
     * 2. The exception message is appropriate
     */
    @Test
    public void testRequiredItems() {
        Lst.Builder builder = new Lst.Builder(false);
        
        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> builder.build(),
            "Should throw exception when list is empty");
        
        assertTrue(ex.getMessage().contains("must contain at least one item"),
            "Exception message should mention item requirement");
    }

    /**
     * Tests null validation in list methods.
     * Verifies that:
     * 1. Null item content throws NullPointerException
     * 2. Null nested list throws NullPointerException
     * 3. Adding nested list without parent item throws IllegalStateException
     */
    @Test
    public void testNullValidation() {
        Lst.Builder builder = new Lst.Builder(false);

        // Test null item content
        assertThrows(NullPointerException.class,
            () -> builder.addItem(null),
            "Should throw exception when adding null item content");

        // Test null nested list
        builder.addItem(new TestElement("Parent item"));
        assertThrows(NullPointerException.class,
            () -> builder.addNestedList(null),
            "Should throw exception when adding null nested list");

        // Test adding nested list without parent item
        Lst.Builder newBuilder = new Lst.Builder(false);
        Lst validList = new Lst.Builder(false)
            .addItem(new TestElement("Valid item"))
            .build();
        
        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> newBuilder.addNestedList(validList),
            "Should throw exception when adding nested list without parent item");
        
        assertTrue(ex.getMessage().contains("Cannot add nested list without a parent item"),
            "Exception message should mention missing parent item");
    }

    /**
     * Tests deeply nested lists.
     * Verifies that:
     * 1. Multiple levels of nesting work correctly
     * 2. Indentation increases properly with each level
     * 3. Each level maintains its formatting
     */
    @Test
    public void testDeeplyNestedLists() {
        // Create level 3 (innermost)
        Lst level3 = new Lst.Builder(true)
            .addItem(new TestElement("Level 3 item"))
            .build();

        // Create level 2
        Lst level2 = new Lst.Builder(false)
            .addItem(new TestElement("Level 2 item"))
            .addNestedList(level3)
            .build();

        // Create level 1 (outermost)
        Lst level1 = new Lst.Builder(true)
            .addItem(new TestElement("Level 1 item"))
            .addNestedList(level2)
            .build();

        String expected = "1. Level 1 item\n" +
                         "  - Level 2 item\n" +
                         "    1. Level 3 item";
        assertEquals(expected, level1.toMarkdown(),
            "Deeply nested lists should maintain proper indentation and formatting");
    }
}
