package ru.nsu.vyaznikova.model.grid;

import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.snake.Direction;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для Position.
 * Проверяет:
 * - Создание позиции
 * - Получение координат
 * - Перемещение по dx, dy
 * - Перемещение по направлению
 * - Сравнение позиций
 * - Хеш-код и equals
 */
public class PositionTest {

    /**
     * Проверяет создание позиции и получение координат
     */
    @Test
    void testPositionCreationAndCoordinates() {
        Position position = new Position(5, 10);
        assertEquals(5, position.x());
        assertEquals(10, position.y());
    }

    /**
     * Проверяет перемещение позиции по dx, dy
     */
    @Test
    void testPositionTranslateByDelta() {
        Position initial = new Position(5, 10);
        Position translated = initial.translate(3, -2);
        
        assertEquals(8, translated.x());
        assertEquals(8, translated.y());
        
        // Проверяем, что исходная позиция не изменилась (immutability)
        assertEquals(5, initial.x());
        assertEquals(10, initial.y());
    }

    /**
     * Проверяет перемещение позиции по направлению
     */
    @Test
    void testPositionTranslateByDirection() {
        Position position = new Position(5, 5);
        
        Position up = position.translate(Direction.UP);
        assertEquals(5, up.x());
        assertEquals(4, up.y());
        
        Position down = position.translate(Direction.DOWN);
        assertEquals(5, down.x());
        assertEquals(6, down.y());
        
        Position left = position.translate(Direction.LEFT);
        assertEquals(4, left.x());
        assertEquals(5, left.y());
        
        Position right = position.translate(Direction.RIGHT);
        assertEquals(6, right.x());
        assertEquals(5, right.y());
    }

    /**
     * Проверяет корректность сравнения позиций
     */
    @Test
    void testPositionEquality() {
        Position pos1 = new Position(5, 10);
        Position pos2 = new Position(5, 10);
        Position pos3 = new Position(10, 5);
        
        // Проверяем equals
        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
        assertNotEquals(pos1, null);
        assertEquals(pos1, pos1); // рефлексивность
        
        // Проверяем hashCode
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }

    /**
     * Проверяет граничные случаи при создании позиции
     */
    @Test
    void testPositionBoundaries() {
        // Отрицательные координаты
        Position negative = new Position(-1, -1);
        assertEquals(-1, negative.x());
        assertEquals(-1, negative.y());
        
        // Большие значения
        Position large = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, large.x());
        assertEquals(Integer.MAX_VALUE, large.y());
        
        // Ноль
        Position zero = new Position(0, 0);
        assertEquals(0, zero.x());
        assertEquals(0, zero.y());
    }

    /**
     * Проверяет цепочку перемещений
     */
    @Test
    void testChainedTranslations() {
        Position start = new Position(0, 0);
        Position result = start
            .translate(Direction.RIGHT)
            .translate(Direction.RIGHT)
            .translate(Direction.DOWN)
            .translate(Direction.LEFT);
        
        assertEquals(1, result.x());
        assertEquals(1, result.y());
    }
}
