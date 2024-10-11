package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

class ExpressionTest {

    @Test
    void testPrintAnswerNumber() {
        Expression number = new Number(5);
        assertEquals("5", number.printAnswer());
    }

    @Test
    void testPrintAnswerVariable() {
        Expression variable = new Variable("x");
        assertEquals("x", variable.printAnswer());
    }

    @Test
    void testPrintAnswerAdd() {
        Expression add = new Add(new Number(3), new Variable("x"));
        assertEquals("(3 + x)", add.printAnswer());
    }

    @Test
    void testPrintAnswerSub() {
        Expression sub = new Sub(new Variable("x"), new Number(5));
        assertEquals("(x - 5)", sub.printAnswer());
    }

    @Test
    void testPrintAnswerMul() {
        Expression mul = new Mul(new Variable("x"), new Number(5));
        assertEquals("(x * 5)", mul.printAnswer());
    }

    @Test
    void testPrintAnswerDiv() {
        Expression div = new Div(new Variable("x"), new Number(5));
        assertEquals("(x / 5)", div.printAnswer());
    }

    // Тесты для метода eval()
    @Test
    void testEvalNumber() {
        Expression number = new Number(5);
        assertEquals(5.0, number.eval(""));
    }

    @Test
    void testEvalVariable() {
        Expression variable = new Variable("x");
        assertEquals(10.0, variable.eval("x = 10"));
    }

    @Test
    void testEvalAdd() {
        Expression add = new Add(new Number(3), new Variable("x"));
        assertEquals(13.0, add.eval("x = 10"));
    }

    @Test
    void testEvalSub() {
        Expression sub = new Sub(new Variable("x"), new Number(5));
        assertEquals(5.0, sub.eval("x = 10"));
    }

    @Test
    void testEvalMul() {
        Expression mul = new Mul(new Variable("x"), new Number(5));
        assertEquals(50.0, mul.eval("x = 10"));
    }

    @Test
    void testEvalDiv() {
        Expression div = new Div(new Variable("x"), new Number(5));
        assertEquals(2.0, div.eval("x = 10"));
    }

    @Test
    void testDerivativeNumber() {
        Expression number = new Number(5);
        Expression derivative = number.derivative("x");
        assertEquals("0", derivative.printAnswer());
    }

    @Test
    void testDerivativeVariable() {
        Expression variable = new Variable("x");
        Expression derivative = variable.derivative("x");
        assertEquals("1", derivative.printAnswer());
    }

    @Test
    void testDerivativeVariableDifferentVar() {
        Expression variable = new Variable("x");
        Expression derivative = variable.derivative("y");
        assertEquals("0", derivative.printAnswer());
    }

    @Test
    void testDerivativeAdd() {
        Expression add = new Add(new Number(3), new Variable("x"));
        Expression derivative = add.derivative("x");
        assertEquals("(0 + 1)", derivative.printAnswer());
    }

    @Test
    void testDerivativeSub() {
        Expression sub = new Sub(new Variable("x"), new Number(5));
        Expression derivative = sub.derivative("x");
        assertEquals("(1 - 0)", derivative.printAnswer());
    }

    @Test
    void testDerivativeMul() {
        Expression mul = new Mul(new Variable("x"), new Number(5));
        Expression derivative = mul.derivative("x");
        assertEquals("((1 * 5) + (x * 0))", derivative.printAnswer());
    }

    @Test
    void testDerivativeDiv() {
        Expression div = new Div(new Variable("x"), new Number(5));
        Expression derivative = div.derivative("x");
        assertEquals("(((1 * 5) - (x * 0)) / (5 * 5))",
                derivative.printAnswer());
    }

    // Тесты для метода simplify()
    @Test
    void testSimplifyNumber() {
        Expression number = new Number(5);
        Expression simplified = number.simplify();
        assertEquals("5", simplified.printAnswer());
    }

    @Test
    void testSimplifyVariable() {
        Expression variable = new Variable("x");
        Expression simplified = variable.simplify();
        assertEquals("x", simplified.printAnswer());
    }

    @Test
    void testSimplifyAddNumbers() {
        Expression add = new Add(new Number(3), new Number(5));
        Expression simplified = add.simplify();
        assertEquals("8", simplified.printAnswer());
    }

    @Test
    void testSimplifySubNumbers() {
        Expression sub = new Sub(new Number(8), new Number(3));
        Expression simplified = sub.simplify();
        assertEquals("5", simplified.printAnswer());
    }

    @Test
    void testSimplifyMulNumbers() {
        Expression mul = new Mul(new Number(3), new Number(5));
        Expression simplified = mul.simplify();
        assertEquals("15", simplified.printAnswer());
    }

    @Test
    void testSimplifyDivNumbers() {
        Expression div = new Div(new Number(10), new Number(2));
        Expression simplified = div.simplify();
        assertEquals("(10 / 2)", simplified.printAnswer());
    }

    @Test
    void testSimplifyMulByZero() {
        Expression mul = new Mul(new Number(0), new Variable("x"));
        Expression simplified = mul.simplify();
        assertEquals("0", simplified.printAnswer());
    }

    @Test
    void testSimplifyMulByOne() {
        Expression mul = new Mul(new Number(1), new Variable("x"));
        Expression simplified = mul.simplify();
        assertEquals("x", simplified.printAnswer());
    }

    @Test
    void testSimplifyComplex() {
        Expression complex = new Add(new Mul(new Variable("x"),
                new Number(5)), new Sub(new Variable("x"), new Number(5)));
        Expression simplified = complex.simplify();
        assertEquals("((x * 5) + (x - 5))",
                simplified.printAnswer());
    }

    @Test
    void testPrint() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        Expression expression = new Add(new Number(3), new Variable("x"));
        expression.print(printStream);

        String expectedOutput = "(3 + x)\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testReadExpression() {
        InputStream inputStream = new ByteArrayInputStream("x + 5".getBytes());
        String expression = Expression.readExpression(inputStream);
        assertEquals("x + 5", expression);
    }
}