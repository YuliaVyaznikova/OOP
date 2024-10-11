package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NumberTest
{

    /**
     * Tests the eval method of the Number class for a positive number.
     */
    @Test
    void evalPositiveNumber()
    {
        Number number = new Number(5);
        assertEquals(5.0, number.eval(""));
    }

    /**
     * Tests the eval method of the Number class for a negative number.
     */
    @Test
    void evalNegativeNumber()
    {
        Number number = new Number(-3);
        assertEquals(-3.0, number.eval(""));
    }

    /**
     * Tests the derivative method of the Number class.
     */
    @Test
    void derivative()
    {
        Number number = new Number(5);
        Expression derivative = number.derivative("x");
        assertEquals("0", derivative.printAnswer());
    }

    /**
     * Tests the printAnswer method of the Number class.
     */
    @Test
    void printAnswer()
    {
        Number number = new Number(5);
        assertEquals("5", number.printAnswer());
    }
}