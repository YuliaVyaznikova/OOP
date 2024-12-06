package ru.nsu.vyaznikova;

/**
 * Represents a Markdown image ![alt](url).
 */
public class Image extends Element {
    private final String alt;
    private final String url;

    public Image(String alt, String url) {
        this.alt = alt;
        this.url = url;
    }

    @Override
    public String toMarkdown() {
        return "![" + alt + "](" + url + ")";
    }
}
