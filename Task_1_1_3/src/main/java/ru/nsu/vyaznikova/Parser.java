package ru.nsu.vyaznikova;

/**
 * Parses a string representation of a mathematical expression into an Expression object.
 */
class Parser
{
    private final String str;
    private int currentChar;
    private final int length;

    /**
     * Constructs a Parser object with the given string representing the expression.
     *
     * @param str1 The string to parse.
     */
    public Parser(String str1)
    {
        this.str = str1.replace(" ", "");
        this.currentChar = 0;
        this.length = str.length();
    }

    /**
     * Parses the expression string and returns an Expression object.
     *
     * @return The parsed Expression object.
     */
    public Expression parse()
    {
        return expression();
    }

    /**
     * Parses the expression, recursively evaluating sub-expressions enclosed in parentheses.
     *
     * @return The parsed Expression object representing the expression.
     */
    private Expression expression()
    {
        Expression leftExpression = term();

        while (currentChar < length)
        {
            char operation = str.charAt(currentChar);
            currentChar++;
            Expression rightExpression = term();

            if (operation == '+')
            {
                leftExpression = new Add(leftExpression, rightExpression);
            }
            else if (operation == '-')
            {
                leftExpression = new Sub(leftExpression, rightExpression);
            }
            else if (operation == '*')
            {
                leftExpression = new Mul(leftExpression, rightExpression);
            }
            else if (operation == '/')
            {
                leftExpression = new Div(leftExpression, rightExpression);
            }
            else
            {
                throw new IllegalArgumentException("Invalid operation: " + operation);
            }
        }

        return leftExpression;
    }

    /**
     * Parses a term, which can be a number, a variable, or a negated number.
     *
     * @return The parsed Expression object representing the term.
     */
    private Expression term()
    {
        Expression expression;
        boolean isNegative = false;
        if (str.charAt(currentChar) == '-')
        {
            isNegative = true;
            currentChar++;
        }
        try
        {
            if (Character.isDigit(str.charAt(currentChar)))
            {
                int number = 0;

                while (Character.isDigit(str.charAt(currentChar)) && currentChar < length - 1)
                {
                    number *= 10;
                    number += Character.getNumericValue(str.charAt(currentChar));
                    currentChar++;
                }

                expression = isNegative ? new Number(-number) : new Number(number);
            }
            else if (Character.isAlphabetic(str.charAt(currentChar)))
            {
                StringBuilder variable = new StringBuilder();

                while (Character.isAlphabetic(str.charAt(currentChar)) && currentChar < length - 1)
                {
                    variable.append(str.charAt(currentChar));
                    currentChar++;
                }

                expression = new Variable(variable.toString());
            }
            else
            {
                throw new IllegalArgumentException("Invalid input: expected variable or number");
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            expression = null;
        }
        return expression;
    }
}