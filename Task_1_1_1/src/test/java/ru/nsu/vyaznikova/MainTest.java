package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.vyaznikova.Main.heapsort;

class MainTest {

    @Test
    public void sample() {
        int[] unsorted = {5, 4, 3, 2, 1};
        int[] sorted = {1, 2, 3, 4, 5};
        heapsort(unsorted);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    void base() {
        int[] unsorted = {834, 65, 181, 0, -25, 57, 11};
        int[] sorted = {-25, 0, 11, 57, 65, 181, 834};
        heapsort(unsorted);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void empty() {
        int[] unsorted = {};
        int[] sorted = {};
        heapsort(unsorted);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void single() {
        int[] unsorted = {1};
        int[] sorted = {1};
        heapsort(unsorted);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void duplicate() {
        int[] unsorted = {3, 1, 2, 3, 2};
        int[] sorted = {1, 2, 2, 3, 3};
        heapsort(unsorted);
        assertArrayEquals(unsorted, sorted);
    }
}