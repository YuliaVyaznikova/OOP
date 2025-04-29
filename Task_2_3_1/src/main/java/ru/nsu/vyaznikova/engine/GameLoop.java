package ru.nsu.vyaznikova.engine;

/**
 * Интерфейс игрового цикла, отвечающий за:
 * - Определение базового поведения игрового цикла
 * - Предоставление методов для запуска и остановки цикла
 * - Обеспечение возможности создания разных реализаций игрового цикла
 */
public interface GameLoop {
    void start();
    void stop();
    void pause();
    void resume();
    boolean isRunning();
}
