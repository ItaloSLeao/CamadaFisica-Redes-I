/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: CamadaAplicacaoReceptor.java
* Funcao...........: Simula a camada de aplicacao de um receptor em uma rede de computadores, de modo que ela recebe
* os bits decodificados pela camada fisica, os transforma novamente em inteiros ASCII e forma a mensagem, que sera
* repassada para a aplicacao do receptor, nivel mais proximo da interface do usuario.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Controlador da GUI
import util.Util; //Classe utilitaria

public class CamadaAplicacaoReceptora {

  /****************************************************************
  * Metodo: camadaAplicacaoReceptora
  * Funcao: Processa os bits do array recebidos, converte-os para caracteres da tabela ASCII e exibe-os na interface do controller
  * Parametros: 
  *   - int bits[]: Array de bits recebidos
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: void
  ****************************************************************/
  public static void camadaAplicacaoReceptora(int bits[], ControllerTelaPrincipal controller) {
    String mensagem = ""; //Armazenara a mensagem decodificada
    
    //Loop para processar cada inteiro no array de bits
    for (int i = 0; i < bits.length; i++) {
      if (bits[i] == 0) {break;} //Se o inteiro for 0, interrompe o processamento (caracteres ASCII > 0)
      
      mensagem += ((char) bits[i]); //Converte o inteiro para caractere e adiciona a mensagem
      controller.adicionarBitsDecodificadosTextArea(Util.para8Bits(bits[i])); //Adiciona os bits formatados a area de texto do controller
      
      try {
        Thread.sleep(controller.getVelocidade()); //A thread dorme conforme a velocidade escolhida na interface grafica
      } catch (Exception e) {
        e.printStackTrace(); //Tratamento de excecao caso a thread seja interrompida
      } //Fim do try-catch

    } //Fim do for de conversao bits em ASCII
    
    for (int i = 0; i < bits.length; i++) { //Loop para exibir a conversao de cada inteiro para ASCII
      //Atualiza o texto do painel de numeros para ascii com o inteiro da tabela e o caractere original
      controller.adicionarIntParaAsciiTextArea(Integer.toString(bits[i]) + " = " + (char) (bits[i]) + ";");
      System.out.println(bits[i] + " = " + (char) (bits[i]) + ";"); //Imprime a conversao int -> ascii no console
      
      try { 
        Thread.sleep(controller.getVelocidade()); //A thread dorme conforme a velocidade
      } catch (Exception e) {
        e.printStackTrace(); //Tratamento de excecao de interrumpcao da thread de transmissao
      } //Fim do try-catch

    } //Fim do for de exibicao

    AplicacaoReceptora.aplicacaoReceptora(mensagem, controller); //Chama a aplicacao receptora, o nivel mais proximo da interface
  } //Fim do metodo camadaAplicacaoReceptora
} //Fim da classe CamadaAplicacaoReceptora