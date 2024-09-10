package ru.nsu.vyaznikova;

import java.util.Arrays;

//public class Main {
//
//    Heap newHeap = new Heap(5);
//
//}

public class Main {
    public static void heapsort(int[] array) {
        Heap heap = new Heap(array.length);
        for (int value : array) {
            heap.insert_new(value);
        }

        int index = 0;
        while (!heap.is_empty()) {
            array[index++] = heap.extract_min();
        }
        return;
    }

    public static void main(String[] args) {
        int[] array = {5, 4, 3, 2, 1};
        heapsort(array);
        System.out.println(Arrays.toString(array));
    }
}

class Heap {
    private int[] heap;
    private int size;

    public Heap(int amount_of_nums) {
        heap = new int[amount_of_nums];
        size = 0;
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void sift_up(int index) {
        int parent_index = (index - 1) / 2;
        while (index > 0 && heap[index] < heap[parent_index]) {
            swap(index, parent_index);
            index = parent_index;
            parent_index = (index - 1) / 2;
        }
    }

    public void insert_new(int value) {
        heap[size] = value;
        sift_up(size);
        size++;
    }

    private void sift_down(int index) {
        int min_index = index;
        int left_index = 2 * index + 1;
        int right_index = 2 * index + 2;

        if (left_index < size && heap[left_index] < heap[min_index]) {
            min_index = left_index;
        }

        if (right_index < size && heap[right_index] < heap[min_index]) {
            min_index = right_index;
        }

        if (min_index != index) {
            swap(index, min_index);
            sift_down(min_index);
        }
    }
    
    public int extract_min() {
        int min = heap[0];
        heap[0] = heap[--size];
        sift_down(0);
        return min;
    }

    public boolean is_empty() {
        return size == 0;
    }
}