package negocio.comuns.academico.enumeradores;

public enum SituacaoDocumentoLocalizadoEnum {

	LOCALIZADO("LOCALIZADO"),
	NAO_LOCALIZADO("NAO_LOCALIZADO");

	String valor;

	SituacaoDocumentoLocalizadoEnum(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
