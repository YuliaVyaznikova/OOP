package ru.nsu.vyaznikova.view.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.vyaznikova.controller.input.InputEvent;
import ru.nsu.vyaznikova.controller.input.InputHandler;

/**
 * Адаптер для преобразования событий JavaFX в события ввода игры.
 */
public class JavaFXInputAdapter {
    private final InputHandler inputHandler;

    public JavaFXInputAdapter(Scene scene, InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        setupInputHandling(scene);
    }

    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyEvent);
    }

    private void handleKeyEvent(KeyEvent event) {
        KeyCode code = event.getCode();
        if (isValidGameKey(code)) {
            inputHandler.handleInput(new InputEvent(code));
        }
    }

    private boolean isValidGameKey(KeyCode code) {
        return switch (code) {
            case UP, DOWN, LEFT, RIGHT, SPACE, R -> true;
            default -> false;
        };
    }
}
