package ru.nsu.vyaznikova;

import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final int MASTER_PORT = 8000;
    private static final String MASTER_HOST = "localhost";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage:");
            System.err.println("  java Main master [array_of_numbers]  - to start master node");
            System.err.println("  java Main worker [worker_id]         - to start worker node");
            System.exit(1);
        }

        String nodeType = args[0].toLowerCase();
        
        try {
            switch (nodeType) {
                case "master":
                    runMaster(args);
                    break;
                case "worker":
                    runWorker(args);
                    break;
                default:
                    System.err.println("Unknown node type: " + nodeType);
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runMaster(String[] args) throws Exception {
        int[] numbers;
        if (args.length > 1) {
            numbers = parseNumbers(args);
        } else {
            numbers = new int[]{6, 8, 7, 13, 5, 9, 4};
        }

        System.out.println("Starting master node on port " + MASTER_PORT);
        System.out.println("Input array: " + arrayToString(numbers));

        MasterNode master = new MasterNode(MASTER_PORT);
        master.start();

        List<WorkerNode> workers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            WorkerNode worker = new WorkerNode("localhost", MASTER_PORT, "worker" + (i + 1));
            worker.start();
            workers.add(worker);
            Thread.sleep(500);
        }

        master.distributeTask(numbers);

        Thread.sleep(2000);

        boolean result = master.getResult();
        System.out.println("\nResult: " + (result ? 
            "Found non-prime numbers in the array" : 
            "All numbers in the array are prime"));
        System.out.println("Input array: " + arrayToString(numbers));

        for (WorkerNode worker : workers) {
            worker.stop();
        }
        System.out.println("Master node is running. Press Ctrl+C to stop.");
        Thread.sleep(Long.MAX_VALUE);
    }

    private static void runWorker(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Worker ID is required");
            System.exit(1);
        }

        String workerId = args[1];
        System.out.println("Starting worker node: " + workerId);
        
        WorkerNode worker = new WorkerNode(MASTER_HOST, MASTER_PORT, workerId);
        worker.start();

        System.out.println("Worker node is running. Press Ctrl+C to stop.");
        Thread.sleep(Long.MAX_VALUE);
    }

    private static int[] parseNumbers(String[] args) {
        int[] numbers = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            numbers[i - 1] = Integer.parseInt(args[i]);
        }
        return numbers;
    }

    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}