package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;

public class Table extends Element {
    public static final String ALIGN_LEFT = ":---";
    public static final String ALIGN_CENTER = ":---:";
    public static final String ALIGN_RIGHT = "---:";

    private final List<String[]> rows;
    private final String[] alignments;

    private Table(List<String[]> rows, String[] alignments) {
        this.rows = rows;
        this.alignments = alignments;
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();

        String[] header = rows.get(0);
        sb.append("| ").append(String.join(" | ", header)).append(" |\n");

        sb.append("| ");
        for (String align : alignments) {
            sb.append(align).append(" | ");
        }
        sb.append("\n");

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            sb.append("| ").append(String.join(" | ", row)).append(" |\n");
        }

        return sb.toString();
    }

    public static class Builder {
        private final List<String[]> rows = new ArrayList<>();
        private String[] alignments;

        public Builder withAlignments(String... alignments) {
            this.alignments = alignments;
            return this;
        }

        public Builder addRow(Object... values) {
            String[] row = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                row[i] = values[i] instanceof Element ? ((Element) values[i]).toMarkdown() : values[i].toString();
            }
            rows.add(row);
            return this;
        }

        public Builder withRowLimit(int limit) {
            while (rows.size() > limit) {
                rows.remove(rows.size() - 1);
            }
            return this;
        }

        public Table build() {
            return new Table(rows, alignments);
        }
    }
}