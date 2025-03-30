package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие движения змейки.
 * Содержит информацию о новой позиции головы и старой позиции хвоста.
 */
public class SnakeMovedEvent implements Event {
    private final Position newHead;
    private final Position oldTail;

    public SnakeMovedEvent(Position newHead, Position oldTail) {
        this.newHead = newHead;
        this.oldTail = oldTail;
    }

    @Override
    public String getEventType() {
        return "SNAKE_MOVED";
    }

    public Position getNewHead() {
        return newHead;
    }

    public Position getOldTail() {
        return oldTail;
    }
}
