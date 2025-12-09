package negocio.comuns.financeiro.enumerador;

public enum FinalidadePixEnum {	
	
	CREDITO_EM_CONTA_CORRENTE("01","Crédito em Conta Corrente"),
	PAGAMENTO_ALUGUEL("02", "Pagamento de Aluguel / Condomínio"),
	PAGAMENTO_DUPLICATAS("03", "Pagamento de Duplicatas e Títulos"),
	PAGAMENTO_DIVIDENDOS("04","Pagamento de Dividendos"),
	PAGAMENTO_MENSALIDADES_ESCOLARES("05","Pagamento de Mensalidades Escolares"),
	PAGAMENTO_FORNECEDOR("07","Pagamento a Fornecedor / Honorários"),
	PAGAMENTO_CAMBIO("08","Pagamento de Câmbio / Fundos / Bolsas"),
	PAGAMENTO_TRIBUTOS("09","Repasse de Arrecadação / Pagamento de Tributos"),
	DOC_POUPANCA("11","DOC para Poupança"),
	DOC_DEPOSITO_JUDICIAL("12","DOC para Depósito Judicial"),
	PENSAO_ALIMENTICIA("13","Pensão Alimentícia"),
	OUTROS("99","Outros");
		
	private String valor;
	private String descricao;
	
	private FinalidadePixEnum(String valor, String descricao) {
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
	
	public boolean isDocPoupanca(){
		return equals(FinalidadePixEnum.DOC_POUPANCA);
	}



}
