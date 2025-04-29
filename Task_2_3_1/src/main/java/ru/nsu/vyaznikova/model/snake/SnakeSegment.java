package ru.nsu.vyaznikova.model.snake;

import ru.nsu.vyaznikova.model.grid.Position;
import java.util.Objects;

/**
 * Класс, представляющий сегмент змейки.
 * Содержит позицию сегмента и ссылки на следующий и предыдущий сегменты.
 */
public class SnakeSegment {
    private final Position position;
    private SnakeSegment next;
    private SnakeSegment previous;

    /**
     * Создает новый сегмент змейки в указанной позиции.
     *
     * @param position позиция сегмента
     * @throws NullPointerException если position равен null
     */
    public SnakeSegment(Position position) {
        this.position = Objects.requireNonNull(position, "Position cannot be null");
    }

    public Position getPosition() {
        return position;
    }

    public SnakeSegment getNext() {
        return next;
    }

    public void setNext(SnakeSegment next) {
        this.next = next;
    }

    public SnakeSegment getPrevious() {
        return previous;
    }

    public void setPrevious(SnakeSegment previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SnakeSegment that = (SnakeSegment) obj;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return String.format("SnakeSegment{position=%s}", position);
    }
}
