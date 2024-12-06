package ru.nsu.vyaznikova;

/**
 * Represents a Markdown header (# ## ###).
 */
public class Header extends Element {
    private final Element content;
    private final int level;

    public Header(Element content, int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Header level must be between 1 and 6");
        }
        this.content = content;
        this.level = level;
    }

    @Override
    public String toMarkdown() {
        return "#".repeat(level) + " " + content.toMarkdown();
    }
}
