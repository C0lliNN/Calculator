package com.raphaelcollin.calculator.model.evaluator;

import com.raphaelcollin.calculator.model.evaluator.operator.Operator;

public class OperatorElement implements Element {
    private final Operator operator;
    private final Element left;
    private final Element right;

    public OperatorElement(Operator operator, Element left, Element right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate() {
        return operator.apply(left.evaluate(), right.evaluate());
    }
}
