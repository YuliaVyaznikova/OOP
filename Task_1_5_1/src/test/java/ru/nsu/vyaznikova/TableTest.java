package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Table class, which represents a Markdown table element.
 */
public class TableTest {

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
    public void testBasicTableCreation() {
        Table table = new Table.Builder()
            .addRow(new TestElement("Header1"), new TestElement("Header2"))
            .addRow(new TestElement("Data1"), new TestElement("Data2"))
            .build();

        String expected = "| Header1 | Header2 |\n" +
                         "| ------ | ------ |\n" +
                         "| Data1 | Data2 |\n";
        
        assertEquals(expected, table.toMarkdown());
    }

    @Test
    public void testTableWithDifferentAlignments() {
        Table table = new Table.Builder()
            .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_RIGHT, Table.ALIGN_CENTER)
            .addRow(new TestElement("Left"), new TestElement("Right"), new TestElement("Center"))
            .addRow(new TestElement("1"), new TestElement("2"), new TestElement("3"))
            .build();

        String expected = "| Left | Right | Center |\n" +
                         "| ------ | -----: | :----: |\n" +
                         "| 1 | 2 | 3 |\n";
        
        assertEquals(expected, table.toMarkdown());
    }

    @Test
    public void testTableWithRowLimit() {
        Table table = new Table.Builder()
            .withRowLimit(2)
            .addRow(new TestElement("Header"))
            .addRow(new TestElement("Row1"))
            .addRow(new TestElement("Row2"))
            .addRow(new TestElement("Row3"))
            .build();

        String expected = "| Header |\n" +
                         "| ------ |\n" +
                         "| Row1 |\n" +
                         "| Row2 |\n";
        
        assertEquals(expected, table.toMarkdown());
    }

    @Test
    public void testTableWithObjects() {
        Table table = new Table.Builder()
            .addRow("Header1", "Header2")
            .addRow(1, 2)
            .addRow(true, false)
            .build();

        String expected = "| Header1 | Header2 |\n" +
                         "| ------ | ------ |\n" +
                         "| 1 | 2 |\n" +
                         "| true | false |\n";
        
        assertEquals(expected, table.toMarkdown());
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
}
