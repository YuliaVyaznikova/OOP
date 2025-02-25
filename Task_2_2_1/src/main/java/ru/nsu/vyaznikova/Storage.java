package ru.nsu.vyaznikova;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Storage {
    private final int capacity;
    private final Queue<PizzaOrder> storageQueue = new LinkedList<>();

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void storePizza(PizzaOrder order) {
        while (storageQueue.size() >= capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        storageQueue.add(order);
        notifyAll();
    }

    public synchronized List<PizzaOrder> takePizzas(int count) {
        while (storageQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return List.of();
            }
        }
        List<PizzaOrder> orders = new LinkedList<>();
        for (int i = 0; i < count && !storageQueue.isEmpty(); i++) {
            orders.add(storageQueue.poll());
        }
        notifyAll();
        return orders;
    }
}