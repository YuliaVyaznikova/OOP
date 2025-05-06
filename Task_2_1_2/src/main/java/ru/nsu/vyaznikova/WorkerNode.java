package ru.nsu.vyaznikova;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerNode {
    private final String masterHost;
    private final int masterPort;
    private final String workerId;
    private Socket socket;
    private Thread messageThread;
    private final AtomicBoolean isRunning;

    public WorkerNode(String masterHost, int masterPort, String workerId) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.workerId = workerId;
        this.isRunning = new AtomicBoolean(false);
    }

    public void start() throws IOException {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }
        System.out.println("Worker " + workerId + " connecting to " + masterHost + ":" + masterPort);
        socket = NetworkUtils.createClientSocket(masterHost, masterPort);
        System.out.println("Worker " + workerId + " connected successfully");
        messageThread = new Thread(this::processMessages);
        messageThread.start();
        System.out.println("Worker " + workerId + " started message processing thread");
    }

    private void processMessages() {
        try {
            while (isRunning.get() && !socket.isClosed()) {
                System.out.println("Worker " + workerId + " waiting for message");
                Message message = NetworkUtils.receiveMessage(socket);
                System.out.println("Worker " + workerId + " received message: " + message.getType());
                processMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error processing messages for worker " + workerId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Worker " + workerId + " stopping");
            stop();
        }
    }

    private void processMessage(Message message) throws IOException {
        try {
            if (message.getType() == Message.MessageType.TASK) {
                Task task = (Task) message.getContent();
                int[] numbers = task.getNumbers();
                boolean hasNonPrime = false;
                
                for (int number : numbers) {
                    if (!PrimeChecker.isPrime(number)) {
                        hasNonPrime = true;
                        break;
                    }
                }
                
                Message resultMessage = new Message(Message.MessageType.RESULT, String.valueOf(hasNonPrime));
                NetworkUtils.sendMessage(socket, resultMessage);
                System.out.println("Worker " + workerId + " processed task and sent result: " + hasNonPrime);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            throw new IOException("Failed to process message", e);
        }
    }

    public void stop() {
        isRunning.set(false);
        NetworkUtils.closeQuietly(socket);
        if (messageThread != null) {
            messageThread.interrupt();
        }
    }
}