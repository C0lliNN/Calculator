package com.raphaelcollin.calculator.model;

import com.raphaelcollin.calculator.model.exception.PointAlreadyPresentException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

// Represents the expression that the user is building
public class Expression {
    private static final String DEFAULT_EXPRESSION = "0";
    private final StringBuilder rawExpression = new StringBuilder(DEFAULT_EXPRESSION);

    // History of operations performed on the expression
    private final Deque<Operation> history;

    // Subscribers to the expression. The subscribers are notified when the expression changes
    private final Set<Consumer<Expression>> subscribers;

    private static final String OPERATIONS_REGEX = "[+*/-]";

    public Expression() {
        this.history = new ArrayDeque<>();
        this.subscribers = new HashSet<>();
    }


    // Returns a formatted string representation of the expression
    public String getExpression() {
        String expression = rawExpression.toString();
        expression = expression.replaceAll(",",".");
        expression = expression.replaceAll("\\+"," + ");
        expression = expression.replaceAll("\\*"," × ");
        expression = expression.replaceAll("/"," ÷ ");
        expression = expression.replaceAll("\\.0","");
        return expression.replaceAll("(?<!\\()\\s*-(?![^()]*\\))"," - ");
    }

    public String getRawExpression() {
        return rawExpression.toString();
    }

    public void addOperator(String operator) {
        if (!Pattern.matches(OPERATIONS_REGEX, operator)) {
            throw new IllegalArgumentException("Invalid operator");
        }

        // The minus sign can be used to represent a negative number or as an arithmetic operation
        if (operator.equals("-")) {
            if (isEmpty()) {
                rawExpression.replace(0, 1, "-");
            } else if (wasAddArithmeticOperation()) {
                rawExpression.append("(-)");
            } else {
                rawExpression.append(operator);
            }
        } else if (!wasAddArithmeticOperation()) {
            rawExpression.append(operator);
        } else {
            rawExpression.replace(rawExpression.length() - 1, rawExpression.length(), operator);
        }

        history.push(Operation.ADD_ARITHMETIC_OPERATION);
        notifySubscribers();
    }

    public void addNumber(int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Invalid number");
        }

        // There are 3 cases to consider when adding a number to the expression
        if (wasSetOperationResult() || isEmpty()) {
            rawExpression.replace(0, rawExpression.length(), String.valueOf(number));
        } else if (hasParentheses()) {
            rawExpression.insert(rawExpression.length() - 1, number);
        } else {
            rawExpression.append(number);
        }

        history.push(Operation.ADD_NUMBER);
        notifySubscribers();
    }

    public void addDecimalPoint() throws PointAlreadyPresentException {
        boolean canAdd = true;
        for (int i = rawExpression.length() - 1; i >= 0; i--) {
            if (rawExpression.charAt(i) == '.') {
                canAdd = false;
                break;
            }
            if (Pattern.matches(OPERATIONS_REGEX, String.valueOf(rawExpression.charAt(i)))) {
                break;
            }
        }

        if (!canAdd) {
            throw new PointAlreadyPresentException("Point already present");
        }

        if (rawExpression.charAt(rawExpression.length() - 1) == ')') {
            rawExpression.insert(rawExpression.length() - 1, ".");
        } else {
            rawExpression.append(".");
        }

        history.push(Operation.ADD_DECIMAL_POINT);
        notifySubscribers();
    }

    public void clearExpression() {
        rawExpression.replace(0, rawExpression.length(), DEFAULT_EXPRESSION);
        history.push(Operation.CLEAR_EXPRESSION);
        notifySubscribers();
    }

    public void removeLastCharacter() {
        int size = rawExpression.length();

        if (rawExpression.charAt(size - 1) == ')') {
            int index = rawExpression.indexOf("(");
            rawExpression.delete(index, size);
        } else {
            rawExpression.deleteCharAt(size - 1);
        }

        if (rawExpression.length() == 0) {
            rawExpression.append(DEFAULT_EXPRESSION);
        }

        history.push(Operation.REMOVE_LAST_CHARACTER);
        notifySubscribers();
    }

    public void setOperationResult(double result) {
        history.push(Operation.SET_OPERATION_RESULT);
        rawExpression.replace(0, rawExpression.length(), String.valueOf(result));
        notifySubscribers();
    }

    public void subscribe(Consumer<Expression> subscriber) {
        subscribers.add(subscriber);
    }

    private void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.accept(this));
    }

    private boolean wasSetOperationResult() {
        return !history.isEmpty() && history.peek() == Operation.SET_OPERATION_RESULT;
    }

    private boolean wasAddArithmeticOperation() {
        return !history.isEmpty() && history.peek() == Operation.ADD_ARITHMETIC_OPERATION;
    }

    private boolean hasParentheses() {
        return rawExpression.toString().contains("(");
    }

    private boolean isEmpty() {
        return rawExpression.toString().equals(DEFAULT_EXPRESSION);
    }

    // Defines the possible operations that can be performed on the expression
    private enum Operation {
        ADD_ARITHMETIC_OPERATION("addArithmeticOperation"),
        ADD_NUMBER("addNumber"),
        ADD_DECIMAL_POINT("addDecimalPoint"),
        CLEAR_EXPRESSION("clearExpression"),
        REMOVE_LAST_CHARACTER("removeLastCharacter"),
        SET_OPERATION_RESULT("setOperationResult");

        final String operation;

        Operation(String operation) {
            this.operation = operation;
        }
    }
}
