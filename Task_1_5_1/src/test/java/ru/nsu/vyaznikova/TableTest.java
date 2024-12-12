package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Table class, which represents a Markdown table element.
 */
public class TableTest {

    /**
     * Tests the creation of a table with headers and its Markdown serialization.
     */
    @Test
    public void testTableWithHeaders() {
        Table table = new Table.Builder()
            .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_RIGHT)
            .addRow("Header1", "Header2")
            .addRow("Row1Col1", "Row1Col2")
            .build();
        String expectedMarkdown = "| Header1 | Header2 |\n| :------ | ------: |\n| Row1Col1 | Row1Col2 |";
        assertEquals(expectedMarkdown, table.toMarkdown(), "Table with headers should be correctly serialized in Markdown.");
    }

    /**
     * Tests the creation of a table with different alignments and its Markdown serialization.
     */
    @Test
    public void testTableWithAlignments() {
        Table table = new Table.Builder()
            .withAlignments(Table.ALIGN_CENTER, Table.ALIGN_LEFT, Table.ALIGN_RIGHT)
            .addRow("Center", "Left", "Right")
            .addRow("C", "L", "R")
            .build();
        String expectedMarkdown = "| Center | Left | Right |\n| :----: | :--- | ----: |\n| C | L | R |";
        assertEquals(expectedMarkdown, table.toMarkdown(), "Table with different alignments should be correctly serialized in Markdown.");
    }

    /**
     * Tests the equality method for Table objects.
     */
    @Test
    public void testTableEquality() {
        Table table1 = new Table.Builder()
            .addRow("Row1Col1", "Row1Col2")
            .build();
        Table table2 = new Table.Builder()
            .addRow("Row1Col1", "Row1Col2")
            .build();
        Table table3 = new Table.Builder()
            .addRow("Row1Col1", "Different")
            .build();

        assertEquals(table1, table2, "Tables with the same content should be equal.");
        assertNotEquals(table1, table3, "Tables with different content should not be equal.");
    }

    /**
     * Tests that creating a Table with mismatched column counts throws an exception.
     */
    @Test
    public void testMismatchedColumnCounts() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Table.Builder()
                .addRow("OnlyOneColumn")
                .addRow("Two", "Columns")
                .build();
        }, "Creating a Table with mismatched column counts should throw IllegalArgumentException.");
    }

    /**
     * Tests the creation of a table with a row limit and its Markdown serialization.
     */
    @Test
    public void testTableWithRowLimit() {
        Table.Builder builder = new Table.Builder()
            .withRowLimit(2)
            .addRow("Header1", "Header2");
        for (int i = 0; i < 5; i++) {
            builder.addRow("Row" + i + "Col1", "Row" + i + "Col2");
        }
        Table table = builder.build();
        String expectedMarkdown = "| Header1 | Header2 |\n| :------ | :------ |\n| Row0Col1 | Row0Col2 |\n| Row1Col1 | Row1Col2 |";
        assertEquals(expectedMarkdown, table.toMarkdown(), "Table with row limit should be correctly serialized in Markdown, showing only the first few rows.");
    }
}
