package ru.nsu.vyaznikova.model.game;

import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Интерфейс для предоставления безопасного доступа к состоянию игры.
 * Используется змейками-роботами для получения информации о текущем состоянии игры
 * без возможности его модификации.
 */
public interface GameStateView {
    /**
     * Получить тип ячейки по координатам.
     *
     * @param x координата x
     * @param y координата y
     * @return тип ячейки
     */
    CellType getCellType(int x, int y);

    /**
     * Проверить, находится ли позиция за пределами игрового поля.
     *
     * @param position проверяемая позиция
     * @return true, если позиция за пределами поля
     */
    boolean isOutOfBounds(Position position);

    /**
     * Проверить наличие коллизии в указанной позиции.
     *
     * @param position проверяемая позиция
     * @param snakeId ID змейки, для которой проверяется коллизия
     * @return true, если в позиции есть коллизия
     */
    boolean checkCollision(Position position, int snakeId);

    /**
     * Получить ширину игрового поля.
     *
     * @return ширина поля
     */
    int getWidth();

    /**
     * Получить высоту игрового поля.
     *
     * @return высота поля
     */
    int getHeight();
}
