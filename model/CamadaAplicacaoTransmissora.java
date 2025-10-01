/****************************************************************
* Autor............: Italo de Souza Leao
* Matricula........: 202410120
* Inicio...........: 14/08/2025
* Ultima alteracao.: 25/08/2025
* Nome.............: CamadaAplicacaoTransmissora.java
* Funcao...........: Essa classe simula o funcionamento de uma camada de aplicacao do transmissor da mensagem, recebendo
* a mensagem, transformando cada caractere dela em seu inteiro na tabela ASCII e passando um vetor de quadros da mensagem
* para a proxima camada de transmissao, a CamadaFisicaTransmissora.
****************************************************************/

package model;

import controller.ControllerTelaPrincipal; //Importacao do controlador da interface
 
public class CamadaAplicacaoTransmissora {

  /****************************************************************
  * Metodo: camadaAplicacaoTransmissora
  * Função: Converte uma mensagem de texto em 'quadros' ASCII, exibe a conversao na interface grafica
  * e envia para a camada fisica de transmissão os quadros convertidos, por meio de uma thread dedicada a transmissao
  * Parâmetros:
  *   - String mensagem: Texto a ser transmitido
  *   - ControllerTelaPrincipal controller: Controlador da interface gráfica
  * Retorno: void
  ****************************************************************/
  public static void camadaAplicacaoTransmissora(String mensagem, ControllerTelaPrincipal controller) {
    new Thread(() -> { //Cria uma thread de envio da mensagem usando uma expressao lambda
      try {
        int quadro[] = new int[mensagem.length()]; //Array de inteiros que armazenara o codigo ascii de cada caracter da mensagem
        String textoExibicao; //Texto que será exibido no textArea

        for (int i = 0; i < mensagem.length(); i++) { //Laco que percorre todos os caracteres da mensagem
          textoExibicao = ""; //Reinicia a string
          textoExibicao += mensagem.charAt(i); //Pega o caractere na posicao i da string
          textoExibicao += " = "; //Adiciona o separador de identificacao do caractere com seu valor ascii
          quadro[i] = mensagem.charAt(i); //Valor ASCII do caractere i eh adicionado a posicao i do array
          textoExibicao += quadro[i]; //Adiciona o valor ASCII ao texto de exibicao da area de texto
          textoExibicao += ";";

          String textoAtual = textoExibicao; //Armazena o texto construido na variavel textoAtual

          //Atualiza a area de texto de conversao de caracteres ascii para numeros com o textoAtual
          controller.adicionarAsciiParaIntTextArea(textoAtual);
          Thread.sleep(controller.getVelocidade()); //A thread de transmissao dorme, conforme velocidade definida no controller
          System.out.println(textoAtual); //Imprime no console a codificacao ascii feita na camada de aplicacao
        } //Fim do for da mensagem

        CamadaFisicaTransmissora.camadaFisicaTransmissora(quadro, controller); //Envia o array de inteiros ascii da mensagem para a camada fisica de transmissao
      } catch (Exception e) {
        System.out.println(e.getStackTrace()); //Tratamento de excecao caso o Thread.sleep seja interrompido
      }
    }).start(); //Inicializa a thread de transmissao
  } //Fim do metodo camadaAplicacaoTransmissora
} //Fim da classe CamadaAplicacaoTransmissora