package ru.nsu.vyaznikova;

/**
 * Enum representing the possible ranks of a playing card in a standard deck.
 */
public enum Rank {
    /**
     * Two.
     */
    TWO("Двойка", 2),
    /**
     * Three.
     */
    THREE("Тройка", 3),
    /**
     * Four.
     */
    FOUR("Четвёрка", 4),
    /**
     * Five.
     */
    FIVE("Пятёрка", 5),
    /**
     * Six.
     */
    SIX("Шестёрка", 6),
    /**
     * Seven.
     */
    SEVEN("Семёрка", 7),
    /**
     * Eight.
     */
    EIGHT("Восьмёрка", 8),
    /**
     * Nine.
     */
    NINE("Девятка", 9),
    /**
     * Ten.
     */
    TEN("Десятка", 10),
    /**
     * Jack.
     */
    JACK("Валет", 10),
    /**
     * Queen.
     */
    QUEEN("Дама", 10),
    /**
     * King.
     */
    KING("Король", 10),
    /**
     * Ace.
     */
    ACE("Туз", 11);

    private final String name;
    private final int value;

    /**
     * Constructs a new Rank enum constant with the specified name and value.
     *
     * @param name  The name of the rank.
     * @param value The value of the rank.
     */
    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the rank.
     *
     * @return The name of the rank.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the rank.
     *
     * @return The value of the rank.
     */
    public int getValue() {
        return value;
    }
}