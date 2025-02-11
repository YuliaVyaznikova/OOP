package ru.nsu.vyaznikova;

import java.util.Arrays;

public class SequentialChecker {

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