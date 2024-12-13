package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Element class, which is a base class for Markdown elements.
 */
public class ElementTest {

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
     * Tests the equality method for Element objects.
     */
    @Test
    public void testElementEquality() {
        Element element1 = new TestElement("test");
        Element element2 = new TestElement("test");
        Element element3 = new TestElement("different");

        assertEquals(element1, element2, "Elements with the same content should be equal.");
        assertNotEquals(element1, element3, "Elements with different content should not be equal.");
    }

    /**
     * Tests the hashCode method for Element objects.
     */
    @Test
    public void testElementHashCode() {
        Element element1 = new TestElement("test");
        Element element2 = new TestElement("test");

        assertEquals(element1.hashCode(), element2.hashCode(), "Equal elements should have the same hash code.");
    }

    /**
     * Tests the toMarkdown method for Element objects.
     */
    @Test
    public void testToMarkdown() {
        Element element = new TestElement("test");
        assertEquals("test", element.toMarkdown(), "toMarkdown should return the correct Markdown representation.");
    }
}
