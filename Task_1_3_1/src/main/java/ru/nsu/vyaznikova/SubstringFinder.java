package ru.nsu.vyaznikova;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

 public class SubstringFinder {

    public static List<Integer> find(String filename, String substring) throws IOException {
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

                totalOffset += currentPart.length() - substring.length();
                tail.setLength(0);
                tail.append(currentPart, currentPart.length() - substring.length() + 1, currentPart.length());
            }
        }

        return indices;
    }

    public static void generateLargeTestFile(String filename, String pattern, int repeats) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
            for (int i = 0; i < repeats; i++) {
                writer.write(pattern);
            }
        }
    }

    public static void main(String[] args) {
        try {
            generateLargeTestFile("input.txt", "абракадабра", 1000);
            List<Integer> result = find("input.txt", "бра");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}