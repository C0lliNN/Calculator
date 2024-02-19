package com.raphaelcollin.calculator.model.evaluator.operator;

public class MultiplicationOperator implements Operator {
    @Override
    public double apply(double n, double m) {
        return n * m;
    }
}
