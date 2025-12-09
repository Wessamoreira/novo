package negocio.comuns.academico.enumeradores;

public enum SituacaoPlanoEnsinoEnum {

	AUTORIZADO("AU", "Autorizado/ Publicado"),
	AGUARDANDO_APROVACAO("AA", "Aguardando Aprovação"),
	EM_REVISAO("EM", "Em revisão"),
	PENDENTE("PE", "Pendente"),
	NAO_CADASTRADO("NC", "Não Cadastrado"),
	TODOS("TO", "Todos");

	String valor;
	String descricao;

	private SituacaoPlanoEnsinoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoPlanoEnsinoEnum getEnum(String valor) {
		SituacaoPlanoEnsinoEnum[] valores = values();
		for (SituacaoPlanoEnsinoEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		SituacaoPlanoEnsinoEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
