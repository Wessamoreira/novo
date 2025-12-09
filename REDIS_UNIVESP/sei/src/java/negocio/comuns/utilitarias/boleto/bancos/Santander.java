/*
 * Esta biblioteca e um software livre, que pode ser redistribuido e/ou 
 * modificado sob os termos da LicenÃ§a Publica Geral Reduzida GNU, 
 * conforme publicada pela Free Software Foundation, versao 2.1 da licenca.
 *
 * Esta biblioteca e distribuida na experanca de ser util aos seus usuarios, 
 * porem NAO TEM NENHUMA GARANTIA, EXPLICITAS OU IMPLICITAS, COMERCIAIS OU 
 * DE ATENDIMENTO A UMA DETERMINADA FINALIDADE. 
 * Veja a Licenca Publica Geral Reduzida GNU para maiores detalhes. 
 * A licenca se encontra no arquivo lgpl-br.txt 
 */
package negocio.comuns.utilitarias.boleto.bancos;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;

/**
 * Classe para gerar o boleto do santander
 * @author Mario Grigioni
 */
import java.io.Serializable;

public class Santander implements BoletoBanco, Serializable {

    JBoletoBean boleto;
    public String D1 = "0";
    public String D2 = "0";

    /**
     * Metdodo responsavel por resgatar o numero do banco, coloque no return o codigo do seu banco
     */
    public String getNumero() {
        return "033";
    }

    /**
     * Método paticular do santander
     */
    public String getZero() {
        return "00";
    }

    private String getCampoLivre() {

        // alteração realizada por Jefferson Ricardo Zuchi
        // com base na versao 04/2009 do layout do banco
        String campo = "9" + boleto.getNumConvenio() + Uteis.preencherComZerosPosicoesVagas(boleto.getNossoNumero(), 13) + boleto.getIOS() + boleto.getCarteira();

        return campo;
    }

    /**
     * Classe construtura, recebe como parametro a classe jboletobean
     */
    public Santander(JBoletoBean boleto) {
        this.boleto = boleto;
    }

    /**
     * Metodo que monta o primeiro campo do codigo de barras 
     * Este campo como os demais e feito a partir do da documentacao do banco
     * A documentacao do banco tem a estrutura de como monta cada campo, depois disso
     * Ã© sÃ³ concatenar os campos como no exemplo abaixo.
     */
    private String getCampo1() {
        String campo = getNumero() + String.valueOf(boleto.getMoeda()) + getCampoLivre().substring(0, 5);

        return campo + boleto.getModulo10(campo);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo 
     */
    private String getCampo2() {
        String campo = getCampoLivre().substring(5, 15);

        return campo + boleto.getModulo10(campo);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo3() {
        String campo = getCampoLivre().substring(15);

        return campo + boleto.getModulo10(campo);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo4() {
        String campo = getNumero() + String.valueOf(boleto.getMoeda())
                + boleto.getFatorVencimento() + boleto.getValorTitulo() + getCampoLivre();

        return boleto.getModulo11Santander(campo, 9);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo5() {
        String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
        return campo;
    }

    /**
     * Metodo para monta o desenho do codigo de barras
     * A ordem destes campos tambem variam de banco para banco, entao e so olhar na documentacao do seu banco
     * qual a ordem correta
     */
    public String getCodigoBarras() {

        return getNumero() + String.valueOf(boleto.getMoeda()) + getCampo4() + getCampo5() + getCampoLivre();

    }

    /**
     * Metodo que concatena os campo para formar a linha digitavel
     * E necessario tambem olhar a documentacao do banco para saber a ordem correta a linha digitavel
     */
    public String getLinhaDigitavel() {
        return getCampo1().substring(0, 5) + "." + getCampo1().substring(5) + "  "
                + getCampo2().substring(0, 5) + "." + getCampo2().substring(5) + "  "
                + getCampo3().substring(0, 5) + "." + getCampo3().substring(5) + "  "
                + getCampo4() + "  " + getCampo5();
    }

    public String getCarteiraFormatted() {
        return boleto.getCarteira();
    }

    public String getAgenciaCodCedenteFormatted() {
        return boleto.getAgencia();
    }

    public String getNossoNumeroFormatted() {
        return boleto.getNossoNumero();
    }
}
