package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Juros_moraCaixaEconomicaVO {
	
	@XStreamAlias("TIPO")
	private String tipo;
	
	
	@XStreamAlias("DATA")
	private String data;
	
	@XStreamAlias("VALOR")
	private String valor;	
	
	
	@XStreamAlias("PERCENTUAL")
	private String percentual;
	
	public Juros_moraCaixaEconomicaVO() {
		
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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

	
    
}
