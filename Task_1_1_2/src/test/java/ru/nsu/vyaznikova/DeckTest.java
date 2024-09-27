package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeckTest {

    @Test
    void testConstructor() {
        Deck deck = new Deck();
        assertEquals(52, deck.cards.size());
    }

    @Test
    void testShuffle() {
        Deck deck = new Deck();
        List<Card> originalCards = new ArrayList<>(deck.cards);
        deck.shuffle();
        assertFalse(deck.cards.equals(originalCards));
    }

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