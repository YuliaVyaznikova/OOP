package ru.nsu.vyaznikova;

import java.io.*;
import java.util.Scanner;

/**
 * Abstract class representing a mathematical expression.
 *
 * @author Vyaznikova Elizaveta
 * @version 1.0
 */
public abstract class Expression {

    /**
     * Returns the derivative of the expression with respect
     * to the given variable.
     *
     * @param difVar The variable to differentiate with respect to.
     * @return The expression representing the derivative.
     */
    abstract Expression derivative(String difVar);

    /**
     * Evaluates the value of the expression given the values of variables.
     *
     * @param expression The string with assigned values for variables.
     * @return The value of the expression.
     */
    abstract double eval(String expression);

    /**
     * Returns a string representation of the expression.
     *
     * @return The string representing the expression.
     */
    abstract String printAnswer();

    /**
     * Prints the string representation of the expression
     * to the given output stream.
     *
     * @param outputStream The output stream to print the expression to.
     * @throws IOException If an error occurs while writing to the stream.
     */
    public void print(OutputStream outputStream) throws IOException {
        outputStream.write((this.printAnswer() + "\n").getBytes());
    }

    /**
     * Simplifies the expression based on specific rules.
     *
     * @return A simplified version of the expression,
     * or the original expression if no simplification is possible.
     */
    public abstract Expression simplify();

    /**
     * Reads a string with an expression from the input stream.
     *
     * @param inputStream The input stream to read the string from.
     * @return The string read from the stream.
     */
    public static String readExpression(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        String str = scanner.nextLine();
        scanner.close();
        return str;
    }

    /**
     * Creates an instance of Expression from a given string.
     *
     * @param expression The string representing the mathematical expression.
     * @return An instance of Expression corresponding to the expression string.
     */
    public static Expression createFromString(String expression) {
        expression = expression.replace(" ", "");

        // Обработка скобок
        if (expression.startsWith("(") && expression.endsWith(")")) {
            expression = expression.substring(1, expression.length() - 1);
            return parseExpression(expression);
        } else {
            return parseExpression(expression);
        }
    }

    /**
     * Parses a string representing a mathematical expression
     * and returns a corresponding Expression object.
     *
     * @param expression The string representing the mathematical expression.
     * @return The corresponding Expression object.
     */
    private static Expression parseExpression(String expression) {
        if (Character.isDigit(expression.charAt(0))) {
            return new Number(Integer.parseInt(expression));
        } else if (Character.isLetter(expression.charAt(0))) {
            return new Variable(expression);
        }

        int operatorIndex = findOperatorIndex(expression);
        if (operatorIndex != -1) {
            char operator = expression.charAt(operatorIndex);
            String leftOperand = expression.substring(0, operatorIndex);
            String rightOperand =
                    expression.substring(operatorIndex + 1);

            Expression left = parseExpression(leftOperand);
            Expression right = parseExpression(rightOperand);

            switch (operator) {
                case '+':
                    return new Add(left, right);
                case '-':
                    return new Sub(left, right);
                case '*':
                    return new Mul(left, right);
                case '/':
                    return new Div(left, right);
                default:
                    throw new IllegalArgumentException("Invalid operator: "
                            + operator);
            }
        }

        throw new IllegalArgumentException("Invalid expression: "
                + expression);
    }

    /**
     * Finds the index of the first operator in the expression string.
     *
     * @param expression The expression string.
     * @return The index of the first operator,
     * or -1 if no operator is found.
     */
    private static int findOperatorIndex(String expression) {
        int index = -1;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+'
                    || expression.charAt(i) == '-'
                    || expression.charAt(i) == '*'
                    || expression.charAt(i) == '/') {
                index = i;
                break;
            }
        }
        return index;
    }

    public static void main(String[] args) { }
}