package negocio.comuns.administrativo.enumeradores;

public enum TipoOrdenacaoDisciplinasPortalAluno {
	DATA_INICIO_AULA("Pela Data Inicial das Aulas"),
	AULA_INICIANDO_MES_ATUAL("Pela Data Inicial do mês"),
	NOME_DISCIPLINA("Pelo Nome da Disciplina"),
	ORDEM_ESTUDO_ONLINE_TURMA("Pela ordenação definida na turma");
	
	private final String descricao;
	
	TipoOrdenacaoDisciplinasPortalAluno(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
