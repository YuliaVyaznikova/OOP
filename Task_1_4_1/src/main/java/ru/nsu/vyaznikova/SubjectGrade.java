package ru.nsu.vyaznikova;

/**
 * Represents a grade for a specific subject.
 */
public class SubjectGrade {
    private final String subject;
    private final int grade;
    private final GradeType type;

    /**
     * Creates a new SubjectGrade object.
     *
     * @param subject the name of the subject
     * @param grade   the grade received (from 2 to 5 for EXAM or DIFFERENTIATED_CREDIT, ignored for CREDIT)
     * @param type    the type of grade (EXAM, CREDIT, or DIFFERENTIATED_CREDIT)
     * @throws IllegalArgumentException if grade is not in the valid range for EXAM or DIFFERENTIATED_CREDIT
     */
    public SubjectGrade(String subject, int grade, GradeType type) {
        if ((type == GradeType.EXAM || type == GradeType.DIFFERENTIATED_CREDIT) && (grade < 2 || grade > 5)) {
            throw new IllegalArgumentException("Grade must be between 2 and 5 for exams or differentiated credits.");
        }
        this.subject = subject;
        this.grade = grade;
        this.type = type;
    }

    /**
     * Retrieves the grade value.
     *
     * @return the grade as an integer
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Retrieves the type of the grade.
     *
     * @return the GradeType (EXAM, CREDIT, or DIFFERENTIATED_CREDIT)
     */
    public GradeType getType() {
        return type;
    }

    /**
     * Retrieves the subject name.
     *
     * @return the name of the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns a string representation of the SubjectGrade object.
     *
     * @return a string containing subject, grade, and grade type
     */
    @Override
    public String toString() {
        return String.format("Subject: %s, Grade: %s, Type: %s",
                subject,
                type == GradeType.CREDIT ? "Pass" : grade,
                type);
    }
}