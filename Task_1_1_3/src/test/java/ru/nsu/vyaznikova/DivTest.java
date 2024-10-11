package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    /**
     * Tests the eval method of the Div class for two numbers.
     */
    @Test
    void evalNumbers() {
        Expression left = new Number(10);
        Expression right = new Number(2);
        Expression div = new Div(left, right);
        assertEquals(5.0, div.eval(""));
    }

    /**
     * Tests the eval method of the Div class for a variable and a number.
     */
    @Test
    void evalVariableAndNumber() {
        Expression left = new Variable("x");
        Expression right = new Number(2);
        Expression div = new Div(left, right);
        assertEquals(5.0, div.eval("x = 10"));
    }

    /**
     * Tests the derivative method of the Div class for two variables.
     */
    @Test
    void derivativeVariables() {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        Expression div = new Div(left, right);
        Expression derivative = div.derivative("x");
        assertEquals("(((1 * y) - (x * 0)) / (y * y))",
                derivative.printAnswer());
    }

    /**
     * Tests the derivative method of the Div class for a variable and a number.
     */
    @Test
    void derivativeVariableAndNumber() {
        Expression left = new Variable("x");
        Expression right = new Number(2);
        Expression div = new Div(left, right);
        Expression derivative = div.derivative("x");
        assertEquals("(((1 * 2) - (x * 0)) / (2 * 2))",
                derivative.printAnswer());
    }

    @Test
    void testEvalDivByZero() {
        Expression left = new Number(10);
        Expression right = new Number(0);
        Expression div = new Div(left, right);
        assertEquals(Double.NaN, div.eval(""));
    }

    /**
     * Tests the printAnswer method of the Div class.
     */
    @Test
    void printAnswer() {
        Expression left = new Variable("x");
        Expression right = new Number(2);
        Expression div = new Div(left, right);
        assertEquals("(x / 2)", div.printAnswer());
    }
}