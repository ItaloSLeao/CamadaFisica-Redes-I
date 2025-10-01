/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 16/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: Util.java
* Funcao...........: Classe com metodos estaticos utilitarios para as camadas fisicas e o meio de comunicacao,
* no tocante a formatar os bits enviados e recebidos na interface grafica.
****************************************************************/

package util;

public class Util {

  /****************************************************************
  * Metodo: bitsParaString
  * Funcao: Converte um inteiro de 32 bits para representacao binaria (0 e 1).
  * Parametros: int bits - O inteiro a ser formatado
  * Retorno: String - A representacao binaria em formato de texto
  ****************************************************************/
  public static String bitsParaString(int bits) {
    String retorno = ""; //Inicializa a string
    int mascara = 1; //Bit mascara

    for (int i = 0; i < 32; i++) { //Itera sobre os 32 bits do inteiro
      if (i % 8 == 0){ //A cada 8 bits da um espaco de separacao visual
        retorno = ' ' + retorno;
      } //Fim do if
        
      Boolean bitAtivo = (mascara & bits) != 0; //Operacao AND com a mascara, se != 0, true, se nao, false
      mascara <<= 1; //Move a mascara 1 bit a esquerda
      
      if (bitAtivo) //Se o primeiro bit do inteiro nao for 0
        retorno = "1" + retorno; //Escreve 1 na string
      else //Caso contrario
        retorno = "0" + retorno; //Escreve 0 na string
    } //Fim do for sobre o inteiro de 32 bits

    return retorno; //Retorna a string com a representacao binaria 32 bits do inteiro
  } //Fim da classe bitsParaStrings


  /****************************************************************
  * Metodo: para8Bits
  * Funcao: Converte um inteiro para sua representacao em 8 bits (String).
  * Parametros: int n - O inteiro a ser formatado
  * Retorno: String - A representacao binaria de 8 bits em formato string
  ****************************************************************/
  public static String para8Bits(int n) {
    String retorno = ""; //Inicializa a string
    int bit = 1; //Bit mascara
    for (int i = 0; i < 8; i++) { //Itera sobre os 8 bits do inteiro
      retorno = ((bit & n) != 0 ? "1" : "0") + retorno; //Retorna 1 se AND com a mascara != 0, se nao, 0
      bit <<= 1; //Avanca a mascara 1 bit para a esquerda
    } //Fim do for sobre os 8 bits

    return retorno; //Retorna o inteiro de 8 bits formatado (0000...01010101) -> (01010101)
  } //Fim do metodo para8Bits
  
} //Fim da classe Util