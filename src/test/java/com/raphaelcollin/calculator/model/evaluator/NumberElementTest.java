package com.raphaelcollin.calculator.model.evaluator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberElementTest {

    @Test
    void evaluate() {
        NumberElement numberElement = new NumberElement(5);
        assertEquals(5, numberElement.evaluate());
    }
}