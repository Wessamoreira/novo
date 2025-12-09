package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface PeriodoLetivoAtivoUnidadeEnsinoCursoInterfaceFacade {

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO novo() throws Exception;

	public void incluir(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception;

	public void alterar(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception;

	public void alterarListaFechamento(List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> listaFechamento, String nivelProcessoMatricula, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer codigoCurso, Integer codigoTurno, Date dataFim, Boolean realizarCalculoMediaFinalFechPeriodo, UsuarioVO usuario, String nivelEducacionalApresentar, ProgressBarVO progressBarVO, String periodicidade) throws Exception;

	public void excluir(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception;

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPeriodoAtivo(Integer unidadeEnsino, Integer curso, Integer turno, String tipo, String mes, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirPorCodigoEUnidadeEnsinoCurso(Integer codigo, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception;

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorUnidadeEnsinoTurnoCursoAnoSemestre(String ano, String semestre, Integer codigoUnidadeEnsino, Integer codigoTurno, Integer codigoCurso, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> consultarPorUnidadeEnsinoCursoSituacaoDataFechamentoSemMatriculaPeriodoAtiva(Integer periodoLetivoAtivoUnidadeEnsinoCurso, Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turno, Date dataFimPeriodoLetivo, String ano, String semestre, String situacaoFechamentoPeriodoLetivo, String nivelEducacionalApresentar, String periodicidade) throws Exception;

	public void alterarDataPeriodoLetivoAtivoParaFinalizacao(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, Date dataAlterar, UsuarioVO usuario) throws Exception;

	void calcularDadosDiaSemanaLetiva(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) throws Exception;

	void inicializarDadosCalendarioBimestre(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, ProcessoMatriculaVO processoMatriculaVO) throws Exception;

	void calcularDadosDiaSemanaLetivaQtdeDias(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) throws Exception;

	PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorUltimaMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Date consultarDataFimPeriodoLetivoPorTurmaAnoSemestre(Integer turma, String ano, String semestre) throws Exception;

	void alterarDataFimPeriodoLetivoAtivoParaFinalizacao(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception;
	
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarDataInicioDataFimPeriodoBimestrePorMatriculaPeriodoUnidadeEnsinoCurso(Integer matriculaPeriodo, Integer unidadeEnsinoCurso, UsuarioVO usuarioVO);

	/**
	 * @author Rodrigo Wind - 04/11/2015
	 * @param matricula
	 * @param ano
	 * @param semestre
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorMatriculaAnoSemestre(String matricula, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(Integer matriculaperiodoturmadisciplina, UsuarioVO usuarioVO) throws Exception;

	public void alterarDatasPeriodoLetivo(
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivolUnidadeEnsinoCursoVO, UsuarioVO usuarioLogado) throws Exception;
}
