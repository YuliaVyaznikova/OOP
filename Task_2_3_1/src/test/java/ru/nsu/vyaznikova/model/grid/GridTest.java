package ru.nsu.vyaznikova.model.grid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для Grid.
 * Проверяет:
 * - Инициализацию поля
 * - Установку и получение значений ячеек
 * - Проверку границ
 * - Поиск пустых ячеек
 * - Работу со стенами
 */
public class GridTest {
    private Grid grid;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    @BeforeEach
    void setUp() {
        grid = new Grid(WIDTH, HEIGHT);
    }

    /**
     * Проверяет корректность инициализации поля
     */
    @Test
    void testGridInitialization() {
        assertEquals(WIDTH, grid.getWidth());
        assertEquals(HEIGHT, grid.getHeight());

        // Проверяем стены по периметру
        for (int x = 0; x < WIDTH; x++) {
            assertEquals(CellType.WALL, grid.getCell(new Position(x, 0)));
            assertEquals(CellType.WALL, grid.getCell(new Position(x, HEIGHT - 1)));
        }
        for (int y = 0; y < HEIGHT; y++) {
            assertEquals(CellType.WALL, grid.getCell(new Position(0, y)));
            assertEquals(CellType.WALL, grid.getCell(new Position(WIDTH - 1, y)));
        }

        // Проверяем, что внутренние клетки пустые
        for (int y = 1; y < HEIGHT - 1; y++) {
            for (int x = 1; x < WIDTH - 1; x++) {
                assertEquals(CellType.EMPTY, grid.getCell(new Position(x, y)));
            }
        }
    }

    /**
     * Проверяет установку и получение значений ячеек
     */
    @Test
    void testSetAndGetCell() {
        Position pos = new Position(5, 5);
        grid.setCell(pos, CellType.SNAKE_HEAD);
        assertEquals(CellType.SNAKE_HEAD, grid.getCell(pos));

        grid.setCell(pos, CellType.SNAKE_BODY);
        assertEquals(CellType.SNAKE_BODY, grid.getCell(pos));

        grid.setCell(pos, CellType.FOOD);
        assertEquals(CellType.FOOD, grid.getCell(pos));
    }

    /**
     * Проверяет валидацию позиций
     */
    @Test
    void testIsValidPosition() {
        // Проверяем валидные позиции
        assertTrue(grid.isValidPosition(new Position(1, 1)));
        assertTrue(grid.isValidPosition(new Position(WIDTH - 1, HEIGHT - 1)));

        // Проверяем невалидные позиции
        assertFalse(grid.isValidPosition(new Position(-1, 5)));
        assertFalse(grid.isValidPosition(new Position(5, -1)));
        assertFalse(grid.isValidPosition(new Position(WIDTH, 5)));
        assertFalse(grid.isValidPosition(new Position(5, HEIGHT)));
    }

    /**
     * Проверяет поиск случайной пустой ячейки
     */
    @Test
    void testFindRandomEmptyCell() {
        // Заполняем почти все поле, оставляя одну пустую ячейку
        Position emptyPos = new Position(5, 5);
        for (int y = 1; y < HEIGHT - 1; y++) {
            for (int x = 1; x < WIDTH - 1; x++) {
                if (x != emptyPos.x() || y != emptyPos.y()) {
                    grid.setCell(new Position(x, y), CellType.SNAKE_BODY);
                }
            }
        }

        // Проверяем, что findRandomEmptyCell находит единственную пустую ячейку
        Position found = grid.findRandomEmptyCell();
        assertEquals(emptyPos, found);
        assertEquals(CellType.EMPTY, grid.getCell(found));
    }

    /**
     * Проверяет, что случайная пустая ячейка не появляется в стенах
     */
    @Test
    void testRandomEmptyCellNotInWalls() {
        for (int i = 0; i < 100; i++) { // Проверяем много раз для уверенности
            Position pos = grid.findRandomEmptyCell();
            assertTrue(pos.x() > 1 && pos.x() < WIDTH - 2);
            assertTrue(pos.y() > 1 && pos.y() < HEIGHT - 2);
            assertEquals(CellType.EMPTY, grid.getCell(pos));
        }
    }

    /**
     * Проверяет граничные случаи при работе с сеткой
     */
    @Test
    void testEdgeCases() {
        // Пробуем установить значение в стену
        Position wallPos = new Position(0, 0);
        grid.setCell(wallPos, CellType.SNAKE_HEAD);
        assertEquals(CellType.SNAKE_HEAD, grid.getCell(wallPos));

        // Проверяем угловые позиции
        assertTrue(grid.isValidPosition(new Position(0, 0)));
        assertTrue(grid.isValidPosition(new Position(WIDTH - 1, 0)));
        assertTrue(grid.isValidPosition(new Position(0, HEIGHT - 1)));
        assertTrue(grid.isValidPosition(new Position(WIDTH - 1, HEIGHT - 1)));
    }
}
