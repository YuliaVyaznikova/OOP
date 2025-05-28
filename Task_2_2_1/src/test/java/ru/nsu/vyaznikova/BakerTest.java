package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.Queue;

class BakerTest {

    @Test
    void testTakeOrderFromQueue() throws InterruptedException {
        Queue<PizzaOrder> orderQueue = new LinkedList<>();
        Object queueLock = new Object();
        Storage storage = new Storage(10);

        Baker baker = new Baker(1, 1000, orderQueue, storage, queueLock);

        PizzaOrder order = new PizzaOrder(1);
        orderQueue.add(order);

        Thread thread = new Thread(baker);
        thread.start();

        Thread.sleep(500);

        assertTrue(orderQueue.isEmpty());

        baker.stop();
        thread.join();
    }

    @Test
    void testStopBaker() throws InterruptedException {
        Queue<PizzaOrder> orderQueue = new LinkedList<>();
        Object queueLock = new Object();
        Storage storage = new Storage(10);

        Baker baker = new Baker(1, 1000, orderQueue, storage, queueLock);

        Thread thread = new Thread(baker);
        thread.start();

        baker.stop();
        thread.join();

        assertFalse(thread.isAlive());
    }
}