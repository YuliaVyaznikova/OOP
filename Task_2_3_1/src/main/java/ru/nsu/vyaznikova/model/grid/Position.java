package ru.nsu.vyaznikova.model.grid;

import ru.nsu.vyaznikova.model.snake.Direction;

/**
 * Record для представления позиции на игровом поле, отвечающий за:
 * - Хранение координат x и y
 * - Неизменяемость позиции (immutability)
 * - Создание новых позиций при перемещении
 * - Корректное сравнение позиций
 */
public record Position(int x, int y) {
    public Position translate(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public Position translate(Direction direction) {
        return translate(direction.dx, direction.dy);
    }
}
