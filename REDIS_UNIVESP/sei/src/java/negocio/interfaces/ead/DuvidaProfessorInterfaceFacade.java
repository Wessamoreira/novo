package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.DuvidaProfessorInteracaoVO;
import negocio.comuns.ead.DuvidaProfessorVO;
import negocio.comuns.ead.QuadroResumoDuvidaProfessorVO;
import negocio.comuns.ead.enumeradores.SituacaoDuvidaProfessorEnum;


public interface DuvidaProfessorInterfaceFacade {
    
    void persistir(DuvidaProfessorVO duvidaProfessorVO, Boolean controlarAcesso, UsuarioVO usuarioVO, String idEntidade) throws Exception;
    
    void realizarRegistroDuvidaComoFrequente(Integer duvidaProfessor, Boolean frequente) throws Exception;
    
    List<DuvidaProfessorVO> consutar(String matricula, Integer turma, Integer disciplina, 
            SituacaoDuvidaProfessorEnum situacaoDuvidaProfessorEnum, Boolean frequente, Boolean trazerDuvidaDosColegas, Boolean controlarAcesso, 
            UsuarioVO usuarioVO, Integer limite, Integer pagina, String ano, String semestre) throws Exception;
    
    void incluirDuvidaProfessorInteracao(DuvidaProfessorVO duvidaProfessorVO, DuvidaProfessorInteracaoVO duvidaProfessorInteracaoVO, UsuarioVO usuarioVO) throws Exception;
    void finalizarDuvidaProfessor(DuvidaProfessorVO duvidaProfessorVO, UsuarioVO usuarioVO) throws Exception;

    List<QuadroResumoDuvidaProfessorVO> consultarResumoDuvidaProfessor(String matricula, Integer turma, Integer disciplina, UsuarioVO usuarioVO, String ano, String semestre);

    Integer consultarQtdeAtualizacaoDuvidaPorUsuarioProfessor(UsuarioVO usuario, Integer unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    Integer consultarQtdeAtualizacaoDuvidaPorUsuarioAluno(String matricula, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO);

	SqlRowSet consultarProfessorQueTemDuvidasAResponder(UsuarioVO usuarioVO) throws Exception;

	SqlRowSet consultarDadosEnvioNotificacaoDuvidaProfessor(Integer codigoDuvidaProfessor) throws Exception;

	Integer consutarTotalRegistro(String matricula, Integer turma, Integer disciplina, SituacaoDuvidaProfessorEnum situacaoDuvidaProfessorEnum, Boolean frequente, Boolean trazerDuvidaDosColegas, UsuarioVO usuarioVO, String ano, String semestre) throws Exception;
	
	public List<DuvidaProfessorVO> consultarAtualizacaoDuvidaPorUsuarioProfessor(UsuarioVO usuario, Integer unidadeEnsino, String ano, String semestre) throws Exception;
	
	public Integer consultarTotalRegistroAtualizacaoDuvidaPorProfessor(UsuarioVO usuario, Integer unidadeEnsino, String ano, String semestre) throws Exception;
}
