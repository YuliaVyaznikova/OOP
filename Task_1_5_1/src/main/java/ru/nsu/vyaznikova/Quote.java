package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Markdown quote that can span multiple lines and contain nested elements.
 * Markdown symbol: &quot;&gt;&quot;
 * Examples:
 * - Single line quote: &quot;&gt;&quot; Quote content
 * - Multi-line quote:
 * &quot;&gt;&quot; Line 1
 * &quot;&gt;&quot; Line 2
 */
public class Quote extends Element {
    private final List<Element> content;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the Builder instance containing quote configuration
     */
    private Quote(Builder builder) {
        this.content = Collections.unmodifiableList(new ArrayList<>(builder.content));
    }

    /**
     * Converts the quote to its Markdown representation.
     * Each line of the quote is prefixed with &quot;&gt;&quot;
     *
     * @return A string containing the Markdown representation of the quote
     */
    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (Element element : content) {
            sb.append("> ").append(element.toMarkdown()).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Builder class for creating Quote instances.
     * Supports adding multiple content elements.
     */
    public static class Builder {
        private final List<Element> content = new ArrayList<>();

        /**
         * Adds a content element to the quote.
         *
         * @param element the element to add to the quote
         * @return this builder instance
         * @throws IllegalArgumentException if element is null
         */
        public Builder addContent(Element element) {
            if (element == null) {
                throw new IllegalArgumentException("Quote content element cannot be null");
            }
            content.add(element);
            return this;
        }

        /**
         * Adds multiple content elements to the quote.
         *
         * @param elements the elements to add to the quote
         * @return this builder instance
         * @throws IllegalArgumentException if elements is null or contains null
         */
        public Builder addContent(Element... elements) {
            Objects.requireNonNull(elements, "Elements array cannot be null");
            for (Element element : elements) {
                addContent(element);
            }
            return this;
        }

        /**
         * Builds the Quote instance.
         *
         * @return a new Quote instance
         * @throws IllegalStateException if no content has been added
         */
        public Quote build() {
            if (content.isEmpty()) {
                throw new IllegalStateException("Quote must contain at least one content element");
            }
            return new Quote(this);
        }
    }
}