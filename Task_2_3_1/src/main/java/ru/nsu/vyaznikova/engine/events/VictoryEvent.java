package ru.nsu.vyaznikova.engine.events;

/**
 * Событие победы в игре.
 * Содержит информацию о финальном счете.
 */
public class VictoryEvent implements Event {
    private final int finalScore;

    public VictoryEvent(int finalScore) {
        this.finalScore = finalScore;
    }

    @Override
    public String getEventType() {
        return "VICTORY";
    }

    public int getFinalScore() {
        return finalScore;
    }
}
