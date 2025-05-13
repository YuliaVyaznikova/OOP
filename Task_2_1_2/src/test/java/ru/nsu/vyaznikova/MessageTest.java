package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MessageTest {
    @Test
    void testMessageCreation() {
        Message message = new Message(
            Message.MessageType.TASK_REQUEST,
            "test content"
        );
        assertEquals(Message.MessageType.TASK_REQUEST, message.getType());
        assertEquals("test content", message.getContent());
    }

    @Test
    void testMessageEquality() {
        final Message message1 = new Message(Message.MessageType.TASK_REQUEST, "test");
        final Message message2 = new Message(Message.MessageType.TASK_REQUEST, "test");
        final Message message3 = new Message(Message.MessageType.RESULT, "test");
        final Message message4 = new Message(Message.MessageType.TASK_REQUEST, "different");

        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
        assertNotEquals(message1, message3);
        assertNotEquals(message1, message4);
        assertNotEquals(message1, null);
        assertNotEquals(message1, "not a message");
    }

    @Test
    void testAllMessageTypes() {
        Message.MessageType[] types = Message.MessageType.values();
        for (Message.MessageType type : types) {
            Message message = new Message(type, "test");
            assertEquals(type, message.getType());
        }
    }

    @Test
    void testToString() {
        Message message = new Message(Message.MessageType.TASK_REQUEST, "test content");
        String toString = message.toString();
        assertTrue(toString.contains("TASK_REQUEST"));
        assertTrue(toString.contains("test content"));
    }

    @Test
    void testNullContent() {
        assertThrows(NullPointerException.class, () -> new Message(Message.MessageType.TASK_REQUEST, null));
    }

    @Test
    void testNullType() {
        assertThrows(NullPointerException.class, () -> new Message(null, "content"));
    }
}
