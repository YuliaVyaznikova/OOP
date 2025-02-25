package ru.nsu.vyaznikova;

import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int trunkCapacity; // Вместимость багажника
    private final Storage storage;

    public Courier(int id, int trunkCapacity, Storage storage) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (true) {
            List<PizzaOrder> orders = storage.takePizzas(trunkCapacity);
            if (orders.isEmpty()) {
                // Если пицц нет, курьер завершает работу
                break;
            }
            System.out.println("[" + orders.get(0).getOrderId() + "] [Доставка курьером " + id + "]");
            try {
                Thread.sleep(1000); // Симуляция времени доставки
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[" + orders.get(0).getOrderId() + "] [Доставлено]");
        }
    }
}