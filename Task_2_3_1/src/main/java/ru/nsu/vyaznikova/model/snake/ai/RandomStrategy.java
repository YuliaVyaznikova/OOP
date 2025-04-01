package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.game.GameStateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Стратегия случайного движения - змейка двигается в случайном направлении,
 * избегая столкновений.
 */
public class RandomStrategy implements SnakeStrategy {
    private final Random random = new Random();
    private Direction lastDirection = Direction.RIGHT;

    @Override
    public Direction getNextMove(Position currentPosition, GameStateView gameState) {
        // Список возможных направлений
        List<Direction> possibleDirections = new ArrayList<>();

        // Проверяем все направления
        for (Direction direction : Direction.values()) {
            // Не двигаемся в противоположном направлении
            if (direction.dx == -lastDirection.dx && direction.dy == -lastDirection.dy) {
                continue;
            }

            Position newPosition = new Position(
                currentPosition.x() + direction.dx,
                currentPosition.y() + direction.dy
            );

            // Проверяем, что новая позиция безопасна
            if (isSafeMove(newPosition, gameState)) {
                possibleDirections.add(direction);
            }
        }

        // Если есть безопасные направления, выбираем случайное из них
        if (!possibleDirections.isEmpty()) {
            lastDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
            return lastDirection;
        }

        // Если безопасных направлений нет, продолжаем двигаться в текущем направлении
        return lastDirection;
    }

    private boolean isSafeMove(Position position, GameStateView gameState) {
        return !gameState.isOutOfBounds(position) && !gameState.checkCollision(position, -1);
    }
}
