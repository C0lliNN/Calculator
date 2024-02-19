package com.raphaelcollin.calculator.model.evaluator;

import com.raphaelcollin.calculator.model.evaluator.operator.MultiplicationOperator;
import com.raphaelcollin.calculator.model.evaluator.operator.SumOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTreeTest {

    @Test
    void evaluate() {
        Element element = new NumberElement(8);
        ExpressionTree tree = new ExpressionTree(element);
        assertEquals(8, tree.evaluate());

        element = new OperatorElement(
                new SumOperator(),
                new NumberElement(8),
                new OperatorElement(new MultiplicationOperator(), new NumberElement(2), new NumberElement(3))
        );

        tree = new ExpressionTree(element);
        assertEquals(14, tree.evaluate());
    }
}