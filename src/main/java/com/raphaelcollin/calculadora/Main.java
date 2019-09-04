package com.raphaelcollin.calculadora;

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
        Parent root = FXMLLoader.load(getClass().getResource("/janela_principal.fxml"));
        primaryStage.setTitle("Calculadora");
        primaryStage.setResizable(false);

            /* Configuração do tamanho da janela
            *
            * Largura: 22% da largura atual do computador onde está sendo executada a aplicação
            * Altura: 120% da largura da aplicação - Utizando dessa forma para manter a proporção de tela em
            * qualquer resolução */

        Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();
        primaryStage.setScene(new Scene(root, tamanhoTela.getWidth() * 0.22,
                tamanhoTela.getWidth() * 0.22 * 1.2017));

        primaryStage.getIcons().add(new Image("file:arquivos/icon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
