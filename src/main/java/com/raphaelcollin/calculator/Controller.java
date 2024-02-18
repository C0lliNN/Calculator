package com.raphaelcollin.calculator;

import com.raphaelcollin.calculator.model.Calculator;
import com.raphaelcollin.calculator.model.Expression;
import com.raphaelcollin.calculator.model.evaluator.ExpressionEvaluator;
import com.raphaelcollin.calculator.model.exception.InvalidFactorialException;
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
    private static final int MAX_CHARACTERS = 17;

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

    private final Calculator calculator = new Calculator(new ExpressionEvaluator());
    private final Expression expression = new Expression();

    public void initialize() {
        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        borderPane.setPadding(new Insets(screenSize.getHeight() * 0.02777,
                screenSize.getWidth() * 0.015625, screenSize.getHeight() * 0.009259,
                screenSize.getWidth() * 0.015625));

        gridPane.setHgap(screenSize.getWidth() * 0.00677);
        gridPane.setVgap(screenSize.getWidth() * 0.00677);
        BorderPane.setMargin(gridPane, new Insets(screenSize.getHeight() * 0.0324, 0, 0, 0));

        expression.subscribe(e -> label.setText(e.getExpression()));

        label.setFont(new Font("Verdana", screenSize.getWidth() * 0.018));
        label.setWrapText(false);

        // Max = 17 Characters
        label.textProperty().addListener((observable, oldValue, newValue) -> {
            if (label.getText().length() > MAX_CHARACTERS) {
                label.setText(label.getText().substring(0, MAX_CHARACTERS));
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
        expression.removeLastCharacter();
    }

    @FXML
    public void handleClearButton() {
        expression.clearExpression();
    }

    @FXML
    public void handlePercentage() {
        try {
            double value = calculateExpression();
            double result = calculator.percentage(value);
            expression.setOperationResult(result);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    public void handleSquareRoot() {
        try {
            double value = calculateExpression();
            double result = calculator.squareRoot(value);
            expression.setOperationResult(result);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    public void handleOperatorsButtons(ActionEvent event) {
        Button button = (Button) event.getSource();
        switch (button.getText()) {
            case "+":
                expression.addOperator("+");
                break;
            case "-":
                expression.addOperator("-");
                break;
            case "×":
                expression.addOperator("*");
                break;
            case "÷":
                expression.addOperator("/");
                break;
        }

    }

    @FXML
    public void handleSquare() {
        try {
            double value = calculateExpression();
            value = calculator.square(value);
            expression.setOperationResult(value);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    public void handleCube() {
        try {
            double value = calculateExpression();
            value = calculator.cube(value);
            expression.setOperationResult(value);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    public void handleNumbersButtons(ActionEvent event) {
        Button button = (Button) event.getSource();

        expression.addNumber(Integer.parseInt(button.getText()));
    }

    @FXML
    public void handleOneDividedByX() {
        try {
            double value = calculateExpression();
            double result = calculator.inverse(value);
            expression.setOperationResult(result);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    public void handleFactorial() {
        try {
            double value = calculateExpression();
            long factorial = calculator.factorial(value);
            expression.setOperationResult(factorial);
        } catch (InvalidFactorialException e) {
            handleException(e);
        }
    }

    @FXML
    public void handlePlusOrMinus() {
        try {
            double value = calculateExpression();
            double result = calculator.invertSignal(value);
            expression.setOperationResult(result);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @FXML
    public void handlePoint() {
        expression.addDecimalPoint();
    }

    @FXML
    private void handleEqualButton() {
        try {
            double result = calculateExpression();
            expression.setOperationResult(result);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private double calculateExpression() {
        return calculator.evaluate(expression.getExpression());
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

    private void handleException(Exception e) {
        Platform.runLater((Runnable)
                Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default"));
        System.err.println(e.getMessage());
    }
}