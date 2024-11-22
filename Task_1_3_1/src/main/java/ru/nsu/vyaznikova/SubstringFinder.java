package ru.nsu.vyaznikova;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for finding all starting indices of a substring in a UTF-8 encoded file.
 */
public class SubstringFinder {

    /**
     * Finds all starting indices of a given substring in the specified file.
     *
     * @param filename  the name of the file to search
     * @param substring the substring to search for
     * @return a list of starting indices (as long values) where the substring is found
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static List<Long> find(String filename, String substring) throws IOException {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>(); // Return an empty list for empty or null substrings
        }

        byte[] targetBytes = substring.getBytes(StandardCharsets.UTF_8);
        int targetLength = targetBytes.length;

        List<Long> indices = new ArrayList<>();
        long currentCharPosition = 0; // Tracks the position of characters in the file
        int bufferSize = Math.max(4096, targetLength * 2); // Buffer size, at least twice the substring length
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        int overlap = targetLength - 1; // Number of bytes to overlap between buffers
        byte[] overlapBytes = new byte[overlap];
        int overlapBytesLength = 0;

        try (FileInputStream stream = new FileInputStream(filename)) {
            while ((bytesRead = stream.read(buffer)) != -1) {
                // Combine overlap from previous buffer with the current buffer
                byte[] combinedBuffer = new byte[overlapBytesLength + bytesRead];
                System.arraycopy(overlapBytes, 0, combinedBuffer, 0, overlapBytesLength);
                System.arraycopy(buffer, 0, combinedBuffer, overlapBytesLength, bytesRead);

                // Prepare overlap bytes for the next iteration
                overlapBytesLength = Math.min(overlap, bytesRead);
                System.arraycopy(buffer, bytesRead - overlapBytesLength, overlapBytes, 0, overlapBytesLength);

                // Search for the substring in the combined buffer
                for (int i = 0; i <= combinedBuffer.length - targetLength; i++) {
                    boolean match = true;
                    for (int j = 0; j < targetLength; j++) {
                        if (combinedBuffer[i + j] != targetBytes[j]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        indices.add(currentCharPosition);
                    }
                    // Increment the character position for UTF-8 valid starting bytes
                    if ((combinedBuffer[i] & 0b11000000) != 0b10000000) {
                        currentCharPosition++;
                    }
                }
            }
        }

        return indices;
    }
}