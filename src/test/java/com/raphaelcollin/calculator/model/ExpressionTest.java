package com.raphaelcollin.calculator.model;

import com.raphaelcollin.calculator.model.exception.PointAlreadyPresentException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    private Expression expression;

    @BeforeEach
    void setUp() {
        expression = new Expression();
    }

    @Nested
    class AddNumberClass {

        @Test
        @DisplayName("When number is negative, then it should throw an exception")
        void testAddNumberWhenNumberIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> expression.addNumber(-1));
        }

        @Test
        @DisplayName("When number is greater than 9, then it should throw an exception")
        void testAddNumberWhenNumberIsGreaterThan9() {
            assertThrows(IllegalArgumentException.class, () -> expression.addNumber(10));
        }

        @Test
        @DisplayName("When the operation result was set, then it should set the number")
        void testAddNumberWhenOperationResultWasSet() {
            expression.setOperationResult(10);
            expression.addNumber(5);

            assertEquals("5", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the expression has parentheses, then it should set the number inside the parentheses")
        void testAddNumberWhenExpressionHasParentheses() {
            expression.addNumber(5);

            expression.addOperator("*");
            expression.addOperator("-");
            expression.addNumber(3);

            assertEquals("5*(-3)", expression.getRawExpression());
        }

        @Test
        @DisplayName("Otherwise, it should append the number to the expression")
        void testAddNumberOtherwise() {
            expression.addNumber(5);
            expression.addNumber(3);

            assertEquals("53", expression.getRawExpression());
        }
    }

    @Nested
    class AddOperatorClass {

        @Test
        @DisplayName("When the operator is invalid, then it should throw an exception")
        void testAddOperatorWhenOperatorIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> expression.addOperator("a"));
        }

        @Test
        @DisplayName("When the operator is a minus sign and expression is empty, then it should set the expression to a negative number")
        void testAddOperatorWhenOperatorIsMinusSign() {
            expression.addOperator("-");

            assertEquals("-", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the operator is a minus sign and the last operation was an arithmetic operation, then it should append the minus sign to the expression")
        void testAddOperatorWhenOperatorIsMinusSignAndLastOperationWasArithmeticOperation() {
            expression.addNumber(5);
            expression.addOperator("*");
            expression.addOperator("-");

            assertEquals("5*(-)", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the operator is a minus sign and the previous conditions are not met, then it should append the minus sign to the expression")
        void testAddOperatorWhenOperatorIsMinusSignAndExpressionIsNotEmpty() {
            expression.addNumber(5);
            expression.addOperator("-");

            assertEquals("5-", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the operation is not a minus sign and the last operation was an arithmetic operation, then it should replace the last operation with the new one")
        void testAddOperatorWhenOperationIsNotMinusSignAndLastOperationWasArithmeticOperation() {
            expression.addNumber(5);
            expression.addOperator("*");
            expression.addOperator("+");

            assertEquals("5+", expression.getRawExpression());
        }

        @Test
        @DisplayName("Otherwise, it should append the operator to the expression")
        void testAddOperatorOtherwise() {
            expression.addNumber(5);
            expression.addOperator("+");

            assertEquals("5+", expression.getRawExpression());
        }
    }

    @Nested
    class AddDecimalPointClass {
        @Test
        @DisplayName("When the expression is empty, then it should append a zero and a decimal point")
        void testAddDecimalPointWhenExpressionIsEmpty() {
            expression.addDecimalPoint();

            assertEquals("0.", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the expression already has a decimal point, then it should throw an exception")
        void testAddDecimalPointWhenExpressionAlreadyHasDecimalPoint() {
            expression.addNumber(5);
            expression.addDecimalPoint();

            assertThrows(PointAlreadyPresentException.class, () -> expression.addDecimalPoint());

            expression.addNumber(3);
            assertThrows(PointAlreadyPresentException.class, () -> expression.addDecimalPoint());
        }

        @Test
        @DisplayName("When expressions has parentheses, then it should append a decimal point to the number inside the parentheses")
        void testAddDecimalPointWhenExpressionHasParentheses() {
            expression.addNumber(5);
            expression.addOperator("*");
            expression.addOperator("-");
            expression.addNumber(3);
            expression.addDecimalPoint();

            assertEquals("5*(-3.)", expression.getRawExpression());
        }

        @Test
        @DisplayName("Otherwise, it should append a decimal point to the expression")
        void testAddDecimalPointOtherwise() {
            expression.addNumber(5);
            expression.addDecimalPoint();

            assertEquals("5.", expression.getRawExpression());

            expression.addNumber(3);
            expression.addOperator("+");

            expression.addNumber(5);
            expression.addDecimalPoint();
            expression.addNumber(8);
            assertEquals("5.3+5.8", expression.getRawExpression());
        }
    }

    @Test
    @DisplayName("clear method should clear the expression")
    void testClear() {
        expression.addNumber(5);
        expression.addOperator("+");
        expression.addNumber(3);
        expression.addDecimalPoint();
        expression.addNumber(8);

        expression.clearExpression();

        assertEquals("0", expression.getRawExpression());
    }

    @Nested
    class RemoveLastCharClass {

        @Test
        @DisplayName("When the expression is empty, then it should do nothing")
        void testRemoveLastCharWhenExpressionIsEmpty() {
            expression.removeLastCharacter();

            assertEquals("0", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the expression has only one character, then it should set the expression to zero")
        void testRemoveLastCharWhenExpressionHasOnlyOneCharacter() {
            expression.addNumber(5);
            expression.removeLastCharacter();

            assertEquals("0", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the expression has a negative number, then it should set the expression to zero")
        void testRemoveLastCharWhenExpressionHasANegativeNumber() {
            expression.addOperator("-");
            expression.removeLastCharacter();

            assertEquals("0", expression.getRawExpression());
        }

        @Test
        @DisplayName("When the expression has a decimal point, then it should remove the decimal point")
        void testRemoveLastCharWhenExpressionHasADecimalPoint() {
            expression.addNumber(5);
            expression.addDecimalPoint();
            expression.removeLastCharacter();

            assertEquals("5", expression.getRawExpression());
        }

        @Test
        @DisplayName("Otherwise, it should remove the last character from the expression")
        void testRemoveLastCharOtherwise() {
            expression.addNumber(5);
            expression.addOperator("+");
            expression.addNumber(3);
            expression.removeLastCharacter();

            assertEquals("5+", expression.getRawExpression());
        }
    }

    @Test
    @DisplayName("setOperationResult method should set the operation result")
    void testSetOperationResult() {
        expression.setOperationResult(10.433333000);

        assertEquals("10.433333", expression.getRawExpression());
    }

    @Nested
    class GetExpressionClass {
        @Test
        @DisplayName("When the expression is empty, then it should return 0")
        void testGetExpressionWhenExpressionIsEmpty() {
            assertEquals("0", expression.getExpression());
        }

        @Test
        @DisplayName("When the expression has only a number, then it should return the number")
        void testGetExpressionWhenExpressionHasOnlyANumber() {
            expression.addNumber(5);

            assertEquals("5", expression.getExpression());
        }

        @Test
        @DisplayName("When the expression has multiple operators, then it should return the formatted expression")
        void testGetExpressionWhenExpressionHasMultipleOperators() {
            expression.addNumber(5);
            expression.addOperator("+");
            expression.addNumber(3);
            expression.addOperator("*");
            expression.addNumber(4);
            expression.addOperator("-");
            expression.addNumber(2);

            assertEquals("5 + 3 × 4 - 2", expression.getExpression());
        }
    }
}