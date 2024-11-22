package ru.nsu.vyaznikova;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubstringFinder {
    /**
     * Находит все вхождения подстроки в файле.
     *
     * @param filename имя файла
     * @param substring подстрока для поиска
     * @return список позиций начала вхождений
     * @throws IOException если файл не найден или произошла ошибка чтения
     */
    public static List<Long> find(String filename, String substring) throws IOException {
        // Преобразуем искомую подстроку в массив байтов
        byte[] searchBytes = substring.getBytes(StandardCharsets.UTF_8);
        int searchLength = searchBytes.length;

        // Установим размер буфера больше длины подстроки
        int bufferSize = Math.max(8192, searchLength * 2);
        CircularBuffer buffer = new CircularBuffer(bufferSize);

        List<Long> indices = new ArrayList<>();
        long currentSymbolPosition = 0; // Счётчик символов (в UTF-8)

        try (InputStream inputStream = new FileInputStream(filename)) {
            int currentByte;
            while ((currentByte = inputStream.read()) != -1) {
                byte b = (byte) currentByte;
                buffer.add(b);

                // Обновляем счётчик символов при обнаружении начала нового символа UTF-8
                if (isStartOfUtf8Character(b)) {
                    currentSymbolPosition++;
                }

                // Проверяем, есть ли совпадение подстроки в буфере
                if (buffer.matches(searchBytes)) {
                    indices.add(currentSymbolPosition - substring.length());
                }
            }
        }

        return indices;
    }

    /**
     * Проверяет, является ли байт началом символа UTF-8.
     *
     * @param b байт
     * @return true, если это начало символа
     */
    private static boolean isStartOfUtf8Character(byte b) {
        return (b & 0b10000000) == 0 || (b & 0b11000000) == 0b11000000;
    }

    public static void main(String[] args) throws IOException {
        String filename = "input.txt";
        String substring = "бра";

        // Пример вызова функции
        List<Long> indices = find(filename, substring);
        System.out.println(indices);
    }
}

/**
 * Класс для циклического буфера.
 */
class CircularBuffer {
    private final byte[] buffer;
    private int size = 0;
    private int start = 0;

    public CircularBuffer(int capacity) {
        this.buffer = new byte[capacity];
    }

    /**
     * Добавляет байт в буфер.
     *
     * @param b добавляемый байт
     */
    public void add(byte b) {
        buffer[(start + size) % buffer.length] = b;
        if (size < buffer.length) {
            size++;
        } else {
            start = (start + 1) % buffer.length;
        }
    }

    /**
     * Проверяет, совпадает ли содержимое буфера с заданным массивом байтов.
     *
     * @param bytes массив байтов для сравнения
     * @return true, если совпадает
     */
    public boolean matches(byte[] bytes) {
        if (bytes.length > size) {
            return false;
        }
        for (int i = 0; i < bytes.length; i++) {
            if (buffer[(start + i) % buffer.length] != bytes[i]) {
                return false;
            }
        }
        return true;
    }
}