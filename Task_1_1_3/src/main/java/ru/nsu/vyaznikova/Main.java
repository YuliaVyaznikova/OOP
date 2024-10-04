package ru.nsu.vyaznikova;

public class Main {
    public static void main(String[] args) {
        Expression e = Parser.parse("(3+(2*x))");
        System.out.println("Expression: " + e.toString());

        Expression de = e.derivative("x");
        System.out.println("Derivative: " + de.toString());

        System.out.println("Eval: " + e.eval("x = 10; y = 13"));

        Expression simplified = e.simplify();
        System.out.println("Simplified: " + simplified.toString());
    }
}