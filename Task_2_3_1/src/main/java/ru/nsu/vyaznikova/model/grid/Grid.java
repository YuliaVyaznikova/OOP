package ru.nsu.vyaznikova.model.grid;

import java.util.Random;

/**
 * Класс игрового поля, отвечающий за:
 * - Хранение двумерного массива клеток игрового поля
 * - Предоставление методов для доступа и изменения состояния клеток
 * - Проверку границ поля
 * - Поиск случайных пустых клеток для размещения еды
 */
public class Grid {
    private final int width;
    private final int height;
    private final CellType[][] cells;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new CellType[height][width];
        initializeGrid();
    }

    private void initializeGrid() {
        // Заполняем поле пустыми клетками
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = CellType.EMPTY;
            }
        }

        // Создаем стены по периметру
        for (int x = 0; x < width; x++) {
            cells[0][x] = CellType.WALL;
            cells[height - 1][x] = CellType.WALL;
        }
        for (int y = 0; y < height; y++) {
            cells[y][0] = CellType.WALL;
            cells[y][width - 1] = CellType.WALL;
        }
    }

    public Position findRandomEmptyCell() {
        Random random = new Random();
        int x, y;
        do {
            // Спавним подальше от стен
            x = random.nextInt(width - 4) + 2;
            y = random.nextInt(height - 4) + 2;
        } while (cells[y][x] != CellType.EMPTY);
        
        return new Position(x, y);
    }

    public void setCell(Position pos, CellType type) {
        cells[pos.y()][pos.x()] = type;
    }

    public CellType getCell(Position pos) {
        return cells[pos.y()][pos.x()];
    }

    public boolean isValidPosition(Position pos) {
        return pos.x() >= 0 && pos.x() < width && 
               pos.y() >= 0 && pos.y() < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
