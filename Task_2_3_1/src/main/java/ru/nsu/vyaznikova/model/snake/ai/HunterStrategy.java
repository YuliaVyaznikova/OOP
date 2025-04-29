package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.game.GameStateView;
import ru.nsu.vyaznikova.model.grid.CellType;

/**
 * Стратегия охотника - змейка преследует игрока.
 */
public class HunterStrategy implements SnakeStrategy {
    private Direction lastDirection = Direction.RIGHT;

    @Override
    public Direction getNextMove(Position currentPosition, GameStateView gameState) {
        // Ищем позицию игрока
        Position playerPosition = findPlayerPosition(gameState);
        if (playerPosition == null) {
            return lastDirection; // Если игрок не найден, продолжаем двигаться в текущем направлении
        }

        // Вычисляем разницу в координатах
        int dx = playerPosition.x() - currentPosition.x();
        int dy = playerPosition.y() - currentPosition.y();

        // Пробуем двигаться в направлении игрока
        Direction primaryDirection = null;
        Direction secondaryDirection = null;

        // Выбираем приоритетное направление
        if (Math.abs(dx) > Math.abs(dy)) {
            primaryDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            secondaryDirection = dy > 0 ? Direction.DOWN : Direction.UP;
        } else {
            primaryDirection = dy > 0 ? Direction.DOWN : Direction.UP;
            secondaryDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        // Проверяем безопасность основного направления
        Position nextPosition = new Position(
            currentPosition.x() + primaryDirection.dx,
            currentPosition.y() + primaryDirection.dy
        );

        if (isSafeMove(nextPosition, gameState)) {
            lastDirection = primaryDirection;
            return primaryDirection;
        }

        // Пробуем вторичное направление
        nextPosition = new Position(
            currentPosition.x() + secondaryDirection.dx,
            currentPosition.y() + secondaryDirection.dy
        );

        if (isSafeMove(nextPosition, gameState)) {
            lastDirection = secondaryDirection;
            return secondaryDirection;
        }

        // Если оба направления опасны, ищем любое безопасное
        for (Direction dir : Direction.values()) {
            if (dir.dx == -lastDirection.dx && dir.dy == -lastDirection.dy) {
                continue;
            }
            nextPosition = new Position(
                currentPosition.x() + dir.dx,
                currentPosition.y() + dir.dy
            );
            if (isSafeMove(nextPosition, gameState)) {
                lastDirection = dir;
                return dir;
            }
        }

        return lastDirection; // В крайнем случае идем в текущем направлении
    }

    private Position findPlayerPosition(GameStateView gameState) {
        for (int x = 0; x < gameState.getWidth(); x++) {
            for (int y = 0; y < gameState.getHeight(); y++) {
                if (gameState.getCellType(x, y) == CellType.SNAKE_HEAD) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    private boolean isSafeMove(Position position, GameStateView gameState) {
        return !gameState.isOutOfBounds(position) && !gameState.checkCollision(position, -1);
    }
}
