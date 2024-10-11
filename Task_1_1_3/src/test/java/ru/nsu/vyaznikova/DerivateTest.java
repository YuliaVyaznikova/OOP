package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DerivateTest
{

    /**
     * Tests the derivative method for a simple addition.
     */
    @Test
    void testDerivativeAdd()
    {
        Expression e = new Add(new Number(3), new Variable("x"));
        Expression result = e.derivative("x");
        assertEquals("(0 + 1)", result.printAnswer());
    }

    /**
     * Tests the derivative method for a simple subtraction.
     */
    @Test
    void testDerivativeSub()
    {
        Expression e = new Sub(new Variable("x"), new Number(5));
        Expression result = e.derivative("x");
        assertEquals("(1 - 0)", result.printAnswer());
    }

    /**
     * Tests the derivative method for a simple multiplication.
     */
    @Test
    void testDerivativeMul()
    {
        Expression e = new Mul(new Variable("x"), new Number(5));
        Expression result = e.derivative("x");
        assertEquals("((1 * 5) + (x * 0))", result.printAnswer());
    }

    /**
     * Tests the derivative method for a simple division.
     */
    @Test
    void testDerivativeDiv()
    {
        Expression e = new Div(new Variable("x"), new Number(5));
        Expression result = e.derivative("x");
        assertEquals("(((1 * 5) - (x * 0)) / (5 * 5))", result.printAnswer());
    }
}