package relatorio.negocio.interfaces.academico;

import java.io.File;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface DiarioRelInterfaceFacade {

	List<DiarioRegistroAulaVO> consultarRegistroAula(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, Integer disciplina, TurmaVO turmaVO, String semestre, String ano, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoDiario, String tipoAluno, String mes, String anoMes, Integer configuracaoAcademico, Boolean apresentarAulasNaoRegistradas, Boolean apresentarSituacaoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim, List<String> filtroNotasPorBimestre) throws Exception;

	List<DiarioRegistroAulaVO> consultarRegistroAulaEspelho(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	List<RegistroAulaVO> consultarRegistroAulaVerso(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, Integer disciplina, TurmaVO turmaVO, String semestre, String ano, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String mes, String mesAno, Integer configuracaoAcademico, Boolean apresentarAulasNaoRegistradas, Boolean apresentarSituacaoMatricula, String tipoLayout, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim , boolean apresentarVersoAgrupadoPorData) throws Exception;

	void validarDadosRelatorio(TurmaVO turmaVO, String semestre, String ano, Integer disciplina, UnidadeEnsinoVO unidadeEnsinoVO, boolean possuiDiversidadeConfiguracaoAcademico, Integer configuracaoAcademico) throws Exception;

	ProfessorTitularDisciplinaTurmaVO consultarProfessorTitularTurma(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcessao, UsuarioVO usuario) throws Exception;

	List<DiarioRegistroAulaVO> consultarRegistroAulaVisaoAdministrativa(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, Integer disciplina, TurmaVO turmaVO, String semestre, String ano, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoDiario, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, Boolean apresentarAulasNaoRegistradas, Boolean apresentarSituacaoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim, List<String> filtroNotasPorBimestre) throws Exception;

	/**
	 * @author Rodrigo Wind - 14/03/2016
	 * @param turmaVO
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param layoutEtiquetaVO
	 * @param numeroCopias
	 * @param linha
	 * @param coluna
	 * @param removerEspacoTAGVazia
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	String realizarImpressaoEtiqueta(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, LayoutEtiquetaVO layoutEtiquetaVO, String tipoLayout, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Rodrigo Wind - 15/03/2016
	 * @param turmaVO
	 * @param disciplina
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	Integer obterCargaHorariaDisciplina(TurmaVO turmaVO, Integer disciplina, UsuarioVO usuarioVO) throws Exception;

	String executarAssinaturaDiarios(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, List<PessoaVO> professor, File fileAssinar, ConfiguracaoGeralSistemaVO config, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, boolean validarAulasRegistradas, boolean pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, UsuarioVO usuarioVO) throws Exception;


}