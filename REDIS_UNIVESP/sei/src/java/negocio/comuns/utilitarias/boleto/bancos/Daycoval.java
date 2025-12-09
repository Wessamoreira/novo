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

import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;

/**
 * Classe responsavel em criar os campos do Banco Bradesco.
 * @author Fabio Souza
 */
import java.io.Serializable;

public class Daycoval implements BoletoBanco, Serializable {

    JBoletoBean boleto;

    public String getNumero() {
    	// O número do banco daycoval é 707, porem na geração do boleto é utilizado o mesmo numero do bradesco.
    	// O daycoval, apenas modifica o arquivo de retorno e remessa, não modifica a geração do boleto, mantendo assim identico ao do BRADESCO.
        return "237";
    }

    public Daycoval(JBoletoBean boleto) {
        this.boleto = boleto;
    }

    private String getCampoLivre() {

        String campo = boleto.getAgencia() + boleto.getCarteira() + boleto.getNossoNumero() + boleto.getContaCorrente() + "0";

        return campo;
    }

    private String getCampo1() {
        String campo = getNumero() + boleto.getMoeda() + getCampoLivre().substring(0, 5);

        return boleto.getDigitoCampo(campo, 2);
    }

    private String getCampo2() {
        String campo = getCampoLivre().substring(5, 15);

        return boleto.getDigitoCampo(campo, 1);
    }

    private String getCampo3() {
        String campo = getCampoLivre().substring(15, getCampoLivre().length() >= 25? 25:getCampoLivre().length());

        return boleto.getDigitoCampo(campo, 1);
    }

    private String getCampo4() {
        String campo = getNumero()
                + boleto.getMoeda()
                + boleto.getFatorVencimento()
                + boleto.getValorTitulo()
                + boleto.getAgencia()
                + boleto.getCarteira()
                + boleto.getNossoNumero()
                + boleto.getContaCorrente() + "0";

        return boleto.getDigitoCodigoBarras(campo);
    }

    private String getCampo5() {
        String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
        return campo;
    }

    public String getCodigoBarras() {
        String campo = getNumero() + String.valueOf(boleto.getMoeda()) + getCampo4()
                + boleto.getFatorVencimento() + boleto.getValorTitulo() + boleto.getAgencia()
                + boleto.getCarteira() + boleto.getNossoNumero() + boleto.getContaCorrente() + "0";

        return campo;
    }

    public String getLinhaDigitavel() {
        return getCampo1().substring(0, 5) + "." + getCampo1().substring(5) + "  "
                + getCampo2().substring(0, 5) + "." + getCampo2().substring(5) + "  "
                + getCampo3().substring(0, 5) + "." + getCampo3().substring(5) + "  "
                + getCampo4() + "  " + getCampo5();
    }

    /**
     * Recupera a carteira no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getCarteiraFormatted() {
        return boleto.getCarteira();
    }

    /**
     * Recupera a agencia / codigo cedente no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getAgenciaCodCedenteFormatted() {
        return boleto.getAgencia() + " / " + boleto.getContaCorrente() + "-" + boleto.getDvContaCorrente();
    }

    /**
     * Recupera o nossoNumero no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getNossoNumeroFormatted() {
        return getCarteiraFormatted().concat(" / ").concat(boleto.getNossoNumero());
    }
}
