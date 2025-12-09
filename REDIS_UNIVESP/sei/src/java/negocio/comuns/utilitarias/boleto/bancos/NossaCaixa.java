/*
 * Esta biblioteca e um software livre, que pode ser redistribuido e/ou
 * modificado sob os termos da Licenca Publica Geral Reduzida GNU,
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
 * @author Vitor Motta - Fly Solution
 */
import java.io.Serializable; public class NossaCaixa implements BoletoBanco, Serializable {
    
    JBoletoBean boleto;
    
    private final String inicioNossoNumero = "99";
    private String indetSistema = "9";
    private String dv=null, dv1 = null, dv2 = null;
    private String codigoBarras = null;
    private String campoLivre = null;
    
    public String getNumero() {
        return "151";
    }
    
    public String getNumeroSemDigito() {
        return "151";
    }
    
    public NossaCaixa(JBoletoBean boleto) {
        this.boleto = boleto;
        
        // adicionando o inicio para o nosso numero
        boleto.setNossoNumero( this.inicioNossoNumero + boleto.getNossoNumero(), 9 );
        // **********************
        
        // Calculando o digito verificador caracter 43 e 44 no código de barras
        String nn = boleto.getNossoNumero().substring(1);
        String modalidade = getModalidade( boleto.getContaCorrente().substring(0,2) );
        String cc = boleto.getContaCorrente().substring(3);
        
        
        String calcdv = this.indetSistema + nn + boleto.getAgencia() + modalidade + cc + getNumeroSemDigito();
        this.dv1=getDv43( calcdv );
        this.dv2=getDv44( calcdv + this.dv1 );
        
        if (dv2.length() == 2){
            this.dv1 = this.dv2.substring(0,1);
            this.dv2 = this.dv2.substring(1,2);
        }
        // **********************
        
        // Calculando digito verificador
        String campo = getNumeroSemDigito() + boleto.getMoeda() + boleto.getFatorVencimento() + boleto.getValorTitulo() +
                this.indetSistema + nn + boleto.getAgencia() + modalidade + cc + getNumeroSemDigito() + this.dv1 + this.dv2;
        
        this.dv = boleto.getDigitoCodigoBarras( campo );
        // **********************
        
        // Montando o codigo de barras
        this.codigoBarras = getNumeroSemDigito() + boleto.getMoeda() + this.dv + boleto.getFatorVencimento() + boleto.getValorTitulo() +
                this.indetSistema + nn + boleto.getAgencia() + modalidade + cc + getNumeroSemDigito() + this.dv1 + this.dv2;
        // **********************
        
        // Corrigindo a conta corrente para modalidade(2) + contaCorrente(7), exemplo 22 7777777
        boleto.setContaCorrente( boleto.getContaCorrente().substring(0,3) + "0" + boleto.getContaCorrente().substring(3) );
        // **********************
        
        // Adicionando o DV para o nosso numero
        String dvNossoNumero = getDvNossoNumero( boleto.getAgencia() + boleto.getContaCorrente().substring(0,2) + boleto.getContaCorrente().substring(3) + boleto.getDvContaCorrente() + boleto.getNossoNumero());
        boleto.setNossoNumero( boleto.getNossoNumero() + "-" + dvNossoNumero);
        // **********************
        
        //definindo o campolivre
        this.campoLivre = this.codigoBarras.substring(19);
        // **********************
        
    }
    
    private String getCampo1() {
        String campo = getNumeroSemDigito() + boleto.getMoeda() + this.campoLivre.substring(0,5);
        return boleto.getDigitoCampo(campo);
    }
    
    private String getCampo2() {
        String campo =  this.campoLivre.substring(5,15);
        return boleto.getDigitoCampo(campo);
    }
    
    /**
     * ver documentacao do banco para saber qual a ordem deste campo
     */
    private String getCampo3() {
        String campo = this.campoLivre.substring(15);
        return boleto.getDigitoCampo(campo);
    }
    
    private String getCampo4() {
        return this.dv;
    }
    
    private String getCampo5() {
        String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
        return campo;
    }
    
    public String getCodigoBarras() {
        return this.codigoBarras;
    }
    
    public String getLinhaDigitavel() {
        return 	getCampo1().substring(0,5) + "." + getCampo1().substring(5) + "  " +
                getCampo2().substring(0,5) + "." + getCampo2().substring(5) + "  " +
                getCampo3().substring(0,5) + "." + getCampo3().substring(5) + "  " +
                getCampo4() + "  " + getCampo5();
    }
    
    /**
     * Métodos específicos do banco
     */
    public String getDv43(String numero) {
        
        int total = 0;
        int fator = 2;
        
        int numeros, temp;
        
        for (int i = numero.length(); i > 0; i--) {
            
            numeros = Integer.parseInt( numero.substring(i-1,i) );
            
            temp = numeros * fator;
            if (temp > 9) temp=temp-9; // Regra do banco NossaCaixa
            
            total += temp;
            
            // valores assumidos: 212121...
            fator = (fator % 2) + 1;
        }
        
        int resto = total % 10;
        
        if (resto > 0)
            resto = 10 - resto;
        
        return String.valueOf( resto );
        
    }
    
    public String getDv44(String numero) {
        
        int resto = modulo_11(numero);
        
        int dv=0;
        
        if(resto > 1){
            dv = 11-resto;
        } else if(resto == 1){
            int dv1 = Integer.parseInt( numero.substring(23,24) );
            dv1++;
            if (dv1 == 10) dv1 = 0;
            numero = numero.substring(0,23) + String.valueOf( dv1 );
            String dv44 = getDv44(numero);
            dv = Integer.parseInt( String.valueOf( dv1 ) + dv44 );
        }
        
        return String.valueOf( dv );
    }
    
    public int modulo_11(String numero)  {
        
        int total = 0, temp;
        int fator = 2;
        int numeros;
        
        for (int i = numero.length(); i > 0; i--) {

            numeros = Integer.parseInt( numero.substring(i-1,i) );

            temp = numeros * fator;

            total += temp;

            // valores assumidos: 2345672345...
            fator = ((fator-1) % 6) + 2;
        }

        int resto = total % 11;

        return resto;
    }
    
    public String getDvNossoNumero(String numext) {
        
        String pesos = "31973197319731319731973";
        int total=0, numeros, peso;
        
        for (int i = numext.length(); i > 0; i--) {
            
            numeros = Integer.parseInt( numext.substring(i-1,i) );
            peso = Integer.parseInt( pesos.substring(i-1,i) );
            
            total += numeros * peso;
        }
        
        int resto = total % 10;
        if (resto > 0) resto = 10 - resto;
        
        return String.valueOf( resto );
    }
    
    private String getModalidade(String modalidade) {

        if (modalidade.equals("01"))
            modalidade = "1";

        else if (modalidade.equals("04"))
            modalidade = "4";

        else if (modalidade.equals("09"))
            modalidade = "9";

        else if (modalidade.equals("13"))
            modalidade = "3";

        else if (modalidade.equals("16"))
            modalidade = "6";

        else if (modalidade.equals("17"))
            modalidade = "7";

        else if (modalidade.equals("18"))
            modalidade = "8";

        return modalidade;
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
        return boleto.getAgencia() + " / " + boleto.getContaCorrente() + " " + boleto.getDvContaCorrente();
    }

    /**
     * Recupera a nossoNumero no padrao especificado pelo banco
     * @author Gladyston Batista/Eac Software
     */
    public String getNossoNumeroFormatted() {
        return boleto.getNossoNumero();
    }

}