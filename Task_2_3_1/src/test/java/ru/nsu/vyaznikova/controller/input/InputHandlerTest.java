package ru.nsu.vyaznikova.controller.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.input.KeyCode;
import ru.nsu.vyaznikova.model.game.GameModel;

class InputHandlerTest {
    private GameModel gameModel;
    private TestInputHandler inputHandler;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private static final int TARGET_LENGTH = 5;

    private class TestInputHandler implements InputHandler {
        private final GameModel model;

        TestInputHandler(GameModel model) {
            this.model = model;
        }

        @Override
        public void handleInput(InputEvent event) {
            switch (event.keyCode()) {
                case SPACE -> model.togglePause();
                case R -> model.reset();
                default -> {} // Ignore other keys
            }
        }
    }

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(GRID_WIDTH, GRID_HEIGHT, TARGET_LENGTH);
        inputHandler = new TestInputHandler(gameModel);
    }

    @Test
    void handleInput_SpaceKey_TogglesPause() {
        boolean initialPauseState = gameModel.isPaused();
        inputHandler.handleInput(new InputEvent(KeyCode.SPACE));
        assertNotEquals(initialPauseState, gameModel.isPaused());
    }

    @Test
    void handleInput_RKey_ResetsGame() {
        // First pause the game
        inputHandler.handleInput(new InputEvent(KeyCode.SPACE));
        assertTrue(gameModel.isPaused());

        // Then reset
        inputHandler.handleInput(new InputEvent(KeyCode.R));
        assertFalse(gameModel.isPaused());
    }
}
