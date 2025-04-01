package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;

import java.util.Random;

/**
 * Стратегия случайного движения змейки.
 * Змейка случайным образом выбирает направление движения,
 * избегая столкновений со стенами и другими змейками.
 */
public class RandomStrategy implements SnakeStrategy {
    private final Random random = new Random();
    private Direction lastDirection = Direction.RIGHT;

    @Override
    public Direction getNextMove(Position currentPosition, GameModel gameModel) {
        // Список всех возможных направлений
        Direction[] directions = Direction.values();
        
        // Перемешиваем направления случайным образом
        for (int i = directions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Direction temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        // Проверяем каждое направление, начиная со случайного
        for (Direction direction : directions) {
            // Не двигаемся в противоположном направлении
            if (direction.dx == -lastDirection.dx && direction.dy == -lastDirection.dy) {
                continue;
            }

            // Проверяем новую позицию
            Position newPosition = new Position(
                currentPosition.x() + direction.dx,
                currentPosition.y() + direction.dy
            );

            // Если в этом направлении нет препятствий, используем его
            if (!gameModel.isOutOfBounds(newPosition) && !gameModel.checkCollision(newPosition, -1)) {
                lastDirection = direction;
                return direction;
            }
        }

        // Если все направления заблокированы, продолжаем двигаться в текущем направлении
        return lastDirection;
    }
}
