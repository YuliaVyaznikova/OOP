package ru.nsu.vyaznikova;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Master node in the distributed system for prime number checking.
 * Manages worker nodes, distributes tasks, and collects results.
 * Provides fault tolerance through worker health monitoring and task redistribution.
 */
public class MasterNode {
    private final int port;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private volatile boolean isStarted = false;
    private ServerSocket serverSocket;
    private final Map<String, Socket> workers;
    private final Map<String, Long> workerLastHeartbeat;
    private final Set<String> activeWorkers;
    private final TaskPool taskPool;
    private final ScheduledExecutorService healthChecker;

    private static final int OPTIMAL_CHUNK_SIZE = 1000; // Optimal size for task chunks
    private static final long WORKER_TIMEOUT = 30000; // 30 seconds timeout for workers
    private final AtomicInteger totalTasks = new AtomicInteger(0);
    private final AtomicInteger completedTasks = new AtomicInteger(0);

    /**
     * Creates a new master node.
     *
     * @param port The port number to listen for worker connections
     */
    public MasterNode(int port) {
        this.port = port;
        this.workers = new ConcurrentHashMap<>();
        this.workerLastHeartbeat = new ConcurrentHashMap<>();
        this.activeWorkers = ConcurrentHashMap.newKeySet();
        this.taskPool = new TaskPool();
        this.healthChecker = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the master node, begins accepting worker connections and monitoring worker health.
     *
     * @throws IOException if server socket creation fails
     */
    public void start() throws IOException {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }
        serverSocket = NetworkUtils.createServerSocket(port);
        Thread acceptThread = new Thread(this::acceptWorkers);
        acceptThread.start();
        
        // Start health checker
        healthChecker.scheduleAtFixedRate(this::checkWorkerHealth, 5, 5, TimeUnit.SECONDS);
        
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
                System.out.println("Received message from worker " + workerId 
                        + ": " + message.getType());
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

    private void processWorkerMessage(String workerId, Message message) throws IOException {
        workerLastHeartbeat.put(workerId, System.currentTimeMillis());
        
        switch (message.getType()) {
            case TASK_REQUEST:
                handleTaskRequest(workerId);
                break;
            case RESULT:
                handleTaskResult(workerId, (TaskResult) message.getContent());
                break;
            case HEARTBEAT:
                // Just update the heartbeat timestamp, which was done above
                break;
            default:
                System.out.println("Unexpected message type from worker " + workerId 
                        + ": " + message.getType());
        }
    }

    private void handleTaskRequest(String workerId) throws IOException {
        Optional<Task> task = taskPool.getNextTask(workerId);
        if (task.isPresent()) {
            Message taskMessage = new Message(Message.MessageType.TASK, task.get());
            NetworkUtils.sendMessage(workers.get(workerId), taskMessage);
            System.out.println("Sent task to worker " + workerId);
        } else {
            Message noTaskMessage = new Message(Message.MessageType.NO_TASKS, "No tasks available");
            NetworkUtils.sendMessage(workers.get(workerId), noTaskMessage);
            System.out.println("No tasks available for worker " + workerId);
        }
    }

    private void handleTaskResult(String workerId, TaskResult result) {
        taskPool.processResult(result.getTaskId(), workerId, result.hasNonPrime());
        completedTasks.incrementAndGet();
        System.out.println("Processed result from worker " + workerId + ": " + result 
                + " (Completed: " + completedTasks.get() + "/" + totalTasks.get() + ")");
    }

    /**
     * Distributes a task across available workers.
     *
     * @param numbers Array of numbers to check for primality
     * @throws IOException if task distribution fails
     * @throws IllegalStateException if no workers are connected
     */
    public void distributeTask(int[] numbers) throws IOException {
        if (workers.isEmpty()) {
            throw new IllegalStateException("No workers connected");
        }
        
        // Split the array into chunks
        int totalLength = numbers.length;
        int numChunks = Math.max(1, Math.min(totalLength / OPTIMAL_CHUNK_SIZE, workers.size() * 2));
        int baseChunkSize = totalLength / numChunks;
        int remainder = totalLength % numChunks;
        
        int start = 0;
        for (int i = 0; i < numChunks; i++) {
            int chunkSize = baseChunkSize + (i < remainder ? 1 : 0);
            String taskId = UUID.randomUUID().toString();
            Task task = new Task(numbers, start, start + chunkSize, taskId);
            taskPool.addTask(taskId, task);
            start += chunkSize;
            totalTasks.incrementAndGet();
        }
        
        System.out.println("Distributed array into " + numChunks + " tasks");
    }

    public boolean getResult() {
        return taskPool.hasNonPrimeResult();
    }

    private void removeWorker(String workerId) {
        Socket socket = workers.remove(workerId);
        NetworkUtils.closeQuietly(socket);
        activeWorkers.remove(workerId);
    }

    private void checkWorkerHealth() {
        long currentTime = System.currentTimeMillis();
        Set<String> deadWorkers = new HashSet<>();
        
        workerLastHeartbeat.forEach((workerId, lastHeartbeat) -> {
            if (currentTime - lastHeartbeat > WORKER_TIMEOUT) {
                System.out.println("Worker " + workerId + " appears to be dead, removing...");
                deadWorkers.add(workerId);
            }
        });
        
        deadWorkers.forEach(this::removeWorker);
    }
    
    /**
     * Stops the master node and cleans up all resources.
     * Disconnects all workers and stops accepting new connections.
     */
    public void stop() {
        isRunning.set(false);
        isStarted = false;
        healthChecker.shutdownNow();
        NetworkUtils.closeQuietly(serverSocket);
        workers.values().forEach(NetworkUtils::closeQuietly);
        workers.clear();
        activeWorkers.clear();
        workerLastHeartbeat.clear();
        totalTasks.set(0);
        completedTasks.set(0);
    }
}