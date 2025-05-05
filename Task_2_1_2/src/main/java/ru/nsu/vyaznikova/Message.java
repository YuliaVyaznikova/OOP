package ru.nsu.vyaznikova;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum MessageType {
        TASK,
        RESULT,
        HEARTBEAT,
        ERROR
    }
    
    private final MessageType type;
    private final String content;
    private final long timestamp;
    
    public Message(MessageType type, String content) {
        this.type = Objects.requireNonNull(type, "Message type cannot be null");
        this.content = Objects.requireNonNull(content, "Message content cannot be null");
        this.timestamp = System.currentTimeMillis();
    }
    
    public MessageType getType() {
        return type;
    }
    
    public String getContent() {
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
        return timestamp == message.timestamp &&
               type == message.type &&
               Objects.equals(content, message.content);
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
