package ru.nsu.vyaznikova;

public class Mul extends BinaryOperation {
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    @Override
    public int eval(String assign) {
        return left.eval(assign) * right.eval(assign);
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            return new Number(((Number) simplifiedLeft).value * ((Number) simplifiedRight).value);
        } else if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).value == 0) {
            return new Number(0);
        } else if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).value == 1) {
            return simplifiedRight;
        } else if (simplifiedRight instanceof Number && ((Number) simplifiedRight).value == 0) {
            return new Number(0);
        } else if (simplifiedRight instanceof Number && ((Number) simplifiedRight).value == 1) {
            return simplifiedLeft;
        } else {
            return new Mul(simplifiedLeft, simplifiedRight);
        }
    }
}