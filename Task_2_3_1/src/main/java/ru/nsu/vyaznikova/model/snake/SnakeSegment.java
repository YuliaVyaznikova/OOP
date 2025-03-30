package ru.nsu.vyaznikova.model.snake;

import ru.nsu.vyaznikova.model.grid.Position;

public class SnakeSegment {
    private final Position position;
    private SnakeSegment next;
    private SnakeSegment previous;

    public SnakeSegment(Position position) {
        this.position = position;
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
}
