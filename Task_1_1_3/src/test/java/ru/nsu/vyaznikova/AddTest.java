package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddTest
{

    /**
     * Tests the eval method of the Add class for two numbers.
     */
    @Test
    void evalNumbers()
    {
        Expression left = new Number(3);
        Expression right = new Number(5);
        Expression add = new Add(left, right);
        assertEquals(8.0, add.eval(""));
    }

    /**
     * Tests the eval method of the Add class for a variable and a number.
     */
    @Test
    void evalVariableAndNumber()
    {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression add = new Add(left, right);
        assertEquals(15.0, add.eval("x = 10"));
    }

    /**
     * Tests the derivative method of the Add class for two variables.
     */
    @Test
    void derivativeVariables()
    {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        Expression add = new Add(left, right);
        Expression derivative = add.derivative("x");
        assertEquals("(1 + 0)", derivative.printAnswer());
    }

    /**
     * Tests the derivative method of the Add class for a variable and a number.
     */
    @Test
    void derivativeVariableAndNumber()
    {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression add = new Add(left, right);
        Expression derivative = add.derivative("x");
        assertEquals("(1 + 0)", derivative.printAnswer());
    }

    /**
     * Tests the printAnswer method of the Add class.
     */
    @Test
    void printAnswer()
    {
        Expression left = new Variable("x");
        Expression right = new Number(5);
        Expression add = new Add(left, right);
        assertEquals("(x + 5)", add.printAnswer());
    }
}