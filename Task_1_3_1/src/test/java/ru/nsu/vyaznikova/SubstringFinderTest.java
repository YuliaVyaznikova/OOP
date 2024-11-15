package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SubstringFinderTest {

    @Test
    void testFind_smallFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "абракадабра");
        List<Integer> result = SubstringFinder.find(tempFile.toString(), "бра");
        assertEquals(List.of(1, 8), result);
        Files.delete(tempFile);
    }

    @Test
    void testFind_largeFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        String pattern = "абракадабра";
        int repeats = 1000;
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < repeats; i++) {
            largeText.append(pattern);
        }
        Files.writeString(tempFile, largeText);

        List<Integer> result = SubstringFinder.find(tempFile.toString(), "бра");
        assertTrue(result.size() > 0);
        Files.delete(tempFile);
    }


    @Test
    void testFind_emptyString() {
        List<Integer> result = SubstringFinder.find("some_file.txt", "");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFind_nullString() {
        List<Integer> result = SubstringFinder.find("some_file.txt", null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFind_fileNotFound() {
        List<Integer> result = SubstringFinder.find("nonexistent_file.txt", "test");
        assertTrue(result.isEmpty());
    }


    @Test
    void testGenerateLargeTestFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        SubstringFinder.generateLargeTestFile(tempFile.toString(), "test", 100);
        assertTrue(Files.size(tempFile) > 0);
        Files.delete(tempFile);
    }


}