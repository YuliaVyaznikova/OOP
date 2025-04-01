package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.game.GameModel;

/**
 * Интерфейс для стратегий поведения змеек-роботов.
 */
public interface SnakeStrategy {
    /**
     * Определяет следующее направление движения для змейки-робота.
     *
     * @param currentPosition текущая позиция головы змейки
     * @param gameModel текущее состояние игры
     * @return направление, в котором должна двигаться змейка
     */
    Direction getNextMove(Position currentPosition, GameModel gameModel);
}
