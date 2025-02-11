package ru.nsu.vyaznikova;

import java.util.Arrays;

public class ParallelStreamChecker {

    public static boolean hasNonPrime(int[] numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        return Arrays.stream(numbers).parallel().anyMatch(number -> !PrimeChecker.isPrime(number));
    }
}