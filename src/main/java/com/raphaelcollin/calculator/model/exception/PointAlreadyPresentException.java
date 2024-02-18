package com.raphaelcollin.calculator.model.exception;

public class PointAlreadyPresentException extends RuntimeException {
    public PointAlreadyPresentException(String message) {
        super(message);
    }
}
