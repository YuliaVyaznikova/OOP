package ru.nsu.vyaznikova.model.snake;

import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.grid.Position;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса SnakeSegment.
 * Проверяет:
 * - Создание сегмента с разными позициями
 * - Получение позиции сегмента
 * - Сравнение сегментов
 * - Строковое представление сегмента
 */
public class SnakeSegmentTest {

    @Test
    void testSegmentCreation() {
        Position pos = new Position(1, 2);
        SnakeSegment segment = new SnakeSegment(pos);
        assertEquals(pos, segment.getPosition(), "Position should match constructor argument");
    }

    @Test
    void testEqualsAndHashCode() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(1, 2);
        Position pos3 = new Position(3, 4);

        SnakeSegment segment1 = new SnakeSegment(pos1);
        SnakeSegment segment2 = new SnakeSegment(pos2);
        SnakeSegment segment3 = new SnakeSegment(pos3);

        // Проверяем equals
        assertTrue(segment1.equals(segment1), "Segment should be equal to itself");
        assertTrue(segment1.equals(segment2), "Segments with same position should be equal");
        assertFalse(segment1.equals(null), "Segment should not be equal to null");
        assertFalse(segment1.equals(new Object()), "Segment should not be equal to object of different class");
        assertFalse(segment1.equals(segment3), "Segments with different positions should not be equal");

        // Проверяем hashCode
        assertEquals(segment1.hashCode(), segment2.hashCode(), "Equal segments should have equal hash codes");
        assertNotEquals(segment1.hashCode(), segment3.hashCode(), "Different segments should have different hash codes");
    }

    @Test
    void testToString() {
        SnakeSegment segment = new SnakeSegment(new Position(1, 2));
        String expected = "SnakeSegment{position=Position[x=1, y=2]}";
        assertEquals(expected, segment.toString(), "toString should return correct string representation");
    }

    @Test
    void testDifferentPositions() {
        // Проверяем различные позиции
        int[][] positions = {{0, 0}, {-1, -1}, {1, 1}, {100, 100}, {Integer.MAX_VALUE, Integer.MAX_VALUE}};
        for (int[] pos : positions) {
            Position position = new Position(pos[0], pos[1]);
            SnakeSegment segment = new SnakeSegment(position);
            assertEquals(position, segment.getPosition(), 
                "Position should match for coordinates (" + pos[0] + ", " + pos[1] + ")");
        }
    }

    @Test
    void testNullPosition() {
        // Проверяем, что создание сегмента с null позицией вызывает исключение
        assertThrows(NullPointerException.class, () -> new SnakeSegment(null),
            "Creating segment with null position should throw NullPointerException");
    }
}
