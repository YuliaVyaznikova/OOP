package ru.nsu.vyaznikova;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PizzeriaSimulator {
    private final Queue<PizzaOrder> orderQueue = new ConcurrentLinkedQueue<>();
    private final Storage storage;
    private final Baker[] bakers;
    private final Courier[] couriers;

    public PizzeriaSimulator(int N, int M, int T, int[] bakerSpeeds, int[] courierCapacities) {
        this.storage = new Storage(T);
        this.bakers = new Baker[N];
        for (int i = 0; i < N; i++) {
            bakers[i] = new Baker(i + 1, bakerSpeeds[i], orderQueue, storage);
        }
        this.couriers = new Courier[M];
        for (int i = 0; i < M; i++) {
            couriers[i] = new Courier(i + 1, courierCapacities[i], storage);
        }
    }

    public void startSimulation() {
        for (Baker baker : bakers) {
            new Thread(baker).start();
        }
        for (Courier courier : couriers) {
            new Thread(courier).start();
        }
    }

    public void placeOrder(PizzaOrder order) {
        orderQueue.add(order);
    }

    public void stopSimulation() {
        // Остановка пекарей и курьеров
        for (Baker baker : bakers) {
            baker.stop();
        }
        for (Courier courier : couriers) {
            courier.stop();
        }
    }

    public static void main(String[] args) {
        int N = 2; // Количество пекарей
        int M = 2; // Количество курьеров
        int T = 10; // Вместимость склада
        int[] bakerSpeeds = {2000, 3000}; // Скорость приготовления пекарей
        int[] courierCapacities = {2, 3}; // Вместимость багажников курьеров

        PizzeriaSimulator simulator = new PizzeriaSimulator(N, M, T, bakerSpeeds, courierCapacities);
        simulator.startSimulation();

        // Пример поступления заказов
        for (int i = 1; i <= 10; i++) {
            simulator.placeOrder(new PizzaOrder(i));
            try {
                Thread.sleep(500); // Задержка между заказами
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Остановка симуляции через некоторое время
        try {
            Thread.sleep(10000); // Время работы пиццерии
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        simulator.stopSimulation();
    }
}