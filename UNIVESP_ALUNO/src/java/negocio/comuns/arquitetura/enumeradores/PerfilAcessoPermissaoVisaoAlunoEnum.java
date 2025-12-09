package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoVisaoAlunoEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Aluno
	 *
	 */
	MEUS_DADOS_ABA_QUALIFICAOES_INFORMACOES_COMPLEMENTARES("MeusDadosAbaQualificaoesInformacoesComplementares",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MeusDadosAbaQualificaoesInformacoesComplementares_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaQualificaoesInformacoesComplementares_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MeusDadosAbaQualificaoesInformacoesComplementares_titulo"),
							UteisJSF.internacionalizar(
									"per_MeusDadosAbaQualificaoesInformacoesComplementares_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	MEUS_DADOS_ABA_QUESTIONARIO("MeusDadosAbaQuestionario", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusDadosAbaQuestionario_titulo"),
					UteisJSF.internacionalizar("per_MeusDadosAbaQuestionario_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusDadosAbaQuestionario_titulo"),
					UteisJSF.internacionalizar("per_MeusDadosAbaQuestionario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	MEUS_DADOS_ABA_EXPERIENCIAS_PROFISSIONAIS("MeusDadosAbaExperienciasProfissionais",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MeusDadosAbaExperienciasProfissionais_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaExperienciasProfissionais_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MeusDadosAbaExperienciasProfissionais_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaExperienciasProfissionais_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	MEUS_DADOS_ABA_DADOS_BASICOS("MeusDadosAbaDadosBasicos", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusDadosAbaDadosBasicos_titulo"),
					UteisJSF.internacionalizar("per_MeusDadosAbaDadosBasicos_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusDadosAbaDadosBasicos_titulo"),
					UteisJSF.internacionalizar("per_MeusDadosAbaDadosBasicos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	MEUS_DADOS_ABA_FORMACAO_EXTRA_CURRICULAR("MeusDadosAbaFormacaoExtraCurricular",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoExtraCurricular_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoExtraCurricular_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoExtraCurricular_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoExtraCurricular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	MEUS_DADOS_ABA_FORMACAO_ACADEMICA("MeusDadosAbaFormacaoAcademica",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoAcademica_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoAcademica_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoAcademica_titulo"),
							UteisJSF.internacionalizar("per_MeusDadosAbaFormacaoAcademica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	MEUS_DADOS_ABA_OBJETIVO("MeusDadosAbaObjetivo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusDadosAbaObjetivo_titulo"),
					UteisJSF.internacionalizar("per_MeusDadosAbaObjetivo_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusDadosAbaObjetivo_titulo"),
					UteisJSF.internacionalizar("per_MeusDadosAbaObjetivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Minhas Notas
	 *
	 */
	MINHAS_NOTAS("MinhasNotas",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MinhasNotas_titulo"),
							UteisJSF.internacionalizar("per_MinhasNotas_ajuda"), new String[] { "minhasNotasAluno" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MinhasNotas_titulo"),
							UteisJSF.internacionalizar("per_MinhasNotas_ajuda"), new String[] { "minhasNotasAluno" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITIR_IMPRIMIR_MATRIZ_CURRICULAR_ALUNO("PermitirImprimirMatrizCurricularAluno",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_titulo"),
							UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_titulo"),
							UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_NOTAS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITIR_APRESENTAR_PROFESSOR("PermitirApresentarProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermitirApresentarProfessor_titulo"),
					UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirApresentarProfessor_ajuda"),
							UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_NOTAS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITIR_GERAR_RELATORIO_DOCUMENTO_INTEGRALIZACAO_CURRICULAR(
			"PermitirGerarRelatorioDocumentoIntegralizacaoCurricular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioDocumentoIntegralizacaoCurricular_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioDocumentoIntegralizacaoCurricular_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar(
									"per_PermitirGerarRelatorioDocumentoIntegralizacaoCurricular_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirGerarRelatorioDocumentoIntegralizacaoCurricular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_NOTAS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	MARCAR_REPOSICAO_AULA("MarcarReposicaoAula", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MarcarReposicaoAula_titulo"),
					UteisJSF.internacionalizar("per_MarcarReposicaoAula_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MarcarReposicaoAula_titulo"),
					UteisJSF.internacionalizar("per_MarcarReposicaoAula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_NOTAS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITIR_APRESENTAR_TODAS_NOTAS_PARAMETRIZADAS_CONFIGURACAO_ACADEMICA(
			"PermitirApresentarTodasNotasParametrizadasConfiguracaoAcademica",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar(
									"per_PermitirApresentarTodasNotasParametrizadasConfiguracaoAcademica_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirApresentarTodasNotasParametrizadasConfiguracaoAcademica_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar(
									"per_PermitirApresentarTodasNotasParametrizadasConfiguracaoAcademica_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirApresentarTodasNotasParametrizadasConfiguracaoAcademica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_NOTAS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Meus Amigos
	 *
	 */
	MEUS_AMIGOS("MeusAmigos", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusAmigos_titulo"),
					UteisJSF.internacionalizar("per_MeusAmigos_ajuda"), new String[] { "meusAmigosAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusAmigos_titulo"),
					UteisJSF.internacionalizar("per_MeusAmigos_ajuda"), new String[] { "meusAmigosAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Meus Professores
	 *
	 */
	MEUS_PROFESSORES("MeusProfessores",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusProfessores_titulo"),
							UteisJSF.internacionalizar("per_MeusProfessores_ajuda"),
							new String[] { "meusProfessoresAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusProfessores_titulo"),
							UteisJSF.internacionalizar("per_MeusProfessores_ajuda"),
							new String[] { "meusProfessoresAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Matricula Online Visao Aluno
	 *
	 */
	MATRICULA_ONLINE_VISAO_ALUNO("MatriculaOnlineVisaoAluno", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MatriculaOnlineVisaoAluno_titulo"),
					UteisJSF.internacionalizar("per_MatriculaOnlineVisaoAluno_ajuda"),
					new String[] { "matriculaOnlineVisaoAlunoForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MatriculaOnlineVisaoAluno_titulo"),
					UteisJSF.internacionalizar("per_MatriculaOnlineVisaoAluno_ajuda"),
					new String[] { "matriculaOnlineVisaoAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Minhas Contas Pagar
	 *
	 */
	MINHAS_CONTAS_PAGAR("MinhasContasPagar",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MinhasContasPagar_titulo"),
							UteisJSF.internacionalizar("per_MinhasContasPagar_ajuda"),
							new String[] { "minhasContasPagarAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MinhasContasPagar_titulo"),
							UteisJSF.internacionalizar("per_MinhasContasPagar_ajuda"),
							new String[] { "minhasContasPagarAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	IMPRIMIR_BOLETO_VENCIDO_VISAO_ALUNO("ImprimirBoletoVencidoVisaoAluno",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ImprimirBoletoVencidoVisaoAluno_titulo"),
							UteisJSF.internacionalizar("per_ImprimirBoletoVencidoVisaoAluno_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ImprimirBoletoVencidoVisaoAluno_titulo"),
							UteisJSF.internacionalizar("per_ImprimirBoletoVencidoVisaoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITIR_VISUALIZAR_FINANCIAMENTO_ESTUDANTIL("PermitirVisualizarFinanciamentoEstudantil",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirVisualizarFinanciamentoEstudantil_titulo"),
							UteisJSF.internacionalizar("per_PermitirVisualizarFinanciamentoEstudantil_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirVisualizarFinanciamentoEstudantil_titulo"),
							UteisJSF.internacionalizar("per_PermitirVisualizarFinanciamentoEstudantil_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITE_VISUALIZAR_ABA_RECORRENCIA_VISAO_ALUNO("PermiteVisualizarAbaRecorrenciaVisaoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermiteVisualizarAbaRecorrenciaVisaoAluno_titulo"),
					UteisJSF.internacionalizar("per_PermiteVisualizarAbaRecorrenciaVisaoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITE_VISUALIZAR_ABA_TRANSACAO_CARTAO_VISAO_ALUNO("PermiteVisualizarAbaTransacaoCartaoVisaoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermiteVisualizarAbaTransacaoCartaoVisaoAluno_titulo"),
					UteisJSF.internacionalizar("per_PermiteVisualizarAbaTransacaoCartaoVisaoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITE_VISUALIZAR_PARCELAS_PERIODO_CONTRATO_ELETRONICO_PENDENDE_REJEITADO(
			"PermiteVisualizarParcelasPeriodoContratosEletronico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermiteVisualizarParcelasPeriodoContratosEletronico_titulo"),
					UteisJSF.internacionalizar("per_PermiteVisualizarParcelasPeriodoContratosEletronico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITE_PAGAMENTO_PARCELAS_PERIODO_CONTRATO_ELETRONICO_PENDENDE_REJEITADO(
			"PermitePagamentoParcelasPeriodoContratosEletronico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermitePagamentoParcelasPeriodoContratosEletronico_titulo"),
					UteisJSF.internacionalizar("per_PermitePagamentoParcelasPeriodoContratosEletronico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MINHAS_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Busca Vagas
	 *
	 */
	BUSCA_VAGAS("BuscaVagas", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_BuscaVagas_titulo"),
					UteisJSF.internacionalizar("per_BuscaVagas_ajuda"), new String[] { "homeBancoTalentos.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_BuscaVagas_titulo"),
					UteisJSF.internacionalizar("per_BuscaVagas_ajuda"), new String[] { "homeBancoTalentos.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Meus Contratos
	 *
	 */
	MEUS_CONTRATOS("MeusContratos", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MeusContratos_titulo"),
					UteisJSF.internacionalizar("per_MeusContratos_ajuda"), new String[] { "meusContratosAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MeusContratos_titulo"),
					UteisJSF.internacionalizar("per_MeusContratos_ajuda"),
					new String[] { "meusContratosAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITIR_VISUALIZAR_APENAS_ULTIMO_CONTRATO_MATRICULA("PermitirVisualizarApenasUltimoContratoMatricula",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirVisualizarApenasUltimoContratoMatricula_titulo"),
							UteisJSF.internacionalizar("per_PermitirVisualizarApenasUltimoContratoMatricula_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirVisualizarApenasUltimoContratoMatricula_titulo"),
							UteisJSF.internacionalizar("per_PermitirVisualizarApenasUltimoContratoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.MEUS_CONTRATOS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Matricula Renovacao Aluno
	 *
	 */
//	MATRICULA_RENOVACAO_ALUNO("MatriculaRenovacaoAluno", new PermissaoVisao[] {
//			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MatriculaRenovacaoAluno_titulo"),
//					UteisJSF.internacionalizar("per_MatriculaRenovacaoAluno_ajuda"), new String[] { "" }),
//			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MatriculaRenovacaoAluno_titulo"),
//					UteisJSF.internacionalizar("per_MatriculaRenovacaoAluno_ajuda"), new String[] { "" }) },
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	
	/**
	 * Lista Exercicio Aluno
	 *
	 */
	LISTA_EXERCICIO_ALUNO("ListaExercicioAluno", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ListaExercicioAluno_titulo"),
					UteisJSF.internacionalizar("per_ListaExercicioAluno_ajuda"),
					new String[] { "listaExercicioAlunoCons.xhtml", "listaExercicioAlunoForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ListaExercicioAluno_titulo"),
					UteisJSF.internacionalizar("per_ListaExercicioAluno_ajuda"),
					new String[] { "listaExercicioAlunoCons.xhtml", "listaExercicioAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Configuracoes Visao
	 *
	 */
	LINK_VISOES_MOODLE_ALUNO("LinkVisoesMoodle", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
			UteisJSF.internacionalizar("per_LinkVisoesMoodle_titulo"),
			UteisJSF.internacionalizar("per_LinkVisoesMoodle_ajuda"), new String[] { "linkVisoesMoodle.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	CONFIGURACOES_VISAO("ConfiguracoesVisao",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ConfiguracoesVisao_titulo"),
							UteisJSF.internacionalizar("per_ConfiguracoesVisao_ajuda"),
							new String[] { "configuracaoAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ConfiguracoesVisao_titulo"),
							UteisJSF.internacionalizar("per_ConfiguracoesVisao_ajuda"),
							new String[] { "configuracaoAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	CONFIGURACOES_ALTERACAO_SENHA("ConfiguracoesAlteracaoSenha",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoSenha_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoSenha_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoSenha_titulo"),
							UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoSenha_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.CONFIGURACOES_VISAO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	CONFIGURACOES_ALTERACAO_COR_TELA("ConfiguracoesAlteracaoCorTela",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoCorTela_titulo"),
							UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoCorTela_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoCorTela_titulo"),
							UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoCorTela_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.CONFIGURACOES_VISAO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	CONFIGURACOES_ALTERACAO_FOTO("ConfiguracoesAlteracaoFoto", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoFoto_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoFoto_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoFoto_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracoesAlteracaoFoto_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.CONFIGURACOES_VISAO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	/**
	 * Atividade Complementar Aluno
	 *
	 */
	ATIVIDADE_COMPLEMENTAR_ALUNO("AtividadeComplementarAluno", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_AtividadeComplementarAluno_titulo"),
					UteisJSF.internacionalizar("per_AtividadeComplementarAluno_ajuda"),
					new String[] { "atividadeComplementarAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_AtividadeComplementarAluno_titulo"),
					UteisJSF.internacionalizar("per_AtividadeComplementarAluno_ajuda"),
					new String[] { "atividadeComplementarAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Estagio Aluno
	 *
	 */
	ESTAGIO_ALUNO("EstagioAluno",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_EstagioAluno_titulo"),
							UteisJSF.internacionalizar("per_EstagioAluno_ajuda"),
							new String[] { "estagioAluno.xhtml", "mapaEstagioFacilitador.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_EstagioAluno_titulo"),
							UteisJSF.internacionalizar("per_EstagioAluno_ajuda"),
							new String[] { "estagioAluno.xhtml", "mapaEstagioFacilitador.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Atividade Discursiva Aluno
	 *
	 */
	ATIVIDADE_DISCURSIVA_ALUNO("AtividadeDiscursivaAluno",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_AtividadeDiscursivaAluno_titulo"),
							UteisJSF.internacionalizar("per_AtividadeDiscursivaAluno_ajuda"),
							new String[] { "atividadeDiscursivaAlunoCons.xhtml",
									"atividadeDiscursivaAlunoForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_AtividadeDiscursivaAluno_titulo"),
							UteisJSF.internacionalizar("per_AtividadeDiscursivaAluno_ajuda"),
							new String[] { "atividadeDiscursivaAlunoCons.xhtml",
									"atividadeDiscursivaAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Duvida Professor Aluno
	 *
	 */
	DUVIDA_PROFESSOR_ALUNO("DuvidaProfessorAluno", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_DuvidaProfessorAluno_titulo"),
					UteisJSF.internacionalizar("per_DuvidaProfessorAluno_ajuda"), new String[] { "" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_DuvidaProfessorAluno_titulo"),
					UteisJSF.internacionalizar("per_DuvidaProfessorAluno_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Criterio Avaliacao Aluno Visao Pais
	 *
	 */
	CRITERIO_AVALIACAO_ALUNO_VISAO_PAIS("CriterioAvaliacaoAlunoVisaoPais",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoPais_titulo"),
							UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoPais_ajuda"),
							new String[] { "criterioAvaliacaoAlunoVisaoPais.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoPais_titulo"),
							UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoPais_ajuda"),
							new String[] { "criterioAvaliacaoAlunoVisaoPais.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Plano Estudo
	 *
	 */
	PLANO_ESTUDO("PlanoEstudo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_PlanoEstudo_titulo"),
					UteisJSF.internacionalizar("per_PlanoEstudo_ajuda"), new String[] { "planoDeEstudoAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_PlanoEstudo_titulo"),
					UteisJSF.internacionalizar("per_PlanoEstudo_ajuda"), new String[] { "planoDeEstudoAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Download Arquivo
	 *
	 */
	DOWNLOAD_ARQUIVO("DownloadArquivo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_DownloadArquivo_titulo"),
					UteisJSF.internacionalizar("per_DownloadArquivo_ajuda"), new String[] { "downloadArquivo.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_DownloadArquivo_titulo"),
					UteisJSF.internacionalizar("per_DownloadArquivo_ajuda"),
					new String[] { "downloadArquivo.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	PERMITIR_ALUNO_VIZUALIZAR_MATERIAIS_PERIODO_CONCLUIDO("PermitirAlunoVizualizarMateriaisPeriodoConcluido",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirAlunoVizualizarMateriaisPeriodoConcluido_titulo"),
							UteisJSF.internacionalizar("per_PermitirAlunoVizualizarMateriaisPeriodoConcluido_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirAlunoVizualizarMateriaisPeriodoConcluido_titulo"),
							UteisJSF.internacionalizar("per_PermitirAlunoVizualizarMateriaisPeriodoConcluido_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.DOWNLOAD_ARQUIVO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	DOCUMENTOS_DIGITAIS("DocumentosDigitais",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_DocumentosDigitais_titulo"),
							UteisJSF.internacionalizar("per_DocumentosDigitais_ajuda"),
							new String[] { "documentosDigitaisCons.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_DocumentosDigitais_titulo"),
							UteisJSF.internacionalizar("per_DocumentosDigitais_ajuda"),
							new String[] { "documentosDigitaisCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITIR_DOCUMENTOS_DIGITAIS_BOLETIM_ACADEMICO("PermitirDocumentosDigitaisBoletimAcademico",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisBoletimAcademico_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisBoletimAcademico_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisBoletimAcademico_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisBoletimAcademico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.DOCUMENTOS_DIGITAIS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITIR_DOCUMENTOS_DIGITAIS_DECLARACAO("PermitirDocumentosDigitaisDeclaracao",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDeclaracao_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDeclaracao_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDeclaracao_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDeclaracao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.DOCUMENTOS_DIGITAIS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITIR_DOCUMENTOS_DIGITAIS_HISTORICO("PermitirDocumentosDigitaisHistorico",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisHistorico_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisHistorico_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisHistorico_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisHistorico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.DOCUMENTOS_DIGITAIS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITIR_DOCUMENTOS_DIGITAIS_CERTIFICADOS("PermitirDocumentosDigitaisCertificados",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisCertificados_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisCertificados_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisCertificados_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisCertificados_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.DOCUMENTOS_DIGITAIS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	PERMITIR_DOCUMENTOS_DIGITAIS_DIPLOMAS("PermitirDocumentosDigitaisDiploma",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDiploma_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDiploma_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDiploma_titulo"),
							UteisJSF.internacionalizar("per_PermitirDocumentosDigitaisDiploma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoVisaoAlunoEnum.DOCUMENTOS_DIGITAIS,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO),

	/**
	 * Forum Aluno
	 *
	 */
	FORUM_ALUNO("ForumAluno",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ForumAluno_titulo"),
							UteisJSF.internacionalizar("per_ForumAluno_ajuda"),
							new String[] { "forumAlunoCons.xhtml", "forumAlunoForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ForumAluno_titulo"),
							UteisJSF.internacionalizar("per_ForumAluno_ajuda"),
							new String[] { "forumAlunoCons.xhtml", "forumAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Minhas Faltas
	 *
	 */
	MINHAS_FALTAS("MinhasFaltas", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MinhasFaltas_titulo"),
					UteisJSF.internacionalizar("per_MinhasFaltas_ajuda"), new String[] { "minhasFaltasAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_MinhasFaltas_titulo"),
					UteisJSF.internacionalizar("per_MinhasFaltas_ajuda"), new String[] { "minhasFaltasAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_ALUNO),
	/**
	 * Atividade Complementar Aluno
	 *
	 */
	ATIVIDADE_COMPLEMENTAR_ALUNO_PERMITE_INCLUIR_ATIVIDADE("AtividadeComplementarAluno_permitirIncluirAtividade",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar(
									"per_AtividadeComplementarAluno_permitirIncluirAtividade_titulo"),
							UteisJSF.internacionalizar("per_AtividadeComplementarAluno_permitirIncluirAtividade_ajuda"),
							new String[] { "atividadeComplementarAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar(
									"per_AtividadeComplementarAluno_permitirIncluirAtividade_titulo"),
							UteisJSF.internacionalizar("per_AtividadeComplementarAluno_permitirIncluirAtividade_ajuda"),
							new String[] { "atividadeComplementarAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, ATIVIDADE_COMPLEMENTAR_ALUNO,
			PerfilAcessoSubModuloEnum.VISAO_ALUNO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoVisaoAlunoEnum(String valor, PermissaoVisao[] permissaoVisao,
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
