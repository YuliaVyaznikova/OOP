package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the Suit enum, ensuring its methods work as expected.
 */
public class SuitTest {

    /**
     * Tests the getRussianName() method, verifying that it returns the correct Russian name for the Suit.
     */
    @Test
    void testGetRussianName() {
        assertEquals("Черви", Suit.HEARTS.getRussianName());
        assertEquals("Буби", Suit.DIAMONDS.getRussianName());
        assertEquals("Крести", Suit.CLUBS.getRussianName());
        assertEquals("Пики", Suit.SPADES.getRussianName());
    }
}