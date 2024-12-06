package ru.nsu.vyaznikova;

/**
 * Represents a Markdown link [text](url).
 */
public class Link extends Element {
    private final Element text;
    private final String url;

    public Link(Element text, String url) {
        this.text = text;
        this.url = url;
    }

    @Override
    public String toMarkdown() {
        return "[" + text.toMarkdown() + "](" + url + ")";
    }
}
