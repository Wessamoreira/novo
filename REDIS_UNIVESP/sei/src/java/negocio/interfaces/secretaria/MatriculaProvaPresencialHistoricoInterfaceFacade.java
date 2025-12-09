/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialHistoricoVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface MatriculaProvaPresencialHistoricoInterfaceFacade {
	
	public void incluirMatriculaProvaPresencialHistorico(Integer matriculaProvaPresencial, List<MatriculaProvaPresencialDisciplinaVO> objetos, Integer configuracaoAcademico, String variavelNota, Boolean realizarCalculoMediaLancamentoNota, String ano, String semestre, UsuarioVO usuario) throws Exception;
	public void incluir(final MatriculaProvaPresencialHistoricoVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final MatriculaProvaPresencialHistoricoVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(MatriculaProvaPresencialHistoricoVO obj, UsuarioVO usuario) throws Exception;
	public void executarAtualizacaoHistoricoNotaAtualizadaParaHistoricoOriginal(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception;
	public void excluirPorMatriculaProvaPresencialDisciplina(MatriculaProvaPresencialDisciplinaVO obj, UsuarioVO usuario) throws Exception;
	public void excluirPorListaMatriculaProvaPresencialDisciplina(List<MatriculaProvaPresencialVO> listaMatriculaProvaPresencialVOs, UsuarioVO usuarioVO) throws Exception;
	public MatriculaProvaPresencialHistoricoVO inicializarDadosMatriculaProvaPresencialHistoricoPorHistorico(MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO, HistoricoVO historicoVO, Integer configuracaoAcademico, String variavelNota, Boolean realizarCalculoMediaLancamentoNota, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
}
