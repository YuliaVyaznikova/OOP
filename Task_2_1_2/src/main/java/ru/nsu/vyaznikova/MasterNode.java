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
    private final AtomicBoolean isRunning;
    private ServerSocket serverSocket;
    private final Map<String, Socket> workers;
    private final Map<String, Boolean> taskResults;
    private final Set<String> activeWorkers;

    public MasterNode(int port) {
        this.port = port;
        this.isRunning = new AtomicBoolean(false);
        this.workers = new ConcurrentHashMap<>();
        this.taskResults = new ConcurrentHashMap<>();
        this.activeWorkers = ConcurrentHashMap.newKeySet();
    }

    public void start() throws IOException {
        isRunning.set(true);
        serverSocket = NetworkUtils.createServerSocket(port);
        
        Thread acceptThread = new Thread(this::acceptWorkers);
        acceptThread.start();
    }

    private void acceptWorkers() {
        while (isRunning.get() && !serverSocket.isClosed()) {
            try {
                Socket workerSocket = serverSocket.accept();
                String workerId = UUID.randomUUID().toString();
                workers.put(workerId, workerSocket);
                activeWorkers.add(workerId);
                
                Thread workerThread = new Thread(() -> handleWorkerMessages(workerId));
                workerThread.start();
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("Error accepting worker: " + e.getMessage());
                }
            }
        }
    }

    private void handleWorkerMessages(String workerId) {
        Socket socket = workers.get(workerId);
        try {
            while (isRunning.get() && !socket.isClosed()) {
                Message message = NetworkUtils.receiveMessage(socket);
                processWorkerMessage(workerId, message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling worker " + workerId + ": " + e.getMessage());
        } finally {
            removeWorker(workerId);
        }
    }

    private void processWorkerMessage(String workerId, Message message) throws IOException {
        
    }

    private void removeWorker(String workerId) {
    }

    public void stop() {
    }
}