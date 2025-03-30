package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.game.GameState;

/**
 * Событие изменения состояния игры.
 * Содержит информацию о старом и новом состояниях игры.
 */
public class StateChangedEvent implements Event {
    private final GameState oldState;
    private final GameState newState;

    public StateChangedEvent(GameState oldState, GameState newState) {
        this.oldState = oldState;
        this.newState = newState;
    }

    @Override
    public String getEventType() {
        return "STATE_CHANGED";
    }

    public GameState getOldState() {
        return oldState;
    }

    public GameState getNewState() {
        return newState;
    }

    @Override
    public String toString() {
        return String.format("StateChangedEvent{from %s to %s}", oldState, newState);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StateChangedEvent that = (StateChangedEvent) obj;
        return oldState == that.oldState && newState == that.newState;
    }

    @Override
    public int hashCode() {
        int result = oldState.hashCode();
        result = 31 * result + newState.hashCode();
        return result;
    }
}
