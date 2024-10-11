package ru.nsu.vyaznikova;

/**
 * Represents a variable as a mathematical expression.
 */
public class Variable extends Expression {
    private final String variable;

    /**
     * Constructs a Variable object with the given variable name.
     *
     * @param var The name of the variable.
     */
    public Variable(String var) {
        this.variable = var;
    }

    /**
     * Returns the derivative of the variable.
     *
     * @param difVar The variable to differentiate with respect to.
     * @return A Number object representing 1
     * if the variable is the same as difVar, 0 otherwise.
     */
    @Override
    public Expression derivative(String difVar) {
        Expression result;

        if (difVar.equals(this.variable)) {
            result = new Number(1);
        } else {
            result = new Number(0);
        }

        return result;
    }

    /**
     * Evaluates the value of the variable given the values of variables.
     *
     * @param expression The string with assigned values for variables.
     * @return The value of the variable if found in the expression,
     * 0 otherwise.
     */
    @Override
    public double eval(String expression) {
        String[] assignments = expression.split("; ");

        for (String assignment : assignments) {
            String[] variableAndValue = assignment.split(" = ");
            if (variableAndValue[0].equals(variable)) {
                try {
                    return Double.parseDouble(variableAndValue[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid variable value: "
                            + variableAndValue[1]);
                }
            }
        }

        return 0;
    }

    /**
     * Returns the name of the variable as a string representation.
     *
     * @return The name of the variable.
     */
    @Override
    public String printAnswer() {
        return variable;
    }

    @Override
    public Expression simplify() {
        return this;
    }
}