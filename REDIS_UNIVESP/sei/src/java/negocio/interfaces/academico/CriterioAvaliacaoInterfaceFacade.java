package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CriterioAvaliacaoInterfaceFacade {

	void persistir(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void ativar(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void inativar(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	CriterioAvaliacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;
	
	List<CriterioAvaliacaoVO> consultar(String opcaoConsulta, String valorConsulta, Integer unidadeEnsino, boolean validarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception;
	
	Integer consultarTotalRegistro(String opcaoConsulta, String valorConsulta, Integer unidadeEnsino) throws Exception;
	
	void adicionarCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO, Integer periodoLetivo, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(CriterioAvaliacaoVO criterioAvaliacaoVO) throws ConsistirException;

	void adicionarTodosCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO, UsuarioVO usuarioVO) throws Exception;

	void excluirCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO, CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, UsuarioVO usuarioVO) throws Exception;

	void excluir(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;	
	
}
