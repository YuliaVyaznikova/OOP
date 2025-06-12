package ru.nsu.vyaznikova;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        simulator.placeOrder(new PizzaOrder(1));
        simulator.placeOrder(new PizzaOrder(1));
        Thread.sleep(5000);
        simulator.stopSimulation();
        String output = outputStream.toString();
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testStopSimulation() throws InterruptedException {
        simulator.startSimulation();
        simulator.placeOrder(new PizzaOrder(1));
        simulator.stopSimulation();
        String output = outputStream.toString();
        assertTrue(output.contains("Stopping simulation..."));
        assertTrue(output.contains("Simulation stopped."));
    }

    @Test
    public void testMultipleOrders() throws InterruptedException {
        simulator.startSimulation();
        simulator.placeOrder(new PizzaOrder(1));
        simulator.placeOrder(new PizzaOrder(2));
        simulator.placeOrder(new PizzaOrder(3));
        Thread.sleep(5000);
        simulator.stopSimulation();
        String output = outputStream.toString();
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testSingleBakerSingleCourier() throws InterruptedException {
        PizzeriaSimulator simulator = new PizzeriaSimulator(1, 1, 1, new int[]{100}, new int[]{1});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        simulator.startSimulation();
        simulator.placeOrder(new PizzaOrder(1));
        Thread.sleep(5000);
        simulator.stopSimulation();
        
        String output = outputStream.toString();
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testMultipleBakersMultipleCouriers() throws InterruptedException {
        PizzeriaSimulator simulator = new PizzeriaSimulator(2, 2, 10, new int[]{100, 200}, new int[]{2, 3});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        simulator.startSimulation();
        simulator.placeOrder(new PizzaOrder(1));
        simulator.placeOrder(new PizzaOrder(2));
        Thread.sleep(5000);
        simulator.stopSimulation();
        
        String output = outputStream.toString();
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testInterruptedSimulation() throws InterruptedException {
        PizzeriaSimulator simulator = new PizzeriaSimulator(1, 1, 1, new int[]{100}, new int[]{1});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        simulator.startSimulation();
        simulator.placeOrder(new PizzaOrder(1));
        Thread.sleep(10000); // Увеличиваем время ожидания
        simulator.stopSimulation();
        
        String output = outputStream.toString();
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testStorageCapacity() throws InterruptedException {
        PizzeriaSimulator simulator = new PizzeriaSimulator(1, 1, 2, new int[]{100}, new int[]{1});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        simulator.startSimulation();
        simulator.placeOrder(new PizzaOrder(1));
        simulator.placeOrder(new PizzaOrder(2));
        Thread.sleep(3000);
        simulator.stopSimulation();
        
        String output = outputStream.toString();
        assertTrue(output.contains("[Принят в работу"));
        assertTrue(output.contains("[Готово"));
        assertTrue(output.contains("[Доставка"));
    }

    @Test
    public void testMainMethod() throws InterruptedException {
        PizzeriaSimulator simulator = new PizzeriaSimulator(2, 2, 10, new int[]{2000, 3000}, new int[]{2, 3});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        simulator.startSimulation();
        
        for (int i = 1; i <= 10; i++) {
            simulator.placeOrder(new PizzaOrder(i));
            Thread.sleep(500);
        }
        
        Thread.sleep(10000);
        simulator.stopSimulation();
        
        String output = outputStream.toString();
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

    @Test
    public void testPizzaOrder() {
        PizzaOrder order = new PizzaOrder(1);
        assertEquals(1, order.getOrderId());
        String expectedToString = "PizzaOrder{" 
            + "orderId=" + order.getOrderId() 
            + '}';
        assertEquals(expectedToString, order.toString());
        PizzaOrder sameOrder = new PizzaOrder(1);
        PizzaOrder differentOrder = new PizzaOrder(2);
        assertTrue(order.equals(sameOrder));
        assertFalse(order.equals(differentOrder));
        assertFalse(order.equals(null));
        assertFalse(order.equals(new Object()));
        assertEquals(order.hashCode(), sameOrder.hashCode());
        assertNotEquals(order.hashCode(), differentOrder.hashCode());
    }
    }