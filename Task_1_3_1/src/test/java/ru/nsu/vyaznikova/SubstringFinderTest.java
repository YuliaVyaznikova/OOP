package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the SubstringFinder class.
 */
public class SubstringFinderTest {

    /**
     * Tests finding a substring in an empty file.
     */
    @Test
    public void testEmptyFile() throws IOException {
        File emptyFile = createTempFile("");
        List<Long> result = SubstringFinder.find(emptyFile.getPath(), "test");
        assertEquals(List.of(), result, "No occurrences should be found in an empty file.");
    }

    /**
     * Tests searching for an empty substring.
     */
    @Test
    public void testEmptySubstring() throws IOException {
        File file = createTempFile("sample text");
        List<Long> result = SubstringFinder.find(file.getPath(), "");
        assertEquals(List.of(), result, "Searching for an empty substring should return an empty result.");
    }

    /**
     * Tests finding a single character in the file.
     */
    @Test
    public void testFindSingleCharacter() throws IOException {
        File file = createTempFile("abcabcabc");
        List<Long> result = SubstringFinder.find(file.getPath(), "a");
        assertEquals(List.of(0L, 3L, 6L), result, "The character 'a' should be found at indices 0, 3, and 6.");
    }

    /**
     * Tests finding a substring within a larger text.
     */
    @Test
    public void testFindSubstring() throws IOException {
        File file = createTempFile("this is a simple example of a simple text.");
        List<Long> result = SubstringFinder.find(file.getPath(), "simple");
        assertEquals(List.of(10L, 30L), result, "The substring 'simple' should be found at indices 10 and 28.");
    }

    /**
     * Tests finding a substring in a large file.
     */
    @Test
    public void testLargeFile() throws IOException {
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeText.append("abc");
        }
        largeText.append("test");
        File file = createTempFile(largeText.toString());
        List<Long> result = SubstringFinder.find(file.getPath(), "test");
        assertEquals(List.of(30000L), result, "The substring 'test' should be found at the correct position in a large file.");
    }

    /**
     * Tests finding UTF-8 characters in the file.
     */
    @Test
    public void testUtf8Characters() throws IOException {
        File file = createTempFile("Привет, мир! 😊 Привет!");
        List<Long> result = SubstringFinder.find(file.getPath(), "Привет");
        assertEquals(List.of(0L, 15L), result, "The UTF-8 substring 'Привет' should be found at indices 0 and 14.");
    }

    /**
     * Helper method to create a temporary file with the given content.
     *
     * @param content the content to write to the temporary file
     * @return the temporary file
     * @throws IOException if an I/O error occurs
     */
    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write(content);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }
}