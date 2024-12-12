package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Text class, which represents plain text with various formatting options in Markdown.
 */
public class TextTest {

    /**
     * Tests the creation of bold text and its Markdown serialization.
     */
    @Test
    public void testBoldText() {
        Text boldText = Text.Bold("Bold Text");
        assertEquals("**Bold Text**", boldText.toMarkdown(), "Bold text should be wrapped with double asterisks.");
    }

    /**
     * Tests the creation of italic text and its Markdown serialization.
     */
    @Test
    public void testItalicText() {
        Text italicText = new Text.Builder().setContent("Italic Text").setItalic(true).build();
        assertEquals("*Italic Text*", italicText.toMarkdown(), "Italic text should be wrapped with single asterisks.");
    }

    /**
     * Tests the creation of strikethrough text and its Markdown serialization.
     */
    @Test
    public void testStrikethroughText() {
        Text strikethroughText = new Text.Builder().setContent("Strikethrough Text").setStrikethrough(true).build();
        assertEquals("~~Strikethrough Text~~", strikethroughText.toMarkdown(), "Strikethrough text should be wrapped with double tildes.");
    }

    /**
     * Tests the creation of inline code text and its Markdown serialization.
     */
    @Test
    public void testCodeText() {
        Text codeText = new Text.Builder().setContent("Code Text").setCode(true).build();
        assertEquals("`Code Text`", codeText.toMarkdown(), "Code text should be wrapped with backticks.");
    }

    /**
     * Tests the equality method for Text objects.
     */
    @Test
    public void testTextEquality() {
        Text text1 = new Text.Builder().setContent("Equal Text").setBold(true).build();
        Text text2 = new Text.Builder().setContent("Equal Text").setBold(true).build();
        Text text3 = new Text.Builder().setContent("Different Text").setBold(true).build();

        assertEquals(text1, text2, "Text objects with the same content and formatting should be equal.");
        assertNotEquals(text1, text3, "Text objects with different content should not be equal.");
    }
}
