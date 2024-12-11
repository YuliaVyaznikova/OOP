package ru.nsu.vyaznikova;

/**
 * Represents a Markdown task/checkbox item.
 */
public class Task extends Element {
    private final Element description;
    private final boolean completed;

    public Task(Element description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }

    @Override
    public String toMarkdown() {
        return "- [" + (completed ? "x" : " ") + "] " + description.toMarkdown();
    }
}