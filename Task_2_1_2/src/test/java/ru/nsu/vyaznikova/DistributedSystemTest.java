package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class DistributedSystemTest {
    private MasterNode master;
    private List<WorkerNode> workers;
    private static final int MASTER_PORT = 8000;

    @BeforeEach
    void setUp() {
        master = new MasterNode(MASTER_PORT);
        workers = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        for (WorkerNode worker : workers) {
            if (worker != null) worker.stop();
        }
        if (master != null) master.stop();
    }

    private void startWorker(String id) throws Exception {
        WorkerNode worker = new WorkerNode("localhost", MASTER_PORT, id);
        worker.start();
        workers.add(worker);
        Thread.sleep(500); // Give worker time to connect
    }

    @Test
    void testMultipleWorkers() throws Exception {
        master.start();
        Thread.sleep(500);
        assertTrue(master.isStarted(), "Master node failed to start");

        // Start multiple workers
        startWorker("worker1");
        startWorker("worker2");
        startWorker("worker3");

        // Test with non-prime numbers
        int[] numbers = {4, 6, 8, 9};
        master.distributeTask(numbers);
        Thread.sleep(2000); // Give workers time to process
        assertTrue(master.getResult(), "Should find non-prime numbers");
    }

    @Test
    void testExample1WithRedundancy() throws Exception {
        master.start();
        Thread.sleep(500);

        // Start workers with redundancy
        startWorker("worker1");
        startWorker("worker2");

        int[] numbers = {6, 8, 7, 13, 5, 9, 4};
        master.distributeTask(numbers);
        Thread.sleep(2000);
        assertTrue(master.getResult(), "Should find non-prime numbers in example 1");
    }

    @Test
    void testExample2WithRedundancy() throws Exception {
        master.start();
        Thread.sleep(500);

        // Start workers with redundancy
        startWorker("worker1");
        startWorker("worker2");

        int[] numbers = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                        6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        master.distributeTask(numbers);
        Thread.sleep(2000);
        assertFalse(master.getResult(), "Should not find any non-prime numbers in example 2");
    }

    @Test
    void testWorkerFailure() throws Exception {
        master.start();
        Thread.sleep(500);

        // Start two workers
        startWorker("worker1");
        startWorker("worker2");

        int[] numbers = {4, 6, 8, 9};
        master.distributeTask(numbers);
        Thread.sleep(500);

        // Stop one worker to simulate failure
        workers.get(0).stop();

        Thread.sleep(2000); // Wait for task reassignment and processing
        assertTrue(master.getResult(), "Should find non-prime numbers despite worker failure");
    }
}