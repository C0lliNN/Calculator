package com.raphaelcollin.calculadora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/mainwindow.fxml"));
        primaryStage.setTitle("Calculadora");
        primaryStage.setResizable(false);

            /* Configuração do tamanho da janela
            *
            * Largura: 22% da largura atual do computador onde está sendo executada a aplicação
            * Altura: 47% da altura atual do computador onde está sendo executada a aplicação */

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setScene(new Scene(root, dimension.width * 0.22, dimension.height * 0.47));

        primaryStage.getIcons().add(new Image("file:arquivos/icon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
