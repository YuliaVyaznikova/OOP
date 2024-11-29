package ru.nsu.vyaznikova;

/** Represents the type of grade for a subject. */
public enum GradeType {
    /** Final exam grade. */
    EXAM, // Экзамен

    /** Pass or fail grade, without differentiation. */
    CREDIT, // Зачет

    /** Differentiated grade for a subject, usually on a scale of 2 to 5. */
    DIFFERENTIATED_CREDIT // Дифференцированный зачет
}
