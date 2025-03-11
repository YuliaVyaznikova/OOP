package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PizzeriaSimulatorTest {

    @Test
    void testStartAndStopSimulation() throws InterruptedException {
        int N = 10;
        int M = 10;
        int T = 100;
        int[] bakerSpeeds = {11};
        int[] courierCapacities = {2};

        PizzeriaSimulator simulator = new PizzeriaSimulator(N, M, T, bakerSpeeds, courierCapacities);

        simulator.startSimulation();

        simulator.placeOrder(new PizzaOrder(1));

        Thread.sleep(20);

        simulator.stopSimulation();

        assertTrue(true);
    }
}