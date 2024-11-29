package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Unit tests for the StudentRecordBook class. */
class StudentRecordBookTest {

    /** Verifies the functionality of adding semesters to the record book. */
    @Test
    void testAddSemester() {
        StudentRecordBook recordBook = new StudentRecordBook("John Doe", true);
        SemesterRecord semester = new SemesterRecord();
        semester.addExam("Mathematics", 5);
        recordBook.addSemester(semester);

        assertEquals(1, recordBook.getSemesters().size());
        assertEquals(5, recordBook.getSemesters().get(0).getExams().get("Mathematics"));
    }

    /** Verifies the GPA calculation functionality. */
    @Test
    void testcalculateGpa() {
        StudentRecordBook recordBook = new StudentRecordBook("John Doe", true);
        SemesterRecord semester1 = new SemesterRecord();
        semester1.addExam("Mathematics", 5);
        semester1.addDifferentiatedCredit("Physics", 4);
        recordBook.addSemester(semester1);

        SemesterRecord semester2 = new SemesterRecord();
        semester2.addExam("Programming", 4);
        semester2.addDifferentiatedCredit("English", 5);
        recordBook.addSemester(semester2);

        double gpa = recordBook.calculateGpa();
        assertEquals(4.5, gpa, 0.01);
    }

    /** Verifies the functionality of transferring to budget-based education. */
    @Test
    void testCanTransferToBudget() {
        StudentRecordBook recordBook = new StudentRecordBook("John Doe", true);
        SemesterRecord semester1 = new SemesterRecord();
        semester1.addExam("Mathematics", 5);
        semester1.addDifferentiatedCredit("Physics", 4);
        recordBook.addSemester(semester1);

        SemesterRecord semester2 = new SemesterRecord();
        semester2.addExam("Programming", 4);
        semester2.addDifferentiatedCredit("English", 5);
        recordBook.addSemester(semester2);

        assertTrue(recordBook.canTransferToBudget());

        SemesterRecord semester3 = new SemesterRecord();
        semester3.addExam("History", 2);
        recordBook.addSemester(semester3);

        assertFalse(recordBook.canTransferToBudget());
    }

    /** Verifies the eligibility for an honors diploma. */
    @Test
    void testCanGetHonorsDiploma() {
        StudentRecordBook recordBook = new StudentRecordBook("John Doe", true);
        recordBook.setThesisGrade(5);

        SemesterRecord semester1 = new SemesterRecord();
        semester1.addExam("Mathematics", 5);
        semester1.addDifferentiatedCredit("Physics", 5);
        recordBook.addSemester(semester1);

        SemesterRecord semester2 = new SemesterRecord();
        semester2.addExam("Programming", 5);
        semester2.addDifferentiatedCredit("English", 5);
        recordBook.addSemester(semester2);

        assertTrue(recordBook.canGetHonorsDiploma());
    }

    /** Verifies the eligibility for an increased scholarship. */
    @Test
    void testCanGetIncreasedScholarship() {
        StudentRecordBook recordBook = new StudentRecordBook("John Doe", true);
        SemesterRecord semester1 = new SemesterRecord();
        semester1.addExam("Mathematics", 5);
        semester1.addDifferentiatedCredit("Physics", 5);
        recordBook.addSemester(semester1);

        assertTrue(recordBook.canGetIncreasedScholarship());

        SemesterRecord semester2 = new SemesterRecord();
        semester2.addExam("Programming", 4);
        recordBook.addSemester(semester2);

        assertFalse(recordBook.canGetIncreasedScholarship());
    }

    /** Verifies the FIT program initialization. */
    @Test
    void testinitializeFitProgram() {
        StudentRecordBook recordBook = StudentRecordBook.initializeFitProgram("John Doe", true);

        assertEquals(4, recordBook.getSemesters().size());
        assertNotNull(recordBook.getSemesters().get(0).getExams());
    }
}
