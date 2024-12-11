package ru.nsu.vyaznikova;

/**
 * Represents a Markdown code block with language specification.
 * Markdown symbols: ```language
code
```
 */
public class CodeBlock extends Element {
    private final String code;
    private final String language;

    public CodeBlock(String code, String language) {
        this.code = code;
        this.language = language;
    }

    @Override
    public String toMarkdown() {
        return "```" + language + "\n" + code + "\n```";
    }
}