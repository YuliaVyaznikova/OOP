package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RankTest {

    @Test
    void testGetName() {
        assertEquals("Валет", Rank.JACK.getName());
    }

    @Test
    void testGetValue() {
        assertEquals(10, Rank.KING.getValue());
        assertEquals(11, Rank.ACE.getValue());
    }
}