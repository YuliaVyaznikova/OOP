package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testParseNumbersValidInput() {
        String[] args = {"master", "1", "2", "3", "4", "5"};
        int[] numbers = Main.parseNumbers(args);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, numbers);
    }

    @Test
    void testParseNumbersEmptyInput() {
        String[] args = {"master"};
        assertThrows(IllegalArgumentException.class, () -> Main.parseNumbers(args));
    }

    @Test
    void testParseNumbersInvalidInput() {
        String[] args = {"master", "1", "2", "not_a_number", "4"};
        assertThrows(NumberFormatException.class, () -> Main.parseNumbers(args));
    }

    @Test
    void testParseNumbersNegativeNumbers() {
        String[] args = {"master", "1", "-2", "3"};
        int[] numbers = Main.parseNumbers(args);
        assertArrayEquals(new int[]{1, -2, 3}, numbers);
    }

    @Test
    void testParseNumbersLargeNumbers() {
        String[] args = {"master", "2147483647", "-2147483648"};
        int[] numbers = Main.parseNumbers(args);
        assertArrayEquals(new int[]{2147483647, -2147483648}, numbers);
    }
}
