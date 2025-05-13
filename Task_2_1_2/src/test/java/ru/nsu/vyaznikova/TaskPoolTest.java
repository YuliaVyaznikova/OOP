package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

class TaskPoolTest {
    private TaskPool taskPool;
    private static final int MAX_ASSIGNMENTS = 2;
    
    @BeforeEach
    void setUp() {
        taskPool = new TaskPool(MAX_ASSIGNMENTS);
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
    void testMaxAssignments() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{1, 2, 3});
        taskPool.addTask(taskId, task);
        
        // First assignment
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        taskPool.processResult(taskId, "worker1", false);
        
        // Second assignment
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent());
        taskPool.processResult(taskId, "worker2", false);
        
        // Third assignment should fail
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        assertFalse(result3.isPresent());
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
        
        taskPool.getNextTask("worker1");
        taskPool.processResult(taskId, "worker1", false);
        
        // Need second worker to confirm
        taskPool.getNextTask("worker2");
        taskPool.processResult(taskId, "worker2", false);
        
        assertTrue(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
    }
    

    
    @Test
    void testTaskReassignment() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // First worker gets task but doesn't complete it
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        
        // Second worker should be able to get the same task
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent());
        assertEquals(result1.get(), result2.get());
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
        // Process result for non-existent task
        taskPool.processResult("invalid-task", "worker1", true);
        
        assertFalse(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
    }
}
