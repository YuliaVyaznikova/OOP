package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SuitTest {

    @Test
    void testGetRussianName() {
        assertEquals("Черви", Suit.HEARTS.getRussianName());
        assertEquals("Буби", Suit.DIAMONDS.getRussianName());
        assertEquals("Крести", Suit.CLUBS.getRussianName());
        assertEquals("Пики", Suit.SPADES.getRussianName());
    }
}