package ru.nsu.vyaznikova;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a message in the distributed system's communication protocol.
 * Messages are serializable for network transmission and contain a type,
 * content payload, and timestamp for timeout tracking.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Defines the types of messages that can be exchanged between nodes.
     * Each type represents a different kind of communication or request.
     */
    public enum MessageType {
        TASK_REQUEST,   // Worker requests a task
        TASK,           // Server sends a task
        RESULT,         // Worker sends result
        NO_TASKS,       // Server has no tasks available
        HEARTBEAT,      // Heartbeat message
        ERROR           // Error message
    }
    
    private final MessageType type;
    private final Object content;
    private final long timestamp;
    
    /**
     * Creates a new message with the specified type and content.
     * The message's timestamp is automatically set to the current time.
     *
     * @param type type of the message
     * @param content content/payload of the message
     * @throws NullPointerException if type or content is null
     */
    public Message(MessageType type, Object content) {
        this.type = Objects.requireNonNull(type, "Message type cannot be null");
        this.content = Objects.requireNonNull(content, "Message content cannot be null");
        this.timestamp = System.currentTimeMillis();
    }
    
    public MessageType getType() {
        return type;
    }
    
    public Object getContent() {
        return content;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public boolean isExpired(long timeoutMs) {
        return System.currentTimeMillis() - timestamp > timeoutMs;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message message)) {
            return false;
        }
        return timestamp == message.timestamp
               && type == message.type
               && Objects.equals(content, message.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, content, timestamp);
    }
    
    @Override
    public String toString() {
        return String.format("Message{type=%s, content=%s, timestamp=%d}", 
            type, content, timestamp);
    }
}