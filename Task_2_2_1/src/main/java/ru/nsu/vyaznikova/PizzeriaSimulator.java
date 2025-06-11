package ru.nsu.vyaznikova;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PizzeriaSimulator {
    private final Queue<PizzaOrder> orderQueue = new LinkedList<>();
    private final Object queueLock = new Object();
    private final Storage storage;
    private final Baker[] bakers;
    private final Courier[] couriers;
    private final ExecutorService bakerExecutor;
    private final ExecutorService courierExecutor;



    public PizzeriaSimulator(int N, int M, int T, int[] bakerSpeeds, int[] courierCapacities) {
        this.storage = new Storage(T);
        this.bakers = createBakers(N, bakerSpeeds);
        this.couriers = createCouriers(M, courierCapacities);
        this.bakerExecutor = Executors.newFixedThreadPool(N);
        this.courierExecutor = Executors.newFixedThreadPool(M);
    }

    private Baker[] createBakers(int N, int[] bakerSpeeds) {
        Baker[] bakers = new Baker[N];
        for (int i = 0; i < N; i++) {
            bakers[i] = new Baker(i + 1, bakerSpeeds[i], orderQueue, storage, queueLock);
        }
        return bakers;
    }

    private Courier[] createCouriers(int M, int[] courierCapacities) {
        Courier[] couriers = new Courier[M];
        for (int i = 0; i < M; i++) {
            couriers[i] = new Courier(i + 1, courierCapacities[i], storage);
        }
        return couriers;
    }

    public void startSimulation() {
        for (Baker baker : bakers) {
            bakerExecutor.execute(baker);
        }
        for (Courier courier : couriers) {
            courierExecutor.execute(courier);
        }
    }

    public void placeOrder(PizzaOrder order) {
        synchronized (queueLock) {
            orderQueue.add(order);
            queueLock.notifyAll();
        }
    }

    public void stopSimulation() {
        System.out.println("Stopping simulation...");

        // 1. Сигнализируем всем работникам о прекращении работы
        for (Baker baker : bakers) {
            baker.stop();
        }
        for (Courier courier : couriers) {
            courier.stop();
        }

        // 2. Прерываем ожидание всех потоков, чтобы они могли проверить флаг isRunning
        // Это нужно делать после того, как все флаги isRunning установлены в false
        synchronized (queueLock) {
            queueLock.notifyAll();
        }
        storage.stop(); // Этот метод вызывает notifyAll на storage.lock

        // 3. Завершаем работу пулов потоков
        bakerExecutor.shutdown();
        courierExecutor.shutdown();

        try {
            // Ожидаем завершения всех задач в пулах
            if (!bakerExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Baker threads did not terminate in time");
                bakerExecutor.shutdownNow();
            }
            if (!courierExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Courier threads did not terminate in time");
                courierExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            bakerExecutor.shutdownNow();
            courierExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Simulation stopped.");
    }

    public static void main(String[] args) {
        int N = 2;
        int M = 2;
        int T = 10;
        int[] bakerSpeeds = {2000, 3000};
        int[] courierCapacities = {2, 3};

        PizzeriaSimulator simulator = new PizzeriaSimulator(N, M, T, bakerSpeeds, courierCapacities);
        simulator.startSimulation();

        for (int i = 1; i <= 10; i++) {
            simulator.placeOrder(new PizzaOrder(i));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while placing orders.");
                return;
            }
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting to stop simulation.");
            Thread.currentThread().interrupt();
        }
        simulator.stopSimulation();
    }
}