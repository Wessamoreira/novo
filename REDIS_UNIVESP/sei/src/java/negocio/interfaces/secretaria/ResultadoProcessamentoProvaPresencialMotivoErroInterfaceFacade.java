package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessamentoProvaPresencialMotivoErroVO;

public interface ResultadoProcessamentoProvaPresencialMotivoErroInterfaceFacade {
	
	public void validarDados(ResultadoProcessamentoProvaPresencialMotivoErroVO obj) throws Exception;
	public void incluir(final ResultadoProcessamentoProvaPresencialMotivoErroVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final ResultadoProcessamentoProvaPresencialMotivoErroVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(ResultadoProcessamentoProvaPresencialMotivoErroVO obj, UsuarioVO usuario) throws Exception;
	public void excluirResultadoErroPorResultadoProcessamentoProvaPresencial(Integer resultadoProcessamento, UsuarioVO usuario) throws Exception;
	public void incluirResultadoProcessamentoErro(Integer resultadoProcessamentoProvaPresencial, List<ResultadoProcessamentoProvaPresencialMotivoErroVO> objetos, UsuarioVO usuario) throws Exception;
	public List<ResultadoProcessamentoProvaPresencialMotivoErroVO> consultarPorResultadoProcessamentoProvaPresencial(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO);

}
