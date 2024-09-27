package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    void testTakeCard() {
        Deck deck = new Deck();
        Player player = new Player(deck) {}; // Используем анонимный класс для тестирования
        player.takeCard();
        assertEquals(1, player.getCards().size());
    }

    @Test
    void testGetCards() {
        List<Card> expectedCards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.TWO)
        ));
        Player player = new Player(new Deck()) {}; // Используем анонимный класс
        player.cards = expectedCards;
        assertEquals(expectedCards, player.getCards());
    }

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

    @Test
    void testCalculateScore_MultipleAces() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.DIAMONDS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.SEVEN)
        ));
        Player player = new Player(new Deck()) {};
        player.cards = cards;
        assertEquals(18, player.calculateScore());
    }

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