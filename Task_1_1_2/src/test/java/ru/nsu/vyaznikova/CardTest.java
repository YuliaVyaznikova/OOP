package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    void testGetSuit() {
        Card card = new Card(Suit.HEARTS, Rank.KING);
        assertEquals(Suit.HEARTS, card.getSuit());
    }

    @Test
    void testGetRank() {
        Card card = new Card(Suit.DIAMONDS, Rank.ACE);
        assertEquals(Rank.ACE, card.getRank());
    }

    @Test
    void testToString() {
        Card card = new Card(Suit.SPADES, Rank.TEN);
        assertEquals("Десятка Пики", card.toString());
    }
}