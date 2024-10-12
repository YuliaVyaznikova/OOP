package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class VariableTest {

    /**
     * Tests the eval method of the Variable class
     * when the variable is found in the expression.
     */
    @Test
    void evalVariableFound() {
        Variable variable = new Variable("x");
        assertEquals(10.0,
                variable.eval("x = 10; y = 5"));
    }

    /**
     * Tests the eval method of the Variable class
     * when the variable is not found in the expression.
     */
    @Test
    void evalVariableNotFound() {
        Variable variable = new Variable("x");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            variable.eval("y = 5");
        });

        String expectedMessage = "Variable not found in the expression: x";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Tests the derivative method of the Variable class
     * when the differentiation variable matches the variable.
     */
    @Test
    void derivativeMatchingVariable() {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("x");
        assertEquals("1", derivative.printAnswer());
    }

    /**
     * Tests the derivative method of the Variable class
     * when the differentiation variable is different.
     */
    @Test
    void derivativeDifferentVariable() {
        Variable variable = new Variable("x");
        Expression derivative = variable.derivative("y");
        assertEquals("0", derivative.printAnswer());
    }

    /**
     * Tests the printAnswer method of the Variable class.
     */
    @Test
    void printAnswer() {
        Variable variable = new Variable("x");
        assertEquals("x", variable.printAnswer());
    }
}