package ru.nsu.vyaznikova.model.snake;

import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.ai.SnakeStrategy;
import ru.nsu.vyaznikova.model.game.GameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Змейка, управляемая искусственным интеллектом.
 */
public class AISnake {
    private final List<Position> body;
    private final SnakeStrategy strategy;
    private final GameModel gameModel;
    private Direction currentDirection;
    private final int id;

    /**
     * Создает новую змейку-робота.
     *
     * @param startPosition начальная позиция
     * @param strategy стратегия поведения
     * @param gameModel модель игры
     * @param id идентификатор змейки
     */
    public AISnake(Position startPosition, SnakeStrategy strategy, GameModel gameModel, int id) {
        this.body = new ArrayList<>();
        this.body.add(startPosition);
        this.strategy = strategy;
        this.gameModel = gameModel;
        this.id = id;
        this.currentDirection = Direction.RIGHT; // Начальное направление
    }

    /**
     * Обновляет состояние змейки.
     *
     * @return true, если змейка жива, false если произошло столкновение
     */
    public boolean update() {
        Position head = getHeadPosition();
        Direction nextMove = strategy.getNextMove(head, gameModel);
        
        if (nextMove != null) {
            currentDirection = nextMove;
        }

        Position newHead = new Position(
            head.x() + currentDirection.dx,
            head.y() + currentDirection.dy
        );

        // Проверяем столкновения
        if (gameModel.checkCollision(newHead, id)) {
            return false;
        }

        // Проверяем еду
        if (gameModel.isFoodPosition(newHead)) {
            grow(newHead);
            gameModel.onFoodEaten(newHead, id);
        } else {
            move(newHead);
        }

        return true;
    }

    /**
     * Перемещает змейку на новую позицию.
     *
     * @param newHead новая позиция головы
     */
    public void move(Position newHead) {
        body.remove(body.size() - 1); // Удаляем хвост
        body.add(0, newHead); // Добавляем новую голову
    }

    /**
     * Увеличивает длину змейки.
     *
     * @param newHead новая позиция головы
     */
    public void grow(Position newHead) {
        body.add(0, newHead);
    }

    /**
     * @return текущая позиция головы змейки
     */
    public Position getHeadPosition() {
        return body.get(0);
    }

    /**
     * @return список всех позиций тела змейки
     */
    public List<Position> getBody() {
        return new ArrayList<>(body);
    }

    /**
     * @return длина змейки
     */
    public int getLength() {
        return body.size();
    }

    /**
     * @return идентификатор змейки
     */
    public int getId() {
        return id;
    }
}
