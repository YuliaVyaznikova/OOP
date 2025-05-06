package ru.nsu.vyaznikova;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int[] numbers;
    private final int startIndex;
    private final int endIndex;
    private final String taskId;
    
    public Task(int[] numbers, int startIndex, int endIndex, String taskId) {
        this.numbers = Objects.requireNonNull(numbers, "Numbers array cannot be null").clone();
        this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
        
        if (startIndex < 0 || endIndex > numbers.length || startIndex > endIndex) {
            throw new IllegalArgumentException("Invalid index range");
        }
        
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public Task(int[] numbers) {
        this(numbers, 0, numbers.length, UUID.randomUUID().toString());
    }
    
    public int[] getNumbers() {
        return Arrays.copyOfRange(numbers, startIndex, endIndex);
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public int getStartIndex() {
        return startIndex;
    }
    
    public int getEndIndex() {
        return endIndex;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task task)) {
            return false;
        }
        return Objects.equals(taskId, task.taskId) &&
               startIndex == task.startIndex &&
               endIndex == task.endIndex &&
               Arrays.equals(numbers, task.numbers);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(startIndex, endIndex, taskId);
        result = 31 * result + Arrays.hashCode(numbers);
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("Task{id=%s, range=[%d, %d], numbers=%s}",
            taskId, startIndex, endIndex, 
            Arrays.toString(Arrays.copyOfRange(numbers, startIndex, endIndex)));
    }
}
