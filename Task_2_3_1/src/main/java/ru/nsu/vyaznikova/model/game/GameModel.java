/**
 * Модель игры, отвечающая за:
 * - Создание и управление игровым полем (grid[][])
 * - Инициализацию змейки в центре поля
 * - Создание стен по периметру
 * - Размещение еды на поле
 * - Управление состоянием игры (running/game over/victory)
 * - Обработку столкновений и взаимодействий
 * - Обновление состояния игры в игровом цикле
 */
package ru.nsu.vyaznikova.model.game;

import ru.nsu.vyaznikova.engine.events.EventBus;
import ru.nsu.vyaznikova.engine.events.*;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.snake.Snake;

import java.util.Random;

public class GameModel {
    private final int width;
    private final int height;
    private final int targetLength;
    private final CellType[][] grid;
    private final Random random;
    private Snake snake;
    private Direction currentDirection;
    private Position food;
    private GameState gameState;

    public GameModel(int width, int height, int targetLength) {
        this.width = width;
        this.height = height;
        this.targetLength = targetLength;
        this.grid = new CellType[height][width];
        this.random = new Random();
        this.gameState = GameState.RUNNING;
        
        initializeGrid();
        initializeSnake();
        spawnFood();
    }

    private void initializeGrid() {
        // Заполняем поле пустыми клетками
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    grid[y][x] = CellType.WALL;
                } else {
                    grid[y][x] = CellType.EMPTY;
                }
            }
        }
    }

    private void initializeSnake() {
        int startX = width / 2;
        int startY = height / 2;
        snake = new Snake(new Position(startX, startY));
        grid[startY][startX] = CellType.SNAKE_HEAD;
    }

    public void update() {
        if (gameState != GameState.RUNNING || currentDirection == null) {
            return;
        }

        Position head = snake.getHeadPosition();
        Position newHead = new Position(
            head.x() + currentDirection.dx,
            head.y() + currentDirection.dy
        );

        // Проверяем столкновения
        if (checkCollision(newHead)) {
            gameState = GameState.GAME_OVER;
            EventBus.getInstance().publish(new GameOverEvent(newHead));
            return;
        }

        // Очищаем старые позиции змейки
        Position oldTail = null;
        for (Position pos : snake.getBody()) {
            grid[pos.y()][pos.x()] = CellType.EMPTY;
            oldTail = pos;
        }

        // Проверяем еду
        boolean growing = grid[newHead.y()][newHead.x()] == CellType.FOOD;
        if (growing) {
            snake.grow(newHead);
            Position eatenFoodPos = food;
            spawnFood();
            EventBus.getInstance().publish(new FoodEatenEvent(eatenFoodPos, food));

            // Проверяем победу
            if (snake.getLength() >= targetLength) {
                GameState oldState = this.gameState;
                gameState = GameState.VICTORY;
                EventBus.getInstance().publish(new VictoryEvent(snake.getLength()));
                EventBus.getInstance().publish(new StateChangedEvent(oldState, gameState));
                return;
            }
        } else {
            snake.move(newHead);
        }

        // Обновляем позиции змейки на поле
        for (Position pos : snake.getBody()) {
            grid[pos.y()][pos.x()] = CellType.SNAKE_BODY;
        }
        grid[newHead.y()][newHead.x()] = CellType.SNAKE_HEAD;

        // Публикуем событие о движении змейки
        EventBus.getInstance().publish(new SnakeMovedEvent(head, newHead, oldTail));
    }

    private boolean checkCollision(Position pos) {
        if (isOutOfBounds(pos)) {
            return true;
        }

        CellType cellType = grid[pos.y()][pos.x()];
        return cellType == CellType.WALL || cellType == CellType.SNAKE_BODY;
    }

    private boolean isOutOfBounds(Position pos) {
        return pos.x() < 0 || pos.x() >= width || pos.y() < 0 || pos.y() >= height;
    }

    private void spawnFood() {
        // Находим все пустые клетки
        int emptyCells = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == CellType.EMPTY) {
                    emptyCells++;
                }
            }
        }

        if (emptyCells == 0) {
            return;
        }

        // Выбираем случайную пустую клетку
        int targetCell = random.nextInt(emptyCells);
        int currentCell = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == CellType.EMPTY) {
                    if (currentCell == targetCell) {
                        grid[y][x] = CellType.FOOD;
                        food = new Position(x, y);
                        return;
                    }
                    currentCell++;
                }
            }
        }
    }

    public void setDirection(Direction newDirection) {
        // Проверяем, что новое направление не противоположно текущему
        if (currentDirection != null && 
            newDirection.dx == -currentDirection.dx && 
            newDirection.dy == -currentDirection.dy) {
            return;
        }
        currentDirection = newDirection;
    }

    public CellType getCellType(int x, int y) {
        return grid[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return snake.getLength();
    }

    public GameState getGameState() {
        return gameState;
    }

    /**
     * Устанавливает новое состояние игры
     * @param newState новое состояние
     */
    public void setGameState(GameState newState) {
        if (this.gameState != newState) {
            GameState oldState = this.gameState;
            this.gameState = newState;
            EventBus.getInstance().publish(new StateChangedEvent(oldState, newState));
        }
    }
}
