package com.raphaelcollin.calculator.expression;

import com.raphaelcollin.calculator.model.evaluator.ExpressionEvaluator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionEvaluatorTest {

    @ParameterizedTest
    @CsvFileSource(resources = "expressions.csv", numLinesToSkip = 0)
    void testEvaluate(String expression, double expected) {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        double actual = evaluator.evaluate(expression);

        assertEquals(expected, actual);
    }
}