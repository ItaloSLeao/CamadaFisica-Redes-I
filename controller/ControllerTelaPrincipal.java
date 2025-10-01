/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: ControllerTelaPrincipal.java
* Funcao...........: Controlador da interface grafica do usuario, definindo seu conteudo (botoes, textArea, comboBox,
* imageViews, sliders etc.) e seus comportamentos mediante chamada de metodos, cliques e demasiados eventos. 
****************************************************************/

package controller;

//Importacoes para o funcionamento do metodo de inicializacao da interface implementada
import java.net.URL;
import java.util.ResourceBundle;

//Importacoes para o funcionamento correto da aplicacao JavaFX
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Arrays; //Importacao para converter array em lista

import model.AplicacaoTransmissora; //Importacao da classe de aplicacao transmissora do programa

public class ControllerTelaPrincipal implements Initializable {
  @FXML //Notacao de insercao do id FXML
  private Label labelEnviar, labelEnviarSombra; //Instanciacao dos labels do botao de enviar mensagem

  @FXML //Notacao de insercao do id FXML
  private Button botaoEnviar, botaoFechar, botaoMinimizar; //Instancia os botoes de enviar mensagem, fechar e minimizar sobre o programa

  @FXML //Notacao de insercao do id FXML
  //Instanciacao das areas de texto que exibirao as etapas de codificacao e decodificao da mensagem, isto eh, do processo de transmissao
  private TextArea msgEnviadaTextArea, asciiParaIntTextArea, bitsTextArea, bitsCodificadosTextArea, 
                   bitsRecebidosTextArea, bitsDecodificadosTextArea, intParaAsciiTextArea, msgRecebidaTextArea;

  @FXML //Notacao de insercao do id FXML
  private ComboBox<String> comboBoxCodificacao; //Instanciacao de uma ComboBox como painel de selecao da codificacao da mensagem

  @FXML //Notacao de insercao do id FXML 
  //Instanciacao de ImageViews para representar graficamente os sinais de LOW (bit 0) transmitidos
  private ImageView lowImagem0, lowImagem1, lowImagem2, lowImagem3, lowImagem4, lowImagem5, lowImagem6, lowImagem7, 
                    lowImagem8, lowImagem9, lowImagem10, lowImagem11; //Indices ajustados para vetores

  @FXML //Notacao de insercao do id FXML
  //Instanciacao de ImageViews para representar graficamente os sinais de MID (transicao) transmitidos
  private ImageView midImagem0, midImagem1, midImagem2, midImagem3, midImagem4, midImagem5, midImagem6, midImagem7, 
                    midImagem8, midImagem9, midImagem10, midImagem11; //Indices ajustados para vetores

  @FXML //Notacao de insercao do id FXML
  //Instanciacao de ImageViews para representar graficamente os sinais de HIGH (bit 1) transmitidos
  private ImageView highImagem0, highImagem1, highImagem2, highImagem3, highImagem4, highImagem5, highImagem6, highImagem7, 
                    highImagem8, highImagem9, highImagem10, highImagem11; //Indices ajustados para vetores

  @FXML //Notacao de insercao do id FXML
  private Slider sliderDeVelocidade; //Slider que controla a velocidade de transmissao dos dados no programa

  private ImageView lowImagens[], midImagens[], highImagens[]; //Vetores que armazenarao as imagens dos sinais, para controlar suas exibicoes na interface

  //Lista observavel que armazena as opcoes de codificacao da mensagem a ser transmitida
  private String[] codificacao = {"Codificacao Binaria", "Codificacao Manchester", "Codificacao Manchester Diferencial"};

  private int sinalAnterior = 0; //Inteiro que armazena o ultimo sinal enviado pela camada fisica ao meio de comunicacao
  private int milissegundos = 802; //Inteiro que armazena a quantidade de milissegundos que a thread de transmissao vai dormir



  /****************************************************************
  * Metodo: initialize
  * Funcao: Metodo chamado para inicializar o controller apos seu elemento raiz ter sido criado. Esse metodo carrega as imageviews, 
  * os itens da combobox, define os eventos dos elementos graficos e faz todos os ajustes necessarios antes da exibicao ao usuario
  * Parametros: 
  *   - URL location: Localizacao usada para resolver caminhos relativos ao objeto raiz
  *   - ResourceBundle resources: Recursos usados para localizar o objeto raiz
  * Retorno: void
  ****************************************************************/
  @Override //Notacao de sobrescricao de um metodo herdado
  public void initialize(URL location, ResourceBundle resources) {
    lowImagens = new ImageView[]{ lowImagem0, lowImagem1, lowImagem2, lowImagem3, lowImagem4, lowImagem5, lowImagem6, lowImagem7, 
                                  lowImagem8, lowImagem9, lowImagem10, lowImagem11}; //As imagens LOW sao vetorizadas

    midImagens = new ImageView[]{ midImagem0, midImagem1, midImagem2, midImagem3, midImagem4, midImagem5, midImagem6, midImagem7, 
                                  midImagem8, midImagem9, midImagem10, midImagem11}; //As imagens MID sao vetorizadas

    highImagens = new ImageView[]{highImagem0, highImagem1, highImagem2, highImagem3, highImagem4, highImagem5, highImagem6, highImagem7, 
                                  highImagem8, highImagem9, highImagem10, highImagem11}; //As imagens HIGH sao vetorizadas

    comboBoxCodificacao.getItems().addAll(Arrays.asList(codificacao)); //Insere os tipos de codificacao disponiveis no painel de escolha
    comboBoxCodificacao.setValue(codificacao[0]); //Seleciona a codificacao binaria como opcao padrao em tempo de execucao

    for (int i = 0; i < 12; i++) { //Laco que torna todos as imagens dos sinais, exceto do LOW invisiveis
      midImagens[i].setVisible(false); //As imagens MID ficam invisiveis
      highImagens[i].setVisible(false); //As imagens HIGH ficam invisiveis
    } //Fim do laco for

    botaoEnviar.setOnAction(event -> { //Evento ouvinte para o clique no botaoEnviar
      botaoEnviar.setDisable(true); //Desabilita o botao de enviar a fim de evitar inconsistencias no programa
      comboBoxCodificacao.setDisable(true); //Desabilita o painel de selecao a fim de evitar inconsistencias no programa
      limparTextArea(); //Limpa as areas de texto, cada vez que uma nova mensagem eh enviada
      labelEnviar.setDisable(true); //Desabilita o label do botao de enviar para acompanha-lo visualmente
      labelEnviarSombra.setDisable(true); //Desabilita tambem a sombra, por consistencia visual
      AplicacaoTransmissora.aplicacaoTransmissora(this); //Chama a aplicacao transmissora, passando o controller da aplicacao do programa
    }); //Fim de setOnAction (botaoFechar)

    botaoMinimizar.setOnAction(event -> { //Evento ouvinte para o clique no botaoMinimizar
      Stage stage = (Stage) botaoMinimizar.getScene().getWindow(); //Define o stage ao qual o botao pertence
      stage.setIconified(true); //Iconifica o stage, isto eh, minimiza-o
    }); //Fim de setOnAction (botaoMinimizar)

    sliderDeVelocidade.setOnMouseDragged(Event -> { //Configura um evento ouvinte para a movimentacao do slider de velocidade
      milissegundos = 950 - 9 * (int) sliderDeVelocidade.getValue(); //O tempo que a thread dorme eh ajustado conforme o valor de velocidade do slider
      //Os milissegundos variam de 950ms de sono (valor do slider == 0) a 50ms de sono (valor do slider == 100)
    }); //Fim de setOnMouseDragged

  } //Fim do metodo initialize




  /****************************************************************
  * Metodo: fechar
  * Funcao: Metodo que fecha a aplicacao, terminando a execucao da JVM
  * Parametros: void
  * Retorno: void
  ****************************************************************/
  @FXML //Notacao de insercao do id FXML
  public void fechar() {
    System.exit(0); //Termina a JVM rodando com status 0
  } //Fim do metodo fechar
  


  /****************************************************************
  * Metodo: reativar
  * Funcao: Metodo que habilita novamente os componentes desabilitados durante a transmissao da mensagem
  * Parametros: void
  * Retorno: void
  ****************************************************************/
  public void reativar() {
    Platform.runLater(()->{
      botaoEnviar.setDisable(false); //Reativa o botao de enviar
      comboBoxCodificacao.setDisable(false); //Reativa o painel de selecao
      labelEnviar.setDisable(false); //Reabilita o label do botao enviar
      labelEnviarSombra.setDisable(false); //Reabilita o label sombra do botao enviar
    }); //Fim do metodo runLater
  } //Fim do metodo reativar

  

  /****************************************************************
  * Metodo: limparTextArea
  * Funcao: Metodo que limpa os textos da transmissao da mensagem anterior dos paineis, para a nova transmissao
  * Parametros: void
  * Retorno: void
  ****************************************************************/
  public void limparTextArea() {
    asciiParaIntTextArea.setText(""); //Reinicia o texto com ""
    bitsTextArea.setText(""); //Reinicia o texto com ""
    bitsCodificadosTextArea.setText(""); //Reinicia o texto com ""
    bitsRecebidosTextArea.setText(""); //Reinicia o texto com ""
    bitsDecodificadosTextArea.setText(""); //Reinicia o texto com ""
    intParaAsciiTextArea.setText(""); //Reinicia o texto com ""
    msgRecebidaTextArea.setText(""); //Reinicia o texto com ""
  } //Fim do metodo limparTextArea



  /****************************************************************
  * Metodo: atualizarSinais
  * Funcao: Metodo que desloca cada segmento do sinal para o segmento seguinte, 
  * dando continuidade a sequencia de sinais pela tela, como uma onda
  * Parametros: void
  * Retorno: void
  ****************************************************************/
  public void atualizarSinais() {
    Platform.runLater(() -> {
      for (int i = 11; i >= 1; i--) { //Laco regressivo para verificar e definir o estado das imagens
        //A visibilidade das imagens dos sinal LOW, MID e HIGH sao condicionadas pela situacao da imagem anterior a ela
        lowImagens[i].setVisible(lowImagens[i - 1].isVisible()); 
        midImagens[i].setVisible(midImagens[i - 1].isVisible()); 
        highImagens[i].setVisible(highImagens[i - 1].isVisible());
      } //Fim do for
    }); //Fim do metodo runLater
  } //Fim do metodo atualizarSinais



  /****************************************************************
  * Metodo: sinalizar
  * Funcao: Metodo para desenhar o novo bit de dados que chega a primeira posicao da tela. Isso eh feito
  * interpretando o valor do bit como LOW ou HIGH e ajustando a visibilidade das suas imagens e
  * a imagem do sinal de MID, mediante a diferenca entre o sinal atual e o sinalAnterior
  * Parametros: int bit - o bit que esta sendo transmitido e sera desenhado na tela
  * Retorno: void
  ****************************************************************/
  public void sinalizar(int bit) {
    Platform.runLater(() -> {
      //Torna as imagens da posicao 0 invisiveis
      highImagens[0].setVisible(false);
      midImagens[0].setVisible(false);
      lowImagens[0].setVisible(false);

      if (bit != sinalAnterior) { // Se o bit recebido for diferente ao anterior, a imagem de MID eh ativada
        midImagens[1].setVisible(true);
      } //Fim do if

      if (bit == 0) { // Se o bit recebido for 0, a imagem de LOW eh ativada
        lowImagens[0].setVisible(true);
      } else { // Se o bit recebido for 1, a imagem de HIGH eh ativada
        highImagens[0].setVisible(true);
      } //Fim do if-else

      sinalAnterior = bit; // O ultimo sinal eh armazenado com o bit atual
    }); //Fim do metodo runLater
  } //Fim do metodo sinalizar



  /****************************************************************
  * Metodo: adicionarAsciiParaIntTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto da conversao de caractere ascii para inteiro
  * Parametros: String mensagem - A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarAsciiParaIntTextArea(String mensagem) {
    Platform.runLater(() -> {
      asciiParaIntTextArea.setText(asciiParaIntTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarAsciiParaIntTextArea



  /****************************************************************
  * Metodo: adicionarBitsTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto do painel de bits 
  * Parametros: String mensagem - A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarBitsTextArea(String mensagem) {
    Platform.runLater(() -> {
      bitsTextArea.setText(bitsTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarBitsTextArea



  /****************************************************************
  * Metodo: adicionarBitsCodificadosTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto do painel de bits codificados 
  * Parametros: String mensagem - A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarBitsCodificadosTextArea(String mensagem) {
    Platform.runLater(() -> {
      bitsCodificadosTextArea.setText(bitsCodificadosTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarBitsCodificadosTextArea



  /****************************************************************
  * Metodo: adicionarBitsRecebidosTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto do painel de bits recebidos
  * Parametros: String mensagem - A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarBitsRecebidosTextArea(String mensagem) {
    Platform.runLater(() -> {
      bitsRecebidosTextArea.setText(bitsRecebidosTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarBitsRecebidosTextArea



  /****************************************************************
  * Metodo: adicionarBitsDecodificadosTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto do painel de bits decodificados 
  * Parametros: String mensagem -A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarBitsDecodificadosTextArea(String mensagem) {
    Platform.runLater(() -> {
      bitsDecodificadosTextArea.setText(bitsDecodificadosTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarBitsDecodificadosTextArea



  /****************************************************************
  * Metodo: adicionarIntParaAsciiTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto do painel de inteiros para caracteres ascii
  * Parametros: String mensagem - A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarIntParaAsciiTextArea(String mensagem) {
    Platform.runLater(() -> {
      intParaAsciiTextArea.setText(intParaAsciiTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarIntParaAsciiTextArea



  /****************************************************************
  * Metodo: adicionarMsgRecebidaTextArea
  * Funcao: Metodo que adiciona a mensagem ao texto do painel da mensagem recebida
  * Parametros: String mensagem - A mensagem a ser adicionada
  * Retorno: void
  ****************************************************************/
  public void adicionarMsgRecebidaTextArea(String mensagem) {
    Platform.runLater(() -> {
      msgRecebidaTextArea.setText(msgRecebidaTextArea.getText() + ' ' + mensagem);
    }); //Fim do metodo runLater
  } //Fim do metodo adicionarMsgRecebidaTextArea



  /****************************************************************
  * Metodo: getCodificacao
  * Funcao: Metodo que retorna o inteiro correspondente a codificacao escolhida
  * Parametros: void
  * Retorno: int - a codificacao
  ****************************************************************/
  public int getCodificacao() {
    if (comboBoxCodificacao.getValue().equals("Codificacao Binaria")) {
      return 1;
    } else if (comboBoxCodificacao.getValue().equals("Codificacao Manchester")) {
      return 2;
    } else { //Ao caso da opcao escolhida ser "Codificacao Manchester Diferencial"
      return 3;
    } //Fim do if-else
  } //Fim do metodo getCodificacao



  /****************************************************************
  * Metodo: getMensagem
  * Funcao: Metodo que retorna a mensagem escrita em msgEnviadaTextArea
  * Parametros: void
  * Retorno: String - a mensagem
  ****************************************************************/
  public String getMensagem() {
    return msgEnviadaTextArea.getText();
  } //Fim do metodo getMensagem



  /****************************************************************
  * Metodo: getVelocidade
  * Funcao: Metodo que retorna o tempo de sono da thread (associado a velocidade escolhida)
  * Parametros: void
  * Retorno: int - os milissegundos
  ****************************************************************/
  public int getVelocidade() {
    return milissegundos;
  } //Fim do metodo getVelocidade

} //Fim da classe ControllerTelaPrincipal