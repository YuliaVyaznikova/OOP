package ru.nsu.vyaznikova.engine.events;

import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.grid.Position;
import ru.nsu.vyaznikova.model.game.GameState;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для всех игровых событий.
 * Проверяет:
 * - Создание событий
 * - Получение данных из событий
 * - Сравнение событий
 * - Корректность toString
 */
public class GameEventsTest {

    @Test
    void testFoodEatenEvent() {
        Position foodPosition = new Position(5, 5);
        Position newFoodPosition = new Position(7, 7);
        FoodEatenEvent event = new FoodEatenEvent(foodPosition, newFoodPosition);

        assertEquals(foodPosition, event.getEatenFoodPosition());
        assertEquals(newFoodPosition, event.getNewFoodPosition());
        assertTrue(event.toString().contains("FoodEatenEvent"));
        assertTrue(event.toString().contains("eaten at (5, 5)"));
        assertTrue(event.toString().contains("new at (7, 7)"));
    }

    @Test
    void testSnakeMovedEvent() {
        Position oldHead = new Position(5, 5);
        Position newHead = new Position(6, 5);
        Position oldTail = new Position(4, 5);
        SnakeMovedEvent event = new SnakeMovedEvent(oldHead, newHead, oldTail);

        assertEquals(oldHead, event.getOldHeadPosition());
        assertEquals(newHead, event.getNewHeadPosition());
        assertEquals(oldTail, event.getOldTailPosition());
        assertTrue(event.toString().contains("SnakeMovedEvent"));
        assertTrue(event.toString().contains("from (5, 5) to (6, 5)"));
    }

    @Test
    void testGameOverEvent() {
        Position collisionPosition = new Position(5, 5);
        GameOverEvent event = new GameOverEvent(collisionPosition);

        assertEquals(collisionPosition, event.getCollisionPosition());
        assertTrue(event.toString().contains("GameOverEvent"));
        assertTrue(event.toString().contains("at (5, 5)"));
    }

    @Test
    void testVictoryEvent() {
        int finalScore = 100;
        VictoryEvent event = new VictoryEvent(finalScore);

        assertEquals(finalScore, event.getFinalScore());
        assertTrue(event.toString().contains("VictoryEvent"));
        assertTrue(event.toString().contains("score: 100"));
    }

    @Test
    void testStateChangedEvent() {
        GameState oldState = GameState.RUNNING;
        GameState newState = GameState.PAUSED;
        StateChangedEvent event = new StateChangedEvent(oldState, newState);

        assertEquals(oldState, event.getOldState());
        assertEquals(newState, event.getNewState());
        assertTrue(event.toString().contains("StateChangedEvent"));
        assertTrue(event.toString().contains("RUNNING"));
        assertTrue(event.toString().contains("PAUSED"));
    }

    @Test
    void testEventEquality() {
        // Тестируем equals и hashCode для FoodEatenEvent
        Position food1 = new Position(5, 5);
        Position food2 = new Position(7, 7);
        FoodEatenEvent event1 = new FoodEatenEvent(food1, food2);
        FoodEatenEvent event2 = new FoodEatenEvent(food1, food2);
        FoodEatenEvent event3 = new FoodEatenEvent(food2, food1);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
        assertNotEquals(event1, null);
        assertNotEquals(event1, new Object());

        // Тестируем equals и hashCode для SnakeMovedEvent
        Position oldHead = new Position(5, 5);
        Position newHead = new Position(6, 5);
        Position oldTail = new Position(4, 5);
        SnakeMovedEvent moveEvent1 = new SnakeMovedEvent(oldHead, newHead, oldTail);
        SnakeMovedEvent moveEvent2 = new SnakeMovedEvent(oldHead, newHead, oldTail);
        SnakeMovedEvent moveEvent3 = new SnakeMovedEvent(newHead, oldHead, oldTail);

        assertEquals(moveEvent1, moveEvent2);
        assertEquals(moveEvent1.hashCode(), moveEvent2.hashCode());
        assertNotEquals(moveEvent1, moveEvent3);
    }

    @Test
    void testEventToString() {
        // Проверяем, что toString содержит всю важную информацию
        Position pos1 = new Position(5, 5);
        Position pos2 = new Position(6, 5);
        Position pos3 = new Position(4, 5);

        FoodEatenEvent foodEvent = new FoodEatenEvent(pos1, pos2);
        String foodEventString = foodEvent.toString();
        assertTrue(foodEventString.contains("5, 5"));
        assertTrue(foodEventString.contains("6, 5"));

        SnakeMovedEvent moveEvent = new SnakeMovedEvent(pos1, pos2, pos3);
        String moveEventString = moveEvent.toString();
        assertTrue(moveEventString.contains("5, 5"));
        assertTrue(moveEventString.contains("6, 5"));
        assertTrue(moveEventString.contains("4, 5"));

        GameOverEvent gameOverEvent = new GameOverEvent(pos1);
        String gameOverString = gameOverEvent.toString();
        assertTrue(gameOverString.contains("5, 5"));

        VictoryEvent victoryEvent = new VictoryEvent(100);
        String victoryString = victoryEvent.toString();
        assertTrue(victoryString.contains("100"));

        StateChangedEvent stateEvent = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        String stateString = stateEvent.toString();
        assertTrue(stateString.contains("RUNNING"));
        assertTrue(stateString.contains("PAUSED"));
    }
}
