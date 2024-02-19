package com.raphaelcollin.calculator.model.evaluator.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SumOperatorTest {

    @Test
    void testApply() {
        SumOperator sumOperator = new SumOperator();
        assertEquals(7, sumOperator.apply(5, 2));
    }
}