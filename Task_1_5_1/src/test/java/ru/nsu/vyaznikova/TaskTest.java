package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Task class.
 * These tests verify the functionality of creating and formatting Markdown task items
 * with different completion states and content elements, including nested elements
 * and various edge cases.
 */
public class TaskTest {

    /**
     * Helper class for testing Task with simple text content.
     * This class provides a predictable Markdown output for testing purposes.
     */
    private static class TestElement extends Element {
        private final String content;

        public TestElement(String content) {
            this.content = content;
        }

        @Override
        public String toMarkdown() {
            return content;
        }
    }

    /**
     * Tests the creation of a basic uncompleted task.
     * Verifies that:
     * 1. The checkbox is properly formatted with square brackets
     * 2. The checkbox contains a space for uncompleted status
     * 3. The content is correctly appended after the checkbox
     */
    @Test
    public void testBasicUncompletedTask() {
        Task task = new Task.Builder()
                .addContent(new TestElement("Write unit tests"))
                .build();

        assertEquals("- [ ] Write unit tests", task.toMarkdown(),
                "Uncompleted task should have empty checkbox");
    }

    /**
     * Tests the creation of a completed task.
     * Verifies that:
     * 1. The checkbox is properly formatted
     * 2. The checkbox contains 'x' for completed status
     * 3. The content is correctly formatted
     */
    @Test
    public void testCompletedTask() {
        Task task = new Task.Builder()
                .setCompleted(true)
                .addContent(new TestElement("Task completed"))
                .build();

        assertEquals("- [x] Task completed", task.toMarkdown(),
                "Completed task should have 'x' in checkbox");
    }

    /**
     * Tests task with multiple content elements.
     * Verifies that:
     * 1. Multiple content elements are properly space-separated
     * 2. The order of elements is preserved
     * 3. The overall formatting is correct
     */
    @Test
    public void testTaskWithMultipleContent() {
        Task task = new Task.Builder()
                .addContent(
                        new TestElement("Learn"),
                        new TestElement("Java"),
                        new TestElement("Programming")
                )
                .build();

        assertEquals("- [ ] Learn Java Programming", task.toMarkdown(),
                "Task with multiple content elements should be space-separated");
    }

    /**
     * Tests task creation with varargs content method.
     * Verifies that:
     * 1. The varargs method correctly accepts multiple elements
     * 2. Elements are properly added to the task
     * 3. The formatting is correct
     */
    @Test
    public void testTaskWithVarargsContent() {
        Element[] elements = {
                new TestElement("First"),
                new TestElement("Second"),
                new TestElement("Third")
        };

        Task task = new Task.Builder()
                .addContent(elements)
                .build();

        assertEquals("- [ ] First Second Third", task.toMarkdown(),
                "Task should accept content elements via varargs");
    }

    /**
     * Tests validation of required content.
     * Verifies that:
     * 1. Building a task without content throws IllegalStateException
     * 2. The exception message is appropriate
     */
    @Test
    public void testRequiredContent() {
        Task.Builder builder = new Task.Builder();

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> builder.build(),
                "Should throw exception when no content is added");

        assertTrue(ex.getMessage().contains("must contain at least one content element"),
                "Exception message should mention content requirement");
    }

    /**
     * Tests null validation in content methods.
     * Verifies that:
     * 1. Null single element throws NullPointerException
     * 2. Null varargs array throws NullPointerException
     * 3. Null element within varargs throws NullPointerException
     * 4. Exception messages are appropriate
     */
    @Test
    public void testNullValidation() {
        Task.Builder builder = new Task.Builder();

        // Test null single element
        assertThrows(NullPointerException.class,
                () -> builder.addContent((Element) null),
                "Should throw exception when adding null element");

        // Test null varargs array
        assertThrows(NullPointerException.class,
                () -> builder.addContent((Element[]) null),
                "Should throw exception when adding null array");

        // Test null element within varargs
        Element[] elementsWithNull = {
                new TestElement("Valid"),
                null,
                new TestElement("Also Valid")
        };
        assertThrows(NullPointerException.class,
                () -> builder.addContent(elementsWithNull),
                "Should throw exception when array contains null element");
    }

    /**
     * Tests task with complex nested elements.
     * Verifies that:
     * 1. Nested elements are properly formatted
     * 2. The structure of nested elements is preserved
     * 3. The overall task formatting is correct
     */
    @Test
    public void testTaskWithNestedElements() {
        // Create a task with bold and italic elements
        Task task = new Task.Builder()
                .addContent(
                        new TestElement("Complete"),
                        new TestElement("**important**"),
                        new TestElement("task"),
                        new TestElement("*urgently*")
                )
                .setCompleted(true)
                .build();

        assertEquals("- [x] Complete **important** task *urgently*",
                task.toMarkdown(),
                "Task should properly format nested Markdown elements");
    }

    /**
     * Tests toggling task completion status.
     * Verifies that:
     * 1. Default status is uncompleted
     * 2. Status can be set to completed
     * 3. Status can be changed multiple times
     */
    @Test
    public void testCompletionToggle() {
        Task.Builder builder = new Task.Builder()
                .addContent(new TestElement("Toggle task"));

        // Test default status (uncompleted)
        Task defaultTask = builder.build();
        assertEquals("- [ ] Toggle task", defaultTask.toMarkdown(),
                "Task should be uncompleted by default");

        // Test completed status
        Task completedTask = builder.setCompleted(true).build();
        assertEquals("- [x] Toggle task", completedTask.toMarkdown(),
                "Task should show as completed when set to true");

        // Test switching back to uncompleted
        Task uncompletedTask = builder.setCompleted(false).build();
        assertEquals("- [ ] Toggle task", uncompletedTask.toMarkdown(),
                "Task should show as uncompleted when set back to false");
    }
}
