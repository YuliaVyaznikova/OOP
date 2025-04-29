package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие перемещения змейки.
 * Содержит информацию о старой и новой позициях головы змейки, а также о старой позиции хвоста.
 */
public class SnakeMovedEvent implements Event {
    private final Position oldHeadPosition;
    private final Position newHeadPosition;
    private final Position oldTailPosition;

    public SnakeMovedEvent(Position oldHeadPosition, Position newHeadPosition, Position oldTailPosition) {
        this.oldHeadPosition = oldHeadPosition;
        this.newHeadPosition = newHeadPosition;
        this.oldTailPosition = oldTailPosition;
    }

    @Override
    public String getEventType() {
        return "SNAKE_MOVED";
    }

    public Position getOldHeadPosition() {
        return oldHeadPosition;
    }

    public Position getNewHeadPosition() {
        return newHeadPosition;
    }

    public Position getOldTailPosition() {
        return oldTailPosition;
    }

    @Override
    public String toString() {
        return String.format("SnakeMovedEvent{from (%d, %d) to (%d, %d), tail was at (%d, %d)}", 
            oldHeadPosition.x(), oldHeadPosition.y(),
            newHeadPosition.x(), newHeadPosition.y(),
            oldTailPosition.x(), oldTailPosition.y());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SnakeMovedEvent that = (SnakeMovedEvent) obj;
        return oldHeadPosition.equals(that.oldHeadPosition) && 
               newHeadPosition.equals(that.newHeadPosition) &&
               oldTailPosition.equals(that.oldTailPosition);
    }

    @Override
    public int hashCode() {
        int result = oldHeadPosition.hashCode();
        result = 31 * result + newHeadPosition.hashCode();
        result = 31 * result + oldTailPosition.hashCode();
        return result;
    }
}
