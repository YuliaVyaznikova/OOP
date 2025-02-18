package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The {@code ParallelStreamCheckerTest} class contains unit tests for the
 * @link ParallelStreamChecker} class.
 */
public class ParallelStreamCheckerTest {

    /**
     * Tests the {@link ParallelStreamChecker#hasNonPrime(int[])} method with
     * an array containing a non-prime number.
     */
    @Test
    void testHasNonPrime_WithNonPrime() {
        int[] numbers = {2, 3, 5, 6, 7, 11};
        assertTrue(ParallelStreamChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link ParallelStreamChecker#hasNonPrime(int[])} method with
     * an array containing only prime numbers.
     */
    @Test
    void testHasNonPrime_OnlyPrimes() {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        assertFalse(ParallelStreamChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link ParallelStreamChecker#hasNonPrime(int[])} method
     * with an empty array.
     */
    @Test
    void testHasNonPrime_EmptyArray() {
        int[] numbers = {};
        assertFalse(ParallelStreamChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link ParallelStreamChecker#hasNonPrime(int[])} method
     * with an array containing one non-prime number.
     */
    @Test
    void testHasNonPrime_OneNonPrime() {
        int[] numbers = {9};
        assertTrue(ParallelStreamChecker.hasNonPrime(numbers));
    }

    /**
     * Tests the {@link ParallelStreamChecker#hasNonPrime(int[])} method
     * with a large array containing non-prime numbers.
     */
    @Test
    void testHasNonPrime_BigArray() {
        int size = 1000;
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = PrimeChecker.isPrime(i) ? i + 1 : i + 2;
        }
        assertTrue(ParallelStreamChecker.hasNonPrime(numbers));
    }

    /**
     * Test
     */
    @Test
    void testHasNonPrime_NullArray_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ParallelStreamChecker.hasNonPrime(null));
    }
}