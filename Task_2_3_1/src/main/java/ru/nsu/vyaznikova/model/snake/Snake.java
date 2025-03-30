package ru.nsu.vyaznikova.model.snake;

import ru.nsu.vyaznikova.model.grid.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс змейки, отвечающий за:
 * - Хранение списка позиций тела змейки
 * - Управление движением (добавление головы, удаление хвоста)
 * - Управление ростом при поедании еды
 * - Предоставление информации о позициях головы и хвоста
 * - Отслеживание текущей длины змейки
 * - Проверку столкновений с телом змейки
 */
public class Snake {
    private final List<Position> body;
    private int length;

    public Snake(Position startPosition) {
        this.body = new ArrayList<>();
        this.body.add(startPosition);
        this.length = 1;
    }

    public void grow(Position newHeadPosition) {
        body.add(0, newHeadPosition);
        length++;
    }

    public void move(Position newHeadPosition) {
        body.add(0, newHeadPosition);
        body.remove(body.size() - 1);
    }

    public Position getHeadPosition() {
        return body.get(0);
    }

    public Position getTailPosition() {
        return body.get(body.size() - 1);
    }

    public int getLength() {
        return length;
    }

    public boolean containsPosition(Position position) {
        return body.contains(position);
    }

    public List<Position> getBody() {
        return new ArrayList<>(body);
    }
}