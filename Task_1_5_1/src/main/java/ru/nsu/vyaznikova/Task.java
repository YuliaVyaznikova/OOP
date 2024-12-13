package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Markdown task/checkbox item with support for nested elements.
 * <p>
 * Examples:
 * - Uncompleted task: - [ ] Task description
 * - Completed task: - [x] Task description
 * - Task with nested elements: - [ ] Task with **bold** and [link](url)
 */
public class Task extends Element {
    private final List<Element> content;
    private final boolean completed;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the Builder instance containing task configuration
     */
    private Task(Builder builder) {
        this.content = Collections.unmodifiableList(new ArrayList<>(builder.content));
        this.completed = builder.completed;
    }

    /**
     * Converts the task to its Markdown representation.
     * The format is: - [x] or - [ ] followed by the task content.
     * <p>
     * Examples:
     * - Uncompleted: - [ ] Task description
     * - Completed: - [x] Task description
     * - With nested elements: - [ ] Task with **bold** text
     *
     * @return A string containing the Markdown representation of the task
     */
    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder()
                .append("- [")
                .append(completed ? "x" : " ")
                .append("] ");

        for (int i = 0; i < content.size(); i++) {
            sb.append(content.get(i).toMarkdown());
            if (i < content.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * Builder class for creating Task instances.
     * Supports setting completion status and adding multiple content elements.
     */
    public static class Builder {
        private final List<Element> content = new ArrayList<>();
        private boolean completed = false;  // Default to uncompleted

        /**
         * Sets the completion status of the task.
         *
         * @param completed true if the task is completed, false otherwise
         * @return this builder instance
         */
        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        /**
         * Adds a content element to the task description.
         *
         * @param element the element to add to the task content
         * @return this builder instance
         * @throws IllegalArgumentException if element is null
         */
        public Builder addContent(Element element) {
            Objects.requireNonNull(element, "Task content element cannot be null");
            content.add(element);
            return this;
        }

        /**
         * Adds multiple content elements to the task description.
         *
         * @param elements the elements to add to the task content
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
         * Builds the Task instance.
         *
         * @return a new Task instance
         * @throws IllegalStateException if no content has been added
         */
        public Task build() {
            if (content.isEmpty()) {
                throw new IllegalStateException("Task must contain at least one content element");
            }
            return new Task(this);
        }
    }
}