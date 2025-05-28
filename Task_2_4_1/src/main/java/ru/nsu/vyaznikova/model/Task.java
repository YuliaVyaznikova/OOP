package ru.nsu.vyaznikova.model;

import java.time.LocalDateTime;

/**
 * Represents a task in the OOP course.
 */
public class Task {
    private final String id;
    private final String name;
    private final int maxScore;
    private final LocalDateTime softDeadline;
    private final LocalDateTime hardDeadline;
    private final String description;

    public Task(String id, String name, int maxScore, 
                LocalDateTime softDeadline, LocalDateTime hardDeadline, 
                String description) {
        this.id = id;
        this.name = name;
        this.maxScore = maxScore;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public LocalDateTime getSoftDeadline() {
        return softDeadline;
    }

    public LocalDateTime getHardDeadline() {
        return hardDeadline;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxScore=" + maxScore +
                ", softDeadline=" + softDeadline +
                ", hardDeadline=" + hardDeadline +
                '}';
    }
}
