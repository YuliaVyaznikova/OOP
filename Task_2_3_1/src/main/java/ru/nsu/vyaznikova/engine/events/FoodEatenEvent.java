package ru.nsu.vyaznikova.engine.events;

import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Событие поедания еды змейкой.
 */
public class FoodEatenEvent implements Event {
    private final Position oldFoodPosition;
    private final Position newFoodPosition;

    /**
     * Создает новое событие поедания еды.
     *
     * @param oldFoodPosition позиция съеденной еды
     * @param newFoodPosition позиция новой еды
     */
    public FoodEatenEvent(Position oldFoodPosition, Position newFoodPosition) {
        this.oldFoodPosition = oldFoodPosition;
        this.newFoodPosition = newFoodPosition;
    }

    /**
     * @return позиция съеденной еды
     */
    public Position getOldFoodPosition() {
        return oldFoodPosition;
    }

    /**
     * @return позиция новой еды
     */
    public Position getNewFoodPosition() {
        return newFoodPosition;
    }

    @Override
    public String getEventType() {
        return "FOOD_EATEN";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodEatenEvent that = (FoodEatenEvent) o;
        return oldFoodPosition.equals(that.oldFoodPosition) &&
               newFoodPosition.equals(that.newFoodPosition);
    }

    @Override
    public int hashCode() {
        int result = oldFoodPosition.hashCode();
        result = 31 * result + newFoodPosition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FoodEatenEvent{" +
                "oldFoodPosition=" + oldFoodPosition +
                ", newFoodPosition=" + newFoodPosition +
                '}';
    }
}
