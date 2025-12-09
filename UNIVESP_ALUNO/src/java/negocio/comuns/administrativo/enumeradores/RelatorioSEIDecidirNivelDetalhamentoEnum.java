package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum RelatorioSEIDecidirNivelDetalhamentoEnum {
	UNIDADE_ENSINO,
	NIVEL_EDUCACIONAL,
	CURSO,
	TURNO,
	TURMA,
	MATRICULA,
	CENTRO_RESULTADO,
	CONTA_PAGAR_FUNCIONARIO,
	HISTORICO,

	FUNCIONARIO_CARGO,
	FALTAS_FUNCIONARIO,
	CONTRA_CHEQUE,
	PERIODO_AQUISITIVO, 
	PROGRESSAO_SALARIAL,
	EVENTO_FOLHA_PAGAMENTO,
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR,
	CROSSTAB,

	COMPROMISSO,
	PRE_INSCRICAO,
	PROSPECTS,
	INTERACOES_WORKFLOW, 
	FOLLOW_UP,

	PLANO_ORCAMENTARIO,
	REQUISICAO,
	PRODUTO_SERVICO,
	CENTRORESULTADO_CATEGORIADESPESA,
	REQUERIMENTO,
	
	PROCESSO_SELETIVO,
	INSCRICAO,
	EIXO_CURSO,
	PROCESSO_SELETIVO_CURSO,
	
	CATALOGO,
	EMPRESTIMO,
	
	ESTAGIO,
	RELATORIO_ESTAGIO,
	TERMO_ADITIVO,
	TERMO_RESCISAO;
	

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RelatorioSEIDecidirNivelDetalhamentoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

}
