/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialRespostaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface MatriculaProvaPresencialRespostaInterfaceFacade {

	public void incluir(final MatriculaProvaPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final MatriculaProvaPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(MatriculaProvaPresencialRespostaVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarMatriculaProvaPresencialResposta(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialRespostaVO> objetos, UsuarioVO usuario) throws Exception;
	
	public void excluirMatriculaProvaPresencialRespostaPorMatriculaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuario) throws Exception;
	
	public void incluirMatriculaProvaPresencialResposta(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialRespostaVO> objetos, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaProvaPresencialRespostaVO> consultarPorMatriculaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuarioVO);
	
	public void excluirPorMatriculaProvaPresencial(List<MatriculaProvaPresencialVO> listaProvaPresencialVOs, UsuarioVO usuarioVO) throws Exception;

}
