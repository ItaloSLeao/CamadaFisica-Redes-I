/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: AplicacaoTransmissora.java
* Funcao...........: Classe responsavel por fazer a captacao da mensagem inserida na interface grafica, pelo controller,
* imprimir a mensagem no console e passa-la a proxima camada de transmissao, a CamadaAplicacaoTransmissora.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Controlador da GUI

public class AplicacaoTransmissora {

  /****************************************************************
  * Metodo: aplicacaoTransmissora
  * Funcao: Metodo que captura a mensagem da GUI, imprime no console e envia para a camada de aplicacao
  * Parametros:
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica do usuario
  * Retorno: void
  ****************************************************************/
  public static void aplicacaoTransmissora(ControllerTelaPrincipal controller){
    String mensagem = controller.getMensagem(); //Obtem a mensagem que o usuario digitou na interface grafica pelo controller
    System.out.println("Mensagem enviada: " + mensagem); //Escreve a mensagem de envio no console, como forma de controle de debug
    CamadaAplicacaoTransmissora.camadaAplicacaoTransmissora(mensagem, controller); //Chama a proxima camada de envio, passando a mensagem
    //e o controller da aplicacao como parametro para continuar a simulacao da rede
  } //Fim do metodo aplicacaoTransmissora
} //Fim da classe AplicacaoTransmissora