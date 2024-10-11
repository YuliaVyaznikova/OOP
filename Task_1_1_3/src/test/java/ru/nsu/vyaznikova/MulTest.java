package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MulTest {

    /**
     * Tests the eval method of the Mul class for two numbers.
     */
    @Test
    void evalNumbers() {
        Expression left = new Number(3);
        Expression right = new Number(5);
        Expression mul = new Mul(left, right);
        assertEquals(15.0, mul.eval(""));
    }

    /**
     * Tests the eval method of the Mul class for a variable and a number.
     */
    @Test
    void evalVariableAndNumber() {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression mul = new Mul(left, right);
        assertEquals(50.0, mul.eval("x = 10"));
    }

    /**
     * Tests the derivative method of the Mul class for two variables.
     */
    @Test
    void derivativeVariables() {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        Expression mul = new Mul(left, right);
        Expression derivative = mul.derivative("x");
        assertEquals("((1 * y) + (x * 0))", derivative.printAnswer());
    }

    /**
     * Tests the derivative method of the Mul class for a variable and a number.
     */
    @Test
    void derivativeVariableAndNumber() {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression mul = new Mul(left, right);
        Expression derivative = mul.derivative("x");
        assertEquals("((1 * 5) + (x * 0))", derivative.printAnswer());
    }

    /**
     * Tests the printAnswer method of the Mul class.
     */
    @Test
    void printAnswer() {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression mul = new Mul(left, right);
        assertEquals("(x * 5)", mul.printAnswer());
    }
}