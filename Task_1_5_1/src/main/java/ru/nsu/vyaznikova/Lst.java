package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Markdown list (ordered or unordered) with support for nested lists.
 * Markdown symbols:
 * - Unordered list: -
 * - Ordered list: 1., 2., etc.
 */
public class Lst extends Element {
    private final List<ListItem> items;
    private final boolean ordered;

    private Lst(Builder builder) {
        this.ordered = builder.ordered;
        this.items = Collections.unmodifiableList(new ArrayList<>(builder.items));
    }

    /**
     * Converts the list to Markdown format with the specified prefix for indentation.
     *
     * @param prefix the indentation prefix
     * @return the Markdown representation of the list
     */
    private String toMarkdown(String prefix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(items.get(i).toMarkdown(prefix, ordered, i + 1));
        }
        return sb.toString();
    }

    @Override
    public String toMarkdown() {
        return toMarkdown("");
    }

    /**
     * Represents a single item in the list, which can contain an element and nested lists.
     */
    private static class ListItem {
        private final Element content;
        private final List<Lst> nestedLists;

        private ListItem(Element content) {
            this.content = Objects.requireNonNull(content, "List item content cannot be null");
            this.nestedLists = new ArrayList<>();
        }

        private void addNestedList(Lst list) {
            Objects.requireNonNull(list, "Nested list cannot be null");
            nestedLists.add(list);
        }

        private String toMarkdown(String prefix, boolean ordered, int index) {
            StringBuilder sb = new StringBuilder();
            // Add main content with proper prefix
            sb.append(prefix);
            if (ordered) {
                sb.append(index).append(". ");
            } else {
                sb.append("- ");
            }
            sb.append(content.toMarkdown());

            // Add nested lists with increased indentation
            for (Lst nestedList : nestedLists) {
                sb.append("\n").append(nestedList.toMarkdown(prefix + "  "));
            }
            return sb.toString();
        }
    }

    /**
     * Builder class for creating Lst instances.
     */
    public static class Builder {
        private final List<ListItem> items = new ArrayList<>();
        private final boolean ordered;
        private ListItem currentItem;

        /**
         * Creates a new Builder for a list.
         *
         * @param ordered true for ordered list, false for unordered
         */
        public Builder(boolean ordered) {
            this.ordered = ordered;
        }

        /**
         * Adds a new item to the list.
         *
         * @param content the content of the item
         * @return this builder instance
         * @throws IllegalArgumentException if content is null
         */
        public Builder addItem(Element content) {
            Objects.requireNonNull(content, "List item content cannot be null");
            currentItem = new ListItem(content);
            items.add(currentItem);
            return this;
        }

        /**
         * Adds a new nested list to the current item.
         *
         * @param nestedList the nested list to add
         * @return this builder instance
         * @throws IllegalStateException    if no current item exists
         * @throws IllegalArgumentException if nestedList is null
         */
        public Builder addNestedList(Lst nestedList) {
            if (currentItem == null) {
                throw new IllegalStateException("Cannot add nested list without a parent item");
            }
            Objects.requireNonNull(nestedList, "Nested list cannot be null");
            currentItem.addNestedList(nestedList);
            return this;
        }

        /**
         * Builds the Lst instance.
         *
         * @return a new Lst instance
         * @throws IllegalStateException if the list is empty
         */
        public Lst build() {
            if (items.isEmpty()) {
                throw new IllegalStateException("List must contain at least one item");
            }
            return new Lst(this);
        }
    }
}