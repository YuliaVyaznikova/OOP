package ru.nsu.vyaznikova;

/**
 * Represents plain text and its formatting variations in Markdown.
 */
public class Text extends Element {
    private final String content;

    /**
     * Constructs a Text object using the Builder pattern.
     * @param builder the Builder instance containing the content
     */
    private Text(Builder builder) {
        this.content = builder.content;
    }

    /**
     * Converts the text content to its Markdown representation.
     * @return A string containing the Markdown representation of the text
     */
    @Override
    public String toMarkdown() {
        return content;
    }

    /**
     * Builder class for constructing Text objects.
     */
    public static class Builder {
        private String content;

        /**
         * Sets the content of the text.
         * @param content the content to set
         * @return the Builder instance for method chaining
         * @throws IllegalArgumentException if content is null
         */
        public Builder setContent(String content) {
            if (content == null) {
                throw new IllegalArgumentException("Content cannot be null");
            }
            this.content = content;
            return this;
        }

        /**
         * Builds the Text object.
         * @return a new Text instance
         */
        public Text build() {
            return new Text(this);
        }
    }

    /**
     * Represents bold text in Markdown.
     */
    public static class Bold extends Text {
        /**
         * Constructs a Bold text object.
         * @param content the content of the bold text
         */
        public Bold(String content) {
            super(new Builder().setContent(content));
        }

        /**
         * Converts the bold text to its Markdown representation.
         * @return A string containing the Markdown representation of the bold text
         */
        @Override
        public String toMarkdown() {
            return "**" + super.toMarkdown() + "**";
        }
    }

    /**
     * Represents italic text in Markdown.
     */
    public static class Italic extends Text {
        /**
         * Constructs an Italic text object.
         * @param content the content of the italic text
         */
        public Italic(String content) {
            super(new Builder().setContent(content));
        }

        /**
         * Converts the italic text to its Markdown representation.
         * @return A string containing the Markdown representation of the italic text
         */
        @Override
        public String toMarkdown() {
            return "*" + super.toMarkdown() + "*";
        }
    }

    /**
     * Represents strikethrough text in Markdown.
     */
    public static class Strikethrough extends Text {
        /**
         * Constructs a Strikethrough text object.
         * @param content the content of the strikethrough text
         */
        public Strikethrough(String content) {
            super(new Builder().setContent(content));
        }

        /**
         * Converts the strikethrough text to its Markdown representation.
         * @return A string containing the Markdown representation of the strikethrough text
         */
        @Override
        public String toMarkdown() {
            return "~~" + super.toMarkdown() + "~~";
        }
    }

    /**
     * Represents inline code in Markdown.
     */
    public static class InlineCode extends Text {
        /**
         * Constructs an InlineCode text object.
         * @param content the content of the inline code text
         */
        public InlineCode(String content) {
            super(new Builder().setContent(content));
        }

        /**
         * Converts the inline code text to its Markdown representation.
         * @return A string containing the Markdown representation of the inline code text
         */
        @Override
        public String toMarkdown() {
            return "`" + super.toMarkdown() + "`";
        }
    }
}