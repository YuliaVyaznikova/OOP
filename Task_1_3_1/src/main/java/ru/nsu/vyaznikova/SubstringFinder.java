package ru.nsu.vyaznikova;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SubstringFinder {

    public static List<Integer> find(String filename, String substring) {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> indices = new ArrayList<>();
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        StringBuilder tail = new StringBuilder();
        int totalOffset = 0;

        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filename))) {
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                String currentPart = tail + new String(buffer, 0, bytesRead, "UTF-8");
                int index = 0;
                while ((index = currentPart.indexOf(substring, index)) != -1) {
                    indices.add(totalOffset + index);
                    index++;
                }

                totalOffset += currentPart.length();
                tail.setLength(0);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return indices;
    }

    public static void generateLargeTestFile(String filename, String pattern, int repeats) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
            for (int i = 0; i < repeats; i++) {
                writer.write(pattern);
            }
        } catch (IOException e) {
            System.err.println("Ошибка создания тестового файла: " + e.getMessage());
        }
    }
}