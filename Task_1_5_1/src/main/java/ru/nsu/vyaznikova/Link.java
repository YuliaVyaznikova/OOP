package ru.nsu.vyaznikova;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a Markdown link with text, URL, and optional title.

 * Examples:
 * - Basic link: [text](url)
 * - Link with title: [text](url "title")
 */
public class Link extends Element {
    private final Element text;
    private final String url;
    private final Optional<String> title;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the Builder instance containing link configuration
     */
    private Link(Builder builder) {
        this.text = builder.text;
        this.url = builder.url;
        this.title = Optional.ofNullable(builder.title);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder()
            .append("[")
            .append(text.toMarkdown())
            .append("](")
            .append(url);

        title.ifPresent(t -> sb.append(" \"").append(t).append("\""));

        return sb.append(")").toString();
    }

    /**
     * Builder class for creating Link instances.
     * Supports setting link text, URL, and optional title.
     */
    public static class Builder {
        private Element text;
        private String url;
        private String title;

        /**
         * Sets the link text.
         *
         * @param text the element to use as link text
         * @return this builder instance
         * @throws IllegalArgumentException if text is null
         */
        public Builder setText(Element text) {
            this.text = Objects.requireNonNull(text, "Link text cannot be null");
            return this;
        }

        /**
         * Sets the link URL.
         *
         * @param url the URL the link points to
         * @return this builder instance
         * @throws IllegalArgumentException if url is null or malformed
         */
        public Builder setUrl(String url) {
            Objects.requireNonNull(url, "URL cannot be null");
            try {
                // Validate URL format using URI
                new URI(url).parseServerAuthority();
                this.url = url;
                return this;
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid URL format: " + url, e);
            }
        }

        /**
         * Sets the link title (optional).
         * The title appears as a tooltip when hovering over the link.
         *
         * @param title the title for the link, can be null
         * @return this builder instance
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Builds the Link instance.
         *
         * @return a new Link instance
         * @throws IllegalStateException if text or URL is not set
         */
        public Link build() {
            if (text == null) {
                throw new IllegalStateException("Link text must be set");
            }
            if (url == null) {
                throw new IllegalStateException("Link URL must be set");
            }
            return new Link(this);
        }
    }
}