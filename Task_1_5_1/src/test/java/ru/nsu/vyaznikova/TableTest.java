package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Table class, which represents a Markdown table element.
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
}
