package com.raphaelcollin.calculator.model;

import com.raphaelcollin.calculator.model.expression.ExpressionEvaluator;

public class Calculator {
    private final ExpressionEvaluator expressionEvaluator;
    public Calculator(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public double percentage(double value) {
        return value / 100;
    }

    public double squareRoot(double value) {
        return Math.sqrt(value);
    }

    public double square(double value) {
        return Math.pow(value, 2);
    }

    public double cube(double value) {
        return Math.pow(value, 3);
    }

    public double inverse(double value) {
        return 1 / value;
    }

    public long factorial(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Factorial of a negative number is undefined");
        }

        if (value == 0) {
            return 1;
        }

        long result = 1;

        for (int i = 1; i <= value; i++) {
            result *= i;
        }

        return result;
    }

    public double invertSignal(double value) {
        return -value;
    }

    public double evaluate(String expression) {
        return expressionEvaluator.evaluate(expression);
    }
}
