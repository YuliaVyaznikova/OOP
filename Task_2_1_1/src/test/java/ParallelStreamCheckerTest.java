package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The ParallelStreamCheckerTest class contains unit tests for the ParallelStreamChecker class.
 */
public class ParallelStreamCheckerTest {
    @Test
    void testHasNonPrime_WithNonPrime() {
        int[] numbers = {2, 3, 5, 6, 7, 11};
        assertTrue(ParallelStreamChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_OnlyPrimes() {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        assertFalse(ParallelStreamChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_EmptyArray() {
        int[] numbers = {};
        assertFalse(ParallelStreamChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_OneNonPrime() {
        int[] numbers = {9};
        assertTrue(ParallelStreamChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_BigArray() {
        int size = 1000;
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = PrimeChecker.isPrime(i) ? i + 1 : i + 2;
        }
        assertTrue(ParallelStreamChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_NullArray_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ParallelStreamChecker.hasNonPrime(null));
    }
}