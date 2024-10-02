package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test class for the Card class, ensuring its methods work as expected.
 */
public class CardTest {

    /**
     * Tests the getSuit() method, verifying that it returns the correct suit.
     */
    @Test
    void testGetSuit() {
        Card card = new Card(Suit.HEARTS, Rank.KING);
        assertEquals(Suit.HEARTS, card.getSuit());
    }

    /**
     * Tests the getRank() method, verifying that it returns the correct rank.
     */
    @Test
    void testGetRank() {
        Card card = new Card(Suit.DIAMONDS, Rank.ACE);
        assertEquals(Rank.ACE, card.getRank());
    }

    /**
     * Tests the toString() method, verifying that it returns the correct string representation of the card.
     */
    @Test
    void testToString() {
        Card card = new Card(Suit.SPADES, Rank.TEN);
        assertEquals("Десятка Пики", card.toString());
    }
}