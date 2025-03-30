package ru.nsu.vyaznikova.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.engine.events.EventBus;
import ru.nsu.vyaznikova.engine.events.*;
import ru.nsu.vyaznikova.model.game.GameState;
import javafx.scene.text.Text;

/**
 * Представление игры, отвечающее за:
 * - Создание и управление Canvas для отрисовки
 * - Подписку на игровые события через EventBus
 * - Отрисовку всех элементов игры (стены, еда, змейка)
 * - Реализацию градиента цвета змейки (темная голова, светлое тело)
 * - Обновление счета и отображение игровых сообщений
 * - Перерисовку поля при изменении состояния игры
 */
public class GameView extends Canvas implements EventListener {
    private final GameModel gameModel;
    private final int cellSize = 30;
    private final Color wallColor = Color.GRAY;
    private final Color foodColor = Color.RED;
    private final Color snakeHeadColor = Color.rgb(0, 100, 0); // Темно-зеленый для головы
    private final Color snakeBaseColor = Color.rgb(0, 200, 0); // Светло-зеленый для тела
    private final Text scoreText;

    public GameView(GameModel gameModel, Text scoreText) {
        this.gameModel = gameModel;
        this.scoreText = scoreText;
        setWidth(gameModel.getWidth() * cellSize);
        setHeight(gameModel.getHeight() * cellSize);
        subscribeToEvents();
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Рисуем все ячейки
        for (int y = 0; y < gameModel.getHeight(); y++) {
            for (int x = 0; x < gameModel.getWidth(); x++) {
                CellType cellType = gameModel.getCellType(x, y);
                switch (cellType) {
                    case WALL -> {
                        gc.setFill(wallColor);
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                    case SNAKE_HEAD -> {
                        gc.setFill(snakeHeadColor);
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                    case SNAKE_BODY -> {
                        // Вычисляем цвет для тела змейки на основе расстояния от головы
                        double distanceFromHead = getDistanceFromHead(x, y);
                        Color segmentColor = calculateSegmentColor(distanceFromHead);
                        gc.setFill(segmentColor);
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                    case FOOD -> {
                        gc.setFill(foodColor);
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                    case EMPTY -> {
                        gc.setFill(Color.WHITE);
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
        }

        // Рисуем сетку
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        for (int x = 0; x <= gameModel.getWidth(); x++) {
            gc.strokeLine(x * cellSize, 0, x * cellSize, getHeight());
        }
        for (int y = 0; y <= gameModel.getHeight(); y++) {
            gc.strokeLine(0, y * cellSize, getWidth(), y * cellSize);
        }
    }

    private double getDistanceFromHead(int x, int y) {
        // Упрощенный расчет расстояния от головы
        // В реальной реализации можно использовать более точный расчет через Snake
        return Math.abs(x - gameModel.getWidth() / 2) + Math.abs(y - gameModel.getHeight() / 2);
    }

    private Color calculateSegmentColor(double distance) {
        // Интерполируем между snakeHeadColor и snakeBaseColor на основе расстояния
        double maxDistance = gameModel.getLength();
        double ratio = Math.min(distance / maxDistance, 1.0);
        
        return Color.rgb(
            (int) (snakeHeadColor.getRed() * 255 * (1 - ratio) + snakeBaseColor.getRed() * 255 * ratio),
            (int) (snakeHeadColor.getGreen() * 255 * (1 - ratio) + snakeBaseColor.getGreen() * 255 * ratio),
            (int) (snakeHeadColor.getBlue() * 255 * (1 - ratio) + snakeBaseColor.getBlue() * 255 * ratio)
        );
    }

    private void subscribeToEvents() {
        EventBus eventBus = EventBus.getInstance();
        
        eventBus.subscribe("SNAKE_MOVED", this);
        eventBus.subscribe("FOOD_EATEN", this);
        eventBus.subscribe("GAME_OVER", this);
        eventBus.subscribe("VICTORY", this);
        eventBus.subscribe("STATE_CHANGED", this);
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getEventType()) {
            case "SNAKE_MOVED" -> draw();
            case "FOOD_EATEN" -> {
                draw();
                scoreText.setText("Length: " + gameModel.getLength());
            }
            case "GAME_OVER" -> {
                GameOverEvent e = (GameOverEvent) event;
                draw();
                scoreText.setText("Game Over! " + e.getCollisionPosition());
            }
            case "VICTORY" -> {
                VictoryEvent e = (VictoryEvent) event;
                draw();
                scoreText.setText("Victory! Score: " + e.getFinalScore());
            }
            case "STATE_CHANGED" -> {
                StateChangedEvent e = (StateChangedEvent) event;
                if (e.getNewState() == GameState.RUNNING) {
                    updateScore();
                }
            }
        }
    }

    private void updateScore() {
        if (gameModel.getGameState() == GameState.RUNNING) {
            scoreText.setText("Length: " + gameModel.getLength());
        }
    }
}
