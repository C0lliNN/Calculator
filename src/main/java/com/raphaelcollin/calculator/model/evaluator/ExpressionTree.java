package com.raphaelcollin.calculator.model.evaluator;

public class ExpressionTree {
    private final Element root;

    public ExpressionTree(Element root) {
        this.root = root;
    }

    public double evaluate() {
        return root.evaluate();
    }
}
