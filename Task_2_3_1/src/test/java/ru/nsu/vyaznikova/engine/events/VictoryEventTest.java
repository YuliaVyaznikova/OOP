package ru.nsu.vyaznikova.engine.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса VictoryEvent.
 * Проверяет:
 * - Корректность создания события
 * - Получение финальной длины змейки
 * - Работу методов equals, hashCode и toString
 */
public class VictoryEventTest {

    @Test
    void testEventCreation() {
        VictoryEvent event = new VictoryEvent(100);
        assertEquals("VICTORY", event.getEventType(), "Event type should be VICTORY");
        assertEquals(100, event.getFinalLength(), "Final length should be 100");
    }

    @Test
    void testEqualsAndHashCode() {
        VictoryEvent event1 = new VictoryEvent(100);
        VictoryEvent event2 = new VictoryEvent(100);
        VictoryEvent event3 = new VictoryEvent(200);

        // Проверяем equals
        assertTrue(event1.equals(event1), "Event should be equal to itself");
        assertTrue(event1.equals(event2), "Events with same length should be equal");
        assertFalse(event1.equals(null), "Event should not be equal to null");
        assertFalse(event1.equals(new Object()), "Event should not be equal to object of different class");
        assertFalse(event1.equals(event3), "Events with different lengths should not be equal");

        // Проверяем hashCode
        assertEquals(event1.hashCode(), event2.hashCode(), "Equal events should have equal hash codes");
        assertNotEquals(event1.hashCode(), event3.hashCode(), "Different events should have different hash codes");
    }

    @Test
    void testToString() {
        VictoryEvent event = new VictoryEvent(100);
        String str = event.toString();
        assertTrue(str.contains("finalLength=100"), "toString should contain the final length");
    }

    @Test
    void testDifferentLengths() {
        // Проверяем различные значения длины
        int[] lengths = {0, 1, 10, 100, 1000, Integer.MAX_VALUE};
        for (int length : lengths) {
            VictoryEvent event = new VictoryEvent(length);
            assertEquals(length, event.getFinalLength(), "Final length should match the constructor argument");
        }
    }

    @Test
    void testNegativeLength() {
        // Проверяем, что отрицательная длина тоже обрабатывается корректно
        VictoryEvent event = new VictoryEvent(-100);
        assertEquals(-100, event.getFinalLength(), "Negative length should be handled correctly");
    }
}
