package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoProcessoSeletivoEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Questionario Processo Seletivo
	 *
	 */
	QUESTIONARIO_PROCESSO_SELETIVO("QuestionarioProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuestionarioProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_QuestionarioProcessoSeletivo_ajuda"),
					new String[] { "questionarioCons.xhtml", "questionarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_QUESTIONARIO_PROCESSO_SELETIVO),
	/**
	 * Gabarito
	 *
	 */
	GABARITO("Gabarito",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Gabarito_titulo"), UteisJSF.internacionalizar("per_Gabarito_ajuda"),
					new String[] { "gabaritoCons.xhtml", "gabaritoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	/**
	 * Pergunta Processo Seletivo
	 *
	 */
	PERGUNTA_PROCESSO_SELETIVO("PerguntaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PerguntaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_PerguntaProcessoSeletivo_ajuda"),
					new String[] { "perguntaCons.xhtml", "perguntaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_QUESTIONARIO_PROCESSO_SELETIVO),
	/**
	 * Prova Processo Seletivo
	 *
	 */
	PROVA_PROCESSO_SELETIVO("ProvaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProvaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_ProvaProcessoSeletivo_ajuda"),
					new String[] { "provaProcessoSeletivoCons.xhtml", "provaProcessoSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	ATIVAR_PROVA_PROCESSO_SELETIVO("AtivarProvaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtivarProvaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_AtivarProvaProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.PROVA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	INATIVAR_PROVA_PROCESSO_SELETIVO("InativarProvaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InativarProvaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_InativarProvaProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.PROVA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	/**
	 * Questao Processo Seletivo
	 *
	 */
	QUESTAO_PROCESSO_SELETIVO("QuestaoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuestaoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_QuestaoProcessoSeletivo_ajuda"),
					new String[] { "questaoProcessoSeletivoCons.xhtml", "questaoProcessoSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	ATIVAR_QUESTAO_PROCESSO_SELETIVO("AtivarQuestaoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtivarQuestaoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_AtivarQuestaoProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.QUESTAO_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),

	INATIVAR_QUESTAO_PROCESSO_SELETIVO("InativarQuestaoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InativarQuestaoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_InativarQuestaoProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.QUESTAO_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	CANCELAR_QUESTAO_PROCESSO_SELETIVO("CancelarQuestaoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CancelarQuestaoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_CancelarQuestaoProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.QUESTAO_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO),
	/**
	 * Cartao Resposta Rel
	 *
	 */
	CARTAO_RESPOSTA_REL("CartaoRespostaRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_CartaoRespostaRel_titulo"),
			UteisJSF.internacionalizar("per_CartaoRespostaRel_ajuda"), new String[] { "cartaoRespostaRel.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Comprovante Inscricao
	 *
	 */
	COMPROVANTE_INSCRICAO("ComprovanteInscricao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ComprovanteInscricao_titulo"),
					UteisJSF.internacionalizar("per_ComprovanteInscricao_ajuda"),
					new String[] { "comprovanteInscricao.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Cartao Resposta
	 *
	 */
	CARTAO_RESPOSTA("CartaoResposta", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_CartaoResposta_titulo"),
			UteisJSF.internacionalizar("per_CartaoResposta_ajuda"), new String[] { "cartaoRespostaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Notificacao Processo Seletivo
	 *
	 */
	NOTIFICACAO_PROCESSO_SELETIVO("NotificacaoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NotificacaoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_NotificacaoProcessoSeletivo_ajuda"),
					new String[] { "notificacaoProcessoSeletivo.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Disciplinas Proc Seletivo
	 *
	 */
	DISCIPLINAS_PROC_SELETIVO("DisciplinasProcSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DisciplinasProcSeletivo_titulo"),
					UteisJSF.internacionalizar("per_DisciplinasProcSeletivo_ajuda"),
					new String[] { "disciplinasProcSeletivoCons.xhtml", "disciplinasProcSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Inscricao
	 *
	 */
	INSCRICAO("Inscricao", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Inscricao_titulo"), UteisJSF.internacionalizar("per_Inscricao_ajuda"),
			new String[] { "inscricaoCons.xhtml", "inscricaoForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	LIBERAR_INSCRICAO_FORA_PRAZO("LiberarInscricaoForaPrazo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarInscricaoForaPrazo_titulo"),
					UteisJSF.internacionalizar("per_LiberarInscricaoForaPrazo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoProcessoSeletivoEnum.INSCRICAO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	INSCRICAO_LIBERAR_PGTO("Inscricao_LiberarPgto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InscricaoLiberarPgto_titulo"),
					UteisJSF.internacionalizar("per_InscricaoLiberarPgto_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoProcessoSeletivoEnum.INSCRICAO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),

	INSCRICAO_CANCELAR("PermitirRealizarCancelamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InscricaoRealizarCancelamento_titulo"),
					UteisJSF.internacionalizar("per_InscricaoRealizarCancelamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoProcessoSeletivoEnum.INSCRICAO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	INSCRICAO_CANCELAR_DEVIDO_OUTRA_INSCRICAO("PermitirRealizarCancelamentoDevidoOutraInscricao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InscricaoRealizarCancelamentoDevidoOutraInscricao_titulo"),
					UteisJSF.internacionalizar("per_InscricaoRealizarCancelamentoDevidoOutraInscricao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoProcessoSeletivoEnum.INSCRICAO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),

	/**
	 * Proc Seletivo
	 *
	 */
	PROC_SELETIVO("ProcSeletivo", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ProcSeletivo_titulo"), UteisJSF.internacionalizar("per_ProcSeletivo_ajuda"),
			new String[] { "procSeletivoCons.xhtml", "procSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	PERMITIR_ALTERAR_DATA_PROVA_PROCESSO_SELETIVO("PermitirAlterarDataProvaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarDataProvaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarDataProvaProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoProcessoSeletivoEnum.PROC_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),

	/**
	 * Resultado Processo Seletivo
	 *
	 */
	RESULTADO_PROCESSO_SELETIVO("ResultadoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ResultadoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_ResultadoProcessoSeletivo_ajuda"),
					new String[] { "resultadoProcessoSeletivoCons.xhtml", "resultadoProcessoSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),

	/**
	 * Candidato
	 *
	 */
	CANDIDATO("Candidato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Candidato_titulo"), UteisJSF.internacionalizar("per_Candidato_ajuda"),
			new String[] { "candidatoCons.xhtml", "candidatoForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Distribuir Sala Processo Seletivo
	 *
	 */
	DISTRIBUIR_SALA_PROCESSO_SELETIVO("DistribuirSalaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DistribuirSalaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_DistribuirSalaProcessoSeletivo_ajuda"),
					new String[] { "distribuirSalaProcessoSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Perfil Socio Economico
	 *
	 */
	PERFIL_SOCIO_ECONOMICO("PerfilSocioEconomico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PerfilSocioEconomico_titulo"),
					UteisJSF.internacionalizar("per_PerfilSocioEconomico_ajuda"),
					new String[] { "perfilSocioEconomicoCons.xhtml", "perfilSocioEconomicoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Processar Resultado Processo Seletivo
	 *
	 */
	PROCESSAR_RESULTADO_PROCESSO_SELETIVO("ProcessarResultadoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessarResultadoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_ProcessarResultadoProcessoSeletivo_ajuda"),
					new String[] { "processarResultadoProcessoSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Grupo Disciplina Proc Seletivo
	 *
	 */
	GRUPO_DISCIPLINA_PROC_SELETIVO("GrupoDisciplinaProcSeletivo", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_GrupoDisciplinaProcSeletivo_titulo"),
			UteisJSF.internacionalizar("per_GrupoDisciplinaProcSeletivo_ajuda"),
			new String[] { "grupoDisciplinaProcSeletivoCons.xhtml", "grupoDisciplinaProcSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	/**
	 * Proc Seletivo Rel
	 *
	 */
	PROC_SELETIVO_REL("ProcSeletivoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ProcSeletivoRel_titulo"),
			UteisJSF.internacionalizar("per_ProcSeletivoRel_ajuda"), new String[] { "procSeletivoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Proc Seletivo Provas Rel
	 *
	 */
	PROC_SELETIVO_PROVAS_REL("ProcSeletivoProvasRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcSeletivoProvasRel_titulo"),
					UteisJSF.internacionalizar("per_ProcSeletivoProvasRel_ajuda"),
					new String[] { "procSeletivoProvasRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Alunos Processo Seletivo Rel
	 *
	 */
	ALUNOS_PROCESSO_SELETIVO_REL("AlunosProcessoSeletivoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosProcessoSeletivoRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosProcessoSeletivoRel_ajuda"),
					new String[] { "alunosProcessoSeletivoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Proc Seletivo Inscricoes Rel
	 *
	 */
	PROC_SELETIVO_INSCRICOES_REL("ProcSeletivoInscricoesRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcSeletivoInscricoesRel_titulo"),
					UteisJSF.internacionalizar("per_ProcSeletivoInscricoesRel_ajuda"),
					new String[] { "procSeletivoInscricoesRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Estatistica Processo Seletivo
	 *
	 */
	ESTATISTICA_PROCESSO_SELETIVO("EstatisticaProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivo_ajuda"),
					new String[] { "estatisticaProcessoSeletivoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_INSCRITO_BAIRRO("EstatisticaProcessoSeletivoRelInscritoBairro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelInscritoBairro_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelInscritoBairro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),

	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_REPROVADO("EstatisticaProcessoSeletivoRelListaReprovado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaReprovado_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaReprovado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_CLASSIFICADO("EstatisticaProcessoSeletivoRelListaClassificado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaClassificado_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaClassificado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_AUSENTE("EstatisticaProcessoSeletivoRelListaAusente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaAusente_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaAusente_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_MURAL_CANDIDATO("EstatisticaProcessoSeletivoRelListaMuralCandidato",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaMuralCandidato_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaMuralCandidato_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_AUSENTE_PRESENTE_POR_CURSO_TURNO(
			"EstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_EstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno_titulo"),
					UteisJSF.internacionalizar(
							"per_EstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_APROVADO("EstatisticaProcessoSeletivoRelListaAprovado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaAprovado_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaAprovado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_INSCRITO_CURSO("EstatisticaProcessoSeletivoRelInscritoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelInscritoCurso_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelInscritoCurso_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_FREQUENCIA("EstatisticaProcessoSeletivoRelListaFrequencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaFrequencia_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaFrequencia_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_DADOS_CANDIDATO("EstatisticaProcessoSeletivoRelDadosCandidato",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelDadosCandidato_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelDadosCandidato_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_NAO_MATRICULADO("EstatisticaProcessoSeletivoRelListaNaoMatriculado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaNaoMatriculado_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaNaoMatriculado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	ESTATISTICA_PROCESSO_SELETIVO_REL_LISTA_MATRICULADO("EstatisticaProcessoSeletivoRelListaMatriculado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaMatriculado_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaProcessoSeletivoRelListaMatriculado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoProcessoSeletivoEnum.ESTATISTICA_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),

	RELATORIO_SEIDECIDIR_PROCESSO_SELETIVO("RelatorioSEIDecidirProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirProcessoSeletivo_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_PROCESSO_SELETIVO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_PROCESSO_SELETIVO,
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Resultado Processo Seletivo Rel
	 *
	 */
	RESULTADO_PROCESSO_SELETIVO_REL("ResultadoProcessoSeletivoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ResultadoProcessoSeletivoRel_titulo"),
					UteisJSF.internacionalizar("per_ResultadoProcessoSeletivoRel_ajuda"),
					new String[] { "resultadoProcessoSeletivoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Resultado Questionario Processo Seletivo Rel
	 *
	 */
	RESULTADO_QUESTIONARIO_PROCESSO_SELETIVO_REL("ResultadoQuestionarioProcessoSeletivoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ResultadoQuestionarioProcessoSeletivoRel_titulo"),
					UteisJSF.internacionalizar("per_ResultadoQuestionarioProcessoSeletivoRel_ajuda"),
					new String[] { "resultadoQuestionarioProcessoSeletivoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Chamada Candidato Aprovado Rel
	 *
	 */
	CHAMADA_CANDIDATO_APROVADO_REL("ChamadaCandidatoAprovadoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ChamadaCandidatoAprovadoRel_titulo"),
					UteisJSF.internacionalizar("per_ChamadaCandidatoAprovadoRel_ajuda"),
					new String[] { "chamadaCandidatoAprovadoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Declaracao Aprovacao Vest Rel
	 *
	 */
	DECLARACAO_APROVACAO_VEST_REL("DeclaracaoAprovacaoVestRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoAprovacaoVestRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoAprovacaoVestRel_ajuda"),
					new String[] { "declaracaoAprovacaoVestRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Casos Especiais Rel
	 *
	 */
	CASOS_ESPECIAIS_REL("CasosEspeciaisRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_CasosEspeciaisRel_titulo"),
			UteisJSF.internacionalizar("per_CasosEspeciaisRel_ajuda"), new String[] { "casosEspeciaisRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Processo Seletivo Aprovado Reprovado Rel
	 *
	 */
	PROCESSO_SELETIVO_REDACAO_REL("ProcessoSeletivoRedacaoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessoSeletivoRedacaoRel_titulo"),
					UteisJSF.internacionalizar("per_ProcessoSeletivoRedacaoRel_ajuda"),
					new String[] { "processoSeletivoRedacaoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Processo Seletivo Aprovado Reprovado Rel
	 *
	 */
	PROCESSO_SELETIVO_APROVADO_REPROVADO_REL("ProcessoSeletivoAprovadoReprovadoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessoSeletivoAprovadoReprovadoRel_titulo"),
					UteisJSF.internacionalizar("per_ProcessoSeletivoAprovadoReprovadoRel_ajuda"),
					new String[] { "processoSeletivoAprovadoReprovadoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Alunos Matriculados Por Processo Seletivo Rel
	 *
	 */
	ALUNOS_MATRICULADOS_POR_PROCESSO_SELETIVO_REL("AlunosMatriculadosPorProcessoSeletivoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosMatriculadosPorProcessoSeletivoRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosMatriculadosPorProcessoSeletivoRel_ajuda"),
					new String[] { "alunosMatriculadosPorProcessoSeletivoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),
	/**
	 * Processo Seletivo Ocorrencia Rel
	 *
	 */
	PROCESSO_SELETIVO_OCORRENCIA_REL("ProcessoSeletivoOcorrenciaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessoSeletivoOcorrenciaRel_titulo"),
					UteisJSF.internacionalizar("per_ProcessoSeletivoOcorrenciaRel_ajuda"),
					new String[] { "processoSeletivoOcorrenciaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO_RELATORIO),

	/**
	 * Impressão Texto Padrão Processo Seletivo
	 *
	 */
	IMPRESSAO_TEXTO_PADRAO_PROCESSO_SELETIVO("ImpressaoTextoPadraoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ImpressaoTextoPadraoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_ImpressaoTextoPadraoProcessoSeletivo_ajuda"),
					new String[] { "impressaoTextoPadraoProcessoSeletivo.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),

	
	IMPORTAR_CANDIDATO_INSCRICAO_PROCESSO_SELETIVO("ImportarCandidatoInscricaoProcessoSeletivo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ImportarCandidatoInscricaoProcessoSeletivo_titulo"),UteisJSF.internacionalizar("per_ImportarCandidatoInscricaoProcessoSeletivo_ajuda"), new String[]{"importarCandidatoInscricaoProcessoSeletivoCons.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO),
	
	/**
	 * Texto Padrão Processo Seletivo
	 *
	 */
	TEXTO_PADRAO_PROCESSO_SELETIVO("TextoPadraoProcessoSeletivo", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_TextoPadraoProcessoSeletivo_titulo"),
			UteisJSF.internacionalizar("per_TextoPadraoProcessoSeletivo_ajuda"),
			new String[] { "textoPadraoProcessoSeletivoCons.xhtml", "textoPadraoProcessoSeletivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PROCESSO_SELETIVO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoProcessoSeletivoEnum(String valor, PermissaoVisao[] permissaoVisao,
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
