package negocio.comuns.academico.enumeradores;

public enum SituacaoPlanoFinanceiroCursoEnum {

	TODAS("TODAS"), EM_CONSTRUCAO("Em Construção"), ATIVO("Ativo"), INATIVO("Inativo");

	String descricao;

	SituacaoPlanoFinanceiroCursoEnum(String descricao) {
		this.descricao = descricao;
	}

	public static SituacaoPlanoFinanceiroCursoEnum getEnum(SituacaoPlanoFinanceiroCursoEnum situacao) {
		SituacaoPlanoFinanceiroCursoEnum[] valores = values();
		for (SituacaoPlanoFinanceiroCursoEnum obj : valores) {
			if (obj.name().equals(situacao.name())) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(SituacaoPlanoFinanceiroCursoEnum situacao) {
		SituacaoPlanoFinanceiroCursoEnum obj = getEnum(situacao);
		if (obj != null) {
			return obj.getDescricao();
		}
		return situacao.name();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
