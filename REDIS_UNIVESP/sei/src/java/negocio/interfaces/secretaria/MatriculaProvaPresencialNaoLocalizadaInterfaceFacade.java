/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialNaoLocalizadaVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface MatriculaProvaPresencialNaoLocalizadaInterfaceFacade {
	
	public void validarDados(MatriculaProvaPresencialNaoLocalizadaVO obj) throws Exception;
	
	public void incluir(final MatriculaProvaPresencialNaoLocalizadaVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterar(final MatriculaProvaPresencialNaoLocalizadaVO obj, UsuarioVO usuario) throws Exception;
	
	public void excluir(MatriculaProvaPresencialNaoLocalizadaVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarMatriculaProvaPresencialNaoLocalizada(Integer resultadoProvaPresencial, List<MatriculaProvaPresencialNaoLocalizadaVO> objetos, UsuarioVO usuario) throws Exception;
	
	public void excluirMatriculaProvaPresencialNaoLocalizadaPorResultadoArquivo(Integer matriculaProvaPresencial, UsuarioVO usuario) throws Exception;
	
	public void incluirMatriculaProvaPresencialNaoLocalizada(Integer resultadoProvaPresencial, List<MatriculaProvaPresencialNaoLocalizadaVO> objetos, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaProvaPresencialNaoLocalizadaVO> consultarPorResultadoProcessamentoArquivo(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO);
	
}
