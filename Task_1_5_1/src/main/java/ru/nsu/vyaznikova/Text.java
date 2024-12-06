package ru.nsu.vyaznikova;

public class Text extends Element {
    private final String content;

    public Text(String content) {
        this.content = content;
    }

    @Override
    public String toMarkdown() {
        return content;
    }

    public static class Bold extends Text {
        public Bold(String content) {
            super(content);
        }

        @Override
        public String toMarkdown() {
            return "**" + super.toMarkdown() + "**";
        }
    }

    public static class Italic extends Text {
        public Italic(String content) {
            super(content);
        }

        @Override
        public String toMarkdown() {
            return "*" + super.toMarkdown() + "*";
        }
    }

    public static class Strikethrough extends Text {
        public Strikethrough(String content) {
            super(content);
        }

        @Override
        public String toMarkdown() {
            return "~~" + super.toMarkdown() + "~~";
        }
    }

    public static class Code extends Text {
        public Code(String content) {
            super(content);
        }

        @Override
        public String toMarkdown() {
            return "`" + super.toMarkdown() + "`";
        }
    }
}