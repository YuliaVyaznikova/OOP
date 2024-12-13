package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for the Link class.
 * 
 * <p>These tests verify the functionality of link creation and validation in Markdown format.
 * Tests include basic link creation, links with titles, and handling of invalid inputs.</p>
 */
public class LinkTest {

    /**
     * Helper class for testing Link with simple text content.
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
     * Tests the creation of a basic link with text and URL.
     */
    @Test
    public void testBasicLink() {
        Link link = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/")
            .build();

        assertEquals("[Java Documentation](https://docs.oracle.com/en/java/)", link.toMarkdown(),
            "Basic link should be formatted as [text](url)");
    }

    /**
     * Tests the creation of a link with a title.
     */
    @Test
    public void testLinkWithTitle() {
        Link link = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/")
            .setTitle("Official Java Documentation")
            .build();

        assertEquals("[Java Documentation](https://docs.oracle.com/en/java/ \"Official Java Documentation\")", 
            link.toMarkdown(),
            "Link with title should be formatted as [text](url \"title\")");
    }

    /**
     * Tests link creation with various valid URL formats.
     * Verifies that the builder accepts:
     * 1. Main documentation URL
     * 2. Specific section URLs
     * 3. URLs with different paths
     */
    @Test
    public void testValidUrls() {
        String[] validUrls = {
            "https://docs.oracle.com/en/java/",
            "https://docs.oracle.com/en/java/javase/",
            "https://docs.oracle.com/en/java/javase/21/",
            "https://docs.oracle.com/en/java/javase/21/docs/api/"
        };

        for (String url : validUrls) {
            Link link = new Link.Builder()
                .setText(new TestElement("Java Docs"))
                .setUrl(url)
                .build();

            assertTrue(link.toMarkdown().contains(url),
                "Link should contain the valid URL: " + url);
        }
    }

    /**
     * Tests validation of required fields.
     * Verifies that:
     * 1. Missing text throws IllegalStateException
     * 2. Missing URL throws IllegalStateException
     * 3. Null text throws NullPointerException
     * 4. Null URL throws NullPointerException
     */
    @Test
    public void testRequiredFields() {
        Link.Builder builder = new Link.Builder();

        // Test building without text
        IllegalStateException noTextEx = assertThrows(IllegalStateException.class,
            () -> new Link.Builder()
                .setUrl("https://docs.oracle.com/en/java/")
                .build(),
            "Should throw exception when text is not set");
        assertTrue(noTextEx.getMessage().contains("text must be set"),
            "Exception message should mention missing text");

        // Test building without URL
        IllegalStateException noUrlEx = assertThrows(IllegalStateException.class,
            () -> new Link.Builder()
                .setText(new TestElement("Java Docs"))
                .build(),
            "Should throw exception when URL is not set");
        assertTrue(noUrlEx.getMessage().contains("URL must be set"),
            "Exception message should mention missing URL");

        // Test null text
        NullPointerException nullTextEx = assertThrows(NullPointerException.class,
            () -> builder.setText(null),
            "Should throw exception when text is null");
        assertTrue(nullTextEx.getMessage().contains("text cannot be null"),
            "Exception message should mention null text");

        // Test null URL
        NullPointerException nullUrlEx = assertThrows(NullPointerException.class,
            () -> builder.setUrl(null),
            "Should throw exception when URL is null");
        assertTrue(nullUrlEx.getMessage().contains("URL cannot be null"),
            "Exception message should mention null URL");
    }

    /**
     * Tests that optional title can be null.
     * Verifies that:
     * 1. Null title is handled gracefully
     * 2. Link is formatted correctly without a title
     * 3. Setting title to null after setting it to a value works correctly
     */
    @Test
    public void testOptionalTitle() {
        Link.Builder builder = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/");

        Link linkWithoutTitle = builder.build();
        Link linkWithNullTitle = builder.setTitle(null).build();
        Link linkWithTitle = builder.setTitle("Official Java Docs").build();
        Link linkWithTitleSetToNull = builder.setTitle(null).build();

        assertEquals("[Java Documentation](https://docs.oracle.com/en/java/)", 
            linkWithoutTitle.toMarkdown(),
            "Link without title should not include title section");
        assertEquals("[Java Documentation](https://docs.oracle.com/en/java/)", 
            linkWithNullTitle.toMarkdown(),
            "Link with null title should not include title section");
        assertEquals("[Java Documentation](https://docs.oracle.com/en/java/ \"Official Java Docs\")", 
            linkWithTitle.toMarkdown(),
            "Link with title should include title section");
        assertEquals("[Java Documentation](https://docs.oracle.com/en/java/)", 
            linkWithTitleSetToNull.toMarkdown(),
            "Link with title set back to null should not include title section");
    }

    /**
     * Tests link equality comparison.
     * Verifies that links are equal when they have:
     * 1. The same text
     * 2. The same URL
     * 3. The same title (or both have no title)
     * And links are not equal when they have different:
     * 1. Text
     * 2. URL
     * 3. Title
     */
    @Test
    public void testLinkEquality() {
        Link link1 = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/")
            .setTitle("Official Java Documentation")
            .build();

        Link link2 = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/")
            .setTitle("Official Java Documentation")
            .build();

        Link differentText = new Link.Builder()
            .setText(new TestElement("Different Text"))
            .setUrl("https://docs.oracle.com/en/java/")
            .setTitle("Official Java Documentation")
            .build();

        Link differentUrl = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/javase/21/")
            .setTitle("Official Java Documentation")
            .build();

        Link differentTitle = new Link.Builder()
            .setText(new TestElement("Java Documentation"))
            .setUrl("https://docs.oracle.com/en/java/")
            .setTitle("Different Title")
            .build();

        assertEquals(link1, link2,
            "Links with same text, URL, and title should be equal");
        assertEquals(link1.hashCode(), link2.hashCode(),
            "Equal links should have same hash code");

        assertNotEquals(link1, differentText,
            "Links with different text should not be equal");
        assertNotEquals(link1, differentUrl,
            "Links with different URLs should not be equal");
        assertNotEquals(link1, differentTitle,
            "Links with different titles should not be equal");
    }
}
