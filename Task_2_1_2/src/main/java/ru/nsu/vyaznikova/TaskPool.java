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


/**
 * Manages a pool of tasks for distributed prime number checking.
 * Initially contains N unfinished tasks that need to be processed by N * 2 workers.
 *
 * Task Assignment:
 * - Each unfinished task is assigned to two available workers
 * - System tracks which workers are processing each task through TaskAssignment
 * - While a task is assigned to two workers, other workers cannot get it
 *
 * Task Processing:
 * - Both workers independently check numbers in the task for primality
 * - As soon as any worker completes the check and returns a result, the task is marked as completed
 * - Second worker can periodically check via isTaskCompleted if the task is already done
 * - If task is completed by first worker, second worker can stop processing as its result is not needed
 *
 * Timeout Handling:
 * - System monitors task execution time for each worker
 * - If a worker doesn't respond within the timeout period, it's considered dead
 * - If both workers die:
 *   1. Task is marked as unfinished
 *   2. Old worker information is cleared
 *   3. Task returns to the pool
 *   4. Task can be assigned to a new pair of workers
 */
public class TaskPool {
    private final Map<String, Task> availableTasks;
    final ConcurrentMap<String, TaskAssignment> assignedTasks;

    static final long TASK_TIMEOUT = 30000; // 30 seconds
    // Each task must be processed by exactly two workers
    private static final int REQUIRED_WORKERS = 2;
    private final ScheduledExecutorService timeoutChecker;
    // Maps worker to their assigned tasks
    private final ConcurrentMap<String, Set<String>> workerTasks;

    static class TaskAssignment {
        final Set<String> workerIds;           // Current workers processing the task
        // Assignment time for each worker
        final Map<String, Long> assignmentTimes;
        // Task is completed (result received from any worker)
        volatile boolean isCompleted;
        volatile boolean hasNonPrime;          // Non-prime number found
        final Task originalTask;               // Original task

        TaskAssignment(Task task) {
            this.workerIds = ConcurrentHashMap.newKeySet();
            this.assignmentTimes = new ConcurrentHashMap<>();
            this.isCompleted = false;
            this.hasNonPrime = false;
            this.originalTask = task;
        }

        synchronized boolean canBeAssigned() {
            // Task can be assigned only if:
            // 1. It's not completed yet
            // 2. It has less than 2 workers
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
            // Если задача уже выполнена, просто очищаем информацию о воркере
            if (assignment.isCompleted) {
                assignment.workerIds.remove(workerId);
                assignment.assignmentTimes.remove(workerId);
                Set<String> workerTaskSet = workerTasks.get(workerId);
                if (workerTaskSet != null) {
                    workerTaskSet.remove(taskId);
                }
                return;
            }
            
            // Помечаем задачу как выполненную
            assignment.isCompleted = true;
            
            // Если найдено составное число, отмечаем это
            if (hasNonPrime) {
                assignment.hasNonPrime = true;
            }
            
            // Clean up worker information
            for (String currentWorkerId : assignment.workerIds) {
                Set<String> workerTaskSet = workerTasks.get(currentWorkerId);
                if (workerTaskSet != null) {
                    workerTaskSet.remove(taskId);
                }
            }
            assignment.workerIds.clear();
            assignment.assignmentTimes.clear();
        }
    }

    /**
     * Checks for and handles timed out task assignments.
     * Removes timed out assignments and makes tasks available for reassignment.
     */
    // For testing purposes only
    TaskAssignment getAssignmentForTest(String taskId) {
        return assignedTasks.get(taskId);
    }

    /**
     * Checks if the task is completed.
     *
     * @param taskId Task ID
     * @return true if the task is already completed, false otherwise
     */
    public boolean isTaskCompleted(String taskId) {
        TaskAssignment assignment = assignedTasks.get(taskId);
        return assignment != null && assignment.isCompleted;
    }

    /**
     * Checks for timed out tasks and handles their reassignment.
     * If both workers for a task have timed out, the task is returned to the available pool.
     */
    public void checkTimeouts() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, TaskAssignment> entry : assignedTasks.entrySet()) {
            String taskId = entry.getKey();
            TaskAssignment assignment = entry.getValue();

            if (!assignment.isCompleted) {
                // Проверяем таймауты для всех воркеров
                Map<String, Long> times = new HashMap<>(assignment.assignmentTimes);
                boolean allWorkersFailed = true;
                
                for (Map.Entry<String, Long> timeEntry : times.entrySet()) {
                    String workerId = timeEntry.getKey();
                    long assignmentTime = timeEntry.getValue();

                    if (currentTime - assignmentTime > TASK_TIMEOUT) {
                        // Удаляем воркера с таймаутом
                        assignment.workerIds.remove(workerId);
                        assignment.assignmentTimes.remove(workerId);
                        
                        // Очищаем задачу из списка задач воркера
                        Set<String> workerTaskSet = workerTasks.get(workerId);
                        if (workerTaskSet != null) {
                            workerTaskSet.remove(taskId);
                        }
                    } else {
                        allWorkersFailed = false;
                    }
                }

                // Если все воркеры умерли, возвращаем задачу в пул
                if (allWorkersFailed) {
                    assignment.workerIds.clear();
                    assignment.assignmentTimes.clear();
                    availableTasks.put(taskId, assignment.originalTask);
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