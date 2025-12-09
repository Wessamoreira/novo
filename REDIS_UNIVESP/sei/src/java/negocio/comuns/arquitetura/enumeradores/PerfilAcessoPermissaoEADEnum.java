package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoEADEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Questao Atividade Discursiva
	 *
	 */
	QUESTAO_ATIVIDADE_DISCURSIVA("QuestaoAtividadeDiscursiva",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_QuestaoAtividadeDiscursiva_titulo"),
							UteisJSF.internacionalizar("per_QuestaoAtividadeDiscursiva_ajuda"),
							new String[] { "questaoCons.xhtml", "questaoForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_QuestaoAtividadeDiscursiva_titulo"),
							UteisJSF.internacionalizar("per_QuestaoAtividadeDiscursiva_ajuda"),
							new String[] { "questaoProfessorCons.xhtml", "questaoProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	INATIVAR_ATIVIDADE_DISCURSIVA("InativarAtividadeDiscursiva",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_InativarAtividadeDiscursiva_titulo"),
							UteisJSF.internacionalizar("per_InativarAtividadeDiscursiva_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_InativarAtividadeDiscursiva_titulo"),
							UteisJSF.internacionalizar("per_InativarAtividadeDiscursiva_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ATIVIDADE_DISCURSIVA,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	ALTERAR_ATIVIDADE_DISCURSIVA_OUTRO_PROFESSOR("AlterarAtividadeDiscursivaOutroProfessor",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_AlterarAtividadeDiscursivaOutroProfessor_titulo"),
							UteisJSF.internacionalizar("per_AlterarAtividadeDiscursivaOutroProfessor_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_AlterarAtividadeDiscursivaOutroProfessor_titulo"),
							UteisJSF.internacionalizar("per_AlterarAtividadeDiscursivaOutroProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ATIVIDADE_DISCURSIVA,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	PERMITEALTERARQUESTOESATIVIDADEDISCURSIVAATIVAS("PermiteAlterarQuestoesAtividadeDiscursivaAtivas",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesAtividadeDiscursivaAtivas_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesAtividadeDiscursivaAtivas_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesAtividadeDiscursivaAtivas_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesAtividadeDiscursivaAtivas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ATIVIDADE_DISCURSIVA,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	/**
	 * Questao Online
	 *
	 */
	QUESTAO_ONLINE("QuestaoOnline", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_QuestaoOnline_titulo"),
					UteisJSF.internacionalizar("per_QuestaoOnline_ajuda"),
					new String[] { "questaoCons.xhtml", "questaoForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_QuestaoOnline_titulo"),
					UteisJSF.internacionalizar("per_QuestaoOnline_ajuda"),
					new String[] { "questaoProfessorCons.xhtml", "questaoProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	ALTERAR_QUESTAO_ONLINE_OUTRO_PROFESSOR("AlterarQuestaoOnlineOutroProfessor",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_AlterarQuestaoOnlineOutroProfessor_titulo"),
							UteisJSF.internacionalizar("per_AlterarQuestaoOnlineOutroProfessor_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_AlterarQuestaoOnlineOutroProfessor_titulo"),
							UteisJSF.internacionalizar("per_AlterarQuestaoOnlineOutroProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	INATIVAR_QUESTAO_ONLINE("InativarQuestaoOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InativarQuestaoOnline_titulo"),
					UteisJSF.internacionalizar("per_InativarQuestaoOnline_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_InativarQuestaoOnline_titulo"),
							UteisJSF.internacionalizar("per_InativarQuestaoOnline_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),

	ANULAR_QUESTAO_ONLINE("AnularQuestaoOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AnularQuestaoOnline_titulo"),
					UteisJSF.internacionalizar("per_AnularQuestaoOnline_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_AnularQuestaoOnline_titulo"),
							UteisJSF.internacionalizar("per_AnularQuestaoOnline_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	PERMITEALTERARQUESTOESONLINEATIVAS("PermiteAlterarQuestoesOnlineAtivas",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesOnlineAtivas_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesOnlineAtivas_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesOnlineAtivas_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarQuestoesOnlineAtivas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	/**
	 * Questao Exercicio
	 *
	 */
	QUESTAO_EXERCICIO("QuestaoExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuestaoExercicio_titulo"),
					UteisJSF.internacionalizar("per_QuestaoExercicio_ajuda"),
					new String[] { "questaoCons.xhtml", "questaoForm.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	INATIVAR_EXERCICIO("InativarExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InativarExercicio_titulo"),
					UteisJSF.internacionalizar("per_InativarExercicio_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	ALTERAR_EXERCICIO_OUTRO_PROFESSOR("AlterarExercicioOutroProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarExercicioOutroProfessor_titulo"),
					UteisJSF.internacionalizar("per_AlterarExercicioOutroProfessor_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	PERMITEALTERARQUESTOESEXERCICIOATIVAS("PermiteAlterarQuestoesExercicioAtivas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarQuestoesExercicioAtivas_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarQuestoesExercicioAtivas_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	/**
	 * Lista Exercicio
	 *
	 */
	LISTA_EXERCICIO("ListaExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ListaExercicio_titulo"),
					UteisJSF.internacionalizar("per_ListaExercicio_ajuda"),
					new String[] { "questaoCons.xhtml", "questaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	INATIVAR_LISTA_EXERCICIO("InativarListaExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InativarListaExercicio_titulo"),
					UteisJSF.internacionalizar("per_InativarListaExercicio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.LISTA_EXERCICIO,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	ALTERAR_LISTA_EXERCICIO_OUTRO_PROFESSOR("AlterarListaExercicioOutroProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarListaExercicioOutroProfessor_titulo"),
					UteisJSF.internacionalizar("per_AlterarListaExercicioOutroProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.LISTA_EXERCICIO,
			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	/**
	 * Questao Presencial Este recurso foi comentado pois não mais a necessidade por
	 * enquando do mesmo no sistema
	 */

//	QUESTAO_PRESENCIAL("QuestaoPresencial", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_QuestaoPresencial_titulo"),UteisJSF.internacionalizar("per_QuestaoPresencial_ajuda"), new String[]{"questaoCons.xhtml","questaoForm.xhtml"}),
//			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_QuestaoPresencial_titulo"),UteisJSF.internacionalizar("per_QuestaoPresencial_ajuda"), new String[]{"questaoProfessorCons.xhtml","questaoProfessorForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
//	ALTERAR_QUESTAO_PRESENCIAL_OUTRO_PROFESSOR("AlterarQuestaoPresencialOutroProfessor", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AlterarQuestaoPresencialOutroProfessor_titulo"),UteisJSF.internacionalizar("per_AlterarQuestaoPresencialOutroProfessor_ajuda")),
//			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AlterarQuestaoPresencialOutroProfessor_titulo"),UteisJSF.internacionalizar("per_AlterarQuestaoPresencialOutroProfessor_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//			PerfilAcessoPermissaoEADEnum.QUESTAO_PRESENCIAL, 
//			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
//	CANCELAR_QUESTAO_PRESENCIAL("CancelarQuestaoPresencial", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CancelarQuestaoPresencial_titulo"),UteisJSF.internacionalizar("per_CancelarQuestaoPresencial_ajuda")),
//			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_CancelarQuestaoPresencial_titulo"),UteisJSF.internacionalizar("per_CancelarQuestaoPresencial_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//			PerfilAcessoPermissaoEADEnum.QUESTAO_PRESENCIAL, 
//			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
//	INATIVAR_QUESTAO_PRESENCIAL("InativarQuestaoPresencial", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_InativarQuestaoPresencial_titulo"),UteisJSF.internacionalizar("per_InativarQuestaoPresencial_ajuda")),
//			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_InativarQuestaoPresencial_titulo"),UteisJSF.internacionalizar("per_InativarQuestaoPresencial_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//			PerfilAcessoPermissaoEADEnum.QUESTAO_PRESENCIAL, 
//			PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
//    PERMITEALTERARQUESTOESPRESENCIALATIVAS("PermiteAlterarQuestoesPresencialAtivas", new PermissaoVisao[] {
//					 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarQuestoesPresencialAtivas_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarQuestoesPresencialAtivas_ajuda")),
//					 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermiteAlterarQuestoesPresencialAtivas_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarQuestoesPresencialAtivas_ajuda"))
//					},
//					TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//					PerfilAcessoPermissaoEADEnum.QUESTAO_PRESENCIAL, 
//					PerfilAcessoSubModuloEnum.EAD_BANCO_QUESTOES),
	/**
	 * Politica Divulgacao Matricula Online
	 *
	 */
	POLITICA_DIVULGACAO_MATRICULA_ONLINE("PoliticaDivulgacaoMatriculaOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PoliticaDivulgacaoMatriculaOnline_titulo"),
					UteisJSF.internacionalizar("per_PoliticaDivulgacaoMatriculaOnline_ajuda"),
					new String[] { "politicaDivulgacaoMatriculaOnlineCons.xhtml",
							"politicaDivulgacaoMatriculaOnlineForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Conteudo
	 *
	 */
	CONTEUDO("Conteudo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Conteudo_titulo"),
							UteisJSF.internacionalizar("per_Conteudo_ajuda"),
							new String[] { "conteudoCons.xhtml", "conteudoForm.xhtml", "unidadeConteudoForm.xhtml",
									"conteudoAlunoForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Conteudo_titulo"),
							UteisJSF.internacionalizar("per_Conteudo_ajuda"),
							new String[] { "conteudoProfessorCons.xhtml", "conteudoForm.xhtml",
									"unidadeConteudoForm.xhtml", "conteudoAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),

	PERMITIR_PROFESSOR_CADASTRAR_CONTEUDO_APENAS_AULAS_PROGRAMADAS(
			"PermitirProfessorCadastrarConteudoApenasAulasProgramadas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarConteudoApenasAulasProgramadas_titulo"),
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarConteudoApenasAulasProgramadas_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_PermitirProfessorCadastrarConteudoApenasAulasProgramadas_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirProfessorCadastrarConteudoApenasAulasProgramadas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, CONTEUDO, PerfilAcessoSubModuloEnum.EAD_EAD),

	ATIVAR_INATIVAR_CONTEUDO("AtivarInativarConteudo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AtivarInativarConteudo_titulo"),
					UteisJSF.internacionalizar("per_AtivarInativarConteudo_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtivarInativarConteudo_titulo"),
					UteisJSF.internacionalizar("per_AtivarInativarConteudo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, CONTEUDO, PerfilAcessoSubModuloEnum.EAD_EAD),

	PERMITIR_PROFESSOR_CADASTRAR_APENAS_CONTEUDOS_EXCLUSIVOS("PermitirProfessorCadastrarApenasConteudosExclusivos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarApenasConteudosExclusivos_titulo"),
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarApenasConteudosExclusivos_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_PermitirProfessorCadastrarApenasConteudosExclusivos_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirProfessorCadastrarApenasConteudosExclusivos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, CONTEUDO, PerfilAcessoSubModuloEnum.EAD_EAD),

	PERMITIR_PROFESSOR_CADASTRAR_CONTEUDO_QUALQUER_DISCIPLINA("PermitirProfessorCadastrarConteudoQualquerDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarConteudoQualquerDisciplina_titulo"),
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarConteudoQualquerDisciplina_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_PermitirProfessorCadastrarConteudoQualquerDisciplina_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirProfessorCadastrarConteudoQualquerDisciplina_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, CONTEUDO, PerfilAcessoSubModuloEnum.EAD_EAD),

	PERMITIR_PROFESSOR_REABRIR_AVALIACAO_PBL("PermitirProfessorReabrirAvaliacaoPBL",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAvaliacaoPBL_titulo"),
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAvaliacaoPBL_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAvaliacaoPBL_titulo"),
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAvaliacaoPBL_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, CONTEUDO, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_PROFESSOR_REABRIR_ATA_PBL("PermitirProfessorReabrirAtaPBL",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAtaPBL_titulo"),
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAtaPBL_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAtaPBL_titulo"),
							UteisJSF.internacionalizar("per_PermitirProfessorReabrirAtaPBL_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, CONTEUDO, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	/**
	 * Monitoramento Alunos EAD
	 *
	 */
	MONITORAMENTO_ALUNOS_EAD("MonitoramentoAlunosEAD",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MonitoramentoAlunosEAD_titulo"),
					UteisJSF.internacionalizar("per_MonitoramentoAlunosEAD_ajuda"),
					new String[] { "monitoramentoAlunosEADCons.xhtml", "monitoramentoAlunosEADForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Parametros Monitoramento Avaliacao Online
	 *
	 */
	PARAMETROS_MONITORAMENTO_AVALIACAO_ONLINE("ParametrosMonitoramentoAvaliacaoOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ParametrosMonitoramentoAvaliacaoOnline_titulo"),
					UteisJSF.internacionalizar("per_ParametrosMonitoramentoAvaliacaoOnline_ajuda"),
					new String[] { "parametrosMonitoramentoAvaliacaoOnlineCons.xhtml",
							"parametrosMonitoramentoAvaliacaoOnlineForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Anotacao Disciplina
	 *
	 */
	ANOTACAO_DISCIPLINA("AnotacaoDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AnotacaoDisciplina_titulo"),
					UteisJSF.internacionalizar("per_AnotacaoDisciplina_ajuda"),
					new String[] { "anotacaoDisciplinaCons.xhtml", "anotacaoDisciplinaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Avaliacao Online
	 *
	 */
	AVALIACAO_ONLINE("AvaliacaoOnline", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AvaliacaoOnline_titulo"),
					UteisJSF.internacionalizar("per_AvaliacaoOnline_ajuda"),
					new String[] { "avaliacaoOnlineCons.xhtml", "avaliacaoOnlineForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AvaliacaoOnline_titulo"),
					UteisJSF.internacionalizar("per_AvaliacaoOnline_ajuda"),
					new String[] { "avaliacaoOnlineCons.xhtml", "avaliacaoOnlineForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	INICIAR_ATIVO_RECURSO_RANDOMIZAR_QUESTOES_CADASTRADAS_PROFESSOR(
			"IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor_titulo"),
					UteisJSF.internacionalizar("per_IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor_titulo"),
							UteisJSF.internacionalizar(
									"per_IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.AVALIACAO_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_EAD),
	DESABILITAR_ALTERACAO_RECURSO_RANDOMIZAR_QUESTOES_CADASTRADAS_PROFESSOR(
			"DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor_titulo"),
							UteisJSF.internacionalizar(
									"per_DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor_titulo"),
							UteisJSF.internacionalizar(
									"per_DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.AVALIACAO_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Gestao Avaliacao Online
	 *
	 */
	GESTAO_AVALIACAO_ONLINE("GestaoAvaliacaoOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GestaoAvaliacaoOnline_titulo"),
					UteisJSF.internacionalizar("per_GestaoAvaliacaoOnline_ajuda"),
					new String[] { "gestaoAvaliacaoOnlineCons.xhtml", "gestaoAvaliacaoOnlineForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_GestaoAvaliacaoOnline_titulo"),
							UteisJSF.internacionalizar("per_GestaoAvaliacaoOnline_ajuda"),
							new String[] { "gestaoAvaliacaoOnlineProfessorCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Configuracao Conteudo Turma
	 *
	 */
	CONFIGURACAO_CONTEUDO_TURMA("ConfiguracaoConteudoTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoConteudoTurma_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoConteudoTurma_ajuda"),
					new String[] { "configuracaoConteudoTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	/**
	 * Calendario Atividade Matricula
	 *
	 */
	CALENDARIO_ATIVIDADE_MATRICULA("CalendarioAtividadeMatricula", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CalendarioAtividadeMatricula_titulo"),
			UteisJSF.internacionalizar("per_CalendarioAtividadeMatricula_ajuda"),
			new String[] { "calendarioAtividadeMatriculaCons.xhtml", "calendarioAtividadeMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),

	/**
	 * Programacao Tutoria Online
	 *
	 */
	PROGRAMACAO_TUTORIA_ONLINE("ProgramacaoTutoriaOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProgramacaoTutoriaOnline_titulo"),
					UteisJSF.internacionalizar("per_ProgramacaoTutoriaOnline_ajuda"),
					new String[] { "programacaoTutoriaOnlineCons.xhtml", "programacaoTutoriaOnlineForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_EAD),
	PERMITE_PROGRAMACAO_TUTORIA_ONLINE_COM_CLASSROOM("PermiteProgramacaoTutoriaOnlineComClassroom",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteProgramacaoTutoriaOnlineComClassroom_titulo"),
					UteisJSF.internacionalizar("per_PermiteProgramacaoTutoriaOnlineComClassroom_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.PROGRAMACAO_TUTORIA_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_EAD),
	PERMITE_PROGRAMACAO_TUTORIA_ONLINE_COM_BLACKBOARD("PermiteProgramacaoTutoriaOnlineComBlackboard",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteProgramacaoTutoriaOnlineComBlackboard_titulo"),
					UteisJSF.internacionalizar("per_PermiteProgramacaoTutoriaOnlineComBlackboard_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.PROGRAMACAO_TUTORIA_ONLINE,
			PerfilAcessoSubModuloEnum.EAD_EAD),

	/**
	 * Configuracao EAD
	 *
	 */
	CONFIGURACAO_EAD("ConfiguracaoEAD",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoEAD_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoEAD_ajuda"),
					new String[] { "configuracaoEADCons.xhtml", "configuracaoEADForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.EAD_CONFIGURACOES),
	
	/**
	 * Relatorio SEI Decidir EAD
	 *
	 */
	RELATORIO_SEIDECIDIR_EAD("RelatorioSEIDecidirEAD",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirEAD_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirEAD_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.EAD_RELATORIO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_EAD_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirEADApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirEADApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirEADApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_EAD,
			PerfilAcessoSubModuloEnum.EAD_RELATORIO),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_EAD("PermitirVisualizarScriptSqlRelatorioSeiDecidirEAD",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_EAD,
			PerfilAcessoSubModuloEnum.EAD_RELATORIO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoEADEnum(String valor, PermissaoVisao[] permissaoVisao,
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
