package ru.nsu.vyaznikova.model.grid;

/**
 * Типы ячеек на игровом поле.
 */
public enum CellType {
    EMPTY,      // Пустая ячейка
    WALL,       // Стена
    FOOD,       // Еда
    SNAKE_HEAD, // Голова змейки игрока
    SNAKE_BODY, // Тело змейки игрока
    AI_SNAKE    // Змейка-робот (и голова, и тело)
}
