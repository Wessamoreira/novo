package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoVisaoProfessorEnum implements PerfilAcessoPermissaoEnumInterface {

//	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_SEM_INFORMAR_CONTEUDO_PRESENCIAL("PermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial_titulo"),UteisJSF.internacionalizar("per_PermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//			PerfilAcessoPermissaoEADEnum.QUESTAO_PRESENCIAL, 
//			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
//	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_PARA_QUALQUER_DISCIPLINA_PRESENCIAL("PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial_titulo"),UteisJSF.internacionalizar("per_PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaPresencial_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//			PerfilAcessoPermissaoEADEnum.QUESTAO_PRESENCIAL, 
//			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_SEM_INFORMAR_CONTEUDO_EXERCICIO(
			"PermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Professor
	 *
	 */
	PROFESSOR("Professor", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
			UteisJSF.internacionalizar("per_Professor_titulo"), UteisJSF.internacionalizar("per_Professor_ajuda"),
			new String[] { "dadosPessoaisProfessor.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	ALTERAR_FORMACAO_ACADEMICA("AlterarFormacaoAcademica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_AlterarFormacaoAcademica_titulo"),
					UteisJSF.internacionalizar("per_AlterarFormacaoAcademica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Questao Exercicio
	 *
	 */
	QUESTAO_EXERCICIO("QuestaoExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_QuestaoExercicio_titulo"),
					UteisJSF.internacionalizar("per_QuestaoExercicio_ajuda"),
					new String[] { "questaoProfessorCons.xhtml", "questaoProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	INATIVAR_EXERCICIO("InativarExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_InativarExercicio_titulo"),
					UteisJSF.internacionalizar("per_InativarExercicio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	ALTERAR_EXERCICIO_OUTRO_PROFESSOR("AlterarExercicioOutroProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_AlterarExercicioOutroProfessor_titulo"),
					UteisJSF.internacionalizar("per_AlterarExercicioOutroProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITEALTERARQUESTOESEXERCICIOATIVAS("PermiteAlterarQuestoesExercicioAtivas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermiteAlterarQuestoesExercicioAtivas_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarQuestoesExercicioAtivas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_SEM_INFORMAR_CONTEUDO_QUESTAO_EXERCICIO(
			"PermitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarQuestaoSemInformarConteudoOnline_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoSemInformarConteudoOnline_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_EM_DISCIPLINAS_ANOS_ANTERIORES_EXERCICIO(
			"PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnterioresExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnterioresExercicio_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoSemInformarConteudoExercicio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_PARA_QUALQUER_DISCIPLINA_EXERCICIO(
			"PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaExercicio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.QUESTAO_EXERCICIO,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Lista Exercicio Professor
	 *
	 */
	LISTA_EXERCICIO_PROFESSOR("ListaExercicioProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_ListaExercicioProfessor_titulo"),
					UteisJSF.internacionalizar("per_ListaExercicioProfessor_ajuda"),
					new String[] { "listaExercicioProfessorCons.xhtml", "listaExercicioProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	INATIVAR_LISTA_EXERCICIO_PROFESSOR("InativarListaExercicioProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_InativarListaExercicioProfessor_titulo"),
					UteisJSF.internacionalizar("per_InativarListaExercicioProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoProfessorEnum.LISTA_EXERCICIO_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	ALTERAR_LISTA_EXERCICIO_OUTRO_PROFESSOR_PROFESSOR("AlterarListaExercicioOutroProfessorProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_AlterarListaExercicioOutroProfessorProfessor_titulo"),
					UteisJSF.internacionalizar("per_AlterarListaExercicioOutroProfessorProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoProfessorEnum.LISTA_EXERCICIO_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Criterio Avaliacao Aluno Visao Professor
	 *
	 */
	CRITERIO_AVALIACAO_ALUNO_VISAO_PROFESSOR("CriterioAvaliacaoAlunoVisaoProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoProfessor_titulo"),
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoProfessor_ajuda"),
					new String[] { "criterioAvaliacaoVisaoProfessorCons.xhtml",
							"criterioAvaliacaoVisaoProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_ALTERACOES_CRITERIOS_AVALIACOES_GERAIS("PermitirAlteracoesCriterioAvaliacoesGerais",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirAlteracoesCriterioAvaliacoesGerais_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlteracoesCriterioAvaliacoesGerais_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoProfessorEnum.CRITERIO_AVALIACAO_ALUNO_VISAO_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Reserva Patrimonio Visao Professor
	 *
	 */
	RESERVA_PATRIMONIO_VISAO_PROFESSOR("OcorrenciaPatrimonioVisaoProfessor", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ReservaPatrimonioVisaoProfessor_titulo"),
			UteisJSF.internacionalizar("per_ReservaPatrimonioVisaoProfessor_ajuda"),
			new String[] { "ocorrenciaPatrimonioProfessorCons.xhtml", "ocorrenciaPatrimonioProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_SEM_INFORMAR_CONTEUDO_ONLINE(
			"PermitirProfessorCadastrarQuestaoSemInformarConteudoOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirProfessorCadastrarQuestaoSemInformarConteudoOnline_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoSemInformarConteudoOnline_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_PARA_QUALQUER_DISCIPLINA_ONLINE(
			"PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoParaQualquerDisciplinaOnline_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITIR_PROFESSOR_CADASTRAR_QUESTAO_EM_DISCIPLINAS_ANOS_ANTERIORES(
			"PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnteriores",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnteriores_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirProfessorCadastrarQuestaoEmDisciplinasAnosAnteriores_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEADEnum.QUESTAO_ONLINE,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	/**
	 * Avaliacao Online Professor
	 *
	 */

	ATIVIDADE_DISCURSIVA_PROFESSOR("AtividadeDiscursivaProfessor", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AtividadeDiscursivaProfessor_titulo"),
			UteisJSF.internacionalizar("per_AtividadeDiscursivaProfessor_ajuda"),
			new String[] { "atividadeDiscursivaProfessorCons.xhtml", "atividadeDiscursivaProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	VINCULAR_NOTA_ESPECIFICA("VincularNotaEspecifica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_VincularNotaEspecifica_titulo"),
					UteisJSF.internacionalizar("per_VincularNotaEspecifica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoProfessorEnum.ATIVIDADE_DISCURSIVA_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Duvida Professor
	 *
	 */
	DUVIDA_PROFESSOR("DuvidaProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_DuvidaProfessor_titulo"),
					UteisJSF.internacionalizar("per_DuvidaProfessor_ajuda"),
					new String[] { "duvidaProfessorCons.xhtml", "duvidaProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	/**
	 * Configuracao Conteudo Turma Visao Professor
	 *
	 */
	CONFIGURACAO_CONTEUDO_TURMA_VISAO_PROFESSOR("ConfiguracaoConteudoTurmaVisaoProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_ConfiguracaoConteudoTurmaVisaoProfessor_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoConteudoTurmaVisaoProfessor_ajuda"),
					new String[] { "configuracaoConteudoTurmaProfessorCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	/**
	 * Monitoramento Alunos EAD Visao Professor
	 *
	 */
	MONITORAMENTO_ALUNOS_EADVISAO_PROFESSOR("MonitoramentoAlunosEADVisaoProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_MonitoramentoAlunosEADVisaoProfessor_titulo"),
					UteisJSF.internacionalizar("per_MonitoramentoAlunosEADVisaoProfessor_ajuda"),
					new String[] { "monitoramentoAlunosEADVisaoProfessorCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Conteudo Visão Professor
	 *
	 *//*
		 * CONTEUDO_VISAO_PROFESSOR("ConteudoVisaoProfessor", new PermissaoVisao[] { new
		 * PermissaoVisao(TipoVisaoEnum.PROFESSOR,
		 * UteisJSF.internacionalizar("per_ConteudoVisaoProfessor_titulo"),UteisJSF.
		 * internacionalizar("per_ConteudoVisaoProfessor_ajuda"), new
		 * String[]{"conteudoProfessorCons.xhtml"}) },
		 * TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
		 * PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
		 */

	/**
	 * Gestao Evento Conteudo Turma Visao Professor
	 *
	 */
	GESTAO_EVENTO_CONTEUDO_TURMA_VISAO_PROFESSOR("GestaoEventoConteudoTurmaVisaoProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_GestaoEventoConteudoTurmaVisaoProfessor_titulo"),
					UteisJSF.internacionalizar("per_GestaoEventoConteudoTurmaVisaoProfessor_ajuda"),
					new String[] { "gestaoEventoConteudoCons.xhtml", "gestaoEventoConteudoAcessosCons.xhtml",
							"gestaoEventoConteudoAcessosAtaCons.xhtml", "conteudoAlunoForm.xhtml",
							"gestaoEventoConteudoAvaliacaoNotaFinalCons.xhtml",
							"monitoramentoAlunosRegistroAcessoCons.xhtml", "monitoramentoAlunosPBLCons.xhtml",
							"monitoramentoAlunosEADVisaoProfessorCons.xhtml", "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITE_ALTERAR_NOTA_ALUNO_AVALIACAO_PBL("PermiteAlterarNotaAlunoAvaliacaoPBL",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermiteAlterarNotaAlunoAvaliacaoPBL_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarNotaAlunoAvaliacaoPBL_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoProfessorEnum.GESTAO_EVENTO_CONTEUDO_TURMA_VISAO_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Upload
	 *
	 */
	PERMITIR_USUARIO_REALIZAR_UPLOAD("PermitirUsuarioRealizarUpload",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirUsuarioRealizarUpload_titulo"),
							UteisJSF.internacionalizar("per_PermitirUsuarioRealizarUpload_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirUsuarioRealizarUpload_titulo"),
							UteisJSF.internacionalizar("per_PermitirUsuarioRealizarUpload_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirUsuarioRealizarUpload_titulo"),
							UteisJSF.internacionalizar("per_PermitirUsuarioRealizarUpload_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.UPLOAD,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	POSTAR_MATERIAL_COM_TURMA_OBRIGATORIAMENTE_INFORMADO("PostarMaterialComTurmaObrigatoriamenteInformado",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PostarMaterialComTurmaObrigatoriamenteInformado_titulo"),
							UteisJSF.internacionalizar("per_PostarMaterialComTurmaObrigatoriamenteInformado_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PostarMaterialComTurmaObrigatoriamenteInformado_titulo"),
							UteisJSF.internacionalizar("per_PostarMaterialComTurmaObrigatoriamenteInformado_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PostarMaterialComTurmaObrigatoriamenteInformado_titulo"),
							UteisJSF.internacionalizar("per_PostarMaterialComTurmaObrigatoriamenteInformado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.UPLOAD,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	POSTAR_MATERIAL_COM_PROFESSOR_OBRIGATORIAMENTE_INFORMADO("PostarMaterialComProfessorObrigatoriamenteInformado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PostarMaterialComProfessorObrigatoriamenteInformado_titulo"),
					UteisJSF.internacionalizar("per_PostarMaterialComProfessorObrigatoriamenteInformado_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_PostarMaterialComProfessorObrigatoriamenteInformado_titulo"),
							UteisJSF.internacionalizar(
									"per_PostarMaterialComProfessorObrigatoriamenteInformado_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_PostarMaterialComProfessorObrigatoriamenteInformado_titulo"),
							UteisJSF.internacionalizar(
									"per_PostarMaterialComProfessorObrigatoriamenteInformado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.UPLOAD,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_PROFESSOR_EXCLUIR_ARQUIVO_INSTITUICAO("PermitirProfessorExcluirArquivoInstituicao",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirProfessorExcluirArquivoInstituicao_titulo"),
							UteisJSF.internacionalizar("per_PermitirProfessorExcluirArquivoInstituicao_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirProfessorExcluirArquivoInstituicao_titulo"),
							UteisJSF.internacionalizar("per_PermitirProfessorExcluirArquivoInstituicao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.UPLOAD,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	APRESENTAR_DATA_DISPONIBILIZACAO_MATERIAL("ApresentarDataDisponibilizacaoMaterial",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_ApresentarDataDisponibilizacaoMaterial_titulo"),
							UteisJSF.internacionalizar("per_ApresentarDataDisponibilizacaoMaterial_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_ApresentarDataDisponibilizacaoMaterial_titulo"),
							UteisJSF.internacionalizar("per_ApresentarDataDisponibilizacaoMaterial_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.UPLOAD,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Forum Professor
	 *
	 */
	FORUM_PROFESSOR("ForumProfessor",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ForumProfessor_titulo"),
							UteisJSF.internacionalizar("per_ForumProfessor_ajuda"),
							new String[] { "forumProfessorCons.xhtml", "forumProfessorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	ATIVAR_INATIVAR_FORUM_PROFESSOR("AtivarInativarForumProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_AtivarInativarForumProfessor_titulo"),
					UteisJSF.internacionalizar("per_AtivarInativarForumProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.FORUM_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	ALTERAR_TEMA_FORUM_PROFESSOR("AlterarTemaForumProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_AlterarTemaForumProfessor_titulo"),
					UteisJSF.internacionalizar("per_AlterarTemaForumProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.FORUM_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	APRESENTAR_SOMENTE_FORUM_CRIADO_PROPRIO_PROFESSOR("ApresentarSomenteForumCriadoProprioProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_ApresentarSomenteForumCriadoProprioProfessor_titulo"),
					UteisJSF.internacionalizar("per_ApresentarSomenteForumCriadoProprioProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.FORUM_PROFESSOR,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	/**
	 * Registro Aula
	 *
	 */
	REGISTRAR_AULA_VISUALIZAR_MATRICULA_TR_CA("RegistrarAula_VisualizarMatriculaTR_CA",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_RegistrarAulaVisualizarMatriculaTRCA_titulo"),
					UteisJSF.internacionalizar("per_RegistrarAulaVisualizarMatriculaTRCA_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITE_LANCAMENTO_AULA_FUTURA_PROFESSOR("PermiteLancamentoAulaFuturaProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermiteLancamentoAulaFuturaProfessor_titulo"),
					UteisJSF.internacionalizar("per_PermiteLancamentoAulaFuturaProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	LINK_VISOES_MOODLE_ALUNO("LinkVisoesMoodle", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
			UteisJSF.internacionalizar("per_LinkVisoesMoodle_titulo"),
			UteisJSF.internacionalizar("per_LinkVisoesMoodle_ajuda"), new String[] { "linkVisoesMoodle.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	/**
	 * Meus Horarios
	 *
	 */
	MEUS_HORARIOS("MeusHorarios", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_MeusHorarios_titulo"),
					UteisJSF.internacionalizar("per_MeusHorarios_ajuda"),
					new String[] { "meusHorariosProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_MeusHorarios_titulo"),
					UteisJSF.internacionalizar("per_MeusHorarios_ajuda"),
					new String[] { "horariosProfessorVisaoCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusHorarios_titulo"),
					UteisJSF.internacionalizar("per_MeusHorarios_ajuda"), new String[] { "meusHorariosAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusHorarios_titulo"),
					UteisJSF.internacionalizar("per_MeusHorarios_ajuda"), new String[] { "meusHorariosAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_GERACAO_EVENTO_ONLINE_GOOGLE_MEET("PermitirGeracaoEventoOnlineGoogleMeet",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirGeracaoEventoOnlineGoogleMeet_titulo"),
					UteisJSF.internacionalizar("per_PermitirGeracaoEventoOnlineGoogleMeet_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.MEUS_HORARIOS,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	PERMITIR_GERACAO_EVENTO_ONLINE_CLASSROOM("PermitirGeracaoEventoOnlineClassroom",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_PermitirGeracaoEventoOnlineClassroom_titulo"),
					UteisJSF.internacionalizar("per_PermitirGeracaoEventoOnlineClassroom_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoProfessorEnum.MEUS_HORARIOS,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),

	HORAS_ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO("AtividadeExtraClasseProfessorPostado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_HorasAtividadeExtraClasse_titulo"),
					UteisJSF.internacionalizar("per_HorasAtividadeExtraClasse_ajuda"),
					new String[] { "atividadeExtraClasse.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_PROFESSOR);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoVisaoProfessorEnum(String valor, PermissaoVisao[] permissaoVisao,
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
