package ru.nsu.vyaznikova;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskPoolTest {
    private TaskPool taskPool;

    @BeforeEach
    void setUp() {
        taskPool = new TaskPool();
    }

    @Test
    void testAddAndGetTask() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{1, 2, 3});
        taskPool.addTask(taskId, task);
        
        Optional<Task> result = taskPool.getNextTask("worker1");
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }
    
    @Test
    void testGetNonExistentTask() {
        Optional<Task> result = taskPool.getNextTask("worker1");
        assertFalse(result.isPresent());
    }
    
    
    @Test
    void testProcessResultNonPrime() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{4, 5, 6});
        taskPool.addTask(taskId, task);
        
        taskPool.getNextTask("worker1");
        taskPool.processResult(taskId, "worker1", true);
        
        assertTrue(taskPool.hasCompletedTasks());
        assertTrue(taskPool.hasNonPrimeResult());
    }
    
    @Test
    void testProcessResultAllPrime() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // First worker confirms all prime
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        taskPool.processResult(taskId, "worker1", false);
        
        // Task should be completed after first response
        assertTrue(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
        
        // Second worker shouldn't get the completed task
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertFalse(result2.isPresent());
    }
    

    
    @Test
    void testTaskReassignment() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // First three workers get the task
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent());
        
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        assertTrue(result3.isPresent());
        
        // All workers return different results
        taskPool.processResult(taskId, "worker1", false);
        taskPool.processResult(taskId, "worker2", true);
        taskPool.processResult(taskId, "worker3", false);
        
        // Task should be completed due to non-prime result
        assertTrue(taskPool.hasCompletedTasks());
        assertTrue(taskPool.hasNonPrimeResult());
    }
    
    @Test
    void testInvalidWorkerResult() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // Process result from worker that wasn't assigned the task
        taskPool.processResult(taskId, "worker1", true);
        
        assertFalse(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
    }
    
    @Test
    void testInvalidTaskResult() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // Process result for non-existent task
        taskPool.processResult("invalid-task", "worker1", false);
        
        assertFalse(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
    }

    @Test
    void testWorkerFailure() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // First worker gets task but fails (no response)
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        
        // Simulate timeout check
        taskPool.checkTimeouts();
        
        // Second worker should be able to get the task
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent());
        taskPool.processResult(taskId, "worker2", false);
        
        // Task should be completed after first successful response
        assertTrue(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
        
        // Third worker shouldn't get the completed task
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        assertFalse(result3.isPresent());
    }
    

}
