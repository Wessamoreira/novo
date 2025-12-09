package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DistribuicaoSubturmaInterfaceFacade {

	void buscarAlunosSubturma(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<TurmaVO> subturmaVOs, TurmaVO turmaPrincipal, String ano, String semestre, Integer disciplina, String situacaoMatriculaPeriodo, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma) throws Exception;

	void realizarDistribuicaoAlunoTurma(List<TurmaVO> subturmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Integer disciplina, UsuarioVO usuarioVO) throws Exception;

	List<String> executarAlteracaoTurmaAlunoDistribuicaoSubturma(TurmaVO turmaPrincipal, List<TurmaVO> subturmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs, boolean controlarAcesso, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma, boolean removerVinculoSubturmaTeoricaPratica) throws Exception;

	void executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(TurmaVO turmaPrincipal, MatriculaPeriodoTurmaDisciplinaVO obj, List<TurmaVO> subturmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, String ano, String semestre, Integer disciplina, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma, boolean removerVinculoSubturmaTeoricaPratica) throws Exception;

	void removerAlunoSubturma(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<TurmaVO> subturmaVOs, TurmaVO subturma, MatriculaPeriodoTurmaDisciplinaVO mptd, Integer disciplina, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception;

	void removerTodosAlunosSubturma(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<TurmaVO> subturmaVOs, TurmaVO subturma, Integer disciplina, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception;

	/**
	 * @author Wellington Rodrigues - 11/03/2015
	 * @param turmaOrigem
	 * @param mptd
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaValidandoRegistroAulaAbono(TurmaVO turmaOrigem, MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception;

	/**
	 * @author Wellington Rodrigues - 11/03/2015
	 * @param turmaOrigem
	 * @param matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(TurmaVO turmaOrigem, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception;

	/**
	 * @author Wellington Rodrigues - 16/06/2015
	 * @param matricula
	 * @param turmaOrigem
	 * @param turmaDestino
	 * @param disciplina
	 * @param ultimaMatriculaPeriodo
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void verificarRegistroAulaParaAbono(String matricula, TurmaVO turmaOrigem, TurmaVO turmaDestino, Integer disciplina, MatriculaPeriodoVO ultimaMatriculaPeriodo, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 16/06/2015
	 * @param turmaOrigem
	 * @param turmaDestino
	 * @param disciplinaVO
	 * @param ultimaMatriculaPeriodoAtiva
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	boolean executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula(TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma,  Boolean validarChoqueHorarioOutraMatricula) throws Exception;

	/**
	 * @author Wellington - 14 de out de 2015
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param tipoSubTurma
	 * @throws Exception
	 */
	void executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, TipoSubTurmaEnum tipoSubTurma) throws Exception;

}
