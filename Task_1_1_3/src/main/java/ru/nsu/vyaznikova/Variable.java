package ru.nsu.vyaznikova;

public class Variable extends Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression derivative(String var) {
        if (name.equals(var)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }

    @Override
    public int eval(String assign) {
        return Expression.parseAssignments(assign).getOrDefault(name, 0);
    }

    @Override
    public Expression simplify() {
        return this;
    }
}