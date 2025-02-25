package ru.nsu.vyaznikova;

import java.util.Queue;

public class Baker implements Runnable {
    private final int id;
    private final int cookingSpeed; // Время приготовления одной пиццы в миллисекундах
    private final Queue<PizzaOrder> orderQueue;
    private final Storage storage;

    public Baker(int id, int cookingSpeed, Queue<PizzaOrder> orderQueue, Storage storage) {
        this.id = id;
        this.cookingSpeed = cookingSpeed;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (true) {
            PizzaOrder order = null;
            synchronized (orderQueue) {
                if (!orderQueue.isEmpty()) {
                    order = orderQueue.poll();
                }
            }
            if (order != null) {
                System.out.println("[" + order.getOrderId() + "] [Принят в работу пекарем " + id + "]");
                try {
                    Thread.sleep(cookingSpeed); // Симуляция времени приготовления
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println("[" + order.getOrderId() + "] [Готово]");
                storage.storePizza(order);
            } else {
                // Если заказов нет, пекарь завершает работу
                break;
            }
        }
    }
}