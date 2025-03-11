package ru.nsu.vyaznikova;

import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int trunkCapacity;
    private final Storage storage;
    private volatile boolean isRunning = true;

    public Courier(int id, int trunkCapacity, Storage storage) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                List<PizzaOrder> orders = takePizzasFromStorage();
                if (orders.isEmpty()) {
                    break;
                }
                deliverPizzas(orders);
            }
        } finally {
            System.out.println("Курьер " + id + " завершил работу.");
        }
    }

    private List<PizzaOrder> takePizzasFromStorage() {
        List<PizzaOrder> orders = storage.takePizzas(trunkCapacity);
        if (orders.isEmpty() && isRunning) {
            synchronized (storage.getStorageLock()) {
                while (orders.isEmpty() && isRunning) {
                    try {
                        storage.getStorageLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Курьер " + id + " прерван во время ожидания пиццы.");
                        return List.of();
                    }
                    orders = storage.takePizzas(trunkCapacity);
                }
            }
        }
        return orders;
    }

    private void deliverPizzas(List<PizzaOrder> orders) {
        for (PizzaOrder order : orders) {
            System.out.println("[" + order.getOrderId() + "] [Доставка курьером " + id + "]");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Курьер " + id + " прерван во время доставки.");
            return;
        }

        for (PizzaOrder order : orders) {
            System.out.println("[" + order.getOrderId() + "] [Доставлено]");
        }
    }

    public void stop() {
        isRunning = false;
        synchronized (storage.getStorageLock()) {
            storage.getStorageLock().notifyAll();
        }
    }
}