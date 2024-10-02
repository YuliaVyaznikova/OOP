package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the Player class, ensuring its methods work as expected.
 */
public class PlayerTest {

    /**
     * Tests the takeCard() method, verifying that it adds a card to the player's hand.
     */
    @Test
    void testTakeCard() {
        Deck deck = new Deck();
        Player player = new Player(deck) {}; // Uses an anonymous class for testing
        player.takeCard();
        assertEquals(1, player.getCards().size());
    }

    /**
     * Tests the getCards() method, verifying that it returns the correct hand of cards.
     */
    @Test
    void testGetCards() {
        List<Card> expectedCards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.TWO)
        ));
        Player player = new Player(new Deck()) {}; // Uses an anonymous class
        player.cards = expectedCards;
        assertEquals(expectedCards, player.getCards());
    }

    /**
     * Tests the calculateScore() method with a hand that doesn't contain Aces,
     * verifying that the score is calculated correctly.
     */
    @Test
    void testCalculateScore_NoAces() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.DIAMONDS, Rank.JACK),
                new Card(Suit.CLUBS, Rank.SIX)
        ));
        Player player = new Player(new Deck()) {};
        player.cards = cards;
        assertEquals(26, player.calculateScore());
    }

    /**
     * Tests the calculateScore() method with a hand that contains one Ace,
     * verifying that the Ace is valued as 11 if it doesn't cause a bust.
     */
    @Test
    void testCalculateScore_OneAce() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.TEN)
        ));
        Player player = new Player(new Deck()) {};
        player.cards = cards;
        assertEquals(21, player.calculateScore());
    }

    /**
     * Tests the calculateScore() method with a hand that contains multiple Aces,
     * verifying that enough Aces are valued as 1 to avoid a bust.
     */
    @Test
    void testCalculateScore_MultipleAces() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.SEVEN)
        ));
        Player player = new Player(new Deck()) {};
        player.cards = cards;
        assertEquals(19, player.calculateScore());
    }

    /**
     * Tests the calculateScore() method with a hand that would bust with an Ace valued as 11,
     * verifying that the Ace is valued as 1 to avoid a bust.
     */
    @Test
    void testCalculateScore_BustWithAce() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.KING),
                new Card(Suit.CLUBS, Rank.SIX)
        ));
        Player player = new Player(new Deck()) {};
        player.cards = cards;
        assertEquals(17, player.calculateScore());
    }
}