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

        assertEquals(foodPosition, event.getOldFoodPosition());
        assertEquals(newFoodPosition, event.getNewFoodPosition());
        assertEquals("FOOD_EATEN", event.getEventType());
        assertTrue(event.toString().contains("oldFoodPosition=" + foodPosition));
        assertTrue(event.toString().contains("newFoodPosition=" + newFoodPosition));
    }

    @Test
    void testGameOverEvent() {
        Position collisionPosition = new Position(5, 5);
        GameOverEvent event = new GameOverEvent(collisionPosition);

        assertEquals(collisionPosition, event.getPosition());
        assertEquals("GAME_OVER", event.getEventType());
        assertTrue(event.toString().contains("position=" + collisionPosition));
    }

    @Test
    void testVictoryEvent() {
        int finalLength = 10;
        VictoryEvent event = new VictoryEvent(finalLength);

        assertEquals(finalLength, event.getFinalLength());
        assertEquals("VICTORY", event.getEventType());
        assertTrue(event.toString().contains("finalLength=" + finalLength));
    }

    @Test
    void testStateChangedEvent() {
        GameState oldState = GameState.RUNNING;
        GameState newState = GameState.PAUSED;
        StateChangedEvent event = new StateChangedEvent(oldState, newState);

        assertEquals(oldState, event.getOldState());
        assertEquals(newState, event.getNewState());
        assertEquals("STATE_CHANGED", event.getEventType());
        assertTrue(event.toString().contains("oldState=" + oldState));
        assertTrue(event.toString().contains("newState=" + newState));
    }

    @Test
    void testEventEquality() {
        // Test FoodEatenEvent equality
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);
        FoodEatenEvent food1 = new FoodEatenEvent(pos1, pos2);
        FoodEatenEvent food2 = new FoodEatenEvent(pos1, pos2);
        assertEquals(food1, food2);
        assertEquals(food1.hashCode(), food2.hashCode());

        // Test GameOverEvent equality
        GameOverEvent gameOver1 = new GameOverEvent(pos1);
        GameOverEvent gameOver2 = new GameOverEvent(pos1);
        assertEquals(gameOver1, gameOver2);
        assertEquals(gameOver1.hashCode(), gameOver2.hashCode());

        // Test VictoryEvent equality
        VictoryEvent victory1 = new VictoryEvent(10);
        VictoryEvent victory2 = new VictoryEvent(10);
        assertEquals(victory1, victory2);
        assertEquals(victory1.hashCode(), victory2.hashCode());

        // Test StateChangedEvent equality
        StateChangedEvent state1 = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        StateChangedEvent state2 = new StateChangedEvent(GameState.RUNNING, GameState.PAUSED);
        assertEquals(state1, state2);
        assertEquals(state1.hashCode(), state2.hashCode());
    }
}
