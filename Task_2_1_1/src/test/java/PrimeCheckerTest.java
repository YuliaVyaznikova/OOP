package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The {@code PrimeCheckerTest} class contains unit tests for the
 * {@link PrimeChecker} class.
 */
public class PrimeCheckerTest {

    /**
     * Tests the {@link PrimeChecker#isPrime(int)} method
     * with various prime numbers.
     */
    @Test
    void testIsPrime_PrimeNumber() {
        assertTrue(PrimeChecker.isPrime(2));
        assertTrue(PrimeChecker.isPrime(3));
        assertTrue(PrimeChecker.isPrime(5));
        assertTrue(PrimeChecker.isPrime(7));
        assertTrue(PrimeChecker.isPrime(11));
        assertTrue(PrimeChecker.isPrime(101));
    }

    /**
     * Tests the {@link PrimeChecker#isPrime(int)} method
     * with various non-prime numbers.
     */
    @Test
    void testIsPrime_NonPrimeNumber() {
        assertFalse(PrimeChecker.isPrime(1));
        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(6));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(9));
        assertFalse(PrimeChecker.isPrime(10));
        assertFalse(PrimeChecker.isPrime(12));
    }

    /**
     * Tests the {@link PrimeChecker#isPrime(int)} method
     * with edge cases such as 0 and negative numbers.
     */
    @Test
    void testIsPrime_EdgeCases() {
        assertFalse(PrimeChecker.isPrime(0));
        assertFalse(PrimeChecker.isPrime(-5));
    }
}