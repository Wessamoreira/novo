package negocio.comuns.lacuna;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RespostaAssinaturaInicializaLacunaVO {

	@JsonProperty("documentoAssinado")
	private Integer documentoAssinado;
	@JsonProperty("ordemAssinatura")
	private Integer ordemAssinatura;
	@JsonProperty("codigoPessoaLogada")
	private Integer codigoPessoaLogada;
	@JsonProperty("tokenLacuna")
	private String tokenLacuna;
	@JsonProperty("thumbprintCertificado")
	private String thumbprintCertificado;

	public Integer getDocumentoAssinado() {
		return documentoAssinado;
	}

	public void setDocumentoAssinado(Integer documentoAssinado) {
		this.documentoAssinado = documentoAssinado;
	}
	
	public Integer getOrdemAssinatura() {
		return ordemAssinatura;
	}
	
	public void setOrdemAssinatura(Integer ordemAssinatura) {
		this.ordemAssinatura = ordemAssinatura;
	}
	
	public Integer getCodigoPessoaLogada() {
		return codigoPessoaLogada;
	}
	
	public void setCodigoPessoaLogada(Integer codigoPessoaLogada) {
		this.codigoPessoaLogada = codigoPessoaLogada;
	}

	public String getTokenLacuna() {
		return tokenLacuna;
	}

	public void setTokenLacuna(String tokenLacuna) {
		this.tokenLacuna = tokenLacuna;
	}

	public String getThumbprintCertificado() {
		return thumbprintCertificado;
	}

	public void setThumbprintCertificado(String thumbprintCertificado) {
		this.thumbprintCertificado = thumbprintCertificado;
	}
}
