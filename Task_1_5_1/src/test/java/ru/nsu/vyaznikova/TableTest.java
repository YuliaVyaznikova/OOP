package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Table class.
 *
 * <p>These tests verify the functionality of table creation and formatting in Markdown format.
 * Tests include basic tables, alignment options, and handling of invalid inputs.</p>
 */
public class TableTest {

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

    @Test
    public void testEmptyTable() {
        assertThrows(IllegalStateException.class, () -> {
            new Table.Builder().build();
        });
    }

    @Test
    public void testInvalidRowLimit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Table.Builder().withRowLimit(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Table.Builder().withRowLimit(-1);
        });
    }

    @Test
    public void testInvalidAlignment() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Table.Builder().withAlignments("invalid");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Table.Builder().withAlignments();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Table.Builder().withAlignments((String[]) null);
        });
    }

    private static class TestElement extends Element {
        private final String content;

        public TestElement(String content) {
            this.content = content;
        }

        @Override
        public String toMarkdown() {
            return content;
        }
    }
}
