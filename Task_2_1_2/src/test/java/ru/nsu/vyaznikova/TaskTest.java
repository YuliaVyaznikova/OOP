package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskTest {
    @Test
    void testConstructorWithValidInput() {
        int[] numbers = {1, 2, 3, 4, 5};
        String taskId = "test-task";
        Task task = new Task(numbers, 1, 4, taskId);
        
        assertArrayEquals(new int[]{2, 3, 4}, task.getNumbers());
        assertEquals(taskId, task.getTaskId());
        assertEquals(1, task.getStartIndex());
        assertEquals(4, task.getEndIndex());
    }

    @Test
    void testConstructorWithNullNumbers() {
        assertThrows(NullPointerException.class, () -> 
            new Task(null, 0, 1, "test-task"));
    }

    @Test
    void testConstructorWithNullTaskId() {
        assertThrows(NullPointerException.class, () -> 
            new Task(new int[]{1, 2, 3}, 0, 3, null));
    }

    @Test
    void testConstructorWithInvalidRange() {
        int[] numbers = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> 
            new Task(numbers, -1, 2, "test-task")); // negative start
        assertThrows(IllegalArgumentException.class, () -> 
            new Task(numbers, 0, 4, "test-task")); // end > length
        assertThrows(IllegalArgumentException.class, () -> 
            new Task(numbers, 2, 1, "test-task")); // start > end
    }

    @Test
    void testDefaultConstructor() {
        int[] numbers = {1, 2, 3};
        Task task = new Task(numbers);
        
        assertArrayEquals(numbers, task.getNumbers());
        assertNotNull(task.getTaskId());
        assertEquals(0, task.getStartIndex());
        assertEquals(3, task.getEndIndex());
    }

    @Test
    void testEquals() {
        int[] numbers = {1, 2, 3, 4, 5};
        String taskId = "test-task";
        final Task task1 = new Task(numbers, 1, 4, taskId);
        final Task task2 = new Task(numbers.clone(), 1, 4, taskId);
        final Task task3 = new Task(numbers, 1, 3, taskId);
        final Task task4 = new Task(numbers, 1, 4, "different-id");
        
        assertEquals(task1, task1); // same object
        assertEquals(task1, task2); // equal objects
        assertNotEquals(task1, task3); // different endIndex
        assertNotEquals(task1, task4); // different taskId
        
        final Task task5 = new Task(new int[]{1, 2, 3}, 1, 3, taskId);
        assertNotEquals(task1, task5); // different numbers
        assertNotEquals(task1, null); // null
        assertNotEquals(task1, new Object()); // different type
    }

    @Test
    void testHashCode() {
        int[] numbers = {1, 2, 3};
        String taskId = "test-task";
        Task task1 = new Task(numbers, 0, 3, taskId);
        Task task2 = new Task(numbers.clone(), 0, 3, taskId);
        
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testToString() {
        int[] numbers = {1, 2, 3, 4, 5};
        String taskId = "test-task";
        Task task = new Task(numbers, 1, 4, taskId);
        
        String expected = "Task{id=test-task, range=[1, 4], numbers=[2, 3, 4]}";
        assertEquals(expected, task.toString());
    }
}
