package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Element class, which is a base class for Markdown elements.
 */
public class ElementTest {

    /**
     * Tests the equality method for Element objects.
     */
    @Test
    public void testElementEquality() {
        Element element1 = new Element() {
            @Override
            public String toMarkdown() {
                return "Element1";
            }
        };
        Element element2 = new Element() {
            @Override
            public String toMarkdown() {
                return "Element1";
            }
        };
        Element element3 = new Element() {
            @Override
            public String toMarkdown() {
                return "Element3";
            }
        };

        assertEquals(element1, element2, "Elements with the same content should be equal.");
        assertNotEquals(element1, element3, "Elements with different content should not be equal.");
    }

    /**
     * Tests the hashCode method for Element objects.
     */
    @Test
    public void testElementHashCode() {
        Element element1 = new Element() {
            @Override
            public String toMarkdown() {
                return "Element1";
            }
        };
        Element element2 = new Element() {
            @Override
            public String toMarkdown() {
                return "Element1";
            }
        };

        assertEquals(element1.hashCode(), element2.hashCode(), "Equal elements should have the same hash code.");
    }

    /**
     * Tests the toMarkdown method for Element objects.
     */
    @Test
    public void testToMarkdown() {
        Element element = new Element() {
            @Override
            public String toMarkdown() {
                return "ElementMarkdown";
            }
        };

        assertEquals("ElementMarkdown", element.toMarkdown(), "toMarkdown should return the correct Markdown representation.");
    }
}
