package ru.nsu.vyaznikova;

import java.util.Arrays;

/**
 * The {@code ParallelStreamChecker} class provides a method
 * to check if an array contains a non-prime number
 * using a parallel stream.
 */
public class ParallelStreamChecker {

    /**
     * Checks if the given array contains at least one
     * non-prime number using a parallel stream.
     */
    public static boolean hasNonPrime(int[] numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        return Arrays.stream(numbers).parallel().anyMatch(number -> !PrimeChecker.isPrime(number));
    }
}