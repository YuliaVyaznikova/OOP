package ru.nsu.vyaznikova.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a checkpoint (control point) in the OOP course.
 */
public class Checkpoint {
    private final String name;
    private final LocalDateTime date;
    private final List<Task> includedTasks;

    public Checkpoint(String name, LocalDateTime date) {
        this.name = name;
        this.date = date;
        this.includedTasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Task> getIncludedTasks() {
        return Collections.unmodifiableList(includedTasks);
    }

    public void addTask(Task task) {
        if (task != null && !includedTasks.contains(task)) {
            includedTasks.add(task);
        }
    }

    public void removeTask(Task task) {
        includedTasks.remove(task);
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", taskCount=" + includedTasks.size() +
                '}';
    }
}
