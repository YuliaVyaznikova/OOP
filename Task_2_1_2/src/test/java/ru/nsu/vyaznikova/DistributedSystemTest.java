package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for the distributed system implementation.
 * Verifies the interaction between master and worker nodes.
 */
@Timeout(value = 10, unit = TimeUnit.SECONDS)
public class DistributedSystemTest {
    private MasterNode master;
    private List<WorkerNode> workers;
    private static int masterPort = 8000;
    private int currentPort;
    private static final long WAIT_TIME = 100;

    private int getNextPort() {
        return masterPort++;
    }

    @BeforeEach
    void setUp() {
        currentPort = getNextPort();
        master = new MasterNode(currentPort);
        workers = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        for (WorkerNode worker : workers) {
            if (worker != null) {
                worker.stop();
            }
        }
        if (master != null) {
            master.stop();
        }
    }

    private void startWorker(String id) throws Exception {
        WorkerNode worker = new WorkerNode("localhost", currentPort, id);
        worker.start();
        workers.add(worker);
        Thread.sleep(WAIT_TIME);
    }

    private void waitForResult() throws Exception {
        int attempts = 20;
        while (attempts > 0) {
            if (!master.isRunning()) {
                fail("Master node stopped unexpectedly");
            }
            Thread.sleep(WAIT_TIME);
            attempts--;
        }
    }

    @Test
    void testMultipleWorkers() throws Exception {
        master.start();
        Thread.sleep(WAIT_TIME);
        assertTrue(master.isRunning(), "Master node failed to start");

        // Create twice as many workers as tasks
        int[] numbers = {4, 6};
        int numWorkers = numbers.length * 2;
        for (int i = 0; i < numWorkers; i++) {
            startWorker("worker" + (i + 1));
        }

        master.distributeTask(numbers);
        waitForResult();

        assertTrue(master.getResult(), "Should find non-prime numbers");
    }

    @Test
    void testExample1WithRedundancy() throws Exception {
        master.start();
        Thread.sleep(WAIT_TIME);

        // Create twice as many workers as tasks
        int[] numbers = {6, 8, 7, 13, 5, 9, 4};
        int numWorkers = numbers.length * 2;
        for (int i = 0; i < numWorkers; i++) {
            startWorker("worker" + (i + 1));
        }

        master.distributeTask(numbers);
        waitForResult();
        assertTrue(master.getResult(), "Should find non-prime numbers in example 1");
    }

    @Test
    void testExample2WithRedundancy() throws Exception {
        master.start();
        Thread.sleep(WAIT_TIME);

        // Create twice as many workers as tasks
        int[] numbers = {2, 3, 5, 7, 11, 13, 17};
        int numWorkers = numbers.length * 2;
        for (int i = 0; i < numWorkers; i++) {
            startWorker("worker" + (i + 1));
        }

        master.distributeTask(numbers);
        waitForResult();
        assertFalse(master.getResult(), "Should not find non-prime numbers in example 2");
    }

    @Test
    void testWorkerFailure() throws Exception {
        master.start();
        Thread.sleep(WAIT_TIME);

        startWorker("worker1");
        startWorker("worker2");

        int[] numbers = {4, 6};
        master.distributeTask(numbers);
        Thread.sleep(WAIT_TIME);

        workers.get(0).stop();
        waitForResult();
        assertTrue(master.getResult(), "Should find non-prime numbers despite worker failure");
    }
}