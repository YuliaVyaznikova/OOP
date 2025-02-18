package ru.nsu.vyaznikova;

/**
 * The {@code PrimeChecker} class provides a method for checking if a number is prime.
 */
public class PrimeChecker {

    /**
     * Checks if a given number is prime.
     *
     * @param number The number to check for primality.
     * @return {@code true} if the number is prime, {@code false} otherwise.
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
}