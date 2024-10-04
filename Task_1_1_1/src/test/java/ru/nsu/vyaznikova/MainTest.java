package ru.nsu.vyaznikova;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static ru.nsu.vyaznikova.Main.heapsort;

class MainTest {
    @Test
    public void base() {
        int size_of_array = 100;
        double c = 1.1;
        for (int i = 0; i < 100; i++) {
            double x = 0;
            size_of_array = (int) (size_of_array * c);
            for (int j = 0; j < 10; j++) {
                int[] unsorted = new int[size_of_array];
                int[] sorted = new int[size_of_array];
                for (int k = 0; k < size_of_array; k++) {
                    unsorted[k] = new Random().nextInt(i * 1000000 + 1);
                    sorted[k] = unsorted[k];
                }
                Arrays.sort(sorted);

                long start = System.nanoTime();

                heapsort(unsorted);

                long finish = System.nanoTime();
                long elapsed = finish - start;
                x += elapsed;

                assertArrayEquals(unsorted, sorted);
            }
            System.out.printf("%d %f\n", size_of_array, x / 10);
        }
    }
}