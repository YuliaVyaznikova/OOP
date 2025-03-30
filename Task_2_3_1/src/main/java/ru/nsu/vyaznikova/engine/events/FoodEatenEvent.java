package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие поедания еды змейкой.
 * Содержит информацию о позиции съеденной еды и новой длине змейки.
 */
public class FoodEatenEvent implements Event {
    private final Position eatenFoodPosition;
    private final Position newFoodPosition;

    public FoodEatenEvent(Position eatenFoodPosition, Position newFoodPosition) {
        this.eatenFoodPosition = eatenFoodPosition;
        this.newFoodPosition = newFoodPosition;
    }

    @Override
    public String getEventType() {
        return "FOOD_EATEN";
    }

    public Position getEatenFoodPosition() {
        return eatenFoodPosition;
    }

    public Position getNewFoodPosition() {
        return newFoodPosition;
    }

    @Override
    public String toString() {
        return String.format("FoodEatenEvent{eaten at (%d, %d), new at (%d, %d)}", 
            eatenFoodPosition.x(), eatenFoodPosition.y(),
            newFoodPosition.x(), newFoodPosition.y());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FoodEatenEvent that = (FoodEatenEvent) obj;
        return eatenFoodPosition.equals(that.eatenFoodPosition) && 
               newFoodPosition.equals(that.newFoodPosition);
    }

    @Override
    public int hashCode() {
        int result = eatenFoodPosition.hashCode();
        result = 31 * result + newFoodPosition.hashCode();
        return result;
    }
}
