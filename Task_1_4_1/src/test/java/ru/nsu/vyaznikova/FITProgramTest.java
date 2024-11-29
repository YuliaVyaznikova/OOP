package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FITProgram class.
 */
class FITProgramTest {

    /**
     * Verifies the contents of the first semester.
     */
    @Test
    void testFirstSemester() {
        SemesterRecord semester = FITProgram.firstSemester();

        // Check number of exams
        assertEquals(3, semester.getExams().size());
        assertTrue(semester.getExams().containsKey("Introduction to Algebra and Analysis"));
        assertTrue(semester.getExams().containsKey("Introduction to Discrete Mathematics and Logic"));
        assertTrue(semester.getExams().containsKey("History of Russia"));

        // Check number of differentiated credits
        assertEquals(3, semester.getDifferentiatedCredits().size());
        assertTrue(semester.getDifferentiatedCredits().containsKey("Declarative Programming"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Imperative Programming"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("ORG"));

        // Check number of regular credits
        assertEquals(3, semester.getRegularCredits().size());
        assertTrue(semester.getRegularCredits().contains("Digital Platforms"));
        assertTrue(semester.getRegularCredits().contains("Foreign Language"));
        assertTrue(semester.getRegularCredits().contains("Physical Culture and Sports"));
    }

    /**
     * Verifies the contents of the second semester.
     */
    @Test
    void testSecondSemester() {
        SemesterRecord semester = FITProgram.secondSemester();

        // Check number of exams
        assertEquals(3, semester.getExams().size());
        assertTrue(semester.getExams().containsKey("Introduction to Algebra and Analysis"));
        assertTrue(semester.getExams().containsKey("Introduction to Discrete Mathematics and Logic"));
        assertTrue(semester.getExams().containsKey("Imperative Programming"));

        // Check number of differentiated credits
        assertEquals(3, semester.getDifferentiatedCredits().size());
        assertTrue(semester.getDifferentiatedCredits().containsKey("Digital Platforms"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Declarative Programming"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Foreign Language"));

        // Check number of regular credits
        assertEquals(2, semester.getRegularCredits().size());
        assertTrue(semester.getRegularCredits().contains("History of Russia"));
        assertTrue(semester.getRegularCredits().contains("Physical Culture and Sports"));
    }

    /**
     * Verifies the contents of the third semester.
     */
    @Test
    void testThirdSemester() {
        SemesterRecord semester = FITProgram.thirdSemester();

        // Check number of exams
        assertEquals(2, semester.getExams().size());
        assertTrue(semester.getExams().containsKey("Differential Equations and Complex Variable Theory"));
        assertTrue(semester.getExams().containsKey("Introduction to Artificial Intelligence"));

        // Check number of differentiated credits
        assertEquals(6, semester.getDifferentiatedCredits().size());
        assertTrue(semester.getDifferentiatedCredits().containsKey("Operating Systems"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Object-Oriented Programming"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Foreign Language"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Probability Theory and Statistics"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("PAC"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Computation Models"));

        // Check number of regular credits
        assertEquals(1, semester.getRegularCredits().size());
        assertTrue(semester.getRegularCredits().contains("Physical Culture and Sports"));
    }

    /**
     * Verifies the contents of the fourth semester.
     */
    @Test
    void testFourthSemester() {
        SemesterRecord semester = FITProgram.fourthSemester();

        // Check number of exams
        assertEquals(5, semester.getExams().size());
        assertTrue(semester.getExams().containsKey("Operating Systems"));
        assertTrue(semester.getExams().containsKey("Object-Oriented Programming"));
        assertTrue(semester.getExams().containsKey("Probability Theory and Statistics"));
        assertTrue(semester.getExams().containsKey("Computation Models"));
        assertTrue(semester.getExams().containsKey("Theory of Parallelism"));

        // Check number of differentiated credits
        assertEquals(5, semester.getDifferentiatedCredits().size());
        assertTrue(semester.getDifferentiatedCredits().containsKey("Business English"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("PAC"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Introduction to Computer Networks"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Introduction to Analog Electronics"));
        assertTrue(semester.getDifferentiatedCredits().containsKey("Embedded Digital Control Systems"));

        // Regular credits (no regular credits expected here)
        assertEquals(0, semester.getRegularCredits().size());
    }
}
