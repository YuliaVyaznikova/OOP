package ru.nsu.vyaznikova;

import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract class representing a mathematical expression.
 *
 * @author Vyaznikova Elizaveta
 * @version 1.0
 */
public abstract class Expression {

    /**
     * Returns the derivative of the expression with respect to the given variable.
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
     * Prints the string representation of the expression to the given output stream.
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

    public static void main(String[] args) {
    }
}