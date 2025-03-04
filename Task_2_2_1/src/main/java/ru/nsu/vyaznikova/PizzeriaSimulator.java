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

    private volatile boolean isRunning = true;

    public PizzeriaSimulator(int N, int M, int T, int[] bakerSpeeds, int[] courierCapacities) {
        this.storage = new Storage(T);
        this.bakers = new Baker[N];
        this.bakerExecutor = Executors.newFixedThreadPool(N);
        for (int i = 0; i < N; i++) {
            bakers[i] = new Baker(i + 1, bakerSpeeds[i], orderQueue, storage, queueLock);
        }
        this.couriers = new Courier[M];
        this.courierExecutor = Executors.newFixedThreadPool(M);
        for (int i = 0; i < M; i++) {
            couriers[i] = new Courier(i + 1, courierCapacities[i], storage);
        }
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
        isRunning = false;

        synchronized (queueLock) {
            queueLock.notifyAll();
        }

        for (Baker baker : bakers) {
            baker.stop();
        }
        bakerExecutor.shutdown();

        for (Courier courier : couriers) {
            courier.stop();
        }
        courierExecutor.shutdown();

        storage.stop();

        try {
            bakerExecutor.awaitTermination(60, TimeUnit.SECONDS);
            courierExecutor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for threads to terminate.");
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
                Thread.currentThread().interrupt();
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