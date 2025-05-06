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
    }

    private void processTask(Message message) throws IOException {
    }

    public void stop() {
        isRunning = false;
        NetworkUtils.closeQuietly(socket);
    }
}