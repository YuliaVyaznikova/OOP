package ru.nsu.vyaznikova.controller.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.snake.Direction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для InputHandler.
 * Проверяет:
 * - Обработку событий ввода
 * - Преобразование клавиш в направления
 * - Корректную передачу направлений в GameModel
 */
public class InputHandlerTest {
    private GameModel gameModel;
    private TestInputHandler inputHandler;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final int TARGET_LENGTH = 5;

    /**
     * Тестовая реализация InputHandler для проверки обработки ввода
     */
    private static class TestInputHandler implements InputHandler {
        private final GameModel gameModel;

        public TestInputHandler(GameModel gameModel) {
            this.gameModel = gameModel;
        }

        @Override
        public void handleInput(InputEvent event) {
            switch (event.getKey()) {
                case UP -> gameModel.setDirection(Direction.UP);
                case DOWN -> gameModel.setDirection(Direction.DOWN);
                case LEFT -> gameModel.setDirection(Direction.LEFT);
                case RIGHT -> gameModel.setDirection(Direction.RIGHT);
            }
        }
    }

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(WIDTH, HEIGHT, TARGET_LENGTH);
        inputHandler = new TestInputHandler(gameModel);
    }

    /**
     * Проверяет обработку события нажатия клавиши вверх
     */
    @Test
    void testHandleUpKey() {
        InputEvent event = new InputEvent(InputEventType.KEY_PRESSED, Key.UP);
        inputHandler.handleInput(event);
        // Проверяем, что змейка начала двигаться вверх, отслеживая её позицию
        Position initialHead = findSnakeHead();
        gameModel.update();
        Position newHead = findSnakeHead();
        assertTrue(newHead.y() < initialHead.y() || // Либо змейка двигается вверх
                  (initialHead.y() <= 1 && newHead.y() > initialHead.y())); // Либо уперлась в стену
    }

    /**
     * Проверяет обработку события нажатия клавиши вниз
     */
    @Test
    void testHandleDownKey() {
        InputEvent event = new InputEvent(InputEventType.KEY_PRESSED, Key.DOWN);
        inputHandler.handleInput(event);
        Position initialHead = findSnakeHead();
        gameModel.update();
        Position newHead = findSnakeHead();
        assertTrue(newHead.y() > initialHead.y() || 
                  (initialHead.y() >= HEIGHT - 2 && newHead.y() < initialHead.y()));
    }

    /**
     * Проверяет обработку события нажатия клавиши влево
     */
    @Test
    void testHandleLeftKey() {
        InputEvent event = new InputEvent(InputEventType.KEY_PRESSED, Key.LEFT);
        inputHandler.handleInput(event);
        Position initialHead = findSnakeHead();
        gameModel.update();
        Position newHead = findSnakeHead();
        assertTrue(newHead.x() < initialHead.x() || 
                  (initialHead.x() <= 1 && newHead.x() > initialHead.x()));
    }

    /**
     * Проверяет обработку события нажатия клавиши вправо
     */
    @Test
    void testHandleRightKey() {
        InputEvent event = new InputEvent(InputEventType.KEY_PRESSED, Key.RIGHT);
        inputHandler.handleInput(event);
        Position initialHead = findSnakeHead();
        gameModel.update();
        Position newHead = findSnakeHead();
        assertTrue(newHead.x() > initialHead.x() || 
                  (initialHead.x() >= WIDTH - 2 && newHead.x() < initialHead.x()));
    }

    /**
     * Вспомогательный метод для поиска головы змейки на поле
     */
    private Position findSnakeHead() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (gameModel.getCellType(x, y) == CellType.SNAKE_HEAD) {
                    return new Position(x, y);
                }
            }
        }
        fail("Snake head not found");
        return null;
    }
}
