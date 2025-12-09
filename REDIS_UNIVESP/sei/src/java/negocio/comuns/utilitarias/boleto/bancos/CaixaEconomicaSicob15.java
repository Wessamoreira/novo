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
 * Classe modelo para a criacao de outros
 * Copie este arquivo para o nome do banco q vc pretende criar, seguindo a mesma nomeclatura de nomes das outras classes
 * Caso o banco tenha algum metodo diferente de calcular os seus campos, vc pode criar os seu metodos particulares dentro
 * desta classe, pois os metodos que tem nesta classe sao padroes
 * @author Fabio Souza
 */
import java.io.Serializable;

public class CaixaEconomicaSicob15 implements BoletoBanco, Serializable {

    JBoletoBean boleto;

    /**
     * Metdodo responsavel por resgatar o numero do banco, coloque no return o codigo do seu banco
     */
    public String getNumero() {
        return "104";
    }

    /**
     * Retorna o Campo livre
     */
    private String getCampoLivre() {
    	String campo = boleto.getCedente() + boleto.getAgencia() + "87" + boleto.getNossoNumero();    			
    	return campo;
    }

    /**
     * Classe construtura, recebe como parametro a classe jboletobean
     * @param boleto
     */
    public CaixaEconomicaSicob15(JBoletoBean boleto) {
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
        return campo.substring(0, 5) + "." + campo.substring(5) + boleto.getDigitoVerificadorLinhaDigitavelCaixaEconomica(campo, 2);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo2() {
        String campo = getCampoLivre().substring(5, 15);
        return campo.substring(0, 5) + "." + campo.substring(5) + boleto.getDigitoVerificadorLinhaDigitavelCaixaEconomica(campo, 2);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo3() {
        String campo = getCampoLivre().substring(15);
        return campo.substring(0, 5) + "." + campo.substring(5) + boleto.getDigitoVerificadorLinhaDigitavelCaixaEconomica(campo, 2);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo4() {
        String parte1 = getNumero() + String.valueOf(boleto.getMoeda());
        String parte2 = String.valueOf(boleto.getFatorVencimento() + boleto.getValorTitulo()) + getCampoLivre();
        return boleto.getDigitoVerificadorGeralCodigoBarrasCaixaEconomica(parte1 + parte2);
    }

    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo5() {
    	String campo1 = boleto.getFatorVencimento() + "";
    	String campo2 = boleto.getValorTitulo() + "";
    	int qtd = campo1.length() + campo2.length() - 14;
    	String campo = "";
    	if (qtd > 0) {
    		int valor = 0;
    		String zero = "";
    		while (valor < qtd) {
    			zero += "0";
    			valor++;
    		}
    		campo = zero ;
    	}
        return boleto.getFatorVencimento() + campo + boleto.getValorTitulo();
    }

    /**
     * Metodo para monta o desenho do codigo de barras
     * A ordem destes campos tambem variam de banco para banco, entao e so olhar na documentacao do seu banco
     * qual a ordem correta
     */
    public String getCodigoBarras() {
    	
        String parte1 = getNumero() + String.valueOf(boleto.getMoeda());
        String parte2 = String.valueOf(boleto.getFatorVencimento() + boleto.getValorTitulo()) + getCampoLivre();
        return parte1 + boleto.getDigitoVerificadorGeralCodigoBarrasCaixaEconomica(parte1 + parte2) + parte2;
//        return getNumero() + String.valueOf(boleto.getMoeda()) + String.valueOf(boleto.getFatorVencimento() + boleto.getValorTitulo()) + getCampoLivre() + boleto.getDigitoVerificadorCampoLivreCaixaEconomica(getCampoLivre());
    }

    /**
     * Metodo que concatena os campo para formar a linha digitavel
     * E necessario tambem olhar a documentacao do banco para saber a ordem correta a linha digitavel
     */
    public String getLinhaDigitavel() {
        return getCampo1() + "  "
                + getCampo2() + "  "
                + getCampo3() + "  "
                + getCampo4() + "  " + getCampo5();
    }

    /**
     * Recupera a carteira no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getCarteiraFormatted() {
        return "SR";
    }

    /**
     * Recupera a carteira no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getAgenciaCodCedenteFormatted() {

        Integer f1 = Integer.parseInt(boleto.getAgencia().substring(0, 4));
        Integer f2 = Integer.parseInt(boleto.getCodigoOperacao().substring(0, 3));
        Integer f3 = Integer.parseInt(boleto.getCodigoFornecidoAgencia());
        Integer f4 = Integer.parseInt(boleto.getDvCodigoFornecidoAgencia());

        return String.format("%04d.%03d.%08d-%01d", f1, f2, f3, f4);
    }

    public String getNossoNumeroFormatted() {
        return boleto.getCarteira() + "." + boleto.getNossoNumero() + "-" + boleto.getDvNossoNumero();
    }
}