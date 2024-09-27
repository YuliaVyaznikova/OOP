package ru.nsu.vyaznikova;

/**
 * Represents a human player in a Blackjack game.
 * Inherits from the Player class, providing specific human player-related behavior.
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a new HumanPlayer object with the specified deck.
     *
     * @param deck The deck of cards used in the game.
     */
    HumanPlayer(Deck deck) {
        super(deck);
    }
}