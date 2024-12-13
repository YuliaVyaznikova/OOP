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
    public static final String ALIGN_CENTER = ":----:";

    private final List<List<Element>> rows;
    private final String[] alignments;
    private final int rowLimit;

    /**
     * Private constructor used by the Builder.
     */
    private Table(Builder builder) {
        this.rows = new ArrayList<>(builder.rows);
        this.alignments = builder.alignments.clone();
        this.rowLimit = builder.rowLimit;
    }

    @Override
    public String toMarkdown() {
        if (rows.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        // Header
        sb.append(formatRow(rows.get(0)));

        // Alignment row
        sb.append("| ");
        for (int i = 0; i < alignments.length; i++) {
            sb.append(alignments[i]);
            sb.append(i < alignments.length - 1 ? " | " : " |");
        }
        sb.append("\n");

        // Data rows (respect row limit)
        int dataRows = Math.min(rows.size() - 1, rowLimit);
        for (int i = 1; i <= dataRows; i++) {
            sb.append(formatRow(rows.get(i)));
        }

        return sb.toString();
    }

    private String formatRow(List<Element> row) {
        StringBuilder sb = new StringBuilder("| ");
        for (int i = 0; i < row.size(); i++) {
            sb.append(row.get(i).toMarkdown());
            sb.append(i < row.size() - 1 ? " | " : " |");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Builder class for creating Table instances.
     */
    public static class Builder {
        private final List<List<Element>> rows = new ArrayList<>();
        private String[] alignments = {ALIGN_LEFT};  // Default left alignment
        private int rowLimit = Integer.MAX_VALUE;

        /**
         * Sets the row limit for the table.
         *
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
         *
         * @param alignments the alignment strings (ALIGN_LEFT, ALIGN_RIGHT, ALIGN_CENTER)
         * @return this builder instance
         * @throws IllegalArgumentException if alignments is null or empty
         */
        public Builder withAlignments(String... alignments) {
            if (alignments == null || alignments.length == 0) {
                throw new IllegalArgumentException("Alignments cannot be null or empty");
            }
            for (String align : alignments) {
                if (!ALIGN_LEFT.equals(align)
                    && !ALIGN_RIGHT.equals(align)
                    && !ALIGN_CENTER.equals(align)) {
                    throw new IllegalArgumentException("Invalid alignment: " + align);
                }
            }
            this.alignments = alignments.clone();
            return this;
        }

        /**
         * Adds a row to the table using variable arguments of Elements.
         *
         * @param elements the elements to add as a row
         * @return this builder instance
         */
        public Builder addRow(Element... elements) {
            Objects.requireNonNull(elements, "Elements cannot be null");
            rows.add(new ArrayList<>(Arrays.asList(elements)));
            return this;
        }

        /**
         * Adds a row to the table using variable arguments of Objects.
         * Objects are converted to Text elements.
         *
         * @param objects the objects to add as a row
         * @return this builder instance
         */
        public Builder addRow(Object... objects) {
            Objects.requireNonNull(objects, "Objects cannot be null");
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
         *
         * @return a new Table instance
         * @throws IllegalStateException if no rows have been added
         */
        public Table build() {
            if (rows.isEmpty()) {
                throw new IllegalStateException("Table must have at least one row");
            }

            // Ensure all rows have the same number of columns as the header
            int columns = rows.get(0).size();
            for (int i = 1; i < rows.size(); i++) {
                List<Element> row = rows.get(i);
                while (row.size() < columns) {
                    row.add(new Text.Builder().setContent("").build());
                }
            }

            // Ensure we have enough alignment specifications
            if (alignments.length < columns) {
                String[] newAlignments = new String[columns];
                System.arraycopy(alignments, 0, newAlignments, 0, alignments.length);
                for (int i = alignments.length; i < columns; i++) {
                    newAlignments[i] = ALIGN_LEFT;  // Default to left alignment
                }
                alignments = newAlignments;
            }

            return new Table(this);
        }
    }
}
