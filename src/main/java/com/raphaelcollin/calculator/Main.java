package com.raphaelcollin.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view.fxml"));
        primaryStage.setTitle("Calculator");
        primaryStage.setResizable(false);

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        primaryStage.setScene(new Scene(root, screenSize.getWidth() * 0.22,
                screenSize.getWidth() * 0.22 * 1.2017));

        primaryStage.getIcons().add(new Image("file:img/icon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
