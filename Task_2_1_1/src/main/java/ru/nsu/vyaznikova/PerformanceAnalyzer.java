package ru.nsu.vyaznikova;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PerformanceAnalyzer {

    private static final int NUM_ITERATIONS = 10; // Number of iterations for averaging
    private static final int ARRAY_SIZE = 100000;  // Size of the test array
    private static final String OUTPUT_FILE = "performance_results.csv"; // Output CSV file

    public static long measureSequential(int[] numbers) {
        long totalTime = 0;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            SequentialChecker.hasNonPrime(numbers);
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        return totalTime / NUM_ITERATIONS;
    }

    public static long measureThreaded(int[] numbers, int numThreads) throws InterruptedException {
        long totalTime = 0;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            ThreadedChecker.hasNonPrime(numbers, numThreads);
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        return totalTime / NUM_ITERATIONS;
    }

    public static long measureParallelStream(int[] numbers) {
        long totalTime = 0;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            ParallelStreamChecker.hasNonPrime(numbers);
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        return totalTime / NUM_ITERATIONS;
    }

    public static int[] generateTestArray(int size) {
        int[] numbers = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            numbers[i] = Integer.MAX_VALUE - random.nextInt(10000); // Generate numbers close to MAX_VALUE
        }

        // Add a few composite numbers to ensure the algorithm doesn't terminate too early
        for (int i = 0; i < 5; i++) {
            numbers[random.nextInt(size)] = 1234567890 + i; // Some arbitrary composite numbers
        }

        return numbers;
    }


    public static void main(String[] args) throws InterruptedException {
        int[] testArray = generateTestArray(ARRAY_SIZE);

        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            // Write the header
            writer.write("Номер измерения,Время выполнения (нс),Алгоритм,Потоки\n");

            int measurementNumber = 1; // Counter for measurement number

            // Measure and write Sequential
            long sequentialTime = measureSequential(testArray);
            writer.write(String.format("%d,%d,Sequential,1\n", measurementNumber++, sequentialTime, sequentialTime));

            // Measure and write Threaded with thread counts from 1 to 16
            for (int numThreads = 1; numThreads <= 16; numThreads++) {
                long threadedTime = measureThreaded(testArray, numThreads);
                writer.write(String.format("%d,%d,Threaded,%d\n", measurementNumber++, threadedTime, numThreads));
            }

            // Measure and write Parallel Stream
            long parallelStreamTime = measureParallelStream(testArray);
            writer.write(String.format("%d,%d,Parallel Stream,N/A\n", measurementNumber++, parallelStreamTime, parallelStreamTime));

            System.out.println("Results written to " + OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}