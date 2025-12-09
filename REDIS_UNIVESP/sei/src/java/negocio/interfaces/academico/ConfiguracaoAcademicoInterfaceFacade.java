package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ConfiguracaoAcademicoInterfaceFacade {

	public ConfiguracaoAcademicoVO novo() throws Exception;

	public void incluir(ConfiguracaoAcademicoVO obj, UsuarioVO usuario) throws Exception;

	public void incluir(List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs, UsuarioVO usuario) throws Exception;

	public void alterar(ConfiguracaoAcademicoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs, UsuarioVO usuario) throws Exception;

	public void excluir(ConfiguracaoAcademicoVO obj, UsuarioVO usuario) throws Exception;

	public List<ConfiguracaoAcademicoVO> consultarPorCodigoConfiguracoes(Integer condigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public ConfiguracaoAcademicoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	@Deprecated
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarConfiguracoesASeremUsadas(boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public Integer verificarPeriodoReativacaoMatricula(Integer diasMaximoReativacaoMatricula, Date data, Date date, UsuarioVO usuario);

	public void excluirConfiguracaoAcademico(List listaObjetos, Integer configuracoes);

	public Boolean consultarPorMatriculaCurso(String matricula, UsuarioVO usuario) throws Exception;

	public List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Boolean consultarPermissaoCursarDisciplinaPreRequisito(Integer codigo, UsuarioVO usuario) throws Exception;

	public Boolean consultarPermissaoPreRequisitoDisciplinaConcomitante(Integer codigo, UsuarioVO usuario) throws Exception;

	void adicionarConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO, ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO, TipoNotaConceitoEnum tipoNotaConceito, Boolean mediaFinal) throws Exception;

	void removerConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO, ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO, TipoNotaConceitoEnum tipoNotaConceito) throws Exception;

	public ConfiguracaoAcademicoVO consultarPorCodigoCurso(Integer codigoCurso, UsuarioVO usuario) throws Exception;

	public ConfiguracaoAcademicoVO consultarPorDisciplinaMatriculaPeriodoAlunoVinculadoGradeDisciplina(Integer disciplina, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

	public ConfiguracaoAcademicoVO obterConfiguracaoAcademicoPorHistoricoTurmaDisciplinaCurso(HistoricoVO historicoVO, TurmaVO turmaVO, GradeDisciplinaVO gradeDisciplinaVO, CursoVO cursoVO, UsuarioVO usuarioVO);

	Integer consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(Integer matriculaPeriodo, Integer disciplina) throws Exception;

	Integer consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatrizDisciplinaTurmaCurso(Integer matrizCurricular, Integer disciplina, Integer turma, Integer curso) throws Exception;

        public List<ConfiguracaoAcademicoVO> consultarPorUnidadeEnsinoNivelCombobox(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

        public void prepararVariaveisNotaParaSubstituicaoFormulaNota(ConfiguracaoAcademicoVO cfgAcademico, HistoricoVO historico, UsuarioVO usuario) throws Exception;

		/**
		 * @author Wellington Rodrigues - 17/03/2015
		 * @param codigoTurma
		 * @param turmaAgrupada
		 * @param semestre
		 * @param ano
		 * @param disciplina
		 * @param unidadeEnsino
		 * @param controlarAcesso
		 * @param usuario
		 * @return
		 * @throws Exception
		 */
		List<ConfiguracaoAcademicoVO> consultarExistenciaMaisDeUmaConfiguracaoAcademicoHistoricoDiario(TurmaVO turmaVO, String semestre, String ano, Integer disciplina, String filtroTipoCursoAluno, String tipoAluno, String tipoLayout, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, boolean trazerAlunoTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
		
		public List<String> consultarVariavelNotaPorConfiguracaoAcademico(Integer configuracaoAcademico, UsuarioVO usuarioVO);
		
		public String consultarNotaUtilizarPorConfiguracaoAcademicoTituloNota(Integer configuracaoAcademico, String tituloNota, UsuarioVO usuarioVO) throws Exception;

		/** 
		 * @author Wellington Rodrigues - 10 de jul de 2015 
		 * @param turma
		 * @param controlarAcesso
		 * @param nivelMontarDados
		 * @param usuario
		 * @return
		 * @throws Exception 
		 */
		List<ConfiguracaoAcademicoVO> consultarPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		/** 
		 * @author Wellington Rodrigues - 17 de jul de 2015 
		 * @param unidadeEnsino
		 * @param controlarAcesso
		 * @param nivelMontarDados
		 * @param usuario
		 * @return
		 * @throws Exception 
		 */
		List<ConfiguracaoAcademicoVO> consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		/** 
		 * @author Wellington Rodrigues - 17 de jul de 2015 
		 * @param disciplina
		 * @param controlarAcesso
		 * @param nivelMontarDados
		 * @param usuario
		 * @return
		 * @throws Exception 
		 */
		List<ConfiguracaoAcademicoVO> consultarPorDisciplinaPorTurma(Integer disciplina, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		/** 
		 * @author Wellington Rodrigues - 17 de jul de 2015 
		 * @param curso
		 * @param controlarAcesso
		 * @param usuario
		 * @return
		 * @throws Exception 
		 */
		List<ConfiguracaoAcademicoVO> consultarPorCurso(Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

		public Boolean consultarPermissaoBloquearRegistroAulaAnteriorDataMatricula(Integer codigo, UsuarioVO usuario) throws Exception;		

		/** 
		 * @author Victor Hugo de Paula Costa - 10 de ago de 2016 
		 * @param codigoPrm
		 * @param usuario
		 * @return
		 * @throws Exception 
		 */
		ConfiguracaoAcademicoVO consultarPorChavePrimariaDadosMinimos(Integer codigoPrm, UsuarioVO usuario) throws Exception;

		List<SelectItem> montarListaSelectItemOpcoesDeNotas(ConfiguracaoAcademicoVO ca, boolean somenteNotaTipoLancamento, TipoUsoNotaEnum tipoUsoNota);

		/** 
		 * @author Victor Hugo de Paula Costa - 12 de set de 2016 
		 * @param codigoDisciplina
		 * @param codigoTurma
		 * @param codigoCurso
		 * @param nivelMontarDados
		 * @param usuarioVO
		 * @return
		 * @throws Exception 
		 */
		ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoASerUsadaLancamentoNota(Integer codigoDisciplina, Integer codigoTurma, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

		/** 
		 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
		 * @param matricula
		 * @param nivelMontarDados
		 * @param usuarioVO
		 * @return
		 * @throws Exception 
		 */
		ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoPorMatriculaCurso(String matricula, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

		List<ConfiguracaoAcademicoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(
				Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre,
				Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina,
				boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO,
				Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta, boolean permitirRealizarLancamentoAlunosPreMatriculados, int nivelMontarDados,
				UsuarioVO usuario) throws Exception;

		List<ConfiguracaoAcademicoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessor(
				Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre,
				String situacaoMatricula, String situacaoHistorico, boolean verificarDisciplina,
				boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiramente,boolean permitiVisualizarAlunoTR_CA,boolean controlarAcesso,
				int nivelMontarDados, UsuarioVO usuario) throws Exception;		
		ConfiguracaoAcademicoVO realizarDefinicaoConfiguracaoAcademicaVincularHistoricoAluno(CursoVO cursoVO,
				GradeDisciplinaVO gradeDisciplinaVO,
				GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO,
				GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuario)
				throws Exception;
		public List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicaLancamentoNotaMobile(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

		public Integer consultarQuantidadeCasasDecimaisConfiguracaoAcademico(Integer codigoPrm, UsuarioVO usuario) throws Exception;

		List<SelectItem> consultarVariavelTituloJaRegistradasForumRegistroNotaPorForumTurmaAnoSemestre(List<SelectItem> listaSelectItemTipoInformarNota, ForumVO forum, TurmaVO turma, String ano, String semestre, UsuarioVO usuarioVO);

		List<ConfiguracaoAcademicoVO> consultarPorDisciplinaPorTurmaConfiguracaoTurmaDisciplinaOuGradeDisciplina(Integer disciplina, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		ConfiguracaoAcademicoVO consultarConfiguracaoVinculadaCursoPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

		ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorTurma(Integer turma)
				throws Exception;
		
		public Double realizarCalculoCoeficienteRendimento(String formulaBase, List<HistoricoVO> historicoVOs) throws Exception;
		
		public List<ConfiguracaoAcademicoVO> consultarTodasConfiguracaoAcademica(int nivelMontarDados,boolean controlarAcesso, UsuarioVO usuario) throws Exception;



		ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception;

		ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(String matricula, String ano, String semestre) throws Exception;
		
		public ConfiguracaoAcademicoVO consultarPorCodigoTurmaAgrupada(Integer codigoTurmaAgrupada, UsuarioVO usuario) throws Exception;

		List<SelectItem> consultarVariavelTituloConfiguracaoAcademicoPorTipoUsoNota(TipoUsoNotaEnum tipoUsoNotaEnum,
				UsuarioVO usuarioVO);

		ConfiguracaoAcademicoVO consultarMascaraNumeroRegistroDiplomaENumeroProcessoExpedicaoDiplomaPorCurso(Integer curso, boolean controlarAcesso, UsuarioVO usuario)	throws Exception;

		ConfiguracaoAcademicoVO consultarPorChavePrimariaUnica(Integer codigoPrm, UsuarioVO usuario) throws Exception;
}