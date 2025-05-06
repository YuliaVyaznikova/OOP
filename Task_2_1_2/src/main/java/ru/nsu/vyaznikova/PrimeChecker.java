package ru.nsu.vyaznikova;

public class PrimeChecker {

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

    public static boolean hasNonPrime(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return true;
            }
        }
        return false;
    }

    public static String checkArrayResult(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return String.format("Found non-prime number: %d", number);
            }
        }
        return "All numbers are prime";
    }
}
