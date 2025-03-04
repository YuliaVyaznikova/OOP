package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Storage {
    private final int capacity;
    private final Queue<PizzaOrder> storageQueue = new LinkedList<>();
    private volatile boolean isRunning = true;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void storePizza(PizzaOrder order) {
        while (storageQueue.size() >= capacity && isRunning) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        if (!isRunning) {
            return;
        }

        storageQueue.add(order);
        notifyAll();
    }

    public synchronized List<PizzaOrder> takePizzas(int count) {
        while (storageQueue.isEmpty() && isRunning) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return List.of();
            }
        }

        if (!isRunning) {
            return List.of();
        }

        List<PizzaOrder> orders = new ArrayList<>();
        for (int i = 0; i < count && !storageQueue.isEmpty(); i++) {
            orders.add(storageQueue.poll());
        }
        notifyAll();
        return orders;
    }

    public synchronized Object getStorageLock() {
        return this;
    }

    public synchronized void stop() {
        isRunning = false;
        notifyAll();
    }

    public boolean isRunning() {
        return isRunning;
    }
}