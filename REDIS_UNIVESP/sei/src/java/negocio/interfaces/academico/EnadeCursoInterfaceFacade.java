package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface EnadeCursoInterfaceFacade {

	public void validarDados(EnadeCursoVO enadeCursoVO) throws Exception;

	public void incluir(final EnadeCursoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final EnadeCursoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(EnadeCursoVO obj, UsuarioVO usuario) throws Exception;
	
	public void incluirEnadeCursoVOs(EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarEnadeCursoVOs(EnadeVO enadeVO, UsuarioVO usuarioVO) throws Exception;
	
	public void excluirEnadeCursoVOs(EnadeVO enadeVO) throws Exception;
	
	public List<EnadeCursoVO> consultarPorEnade(Integer enade, UsuarioVO usuarioVO) throws Exception;
	
	public void excluirEnadeCursoPorEnade(EnadeVO enadeVO) throws Exception;
	
	public List<EnadeCursoVO> consultarPorCodigoEnade(Integer valorConsulta, UsuarioVO usuario) throws Exception;
	
	public List<EnadeCursoVO> consultarPorTituloEnade(String valorConsulta, UsuarioVO usuario) throws Exception;

}
