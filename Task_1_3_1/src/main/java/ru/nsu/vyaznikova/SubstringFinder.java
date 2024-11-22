package ru.nsu.vyaznikova;

import java.io.FileInputStream;
import java.io.InputStream;
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

        // Use UTF-8 to encode the substring to search for
        byte[] substringBytes = substring.getBytes(StandardCharsets.UTF_8);
        int substringLength = substringBytes.length;
        CircularBuffer buffer = new CircularBuffer(substringLength + 1);
        List<Long> indices = new ArrayList<>();

        try (InputStream input = new FileInputStream(filename)) {
            long currentFilePos = 0;     // Current position in the file (in bytes)
            long currentCharCount = 0;   // Number of UTF-8 characters processed
            int currentSubstringPos = 0;
            int bytesRead;

            while ((bytesRead = input.read()) != -1) {
                byte currentByte = (byte) bytesRead;

                // Update character count if this byte starts a new UTF-8 character
                int utf8ByteLength = getUtf8ByteLength(currentByte);
                if (utf8ByteLength > 0) {
                    currentCharCount++;
                }

                buffer.set(currentFilePos, currentByte);

                // Matching process (considering multi-byte UTF-8 characters)
                if (currentSubstringPos < substringLength &&
                        substringBytes[currentSubstringPos] == buffer.get(currentFilePos)) {
                    currentSubstringPos++;
                    if (currentSubstringPos == substringLength) {
                        // Found a match, record the position using character count, not byte position
                        indices.add(currentCharCount - substring.length());
                    }
                } else {
                    currentSubstringPos = 0;
                }

                currentFilePos++;
            }
        }

        return indices;
    }

    /**
     * Determines the number of bytes in the current UTF-8 character based on its first byte.
     *
     * @param b the byte to analyze
     * @return the number of bytes in the UTF-8 character, or 0 if this byte is not a valid start byte
     */
    private static int getUtf8ByteLength(byte b) {
        if ((b & 0b10000000) == 0) { // Single-byte character (ASCII)
            return 1;
        } else if ((b & 0b11100000) == 0b11000000) { // Start of a 2-byte character
            return 2;
        } else if ((b & 0b11110000) == 0b11100000) { // Start of a 3-byte character
            return 3;
        } else if ((b & 0b11111000) == 0b11110000) { // Start of a 4-byte character (including emojis)
            return 4;
        } else {
            return 0; // Not a valid UTF-8 start byte
        }
    }

    /**
     * A circular buffer for storing and retrieving elements with large index values.
     */
    private static class CircularBuffer {
        private byte[] arr;
        private int size;

        /**
         * Initializes the buffer with the specified size.
         *
         * @param arsize the size of the buffer
         */
        CircularBuffer(int arsize) {
            size = arsize;
            arr = new byte[size];
        }

        /**
         * Sets the element at the specified index.
         *
         * @param id   the index where the element should be set
         * @param elem the element to set
         */
        public void set(long id, byte elem) {
            arr[(int) (id % ((long) size))] = elem;
        }

        /**
         * Retrieves the element at the specified index.
         *
         * @param id the index of the element to retrieve
         * @return the element at the specified index
         */
        public byte get(long id) {
            return arr[(int) (id % ((long) size))];
        }
    }
}