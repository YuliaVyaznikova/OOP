package ru.nsu.fit.model;

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

    /**
     * Adds a student to the group and sets up bidirectional relationship.
     *
     * @param student Student to add to the group
     */
    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
            student.setGroup(this);
        }
    }

    /**
     * Removes a student from the group.
     *
     * @param student Student to remove from the group
     */
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
