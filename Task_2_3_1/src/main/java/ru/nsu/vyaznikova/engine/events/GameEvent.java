package ru.nsu.vyaznikova.engine.events;

/**
 * Класс события игры, отвечающий за:
 * - Определение типов игровых событий (движение змейки, поедание еды и т.д.)
 * - Передачу информации о произошедших событиях между компонентами
 * - Обеспечение типобезопасного способа коммуникации в игре
 */
public class GameEvent {
    private final EventType type;

    public GameEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public enum EventType {
        SNAKE_MOVED,
        FOOD_EATEN,
        COLLISION,
        GAME_OVER,
        VICTORY,
        STATE_CHANGED
    }
}
