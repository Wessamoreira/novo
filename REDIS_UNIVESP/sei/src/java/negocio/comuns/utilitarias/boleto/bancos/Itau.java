/*
 * Esta biblioteca e um software livre, que pode ser redistribuido e/ou
 * modificado sob os termos da Licença Publica Geral Reduzida GNU,
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
 * Classe responsavel em criar os campos do Banco Itaú.
 * @author Fabio Souza
 */
import java.io.Serializable; 

public class Itau implements BoletoBanco , Serializable{
    
    JBoletoBean boleto;
    
    public String getNumero() {
        return "341";
    }
    
    public Itau(JBoletoBean boleto) {
        this.boleto = boleto;
    }
    
    public int getDacNossoNumero() {
        int dac = 0;
        
        String campo = String.valueOf(boleto.getAgencia()) + boleto.getContaCorrente() + String.valueOf(boleto.getCarteira()) + boleto.getNossoNumero();
        
        int multiplicador = 1;
        int multiplicacao = 0;
        int soma_campo = 0;
        
        for (int i = 0; i < campo.length(); i++) {
            multiplicacao = Integer.parseInt(campo.substring(i,1+i)) * multiplicador;
            
            if (multiplicacao >= 10) {
                multiplicacao = Integer.parseInt(String.valueOf(multiplicacao).substring(0,1)) + Integer.parseInt(String.valueOf(multiplicacao).substring(1));
            }
            soma_campo = soma_campo + multiplicacao;
            
            if (multiplicador == 2)
                multiplicador = 1;
            else
                multiplicador = 2;
            
        }
        
        dac = 10 - (soma_campo%10);
        
        if (dac == 10)
            dac = 0;
        
        return dac;
    }
    
    private String getCampo1() {
        String campo = getNumero() + String.valueOf(boleto.getMoeda()) + String.valueOf(boleto.getCarteira()) + boleto.getNossoNumero().substring(0,2);
        return boleto.getDigitoCampo(campo,2);
    }
    
    private String getCampo2() {
        String campo = boleto.getNossoNumero().substring(2) + String.valueOf(getDacNossoNumero()) + String.valueOf(boleto.getAgencia()).substring(0,3);
        
        return boleto.getDigitoCampo(campo,1);
    }
    
    private String getCampo3() {
        String campo = String.valueOf(boleto.getAgencia()).substring(3) + boleto.getContaCorrente() + boleto.getDvContaCorrente().trim() + "000";
        
        return boleto.getDigitoCampo(campo,1);
    }
    
    private String getCampo4() {
        String campo = 	getNumero() + String.valueOf(boleto.getMoeda()) +
                boleto.getFatorVencimento() + boleto.getValorTitulo() + String.valueOf(boleto.getCarteira()) +
                String.valueOf(boleto.getNossoNumero()) + getDacNossoNumero() +
                String.valueOf(boleto.getAgencia()) + boleto.getContaCorrente() + boleto.getDvContaCorrente().trim() + "000";
        
        return boleto.getDigitoCodigoBarras(campo);
    }
    
    private String getCampo5() {
        String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
        return campo;
    }
    
    public String getCodigoBarras() {
        return getNumero() + String.valueOf(boleto.getMoeda()) + String.valueOf(getCampo4()) + String.valueOf(getCampo5()) + 
                String.valueOf(boleto.getCarteira()) + String.valueOf(boleto.getNossoNumero()) + String.valueOf(getDacNossoNumero()) + 
                String.valueOf(boleto.getAgencia()) + boleto.getContaCorrente() + boleto.getDvContaCorrente().trim() + "000";
    }
    
    public String getLinhaDigitavel() {
        return 	getCampo1().substring(0,5) + "." + getCampo1().substring(5) + "  " +
                getCampo2().substring(0,5) + "." + getCampo2().substring(5) + "  " +
                getCampo3().substring(0,5) + "." + getCampo3().substring(5) + "  " +
                getCampo4() + "  " + getCampo5();
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
        return boleto.getAgencia() + " / " + boleto.getContaCorrente() + "-" + boleto.getDvContaCorrente().trim();
    }
    
    /**
     * Recupera o nossoNumero no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getNossoNumeroFormatted() {
        return String.valueOf(Long.parseLong(boleto.getNossoNumero()));
    }
    
}