package ru.nsu.vyaznikova.view.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.vyaznikova.controller.input.InputEvent;
import ru.nsu.vyaznikova.controller.input.InputEventType;
import ru.nsu.vyaznikova.controller.input.InputHandler;
import ru.nsu.vyaznikova.controller.input.Key;

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
        scene.setOnKeyReleased(this::handleKeyEvent);
    }

    private void handleKeyEvent(KeyEvent event) {
        Key key = convertKeyCode(event.getCode());
        if (key != null) {
            InputEventType type = event.getEventType() == KeyEvent.KEY_PRESSED 
                ? InputEventType.KEY_PRESSED 
                : InputEventType.KEY_RELEASED;
            inputHandler.handleInput(new InputEvent(type, key));
        }
    }

    private Key convertKeyCode(KeyCode code) {
        return switch (code) {
            case UP -> Key.UP;
            case DOWN -> Key.DOWN;
            case LEFT -> Key.LEFT;
            case RIGHT -> Key.RIGHT;
            case SPACE -> Key.SPACE;
            case ESCAPE -> Key.ESCAPE;
            case ENTER -> Key.ENTER;
            default -> null;
        };
    }
}
