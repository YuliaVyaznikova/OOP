package ru.nsu.vyaznikova;

public class SubjectGrade {
    private final String subject;
    private final int grade;
    private final GradeType type;

    public SubjectGrade(String subject, int grade, GradeType type) {
        this.subject = subject;
        this.grade = grade;
        this.type = type;
    }

    public int getGrade() {
        return grade;
    }

    public GradeType getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }
}
