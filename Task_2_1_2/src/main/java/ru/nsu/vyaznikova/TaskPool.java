package ru.nsu.vyaznikova;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPool {
    private final Map<String, Task> availableTasks;
    private final Map<String, TaskAssignment> assignedTasks;

    private static final long TASK_TIMEOUT = 30000; // 30 seconds
    private static final int MAX_PARALLEL_WORKERS = 3; // Maximum number of workers that can process a task in parallel
    private final ScheduledExecutorService timeoutChecker;
    private final ConcurrentMap<String, Set<String>> workerTasks; // Maps worker to their assigned tasks

    private static class TaskAssignment {
        final Set<String> workerIds;
        final Map<String, Long> assignmentTimes;
        final AtomicInteger completedAssignments;
        final AtomicInteger failedAssignments;
        volatile boolean isCompleted;
        volatile boolean hasNonPrime;
        final Task originalTask;
        final Set<Boolean> results; // Store results from different workers

        TaskAssignment(Task task) {
            this.workerIds = ConcurrentHashMap.newKeySet();
            this.assignmentTimes = new ConcurrentHashMap<>();
            this.completedAssignments = new AtomicInteger(0);
            this.failedAssignments = new AtomicInteger(0);
            this.isCompleted = false;
            this.hasNonPrime = false;
            this.originalTask = task;
            this.results = ConcurrentHashMap.newKeySet();
        }

        synchronized boolean canBeAssigned() {
            return !isCompleted && workerIds.size() < MAX_PARALLEL_WORKERS;
        }
    }

    public TaskPool() {
        this.availableTasks = new ConcurrentHashMap<>();
        this.assignedTasks = new ConcurrentHashMap<>();

        this.workerTasks = new ConcurrentHashMap<>();
        this.timeoutChecker = Executors.newSingleThreadScheduledExecutor();
        this.timeoutChecker.scheduleAtFixedRate(this::checkTimeouts, 5, 5, TimeUnit.SECONDS);
    }

    public void addTask(String taskId, Task task) {
        availableTasks.put(taskId, task);
    }

    public synchronized Optional<Task> getNextTask(String workerId) {
        // Initialize worker's task set if not exists
        workerTasks.putIfAbsent(workerId, ConcurrentHashMap.newKeySet());

        // First try to find a task that needs verification
        for (Map.Entry<String, TaskAssignment> entry : assignedTasks.entrySet()) {
            String taskId = entry.getKey();
            TaskAssignment assignment = entry.getValue();

            if (assignment.canBeAssigned() && !assignment.workerIds.contains(workerId)) {
                assignment.workerIds.add(workerId);
                assignment.assignmentTimes.put(workerId, System.currentTimeMillis());
                workerTasks.get(workerId).add(taskId);
                return Optional.of(assignment.originalTask);
            }
        }

        // Then look for new tasks
        for (Map.Entry<String, Task> entry : availableTasks.entrySet()) {
            String taskId = entry.getKey();
            Task task = entry.getValue();

            TaskAssignment assignment = assignedTasks.computeIfAbsent(taskId, k -> new TaskAssignment(task));

            if (!assignment.isCompleted && 
                !assignment.workerIds.contains(workerId) && 
                assignment.canBeAssigned()) {
                
                assignment.workerIds.add(workerId);
                assignment.assignmentTimes.put(workerId, System.currentTimeMillis());
                workerTasks.get(workerId).add(taskId);
                availableTasks.remove(taskId);
                
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    public synchronized void processResult(String taskId, String workerId, boolean hasNonPrime) {
        TaskAssignment assignment = assignedTasks.get(taskId);
        if (assignment != null && assignment.workerIds.contains(workerId)) {
            assignment.results.add(hasNonPrime);
            assignment.completedAssignments.incrementAndGet();
            
            // Remove task from worker's assigned tasks
            Set<String> workerTaskSet = workerTasks.get(workerId);
            if (workerTaskSet != null) {
                workerTaskSet.remove(taskId);
            }

            // If we found a non-prime number, complete immediately
            if (hasNonPrime) {
                assignment.hasNonPrime = true;
                assignment.isCompleted = true;
                return;
            }

            // Task is complete when we get at least one valid response
            assignment.isCompleted = true;
        }
    }

    public void checkTimeouts() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, TaskAssignment> entry : assignedTasks.entrySet()) {
            String taskId = entry.getKey();
            TaskAssignment assignment = entry.getValue();

            if (!assignment.isCompleted) {
                for (Map.Entry<String, Long> timeEntry : new HashMap<>(assignment.assignmentTimes).entrySet()) {
                    String workerId = timeEntry.getKey();
                    long assignmentTime = timeEntry.getValue();

                    if (currentTime - assignmentTime > TASK_TIMEOUT) {
                        System.out.println("Task " + taskId + " timed out for worker " + workerId);
                        
                        // Remove the timed out assignment
                        assignment.workerIds.remove(workerId);
                        assignment.assignmentTimes.remove(workerId);
                        assignment.failedAssignments.incrementAndGet();

                        // Remove from worker's task set
                        Set<String> workerTaskSet = workerTasks.get(workerId);
                        if (workerTaskSet != null) {
                            workerTaskSet.remove(taskId);
                        }

                        // Make task available again if it's not completed
                        if (!assignment.isCompleted) {
                            Task originalTask = assignment.originalTask;
                            if (originalTask != null) {
                                availableTasks.put(taskId, originalTask);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean hasCompletedTasks() {
        return assignedTasks.values().stream().anyMatch(a -> a.isCompleted);
    }

    public boolean hasNonPrimeResult() {
        return assignedTasks.values().stream().anyMatch(a -> a.hasNonPrime);
    }
}