package ru.nsu.vyaznikova;

import java.util.Arrays;

/**
 * The {@code SequentialChecker} class provides a method to check if
 * an array contains a non-prime number,
 * processing the array sequentially.
 */
public class SequentialChecker {

    /**
     * Checks if the given array contains at least one non-prime number.
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
        for (int number : numbers) {
            if (!PrimeChecker.isPrime(number)) {
                return true;
            }
        }
        return false;
    }
}