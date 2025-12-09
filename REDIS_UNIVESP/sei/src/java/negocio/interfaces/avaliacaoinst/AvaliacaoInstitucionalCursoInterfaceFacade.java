package negocio.interfaces.avaliacaoinst;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalCursoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;

public interface AvaliacaoInstitucionalCursoInterfaceFacade {
	
	public void incluirAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;
	public void alterarAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;
	public void excluirAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;
	public List<AvaliacaoInstitucionalCursoVO> consultarPorAvaliacaoInstitucional(Integer avaliacaoInstitucional, UsuarioVO usuarioVO) throws Exception;	

}
