package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class MultaBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("DATA")
	private String data;
	
	@XStreamAlias("VALOR")
	private String valor;

	@XStreamAlias("PERCENTUAL")
	private String percentual;
	
	public MultaBoletoOnlineCaixaEconomicaVO() {
		
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getPercentual() {
		return percentual;
	}
	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}
	
	
}
