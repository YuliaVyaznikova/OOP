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
    public static List<Long> findSubstringIndices(String filename, String substring) throws IOException {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>(); // Return an empty list for empty or null substrings
        }

        // Encode the substring to search for using UTF-8
        byte[] substringBytes = substring.getBytes(StandardCharsets.UTF_8);
        int substringLength = substringBytes.length;
        CircularBuffer byteBuffer = new CircularBuffer(substringLength + 1);
        List<Long> foundIndices = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(filename)) {
            long fileBytePosition = 0;     // Current position in the file (in bytes)
            long characterCount = 0;   // Number of UTF-8 characters processed
            int substringMatchPosition = 0;
            int bytesRead;

            while ((bytesRead = inputStream.read()) != -1) {
                byte currentByte = (byte) bytesRead;

                // Update character count if this byte starts a new UTF-8 character
                int utf8ByteLength = determineUtf8CharacterLength(currentByte);
                if (utf8ByteLength > 0) {
                    characterCount++;
                }

                byteBuffer.setByteAt(fileBytePosition, currentByte);

                // Matching process (considering multi-byte UTF-8 characters)
                if (substringMatchPosition < substringLength &&
                        substringBytes[substringMatchPosition] == byteBuffer.getByteAt(fileBytePosition)) {
                    substringMatchPosition++;
                    if (substringMatchPosition == substringLength) {
                        // Found a match, record the position using character count, not byte position
                        foundIndices.add(characterCount - substring.length());
                    }
                } else {
                    substringMatchPosition = 0;
                }

                fileBytePosition++;
            }
        }

        return foundIndices;
    }

    /**
     * Determines the number of bytes in the current UTF-8 character based on its first byte.
     *
     * @param byteValue the byte to analyze
     * @return the number of bytes in the UTF-8 character, or 0 if this byte is not a valid start byte
     */
    private static int determineUtf8CharacterLength(byte byteValue) {
        if ((byteValue & 0b10000000) == 0) { // Single-byte character (ASCII)
            return 1;
        } else if ((byteValue & 0b11100000) == 0b11000000) { // Start of a 2-byte character
            return 2;
        } else if ((byteValue & 0b11110000) == 0b11100000) { // Start of a 3-byte character
            return 3;
        } else if ((byteValue & 0b11111000) == 0b11110000) { // Start of a 4-byte character (including emojis)
            return 4;
        } else {
            return 0; // Not a valid UTF-8 start byte
        }
    }

    /**
     * A circular buffer for storing and retrieving elements with large index values.
     */
    private static class CircularBuffer {
        private byte[] bufferArray;
        private int bufferSize;

        /**
         * Initializes the buffer with the specified size.
         *
         * @param size the size of the buffer
         */
        CircularBuffer(int size) {
            bufferSize = size;
            bufferArray = new byte[bufferSize];
        }

        /**
         * Sets the element at the specified index.
         *
         * @param index the index where the element should be set
         * @param value the element to set
         */
        public void setByteAt(long index, byte value) {
            bufferArray[(int) (index % ((long) bufferSize))] = value;
        }

        /**
         * Retrieves the element at the specified index.
         *
         * @param index the index of the element to retrieve
         * @return the element at the specified index
         */
        public byte getByteAt(long index) {
            return bufferArray[(int) (index % ((long) bufferSize))];
        }
    }
}