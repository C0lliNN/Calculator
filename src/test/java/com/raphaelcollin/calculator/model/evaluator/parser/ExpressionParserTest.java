package com.raphaelcollin.calculator.model.evaluator.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionParserTest {

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "-4,-4",
            "3.14,3.14",
            "3.1+4,7.1",
            "4-5,-1",
            "4+(-5),-1",
            "-4*(-5),20",
            "4*5,20",
            "4/5,0.8",
            "4-5+5*(-3),-16",
            "135*135,18225",
            "135*135+135,18360",
            "135+135*135,18360",
            "18/2+4*3,21",
            "35/(-5)*(-2)-10,4",
            "5*4*3*2*1/120,1",
            "84*4-4+5/5+9*2/2,342",
            "25+82*4-199/3.4-5*2.5-(-4.5)+2*20,326.470588235"
    })
    void parse(String expression, double expected) {
        ExpressionParser parser = new ExpressionParser();
        assertTrue(Math.abs(expected - parser.parse(expression).evaluate()) < 0.0001);
    }
}