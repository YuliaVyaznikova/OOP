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
    }

    public void stop() {
        isRunning = false;
        NetworkUtils.closeQuietly(socket);
    }
}