package ru.nsu.vyaznikova.model.snake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.grid.Position;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для Snake.
 * Проверяет:
 * - Создание змейки с начальной позицией
 * - Движение змейки в разных направлениях
 * - Рост змейки
 * - Корректность позиций тела змейки
 * - Проверку столкновений
 */
public class SnakeTest {
    private Snake snake;
    private static final Position INITIAL_POSITION = new Position(5, 5);

    @BeforeEach
    void setUp() {
        snake = new Snake(INITIAL_POSITION);
    }

    /**
     * Проверяет корректность начального состояния змейки
     */
    @Test
    void testInitialState() {
        assertEquals(1, snake.getLength());
        assertEquals(INITIAL_POSITION, snake.getHeadPosition());
        assertTrue(snake.getBody().contains(INITIAL_POSITION));
    }

    /**
     * Проверяет базовое движение змейки без роста
     */
    @Test
    void testBasicMovement() {
        Position newPosition = new Position(6, 5);
        snake.move(newPosition);

        assertEquals(1, snake.getLength());
        assertEquals(newPosition, snake.getHeadPosition());
        assertFalse(snake.getBody().contains(INITIAL_POSITION));
        assertTrue(snake.getBody().contains(newPosition));
    }

    /**
     * Проверяет рост змейки при движении
     */
    @Test
    void testGrowth() {
        Position newPosition = new Position(6, 5);
        snake.grow(newPosition);

        assertEquals(2, snake.getLength());
        assertEquals(newPosition, snake.getHeadPosition());
        assertTrue(snake.getBody().contains(INITIAL_POSITION));
        assertTrue(snake.getBody().contains(newPosition));
    }

    /**
     * Проверяет последовательное движение змейки
     */
    @Test
    void testSequentialMovement() {
        // Сначала растим змейку
        snake.grow(new Position(6, 5));
        snake.grow(new Position(7, 5));
        
        assertEquals(3, snake.getLength());

        // Двигаем змейку без роста
        snake.move(new Position(8, 5));

        assertEquals(3, snake.getLength());
        assertEquals(new Position(8, 5), snake.getHeadPosition());
        
        List<Position> body = snake.getBody();
        assertEquals(3, body.size());
        assertTrue(body.contains(new Position(8, 5))); // Голова
        assertTrue(body.contains(new Position(7, 5))); // Тело
        assertTrue(body.contains(new Position(6, 5))); // Хвост
    }

    /**
     * Проверяет обнаружение столкновений с телом змейки
     */
    @Test
    void testCollisionDetection() {
        // Растим змейку до размера 4
        snake.grow(new Position(6, 5));
        snake.grow(new Position(6, 6));
        snake.grow(new Position(5, 6));

        // Проверяем столкновение с телом
        assertTrue(snake.containsPosition(new Position(6, 5)));
        assertTrue(snake.containsPosition(new Position(6, 6)));
        assertTrue(snake.containsPosition(new Position(5, 6)));

        // Проверяем отсутствие столкновения со свободной клеткой
        assertFalse(snake.containsPosition(new Position(4, 5)));
    }

    /**
     * Проверяет движение змейки во всех направлениях
     */
    @Test
    void testMovementInAllDirections() {
        // Вправо
        snake.move(new Position(6, 5));
        assertEquals(new Position(6, 5), snake.getHeadPosition());

        // Вниз
        snake.move(new Position(6, 6));
        assertEquals(new Position(6, 6), snake.getHeadPosition());

        // Влево
        snake.move(new Position(5, 6));
        assertEquals(new Position(5, 6), snake.getHeadPosition());

        // Вверх
        snake.move(new Position(5, 5));
        assertEquals(new Position(5, 5), snake.getHeadPosition());
    }
}
