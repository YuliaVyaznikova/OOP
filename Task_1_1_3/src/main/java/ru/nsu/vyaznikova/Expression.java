package ru.nsu.vyaznikova;

import java.io.*;
import java.util.Scanner;

/**
 * Abstract class representing a mathematical expression.
 *
 * @author Vyaznikova Elizaveta
 * @version 1.0
 */
public abstract class Expression
{

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
    public void print(OutputStream outputStream) throws IOException
    {
        outputStream.write((this.printAnswer() + "\n").getBytes());
    }

    /**
     * Simplifies the expression based on specific rules.
     *
     * @return A simplified version of the expression, or the original expression if no simplification is possible.
     */
    public abstract Expression simplify();

    /**
     * Creates an instance of Expression from a string read from the input stream.
     *
     * @param inputStream The input stream to read the expression string from.
     * @return An instance of Expression corresponding to the expression string.
     */
    public static Expression create(InputStream inputStream)
    {
        String expression = readExpression(inputStream);
        Parser parser = new Parser(expression);
        return parser.parse();
    }

    /**
     * Reads a string with an expression from the input stream.
     *
     * @param inputStream The input stream to read the string from.
     * @return The string read from the stream.
     */
    public static String readExpression(InputStream inputStream)
    {
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
        try (ByteArrayInputStream bais = new ByteArrayInputStream(expression.getBytes())) {
            return create(bais);
        } catch (IOException e) {
            throw new RuntimeException("Error creating Expression from string", e);
        }
    }

    public static void main(String[] args) { }
}