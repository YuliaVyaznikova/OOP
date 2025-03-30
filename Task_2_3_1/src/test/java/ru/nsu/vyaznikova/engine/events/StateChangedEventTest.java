package ru.nsu.vyaznikova.engine.events;

import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.game.GameState;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса StateChangedEvent.
 * Проверяет:
 * - Корректность создания события
 * - Получение старого и нового состояний
 * - Работу методов equals, hashCode и toString
 */
public class StateChangedEventTest {

    @Test
    void testEventCreation() {
        StateChangedEvent event = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        assertEquals("STATE_CHANGED", event.getEventType(), "Event type should be STATE_CHANGED");
        assertEquals(GameState.RUNNING, event.getOldState(), "Old state should be RUNNING");
        assertEquals(GameState.PAUSED, event.getNewState(), "New state should be PAUSED");
    }

    @Test
    void testEqualsAndHashCode() {
        StateChangedEvent event1 = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        StateChangedEvent event2 = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        StateChangedEvent event3 = new StateChangedEvent(GameState.RUNNING, GameState.GAME_OVER);
        StateChangedEvent event4 = new StateChangedEvent(GameState.PAUSED, GameState.PAUSED);

        // Проверяем equals
        assertTrue(event1.equals(event1), "Event should be equal to itself");
        assertTrue(event1.equals(event2), "Events with same states should be equal");
        assertFalse(event1.equals(null), "Event should not be equal to null");
        assertFalse(event1.equals(new Object()), "Event should not be equal to object of different class");
        assertFalse(event1.equals(event3), "Events with different new states should not be equal");
        assertFalse(event1.equals(event4), "Events with different old states should not be equal");

        // Проверяем hashCode
        assertEquals(event1.hashCode(), event2.hashCode(), "Equal events should have equal hash codes");
        assertNotEquals(event1.hashCode(), event3.hashCode(), "Different events should have different hash codes");
    }

    @Test
    void testToString() {
        StateChangedEvent event = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        String expected = "StateChangedEvent{from RUNNING to PAUSED}";
        assertEquals(expected, event.toString(), "toString should return correct string representation");
    }

    @Test
    void testAllStateTransitions() {
        // Проверяем все возможные переходы состояний
        for (GameState oldState : GameState.values()) {
            for (GameState newState : GameState.values()) {
                StateChangedEvent event = new StateChangedEvent(oldState, newState);
                assertEquals(oldState, event.getOldState(), "Old state should match");
                assertEquals(newState, event.getNewState(), "New state should match");
            }
        }
    }
}
