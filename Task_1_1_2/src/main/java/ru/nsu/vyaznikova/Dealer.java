package ru.nsu.vyaznikova;

/**
 * Represents the dealer in a Blackjack game.
 * Inherits from the Player class, providing specific dealer-related behavior.
 */
public class Dealer extends Player {

    /**
     * Constructs a new Dealer object with the specified deck.
     *
     * @param deck The deck of cards used in the game.
     */
    Dealer(Deck deck) {
        super(deck);
    }
}