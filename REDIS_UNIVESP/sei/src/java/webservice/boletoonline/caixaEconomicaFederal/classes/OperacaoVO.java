package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class OperacaoVO {

	
	@XStreamAlias("CODIGO_BENEFICIARIO")
	private String codigo_beneficiario;
	
	@XStreamAlias("TITULO")
	private TituloVO titulo;
	
	@XStreamAlias("NOSSO_NUMERO")
	private String nossoNumero;
	
	
	public OperacaoVO() {
		
	}

	public String getCodigo_beneficiario() {
		return codigo_beneficiario;
	}

	public void setCodigo_beneficiario(String codigo_beneficiario) {
		this.codigo_beneficiario = codigo_beneficiario;
	}

	public TituloVO getTitulo() {
		return titulo;
	}

	public void setTitulo(TituloVO titulo) {
		this.titulo = titulo;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	
	
}
