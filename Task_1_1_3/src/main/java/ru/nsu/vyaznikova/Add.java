package ru.nsu.vyaznikova;

/**
 * Represents addition of two expressions.
 */
public class Add extends Expression
{
    private final Expression left;
    private final Expression right;

    /**
     * Constructs an Add object with the given left and right expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     */
    Add(Expression left, Expression right)
    {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the derivative of the addition.
     *
     * @param difVar The variable to differentiate with respect to.
     * @return An Add expression representing the sum of derivatives of left and right expressions.
     */
    @Override
    public Expression derivative(String difVar)
    {
        Expression derLeft = left.derivative(difVar);
        Expression derRight = right.derivative(difVar);
        return new Add(derLeft, derRight);
    }

    /**
     * Evaluates the sum of the left and right expressions.
     *
     * @param expression The string with assigned values for variables.
     * @return The sum of the evaluated left and right expressions.
     */
    @Override
    public double eval(String expression)
    {
        return left.eval(expression) + right.eval(expression);
    }

    /**
     * Returns a string representation of the addition expression.
     *
     * @return The string representing the addition, enclosed in parentheses.
     */
    @Override
    public String printAnswer()
    {
        return "(" + left.printAnswer() + " + " + right.printAnswer() + ")";
    }

    @Override
    public Expression simplify()
    {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();

        if (leftSimplified instanceof Number && rightSimplified instanceof Number)
        {
            return new Number((int) ((Number) leftSimplified).number + (int) ((Number) rightSimplified).number);
        }

        return new Add(leftSimplified, rightSimplified);
    }
}