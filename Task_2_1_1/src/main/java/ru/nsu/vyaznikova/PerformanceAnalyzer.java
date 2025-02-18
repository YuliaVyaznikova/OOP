package ru.nsu.vyaznikova;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * The {@code PerformanceAnalyzer} class measures and records
 * the performance of different algorithms
 * for finding non-prime numbers in an array.
 * It generates test data, runs the algorithms, and
 * outputs the results to a CSV file.
 */
public class PerformanceAnalyzer {

    private static final int NUM_ITERATIONS = 10;
    private static final int ARRAY_SIZE = 100000;
    private static final String OUTPUT_FILE = "performance_results.csv";

    /**
     * Measures the average execution time of the sequential non-prime checker.
     *
     * @param numbers The array of numbers to check.
     * @return The average execution time in nanoseconds.
     */
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

    /**
     * Measures the average execution time of the threaded non-prime checker.
     *
     * @param numbers    The array of numbers to check.
     * @param numThreads The number of threads to use.
     * @return The average execution time in nanoseconds.
     * @throws InterruptedException If any thread is interrupted during execution.
     */
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

    /**
     * Measures the average execution time of the parallel stream non-prime checker.
     *
     * @param numbers The array of numbers to check.
     * @return The average execution time in nanoseconds.
     */
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

    /**
     * Generates a test array of integers. The array contains numbers close to Integer.MAX_VALUE
     * and includes a few composite numbers to ensure the algorithm doesn't terminate prematurely.
     *
     * @param size The size of the array to generate.
     * @return The generated array of integers.
     */
    public static int[] generateTestArray(int size) {
        int[] numbers = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            numbers[i] = Integer.MAX_VALUE - random.nextInt(10000);
        }

        for (int i = 0; i < 5; i++) {
            numbers[random.nextInt(size)] = 1234567890 + i;
        }

        return numbers;
    }


    /**
     * The main method of the {@code PerformanceAnalyzer} class.  It generates test data,
     * measures the performance of different non-prime checking algorithms, and writes the
     * results to a CSV file.
     *
     * @param args Command line arguments (not used).
     * @throws InterruptedException If any thread is interrupted.
     */
    public static void main(String[] args) throws InterruptedException {
        int[] testArray = generateTestArray(ARRAY_SIZE);

        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            writer.write("Номер измерения,Время выполнения (нс),Алгоритм,Потоки\n");

            int measurementNumber = 1;

            long sequentialTime = measureSequential(testArray);
            writer.write(String.format("%d,%d,Sequential,1\n", measurementNumber++,
                sequentialTime));

            for (int numThreads = 1; numThreads <= 16; numThreads++) {
                long threadedTime = measureThreaded(testArray, numThreads);
                writer.write(String.format("%d,%d,Threaded,%d\n", measurementNumber++,
                    threadedTime, numThreads));
            }

            long parallelStreamTime = measureParallelStream(testArray);
            writer.write(String.format("%d,%d,Parallel Stream,N/A\n", measurementNumber++,
                parallelStreamTime));

            System.out.println("Results written to " + OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}