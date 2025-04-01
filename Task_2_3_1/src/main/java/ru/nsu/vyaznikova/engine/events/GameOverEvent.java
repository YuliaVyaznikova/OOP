package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие окончания игры.
 */
public class GameOverEvent implements Event {
    private final Position position;

    /**
     * Создает новое событие окончания игры.
     *
     * @param position позиция, где произошло столкновение
     */
    public GameOverEvent(Position position) {
        this.position = position;
    }

    /**
     * @return позиция, где произошло столкновение
     */
    public Position getPosition() {
        return position;
    }

    @Override
    public String getEventType() {
        return "GAME_OVER";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameOverEvent that = (GameOverEvent) o;
        return position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    @Override
    public String toString() {
        return "GameOverEvent{" +
                "position=" + position +
                '}';
    }
}
