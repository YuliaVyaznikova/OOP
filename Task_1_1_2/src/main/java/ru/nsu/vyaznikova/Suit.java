package ru.nsu.vyaznikova;

/**
 * Enum representing the possible suits of a playing card in a standard deck.
 */
public enum Suit {
    /**
     * Hearts.
     */
    HEARTS,
    /**
     * Diamonds.
     */
    DIAMONDS,
    /**
     * Clubs.
     */
    CLUBS,
    /**
     * Spades.
     */
    SPADES;

    /**
     * Returns the Russian name of the suit.
     *
     * @return The Russian name of the suit.
     */
    public String getRussianName() {
        switch (this) {
            case HEARTS:
                return "Черви";
            case DIAMONDS:
                return "Буби";
            case CLUBS:
                return "Крести";
            case SPADES:
                return "Пики";
            default:
                return "";
        }
    }
}