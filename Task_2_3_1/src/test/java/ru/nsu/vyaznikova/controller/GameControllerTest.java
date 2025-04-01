package ru.nsu.vyaznikova.controller;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vyaznikova.model.game.GameModel;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private GameModel gameModel;
    private GameController gameController;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private static final int TARGET_LENGTH = 5;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(GRID_WIDTH, GRID_HEIGHT, TARGET_LENGTH);
        gameController = new GameController(gameModel);
    }

    @Test
    void handleInput_SpaceKey_TogglesPause() {
        boolean initialPauseState = gameModel.isPaused();
        gameController.handleKeyPress(new javafx.scene.input.KeyEvent(null, "", "", KeyCode.SPACE, false, false, false, false));
        assertNotEquals(initialPauseState, gameModel.isPaused());
    }

    @Test
    void handleInput_RKey_ResetsGame() {
        // First move the snake
        gameController.handleKeyPress(new javafx.scene.input.KeyEvent(null, "", "", KeyCode.RIGHT, false, false, false, false));
        gameModel.update();

        // Then reset
        gameController.handleKeyPress(new javafx.scene.input.KeyEvent(null, "", "", KeyCode.R, false, false, false, false));
        
        // Check if snake is back at starting position
        assertEquals(GRID_WIDTH / 2, gameModel.getSnakeHeadX());
        assertEquals(GRID_HEIGHT / 2, gameModel.getSnakeHeadY());
    }
}
