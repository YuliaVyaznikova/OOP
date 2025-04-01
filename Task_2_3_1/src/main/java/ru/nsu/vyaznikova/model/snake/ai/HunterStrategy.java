package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;

/**
 * Стратегия "охотника" - змейка преследует голову игрока.
 */
public class HunterStrategy implements SnakeStrategy {
    private Direction lastDirection = Direction.RIGHT;

    @Override
    public Direction getNextMove(Position currentPosition, GameModel gameModel) {
        // Ищем голову змейки игрока
        Position targetPosition = null;
        for (int y = 0; y < gameModel.getHeight(); y++) {
            for (int x = 0; x < gameModel.getWidth(); x++) {
                if (gameModel.getCellType(x, y) == CellType.SNAKE_HEAD) {
                    targetPosition = new Position(x, y);
                    break;
                }
            }
            if (targetPosition != null) break;
        }

        if (targetPosition == null) {
            return lastDirection; // Если цель не найдена, продолжаем двигаться в текущем направлении
        }

        // Вычисляем разницу в координатах
        int dx = targetPosition.x() - currentPosition.x();
        int dy = targetPosition.y() - currentPosition.y();

        // Пробуем двигаться в направлении цели
        Direction[] possibleDirections = new Direction[2];
        if (Math.abs(dx) > Math.abs(dy)) {
            // Приоритет горизонтального движения
            possibleDirections[0] = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            possibleDirections[1] = dy > 0 ? Direction.DOWN : Direction.UP;
        } else {
            // Приоритет вертикального движения
            possibleDirections[0] = dy > 0 ? Direction.DOWN : Direction.UP;
            possibleDirections[1] = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        // Проверяем возможные направления
        for (Direction direction : possibleDirections) {
            // Не двигаемся в противоположном направлении
            if (direction.dx == -lastDirection.dx && direction.dy == -lastDirection.dy) {
                continue;
            }

            Position newPosition = new Position(
                currentPosition.x() + direction.dx,
                currentPosition.y() + direction.dy
            );

            // Проверяем, что новая позиция безопасна
            if (!gameModel.checkCollision(newPosition, -1)) {
                lastDirection = direction;
                return direction;
            }
        }

        // Если все предпочтительные направления заблокированы, 
        // пробуем любое доступное направление
        for (Direction direction : Direction.values()) {
            if (direction.dx == -lastDirection.dx && direction.dy == -lastDirection.dy) {
                continue;
            }

            Position newPosition = new Position(
                currentPosition.x() + direction.dx,
                currentPosition.y() + direction.dy
            );

            if (!gameModel.checkCollision(newPosition, -1)) {
                lastDirection = direction;
                return direction;
            }
        }

        // Если все направления заблокированы, продолжаем двигаться в текущем направлении
        return lastDirection;
    }
}
