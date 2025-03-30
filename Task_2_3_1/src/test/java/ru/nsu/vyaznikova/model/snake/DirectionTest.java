package ru.nsu.vyaznikova.model.snake;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса Direction.
 * Проверяет:
 * - Корректность значений смещений для каждого направления
 * - Правильность определения противоположных направлений
 */
public class DirectionTest {

    @Test
    void testDirectionOffsets() {
        // Проверяем смещения для UP
        assertEquals(0, Direction.UP.dx, "UP dx should be 0");
        assertEquals(-1, Direction.UP.dy, "UP dy should be -1");

        // Проверяем смещения для DOWN
        assertEquals(0, Direction.DOWN.dx, "DOWN dx should be 0");
        assertEquals(1, Direction.DOWN.dy, "DOWN dy should be 1");

        // Проверяем смещения для LEFT
        assertEquals(-1, Direction.LEFT.dx, "LEFT dx should be -1");
        assertEquals(0, Direction.LEFT.dy, "LEFT dy should be 0");

        // Проверяем смещения для RIGHT
        assertEquals(1, Direction.RIGHT.dx, "RIGHT dx should be 1");
        assertEquals(0, Direction.RIGHT.dy, "RIGHT dy should be 0");
    }

    @Test
    void testOppositeDirections() {
        // Проверяем противоположные направления
        assertTrue(Direction.UP.isOpposite(Direction.DOWN), "UP and DOWN should be opposite");
        assertTrue(Direction.DOWN.isOpposite(Direction.UP), "DOWN and UP should be opposite");
        assertTrue(Direction.LEFT.isOpposite(Direction.RIGHT), "LEFT and RIGHT should be opposite");
        assertTrue(Direction.RIGHT.isOpposite(Direction.LEFT), "RIGHT and LEFT should be opposite");

        // Проверяем не противоположные направления
        assertFalse(Direction.UP.isOpposite(Direction.LEFT), "UP and LEFT should not be opposite");
        assertFalse(Direction.UP.isOpposite(Direction.RIGHT), "UP and RIGHT should not be opposite");
        assertFalse(Direction.DOWN.isOpposite(Direction.LEFT), "DOWN and LEFT should not be opposite");
        assertFalse(Direction.DOWN.isOpposite(Direction.RIGHT), "DOWN and RIGHT should not be opposite");
        assertFalse(Direction.UP.isOpposite(Direction.UP), "UP and UP should not be opposite");
        assertFalse(Direction.DOWN.isOpposite(Direction.DOWN), "DOWN and DOWN should not be opposite");
        assertFalse(Direction.LEFT.isOpposite(Direction.LEFT), "LEFT and LEFT should not be opposite");
        assertFalse(Direction.RIGHT.isOpposite(Direction.RIGHT), "RIGHT and RIGHT should not be opposite");
    }
}
