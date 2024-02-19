package com.raphaelcollin.calculator.model.evaluator.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiplicationOperatorTest {

    @Test
    void testApply() {
        MultiplicationOperator multiplicationOperator = new MultiplicationOperator();
        assertEquals(10, multiplicationOperator.apply(5, 2));
    }
}