package ru.nsu.vyaznikova.engine.events;

/**
 * Событие победы в игре.
 * Содержит информацию о финальном счете игрока.
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

    @Override
    public String toString() {
        return String.format("VictoryEvent{score: %d}", finalScore);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VictoryEvent that = (VictoryEvent) obj;
        return finalScore == that.finalScore;
    }

    @Override
    public int hashCode() {
        return finalScore;
    }
}
