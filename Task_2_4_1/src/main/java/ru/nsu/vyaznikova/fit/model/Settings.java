package ru.nsu.fit.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents system settings and grading criteria for the OOP course.
 */
public class Settings {
    private final Map<String, Integer> gradeThresholds;
    private final long testTimeoutSeconds;
    private final boolean considerActivityInGrade;
    private final double activityWeight;

    public Settings(long testTimeoutSeconds, boolean considerActivityInGrade, double activityWeight) {
        this.testTimeoutSeconds = testTimeoutSeconds;
        this.considerActivityInGrade = considerActivityInGrade;
        this.activityWeight = activityWeight;
        this.gradeThresholds = new HashMap<>();
    }

    /**
     * Sets the minimum score threshold for a specific grade.
     *
     * @param grade The grade (e.g., "A", "B", "C")
     * @param minScore The minimum score required for this grade
     */
    public void setGradeThreshold(String grade, int minScore) {
        gradeThresholds.put(grade, minScore);
    }

    public Map<String, Integer> getGradeThresholds() {
        return new HashMap<>(gradeThresholds);
    }

    public long getTestTimeoutSeconds() {
        return testTimeoutSeconds;
    }

    public boolean isConsiderActivityInGrade() {
        return considerActivityInGrade;
    }

    public double getActivityWeight() {
        return activityWeight;
    }

    /**
     * Calculates the final grade based on score and activity (if enabled).
     *
     * @param score Raw score
     * @param activityPercentage Activity percentage (0-100)
     * @return The calculated grade
     */
    public String calculateGrade(double score, double activityPercentage) {
        double finalScore = score;
        if (considerActivityInGrade) {
            finalScore = score * (1 - activityWeight) + 
                        (score * (activityPercentage / 100) * activityWeight);
        }

        String result = "F";
        for (Map.Entry<String, Integer> entry : gradeThresholds.entrySet()) {
            if (finalScore >= entry.getValue()) {
                result = entry.getKey();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "gradeThresholds=" + gradeThresholds +
                ", testTimeoutSeconds=" + testTimeoutSeconds +
                ", considerActivityInGrade=" + considerActivityInGrade +
                ", activityWeight=" + activityWeight +
                '}';
    }
}
