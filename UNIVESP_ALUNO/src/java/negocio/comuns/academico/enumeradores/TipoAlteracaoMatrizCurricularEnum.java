package negocio.comuns.academico.enumeradores;

public enum TipoAlteracaoMatrizCurricularEnum {
 
	ADICIONAR_GRADE_CURRICULAR_GRUPO_OPTATIVA("INCLUSÃO - Grupo de Optativa"),
	EXCLUIR_GRADE_CURRICULAR_GRUPO_OPTATIVA("EXCLUSÃO - Grupo de Optativa"),
	ADICIONAR_GRUPO_OPTATIVA_DISCIPLINA("INCLUSÃO - Disciplina (Optativa)"),
	EXCLUIR_GRUPO_OPTATIVA_DISCIPLINA("EXCLUSÃO - Disciplina (Optativa)"),
	ADICIONAR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA("INCLUSÃO - Disciplina Composta (Optativa)"),
	EDITAR_GRUPO_OPTATIVA_DISCIPLINA("ALTERAÇÃO - Disciplina (Optativa)"),
	EDITAR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA("ALTERAÇÃO - Disciplina Composta (Optativa)"),
	EXCLUIR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA("EXCLUSÃO - Disciplina Composta (Optativa)"),
	DEFINIR_PRE_REQUISITO_GRUPO_DISCIPLINA_OPTATIVA("ALTERAÇÃO - Pré-Requisito Disciplina (Optativa)"),
	EXCLUIR_GRADE_DISCIPLINA_COMPOSTA("EXCLUSÃO - Disciplina Composta"),
	ADICIONAR_GRUPO_OPTATIVA_PERIODO_LETIVO("INCLUSÃO - Grupo de Optativa no Periodo Letivo"),
	EXCLUIR_GRUPO_OPTATIVA_PERIODO_LETIVO("EXCLUSÃO - Grupo de Optativa no Periodo Letivo"),
	EDITAR_GRUPO_OPTATIVA_PERIODO_LETIVO("ALTERAÇÃO - Grupo de Optativa no Periodo Letivo"),
	ADICIONAR_GRADE_DISCIPLINA("INCLUSÃO - Disciplina"),
	EDITAR_GRADE_DISCIPLINA("ALTERAÇÃO - Disciplina"),
	EXCLUIR_GRADE_DISCIPLINA("EXCLUSÃO - Disciplina"),
	ADICIONAR_GRADE_DISCIPLINA_COMPOSTA("INCLUSÃO - Disciplina Composta"),
	EDITAR_GRADE_DISCIPLINA_COMPOSTA("ALTERAÇÃO - Disciplina Composta"),
	ADICIONAR_PERIODO_LETIVO("INCLUSÃO - Período Letivo"),
	EXCLUIR_PERIODO_LETIVO("EXCLUSÃO - Período Letivo"),
	EDITAR_PERIODO_LETIVO("ALTERAÇÃO - Período Letivo"),
	EDITAR_DADOS_GERAIS_GRADE("ALTERAÇÃO geral na grade"),
	EXCLUIR_PRE_REQUISITO_GRADE_DISCIPLINA("EXCLUSÃO - Pré-Requisito"),
	ADICIONAR_PRE_REQUISITO_GRADE_DISCIPLINA("INCLUSÃO - Pré-Requisito"),
	EXCLUIR_PRE_REQUISITO_GRUPO_OPTATIVA_DISCIPLINA("EXCLUSÃO - Pré-Requisito (Optativa)"),
	ADICIONAR_PRE_REQUISITO_GRUPO_OPTATIVA_DISCIPLINA("INCLUSÃO - Pré-Requisito (Optativa)"),
	NENHUM("Nenhum");
	
	private String descricao;
	
	TipoAlteracaoMatrizCurricularEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
