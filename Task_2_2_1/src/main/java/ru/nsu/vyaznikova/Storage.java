package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Класс, представляющий хранилище готовых пицц.
 */
public class Storage {
    private final int capacity;
    private final Queue<PizzaOrder> storageQueue = new LinkedList<>();
    private final Object lock = new Object();
    private volatile boolean isRunning = true;

    /**
     * Создает новое хранилище.
     */
    public Storage(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Добавляет заказ в хранилище.
     */
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

    /**
     * Берет заказы из хранилища.
     */
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

    /**
     * Останавливает работу хранилища.
     */
    public void stop() {
        synchronized (lock) {
            isRunning = false;
            lock.notifyAll();
        }
    }

    /**
     * Проверяет, работает ли хранилище.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Возвращает объект блокировки хранилища.
     */
    public Object getStorageLock() {
        return lock;
    }
}