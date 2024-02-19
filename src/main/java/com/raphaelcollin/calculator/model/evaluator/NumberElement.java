package com.raphaelcollin.calculator.model.evaluator;

public class NumberElement implements Element {
    private final double value;

    public NumberElement(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }
}
