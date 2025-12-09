/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface MatriculaProvaPresencialInterfaceFacade {
	
	public void incluir(final MatriculaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;
	
	public void alterar(final MatriculaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;
	
	public void excluir(MatriculaProvaPresencialVO obj, UsuarioVO usuario) throws Exception;

	public void alterarMatriculaProvaPresencial(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcesamentoArquivoVO, List<MatriculaProvaPresencialVO> objetos, UsuarioVO usuario) throws Exception;

	public void excluirMatriculaProvaPresencialPorResultado(Integer resultadoProcessamentoArquivo, UsuarioVO usuario) throws Exception;

	public void incluirMatriculaProvaPresencial(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcesamentoArquivoVO, ProgressBarVO progressBarVO, List<MatriculaProvaPresencialVO> objetos, UsuarioVO usuario) throws Exception;
	
	public List<MatriculaProvaPresencialVO> consultarPorResultadoProcessamentoArquivo(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO);
	
	public void adicionarNotaHistoricoDeAcordoComTituloNota(HistoricoVO historicoVO, String tituloNota, Double nota);
	
	public void preencherTodosListaAluno(List<MatriculaProvaPresencialVO> matriculaProvaPresencialVOs, String situacao);
	
	public void desmarcarTodosListaAluno(List<MatriculaProvaPresencialVO> matriculaProvaPresencialVOs, String situacao);

}
