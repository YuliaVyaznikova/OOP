package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие поедания еды змейкой.
 * Содержит информацию о позиции съеденной еды и новой длине змейки.
 */
public class FoodEatenEvent implements Event {
    private final Position foodPosition;
    private final int newScore;

    public FoodEatenEvent(Position foodPosition, int newScore) {
        this.foodPosition = foodPosition;
        this.newScore = newScore;
    }

    @Override
    public String getEventType() {
        return "FOOD_EATEN";
    }

    public Position getFoodPosition() {
        return foodPosition;
    }

    public int getNewScore() {
        return newScore;
    }
}
