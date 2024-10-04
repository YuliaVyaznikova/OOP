package ru.nsu.vyaznikova;

public class Parser {
    public static Expression parse(String expression) {
        expression = expression.trim();
        if (expression.startsWith("(") && expression.endsWith(")")) {
            expression = expression.substring(1, expression.length() - 1).trim();
        }

        if (Character.isDigit(expression.charAt(0))) {
            return new Number(Integer.parseInt(expression));
        } else if (Character.isLetter(expression.charAt(0))) {
            return new Variable(expression);
        } else {
            String[] parts = expression.split("(?<=[-+*/])|(?=[-+*/])");
            Expression left = parse(parts[0].trim());
            String operator = parts[1].trim();
            Expression right = parse(parts[2].trim());

            switch (operator) {
                case "+":
                    return new Add(left, right);
                case "-":
                    return new Sub(left, right);
                case "*":
                    return new Mul(left, right);
                case "/":
                    return new Div(left, right);
                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        }
    }
}