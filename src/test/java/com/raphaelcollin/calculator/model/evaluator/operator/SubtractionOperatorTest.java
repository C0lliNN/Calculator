package com.raphaelcollin.calculator.model.evaluator.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtractionOperatorTest {

    @Test
    void apply() {
        SubtractionOperator subtractionOperator = new SubtractionOperator();
        assertEquals(3, subtractionOperator.apply(5, 2));
    }
}