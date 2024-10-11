package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest
    {

    /**
     * Tests the print method of the Expression class.
     */
    @Test
    void print() throws IOException
    {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x"))); // Создаем выражение вручную
        String mustBe = "(3 + (2 * x))";
        assertPrint(e, mustBe);
    }

    /**
     * Tests the print method for a more complex expression.
     */
    @Test
    void print1()
    {
        Expression e = new Div(new Number(3), new Mul(new Variable("y"), new Variable("x"))); // Создаем выражение вручную
        String res = e.printAnswer();
        assertEquals(res, "(3 / (y * x))");
    }

    /**
     * Tests the print method for an expression with addition and subtraction.
     */
    @Test
    void print2()
    {
        Expression e = new Div(new Add(new Number(3), new Variable("x")), new Sub(new Variable("y"), new Variable("x"))); // Создаем выражение вручную
        String res = e.printAnswer();
        assertEquals(res, "((3 + x) / (y - x))");
    }

    /**
     * Tests the print method for a complex expression.
     */
    @Test
    void print3()
    {
        Expression e = new Mul(new Div(new Add(new Number(2), new Variable("x")), new Number(3)),
                new Sub(new Variable("Ax"), new Mul(new Number(-4), new Variable("y")))); // Создаем выражение вручную
        String res = e.printAnswer();
        assertEquals(res, "(((2 + x) / 3) * (Ax - (-4 * y)))");
    }

    /**
     * Tests the print method for an empty expression.
     */
    @Test
    void print4()
    {
        Expression e = new Number(0);
        String res = e.printAnswer();
        assertEquals(res, "0");
    }

    private void assertPrint(Expression e, String mustBe) throws IOException
    {
        // Create a stream to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Save original stream
        PrintStream originalOut = System.out;
        System.setOut(printStream); // Redirect System.out

        try
        {
            e.print(System.out); // Get the output
            String output = outputStream.toString().trim(); // Remove extra spaces and line breaks
            assertEquals(mustBe, output); // Check that the output matches the expected value
        }
        finally
        {
            System.setOut(originalOut); // Restore the original stream
        }
    }

    /**
     * Tests the simplify method for a simple addition with numbers.
     */
    @Test
    void testSimplifyAddNumbers()
    {
        Expression e = new Add(new Number(3), new Number(5));
        Expression simplified = e.simplify();
        assertEquals("8", simplified.printAnswer());
    }

    /**
     * Tests the simplify method for a simple subtraction with numbers.
     */
    @Test
    void testSimplifySubNumbers()
    {
        Expression e = new Sub(new Number(8), new Number(3));
        Expression simplified = e.simplify();
        assertEquals("5", simplified.printAnswer());
    }

    /**
     * Tests the simplify method for a simple multiplication with numbers.
     */
    @Test
    void testSimplifyMulNumbers()
    {
        Expression e = new Mul(new Number(3), new Number(5));
        Expression simplified = e.simplify();
        assertEquals("15", simplified.printAnswer());
    }

    /**
     * Tests the simplify method for a simple division with numbers.
     */
    @Test
    void testSimplifyDivNumbers()
    {
        Expression e = new Div(new Number(10), new Number(2));
        Expression simplified = e.simplify();
        assertEquals("(10 / 2)", simplified.printAnswer()); // Деление не упрощается, так как не является целым числом
    }

//    /**
//     * Tests the simplify method for a subtraction of two identical sub-expressions.
//     */
//    @Test
//    void testSimplifySubIdentical() {
//        Expression e = new Sub(new Add(new Variable("x"), new Number(3)), new Add(new Variable("x"), new Number(3)));
//        Expression simplified = e.simplify();
//        assertEquals("0", simplified.printAnswer());
//    }

    /**
     * Tests the simplify method for a multiplication by 0.
     */
    @Test
    void testSimplifyMulByZero()
    {
        Expression e = new Mul(new Number(0), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("0", simplified.printAnswer());
    }

    /**
     * Tests the simplify method for a multiplication by 1.
     */
    @Test
    void testSimplifyMulByOne()
    {
        Expression e = new Mul(new Number(1), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("x", simplified.printAnswer());
    }

    /**
     * Tests the simplify method for a complex expression.
     */
    @Test
    void testSimplifyComplex()
    {
        Expression e = new Add(new Mul(new Variable("x"), new Number(5)), new Sub(new Variable("x"), new Number(5)));
        Expression simplified = e.simplify();
        assertEquals("((x * 5) + (x - 5))", simplified.printAnswer());
    }
}