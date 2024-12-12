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
            .addRow(new Text.Builder().setContent("Header1").build(), 
                    new Text.Builder().setContent("Header2").build())
            .addRow(new Text.Builder().setContent("Row1Col1").build(), 
                    new Text.Builder().setContent("Row1Col2").build())
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
            .addRow(new Text.Builder().setContent("Center").build(), 
                    new Text.Builder().setContent("Left").build(), 
                    new Text.Builder().setContent("Right").build())
            .addRow(new Text.Builder().setContent("C").build(), 
                    new Text.Builder().setContent("L").build(), 
                    new Text.Builder().setContent("R").build())
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
            .addRow(new Text.Builder().setContent("Row1Col1").build(), 
                    new Text.Builder().setContent("Row1Col2").build())
            .build();
        Table table2 = new Table.Builder()
            .addRow(new Text.Builder().setContent("Row1Col1").build(), 
                    new Text.Builder().setContent("Row1Col2").build())
            .build();
        Table table3 = new Table.Builder()
            .addRow(new Text.Builder().setContent("Row1Col1").build(), 
                    new Text.Builder().setContent("Different").build())
            .build();

        assertEquals(table1, table2, "Tables with the same content should be equal.");
        assertNotEquals(table1, table3, "Tables with different content should not be equal.");
    }

    /**
     * Tests that creating a Table with mismatched column counts adds empty columns.
     */
    @Test
    public void testMismatchedColumnCounts() {
        Table table = new Table.Builder()
            .addRow(new Text.Builder().setContent("OnlyOneColumn").build())
            .addRow(new Text.Builder().setContent("Two").build(), 
                    new Text.Builder().setContent("Columns").build())
            .build();
        String expectedMarkdown = "| OnlyOneColumn |  |\n| :----------- | :------ |\n| Two | Columns |";
        assertEquals(expectedMarkdown, table.toMarkdown(), "Table with mismatched column counts should add empty columns.");
    }

    /**
     * Tests the creation of a table with a row limit and its Markdown serialization.
     */
    @Test
    public void testTableWithRowLimit() {
        Table.Builder builder = new Table.Builder()
            .withRowLimit(2)
            .addRow(new Text.Builder().setContent("Header1").build(), 
                    new Text.Builder().setContent("Header2").build());
        for (int i = 0; i < 5; i++) {
            builder.addRow(new Text.Builder().setContent("Row" + i + "Col1").build(), 
                           new Text.Builder().setContent("Row" + i + "Col2").build());
        }
        Table table = builder.build();
        String expectedMarkdown = "| Header1 | Header2 |\n| :------ | :------ |\n| Row0Col1 | Row0Col2 |";
        assertEquals(expectedMarkdown, table.toMarkdown(), "Table with row limit should be correctly serialized in Markdown, showing only the first few rows.");
    }
}
