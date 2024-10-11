package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest
{

    /**
     * Tests the eval method of the Variable class when the variable is found in the expression.
     */
    @Test
    void evalVariableFound()
    {
        Variable variable = new Variable("x");
        assertEquals(10.0, variable.eval("x = 10; y = 5"));
    }

    /**
     * Tests the eval method of the Variable class when the variable is not found in the expression.
     */
    @Test
    void evalVariableNotFound()
    {
        Variable variable = new Variable("x");
        assertEquals(0.0, variable.eval("y = 5"));
    }

    /**
     * Tests the derivative method of the Variable class when the differentiation variable matches the variable.
     */
    @Test
    void derivativeMatchingVariable()
    {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("x");
        assertEquals("1", derivative.printAnswer());
    }

    /**
     * Tests the derivative method of the Variable class when the differentiation variable is different.
     */
    @Test
    void derivativeDifferentVariable()
    {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("y");
        assertEquals("0", derivative.printAnswer());
    }

    /**
     * Tests the printAnswer method of the Variable class.
     */
    @Test
    void printAnswer()
    {
        Variable variable = new Variable("x");
        assertEquals("x", variable.printAnswer());
    }
}