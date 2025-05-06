package ru.nsu.vyaznikova;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class MasterNode {
    private final int port;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private volatile boolean isStarted = false;
    private ServerSocket serverSocket;
    private final Map<String, Socket> workers;
    private final Map<String, Boolean> taskResults;
    private volatile boolean hasNonPrimes = false;
    private final Set<String> activeWorkers;

    public MasterNode(int port) {
        this.port = port;
        this.workers = new ConcurrentHashMap<>();
        this.taskResults = new ConcurrentHashMap<>();
        this.activeWorkers = ConcurrentHashMap.newKeySet();
    }

    public void start() throws IOException {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }
        serverSocket = NetworkUtils.createServerSocket(port);
        Thread acceptThread = new Thread(this::acceptWorkers);
        acceptThread.start();
        isStarted = true;
        System.out.println("Master node started successfully");
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public boolean isStarted() {
        return isStarted;
    }

    private void acceptWorkers() {
        System.out.println("Starting to accept workers on port " + port);
        while (isRunning.get() && !serverSocket.isClosed()) {
            try {
                System.out.println("Waiting for worker connection...");
                Socket workerSocket = serverSocket.accept();
                String workerId = UUID.randomUUID().toString();
                System.out.println("Worker connected: " + workerId);
                workers.put(workerId, workerSocket);
                activeWorkers.add(workerId);
                
                Thread workerThread = new Thread(() -> handleWorkerMessages(workerId));
                workerThread.start();
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("Error accepting worker: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleWorkerMessages(String workerId) {
        Socket socket = workers.get(workerId);
        try {
            System.out.println("Started handling messages for worker: " + workerId);
            while (isRunning.get() && !socket.isClosed()) {
                System.out.println("Waiting for message from worker: " + workerId);
                Message message = NetworkUtils.receiveMessage(socket);
                System.out.println("Received message from worker " + workerId + ": " + message.getType());
                processWorkerMessage(workerId, message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling worker " + workerId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Worker disconnected: " + workerId);
            removeWorker(workerId);
        }
    }

    private void processWorkerMessage(String workerId, Message message) {
        if (message.getType() == Message.MessageType.RESULT) {
            boolean result = Boolean.parseBoolean((String)message.getContent());
            if (result) {
                hasNonPrimes = true;
                System.out.println("Worker " + workerId + " found non-prime numbers");
            } else {
                System.out.println("Worker " + workerId + " found only prime numbers");
            }
            taskResults.put(workerId, result);
        }
    }

    public void distributeTask(int[] numbers) throws IOException {
        if (workers.isEmpty()) {
            throw new IllegalStateException("No workers connected");
        }
        Task task = new Task(numbers);
        Message taskMessage = new Message(Message.MessageType.TASK, task);
        
        for (Map.Entry<String, Socket> worker : workers.entrySet()) {
            try {
                NetworkUtils.sendMessage(worker.getValue(), taskMessage);
                System.out.println("Task sent to worker: " + worker.getKey());
            } catch (IOException e) {
                System.err.println("Failed to send task to worker " + worker.getKey() + ": " + e.getMessage());
                removeWorker(worker.getKey());
            }
        }
    }

    public boolean getResult() {
        return hasNonPrimes;
    }

    private void removeWorker(String workerId) {
    }

    public void stop() {
        isRunning.set(false);
        NetworkUtils.closeQuietly(serverSocket);
        workers.values().forEach(NetworkUtils::closeQuietly);
        workers.clear();
        taskResults.clear();
        hasNonPrimes = false;
    }

    public void distributeTask(int[] numbers) {
        if (workers.isEmpty()) {
            throw new IllegalStateException("No workers available");
        }

        String taskId = UUID.randomUUID().toString();
        Task task = new Task(numbers, 0, numbers.length, taskId);
        Message message = new Message(Message.MessageType.TASK, task);

        for (Map.Entry<String, Socket> worker : workers.entrySet()) {
            try {
                NetworkUtils.sendMessage(worker.getValue(), message);
            } catch (IOException e) {
                System.err.println("Error sending task to worker " + worker.getKey() + ": " + e.getMessage());
                removeWorker(worker.getKey());
            }
        }
    }

    public boolean getResult() {
        return hasNonPrimes;
    }
}