package ru.nsu.vyaznikova;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubstringFinderTest {
    private static final String TEST_FILE = "test_input.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Создаём временный тестовый файл
        try (FileWriter writer = new FileWriter(TEST_FILE, StandardCharsets.UTF_8)) {
            writer.write("абракадабра");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Удаляем временный тестовый файл
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    void testFindFullString() throws IOException {
        List<Long> indices = SubstringFinder.find(TEST_FILE, "абракадабра");
        assertEquals(List.of(0L), indices);
    }

    @Test
    void testNoOccurrences() throws IOException {
        List<Long> indices = SubstringFinder.find(TEST_FILE, "не существует");
        assertTrue(indices.isEmpty());
    }

    @Test
    void testFileNotFound() {
        assertThrows(IOException.class, () -> SubstringFinder.find("non_existent_file.txt", "бра"));
    }
}