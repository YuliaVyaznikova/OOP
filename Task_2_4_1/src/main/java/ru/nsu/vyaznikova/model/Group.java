package ru.nsu.vyaznikova.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a student group in the OOP course.
 */
public class Group {
    private final String name;
    private final List<Student> students;

    public Group(String name) {
        this.name = name;
        this.students = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
            student.setGroup(this);
        }
    }

    public void removeStudent(Student student) {
        if (student != null && students.contains(student)) {
            students.remove(student);
            student.setGroup(null);
        }
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", studentCount=" + students.size() +
                '}';
    }
}
