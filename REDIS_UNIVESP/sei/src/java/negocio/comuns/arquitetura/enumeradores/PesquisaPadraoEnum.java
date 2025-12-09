package negocio.comuns.arquitetura.enumeradores;

public enum PesquisaPadraoEnum {

	ALUNO("Pesquisar Aluno", "Pesquise pelo nome, matrícula, cpf ou email do aluno ou responsável"),
	PROSPECTS("Pesquisar Prospects", "Pesquise pelo nome, telefone, cpf ou email da pessoa");
	
	private String descricao;
	private String tooltip;
	
	private PesquisaPadraoEnum(String descricao, String tooltip) {
		this.descricao = descricao;
		this.tooltip = tooltip;
	}

	public String getName() {
		return this.name();
	}

	public static PesquisaPadraoEnum getEnum(String valor) {
		PesquisaPadraoEnum[] valores = values();
		for (PesquisaPadraoEnum obj : valores) {
			if (obj.name().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getTooltip() {
		return tooltip;
	}


	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

}