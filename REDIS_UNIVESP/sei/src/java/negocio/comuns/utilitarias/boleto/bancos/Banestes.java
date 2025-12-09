package negocio.comuns.utilitarias.boleto.bancos;

import java.io.Serializable;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;

public class Banestes implements BoletoBanco , Serializable {
	
	
	  JBoletoBean boleto;
	  private String campoValor;
	  
	  public Banestes(JBoletoBean boleto) {
	        this.boleto = boleto;
	    }
	    
	    public String getNumero() {
	        return "021";
	    }
	    
	    /*
	     * Por default valor 4 que indica com registro vide Manual.
	     */
	    public String getTipoCobranca() {
	        return "4";
	    }
	    
	    
	    
	    
	    public String getZero() {
	        return "00";
	    }
	    
	    
	    //NNNNNNNN CCCCCCCCCCC R 021 d1 d2 - chave asbace
	    // Nosso numero + conta corrente + Fator cobranca + banco + d1 + 2
	    //Chave ASBACE
	    private String getASBACE() {
	        String campo = boleto.getNossoNumero().substring(0, 8) +  Uteis.preencherComZerosPosicoesVagas((boleto.getContaCorrente() + boleto.getDvContaCorrente()), 11) +  getTipoCobranca() + getNumero() ;
	         campo =  get1DigitoCampoVerificadorASBACE(campo,2);
	         campo =  get2DigitoCampoVerificadorASBACE(campo,7);
	        return campo;
	    }
	    
	    private String getCampo1() {
	        String campo = getNumero() + boleto.getMoeda() + getASBACE().substring(0,5);
	        return boleto.getDigitoCampo(campo,2);
	    }
	    
	    private String getCampo2() {
	        String campo = getASBACE().substring(5,15);
	        return boleto.getDigitoCampo(campo,1);
	    }
	    
	    private String getCampo3() {
	        String campo = getASBACE().substring(15);	       
	        return boleto.getDigitoCampo(campo,1);
	    }
	    
	    private String getCampo4() {
	        String campo = 	getNumero() + String.valueOf(boleto.getMoeda()) +  boleto.getFatorVencimento() + boleto.getValorTitulo() + getASBACE();
	        
	        return boleto.getDigitoCodigoBarras(campo);
	    }
	    
	    private String getCampo5() {
	        String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
	        return campo;
	    }
	    
	    public String getCodigoBarras() {
	        String campo = 	getNumero() + String.valueOf(boleto.getMoeda()) + getCampo4() + boleto.getFatorVencimento() + boleto.getValorTitulo() + getASBACE();
	        
	        return campo;
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
	        return boleto.getAgencia() + " / " + boleto.getContaCorrente() + "-" + boleto.getDvContaCorrente();
	    }
	    
	    /**
	     * Recupera o nossoNumero no padrao especificado pelo banco
	     * @author Gladyston Batista/Eac Software
	     */
	    public String getNossoNumeroFormatted() {
	        return String.valueOf(Long.parseLong(boleto.getNossoNumero()));
	    }

		public String getCampoValor() {
			return campoValor;
		}

		public void setCampoValor(String campoValor) {
			this.campoValor = campoValor;
		}
		public String get1DigitoCampoVerificadorASBACE(String campo, int mult) {
	        //Esta rotina faz o calcula 212121

	        int multiplicador = mult;
	        int multiplicacao = 0;
	        int soma_campo = 0;

	        for (int i = 0; i < campo.length(); i++) {
	            multiplicacao = Integer.parseInt(campo.substring(i, 1 + i)) * multiplicador;

	            if (multiplicacao > 9) {
	                multiplicacao = multiplicacao - 9;
	            }
	            soma_campo = soma_campo + multiplicacao;
	            
	            multiplicador = (multiplicador % 2) + 1;
	            

	        }
	        int dac = 10 - (soma_campo % 10);

	        if (dac == 10) {
	            dac = 0;
	        }

	        campo = campo + String.valueOf(dac);

	        return campo;
	    }
	    
	public String get2DigitoCampoVerificadorASBACE(String campo, int type) {
		// Modulo 11 - 234567 (type = 7)
		// Modulo 11 - 23456789 (type = 9)

		int multiplicador = 2;
		int multiplicacao = 0;
		int soma_campo = 0;

		for (int i = campo.length(); i > 0; i--) {
			multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;

			soma_campo = soma_campo + multiplicacao;

			multiplicador++;
			if (multiplicador > 7 && type == 7) {
				multiplicador = 2;
			} else if (multiplicador > 9 && type == 9) {
				multiplicador = 2;
			}
		}


		int dac = (soma_campo % 11);

		if (dac == 1) {
			int campoD1 = Integer.parseInt(campo.substring(23, 24));
			    campoD1 =  campoD1 + 1;			  
			if (campoD1 == 10) {
				campo = campo.substring(0, 23) + 0;
				get2DigitoCampoVerificadorASBACE(campo, 7);
			} else {
				campo = campo.substring(0, 23) + (campoD1);
				campo = get2DigitoCampoVerificadorASBACE(campo, 7);				
				return campo ;
			}
		} else if (dac > 1) {
			dac = 11 - dac;
		}else {
			dac = 0 ;
		}
		campo = campo + ((Integer) dac).toString();
		return campo;
	}

}