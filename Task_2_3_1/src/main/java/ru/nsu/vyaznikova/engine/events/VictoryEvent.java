package ru.nsu.vyaznikova.engine.events;

/**
 * Событие победы в игре.
 */
public class VictoryEvent implements Event {
    private final int finalLength;

    /**
     * Создает новое событие победы.
     *
     * @param finalLength итоговая длина змейки
     */
    public VictoryEvent(int finalLength) {
        this.finalLength = finalLength;
    }

    /**
     * @return итоговая длина змейки
     */
    public int getFinalLength() {
        return finalLength;
    }

    @Override
    public String getEventType() {
        return "VICTORY";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VictoryEvent that = (VictoryEvent) o;
        return finalLength == that.finalLength;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(finalLength);
    }

    @Override
    public String toString() {
        return "VictoryEvent{" +
                "finalLength=" + finalLength +
                '}';
    }
}
