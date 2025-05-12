package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
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
