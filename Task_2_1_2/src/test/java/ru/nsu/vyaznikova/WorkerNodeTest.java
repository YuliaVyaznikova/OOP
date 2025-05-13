package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class WorkerNodeTest {
    private static final int TEST_PORT = 8888;
    private static final String TEST_HOST = "localhost";
    private ServerSocket serverSocket;
    private WorkerNode worker;
    private Socket clientSocket;

    @BeforeEach
    void setUp() throws IOException {
        serverSocket = new ServerSocket(TEST_PORT);
        serverSocket.setReuseAddress(true);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (worker != null) {
            worker.stop();
        }
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    @Test
    void testWorkerStartAndConnect() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        
        // Start server thread
        Thread serverThread = new Thread(() -> {
            try {
                clientSocket = serverSocket.accept();
                connectionLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Start worker
        worker = new WorkerNode(TEST_HOST, TEST_PORT, "test-worker");
        worker.start();

        // Wait for connection
        assertTrue(connectionLatch.await(5, TimeUnit.SECONDS), "Connection timed out");
        assertTrue(clientSocket.isConnected(), "Client socket should be connected");
    }



    @Test
    void testWorkerTaskProcessing() throws Exception {
        CountDownLatch taskLatch = new CountDownLatch(1);
        
        // Start server thread
        Thread serverThread = new Thread(() -> {
            try {
                clientSocket = serverSocket.accept();
                
                // Wait for task request
                Message requestMessage = NetworkUtils.receiveMessage(clientSocket);
                assertEquals(Message.MessageType.TASK_REQUEST, requestMessage.getType());
                
                // Send task
                Task task = new Task(new int[]{4, 6, 8}, 0, 3, "test-task");
                Message taskMessage = new Message(Message.MessageType.TASK, task);
                NetworkUtils.sendMessage(clientSocket, taskMessage);
                
                // Wait for result
                Message resultMessage = NetworkUtils.receiveMessage(clientSocket);
                assertEquals(Message.MessageType.RESULT, resultMessage.getType());
                TaskResult result = (TaskResult) resultMessage.getContent();
                assertTrue(result.hasNonPrime());
                taskLatch.countDown();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Start worker
        worker = new WorkerNode(TEST_HOST, TEST_PORT, "test-worker");
        worker.start();

        // Wait for task completion
        assertTrue(taskLatch.await(5, TimeUnit.SECONDS), "Task processing timed out");
    }

    @Test
    void testWorkerReconnect() throws Exception {
        // Start worker with wrong port
        worker = new WorkerNode(TEST_HOST, TEST_PORT + 1, "test-worker");
        assertThrows(IOException.class, () -> worker.start());
    }

}
