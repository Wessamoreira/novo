package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CalendarioRegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface CalendarioRegistroAulaInterfaceFacade {
    
    
    void persistir(CalendarioRegistroAulaVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
    void excluir(CalendarioRegistroAulaVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
    
    CalendarioRegistroAulaVO consultarPorCalendarioRegistroUnico(Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, String ano,  Boolean validarAcesso, UsuarioVO usuarioVO ) throws Exception;
	Integer consultarTotalRegistro(Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor,  String ano) throws Exception;
	List<CalendarioRegistroAulaVO> consultar(Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor,  String ano, Boolean validarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	CalendarioRegistroAulaVO consultarPorCalendarioRegistroAulaUtilizar(Integer unidadeEnsino, Integer turma, Boolean turmaAgrupada, Integer professor,  String ano,  Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	void validarDados(CalendarioRegistroAulaVO obj) throws ConsistirException;
	void realizarClonagemCalendarioRegistroAula(CalendarioRegistroAulaVO calendarioRegistroAulaVO, UsuarioVO usuarioVO) throws Exception;
    
    
    
    

}
