package com.raphaelcollin.calculadora;

import bsh.EvalError;
import bsh.Interpreter;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label label;
    @FXML
    public Button backspaceButton;
    @FXML
    public Button porcentagemButton;
    @FXML
    public Button fracaoButton;
    @FXML
    public Button plusOrMinusButton;

    /* Esse atributo vai servir de controle para quando for realizada uma operação (botao igual,
       botao raiz quadrada, etc..). Quando for realizada uma operação, e for acionado um número,
       este devera sobrescrever a string e não ser adicionado a ela
       Exemplo: foi realizada o operação de igual entre "4 X 4" e o resultado foi igual a 16,
       quando o botão 2 é acionado o resultado será "2" e não "162"*/

    private boolean operacaoRealizada = false;

    /* Esse atributo serve para evitar que se tenha mais de um ponto em um número
     * Exemplo: 25.212.52*/

    private boolean pontoEmNumero = false;

     /* Esse atribudo serve para lidar quando o operador de subtração for acionado e o ultimo caracter da string for
     * outro operador. Quando isso acontecer nós vamos abrir um parênteses e a partir de então os próximos números
       serão colocados dentro do parênteses até que seja realizada alguma operação ou outro operador for acionado*/

    private boolean parentesesNaExpressao = false;

    // Interpretador bsh

    private Interpreter i = new Interpreter();


    public void initialize(){

            /* Esse objeto vai servir para obter a resolução atual do computador em que está sendo executado
             a aplicação */

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        label.setFont(new Font("Verdana",dimension.width * 0.018));

        label.setWrapText(true);

            // O label não pode ter mais que 20 caracteres

        label.textProperty().addListener((observable, oldValue, newValue) -> {
            if (label.getText().length() > 20){
                label.setText(label.getText().substring(0,20));
            }

        });

            // Configurações gerais dos botões

        for (Node node: gridPane.getChildren()){
            Button button = (Button) node;

            button.setOnMouseClicked(e -> crescerButton(button));

                // Colocando o tamanho dos botões para 3% do tamanho da tela

            button.setPrefSize(dimension.width * 0.03,dimension.width * 0.03);

                // Configurando fontes específicas

            if (button.getText().equals("x²") || button.getText().equals("x³")){
                button.setFont(new Font("Cambria Italic",dimension.width * 0.012));
            } else {
                button.setFont(new Font("Verdana",dimension.width * 0.011));
            }

                // Colocando uma sombra externa em cada botão

            button.setEffect(new DropShadow(3, Color.BLACK));

        }

        porcentagemButton.setText("%");

            // Imagem do botão 1 / x

        ImageView imageView = new ImageView(new Image("file:arquivos/fracao.png"));

        imageView.setFitWidth(dimension.width * 0.016);
        imageView.setFitHeight(dimension.width * 0.016);

        fracaoButton.setGraphic(imageView);

            // Imagem do botão backspace/delete

        ImageView imageView2 = new ImageView(new Image("file:arquivos/backspace-icon.png"));

        imageView2.setFitWidth(dimension.width * 0.016);
        imageView2.setFitHeight(dimension.width * 0.016);

        backspaceButton.setGraphic(imageView2);

            // Imagem do botão + ou -

        ImageView imageView3 = new ImageView(new Image("file:arquivos/plusorminusicon.png"));

        imageView3.setFitWidth(dimension.width * 0.015);
        imageView3.setFitHeight(dimension.width * 0.015);

        plusOrMinusButton.setGraphic(imageView3);

    }

        /* OS MÉTODOS ESTÃO ORGANIZADOS DE CIMA PARA BAIXO E DA ESQUERDA PARA DIREITA SEGUINDO A ORDEM
        *  EM QUE ESTÃO APARECENDO NA INTERFACE GRÁFICA. ALGUNS MÉTODOS SÃO UTILIZADOS POR MAIS DE UM BOTÃO.
        *  NESSE CASO, ESSES BOTÕES SERÃO PULADOS*/



        /* Esse método vai deletar o último caracter da expressão, Se o caracter delatado for ")" então
       nós vamos remover o parênteses inteiro e atualizar a variável de controle

       Se o caracter deletado for "." então nós vamos atualizar a variável de controle.  */

    @FXML
    public void handleDeleteButton(){

        label.setText(label.getText().trim());

        int size = label.getText().length();

        if(label.getText().endsWith(")")){
            int index = label.getText().indexOf("(");
            label.setText(label.getText().substring(0,index));
            parentesesNaExpressao = false;

        } else {
            if (label.getText().endsWith(".")){
                pontoEmNumero = false;
            }
            label.setText(label.getText().substring(0,size-1));
            if (label.getText().isEmpty()){
                label.setText("0");
            }

            label.setText(label.getText().trim());
        }

    }

    // Esse método vai apenas limpar o contéudo do label, colocar um 0 e atualizar as variáveis de controle

    @FXML
    public void handleClearButton(){
        label.setText("0");
        operacaoRealizada = false;
        pontoEmNumero = false;
        parentesesNaExpressao = false;
    }

    /* Nesse método, iremos calcular a porcentagem do último valor presente no label
     *
     * EX: 20 ==> 0.20
     * EX: 50 x 10 ==> 50 * 0.10 */

    @FXML
    public void handlePorcentagem() {
        String expressao = label.getText().trim();

        double valor;

        if (expressao.matches("^\\d+")){
            valor = Double.parseDouble(expressao) / 100;
            label.setText(String.format("%.2f",valor));

        } else{
            Pattern pattern = Pattern.compile("(.*) (.*)$");
            Matcher matcher = pattern.matcher(expressao);

            if(matcher.find()){
                try {
                    valor = Double.parseDouble(matcher.group(2).trim()) / 100;
                    label.setText(label.getText().replaceAll("(.*) (.*)$",matcher.group(1) +
                            String.format(" %.2f",valor)));
                } catch (NumberFormatException e){
                    return;
                }

            }
        }

        label.setText(label.getText().replaceAll(",","."));
        pontoEmNumero = label.getText().contains(".");

    }

    /* Nesse método, iremos calcular a raiz quadrada do número contido no label ou a raiz quadrada do resultado
     *  da expressão contida no label.
     *
     * EX: 100 ==> 10
     * EX: 32 x 2 ==> 8 */

    @FXML
    public void handleRaizQuadradaButton(){

        handleIgualButton();

        double value = Double.parseDouble(label.getText());
        if (value < 0){
            label.setText("Valor Inválido");
        } else {
            double result = Math.sqrt(value);
            label.setText(String.format("%f",result));
        }

        handleCasasDecimais();

        operacaoRealizada = true;
        parentesesNaExpressao = false;
        pontoEmNumero = label.getText().contains(".");
    }

        /* Nesse método, nós vamos lidar com botões operadores, Se o label terminal com um (-), ou seja, um
       parentêses vazio, vamos retirar esse parentêses

       Se o último caracter for outro operador, nós vamos deletar o outro operador e colocar o novo,
       a menos que o novo operador for o da subtração, esse caso está descrito logo abaixo

       Se o label estiver apenas com o '0' e o botão de subtração for acionado, retiraremos o 0 e colocaremos
       o '-'. EX: 0 ==> -

       Assim o usuário poderá digitar números negativos

       Quando o operador de subtração é ativado e o último caracter é outro operador (452x) será aberto
        um parênteses e todos os número digitados apartir de agora, será colocado dentro do parênteses (452x(-))

       Se o último caracter for o mesmo operador que foi acionado, nós não faremos nada

       */

    @FXML
    public void handleOperatorsButtons(ActionEvent event){
        Button button = (Button) event.getSource();

        String expression = label.getText().trim();

        if(label.getText().endsWith("(-)")){
            label.setText(label.getText().replace("(-)",""));
        }
        if(button.getText().equals("-") && label.getText().equals("0")){
            label.setText("-");
        }
        if (!expression.endsWith(button.getText())){
            handleOperatorClick(button.getText());
        }

        pontoEmNumero = false;
    }

    /* Nesse método, iremos calcular o quadrado e/ou o cubo do número ou da expressão contida no label
     *  Se o botão acionado for o x², elevaremos ao quadrado
     *  Se o botão acionado for o x³, elevaremos ao cubo */

    @FXML
    public void handlePotencia(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();

        double valor;

        handleIgualButton();

        valor = Double.parseDouble(label.getText());

        if (text.equals("x²")){
            valor = valor * valor;
        } else {
            valor = valor * valor * valor;
        }

        label.setText(String.format("%f",valor));

        handleCasasDecimais();

        operacaoRealizada = true;
        parentesesNaExpressao = false;
        pontoEmNumero = label.getText().contains(".");

    }

    /* Nesse método, vamos lidar com os botões numéricos. Se o conteúdo do label for igual a "0",
       alguma string de erro ou tiver sido feita alguma operação nós vamos colocar o conteudo do
       botão acionado no label

       Exemplo : String atual: "0", botão ativado: 5, teremos essa String no label: "5"

       Se existir algum parentes da expressão, o número deverá ser colocado dentro do parentêses

       Se nenhuma das condições acima for atingida, então nos vamos acrescentar a string do botão acionado a
       string atual

       Exemplo: Stringa atual: 10, Botão ativado: 5, Resultado: "105" */

    @FXML
    public void handleNumbersButtons(ActionEvent event){
        Button button = (Button) event.getSource();

        if(verificarInput() || operacaoRealizada){
            label.setText(button.getText());
            operacaoRealizada = false;
        } else if(parentesesNaExpressao){

            int length = label.getText().length();
            label.setText(label.getText().substring(0,length-1) + button.getText() +
                    label.getText().substring(length-1,length));

        } else{
            label.setText(label.getText()+button.getText());

        }
    }

    /* Nesse método iremos calcular o resultado de 1 divido pelo valor ou pelo resultado da expressão
     * presente no label
     *
     * EX: 5 ==> 0.2
     * EX: 5 x 2 ==> 0.1 */

    @FXML
    public void handleUmSobreX() {
        handleIgualButton();

        double valor = Double.parseDouble(label.getText());

        if (valor != 0){
            valor = 1 / valor;

            label.setText(String.format("%f",valor));

            handleCasasDecimais();

            operacaoRealizada = true;
            parentesesNaExpressao = false;
            pontoEmNumero = label.getText().contains(".");
        }


    }

    /* Nesse método, iremos inverter o valor do número ou do resultado da expressão contida no label
     *
     * Ex: 5 ==> -5
     * Ex: 5 x 4 => -20 */

    @FXML
    public void handleMaisOuMenosButton(){
        label.setText(label.getText().trim());
        if (!label.getText().equals("0")){
            handleIgualButton();
            double number = Double.parseDouble(label.getText());
            number = number * (-1);
            label.setText(String.format("%f",number));
            handleCasasDecimais();
        }
    }


    /* Esse método vai colocar pontos em número e também vai evitar bugs como, por exemplo, o número ter
       dois pontos

       Exemplo: 5.25.21 */

    @FXML
    public void handlePonto(){

        if ((!(verificarUltimoCharOperador() || verificarInput()) && !pontoEmNumero) ||
                label.getText().equals("0")){

            label.setText(label.getText()+".");
            operacaoRealizada = false;
            pontoEmNumero = true;

        }
    }

    // Esse método vai calcular o resultado da expressão presente no label

    @FXML
    private void handleIgualButton(){
        String expressao = label.getText();
        expressao = expressao.replaceAll(",",".");
        expressao = expressao.replaceAll("\\s","");

            /* Se o último caracter do label é um operador ou ponto (exemplo: 5x4x), O código
             vai retirar esse caracter (exemplo: 5 x 4) */

        if (verificarUltimoCharOperador()){
            expressao = expressao.substring(0,expressao.length()-1);
        }

            /* Agora, nós vamos trocar os operadores "÷" e "×" para "/" and "*" porque o interpretador
             só consegue entender esses operadores */


        if(expressao.contains(".")){ // Isso é para corigir um bug no interpretador
            expressao = expressao.replaceAll("÷","/");
        } else{
            expressao = expressao.replaceAll("÷",".0/");
        }

        expressao = expressao.replaceAll("×","*");

            /* Aqui é onde o interpretador bsh funciona
             Se o resultado for um inteiro, O interpretador vai retornar um Integer Object
             Se o resultado for um número real, O interpretador vai retornar um Double object */

        try {
            i.eval("resultado = " + expressao);
            if(i.get("resultado") instanceof Integer){
                String result = String.format("%d",(Integer) i.get("resultado"));
                label.setText(result);
            } else if(i.eval("resultado") instanceof Double){
                String result = String.format("%f",(Double) i.get("resultado"));
                label.setText(result);
                handleCasasDecimais();
            }
            operacaoRealizada = true;
            parentesesNaExpressao = false;
        } catch (EvalError e){
            e.printStackTrace();
        }
    }

        /* Esse método vai verificar se o input é valido ou não para os métodos handleOperatorsButtons e
           handlePonto */

    private boolean verificarInput(){
        return label.getText().equals("0") || label.getText().equals("Valor Inválido")
                || label.getText().equals("Formato Inválido");
    }

        // Esse método vai verificar se o último caracter do label é um operador

    private boolean verificarUltimoCharOperador(){
        String text = label.getText().trim();

        return text.matches(".*[+×÷ " + Pattern.quote("-") + "]$");

    }

        /* Esse método vai lidar com o click em botões operadores. Se o útlimo caracter do label não for um
        *  operador, vamos acrescentar o operador que sofreu o click no final do label
        *
        * EX: 5 x 4 ==> 5 x 5 x 10
        *
        * Se o último caractere do label for um operador e o botão do operador de subtração sofrer o click,
        * vamos criar um parentêses e todos número acionados partir dali vão ser colocados dentro
        * do parentêses
        *
        * EX: 5 x ==> 5 x (-)
        *
        * Se nenhuma das condições acima foi atingida, significa que o último caracter é um operador, e o
        * operador que foi acionado não é o de subtração. Nesse caso, iremos substituir um operador por outro */

    private void handleOperatorClick(String operator){
        boolean result = verificarUltimoCharOperador();

        if (!result){
            label.setText(label.getText()+ " " + operator + " ");
            parentesesNaExpressao = false;
        }else {
            int size = label.getText().length();
            if(operator.equals("-")){
                label.setText(label.getText()+"(-)");
                parentesesNaExpressao = true;
            } else{
                if (size > 1){
                    label.setText(label.getText().substring(0,size-2));
                } else {
                    label.setText(label.getText().substring(0,size-1));
                }

                label.setText(label.getText() + operator + " ");
                parentesesNaExpressao = false;
            }
        }
        operacaoRealizada = false;

    }

        // Esse método vai remover zeros redundantes

    private void handleCasasDecimais(){

        String expression = label.getText().replaceAll(",",".");

        Pattern pattern = Pattern.compile("(.*" + Pattern.quote(".") + ".*)0+$");
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()){
            expression = (matcher.group(1));
            matcher = pattern.matcher(expression);
        }
        pattern = Pattern.compile("^(.*)(" + Pattern.quote(".") + "0?)$");
        matcher = pattern.matcher(expression);

        if (matcher.find()){
            expression = matcher.group(1);
        }

        label.setText(expression);
    }

        // Esse método vai crescer o botão e voltar ao normal em 0.2s quando o botao for acionado

    private void crescerButton(Button button){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200),button);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.03);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1.03);
        scaleTransition.setCycleCount(2);

        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
}