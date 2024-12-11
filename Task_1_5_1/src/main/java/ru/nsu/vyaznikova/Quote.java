package ru.nsu.vyaznikova;

/**
 * Represents a Markdown quote.
 * Markdown symbol: >
 */
public class Quote extends Element {
    private final Element content;

    public Quote(Element content) {
        this.content = content;
    }

    @Override
    public String toMarkdown() {
        return "> " + content.toMarkdown();
    }
}