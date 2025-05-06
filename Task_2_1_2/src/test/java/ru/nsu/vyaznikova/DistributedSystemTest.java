package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistributedSystemTest {
    private MasterNode master;
    private WorkerNode worker1;
    private WorkerNode worker2;
    private static final int MASTER_PORT = 8000;

    @BeforeEach
    void setUp() {
        master = new MasterNode(MASTER_PORT);
    }

    @AfterEach
    void tearDown() {
        if (worker1 != null) worker1.stop();
        if (worker2 != null) worker2.stop();
        if (master != null) master.stop();
    }

    @Test
    void testBasicConnection() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        
        Thread masterThread = new Thread(() -> {
            try {
                master.start();
                connectionLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        masterThread.start();
        
        // Ждем запуска мастера
        long startTime = System.currentTimeMillis();
        while (!master.isStarted() && System.currentTimeMillis() - startTime < 5000) {
            Thread.sleep(100);
        }
        assertTrue(master.isStarted(), "Master node failed to start");
        
        worker1 = new WorkerNode("localhost", MASTER_PORT, "worker1");
        worker1.start();
        
        Thread.sleep(2000);
        
        // проверяем, что воркер подключился
    }

    @Test
    void testArrayWithNonPrimes() throws Exception {
        int[] numbers = {6, 8, 7, 13, 5, 9, 4};
        
        CountDownLatch masterLatch = new CountDownLatch(1);
        Thread masterThread = new Thread(() -> {
            try {
                master.start();
                masterLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        masterThread.start();
        
        assertTrue(masterLatch.await(5, TimeUnit.SECONDS), "Master node failed to start");
        
        worker1 = new WorkerNode("localhost", MASTER_PORT, "worker1");
        worker2 = new WorkerNode("localhost", MASTER_PORT, "worker2");
        
        worker1.start();
        worker2.start();
        
        Thread.sleep(2000);
        
        // после реализации методов распределения задач и получения результатов:
        // master.distributeTask(numbers);
        // assertTrue(master.getResult(), "Should find non-prime numbers");
    }

    @Test
    void testArrayWithOnlyPrimes() throws Exception {
        int[] numbers = {2, 3, 5, 7, 11, 13, 17};
        
        CountDownLatch masterLatch = new CountDownLatch(1);
        Thread masterThread = new Thread(() -> {
            try {
                master.start();
                masterLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        masterThread.start();
        
        assertTrue(masterLatch.await(5, TimeUnit.SECONDS), "Master node failed to start");
        
        worker1 = new WorkerNode("localhost", MASTER_PORT, "worker1");
        worker1.start();
        
        Thread.sleep(2000);
        
        // после реализации методов распределения задач и получения результатов:
        // master.distributeTask(numbers);
        // assertFalse(master.getResult(), "Should not find any non-prime numbers");
    }

    @Test
    void testWorkerFailure() throws Exception {
        int[] numbers = {6, 8, 7, 13, 5, 9, 4};
        
        CountDownLatch masterLatch = new CountDownLatch(1);
        Thread masterThread = new Thread(() -> {
            try {
                master.start();
                masterLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        masterThread.start();
        
        assertTrue(masterLatch.await(5, TimeUnit.SECONDS), "Master node failed to start");
        
        worker1 = new WorkerNode("localhost", MASTER_PORT, "worker1");
        worker2 = new WorkerNode("localhost", MASTER_PORT, "worker2");
        
        worker1.start();
        worker2.start();
        
        Thread.sleep(2000);
        
        worker1.stop();
        
        // после реализации методов распределения задач и получения результатов:
        // master.distributeTask(numbers);
        // assertTrue(master.getResult(), "Should still find non-prime numbers even with one worker down");
    }
}
