package ru.nsu.vyaznikova;

import java.util.*;
import java.util.stream.Collectors;

public class StudentRecordBook {
    private String studentName;
    private boolean isTuitionBased; // true - платное, false - бюджетное
    private final List<SemesterRecord> semesters = new ArrayList<>();
    private boolean isThesisCompleted; // true - защита диплома выполнена
    private int thesisGrade; // оценка за диплом

    public StudentRecordBook(String studentName, boolean isTuitionBased) {
        this.studentName = studentName;
        this.isTuitionBased = isTuitionBased;
        this.isThesisCompleted = false; // изначально диплом не защищен
    }

    public void addSemester(SemesterRecord semester) {
        semesters.add(semester);
    }

    public void setThesisGrade(int grade) {
        this.isThesisCompleted = true;
        this.thesisGrade = grade;
    }

    // 1. Текущий средний балл
    public double calculateGPA() {
        return semesters.stream()
                .flatMap(semester -> semester.getGrades().stream())
                .mapToInt(SubjectGrade::getGrade)
                .average()
                .orElse(0.0);
    }

    // 2. Возможность перевода на бюджет
    public boolean canTransferToBudget() {
        if (!isTuitionBased) return false;
        if (semesters.size() < 2) return false;

        return semesters.stream()
                .skip(Math.max(0, semesters.size() - 2))
                .flatMap(semester -> semester.getGrades().stream())
                .noneMatch(grade -> grade.getGrade() == 3 && grade.getType() == GradeType.EXAM);
    }

    // 3. Возможность получения красного диплома
    public boolean canGetHonorsDiploma() {
        if (!isThesisCompleted || thesisGrade < 5) return false;

        List<SubjectGrade> allGrades = semesters.stream()
                .flatMap(semester -> semester.getGrades().stream())
                .collect(Collectors.toList());

        long excellentCount = allGrades.stream().filter(grade -> grade.getGrade() == 5).count();
        long totalCount = allGrades.size();

        boolean hasNoSatisfactory = allGrades.stream().noneMatch(grade -> grade.getGrade() == 3);

        return hasNoSatisfactory && totalCount > 0 && (double) excellentCount / totalCount >= 0.75;
    }

    // 4. Возможность получения повышенной стипендии
    public boolean canGetIncreasedScholarship() {
        if (semesters.isEmpty()) return false;

        return semesters.get(semesters.size() - 1).getGrades().stream()
                .allMatch(grade -> grade.getGrade() >= 4);
    }
}
