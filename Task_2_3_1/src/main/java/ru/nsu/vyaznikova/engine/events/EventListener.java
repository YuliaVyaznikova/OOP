package ru.nsu.vyaznikova.engine.events;

/**
 * Функциональный интерфейс слушателя событий, отвечающий за:
 * - Определение контракта для обработки игровых событий
 * - Обеспечение возможности использования лямбда-выражений для обработки событий
 * - Поддержку паттерна Observer в событийной системе
 */
@FunctionalInterface
public interface EventListener {
    void onEvent(GameEvent event);
}
