package com.raphaelcollin.calculator.model.evaluator.parser;

import com.raphaelcollin.calculator.model.evaluator.Element;
import com.raphaelcollin.calculator.model.evaluator.ExpressionTree;
import com.raphaelcollin.calculator.model.evaluator.NumberElement;
import com.raphaelcollin.calculator.model.evaluator.OperatorElement;
import com.raphaelcollin.calculator.model.evaluator.operator.DivisionOperator;
import com.raphaelcollin.calculator.model.evaluator.operator.MultiplicationOperator;
import com.raphaelcollin.calculator.model.evaluator.operator.SubtractionOperator;
import com.raphaelcollin.calculator.model.evaluator.operator.SumOperator;
import com.raphaelcollin.calculator.model.exception.InvalidExpressionException;

public class ExpressionParser {
    private static final String VALID_EXPRESSION_REGEX = "[-+*/.()0-9]+";
    private static final String NUMBER_REGEX = "(\\(?-)?[0-9]+(\\.[0-9]+)?\\)?";

    public ExpressionTree parse(String expression) {
        validateExpression(expression);

        Element root = parseRecursive(expression);
        return new ExpressionTree(root);
    }

    private Element parseRecursive(String expression) {
        if (expression.matches(NUMBER_REGEX)) {
            return new NumberElement(Double.parseDouble(expression.replaceAll("[()]", "")));
        }

        int index = searchForOperator(expression, '+');

        if (index >= 0) {
            return new OperatorElement(
                    new SumOperator(),
                    parseRecursive(expression.substring(0, index)),
                    parseRecursive(expression.substring(index + 1))
            );
        }

        index = searchForOperator(expression, '-');
        if (index >= 0) {
            return new OperatorElement(
                    new SubtractionOperator(),
                    parseRecursive(expression.substring(0, index)),
                    parseRecursive(expression.substring(index + 1))
            );
        }

        index = searchForOperator(expression, '*');
        if (index >= 0) {
            return new OperatorElement(
                    new MultiplicationOperator(),
                    parseRecursive(expression.substring(0, index)),
                    parseRecursive(expression.substring(index + 1))
            );
        }

        index = searchForOperator(expression, '/');
        if (index >= 0) {
            return new OperatorElement(
                    new DivisionOperator(),
                    parseRecursive(expression.substring(0, index)),
                    parseRecursive(expression.substring(index + 1))
            );
        }

        throw new InvalidExpressionException("Invalid expression: " + expression);
    }

    private void validateExpression(String expression) {
        if (!expression.matches(VALID_EXPRESSION_REGEX)) {
            throw new InvalidExpressionException("Invalid expression: " + expression);
        }
    }

    private int searchForOperator(String expression, char operator) {
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (expression.charAt(i) == operator) {
                if (i == 0 || expression.charAt(i - 1) == '(') {
                    continue;
                }
                return i;
            }
        }

        return -1;
    }
}
