package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PosVencimentoBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("ACAO")
	private String acao;
	
	@XStreamAlias("NUMERO_DIAS")
	private String numero_dias;
	
	public PosVencimentoBoletoOnlineCaixaEconomicaVO() {
		
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getNumero_dias() {
		return numero_dias;
	}

	public void setNumero_dias(String numero_dias) {
		this.numero_dias = numero_dias;
	}

	
}
