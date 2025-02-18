package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@code ThreadedChecker} class provides a method to check if
 * an array contains a non-prime number using multiple threads.
 */
public class ThreadedChecker {

    /**
     * Checks if the given array contains at least one non-prime number using
     * a specified number of threads.
     *
     * @param numbers    The array of integers to check.
     * @param numThreads The number of threads to use for the check.
     * @return {@code true} if the array contains at least one non-prime number,
     * {@code false} otherwise.
     * @throws IllegalArgumentException if the input array is {@code null} or
     * the number of threads is not positive.
     * @throws InterruptedException   if any thread is interrupted during execution.
     */
    public static boolean hasNonPrime(int[] numbers, int numThreads) throws InterruptedException {
        if (numbers == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        if (numThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be greater than 0");
        }
        if (numbers.length == 0) return false;

        AtomicBoolean result = new AtomicBoolean(false);
        List<Thread> threads = new ArrayList<>();
        int chunkSize = numbers.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? numbers.length : start + chunkSize;

            Thread thread = new Thread(() -> {
                try {
                    for (int j = start; j < end; j++) {
                        if (result.get()) {
                            return;
                        }
                        if (!PrimeChecker.isPrime(numbers[j])) {
                            result.set(true);
                            return;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Thread interrupted: " + e.getMessage());
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        return result.get();
    }
}