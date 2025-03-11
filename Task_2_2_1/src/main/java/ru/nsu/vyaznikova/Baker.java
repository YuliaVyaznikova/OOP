package ru.nsu.vyaznikova;

import java.util.Queue;

public class Baker implements Runnable {
    private final int id;
    private final int cookingSpeed;
    private final Queue<PizzaOrder> orderQueue;
    private final Storage storage;
    private final Object queueLock;
    private volatile boolean isRunning = true;

    public Baker(int id, int cookingSpeed, Queue<PizzaOrder> orderQueue, Storage storage, Object queueLock) {
        this.id = id;
        this.cookingSpeed = cookingSpeed;
        this.orderQueue = orderQueue;
        this.storage = storage;
        this.queueLock = queueLock;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                PizzaOrder order = takeOrderFromQueue();
                if (order == null) {
                    break;
                }
                processOrder(order);
            }
        } finally {
            System.out.println("Пекарь " + id + " завершил работу.");
        }
    }

    private PizzaOrder takeOrderFromQueue() {
        synchronized (queueLock) {
            while (orderQueue.isEmpty() && isRunning) {
                try {
                    queueLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Пекарь " + id + " прерван во время ожидания заказа.");
                    return null;
                }
            }
            return orderQueue.isEmpty() ? null : orderQueue.poll();
        }
    }

    private void processOrder(PizzaOrder order) {
        System.out.println("[" + order.getOrderId() + "] [Принят в работу пекарем " + id + "]");
        try {
            Thread.sleep(cookingSpeed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Пекарь " + id + " прерван во время приготовления заказа " + order.getOrderId());
            return;
        }
        System.out.println("[" + order.getOrderId() + "] [Готово]");
        storage.storePizza(order);
    }

    public void stop() {
        isRunning = false;
        synchronized (queueLock) {
            queueLock.notifyAll();
        }
    }
}