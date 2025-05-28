package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        
        // First worker gets the task
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        
        // Second worker gets the task
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent());
        
        // First worker confirms all prime
        taskPool.processResult(taskId, "worker1", false);
        
        // Task should be completed after first response
        assertTrue(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
        
        // Third worker shouldn't get the task as it's completed
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        assertFalse(result3.isPresent());
        
        // Second worker's response shouldn't change the result
        taskPool.processResult(taskId, "worker2", false);
        assertTrue(taskPool.hasCompletedTasks());
        assertFalse(taskPool.hasNonPrimeResult());
    }
    

    
    @Test
    void testTaskReassignment() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent());
        
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent());
        
        // Third worker should not get the task
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        assertFalse(result3.isPresent());
        
        // First worker returns non-prime result
        taskPool.processResult(taskId, "worker1", true);
        
        // Task should be completed and marked as having non-prime after first result
        assertTrue(taskPool.hasCompletedTasks());
        assertTrue(taskPool.hasNonPrimeResult());
        
        // Second worker's result doesn't matter
        taskPool.processResult(taskId, "worker2", false);
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
    void testTwoWorkersRequired() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // First worker gets task
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        assertTrue(result1.isPresent(), "First worker should get the task");
        
        // Second worker gets task
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result2.isPresent(), "Second worker should get the task");
        
        // Third worker should not get task
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        assertFalse(result3.isPresent(), "Third worker should not get the task");
    }
    
    @Test
    void testIsTaskCompleted() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // Initially task is not completed
        assertFalse(taskPool.isTaskCompleted(taskId));
        
        // Assign task to two workers
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        
        // Task is still not completed
        assertFalse(taskPool.isTaskCompleted(taskId));
        
        // First worker returns result
        taskPool.processResult(taskId, "worker1", true);
        
        // Now task is completed
        assertTrue(taskPool.isTaskCompleted(taskId));
        
        // Second worker can check status and stop processing
        assertTrue(taskPool.isTaskCompleted(taskId));
    }

    @Test
    void testBothWorkersTimeout() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(new int[]{2, 3, 5});
        taskPool.addTask(taskId, task);
        
        // Assign task to two workers
        Optional<Task> result1 = taskPool.getNextTask("worker1");
        Optional<Task> result2 = taskPool.getNextTask("worker2");
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        
        // Modify assignment times to simulate timeout
        TaskPool.TaskAssignment assignment = taskPool.getAssignmentForTest(taskId);
        if (assignment != null) {
            long timeoutTime = System.currentTimeMillis() - TaskPool.TASK_TIMEOUT - 1000;
            assignment.assignmentTimes.put("worker1", timeoutTime);
            assignment.assignmentTimes.put("worker2", timeoutTime);
        }
        
        // Check timeouts - should return task to pool
        taskPool.checkTimeouts();
        
        // Task should return to pool and be available for new workers
        Optional<Task> result3 = taskPool.getNextTask("worker3");
        Optional<Task> result4 = taskPool.getNextTask("worker4");
        assertTrue(result3.isPresent(), "Task should be reassigned to new worker after timeout");
        assertTrue(result4.isPresent(), "Task should be reassigned to second new worker");
        
        // One of the new workers completes the task
        taskPool.processResult(taskId, "worker3", false);
        assertTrue(taskPool.hasCompletedTasks(), "Task should be completed");
        assertFalse(taskPool.hasNonPrimeResult(), "Task should not have non-prime result");
        
        // Second new worker's result doesn't change the outcome
        taskPool.processResult(taskId, "worker4", false);
        assertTrue(taskPool.hasCompletedTasks(), "Task should remain completed");
        assertFalse(taskPool.hasNonPrimeResult(), "Task should still not have non-prime result");
    }
    

}
