package negocio.comuns.faturamento.nfe.enumeradores;


public enum SituacaoNotaFiscalSaidaEnum {
	
	AGUARDANDO_ENVIO("AE", "Aguardando Envio"),
	ENVIADA("EN", "Enviada"),
	REJEITADA("RE", "Rejeitada"),
	CANCELADA("CA", "Cancelada"),
	AUTORIZADA("AU", "Autorizada"),
	INUTILIZADA("IN", "Inutilizada");
	
	private String valor;
	private String descricao;

	private SituacaoNotaFiscalSaidaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoNotaFiscalSaidaEnum getEnumPorValor(String valor) {
		for (SituacaoNotaFiscalSaidaEnum situacaoNotaFiscalSaidaEnum : SituacaoNotaFiscalSaidaEnum.values()) {
			if (situacaoNotaFiscalSaidaEnum.getValor().equalsIgnoreCase(valor)) {
				return situacaoNotaFiscalSaidaEnum;
			}
		}
		return null;
	}

	public static SituacaoNotaFiscalSaidaEnum getEnumPorDescricao(String valor) {
		for (SituacaoNotaFiscalSaidaEnum situacaoNotaFiscalSaidaEnum : SituacaoNotaFiscalSaidaEnum.values()) {
			if (situacaoNotaFiscalSaidaEnum.getValor().equalsIgnoreCase(valor)) {
				return situacaoNotaFiscalSaidaEnum;
			}
		}
		return null;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
