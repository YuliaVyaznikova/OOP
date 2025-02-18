package ru.nsu.vyaznikova;

import java.util.Arrays;

/**
 * The {@code ParallelStreamChecker} class provides a method to check if an array contains a non-prime number
 * using a parallel stream.
 */
public class ParallelStreamChecker {

    /**
     * Checks if the given array contains at least one
     * non-prime number using a parallel stream.
     *
     * @param numbers The array of integers to check.
     * @return {@code true} if the array contains at least one non-prime number,
     * {@code false} otherwise.
     * @throws IllegalArgumentException if the input array is {@code null}.
     */
    public static boolean hasNonPrime(int[] numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        return Arrays.stream(numbers).parallel().anyMatch(number -> !PrimeChecker.isPrime(number));
    }
}