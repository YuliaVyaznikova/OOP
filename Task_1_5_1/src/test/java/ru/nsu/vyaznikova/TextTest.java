package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Text class.
 *
 * <p>These tests verify the functionality of text creation and formatting in Markdown format.
 * Tests include basic text, bold/italic formatting, and handling of invalid inputs.</p>
 */
public class TextTest {

    /**
     * Tests the creation of basic text without formatting.
     */
    @Test
    public void testBasicText() {
        Text text = new Text.Builder()
            .setContent("Simple text")
            .build();

        assertEquals("Simple text", text.toMarkdown());
    }

    /**
     * Tests text with bold formatting.
     */
    @Test
    public void testBoldText() {
        Text text = new Text.Builder()
            .setContent("Bold text")
            .setBold(true)
            .build();

        assertEquals("**Bold text**", text.toMarkdown());
    }

    /**
     * Tests the creation of italic text and its Markdown serialization.
     */
    @Test
    public void testItalicText() {
        Text italicText = new Text.Builder()
            .setContent("Italic Text")
            .setItalic(true)
            .build();
        assertEquals("*Italic Text*", italicText.toMarkdown(),
            "Italic text should be wrapped with single asterisks.");
    }

    /**
     * Tests the creation of strikethrough text and its Markdown serialization.
     */
    @Test
    public void testStrikethroughText() {
        Text strikethroughText = new Text.Builder()
            .setContent("Strikethrough Text")
            .setStrikethrough(true)
            .build();
        assertEquals("~~Strikethrough Text~~", strikethroughText.toMarkdown(),
            "Strikethrough text should be wrapped with double tildes.");
    }

    /**
     * Tests the creation of inline code text and its Markdown serialization.
     */
    @Test
    public void testCodeText() {
        Text codeText = new Text.Builder()
            .setContent("Code Text")
            .setCode(true)
            .build();
        assertEquals("`Code Text`", codeText.toMarkdown(),
            "Code text should be wrapped with backticks.");
    }

    /**
     * Tests the equality method for Text objects.
     */
    @Test
    public void testTextEquality() {
        Text text1 = new Text.Builder()
            .setContent("Equal Text")
            .setBold(true)
            .build();
        Text text2 = new Text.Builder()
            .setContent("Equal Text")
            .setBold(true)
            .build();
        Text text3 = new Text.Builder()
            .setContent("Different Text")
            .setBold(true)
            .build();

        assertEquals(text1, text2,
            "Text objects with the same content and formatting should be equal.");
        assertNotEquals(text1, text3, "Text objects with different content should not be equal.");
    }
}
