package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadedChecker {

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
            // Последнему потоку достаются все оставшиеся элементы
            int end = (i == numThreads - 1) ? numbers.length : start + chunkSize;

            Thread thread = new Thread(() -> {
                try {
                    for (int j = start; j < end; j++) {
                        // Если уже нашли составное число, сразу выходим
                        if (result.get()) {
                            return;
                        }
                        if (!PrimeChecker.isPrime(numbers[j])) {
                            result.set(true);
                            return; // Завершаем поток сразу после обнаружения
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