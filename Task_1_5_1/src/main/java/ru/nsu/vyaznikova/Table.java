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
     * @param rows the rows of the table
     * @param rowLimit the maximum number of rows allowed
     * @param alignments the alignments for the table columns
     */
    private Table(List<List<Element>> rows, int rowLimit, Alignment[] alignments) {
        this.rows = rows;
        this.alignments = alignments;
        this.rowLimit = rowLimit;
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
        private int rowLimit = Integer.MAX_VALUE;
        private Alignment[] alignments = new Alignment[]{Alignment.LEFT};

        /**
         * Sets the row limit for the table.
         * @param limit maximum number of rows
         * @return this builder instance
         * @throws IllegalArgumentException if limit is less than or equal to 0
         */
        public Builder withRowLimit(int limit) {
            if (limit <= 0) {
                throw new IllegalArgumentException("Row limit must be positive");
            }
            this.rowLimit = limit;
            return this;
        }

        /**
         * Sets the alignments for table columns.
         * @param alignments array of column alignments
         * @return this builder instance
         * @throws IllegalArgumentException if alignments is null or empty
         */
        public Builder withAlignments(Alignment... alignments) {
            if (alignments == null || alignments.length == 0) {
                throw new IllegalArgumentException("Alignments cannot be null or empty");
            }
            this.alignments = alignments.clone();
            return this;
        }

        /**
         * Adds a row to the table using variable arguments of Elements.
         * @param elements the elements to add as a row
         * @return this builder instance
         * @throws IllegalArgumentException if elements is null
         */
        public Builder addRow(Element... elements) {
            if (elements == null) {
                throw new IllegalArgumentException("Elements cannot be null");
            }
            List<Element> row = new ArrayList<>(Arrays.asList(elements));
            rows.add(row);
            return this;
        }

        /**
         * Adds a row to the table using Objects that will be converted to Text elements.
         * @param objects the objects to add as a row
         * @return this builder instance
         * @throws IllegalArgumentException if objects is null
         */
        public Builder addRow(Object... objects) {
            if (objects == null) {
                throw new IllegalArgumentException("Objects cannot be null");
            }
            List<Element> row = new ArrayList<>();
            for (Object obj : objects) {
                if (obj instanceof Element) {
                    row.add((Element) obj);
                } else {
                    row.add(new Text.Builder().setContent(String.valueOf(obj)).build());
                }
            }
            rows.add(row);
            return this;
        }

        /**
         * Builds the Table instance.
         * @return a new Table instance
         */
        public Table build() {
            return new Table(rows, rowLimit, alignments);
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