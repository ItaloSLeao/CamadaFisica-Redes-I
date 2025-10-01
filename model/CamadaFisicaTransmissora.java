/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: CamadaFisicaTransmissora.java
* Funcao...........: Classe que simula o funcionamento de uma camada fisica de um transmissor, em uma rede de computadores,
* recebendo os 'quadros' da mensagem, transformando-os em um vetor de bits, mediante a codificacao escolhida na GUI, e 
* repassando os para o meio de comunicacao entre o transmissor e o receptor.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Importacao do controlador da GUI
import util.Util; //Importacao de classe utilitaria de formatacao

public class CamadaFisicaTransmissora {

  /****************************************************************
  * Metodo: camadaFisicaTransmissora
  * Funcao: Codifica os 'quadros' conforme o tipo de codificacao selecionado e envia para o meio de comunicacao,
  * iniciando o processo de transmissao fisica da mensagem
  * Parametros:
  *   - int[] quadro: Array de inteiros com os codigos ascii dos caracteres
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: void
  ****************************************************************/
  public static void camadaFisicaTransmissora(int[] quadro, ControllerTelaPrincipal controller) {
    int tipoCodificacao = controller.getCodificacao(); //Obtem o tipo de codificacao selecionado na interface
    int[] fluxoBits = new int[1]; //Inicializa o array que ira armazenar o fluxo de bits codificados

    switch (tipoCodificacao) { //Seleciona o tipo de codificacao com base na escolha do usuario
      case 1: //Codificacao binaria simples
        fluxoBits = camadaFisicaTransmissoraCodificacaoBinaria(quadro, controller);
        break;
      case 2: //Codificacao Manchester
        fluxoBits = camadaFisicaTransmissoraCodificacaoManchester(quadro, controller);
        break;
      default: //Codificacao Manchester Diferencial
        fluxoBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro, controller);
        break;
    } //Fim do bloco switch

    //Envia o fluxo de bits codificados junto ao controller para o meio de comunicacao
    MeioDeComunicacao.meioDeComunicacao(fluxoBits, controller);
  } //Fim do metodo camadaFisicaTransmissora



  /****************************************************************
  * Metodo: camadaFisicaTransmissoraCodificacaoBinaria
  * Funcao: Realiza a codificacao binaria dos quadros recebidos
  * Parametros:
  *   - int quadro[]: Vetor com os bits de cada caractere a ser codificado
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: int[] - Array de bits codificados
  ****************************************************************/
  public static int[] camadaFisicaTransmissoraCodificacaoBinaria(int quadro[], ControllerTelaPrincipal controller) {
    int tamanho = (quadro.length - 1) / 4 + 1; //Calcula o tamanho necessario para o array de bits codificados, 
    //ao caso em que em cada posicao do array esta um bloco de 32 bits, que comporta 4 caracteres de 8 bits
    int codificado[] = new int[tamanho];  //Vetor que vai armazenar os blocos de bits codificados
    int informacao; //Variavel inteira que vai empacotar os 32 bits, isto eh, um bloco de bits

    for (int i = 0; i < quadro.length; i++) //Laco para adicionar cada caractere ascii em formato de 8 bits na interface
      controller.adicionarBitsTextArea(Util.para8Bits(quadro[i]) + ' ');

    for (int i = 0; i < codificado.length; i++) { //Laco para gerar cada bloco codificado
      //Nesse momento, ao atribuir um elemento de quadro[] a um inteiro, ele estara na forma de 32 bits, como o computador enxerga
      informacao = quadro[i * 4]; //Armazena o primeiro caractere na forma de 32 bits no inteiro

      //Esses blocos ifs, a seguir, irao realizar operacoes de bitwise left com os 32 bits de cada um dos 
      //proximos 3 caracteres do quadro[] e, em seguida, vao operar OR com eles, mesclando, em um unico bloco
      //de 32 bits, os 4 caracteres de 8 bits cada (anteriormente na forma 32 bits repletos de 24 zeros) 

      if (i * 4 + 1 < quadro.length) {informacao |= (quadro[i * 4 + 1] << 8);} //Segundos 8 bits (posicoes 8-15)
      
      if (i * 4 + 2 < quadro.length) {informacao |= (quadro[i * 4 + 2] << 16);} //Terceiros 8 bits (posicoes 16-23)
      
      if (i * 4 + 3 < quadro.length) {informacao |= (quadro[i * 4 + 3] << 24);} //Quartos 8 bits (posicoes 24-31)
      
      codificado[i] = informacao; //Armazena os bits acumulados de 4 caracteres no vetor

      try {
        Thread.sleep(controller.getVelocidade()); //Controla a velocidade de codificacao, colocando a thread para dormir
      } catch (Exception e) {
        e.printStackTrace(); //Tratamento da excecao de interrupcao do sleep da thread
      } //Fim do try-catch

      //Adiciona na area de texto de bits codificados sua versao em forma de string usando a classe Util
      controller.adicionarBitsCodificadosTextArea(Util.bitsParaString(codificado[i]) + '\n');
    } //Fim do laco for de codificacao

    return codificado; //Retorna os bits codificados
  } //Fim do metodo camadaFisicaTransmissoraCodificaoBinaria




  /****************************************************************
  * Metodo: camadaFisicaTransmissoraCodificacaoManchester
  * Funcao: Realiza a codificacao Manchester dos quadros recebidos
  * Parametros:
  *   - int quadro[]: Vetor com os bits de cada caractere a ser codificado
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: int[] - Array de bits codificados
  ****************************************************************/
  public static int[] camadaFisicaTransmissoraCodificacaoManchester(int quadro[], ControllerTelaPrincipal controller) {
    /*Calcula o tamanho necessario para o array de codificacao, ao caso em que, cada posicao do vetor comporta, agora,
    * apenas dois caracteres de 8 bits (originalmente) e que, agora, pelo padrao Manchester, terao 16 bits de 'tamanho', 
    * contando com os 'bits de transicao'*/
    int tamanho = (quadro.length - 1) / 2 + 1;
    int codificado[] = new int[tamanho]; //Vetor que armazenara os inteiros com os bits ja codificados
    int informacao; //Variavel inteira que vai empacotar 32 bits, mas agora um bloco com a metade de caracteres
    int bit; //Mascara de bit usada para isolar um bit de cada vez do caractere original

    //Exibe os bits originais na interface
    for (int i = 0; i < quadro.length; i++)
      controller.adicionarBitsTextArea(Util.para8Bits(quadro[i]) + ' ');

    for (int i = 0; i < tamanho; i++) { //Laco para processar os caracteres de entrada em pares
      informacao = 0; //A variavel eh zerada para ser preenchida
      bit = 1; //Reinicia a mascara para 1, para comecar a ler o caractere pelo seu primeiro bit

      //Laco que ira iterar sobre os 8 bits do primeiro caractere do par envolvido (2 caracteres por bloco de 32 bits)
      for (int j = 0; j < 8; j++) {
        //Manchester codifica cada bit como 0 -> 01 e 1 -> 10
        /*O bit da posicao j do caractere eh comparado AND com a mascara, e o resultado da operacao (o proprio bit orginal) 
        * eh movido a esquerda  em j*2 posicoes e mesclado com o bloco de bits em informacao*/
        informacao |= ((((quadro[i * 2] & bit) ^ 0) != 0) ? 1 : 0) << (j * 2);
        /*O bit da posicao j eh comparado AND com a mascara e o resultado da operacao eh verificado se eh diferente do
        * bit mascara pela operacao XOR, gerando o par do bit na codificacao Manchester, que eh movido j*2+1 posicoes 
        * a esquerda e mesclado com o bloco de bits em informacao, ja contendo o seu par (o bit original do caractere)*/
        informacao |= ((((quadro[i * 2] & bit) ^ bit) != 0) ? 1 : 0) << (j * 2 + 1);
        bit <<= 1; //Desloca a mascara um bit a esquerda, para ler o proximo bit do caractere
      } //Fim do for do primeiro caractere

      bit = 1; //Reinicia a mascara de bit para ler o segundo caractere, caso exista
      if (i * 2 + 1 < quadro.length) { //Verifica se ha proximo caractere para leitura, se i*2+1 nao esta alem de quadro[]
        for (int j = 0; j < 8; j++) { //Itera sobre os 8 bits do caractere
          /*O bit da posicao j do caractere eh comparado AND com a mascara e o resultado (o proprio bit original) eh movido
          * a esquerda em j*2 + 16 posicoes (adequado a insercao com um caractere de 16 bits ja posicionado) e eh mesclado*/
          informacao |= ((((quadro[i * 2 + 1] & bit) ^ 0) != 0) ? 1 : 0) << (j * 2 + 16);
          /*O bit da posicao j eh comparado AND com a mascara e o resultado da operacao eh comparado XOR com o bit mascara,
          * gerando o par do bit na codificacao Manchester, movido j*2 + 17 posicoes a esquerda e mesclado em informacao*/
          informacao |= ((((quadro[i * 2 + 1] & bit) ^ bit) != 0) ? 1 : 0) << (j * 2 + 1 + 16);
          bit <<= 1; //Move a mascara para o proximo bit
        } //Fim do for do segundo caractere
      } //Fim do if do segundo caractere

      codificado[i] = informacao; //Armazena o inteiro de 32 bits com, no maximo, dois caracteres armazenados no vetor
      
      controller.adicionarBitsCodificadosTextArea(Util.bitsParaString(codificado[i]) + "\n"); //Atualiza o texto dos bits codificados na interface
    } //Fim do for da codificacao de quadro[]

    return codificado; //Retorna o vetor codificado
  } //Fim do metodo camadaFisicaTransmissoraCodificacaoManchester




  /****************************************************************
  * Metodo: camadaFisicaTransmissoraCodificacaoManchesterDiferencial
  * Funcao: Realiza a codificacao Manchester Diferencial dos quadros recebidos
  * Parametros:
  *   - int quadro[]: Vetor com os bits de cada caractere a ser codificado
  *   - ControllerTelaPrincipal controller: Controlador da interface grafica
  * Retorno: int[] - Array de bits codificados
  ****************************************************************/
  public static int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial(int quadro[], ControllerTelaPrincipal controller) {
    /*Calcula o tamanho necessario para o array de codificacao, que, como na codificacao Manchester, cada bloco de 32 bits
    * comporta agora apenas 2 caracteres de 8 bits, que apos codificados terao 16 bits cada*/
    int tamanho = (quadro.length - 1) / 2 + 1;
    int codificado[] = new int[tamanho]; //Vetor que armazenara os inteiros com os blocos de bits codificados
    int informacao; //Variavel inteira que ira acumular cada unidade do vetor com 32 bits
    int bit; //Bit mascara para isolar um bit do caractere original
    Boolean ultimoBit; //Armazena o estado do ultimo bit, isto eh, o diferencial dessa codificacao

    //Exibe os bits originais na interface
    for (int i = 0; i < quadro.length; i++)
      controller.adicionarBitsTextArea(Util.para8Bits(quadro[i]) + ' ');

    for (int i = 0; i < tamanho; i++) { //Laco para processar os caracteres de entrada em pares, assim como em Manchester
      informacao = 0; //O bloco eh zerado para ser preenchido
      bit = 1; //Reinicia a mascara para ler o primeiro bit do proximo caractere

      //Processa especialmente o primeiro bit, que precisa estabelecer um estado inicial por nao ter bit anterior
      if ((quadro[i * 2] & bit) != 0) { //Se AND do bit e da mascara nao for 0, o bit original nao eh 0
        informacao |= 1; //Bit 1 codificado como transicao por uma operacao OR com informacao e 1 (000[...]01), resultando em 01
        ultimoBit = false; //O ultimo bit eh definido como false, usado para representar o sinal LOW
      } else { //Se o bit original for 1
        informacao |= (1 << 1); //Bit 0 codificado por uma operacao OR com informacao e 1 << 1 (00[...]10), resultando em 10
        ultimoBit = true; //O ultimo bit eh definido como true, usado para representar o HIGH
      }
      bit <<= 1; //A mascara eh movida uma posicao para a esquerda

      for (int j = 1; j < 8; j++) { //Laco para processar os 7 bits restantes do primeiro caractere, usando ultimoBit como referencia
        int atual = (((quadro[i * 2] & bit) != 0) ? 1 : 0); //Isola o bit atual com uma operacao AND com a mascara
        int paraAdicionar; //Variavel que vai armazenar o par de bits a ser adicionado

        if (atual == 1) { //Se o bit atual for 000[...]001
          //Sempre inverte no meio do clock
          paraAdicionar = ultimoBit ? 1 : 2; //Se o ultimoBit era HIGH (true), o proximo par eh 1 (000[...]01), isto eh, HIGH-LOW
          /*Se o ultimoBit era LOW (false), o proximo par eh 2 (000[...]10), isto eh, LOW-HIGH, respeitando as transicoes de sinal*/
          ultimoBit = !ultimoBit; //Inverte o estado do ultimoBit, pois ha transicao no meio do clock
        } else { //Se o bit atual for 000[...]00
          paraAdicionar = ultimoBit ? 2 : 1; //Se o ultimoBit era HIGH(true), o proximo par eh 2 (...10), isto eh, LOW-HIGH,
          //Se o ultimoBit era LOW(false), o proximo par de bits eh 1 (...01), isto eh, HIGH-LOW, respeitando as transicoes de sinal
        } //Fim do if-else

        informacao |= paraAdicionar << (j * 2); //Bitwise left em paraAdicionar com j*2 posicoes a esquerda e mescla (OR) com informacao
        bit <<= 1; //Prepara a mascara para o proximo bit
      } //Fim do for do primeiro caractere

      bit = 1; //Reinicia a mascara para ler o proximo caractere, caso exista
      if (i * 2 + 1 < quadro.length) { //Se i*2 + 1 nao estiver alem dos limites de quadro[]
        for (int j = 0; j < 8; j++) { //Itera sobre os 8 bits do caractere
          int atual = (((quadro[i * 2 + 1] & bit) != 0) ? 1 : 0); //Isola o bit atual em AND com a mascara
          int paraAdicionar; //Armazenara o par de bits a ser adicionado

          if (atual == 1) { //Se o bit atual for ...01
            paraAdicionar = ultimoBit ? 1 : 2; //Se o ultimoBit era HIGH(true), o proximo par eh ...01, caso contrario, eh ...10
            ultimoBit = !ultimoBit; //Inverte o estado de ultimoBit, para transicao no meio do clock
          } else { //Se o bit atual for ...10
            paraAdicionar = ultimoBit ? 2 : 1; //Se ultimoBit == true (HIGH), o proximo par de bits eh 01, caso contrario, eh 10
          } //Fim do if-else

          informacao |= paraAdicionar << (j * 2 + 16); //Bitwise left em paraAdicionar com adequacaco para os 16 bits do caractere anterior
          //ja posicionados, isto eh, j*2 + 16 a esquerda e mesclagem (OR) com o bloco de bits informacao
          bit <<= 1; //Prepara a mascara para o proximo bit
        } //Fim do for do segundo caractere
      } //Fim do if do segundo caractere

      codificado[i] = informacao; //Armazena o inteiro de 32 bits com os pares de bits codificados no vetor
      
      controller.adicionarBitsCodificadosTextArea(Util.bitsParaString(codificado[i]) + "\n"); //Atualiza o texto dos bits codificados na interface
    } //Fim do for de codificacao

    return codificado; //Retorna o vetor codificado
  } //Fim do metodo camadaFisicaTransmissoraCodificacaoManchesterDiferencial
} //Fim da classe CamadaFisicaTransmissora