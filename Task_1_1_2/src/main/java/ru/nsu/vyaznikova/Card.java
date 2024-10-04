package ru.nsu.vyaznikova;

/**
 * Represents a single playing card in a deck.
 */
public class Card {

    /**
     * The suit of the card.
     */
    private Suit suit;

    /**
     * The rank of the card.
     */
    private Rank rank;

    /**
     * Constructs a new Card object with the specified suit and rank.
     *
     * @param suit The suit of the card.
     * @param rank The rank of the card.
     */
    Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the suit of the card.
     *
     * @return The suit of the card.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the rank of the card.
     *
     * @return The rank of the card.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns a string representation of the card in the format "Rank Suit".
     *
     * @return A string representation of the card.
     */
    @Override
    public String toString() {
        return rank.getName() + " " + suit.getRussianName();
    }
}