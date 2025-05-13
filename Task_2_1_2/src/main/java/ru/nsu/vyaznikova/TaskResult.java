package ru.nsu.vyaznikova;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the result of a task execution in the distributed system.
 * Contains information about the task ID and whether non-prime numbers were found.
 * This class is serializable to support network transmission.
 */
public class TaskResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String taskId;
    private final boolean hasNonPrime;
    
    public TaskResult(String taskId, boolean hasNonPrime) {
        this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
        this.hasNonPrime = hasNonPrime;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public boolean hasNonPrime() {
        return hasNonPrime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskResult result)) {
            return false;
        }
        return hasNonPrime == result.hasNonPrime
               && Objects.equals(taskId, result.taskId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taskId, hasNonPrime);
    }
    
    @Override
    public String toString() {
        return "TaskResult{taskId='" + taskId + "', hasNonPrime=" + hasNonPrime + '}';
    }
}