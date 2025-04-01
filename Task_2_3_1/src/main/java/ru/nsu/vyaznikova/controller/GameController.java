package ru.nsu.vyaznikova.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.vyaznikova.controller.input.InputEvent;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.model.snake.Direction;

/**
 * Контроллер игры, обрабатывающий пользовательский ввод.
 */
public class GameController {
    private final GameModel model;

    /**
     * Создает новый контроллер игры.
     *
     * @param model модель игры
     */
    public GameController(GameModel model) {
        this.model = model;
    }

    /**
     * Обрабатывает нажатие клавиши.
     *
     * @param event событие нажатия клавиши
     */
    public void handleKeyPress(KeyEvent event) {
        handleInput(new InputEvent(event.getCode()));
    }

    /**
     * Обрабатывает пользовательский ввод.
     *
     * @param event событие ввода
     */
    public void handleInput(InputEvent event) {
        KeyCode keyCode = event.keyCode();
        Direction currentDirection = model.getSnakeDirection();
        
        switch (keyCode) {
            case UP, W -> {
                if (currentDirection == null || currentDirection != Direction.DOWN) {
                    model.setDirection(Direction.UP);
                }
            }
            case DOWN, S -> {
                if (currentDirection == null || currentDirection != Direction.UP) {
                    model.setDirection(Direction.DOWN);
                }
            }
            case LEFT, A -> {
                if (currentDirection == null || currentDirection != Direction.RIGHT) {
                    model.setDirection(Direction.LEFT);
                }
            }
            case RIGHT, D -> {
                if (currentDirection == null || currentDirection != Direction.LEFT) {
                    model.setDirection(Direction.RIGHT);
                }
            }
            case SPACE -> model.togglePause();
            case R -> model.reset();
            default -> {} // Do nothing for other keys
        }
    }
}
