package com.raphaelcollin.calculator.model.expression;

import bsh.EvalError;
import bsh.Interpreter;

import java.util.regex.Pattern;

public class ExpressionEvaluator {
    private final Interpreter i = new Interpreter();

    public double evaluate(String expression) {
        expression = expression.replaceAll(",",".");
        expression = expression.replaceAll("\\s","");

        if (checkLastCharOperator(expression)){
            expression = expression.substring(0,expression.length()-1);
        }

        if(expression.contains(".")){ // Isso é para corigir um bug no interpretador
            expression = expression.replaceAll("÷","/");
        } else{
            expression = expression.replaceAll("÷",".0/");
        }

        expression = expression.replaceAll("×","*");
        expression = expression.replaceAll("x","*");

        try {
            i.eval("result = " + expression);
            if(i.get("result") instanceof Integer){
                return (Integer) i.get("result");
            } else if(i.get("result") instanceof Double){
                return (Double) i.get("result");
            }
        } catch (EvalError e){
           throw new RuntimeException(e);
        }

        return 0.0;
    }

    private boolean checkLastCharOperator(String expression) {
        return expression.matches(".*[+×÷" + Pattern.quote("-") + "]$");
    }

}
