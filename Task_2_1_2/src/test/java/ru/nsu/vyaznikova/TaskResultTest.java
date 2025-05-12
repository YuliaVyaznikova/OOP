package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskResultTest {
    @Test
    void testTaskResultCreation() {
        TaskResult result = new TaskResult("task1", true);
        assertEquals("task1", result.getTaskId());
        assertTrue(result.hasNonPrime());

        TaskResult result2 = new TaskResult("task2", false);
        assertEquals("task2", result2.getTaskId());
        assertFalse(result2.hasNonPrime());
    }

    @Test
    void testTaskResultEquality() {
        TaskResult result1 = new TaskResult("task1", true);
        TaskResult result2 = new TaskResult("task1", true);
        TaskResult result3 = new TaskResult("task1", false);
        TaskResult result4 = new TaskResult("task2", true);

        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
        assertNotEquals(result1, result3);
        assertNotEquals(result1, result4);
        assertNotEquals(result1, null);
        assertNotEquals(result1, "not a task result");
    }

    @Test
    void testToString() {
        TaskResult result = new TaskResult("task1", true);
        String toString = result.toString();
        assertTrue(toString.contains("task1"));
        assertTrue(toString.contains("true"));
    }

    @Test
    void testNullTaskId() {
        assertThrows(NullPointerException.class, () -> new TaskResult(null, true));
    }
}
