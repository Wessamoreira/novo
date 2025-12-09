package negocio.comuns.lacuna;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InitializeRequest {

	@JsonProperty("codigoDocumentoAssinado")
	private Integer codigoDocumentoAssinado;
	@JsonProperty("thumbprintCertificado")
	private String thumbprintCertificado;
	@JsonProperty("ordemAssinatura")
	private Integer ordemAssinatura;
	@JsonProperty("codigoPessoaLogada")
	private Integer codigoPessoaLogada;

	public Integer getCodigoDocumentoAssinado() {
		return codigoDocumentoAssinado;
	}

	public void setCodigoDocumentoAssinado(Integer codigoDocumentoAssinado) {
		this.codigoDocumentoAssinado = codigoDocumentoAssinado;
	}

	public String getThumbprintCertificado() {
		return thumbprintCertificado;
	}

	public void setThumbprintCertificado(String thumbprintCertificado) {
		this.thumbprintCertificado = thumbprintCertificado;
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

}
