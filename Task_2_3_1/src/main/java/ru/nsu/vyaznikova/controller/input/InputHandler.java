package ru.nsu.vyaznikova.controller.input;

/**
 * Интерфейс для обработки пользовательского ввода.
 * Определяет контракт для всех обработчиков ввода в игре.
 */
public interface InputHandler {
    /**
     * Обработать событие ввода.
     * @param event Событие ввода для обработки
     */
    void handleInput(InputEvent event);
}
