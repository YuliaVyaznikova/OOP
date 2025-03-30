package ru.nsu.vyaznikova.engine.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.grid.Position;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для EventBus.
 * Проверяет:
 * - Синглтон паттерн EventBus
 * - Подписку на события
 * - Отписку от событий
 * - Публикацию событий
 * - Получение событий подписчиками
 */
public class EventBusTest {
    private EventBus eventBus;
    private TestEventListener listener;

    /**
     * Тестовый слушатель событий
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
        eventBus = EventBus.getInstance();
        listener = new TestEventListener();
    }

    /**
     * Проверяет, что EventBus реализует паттерн Синглтон
     */
    @Test
    void testSingletonPattern() {
        EventBus anotherInstance = EventBus.getInstance();
        assertSame(eventBus, anotherInstance);
    }

    /**
     * Проверяет подписку на события и их получение
     */
    @Test
    void testSubscriptionAndEventDelivery() {
        eventBus.subscribe("SNAKE_MOVED", listener);
        
        Event event = new SnakeMovedEvent(new Position(1, 1), new Position(0, 0));
        eventBus.publish(event);

        assertEquals(1, listener.getReceivedEvents().size());
        assertSame(event, listener.getReceivedEvents().get(0));
    }

    /**
     * Проверяет отписку от событий
     */
    @Test
    void testUnsubscribe() {
        eventBus.subscribe("FOOD_EATEN", listener);
        eventBus.unsubscribe("FOOD_EATEN", listener);

        Event event = new FoodEatenEvent(new Position(1, 1), 5);
        eventBus.publish(event);

        assertTrue(listener.getReceivedEvents().isEmpty());
    }

    /**
     * Проверяет, что события доставляются только подписанным слушателям
     */
    @Test
    void testEventDeliveryToCorrectSubscribers() {
        TestEventListener snakeListener = new TestEventListener();
        TestEventListener foodListener = new TestEventListener();

        eventBus.subscribe("SNAKE_MOVED", snakeListener);
        eventBus.subscribe("FOOD_EATEN", foodListener);

        Event snakeEvent = new SnakeMovedEvent(new Position(1, 1), new Position(0, 0));
        Event foodEvent = new FoodEatenEvent(new Position(1, 1), 5);

        eventBus.publish(snakeEvent);
        eventBus.publish(foodEvent);

        assertEquals(1, snakeListener.getReceivedEvents().size());
        assertEquals(1, foodListener.getReceivedEvents().size());
        assertSame(snakeEvent, snakeListener.getReceivedEvents().get(0));
        assertSame(foodEvent, foodListener.getReceivedEvents().get(0));
    }

    /**
     * Проверяет обработку множественных подписчиков
     */
    @Test
    void testMultipleSubscribers() {
        TestEventListener listener1 = new TestEventListener();
        TestEventListener listener2 = new TestEventListener();

        eventBus.subscribe("GAME_OVER", listener1);
        eventBus.subscribe("GAME_OVER", listener2);

        Event event = new GameOverEvent("Test game over");
        eventBus.publish(event);

        assertEquals(1, listener1.getReceivedEvents().size());
        assertEquals(1, listener2.getReceivedEvents().size());
        assertSame(event, listener1.getReceivedEvents().get(0));
        assertSame(event, listener2.getReceivedEvents().get(0));
    }

    /**
     * Проверяет обработку неизвестных типов событий
     */
    @Test
    void testUnknownEventType() {
        eventBus.subscribe("UNKNOWN_EVENT", listener);
        Event event = new Event() {
            @Override
            public String getEventType() {
                return "DIFFERENT_EVENT";
            }
        };
        eventBus.publish(event);

        assertTrue(listener.getReceivedEvents().isEmpty());
    }
}
