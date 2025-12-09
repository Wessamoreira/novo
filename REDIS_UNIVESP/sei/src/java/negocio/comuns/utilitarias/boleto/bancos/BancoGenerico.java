/*
 * Esta biblioteca e um software livre, que pode ser redistribuido e/ou
 * modificado sob os termos da Licen√ßa Publica Geral Reduzida GNU,
 * conforme publicada pela Free Software Foundation, versao 2.1 da licenca.
 *
 * Esta biblioteca e distribuida na experanca de ser util aos seus usuarios,
 * porem NAO TEM NENHUMA GARANTIA, EXPLICITAS OU IMPLICITAS, COMERCIAIS OU
 * DE ATENDIMENTO A UMA DETERMINADA FINALIDADE.
 * Veja a Licenca Publica Geral Reduzida GNU para maiores detalhes.
 * A licenca se encontra no arquivo lgpl-br.txt
 */
package negocio.comuns.utilitarias.boleto.bancos;

import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;

/**
 * Classe modelo para a criacao de outros
 * Copie este arquivo para o nome do banco q vc pretende criar, seguindo a mesma nomeclatura de nomes das outras classes
 * Caso o banco tenha algum metodo diferente de calcular os seus campos, vc pode criar os seu metodos particulares dentro
 * desta classe, pois os metodos que tem nesta classe sao padroes
 * @author Gladyston Batista
 */
import java.io.Serializable; public class BancoGenerico implements BoletoBanco, Serializable {

  JBoletoBean boleto;

  /**
   * Metdodo responsavel por resgatar o numero do banco, coloque no return o codigo do seu banco
   */
  public String getNumero() {
    return "99999";
  }

  /**
   * Classe construtura, recebe como parametro a classe jboletobean
   */
  public BancoGenerico(JBoletoBean boleto) {
    this.boleto = boleto;
  }

  /**
   * Metodo que monta o primeiro campo do codigo de barras
   * Este campo como os demais e feito a partir do da documentacao do banco
   * A documentacao do banco tem a estrutura de como monta cada campo, depois disso
   * √© s√≥ concatenar os campos como no exemplo abaixo.
   */
  private String getCampo1() {
    return "";
  }

  /**
   * ver documentacao do banco para saber qual a ordem deste campo
   */
  private String getCampo2() {
    return "";
  }

  /**
   * ver documentacao do banco para saber qual a ordem deste campo
   */
  private String getCampo3() {
    return "";
  }

  /**
   * ver documentacao do banco para saber qual a ordem deste campo
   */
  private String getCampo4() {
    return "";
  }

  /**
   * ver documentacao do banco para saber qual a ordem deste campo
   */
  private String getCampo5() {
    return "1234";
  }

  /**
   * Metodo para monta o desenho do codigo de barras
   * A ordem destes campos tambem variam de banco para banco, entao e so olhar na documentacao do seu banco
   * qual a ordem correta
   */
  public String getCodigoBarras() {
      return "0000000000000000000000000000000000000000";
  }

  /**
   * Metodo que concatena os campo para formar a linha digitavel
   * E necessario tambem olhar a documentacao do banco para saber a ordem correta a linha digitavel
   */
  public String getLinhaDigitavel() {
      return "Banco n„o encontrado. CobranÁa sem boleto";
  }

  /**
   * Recupera a carteira no padrao especificado pelo banco
   * @author Gladyston Batista/Eac Software
   */
  public String getCarteiraFormatted() {
    return ""; //boleto.getCarteira();
  }

  /**
   * Recupera a agencia / codigo cedente no padrao especificado pelo banco
   * @author Gladyston Batista/Eac Software
   */
  public String getAgenciaCodCedenteFormatted() {
    return ""; //return boleto.getAgencia() + " / " + boleto.getContaCorrente() + "-" + boleto.getDvContaCorrente();
  }

  /**
   * Recupera o nossoNumero no padrao especificado pelo banco
   * @author Gladyston Batista/Eac Software
   */
  public String getNossoNumeroFormatted() {
    return ""; //String.valueOf(Integer.parseInt(boleto.getNossoNumero()));
  }
}
