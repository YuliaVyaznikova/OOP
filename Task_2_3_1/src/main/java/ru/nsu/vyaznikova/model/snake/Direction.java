package ru.nsu.vyaznikova.model.snake;

/**
 * Перечисление направлений движения змейки, отвечающее за:
 * - Определение возможных направлений движения (UP, DOWN, LEFT, RIGHT)
 * - Хранение смещений по осям для каждого направления
 * - Проверку противоположных направлений для предотвращения разворота на 180 градусов
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public boolean isOpposite(Direction other) {
        return this.dx == -other.dx && this.dy == -other.dy;
    }
}
