package negocio.comuns.academico.enumeradores;

public enum TituloImpactoMatrizCurricularEnum {

	EXCLUSAO_HISTORICO("Exclusão Histórico"),
	ALTERACAO_HISTORICO("Alteração Histórico"),
	EXCLUSAO_DISCIPLINA_TURMA("Exclusão Disciplina nas Turmas"),
	PROGRAMACAO_AULA_REGISTRO_ORFAO("Programação de Aula - Registros Orfãos"),
	REGISTRO_AUAL_REGISTRO_ORFAO("Registros de Aula - Registros Orfãos"),
	CONTROLE_VAGAS_DISCIPLINA_REGISTRO_ORFAO("Controle Vagas Disciplina - Registros Orfãos"),
	INCLUSAO_GRADE_DISCIPLINA("Inclusão Grade Disciplina");
	
	String titulo;
	
	TituloImpactoMatrizCurricularEnum(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
	
}
