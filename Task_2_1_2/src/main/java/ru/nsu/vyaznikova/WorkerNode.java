package ru.nsu.vyaznikova;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A worker node in the distributed system that processes prime number checking tasks.
 * Connects to a master node, receives tasks, processes them, and sends back results.
 * Maintains connection with heartbeat mechanism and supports automatic reconnection.
 */
public class WorkerNode {
    private final String masterHost;
    private final int masterPort;
    private final String workerId;
    private Socket socket;
    private Thread messageThread;
    private final AtomicBoolean isRunning;
    private final ScheduledExecutorService heartbeatExecutor;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final long RECONNECT_DELAY_MS = 5000;
    private static final long HEARTBEAT_INTERVAL_MS = 5000;

    /**
     * Creates a new worker node.
     *
     * @param masterHost the hostname of the master node
     * @param masterPort the port number of the master node
     * @param workerId unique identifier for this worker
     */
    public WorkerNode(String masterHost, int masterPort, String workerId) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.workerId = workerId;
        this.isRunning = new AtomicBoolean(false);
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the worker node, connecting to the master and beginning task processing.
     *
     * @throws IOException if connection to master fails
     */
    public void start() throws IOException {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }
        connect();
        startHeartbeat();
    }

    private void connect() throws IOException {
        int attempts = 0;
        while (attempts < MAX_RECONNECT_ATTEMPTS) {
            try {
                System.out.println("Worker " + workerId + " connecting to " + masterHost + ":" 
                        + masterPort + " (attempt " + (attempts + 1) + " of " 
                        + MAX_RECONNECT_ATTEMPTS + ")");
                socket = NetworkUtils.createClientSocket(masterHost, masterPort);
                System.out.println("Worker " + workerId + " connected successfully");
                messageThread = new Thread(this::processMessages);
                messageThread.start();
                System.out.println("Worker " + workerId + " started message processing thread");
                return;
            } catch (IOException e) {
                attempts++;
                if (attempts >= MAX_RECONNECT_ATTEMPTS) {
                    throw e;
                }
                System.err.println("Connection attempt failed, retrying in " 
                        + (RECONNECT_DELAY_MS / 1000) + " seconds...");
                try {
                    Thread.sleep(RECONNECT_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException(
                            "Interrupted while attempting to connect", ie);
                }
            }
        }
    }

    private void startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                if (isRunning.get() && socket != null && !socket.isClosed()) {
                    Message heartbeat = new Message(Message.MessageType.HEARTBEAT, workerId);
                    NetworkUtils.sendMessage(socket, heartbeat);
                }
            } catch (IOException e) {
                System.err.println("Failed to send heartbeat: " + e.getMessage());
            }
        }, HEARTBEAT_INTERVAL_MS, HEARTBEAT_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    private void processMessages() {
        try {
            while (isRunning.get() && !socket.isClosed()) {
                try {
                    // Request a task
                    requestTask();
                    
                    // Wait for response with timeout
                    Message message = NetworkUtils.receiveMessage(socket);
                    System.out.println("Worker " + workerId + " received message: " + message.getType());
                    
                    switch (message.getType()) {
                        case TASK:
                            processTask(message);
                            break;
                        case NO_TASKS:
                            Thread.sleep(1000); // Wait before requesting again
                            break;
                        case ERROR:
                            System.err.println("Error from master: " 
                                    + message.getContent());
                            break;
                        default:
                            System.out.println("Worker " + workerId 
                                    + " received unexpected message type: " 
                                    + message.getType());
                    }
                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                    // Try to reconnect
                    tryReconnect();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal error in worker " + workerId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Worker " + workerId + " stopping");
            stop();
        }
    }

    private void tryReconnect() {
        if (!isRunning.get()) {
            return;
        }
        
        System.out.println("Attempting to reconnect...");
        NetworkUtils.closeQuietly(socket);
        
        try {
            connect();
        } catch (IOException e) {
            System.err.println("Failed to reconnect: " + e.getMessage());
        }
    }

    private void requestTask() throws IOException {
        Message requestMessage = new Message(Message.MessageType.TASK_REQUEST, workerId);
        NetworkUtils.sendMessage(socket, requestMessage);
        System.out.println("Worker " + workerId + " requested a task");
    }

    private void processTask(Message message) throws IOException {
        Task task = (Task) message.getContent();
        int[] numbers = task.getNumbers();
        boolean hasNonPrime = false;
        
        for (int number : numbers) {
            if (!PrimeChecker.isPrime(number)) {
                hasNonPrime = true;
                break;
            }
        }
        
        Message resultMessage = new Message(Message.MessageType.RESULT, 
            new TaskResult(task.getTaskId(), hasNonPrime));
        NetworkUtils.sendMessage(socket, resultMessage);
        System.out.println("Worker " + workerId + " processed task " + task.getTaskId() 
                + " and sent result: " + hasNonPrime);
    }

    /**
     * Stops the worker node, cleaning up all resources.
     */
    public void stop() {
        // First stop all background activities
        isRunning.set(false);
        heartbeatExecutor.shutdownNow();
        
        // Wait for heartbeat executor to finish
        try {
            if (!heartbeatExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                System.err.println("Heartbeat executor did not terminate in time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Close socket to interrupt any blocking operations
        NetworkUtils.closeQuietly(socket);
        
        // Wait for message thread to finish
        if (messageThread != null) {
            messageThread.interrupt();
            try {
                messageThread.join(2000); // Wait up to 2 seconds for thread to finish
                if (messageThread.isAlive()) {
                    System.err.println("Message thread did not terminate in time");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}