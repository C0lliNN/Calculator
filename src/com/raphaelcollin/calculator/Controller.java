package com.raphaelcollin.calculator;

import bsh.EvalError;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import bsh.Interpreter;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    private TextField textField;

    private boolean lastOperation = false;

    private boolean dotInNumber = false;

    private boolean isWithBracket = false;

    private Interpreter i = new Interpreter();


    // Just darken the button when the mouse is over some button

    @FXML
    public void handleMouseOver(MouseEvent event){
        Button button = (Button) event.getSource();
        darkenButton(button);
    }

    // Just light up when the mouse is out some button

    @FXML
    public void handleMouseExit(MouseEvent event){
        Button button = (Button) event.getSource();
        lightUpButton(button);
    }

    // Handle when some number button is activated, if the text field is equal to "0" and
    // the button activated was the button 0 then we don't want to do anything because "0" is
    // the our pattern

    // if the current string in text field is equal to "0", some error string or it was made some operation we want to
    // set the button text to the text field
    // example : current string: "0", button activate: 5, we'll have this string: "5" and not "05"

    // else we want to add the activated button text to the current string
    // example: current string: 10, button activated: 5, we'll have this string: "105"

    @FXML
    public void handleNumbersButtons(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        if(textField.getText().equals("0") && button.getText().equals("0")){
            return;
        }
        if(verifyInput() || lastOperation){
            textField.setText(button.getText());
            lastOperation = false;
        } else if(!isWithBracket){
            textField.setText(textField.getText()+button.getText());
        } else{
            int length = textField.getText().length();
            textField.setText(textField.getText(0,length-1)+button.getText()+textField.getText(length-1,length));
        }
    }

    // In this event, we'll handle the operators click, if the last char is other operator, we'll delete the other operator
    // and set the new operator
    // if the last operator is equal to new operator, we won't do anything

    // When the minus operator is activated and the last char is other operators (452x) the result is (452x(-))
    // if the use don't type any number inside bracket, this string ("(-)") will be remover when the next operator is activated

    @FXML
    public void handleOperatorsButtons(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        if(textField.getText().contains("(-)")){
            textField.setText(textField.getText().replace("(-)",""));
        }
        if(button.getText().equals("-") && textField.getText().equals("0")){
            textField.setText("-");
        }else{
            handleOperatorClick(button.getText().charAt(0));
        }

        dotInNumber = false;
    }

    // I create the dotInNumber to avoid bug of the expression string have two dots in one number
    // Example : 5.23.21

    @FXML
    public void handleDotButton(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        if (textField.getText().equals("0")){
            textField.setText(textField.getText()+".");
            lastOperation = false;
            dotInNumber = true;
            return;
        }
        if (!verifyLastChar() && !dotInNumber){
            textField.setText(textField.getText()+".");
            lastOperation = false;
            dotInNumber = true;
        }
    }

        // first, we'll try to invert the number, if expression string is equal to "5" the result is equal to "-5"
        // If the expression string is equal to "5 x 4" for example, we'll try to solve the expression and then
        // invert the result else we'll set the text-field to "Invalid Format"

    @FXML
    public void handleMoreOrLessButton(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        try {
            invertNumber();

        } catch (NumberFormatException e){
            equalButton();
            try {
                invertNumber();
            } catch (NumberFormatException ew){
                textField.setText("Invalid format");
            }
        }
    }

    // In this event, we'll just the the text field to "0" and update the controls variables

    @FXML
    public void handleCButton(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        textField.setText("0");
        lastOperation = false;
        dotInNumber = false;
        isWithBracket = false;
    }

    // This event delete the last char of the expression string,
    // if the deleted char is "." then we'll update the control variable
    // if the deleted char is ")" the we'll remove the entire bracket and update the control variable

    @FXML
    public void handleBackSpaceButton(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        int size = textField.getText().length();
        if(textField.getText().charAt(size-1) == ')'){
            int index = textField.getText().indexOf("(");
            textField.setText(textField.getText(0,index));
            isWithBracket = false;
            return;
        }
        if (textField.getText().charAt(size-1) == '.'){
            dotInNumber = false;
        }
        textField.setText(textField.getText().substring(0,size-1));
        if (textField.getText().isEmpty()){
            textField.setText("0");
        }
    }


    // first, we'll try to calculate the square root of the current expression string, if we can not
    // we'll try to solve the expression. If we can not do this we'll set this message in text field
    // "Invalid Format"

    @FXML
    public void handleSquareRootButton(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        try{
            double value = Double.parseDouble(textField.getText());
            if (value < 0){
                textField.setText("Invalid value");
                return;
            }
            double result = Math.sqrt(value);
            textField.setText(String.format("%f",result));
            lastOperation = true;
            isWithBracket = false;
            handleDecimalPlaces();
        } catch (NumberFormatException e){
            equalButton();
            try {
                double number = Double.parseDouble(textField.getText());
                double result = Math.sqrt(number);
                textField.setText(String.format("%f",result));
                isWithBracket = false;
                handleTextFieldLength();
                handleDecimalPlaces();
            } catch (NumberFormatException ew){
                textField.setText("Invalid format");
            }
        }
        dotInNumber = textField.getText().contains(".");
    }

        // EQUAL BUTTON

    @FXML
    public void handleEqualButton(ActionEvent event){
        Button button = (Button) event.getSource();
        growUpButton(button);
        equalButton();
    }


    // This method will calculate the expression in text field

    private void equalButton(){
        String expression = textField.getText();

            // if the last char of the expression string is an operator or dot (example: 5x4x), the code
            // will cut this char (example: 5 x 4)

        if (verifyLastChar()){
            expression = expression.substring(0,expression.length()-1);
        }

            // Now, we'll change the operators "÷" and "x" to "/" and "*" because the interpreter
            // only can understand these operators

        int index = expression.indexOf("÷");

        if (index >= 0){
            if(expression.contains(".")){ // This is to fix a bug in bsg Interpreter
                expression = expression.replaceAll("÷","/");
            } else{
                expression = expression.replaceAll("÷",".0/");
            }
        }
        index = expression.indexOf("x");
        if (index >= 0){
            expression = expression.replaceAll("x","*");

            // Here is where the bsh interpreter works
            // If the result is an integer, the interpreter will return the Integer object
            // If the result is an double, the interpreter will return the Double object
        }
        try {
            i.eval("result = "+expression);
            if(i.get("result") instanceof Integer){
                String result = String.format("%d",(Integer) i.get("result"));
                textField.setText(result);
            } else if(i.eval("result") instanceof Double){
                String result = String.format("%f",(Double) i.get("result"));
                textField.setText(result);
                handleDecimalPlaces();
            }
            lastOperation = true;
            isWithBracket = false;
        } catch (EvalError e){
            e.printStackTrace();
        }
    }

        // This method will check if this input is valid or not

    private boolean verifyInput(){
        return textField.getText().equals("0") || textField.getText().equals("Invalid value")
                || textField.getText().equals("Invalid format");
    }

        // This method will check the input for operations buttons

    private int verifyLastChar(char operator){
        String text = textField.getText();
        int size = text.length();
        char lastChar = text.charAt(size-1);
        if (lastChar == operator){
            return 0;
        } else if (lastChar == '+' || lastChar == '-' || lastChar == 'x' || lastChar == '÷'){
            return -1;
        } else {
            return 1;
        }
    }

        // This method will also check the input

    private boolean verifyLastChar(){
        int size = textField.getText().length();
        char lastChar = textField.getText().charAt(size-1);
        return lastChar == '+' || lastChar == '-' || lastChar == 'x' || lastChar == '÷' || lastChar == '.' || verifyInput();
    }

        // This method will handle the operator click

    private void handleOperatorClick(char operator){
        int result = verifyLastChar(operator);
        if (result == 1){
            textField.setText(textField.getText()+operator);
            isWithBracket = false;
        }
        if (result == -1){
            int size = textField.getText().length();
            if(operator == '-'){
                textField.setText(textField.getText()+"(-)");
                isWithBracket = true;
            } else{
                textField.setText(textField.getText(0,size-1));
                textField.setText(textField.getText()+operator);
                isWithBracket = false;
            }

        }
        lastOperation = false;

    }

        // This method will remove redundant zeros

    private void handleDecimalPlaces(){
        String pieces[] = textField.getText().split(Pattern.quote("."));
        for (int c = pieces[1].length()-1; c >= 0; c--){
            char lastChar = pieces[1].charAt(c);
            if (lastChar == '0' || lastChar == '.'){
                int size = textField.getText().length();
                textField.setText(textField.getText().substring(0,size-1));
                if (textField.getText().isEmpty()){
                    textField.setText("0");
                }
            }
            else{
                return;
            }

            if (textField.getText().charAt(textField.getText().length()-1) == '.'){
                int size = textField.getText().length();
                textField.setText(textField.getText().substring(0,size-1));
            }
        }
    }

        // This method won't allow the text filed have more of 15 characters

    private void handleTextFieldLength(){
        final int textFieldLength = 15;
        if (textField.getText().length() > 15)
            textField.setText(textField.getText().substring(0,textFieldLength));
    }

        // This method will invert the sign of the number

    private void invertNumber(){
        double number = Double.parseDouble(textField.getText());
        number = number * (-1);
        textField.setText(String.format("%f",number));
        handleTextFieldLength();
        handleDecimalPlaces();
    }

        // This method will growUp and back to normal the button in 0.2s

    private void growUpButton(Button button){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200),button);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.03);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1.03);
        scaleTransition.setCycleCount(2);

        scaleTransition.setAutoReverse(true);

        scaleTransition.play();
    }

        // This methods will darken the button when the mouse entered the button

    private void darkenButton(Button button){

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);

        button.setEffect(colorAdjust);
    }

        // This method will lightUp the button when the mouse exited the button

    private void lightUpButton(Button button){

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);

        button.setEffect(colorAdjust);

    }
}