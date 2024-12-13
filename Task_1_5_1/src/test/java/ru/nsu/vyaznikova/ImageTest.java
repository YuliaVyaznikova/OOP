package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Image class.
 * These tests verify the functionality of creating and formatting Markdown images
 * with different combinations of alt text and URLs.
 */
public class ImageTest {

    /**
     * Tests the creation of a basic image with both alt text and URL.
     * Verifies that:
     * 1. The image is correctly formatted with alt text in square brackets
     * 2. The URL is correctly enclosed in parentheses
     * 3. The exclamation mark is present at the start
     */
    @Test
    public void testBasicImage() {
        Image image = new Image.Builder()
            .setAlt("Java Documentation")
            .setUrl("https://docs.oracle.com/en/java/")
            .build();

        assertEquals("![Java Documentation](https://docs.oracle.com/en/java/)",
            image.toMarkdown(),
            "Basic image should be formatted as ![alt](url)");
    }

    /**
     * Tests the creation of an image without alt text.
     * Verifies that:
     * 1. The image is correctly formatted with empty alt text
     * 2. The square brackets are still present but empty
     * 3. The URL is correctly formatted
     */
    @Test
    public void testImageWithoutAlt() {
        Image image = new Image.Builder()
            .setUrl("https://docs.oracle.com/en/java/")
            .build();

        assertEquals("![](https://docs.oracle.com/en/java/)",
            image.toMarkdown(),
            "Image without alt text should be formatted as ![](url)");
    }

    /**
     * Tests the creation of an image with explicit null alt text.
     * Verifies that:
     * 1. Null alt text is handled gracefully
     * 2. The result is the same as not setting alt text
     */
    @Test
    public void testImageWithNullAlt() {
        Image image = new Image.Builder()
            .setAlt(null)
            .setUrl("https://docs.oracle.com/en/java/")
            .build();

        assertEquals("![](https://docs.oracle.com/en/java/)",
            image.toMarkdown(),
            "Image with null alt text should be formatted as ![](url)");
    }

    /**
     * Tests validation of required URL field.
     * Verifies that:
     * 1. Missing URL throws IllegalStateException
     * 2. The exception message mentions URL requirement
     */
    @Test
    public void testRequiredUrl() {
        Image.Builder builder = new Image.Builder();

        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> builder.build(),
            "Should throw exception when URL is not set");

        assertTrue(ex.getMessage().contains("URL must be set"),
            "Exception message should mention URL requirement");
    }

    /**
     * Tests URL validation.
     * Verifies that:
     * 1. Null URL throws NullPointerException
     * 2. The exception message mentions URL requirement
     */
    @Test
    public void testNullUrl() {
        Image.Builder builder = new Image.Builder();

        NullPointerException ex = assertThrows(NullPointerException.class,
            () -> builder.setUrl(null),
            "Should throw exception when URL is null");

        assertTrue(ex.getMessage().contains("URL cannot be null"),
            "Exception message should mention null URL");
    }

    /**
     * Tests that empty alt text is preserved.
     * Verifies that:
     * 1. Empty string alt text remains empty in output
     * 2. The formatting is correct with empty alt text
     */
    @Test
    public void testEmptyAltText() {
        Image image = new Image.Builder()
            .setAlt("")
            .setUrl("https://docs.oracle.com/en/java/")
            .build();

        assertEquals("![](https://docs.oracle.com/en/java/)",
            image.toMarkdown(),
            "Image with empty alt text should be formatted as ![](url)");
    }

    /**
     * Tests that special characters in alt text are preserved.
     * Verifies that:
     * 1. Alt text with special characters is preserved exactly
     * 2. The formatting remains correct
     */
    @Test
    public void testSpecialCharactersInAlt() {
        String altWithSpecialChars = "Java & Documentation: [Version 21]";
        Image image = new Image.Builder()
            .setAlt(altWithSpecialChars)
            .setUrl("https://docs.oracle.com/en/java/")
            .build();

        assertEquals("![" + altWithSpecialChars + "](https://docs.oracle.com/en/java/)",
            image.toMarkdown(),
            "Image with special characters in alt text should preserve those characters");
    }
}
