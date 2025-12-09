/**
 * 
 */
package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * @author Carlos Eugênio
 *
 */
public interface ResultadoProcessamentoArquivoRespostaProvaPresencialInterfaceFacade {

	public void persistir(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception;

	public void validarDados(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj) throws Exception;

	public void incluir(final ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;

	public void alterar(final ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception;
	
	public void excluir(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception;
	
	public void carregarDados(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception;
	
	public void carregarDados(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> consultarPorGabarito(String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> consultarPorMatricula(String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> consultarPorAluno(String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

}
