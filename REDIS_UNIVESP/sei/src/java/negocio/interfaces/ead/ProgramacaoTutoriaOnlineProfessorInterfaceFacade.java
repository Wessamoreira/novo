package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;

/**
 * @author Victor Hugo 11/11/2014
 */
public interface ProgramacaoTutoriaOnlineProfessorInterfaceFacade {

	void incluir(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO) throws Exception;

	void persistir(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	ProgramacaoTutoriaOnlineProfessorVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ProgramacaoTutoriaOnlineProfessorVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;	

	List consultarPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistirProgramacaoTutoriaOnlineProfessores(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception;

	List<ProgramacaoTutoriaOnlineProfessorVO> consultarPorProfessoresAtivosAlunosAtivosEQtdeAlunosAtivosMenorQtdeAlunosTutoria(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception;	

	ProgramacaoTutoriaOnlineProfessorVO consultarPorProgramacaoTutoriaOnlineEProfessor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, Integer codigoProfessor, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void realizarAlteracaoTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, Integer codigoTutor, List<MatriculaPeriodoTurmaDisciplinaVO> lista, UsuarioVO usuarioVO) throws Exception;

	void atutalizarQtdAlunosTutoriaProgramacaoTutoriaOnlineProfessor(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	List<ProgramacaoTutoriaOnlineProfessorVO> consultarTutorSemProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, int nivelMontarDados, UsuarioVO usuarioVO)  throws Exception;

	void realizarDefinicaoOrdemPrioridadeProgramacaoTutoriaOnlineProfessores(List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs);

	void alterarOrdemPrioridadeProgramacaoTutoriaOnlineProfessores(List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs) throws Exception;

	

}
