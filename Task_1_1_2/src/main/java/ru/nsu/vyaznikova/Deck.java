package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of playing cards used in a Blackjack game.
 */
public class Deck {

    /**
     * The list of cards in the deck.
     */
    List<Card> cards;

    /**
     * Constructs a new Deck object with a full set of 52 cards.
     */
    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Constructs a new Deck object with the specified list of cards.
     *
     * @param cards The list of cards to be used in the deck.
     */
    Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     * Shuffles the deck of cards randomly.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Takes the top card from the deck.
     *
     * @return The top card from the deck.
     */
    public Card takeCard() {
        return cards.remove(0);
    }
}