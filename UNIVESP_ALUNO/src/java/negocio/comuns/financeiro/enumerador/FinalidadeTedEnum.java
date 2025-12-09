package negocio.comuns.financeiro.enumerador;

public enum FinalidadeTedEnum {
	
	PAGAMENTO_IMPOSTOS_TRIBUTOS("00001","Pagamento de Impostos Tributos e Taxas"),
	PAGAMENTO_CONCESSIONARIA_SERVICO_PUBLICO("00002","Pagamento a Concessionárias Serviço Público"),
	PAGAMENTO_DIVIDENDOS("00003","Pagamento de Dividendos"),
	PAGAMENTO_FORNECEDORES("00005","Pagamento a Fornecedores"),
	PAGAMENTO_HONORARIOS("00006","Pagamento de Honorários"),
	PAGAMENTO_ALUGUEL("00007","Pagamento de aluguéis e taxas condomínio"),
	PAGAMENTO_DUPLICATAS("00008","Pagamento de duplicatas e títulos"),
	PAGAMENTO_MENSALIDADE_ESCOLAR("00009","Pagamento de mensalidade escolar"),
	CREDITO_CONTA("00010","Crédito em conta"),
	PENSAO_ALIMENTICIA("00101","Pensão alimentícia"),
	OUTROS("99999","Outros");
	
	private String valor;
	private String descricao;
	
	private FinalidadeTedEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getValor() {
		return this.valor;
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
