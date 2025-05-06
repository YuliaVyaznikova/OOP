package ru.nsu.vyaznikova;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
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
    private final Set<String> activeWorkers;
    private final TaskPool taskPool;
    private static final int MAX_ASSIGNMENTS_PER_TASK = 2; // Each task can be assigned to at most 2 workers

    public MasterNode(int port) {
        this.port = port;
        this.workers = new ConcurrentHashMap<>();
        this.activeWorkers = ConcurrentHashMap.newKeySet();
        this.taskPool = new TaskPool(MAX_ASSIGNMENTS_PER_TASK);
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

    private void processWorkerMessage(String workerId, Message message) throws IOException {
        switch (message.getType()) {
            case TASK_REQUEST:
                handleTaskRequest(workerId);
                break;
            case RESULT:
                handleTaskResult(workerId, (TaskResult)message.getContent());
                break;
            default:
                System.out.println("Unexpected message type from worker " + workerId + ": " + message.getType());
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
        System.out.println("Processed result from worker " + workerId + ": " + result);
    }

    public void distributeTask(int[] numbers) throws IOException {
        if (workers.isEmpty()) {
            throw new IllegalStateException("No workers connected");
        }
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(numbers, 0, numbers.length, taskId);
        taskPool.addTask(taskId, task);
        System.out.println("Added task " + taskId + " to pool");
    }

    public boolean getResult() {
        return taskPool.hasNonPrimeResult();
    }

    private void removeWorker(String workerId) {
        Socket socket = workers.remove(workerId);
        NetworkUtils.closeQuietly(socket);
        activeWorkers.remove(workerId);
    }

    public void stop() {
        isRunning.set(false);
        isStarted = false;
        NetworkUtils.closeQuietly(serverSocket);
        workers.values().forEach(NetworkUtils::closeQuietly);
        workers.clear();
        activeWorkers.clear();
    }
}