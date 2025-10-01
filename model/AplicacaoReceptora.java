/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: AplicacaoReceptora.java
* Funcao...........: Classe responsavel por receber a mensagem enviada pela camada de aplicacao, imprimi-la
* no console e comandar o controller da GUI para que a mensagem seja exibida no painel do receptor.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Controlador da GUI

public class AplicacaoReceptora {

  /****************************************************************
  * Metodo: aplicacaoReceptora
  * Funcao: Metodo que imprime a mensagem recebida no console e envia-la para exibicao na interface grafica
  * Parametros:
  *   - String mensagem: A mensagem codificada recebida da camada de aplicacao receptora
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: void
  ****************************************************************/
  public static void aplicacaoReceptora(String mensagem, ControllerTelaPrincipal controller){
    System.out.println("Mensagem recebida: " + mensagem); //Escreve a mensagem no console, como forma de depuracao
    controller.adicionarMsgRecebidaTextArea(mensagem); //Adiciona a mensagem recebida a area de texto do receptor na GUI
    controller.reativar(); //Reativa o botao e o painel de selecao apos a transmissao ser concluida
  } //Fim do metodo aplicacaoReceptora
} //Fim da classe AplicacaoReceptora