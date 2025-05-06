package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

public class DistributedSystemTest {
    private MasterNode master;
    private WorkerNode worker;
    private static final int MASTER_PORT = 8000;

    @BeforeEach
    void setUp() {
        master = new MasterNode(MASTER_PORT);
    }

    @AfterEach
    void tearDown() {
        if (worker != null) worker.stop();
        if (master != null) master.stop();
    }

    @Test
    void testBasicConnection() throws Exception {
        // Start master
        master.start();
        Thread.sleep(1000);
        assertTrue(master.isStarted(), "Master node failed to start");

        // Start worker
        worker = new WorkerNode("localhost", MASTER_PORT, "worker1");
        worker.start();
        Thread.sleep(1000);

        // Test with non-prime numbers
        int[] numbers = {4, 6, 8, 9};
        master.distributeTask(numbers);
        Thread.sleep(1000);
        assertTrue(master.getResult(), "Should find non-prime numbers");
    }
}
