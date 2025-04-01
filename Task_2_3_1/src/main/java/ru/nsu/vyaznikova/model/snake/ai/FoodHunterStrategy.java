package ru.nsu.vyaznikova.model.snake.ai;

import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;

/**
 * Стратегия "охотника за едой" - змейка ищет ближайшую еду и движется к ней.
 */
public class FoodHunterStrategy implements SnakeStrategy {
    private Direction lastDirection = Direction.RIGHT;

    @Override
    public Direction getNextMove(Position currentPosition, GameModel gameModel) {
        // Ищем ближайшую еду
        Position nearestFood = findNearestFood(currentPosition, gameModel);
        
        if (nearestFood == null) {
            // Если еда не найдена, продолжаем двигаться в текущем направлении
            return lastDirection;
        }

        // Вычисляем разницу в координатах
        int dx = nearestFood.x() - currentPosition.x();
        int dy = nearestFood.y() - currentPosition.y();

        // Выбираем приоритетное направление движения
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

        // Если предпочтительные направления заблокированы, ищем любое безопасное
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

    /**
     * Находит ближайшую еду на поле.
     */
    private Position findNearestFood(Position current, GameModel gameModel) {
        Position nearest = null;
        int minDistance = Integer.MAX_VALUE;

        // Сканируем все поле в поисках еды
        for (int y = 0; y < gameModel.getHeight(); y++) {
            for (int x = 0; x < gameModel.getWidth(); x++) {
                if (gameModel.getCellType(x, y) == CellType.FOOD) {
                    Position foodPos = new Position(x, y);
                    int distance = calculateManhattanDistance(current, foodPos);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = foodPos;
                    }
                }
            }
        }

        return nearest;
    }

    /**
     * Вычисляет манхэттенское расстояние между двумя точками.
     */
    private int calculateManhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }
}
