package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvalTest {

    /**
     * Tests the eval method for a simple addition.
     */
    @Test
    void testEvalAdd() {
        Expression e = new Add(new Number(3), new Number(5));
        assertEquals(8.0, e.eval(""));
    }

    /**
     * Tests the eval method for a simple subtraction.
     */
    @Test
    void testEvalSub() {
        Expression e = new Sub(new Number(8), new Number(3));
        assertEquals(5.0, e.eval(""));
    }

    /**
     * Tests the eval method for a simple multiplication.
     */
    @Test
    void testEvalMul() {
        Expression e = new Mul(new Number(3), new Number(5));
        assertEquals(15.0, e.eval(""));
    }

    /**
     * Tests the eval method for a simple division.
     */
    @Test
    void testEvalDiv() {
        Expression e = new Div(new Number(10), new Number(2));
        assertEquals(5.0, e.eval(""));
    }
}