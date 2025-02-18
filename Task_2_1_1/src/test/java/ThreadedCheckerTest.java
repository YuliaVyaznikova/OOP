package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The {@code ThreadedCheckerTest} class contains unit tests for the {@link ThreadedChecker} class.
 */
public class ThreadedCheckerTest {

    /**
     * Tests the {@link ThreadedChecker#hasNonPrime(int[], int)} method with an array containing a non-prime number
     * and different thread counts.
     */
    @Test
    void testHasNonPrime_WithNonPrime() throws InterruptedException {
        int[] numbers = {2, 3, 5, 6, 7, 11};
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 2));
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 3));
    }

    /**
     * Tests the {@link ThreadedChecker#hasNonPrime(int[], int)} method with an array containing only prime numbers.
     */
    @Test
    void testHasNonPrime_OnlyPrimes() throws InterruptedException {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        assertFalse(ThreadedChecker.hasNonPrime(numbers, 2));
    }

    /**
     * Tests the {@link ThreadedChecker#hasNonPrime(int[], int)} method with an empty array.
     */
    @Test
    void testHasNonPrime_EmptyArray() throws InterruptedException {
        int[] numbers = {};
        assertFalse(ThreadedChecker.hasNonPrime(numbers, 2));
    }

    /**
     * Tests the {@link ThreadedChecker#hasNonPrime(int[], int)} method with an array containing a single non-prime
     * number.
     */
    @Test
    void testHasNonPrime_OneNonPrime() throws InterruptedException {
        int[] numbers = {9};
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 2));
    }

    /**
     * Tests the {@link ThreadedChecker#hasNonPrime(int[], int)} method with a large array containing non-prime numbers
     * and different thread counts.
     */
    @Test
    void testHasNonPrime_BigArrayWithNonPrime() throws InterruptedException {
        int size = 1000;
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = PrimeChecker.isPrime(i) ? i + 1 : i + 2;
        }
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 4));
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 1));
        assertTrue(ThreadedChecker.hasNonPrime(numbers, size * 2));
    }

    /**
     * Tests the {@link ThreadedChecker#hasNonPrime(int[], int)} method with a large array containing only prime
     * numbers.
     */
    @Test
    void testHasNonPrime_BigArrayOnlyPrimes() throws InterruptedException {
        int size = 1000;
        int[] numbers = new int[size];

        List<Integer> primes = new ArrayList<>();
        int num = 2;
        while (primes.size() < size) {
            if (PrimeChecker.isPrime(num)) {
                primes.add(num);
            }
            num++;
        }

        for (int i = 0; i < size; i++) {
            numbers[i] = primes.get(i);
        }

        assertFalse(ThreadedChecker.hasNonPrime(numbers, 4));
    }

    /**
     * Tests that the {@link ThreadedChecker#hasNonPrime(int[], int)} method throws an
     * {@link IllegalArgumentException} when the input array is null.
     */
    @Test
    void testHasNonPrime_NullArray_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ThreadedChecker.hasNonPrime(null, 2));
    }

    /**
     * Tests that the {@link ThreadedChecker#hasNonPrime(int[], int)} method throws an
     * {@link IllegalArgumentException} when the thread count is invalid (0 or negative).
     */
    @Test
    void testHasNonPrime_InvalidThreadCount_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ThreadedChecker.hasNonPrime(new int[]{1, 2, 3}, 0));
        assertThrows(IllegalArgumentException.class, () ->
            ThreadedChecker.hasNonPrime(new int[]{1, 2, 3}, -1));
    }
}