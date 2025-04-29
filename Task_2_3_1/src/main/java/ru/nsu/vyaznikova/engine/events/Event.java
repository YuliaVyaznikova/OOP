package ru.nsu.vyaznikova.engine.events;

/**
 * Базовый интерфейс для всех событий в игре.
 * Определяет общий контракт для всех типов событий.
 */
public interface Event {
    /**
     * Получить тип события.
     * @return Строковый идентификатор типа события
     */
    String getEventType();
}
