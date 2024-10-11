package ru.nsu.vyaznikova;

/**
 * Represents division of two expressions.
 */
class Div extends Expression
{
    private final Expression left;
    private final Expression right;

    /**
     * Constructs a Div object with the given left and right expressions.
     *
     * @param left  The left expression.
     * @param right The right expression.
     */
    public Div(Expression left, Expression right)
    {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the derivative of the division.
     *
     * @param difVar The variable to differentiate with respect to.
     * @return A Div expression representing the derivative of the division,
     *         calculated using the quotient rule.
     */
    @Override
    public Expression derivative(String difVar)
    {
        Expression derLeft = new Mul(left.derivative(difVar), right);
        Expression derRight = new Mul(left, right.derivative(difVar));
        return new Div(new Sub(derLeft, derRight), new Mul(right, right));
    }

    /**
     * Evaluates the quotient of the left and right expressions.
     *
     * @param expression The string with assigned values for variables.
     * @return The quotient of the evaluated left and right expressions.
     */
    @Override
    public double eval(String expression)
    {
        try
        {
            double rightValue = right.eval(expression);
            if (rightValue == 0)
            {
                throw new ArithmeticException("Division by zero");
            }
            return left.eval(expression) / rightValue;
        }
        catch (ArithmeticException e)
        {
            System.err.println("Error: " + e.getMessage());
            return Double.NaN;
        }
    }

    /**
     * Returns a string representation of the division expression.
     *
     * @return The string representing the division, enclosed in parentheses.
     */
    @Override
    public String printAnswer()
    {
        return "(" + left.printAnswer() + " / " + right.printAnswer() + ")";
    }

    /**
     * Simplifies the expression based on specific rules.
     *
     * @return A simplified version of the expression, or the original expression if no simplification is possible.
     */
    @Override
    public Expression simplify()
    {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();
        return new Div(leftSimplified, rightSimplified);
    }
}