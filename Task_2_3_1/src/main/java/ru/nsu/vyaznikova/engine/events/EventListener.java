package ru.nsu.vyaznikova.engine.events;

/**
 * Интерфейс слушателя событий.
 * Определяет контракт для всех обработчиков событий в игре.
 */
@FunctionalInterface
public interface EventListener {
    /**
     * Обработать событие.
     * @param event Событие для обработки
     */
    void onEvent(Event event);
}
