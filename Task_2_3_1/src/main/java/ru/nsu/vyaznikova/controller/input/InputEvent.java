package ru.nsu.vyaznikova.controller.input;

import javafx.scene.input.KeyCode;

/**
 * Класс, представляющий событие ввода.
 */
public record InputEvent(KeyCode keyCode) {
    /**
     * Создает новое событие ввода.
     *
     * @param keyCode код клавиши
     */
    public InputEvent {
        if (keyCode == null) {
            throw new IllegalArgumentException("KeyCode cannot be null");
        }
    }
}
