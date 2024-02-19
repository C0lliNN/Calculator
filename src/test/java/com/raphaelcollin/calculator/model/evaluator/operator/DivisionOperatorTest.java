package com.raphaelcollin.calculator.model.evaluator.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivisionOperatorTest {

    @Test
    void apply() {
        DivisionOperator divisionOperator = new DivisionOperator();
        assertEquals(5, divisionOperator.apply(10, 2));
    }
}