package com.raphaelcollin.calculator.model.evaluator;

import com.raphaelcollin.calculator.model.evaluator.operator.MultiplicationOperator;
import com.raphaelcollin.calculator.model.evaluator.operator.SumOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperatorElementTest {

    @Test
    void testApply() {
        OperatorElement element = new OperatorElement(
                new SumOperator(),
                new NumberElement(8),
                new OperatorElement(new MultiplicationOperator(), new NumberElement(2), new NumberElement(3))
        );

        assertEquals(14, element.evaluate());
    }
}