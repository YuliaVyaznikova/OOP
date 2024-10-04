package ru.nsu.vyaznikova;

public class Number extends Expression {
    int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    public int eval(String assign) {
        return value;
    }

    @Override
    public Expression simplify() {
        return this;
    }
}