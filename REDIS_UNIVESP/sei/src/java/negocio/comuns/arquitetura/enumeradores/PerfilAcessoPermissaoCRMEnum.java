package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoCRMEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Campanha
	 *
	 */
	CAMPANHA("Campanha",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Campanha_titulo"), UteisJSF.internacionalizar("per_Campanha_ajuda"),
					new String[] { "campanhaCons.xhtml", "campanhaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	PERMITIR_VISUALIZAR_CAMPANHA_COBRANCA("PermitirVisualizarCampanhaCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarCampanhaCobranca_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarCampanhaCobranca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.CAMPANHA,
			PerfilAcessoSubModuloEnum.CRM_CRM),

	PERMITIR_VISUALIZAR_CAMPANHA_VENDAS("PermitirVisualizarCampanhaVendas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarCampanhaVendas_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarCampanhaVendas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.CAMPANHA,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * extrato Comissao Rel
	 *
	 */
	EXTRATO_COMISSAO_REL("extratoComissaoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_extratoComissaoRel_titulo"),
					UteisJSF.internacionalizar("per_extratoComissaoRel_ajuda"),
					new String[] { "extratoComissaoSinteticoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM),
	/**
	 * Produtividade Rel
	 *
	 */
	PRODUTIVIDADE_CONSULTOR_REL("ProdutividadeConsultorRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_produtividadeConsultorRel_titulo"),
					UteisJSF.internacionalizar("per_produtividadeConsultorRel_ajuda"),
					new String[] { "produtividadeConsultorRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM),
	/**
	 * Consultor Por Matricula Rel
	 *
	 */
	CONSULTOR_POR_MATRICULA_REL("ConsultorPorMatriculaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConsultorPorMatriculaRel_titulo"),
					UteisJSF.internacionalizar("per_ConsultorPorMatriculaRel_ajuda"),
					new String[] { "consultorPorMatriculaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM),
	/**
	 * recibo Comissoes Rel
	 *
	 */
	RECIBO_COMISSOES_REL("reciboComissoesRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_reciboComissoesRel_titulo"),
			UteisJSF.internacionalizar("per_reciboComissoesRel_ajuda"), new String[] { "reciboComissoesRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM),
	/**
	 * Busca Prospect
	 *
	 */
	BUSCA_PROSPECT("BuscaProspect",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BuscaProspect_titulo"),
					UteisJSF.internacionalizar("per_BuscaProspect_ajuda"), new String[] { "buscaProspect.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	BUSCA_PROSPECT_CONSULTAR_PROSPECT_OUTROS_CONSULTORES_ULTIMA_INTERACAO(
			"BuscaProspect_consultarProspectOutrosConsultoresUltimaInteracao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_BuscaProspectconsultarProspectOutrosConsultoresUltimaInteracao_titulo"),
					UteisJSF.internacionalizar(
							"per_BuscaProspectconsultarProspectOutrosConsultoresUltimaInteracao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.BUSCA_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	BUSCA_PROSPECT_GERAR_LISTA_EMAIL("BuscaProspect_gerarListaEmail",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BuscaProspectgerarListaEmail_titulo"),
					UteisJSF.internacionalizar("per_BuscaProspectgerarListaEmail_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.BUSCA_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	BUSCA_PROSPECT_CONSULTAR_TODAS_UNIDADES("BuscaProspect_consultarTodasUnidades",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BuscaProspectconsultarTodasUnidades_titulo"),
					UteisJSF.internacionalizar("per_BuscaProspectconsultarTodasUnidades_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.BUSCA_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	BUSCA_PROSPECT_CONSULTAR_PROSPECT_OUTROS_CONSULTORES_RESPONSAVEIS(
			"BuscaProspect_consultarProspectOutrosConsultoresResponsaveis",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_BuscaProspectconsultarProspectOutrosConsultoresResponsaveis_titulo"),
					UteisJSF.internacionalizar(
							"per_BuscaProspectconsultarProspectOutrosConsultoresResponsaveis_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.BUSCA_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Painel Gestor Supervisao Venda
	 *
	 */
	PAINEL_GESTOR_SUPERVISAO_VENDA("PainelGestorSupervisaoVenda",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorSupervisaoVenda_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorSupervisaoVenda_ajuda"),
					new String[] { "painelGestorSupervisaoVenda.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Comissionamento Turma
	 *
	 */
	COMISSIONAMENTO_TURMA("ComissionamentoTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ComissionamentoTurma_titulo"),
					UteisJSF.internacionalizar("per_ComissionamentoTurma_ajuda"),
					new String[] { "comissionamentoTurmaCons.xhtml", "comissionamentoTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Segmentacao Prospect
	 *
	 */
	SEGMENTACAO_PROSPECT("SegmentacaoProspect",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SegmentacaoProspect_titulo"),
					UteisJSF.internacionalizar("per_SegmentacaoProspect_ajuda"),
					new String[] { "segmentacaoProspectCons.xhtml", "segmentacaoProspectForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	PROSPECTS_UNIFICAR_PROSPECTS_DUPLICADOS("Prospects_unificarProspectsDuplicados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProspectsunificarProspectsDuplicados_titulo"),
					UteisJSF.internacionalizar("per_ProspectsunificarProspectsDuplicados_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.SEGMENTACAO_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	PROSPECTS_ALTERAR_CONSULTOR_RESPONSAVEL("Prospects_alterarConsultorResponsavel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProspectsalterarConsultorResponsavel_titulo"),
					UteisJSF.internacionalizar("per_ProspectsalterarConsultorResponsavel_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.SEGMENTACAO_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Percentual Configuracao Ranking
	 *
	 */
	PERCENTUAL_CONFIGURACAO_RANKING("PercentualConfiguracaoRanking",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PercentualConfiguracaoRanking_titulo"),
					UteisJSF.internacionalizar("per_PercentualConfiguracaoRanking_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Tipo Contato
	 *
	 */
	TIPO_CONTATO("TipoContato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_TipoContato_titulo"), UteisJSF.internacionalizar("per_TipoContato_ajuda"),
			new String[] { "tipoContatoCons.xhtml", "tipoContatoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Tipo Midia Captacao
	 *
	 */
	TIPO_MIDIA_CAPTACAO("TipoMidiaCaptacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoMidiaCaptacao_titulo"),
					UteisJSF.internacionalizar("per_TipoMidiaCaptacao_ajuda"),
					new String[] { "tipoMidiaCaptacaoCons.xhtml", "tipoMidiaCaptacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),

	/**
	 * Configuracao Ranking
	 *
	 */
	CONFIGURACAO_RANKING("ConfiguracaoRanking",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoRanking_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoRanking_ajuda"),
					new String[] { "configuracaoRankingCons.xhtml", "configuracaoRankingForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Painel Gestor Vendedor
	 *
	 */
	PAINEL_GESTOR_VENDEDOR("PainelGestorVendedor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorVendedor_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorVendedor_ajuda"),
					new String[] { "painelGestorCrmVendedor.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),

	/**
	 * Follow Up Prospect
	 *
	 */
	FOLLOW_UP_PROSPECT("FollowUpProspect",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FollowUpProspect_titulo"),
					UteisJSF.internacionalizar("per_FollowUpProspect_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	PERMITI_MATRICULA_DIRETA_FOLLOW_UP("PermitiMatriculaDiretaFollowUp",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitiMatriculaDiretaFollowUp_titulo"),
					UteisJSF.internacionalizar("per_PermitiMatriculaDiretaFollowUp_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.FOLLOW_UP_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	PERMITI_SELECIONAR_FUNCIONARIO_COMPROMISSO("PermitiSelecionarFuncionarioCompromisso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitiSelecionarFuncionarioMatricula_titulo"),
					UteisJSF.internacionalizar("per_PermitiSelecionarFuncionarioMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.FOLLOW_UP_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	ALTERAR_OBSERVACAO_INTERACAO_FOLLOW_UP("AlterarObservacaoInteracaoFollowUp",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarObservacaoInteracaoFollowUp_titulo"),
					UteisJSF.internacionalizar("per_AlterarObservacaoInteracaoFollowUp_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.FOLLOW_UP_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	GRAVAR_INTERACAO_FOLLOW_UP("GravarInteracaoFollowUp",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GravarInteracaoFollowUp_titulo"),
					UteisJSF.internacionalizar("per_GravarInteracaoFollowUp_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.FOLLOW_UP_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	VISUALIZAR_FOLLOW_UP_PROSPECT("VisualizarFollowUpProspect",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_VisualizarFollowUpProspect_titulo"),
					UteisJSF.internacionalizar("per_VisualizarFollowUpProspect_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.FOLLOW_UP_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	EXCLUIR_HISTORICO_FOLLOW_UP("ExcluirHistoricoFollowUp",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExcluirHistoricoFollowUp_titulo"),
					UteisJSF.internacionalizar("per_ExcluirHistoricoFollowUp_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.FOLLOW_UP_PROSPECT,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Situacao Prospect Pipeline
	 *
	 */
	SITUACAO_PROSPECT_PIPELINE("SituacaoProspectPipeline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SituacaoProspectPipeline_titulo"),
					UteisJSF.internacionalizar("per_SituacaoProspectPipeline_ajuda"),
					new String[] { "situacaoProspectPipelineCons.xhtml", "situacaoProspectPipelineForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Ranking
	 *
	 */
	RANKING("Ranking",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Ranking_titulo"),
							UteisJSF.internacionalizar("per_Ranking_ajuda"), new String[] { "rankingForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Workflow
	 *
	 */
	WORKFLOW("Workflow",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Workflow_titulo"), UteisJSF.internacionalizar("per_Workflow_ajuda"),
					new String[] { "workflowCons.xhtml", "workflowForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Agenda Pessoa
	 *
	 */
	AGENDA_PESSOA("AgendaPessoa", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_AgendaPessoa_titulo"), UteisJSF.internacionalizar("per_AgendaPessoa_ajuda"),
			new String[] { "agendaPessoaCons.xhtml", "agendaPessoaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	AGENDA_PERMITIR_MATRICULA_DIRETAMENTE_AGENDA("Agenda_permitirMatriculaDiretamenteAgenda",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AgendapermitirMatriculaDiretamenteAgenda_titulo"),
					UteisJSF.internacionalizar("per_AgendapermitirMatriculaDiretamenteAgenda_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.AGENDA_PESSOA,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	AGENDA_PERMITIR_EXCLUIR_COMPROMISSO_NAO_INICIADO("Agenda_permitirExcluirCompromissoNaoIniciado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AgendapermitirExcluirCompromissoNaoIniciado_titulo"),
					UteisJSF.internacionalizar("per_AgendapermitirExcluirCompromissoNaoIniciado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.AGENDA_PESSOA,
			PerfilAcessoSubModuloEnum.CRM_CRM),

	VISAO_ADMINISTRADOR_AGENDA_PESSOA("VisaoAdministradorAgendaPessoa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_VisaoAdministradorAgendaPessoa_titulo"),
					UteisJSF.internacionalizar("per_VisaoAdministradorAgendaPessoa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.AGENDA_PESSOA,
			PerfilAcessoSubModuloEnum.CRM_CRM),

	VER_AGENDA_OUTRAS_UNIDADES_VISAO_ADMINISTRATIVA_CRM("verAgendaOutrasUnidadesVisaoAdministrativaCRM",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_verAgendaOutrasUnidadesVisaoAdministrativaCRM_titulo"),
					UteisJSF.internacionalizar("per_verAgendaOutrasUnidadesVisaoAdministrativaCRM_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.AGENDA_PESSOA,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Novo Prospect
	 *
	 */
	NOVO_PROSPECT("NovoProspect",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NovoProspect_titulo"),
					UteisJSF.internacionalizar("per_NovoProspect_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Meta
	 *
	 */
	META("Meta",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Meta_titulo"), UteisJSF.internacionalizar("per_Meta_ajuda"),
					new String[] { "metaCons.xhtml", "metaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Interacao Workflow
	 *
	 */
	INTERACAO_WORKFLOW("InteracaoWorkflow",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InteracaoWorkflow_titulo"),
					UteisJSF.internacionalizar("per_InteracaoWorkflow_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	VOLTAR_ETAPA_ANTERIOR("VoltarEtapaAnterior",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_VoltarEtapaAnterior_titulo"),
					UteisJSF.internacionalizar("per_VoltarEtapaAnterior_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.INTERACAO_WORKFLOW,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Pre Inscricao Log
	 *
	 */
	PRE_INCRICAO_LOG("PreInscricaoLog",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_preIncricaoLog_titulo"),
					UteisJSF.internacionalizar("per_preIncricaoLog_ajuda"),
					new String[] { "preInscricaoLogCons.xhtml", "preInscricaoLogForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Prospects
	 *
	 */
	PROSPECTS("Prospects", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Prospects_titulo"), UteisJSF.internacionalizar("per_Prospects_ajuda"),
			new String[] { "prospectsCons.xhtml", "prospectsForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.CRM_CRM),

	LIBERAR_EDICAO_PROSPECT_VINCULADO_PESSOA_SOMENTE_COM_SENHA(
			"Prospects_LiberarEdicaoProspectVinculadoPessoaSomenteComSenha",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_Prospects_LiberarEdicaoProspectVinculadoPessoaSomenteComSenha_titulo"),
					UteisJSF.internacionalizar(
							"per_Prospects_LiberarEdicaoProspectVinculadoPessoaSomenteComSenha_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoCRMEnum.PROSPECTS,
			PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Pre Inscricao
	 *
	 */
	PRE_INSCRICAO("PreInscricao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PreInscricao_titulo"),
					UteisJSF.internacionalizar("per_PreInscricao_ajuda"), new String[] { "homePreInscricao.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),

	/**
	 * Motivo Insucesso
	 *
	 */
	MOTIVO_INSUCESSO("MotivoInsucesso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MotivoInsucesso_titulo"),
					UteisJSF.internacionalizar("per_MotivoInsucesso_ajuda"),
					new String[] { "motivoInsucessoCons.xhtml", "motivoInsucessoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),
	/**
	 * Registro Entrada
	 *
	 */
	REGISTRO_ENTRADA("RegistroEntrada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroEntrada_titulo"),
					UteisJSF.internacionalizar("per_RegistroEntrada_ajuda"),
					new String[] { "registroEntradaCons.xhtml", "registroEntradaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.CRM_CRM),

	RELATORIO_SEIDECIDIR_CRM("RelatorioSEIDecidirCrm",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirCrm_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirCrm_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM),

	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_CRM_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirCrmApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirReceitaApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirReceitaApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_CRM,
			PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM),

	/**
	 * extrato Comissao Rel
	 *
	 */
	POS_VENDA_REL("PosVendaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_posVendaRel_titulo"),
					UteisJSF.internacionalizar("per_posVendaRel_ajuda"), new String[] { "posVendaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.CRM_RELATORIOS_CRM);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoCRMEnum(String valor, PermissaoVisao[] permissaoVisao,
			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
			PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.valor = valor;
		this.permissaoVisao = permissaoVisao;
		this.tipoPerfilAcesso = tipoPerfilAcesso;
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	private String valor;
	private PermissaoVisao[] permissaoVisao;
	private TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso;
	private PerfilAcessoSubModuloEnum perfilAcessoSubModulo;
	private Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum;

	/**
	 * @return the tipoPerfilAcesso
	 */
	public TipoPerfilAcessoPermissaoEnum getTipoPerfilAcesso() {
		if (tipoPerfilAcesso == null) {
			tipoPerfilAcesso = TipoPerfilAcessoPermissaoEnum.ENTIDADE;
		}
		return tipoPerfilAcesso;
	}

	/**
	 * @param tipoPerfilAcesso the tipoPerfilAcesso to set
	 */
	public void setTipoPerfilAcesso(TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso) {
		this.tipoPerfilAcesso = tipoPerfilAcesso;
	}

	/**
	 * @return the perfilAcessoSubModulo
	 */
	public PerfilAcessoSubModuloEnum getPerfilAcessoSubModulo() {
		if (perfilAcessoSubModulo == null) {
			perfilAcessoSubModulo = PerfilAcessoSubModuloEnum.TODOS;
		}
		return perfilAcessoSubModulo;
	}

	/**
	 * @param perfilAcessoSubModulo the perfilAcessoSubModulo to set
	 */
	public void setPerfilAcessoSubModulo(PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the permissaoSuperiorEnum
	 */
	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissaoSuperiorEnum() {
		return permissaoSuperiorEnum;
	}

	/**
	 * @param permissaoSuperiorEnum the permissaoSuperiorEnum to set
	 */
	public void setPermissaoSuperiorEnum(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum) {
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
	}

	/**
	 * @return the PermissaoVisao
	 */
	public PermissaoVisao[] getPermissaoVisao() {
		if (permissaoVisao == null) {
			permissaoVisao = new PermissaoVisao[0];
		}
		return permissaoVisao;
	}

	/**
	 * @param PermissaoVisao the PermissaoVisao to set
	 */
	public void setPermissaoVisao(PermissaoVisao[] PermissaoVisao) {
		this.permissaoVisao = PermissaoVisao;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public Boolean getUtilizaVisao(TipoVisaoEnum tipoVisaoEnum) {
		for (PermissaoVisao permissaoVisao : getPermissaoVisao()) {
			if (permissaoVisao.equals(tipoVisaoEnum)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return getPermissaoVisao(tipoVisaoEnum).getDescricao();
		}
		return "";
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return getPermissaoVisao(tipoVisaoEnum).getAjuda();
		}
		return "";
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return Arrays.asList(getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso());
		}
		return null;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			for (PermissaoVisao permissaoVisao2 : getPermissaoVisao()) {
				if (permissaoVisao2.equals(tipoVisaoEnum)) {
					return permissaoVisao2;
				}
			}
		}
		return null;
	}

	public Boolean getIsApresentarApenasPermissaoTotal() {
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)
				&& getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
	}

	public String descricaoModulo;

	@Override
	public String getDescricaoModulo() {
		if (descricaoModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoModulo += getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar();
			} else {
				descricaoModulo = "";
			}
		}
		return descricaoModulo;
	}

	public String descricaoSubModulo;

	@Override
	public String getDescricaoSubModulo() {
		if (descricaoSubModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoSubModulo = getPerfilAcessoSubModulo().getDescricao();

			} else {
				descricaoSubModulo = "";
			}
		}
		return descricaoSubModulo;
	}

	public String descricaoModuloSubModulo;

	@Override
	public String getDescricaoModuloSubModulo() {
		if (descricaoModuloSubModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()
						+ " - " + getPerfilAcessoSubModulo().getDescricao();
			} else {
				descricaoModuloSubModulo = "";
			}
		}
		return descricaoModuloSubModulo;
	}

}
