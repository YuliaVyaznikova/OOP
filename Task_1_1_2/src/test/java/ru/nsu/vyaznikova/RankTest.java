package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the Rank enum, ensuring its methods work as expected.
 */
public class RankTest {

    /**
     * Tests the getName() method, verifying that it returns the correct name for the Rank.
     */
    @Test
    void testGetName() {
        assertEquals("Валет", Rank.JACK.getName());
    }

    /**
     * Tests the getValue() method, verifying that it returns the correct value for the Rank.
     */
    @Test
    void testGetValue() {
        assertEquals(10, Rank.KING.getValue());
        assertEquals(11, Rank.ACE.getValue());
    }
}