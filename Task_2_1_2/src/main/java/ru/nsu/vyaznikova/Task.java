package ru.nsu.vyaznikova;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int[] numbers;
    private final int startIndex;
    private final int endIndex;
    private final String taskId;
    
    public Task(int[] numbers, int startIndex, int endIndex, String taskId) {
        this.numbers = Objects.requireNonNull(numbers, "Numbers array cannot be null");
        this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
        
        if (startIndex < 0 || endIndex > numbers.length || startIndex > endIndex) {
            throw new IllegalArgumentException("Invalid index range");
        }
        
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return startIndex == task.startIndex &&
               endIndex == task.endIndex &&
               Arrays.equals(numbers, task.numbers) &&
               Objects.equals(taskId, task.taskId);
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
