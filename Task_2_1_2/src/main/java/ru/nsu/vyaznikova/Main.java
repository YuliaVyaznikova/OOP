package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point for the distributed prime number checking system.
 * Supports running either a master node or worker nodes from command line.
 */
public class Main {
    private static final int MASTER_PORT = 8000;
    private static final String MASTER_HOST = "localhost";

    private static boolean testMode = false;

    public static void setTestMode(boolean mode) {
        testMode = mode;
    }

    /**
     * Main method to start either a master or worker node.
     * Usage:
     *   java Main master [array_of_numbers] - to start master node
     *   java Main worker [worker_id]        - to start worker node
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage:");
            System.err.println("  java Main master [array_of_numbers]  - to start master node");
            System.err.println("  java Main worker [worker_id]         - to start worker node");
            if (testMode) {
                throw new RuntimeException("Invalid arguments");
            } else {
                System.exit(1);
            }
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
                    if (testMode) {
                        throw new RuntimeException("Unknown node type: " + nodeType);
                    } else {
                        System.exit(1);
                    }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (testMode) {
                throw new RuntimeException("Error: " + e.getMessage());
            } else {
                System.exit(1);
            }
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

        // Calculate number of tasks (each number is a separate task)
        int numTasks = numbers.length;
        // Create twice as many workers as tasks to ensure each task can have 2 workers
        int numWorkers = numTasks * 2;
        
        List<WorkerNode> workers = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            WorkerNode worker = new WorkerNode("localhost", MASTER_PORT, "worker" + (i + 1));
            worker.start();
            workers.add(worker);
            Thread.sleep(500);
        }

        master.distributeTask(numbers);

        Thread.sleep(2000);

        boolean result = master.getResult();
        System.out.println("\nResult: " + (result 
            ? "Found non-prime numbers in the array" 
            : "All numbers in the array are prime"));
        System.out.println("Input array: " + arrayToString(numbers));

        for (WorkerNode worker : workers) {
            worker.stop();
        }
        System.out.println("Master node is running. Press Ctrl+C to stop.");
        if (!testMode) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    private static void runWorker(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Worker ID is required");
            if (testMode) {
                throw new RuntimeException("Worker ID is required");
            } else {
                System.exit(1);
            }
        }

        String workerId = args[1];
        System.out.println("Starting worker node: " + workerId);
        
        WorkerNode worker = new WorkerNode(MASTER_HOST, MASTER_PORT, workerId);
        worker.start();

        System.out.println("Worker node is running. Press Ctrl+C to stop.");
        if (!testMode) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    /**
     * Parses command line arguments into an array of integers.
     *
     * @param args command line arguments
     * @return array of parsed integers
     * @throws IllegalArgumentException if no numbers are provided
     * @throws NumberFormatException if any argument is not a valid integer
     */
    static int[] parseNumbers(String[] args) {
        if (args.length <= 1) {
            throw new IllegalArgumentException("No numbers provided");
        }
        int[] numbers = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            numbers[i - 1] = Integer.parseInt(args[i]);
        }
        return numbers;
    }

    /**
     * Converts an array of integers to a string representation.
     * The format is [n1, n2, ..., nN] where n1...nN are array elements.
     *
     * @param array array of integers to convert
     * @return string representation of the array
     */
    public static String arrayToString(int[] array) {
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