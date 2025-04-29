package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.game.GameStateView;

/**
 * Интерфейс для стратегий поведения змеек-роботов.
 */
public interface SnakeStrategy {
    /**
     * Определяет следующее направление движения для змейки-робота.
     *
     * @param currentPosition текущая позиция головы змейки
     * @param gameState текущее состояние игры (только для чтения)
     * @return направление, в котором должна двигаться змейка
     */
    Direction getNextMove(Position currentPosition, GameStateView gameState);
}
