package ru.nsu.vyaznikova.engine.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.game.GameState;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для EventBus.
 * Проверяет:
 * - Регистрацию подписчиков
 * - Отмену подписки
 * - Публикацию событий
 * - Получение событий подписчиками
 */
class EventBusTest {
    private EventBus eventBus;
    private TestSubscriber subscriber;

    @BeforeEach
    void setUp() {
        eventBus = EventBus.getInstance();
        subscriber = new TestSubscriber();
        eventBus.subscribe(subscriber);
    }

    @Test
    void testSubscribeAndPublish() {
        Position pos = new Position(1, 1);
        Event event = new GameOverEvent(pos);
        eventBus.publish(event);
        assertEquals(event, subscriber.getLastEvent());
    }

    @Test
    void testUnsubscribe() {
        eventBus.unsubscribe(subscriber);
        Position pos = new Position(1, 1);
        Event event = new GameOverEvent(pos);
        eventBus.publish(event);
        assertNull(subscriber.getLastEvent());
    }

    @Test
    void testMultipleSubscribers() {
        TestSubscriber subscriber2 = new TestSubscriber();
        eventBus.subscribe(subscriber2);

        Position pos = new Position(1, 1);
        Event event = new GameOverEvent(pos);
        eventBus.publish(event);

        assertEquals(event, subscriber.getLastEvent());
        assertEquals(event, subscriber2.getLastEvent());

        eventBus.unsubscribe(subscriber2);
    }

    @Test
    void testDifferentEventTypes() {
        // Тестируем GameOverEvent
        Position pos = new Position(1, 1);
        Event gameOverEvent = new GameOverEvent(pos);
        eventBus.publish(gameOverEvent);
        assertEquals(gameOverEvent, subscriber.getLastEvent());

        // Тестируем FoodEatenEvent
        Position food1 = new Position(1, 1);
        Position food2 = new Position(2, 2);
        Event foodEvent = new FoodEatenEvent(food1, food2);
        eventBus.publish(foodEvent);
        assertEquals(foodEvent, subscriber.getLastEvent());

        // Тестируем SnakeMovedEvent
        Position oldHead = new Position(1, 1);
        Position newHead = new Position(2, 2);
        Position oldTail = new Position(0, 0);
        Event moveEvent = new SnakeMovedEvent(oldHead, newHead, oldTail);
        eventBus.publish(moveEvent);
        assertEquals(moveEvent, subscriber.getLastEvent());

        // Тестируем StateChangedEvent
        Event stateEvent = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        eventBus.publish(stateEvent);
        assertEquals(stateEvent, subscriber.getLastEvent());
    }

    @Test
    void testSubscriptionAndEventDelivery() {
        TestEventListener listener = new TestEventListener();
        eventBus.subscribe("SNAKE_MOVED", listener);
        
        Position oldHead = new Position(1, 1);
        Position newHead = new Position(0, 0);
        Position oldTail = new Position(2, 2);
        Event event = new SnakeMovedEvent(oldHead, newHead, oldTail);
        eventBus.publish(event);

        assertEquals(1, listener.getReceivedEvents().size());
        assertEquals(event, listener.getReceivedEvents().get(0));
    }

    @Test
    void testUnsubscriptionByEventType() {
        TestEventListener listener = new TestEventListener();
        eventBus.subscribe("SNAKE_MOVED", listener);
        eventBus.unsubscribe("SNAKE_MOVED", listener);

        Position oldHead = new Position(1, 1);
        Position newHead = new Position(0, 0);
        Position oldTail = new Position(2, 2);
        Event event = new SnakeMovedEvent(oldHead, newHead, oldTail);
        eventBus.publish(event);

        assertTrue(listener.getReceivedEvents().isEmpty());
    }

    @Test
    void testMultipleEventTypes() {
        TestEventListener snakeListener = new TestEventListener();
        TestEventListener foodListener = new TestEventListener();
        eventBus.subscribe("SNAKE_MOVED", snakeListener);
        eventBus.subscribe("FOOD_EATEN", foodListener);

        Position oldHead = new Position(1, 1);
        Position newHead = new Position(0, 0);
        Position oldTail = new Position(2, 2);
        Event snakeEvent = new SnakeMovedEvent(oldHead, newHead, oldTail);
        
        Position eatenFood = new Position(1, 1);
        Position newFood = new Position(2, 2);
        Event foodEvent = new FoodEatenEvent(eatenFood, newFood);

        eventBus.publish(snakeEvent);
        eventBus.publish(foodEvent);

        assertEquals(1, snakeListener.getReceivedEvents().size());
        assertEquals(snakeEvent, snakeListener.getReceivedEvents().get(0));
        assertEquals(1, foodListener.getReceivedEvents().size());
        assertEquals(foodEvent, foodListener.getReceivedEvents().get(0));
    }

    private static class TestSubscriber implements EventListener {
        private Event lastEvent;

        @Override
        public void onEvent(Event event) {
            lastEvent = event;
        }

        public Event getLastEvent() {
            return lastEvent;
        }
    }

    private static class TestEventListener implements EventListener {
        private final java.util.List<Event> receivedEvents = new java.util.ArrayList<>();

        @Override
        public void onEvent(Event event) {
            receivedEvents.add(event);
        }

        public java.util.List<Event> getReceivedEvents() {
            return receivedEvents;
        }
    }
}
