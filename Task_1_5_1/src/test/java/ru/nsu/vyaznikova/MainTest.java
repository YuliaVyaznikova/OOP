package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit tests for the Main class.
 */
public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up the test environment before each test.
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
     * Tests that the main method handles empty arguments array.
     */
    @Test
    public void testMainWithEmptyArgs() {
        assertDoesNotThrow(() -> Main.main(new String[]{}),
                "Main should handle empty arguments array");
    }

    /**
     * Tests that the main method handles null arguments array.
     */
    @Test
    public void testMainWithNullArgs() {
        assertDoesNotThrow(() -> Main.main(null),
                "Main should handle null arguments array");
    }
}
