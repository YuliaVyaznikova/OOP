package ru.nsu.vyaznikova;

/**
 * Represents a Markdown link.
 * Markdown symbols: [text](url)
 */
public class Link extends Text {
    private final String url;

    public Link(Element text, String url) {
        super(text.toMarkdown());
        this.url = url;
    }

    @Override
    public String toMarkdown() {
        return "[" + super.toMarkdown() + "](" + url + ")";
    }
}