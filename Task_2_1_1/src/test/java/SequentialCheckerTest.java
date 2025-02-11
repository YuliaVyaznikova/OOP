package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SequentialCheckerTest {

    @Test
    void testHasNonPrime_WithNonPrime() {
        int[] numbers = {2, 3, 5, 6, 7, 11};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_OnlyPrimes() {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        assertFalse(SequentialChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_EmptyArray() {
        int[] numbers = {};
        assertFalse(SequentialChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_OneNonPrime() {
        int[] numbers = {9};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_NullArray_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SequentialChecker.hasNonPrime(null));
    }

    @Test
    void testHasNonPrime_ArrayWithZero() {
        int[] numbers = {2, 3, 0, 7};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }

    @Test
    void testHasNonPrime_ArrayWithOne() {
        int[] numbers = {2, 3, 1, 7};
        assertTrue(SequentialChecker.hasNonPrime(numbers));
    }
}