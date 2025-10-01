/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: MeioDeComunicacao.java
* Funcao...........: Modela o funcionamento de um meio de comunicacao estabelecido em uma rede de computadores,
* transmitindo informacoes de um ponto a outro, dando continuidade ao fluxo de envio entre as camadas.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Controlador da GUI
import util.Util; //Classe utilitaria
 
public class MeioDeComunicacao {

  /****************************************************************
  * Metodo: meioDeComunicacao
  * Funcao: Simula a transmissao de dados em um meio de comunicacao, passando de um ponto Transmissor para um ponto Receptor
  * Parametros:
  *   - int fluxoBits[]: Fluxo de bits a ser transmitido
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: void
  ****************************************************************/
  public static void meioDeComunicacao(int fluxoBits[], ControllerTelaPrincipal controller) {
    int fluxoBitsPontoA[] = fluxoBits; //Ponto A (transmissor) recebe o fluxo de bits original
    int fluxoBitsPontoB[] = new int[fluxoBits.length]; //Ponto B (receptor) inicializa um novo array para receber os bits
    int bitParaEnviar; //Bit que sera transmitido no momento

    //Laco para processar cada bloco de inteiros de 32 bits em fluxoBits[] 
    for (int i = 0; i < fluxoBits.length; i++) {
      System.out.println("Bits enviados: " + Util.bitsParaString(fluxoBitsPontoA[i])); //Escreve no console o bloco enviado

      int bits = fluxoBitsPontoA[i]; //Obtem os bits do bloco atual
      int bitComparacao = 1; //Bit mascara para extracao
      bitParaEnviar = 0; //Zera o acumulador de bits

      for (int x = 0; x < 32; x++) { //Laco para processar cada bit dos 32 que compoe o bloco atual, isto eh, i
        System.out.println("Bloco de bits: " + Integer.toString(i + 1) + " Bit: " + Integer.toString(x + 1) + " == "
                          + Integer.toString((bits & bitComparacao) != 0 ? 1 : 0)); //Imprime o bit sendo processado e o bloco
        bitParaEnviar |= (bits & bitComparacao); //Acumula o bit atual para envio usando a operacao de OR nos bits e na mascara
        
        controller.atualizarSinais(); //Atualiza a visualizacao dos sinais na interface grafica
        controller.sinalizar((bits & bitComparacao) != 0 ? 1 : 0); //Define o sinal atual a ser exibido, de acordo o bit

        /************************************************
        * Tratamento especial para codificacao binaria:
        * Como a codificacao binaria nao muda o sinal no meio do bit,
        * duplicamos a exibicao para melhorar a visualizacao
        ************************************************/
        if (controller.getCodificacao() == 1) {
          controller.atualizarSinais(); //Atualiza novamente os sinais
          controller.sinalizar((bits & bitComparacao) != 0 ? 1 : 0); //Duplica o sinal atual na visualizacao, haja vista que
          //a codificacao eh binaria e nao ha 'bits de transicao', sendo o sinal HIGH ou LOW, apenas
        } //Fim do if

        bitComparacao <<= 1; //Prepara para o proximo bit, movendos os bits para a esquerda em uma posicao

        try {
          Thread.sleep(controller.getVelocidade()); //Controla a velocidade de transmissao, colocando a thread para dormir
        } catch (Exception e) {
          e.printStackTrace(); //Tratamento de excecao caso a thread seja interrompida
        } //Fim do blobo try-catch
      } //Fim do for dos bits dos inteiros de fluxoBits[]
      
      fluxoBitsPontoB[i] = bitParaEnviar; //Armazena os bits recebidos no ponto B

      System.out.println("Bits recebidos: " + Util.bitsParaString(fluxoBitsPontoB[i])); //Escreve no console o bloco recebido
    } //Fim do for dos blocos de fluxoBits[]
    
    //Envia o fluxo de bits recebido para a camada fisica receptora, dando continuidade a transmissao da mensagem
    CamadaFisicaReceptora.camadaFisicaReceptora(fluxoBitsPontoB, controller);
  } //Fim do metodo meioDeComunicacao
} //Fim da classe MeioDeComunicacao