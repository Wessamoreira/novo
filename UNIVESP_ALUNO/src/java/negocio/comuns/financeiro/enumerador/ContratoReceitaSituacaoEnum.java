package negocio.comuns.financeiro.enumerador;

public enum ContratoReceitaSituacaoEnum {
	ATIVO("AT", "Ativo"), FINALIZADO("FI", "Finalizado"), EM_CONSTRUCAO("CO", "Em Construção");

	private String valor;
	private String descricao;

	private ContratoReceitaSituacaoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static ContratoReceitaSituacaoEnum getEnumPorValor(String valor) {
		for (ContratoReceitaSituacaoEnum contratoReceitaSituacaoEnum : ContratoReceitaSituacaoEnum.values()) {
			if (contratoReceitaSituacaoEnum.getValor().equalsIgnoreCase(valor)) {
				return contratoReceitaSituacaoEnum;
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
