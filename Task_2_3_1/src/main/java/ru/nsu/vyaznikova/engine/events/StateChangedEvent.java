package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.game.GameState;

/**
 * Событие изменения состояния игры.
 * Содержит информацию о старом и новом состоянии.
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
}
