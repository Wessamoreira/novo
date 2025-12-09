package relatorio.negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum CrossTabEnum {

	LABEL_COLUNA("LABEL_COLUNA", "Label Coluna"),
	LABEL_COLUNA2("LABEL_COLUNA2", "Label Coluna 2"),
	LABEL_LINHA("LABEL_LINHA", "Label Linha"),
	LABEL_LINHA2("LABEL_LINHA2", "Label Linha 2"),
	LABEL_LINHA3("LABEL_LINHA3", "Label Linha 3"),
	LABEL_LINHA4("LABEL_LINHA4", "Label Linha 4"),
	VALOR("VALOR", "Valor"),
	VALOR_2("VALOR_2", "Valor 2"),;

	String valor;
	String descricao;

	private CrossTabEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
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

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_CrossTabEnum_" + this.name());
	}

	public static CrossTabEnum getEnumPorValor(String valor) {
		for (CrossTabEnum cossTab : CrossTabEnum.values()) {

			if (cossTab.getValor().equals(valor))
				return cossTab;
		}

		return null;
	}

}
