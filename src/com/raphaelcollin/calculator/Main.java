package com.raphaelcollin.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("Calculator");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 420));
        primaryStage.getIcons().addAll(
                new Image("file:icon64x64.png"),
                new Image("file:icon32x32.png")
        );
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
