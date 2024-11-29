package ru.nsu.vyaznikova;

/**
 * Provides pre-defined semester records for the FIT (Faculty of Information Technology) curriculum.
 * Each semester record includes the exams, differentiated credits, and regular credits based on the
 * FIT program.
 */
public class FITProgram {

    /**
     * Generates the first semester record.
     *
     * @return the semester record for the first semester
     */
    public static SemesterRecord firstSemester() {
        SemesterRecord semester = new SemesterRecord();
        // Exams
        semester.addExam("Introduction to Algebra and Analysis", 5);
        semester.addExam("Introduction to Discrete Mathematics and Logic", 4);
        semester.addExam("History of Russia", 3);
        // Differentiated credits
        semester.addDifferentiatedCredit("Declarative Programming", 5);
        semester.addDifferentiatedCredit("Imperative Programming", 5);
        semester.addDifferentiatedCredit("ORG", 4);
        // Regular credits
        semester.addRegularCredit("Digital Platforms");
        semester.addRegularCredit("Foreign Language");
        semester.addRegularCredit("Physical Culture and Sports");
        return semester;
    }

    /**
     * Generates the second semester record.
     *
     * @return the semester record for the second semester
     */
    public static SemesterRecord secondSemester() {
        SemesterRecord semester = new SemesterRecord();
        // Exams
        semester.addExam("Introduction to Algebra and Analysis", 4);
        semester.addExam("Introduction to Discrete Mathematics and Logic", 4);
        semester.addExam("Imperative Programming", 5);
        // Differentiated credits
        semester.addDifferentiatedCredit("Digital Platforms", 5);
        semester.addDifferentiatedCredit("Declarative Programming", 4);
        semester.addDifferentiatedCredit("Foreign Language", 5);
        // Regular credits
        semester.addRegularCredit("History of Russia");
        semester.addRegularCredit("Physical Culture and Sports");
        return semester;
    }

    /**
     * Generates the third semester record.
     *
     * @return the semester record for the third semester
     */
    public static SemesterRecord thirdSemester() {
        SemesterRecord semester = new SemesterRecord();
        // Exams
        semester.addExam("Differential Equations and Complex Variable Theory", 4);
        semester.addExam("Introduction to Artificial Intelligence", 5);
        // Differentiated credits
        semester.addDifferentiatedCredit("Operating Systems", 5);
        semester.addDifferentiatedCredit("Object-Oriented Programming", 5);
        semester.addDifferentiatedCredit("Foreign Language", 5);
        semester.addDifferentiatedCredit("Probability Theory and Statistics", 4);
        semester.addDifferentiatedCredit("PAC", 4);
        semester.addDifferentiatedCredit("Computation Models", 4);
        // Regular credits
        semester.addRegularCredit("Physical Culture and Sports");
        return semester;
    }

    /**
     * Generates the fourth semester record.
     *
     * @return the semester record for the fourth semester
     */
    public static SemesterRecord fourthSemester() {
        SemesterRecord semester = new SemesterRecord();
        // Exams
        semester.addExam("Operating Systems", 5);
        semester.addExam("Object-Oriented Programming", 5);
        semester.addExam("Probability Theory and Statistics", 5);
        semester.addExam("Computation Models", 4);
        semester.addExam("Theory of Parallelism", 5);
        // Differentiated credits
        semester.addDifferentiatedCredit("Business English", 5);
        semester.addDifferentiatedCredit("PAC", 4);
        semester.addDifferentiatedCredit("Introduction to Computer Networks", 5);
        semester.addDifferentiatedCredit("Introduction to Analog Electronics", 4);
        semester.addDifferentiatedCredit("Embedded Digital Control Systems", 4);
        return semester;
    }
}
