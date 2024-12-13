package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Markdown header with a specified level (1-6) and content.
 * The header can contain multiple elements as content.

 * Examples:
 * - Level 1: # Header
 * - Level 2: ## Header
 * - Level 3: ### Header
 * - Level 4: #### Header
 * - Level 5: ##### Header
 * - Level 6: ###### Header
 */
public class Header extends Element {
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 6;

    private final List<Element> content;
    private final int level;

    /**
     * Private constructor used by the Builder.
     * 
     * @param builder the Builder instance containing header configuration
     */
    private Header(Builder builder) {
        this.content = Collections.unmodifiableList(new ArrayList<>(builder.content));
        this.level = builder.level;
    }

    /**
     * Converts the header to its Markdown representation.
     *
     * The method generates a Markdown header by:
     * 1. Repeating '#' characters based on the header level
     * 2. Adding a space after the '#' characters
     * 3. Concatenating the content elements' Markdown representations
     *
     * Examples:
     * - Level 1 header: "# Content"
     * - Level 2 header: "## Content"
     * - Multiple content elements: "### First Second"
     *
     * @return A string containing the Markdown representation of the header
     */
    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("#".repeat(level)).append(" ");

        for (int i = 0; i < content.size(); i++) {
            sb.append(content.get(i).toMarkdown());
            if (i < content.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * Builder class for creating Header instances.
     * Supports adding multiple content elements and validates the header level.
     */
    public static class Builder {
        private final List<Element> content = new ArrayList<>();
        private int level = 1; // Default level is 1

        /**
         * Sets the header level.
         * 
         * @param level the level of the header (must be between 1 and 6)
         * @return this builder instance
         * @throws IllegalArgumentException if level is not between 1 and 6
         */
        public Builder setLevel(int level) {
            if (level < MIN_LEVEL || level > MAX_LEVEL) {
                throw new IllegalArgumentException(
                        String.format("Header level must be between %d and %d, got: %d",
                                MIN_LEVEL, MAX_LEVEL, level));
            }
            this.level = level;
            return this;
        }

        /**
         * Adds a content element to the header.
         * Multiple elements will be joined with spaces in the final header.
         * 
         * @param element the element to add to the header content
         * @return this builder instance
         * @throws IllegalArgumentException if element is null
         */
        public Builder addContent(Element element) {
            Objects.requireNonNull(element, "Header content element cannot be null");
            content.add(element);
            return this;
        }

        /**
         * Adds multiple content elements to the header.
         * Elements will be joined with spaces in the final header.
         * 
         * @param elements the elements to add to the header content
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
         * Builds the Header instance.
         * 
         * @return a new Header instance
         * @throws IllegalStateException if no content has been added
         */
        public Header build() {
            if (content.isEmpty()) {
                throw new IllegalStateException("Header must have at least one content element");
            }
            return new Header(this);
        }
    }
}