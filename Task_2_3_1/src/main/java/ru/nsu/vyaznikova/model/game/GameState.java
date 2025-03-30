package ru.nsu.vyaznikova.model.game;

/**
 * Перечисление состояний игры, отвечающее за:
 * - Определение возможных состояний игры (RUNNING, GAME_OVER, VICTORY)
 * - Обеспечение типобезопасности при работе с состояниями
 * - Контроль жизненного цикла игры
 */
public enum GameState {
    RUNNING,
    PAUSED,
    GAME_OVER,
    VICTORY
}
