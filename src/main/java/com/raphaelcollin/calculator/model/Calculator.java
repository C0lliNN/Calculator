package com.raphaelcollin.calculator.model;

import com.raphaelcollin.calculator.model.evaluator.ExpressionTree;
import com.raphaelcollin.calculator.model.evaluator.parser.ExpressionParser;
import com.raphaelcollin.calculator.model.exception.InvalidFactorialException;

public class Calculator {
    private final ExpressionParser expressionParser;
    public Calculator(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public double percentage(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Percentage of a negative number is undefined");
        }
        return value / 100;
    }

    public double squareRoot(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Square root of a negative number is undefined");
        }
        return Math.sqrt(value);
    }

    public double square(double value) {
        return Math.pow(value, 2);
    }

    public double cube(double value) {
        return Math.pow(value, 3);
    }

    public double inverse(double value) {
        if (value == 0) {
            throw new IllegalArgumentException("Inverse of 0 is undefined");
        }
        return 1 / value;
    }

    public long factorial(double value) {
        if (value < 0) {
            throw new InvalidFactorialException("Factorial of a negative number is undefined");
        }

        if (Math.round(value) != value) {
            throw new InvalidFactorialException("Factorial of a non-integer number is undefined");
        }

        long integer = Math.round(value);

        if (integer == 0) {
            return 1;
        }

        long result = 1;

        for (int i = 1; i <= integer; i++) {
            result *= i;
        }

        return result;
    }

    public double invertSignal(double value) {
        if (value == 0) {
            return 0;
        }
        return -value;
    }

    public double evaluate(String expression) {
        ExpressionTree expressionTree = expressionParser.parse(expression);
        return expressionTree.evaluate();
    }
}
