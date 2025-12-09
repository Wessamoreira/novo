package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoAcademicoEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Aluno
	 *
	 */
	ALUNO("Aluno",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_Aluno_titulo"),
							UteisJSF.internacionalizar("per_Aluno_ajuda"), new String[] { "dadosPessoaisAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_Aluno_titulo"),
							UteisJSF.internacionalizar("per_Aluno_ajuda"), new String[] { "dadosPessoaisAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Aluno_titulo"),
							UteisJSF.internacionalizar("per_Aluno_ajuda"),
							new String[] { "alunoCons.xhtml", "alunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),

	PERMITIR_UNIFICAR_CADASTRO_ALUNO("Aluno_PermitirUnificarCadastroAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Aluno_PermitirUnificarCadastroAluno_titulo"),
					UteisJSF.internacionalizar("per_Aluno_PermitirUnificarCadastroAluno_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	LIBERAR_EDICAO_ALUNO_SOMENTE_COM_SENHA("Aluno_LiberarEdicaoAlunoSomenteComSenha",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Aluno_LiberarEdicaoAlunoSomenteComSenha_titulo"),
					UteisJSF.internacionalizar("per_Aluno_LiberarEdicaoAlunoSomenteComSenha_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	LIBERAR_EDICAO_REGISTRO_ACADEMICO_ALUNO("Aluno_LiberarEdicaoRegistroAcademicoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Aluno_LiberarEdicaoRegistroAcademicoAluno_titulo"),
					UteisJSF.internacionalizar("per_Aluno_LiberarEdicaoRegistroAcademicoAluno_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	/**
	 * Central Aluno
	 */
	CENTRAL_ALUNO("CentralAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CentralAluno_titulo"),
					UteisJSF.internacionalizar("per_CentralAluno_ajuda"), new String[] { "fichaAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_MATRICULA("PermitirVisualizarDadosMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosMatricula_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosMatricula_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_FINANCEIRO("PermitirVisualizarDadosFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosFinanceiro_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_REQUERIMENTO("PermitirVisualizarDadosRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosRequerimento_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosRequerimento_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_BIBLIOTECA("PermitirVisualizarDadosBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosBiblioteca_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_CRM("PermitirVisualizarDadosCRM",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosCRM_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosCRM_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_PROCESSO_SELETIVO("PermitirVisualizarDadosProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosProcessoSeletivo_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	PERMITIR_VISUALIZAR_DADOS_COMUNICACAO_INTERNA("PermitirVisualizarDadosComunicacaoInterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosComunicacaoInterna_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarDadosComunicacaoInterna_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CENTRAL_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),

	ALUNO_INICIAR_MATRICULA("AlunoIniciarMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunoIniciarMatricula_titulo"),
					UteisJSF.internacionalizar("per_AlunoIniciarMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	ALUNO_ARTEFATOS_ENTREGUE("AlunoArtefatosEntregue",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunoArtefatosEntregue_titulo"),
					UteisJSF.internacionalizar("per_AlunoArtefatosEntregue_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	ALUNO_TORNAR_CAMPO_TIPO_MIDIA_CAPTACAO_OBRIGATORIO("AlunoTornarCampoTipoMidiaCaptacaoObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunoTornarCampoTipoMidiaCaptacaoObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_AlunoTornarCampoTipoMidiaCaptacaoObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	/**
	 * Upload
	 *
	 */
	UPLOAD("Upload", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Upload_titulo"),
					UteisJSF.internacionalizar("per_Upload_ajuda"), new String[] { "uploadArquivosProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Upload_titulo"),
					UteisJSF.internacionalizar("per_Upload_ajuda"), new String[] { "uploadArquivosCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Upload_titulo"),
					UteisJSF.internacionalizar("per_Upload_ajuda"), new String[] { "uploadArquivos.xhtml" })
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Minhas Disciplinas
	 *
	 */
	DISCIPLINA("Disciplina", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Disciplina_titulo_prof"),
					UteisJSF.internacionalizar("per_Disciplina_ajuda"), new String[] { "disciplinaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Disciplina_titulo_coord"),
					UteisJSF.internacionalizar("per_Disciplina_ajuda"), new String[] { "disciplinaCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Disciplina_titulo_adm"),
					UteisJSF.internacionalizar("per_Disciplina_ajuda"),
					new String[] { "disciplinaCons.xhtml", "disciplinaForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_Disciplina_titulo"),
					UteisJSF.internacionalizar("per_Disciplina_ajuda"),
					new String[] { "minhasDisciplinasAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_Disciplina_titulo"),
					UteisJSF.internacionalizar("per_Disciplina_ajuda"),
					new String[] { "minhasDisciplinasAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	
	EIXO_CURSO("EixoCurso", new PermissaoVisao[] {
//			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_EixoDeCurso_titulo_prof"),
//					UteisJSF.internacionalizar("per_EixoDeCurso_ajuda"), new String[] { "disciplinaProfessor.xhtml" }),
//			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_EixoDeCurso_titulo_coord"),
//					UteisJSF.internacionalizar("per_EixoDeCurso_ajuda"), new String[] { "disciplinaCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EixoCurso_titulo"),
					UteisJSF.internacionalizar("per_EixoCurso_ajuda"), //AlterarApontamentos
					new String[] { "eixoCursoCons.xhtml", "eixoCursoForm.xhtml" }),
//			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_EixoDeCurso_titulo"),
//					UteisJSF.internacionalizar("per_EixoDeCurso_ajuda"),
//					new String[] { "minhasDisciplinasAluno.xhtml" }),
//			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_EixoDeCurso_titulo"),
//					UteisJSF.internacionalizar("per_EixoDeCurso_ajuda"),
//					new String[] { "minhasDisciplinasAluno.xhtml" }) 
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	
	
	
	
	ALTERAR_TODOS_PLANO_ENSINO("AlterarTodosPlanoEnsino",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_AlterarTodosPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_AlterarTodosPlanoEnsino_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_AlterarTodosPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_AlterarTodosPlanoEnsino_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	PERMITE_EXCLUIR_PLANO_ENSINO("PermiteExcluirPlanoEnsino",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteExcluirPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_PermiteExcluirPlanoEnsino_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermiteExcluirPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_PermiteExcluirPlanoEnsino_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	AUTORIZAR_PUBLICAR_PLANO_ENSINO_VISAO_PROFESSOR_COORDENADOR("AutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_AutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador_titulo"),
					UteisJSF.internacionalizar("per_AutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_AutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador_titulo"),
							UteisJSF.internacionalizar(
									"per_AutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	PERMITIR_CONSULTAR_PLANO_ENSINO_ANTERIOR("PermitirConsultarPlanoEnsinoAnterior",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirConsultarPlanoEnsinoAnterior_titulo"),
							UteisJSF.internacionalizar("per_PermitirConsultarPlanoEnsinoAnterior_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirConsultarPlanoEnsinoAnterior_titulo"),
							UteisJSF.internacionalizar("per_PermitirConsultarPlanoEnsinoAnterior_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	CRIAR_NOVO_PLANO_ENSINO("CriarNovoPlanoEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_CriarNovoPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_CriarNovoPlanoEnsino_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_CriarNovoPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_CriarNovoPlanoEnsino_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	ALTERAR_APENAS_ULTIMO_PLANO_ENSINO("AlterarApenasUltimoPlanoEnsino",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_AlterarApenasUltimoPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_AlterarApenasUltimoPlanoEnsino_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_AlterarApenasUltimoPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_AlterarApenasUltimoPlanoEnsino_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	CLONAR_PLANO_ENSINO_PROFESSOR("ClonarPlanoEnsinoProfessor",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_ClonarPlanoEnsinoProfessor_titulo"),
							UteisJSF.internacionalizar("per_ClonarPlanoEnsinoProfessor_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Advertencia
	 *
	 */
	ADVERTENCIA("Advertencia", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Advertencia_titulo"),
					UteisJSF.internacionalizar("per_Advertencia_ajuda"),
					new String[] { "advertenciaVisaoProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Advertencia_titulo"),
					UteisJSF.internacionalizar("per_Advertencia_ajuda"),
					new String[] { "advertenciaConsCoordenador.xhtml", "advertenciaFormCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Advertencia_titulo"),
					UteisJSF.internacionalizar("per_Advertencia_ajuda"),
					new String[] { "advertenciaCons.xhtml", "advertenciaForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_Advertencia_titulo"),
					UteisJSF.internacionalizar("per_Advertencia_ajuda"),
					new String[] { "minhasAdvertenciasAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_Advertencia_titulo"),
					UteisJSF.internacionalizar("per_Advertencia_ajuda"),
					new String[] { "minhasAdvertenciasAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Lancamento Nota
	 *
	 */
	LANCAMENTO_NOTA("LancamentoNota", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_LancamentoNota_titulo_prof"),
					UteisJSF.internacionalizar("per_LancamentoNota_ajuda"),
					new String[] { "registrarNotaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_LancamentoNota_titulo_coord"),
					UteisJSF.internacionalizar("per_LancamentoNota_ajuda"),
					new String[] { "registrarAulaCoordenador.xhtml", "registrarNotaCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LancamentoNota_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_ajuda"), new String[] { "lancamentoNota.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_LANCAR_NOTA_DISCIPLINA_COMPOSTA("PermiteLancarNotaDisciplinaComposta",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_permiteLancarNotaDisciplinaComposta_titulo"),
							UteisJSF.internacionalizar("per_permiteLancarNotaDisciplinaComposta_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_permiteLancarNotaDisciplinaComposta_titulo"),
							UteisJSF.internacionalizar("per_permiteLancarNotaDisciplinaComposta_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_permiteLancarNotaDisciplinaComposta_titulo"),
							UteisJSF.internacionalizar("per_permiteLancarNotaDisciplinaComposta_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_INFORMAR_TITULO_NOTA("PermiteInformarTituloNota",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTAA_VISUALIZAR_MATRICULA_TR_CA("LancamentoNotaa_VisualizarMatriculaTR_CA",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_LancamentoNotaaVisualizarMatriculaTRCA_titulo"),
							UteisJSF.internacionalizar("per_LancamentoNotaaVisualizarMatriculaTRCA_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_LancamentoNotaaVisualizarMatriculaTRCA_titulo"),
							UteisJSF.internacionalizar("per_LancamentoNotaaVisualizarMatriculaTRCA_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.VISAO_PROFESSOR),
	PERMITIR_LANCAR_NOTA_RETROATIVO("PermitirLancarNotaRetroativo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirLancarNotaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirLancarNotaRetroativo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirLancarNotaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirLancarNotaRetroativo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirLancarNotaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirLancarNotaRetroativo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTA_PERMITIR_ALTERAR_DATA_REGISTRO("LancamentoNota_AlterarDataRegistro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LancamentoNota_alterarDataRegistro_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_alterarDataRegistro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTA_PERMITIR_REPLICAR_NOTA_OUTRA_DISCIPLINA("PermiteReplicarNotaOutraDisciplina", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_LancamentoNota_permiteReplicarNotaOutraDisciplina_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_permiteReplicarNotaOutraDisciplina_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_LancamentoNota_permiteReplicarNotaOutraDisciplina_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_permiteReplicarNotaOutraDisciplina_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LancamentoNota_permiteReplicarNotaOutraDisciplina_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_permiteReplicarNotaOutraDisciplina_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTA_PERMITIR_VISUALIZAR_NOTA_FREQUENCIA_OUTRO_PROFESSOR("LancamentoNota_PermitirVisualizarNotaFrequenciaOutroProfessor", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_LancamentoNota_PermitirVisualizarNotaFrequenciaOutroProfessor_titulo"),UteisJSF.internacionalizar("per_LancamentoNota_PermitirVisualizarNotaFrequenciaOutroProfessor_ajuda")),			 			 			
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA, 
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTA_PERMITE_DEFINIR_DETALHAMENTO_NOTA("LancamentoNota_PermiteDefinirDetalhamentoNota",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTA_CURSOS_INTEGRAIS_TELA_NOTA_AULA_POS("CursosIntegraisDevemSerRegistradoTelaRegistroAulaNotaPos", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_LancamentoNota_CursosIntegraisDevemSerRegistradoTelaRegistroAulaNotaPos_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_CursosIntegraisDevemSerRegistradoTelaRegistroAulaNotaPos_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Ata Prova
	 *
	 */
	ATA_PROVA("AtaProva", 
			new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AtaProva_titulo"),
					UteisJSF.internacionalizar("per_AtaProva_ajuda"), new String[] { "ataProvaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_AtaProva_titulo"),
					UteisJSF.internacionalizar("per_AtaProva_ajuda"),
					new String[] { "ataProvaVisaoCoordenador.xhtml" }),
			
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AtaProva_titulo"),
					UteisJSF.internacionalizar("per_AtaProva_ajuda"), new String[] { "ataProvaFuncionario.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITIR_GERAR_ATA_PROVA_RETROATIVO("PermitirGerarAtaProvaRetroativo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirGerarAtaProvaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirGerarAtaProvaRetroativo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirGerarAtaProvaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirGerarAtaProvaRetroativo_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ATA_PROVA,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Curso
	 *
	 */
	CURSO("Curso",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Curso_titulo"),
							UteisJSF.internacionalizar("per_Curso_ajuda"), new String[] { "cursoProfessor.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Curso_titulo"),
							UteisJSF.internacionalizar("per_Curso_ajuda"), new String[] { "cursoCoordenador.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Curso_titulo"),
							UteisJSF.internacionalizar("per_Curso_ajuda"),
							new String[] { "cursoCons.xhtml", "cursoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	CURSO_MODALIDADECURSO("PermitirInformarMolidadeCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirInformarMolidadeCurso_titulo"),
					UteisJSF.internacionalizar("per_PermitirInformarMolidadeCurso_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	CURSO_EXCLUIR_AUTORIZACAO_CURSO("PermitirExcluirAutorizacaoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirExcluirAutorizacaoCurso_titulo"),
					UteisJSF.internacionalizar("per_PermitirExcluirAutorizacaoCurso_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	CURSO_PERMITIR_ALTERAR_GRADE_CURRICULAR_CONSTRUCAO("PermitirAlterarMatrizCurricularConstrucao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarMatrizCurricularConstrucao_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarMatrizCurricularConstrucao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	CURSO_PERMITIR_ALTERAR_GRADE_CURRICULAR_ATIVA_INATIVA("PermitirAlterarMatrizCurricularAtivaInativa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarMatrizCurricularAtivaInativa_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarMatrizCurricularAtivaInativa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	
	GRADE_CURRICULAR_DESATIVAR_GRADE("GradeCurricular_desativarGrade",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_GradeCurriculardesativarGrade_titulo"),
							UteisJSF.internacionalizar("per_GradeCurriculardesativarGrade_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_GradeCurriculardesativarGrade_titulo"),
							UteisJSF.internacionalizar("per_GradeCurriculardesativarGrade_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_GradeCurriculardesativarGrade_titulo"),
							UteisJSF.internacionalizar("per_GradeCurriculardesativarGrade_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	GRADE_CURRICULAR_ATIVAR_GRADE("GradeCurricular_ativarGrade",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_GradeCurricularativarGrade_titulo"),
							UteisJSF.internacionalizar("per_GradeCurricularativarGrade_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_GradeCurricularativarGrade_titulo"),
							UteisJSF.internacionalizar("per_GradeCurricularativarGrade_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_GradeCurricularativarGrade_titulo"),
							UteisJSF.internacionalizar("per_GradeCurricularativarGrade_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	PERMITIR_ALTERAR_DATA_ATIVACAO_DATA_FINAL_VIGENCIA_MATRIZ_CURRICULAR(
			"PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular_titulo"),
							UteisJSF.internacionalizar(
									"per_PermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Registro Aula Nota
	 *
	 */
	REGISTRO_AULA_NOTA("RegistroAulaNota", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_RegistroAulaNota_titulo"),
					UteisJSF.internacionalizar("per_RegistroAulaNota_ajuda"),
					new String[] { "registrarAulaNotaCoordenador.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_RegistroAulaNota_titulo"),
					UteisJSF.internacionalizar("per_RegistroAulaNota_ajuda"),
					new String[] { "registrarAulaNotaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_RegistroAulaNota_titulo"),
					UteisJSF.internacionalizar("per_RegistroAulaNota_ajuda"),
					new String[] { "registroAulaNotaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	LANCAMENTO_NOTA_OCULTAR_SITUACAO_MATRICULA("LancamentoNota_OcultarSituacaoMatricula",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_LancamentoNotaOcultarSituacaoMatricula_titulo"),
							UteisJSF.internacionalizar("per_LancamentoNotaOcultarSituacaoMatricula_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_LancamentoNotaOcultarSituacaoMatricula_titulo"),
							UteisJSF.internacionalizar("per_LancamentoNotaOcultarSituacaoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	REGISTRO_AULA_NOTA_PERMITE_INFORMAR_TITULO_NOTA("RegistrarAulaNotaPermiteInformarTituloNota",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteInformarTituloNota_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	REGISTRO_AULA_NOTA_PERMITE_DEFINIR_DETALHAMENTO_NOTA("RegistrarAulaNota_PermiteDefinirDetalhamentoNota",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_titulo"),
							UteisJSF.internacionalizar("per_PermiteDefinirDetalhamentoNota_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Registro Atividade Complementar
	 *
	 */
	REGISTRO_ATIVIDADE_COMPLEMENTAR("RegistroAtividadeComplementar",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_RegistroAtividadeComplementar_titulo"),
							UteisJSF.internacionalizar("per_RegistroAtividadeComplementar_ajuda"),
							new String[] { "registroAtividadeComplementarConsCoordenador.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_RegistroAtividadeComplementar_titulo"),
							UteisJSF.internacionalizar("per_RegistroAtividadeComplementar_ajuda"),
							new String[] { "registroAtividadeComplementarCons.xhtml",
									"registroAtividadeComplementarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	PERMITIR_LIBERAR_CARGA_HORARIA_MAXIMA_PERIODO_LETIVO("PermitirLiberarCargaHorariaMaximaPeriodoLetivo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirLiberarCargaHorariaMaximaPeriodoLetivo_titulo"),
							UteisJSF.internacionalizar("per_PermitirLiberarCargaHorariaMaximaPeriodoLetivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirLiberarCargaHorariaMaximaPeriodoLetivo_titulo"),
							UteisJSF.internacionalizar("per_PermitirLiberarCargaHorariaMaximaPeriodoLetivo_ajuda"),
							new String[] { "registroAtividadeComplementarConsCoordenador.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.REGISTRO_ATIVIDADE_COMPLEMENTAR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Acompanhamento Atividade Complementar
	 *
	 */
	ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR("AcompanhamentoAtividadeComplementar",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_AcompanhamentoAtividadeComplementar_titulo"),
							UteisJSF.internacionalizar("per_AcompanhamentoAtividadeComplementar_ajuda"),
							new String[] { "acompanhamentoAtividadeComplementarCoordenador.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_AcompanhamentoAtividadeComplementar_titulo"),
							UteisJSF.internacionalizar("per_AcompanhamentoAtividadeComplementar_ajuda"),
							new String[] { "acompanhamentoAtividadeComplementarCons.xhtml",
									"acompanhamentoAtividadeComplementarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Tema Assunto
	 *
	 */
	TEMA_ASSUNTO("TemaAssunto", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_TemaAssunto_titulo"), UteisJSF.internacionalizar("per_TemaAssunto_ajuda"),
			new String[] { "temaAssuntoCons.xhtml", "temaAssuntoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	
	CALENDARIO_AGRUPAMENTO_TCC("CalendarioAgrupamentoTcc", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_CalendarioAgrupamentoTcc_titulo"), UteisJSF.internacionalizar("per_CalendarioAgrupamentoTcc_ajuda"),new String[] { "calendarioAgrupamentoTccCons.xhtml", "calendarioAgrupamentoTccForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	
	/**
	 * Coordenador TCC
	 *
	 */
//	COORDENADOR_TCC("CoordenadorTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_CoordenadorTCC_titulo"),
//					UteisJSF.internacionalizar("per_CoordenadorTCC_ajuda"),
//					new String[] { "coordenadorTCCCons.xhtml", "coordenadorTCCForm.xhtml" }) },
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
	/**
	 * Trabalho Conclusao Curso
	 *
	 */
//	TRABALHO_CONCLUSAO_CURSO("TrabalhoConclusaoCurso",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_TrabalhoConclusaoCurso_titulo"),
//					UteisJSF.internacionalizar("per_TrabalhoConclusaoCurso_ajuda"),
//					new String[] { "trabalhoConclusaoCursoCons.xhtml", "trabalhoConclusaoCursoForm.xhtml" }) },
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//	ENCAMINHAR_TCC("EncaminharTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_EncaminharTCC_titulo"),
//					UteisJSF.internacionalizar("per_EncaminharTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	REPROVAR_TCC("ReprovarTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_ReprovarTCC_titulo"),
//					UteisJSF.internacionalizar("per_ReprovarTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	POSTAR_ARQUIVO_TCC("PostarArquivoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_PostarArquivoTCC_titulo"),
//					UteisJSF.internacionalizar("per_PostarArquivoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	APROVAR_ELABORACAO_TCC("AprovarElaboracaoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_AprovarElaboracaoTCC_titulo"),
//					UteisJSF.internacionalizar("per_AprovarElaboracaoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	APROVAR_PLANO_TCC("AprovarPlanoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_AprovarPlanoTCC_titulo"),
//					UteisJSF.internacionalizar("per_AprovarPlanoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	REVISAR_PLANO_TCC("RevisarPlanoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_RevisarPlanoTCC_titulo"),
//					UteisJSF.internacionalizar("per_RevisarPlanoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	REGISTRAR_ARTEFATO_TCC("RegistrarArtefatoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_RegistrarArtefatoTCC_titulo"),
//					UteisJSF.internacionalizar("per_RegistrarArtefatoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	LANCAR_NOTAS_TCC("LancarNotasTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_LancarNotasTCC_titulo"),
//					UteisJSF.internacionalizar("per_LancarNotasTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	EXTENDER_PRAZO_EXECUCAO_TCC("ExtenderPrazoExecucaoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_ExtenderPrazoExecucaoTCC_titulo"),
//					UteisJSF.internacionalizar("per_ExtenderPrazoExecucaoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	SOLICITAR_NOVO_ARQUIVO_TCC("SolicitarNovoArquivoTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_SolicitarNovoArquivoTCC_titulo"),
//					UteisJSF.internacionalizar("per_SolicitarNovoArquivoTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	RETORNAR_TCCFASE_ANTERIOR("RetornarTCCFAseAnterior",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_RetornarTCCFAseAnterior_titulo"),
//					UteisJSF.internacionalizar("per_RetornarTCCFAseAnterior_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	MEMBRO_BANCA_TCC("MembroBancaTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_MembroBancaTCC_titulo"),
//					UteisJSF.internacionalizar("per_MembroBancaTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
//
//	DEFINIR_ORIENTADOR_TCC("DefinirOrientadorTCC",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_DefinirOrientadorTCC_titulo"),
//					UteisJSF.internacionalizar("per_DefinirOrientadorTCC_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRABALHO_CONCLUSAO_CURSO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MONOGRAFIA),
	/**
	 * Mapa Notas Disciplina Alunos Rel
	 *
	 */
	MAPA_NOTAS_DISCIPLINA_ALUNOS_REL("MapaNotasDisciplinaAlunosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaNotasDisciplinaAlunosRel_titulo"),
					UteisJSF.internacionalizar("per_MapaNotasDisciplinaAlunosRel_ajuda"),
					new String[] { "mapaNotasDisciplinaAlunosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Controle Documentacao Aluno Rel
	 *
	 */
	CONTROLE_DOCUMENTACAO_ALUNO_REL("ControleDocumentacaoAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleDocumentacaoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_ControleDocumentacaoAlunoRel_ajuda"),
					new String[] { "debitoDocumentosAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Declaracao Transferencia Rel
	 *
	 */
	DECLARACAO_TRANSFERENCIA_REL("DeclaracaoTransferenciaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoTransferenciaRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoTransferenciaRel_ajuda"),
					new String[] { "declaracaoTransferenciaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Aluno Nao Cursou Disciplina Rel
	 *
	 */
	ALUNO_NAO_CURSOU_DISCIPLINA_REL("AlunoNaoCursouDisciplinaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunoNaoCursouDisciplinaRel_titulo"),
					UteisJSF.internacionalizar("per_AlunoNaoCursouDisciplinaRel_ajuda"),
					new String[] { "alunoNaoCursouDisciplinaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Envelope Rel
	 *
	 */
	ENVELOPE_REL("EnvelopeRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EnvelopeRel_titulo"),
					UteisJSF.internacionalizar("per_EnvelopeRel_ajuda"), new String[] { "envelopeRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Alunos Nao Renovaram Rel
	 *
	 */
	ALUNOS_NAO_RENOVARAM_REL("AlunosNaoRenovaramRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosNaoRenovaramRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosNaoRenovaramRel_ajuda"),
					new String[] { "alunosNaoRenovaramRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Estatistica Matricula Rel
	 *
	 */
	ESTATISTICA_MATRICULA_REL("EstatisticaMatriculaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaMatriculaRel_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaMatriculaRel_ajuda"),
					new String[] { "estatisticaMatriculaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Espelho Rel
	 *
	 */
	ESPELHO_REL("EspelhoRel", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EspelhoRel_titulo"),
					UteisJSF.internacionalizar("per_EspelhoRel_ajuda"), new String[] { "espelhoRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_EspelhoRel_titulo"),
					UteisJSF.internacionalizar("per_EspelhoRel_ajuda"), new String[] { "espelhoDiarioProfessor.xhtml" }) ,
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_EspelhoRel_titulo"),
					UteisJSF.internacionalizar("per_EspelhoRel_ajuda"), new String[] { "espelhoDiarioCoordenadorRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	
	ALUNO_BAIXA_FREQUENCIA_REL("AlunoBaixaFrequenciaRel", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AlunoBaixaFrequenciaRel_titulo"),
					UteisJSF.internacionalizar("per_AlunoBaixaFrequenciaRel_ajuda"), new String[] { "alunoBaixaFrequenciaRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_AlunoBaixaFrequenciaRel_titulo"),
					UteisJSF.internacionalizar("per_AlunoBaixaFrequenciaRel_ajuda"), new String[] { "alunoBaixaFrequenciaCoordenador.xhtml" }) },
			
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	
	
	/**
	 * Mapa Situacao Aluno Rel
	 *
	 */
	MAPA_SITUACAO_ALUNO_REL("MapaSituacaoAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSituacaoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_MapaSituacaoAlunoRel_ajuda"),
					new String[] { "mapaSituacaoAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Alunos Matriculados Geral Rel
	 *
	 */
	ALUNOS_MATRICULADOS_GERAL_REL("AlunosMatriculadosGeralRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosMatriculadosGeralRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosMatriculadosGeralRel_ajuda"),
					new String[] { "alunosMatriculadosGeralRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),

	/**
	 * Livro Matriculas Rel
	 *
	 */
	LIVRO_MATRICULAS_REL("LivroMatriculaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LivroMatricula_titulo"),
					UteisJSF.internacionalizar("per_LivroMatricula_ajuda"),
					new String[] { "livroMatriculaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Relacao Endereco Aluno Rel
	 *
	 */
	RELACAO_ENDERECO_ALUNO_REL("RelacaoEnderecoAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelacaoEnderecoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_RelacaoEnderecoAlunoRel_ajuda"),
					new String[] { "relacaoEnderecoAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Abertura Turma Rel
	 *
	 */
	ABERTURA_TURMA_REL("AberturaTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AberturaTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_AberturaTurmaRel_ajuda"),
					new String[] { "aberturaTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Comunicado Debito Documentos Aluno Rel
	 *
	 */
	COMUNICADO_DEBITO_DOCUMENTOS_ALUNO_REL("ComunicadoDebitoDocumentosAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ComunicadoDebitoDocumentosAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_ComunicadoDebitoDocumentosAlunoRel_ajuda"),
					new String[] { "comunicadoDebitoDocumentosAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Declaracao Setransp Rel
	 *
	 */
	DECLARACAO_SETRANSP_REL("DeclaracaoSetranspRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoSetranspRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoSetranspRel_ajuda"),
					new String[] { "declaracaoSetranspRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Mapa Nota Pendencia Aluno Rel
	 *
	 */
	MAPA_NOTA_PENDENCIA_ALUNO_REL("MapaNotaPendenciaAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaNotaPendenciaAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_MapaNotaPendenciaAlunoRel_ajuda"),
					new String[] { "mapaNotaPendenciaAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Boletim Academico Rel
	 *
	 */
	BOLETIM_ACADEMICO_REL("BoletimAcademicoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BoletimAcademicoRel_titulo"),
					UteisJSF.internacionalizar("per_BoletimAcademicoRel_ajuda"),
					new String[] { "boletimAcademicoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Criterio Avaliacao Aluno Rel
	 *
	 */
	CRITERIO_AVALIACAO_ALUNO_REL("CriterioAvaliacaoAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoRel_ajuda"),
					new String[] { "criterioAvaliacaoAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Possiveis Formandos Rel
	 *
	 */
	POSSIVEIS_FORMANDOS_REL("PossiveisFormandosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PossiveisFormandosRel_titulo"),
					UteisJSF.internacionalizar("per_PossiveisFormandosRel_ajuda"),
					new String[] { "possiveisFormandosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Termo Compromisso Documentacao Pendente Rel
	 *
	 */
	TERMO_COMPROMISSO_DOCUMENTACAO_PENDENTE_REL("TermoCompromissoDocumentacaoPendenteRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TermoCompromissoDocumentacaoPendenteRel_titulo"),
					UteisJSF.internacionalizar("per_TermoCompromissoDocumentacaoPendenteRel_ajuda"),
					new String[] { "termoCompromissoDocumentacaoPendenteRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Quadro Alunos Ativos Inativos Rel
	 *
	 */
	QUADRO_ALUNOS_ATIVOS_INATIVOS_REL("QuadroAlunosAtivosInativosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuadroAlunosAtivosInativosRel_titulo"),
					UteisJSF.internacionalizar("per_QuadroAlunosAtivosInativosRel_ajuda"),
					new String[] { "quadroAlunosAtivosInativosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Declaracao Abandono Curso Rel
	 *
	 */
	DECLARACAO_ABANDONO_CURSO_REL("DeclaracaoAbandonoCursoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoAbandonoCursoRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoAbandonoCursoRel_ajuda"),
					new String[] { "declaracaoAbandonoCursoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Alunos Por Disciplinas Rel
	 *
	 */
	ALUNOS_POR_DISCIPLINAS_REL("AlunosPorDisciplinasRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosPorDisciplinasRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosPorDisciplinasRel_ajuda"),
					new String[] { "alunosPorDisciplinasRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Etiqueta Aluno Rel
	 *
	 */
	ETIQUETA_ALUNO_REL("EtiquetaAlunoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_EtiquetaAlunoRel_titulo"),
			UteisJSF.internacionalizar("per_EtiquetaAlunoRel_ajuda"), new String[] { "etiquetaAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Quadro Matricula Rel
	 *
	 */
	QUADRO_MATRICULA_REL("QuadroMatriculaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuadroMatriculaRel_titulo"),
					UteisJSF.internacionalizar("per_QuadroMatriculaRel_ajuda"),
					new String[] { "quadroMatriculaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Disciplinas Grade Rel
	 *
	 */
	DISCIPLINAS_GRADE_REL("DisciplinasGradeRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DisciplinasGradeRel_titulo"),
					UteisJSF.internacionalizar("per_DisciplinasGradeRel_ajuda"),
					new String[] { "disciplinasGradeRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Declaracao Passe Estudantil Rel
	 *
	 */
	DECLARACAO_PASSE_ESTUDANTIL_REL("DeclaracaoPasseEstudantilRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoPasseEstudantilRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoPasseEstudantilRel_ajuda"),
					new String[] { "declaracaoPasseEstudantilRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Controle Vaga Rel
	 *
	 */
	CONTROLE_VAGA_REL("ControleVagaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleVagaRel_titulo"),
					UteisJSF.internacionalizar("per_ControleVagaRel_ajuda"),
					new String[] { "controleVagaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Nota Não Lançada Professor Rel
	 *
	 */
	NOTA_NAO_LANCADA_PROFESSOR_REL("NotaNaoLancadaProfessorRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NotaNaoLancadaProfessorRel_titulo"),
					UteisJSF.internacionalizar("per_NotaNaoLancadaProfessorRel_ajuda"),
					new String[] { "notaNaoLancadaProfessorRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Professor Rel
	 *
	 */
	PROFESSOR_REL("ProfessorRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProfessorRel_titulo"),
					UteisJSF.internacionalizar("per_ProfessorRel_ajuda"), new String[] { "professorRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_PROFESSORES),
	/**
	 * Historico Turma Rel
	 *
	 */
	HISTORICO_TURMA_REL("HistoricoTurmaRel", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_HistoricoTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_HistoricoTurmaRel_ajuda"),
					new String[] { "historicoTurmaRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_HistoricoTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_HistoricoTurmaRel_ajuda"),
					new String[] { "historicoTurmaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_HistoricoTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_HistoricoTurmaRel_ajuda"),
					new String[] { "historicoTurmaCoordenadorRel.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Upload Professor Rel
	 *
	 */
	UPLOAD_PROFESSOR_REL("UploadProfessorRel",
			new PermissaoVisao[] { 
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_UploadProfessorRel_titulo"),
							UteisJSF.internacionalizar("per_UploadProfessorRel_ajuda"),	new String[] { "uploadProfessorRel.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_UploadProfessorRel_titulo"),
							UteisJSF.internacionalizar("per_UploadProfessorRel_ajuda"), new String[] { "uploadProfessorRelVisaoCoordenador.xhtml" })
					},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_PROFESSORES),
	/**
	* Diario Rel
	*
	*/
	DIARIO_REL("DiarioRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_DiarioRel_titulo"),UteisJSF.internacionalizar("per_DiarioRel_ajuda"), new String[]{"diarioRel.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_DiarioRel_titulo"),UteisJSF.internacionalizar("per_DiarioRel_ajuda"), new String[]{"diarioProfessorRel.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_DiarioRel_titulo"),UteisJSF.internacionalizar("per_DiarioRel_ajuda"), new String[]{"diarioCoordenadorRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	
	PERMITIR_GERAR_RELATORIO_DIARIO_RETROATIVO("PermitirGerarRelatorioDiarioRetroativo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioRetroativo_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioRetroativo_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioRetroativo_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioRetroativo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.DIARIO_REL, 
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	
	PERMITIR_GERAR_DIARIO_ASSINADO_COM_TODAS_AULAS_REGISTRADAS("PermitirGerarRelatorioDiarioComTodasAulasRegistradas", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioComTodasAulasRegistradas_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioComTodasAulasRegistradas_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioComTodasAulasRegistradas_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioComTodasAulasRegistradas_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioComTodasAulasRegistradas_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioComTodasAulasRegistradas_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.DIARIO_REL, 
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	
	PERMITIR_GERAR_RELATORIO_DIARIO_ASSINADO("PermitirGerarRelatorioDiarioAssinado", new PermissaoVisao[] {			
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioAssinado_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioAssinado_ajuda"), new String[]{"diarioRel.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioAssinado_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioAssinado_ajuda"), new String[]{"diarioRel.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioAssinado_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioDiarioAssinado_ajuda"), new String[]{"diarioRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.DIARIO_REL, 
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITIR_TRAZER_ALUNOS_TRANSFERENCIA_MATRIZ("PermitirTrazerAlunosTransferenciaMatriz", new PermissaoVisao[] {			
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirTrazerAlunosTransferenciaMatriz_titulo"),UteisJSF.internacionalizar("per_PermitirTrazerAlunosTransferenciaMatriz_ajuda"), new String[]{"diarioRel.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirTrazerAlunosTransferenciaMatriz_titulo"),UteisJSF.internacionalizar("per_PermitirTrazerAlunosTransferenciaMatriz_ajuda"), new String[]{"diarioRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.DIARIO_REL, 
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Horario Aluno Rel
	 *
	 */
	HORARIO_ALUNO_REL("HorarioAlunoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_HorarioAlunoRel_titulo"),
			UteisJSF.internacionalizar("per_HorarioAlunoRel_ajuda"), new String[] { "horarioAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CRONOGRAMA),
	/**
	 * Faltas Alunos Rel
	 *
	 */
	FALTAS_ALUNOS_REL("FaltasAlunosRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_FaltasAlunosRel_titulo"),
			UteisJSF.internacionalizar("per_FaltasAlunosRel_ajuda"), new String[] { "faltasAlunosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Ocorrencias Alunos Rel
	 *
	 */
	OCORRENCIAS_ALUNOS_REL("OcorrenciasAlunosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OcorrenciasAlunosRel_titulo"),
					UteisJSF.internacionalizar("per_OcorrenciasAlunosRel_ajuda"),
					new String[] { "ocorrenciasAlunosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Documentacao Pendente Professor Rel
	 *
	 */
	DOCUMENTACAO_PENDENTE_PROFESSOR_REL("DocumentacaoPendenteProfessorRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DocumentacaoPendenteProfessorRel_titulo"),
					UteisJSF.internacionalizar("per_DocumentacaoPendenteProfessorRel_ajuda"),
					new String[] { "documentacaoPendenteProfessorRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_PROFESSORES),
	/**
	 * Etiqueta Prova Rel
	 *
	 */
	ETIQUETA_PROVA_REL("EtiquetaProvaRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_EtiquetaProvaRel_titulo"),
			UteisJSF.internacionalizar("per_EtiquetaProvaRel_ajuda"), new String[] { "etiquetaProvaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Reposicao Rel
	 *
	 */
	REPOSICAO_REL("ReposicaoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ReposicaoRel_titulo"),
					UteisJSF.internacionalizar("per_ReposicaoRel_ajuda"), new String[] { "reposicaoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Alunos Reprovados Rel
	 *
	 */
	ALUNOS_REPROVADOS_REL("AlunosReprovadosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosReprovadosRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosReprovadosRel_ajuda"),
					new String[] { "alunosReprovadosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Registro Aula Lancada Nao Lancada Rel
	 *
	 */
	REGISTRO_AULA_LANCADA_NAO_LANCADA_REL("RegistroAulaLancadaNaoLancadaRel", new PermissaoVisao[] { 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_RegistroAulaLancadaNaoLancadaRel_titulo"),	UteisJSF.internacionalizar("per_RegistroAulaLancadaNaoLancadaRel_ajuda"), new String[] { "registroAulaLancadaNaoLancadaRel.xhtml" }), 
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_RegistroAulaLancadaNaoLancadaRel_titulo"),UteisJSF.internacionalizar("per_RegistroAulaLancadaNaoLancadaRel_ajuda"), new String[]{"registroAulaLancadaNaoLancadaRelProfessor.xhtml"}),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_RegistroAulaLancadaNaoLancadaRel_titulo"),UteisJSF.internacionalizar("per_RegistroAulaLancadaNaoLancadaRel_ajuda"), new String[]{"registroAulaLancadaNaoLancadaRelProfessor.xhtml"})
	},
	TipoPerfilAcessoPermissaoEnum.RELATORIO, 
	null, 
	PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),

	PERMITIR_GERAR_RELATORIO_REGISTRO_AULA_LANCADA_NAO_LANCADA_RETROATIVO("PermitirGerarRelatorioRegistroAulaNaoLancadaRetroativo", new PermissaoVisao[] {
		 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioRegistroAulaNaoResgistradaRetroativo_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioRegistroAulaNaoResgistradaRetroativo_ajuda")),
		 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirGerarRelatorioRegistroAulaNaoResgistradaRetroativo_titulo"),UteisJSF.internacionalizar("per_PermitirGerarRelatorioRegistroAulaNaoResgistradaRetroativo_ajuda"))
	},
	TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
	PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA_LANCADA_NAO_LANCADA_REL, 
	PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Merito Academico Rel
	 *
	 */
	MERITO_ACADEMICO_REL("MeritoAcademicoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MeritoAcademicoRel_titulo"),
					UteisJSF.internacionalizar("per_MeritoAcademicoRel_ajuda"),
					new String[] { "meritoAcademicoRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_MeritoAcademicoRel_titulo"),
					UteisJSF.internacionalizar("per_MeritoAcademicoRel_ajuda"),
					new String[] { "rankingNotasCoordenadorRel.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Certificado Curso Extensao Rel
	 *
	 */
	CERTIFICADO_CURSO_EXTENSAO_REL("CertificadoCursoExtensaoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CertificadoCursoExtensaoRel_titulo"),
					UteisJSF.internacionalizar("per_CertificadoCursoExtensaoRel_ajuda"),
					new String[] { "certificadoCursoExtensaoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	PERMITE_DEFINIR_REGRA_EMISSAO_CERTIFICADO("PermiteDefiniarRegraEmissaoCertificado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteDefinirRegraDeEmissaoCertificado_titulo"),
					UteisJSF.internacionalizar("per_PermiteDefinirRegraDeEmissaoCertificado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CERTIFICADO_CURSO_EXTENSAO_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Declaracao Cancelamento Matricula Rel
	 *
	 */
	DECLARACAO_CANCELAMENTO_MATRICULA_REL("DeclaracaoCancelamentoMatriculaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoCancelamentoMatriculaRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoCancelamentoMatriculaRel_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Envelope Requerimento Rel
	 *
	 */
	ENVELOPE_REQUERIMENTO_REL("EnvelopeRequerimentoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EnvelopeRequerimentoRel_titulo"),
					UteisJSF.internacionalizar("per_EnvelopeRequerimentoRel_ajuda"),
					new String[] { "envelopeRequerimentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Relatorio SEI Decidir Academico
	 *
	 */
	RELATORIO_SEIDECIDIR_ACADEMICO("RelatorioSEIDecidirAcademico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirAcademico_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirAcademico_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_ACADEMICO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirAcademicoApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_ACADEMICO,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Requerimento Rel
	 *
	 */
	REQUERIMENTO_REL("RequerimentoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoRel_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoRel_ajuda"),
					new String[] { "requerimentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	
	REQUERIMENTO_TCC_REL("RequerimentoTCCRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoTCCRel_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoTCCRel_ajuda"),
					new String[] { "requerimentoTCCRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Declaracao Frequencia Rel
	 *
	 */
	DECLARACAO_FREQUENCIA_REL("DeclaracaoFrequenciaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoFrequenciaRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoFrequenciaRel_ajuda"),
					new String[] { "declaracaoFrequenciaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Alunos Pro Uni Rel
	 *
	 */
	ALUNOS_PRO_UNI_REL("AlunosProUniRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosProUniRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosProUniRel_ajuda"),
					new String[] { "alunosProUniRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Alunos Por Unidade Curso Turma Rel
	 *
	 */
	ALUNOS_POR_UNIDADE_CURSO_TURMA_REL("AlunosPorUnidadeCursoTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlunosPorUnidadeCursoTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_AlunosPorUnidadeCursoTurmaRel_ajuda"),
					new String[] { "alunosPorUnidadeCursoTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Historico Aluno Rel
	 *
	 */
	HISTORICO_ALUNO_REL("HistoricoAlunoRel", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_HistoricoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_HistoricoAlunoRel_ajuda"),
					new String[] { "historicoAlunoRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_HistoricoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_HistoricoAlunoRel_ajuda"),
					new String[] { "historicoAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),	
	APRESENTAR_CAMPO_OBS_COMP_LAYOUT5("ApresentarCampoObsCompLayout5",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ApresentarCampoObsCompLayout5_titulo"),
					UteisJSF.internacionalizar("per_ApresentarCampoObsCompLayout5_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.HISTORICO_ALUNO_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	APRESENTAR_CAMPO_OBS_TRANSFERENCIA_MATRIZ_CURRICULAR("ApresentarObservacaoTransferenciaMatrizCurricular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ApresentarObservacaoTransferenciaMatrizCurricular_titulo"),
					UteisJSF.internacionalizar("per_ApresentarObservacaoTransferenciaMatrizCurricular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.HISTORICO_ALUNO_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITE_VISUALIZAR_ALTERAR_DATA_EXPEDICAO_DIPLOMA("PermiteVisualizarAlterarDataExpedicaoDiploma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteVisualizarAlterarDataExpedicaoDiploma_titulo"),
					UteisJSF.internacionalizar("per_PermiteVisualizarAlterarDataExpedicaoDiploma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.HISTORICO_ALUNO_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITE_VISUALIZAR_ALTERAR_DATA_EMISSAO_HISTORICO("PermiteVisualizarAlterarDataEmissaoHistorico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteVisualizarAlterarDataEmissaoHistorico_titulo"),
					UteisJSF.internacionalizar("per_PermiteVisualizarAlterarDataEmissaoHistorico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.HISTORICO_ALUNO_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITE_CADASTRAR_CONFIGURACAO_HISTORICO("PermitirCadastrarConfiguracaoHistorico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarConfiguracaoHistorico_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarConfiguracaoHistorico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.HISTORICO_ALUNO_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Disciplina Rel
	 *
	 */
	DISCIPLINA_REL("DisciplinaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DisciplinaRel_titulo"),
					UteisJSF.internacionalizar("per_DisciplinaRel_ajuda"), new String[] { "disciplinaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Declaracao Conclusao Curso Rel
	 *
	 */
	DECLARACAO_CONCLUSAO_CURSO_REL("DeclaracaoConclusaoCursoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoConclusaoCursoRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoConclusaoCursoRel_ajuda"),
					new String[] { "declaracaoConclusaoCursoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Ficha Atualizacao Cadastral Rel
	 *
	 */
	FICHA_ATUALIZACAO_CADASTRAL_REL("FichaAtualizacaoCadastralRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FichaAtualizacaoCadastralRel_titulo"),
					UteisJSF.internacionalizar("per_FichaAtualizacaoCadastralRel_ajuda"),
					new String[] { "fichaAtualizacaoCadastralRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Grade Curricular Aluno Rel
	 *
	 */
	GRADE_CURRICULAR_ALUNO_REL("GradeCurricularAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GradeCurricularAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_GradeCurricularAlunoRel_ajuda"),
					new String[] { "gradeCurricularAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Agenda Professor Rel
	 *
	 */
	AGENDA_PROFESSOR_REL("AgendaProfessorRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_AgendaProfessorRel_titulo"),
			UteisJSF.internacionalizar("per_AgendaProfessorRel_ajuda"), new String[] { "agendaProfessorRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CRONOGRAMA),
	PERMITIR_EMITIR_DECLARACAO_REL("PermitirEmitirDecaracaoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EmissaoDeclaracaoAulasMinistradas_titulo"),
					UteisJSF.internacionalizar("per_EmissaoDeclaracaoAulasMinistradas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.AGENDA_PROFESSOR_REL,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CRONOGRAMA),
	/**
	 * Email Turma Rel
	 *
	 */
	EMAIL_TURMA_REL("EmailTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EmailTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_EmailTurmaRel_ajuda"), new String[] { "emailTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Declaracao Transferencia Interna
	 *
	 */
	DECLARACAO_TRANSFERENCIA_INTERNA("DeclaracaoTransferenciaInterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoTransferenciaInterna_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoTransferenciaInterna_ajuda"),
					new String[] { "declaracaoTransferenciaInternaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Estatistica Academica Por Convenio Rel
	 *
	 */
	ESTATISTICA_ACADEMICA_POR_CONVENIO_REL("EstatisticaAcademicaPorConvenioRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstatisticaAcademicaPorConvenioRel_titulo"),
					UteisJSF.internacionalizar("per_EstatisticaAcademicaPorConvenioRel_ajuda"),
					new String[] { "estatisticaAcademicaPorConvenioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Horario Da Turma Rel
	 *
	 */
	HORARIO_DA_TURMA_REL("HorarioDaTurmaRel", new PermissaoVisao[] { 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_HorarioDaTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_HorarioDaTurmaRel_ajuda"), new String[] { "horarioDaTurmaRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_HorarioDaTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_HorarioDaTurmaRel_ajuda"), new String[] { "horarioDaTurmaRel.xhtml" })
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CRONOGRAMA),
	/**
	 * Mapa Nota Aluno Por Turma Rel
	 *
	 */
	MAPA_NOTA_ALUNO_POR_TURMA_REL("MapaNotaAlunoPorTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaNotaAlunoPorTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_MapaNotaAlunoPorTurmaRel_ajuda"),
					new String[] { "mapaNotaAlunoPorTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Livro Registro Rel
	 *
	 */
	LIVRO_REGISTRO_REL("LivroRegistroRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_LivroRegistroRel_titulo"),
			UteisJSF.internacionalizar("per_LivroRegistroRel_ajuda"), new String[] { "livroRegistroRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Livro Registro Rel
	 *
	 */
	PLANO_DISCIPLINA_REL("PlanoDisciplinaRel", 
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PlanoDisciplinaRel_titulo"),
							UteisJSF.internacionalizar("per_PlanoDisciplinaRel_ajuda"),
							new String[] { "planoDisciplinaRel.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PlanoDisciplinaRel_titulo"),
			UteisJSF.internacionalizar("per_PlanoDisciplinaRel_ajuda"),
			new String[] { "planoDisciplinaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Aniversariantes Do Mes Rel
	 *
	 */
	ANIVERSARIANTES_DO_MES_REL("AniversariantesDoMesRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AniversariantesDoMesRel_titulo"),
					UteisJSF.internacionalizar("per_AniversariantesDoMesRel_ajuda"),
					new String[] { "aniversariantesDoMesRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Perfil Turma Rel
	 *
	 */
	PERFIL_TURMA_REL("PerfilTurmaRel", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PerfilTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_PerfilTurmaRel_ajuda"), new String[] { "perfilTurmaRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PerfilTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_PerfilTurmaRel_ajuda"), new String[] { "perfilTurmaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PerfilTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_PerfilTurmaRel_ajuda"), new String[] { "perfilTurmaCoordenadorRel.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Ata Resultados Finais
	 *
	 */
	ATA_RESULTADOS_FINAIS("AtaResultadosFinais",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtaResultadosFinais_titulo"),
					UteisJSF.internacionalizar("per_AtaResultadosFinais_ajuda"),
					new String[] { "ataResultadosFinaisRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	PERMITE_CADASTRAR_CONFIGURACAO_ATA_RESULTADOS_FINAIS("PermitirCadastrarConfiguracaoAtaResultadosFinais",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarConfiguracaoAtaResultadosFinais_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarConfiguracaoAtaResultadosFinais_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ATA_RESULTADOS_FINAIS,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Cronograma De Aulas Rel
	 *
	 */
	CRONOGRAMA_DE_AULAS_REL("CronogramaDeAulasRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CronogramaDeAulasRel_titulo"),
					UteisJSF.internacionalizar("per_CronogramaDeAulasRel_ajuda"),
					new String[] { "cronogramaDeAulasRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_CronogramaDeAulasRel_titulo"),
					UteisJSF.internacionalizar("per_CronogramaDeAulasRel_ajuda"),
					new String[] { "cronogramaDeAulasRel.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CRONOGRAMA),
	
	PERMITIR_EMISSAO_TODAS_UNIDADES_CRONOGRAMA_DE_AULAS_REL("PermitirEmissaoTodasUnidadesCronogramaDeAulasRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirEmissaoTodasUnidadesCronogramaDeAulasRel_titulo"),
					UteisJSF.internacionalizar("per_PermitirEmissaoTodasUnidadesCronogramaDeAulasRel_ajuda"),
					new String[] { "cronogramaDeAulasRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_PermitirEmissaoTodasUnidadesCronogramaDeAulasRel_titulo"),
					UteisJSF.internacionalizar("per_PermitirEmissaoTodasUnidadesCronogramaDeAulasRel_ajuda"),
					new String[] { "cronogramaDeAulasRel.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CRONOGRAMA_DE_AULAS_REL, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CRONOGRAMA),
	
	
	/**
	 * Carta Aluno Rel
	 *
	 */
	CARTA_ALUNO_REL("CartaAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CartaAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_CartaAlunoRel_ajuda"), new String[] { "cartaAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES),
	/**
	 * Frequencia Aluno Rel
	 *
	 */
	FREQUENCIA_ALUNO_REL("FrequenciaAlunoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_FrequenciaAlunoRel_titulo"),
			UteisJSF.internacionalizar("per_FrequenciaAlunoRel_ajuda"), new String[] { "frequenciaAlunoRel.xhtml" }),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
						UteisJSF.internacionalizar("per_FrequenciaAlunoRel_titulo"),
						UteisJSF.internacionalizar("per_FrequenciaAlunoRel_ajuda"), new String[] { "frequenciaAlunoCoordenadorRel.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Processo Matricula Rel
	 *
	 */
	PROCESSO_MATRICULA_REL("ProcessoMatriculaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessoMatriculaRel_titulo"),
					UteisJSF.internacionalizar("per_ProcessoMatriculaRel_ajuda"),
					new String[] { "processoMatriculaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS),
	/**
	 * Atividade Complementar Rel
	 *
	 */
	ATIVIDADE_COMPLEMENTAR_REL("AtividadeComplementarRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtividadeComplementarRel_titulo"),
					UteisJSF.internacionalizar("per_AtividadeComplementarRel_ajuda"),
					new String[] { "atividadeComplementarRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	 * Mapa Equivalencia Matriz Curricular
	 *
	 */
	MAPA_EQUIVALENCIA_MATRIZ_CURRICULAR("MapaEquivalenciaMatrizCurricular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaEquivalenciaMatrizCurricular_titulo"),
					UteisJSF.internacionalizar("per_MapaEquivalenciaMatrizCurricular_ajuda"),
					new String[] { "mapaEquivalenciaMatrizCurricularCons.xhtml",
							"mapaEquivalenciaMatrizCurricularForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Area Conhecimento
	 *
	 */
	AREA_CONHECIMENTO("AreaConhecimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AreaConhecimento_titulo"),
					UteisJSF.internacionalizar("per_AreaConhecimento_ajuda"),
					new String[] { "areaConhecimentoCons.xhtml", "areaConhecimentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Criterio Avaliacao
	 *
	 */
	CRITERIO_AVALIACAO("CriterioAvaliacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CriterioAvaliacao_titulo"),
					UteisJSF.internacionalizar("per_CriterioAvaliacao_ajuda"),
					new String[] { "criterioAvaliacaoCons.xhtml", "criterioAvaliacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Plano Ensino
	 *
	 */
	PLANO_ENSINO("PlanoEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_PlanoEnsino_ajuda"),
					new String[] { "planoEnsinoCons.xhtml", "planoEnsinoForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_PlanoEnsino_ajuda"),
					new String[] { "planoEnsinoCons.xhtml", "planoEnsinoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	AUTORIZAR_PUBLICAR_PLANO_ENSINO("AutorizarPublicarPlanoEnsino",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_AutorizarPublicarPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_AutorizarPublicarPlanoEnsino_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_AutorizarPublicarPlanoEnsino_titulo"),
							UteisJSF.internacionalizar("per_AutorizarPublicarPlanoEnsino_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PLANO_ENSINO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	HABILITAR_CONTROLE_CALENDARIO_LANCAMENTO_PLANOENSINO("HabilitarControlePorCalendarioLancamentoPlanoEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_HabilitarControlePorCalendarioLancamentoPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_HabilitarControlePorCalendarioLancamentoPlanoEnsino_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PLANO_ENSINO,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	HABILITAR_CONTROLE_CALENDARIO_LANCAMENTO_PLANOENSINO_VISAO_PROFESSOR("HabilitarControlePorCalendarioLancamentoPlanoEnsinoVisaoProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_HabilitarControlePorCalendarioLancamentoPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_HabilitarControlePorCalendarioLancamentoPlanoEnsino_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	/**
	 * Campos Plano Ensino
	 *
	 */
	CAMPOS_PLANO_ENSINO("PerguntaPlanoEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PerguntaPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_PerguntaPlanoEnsino_ajuda"),
					new String[] { "perguntaCons.xhtml", "perguntaForm.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Formulario Plano Ensino
	 *
	 */
	FORMULARIO_PLANO_ENSINO("FormularioPlanoEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FormularioPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_FormularioPlanoEnsino_ajuda"),
					new String[] { "questionarioCons.xhtml", "questionarioForm.xhtml" })},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Local Aula
	 *
	 */
	LOCAL_AULA("LocalAula", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_LocalAula_titulo"), UteisJSF.internacionalizar("per_LocalAula_ajuda"),
			new String[] { "localSalaCons.xhtml", "localSalaForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Nota Conceito Indicador Avaliacao
	 *
	 */
	NOTA_CONCEITO_INDICADOR_AVALIACAO("NotaConceitoIndicadorAvaliacao", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_NotaConceitoIndicadorAvaliacao_titulo"),
			UteisJSF.internacionalizar("per_NotaConceitoIndicadorAvaliacao_ajuda"),
			new String[] { "notaConceitoIndicadorAvaliacaoCons.xhtml", "notaConceitoIndicadorAvaliacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Forum
	 *
	 */
	FORUM("Forum",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Forum_titulo"), UteisJSF.internacionalizar("per_Forum_ajuda"),
					new String[] { "forumCons.xhtml", "forumForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	ATIVAR_INATIVAR_FORUM("AtivarInativarForum",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtivarInativarForum_titulo"),
					UteisJSF.internacionalizar("per_AtivarInativarForum_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.FORUM,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	ALTERAR_TEMA_FORUM("AlterarTemaForum",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarTemaForum_titulo"),
					UteisJSF.internacionalizar("per_AlterarTemaForum_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.FORUM,
			PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Turno
	 *
	 */
	TURNO("Turno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turno_titulo"), UteisJSF.internacionalizar("per_Turno_ajuda"),
					new String[] { "turnoCons.xhtml", "turnoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Titulacao Curso
	 *
	 */
	TITULACAO_CURSO("TitulacaoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TitulacaoCurso_titulo"),
					UteisJSF.internacionalizar("per_TitulacaoCurso_ajuda"),
					new String[] { "titulacaoCursoCons.xhtml", "titulacaoCursoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),
	/**
	 * Registro Presenca Colacao Grau
	 *
	 */
	REGISTRO_PRESENCA_COLACAO_GRAU("RegistroPresencaColacaoGrau",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroPresencaColacaoGrau_titulo"),
					UteisJSF.internacionalizar("per_RegistroPresencaColacaoGrau_ajuda"),
					new String[] { "registroPresencaColacaoGrauForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	/**
	 * Programacao Formatura
	 *
	 */
	PROGRAMACAO_FORMATURA("ProgramacaoFormatura",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProgramacaoFormatura_titulo"),
					UteisJSF.internacionalizar("per_ProgramacaoFormatura_ajuda"),
					new String[] { "programacaoFormaturaCons.xhtml", "programacaoFormaturaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	/**
	 * Expedicao Diploma
	 *
	 */
	EXPEDICAO_DIPLOMA("ExpedicaoDiploma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExpedicaoDiploma_titulo"),
					UteisJSF.internacionalizar("per_ExpedicaoDiploma_ajuda"),
					new String[] { "expedicaoDiplomaCons.xhtml", "expedicaoDiplomaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	PERMITE_ALTERAR_UNIDADE_ENSINO_CERTIFICADORA("PermiteAlterarUnidadeEnsinoCertificadora",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarUnidadeEnsinoCertificadora_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarUnidadeEnsinoCertificadora_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.EXPEDICAO_DIPLOMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	PERMITE_DEFINIR_REGRA_EMISSAO_DIPLOMA("PermiteDefinirRegraEmissaoDiploma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteDefinirRegraDeEmissaoDiploma_titulo"),
					UteisJSF.internacionalizar("per_PermiteDefinirRegraDeEmissaoDiploma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.EXPEDICAO_DIPLOMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	/**
	 * Observacao Complementar
	 *
	 */
	OBSERVACAO_COMPLEMENTAR("ObservacaoComplementar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ObservacaoComplementar_titulo"),
					UteisJSF.internacionalizar("per_ObservacaoComplementar_ajuda"),
					new String[] { "observacaoComplementarCons.xhtml", "observacaoComplementarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	/**
	 * Colacao Grau
	 *
	 */
	COLACAO_GRAU("ColacaoGrau", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ColacaoGrau_titulo"), UteisJSF.internacionalizar("per_ColacaoGrau_ajuda"),
			new String[] { "colacaoGrauCons.xhtml", "colacaoGrauForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	/**
	 * Controle Livro Registro Diploma
	 *
	 */
	CONTROLE_LIVRO_REGISTRO_DIPLOMA("ControleLivroRegistroDiploma", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ControleLivroRegistroDiploma_titulo"),
			UteisJSF.internacionalizar("per_ControleLivroRegistroDiploma_ajuda"),
			new String[] { "controleLivroRegistroDiplomaCons.xhtml", "controleLivroRegistroDiplomaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	PERMITIR_REABRIR_LIVRO_FECHADO("PermitirReabrirLivroFechado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirReabrirLivroFechado_titulo"),
					UteisJSF.internacionalizar("per_PermitirReabrirLivroFechado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.CONTROLE_LIVRO_REGISTRO_DIPLOMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	/**
	 * Setransp
	 *
	 */
	SETRANSP("Setransp",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Setransp_titulo"), UteisJSF.internacionalizar("per_Setransp_ajuda"),
					new String[] { "setranspCons.xhtml", "setranspForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_GERACAO_ARQUIVOS),
	/**
	 * Censo
	 *
	 */
	CENSO("Censo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Censo_titulo"), UteisJSF.internacionalizar("per_Censo_ajuda"),
					new String[] { "censoCons.xhtml", "censoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_GERACAO_ARQUIVOS),
	/**
	 * Tipo Categoria
	 *
	 */
	TIPO_CATEGORIA("TipoCategoria",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoCategoria_titulo"),
					UteisJSF.internacionalizar("per_TipoCategoria_ajuda"),
					new String[] { "tipoCategoriaCons.xhtml", "tipoCategoriaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Mapa Local Aula Turma
	 *
	 */
	MAPA_LOCAL_AULA_TURMA("MapaLocalAulaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaLocalAulaTurma_titulo"),
					UteisJSF.internacionalizar("per_MapaLocalAulaTurma_ajuda"),
					new String[] { "mapaLocalAulaTurmaCons.xhtml", "mapaLocalAulaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	MAPA_LOCAL_AULA_TURMA_PERMITE_MODIFICAR_DADOS("MapaLocalAulaTurma_PermiteModificarDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaLocalAulaTurmaPermiteModificarDados_titulo"),
					UteisJSF.internacionalizar("per_MapaLocalAulaTurmaPermiteModificarDados_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_LOCAL_AULA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Distribuicao Subturma
	 *
	 */
	DISTRIBUICAO_SUBTURMA("DistribuicaoSubturma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DistribuicaoSubturma_titulo"),
					UteisJSF.internacionalizar("per_DistribuicaoSubturma_ajuda"),
					new String[] { "distribuicaoSubturmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Mapa De Turmas Encerradas Rel
	 *
	 */
	MAPA_DE_TURMAS_ENCERRADAS_REL("MapaDeTurmasEncerradasRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaDeTurmasEncerradasRel_titulo"),
					UteisJSF.internacionalizar("per_MapaDeTurmasEncerradasRel_ajuda"),
					new String[] { "mapaDeTurmasEncerradasRelCons.xhtml", "mapaDeTurmasEncerradasRelForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Upload Back Up
	 *
	 */
	UPLOAD_BACK_UP("UploadBackUp", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_UploadBackUp_titulo"), UteisJSF.internacionalizar("per_UploadBackUp_ajuda"),
			new String[] { "uploadArquivoBackUp.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Mapa Aluno Apto Formar
	 *
	 */
	MAPA_ALUNO_APTO_FORMAR("MapaAlunoAptoFormar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaAlunoAptoFormar_titulo"),
					UteisJSF.internacionalizar("per_MapaAlunoAptoFormar_ajuda"),
					new String[] { "mapaAlunoAptoFormarCons.xhtml", "mapaAlunoAptoFormarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Solicitar Abertura Turma
	 *
	 */
	SOLICITAR_ABERTURA_TURMA("SolicitarAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("Menu_SolicitarAberturaTurma"),
					UteisJSF.internacionalizar("per_SolicitarAberturaTurma_ajuda"),
					new String[] { "solicitarAberturaTurmaCons.xhtml", "solicitarAberturaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	NAO_AUTORIZAR_SOLICITACAO_ABERTURA_TURMA("NaoAutorizarSolicitacaoAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NaoAutorizarSolicitacaoAberturaTurma_titulo"),
					UteisJSF.internacionalizar("per_NaoAutorizarSolicitacaoAberturaTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.SOLICITAR_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_REVISAR_SOLICITACAO_ABERTURA_TURMA("PermiteRevisarSolicitacaoAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteRevisarSolicitacaoAberturaTurma_titulo"),
					UteisJSF.internacionalizar("per_PermiteRevisarSolicitacaoAberturaTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.SOLICITAR_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	REVISAR_SOLICITACAO_ABERTURA_TURMA("RevisarSolicitacaoAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RevisarSolicitacaoAberturaTurma_titulo"),
					UteisJSF.internacionalizar("per_RevisarSolicitacaoAberturaTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.SOLICITAR_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	FINALIZAR_SOLICITACAO_ABERTURA_TURMA("FinalizarSolicitacaoAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FinalizarSolicitacaoAberturaTurma_titulo"),
					UteisJSF.internacionalizar("per_FinalizarSolicitacaoAberturaTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.SOLICITAR_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	AUTORIZAR_SOLICITACAO_ABERTURA_TURMA("AutorizarSolicitacaoAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AutorizarSolicitacaoAberturaTurma_titulo"),
					UteisJSF.internacionalizar("per_AutorizarSolicitacaoAberturaTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.SOLICITAR_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Log Turma
	 *
	 */
	LOG_TURMA("LogTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LogTurma_titulo"), UteisJSF.internacionalizar("per_LogTurma_ajuda"),
					new String[] { "logTurmaCons.xhtml", "logTurma.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Categoria Turma
	 *
	 */
	CATEGORIA_TURMA("CategoriaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CategoriaTurma_titulo"),
					UteisJSF.internacionalizar("per_CategoriaTurma_ajuda"),
					new String[] { "categoriaTurmaCons.xhtml", "categoriaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Programacao Aula
	 *
	 */
	PROGRAMACAO_AULA("ProgramacaoAula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProgramacaoAula_titulo"),
					UteisJSF.internacionalizar("per_ProgramacaoAula_ajuda"),
					new String[] { "programacaoAulaCons.xhtml", "programacaoAulaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_LIBERAR_PROGRAMACAO_AULA_PROFESSOR_ACIMA_PERMITIDO("PermiteLiberarProgramacaoAulaProfessorAcimaPermitido",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteLiberarProgramacaoAulaProfessorAcimaPermitido_titulo"),
					UteisJSF.internacionalizar("per_PermiteLiberarProgramacaoAulaProfessorAcimaPermitido_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_ALTERAR_EXCLUIR_AULAS_CADASTRADAS_APENAS_PELO_PROPRIO_USUARIO(
			"PermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario_titulo"),
					UteisJSF.internacionalizar(
							"per_PermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),

	PERMITE_INCLUIR_FUNCIONARIO_CARGO("PermiteInformarFuncionarioCargo", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteInformarFuncionarioCargo_titulo"),
					UteisJSF.internacionalizar("per_PermiteInformarFuncionarioCargo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),

	PERMITE_INFORMAR_FUNCIONARIO_CARGO_AGENDA_PROFESSOR("PermiteInformarFuncionarioCargoAgenda", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteInformarFuncionarioCargoAgendaProfessor_titulo"),
			UteisJSF.internacionalizar("per_PermiteInformarFuncionarioCargoAgendaProfessor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.AGENDA_PROFESSOR_REL,
			PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	PERMITE_PROGRAMAR_AULA_DISCIPLINA_ONLINE_TIPO_TUTORIA_DINAMICA(
			"PermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamicao_titulo"),
					UteisJSF.internacionalizar(
							"per_PermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	
	PERMITE_LIBERAR_PROGRAMACAO_AULA_COM_CHOQUE_HORARIO("PermiteLiberarProgramacaoAulaComChoqueHorario",new PermissaoVisao[] { 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_PermiteLiberarProgramacaoAulaComChoqueHorario_titulo"),UteisJSF.internacionalizar("per_PermiteLiberarProgramacaoAulaComChoqueHorario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	
	PERMITE_PROGRAMACAO_AULA_COM_GOOGLE_MEET("PermiteProgramacaoAulaComGoogleMeet",new PermissaoVisao[] { 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_PermiteProgramacaoAulaComGoogleMeet_titulo"),UteisJSF.internacionalizar("per_PermiteProgramacaoAulaComGoogleMeet_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	
	PERMITE_PROGRAMACAO_AULA_COM_CLASSROOM("PermiteProgramacaoAulaComClassroom",new PermissaoVisao[] { 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_PermiteProgramacaoAulaComClassroom_titulo"),UteisJSF.internacionalizar("per_PermiteProgramacaoAulaComClassroom_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	
	PERMITE_PROGRAMACAO_AULA_COM_BLACKBOARD("PermiteProgramacaoAulaComBlackboard",new PermissaoVisao[] { 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_PermiteProgramacaoAulaComBlackboard_titulo"),UteisJSF.internacionalizar("per_PermiteProgramacaoAulaComBlackboard_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROGRAMACAO_AULA, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Vaga Turma
	 *
	 */
	VAGA_TURMA("VagaTurma", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_VagaTurma_titulo"), UteisJSF.internacionalizar("per_VagaTurma_ajuda"),
			new String[] { "vagaTurmaCons.xhtml", "vagaTurmaForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Integracao Mestre GR
	 */
	INTEGRACAO_MESTRE_GR("IntegracaoMestreGR", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_IntegracaoMestreGR_titulo"), UteisJSF.internacionalizar("per_IntegracaoMestreGR_ajuda"),
			new String[] { "integracaoMestreGRForm.xhtml"}) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_CONFIGURACAO_INTEGRACAO("PermiteConfigurarIntegracaoMestreGR",new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_IntegracaoMestreGR_titulo_funcionalidade"),UteisJSF.internacionalizar("per_IntegracaoMestreGR_ajuda_funcionalidade")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.INTEGRACAO_MESTRE_GR, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Turma
	 *
	 */
	TURMA("Turma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_titulo"), UteisJSF.internacionalizar("per_Turma_ajuda"),
					new String[] { "turmaCons.xhtml", "turmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_DEFINIR_DATA_BASE_GERACAO_PARCELA("Turma_definirDataBaseGeracaoParcela",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TurmadefinirDataBaseGeracaoParcela_titulo"),
					UteisJSF.internacionalizar("per_TurmadefinirDataBaseGeracaoParcela_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_PERMITE_ALTERAR_DADOS_EAD("Turma_permiteAlterarDadosEAD",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_permiteAlterarDadosEAD_titulo"),
					UteisJSF.internacionalizar("per_Turma_permiteAlterarDadosEAD_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_ALTERAR_CONDICAO_PAGAMENTO_ALUNO("Turma_alterarCondicaoPagamentoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_alterarCondicaoPagamentoAluno_titulo"),
					UteisJSF.internacionalizar("per_Turma_alterarCondicaoPagamentoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_PERMITE_ACESSO_ABA_FINANCEIRO("Turma_permiteAcessoAbaFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_permiteAcessoAbaFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_Turma_permiteAcessoAbaFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_PERMITE_ALTERAR_CONFIGURACAO_ACADEMICA("Turma_permiteAlterarConfiguracaoAcademica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_permiteAlterarConfiguracaoAcademica_titulo"),
					UteisJSF.internacionalizar("per_Turma_permiteAlterarConfiguracaoAcademica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_PERMITE_ALTERAR_MATRIZ_CURRICULAR_CURSO_INTEGRAL("Turma_permiteAlterarMatrizCurricularCursoIntegral",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_permiteAlterarMatrizCurricularCursoIntegral_titulo"),
					UteisJSF.internacionalizar("per_Turma_permiteAlterarMatrizCurricularCursoIntegral_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_PERMITIR_AGRUPAR_TURMAS_UNIDADE_ENSINO_DIFERENTE("Turma_permitirAgruparTurmasUnidadeEnsinoDiferente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_permitirAgruparTurmasUnidadeEnsinoDiferente_titulo"),
					UteisJSF.internacionalizar("per_Turma_permitirAgruparTurmasUnidadeEnsinoDiferente_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	TURMA_PERMITE_ALTERAR_UNIDADE_ENSINO_TURMA_INTEGRAL("Turma_permiteAlterarUnidadeEnsinoturmaIntegral",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Turma_permiteAlterarUnidadeEnsinoTurmaIntegral_titulo"),
					UteisJSF.internacionalizar("per_Turma_permiteAlterarUnidadeEnsinoTurmaIntegral_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Upload Arquivos Comuns
	 *
	 */
	UPLOAD_ARQUIVOS_COMUNS("UploadArquivosComuns",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_UploadArquivosComuns_titulo"),
					UteisJSF.internacionalizar("per_UploadArquivosComuns_ajuda"),
					new String[] { "uploadArquivosComuns.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * Requerimento
	 *
	 */
	REQUERIMENTO("Requerimento", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Requerimento_titulo"),
					UteisJSF.internacionalizar("per_Requerimento_ajuda"),
					new String[] { "requerimentoCons.xhtml", "requerimentoForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Requerimento_titulo"),
					UteisJSF.internacionalizar("per_Requerimento_ajuda"),
					new String[] { "requerimentoCoordenadorCons.xhtml", "requerimentoCoordenadorForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Requerimento_titulo"),
					UteisJSF.internacionalizar("per_Requerimento_ajuda"),
					new String[] { "requerimentoProfessorCons.xhtml", "requerimentoProfessorForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_RequerimentoAluno_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoAluno_ajuda"),
					new String[] { "requerimentoAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_RequerimentoAluno_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoAluno_ajuda"),
					new String[] { "requerimentoAluno.xhtml" })
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_CONSULTAR_TODAS_UNIDADES("Requerimento_consultarTodasUnidades",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoconsultarTodasUnidades_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoconsultarTodasUnidades_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_APRESENTAR_BOTAO_APROVEITAMENTO("Requerimento_ApresentarBotaoAproveitamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoApresentarBotaoAproveitamento_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoApresentarBotaoAproveitamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_APRESENTAR_BOTAO_TRANSF_EXTERNA("Requerimento_ApresentarBotaoTransfExterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoApresentarBotaoTransfExterna_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoApresentarBotaoTransfExterna_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_CONSULTAR_REQUERIMENTO_OUTRO_DEPARTAMENTO_RESPONSAVEL(
			"Requerimento_consultarRequerimentoOutroDepartamentoResponsavel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_RequerimentoconsultarRequerimentoOutroDepartamentoResponsavel_titulo"),
					UteisJSF.internacionalizar(
							"per_RequerimentoconsultarRequerimentoOutroDepartamentoResponsavel_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	VISUALIZAR_REQUERINENTO_OUTRO_DEPARTAMENTO_TRAMITE("Visualizar_Requerinento_Outro_Departamento_Tramite",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_VisualizarRequerinentoOutroDepartamentoTramite_titulo"),
					UteisJSF.internacionalizar("per_VisualizarRequerinentoOutroDepartamentoTramite_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITE_REALIZAR_TRAMITE_REQUERIMENTO_OUTRO_DEPARTAMENTO(
			"Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_RequerimentopermiteRealizarTramiteRequerimentoOutroDepartamento_titulo"),
					UteisJSF.internacionalizar(
							"per_RequerimentopermiteRealizarTramiteRequerimentoOutroDepartamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_CONSULTAR_REQUERIMENTO_OUTROS_CONSULTORES_RESPONSAVEIS(
			"Requerimento_consultarRequerimentoOutrosConsultoresResponsaveis",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_RequerimentoconsultarRequerimentoOutrosConsultoresResponsaveis_titulo"),
					UteisJSF.internacionalizar(
							"per_RequerimentoconsultarRequerimentoOutrosConsultoresResponsaveis_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	PERMITE_ALTERAR_FUNCIONARIO_RESPONSAVEL("PermiteAlterarFuncionarioResponsavel",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteAlterarFuncionarioResponsavel_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarFuncionarioResponsavel_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermiteAlterarFuncionarioResponsavel_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarFuncionarioResponsavel_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermiteAlterarFuncionarioResponsavel_titulo"),
							UteisJSF.internacionalizar("per_PermiteAlterarFuncionarioResponsavel_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	PERMITIR_INFORMAR_DESCONTO("PermitirInformarDesconto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirInformarDesconto_titulo"),
					UteisJSF.internacionalizar("per_PermitirInformarDesconto_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	PERMITIR_INFORMAR_ACRESCIMO("PermitirInformarAcrescimo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirInformarAcrescimo_titulo"),
					UteisJSF.internacionalizar("per_PermitirInformarAcrescimo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	PERMITIR_VOLTAR_REQUERIMENTO_EXECUCAO("PermitirVoltarRequerimentoExecucao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVoltarRequerimentoExecucao_titulo"),
					UteisJSF.internacionalizar("per_PermitirVoltarRequerimentoExecucao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	PERMITIR_ENVIAR_DEPARTAMENTO_ANTERIOR("Requerimento_PermitirEnviarDepartamentoAnterior",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirEnviarRequerimentoDepartamentoAnterior_titulo"),
							UteisJSF.internacionalizar("per_PermitirEnviarRequerimentoDepartamentoAnterior_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirEnviarRequerimentoDepartamentoAnterior_titulo"),
							UteisJSF.internacionalizar("per_PermitirEnviarRequerimentoDepartamentoAnterior_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirEnviarRequerimentoDepartamentoAnterior_titulo"),
							UteisJSF.internacionalizar("per_PermitirEnviarRequerimentoDepartamentoAnterior_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	REQUERIMENTO_PERMITIR_DEFERIR("Requerimento_PermitirDeferir",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_Requerimento_PermitirDeferir_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirDeferir_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirDeferir_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirDeferir_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirDeferir_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirDeferir_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_INDEFERIR("Requerimento_PermitirIndeferir",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_Requerimento_PermitirIndeferir_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirIndeferir_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirIndeferir_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirIndeferir_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirIndeferir_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirIndeferir_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_IMPRIMIR_COMPROVANTE("Requerimento_PermitirImprimirComprovante",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirComprovante_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirComprovante_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirComprovante_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirComprovante_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirComprovante_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirComprovante_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	REQUERIMENTO_PERMITIR_IMPRIMIR_REQUERIMENTO("Requerimento_PermitirImprimirRequerimento",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirRequerimento_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirRequerimento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirRequerimento_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirRequerimento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirRequerimento_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_PermitirImprimirRequerimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	REQUERIMENTO_APRESENTAR_INICIAR_REQUERIMENTO("Requerimento_ApresentarIniciarRequerimento",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_Requerimento_ApresentarIniciarRequerimento_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_ApresentarIniciarRequerimento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_Requerimento_ApresentarIniciarRequerimento_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_ApresentarIniciarRequerimento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_Requerimento_ApresentarIniciarRequerimento_titulo"),
							UteisJSF.internacionalizar("per_Requerimento_ApresentarIniciarRequerimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	REQUERIMENTO_PERMITIR_AUTORIZAR_PAGAMENTO("RequerimentoPermitirAutorizarPagamento",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_RequerimentoPermitirAutorizarPagamento_titulo"),
							UteisJSF.internacionalizar("per_RequerimentoPermitirAutorizarPagamento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_RequerimentoPermitirAutorizarPagamento_titulo"),
							UteisJSF.internacionalizar("per_RequerimentoPermitirAutorizarPagamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_DEFERIR_INDEFERIR_SOLICITACAO_ISENCAO_TAXA(
			"Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa_titulo"),
							UteisJSF.internacionalizar(
									"per_Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa_titulo"),
							UteisJSF.internacionalizar(
									"per_Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa_titulo"),
							UteisJSF.internacionalizar(
									"per_Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_CONSULTAR_INCLUIR_REQUERIMENTO_APENAS_PROPRIO_USUARIO("RequerimentoPermitirConsultarIncluirRequerimentoApenasProprioUsuario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoPermitirConsultarIncluirRequerimentoApenasProprioUsuario_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoPermitirConsultarIncluirRequerimentoApenasProprioUsuario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_ALTERAR_OBSERVACAO_INCLUIDA_PELO_REQUERENTE("RequerimentoPermitirAlterarObservacaoIncluidaPeloRequerente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RequerimentoPermitirAlterarObservacaoIncluidaPeloRequerente_titulo"),
					UteisJSF.internacionalizar("per_RequerimentoPermitirAlterarObservacaoIncluidaPeloRequerente_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_REQUERENTE_ANEXAR_ARQUIVO(
			"RequerimentoPermitirRequerenteAnexarArquivo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteAnexarArquivo_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_REQUERENTE_VISUALIZAR_TRAMITE(
			"RequerimentoPermitirRequerenteVisualizarTramite",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteVisualizarTramite_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_PERMITIR_REQUERENTE_INTERAGIR_TRAMITE(
			"RequerimentoPermitirRequerenteInteragirTramite",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_titulo"),
							UteisJSF.internacionalizar(
									"per_RequerimentoPermitirRequerenteInteragirTramite_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_CONSULTAR_REQUERIMENTO_OUTRO_DEPARTAMENTO_MESMO_TRAMITE_TODAS_UNIDADES(
			"Requerimento_consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_Requerimento_consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades_titulo"),
					UteisJSF.internacionalizar(
							"per_Requerimento_consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	REQUERIMENTO_CONSULTAR_REQUERIMENTO_OUTROS_RESPONSAVEIS_MESMO_DEPARTAMENTO_TODAS_UNIDADES(
			"Requerimento_consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_Requerimento_consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades_titulo"),
					UteisJSF.internacionalizar(
							"per_Requerimento_consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	/**
	 * Transferencia Interna
	 *
	 */
	TRANSFERENCIA_INTERNA("TransferenciaInterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TransferenciaInterna_titulo"),
					UteisJSF.internacionalizar("per_TransferenciaInterna_ajuda"),
					new String[] { "transferenciaInternaCons.xhtml", "transferenciaInternaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	PERMITE_ESTORNAR_TRANSFERENCIA_INTERNA("PermiteEstornarTransferenciaInterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteEstornarTransferenciaInterna_titulo"),
					UteisJSF.internacionalizar("per_PermiteEstornarTransferenciaInterna_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRANSFERENCIA_INTERNA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	PERMITE_REALIZAR_TRANSFERENCIA_INTERNA_MESMO_CURSO_MATRICULA_INTEGRAL("PermiteRealizarTransferenciaInternaMesmoCursoMatriculaIntegral",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteRealizarTransferenciaInternaMesmoCursoMatriculaIntegral_titulo"),
					UteisJSF.internacionalizar("per_PermiteRealizarTransferenciaInternaMesmoCursoMatriculaIntegral_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRANSFERENCIA_INTERNA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Controle Correspondencia
	 *
	 */
	CONTROLE_CORRESPONDENCIA("ControleCorrespondencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleCorrespondencia_titulo"),
					UteisJSF.internacionalizar("per_ControleCorrespondencia_ajuda"),
					new String[] { "controleCorrespondenciaCons.xhtml", "controleCorrespondenciaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Transferencia Entrada
	 *
	 */
	TRANSFERENCIA_ENTRADA("TransferenciaEntrada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TransferenciaEntrada_titulo"),
					UteisJSF.internacionalizar("per_TransferenciaEntrada_ajuda"),
					new String[] { "transferenciaEntradaCons.xhtml", "transferenciaEntradaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Pergunta Requerimento
	 *
	 */
	PERGUNTA_REQUERIMENTO("PerguntaRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PerguntaRequerimento_titulo"),
					UteisJSF.internacionalizar("per_PerguntaRequerimento_ajuda"),
					new String[] { "perguntaCons.xhtml", "perguntaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	
	RELATORIO_SEIDECIDIR_REQUERIMENTO("RelatorioSEIDecidirRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirRequerimento_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirRequerimento_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_SEI_DECIDIR_REQUERIMENTO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_REQUERIMENTO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirRequerimentoApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_REQUERIMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	/**
	/**
	 * Pergunta Requerimento
	 *
	 */
	QUESTIONARIO_REQUERIMENTO("QuestionarioRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuestionarioRequerimento_titulo"),
					UteisJSF.internacionalizar("per_QuestionarioRequerimento_ajuda"),
					new String[] { "questionarioCons.xhtml", "questionarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Alterar Responsavel Requerimento
	 *
	 */
	ALTERAR_RESPONSAVEL_REQUERIMENTO("AlterarResponsavelRequerimento", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AlterarResponsavelRequerimento_titulo"),
			UteisJSF.internacionalizar("per_AlterarResponsavelRequerimento_ajuda"),
			new String[] { "alterarResponsavelRequerimentoCons.xhtml", "alterarResponsavelRequerimentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Calendario Abertura Requerimento
	 *
	 */
	CALENDARIO_ABERTURA_REQUERIMENTO("CalendarioAberturaRequerimento", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CalendarioAberturaRequerimento_titulo"),
			UteisJSF.internacionalizar("per_CalendarioAberturaRequerimento_ajuda"),
			new String[] { "calendarioAberturaRequerimentoCons.xhtml", "calendarioAberturaRequerimentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Tipo Requerimento
	 *
	 */
	TIPO_REQUERIMENTO("TipoRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoRequerimento_titulo"),
					UteisJSF.internacionalizar("per_TipoRequerimento_ajuda"),
					new String[] { "tipoRequerimentoCons.xhtml", "tipoRequerimentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Transferencia Saida
	 *
	 */
	TRANSFERENCIA_SAIDA("TransferenciaSaida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TransferenciaSaida_titulo"),
					UteisJSF.internacionalizar("per_TransferenciaSaida_ajuda"),
					new String[] { "transferenciaSaidaCons.xhtml", "transferenciaSaidaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	TRANS_SAIDA_PERMITE_ALTERAR_DATA("TransSaida_permiteAlterarData",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TransSaidaPermiteAlterarData_titulo"),
					UteisJSF.internacionalizar("per_TransSaidaPermiteAlterarData_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRANSFERENCIA_SAIDA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	PERMITE_ESTORNAR_TRANSFERENCIA_SAIDA("PermiteEstornarTransferenciaSaida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteEstornarTransferenciaSaida_titulo"),
					UteisJSF.internacionalizar("per_PermiteEstornarTransferenciaSaida_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRANSFERENCIA_SAIDA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),

	/**
	 * Transferencia Matriz Curricular
	 *
	 */
	TRANSFERENCIA_MATRIZ_CURRICULAR("TransferenciaMatrizCurricular", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_TransferenciaMatrizCurricular_titulo"),
			UteisJSF.internacionalizar("per_TransferenciaMatrizCurricular_ajuda"),
			new String[] { "transferenciaMatrizCurricularCons.xhtml", "transferenciaMatrizCurricularForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Transferencia Turma
	 *
	 */
	TRANSFERENCIA_TURMA("TransferenciaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TransferenciaTurma_titulo"),
					UteisJSF.internacionalizar("per_TransferenciaTurma_ajuda"),
					new String[] { "transferenciaTurmaCons.xhtml", "transferenciaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	TRANSFERENCIA_TURMA_PERMITIR_INCLUIR_DISCIPLINA_SEM_AULA_PROGRAMADA_EM_CURSOS_INTEGRAIS(
			"TransferenciaTurmaPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_TransferenciaTurmaPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais_titulo"),
					UteisJSF.internacionalizar(
							"per_TransferenciaTurmaPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.TRANSFERENCIA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Transferencia Turno
	 *
	 */
	TRANSFERENCIA_TURNO("TransferenciaTurno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TransferenciaTurno_titulo"),
					UteisJSF.internacionalizar("per_TransferenciaTurno_ajuda"),
					new String[] { "transferenciaTurnoCons.xhtml", "transferenciaTurnoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TRANSFERENCIAS_TRANCAMENTO),
	/**
	 * Log Matricula
	 *
	 */
	LOG_MATRICULA("LogMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LogMatricula_titulo"),
					UteisJSF.internacionalizar("per_LogMatricula_ajuda"), new String[] { "logMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Consultor Matricula
	 *
	 */
	CONSULTOR_MATRICULA("ConsultorMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConsultorMatricula_titulo"),
					UteisJSF.internacionalizar("per_ConsultorMatricula_ajuda"),
					new String[] { "consultorResponsavelMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	/**
	 * Inclusao Exclusao Disciplina
	 *
	 */
	INCLUSAO_EXCLUSAO_DISCIPLINA("InclusaoExclusaoDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InclusaoExclusaoDisciplina_titulo"),
					UteisJSF.internacionalizar("per_InclusaoExclusaoDisciplina_ajuda"),
					new String[] { "inclusaoExclusaoDisciplinaMatriculaCons.xhtml",
							"inclusaoExclusaoDisciplinaMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	DESCONTO_INCLUSAO_REPOSICAO_FORA_PRAZO("DescontoInclusaoReposicaoForaPrazo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DescontoInclusaoReposicaoForaPrazo_titulo"),
					UteisJSF.internacionalizar("per_DescontoInclusaoReposicaoForaPrazo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUSAO_EXCLUSAO_DISCIPLINA_PERMITIR_EXCLUIR_DISCIPLINA("InclusaoExclusaoDisciplina_permitirExcluirDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InclusaoExclusaoDisciplinapermitirExcluirDisciplina_titulo"),
					UteisJSF.internacionalizar("per_InclusaoExclusaoDisciplinapermitirExcluirDisciplina_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUSAO_EXCLUSAO_DISCIPLINA_PERMITIR_INCLUIR_DISCIPLINA_CHOQUE_HORARIO(
			"InclusaoExclusaoDisciplina_permitirIncluirDisciplinaChoqueHorario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaChoqueHorario_titulo"),
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaChoqueHorario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUSAO_EXCLUSAO_DISCIPLINA_PERMITIR_INCLUIR_DISCIPLINA_PRE_REQUISITO(
			"InclusaoExclusaoDisciplina_permitirIncluirDisciplinaPreRequisito",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaPreRequisito_titulo"),
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaPreRequisito_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUSAO_EXCLUSAO_DISCIPLINA_PERMITIR_INCLUIR_ACIMA_NR_VAGAS("InclusaoAcimaNrMaximoVagas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaAcimaNrMaximoVagas_titulo"),
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaAcimaNrMaximoVagas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUSAO_EXCLUSAO_DISCIPLINA_PERMITIR_INCLUIR_NR_MAXIMO_VAGAS("InclusaoNrMaximoVagas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaNrMaximoVagas_titulo"),
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirIncluirDisciplinaNrMaximoVagas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUSAO_EXCLUSAO_DISCIPLINA_PERMITIR_EXCLUIR_DISCIPLINA_FORA_PRAZO(
			"InclusaoExclusaoDisciplina_permitirExcluirDisciplinaForaPrazo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirExcluirDisciplinaForaPrazo_titulo"),
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinapermitirExcluirDisciplinaForaPrazo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_FILTRAR_SITUACAO_MATRICULA_INCLUSAO_EXCLUSAO_DISCIPLINA(
			"PermitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirFiltrarSituacaoMatriculaInclusaoExclusaoDisciplina_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_REALIZAR_INCLUSAO_EXCLUSAO_DISCIPLINA_SOMENTE_VIA_REQUERIMENTO(
			"Requerimento_PermitirRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_Requerimento_PermitirRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento_titulo"),
					UteisJSF.internacionalizar(
							"per_Requerimento_PermitirRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_REALIZAR_INCLUSAO_EXCLUSAO_DISCIPLINA_SOMENTE_ALUNO_CURSO_COORDENA(
			"PermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermitirRealizarInclusaoExclusaoDisciplinaSomenteAlunoCursoCoordena_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirRealizarInclusaoExclusaoDisciplinaSomenteAlunoCursoCoordena_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_INCLUIR_DISCIPLINA_SEM_AULA_PROGRAMADA_EM_CURSOS_INTEGRAIS(
			"InclusaoExclusaoDisciplinaPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinaPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais_titulo"),
					UteisJSF.internacionalizar(
							"per_InclusaoExclusaoDisciplinaPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.INCLUSAO_EXCLUSAO_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Matricula
	 *
	 */
	MATRICULA("Matricula",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Matricula_titulo"),
							UteisJSF.internacionalizar("per_Matricula_ajuda"),
							new String[] { "renovarMatriculaCons.xhtml", "renovarMatriculaForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_Matricula_titulo"),
							UteisJSF.internacionalizar("per_Matricula_ajuda"),
							new String[] { "renovacaoMatriculaAluno.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_Matricula_titulo"),
							UteisJSF.internacionalizar("per_Matricula_ajuda"),
							new String[] { "renovacaoMatriculaAluno.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_NAO_APRESENTAR_BOTOES_MATRICULA_RENOVACAO("Matricula_NaoApresentarBotoesMatriculaRenovacao",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaNaoApresentarBotoesMatriculaRenovacao_titulo"),
							UteisJSF.internacionalizar("per_MatriculaNaoApresentarBotoesMatriculaRenovacao_ajuda"))
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_INFORMAR_NUMERO_MATRICULA_MANUALMENTE("Matricula_PermitirInformarNumeroMatriculaManualmente",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaPermitirInformarNumeroMatriculaManualmente_titulo"),
							UteisJSF.internacionalizar("per_MatriculaPermitirInformarNumeroMatriculaManualmente_ajuda"))
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_DESCONTO_MATRICULA("Matricula_LiberarDescontoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberarDescontoMatricula_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberarDescontoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_VAGASDE_PROGRAMAS_ESPECIAIS(
			"Matricula_PermitirApresentarFormaIngressoVagasdeProgramasEspeciais",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoVagasdeProgramasEspeciais_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoVagasdeProgramasEspeciais_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_VAGASDE_PROGRAMAS_ESPECIAISFIES(
			"Matricula_PermitirApresentarFormaIngressoVagasdeProgramasEspeciaisFies",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoVagasdeProgramasEspeciaisFies_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoVagasdeProgramasEspeciaisFies_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_NR_MAXIMO_AULAS("MatriculaNrMaximoAulas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaNrMaximoAulas_titulo"),
					UteisJSF.internacionalizar("per_MatriculaNrMaximoAulas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_BLOQUEIA_DESCONTO_PARCELA_MATRICULA("MatriculaBloqueiaDescontoParcelaMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaBloqueiaDescontoParcelaMatricula_titulo"),
					UteisJSF.internacionalizar("per_MatriculaBloqueiaDescontoParcelaMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_AVALIACAO_SERIADA(
			"Matricula_PermitirApresentarFormaIngressoAvaliacaoSeriada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoAvaliacaoSeriada_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoAvaliacaoSeriada_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_NR_MAXIMO_VAGAS("MatriculaNrMaximoVagas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaNrMaximoVagas_titulo"),
					UteisJSF.internacionalizar("per_MatriculaNrMaximoVagas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_TRANSFERENCIA_EXTERNA(
			"Matricula_PermitirApresentarFormaIngressoTransferenciaExterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoTransferenciaExterna_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoTransferenciaExterna_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_TRANSFERENCIA_INTERNA(
			"Matricula_PermitirApresentarFormaIngressoTransferenciaInterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoTransferenciaInterna_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoTransferenciaInterna_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_PROUNI("Matricula_PermitirApresentarFormaIngressoProuni",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoProuni_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoProuni_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_EXCLUSAO_DISCIPLINA_PERIODO_LETIVO_ATUAL_ALUNO_REGULAR(
			"Matricula_LiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_EXCLUSAO_DISCIPLINA_PERIODO_LETIVO_ATUAL_ALUNO_IRREGULAR(
			"Matricula_LiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_AUTORIZAR_INCLUSAO_DISCIPLINA_FORA_PRAZO("Matricula_AutorizarInclusaoDisciplinaForaPrazo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaAutorizarInclusaoDisciplinaForaPrazo_titulo"),
					UteisJSF.internacionalizar("per_MatriculaAutorizarInclusaoDisciplinaForaPrazo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_LIBERACAO_DESBLOQUEIO_POR_SENHA_DESCONTO_ALUNO("PermitirLiberacaoDesbloqueioPorSenhaDescontoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirLiberacaoDesbloqueioPorSenhaDescontoAluno_titulo"),
					UteisJSF.internacionalizar("per_PermitirLiberacaoDesbloqueioPorSenhaDescontoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_REINGRESSO("Matricula_PermitirApresentarFormaIngressoReingresso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoReingresso_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoReingresso_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	MATRICULA_EMITIR_BOLETO_MATRICULA("Matricula_EmitirBoletoMatricula",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaEmitirBoletoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaEmitirBoletoMatricula_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MatriculaEmitirBoletoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaEmitirBoletoMatricula_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MatriculaEmitirBoletoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaEmitirBoletoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	VISUALIZAR_ABA_DESCONTO_PLANO_FINANCEIRO("VisualizarAbaDescontoPlanoFinanceiro",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_VisualizarAbaDescontoPlanoFinanceiro_titulo"),
							UteisJSF.internacionalizar("per_VisualizarAbaDescontoPlanoFinanceiro_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_VisualizarAbaDescontoPlanoFinanceiro_titulo"),
							UteisJSF.internacionalizar("per_VisualizarAbaDescontoPlanoFinanceiro_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_VisualizarAbaDescontoPlanoFinanceiro_titulo"),
							UteisJSF.internacionalizar("per_VisualizarAbaDescontoPlanoFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_INCLUIR_DISCIPLINA_POR_EQUIVALENCIA("PermitirIncluirDisciplinaPorEquivalencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirIncluirDisciplinaPorEquivalencia_titulo"),
					UteisJSF.internacionalizar("per_PermitirIncluirDisciplinaPorEquivalencia_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_REALIZAR_MATRICULA_DISCIPLINA_PRE_REQUISITO(
			"Matricula_PermitirRealizarMatriculaDisciplinaPreRequisito",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirRealizarMatriculaDisciplinaPreRequisito_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirRealizarMatriculaDisciplinaPreRequisito_ajuda")), 
					 new PermissaoVisao(TipoVisaoEnum.ALUNO,
								UteisJSF.internacionalizar("per_MatriculaPermitirRealizarMatriculaDisciplinaPreRequisito_titulo"),
								UteisJSF.internacionalizar("per_MatriculaPermitirRealizarMatriculaDisciplinaPreRequisito_ajuda")), 
					 new PermissaoVisao(TipoVisaoEnum.PAIS,
								UteisJSF.internacionalizar("per_MatriculaPermitirRealizarMatriculaDisciplinaPreRequisito_titulo"),
								UteisJSF.internacionalizar("per_MatriculaPermitirRealizarMatriculaDisciplinaPreRequisito_ajuda"))
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	VISUALIZAR_ABA_DESCONTOS("VisualizarAbaDescontos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_VisualizarAbaDescontos_titulo"),
					UteisJSF.internacionalizar("per_VisualizarAbaDescontos_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_VisualizarAbaDescontos_titulo"),
							UteisJSF.internacionalizar("per_VisualizarAbaDescontos_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_VisualizarAbaDescontos_titulo"),
							UteisJSF.internacionalizar("per_VisualizarAbaDescontos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_ALTERAR_ORDEM_DESCONTOS("MatriculaAlterarOrdemDescontos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaAlterarOrdemDescontos_titulo"),
					UteisJSF.internacionalizar("per_MatriculaAlterarOrdemDescontos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	NAVEGAR_ABA_PENDENCIA_FINANCEIRA("NavegarAbaPendenciaFinanceira",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_NavegarAbaPendenciaFinanceira_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaPendenciaFinanceira_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_NavegarAbaPendenciaFinanceira_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaPendenciaFinanceira_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_NavegarAbaPendenciaFinanceira_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaPendenciaFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_OUTROS_TIPOSDE_SELECAO(
			"Matricula_PermitirApresentarFormaIngressoOutrosTiposdeSelecao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoOutrosTiposdeSelecao_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoOutrosTiposdeSelecao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_SUSPENDER_ATIVA_PENDENCIA_DOCUMENTO("MatriculaSuspenderAtivaPendenciaDocumento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaSuspenderAtivaPendenciaDocumento_titulo"),
					UteisJSF.internacionalizar("per_MatriculaSuspenderAtivaPendenciaDocumento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_DESATIVAR_FINANCEIRO_MANUAL("PermitirDesativarFinanceiroManual",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirDesativarFinanceiroManual_titulo"),
					UteisJSF.internacionalizar("per_PermitirDesativarFinanceiroManual_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_DECISAO_JUDICIAL(
			"Matricula_PermitirApresentarFormaIngressoDecisaoJudicial",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoDecisaoJudicial_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoDecisaoJudicial_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_ACIMA_NR_MAXIMO_VAGAS("MatriculaAcimaNrMaximoVagas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaAcimaNrMaximoVagas_titulo"),
					UteisJSF.internacionalizar("per_MatriculaAcimaNrMaximoVagas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_PORTADOR_DIPLINA(
			"Matricula_PermitirApresentarFormaIngressoPortadorDiplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoPortadorDiplina_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoPortadorDiplina_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_CONTROLE_INCLUSAO_DISCIPLINA_PERIODO_FUTURO(
			"Matricula_LiberarControleInclusaoDisciplinaPeriodoFuturo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberarControleInclusaoDisciplinaPeriodoFuturo_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberarControleInclusaoDisciplinaPeriodoFuturo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_OBRIGATORIEDADE_ALUNO_ACEITAR_TERMO_ACEITE(
			"Matricula_LiberarObrigatoriedadeAlunoAceitarTermoAceiteRenovacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarObrigatoriedadeAlunoAceitarTermoAceiteRenovacao_titulo"),
					UteisJSF.internacionalizar("per_LiberarObrigatoriedadeAlunoAceitarTermoAceiteRenovacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_PEC_G("Matricula_PermitirApresentarFormaIngressoPEC-G",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoPECG_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoPECG_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_VESTIBULAR("Matricula_PermitirApresentarFormaIngressoVestibular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoVestibular_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoVestibular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_AUTORIZAR_INCLUSAO_DISCIPLINA_ACIMA_LIMITE_PERIODO(
			"Matricula_AutorizarInclusaoDisciplinaAcimaLimitePeriodo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaAutorizarInclusaoDisciplinaAcimaLimitePeriodo_titulo"),
					UteisJSF.internacionalizar("per_MatriculaAutorizarInclusaoDisciplinaAcimaLimitePeriodo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	MATRICULA_AUTORIZAR_INCLUSAO_DISCIPLINA_ABAIXO_LIMITE_PERIODO(
			"Matricula_AutorizarInclusaoDisciplinaAbaixoLimitePeriodo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaAutorizarInclusaoDisciplinaAbaixoLimitePeriodo_titulo"),
					UteisJSF.internacionalizar("per_MatriculaAutorizarInclusaoDisciplinaAbaixoLimitePeriodo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	MATRICULA_RENOVAR_COM_PENDENCIA_DOCUMENTO_OBRIGATORIO("Matricula_RenovarComPendenciaDocumentoObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaRenovarComPendenciaDocumentoObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_MatriculaRenovarComPendenciaDocumentoObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_ALTERAR_SITUACAO_MATRICULA_MANUAL("Matricula_PermitirAlterarSituacaoMatriculaManual",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Matricula_PermitirAlterarSituacaoMatriculaManual_titulo"),
					UteisJSF.internacionalizar("per_Matricula_PermitirAlterarSituacaoMatriculaManual_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	/*
	 * NAVEGAR_ABA_PLANO_FINANCEIRO_ALUNO("NavegarAbaPlanoFinanceiroAluno", new
	 * PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
	 * UteisJSF.internacionalizar("per_NavegarAbaPlanoFinanceiroAluno_titulo"),
	 * UteisJSF.internacionalizar("per_NavegarAbaPlanoFinanceiroAluno_ajuda")) },
	 * TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
	 * PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
	 * PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	 */

	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_ENTREVISTA("Matricula_PermitirApresentarFormaIngressoEntrevista",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoEntrevista_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoEntrevista_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_TRANSFERENCIA_EX_OFFICIO(
			"Matricula_PermitirApresentarFormaIngressoTransferenciaExOfficio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoTransferenciaExOfficio_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoTransferenciaExOfficio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_CONFIRMAR_CANCELAR_PRE_MATRICULA("MatriculaConfirmarCancelarPreMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaConfirmarCancelarPreMatricula_titulo"),
					UteisJSF.internacionalizar("per_MatriculaConfirmarCancelarPreMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_FORA_PRAZO("MatriculaForaPrazo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaForaPrazo_titulo"),
					UteisJSF.internacionalizar("per_MatriculaForaPrazo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	NAVEGAR_ABA_DOCUMENTACAO("NavegarAbaDocumentacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NavegarAbaDocumentacao_titulo"),
					UteisJSF.internacionalizar("per_NavegarAbaDocumentacao_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_NavegarAbaDocumentacao_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaDocumentacao_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_NavegarAbaDocumentacao_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaDocumentacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_ALTERAR_DATA_MATRICULA("PermitirAlterarDataMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarDataMatricula_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarDataMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_ALTERAR_DADOS_FINANCEIROS("MatriculaAlterarDadosFinanceiros",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosFinanceiros_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosFinanceiros_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosFinanceiros_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosFinanceiros_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosFinanceiros_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosFinanceiros_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_TIPO_MATRICULA("MatriculaTipoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaTipoMatricula_titulo"),
					UteisJSF.internacionalizar("per_MatriculaTipoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_VAGAS_REMANESCENTES(
			"Matricula_PermitirApresentarFormaIngressoVagasRemanescentes",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoVagasRemanescentes_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoVagasRemanescentes_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_SELECAO_SIMPLIFICADA(
			"Matricula_PermitirApresentarFormaIngressoSelecaoSimplificada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoSelecaoSimplificada_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaPermitirApresentarFormaIngressoSelecaoSimplificada_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_ANEXAR_IMAGEM_DOCUMENTOS_ENTREGUES("MatriculaAnexarImagemDocumentosEntregues",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaAnexarImagemDocumentosEntregues_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAnexarImagemDocumentosEntregues_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MatriculaAnexarImagemDocumentosEntregues_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAnexarImagemDocumentosEntregues_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MatriculaAnexarImagemDocumentosEntregues_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAnexarImagemDocumentosEntregues_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_ALTERAR_DADOS_ACADEMICOS("MatriculaAlterarDadosAcademicos",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosAcademicos_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosAcademicos_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosAcademicos_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosAcademicos_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosAcademicos_titulo"),
							UteisJSF.internacionalizar("per_MatriculaAlterarDadosAcademicos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),	
	PERMITIR_APOS_CONFIRMACAO_DA_RENOVACAO_DA_MATRICULA_PERMITIR_INCLUSAO_EXCLUSAO_DISCIPLINA_APOS_PAGAMENTO_MATRICULA("Matricula_PermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaPermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaPermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_MatriculaPermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaPermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_MatriculaPermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaPermitirAposConfirmacaoRenovacaoMatriculaPermitirInclusaoExclusaoDisciplinaAposPagamentoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	APRESENTAR_CONSULTOR_MATRICULA("ApresentarConsultorMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ApresentarConsultorMatricula_titulo"),
					UteisJSF.internacionalizar("per_ApresentarConsultorMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_AUTORIZAR_RENOVACAO_COM_DEBITO("Matricula_AutorizarRenovacaoComDebito",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaAutorizarRenovacaoComDebito_titulo"),
					UteisJSF.internacionalizar("per_MatriculaAutorizarRenovacaoComDebito_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_ALTERAR_TURNO_RENOVACAO("PermitirAlterarTurnoRenovacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarTurnoRenovacao_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarTurnoRenovacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_INCLUIR_DISCIPLINA_OPTATIVA("PermitirIncluirDisciplinaOptativa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirIncluirDisciplinaOptativa_titulo"),
					UteisJSF.internacionalizar("per_PermitirIncluirDisciplinaOptativa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_ATIVAR_SITUACAO_ULTIMO_PERIODO_ALUNO("PermitirAtivarSituacaoUltimoPeriodoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAtivarSituacaoUltimoPeriodoAluno_titulo"),
					UteisJSF.internacionalizar("per_PermitirAtivarSituacaoUltimoPeriodoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_REALIZAR_EDICAO_MATRICULA_RENOVADA("PermitirRealizarEdicaoMatriculaRenovada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirRealizarEdicaoMatriculaRenovada_titulo"),
					UteisJSF.internacionalizar("per_PermitirRealizarEdicaoMatriculaRenovada_ajuda")),new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirRealizarEdicaoMatriculaRenovada_titulo"),
							UteisJSF.internacionalizar("per_PermitirRealizarEdicaoMatriculaRenovada_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirRealizarEdicaoMatriculaRenovada_titulo"),
							UteisJSF.internacionalizar("per_PermitirRealizarEdicaoMatriculaRenovada_ajuda"))  },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	ATIVAR_FINANCEIRO_MANUAL("AtivarFinanceiroManual",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtivarFinanceiroManual_titulo"),
					UteisJSF.internacionalizar("per_AtivarFinanceiroManual_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_LIBERAR_CONTROLE_INCLUSAO_OBRIGATORIA_DISCIPLINA_DEPENDENCIA(
			"Matricula_LiberarControleInclusaoObrigatoriaDisciplinaDependencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarControleInclusaoObrigatoriaDisciplinaDependencia_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarControleInclusaoObrigatoriaDisciplinaDependencia_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_LIBERAR_INCLUSAO_DISCIPLINA_PERIODO_LETIVO_FUTURO_ALUNO_REGULAR(
			"Matricula_LiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberarAlteracaoCategoriaPlanoFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_LIBERAR_ALTERAR_CATEGORIA_CONDICAO_PAGAMENTO_PLANO_FINANCEIRO_CURSO(
			"Matricula_LiberarAlterarCategoriaCondicaoPagamentoPlanoFinanceiroCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberarAlteracaoCategoriaPlanoFinanceiro_titulo"),
					UteisJSF.internacionalizar(
							"per_MatriculaLiberarInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	NAVEGAR_ABA_DISCIPLINAS("NavegarAbaDisciplinas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NavegarAbaDisciplinas_titulo"),
					UteisJSF.internacionalizar("per_NavegarAbaDisciplinas_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_NavegarAbaDisciplinas_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaDisciplinas_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_NavegarAbaDisciplinas_titulo"),
							UteisJSF.internacionalizar("per_NavegarAbaDisciplinas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_PROCESSO_SELETIVO(
			"Matricula_PermitirApresentarFormaIngressoProcessoSeletivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoProcessoSeletivo_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PRIORIZAR_CONSULTOR_PROSPECT_COMO_PADRAO("PriorizarConsultorProspectComoPadrao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PriorizarConsultorProspectComoPadrao_titulo"),
					UteisJSF.internacionalizar("per_PriorizarConsultorProspectComoPadrao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_DESCONTO_PROGRESSIVO_PRIMEIRA_PARCELA("Matricula_LiberarDescontoProgressivoPrimeiraParcela",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberarDescontoProgressivoPrimeiraParcela_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberarDescontoProgressivoPrimeiraParcela_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_APRESENTAR_FORMA_INGRESSO_ENEM("Matricula_PermitirApresentarFormaIngressoEnem",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoEnem_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirApresentarFormaIngressoEnem_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_MATRICULA("Matricula_LiberarMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberarMatricula_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberarMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
//	PERMITE_LEITURA_ARQUIVO_SCANNER_MATRICULA("PermiteLeituraArquivoScannerMatricula",
//			new PermissaoVisao[] {
//					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScannerMatricula_titulo"),
//							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScannerMatricula_ajuda")),
//					new PermissaoVisao(TipoVisaoEnum.ALUNO,
//							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScannerMatricula_titulo"),
//							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScannerMatricula_ajuda")),
//					new PermissaoVisao(TipoVisaoEnum.PAIS,
//							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScannerMatricula_titulo"),
//							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScannerMatricula_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
//			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_ALTERAR_PERIODO_LETIVO_LIMITE_RENOVACAO("Matricula_PermitirAlterarPeriodoLetivoLimiteRenovacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirAlterarPeriodoLetivoLimiteRenovacao_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirAlterarPeriodoLetivoLimiteRenovacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA("Matricula_PermitirSolicitarAprovacaoLiberacaoFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Matricula_PermitirSolicitarAprovacaoLiberacaoFinanceira_titulo"),
					UteisJSF.internacionalizar("per_Matricula_PermitirSolicitarAprovacaoLiberacaoFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_SOLICITAR_LIBERACAO_MATRICULA_APOS_INICIO_X_MODULOS("Matricula_PermitirSolicitarLiberacaoMatriculaAposInicioXModulos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Matricula_PermitirSolicitarLiberacaoMatriculaAposInicioXModulos_titulo"),
					UteisJSF.internacionalizar("per_Matricula_PermitirSolicitarLiberacaoMatriculaAposInicioXModulos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_RECONSIDERACAO_SOLICITACAO("Matricula_PermitirReconsideracaoSolicitacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Matricula_PermitirReconsideracaoSolicitacao_titulo"),
					UteisJSF.internacionalizar("per_Matricula_PermitirReconsideracaoSolicitacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	INCLUIR_DISCIPLINA_APENAS_TURMA_PROPRIO_CURSO("IncluirDisciplinaApenasTurmaProprioCurso",			
			new PermissaoVisao[] { 
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioCurso_titulo"),UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioCurso_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioCurso_titulo"),UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioCurso_ajuda"))					 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.MATRICULA,PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	INCLUIR_DISCIPLINA_APENAS_TURMA_PROPRIO_UNIDADE_ENSINO("IncluirDisciplinaApenasTurmaProprioUnidadeEnsino",			
			new PermissaoVisao[] { 
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioUnidadeEnsino_titulo"),UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioUnidadeEnsino_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioUnidadeEnsino_titulo"),UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioUnidadeEnsino_ajuda"))					 
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.MATRICULA,PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	INCLUIR_DISCIPLINA_APENAS_TURMA_PROPRIO_MATRIZ_CURRICULAR("IncluirDisciplinaApenasTurmaProprioMatrizCurricular",			
			new PermissaoVisao[] { 
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioMatrizCurricular_titulo"),UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioMatrizCurricular_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioMatrizCurricular_titulo"),UteisJSF.internacionalizar("per_IncluirDisciplinaApenasTurmaProprioMatrizCurricular_ajuda"))					 
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.MATRICULA,PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	PERMITIR_SIMULAR_ACESSO_VISAO_ALUNO("PermitirSimularAcessoVisaoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirSimularAcessoVisaoAluno_titulo"),
					UteisJSF.internacionalizar("per_PermitirSimularAcessoVisaoAluno_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_CADASTRAR_CONVENIO_MATRICULA("PermitirCadastrarConvenioMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarConvenioMatricula_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarConvenioMatricula_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITE_ALTERAR_INFORMACOES_ADICIONAIS_CONVENIO("PermiteAlterarInformacoesAdicionaisConvenio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarInformacoesAdicionaisConvenio_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarInformacoesAdicionaisConvenio_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_HABILITAR_RECURSO_VALIDACAO_DEBITO_INCLUSAO_CONVENIO("Matricula_HabilitarRecursoValidacaoDebitoInclusaoConvenio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_HabilitarRecursoValidacaoDebitoInclusaoConvenio_titulo"),
					UteisJSF.internacionalizar("per_HabilitarRecursoValidacaoDebitoInclusaoConvenio_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_INCLUSAO_CONVENIO_DEBITO_FINANCEIRO("Matricula_LiberarInclusaoConvenioDebitoFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarInclusaoConvenioDebitoFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_LiberarInclusaoConvenioDebitoFinanceiro_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_REGERAR_FINANCEIRO("Matricula_PermitirRegerarFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirRegerarFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirRegerarFinanceiro_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_LIBERAR_VALIDACAO_DADOS_ENADE_CENSO("Matricula_LiberarValidacaoDadosEnadeCenso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberarValidacaoDadosEnadeCenso_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberarValidacaoDadosEnadeCenso_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_PERMITIR_USUARIO_DESCONSIDERAR_DIFERENCA_VALOR_RATEIO("Matricula_PermitirUsuarioDesconsiderarDiferencaValorRateio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaPermitirUsuarioDesconsiderarDiferencaValorRateio_titulo"),
					UteisJSF.internacionalizar("per_MatriculaPermitirUsuarioDesconsiderarDiferencaValorRateio_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITIR_INCLUIR_PLANO_DESCONTO_DIFERENTE_CONDICAO_PAGAMENTO_DO_CURSO("PermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso_titulo"),
					UteisJSF.internacionalizar("per_PermitirIncluirPlanoDescontoDiferenteCondicaoPagamentodoCurso_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MATRICULA_INFORMA_CONTRATO_MATRICULA_VINCULADO_TURMA("MatriculaInformaContratoMatriculaVinculadoTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaInformaContratoMatriculaVinculadoTurma_titulo"),
					UteisJSF.internacionalizar("per_MatriculaInformaContratoMatriculaVinculadoTurmaajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Periodo LetivoAtivo Unidade Ensino Curso
	 *
	 */
	PERIODO_LETIVO_ATIVO_UNIDADE_ENSINO_CURSO("PeriodoLetivoAtivoUnidadeEnsinoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PeriodoLetivoAtivoUnidadeEnsinoCurso_titulo"),
					UteisJSF.internacionalizar("per_PeriodoLetivoAtivoUnidadeEnsinoCurso_ajuda"),
					new String[] { "fechamentoPeriodoLetivoPrmForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	REABRIR_PERIODO_LETIVO_ATIVO_UNIDADE_ENSINO_CURSO("ReabrirPeriodoLetivoAtivoUnidadeEnsinoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirReabrirPeriodoletivo_titulo"),
					UteisJSF.internacionalizar("per_PermitirReabrirPeriodoletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PERIODO_LETIVO_ATIVO_UNIDADE_ENSINO_CURSO,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Impressao Contrato
	 *
	 */
	IMPRESSAO_CONTRATO("ImpressaoContrato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ImpressaoContrato_titulo"),
			UteisJSF.internacionalizar("per_ImpressaoContrato_ajuda"), new String[] { "impressaoContrato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Configuracao Academica Historico
	 *
	 */
	CONFIGURACAO_ACADEMICA_HISTORICO("ConfiguracaoAcademicaHistorico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoAcademicaHistorico_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoAcademicaHistorico_ajuda"),
					new String[] { "configuracaoAcademicaHistorico.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Confirmacao Pre Matricula
	 *
	 */
	CONFIRMACAO_PRE_MATRICULA("ConfirmacaoPreMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfirmacaoPreMatricula_titulo"),
					UteisJSF.internacionalizar("per_ConfirmacaoPreMatricula_ajuda"),
					new String[] { "confirmacaoPreMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Portador Diploma
	 *
	 */
	PORTADOR_DIPLOMA("PortadorDiploma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PortadorDiploma_titulo"),
					UteisJSF.internacionalizar("per_PortadorDiploma_ajuda"),
					new String[] { "portadorDiplomaCons.xhtml", "portadorDiplomaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Processo Matricula
	 *
	 */
	PROCESSO_MATRICULA("ProcessoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessoMatricula_titulo"),
					UteisJSF.internacionalizar("per_ProcessoMatricula_ajuda"),
					new String[] { "processoMatriculaCons.xhtml", "processoMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITE_ALTERAR_PERIODO_CALENDARIO("PermiteAlterarPeriodoCalendario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarPeriodoCalendario_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarPeriodoCalendario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROCESSO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	MUDAR_DATA_FIM_PERIODO_LETIVO("MudarDataFimPeriodoLetivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MudarDataFimPeriodoLetivo_titulo"),
					UteisJSF.internacionalizar("per_MudarDataFimPeriodoLetivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROCESSO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	PERMITE_ALTERAR_SITUACAO_CALENDARIO_MATRICULA("PermiteAlterarSituacaoCalendarioMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_ProcessoMatricula_PermiteAlterarSituacaoCalendarioMatricula_titulo"),
					UteisJSF.internacionalizar(
							"per_ProcessoMatricula_PermiteAlterarSituacaoCalendarioMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.PROCESSO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * Renovar Matricula Por Turma
	 *
	 */
	RENOVAR_MATRICULA_POR_TURMA("RenovarMatriculaPorTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RenovarMatriculaPorTurma_titulo"),
					UteisJSF.internacionalizar("per_RenovarMatriculaPorTurma_ajuda"),
					new String[] { "renovarMatriculaTurmaCons.xhtml", "renovarMatriculaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	PERMITIR_ALUNO_ALTERAR_TURMA_BASE_SUGERIDA("PermitirAlunoAlterarTurmaBaseSugerida",
			new PermissaoVisao[] { 
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirAlunoAlterarTurmaBaseSugerida_titulo"),
							UteisJSF.internacionalizar("per_PermitirAlunoAlterarTurmaBaseSugerida_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirAlunoAlterarTurmaBaseSugerida_titulo"),
							UteisJSF.internacionalizar("per_PermitirAlunoAlterarTurmaBaseSugerida_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	/**
	 * Painel Gestor Visualizar Academico
	 *
	 */
	PAINEL_GESTOR_VISUALIZAR_ACADEMICO("PainelGestorVisualizarAcademico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorVisualizarAcademico_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorVisualizarAcademico_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SEI_DECIDIR_ACADEMICO),
	PAINEL_GESTOR_REQUERIMENTOS_ACADEMICO("PainelGestorRequerimentosAcademico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorRequerimentosAcademico_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorRequerimentosAcademico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.PAINEL_GESTOR_VISUALIZAR_ACADEMICO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SEI_DECIDIR_ACADEMICO),
	PAINEL_GESTOR_COMUNICACAO_INTERNA_ACADEMICO("PainelGestorComunicacaoInternaAcademico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorComunicacaoInternaAcademico_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorComunicacaoInternaAcademico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.PAINEL_GESTOR_VISUALIZAR_ACADEMICO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SEI_DECIDIR_ACADEMICO),
	/**
	 * Historico
	 *
	 */
	HISTORICO("Historico",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Historico_titulo"),
							UteisJSF.internacionalizar("per_Historico_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Motivo Cancelamento Trancamento
	 *
	 */
	MOTIVO_CANCELAMENTO_TRANCAMENTO("MotivoCancelamentoTrancamento", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MotivoCancelamentoTrancamento_titulo"),
			UteisJSF.internacionalizar("per_MotivoCancelamentoTrancamento_ajuda"),
			new String[] { "motivoCancelamentoTrancamentoCons.xhtml", "motivoCancelamentoTrancamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Matricula Enade
	 *
	 */
	MATRICULA_ENADE("MatriculaEnade", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_MatriculaEnade_titulo"),
			UteisJSF.internacionalizar("per_MatriculaEnade_ajuda"), new String[] { "matriculaEnadeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Processar Resultado Prova Presencial
	 *
	 */
	PROCESSAR_RESULTADO_PROVA_PRESENCIAL("ProcessarResultadoProvaPresencial",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessarResultadoProvaPresencial_titulo"),
					UteisJSF.internacionalizar("per_ProcessarResultadoProvaPresencial_ajuda"),
					new String[] { "processarResultadoProvaPresencialCons.xhtml",
							"processarResultadoProvaPresencialForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Questionário Relatório Facilitador
	 *
	 */
	FORMULARIO_RELATORIO_FACILITADOR("FormularioRelatorioFacilitador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FormularioRelatorioFacilitador_titulo"),
					UteisJSF.internacionalizar("per_FormularioRelatorioFacilitador_ajuda"),
					new String[] { "questionarioCons.xhtml",
							"questionarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),	
	/**
	 * Registrar Falta
	 *
	 */
	REGISTRAR_FALTA("RegistrarFalta",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistrarFalta_titulo"),
					UteisJSF.internacionalizar("per_RegistrarFalta_ajuda"),
					new String[] { "registrarFaltaCons.xhtml", "registrarFaltaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Reativacao Matricula
	 *
	 */
	REATIVACAO_MATRICULA("ReativacaoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ReativacaoMatricula_titulo"),
					UteisJSF.internacionalizar("per_ReativacaoMatricula_ajuda"),
					new String[] { "reativacaoMatriculaCons.xhtml", "reativacaoMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	REATIVAR_MATRICULA_LIBERAR_REATIVACAO_MATRICULA_NUMERO_PERIODO_ULTRAPASSADO_AO_CONFIGURADO(
			"ReativarMatricula_liberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar(
					"per_ReativarMatricula_liberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado_titulo"),
					UteisJSF.internacionalizar(
							"per_ReativarMatricula_liberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REATIVACAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Professor Ministrou Aula Turma
	 *
	 */
	PROFESSOR_MINISTROU_AULA_TURMA("ProfessorMinistrouAulaTurma", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ProfessorMinistrouAulaTurma_titulo"),
			UteisJSF.internacionalizar("per_ProfessorMinistrouAulaTurma_ajuda"),
			new String[] { "professorMinistrouAulaTurmaCons.xhtml", "professorMinistrouAulaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Aproveitamento Disciplina
	 *
	 */
	APROVEITAMENTO_DISCIPLINA("AproveitamentoDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AproveitamentoDisciplina_titulo"),
					UteisJSF.internacionalizar("per_AproveitamentoDisciplina_ajuda"),
					new String[] { "aproveitamentoDisciplinaCons.xhtml", "aproveitamentoDisciplinaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Artefato Entrega Aluno
	 *
	 */
	ARTEFATO_ENTREGA_ALUNO("ArtefatoEntregaAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ArtefatoEntregaAluno_titulo"),
					UteisJSF.internacionalizar("per_ArtefatoEntregaAluno_ajuda"),
					new String[] { "artefatoEntregaAlunoCons.xhtml", "artefatoEntregaAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITIR_CRIAR_SCRIPT_ARTEFATO_ENTREGA_ALUNO("PermitirCriarScriptArtefatoEntregaAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCriarScriptArtefatoEntregaAluno_titulo"),
					UteisJSF.internacionalizar("per_PermitirCriarScriptArtefatoEntregaAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ARTEFATO_ENTREGA_ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Registro Entrega Artefato Aluno
	 *
	 */
	REGISTRO_ENTREGA_ARTEFATO_ALUNO("RegistroEntregaArtefatoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroEntregaArtefatoAluno_titulo"),
					UteisJSF.internacionalizar("per_RegistroEntregaArtefatoAluno_ajuda"),
					new String[] { "registroEntregaArtefatoAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	/**
	 * Campos Formulário Relatório Facilitador
	 *
	 */
	CAMPOS_FORMULARIO_RELATORIO_FACILITADOR("CamposFormularioRelatorioFacilitador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CamposFormularioRelatorioFacilitador_titulo"),
					UteisJSF.internacionalizar("per_CamposFormularioRelatorioFacilitador_ajuda"),
					new String[] { "perguntaCons.xhtml",
							"perguntaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Calendário Relatório Final Facilitador
	 *
	 */
	CALENDARIO_RELATORIO_FINAL_FACILITADOR("CalendarioRelatorioFinalFacilitador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CalendarioRelatorioFinalFacilitador_titulo"),
					UteisJSF.internacionalizar("per_CalendarioRelatorioFinalFacilitador_ajuda"),
					new String[] { "calendarioRelatorioFinalFacilitadorCons.xhtml",
							"calendarioRelatorioFinalFacilitadorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),	
	/**
	 * Criterio Avaliacao Aluno
	 *
	 */
	CRITERIO_AVALIACAO_ALUNO("CriterioAvaliacaoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAluno_titulo"),
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAluno_ajuda"),
					new String[] { "criterioAvaliacaoAlunoCons.xhtml", "criterioAvaliacaoAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Enade
	 *
	 */
	ENADE("Enade",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Enade_titulo"), UteisJSF.internacionalizar("per_Enade_ajuda"),
					new String[] { "enadeCons.xhtml", "enadeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Mapa Suspensao Matricula
	 *
	 */
	MAPA_SUSPENSAO_MATRICULA("MapaSuspensaoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_ajuda"),
					new String[] { "mapaSuspensaoMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_LIBERAR_SUSPENSAO_MATRICULA("MapaSuspensaoMatricula_PermitirLiberarSuspensaoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirLiberarSuspensaoMatricula_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirLiberarSuspensaoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_VISUALIZAR_SOLICITACAO_SUSPENSAO_MATRICULA_PENDENCIA_FINANCEIRA("MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_LIBERAR_SUSPENSAO_SOLICITACAO_MATRICULA_PENDENCIA_FINANCEIRA("MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_VISUALIZAR_SOLICITACAO_SUSPENSAO_MATRICULA_PENDENCIA_ACADEMICA("MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_LIBERAR_SUSPENSAO_SOLICITACAO_MATRICULA_PENDENCIA_ACADEMICA("MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_USUARIO_VISUALIZAR_APENAS_SUAS_SOLICITACOES("MapaSuspensaoMatricula_PermitirUsuarioVisualizarApenasSuasSolicitacoes",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirUsuarioVisualizarApenasSuasSolicitacoes_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirUsuarioVisualizarApenasSuasSolicitacoes_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_SUSPENSAO_MATRICULA_PERMITIR_VISUALIZAR_PENDENCIAS_DEFERIDAS_INDEFERIDAS_OUTROS_USUARIOS_MESMA_UNIDADE("MapaSuspensaoMatricula_PermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade_titulo"),
					UteisJSF.internacionalizar("per_MapaSuspensaoMatricula_PermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_SUSPENSAO_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Tipo Atividade Complementar
	 *
	 */
	TIPO_ATIVIDADE_COMPLEMENTAR("TipoAtividadeComplementar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoAtividadeComplementar_titulo"),
					UteisJSF.internacionalizar("per_TipoAtividadeComplementar_ajuda"),
					new String[] { "tipoAtividadeComplementarCons.xhtml", "tipoAtividadeComplementarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Tipo Justificativa Falta
	 *
	 */
	TIPO_JUSTIFICATIVA_FALTA("TipoJustificativaFalta",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoJustificativaFalta_titulo"),
					UteisJSF.internacionalizar("per_TipoJustificativaFalta_ajuda"),
					new String[] { "tipoJustificativaFaltaCons.xhtml", "tipoJustificativaFaltaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	LOG_HISTORICO("LogHistorico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LogHistorico_titulo"),
					UteisJSF.internacionalizar("per_LogHistorico_ajuda"),
					new String[] { "logAuditHistoricoCons.xhtml", "logAuditHistoricoCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Alteracoes Cadastrais Matricula
	 *
	 */
	ALTERACOES_CADASTRAIS_MATRICULA("AlteracoesCadastraisMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlteracoesCadastraisMatricula_titulo"),
					UteisJSF.internacionalizar("per_AlteracoesCadastraisMatricula_ajuda"),
					new String[] { "alteracoesCadastraisMatricula.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITIR_ALTERAR_INFORMACOES_REAJUSTE_PRECO("PermitirAlterarInformacoesReajustePreco",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarInformacoesReajustePreco_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarInformacoesReajustePreco_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.ALTERACOES_CADASTRAIS_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Alterar User Name Senha Alunos
	 *
	 */
	ALTERAR_USER_NAME_SENHA_ALUNOS("AlterarUserNameSenhaAlunos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarUserNameSenhaAlunos_titulo"),
					UteisJSF.internacionalizar("per_AlterarUserNameSenhaAlunos_ajuda"),
					new String[] { "alteracaoSenhaAlunoCons.xhtml", "alteracaoSenhaAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Assinar Documento Entregue
	 *
	 */
	ASSINAR_DOCUMENTO_ALUNO_ENTREGUE("AssinarDocumentoAlunoEntregue",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AssinarDocumentoAlunoEntregue_titulo"),
					UteisJSF.internacionalizar("per_AssinarDocumentoAlunoEntregue_ajuda"),
					new String[] { "assinarDocumentoEntregue.xhtml", "assinarDocumentoEntregue.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Alterar Historico Grade Anterior
	 *
	 */
	ALTERAR_HISTORICO_GRADE_ANTERIOR("AlterarHistoricoGradeAnterior",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarHistoricoGradeAnterior_titulo"),
					UteisJSF.internacionalizar("per_AlterarHistoricoGradeAnterior_ajuda"),
					new String[] { "historicoGradeAnteriorAlteradaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Entrega Documento
	 *
	 */
	ENTREGA_DOCUMENTO("EntregaDocumento", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EntregaDocumento_titulo"),
					UteisJSF.internacionalizar("per_EntregaDocumento_ajuda"),
					new String[] { "entregaDocumentoCons.xhtml", "entregaDocumentoForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_EntregaDocumento_titulo"),
					UteisJSF.internacionalizar("per_EntregaDocumento_ajuda"),
					new String[] { "entregaDocumentoAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_EntregaDocumento_titulo"),
					UteisJSF.internacionalizar("per_EntregaDocumento_ajuda"),
					new String[] { "entregaDocumentoAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_LEITURA_ARQUIVO_SCANNER("PermiteLeituraArquivoScanner",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScanner_titulo"),
							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScanner_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScanner_titulo"),
							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScanner_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScanner_titulo"),
							UteisJSF.internacionalizar("per_PermiteLeituraArquivoScanner_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ENTREGA_DOCUMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	ENTREGA_DOCUMENTO_PERMITE_ANEXAR_ARQUIVO("EntregaDocumentoPermiteAnexarArquivo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteAnexarArquivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteAnexarArquivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteAnexarArquivo_titulo"),
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteAnexarArquivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ENTREGA_DOCUMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	ENTREGA_DOCUMENTO_PERMITE_UPLOAD_DEFERIMENTO_INDEFERIMENTO_DOCUMENTO_CALOURODENTROPRAZOCHAMADAPROCESSOSELETIVO("EntregaDocumentoPermiteUpload_Deferimento_Indeferimento",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteUpload_Deferimento_Indeferimento_titulo"),
							UteisJSF.internacionalizar("per_EntregaDocumentoPermiteUpload_Deferimento_Indeferimento_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ENTREGA_DOCUMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	ENTREGA_DOCUMENTO_ALERTAR_ALUNO_PENDENCIA_DOCUMENTACAO_MATRICULA_LOGAR("EntregaDocumentoAlertarAlunoPendenciaDocumentacaoMatriculaLogar",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_EntregaDocumentoAlertarAlunoPendenciaDocumentacaoMatriculaLogar_titulo"),
							UteisJSF.internacionalizar("per_EntregaDocumentoAlertarAlunoPendenciaDocumentacaoMatriculaLogar_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ENTREGA_DOCUMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Inclusao Disciplina Fora Grade
	 *
	 */
	INCLUSAO_DISCIPLINA_FORA_GRADE("InclusaoDisciplinaForaGrade", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_InclusaoDisciplinaForaGrade_titulo"),
			UteisJSF.internacionalizar("per_InclusaoDisciplinaForaGrade_ajuda"),
			new String[] { "inclusaoDisciplinaForaGradeCons.xhtml", "inclusaoDisciplinaForaGradeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Abono Falta
	 *
	 */
	ABONO_FALTA("AbonoFalta", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_AbonoFalta_titulo"), UteisJSF.internacionalizar("per_AbonoFalta_ajuda"),
			new String[] { "abonoFaltaCons.xhtml", "abonoFaltaForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Identificacao Estudantil
	 *
	 */
	IDENTIFICACAO_ESTUDANTIL("IdentificacaoEstudantil",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IdentificacaoEstudantil_titulo"),
					UteisJSF.internacionalizar("per_IdentificacaoEstudantil_ajuda"),
					new String[] { "identificacaoEstudantil.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Tipo Advertencia
	 *
	 */
	TIPO_ADVERTENCIA("TipoAdvertencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoAdvertencia_titulo"),
					UteisJSF.internacionalizar("per_TipoAdvertencia_ajuda"),
					new String[] { "tipoAdvertenciaCons.xhtml", "tipoAdvertenciaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Impressao Declaracao
	 *
	 */
	IMPRESSAO_DECLARACAO("ImpressaoDeclaracao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ImpressaoDeclaracao_titulo"),
					UteisJSF.internacionalizar("per_ImpressaoDeclaracao_ajuda"),
					new String[] { "impressaoDeclaracao.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Texto Padrao Declaracao
	 *
	 */
	TEXTO_PADRAO_DECLARACAO("TextoPadraoDeclaracao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					 UteisJSF.internacionalizar("per_TextoPadraoDeclaracao_titulo"),
					 UteisJSF.internacionalizar("per_TextoPadraoDeclaracao_ajuda"), 
					 new String[]{"textoPadraoDeclaracaoCons.xhtml","textoPadraoDeclaracaoForm.xhtml"})},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Atividade Complementar
	 *
	 */
	ATIVIDADE_COMPLEMENTAR("AtividadeComplementar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtividadeComplementar_titulo"),
					UteisJSF.internacionalizar("per_AtividadeComplementar_ajuda"),
					new String[] { "atividadeComplementarCons.xhtml", "atividadeComplementarForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_AtividadeComplementar_titulo"),
							UteisJSF.internacionalizar("per_AtividadeComplementar_ajuda"),
							new String[] { "atividadeComplementarProfessor.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_LANCAMENTO_ATIVIDADE_COMPLEMENTAR_FUTURA("PermiteLancamentoAtividadeComplementarFutura",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteLancamentoAtividadeComplementarFutura_titulo"),
					UteisJSF.internacionalizar("per_PermiteLancamentoAtividadeComplementarFutura_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ATIVIDADE_COMPLEMENTAR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Minhas Notas Adminsitrativo
	 *
	 */
	MINHAS_NOTAS_ADMINSITRATIVO("MinhasNotasAdminsitrativo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MinhasNotasAdminsitrativo_titulo"),
					UteisJSF.internacionalizar("per_MinhasNotasAdminsitrativo_ajuda"),
					new String[] { "minhasNotasAdministrativo.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	PERMITIR_APRESENTAR_PROFESSOR("PermitirApresentarProfessor", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermitirApresentarProfessor_titulo"),UteisJSF.internacionalizar("per_PermitirImprimirMatrizCurricularAluno_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.MINHAS_NOTAS_ADMINSITRATIVO, 
			PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_DADOS_ALUNOS),
	/**
	 * Mapa Controle Entrega Documento Upload
	 *
	 */
	MAPA_CONTROLE_ENTREGA_DOCUMENTO_UPLOAD("MapaControleEntregaDocumentoUpload",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaControleEntregaDocumentoUpload_titulo"),
					UteisJSF.internacionalizar("per_MapaControleEntregaDocumentoUpload_ajuda"),
					new String[] { "mapaControleEntregaDocumentoUpload.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Exclusao Matricula
	 *
	 */
	EXCLUSAO_MATRICULA("ExclusaoMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExclusaoMatricula_titulo"),
					UteisJSF.internacionalizar("per_ExclusaoMatricula_ajuda"),
					new String[] { "exclusaoMatriculaCons.xhtml", "exclusaoMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Trancamento
	 *
	 */
	TRANCAMENTO("Trancamento", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Trancamento_titulo"), UteisJSF.internacionalizar("per_Trancamento_ajuda"),
			new String[] { "trancamentoCons.xhtml", "trancamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
//	MAPA_REGISTRO_ABANDONO_CURSO_TRANCAMENTO("MapaRegistroAbandonoCursoTrancamento",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_MapaRegistroAbandonoCursoTrancamento_titulo"),
//					UteisJSF.internacionalizar("per_MapaRegistroAbandonoCursoTrancamento_ajuda"),
//					new String[] { "mapaRegistroAbandonoCursoTrancamentoForm.xhtml" }) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRANCAMENTO,
//			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	TRANCAMENTO_ESTORNAR("Trancamento_Estornar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TrancamentoEstornar_titulo"),
					UteisJSF.internacionalizar("per_TrancamentoEstornar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.TRANCAMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	MAPA_REGISTRO_EVASAO_CURSO("MapaRegistroEvasaoCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaRegistroEvasaoCurso_titulo"),
					UteisJSF.internacionalizar("per_MapaRegistroEvasaoCurso_ajuda"),
					new String[] { "mapaRegistroEvasaoCursoCons.xhtml", "mapaRegistroEvasaoCursoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	/**
	 * Calendario Registro Aula
	 *
	 */
	CALENDARIO_REGISTRO_AULA("CalendarioRegistroAula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CalendarioRegistroAula_titulo"),
					UteisJSF.internacionalizar("per_CalendarioRegistroAula_ajuda"),
					new String[] { "calendarioRegistroAulaCons.xhtml", "calendarioRegistroAulaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Mapa Atualizacao Matricula Formada
	 *
	 */
	MAPA_ATUALIZACAO_MATRICULA_FORMADA("MapaAtualizacaoMatriculaFormada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaAtualizacaoMatriculaFormada_titulo"),
					UteisJSF.internacionalizar("per_MapaAtualizacaoMatriculaFormada_ajuda"),
					new String[] { "mapaAtualizacaoMatriculaFormada.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	MAPA_ATUALIZACAO_MATRICULA_FORMADA_PERMITI_FORMAR_ALUNO_NAO_INTEG(
			"MapaAtualizacaoMatricula_permitirFormarAlunoCurriculoNaoInteg",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_MapaAtualizacaoMatricula_permitirFormarAlunoCurriculoNaoInteg_titulo"),
					UteisJSF.internacionalizar(
							"per_MapaAtualizacaoMatricula_permitirFormarAlunoCurriculoNaoInteg_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoAcademicoEnum.MAPA_ATUALIZACAO_MATRICULA_FORMADA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Mapa Abertura Turma
	 *
	 */
	MAPA_ABERTURA_TURMA("MapaAberturaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaAberturaTurma_titulo"),
					UteisJSF.internacionalizar("per_MapaAberturaTurma_ajuda"),
					new String[] { "mapaAberturaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	ABERTURA_TURMA_PERMITIR_USUARIO_VISUALIZAR_ABA_PENDENCIAS("AberturaTurma_PermitirUsuarioVisualizarAbaPendencias",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AberturaTurmaPermitirUsuarioVisualizarAbaPendencias_titulo"),
					UteisJSF.internacionalizar("per_AberturaTurmaPermitirUsuarioVisualizarAbaPendencias_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITIR_EXCLUIR_SITUACAO_TURMA("PermitirExcluirSituacaoTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirExcluirSituacaoTurma_titulo"),
					UteisJSF.internacionalizar("per_PermitirExcluirSituacaoTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITIR_EDITAR_SITUACAO_TURMA("PermitirEditarSituacaoTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirEditarSituacaoTurma_titulo"),
					UteisJSF.internacionalizar("per_PermitirEditarSituacaoTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_ABERTURA_TURMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	/**
	 * Mapa Relatório Facilitador
	 *
	 */
	MAPA_RELATORIO_FACILITADOR("MapaRelatorioFacilitador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaFacilitador_titulo"),
					UteisJSF.internacionalizar("per_MapaRelatorioFacilitador_ajuda"),
					new String[] { "mapaRelatorioFacilitador.xhtml"}) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	RELATORIO_FACILITADOR_PERMITIR_DEFERIR("RelatorioFacilitador_PermitirDeferir",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirDeferir_titulo"),
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirDeferir_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_RELATORIO_FACILITADOR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	RELATORIO_FACILITADOR_PERMITIR_INDEFERIR("RelatorioFacilitador_PermitirIndeferir",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirIndeferir_titulo"),
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirIndeferir_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_RELATORIO_FACILITADOR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	RELATORIO_FACILITADOR_PERMITIR_CORRECAO_ALUNO("RelatorioFacilitador_PermitirCorrecaoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirCorrecaoAluno_titulo"),
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirCorrecaoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_RELATORIO_FACILITADOR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	RELATORIO_FACILITADOR_PERMITIR_ACESSO_SUPERVISORES("RelatorioFacilitador_PermitirAcessoSupervisores",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirAcessoSupervisores_titulo"),
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirAcessoSupervisores_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_RELATORIO_FACILITADOR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	RELATORIO_FACILITADOR_PERMITIR_SUSPENSAO_BOLSA("RelatorioFacilitador_PermitirSuspensaoBolsa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirSuspensaoBolsa_titulo"),
					UteisJSF.internacionalizar("per_RelatorioFacilitador_PermitirSuspensaoBolsa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MAPA_RELATORIO_FACILITADOR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	/**
	 * MapaReposicao
	 *
	 */
	MAPA_REPOSICAO("MapaReposicao",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("Menu_MapaReposicao"),
							UteisJSF.internacionalizar("per_Administrativo_ajudaMapaReposicaoVisaoAdministrativa"),
							new String[] { "mapaReposicao.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	/**
	 * InclusaoHistoricoAluno
	 *
	 */
	INCLUSAO_HISTORICO_ALUNO("InclusaoHistoricoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InclusaoHistoricoAluno_titulo"),
					UteisJSF.internacionalizar("per_InclusaoHistoricoAluno_ajuda"),
					new String[] { "inclusaoHistoricoAlunoCons.xhtml", "inclusaoHistoricoAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Cancelamento
	 *
	 */
	CANCELAMENTO("Cancelamento", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Cancelamento_titulo"), UteisJSF.internacionalizar("per_Cancelamento_ajuda"),
			new String[] { "cancelamentoCons.xhtml", "cancelamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	CANCELAMENTO_ESTORNAR("Cancelamento_Estornar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CancelamentoEstornar_titulo"),
					UteisJSF.internacionalizar("per_CancelamentoEstornar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CANCELAMENTO,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Mapa Convocacao Enade
	 *
	 */
	MAPA_CONVOCACAO_ENADE("MapaConvocacaoEnade",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaConvocacaoEnade_titulo"),
					UteisJSF.internacionalizar("per_MapaConvocacaoEnade_ajuda"),
					new String[] { "mapaConvocacaoEnadeCons.xhtml", "mapaConvocacaoEnadeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Mapa Local Mapa Documentação Digitalizado GED
	 *
	 */
	MAPA_DOCUMENTACAO_DIGITALIZADO_GED("MapaDocumentacaoDigitalizadoGED",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaDocumentacaoDigitalizadoGED_titulo"),
					UteisJSF.internacionalizar("per_MapaDocumentacaoDigitalizadoGED_ajuda"),
					new String[] { "mapaDocumentacaoDigitalizadoGEDCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	/**
	 * Calendario Lancamento Nota
	 *
	 */
	CALENDARIO_LANCAMENTO_NOTA("CalendarioLancamentoNota",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CalendarioLancamentoNota_titulo"),
					UteisJSF.internacionalizar("per_CalendarioLancamentoNota_ajuda"),
					new String[] { "calendarioLancamentoNotaCons.xhtml", "calendarioLancamentoNotaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Alterar Aproveitamento Disciplina
	 *
	 */
	ALTERAR_APROVEITAMENTO_DISCIPLINA("AlterarAproveitamentoDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarAproveitamentoDisciplina_titulo"),
					UteisJSF.internacionalizar("per_AlterarAproveitamentoDisciplina_ajuda"),
					new String[] { "disciplinaAproveitadaAlteradaMatriculaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Registro Aula
	 *
	 */
	REGISTRO_AULA("RegistroAula", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_RegistroAula_titulo"),
					UteisJSF.internacionalizar("per_RegistroAula_ajuda"),
					new String[] { "registroAulaCons.xhtml", "registroAulaForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_RegistroAula_titulo"),
					UteisJSF.internacionalizar("per_RegistroAula_ajuda"),
					new String[] { "registrarAulaProfessor.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_RegistroAula_titulo"),
					UteisJSF.internacionalizar("per_RegistroAula_ajuda"),
					new String[] { "registrarAulaCoordenador.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	REGISTRO_AULA_VISUALIZAR_DATA_MATRICULA("RegistrarAulaVisualizarDataMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
					UteisJSF.internacionalizar("per_RegistroAulaVisualizarDataMatricula_titulo"),
					UteisJSF.internacionalizar("per_RegistroAulaVisualizarDataMatricula_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	REGISTRO_AULA_ALTERAR_CARGA_HORARIA("RegistroAulaAlterarCargaHoraria",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_RegistroAulaAlterarCargaHoraria_titulo"),
							UteisJSF.internacionalizar("per_RegistroAulaAlterarCargaHoraria_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_RegistroAulaAlterarCargaHoraria_titulo"),
							UteisJSF.internacionalizar("per_RegistroAulaAlterarCargaHoraria_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_RegistroAulaAlterarCargaHoraria_titulo"),
							UteisJSF.internacionalizar("per_RegistroAulaAlterarCargaHoraria_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITIR_REGISTRAR_AULA_RETROATIVO("PermitirRegistrarAulaRetroativo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_PermitirRegistrarAulaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirRegistrarAulaRetroativo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_PermitirRegistrarAulaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirRegistrarAulaRetroativo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirRegistrarAulaRetroativo_titulo"),
							UteisJSF.internacionalizar("per_PermitirRegistrarAulaRetroativo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_LANCAMENTO_AULA_FUTURA("PermiteLancamentoAulaFutura",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteLancamentoAulaFutura_titulo"),
					UteisJSF.internacionalizar("per_PermiteLancamentoAulaFutura_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	REGISTRO_AULA_PERMITIR_VISUALIZAR_FALTA_ALUNO_OUTRO_PROFESSOR("RegistrarAulaPermitirVisualizarFaltarAlunoOutroProfessor", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_RegistroAulaPermitirVisualizarFaltarAlunoOutroProfessor_titulo"),UteisJSF.internacionalizar("per_RegistroAulaPermitirVisualizarFaltarAlunoOutroProfessor_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA, 
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	/**
	 * Parceiro
	 *
	 */
	PERMITIR_REALIZAR_IMPRESSAO_CONTRATO("PermitirRealizarImpressaoContrato",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirRealizarImpressaoContrato_titulo"),
					UteisJSF.internacionalizar("per_PermitirRealizarImpressaoContrato_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.PARCEIRO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ESTAGIO),
	/**
	 * ArquivoAssinado
	 *
	 */
	ARQUIVOASSINADO("ArquivoAssinado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_arquivoAssinado_titulo"),
					UteisJSF.internacionalizar("per_arquivoAssinado_ajuda"),
					new String[] { "documentoAssinado.xhtml", "documentoAssinado.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_LANCAR_NOTA_ALUNO_TRANSFERENCIA_MATRIZ("PermiteLancarNotaAlunoTransferenciaMatriz",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LancamentoNota_permiteLancarNotaAlunoTransferenciaMatriz_titulo"),
					UteisJSF.internacionalizar("per_LancamentoNota_permiteLancarNotaAlunoTransferenciaMatriz_ajuda"),
					new String[] { "lancamentoNota.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.LANCAMENTO_NOTA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITE_REGISTRAR_AULA_ALUNO_TRANSFERENCIA_MATRIZ("PermiteRegistrarAulaAlunoTransferenciaMatriz",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroAula_permiteRegistrarAulaAlunoTransferenciaMatriz_titulo"),
					UteisJSF.internacionalizar("per_RegistroAula_permiteRegistrarAulaAlunoTransferenciaMatriz_ajuda"),
					new String[] { "registroAulaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.REGISTRO_AULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	GESTAO_TURMA("GestaoTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GestaoTurma_titulo"),
					UteisJSF.internacionalizar("per_GestaoTurma_ajuda"), new String[] { "gestaoTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),

	PERMITIR_EXCLUIR_PRE_MATRICULA_CANCELADA("PermitirExcluirPreMatriculaCancelada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirExcluirPreMatriculaCancelada_titulo"),
					UteisJSF.internacionalizar("per_PermitirExcluirPreMatriculaCancelada_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CONFIRMACAO_PRE_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	PERMITIR_EXCLUIR_PRE_MATRICULA("PermitirExcluirPreMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirExcluirPreMatricula_titulo"),
					UteisJSF.internacionalizar("per_PermitirExcluirPreMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.CONFIRMACAO_PRE_MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	LIBERAR_MATRICULA_TURMA_BASE_SEM_VAGA_SEM_AULA("Matricula_LiberarMatriculaTurmaBaseSemVagaSemAula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarMatriculaTurmaBaseSemVagaSemAula_titulo"),
					UteisJSF.internacionalizar("per_LiberarMatriculaTurmaBaseSemVagaSemAula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),

	ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR_PERMITI_DEFERIR("RegistroAtividadeComplementarMatricula_permiteDeferir",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permiteDeferir_titulo"),
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permiteDeferir_ajuda"),
					new String[] { "acompanhamentoAtividadeComplementarCoordenador.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_RegistroAtividadeComplementarMatricula_permiteDeferir_titulo"),
							UteisJSF.internacionalizar(
									"per_RegistroAtividadeComplementarMatricula_permiteDeferir_ajuda"),
							new String[] { "acompanhamentoAtividadeComplementarCons.xhtml",
									"acompanhamentoAtividadeComplementarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR_PERMITI_INDEFERIR("RegistroAtividadeComplementarMatricula_permiteIndeferir",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permiteIndeferir_titulo"),
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permiteIndeferir_ajuda"),
					new String[] { "acompanhamentoAtividadeComplementarCoordenador.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar(
									"per_RegistroAtividadeComplementarMatricula_permiteIndeferir_titulo"),
							UteisJSF.internacionalizar(
									"per_RegistroAtividadeComplementarMatricula_permiteIndeferir_ajuda"),
							new String[] { "acompanhamentoAtividadeComplementarCons.xhtml",
									"acompanhamentoAtividadeComplementarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR_PERMITIR_EDITAR_CH_CONSIDERADA("RegistroAtividadeComplementarMatricula_permitirEditarCHConsiderada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permitirEditarCHConsiderada_titulo"),
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permitirEditarCHConsiderada_ajuda"),
					new String[] { "acompanhamentoAtividadeComplementarCons.xhtml",
					"acompanhamentoAtividadeComplementarForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permitirEditarCHConsiderada_titulo"),
					UteisJSF.internacionalizar("per_RegistroAtividadeComplementarMatricula_permitirEditarCHConsiderada_ajuda"),
					new String[] { "acompanhamentoAtividadeComplementarCoordenador.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, ACOMPANHAMENTO_ATIVIDADE_COMPLEMENTAR,
			PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	MATRICULA_PERMITIR_ALTERAR_CONTRATO_MATRICULA("Matricula_PermitirAlterarContratoMatricula",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_MatriculaPermitirAlterarContratoMatricula_titulo"),
							UteisJSF.internacionalizar("per_MatriculaPermitirAlterarContratoMatricula_ajuda"))},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS), 
	
	IMPRESSAO_CONTRATO_PERMITIR_ALTERAR_CONTRATO_MATRICULA("ImpressaoContrato_PermitirAlterarContratoMatricula", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ImpressaoContrato_PermitirAlterarContratoMatricula_titulo"),
			UteisJSF.internacionalizar("per_ImpressaoContrato_PermitirAlterarContratoMatricula_ajuda"), new String[] { "impressaoContrato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.IMPRESSAO_CONTRATO, PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	UNIFICACAO_CADASTRO_PESSOA("UnificacaoCadastroPessoa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_UnificacaoCadastroPessoa_titulo"),
					UteisJSF.internacionalizar("per_UnificacaoCadastroPessoa_ajuda"),
					new String[] { "unificacaoCadastroPessoa.xhtml", "unificacaoCadastroPessoa.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	/**
	 * Acesso Material Rel
	 *
	 */
	ACESSO_MATERIAL_REL("DownloadRel", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_DownloadRel_titulo"),
					UteisJSF.internacionalizar("per_DownloadRel_ajuda"), new String[] { "acessoMaterialCoordenadorRel.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_DownloadRel_titulo"),
					UteisJSF.internacionalizar("per_DownloadRel_ajuda"), new String[] { "acessoMaterialProfessor.xhtml" })
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_RELATORIOS_ACADEMICO),
	
	PERMITIR_ALUNO_REGULAR_ALTERAR_SUBTURMA_MATRICULA("PermitirAlunoRegularAlterarSubturmaMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_PermitirAlunoRegularAlterarSubturmaMatricula_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlunoRegularAlterarSubturmaMatricula_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	
	PERMITIR_LIBERAR_VALIDACAO_ALUNO_COM_BLOQUEIO_BIBLIOTECA("Matricula_PermitirLiberarValidacaoAlunoComBloqueioBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirLiberarValidacaoAlunoComBloqueioBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PermitirLiberarValidacaoAlunoComBloqueioBiblioteca_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.MATRICULA,
			PerfilAcessoSubModuloEnum.ACADEMICO_MATRICULAS),
	
	ASSINAR_DIGITALMENTE_PLANO_ENSINO("AssinarDigitalmentePlanoEnsino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_AssinarDigitalmentePlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_AssinarDigitalmentePlanoEnsino_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),
	
	CALENDARIO_LANCAMENTO_PLANO_ENSINO("CalendarioLancamentoPlanoEnsino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CalendarioLancamentoPlanoEnsino_titulo"),
					UteisJSF.internacionalizar("per_CalendarioLancamentoPlanoEnsino_ajuda"), new String[] { "calendarioLancamentoPlanoEnsinoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_CURSOS),

	GESTAO_SALAS_NOTAS_BLACKBOARD("GestaoSalasNotasBlackboard",
											   new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_GestaoSalasNotasBlackboard_titulo"),
			UteisJSF.internacionalizar("per_GestaoSalasNotasBlackboard_ajuda"), new String[] { "gestaoSalasNotasBlackboard.xhtml" }) },
	TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	PERMITE_OPERACOES_GRUPO_TCC("PermiteOperacoesGrupoTcc", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteOperacoesGrupoTcc_titulo"),
			UteisJSF.internacionalizar("per_PermiteOperacoesGrupoTcc_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	PERMITE_OPERACOES_GRUPO_PROJETO_INTEGRADOR("PermiteOperacoesGrupoProjetoIntegrador", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteOperacoesGrupoProjetoIntegrador_titulo"),
			UteisJSF.internacionalizar("per_PermiteOperacoesGrupoProjetoIntegrador_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	PERMITE_EXCLUIR_SALA_AULA_BLACKBOARD("PermiteExcluirSalaAulaBlackboard", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteExcluirSalaAulaBlackboard_titulo"),
			UteisJSF.internacionalizar("per_PermiteExcluirSalaAulaBlackboard_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	PERMITE_CRIAR_SALA_BLACKBOARD("PermiteCriarSalaBlackboard", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteCriarSalaBlackboard_titulo"),
			UteisJSF.internacionalizar("per_PermiteCriarSalaBlackboard_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	PERMITE_APURAR_NOTAS_BLACKBOARD("PermiteApurarNotasBlackboard", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteApurarNotasBlackboard_titulo"),
			UteisJSF.internacionalizar("per_PermiteApurarNotasBlackboard_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	PERMITE_COPIAR_CONTEUDO_BLACKBOARD("PermiteCopiarConteudoBlackboard", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermiteCopiarConteudoBlackboard_titulo"),
			UteisJSF.internacionalizar("per_PermiteCopiarConteudoBlackboard_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	PERMITIR_FECHAMENTO_NOTA_BLACKBOARD("PermitirFechamentoNotaBlackBoard", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PermitirFechamentoNotaBlackBoard_titulo"),
			UteisJSF.internacionalizar("per_PermitirFechamentoNotaBlackBoard_ajuda"))}, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.GESTAO_SALAS_NOTAS_BLACKBOARD, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),
	
	APRESENTAR_CONFIGURACAOGED_ALUNO("ApresentarConfiguracaoGEDAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ApresentarConfiguracaoGEDAluno_titulo"),
					UteisJSF.internacionalizar("per_ApresentarConfiguracaoGEDAluno_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.ALUNO,
			PerfilAcessoSubModuloEnum.ACADEMICO_ALUNO),

	MOTIVO_INDEFERIMENTO_DOCUMENTO_ALUNO("MotivoIndeferimentoDocumentoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MotivoIndeferimentoDocumentoAluno_titulo"),
					UteisJSF.internacionalizar("per_MotivoIndeferimentoDocumentoAluno_ajuda"),
					new String[] { "motivoIndeferimentoDocumentoAlunoCons.xhtml", "motivoIndeferimentoDocumentoAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_SECRETARIA),

	OFERTA_DISCIPLINA("OfertaDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OfertaDisciplina_titulo"),
					UteisJSF.internacionalizar("per_OfertaDisciplina_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_EXCLUIR_OFERTA_DISCIPLINA_COM_ALUNO("PermiteExcluirOfertaDisciplinaComAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OfertaDisciplina_PermiteExcluirOfertaDisciplinaComAluno_titulo"),
					UteisJSF.internacionalizar("per_OfertaDisciplina_PermiteExcluirOfertaDisciplinaComAluno_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, OFERTA_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_INCLUIR_OFERTA_DISCIPLINA_HISTORICO_ALUNO("PermiteIncluirOfertaDisciplinaHistoricoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OfertaDisciplina_PermiteIncluirOfertaDisciplinaHistoricoAluno_titulo"),
					UteisJSF.internacionalizar("per_OfertaDisciplina_PermiteIncluirOfertaDisciplinaHistoricoAluno_ajuda")), 
					},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, OFERTA_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	PERMITE_INCLUIR_OFERTA_DISCIPLINA_TURMA("PermiteCriarOfertaDisciplinaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OfertaDisciplina_PermiteCriarOfertaDisciplinaTurma_titulo"),
					UteisJSF.internacionalizar("per_OfertaDisciplina_PermiteCriarOfertaDisciplinaTurma_ajuda")), 
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, OFERTA_DISCIPLINA,
			PerfilAcessoSubModuloEnum.ACADEMICO_TURMA),
	/**
	 * @author Felipi Alves
	 *
	 */
	CONFIGURACAO_DIPLOMA_DIGITAL("ConfiguracaoDiplomaDigital",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoDiplomaDigital_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoDiplomaDigital_ajuda"),
					new String[] { "ConfiguracaoDiplomaDigitalCons.xhtml", "ConfiguracaoDiplomaDigitalForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	GESTAO_XML_GRADE_CURRICULAR("GestaoXmlGradeCurricular",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GestaoXmlGradeCurricular_titulo"),
					UteisJSF.internacionalizar("per_GestaoXmlGradeCurricular_ajuda"),
					new String[] { "gestaoXmlGradeCurricular.xhtml"}) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),
	
	/**
	 * Funcionalidade de registro anulação de diploma
	 * @author Felipi Alves
	 *
	 */
	PERMITE_REGISTRAR_ANULACAO_DIPLOMA("PermitirRegistroAnulacaoDiploma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirRegistroAnulacaoDiploma_titulo"),
					UteisJSF.internacionalizar("per_PermitirRegistroAnulacaoDiploma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoAcademicoEnum.EXPEDICAO_DIPLOMA,
			PerfilAcessoSubModuloEnum.ACADEMICO_FORMATURA),;
	
	
	
	private PerfilAcessoPermissaoAcademicoEnum(String valor, PermissaoVisao[] permissaoVisao,
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
			if (permissaoVisao.getTipoVisaoEnum().equals(tipoVisaoEnum)) {
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
				if (permissaoVisao2.getTipoVisaoEnum().equals(tipoVisaoEnum)) {
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
		if(descricaoModulo == null) {
		if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
			descricaoModulo += getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar();				
		}else {
			descricaoModulo ="";
		}
		}
		return descricaoModulo;
	}

	public String descricaoSubModulo;
	@Override
	public String getDescricaoSubModulo() {
		if(descricaoSubModulo == null) {
			if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
				
				descricaoSubModulo = getPerfilAcessoSubModulo().getDescricao();	
			
			}else {
				descricaoSubModulo ="";
			}
			}
			return descricaoSubModulo;
	}	
	
	public String descricaoModuloSubModulo;

	@Override
	public String getDescricaoModuloSubModulo() {
		if(descricaoModuloSubModulo == null) {
		if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
			descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()+" - "+getPerfilAcessoSubModulo().getDescricao();	
			
		}else {
			descricaoModuloSubModulo ="";
		}
		}
		return descricaoModuloSubModulo;
	}	
}
