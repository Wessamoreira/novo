package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TransferenciaTurmaVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface TransferenciaTurmaInterfaceFacade {

	List<TransferenciaTurmaVO> consultar(String valorConsulta, String campoConsulta, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception;

	TransferenciaTurmaVO consultaRapidaPorChavePrimaria(Integer codigo, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	Boolean imprimirDeclaracaoTransferenciaTurma(TransferenciaTurmaVO transferenciaTurmaVO, Integer textoPadraoDeclaracao, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 15/06/2015
	 * @param obj
	 * @param existeRegistroAula
	 * @param realizarAbonoRegistroAula
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirTransferenciaTurmaVOs(List<TransferenciaTurmaVO> transferenciaTurmaVOs, boolean alterarTurmaOrigem, Integer turmaDestino, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception, ConsistirException;

	/**
	 * @author Wellington Rodrigues - 16/06/2015
	 * @param transferenciaTurmaVOs
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	boolean verificarRegistroAulaParaAbono(List<TransferenciaTurmaVO> transferenciaTurmaVOs, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 16/06/2015
	 * @param transferenciaTurmaVO
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<TransferenciaTurmaVO> executarMontagemDisciplinasParaTransferencia(TransferenciaTurmaVO transferenciaTurmaVO, boolean controlarAcesso, UsuarioVO usuarioVO, Boolean permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais) throws Exception;

	/**
	 * @author Wellington Rodrigues - 17/06/2015
	 * @param transferenciaTurmaVOs
	 * @param objRemover
	 * @throws Exception
	 */
	void removerTransferenciaTurmaVOs(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TransferenciaTurmaVO objRemover) throws Exception;

	/**
	 * @author Wellington - 21 de jan de 2016
	 * @param transferenciaTurmaVOs
	 * @param transferenciaTurmaVO
	 * @param usuarioVO
	 * @throws ConsistirException
	 */
	void executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioConsiderandoTurmaTeoricaEPratica(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TransferenciaTurmaVO transferenciaTurmaVO, UsuarioVO usuarioVO) throws ConsistirException;

	/**
	 * @author Wellington - 21 de jan de 2016
	 * @param transferenciaTurmaVOs
	 * @param turmaDestino
	 * @param disciplinaVO
	 * @param ultimaMatriculaPeriodoAtiva
	 * @param tipoSubTurmaEnum
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(List<TransferenciaTurmaVO> transferenciaTurmaVOs, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, TipoSubTurmaEnum tipoSubTurmaEnum, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 22 de jan de 2016
	 * @param ttVO
	 * @param transferenciaTurmaVO
	 * @param obj
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarMontagemTurmaTeoricaPraticaDestino(TransferenciaTurmaVO ttVO, TransferenciaTurmaVO transferenciaTurmaVO, MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception;
	
	Integer consultarQuantidadeRegistros(String valorConsulta, String campoConsulta) throws Exception;
}
