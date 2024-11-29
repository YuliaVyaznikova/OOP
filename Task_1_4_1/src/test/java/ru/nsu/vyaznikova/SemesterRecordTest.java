package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Unit tests for the SemesterRecord class. */
class SemesterRecordTest {

    /** Verifies the functionality of adding exams. */
    @Test
    void testAddExam() {
        SemesterRecord semester = new SemesterRecord();
        semester.addExam("Mathematics", 5);
        semester.addExam("Physics", 4);

        // Check if exams are added correctly
        Map<String, Integer> exams = semester.getExams();
        assertEquals(2, exams.size());
        assertEquals(5, exams.get("Mathematics"));
        assertEquals(4, exams.get("Physics"));
    }

    /** Verifies the functionality of adding differentiated credits. */
    @Test
    void testAddDifferentiatedCredit() {
        SemesterRecord semester = new SemesterRecord();
        semester.addDifferentiatedCredit("English", 5);
        semester.addDifferentiatedCredit("Programming", 4);

        // Check if differentiated credits are added correctly
        Map<String, Integer> differentiatedCredits = semester.getDifferentiatedCredits();
        assertEquals(2, differentiatedCredits.size());
        assertEquals(5, differentiatedCredits.get("English"));
        assertEquals(4, differentiatedCredits.get("Programming"));
    }

    /** Verifies the functionality of adding regular credits. */
    @Test
    void testAddRegularCredit() {
        SemesterRecord semester = new SemesterRecord();
        semester.addRegularCredit("Physical Education");
        semester.addRegularCredit("Foreign Language");

        // Check if regular credits are added correctly
        Set<String> regularCredits = semester.getRegularCredits();
        assertEquals(2, regularCredits.size());
        assertTrue(regularCredits.contains("Physical Education"));
        assertTrue(regularCredits.contains("Foreign Language"));
    }

    /** Verifies the functionality of getting final grades (exams and differentiated credits). */
    @Test
    void testGetFinalGrades() {
        SemesterRecord semester = new SemesterRecord();
        semester.addExam("Mathematics", 5);
        semester.addDifferentiatedCredit("Programming", 4);
        semester.addRegularCredit("Foreign Language");

        List<Integer> finalGrades = semester.getFinalGrades();
        // Exams and differentiated credits should be in final grades
        assertEquals(2, finalGrades.size());
        // We only added 2 grades (one for exam and one for differentiated credit)
        assertTrue(finalGrades.contains(5)); // Exam grade
        assertTrue(finalGrades.contains(4)); // Differentiated credit grade
    }

    /**
     * Verifies the size and correctness of all collections (exams, differentiated credits, regular
     * credits).
     */
    @Test
    void testGetAllData() {
        SemesterRecord semester = new SemesterRecord();
        semester.addExam("Mathematics", 5);
        semester.addDifferentiatedCredit("Programming", 4);
        semester.addRegularCredit("Foreign Language");

        // Check if all data is accessible
        Map<String, Integer> exams = semester.getExams();
        assertEquals(1, exams.size()); // One exam added
        Map<String, Integer> differentiatedCredits = semester.getDifferentiatedCredits();
        assertEquals(1, differentiatedCredits.size()); // One differentiated credit added
        Set<String> regularCredits = semester.getRegularCredits();
        assertEquals(1, regularCredits.size()); // One regular credit added
    }
}
