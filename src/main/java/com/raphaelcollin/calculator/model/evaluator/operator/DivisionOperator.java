package com.raphaelcollin.calculator.model.evaluator.operator;

public class DivisionOperator implements Operator {
    @Override
    public double apply(double n, double m) {
        return n / m;
    }
}
