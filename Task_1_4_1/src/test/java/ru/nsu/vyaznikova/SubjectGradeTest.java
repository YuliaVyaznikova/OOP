package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SubjectGrade class.
 */
class SubjectGradeTest {

    /**
     * Tests the successful creation of a SubjectGrade object with valid inputs.
     */
    @Test
    void testValidSubjectGradeCreation() {
        SubjectGrade grade = new SubjectGrade("Mathematics", 5, GradeType.EXAM);
        assertEquals("Mathematics", grade.getSubject());
        assertEquals(5, grade.getGrade());
        assertEquals(GradeType.EXAM, grade.getType());
    }

    /**
     * Tests that the toString method produces the correct output for EXAM type grades.
     */
    @Test
    void testToStringForExamGrade() {
        SubjectGrade grade = new SubjectGrade("Mathematics", 5, GradeType.EXAM);
        assertEquals("Subject: Mathematics, Grade: 5, Type: EXAM", grade.toString());
    }

    /**
     * Tests that the toString method produces the correct output for CREDIT type grades.
     */
    @Test
    void testToStringForCreditGrade() {
        SubjectGrade grade = new SubjectGrade("History", 0, GradeType.CREDIT);
        assertEquals("Subject: History, Grade: Pass, Type: CREDIT", grade.toString());
    }

    /**
     * Tests that creating a SubjectGrade with an invalid grade (below 2) throws an exception.
     */
    @Test
    void testInvalidGradeBelowMinimum() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubjectGrade("Physics", 1, GradeType.EXAM),
                "Grade must be between 2 and 5 for exams or differentiated credits.");
    }

    /**
     * Tests that creating a SubjectGrade with an invalid grade (above 5) throws an exception.
     */
    @Test
    void testInvalidGradeAboveMaximum() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubjectGrade("Chemistry", 6, GradeType.DIFFERENTIATED_CREDIT),
                "Grade must be between 2 and 5 for exams or differentiated credits.");
    }

    /**
     * Tests that creating a SubjectGrade with a CREDIT type allows any grade value.
     */
    @Test
    void testValidGradeForCreditType() {
        SubjectGrade grade = new SubjectGrade("Physical Education", 0, GradeType.CREDIT);
        assertEquals(0, grade.getGrade());
        assertEquals(GradeType.CREDIT, grade.getType());
    }
}
