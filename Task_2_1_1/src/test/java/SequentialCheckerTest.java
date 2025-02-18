package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The {@code SequentialCheckerTest} class contains unit tests for the
 * {@link SequentialChecker} class.
 */
public class SequentialCheckerTest {

    /**
     * Tests the {@link SequentialChecker#hasNonPrime(int[])} method
     * with an array containing a non-prime number.
     */
    @Test
    void testHasNonPrime_WithNonPrime() {
        int[] numbers = {2, 3, 5, 6, 7, 11};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link SequentialChecker#hasNonPrime(int[])} method
     * with an array containing only prime numbers.
     */
    @Test
    void testHasNonPrime_OnlyPrimes() {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        assertFalse(SequentialChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link SequentialChecker#hasNonPrime(int[])} method
     * with an empty array.
     */
    @Test
    void testHasNonPrime_EmptyArray() {
        int[] numbers = {};
        assertFalse(SequentialChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link SequentialChecker#hasNonPrime(int[])} method with
     * an array containing a single non-prime number.
     */
    @Test
    void testHasNonPrime_OneNonPrime() {
        int[] numbers = {9};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }

    /**
     * Tests that the {@link SequentialChecker#hasNonPrime(int[])} method
     * throws an {@link IllegalArgumentException}
     * when the input array is null.
     */
    @Test
    void testHasNonPrime_NullArray_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            SequentialChecker.hasNonPrime(null));
    }

    /**
     * Tests the {@link SequentialChecker#hasNonPrime(int[])} method
     * with an array containing zero.
     */
    @Test
    void testHasNonPrime_ArrayWithZero() {
        int[] numbers = {2, 3, 0, 7};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link SequentialChecker#hasNonPrime(int[])} method
     * with an array containing one.
     */
    @Test
    void testHasNonPrime_ArrayWithOne() {
        int[] numbers = {2, 3, 1, 7};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }
}