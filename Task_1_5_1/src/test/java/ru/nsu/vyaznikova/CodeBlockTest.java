package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the CodeBlock class.
 *
 * <p>These tests verify the functionality of code block creation and validation in Markdown format.
 * Tests include basic code blocks, language specification, and handling of invalid inputs.</p>
 */
public class CodeBlockTest {

    /**
     * Tests the creation of a basic code block with language.
     */
    @Test
    public void testBasicCodeBlock() {
        CodeBlock codeBlock = new CodeBlock.Builder()
            .setLanguage("java")
            .setCode("public class Test {}")
            .build();

        String expected = "```java\n"
            + "public class Test {}\n"
            + "```";
        assertEquals(expected, codeBlock.toMarkdown());
    }

    /**
     * Tests code block creation without language specification.
     * Verifies that:
     * 1. The code block works without a language
     * 2. No language identifier appears after the opening backticks
     * 3. The code content is still properly formatted
     */
    @Test
    public void testCodeBlockWithoutLanguage() {
        CodeBlock codeBlock = new CodeBlock.Builder()
            .setCode("print('Hello')")
            .build();

        assertEquals("```\nprint('Hello')\n```",
            codeBlock.toMarkdown(),
            "Code block without language should omit language identifier");
    }

    /**
     * Tests code block with empty language string.
     * Verifies that:
     * 1. Empty language string is handled the same as no language
     * 2. The formatting is correct without language identifier
     */
    @Test
    public void testCodeBlockWithEmptyLanguage() {
        CodeBlock codeBlock = new CodeBlock.Builder()
            .setLanguage("")
            .setCode("print('Hello')")
            .build();

        assertEquals("```\nprint('Hello')\n```",
            codeBlock.toMarkdown(),
            "Code block with empty language should omit language identifier");
    }

    /**
     * Tests handling of null values for both language and code.
     * Verifies that:
     * 1. Null language is handled gracefully
     * 2. Null code is converted to empty string
     * 3. The code block structure remains valid
     */
    @Test
    public void testCodeBlockWithNullValues() {
        CodeBlock codeBlock = new CodeBlock.Builder()
            .setLanguage(null)
            .setCode(null)
            .build();

        assertEquals("```\n\n```",
            codeBlock.toMarkdown(),
            "Code block should handle null values gracefully");
    }

    /**
     * Tests validation of language identifier format.
     * Verifies that:
     * 1. Valid language identifiers are accepted
     * 2. Invalid language identifiers throw IllegalArgumentException
     * 3. The exception message is appropriate
     */
    @Test
    public void testLanguageValidation() {
        // Valid language identifiers
        String[] validLanguages = {
            "java", "python3", "c++", "f#", "javascript", "typescript",
            "go", "rust", "ruby", "php", "html", "css", "shell",
            "kotlin", "scala", "swift", "r", "matlab", "perl"
        };

        for (String lang : validLanguages) {
            assertDoesNotThrow(() -> new CodeBlock.Builder().setLanguage(lang),
                "Should accept valid language identifier: " + lang);
        }

        // Invalid language identifiers
        String[] invalidLanguages = {
            "java script", // Contains space
            "c/c++",      // Contains slash
            "python!",    // Contains exclamation mark
            "ruby@",      // Contains at sign
            "php$"       // Contains dollar sign
        };

        for (String lang : invalidLanguages) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CodeBlock.Builder().setLanguage(lang),
                "Should reject invalid language identifier: " + lang);
            assertTrue(ex.getMessage().contains("Invalid language identifier"),
                "Exception message should mention invalid language identifier");
        }
    }

    /**
     * Tests escaping of special Markdown characters in code content.
     * Verifies that:
     * 1. Triple backticks within code are properly escaped
     * 2. Line endings are normalized
     * 3. The code block remains valid with escaped content
     */
    @Test
    public void testSpecialCharactersEscaping() {
        String codeWithBackticks = "```This is some code```\nwith multiple ```backticks```";
        CodeBlock codeBlock = new CodeBlock.Builder()
            .setLanguage("text")
            .setCode(codeWithBackticks)
            .build();

        String expected = "```text\n"
            + "\\`\\`\\`This is some code\\`\\`\\`\n"
            + "with multiple \\`\\`\\`backticks\\`\\`\\`\n"
            + "```";
        assertEquals(expected, codeBlock.toMarkdown(),
            "Code block should properly escape triple backticks");
    }

    /**
     * Tests handling of different line ending styles.
     * Verifies that:
     * 1. Windows-style line endings (CRLF) are normalized
     * 2. Old Mac-style line endings (CR) are normalized
     * 3. Unix-style line endings (LF) are preserved
     * 4. Mixed line endings are handled correctly
     */
    @Test
    public void testLineEndingNormalization() {
        String windowsStyle = "line1\r\nline2\r\nline3";
        String oldMacStyle = "line1\rline2\rline3";
        String unixStyle = "line1\nline2\nline3";

        CodeBlock windowsBlock = new CodeBlock.Builder().setCode(windowsStyle).build();
        CodeBlock oldMacBlock = new CodeBlock.Builder().setCode(oldMacStyle).build();
        CodeBlock unixBlock = new CodeBlock.Builder().setCode(unixStyle).build();

        String expected = "```\nline1\nline2\nline3\n```";
        assertEquals(expected, windowsBlock.toMarkdown(),
            "Windows-style line endings should be normalized");
        assertEquals(expected, oldMacBlock.toMarkdown(),
            "Old Mac-style line endings should be normalized");
        assertEquals(expected, unixBlock.toMarkdown(),
            "Unix-style line endings should be preserved");

        final String mixedStyle = "line1\r\nline2\rline3\nline4";
        final CodeBlock mixedBlock = new CodeBlock.Builder().setCode(mixedStyle).build();
        assertEquals("```\nline1\nline2\nline3\nline4\n```", mixedBlock.toMarkdown(),
            "Mixed line endings should be normalized");
    }
}
