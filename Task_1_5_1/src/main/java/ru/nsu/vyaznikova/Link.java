package ru.nsu.vyaznikova;

/**
 * Represents a Markdown link element.
 */
public class Link extends Element {
    private final String text;
    private final String url;

    /**
     * Creates a new Link with the specified text and URL.
     * @param text the text to display for the link
     * @param url the URL the link points to
     * @throws IllegalArgumentException if text or url is null
     */
    public Link(String text, String url) {
        if (text == null || url == null) {
            throw new IllegalArgumentException("Text and URL cannot be null");
        }
        this.text = text;
        this.url = url;
    }

    @Override
    public String toMarkdown() {
        return String.format("[%s](%s)", text, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Link link = (Link) obj;
        return text.equals(link.text) && url.equals(link.url);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(text, url);
    }
}