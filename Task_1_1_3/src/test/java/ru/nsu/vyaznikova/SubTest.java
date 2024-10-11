package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTest {

    /**
     * Tests the eval method of the Sub class for two numbers.
     */
    @Test
    void evalNumbers() {
        Expression left = new Number(8);
        Expression right = new Number(3);
        Expression sub = new Sub(left, right);
        assertEquals(5.0, sub.eval(""));
    }

    /**
     * Tests the eval method of the Sub class for a variable and a number.
     */
    @Test
    void evalVariableAndNumber() {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression sub = new Sub(left, right);
        assertEquals(5.0, sub.eval("x = 10"));
    }

    /**
     * Tests the derivative method of the Sub class for two variables.
     */
    @Test
    void derivativeVariables() {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        Expression sub = new Sub(left, right);
        Expression derivative = sub.derivative("x");
        assertEquals("(1 - 0)", derivative.printAnswer());
    }

    /**
     * Tests the derivative method of the Sub class
     * for a variable and a number.
     */
    @Test
    void derivativeVariableAndNumber() {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression sub = new Sub(left, right);
        Expression derivative = sub.derivative("x");
        assertEquals("(1 - 0)", derivative.printAnswer());
    }

    /**
     * Tests the printAnswer method of the Sub class.
     */
    @Test
    void printAnswer() {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression sub = new Sub(left, right);
        assertEquals("(x - 5)", sub.printAnswer());
    }
}