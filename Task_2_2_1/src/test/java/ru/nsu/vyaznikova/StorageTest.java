package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;

class StorageTest {

    @Test
    void testStoreAndTakePizzas() {
        Storage storage = new Storage(2);

        PizzaOrder order1 = new PizzaOrder(1);
        PizzaOrder order2 = new PizzaOrder(2);

        storage.storePizza(order1);
        storage.storePizza(order2);

        List<PizzaOrder> orders = storage.takePizzas(2);
        assertTrue(orders.size() == 2);
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    void testStorageOverflow() throws InterruptedException {
        Storage storage = new Storage(1);

        PizzaOrder order1 = new PizzaOrder(1);
        PizzaOrder order2 = new PizzaOrder(2);

        storage.storePizza(order1);

        Thread thread = new Thread(() -> storage.storePizza(order2));
        thread.start();

        Thread.sleep(100);

        assertTrue(storage.takePizzas(1).size() == 1);

        storage.takePizzas(1);

        thread.join();

        assertTrue(storage.takePizzas(1).size() == 1);
    }

    @Test
    void testEmptyStorage() throws InterruptedException {
        Storage storage = new Storage(1);

        Thread thread = new Thread(() -> {
            List<PizzaOrder> orders = storage.takePizzas(1);
            assertTrue(orders.isEmpty());
        });
        thread.start();

        Thread.sleep(100);

        storage.storePizza(new PizzaOrder(1));

        thread.join();
    }
}