package ru.nsu.vyaznikova;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public WorkerNode(String masterHost, int masterPort, String workerId) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.workerId = workerId;
        this.isRunning = new AtomicBoolean(false);
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
    }

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
                System.out.println("Worker " + workerId + " connecting to " + masterHost + ":" + masterPort + 
                                 " (attempt " + (attempts + 1) + " of " + MAX_RECONNECT_ATTEMPTS + ")");
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
                System.err.println("Connection attempt failed, retrying in " + RECONNECT_DELAY_MS/1000 + " seconds...");
                try {
                    Thread.sleep(RECONNECT_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted while attempting to connect", ie);
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
                            System.err.println("Error from master: " + message.getContent());
                            break;
                        default:
                            System.out.println("Worker " + workerId + " received unexpected message type: " + message.getType());
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
        if (!isRunning.get()) return;
        
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
        System.out.println("Worker " + workerId + " processed task " + task.getTaskId() + 
                         " and sent result: " + hasNonPrime);
    }

    public void stop() {
        isRunning.set(false);
        heartbeatExecutor.shutdownNow();
        NetworkUtils.closeQuietly(socket);
        if (messageThread != null) {
            messageThread.interrupt();
            try {
                messageThread.join(5000); // Wait up to 5 seconds for thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}