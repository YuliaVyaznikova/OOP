package ru.nsu.vyaznikova;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages a pool of tasks for distributed prime number checking.
 * Handles task assignment, result processing, and timeout management for multiple workers.
 * Provides fault tolerance through task reassignment and parallel processing.
 */
public class TaskPool {
    private final Map<String, Task> availableTasks;
    private final Map<String, TaskAssignment> assignedTasks;

    private static final long TASK_TIMEOUT = 30000; // 30 seconds
    private static final int REQUIRED_WORKERS = 2; // Each task must be processed by exactly two workers
    private final ScheduledExecutorService timeoutChecker;
    // Maps worker to their assigned tasks
    private final ConcurrentMap<String, Set<String>> workerTasks;

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
            return !isCompleted && workerIds.size() < REQUIRED_WORKERS;
        }
    }

    /**
     * Creates a new task pool with concurrent data structures and a timeout checker.
     * Initializes the task management system and starts the timeout monitoring service.
     */
    public TaskPool() {
        this.availableTasks = new ConcurrentHashMap<>();
        this.assignedTasks = new ConcurrentHashMap<>();

        this.workerTasks = new ConcurrentHashMap<>();
        this.timeoutChecker = Executors.newSingleThreadScheduledExecutor();
        this.timeoutChecker.scheduleAtFixedRate(this::checkTimeouts, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Adds a new task to the pool.
     *
     * @param taskId unique identifier for the task
     * @param task the task to be added
     */
    public void addTask(String taskId, Task task) {
        availableTasks.put(taskId, task);
    }

    /**
     * Gets the next available task for a worker.
     * First tries to find a task that needs verification, then looks for new tasks.
     *
     * @param workerId unique identifier of the requesting worker
     * @return Optional containing the next task, or empty if no task is available
     */
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

            TaskAssignment assignment = assignedTasks.computeIfAbsent(taskId, 
                    k -> new TaskAssignment(task));

            if (!assignment.isCompleted 
                && !assignment.workerIds.contains(workerId) 
                && assignment.canBeAssigned()) {
                
                assignment.workerIds.add(workerId);
                assignment.assignmentTimes.put(workerId, System.currentTimeMillis());
                workerTasks.get(workerId).add(taskId);
                availableTasks.remove(taskId);
                
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    /**
     * Processes a task result from a worker.
     *
     * @param taskId unique identifier of the completed task
     * @param workerId identifier of the worker that completed the task
     * @param hasNonPrime true if a non-prime number was found in the task range
     */
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

            // If both workers have reported their results
            if (assignment.completedAssignments.get() == REQUIRED_WORKERS) {
                // If any worker found a non-prime number
                if (assignment.results.contains(true)) {
                    assignment.hasNonPrime = true;
                }
                assignment.isCompleted = true;
                return;
            }

            // If we found a non-prime number and at least one worker has completed
            if (hasNonPrime) {
                assignment.hasNonPrime = true;
                // We can mark as complete if at least one worker found a non-prime
                assignment.isCompleted = true;
            }
        }
    }

    /**
     * Checks for and handles timed out task assignments.
     * Removes timed out assignments and makes tasks available for reassignment.
     */
    public void checkTimeouts() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, TaskAssignment> entry : assignedTasks.entrySet()) {
            String taskId = entry.getKey();
            TaskAssignment assignment = entry.getValue();

            if (!assignment.isCompleted) {
                // Create a copy of assignmentTimes to avoid ConcurrentModificationException
                Map<String, Long> times = new HashMap<>(assignment.assignmentTimes);
                for (Map.Entry<String, Long> timeEntry : times.entrySet()) {
                    String workerId = timeEntry.getKey();
                    long assignmentTime = timeEntry.getValue();

                    if (currentTime - assignmentTime > TASK_TIMEOUT) {
                        System.out.println("Task " + taskId 
                                + " timed out for worker " + workerId);
                        
                        // Remove the timed out assignment
                        assignment.workerIds.remove(workerId);
                        assignment.assignmentTimes.remove(workerId);
                        assignment.failedAssignments.incrementAndGet();

                        // Remove from worker's task set
                        Set<String> workerTaskSet = workerTasks.get(workerId);
                        if (workerTaskSet != null) {
                            workerTaskSet.remove(taskId);
                        }

                        // If both workers have failed or task is not completed
                        boolean bothWorkersFailed = assignment.failedAssignments.get() >= REQUIRED_WORKERS;
                        boolean oneFailedOneCompleted = assignment.completedAssignments.get() 
                            + assignment.failedAssignments.get() == REQUIRED_WORKERS 
                            && !assignment.isCompleted;
                        
                        if (bothWorkersFailed || oneFailedOneCompleted) {
                            // Reset the assignment and make task available again
                            assignment.workerIds.clear();
                            assignment.assignmentTimes.clear();
                            assignment.completedAssignments.set(0);
                            assignment.failedAssignments.set(0);
                            assignment.results.clear();
                            
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