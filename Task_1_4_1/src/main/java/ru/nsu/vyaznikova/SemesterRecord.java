package ru.nsu.vyaznikova;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a record for a single semester in a student's record book. Each semester contains
 * information about grades for exams, differentiated tests, and regular credits.
 */
public class SemesterRecord {

    private final Map<String, Integer> exams;
    private final Map<String, Integer> differentiatedCredits;
    private final Set<String> regularCredits;

    /** Creates a new semester record. */
    public SemesterRecord() {
        this.exams = new HashMap<>();
        this.differentiatedCredits = new HashMap<>();
        this.regularCredits = new HashSet<>();
    }

    /**
     * Adds an exam and its grade to the semester record.
     *
     * @param subject the name of the subject
     * @param grade the grade received for the exam (from 2 to 5)
     */
    public void addExam(String subject, int grade) {
        exams.put(subject, grade);
    }

    /**
     * Adds a differentiated credit and its grade to the semester record.
     *
     * @param subject the name of the subject
     * @param grade the grade received for the credit (from 2 to 5)
     */
    public void addDifferentiatedCredit(String subject, int grade) {
        differentiatedCredits.put(subject, grade);
    }

    /**
     * Adds a regular credit to the semester record.
     *
     * @param subject the name of the subject
     */
    public void addRegularCredit(String subject) {
        regularCredits.add(subject);
    }

    /**
     * Retrieves all final grades (exams and differentiated credits) for the semester.
     *
     * @return a list of all grades
     */
    public List<Integer> getFinalGrades() {
        List<Integer> finalGrades = new ArrayList<>(exams.values());
        finalGrades.addAll(differentiatedCredits.values());
        return finalGrades;
    }

    /**
     * Retrieves a map of all exams and their grades.
     *
     * @return a map where the key is the subject name and the value is the grade
     */
    public Map<String, Integer> getExams() {
        return exams;
    }

    /**
     * Retrieves a map of all differentiated credits and their grades.
     *
     * @return a map where the key is the subject name and the value is the grade
     */
    public Map<String, Integer> getDifferentiatedCredits() {
        return differentiatedCredits;
    }

    /**
     * Retrieves a set of all regular credits.
     *
     * @return a set of subject names for which credits have been received
     */
    public Set<String> getRegularCredits() {
        return regularCredits;
    }
}