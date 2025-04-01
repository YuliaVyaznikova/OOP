package ru.nsu.vyaznikova.model.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.engine.events.Event;
import ru.nsu.vyaznikova.engine.events.EventBus;
import ru.nsu.vyaznikova.engine.events.EventListener;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.snake.Direction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для GameModel.
 * Проверяет:
 * - Инициализацию игрового поля и змейки
 * - Движение змейки и обработку столкновений
 * - Поедание еды и рост змейки
 * - Условия проигрыша
 * - Генерацию и обработку событий
 */
public class GameModelTest {
    private GameModel gameModel;
    private TestEventListener eventListener;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final int TARGET_LENGTH = 5;

    /**
     * Тестовый слушатель событий для проверки генерации событий игровой моделью
     */
    private static class TestEventListener implements EventListener {
        private final List<Event> receivedEvents = new ArrayList<>();

        @Override
        public void onEvent(Event event) {
            receivedEvents.add(event);
        }

        public List<Event> getReceivedEvents() {
            return receivedEvents;
        }

        public void clearEvents() {
            receivedEvents.clear();
        }
    }

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(WIDTH, HEIGHT, TARGET_LENGTH);
        eventListener = new TestEventListener();
        EventBus.getInstance().subscribe("SNAKE_MOVED", eventListener);
        EventBus.getInstance().subscribe("FOOD_EATEN", eventListener);
        EventBus.getInstance().subscribe("GAME_OVER", eventListener);
        EventBus.getInstance().subscribe("VICTORY", eventListener);
        EventBus.getInstance().subscribe("STATE_CHANGED", eventListener);
    }

    /**
     * Проверяет корректность начальной инициализации игрового поля
     */
    @Test
    void testInitialGameState() {
        assertEquals(WIDTH, gameModel.getWidth());
        assertEquals(HEIGHT, gameModel.getHeight());
        assertEquals(1, gameModel.getLength());
        assertEquals(GameState.RUNNING, gameModel.getGameState());

        // Проверяем стены по периметру
        for (int x = 0; x < WIDTH; x++) {
            assertEquals(CellType.WALL, gameModel.getCellType(x, 0));
            assertEquals(CellType.WALL, gameModel.getCellType(x, HEIGHT - 1));
        }
        for (int y = 0; y < HEIGHT; y++) {
            assertEquals(CellType.WALL, gameModel.getCellType(0, y));
            assertEquals(CellType.WALL, gameModel.getCellType(WIDTH - 1, y));
        }
    }

    /**
     * Проверяет базовое движение змейки
     */
    @Test
    void testBasicSnakeMovement() {
        eventListener.clearEvents();
        gameModel.setDirection(Direction.RIGHT);
        gameModel.update();

        // Проверяем, что событие движения было сгенерировано
        assertTrue(eventListener.getReceivedEvents().stream()
                .anyMatch(e -> e.getEventType().equals("SNAKE_MOVED")));
    }

    /**
     * Проверяет столкновение змейки со стеной
     */
    @Test
    void testWallCollision() {
        // Двигаем змейку влево к стене
        gameModel.setDirection(Direction.LEFT);
        
        // Продолжаем движение, пока не достигнем стены
        while (gameModel.getGameState() == GameState.RUNNING) {
            gameModel.update();
        }

        assertEquals(GameState.GAME_OVER, gameModel.getGameState());
        assertTrue(eventListener.getReceivedEvents().stream()
                .anyMatch(e -> e.getEventType().equals("GAME_OVER")));
    }

    /**
     * Проверяет поедание еды и рост змейки
     */
    @Test
    void testEatingFoodAndGrowing() {
        int initialLength = gameModel.getLength();
        Position foodPosition = null;

        // Ищем еду на поле
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (gameModel.getCellType(x, y) == CellType.FOOD) {
                    foodPosition = new Position(x, y);
                    break;
                }
            }
            if (foodPosition != null) break;
        }

        assertNotNull(foodPosition, "Еда должна быть на поле");

        // Двигаем змейку к еде
        while (gameModel.getLength() == initialLength) {
            Position headPosition = null;
            // Находим голову змейки
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (gameModel.getCellType(x, y) == CellType.SNAKE_HEAD) {
                        headPosition = new Position(x, y);
                        break;
                    }
                }
                if (headPosition != null) break;
            }

            assertNotNull(headPosition, "Голова змейки должна быть на поле");

            // Выбираем направление движения к еде
            if (foodPosition.x() > headPosition.x()) {
                gameModel.setDirection(Direction.RIGHT);
            } else if (foodPosition.x() < headPosition.x()) {
                gameModel.setDirection(Direction.LEFT);
            } else if (foodPosition.y() > headPosition.y()) {
                gameModel.setDirection(Direction.DOWN);
            } else {
                gameModel.setDirection(Direction.UP);
            }

            gameModel.update();
        }

        assertEquals(initialLength + 1, gameModel.getLength());
        assertTrue(eventListener.getReceivedEvents().stream()
                .anyMatch(e -> e.getEventType().equals("FOOD_EATEN")));
    }
}
