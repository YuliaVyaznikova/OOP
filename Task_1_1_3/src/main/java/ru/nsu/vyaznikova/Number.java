package ru.nsu.vyaznikova;

/**
 * Represents a number as a mathematical expression.
 */
public class Number extends Expression {
    final int number;

    /**
     * Constructs a Number object with the given integer value.
     *
     * @param num The integer value of the number.
     */
    Number(int num) {
        this.number = num;
    }

    /**
     * Returns the derivative of the number, which is always 0.
     *
     * @param difVar The variable to differentiate
     *               with respect to (not used).
     * @return A Number object representing 0.
     */
    @Override
    public Expression derivative(String difVar) {
        Expression result = new Number(0);
        return result;
    }

    /**
     * Evaluates the number, which returns its own value.
     *
     * @param expression The string with assigned values
     *                   for variables (not used).
     * @return The integer value of the number as a double.
     */
    @Override
    public double eval(String expression) {
        return (double) number;
    }

    /**
     * Returns a string representation of the number.
     *
     * @return The string representation of the number.
     */
    @Override
    public String printAnswer() {
        return String.valueOf(number);
    }

    @Override
    public Expression simplify() {
        return this;
    }
}