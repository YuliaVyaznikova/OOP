//package ru.nsu.vyaznikova;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ParserTest {
//
//    /**
//     * Tests the parse method for a simple expression.
//     */
//    @Test
//    void parseSimpleExpression() {
//        Parser parser = new Parser("x + 5");
//        Expression e = parser.parse();
//        Expression mustBe = new Add(new Variable("x"), new Number(5));
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an empty expression.
//     */
//    @Test
//    void parseEmptyExpression() {
//        Parser parser = new Parser("");
//        Expression e = parser.parse();
//        Expression mustBe = new Number(0);
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with one variable.
//     */
//    @Test
//    void parseExpressionWithOneVariable() {
//        Parser parser = new Parser("x");
//        Expression e = parser.parse();
//        Expression mustBe = new Variable("x");
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with a number.
//     */
//    @Test
//    void parseExpressionWithNumber() {
//        Parser parser = new Parser("10");
//        Expression e = parser.parse();
//        Expression mustBe = new Number(10);
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with a minus sign.
//     */
//    @Test
//    void parseExpressionWithMinusSign() {
//        Parser parser = new Parser("-10");
//        Expression e = parser.parse();
//        Expression mustBe = new Number(-10);
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with parentheses.
//     */
//    @Test
//    void parseExpressionWithParentheses() {
//        Parser parser = new Parser("(x + 5)");
//        Expression e = parser.parse();
//        Expression mustBe = new Add(new Variable("x"), new Number(5));
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with multiple operations.
//     */
//    @Test
//    void parseExpressionWithMultipleOperations() {
//        Parser parser = new Parser("x + 5 * 2");
//        Expression e = parser.parse();
//        Expression mustBe = new Add(new Variable("x"), new Mul(new Number(5), new Number(2)));
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with division.
//     */
//    @Test
//    void parseExpressionWithDivision() {
//        Parser parser = new Parser("x / 5");
//        Expression e = parser.parse();
//        Expression mustBe = new Div(new Variable("x"), new Number(5));
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with nested parentheses.
//     */
//    @Test
//    void parseExpressionWithNestedParentheses() {
//        Parser parser = new Parser("((x + 5) * 2)");
//        Expression e = parser.parse();
//        Expression mustBe = new Mul(new Add(new Variable("x"), new Number(5)), new Number(2));
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//
//    /**
//     * Tests the parse method for an expression with negative numbers.
//     */
//    @Test
//    void parseExpressionWithNegativeNumbers() {
//        Parser parser = new Parser("x - 5");
//        Expression e = parser.parse();
//        Expression mustBe = new Sub(new Variable("x"), new Number(5));
//        assertEquals(e.printAnswer(), mustBe.printAnswer());
//    }
//}