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
            heap.insertnew(value);
        }

        int index = 0;
        while (!heap.isempty()) {
            array[index++] = heap.extractmin();
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

    public Heap(int amountofnums) {
        heap = new int[amountofnums];
        size = 0;
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void siftup(int index) {
        int parentindex = (index - 1) / 2;
        while (index > 0 && heap[index] < heap[parentindex]) {
            swap(index, parentindex);
            index = parentindex;
            parentindex = (index - 1) / 2;
        }
    }

    public void insertnew(int value) {
        heap[size] = value;
        siftup(size);
        size++;
    }

    private void siftdown(int index) {
        int minindex = index;
        int leftindex = 2 * index + 1;
        int rightindex = 2 * index + 2;

        if (leftindex < size && heap[leftindex] < heap[minindex]) {
            minindex = leftindex;
        }

        if (rightindex < size && heap[rightindex] < heap[minindex]) {
            minindex = rightindex;
        }

        if (minindex != index) {
            swap(index, minindex);
            siftdown(minindex);
        }
    }
    
    public int extractmin() {
        int min = heap[0];
        heap[0] = heap[--size];
        siftdown(0);
        return min;
    }

    public boolean isempty() {
        return size == 0;
    }
}