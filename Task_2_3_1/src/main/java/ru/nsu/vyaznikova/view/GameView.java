package ru.nsu.vyaznikova.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ru.nsu.vyaznikova.engine.events.*;
import ru.nsu.vyaznikova.model.grid.CellType;
import ru.nsu.vyaznikova.model.grid.Position;

/**
 * Отвечает за отрисовку игры.
 */
public class GameView {
    private static final int CELL_SIZE = 20;
    private static final int GRID_LINE_WIDTH = 1;
    private static final Color GRID_COLOR = Color.LIGHTGRAY;
    private static final Color TEXT_COLOR = Color.BLACK;

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int width;
    private final int height;
    private final CellType[][] grid;

    /**
     * Создает новое представление игры.
     *
     * @param width ширина поля в клетках
     * @param height высота поля в клетках
     * @param grid сетка игрового поля
     */
    public GameView(int width, int height, CellType[][] grid) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.canvas = new Canvas(width * CELL_SIZE, height * CELL_SIZE);
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * @return canvas для отрисовки
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Отрисовывает игровое поле.
     */
    public void render() {
        clearCanvas();
        drawGrid();
        drawGameObjects();
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawGrid() {
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(GRID_LINE_WIDTH);

        // Вертикальные линии
        for (int x = 0; x <= width; x++) {
            gc.strokeLine(x * CELL_SIZE, 0, x * CELL_SIZE, height * CELL_SIZE);
        }

        // Горизонтальные линии
        for (int y = 0; y <= height; y++) {
            gc.strokeLine(0, y * CELL_SIZE, width * CELL_SIZE, y * CELL_SIZE);
        }
    }

    private void drawGameObjects() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellType cellType = grid[y][x];
                gc.setFill(getCellColor(cellType));
                gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private Color getCellColor(CellType cellType) {
        return switch (cellType) {
            case EMPTY -> Color.WHITE;
            case WALL -> Color.GRAY;
            case SNAKE_HEAD -> Color.YELLOW;
            case SNAKE_BODY -> Color.GREEN;
            case FOOD -> Color.RED;
            case AI_SNAKE -> Color.BLUE;
        };
    }

    /**
     * Отображает сообщение о конце игры.
     *
     * @param position позиция столкновения
     */
    public void showGameOver(Position position) {
        showMessage("Game Over!", "Collision at (" + position.x() + ", " + position.y() + ")");
    }

    /**
     * Отображает сообщение о победе.
     *
     * @param finalLength итоговая длина змейки
     */
    public void showVictory(int finalLength) {
        showMessage("Victory!", "Final length: " + finalLength);
    }

    private void showMessage(String title, String subtitle) {
        gc.setFill(new Color(0, 0, 0, 0.5)); // Полупрозрачный черный
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(TEXT_COLOR);
        gc.setTextAlign(TextAlignment.CENTER);

        // Отрисовка заголовка
        gc.setFont(new Font(30));
        gc.fillText(title, canvas.getWidth() / 2, canvas.getHeight() / 2 - 20);

        // Отрисовка подзаголовка
        gc.setFont(new Font(20));
        gc.fillText(subtitle, canvas.getWidth() / 2, canvas.getHeight() / 2 + 20);
    }

    /**
     * Обработчик события окончания игры.
     *
     * @param event событие окончания игры
     */
    public void onGameOver(GameOverEvent event) {
        showGameOver(event.getPosition());
    }

    /**
     * Обработчик события победы.
     *
     * @param event событие победы
     */
    public void onVictory(VictoryEvent event) {
        showVictory(event.getFinalLength());
    }
}
