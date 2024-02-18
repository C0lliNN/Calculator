package com.raphaelcollin.calculator.model;

import com.raphaelcollin.calculator.model.evaluator.ExpressionEvaluator;
import com.raphaelcollin.calculator.model.exception.InvalidFactorialException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private final Calculator calculator = new Calculator(new ExpressionEvaluator());

    @Test
    void percentage() {
        assertEquals(0.01, calculator.percentage(1));
        assertEquals(0.1, calculator.percentage(10));
        assertEquals(0.5, calculator.percentage(50));
        assertEquals(0.75, calculator.percentage(75));
        assertEquals(0.99, calculator.percentage(99));
        assertThrows(IllegalArgumentException.class, () -> calculator.percentage(-1));
    }

    @Test
    void squareRoot() {
        assertEquals(2, calculator.squareRoot(4));
        assertEquals(3, calculator.squareRoot(9));
        assertEquals(4, calculator.squareRoot(16));
        assertEquals(5, calculator.squareRoot(25));
        assertEquals(6, calculator.squareRoot(36));
        assertThrows(IllegalArgumentException.class, () -> calculator.squareRoot(-1));
    }

    @Test
    void square() {
        assertEquals(4, calculator.square(2));
        assertEquals(9, calculator.square(3));
        assertEquals(16, calculator.square(4));
        assertEquals(25, calculator.square(5));
        assertEquals(36, calculator.square(6));
    }

    @Test
    void cube() {
        assertEquals(8, calculator.cube(2));
        assertEquals(27, calculator.cube(3));
        assertEquals(64, calculator.cube(4));
        assertEquals(125, calculator.cube(5));
        assertEquals(216, calculator.cube(6));
    }

    @Test
    void inverse() {
        assertEquals(0.5, calculator.inverse(2));
        assertEquals(0.3333333333333333, calculator.inverse(3));
        assertEquals(0.25, calculator.inverse(4));
        assertEquals(0.2, calculator.inverse(5));
        assertEquals(0.16666666666666666, calculator.inverse(6));
        assertThrows(IllegalArgumentException.class, () -> calculator.inverse(0));
    }

    @Test
    void factorial() {
        assertEquals(1, calculator.factorial(0));
        assertEquals(1, calculator.factorial(1));
        assertEquals(2, calculator.factorial(2));
        assertEquals(6, calculator.factorial(3));
        assertEquals(24, calculator.factorial(4));
        assertThrows(InvalidFactorialException.class, () -> calculator.factorial(-1));
        assertThrows(InvalidFactorialException.class, () -> calculator.factorial(5.1));
    }

    @Test
    void invertSignal() {
        assertEquals(-2, calculator.invertSignal(2));
        assertEquals(3, calculator.invertSignal(-3));
        assertEquals(-4, calculator.invertSignal(4));
        assertEquals(-5, calculator.invertSignal(5));
        assertEquals(-6, calculator.invertSignal(6));
    }

    @Test
    void evaluate() {
        assertEquals(2, calculator.evaluate("1+1"));
        assertEquals(10, calculator.evaluate("5*2"));
        assertEquals(2, calculator.evaluate("4/2"));
        assertEquals(-1, calculator.evaluate("4-5"));
    }
}