/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: Principal.java
* Funcao...........: Realiza as importacoes necessarias para o pleno funcionamento proposto ao JavaFX no trabalho, inicia a aplicacao, 
* inicializa a tela principal do programa, carregando o respectivo arquivo fxml e executa a aplicacao escolhida.
****************************************************************/

//Importacoes de classes para a aplicacao JavaFX
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.ControllerTelaPrincipal; //Importacao do controller da aplicacao

//Importacao de classes de modelagem para o projeto de simulacao de uma rede de computadores
import model.MeioDeComunicacao;
import model.CamadaFisicaTransmissora;
import model.CamadaAplicacaoTransmissora;

@SuppressWarnings("unused") //Notacao de supressao de avisos

public class Principal extends Application {
  /****************************************************************
  * Metodo: main
  * Funcao: Metodo que lanca os argumentos de linhas de comando, isto eh, o ponto central de execucao do programa.
  * Parametros: String[] args - os argumentos de linhas de comando
  * Retorno: void
  ****************************************************************/
  public static void main(String[] args) {
    launch(args); //Lanca uma aplicacao autonoma com os argumentos de linhas de comando args, como parametro
  } //Fim do metodo main

  /****************************************************************
  * Metodo: start
  * Funcao: Metodo da classe Application que carrega o arquivo fxml da tela principal da aplicacao e monta a cena, o palco e exibe ao usuario.
  * Parametros: Stage primaryStage, o palco da aplicacao JavaFX
  * Retorno: void
  ****************************************************************/
  @Override //Notacao de sobrescricao de um metodo abstrato da classe Application
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/telaPrincipal.fxml")); //Carregamento do arquivo fxml da telaPrincipal para a variavel loader
    Parent root = loader.load(); //Definicao do no raiz (root) com o fxml da telaPrincipal e carregamento do respectivo controller pelo metodo .load()
    Scene scene = new Scene(root); //Definicao da cena (scene) com o root como parametro
    primaryStage.setScene(scene); //Definicao do palco (primaryStage) com a scene como parametro
    primaryStage.getIcons().add(
        new Image(getClass().getResourceAsStream("/assets/app-icon.png")) //Instancia uma imagem para ser icone da aplicacao
    );
    primaryStage.setTitle("CAMADA FRUTIGER: REDES DE COMPUTADORES"); //Definicao do titulo do Stage
    primaryStage.resizableProperty().setValue(false); //Definicao do Stage para nao redimensionavel, em prol de evitar erros com a parte grafica
    /*primaryStage.initStyle(StageStyle.UNDECORATED); //Definicao do estilo do Stage para nao-decorado, ou seja, somente um fundo solido, sem abas*/
    primaryStage.show(); //Apresentacao do Stage para o usuario
  } //Fim do metodo start
} //Fim da classe Principal