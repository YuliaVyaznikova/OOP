package ru.nsu.vyaznikova;

public abstract class BinaryOperation extends Expression {
    protected Expression left;
    protected Expression right;

    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}