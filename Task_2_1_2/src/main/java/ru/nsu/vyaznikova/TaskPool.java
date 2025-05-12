package ru.nsu.vyaznikova;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPool {
    private final Map<String, Task> availableTasks;
    private final Map<String, TaskAssignment> assignedTasks;
    private final int maxAssignmentsPerTask;
    private static final long TASK_TIMEOUT = 30000; // 30 seconds
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

        synchronized boolean shouldReassign(int maxAssigns) {
            return !isCompleted && 
                   completedAssignments.get() + failedAssignments.get() < maxAssigns;
        }
    }

    public TaskPool(int maxAssignmentsPerTask) {
        this.availableTasks = new ConcurrentHashMap<>();
        this.assignedTasks = new ConcurrentHashMap<>();
        this.maxAssignmentsPerTask = maxAssignmentsPerTask;
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

            if (assignment.shouldReassign(maxAssignmentsPerTask) && !assignment.workerIds.contains(workerId)) {
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
                assignment.workerIds.size() < maxAssignmentsPerTask) {
                
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
            int completed = assignment.completedAssignments.incrementAndGet();
            
            // Remove task from worker's assigned tasks
            Set<String> workerTaskSet = workerTasks.get(workerId);
            if (workerTaskSet != null) {
                workerTaskSet.remove(taskId);
            }

            // If we have enough matching results or found a composite number
            if (hasNonPrime || completed >= maxAssignmentsPerTask) {
                if (hasNonPrime) {
                    assignment.hasNonPrime = true;
                } else if (completed >= maxAssignmentsPerTask) {
                    // If all results agree, mark as complete
                    boolean allAgree = assignment.results.size() == 1;
                    if (allAgree) {
                        assignment.isCompleted = true;
                    } else {
                        // Results don't agree, need one more verification
                        assignment.failedAssignments.incrementAndGet();
                    }
                }
            }
        }
    }

    private void checkTimeouts() {
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

                        // If task is not completed and not fully assigned, make it available again
                        if (!assignment.isCompleted && 
                            assignment.completedAssignments.get() + assignment.failedAssignments.get() < maxAssignmentsPerTask) {
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