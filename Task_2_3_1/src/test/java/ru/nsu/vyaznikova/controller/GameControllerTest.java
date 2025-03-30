package ru.nsu.vyaznikova.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.game.GameState;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.controller.input.*;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.grid.CellType;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для GameController.
 * Проверяет:
 * - Обработку пользовательского ввода
 * - Изменение направления змейки
 * - Управление паузой
 * - Обработку недопустимых действий
 */
public class GameControllerTest {
    private GameController controller;
    private GameModel model;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int TARGET_LENGTH = 5;

    @BeforeEach
    void setUp() {
        model = new GameModel(WIDTH, HEIGHT, TARGET_LENGTH);
        controller = new GameController(model);
    }

    /**
     * Проверяет движение змейки вверх
     */
    @Test
    void testMoveUp() {
        // Начальная позиция - центр поля
        Position head = findSnakeHead();
        assertTrue(head.x() > 1 && head.x() < WIDTH - 1 && head.y() > 1 && head.y() < HEIGHT - 1,
                "Snake should start in the middle of the field");

        // Проверяем движение вверх
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.UP));
        model.update();
        int y = findSnakeHead().y();
        model.update();
        assertTrue(findSnakeHead().y() < y, "Snake should move up");
    }

    /**
     * Проверяет движение змейки вниз
     */
    @Test
    void testMoveDown() {
        // Начальная позиция - центр поля
        Position head = findSnakeHead();
        assertTrue(head.x() > 1 && head.x() < WIDTH - 1 && head.y() > 1 && head.y() < HEIGHT - 1,
                "Snake should start in the middle of the field");

        // Проверяем движение вниз
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.DOWN));
        model.update();
        int y = findSnakeHead().y();
        model.update();
        assertTrue(findSnakeHead().y() > y, "Snake should move down");
    }

    /**
     * Проверяет движение змейки влево
     */
    @Test
    void testMoveLeft() {
        // Начальная позиция - центр поля
        Position head = findSnakeHead();
        assertTrue(head.x() > 1 && head.x() < WIDTH - 1 && head.y() > 1 && head.y() < HEIGHT - 1,
                "Snake should start in the middle of the field");

        // Проверяем движение влево
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.LEFT));
        model.update();
        int x = findSnakeHead().x();
        model.update();
        assertTrue(findSnakeHead().x() < x, "Snake should move left");
    }

    /**
     * Проверяет движение змейки вправо
     */
    @Test
    void testMoveRight() {
        // Начальная позиция - центр поля
        Position head = findSnakeHead();
        assertTrue(head.x() > 1 && head.x() < WIDTH - 1 && head.y() > 1 && head.y() < HEIGHT - 1,
                "Snake should start in the middle of the field");

        // Проверяем движение вправо
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.RIGHT));
        model.update();
        int x = findSnakeHead().x();
        model.update();
        assertTrue(findSnakeHead().x() > x, "Snake should move right");
    }

    /**
     * Проверяет, что нельзя двигаться в противоположном направлении
     */
    @Test
    void testOppositeDirectionPrevention() {
        // Начальная позиция - центр поля
        Position head = findSnakeHead();
        assertTrue(head.x() > 1 && head.x() < WIDTH - 1 && head.y() > 1 && head.y() < HEIGHT - 1,
                "Snake should start in the middle of the field");

        // Двигаемся вправо
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.RIGHT));
        model.update();
        int x = findSnakeHead().x();
        model.update();
        assertTrue(findSnakeHead().x() > x, "Snake should move right");

        // Пытаемся двигаться влево (должно быть проигнорировано)
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.LEFT));
        model.update();
        x = findSnakeHead().x();
        model.update();
        assertTrue(findSnakeHead().x() > x, "Snake should continue moving right");
    }

    /**
     * Проверяет обработку паузы
     */
    @Test
    void testPauseToggle() {
        // Начальное состояние - игра запущена
        assertEquals(GameState.RUNNING, model.getGameState());

        // Нажимаем пробел для паузы
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.SPACE));
        assertTrue(controller.isPaused(), "Game should be paused");

        // Нажимаем пробел снова для возобновления
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.SPACE));
        assertFalse(controller.isPaused(), "Game should be unpaused");
    }

    /**
     * Проверяет, что клавиши направления игнорируются при отпускании
     */
    @Test
    void testKeyReleasedIgnored() {
        Position head = findSnakeHead();
        controller.handleInput(new InputEvent(InputEventType.KEY_RELEASED, Key.RIGHT));
        model.update();
        assertEquals(head.x(), findSnakeHead().x(), "X position should not change on key release");
        assertEquals(head.y(), findSnakeHead().y(), "Y position should not change on key release");
    }

    /**
     * Проверяет, что клавиши направления не работают в состоянии GAME_OVER
     */
    @Test
    void testDirectionKeysIgnoredWhenGameOver() {
        // Устанавливаем состояние GAME_OVER
        model.setGameState(GameState.GAME_OVER);

        // Пытаемся изменить направление
        Position head = findSnakeHead();
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.RIGHT));
        model.update();
        assertEquals(head.x(), findSnakeHead().x(), "X position should not change in GAME_OVER state");
        assertEquals(head.y(), findSnakeHead().y(), "Y position should not change in GAME_OVER state");
    }

    /**
     * Проверяет, что пробел работает в любом состоянии игры
     */
    @Test
    void testSpaceWorksInAnyState() {
        // В состоянии RUNNING
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.SPACE));
        assertTrue(controller.isPaused(), "Game should be paused in RUNNING state");

        // В состоянии GAME_OVER
        model.setGameState(GameState.GAME_OVER);
        controller.handleInput(new InputEvent(InputEventType.KEY_PRESSED, Key.SPACE));
        assertFalse(controller.isPaused(), "Game should be unpaused in GAME_OVER state");
    }

    /**
     * Вспомогательный метод для поиска головы змейки
     */
    private Position findSnakeHead() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (model.getCellType(x, y) == CellType.SNAKE_HEAD) {
                    return new Position(x, y);
                }
            }
        }
        fail("Snake head not found");
        return null;
    }
}
