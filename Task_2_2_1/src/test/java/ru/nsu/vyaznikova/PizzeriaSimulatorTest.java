package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PizzeriaSimulatorTest {
    private ByteArrayOutputStream outputStream;
    private PizzeriaSimulator simulator;

    @BeforeEach
    public void setUp() {
        simulator = new PizzeriaSimulator(1, 1, 1, new int[]{100}, new int[]{1});
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void test() throws InterruptedException {
        System.out.println("=== Starting test ===");
        simulator.startSimulation();
        System.out.println("Simulation started");
        simulator.placeOrder(new PizzaOrder(1));
        simulator.placeOrder(new PizzaOrder(1));
        System.out.println("Order placed");
        Thread.sleep(5000); // Увеличиваем время ожидания до 5 секунд
        System.out.println("After sleep");
        simulator.stopSimulation();
        System.out.println("Simulation stopped");
        String output = outputStream.toString();
        System.out.println("Test output: " + output);
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testStorage() {
        Storage storage = new Storage(100);
        PizzaOrder order = new PizzaOrder(10);
        storage.storePizza(order);
        PizzaOrder orderOut = storage.takePizzas(1).get(0);
        assertEquals(order.getOrderId(), orderOut.getOrderId());
    }
}