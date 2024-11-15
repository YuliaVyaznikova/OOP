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
                int index = currentPart.indexOf(substring);
                while (index != -1) {
                    indices.add(totalOffset + index);
                    index = currentPart.indexOf(substring, index + 1);
                }

                int lastIndex = currentPart.lastIndexOf(substring);
                totalOffset += (lastIndex == -1) ? currentPart.length() : lastIndex + substring.length();

                tail.setLength(0);
                if (lastIndex != -1) {
                    tail.append(currentPart, lastIndex + substring.length(), currentPart.length());
                } else {
                    tail.append(currentPart, Math.max(0, currentPart.length() - substring.length()), currentPart.length());
                }
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

    public static void main(String[] args) {
        generateLargeTestFile("input.txt", "абракадабра", 1000);
        List<Integer> result = find("input.txt", "бра");
        System.out.println(result);


        //Тест с пустой строкой
        List<Integer> result2 = find("input.txt", "");
        System.out.println(result2);

        //Тест с null
        List<Integer> result3 = find("input.txt", null);
        System.out.println(result3);

    }
}