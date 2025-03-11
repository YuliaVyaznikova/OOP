package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;

class CourierTest {

    @Test
    void testTakePizzasFromStorage() throws InterruptedException {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 2, storage);

        storage.storePizza(new PizzaOrder(1));
        storage.storePizza(new PizzaOrder(2));

        Thread thread = new Thread(courier);
        thread.start();

        Thread.sleep(500);

        List<PizzaOrder> orders = storage.takePizzas(2);
        assertTrue(orders.isEmpty());

        courier.stop();
        thread.join();
    }

    @Test
    void testStopCourier() throws InterruptedException {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 2, storage);

        Thread thread = new Thread(courier);
        thread.start();

        courier.stop();
        thread.join();

        assertFalse(thread.isAlive());
    }
}