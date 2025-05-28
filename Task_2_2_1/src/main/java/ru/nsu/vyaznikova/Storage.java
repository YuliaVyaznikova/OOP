package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Storage {
    private final int capacity;
    private final Queue<PizzaOrder> storageQueue = new LinkedList<>();
    private final Object lock = new Object();
    private volatile boolean isRunning = true;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public void storePizza(PizzaOrder order) {
        synchronized (lock) {
            while (storageQueue.size() >= capacity && isRunning) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Storage interrupted while waiting to store pizza.");
                    return;
                }
            }

            if (!isRunning) {
                return;
            }

            storageQueue.add(order);
            lock.notifyAll();
        }
    }

    public List<PizzaOrder> takePizzas(int count) {
        synchronized (lock) {
            while (storageQueue.isEmpty() && isRunning) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Storage interrupted while waiting to take pizzas.");
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
            lock.notifyAll();
            return orders;
        }
    }

    public void stop() {
        synchronized (lock) {
            isRunning = false;
            lock.notifyAll();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Object getStorageLock() {
        return lock;
    }
}