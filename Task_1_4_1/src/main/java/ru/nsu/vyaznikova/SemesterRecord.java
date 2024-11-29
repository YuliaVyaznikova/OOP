package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;

public class SemesterRecord {
    private final List<SubjectGrade> grades = new ArrayList<>();

    // Добавление оценки
    public void addGrade(String subject, int grade, GradeType type) {
        grades.add(new SubjectGrade(subject, grade, type));
    }

    // Получение списка оценок
    public List<SubjectGrade> getGrades() {
        return new ArrayList<>(grades);
    }
}
