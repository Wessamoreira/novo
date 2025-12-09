package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.financeiro.enumerador.TipoFiltroPeriodoSeiDecidirEnum;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirEntidadeEnum {

	UNIDADE_ENSINO("unidadeEnsino", "unidadeEnsino", TagSEIDecidirUnidadeEnsinoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA, RelatorioSEIDecidirModuloEnum.CRM, RelatorioSEIDecidirModuloEnum.REQUERIMENTO
			, RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO, RelatorioSEIDecidirModuloEnum.PATRIMONIO, RelatorioSEIDecidirModuloEnum.COMPRA, RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL, RelatorioSEIDecidirModuloEnum.EAD, RelatorioSEIDecidirModuloEnum.NOTA_FISCAL, RelatorioSEIDecidirModuloEnum.ESTAGIO}),
	CURSO("curso", "curso", TagSEIDecidirCursoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA, RelatorioSEIDecidirModuloEnum.CRM, RelatorioSEIDecidirModuloEnum.ESTAGIO}),
	TURNO("turno", "turno", TagSEIDecidirTurnoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA, RelatorioSEIDecidirModuloEnum.ESTAGIO}),
	TURMA("turma", "turma", TagSEIDecidirTurmaEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA, RelatorioSEIDecidirModuloEnum.ESTAGIO}),
	MATRICULA("matricula", "matricula", TagSEIDecidirMatriculaEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA, RelatorioSEIDecidirModuloEnum.CRM, RelatorioSEIDecidirModuloEnum.REQUERIMENTO, RelatorioSEIDecidirModuloEnum.ESTAGIO}),
	ALUNO("pessoa", "aluno", TagSEIDecidirAlunoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA, RelatorioSEIDecidirModuloEnum.REQUERIMENTO, RelatorioSEIDecidirModuloEnum.ESTAGIO}),

	RECEBIMENTO("negociacaoRecebimento", "recebimento", TagSEIDecidirRecebimentoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	CONTA_RECEBER("contaReceber", "contaReceber", TagSEIDecidirContaReceberEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	CONTA_PAGAR("contaPagar", "contaPagar", TagSEIDecidirContaPagarEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA}),
	CHEQUE("cheque", "cheque", TagSEIDecidirChequeEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),		

	CONTRA_CHEQUE("contraCheque", "contraCheque", TagSEIDecidirContraChequeEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	FUNCIONARIO_CARGO("funcionarioCargo", "funcionarioCargo", TagSEIDecidirFuncionarioCargoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	FALTAS_FUNCIONARIO("faltasFuncionario", "faltasFuncionario", TagSEIDecidirFaltasFuncionarioEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	PERIODO_AQUISITIVO("periodoAquisitivo", "periodoAquisitivo", TagSEIDecidirPeriodoAquisitivoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	CONTA_PAGAR_FUNCIONARIO("contaPagar", "contaPagar", TagSEIDecidirContaPagarEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	HISTORICO_SALARIAL("historicosalarial", "historicosalarial", TagSEIDecidirHistoricoSalarialEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	EVENTO_FOLHA_PAGAMENTO("eventoFolhaPagamento", "eventoFolhaPagamento", TagSEIDecidirEventoFolhaPagamentoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	
	
	ESTAGIO("estagio", "estagio", TagSEIDecidirEstagioEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ESTAGIO}),
	
	
	CONSULTOR("funcionario", "consultor", TagSEIDecidirFuncionarioEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	CAMPANHA("campanha", "campanha", TagSEIDecidirCampanhaEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	INTERACOES_WORKFLOW("InteracoesWorkflow", "InteracoesWorkflow", TagSEIDecidirInteracoesWorkflowEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	PROSPECTS("prospects", "prospects", TagSEIDecidirProspectsEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	AGENDA("agenda", "agenda", TagSEIDecidirAgendaEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	FOLLOW_UP("followUp", "followUp", TagSEIDecidirFollowUpEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	
	CENTRO_RESULTADO("centroResultado", "centroResultado", TagSEIDecidirCentroResultadoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO}),
	CENTRO_RESULTADO_ORIGEM("centroResultadoOrigem", "centroResultadoOrigem", TagSEIDecidirCentroResultadoOrigemEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO}),
	

	FECHAMENTO_FINANCEIRO_CONTA("fechamentoFinanceiroConta", "fechamentoFinanceiroConta", TagSEIDecidirFechamentoMesReceitaEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR("horaAtividadeExtraClasseProfessor", "horaAtividadeExtraClasseProfessor", TagSEIDecidirAtividadeExtraClasseProfessorEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	CROSSTAB("crosstab", "crosstab", TagSEIDecidirCrossTabRHEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS}),
	
	PLANO_ORCAMENTARIO("planoorcamentario", "planoorcamentario", TagSEIDecidirPlanoOrcamentario.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO}),
	REQUISICAO("requisicao", "requisicao", TagSEIDecidirRequisicao.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO}),
	PRODUTO_SERVICO("produtoservico", "produtoservico", TagSEIDecidirProdutoServico.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO}),
	CENTRORESULTADO_CATEGORIADESPESA("categoriadespesa", "categoriadespesa", TagSEIDecidirCentroResultadoCategoriaDespesa.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO}),
	
	HISTORICO("historico", "historico", TagSEIDecidirHistoricoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, }),
	REQUERIMENTO("requerimento", "requerimento", TagSEIDecidirRequerimentoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.REQUERIMENTO}),
	
	PROCESSO_SELETIVO("processoSeletivo", "processoSeletivo", TagSEIDecidirProcessoSeletivoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO}),
	INSCRICAO("inscricao", "inscricao", TagSEIDecidirInscricaoEnum.values(), new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO});

	private String entidade;
	private String alias;
	private RelatorioSEIDecidirModuloEnum[] modulos;
	private Enum<? extends PerfilTagSEIDecidirEnum>[] tagSeiDecidirEnum;

	private TagSEIDecidirEntidadeEnum(String entidade, String alias, Enum<? extends PerfilTagSEIDecidirEnum>[] tagSeiDecidirEnum, RelatorioSEIDecidirModuloEnum[] modulos) {
		this.entidade = entidade;
		this.alias = alias;
		this.tagSeiDecidirEnum = tagSeiDecidirEnum;
		this.modulos = modulos;
	}
	
	public String getEntidade() {
		if (entidade == null) {
			entidade = "";
		}
		return entidade;
	}
	
	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}
	
	public Enum<? extends PerfilTagSEIDecidirEnum>[] getTagSeiDecidirEnum() {		
		return tagSeiDecidirEnum;
	}
	
	public void setTagSeiDecidirEnum(Enum<? extends PerfilTagSEIDecidirEnum>[] tagSeiDecidirEnum) {
		this.tagSeiDecidirEnum = tagSeiDecidirEnum;
	}

	public String getAlias() {
		if (alias == null) {
			alias = "";
		}
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public RelatorioSEIDecidirModuloEnum[] getModulos() {		
		return modulos;
	}

	public void setModulos(RelatorioSEIDecidirModuloEnum[] modulos) {
		this.modulos = modulos;
	}
	
	public static TagSEIDecidirEntidadeEnum[] getValues(RelatorioSEIDecidirModuloEnum modulo, Boolean nivelDetalhamentoHistorico){
		TagSEIDecidirEntidadeEnum[] filtros = new TagSEIDecidirEntidadeEnum[TipoFiltroPeriodoSeiDecidirEnum.values().length];
		int y = 0;
		for(TagSEIDecidirEntidadeEnum filtro: TagSEIDecidirEntidadeEnum.values()){
			for(int x = 0; x<filtro.getModulos().length;x++){
				if(filtro.getModulos()[x].equals(modulo)){
					if (filtro.equals(TagSEIDecidirEntidadeEnum.HISTORICO) && !nivelDetalhamentoHistorico) {
						continue;
					} else {
						filtros[y++] = filtro;
					}
					break;
				}
			}
		}
		return filtros;
	}	
	
}
