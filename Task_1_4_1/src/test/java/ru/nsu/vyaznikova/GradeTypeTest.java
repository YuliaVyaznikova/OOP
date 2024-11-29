package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the GradeType enum.
 */
class GradeTypeTest {

    /**
     * Tests that the GradeType values match their expected names.
     */
    @Test
    void testGradeTypeEnumValues() {
        assertEquals("EXAM", GradeType.EXAM.name());
        assertEquals("CREDIT", GradeType.CREDIT.name());
        assertEquals("DIFFERENTIATED_CREDIT", GradeType.DIFFERENTIATED_CREDIT.name());
    }
}