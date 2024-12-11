package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Markdown table with rows and columns.
 */
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

    /**
     * Constructs a Table object using the Builder pattern.
     * @param builder the Builder instance containing the table data
     */
    private Table(Builder builder) {
        this.rows = builder.rows;
        this.alignments = builder.alignments;
        this.rowLimit = builder.rowLimit;
    }

    /**
     * Converts the table to its Markdown representation.
     * @return A string containing the Markdown representation of the table
     */
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

    /**
     * Builder class for constructing Table objects.
     */
    public static class Builder {
        private final List<List<Element>> rows = new ArrayList<>();
        private Alignment[] alignments;
        private int rowLimit = Integer.MAX_VALUE;

        /**
         * Sets the alignments for the table columns.
         * @param alignments the alignments for the table columns
         * @return the Builder instance for method chaining
         */
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

        /**
         * Sets the limit for the number of rows in the table.
         * @param rowLimit the maximum number of rows allowed
         * @return the Builder instance for method chaining
         */
        public Builder setRowLimit(int rowLimit) {
            this.rowLimit = rowLimit;
            while (rows.size() > rowLimit) {
                rows.remove(rows.size() - 1);
            }
            return this;
        }

        /**
         * Adds a row to the table.
         * @param row the row to add
         * @return the Builder instance for method chaining
         */
        public Builder addRow(List<Element> row) {
            if (rows.size() >= rowLimit) {
                return this;
            }
            this.rows.add(row);
            return this;
        }

        /**
         * Builds the Table object.
         * @return a new Table instance
         */
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

            return new Table(this);
        }
    }

    /**
     * Returns a string representation of the table in Markdown format.
     * @return a string containing the Markdown representation of the table
     */
    @Override
    public String toString() {
        return toMarkdown();
    }

    /**
     * Compares this table to the specified object for equality.
     * Two tables are considered equal if they have the same row limit,
     * alignments, and rows.
     * @param obj the object to compare this table against
     * @return true if the given object is equal to this table, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Table other = (Table) obj;
        return rowLimit == other.rowLimit &&
               Arrays.equals(alignments, other.alignments) &&
               rows.equals(other.rows);
    }

    /**
     * Returns a hash code value for the table.
     * The hash code is computed based on the row limit,
     * alignments, and rows.
     * @return the hash code value for this table
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(rowLimit);
        result = 31 * result + Arrays.hashCode(alignments);
        result = 31 * result + rows.hashCode();
        return result;
    }
}