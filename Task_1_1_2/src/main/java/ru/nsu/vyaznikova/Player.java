package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a player in a Blackjack game.
 * Provides common functionality for both human and dealer players.
 */
public abstract class Player {

    /**
     * The player's hand of cards.
     */
    protected List<Card> cards;

    /**
     * The deck of cards used in the game.
     */
    protected Deck deck;

    /**
     * Constructs a new Player object with the specified deck.
     *
     * @param deck The deck of cards used in the game.
     */
    Player(Deck deck) {
        this.cards = new ArrayList<>();
        this.deck = deck;
    }

    /**
     * Takes a card from the deck and adds it to the player's hand.
     */
    public void takeCard() {
        cards.add(deck.takeCard());
    }

    /**
     * Returns the player's current hand of cards.
     *
     * @return The player's hand of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Calculates the player's score based on their hand of cards,
     * taking into account the special rules for Aces (1 or 11).
     *
     * @return The player's score.
     */
    public int calculateScore() {
        int score = 0;
        int aceCount = 0;
        for (Card card : cards) {
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
            score += card.getRank().getValue();
        }
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }
}