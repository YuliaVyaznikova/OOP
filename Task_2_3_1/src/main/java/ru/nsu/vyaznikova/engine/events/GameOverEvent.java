package ru.nsu.vyaznikova.engine.events;

/**
 * Событие окончания игры.
 * Содержит информацию о причине окончания игры.
 */
public class GameOverEvent implements Event {
    private final String reason;

    public GameOverEvent(String reason) {
        this.reason = reason;
    }

    @Override
    public String getEventType() {
        return "GAME_OVER";
    }

    public String getReason() {
        return reason;
    }
}
