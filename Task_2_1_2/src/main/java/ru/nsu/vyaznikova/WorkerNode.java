package ru.nsu.vyaznikova;

import java.io.IOException;
import java.net.Socket;

public class WorkerNode {
    private final String masterHost;
    private final int masterPort;
    private final String workerId;
    private Socket socket;
    private boolean isRunning;

    public WorkerNode(String masterHost, int masterPort, String workerId) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.workerId = workerId;
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        while (isRunning) {
            try {
                connectToMaster();
                processMessages();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in worker " + workerId + ": " + e.getMessage());
                NetworkUtils.closeQuietly(socket);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void connectToMaster() throws IOException {
        socket = NetworkUtils.createClientSocket(masterHost, masterPort);
    }

    private void processMessages() throws IOException, ClassNotFoundException {
        while (isRunning && socket != null && !socket.isClosed()) {
            Message message = NetworkUtils.receiveMessage(socket);
            
            switch (message.getType()) {
                case TASK:
                    processTask(message);
                    break;
                case HEARTBEAT:
                    NetworkUtils.sendMessage(socket, new Message(
                        Message.MessageType.HEARTBEAT,
                        "Worker " + workerId + " is alive"
                    ));
                    break;
                case ERROR:
                    System.err.println("Error from master: " + message.getContent());
                    break;
                default:
                    System.err.println("Unknown message type: " + message.getType());
            }
        }
    }

    private void processTask(Message message) throws IOException {
        try {
            String[] parts = message.getContent().split(",");
            int[] numbers = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }
            
            boolean hasNonPrime = PrimeChecker.hasNonPrime(numbers);
            
            Message response = new Message(
                Message.MessageType.RESULT,
                String.valueOf(hasNonPrime)
            );
            NetworkUtils.sendMessage(socket, response);
            
        } catch (Exception e) {
            Message errorMessage = new Message(
                Message.MessageType.ERROR,
                "Error processing task: " + e.getMessage()
            );
            NetworkUtils.sendMessage(socket, errorMessage);
        }
    }

    public void stop() {
        isRunning = false;
        NetworkUtils.closeQuietly(socket);
    }
}