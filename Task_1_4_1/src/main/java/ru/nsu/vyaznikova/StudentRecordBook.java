package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an electronic student record book for a FIT (Faculty of Information Technology)
 * student. The record book tracks semester grades, calculates GPA, and evaluates eligibility for
 * financial and academic achievements.
 *
 * <p>Key functionalities: - Calculate the current GPA for all completed courses. - Determine if the
 * student can transfer from tuition-based to budget-based education. - Assess eligibility for an
 * honors diploma. - Check if the student qualifies for an increased scholarship in the current
 * semester.
 */
public class StudentRecordBook {

    private final String studentName;
    private boolean isTuitionBased;
    private final List<SemesterRecord> semesters;
    private Integer thesisGrade;

    /**
     * Creates a new student record book.
     *
     * @param studentName the name of the student
     * @param isTuitionBased whether the student is tuition-based (true for tuition, false for
     *     budget)
     */
    public StudentRecordBook(String studentName, boolean isTuitionBased) {
        this.studentName = studentName;
        this.isTuitionBased = isTuitionBased;
        this.semesters = new ArrayList<>();
    }

    /**
     * Adds a semester record to the record book.
     *
     * @param semester the semester record to be added
     */
    public void addSemester(SemesterRecord semester) {
        this.semesters.add(semester);
    }

    /**
     * Sets the thesis grade after completion.
     *
     * @param grade the grade received for the thesis (from 2 to 5)
     */
    public void setThesisGrade(int grade) {
        this.thesisGrade = grade;
    }

    /**
     * Calculates the GPA (Grade Point Average) for all completed courses.
     *
     * @return the average GPA, or 0.0 if no grades are present
     */
    public double calculateGpa() {
        return semesters.stream()
                .flatMap(semester -> semester.getFinalGrades().stream())
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Determines if the student can transfer from tuition-based to budget-based education.
     *
     * @return true if eligible for transfer, false otherwise
     */
    public boolean canTransferToBudget() {
        if (!isTuitionBased || semesters.size() < 2) {
            return false;
        }
        List<SemesterRecord> lastTwoSemesters =
                semesters.subList(Math.max(0, semesters.size() - 2), semesters.size());
        return lastTwoSemesters.stream()
                .allMatch(
                        semester ->
                                semester.getFinalGrades().stream().noneMatch(grade -> grade == 2));
    }

    /**
     * Checks if the student is eligible for an honors diploma.
     *
     * @return true if the student qualifies for an honors diploma, false otherwise
     */
    public boolean canGetHonorsDiploma() {
        if (thesisGrade == null || thesisGrade != 5) {
            return false;
        }

        List<Integer> allFinalGrades =
                semesters.stream()
                        .flatMap(semester -> semester.getFinalGrades().stream())
                        .collect(Collectors.toList());

        long excellentCount = allFinalGrades.stream().filter(grade -> grade == 5).count();
        boolean noSatisfactory = allFinalGrades.stream().noneMatch(grade -> grade == 3);

        return excellentCount >= 0.75 * allFinalGrades.size() && noSatisfactory;
    }

    /**
     * Determines if the student qualifies for an increased scholarship in the current semester.
     *
     * @return true if eligible for an increased scholarship, false otherwise
     */
    public boolean canGetIncreasedScholarship() {
        if (semesters.isEmpty()) {
            return false;
        }
        SemesterRecord lastSemester = semesters.get(semesters.size() - 1);
        return lastSemester.getFinalGrades().stream().allMatch(grade -> grade == 5);
    }

    /**
     * Initializes a pre-filled FIT program record book.
     *
     * @param studentName the name of the student
     * @param isTuitionBased whether the student is tuition-based
     * @return a pre-filled record book with the FIT curriculum
     */
    public static StudentRecordBook initializeFitProgram(
            String studentName, boolean isTuitionBased) {
        StudentRecordBook recordBook = new StudentRecordBook(studentName, isTuitionBased);
        recordBook.addSemester(FitProgram.firstSemester());
        recordBook.addSemester(FitProgram.secondSemester());
        recordBook.addSemester(FitProgram.thirdSemester());
        recordBook.addSemester(FitProgram.fourthSemester());
        return recordBook;
    }

    /**
     * Retrieves the list of semesters in the student's record book.
     *
     * @return a list of semester records
     */
    public List<SemesterRecord> getSemesters() {
        return semesters;
    }
}
