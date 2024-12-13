package ru.nsu.vyaznikova;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a Markdown code block with language specification and proper escaping.
 * Examples:
 * - Simple code block:
 * ```java
 * System.out.println("Hello");
 * ```
 * - Code block with special characters:
 * ```python
 * print("```Special```")
 * ```
 */
public class CodeBlock extends Element {
    private final String code;
    private final String language;

    // Common programming language identifiers pattern
    private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^[a-zA-Z0-9+#._-]+$");

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the Builder instance containing code block configuration
     */
    private CodeBlock(Builder builder) {
        this.code = builder.code;
        this.language = builder.language;
    }

    /**
     * Escapes special Markdown characters in code.
     * Handles cases where code contains backticks or other Markdown symbols.
     *
     * @param code the code to escape
     * @return escaped code string
     */
    private static String escapeCode(String code) {
        if (code == null) {
            return "";
        }

        return code
                // Escape any existing fence patterns
                .replace("```", "\\`\\`\\`")
                // Preserve line endings
                .replace("\r\n", "\n")
                .replace("\r", "\n");
    }

    /**
     * Converts the code block to its Markdown representation.
     * Properly escapes special characters and maintains formatting.
     *
     * @return A string containing the Markdown representation of the code block
     */
    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder("```");

        if (language != null && !language.isEmpty()) {
            sb.append(language);
        }

        sb.append("\n")
                .append(escapeCode(code))
                .append("\n```");

        return sb.toString();
    }

    /**
     * Builder class for creating CodeBlock instances.
     * Supports setting programming language and code content.
     */
    public static class Builder {
        private String code = "";
        private String language = "";

        /**
         * Sets the programming language for syntax highlighting.
         *
         * @param language the programming language identifier
         * @return this builder instance
         * @throws IllegalArgumentException if language contains invalid characters
         */
        public Builder setLanguage(String language) {
            if (language != null && !language.isEmpty() 
                    && !LANGUAGE_PATTERN.matcher(language).matches()) {
                throw new IllegalArgumentException("Invalid language identifier format");
            }
            this.language = language != null ? language : "";
            return this;
        }

        /**
         * Sets the code content.
         *
         * @param code the code content
         * @return this builder instance
         */
        public Builder setCode(String code) {
            this.code = code != null ? code : "";
            return this;
        }

        /**
         * Builds the CodeBlock instance.
         *
         * @return a new CodeBlock instance
         */
        public CodeBlock build() {
            return new CodeBlock(this);
        }
    }
}