package ru.nsu.vyaznikova.controller.input;

/**
 * Класс, представляющий событие ввода в игре.
 * Инкапсулирует тип события и связанные с ним данные.
 */
public class InputEvent {
    private final InputEventType type;
    private final Key key;

    public InputEvent(InputEventType type, Key key) {
        this.type = type;
        this.key = key;
    }

    public InputEventType getType() {
        return type;
    }

    public Key getKey() {
        return key;
    }
}
