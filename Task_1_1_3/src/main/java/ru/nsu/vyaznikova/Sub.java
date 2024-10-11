package ru.nsu.vyaznikova;

/**
 * Represents subtraction of two expressions.
 */
public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructs a Sub object with the given
     * left and right expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     */
    Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the derivative of the subtraction.
     *
     * @param difVar The variable to differentiate with respect to.
     * @return A Sub expression representing the difference
     *     of derivatives of left and right expressions.
     */
    @Override
    public Expression derivative(String difVar) {
        Expression derLeft = left.derivative(difVar);
        Expression derRight = right.derivative(difVar);
        return new Sub(derLeft, derRight);
    }

    /**
     * Evaluates the difference of the left and right expressions.
     *
     * @param expression The string with assigned values for variables.
     * @return The difference of the evaluated left and right expressions.
     */
    @Override
    public double eval(String expression) {
        return left.eval(expression) - right.eval(expression);
    }

    /**
     * Returns a string representation of the subtraction expression.
     *
     * @return The string representing the subtraction,
     *     enclosed in parentheses.
     */
    @Override
    public String printAnswer() {
        return "(" + left.printAnswer() + " - " + right.printAnswer() + ")";
    }

    @Override
    public Expression simplify() {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();

        if (leftSimplified instanceof Number
                && rightSimplified instanceof Number) {
            return new Number((int) ((Number) leftSimplified).number
                    - (int) ((Number) rightSimplified).number);
        } else if (leftSimplified.equals(rightSimplified)) {
            return new Number(0);
        }

        return new Sub(leftSimplified, rightSimplified);
    }
}