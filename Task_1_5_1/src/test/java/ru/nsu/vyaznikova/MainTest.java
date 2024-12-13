package ru.nsu.vyaznikova;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Main class.
 * 
 * These tests verify the functionality of the main method which creates and displays a formatted Markdown table with random values.
 */
public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up the test environment before each test.
     * 
     * Redirects System.out to capture output for verification.
     */
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Restores the original System.out after each test.
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Tests the main method's table generation.
     * 
     * Verifies that:
     * 1. The table has the correct header
     * 2. The table contains 5 data rows
     * 3. The table follows Markdown table format
     * 4. Numbers are properly aligned
     * 5. Values greater than 5 are bold
     */
    @Test
    public void testMainOutput() {
        Main.main(new String[]{});

        String output = outContent.toString().trim();
        String[] lines = output.split("\n");

        assertTrue(lines.length >= 4, "Table should have at least 4 lines");

        assertTrue(lines[0].contains("| Index | Random |"), "First line should contain column headers");
        assertTrue(lines[1].contains("|--:|:--|"), "Second line should contain alignment markers");

        assertEquals(7, lines.length, "Table should have 7 lines total (header + separator + 5 data rows)");

        Pattern rowPattern = Pattern.compile("^\\| *\\d+ *\\| .*\\|$");
        for (int i = 2; i < lines.length; i++) {
            assertTrue(rowPattern.matcher(lines[i]).matches(), "Data row should match expected format: " + lines[i]);
        }

        for (int i = 2; i < lines.length; i++) {
            String[] cells = lines[i].split("\\|");
            assertTrue(cells[1].trim().matches("\\d+"), "First cell should be a number");

            String valueCell = cells[2].trim();
            if (valueCell.startsWith("**")) {
                assertTrue(valueCell.matches("\\*\\*[6-9]\\*\\*"), "Bold values should be greater than 5");
            } else {
                int value = Integer.parseInt(valueCell);
                assertTrue(value <= 5, "Non-bold values should be 5 or less");
            }
        }
    }

    /**
     * Tests that the main method handles empty arguments array.
     * 
     * Verifies that:
     * 1. No exceptions are thrown with empty args
     * 2. The program executes successfully
     */
    @Test
    public void testMainWithEmptyArgs() {
        assertDoesNotThrow(() -> Main.main(new String[]{}), "Main should handle empty arguments array");
    }

    /**
     * Tests that the main method handles null arguments array.
     * 
     * Verifies that:
     * 1. No exceptions are thrown with null args
     * 2. The program executes successfully
     */
    @Test
    public void testMainWithNullArgs() {
        assertDoesNotThrow(() -> Main.main(null), "Main should handle null arguments array");
    }

    /**
     * Tests the consistency of table formatting.
     * 
     * Verifies that:
     * 1. Multiple runs produce tables with the same structure
     * 2. All tables have correct alignment markers
     * 3. All tables have the same number of rows
     */
    @Test
    public void testTableFormatConsistency() {
        for (int i = 0; i < 5; i++) {
            outContent.reset();
            Main.main(new String[]{});
            String output = outContent.toString().trim();
            String[] lines = output.split("\n");

            assertEquals(7, lines.length, "Table should always have 7 lines");
            assertEquals("| Index | Random |", lines[0].trim(), "Header should be consistent");
            assertEquals("|--:|:--|", lines[1].trim(), "Alignment markers should be consistent");
        }
    }
}
