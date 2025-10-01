/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: CamadaFisicaReceptor.java
* Funcao...........: Classe que simula o funcionamento da camada fisica de um receptor, recebendo os bits codificados,
* enviados pelo meio de comunicacao, decodificando-os e passando-os para a CamadaAplicacaoReceptora.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Controlador da GUI
import util.Util; //Classe utilitaria

public class CamadaFisicaReceptora {

  /****************************************************************
  * Metodo: camadaFisicaReceptora
  * Funcao: Recebe o fluxo de bits e decodifica conforme o tipo de codificacao selecionado na aplicacao
  * Parametros:
  *   - int fluxoBits[]: Fluxo de bits recebido do meio de comunicacao
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: void
  ****************************************************************/
  public static void camadaFisicaReceptora(int fluxoBits[], ControllerTelaPrincipal controller) {
    for (int i = 0; i < fluxoBits.length; i++) {
      controller.adicionarBitsRecebidosTextArea(Util.bitsParaString(fluxoBits[i]) + '\n');
    } //Fim do for

    try {
      Thread.sleep(controller.getVelocidade()); //A thread dorme de acordo a velocidade na GUI
    } catch (Exception e) {
      e.printStackTrace(); //Tratamento de excecao caso a thread seja interrompida
    } //Fim do try-catch

    int bits[] = new int[1]; //Nao declaramos tamanho pois sera redefinido no switch
    
    switch (controller.getCodificacao()) { //Obtem a codificacao escolhida na interface grafica
      case 1: //Decodificacao Binaria
        bits = camadaFisicaReceptoraDecodificacaoBinaria(fluxoBits);
        break;
      case 2: //Decodificacao Manchester
        bits = camadaFisicaReceptoraDecodificacaoManchester(fluxoBits);
        break;
      default: //Decodificacao Manchester Diferencial
        bits = camadaFisicaReceptoraDecodificacaoManchesterDiferencial(fluxoBits);
        break;
    } //Fim do switch-case

    CamadaAplicacaoReceptora.camadaAplicacaoReceptora(bits, controller); //Chama a camada de aplicacao enviando-lhe a decodificacao
  } //Fim do metodo camadaFisicaReceptora



  /****************************************************************
  * Metodo: camadaFisicaReceptoraDecodificacaoBinaria
  * Funcao: Realiza a decodificacao binaria do fluxo de bits
  * Parametros:
  *   - int fluxoBits[]: Fluxo de bits a ser decodificado
  * Retorno: int[] - Array de inteiros decodificados
  ****************************************************************/
  public static int[] camadaFisicaReceptoraDecodificacaoBinaria(int fluxoBits[]) {
    //Calcula o tamanho do array de decodificacao e o declara (4 caracteres por posicao de fluxoBits)
    int decodificado[] = new int[fluxoBits.length * 4];

    for (int i = 0; i < fluxoBits.length; i++) {
      int valores[] = new int[4]; //Array para os 4 valores de 8 bits
      int aux = fluxoBits[i]; //Vetor que recebe o fluxo de bits para a comparacao

      for (int j = 0; j < 4; j++) { //Laco para cada caractere do inteiro de 32 bits
        //Operacao AND com a mascara 255 e aux retorna os mesmos valores de aux (1 eh elemento neutro da multiplicacao)
        valores[j] = aux & 255; //Extrai os ultimos 8 bits (255 = 11111111 em binario)
        aux >>= 8; //Desloca 8 bits para direita (bitwise right) para processar o proximo caractere
      } //Fim do for dos caracteres

      int novosBits[] = valores; //Vetor com os 4 novos inteiros (representando caracteres) extraidos 
      for (int x = 0; x < 4; x++) { //Laco para alocar os inteiros decodificados no array correspondente
        decodificado[i * 4 + x] = novosBits[x]; //Aloca em grupos de cada 4 inteiros
      } //Fim do for de alocacao
    } //Fim do for de decodificacao

    return decodificado; //Retorna o vetor decodificado
  } //Fim do metodo camadaFisicaReceptoraDecodificacaoBinaria



  /****************************************************************
  * Metodo: camadaFisicaReceptoraDecodificacaoManchester
  * Funcao: Realiza a decodificacao Manchester do fluxo de bits
  * Parametros:
  *   - int fluxoBits[]: Fluxo de bits a ser decodificado
  * Retorno: int[] - Array de inteiros decodificados
  ****************************************************************/
  public static int[] camadaFisicaReceptoraDecodificacaoManchester(int fluxoBits[]) {
    //Declara o vetor de decodificacao com tamanho adequado para o padrao Manchester (2 caracteres por inteiro de 32 bits)
    int decodificado[] = new int[fluxoBits.length * 2];
    int numero; //Variavel usada para reconstruir os inteiros
    int bit; //Variavel mascara usada para comparar cada um dos 32 bits
    int novoBit; //Variavel usada para receber todos os bits do grupo
    
    for (int i = 0; i < fluxoBits.length; i++) { //Laco para iterar sobre cada grupo de 32 bits em fluxoBits
      numero = 0; //Reinicia a variavel para preenche-la
      bit = 1; //Reinicia o bit mascara para comecar a comparacao do primeiro bit
      novoBit = fluxoBits[i]; //Armazena cada inteiro de 32 bits das iteracoes

      for (int j = 0; j < 8; j++) { //Laco para iterar sobre cada um dos 8 pares de bits
        /*Extrai o primeiro bit de novoBit com AND na mascara, posiciona j posicoes a esquerda e acumula em numero*/
        numero += (((novoBit & bit) != 0) ? 1 : 0) << j;
        bit <<= 2; //Avanca 2 bits a esquerda na mascara (em Manchester, os bits estao codificados em pares)
      } //Fim do for sobre os 8 bits

      decodificado[i * 2] = numero; //Armazena o primeiro caractere decodificado, a cada dois caracteres (i*2)
      numero = 0; //Reinicia a variavel
      bit = 1; //Reinicia a mascara
      novoBit >>= 16; //Avanca 16 bits na comparacao, pois em Manchester um caractere de 8 bits eh codificado em 16

      for (int j = 0; j < 8; j++) { //Itera sobre cada um dos 8 pares de bits do segundo caractere
        /*Extrai o bit de novoBit com AND na mascara, posiciona j posicoes a esquerda e acumula em numero*/
        numero += ((novoBit & bit) != 0 ? 1 : 0) << j;
        bit <<= 2;
      } //Fim do for do segundo caractere

      decodificado[i * 2 + 1] = numero; //Aloca o segundo caractere decodificado, a cada dois caracteres
    } //Fim do for de 32 bits
    return decodificado; //Retorna o vetor decodificado
  } //Fim do metodo camadaFisicaReceptoraDecodificacaoManchester



  /****************************************************************
  * Metodo: camadaFisicaReceptoraDecodificacaoManchesterDiferencial
  * Funcao: Realiza a decodificacao Manchester Diferencial do fluxo de bits
  * Parametros:
  *   - int fluxoBits[]: Fluxo de bits a ser decodificado
  * Retorno: int[] - Array de inteiros decodificados
  ****************************************************************/
  public static int[] camadaFisicaReceptoraDecodificacaoManchesterDiferencial(int fluxoBits[]) {
    int decodificado[] = new int[fluxoBits.length * 2]; //Como em Manchester, 2 caracteres por posicao de fluxoBits

    for (int i = 0; i < fluxoBits.length; i++) { //Laco que itera sobre cada grupo de 32 bits
      Boolean ultimoSinal; //Informa se o ultimo bit do par era true = HIGH ou false = LOW
      int aux = fluxoBits[i]; //Armazena o fluxo de bits
      int bit = 1; //Bit mascara de comparacao comecando no primeiro bit
      int informacao; //Bits decodificados

      if ((aux & bit) != 0) { //Se a operacao AND no grupo de 32 bits e a mascara for diferente de 0
        informacao = 1; //O bit decodificado eh 1 (...01)
        ultimoSinal = false; //O ultimo bit eh LOW(false), devido a transicao no meio do clock
      } else { //Se aux AND mascara for igual a 0 (o bit de aux eh 0, consequentemente)
        informacao = 0; //O bit decodificado eh 0 (...000)
        ultimoSinal = true; //O ultimo bit eh HIGH(true), devido a transicao no meio do clock
      } //Fim do if-else de decodificacao do primeiro bit lido

      bit <<= 2; //A mascara avanca duas posicoes a esquerda, pois Manchester codifica em pares

      for (int j = 1; j < 8; j++) { //Itera sobre cada um dos 7 pares de bits restantes do caractere

        if ((aux & bit) != 0) { //Se o bit atual eh diferente de 0 (essa operacao AND com a mascara retorna isso)

          if (ultimoSinal) { //Se o ultimo bit era true
            informacao |= (1 << j); //Mescla 1 (...01) com informacao, acumulando o bit na posicao bitwise left j
            ultimoSinal = !ultimoSinal; //Inverte o bit, pela transicao no clock
          } else { //Se o ultimo bit era false
            informacao |= (0 << j); //Acumula ...00 em informacao na posicao j a esquerda
          } //Fim do if-else de ultimoBit

        } else { //Se o bit atual eh igual a 0

          if (ultimoSinal) { //Se o ultimo bit era true
            informacao |= 0 << j; //Acumula o bit 00...0 na posicao j a esquerda em informacao
          } else { //Se o ultimo bit era false
            informacao |= 1 << j; //Acumula o bit 00...01 na posicao j a esquerda em informacao
            ultimoSinal = !ultimoSinal; //Faz a troca do ultimo bit, pela transicao no clock
          } //Fim do if-else de ultimoBit

        } //Fim do if-else do bit atual do primeiro caractere
        bit <<= 2; //Avanca a mascara duas posicoes a esquerda

      } //Fim do for dos 7 bits restantes

      decodificado[i * 2] = informacao; //Armazena os bits decodificados a cada dois caracteres, em pares
      informacao = 0; //Reinicia a variavel de acumular bits

      for (int j = 0; j < 8; j++) { //Itera sobre os 8 pares de bits do segundo caractere do grupo de 32 bits

        if ((aux & bit) != 0) { //Se o bit atual for diferente 0 (resultado da operacao AND com a mascara)

          if (ultimoSinal) { //Se o ultimoBit era true
            informacao |= 1 << j; //Acumula 1 na posicao j em informacao
            ultimoSinal = !ultimoSinal; //Realiza a troca do sinal de ultimoBit (transicao no clock)
          } else { //Se o ultimoBit era false
            informacao |= 0 << j; //Acumula 0 na posicao j em informacao
          } //Fim do if-else de ultimoBit

        } else { //Se o bit atual for igual a 0

          if (ultimoSinal) { //Se o ultimoBit era true (HIGH)
            informacao |= 0 << j; //Acumula o bit 0 na posicao j em informacao
          } else { //Se o ultimoBit era false (LOW)
            informacao |= 1 << j; //Acumula o bit 1 na posicao j em informacao
            ultimoSinal = !ultimoSinal; //Inverte o sinal de ultimoBit, devido a transicao no clock
          } //Fim do if-else de ultimoBit

        } //Fim do if-else do bit atual do segundo caractere
        bit <<= 2; //Move a mascara duas posicoes a esquerda para dar continuidade a leitura

      } //Fim do for dos 8 bits do segundo caractere

      decodificado[i * 2 + 1] = informacao; //Armazena os bits decodificados uma posicao apos o caractere anterior
    } //Fim do for sobre os 32 bits de cada inteiro de fluxoBits

    return decodificado; //Retorna o vetor decodificado
  } //Fim do metodo camadaFisicaReceptoraDecodificacaoManchesterDiferencial
} //Fim da classe CamadaFisicaReceptora