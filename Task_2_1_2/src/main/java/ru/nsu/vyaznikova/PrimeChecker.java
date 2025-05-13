package ru.nsu.vyaznikova;

/**
 * Utility class for checking prime numbers.
 * Provides methods to check if a single number is prime
 * and to analyze arrays of numbers for non-prime values.
 */
public class PrimeChecker {

    /**
     * Checks if a given number is prime.
     * A prime number is a natural number greater than 1 that is only divisible by 1 and itself.
     *
     * @param number the number to check for primality
     * @return true if the number is prime, false otherwise
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if an array contains any non-prime numbers.
     *
     * @param numbers array of integers to check
     * @return true if any number in the array is not prime, false if all numbers are prime
     */
    public static boolean hasNonPrime(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks an array for non-prime numbers and returns a descriptive message.
     *
     * @param numbers array of integers to check
     * @return a message indicating whether all numbers are prime or which number is non-prime
     */
    public static String checkArrayResult(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return String.format("Found non-prime number: %d", number);
            }
        }
        return "All numbers are prime";
    }
}