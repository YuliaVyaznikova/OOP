package ru.nsu.vyaznikova.controller;

import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.snake.Direction;
import ru.nsu.vyaznikova.model.game.GameState;
import ru.nsu.vyaznikova.controller.input.*;

/**
 * Контроллер игры, отвечающий за:
 * - Обработку пользовательского ввода
 * - Преобразование ввода в команды игры
 * - Передачу команд в модель игры
 * - Предотвращение недопустимых действий
 */
public class GameController implements InputHandler {
    private final GameModel model;
    private boolean isPaused = false;

    public GameController(GameModel model) {
        this.model = model;
    }

    @Override
    public void handleInput(InputEvent event) {
        if (event.getType() != InputEventType.KEY_PRESSED) {
            return;
        }

        if (model.getGameState() != GameState.RUNNING && event.getKey() != Key.SPACE) {
            return;
        }

        switch (event.getKey()) {
            case UP -> model.setDirection(Direction.UP);
            case DOWN -> model.setDirection(Direction.DOWN);
            case LEFT -> model.setDirection(Direction.LEFT);
            case RIGHT -> model.setDirection(Direction.RIGHT);
            case SPACE -> togglePause();
            default -> {}
        }
    }

    private void togglePause() {
        isPaused = !isPaused;
        // TODO: Implement pause functionality in GameModel
    }
}
