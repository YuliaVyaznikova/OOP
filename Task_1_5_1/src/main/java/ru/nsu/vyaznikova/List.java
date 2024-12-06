package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Markdown list (ordered or unordered).
 */
public class List extends Element {
    private final java.util.List<Element> items;
    private final boolean ordered;

    public List(boolean ordered) {
        this.items = new ArrayList<>();
        this.ordered = ordered;
    }

    public void addItem(Element item) {
        items.add(item);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (ordered) {
                sb.append(i + 1).append(". ");
            } else {
                sb.append("- ");
            }
            sb.append(items.get(i).toMarkdown()).append("\n");
        }
        return sb.toString();
    }
}
