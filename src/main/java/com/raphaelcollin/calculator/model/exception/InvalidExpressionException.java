package com.raphaelcollin.calculator.model.exception;

public class InvalidExpressionException extends RuntimeException {
    public InvalidExpressionException(String message) {
        super(message);
    }
}
