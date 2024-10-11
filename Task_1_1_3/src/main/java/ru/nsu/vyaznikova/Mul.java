package ru.nsu.vyaznikova;

/**
 * Represents multiplication of two expressions.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Constructs a Mul object with the given left and right expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     */
    Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the derivative of the multiplication.
     *
     * @param difVar The variable to differentiate with respect to.
     * @return An Add expression representing the sum of two multiplications:
     * - derivative of the left expression multiplied by the right expression
     * - the left expression multiplied by the derivative
     * of the right expression
     */
    @Override
    public Expression derivative(String difVar) {
        Expression derLeft = new Mul(left.derivative(difVar), right);
        Expression derRight = new Mul(left, right.derivative(difVar));
        return new Add(derLeft, derRight);
    }

    /**
     * Evaluates the product of the left and right expressions.
     *
     * @param expression The string with assigned values for variables.
     * @return The product of the evaluated left and right expressions.
     */
    @Override
    public double eval(String expression) {
        return left.eval(expression) * right.eval(expression);
    }

    /**
     * Returns a string representation of the multiplication expression.
     *
     * @return The string representing the multiplication,
     * enclosed in parentheses.
     */
    @Override
    public String printAnswer() {
        return "(" + left.printAnswer() + " * " + right.printAnswer() + ")";
    }

    @Override
    public Expression simplify() {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();

        if (leftSimplified instanceof Number
                && rightSimplified instanceof Number) {
            return new Number((int) ((Number) leftSimplified).number
                    * (int) ((Number) rightSimplified).number);
        } else if (leftSimplified instanceof Number
                && ((Number) leftSimplified).number == 0) {
            return new Number(0);
        } else if (leftSimplified instanceof Number
                && ((Number) leftSimplified).number == 1) {
            return rightSimplified;
        } else if (rightSimplified instanceof Number
                && ((Number) rightSimplified).number == 0) {
            return new Number(0);
        } else if (rightSimplified instanceof Number
                && ((Number) rightSimplified).number == 1) {
            return leftSimplified;
        }

        return new Mul(leftSimplified, rightSimplified);
    }
}