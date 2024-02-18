package com.raphaelcollin.calculator;

import com.raphaelcollin.calculator.model.Calculator;
import com.raphaelcollin.calculator.model.expression.ExpressionEvaluator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static final String URL_IMAGE_FRACTION = "file:img/fracao.png";
    private static final String URL_IMAGE_BACKSPACE = "file:img/backspace-icon.png";
    private static final String URL_IMAGE_PLUS_MINUS = "file:img/plusorminusicon.png";
    private static final String INVALID_VALUE_TEXT = "Invalid value";
    private static final String INVALID_FORMAT_TEXT = "Formato Inválido";
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label label;
    @FXML
    public Button backspaceButton;
    @FXML
    public Button percentButton;
    @FXML
    public Button fractionButton;
    @FXML
    public Button plusOrMinusButton;

    private boolean operationPerformed = false;
    private final Calculator calculator = new Calculator(new ExpressionEvaluator());

    public void initialize() {

        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        borderPane.setPadding(new Insets(screenSize.getHeight() * 0.02777,
                screenSize.getWidth() * 0.015625, screenSize.getHeight() * 0.009259,
                screenSize.getWidth() * 0.015625));

        gridPane.setHgap(screenSize.getWidth() * 0.00677);
        gridPane.setVgap(screenSize.getWidth() * 0.00677);
        BorderPane.setMargin(gridPane, new Insets(screenSize.getHeight() * 0.0324, 0, 0, 0));

        label.setFont(new Font("Verdana", screenSize.getWidth() * 0.018));

        label.setWrapText(false);

        // Max = 17 Characters

        label.textProperty().addListener((observable, oldValue, newValue) -> {
            if (label.getText().length() > 17) {
                label.setText(label.getText().substring(0, 17));
            }


        });

        // Button general Config

        for (Node node : gridPane.getChildren()) {
            Button button = (Button) node;

            button.setOnMouseClicked(e -> scaleButton(button));

            button.setPrefSize(screenSize.getWidth() * 0.03, screenSize.getWidth() * 0.03);

            if (button.getText().equals("x²") || button.getText().equals("x³") || button.getText().equals("x!")) {
                button.setFont(new Font("Cambria Italic", screenSize.getWidth() * 0.012));
            } else {
                button.setFont(new Font("Verdana", screenSize.getWidth() * 0.011));
            }

            button.setEffect(new DropShadow(screenSize.getWidth() * 0.0015625, Color.BLACK));

        }

        percentButton.setText("%");

        ImageView imageView = new ImageView(new Image(URL_IMAGE_FRACTION));

        imageView.setFitWidth(screenSize.getWidth() * 0.016);
        imageView.setFitHeight(screenSize.getWidth() * 0.016);

        fractionButton.setGraphic(imageView);

        ImageView imageView2 = new ImageView(new Image(URL_IMAGE_BACKSPACE));

        imageView2.setFitWidth(screenSize.getWidth() * 0.016);
        imageView2.setFitHeight(screenSize.getWidth() * 0.016);

        backspaceButton.setGraphic(imageView2);

        ImageView imageView3 = new ImageView(new Image(URL_IMAGE_PLUS_MINUS));

        imageView3.setFitWidth(screenSize.getWidth() * 0.015);
        imageView3.setFitHeight(screenSize.getWidth() * 0.015);

        plusOrMinusButton.setGraphic(imageView3);

    }

    @FXML
    public void handleDeleteButton() {

        label.setText(label.getText().trim());

        int size = label.getText().length();

        if (label.getText().endsWith(")")) {
            int index = label.getText().indexOf("(");
            label.setText(label.getText().substring(0, index));
        } else {
            label.setText(label.getText().substring(0, size - 1));
            if (label.getText().isEmpty()) {
                label.setText("0");
            }

            label.setText(label.getText().trim());
        }

    }

    @FXML
    public void handleClearButton() {
        label.setText("0");
        operationPerformed = false;
    }

    @FXML
    public void handlePercent() {
        String expressao = label.getText().trim();

        double value;

        if (expressao.matches("^\\d+")) {
            value = calculator.percentage(Double.parseDouble(expressao));
            label.setText(String.format("%.2f", value));

        } else {
            // Expression with an operator
            Pattern pattern = Pattern.compile("(.*) (.*)$");
            Matcher matcher = pattern.matcher(expressao);

            if (matcher.find()) {
                try {
                    value = calculator.percentage(Double.parseDouble(matcher.group(2).trim()));

                    label.setText(label.getText().replaceAll("(.*) (.*)$", matcher.group(1) +
                            String.format(" %.2f", value)));
                } catch (NumberFormatException e) {
                    return;
                }
            }
        }

        label.setText(label.getText().replaceAll(",", "."));

    }

    @FXML
    public void handleSquareRoot() {

        handleEqualButton();

        double value = Double.parseDouble(label.getText());
        if (value < 0) {
            label.setText(INVALID_VALUE_TEXT);
        } else {
            double result = calculator.squareRoot(value);
            label.setText(String.format("%f", result));
        }

        handleDecimalCases();

        operationPerformed = true;
    }

    @FXML
    public void handleOperatorsButtons(ActionEvent event) {
        Button button = (Button) event.getSource();

        String expression = label.getText().trim();

        if (label.getText().endsWith("(-)")) {
            label.setText(label.getText().replace("(-)", ""));
        }
        if (button.getText().equals("-") && label.getText().equals("0")) {
            label.setText("-");
        }
        if (!expression.endsWith(button.getText())) {
            handleOperatorClick(button.getText());
        }
    }


    @FXML
    public void handlePow(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();

        double value;

        handleEqualButton();

        value = Double.parseDouble(label.getText());

        if (text.equals("x²")) {
            value = calculator.square(value);
        } else {
            value = calculator.cube(value);
        }

        label.setText(String.format("%f", value));

        handleDecimalCases();

        operationPerformed = true;

    }

    @FXML
    public void handleNumbersButtons(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (checkInput() || operationPerformed) {
            label.setText(button.getText());
            operationPerformed = false;
        } else if (hasParenthesesInExpression()) {

            int length = label.getText().length();
            label.setText(label.getText().substring(0, length - 1) + button.getText() +
                    label.getText().substring(length - 1, length));

        } else {
            label.setText(label.getText() + button.getText());

        }
    }

    @FXML
    public void handleOneDividedByX() {
        handleEqualButton();

        double value = Double.parseDouble(label.getText());

        if (value != 0) {
            double result = calculator.inverse(value);

            label.setText(String.format("%f", result));
            handleDecimalCases();

            operationPerformed = true;
        }
    }

    @FXML
    public void handleFatorial() {
        handleEqualButton();

        if (label.getText().contains(".") || label.getText().trim().startsWith("-") ||
                Integer.parseInt(label.getText()) > 65) {
            Platform.runLater((Runnable)
                    Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default"));
            return;
        }

        long value = Long.parseLong(label.getText());
        long factorial = calculator.factorial(value);

        label.setText(String.format("%d", factorial));

        operationPerformed = true;
    }

    @FXML
    public void handlePlusOrMinus() {
        label.setText(label.getText().trim());
        if (!label.getText().equals("0")) {
            handleEqualButton();
            double number = Double.parseDouble(label.getText());
            double result = calculator.invertSignal(number);
            label.setText(String.format("%f", result));
            handleDecimalCases();
        }
    }


    @FXML
    public void handlePoint() {

        if ((!(checkLastCharOperator() || checkInput()) && !hasPointInExpression()) ||
                label.getText().equals("0")) {

            label.setText(label.getText() + ".");
            operationPerformed = false;
        }
    }

    @FXML
    private void handleEqualButton() {
        try {
            Double result = calculator.evaluate(label.getText().trim());
            String formattedResult = String.format("%f", result);
            label.setText(formattedResult);
            handleDecimalCases();
            operationPerformed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkInput() {
        return label.getText().equals("0") || label.getText().equals(INVALID_VALUE_TEXT)
                || label.getText().equals(INVALID_FORMAT_TEXT);
    }

    private boolean checkLastCharOperator() {
        String text = label.getText().trim();

        return text.matches(".*[+×÷" + Pattern.quote("-") + "]$");

    }


    private void handleOperatorClick(String operator) {
        boolean result = checkLastCharOperator();

        if (!result) {
            label.setText(label.getText() + " " + operator + " ");
        } else {
            int size = label.getText().length();
            if (operator.equals("-")) {
                label.setText(label.getText() + "(-)");
            } else {
                if (size > 1) {
                    label.setText(label.getText().substring(0, size - 2));
                } else {
                    label.setText(label.getText().substring(0, size - 1));
                }

                label.setText(label.getText() + operator + " ");
            }
        }
        operationPerformed = false;

    }

    private void handleDecimalCases() {

        String expression = label.getText().replaceAll(",", ".");

        Pattern pattern = Pattern.compile("(.*" + Pattern.quote(".") + ".*)0+$");
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            expression = (matcher.group(1));
            matcher = pattern.matcher(expression);
        }
        pattern = Pattern.compile("^(.*)(" + Pattern.quote(".") + "0?)$");
        matcher = pattern.matcher(expression);

        if (matcher.find()) {
            expression = matcher.group(1);
        }

        label.setText(expression);
    }

    private boolean hasParenthesesInExpression() {
        return label.getText().contains("(") && label.getText().contains(")");
    }

    private boolean hasPointInExpression() {
        return label.getText().contains(".");
    }


    private void scaleButton(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.03);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1.03);
        scaleTransition.setCycleCount(2);

        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

}