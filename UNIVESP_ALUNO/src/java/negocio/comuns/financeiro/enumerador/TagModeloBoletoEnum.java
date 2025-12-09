package negocio.comuns.financeiro.enumerador;

public enum TagModeloBoletoEnum {
	ANTES_ATE_DEPOIS("ANTES_ATE_DEPOIS", "ANTES_ATE_DEPOIS"), TOTAL_DESCONTOS("TOTAL_DESCONTOS", "TOTAL_DESCONTOS"), VALOR_TOTAL("VALOR_TOTAL", "VALOR_TOTAL"), 
	DESCONTO_ALUNO("DESCONTO_ALUNO", "DESCONTO_ALUNO"), JURO_PORCETAGEM("JURO_PORCETAGEM", "JURO_PORCETAGEM"),
	DESCONTO_PROGRESSIVO("DESCONTO_PROGRESSIVO", "DESCONTO_PROGRESSIVO"), JURO_VALOR("JURO_VALOR", "JURO_VALOR"),
	DESCONTO_INSTITUCIONAL("DESCONTO_INSTITUCIONAL", "DESCONTO_INSTITUCIONAL"), MULTA_PORCETAGEM("MULTA_PORCETAGEM", "MULTA_PORCETAGEM"),
	DESCONTO_CONVENIO("DESCONTO_CONVENIO", "DESCONTO_CONVENIO"), MULTA_VALOR("MULTA_VALOR", "MULTA_VALOR"),
	DESCONTO_RATEIO("DESCONTO_RATEIO", "DESCONTO_RATEIO"),
	DATA_MAXIMO_RECEBIMENTO("DATA_MAXIMO_RECEBIMENTO", "DATA_MAXIMO_RECEBIMENTO"),
	NR_PARCELA("NR_PARCELA", "NR_PARCELA"),
	CODIGO_ADMINISTRATIVO("CODIGO_ADMINISTRATIVO", "CODIGO_ADMINISTRATIVO");

	private String valor;
	private String descricao;

	private TagModeloBoletoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TagModeloBoletoEnum getEnumPorValor(String valor) {
		for (TagModeloBoletoEnum contratoReceitaSituacaoEnum : TagModeloBoletoEnum.values()) {
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
