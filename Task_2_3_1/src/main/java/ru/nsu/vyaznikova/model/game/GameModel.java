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
import ru.nsu.vyaznikova.model.snake.AISnake;
import ru.nsu.vyaznikova.model.snake.ai.SnakeStrategy;
import ru.nsu.vyaznikova.model.snake.ai.HunterStrategy;
import ru.nsu.vyaznikova.model.snake.ai.RandomStrategy;
import ru.nsu.vyaznikova.model.snake.ai.FoodHunterStrategy;

import java.util.*;

public class GameModel implements GameStateView {
    private final int width;
    private final int height;
    private final int targetLength;
    private final int foodCount;
    private final CellType[][] grid;
    private final Random random;
    private Snake playerSnake;
    private final List<AISnake> aiSnakes;
    private Direction currentDirection;
    private final List<Position> foodPositions;
    private GameState gameState;
    private final Map<Integer, CellType> snakeIdToCellType;
    private int nextSnakeId;
    private boolean isPaused;

    public GameModel(int width, int height, int targetLength, int foodCount) {
        this.width = width;
        this.height = height;
        this.targetLength = targetLength;
        this.foodCount = foodCount;
        this.grid = new CellType[height][width];
        this.random = new Random();
        this.gameState = GameState.RUNNING;
        this.aiSnakes = new ArrayList<>();
        this.foodPositions = new ArrayList<>();
        this.snakeIdToCellType = new HashMap<>();
        this.nextSnakeId = 1; // 0 зарезервирован для игрока
        this.isPaused = false;
        
        initializeGrid();
        initializeSnake();
        spawnInitialFood();
    }

    public GameModel(int width, int height, int targetLength) {
        this(width, height, targetLength, 1); // По умолчанию 1 элемент еды
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
        playerSnake = new Snake(new Position(startX, startY), 0);
        currentDirection = null; // Начальное направление не задано
        updateSnakeOnGrid(playerSnake.getBody(), CellType.SNAKE_BODY, CellType.SNAKE_HEAD);
        snakeIdToCellType.put(0, CellType.SNAKE_HEAD); // Игрок всегда имеет id = 0

        // Инициализируем змеек-роботов
        initializeAISnakes();
    }

    private void initializeAISnakes() {
        // Создаем змейку-охотника, которая следует за игроком
        AISnake hunterSnake = new AISnake(
            new Position(width / 4, height / 4),
            new HunterStrategy(),
            this,
            aiSnakes.size() + 2
        );
        aiSnakes.add(hunterSnake);
        snakeIdToCellType.put(hunterSnake.getId(), CellType.AI_SNAKE);

        // Создаем змейку со случайным движением
        AISnake randomSnake = new AISnake(
            new Position(width * 3 / 4, height / 4),
            new RandomStrategy(),
            this,
            aiSnakes.size() + 2
        );
        aiSnakes.add(randomSnake);
        snakeIdToCellType.put(randomSnake.getId(), CellType.AI_SNAKE);

        // Создаем змейку-охотника за едой
        AISnake foodHunterSnake = new AISnake(
            new Position(width / 2, height * 3 / 4),
            new FoodHunterStrategy(),
            this,
            aiSnakes.size() + 2
        );
        aiSnakes.add(foodHunterSnake);
        snakeIdToCellType.put(foodHunterSnake.getId(), CellType.AI_SNAKE);
    }

    public void addAISnake(Position startPosition, SnakeStrategy strategy) {
        int id = nextSnakeId++;
        AISnake aiSnake = new AISnake(startPosition, strategy, this, id);
        aiSnakes.add(aiSnake);
        
        // Добавляем новый тип ячейки для этой змейки
        CellType snakeType = CellType.AI_SNAKE;
        snakeIdToCellType.put(id, snakeType);
        
        // Размещаем змейку на поле
        updateSnakeOnGrid(aiSnake.getBody(), snakeType, snakeType);
    }

    private void spawnInitialFood() {
        for (int i = 0; i < foodCount; i++) {
            spawnFood();
        }
    }

    public void update() {
        if (gameState != GameState.RUNNING || isPaused) {
            return;
        }

        // Если игрок ещё не начал движение, змейки-роботы тоже не двигаются
        if (currentDirection == null) {
            return;
        }

        // Обновляем игрока
        updatePlayerSnake();

        // Обновляем ИИ змеек
        Iterator<AISnake> iterator = aiSnakes.iterator();
        while (iterator.hasNext()) {
            AISnake aiSnake = iterator.next();
            if (!updateAISnake(aiSnake)) {
                // Змейка погибла
                removeSnakeFromGrid(aiSnake);
                iterator.remove();
            }
        }

        // Проверяем условия победы/поражения
        checkGameState();
    }

    private void updatePlayerSnake() {
        if (currentDirection == null) {
            return; // Если направление не задано, змейка не двигается
        }

        Position currentHead = playerSnake.getHeadPosition();
        Position newHead = new Position(
            currentHead.x() + currentDirection.dx,
            currentHead.y() + currentDirection.dy
        );

        // Проверяем столкновения
        if (isOutOfBounds(newHead) || checkCollision(newHead, 0)) {
            GameState oldState = gameState;
            gameState = GameState.GAME_OVER;
            EventBus.getInstance().publish(new GameOverEvent(newHead));
            EventBus.getInstance().publish(new StateChangedEvent(oldState, gameState));
            return;
        }

        // Сохраняем старую позицию хвоста для события
        Position oldTail = playerSnake.getTailPosition();
        Position oldHead = playerSnake.getHeadPosition();

        // Очищаем старые позиции змейки
        for (Position pos : playerSnake.getBody()) {
            grid[pos.y()][pos.x()] = CellType.EMPTY;
        }

        // Проверяем, есть ли еда на новой позиции
        if (isFoodPosition(newHead)) {
            playerSnake.grow(newHead);
            onFoodEaten(newHead, 0);
        } else {
            playerSnake.move(newHead);
        }

        // Обновляем отображение змейки на поле
        updateSnakeOnGrid(playerSnake.getBody(), CellType.SNAKE_BODY, CellType.SNAKE_HEAD);

        // Публикуем событие о движении змейки
        EventBus.getInstance().publish(new SnakeMovedEvent(oldHead, newHead, oldTail));
    }

    private boolean updateAISnake(AISnake aiSnake) {
        // Очищаем текущие позиции змейки
        for (Position pos : aiSnake.getBody()) {
            grid[pos.y()][pos.x()] = CellType.EMPTY;
        }

        // Обновляем змейку
        boolean alive = aiSnake.update();

        if (alive) {
            // Обновляем позиции змейки на поле
            CellType bodyType = snakeIdToCellType.get(aiSnake.getId());
            updateSnakeOnGrid(aiSnake.getBody(), bodyType, bodyType);
        }

        return alive;
    }

    private void updateSnakeOnGrid(List<Position> body, CellType bodyType, CellType headType) {
        // Сначала очищаем все старые позиции
        for (Position pos : body) {
            grid[pos.y()][pos.x()] = CellType.EMPTY;
        }
        
        // Затем обновляем новые позиции
        for (int i = 0; i < body.size(); i++) {
            Position pos = body.get(i);
            grid[pos.y()][pos.x()] = i == 0 ? headType : bodyType;
        }
    }

    private void removeSnakeFromGrid(AISnake snake) {
        for (Position pos : snake.getBody()) {
            grid[pos.y()][pos.x()] = CellType.EMPTY;
        }
    }

    public boolean checkCollision(Position pos, int snakeId) {
        if (isOutOfBounds(pos)) {
            return true;
        }

        CellType cellType = grid[pos.y()][pos.x()];
        
        // Проверяем столкновение со стеной
        if (cellType == CellType.WALL) {
            return true;
        }

        // Проверяем столкновение с телом любой змейки
        if (cellType == CellType.SNAKE_BODY) {
            return true;
        }

        // Проверяем столкновение с другими змейками
        for (Map.Entry<Integer, CellType> entry : snakeIdToCellType.entrySet()) {
            if (entry.getKey() != snakeId && cellType == entry.getValue()) {
                return true;
            }
        }

        return false;
    }

    public boolean isOutOfBounds(Position pos) {
        return pos.x() < 0 || pos.x() >= width || pos.y() < 0 || pos.y() >= height;
    }

    public boolean isFoodPosition(Position pos) {
        return grid[pos.y()][pos.x()] == CellType.FOOD;
    }

    public void onFoodEaten(Position pos, int snakeId) {
        foodPositions.remove(pos);
        spawnFood();
        
        // Проверяем победу для игрока
        if (snakeId == 0 && playerSnake.getLength() >= targetLength) {
            GameState oldState = gameState;
            gameState = GameState.VICTORY;
            EventBus.getInstance().publish(new VictoryEvent(playerSnake.getLength()));
            EventBus.getInstance().publish(new StateChangedEvent(oldState, gameState));
        }
    }

    private void spawnFood() {
        List<Position> emptyCells = new ArrayList<>();
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (grid[y][x] == CellType.EMPTY) {
                    emptyCells.add(new Position(x, y));
                }
            }
        }
        
        if (!emptyCells.isEmpty()) {
            Position newFood = emptyCells.get(random.nextInt(emptyCells.size()));
            grid[newFood.y()][newFood.x()] = CellType.FOOD;
            foodPositions.add(newFood);
            EventBus.getInstance().publish(new FoodEatenEvent(null, newFood));
        }
    }

    private void checkGameState() {
        // Проверяем условия победы/поражения
        if (playerSnake.getLength() >= targetLength) {
            GameState oldState = gameState;
            gameState = GameState.VICTORY;
            EventBus.getInstance().publish(new VictoryEvent(playerSnake.getLength()));
            EventBus.getInstance().publish(new StateChangedEvent(oldState, gameState));
        }
    }

    public void setDirection(Direction newDirection) {
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
        return playerSnake.getLength();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newState) {
        GameState oldState = this.gameState;
        this.gameState = newState;
        EventBus.getInstance().publish(new StateChangedEvent(oldState, newState));
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void togglePause() {
        isPaused = !isPaused;
        GameState oldState = gameState;
        GameState newState = isPaused ? GameState.PAUSED : GameState.RUNNING;
        gameState = newState;
        EventBus.getInstance().publish(new StateChangedEvent(oldState, newState));
    }

    public void reset() {
        // Очищаем поле
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = CellType.EMPTY;
            }
        }

        // Сбрасываем змейку
        playerSnake.reset(new Position(width / 2, height / 2));
        foodPositions.clear();

        // Инициализируем поле заново
        initializeGrid();
        spawnInitialFood();

        // Сбрасываем состояние игры
        GameState oldState = gameState;
        gameState = GameState.RUNNING;
        isPaused = false;
        EventBus.getInstance().publish(new StateChangedEvent(oldState, gameState));
    }

    public Direction getSnakeDirection() {
        return playerSnake.getDirection();
    }

    public int getSnakeHeadX() {
        return playerSnake.getHead().x();
    }

    public int getSnakeHeadY() {
        return playerSnake.getHead().y();
    }

    public CellType[][] getGrid() {
        return grid;
    }
}
