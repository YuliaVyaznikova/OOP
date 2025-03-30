package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие окончания игры.
 * Содержит информацию о позиции столкновения, которое привело к окончанию игры.
 */
public class GameOverEvent implements Event {
    private final Position collisionPosition;

    public GameOverEvent(Position collisionPosition) {
        this.collisionPosition = collisionPosition;
    }

    @Override
    public String getEventType() {
        return "GAME_OVER";
    }

    public Position getCollisionPosition() {
        return collisionPosition;
    }

    @Override
    public String toString() {
        return String.format("GameOverEvent{collision at (%d, %d)}", 
            collisionPosition.x(), collisionPosition.y());
    }
}
