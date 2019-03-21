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

    private boolean operacaoRealizada = false;

    /* Esse atributo vai servir de controle para quando for realizada uma operacao (botao igual, botao raiz quadrada
    * etc..). Quando for realizada uma operação, e for acionado um número, este devera sobrescrever a string e não
    *  ser adicionado a ela
    *  Exemplo: foi realizada o operação de igual entre "4 X 4" e o resultado foi igual a 16, quando o botão 2 é acionado
    *  o resultado será "2" e não "162"*/

    private boolean pontoEmNumero = false;

    /* Esse atributo serve para evitar que se tenha mais de um ponto em um número
    * Exemplo: 25.212.52*/

    private boolean parentesesNaExpressao = false;

    /* Esse atribudo serve para lidar quando o operador de subtração for acionado e o ultimo caracter da string for
     * outro operador. Quando isso acontecer nós vamos abrir um parênteses e a partir de então os próximos números
       serão colocados dentro do parênteses até que seja realizada alguma operação ou outro operador for acionado*/

    private Interpreter i = new Interpreter();


    // Esse método vai escurecer o botão quando o mouse passar por cima dele

    @FXML
    public void handleMouseOver(MouseEvent event){
        Button button = (Button) event.getSource();
        escurecerButton(button);
    }

    // Esse método vai clarear o botão quando o mouse sair dele

    @FXML
    public void handleMouseExit(MouseEvent event){
        Button button = (Button) event.getSource();
        lightUpButton(button);
    }

    /* Nesse método, vamos lidar com os botões de números. Se o conteúdo do text field for igual a "0" e
       o botão ativado for o botão 0 então, nós não vamos fazer nada
       Se o conteúdo do text field for igual a "0", alguma string de erro ou tiver sido feita alguma operação
       nós vamos colocar o conteudo do botão acionado no text field
       Exemplo : String atual: "0", botão ativado: 5, nós teremos essa String: "5"

       Se nenhuma das condições acima for atingida, então nos vamos acrescentar a string do botão acionado a string atual
       Exemplo: Stringa atual: 10, Botão ativado: 5, Resultado: "105" */

    @FXML
    public void handleNumbersButtons(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        if(textField.getText().equals("0") && button.getText().equals("0")){
            return;
        }
        if(verificarInput() || operacaoRealizada){
            textField.setText(button.getText());
            operacaoRealizada = false;
        } else if(!parentesesNaExpressao){
            textField.setText(textField.getText()+button.getText());
        } else{
            int length = textField.getText().length();
            textField.setText(textField.getText(0,length-1)+button.getText()+textField.getText(length-1,length));
        }
    }

    /* Nesse método, nós vamos lidar com botões operadores, Se o último caracter for outro operador, nós vamos
       deletar o outro operador e colocar o novo, a menos que o novo operador for o da subtração, esse caso está descrito
       logo abaixo

       Se o último caracter for o mesmo operador que foi acionado, nós não faremos nada

       Quando o operador de subtração é ativado e o último caracter é outro operador (452x) será aberto um parênteses
       e todos os número digitados apartir de agora, será colocado dentro do parênteses (452x(-))*/

    @FXML
    public void handleOperatorsButtons(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        if(textField.getText().contains("(-)")){
            textField.setText(textField.getText().replace("(-)",""));
        }
        if(button.getText().equals("-") && textField.getText().equals("0")){
            textField.setText("-");
        }else{
            handleOperatorClick(button.getText().charAt(0));
        }

        pontoEmNumero = false;
    }

    /* Esse método vai colocar pontos em número e também vai evitar bugs como, por exemplo, o número ter dois pontos
       Exemplo: 5.25.21 */

    @FXML
    public void handlePontoEmNumero(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        if (textField.getText().equals("0")){
            textField.setText(textField.getText()+".");
            operacaoRealizada = false;
            pontoEmNumero = true;
            return;
        }
        if (!verificarUltimoChar() && !pontoEmNumero){
            textField.setText(textField.getText()+".");
            operacaoRealizada = false;
            pontoEmNumero = true;
        }
    }

        /* Primeiramento, nós vamos tentar inverter o número, Se a expressão for "5", o resultado será "-5"
           Se a expressão for igual a "5 x 4" por exemplo, nós vamos tentar resolver a expressão e então
           inverter o resultado, se não conserguimos calcular a expressão, vamos exibir a mensagem "Formato Inválido
           no text field */

    @FXML
    public void handleMaisOuMenosButton(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        try {
            InverterNumero();

        } catch (NumberFormatException e){
            igualButton();
            try {
                InverterNumero();
            } catch (NumberFormatException ew){
                textField.setText("Formato Inválido");
            }
        }
    }

    // Esse método vai apenas limpar o contéudo do text field, colocar um 0 e atualizar as variáveis de controle

    @FXML
    public void handleClearButton(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        textField.setText("0");
        operacaoRealizada = false;
        pontoEmNumero = false;
        parentesesNaExpressao = false;
    }

    /* Esse método vai deletar o último caracter da expressão, Se o caracter deletado for "." então
       Nós vamos atualizar a variável de controle. Se o caracter delatado for ")" então nós vamos remover o parênteses
       inteiro e atualizar a variável de controle */

    @FXML
    public void handleDeleteButton(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        int size = textField.getText().length();
        if(textField.getText().charAt(size-1) == ')'){
            int index = textField.getText().indexOf("(");
            textField.setText(textField.getText(0,index));
            parentesesNaExpressao = false;
            return;
        }
        if (textField.getText().charAt(size-1) == '.'){
            pontoEmNumero = false;
        }
        textField.setText(textField.getText().substring(0,size-1));
        if (textField.getText().isEmpty()){
            textField.setText("0");
        }
    }


    /* Primeiramente, Nós vamos verificar se o número é negativo, se for nos vamos colocar a mensagem no text field
       "Valor Inválido". Se isso não acontecer , nós vamos tentar calcular a raiz quadrada do conteúdo do text field.
        Se nõs não conseguirmos, nós vamos tentar calcular a expressão e calcular a raiz quadrado do resultado da
        expressão. Se nós não conseguirmos calcular a expressão, nós vamos colocar no text field a mensagem "Valor Inváldi*/

    @FXML
    public void handleRaizQuadradaButton(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        try{
            double value = Double.parseDouble(textField.getText());
            if (value < 0){
                textField.setText("Valor Inválido");
                return;
            }
            double result = Math.sqrt(value);
            textField.setText(String.format("%f",result));
            operacaoRealizada = true;
            parentesesNaExpressao = false;
            handleCasasDecimais();
        } catch (NumberFormatException e){
            igualButton();
            try {
                double number = Double.parseDouble(textField.getText());
                double result = Math.sqrt(number);
                textField.setText(String.format("%f",result));
                parentesesNaExpressao = false;
                handleComprimentoTextField();
                handleCasasDecimais();
            } catch (NumberFormatException ew){
                textField.setText("Formato Inválido");
            }
        }
        pontoEmNumero = textField.getText().contains(".");
    }

        // Botão Igual

    @FXML
    public void handleIgualButton(ActionEvent event){
        Button button = (Button) event.getSource();
        crescerButton(button);
        igualButton();
    }


    // Esse método vai calcular a expressão do text field

    private void igualButton(){
        String expressao = textField.getText();
        expressao = expressao.replaceAll(",",".");

            /* Se o último caracter do text field é um operador ou ponto (exemplo: 5x4x), O código
             vai retirar esse caracter (example: 5 x 4) */

        if (verificarUltimoChar()){
            expressao = expressao.substring(0,expressao.length()-1);
        }

            /* Agora, nós vamos trocar os operadores "÷" e "x" para "/" and "*" porque o interpretador
             só consegue entender esses operadores */

        int index = expressao.indexOf("÷");

        if (index >= 0){
            if(expressao.contains(".")){ // Isso é para corigir um bug no interpretador
                expressao = expressao.replaceAll("÷","/");
            } else{
                expressao = expressao.replaceAll("÷",".0/");
            }
        }
        index = expressao.indexOf("x");
        if (index >= 0){
            expressao = expressao.replaceAll("x","*");

            /* Aqui é onde o interpretador bsh funciona
             Se o resultado for um inteiro, O interpretador vai retornar um Integer Object
             Se o resultado for um número real, O interpretador vai retornar um Double object */
        }
        try {
            i.eval("resultado = "+expressao);
            if(i.get("resultado") instanceof Integer){
                String result = String.format("%d",(Integer) i.get("resultado"));
                textField.setText(result);
            } else if(i.eval("resultado") instanceof Double){
                String result = String.format("%f",(Double) i.get("resultado"));
                textField.setText(result);
                handleCasasDecimais();
            }
            operacaoRealizada = true;
            parentesesNaExpressao = false;
        } catch (EvalError e){
            e.printStackTrace();
        }
    }

        // Esse método vai verificar se o input é valido ou não

    private boolean verificarInput(){
        return textField.getText().equals("0") || textField.getText().equals("Valor Inválido")
                || textField.getText().equals("Formato Inválido");
    }

        // Esse método vai verificar o text field para operadores

    private int verificarUltimoChar(char operator){
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

        // Esse método também vai verificar o text field

    private boolean verificarUltimoChar(){
        int size = textField.getText().length();
        char lastChar = textField.getText().charAt(size-1);
        return lastChar == '+' || lastChar == '-' || lastChar == 'x' || lastChar == '÷' || lastChar == '.' || verificarInput();
    }

        // Esse método vai lidar com o click em botões operadores

    private void handleOperatorClick(char operator){
        int result = verificarUltimoChar(operator);
        if (result == 1){
            textField.setText(textField.getText()+operator);
            parentesesNaExpressao = false;
        }
        if (result == -1){
            int size = textField.getText().length();
            if(operator == '-'){
                textField.setText(textField.getText()+"(-)");
                parentesesNaExpressao = true;
            } else{
                textField.setText(textField.getText(0,size-1));
                textField.setText(textField.getText()+operator);
                parentesesNaExpressao = false;
            }

        }
        operacaoRealizada = false;

    }

        // Esse método vai remover zeros redundantes

    private void handleCasasDecimais(){
        String regex;
        if(textField.getText().contains(",")){
            regex = ",";
        } else if(textField.getText().contains(".")){
            regex = ".";
        } else{
            return;
        }
        String[] pieces;
        if(regex.equals(".")){
            pieces = textField.getText().split(Pattern.quote("."));
        } else{
            pieces = textField.getText().split(",");
        }
        for (int c = pieces[1].length()-1; c >= 0; c--){
            char lastChar = pieces[1].charAt(c);
            if (lastChar == '0' || lastChar == regex.charAt(0)){
                int size = textField.getText().length();
                textField.setText(textField.getText().substring(0,size-1));
                if (textField.getText().isEmpty()){
                    textField.setText("0");
                }
            }
            else{
                textField.setText(textField.getText().replaceAll(",","."));
                return;
            }

            if (textField.getText().charAt(textField.getText().length()-1) == regex.charAt(0)){
                int size = textField.getText().length();
                textField.setText(textField.getText().substring(0,size-1));
            }
        }
        textField.setText(textField.getText().replaceAll(",","."));
    }

        // Esse método não vai permitir que o text field tenha mais que 15 caracteres

    private void handleComprimentoTextField(){
        final int textFieldLength = 15;
        if (textField.getText().length() > 15)
            textField.setText(textField.getText().substring(0,textFieldLength));
    }

        // Esse método vai inverter o sinal do número

    private void InverterNumero(){
        double number = Double.parseDouble(textField.getText());
        number = number * (-1);
        textField.setText(String.format("%f",number));
        handleComprimentoTextField();
        handleCasasDecimais();
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

        // Esse método vai escurecer o botão quando o mouse passar por cima dele

    private void escurecerButton(Button button){

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);

        button.setEffect(colorAdjust);
    }

        // Esse método vai clarear o botão quando o mouse sair dele

    private void lightUpButton(Button button){

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);

        button.setEffect(colorAdjust);

    }
}