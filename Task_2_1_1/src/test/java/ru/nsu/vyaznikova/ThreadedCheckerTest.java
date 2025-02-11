package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

public class ThreadedCheckerTest {

    @Test
    void testHasNonPrime_WithNonPrime() throws InterruptedException {
        int[] numbers = {2, 3, 5, 6, 7, 11};
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 2));
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 3));
    }

    @Test
    void testHasNonPrime_OnlyPrimes() throws InterruptedException {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        assertFalse(ThreadedChecker.hasNonPrime(numbers, 2));
    }

    @Test
    void testHasNonPrime_EmptyArray() throws InterruptedException {
        int[] numbers = {};
        assertFalse(ThreadedChecker.hasNonPrime(numbers, 2));
    }

    @Test
    void testHasNonPrime_OneNonPrime() throws InterruptedException {
        int[] numbers = {9};
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 2));
    }

    @Test
    void testHasNonPrime_BigArrayWithNonPrime() throws InterruptedException {
        int size = 1000;
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = PrimeChecker.isPrime(i) ? i + 1 : i + 2;
        }
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 4));
        assertTrue(ThreadedChecker.hasNonPrime(numbers, 1));
        assertTrue(ThreadedChecker.hasNonPrime(numbers, size * 2)); // Потоков больше, чем элементов
    }

    @Test
    void testHasNonPrime_BigArrayOnlyPrimes() throws InterruptedException {
        int size = 1000;
        int[] numbers = new int[size];

        // Generate a list of prime numbers up to a certain limit
        List<Integer> primes = new ArrayList<>();
        int num = 2;
        while (primes.size() < size) {
            if (PrimeChecker.isPrime(num)) {
                primes.add(num);
            }
            num++;
        }

        // Fill the array with prime numbers
        for (int i = 0; i < size; i++) {
            numbers[i] = primes.get(i);
        }

        assertFalse(ThreadedChecker.hasNonPrime(numbers, 4));
    }

    @Test
    void testHasNonPrime_NullArray_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ThreadedChecker.hasNonPrime(null, 2));
    }

    @Test
    void testHasNonPrime_InvalidThreadCount_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ThreadedChecker.hasNonPrime(new int[]{1, 2, 3}, 0));
        assertThrows(IllegalArgumentException.class, () -> ThreadedChecker.hasNonPrime(new int[]{1, 2, 3}, -1));
    }
}