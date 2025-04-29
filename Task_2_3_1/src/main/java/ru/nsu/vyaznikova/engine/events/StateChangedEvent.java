package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.game.GameState;

/**
 * Событие изменения состояния игры.
 */
public class StateChangedEvent implements Event {
    private final GameState oldState;
    private final GameState newState;

    /**
     * Создает новое событие изменения состояния.
     *
     * @param oldState предыдущее состояние
     * @param newState новое состояние
     */
    public StateChangedEvent(GameState oldState, GameState newState) {
        this.oldState = oldState;
        this.newState = newState;
    }

    /**
     * @return предыдущее состояние
     */
    public GameState getOldState() {
        return oldState;
    }

    /**
     * @return новое состояние
     */
    public GameState getNewState() {
        return newState;
    }

    @Override
    public String getEventType() {
        return "STATE_CHANGED";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateChangedEvent that = (StateChangedEvent) o;
        return oldState == that.oldState && newState == that.newState;
    }

    @Override
    public int hashCode() {
        int result = oldState != null ? oldState.hashCode() : 0;
        result = 31 * result + (newState != null ? newState.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StateChangedEvent{" +
                "oldState=" + oldState +
                ", newState=" + newState +
                '}';
    }
}
