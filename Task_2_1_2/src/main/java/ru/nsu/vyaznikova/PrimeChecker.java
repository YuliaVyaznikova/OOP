package ru.nsu.vyaznikova;

public class PrimeChecker {
    
    public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

        // Проверяем делители до корня из числа
        // Оптимизация: проверяем только числа вида 6k ± 1
        int sqrtN = (int) Math.sqrt(number);
        for (int i = 5; i <= sqrtN; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNonPrime(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return true;
            }
        }
        return false;
    }

    public String checkArrayResult(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return String.format("Found non-prime number: %d", number);
            }
        }
        return "All numbers are prime";
    }
}
