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
        Parent root = FXMLLoader.load(getClass().getResource("/mainwindow.fxml"));
        primaryStage.setTitle("Calculadora");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 420));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.jpg")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
