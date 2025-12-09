package negocio.interfaces.ead;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;

/**
 * @author Victor Hugo 11/11/2014
 */
public interface ProgramacaoTutoriaOnlineInterfaceFacade {

	void incluir(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	

	void alterar(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	ProgramacaoTutoriaOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ProgramacaoTutoriaOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	ProgramacaoTutoriaOnlineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorIdentificadorTurma(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorNivelEducacional(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ProgramacaoTutoriaOnlineVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void alterarOrdemPrioridadeTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO2);	

	ProgramacaoTutoriaOnlineVO consultarProgramacaoTutoriaOnlinePorMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarDefinicaoProfessorTutoriaOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, Boolean retornarExcecao, boolean consultarDisciplinaEquivalente) throws Exception;

	void removerTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, UsuarioVO usuarioVO) throws Exception;	

	void ativarProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception;

	void desativarProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception;

	void realizarRedistruibuicaoAlunosEntreTutoresAtivos(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, UsuarioVO usuarioVO) throws Exception;

	void atutalizarSituacaoTutoriaProgramacaoTutoriaOnlineProfessor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public boolean isProgramacaoTutoriaOnlineValidoHorario(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataTermino); 

	public List<ProgramacaoTutoriaOnlineVO> consultarPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, int nivelMontarDados, UsuarioVO usuarioLogado, boolean validarAulaNaoRegistrada) throws Exception;
	
	public Integer programacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception;



	/**
	 * Método responsável por localizar a programação tutoria on-line configurada priorizando a programação configurada para disciplina da turma do semestre.
	 * O mesmo será chamado no momento em que o aluno acessar o estudo on-line pela primera vez.
	 * 
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	ProgramacaoTutoriaOnlineVO consultarProgramacaoTutoriaOnlinePorTurmaDisciplinaAnoSemestre(TurmaVO turma, Integer disciplina, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;



	void executarCargaProgramacaoTutoriaOnlineClassroomAutomatico(ProgramacaoTutoriaOnlineVO obj, UsuarioVO usuario) throws Exception;
	
	void executarCargaProgramacaoTutoriaOnlineSalaAulaBlackboardAutomatico(ProgramacaoTutoriaOnlineVO obj, Integer bimestre, UsuarioVO usuario) throws Exception;
	
}
