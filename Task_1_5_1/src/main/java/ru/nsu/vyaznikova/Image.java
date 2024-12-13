package ru.nsu.vyaznikova;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Represents a Markdown image with URL and alt text.
 * Alt text is displayed when the image cannot be loaded and is important for accessibility.
 * Examples:
 * - Basic image: ![alt text](url)
 * - Image with empty alt: ![](url)
 */
public class Image extends Element {
    private final String alt;
    private final String url;

    /**
 * Private constructor used by the Builder.
 *
 * @param builder the Builder instance containing image configuration
 */
    private Image(Builder builder) {
        this.alt = builder.alt;
        this.url = builder.url;
    }

    /**
 * Converts the image to its Markdown representation.
 * The format is: ![alt text](url)
 * Examples:
 * - With alt text: ![A beautiful sunset](https://example.com/sunset.jpg)
 * - Without alt text: ![](https://example.com/sunset.jpg)
 *
 * @return A string containing the Markdown representation of the image
 */
    @Override
    public String toMarkdown() {
        return String.format("![%s](%s)", alt != null ? alt : "", url);
    }

    /**
 * Builder class for creating Image instances.
 * Supports setting image URL and alt text.
 */
    public static class Builder {
        private String alt;
        private String url;

        /**
 * Sets the alt text for the image.
 * Alt text is displayed when the image cannot be loaded and helps with accessibility.
 *
 * @param alt the alternative text for the image, can be null for empty alt text
 * @return this builder instance
 */
        public Builder setAlt(String alt) {
            this.alt = alt;
            return this;
        }

        /**
 * Sets the image URL.
 *
 * @param url the URL pointing to the image resource
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
 * Builds the Image instance.
 *
 * @return a new Image instance
 * @throws IllegalStateException if URL is not set
 */
        public Image build() {
            if (url == null) {
                throw new IllegalStateException("Image URL must be set");
            }
            return new Image(this);
        }
    }
}