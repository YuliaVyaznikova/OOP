package ru.nsu.vyaznikova.engine.events;

import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.game.GameState;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса StateChangedEvent.
 * Проверяет:
 * - Корректность создания события
 * - Получение нового состояния
 * - Работу методов equals, hashCode и toString
 */
class StateChangedEventTest {

    @Test
    void testGetters() {
        GameState oldState = GameState.RUNNING;
        GameState newState = GameState.GAME_OVER;
        StateChangedEvent event = new StateChangedEvent(oldState, newState);

        assertEquals(oldState, event.getOldState());
        assertEquals(newState, event.getNewState());
        assertEquals("STATE_CHANGED", event.getEventType());
    }

    @Test
    void testEquals() {
        // Создаем события с одинаковыми состояниями
        StateChangedEvent event1 = new StateChangedEvent(GameState.PAUSED, GameState.RUNNING);
        StateChangedEvent event2 = new StateChangedEvent(GameState.PAUSED, GameState.RUNNING);
        StateChangedEvent event3 = new StateChangedEvent(GameState.RUNNING, GameState.GAME_OVER);

        // Проверяем равенство
        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertNotEquals(event1, null);
        assertNotEquals(event1, new Object());
    }

    @Test
    void testHashCode() {
        // Создаем события с одинаковыми состояниями
        StateChangedEvent event1 = new StateChangedEvent(GameState.PAUSED, GameState.RUNNING);
        StateChangedEvent event2 = new StateChangedEvent(GameState.PAUSED, GameState.RUNNING);

        // Проверяем хэш-коды
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testToString() {
        StateChangedEvent event = new StateChangedEvent(GameState.RUNNING, GameState.VICTORY);
        String str = event.toString();
        assertTrue(str.contains("RUNNING"));
        assertTrue(str.contains("VICTORY"));
    }
}
