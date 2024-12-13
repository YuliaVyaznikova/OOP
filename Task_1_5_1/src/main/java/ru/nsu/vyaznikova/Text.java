package ru.nsu.vyaznikova;

import java.util.Objects;

/**
 * Represents plain text and its formatting variations in Markdown.
 */
public class Text extends Element {
    private final String content;
    private final boolean isBold;
    private final boolean isItalic;
    private final boolean isStrikethrough;
    private final boolean isCode;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the Builder instance containing text configuration
     */
    private Text(Builder builder) {
        this.content = builder.content;
        this.isBold = builder.isBold;
        this.isItalic = builder.isItalic;
        this.isStrikethrough = builder.isStrikethrough;
        this.isCode = builder.isCode;
    }

    /**
     * Converts the text to its Markdown representation.
     * Applies formatting in the order: code, bold, italic, strikethrough
     *
     * @return A string containing the Markdown representation of the text
     */
    @Override
    public String toMarkdown() {
        String result = content;

        if (isCode) {
            result = "`" + result + "`";
        }
        if (isBold) {
            result = "**" + result + "**";
        }
        if (isItalic) {
            result = "*" + result + "*";
        }
        if (isStrikethrough) {
            result = "~~" + result + "~~";
        }

        return result;
    }

    /**
     * Static method to create bold text directly.
     * This is provided for compatibility with the example code.
     *
     * @param content the text content
     * @return a new Text instance with bold formatting
     */
    public static Text bold(String content) {
        return new Builder()
                .setContent(content)
                .setBold(true)
                .build();
    }

    /**
     * Builder class for creating Text instances.
     * Supports setting content and various text formatting options.
     */
    public static class Builder {
        private String content = "";
        private boolean isBold = false;
        private boolean isItalic = false;
        private boolean isStrikethrough = false;
        private boolean isCode = false;

        /**
         * Sets the text content.
         *
         * @param content the text content
         * @return this builder instance
         * @throws IllegalArgumentException if content is null
         */
        public Builder setContent(String content) {
            this.content = Objects.requireNonNull(content, "Content cannot be null");
            return this;
        }

        /**
         * Sets whether the text should be bold.
         *
         * @param bold true for bold text
         * @return this builder instance
         */
        public Builder setBold(boolean bold) {
            this.isBold = bold;
            return this;
        }

        /**
         * Sets whether the text should be italic.
         *
         * @param italic true for italic text
         * @return this builder instance
         */
        public Builder setItalic(boolean italic) {
            this.isItalic = italic;
            return this;
        }

        /**
         * Sets whether the text should be strikethrough.
         *
         * @param strikethrough true for strikethrough text
         * @return this builder instance
         */
        public Builder setStrikethrough(boolean strikethrough) {
            this.isStrikethrough = strikethrough;
            return this;
        }

        /**
         * Sets whether the text should be formatted as inline code.
         *
         * @param code true for inline code formatting
         * @return this builder instance
         */
        public Builder setCode(boolean code) {
            this.isCode = code;
            return this;
        }

        /**
         * Builds the Text instance.
         *
         * @return a new Text instance
         * @throws IllegalStateException if no content has been set
         */
        public Text build() {
            if (content.isEmpty()) {
                throw new IllegalStateException("Text must have content");
            }
            return new Text(this);
        }
    }
}