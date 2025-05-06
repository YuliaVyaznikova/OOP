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
                // Request a task
                requestTask();
                
                // Wait for response
                Message message = NetworkUtils.receiveMessage(socket);
                System.out.println("Worker " + workerId + " received message: " + message.getType());
                
                switch (message.getType()) {
                    case TASK:
                        processTask(message);
                        break;
                    case NO_TASKS:
                        Thread.sleep(1000); // Wait before requesting again
                        break;
                    default:
                        System.out.println("Worker " + workerId + " received unexpected message type: " + message.getType());
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.err.println("Error processing messages for worker " + workerId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Worker " + workerId + " stopping");
            stop();
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
        NetworkUtils.closeQuietly(socket);
        if (messageThread != null) {
            messageThread.interrupt();
        }
    }
}