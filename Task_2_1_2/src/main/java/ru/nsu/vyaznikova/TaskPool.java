package ru.nsu.vyaznikova;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPool {
    private final Map<String, Task> availableTasks;
    private final Map<String, TaskAssignment> assignedTasks;
    private final int maxAssignmentsPerTask;
    private static final long TASK_TIMEOUT = 30000;

    private static class TaskAssignment {
        final Set<String> workerIds;
        final Map<String, Long> assignmentTimes;
        final AtomicInteger completedAssignments;
        volatile boolean isCompleted;
        volatile boolean hasNonPrime;
        final Task originalTask;
        TaskAssignment(Task task) {
            this.workerIds = new HashSet<>();
            this.assignmentTimes = new HashMap<>();
            this.completedAssignments = new AtomicInteger(0);
            this.isCompleted = false;
            this.hasNonPrime = false;
            this.originalTask = task;
        }
    }

    public TaskPool(int maxAssignmentsPerTask) {
        this.availableTasks = new ConcurrentHashMap<>();
        this.assignedTasks = new ConcurrentHashMap<>();
        this.maxAssignmentsPerTask = maxAssignmentsPerTask;
    }

    public void addTask(String taskId, Task task) {
        availableTasks.put(taskId, task);
    }

    public synchronized Optional<Task> getNextTask(String workerId) {
        // Check for timed out tasks first
        checkTimeouts();

        // Look for an available task that hasn't been assigned to this worker
        for (Map.Entry<String, Task> entry : availableTasks.entrySet()) {
            String taskId = entry.getKey();
            Task task = entry.getValue();

            TaskAssignment assignment = assignedTasks.computeIfAbsent(taskId, k -> new TaskAssignment(task));

            if (!assignment.isCompleted && 
                !assignment.workerIds.contains(workerId) && 
                assignment.workerIds.size() < maxAssignmentsPerTask) {
                
                assignment.workerIds.add(workerId);
                assignment.assignmentTimes.put(workerId, System.currentTimeMillis());
                availableTasks.remove(taskId);
                
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    public synchronized void processResult(String taskId, String workerId, boolean hasNonPrime) {
        TaskAssignment assignment = assignedTasks.get(taskId);
        if (assignment != null && assignment.workerIds.contains(workerId)) {
            if (hasNonPrime) {
                assignment.hasNonPrime = true;
            }
            
            int completed = assignment.completedAssignments.incrementAndGet();
            if (completed >= maxAssignmentsPerTask || assignment.hasNonPrime) {
                assignment.isCompleted = true;
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
                        // Remove the timed out assignment
                        assignment.workerIds.remove(workerId);
                        assignment.assignmentTimes.remove(workerId);

                        // If task is not completed and not fully assigned, make it available again
                        if (!assignment.isCompleted && assignment.workerIds.size() < maxAssignmentsPerTask) {
                            Task originalTask = assignedTasks.get(taskId).originalTask;
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