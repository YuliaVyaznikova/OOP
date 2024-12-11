package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Table extends Element {
    public static final String ALIGN_LEFT = "------";
    public static final String ALIGN_RIGHT = "-----:";

    public enum Alignment {
        LEFT(ALIGN_LEFT),
        RIGHT(ALIGN_RIGHT);

        private final String markdown;

        Alignment(String markdown) {
            this.markdown = markdown;
        }

        public String getMarkdown() {
            return markdown;
        }
    }

    private final List<List<Element>> rows;
    private final Alignment[] alignments;
    private final int rowLimit;

    private Table(List<List<Element>> rows, Alignment[] alignments, int rowLimit) {
        this.rows = rows;
        this.alignments = alignments;
        this.rowLimit = rowLimit;
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        
        // Header
        List<Element> header = rows.get(0);
        sb.append("| ");
        for (int i = 0; i < header.size(); i++) {
            sb.append(header.get(i).toMarkdown());
            if (i < header.size() - 1) {
                sb.append(" | ");
            } else {
                sb.append(" |");
            }
        }
        sb.append("\n");

        // Alignments
        sb.append("| ");
        for (int i = 0; i < alignments.length; i++) {
            sb.append(alignments[i].getMarkdown());
            if (i < alignments.length - 1) {
                sb.append("| ");
            } else {
                sb.append(" |");
            }
        }
        sb.append("\n");

        // Data rows
        for (int i = 1; i < rows.size(); i++) {
            List<Element> row = rows.get(i);
            sb.append("| ");
            for (int j = 0; j < row.size(); j++) {
                sb.append(row.get(j).toMarkdown());
                if (j < row.size() - 1) {
                    sb.append(" | ");
                } else {
                    sb.append(" |");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return toMarkdown();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Table other = (Table) obj;
        return rowLimit == other.rowLimit &&
               Arrays.equals(alignments, other.alignments) &&
               rows.equals(other.rows);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rowLimit);
        result = 31 * result + Arrays.hashCode(alignments);
        result = 31 * result + rows.hashCode();
        return result;
    }

    public static class Builder {
        private final List<List<Element>> rows = new ArrayList<>();
        private Alignment[] alignments;
        private int rowLimit = Integer.MAX_VALUE;

        public Builder withAlignments(String... alignments) {
            this.alignments = new Alignment[alignments.length];
            for (int i = 0; i < alignments.length; i++) {
                if (alignments[i].equals(ALIGN_LEFT)) {
                    this.alignments[i] = Alignment.LEFT;
                } else if (alignments[i].equals(ALIGN_RIGHT)) {
                    this.alignments[i] = Alignment.RIGHT;
                } else {
                    throw new IllegalArgumentException("Invalid alignment: " + alignments[i]);
                }
            }
            return this;
        }

        public Builder withRowLimit(int limit) {
            this.rowLimit = limit;
            while (rows.size() > limit) {
                rows.remove(rows.size() - 1);
            }
            return this;
        }

        public Builder addRow(Object... values) {
            if (rows.size() >= rowLimit) {
                return this;
            }

            List<Element> row = new ArrayList<>();
            for (Object value : values) {
                if (value instanceof Element) {
                    row.add((Element) value);
                } else {
                    row.add(new Text(String.valueOf(value)));
                }
            }
            rows.add(row);
            return this;
        }

        public Table build() {
            if (rows.isEmpty()) {
                throw new IllegalStateException("Table must have at least one row");
            }

            if (alignments == null) {
                alignments = new Alignment[rows.get(0).size()];
                Arrays.fill(alignments, Alignment.LEFT);
            }

            if (alignments.length != rows.get(0).size()) {
                throw new IllegalStateException(
                    "Number of alignments (" + alignments.length + 
                    ") doesn't match number of columns (" + rows.get(0).size() + ")"
                );
            }

            return new Table(rows, alignments, rowLimit);
        }
    }
}