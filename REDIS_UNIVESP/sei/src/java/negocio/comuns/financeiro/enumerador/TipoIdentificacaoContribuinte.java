package negocio.comuns.financeiro.enumerador;

public enum TipoIdentificacaoContribuinte {
	CPF("033", "1", "Cpf"),
	CNPJ("033", "2", "Cnpj"),
	NIT_PIS_PASEP("033", "3", "Nit/ Pis / Pasep"),
	CEI("033", "4", "Cei"),
	NB("033", "6", "Nb"),
	NUMERO_TITULO("033", "7", "Número do Título"),
	DEBCAD("033", "8", "Debcad"),
	REFERENCIA("033", "9", "Referência");		
			
	private String nrBanco;
	private String valor;
	private String descricao;

	private TipoIdentificacaoContribuinte(String nrBanco, String valor, String descricao) {
		this.nrBanco = nrBanco;
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

	public String getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}

}
