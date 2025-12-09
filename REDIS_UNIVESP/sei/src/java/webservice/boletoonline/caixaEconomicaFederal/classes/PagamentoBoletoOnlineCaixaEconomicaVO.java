package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PagamentoBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("QUANTIDADE_PERMITIDA")
	private String quantidade_permitida;
	
	@XStreamAlias("TIPO")
	private String tipo;
	
	@XStreamAlias("VALOR_MINIMO")
	private String vaLor_minimo;
	
	@XStreamAlias("VALOR_MAXIMO")
	private String valor_maximo;
	
	public PagamentoBoletoOnlineCaixaEconomicaVO() {
		
	}

	public String getQuantidade_permitida() {
		return quantidade_permitida;
	}

	public void setQuantidade_permitida(String quantidade_permitida) {
		this.quantidade_permitida = quantidade_permitida;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVaLor_minimo() {
		return vaLor_minimo;
	}

	public void setVaLor_minimo(String vaLor_minimo) {
		this.vaLor_minimo = vaLor_minimo;
	}

	public String getValor_maximo() {
		return valor_maximo;
	}

	public void setValor_maximo(String valor_maximo) {
		this.valor_maximo = valor_maximo;
	}



}
