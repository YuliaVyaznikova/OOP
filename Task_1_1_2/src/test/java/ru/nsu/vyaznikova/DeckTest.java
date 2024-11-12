package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for the Deck class, ensuring its methods work as expected.
 */
public class DeckTest {

    /**
     * Tests the constructor, verifying that it creates a deck with 52 cards.
     */
    @Test
    void testConstructor() {
        Deck deck = new Deck();
        assertEquals(52, deck.cards.size());
    }

    /**
     * Tests the shuffle() method, verifying that it shuffles the deck randomly,
     * resulting in a different card order.
     */
    @Test
    void testShuffle() {
        Deck deck = new Deck();
        List<Card> originalCards = new ArrayList<>(deck.cards);
        deck.shuffle();
        assertFalse(deck.cards.equals(originalCards));
    }

    /**
     * Tests the takeCard() method, verifying that it removes and returns the top card from the deck.
     */
    @Test
    void testTakeCard() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.TWO)
        ));
        Deck deck = new Deck(cards);
        Card takenCard = deck.takeCard();
        assertEquals(Suit.HEARTS, takenCard.getSuit());
        assertEquals(Rank.ACE, takenCard.getRank());
        assertEquals(1, deck.cards.size());
    }
}