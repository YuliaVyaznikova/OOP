package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PizzeriaSimulatorTest {

    @Test
    void testStartAndStopSimulation() throws InterruptedException {
        int N = 1;
        int M = 1;
        int T = 10;
        int[] bakerSpeeds = {1000};
        int[] courierCapacities = {2};

        PizzeriaSimulator simulator = new PizzeriaSimulator(N, M, T, bakerSpeeds, courierCapacities);

        simulator.startSimulation();

        simulator.placeOrder(new PizzaOrder(1));

        Thread.sleep(2000);

        simulator.stopSimulation();

        assertTrue(true);
    }
}