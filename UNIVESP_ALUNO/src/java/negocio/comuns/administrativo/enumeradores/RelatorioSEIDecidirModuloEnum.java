package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum RelatorioSEIDecidirModuloEnum {

	ACADEMICO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO, RelatorioSEIDecidirNivelDetalhamentoEnum.NIVEL_EDUCACIONAL, RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURNO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURMA, RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA, RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO  }),
	ADMINISTRATIVO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO}),
	AVALIACAO_INSTITUCIONAL(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO}),
	BIBLIOTECA(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.CATALOGO, RelatorioSEIDecidirNivelDetalhamentoEnum.EMPRESTIMO}),
	COMPRA(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO}),
	CRM(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.COMPROMISSO, RelatorioSEIDecidirNivelDetalhamentoEnum.PROSPECTS, RelatorioSEIDecidirNivelDetalhamentoEnum.PRE_INSCRICAO, RelatorioSEIDecidirNivelDetalhamentoEnum.INTERACOES_WORKFLOW, RelatorioSEIDecidirNivelDetalhamentoEnum.FOLLOW_UP, RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO, RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO, RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA}),
	EAD(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO}),
	ESTAGIO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.ESTAGIO, RelatorioSEIDecidirNivelDetalhamentoEnum.RELATORIO_ESTAGIO, RelatorioSEIDecidirNivelDetalhamentoEnum.TERMO_ADITIVO, RelatorioSEIDecidirNivelDetalhamentoEnum.TERMO_RESCISAO}),
	FINANCEIRO_ARVORE_FECHAMENTO_MES(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO, RelatorioSEIDecidirNivelDetalhamentoEnum.NIVEL_EDUCACIONAL, RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURNO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURMA, RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA }),
	FINANCEIRO_CENTRO_RESULTADO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.CENTRO_RESULTADO}),
	FINANCEIRO_DESPESA(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO, RelatorioSEIDecidirNivelDetalhamentoEnum.NIVEL_EDUCACIONAL, RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURNO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURMA, RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA }),
	FINANCEIRO_FECHAMENTO_MES_RECEITA(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO, RelatorioSEIDecidirNivelDetalhamentoEnum.NIVEL_EDUCACIONAL, RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURNO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURMA, RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA }),
	FINANCEIRO_RECEITA(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO, RelatorioSEIDecidirNivelDetalhamentoEnum.NIVEL_EDUCACIONAL, RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURNO, RelatorioSEIDecidirNivelDetalhamentoEnum.TURMA, RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA }),
	NOTA_FISCAL(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO}),
	PATRIMONIO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {}),
	PLANO_ORCAMENTARIO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.PLANO_ORCAMENTARIO, RelatorioSEIDecidirNivelDetalhamentoEnum.REQUISICAO, RelatorioSEIDecidirNivelDetalhamentoEnum.PRODUTO_SERVICO, RelatorioSEIDecidirNivelDetalhamentoEnum.CENTRORESULTADO_CATEGORIADESPESA}),
	PROCESSO_SELETIVO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.PROCESSO_SELETIVO, RelatorioSEIDecidirNivelDetalhamentoEnum.INSCRICAO}),
	RECURSOS_HUMANOS(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.FUNCIONARIO_CARGO, RelatorioSEIDecidirNivelDetalhamentoEnum.CONTRA_CHEQUE, RelatorioSEIDecidirNivelDetalhamentoEnum.FALTAS_FUNCIONARIO, RelatorioSEIDecidirNivelDetalhamentoEnum.PERIODO_AQUISITIVO, RelatorioSEIDecidirNivelDetalhamentoEnum.PROGRESSAO_SALARIAL, RelatorioSEIDecidirNivelDetalhamentoEnum.CONTA_PAGAR_FUNCIONARIO, RelatorioSEIDecidirNivelDetalhamentoEnum.EVENTO_FOLHA_PAGAMENTO, RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR, RelatorioSEIDecidirNivelDetalhamentoEnum.CROSSTAB}),
	REQUERIMENTO(new RelatorioSEIDecidirNivelDetalhamentoEnum[] {RelatorioSEIDecidirNivelDetalhamentoEnum.REQUERIMENTO});


	private RelatorioSEIDecidirModuloEnum(
			RelatorioSEIDecidirNivelDetalhamentoEnum[] relatorioSEIDecidirNivelDetalhamentoEnums) {
		this.relatorioSEIDecidirNivelDetalhamentoEnums = relatorioSEIDecidirNivelDetalhamentoEnums;
	}

	private RelatorioSEIDecidirNivelDetalhamentoEnum[] relatorioSEIDecidirNivelDetalhamentoEnums;

	public RelatorioSEIDecidirNivelDetalhamentoEnum[] getRelatorioSEIDecidirNivelDetalhamentoEnums() {
		return relatorioSEIDecidirNivelDetalhamentoEnums;
	}

	public void setRelatorioSEIDecidirNivelDetalhamentoEnums(
			RelatorioSEIDecidirNivelDetalhamentoEnum[] relatorioSEIDecidirNivelDetalhamentoEnums) {
		this.relatorioSEIDecidirNivelDetalhamentoEnums = relatorioSEIDecidirNivelDetalhamentoEnums;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RelatorioSEIDecidirModuloEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public static RelatorioSEIDecidirModuloEnum getModuloAcessoMenu(String origem){
		if(origem == null || origem.trim().isEmpty()){
			return null;
		}
		if(origem.equals("DESPESA")){
			return RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA;
		}
		if(origem.equals("RECEITA")){
			return RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA;
		}
		if(origem.equals("ACADEMICO")){
			return RelatorioSEIDecidirModuloEnum.ACADEMICO;
		}
		if(origem.equals("RECURSOS_HUMANOS")){
			return RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS;
		}
		if(origem.equals("FECHAMENTO_MES_RECEITA")){
			return RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA;
		}
		if(origem.equals("FINANCEIRO_ARVORE_FECHAMENTO_MES")){
			return RelatorioSEIDecidirModuloEnum.FINANCEIRO_ARVORE_FECHAMENTO_MES;
		}
		if(origem.equals("FINANCEIRO_CENTRO_RESULTADO")){
			return RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO;
		}
		if(origem.equals("ESTAGIO")){
			return RelatorioSEIDecidirModuloEnum.ESTAGIO;
		}
		if(origem.equals("CRM")){
			return RelatorioSEIDecidirModuloEnum.CRM;
		}
		if(origem.equals("PLANO_ORCAMENTARIO")){
			return RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO;
		}
		if(origem.equals("REQUERIMENTO")){
			return RelatorioSEIDecidirModuloEnum.REQUERIMENTO;
		}
		if(origem.equals("PROCESSO_SELETIVO")){
			return RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO;
		}
		
		if(origem.equals("ADMINISTRATIVO")){
			return RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO;
		}
		if(origem.equals("AVALIACAO_INSTITUCIONAL")){
			return RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL;
		}
		if(origem.equals("COMPRA")){
			return RelatorioSEIDecidirModuloEnum.COMPRA;
		}
		if(origem.equals("EAD")){
			return RelatorioSEIDecidirModuloEnum.EAD;
		}
		if(origem.equals("ESTAGIO")){
			return RelatorioSEIDecidirModuloEnum.ESTAGIO;
		}
		if(origem.equals("NOTA_FISCAL")){
			return RelatorioSEIDecidirModuloEnum.NOTA_FISCAL;
		}
		if(origem.equals("PATRIMONIO")){
			return RelatorioSEIDecidirModuloEnum.PATRIMONIO;
		}
		return null;
	}
}
