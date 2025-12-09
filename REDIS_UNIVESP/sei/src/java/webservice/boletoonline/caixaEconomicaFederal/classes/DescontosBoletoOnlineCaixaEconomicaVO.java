package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DESCONTO")
public class DescontosBoletoOnlineCaixaEconomicaVO {
     
	
	@XStreamAlias("DATA")
	private String data ;
	
	@XStreamAlias("PERCENTUAL")
	private String percentual;
	
	@XStreamAlias("VALOR")
	private String valor;
	
	public DescontosBoletoOnlineCaixaEconomicaVO() {
		
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getPercentual() {
		return percentual;
	}

	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	
	
	
}
