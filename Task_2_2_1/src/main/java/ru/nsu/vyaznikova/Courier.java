package ru.nsu.vyaznikova;

import java.util.List;

/**
 * Класс, представляющий курьера в пиццерии.
 */
public class Courier implements Runnable {
    private final int id;
    private final int trunkCapacity;
    private final Storage storage;
    private volatile boolean isRunning = true;

    /**
     * Создает нового курьера.
     */
    public Courier(int id, int trunkCapacity, Storage storage) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.storage = storage;
    }

    @Override
    /**
     * Основной метод работы курьера.
     */
    public void run() {
        try {
            while (isRunning) {
                List<PizzaOrder> orders = takePizzasFromStorage();
                if (orders == null) { // Сигнал к завершению
                    break;
                }
                if (!orders.isEmpty()) {
                    deliverPizzas(orders);
                }
            }
        } finally {
            System.out.println("Курьер " + id + " завершил работу.");
        }
    }

    /**
     * Берет заказы из хранилища.
     */
    private List<PizzaOrder> takePizzasFromStorage() {
        if (!isRunning) {
            return null;
        }
        return storage.takePizzas(trunkCapacity);
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
            onPizzaDelivered(order);
        }
    }

    /**
     * Hook for testing purposes. Called when a pizza is delivered.
     */
    protected void onPizzaDelivered(PizzaOrder order) {
        // This method is intended to be overridden in tests.
    }

    public void stop() {
        isRunning = false;
    }
}