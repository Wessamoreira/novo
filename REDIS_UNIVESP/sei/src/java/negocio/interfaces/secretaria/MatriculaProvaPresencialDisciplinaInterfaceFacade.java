/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface MatriculaProvaPresencialDisciplinaInterfaceFacade {

	public void incluir(final MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarMatriculaProvaPresencial(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialDisciplinaVO> objetos, UsuarioVO usuario) throws Exception;
	
	public void excluirMatriculaProvaPresencialDisciplinaPorMatriculaProvaPresencial(Integer matriculaProvaPresencial, UsuarioVO usuario) throws Exception;
	
	public void incluirMatriculaProvaPresencialDisciplina(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialDisciplinaVO> objetos, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaProvaPresencialDisciplinaVO> consultarPorMatriculaProvaPresencial(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO);
	
	public void excluirPorMatriculaProvaPresencial(List<MatriculaProvaPresencialVO> listaProvaPresencialVOs, UsuarioVO usuarioVO) throws Exception;

}
