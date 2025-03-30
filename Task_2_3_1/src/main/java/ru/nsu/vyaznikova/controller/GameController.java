/**
 * Контроллер игры, отвечающий за:
 * - Обработку пользовательского ввода (нажатия клавиш)
 * - Преобразование нажатий клавиш в направления движения змейки
 * - Передачу команд в модель игры
 * - Предотвращение недопустимых направлений движения
 */
package ru.nsu.vyaznikova.controller;

import javafx.scene.input.KeyEvent;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.game.GameState;

public class GameController {
    private final GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void handleKeyEvent(KeyEvent event) {
        if (model.getGameState() != GameState.RUNNING) {
            return;
        }

        Direction newDirection = switch (event.getCode()) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> Direction.LEFT;
            case RIGHT -> Direction.RIGHT;
            case SPACE -> {
                pauseGame();
                yield null;
            }
            default -> null;
        };

        if (newDirection != null) {
            model.setDirection(newDirection);
        }
    }

    private void pauseGame() {
        // TODO: Implement pause functionality
    }
}
