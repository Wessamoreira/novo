package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoGabaritoRespostaVO;

public interface ResultadoProcessoSeletivoGabaritoRespostaInterfaceFacade {

	public void validarDados(ResultadoProcessoSeletivoGabaritoRespostaVO obj) throws Exception;
	public void incluir(final ResultadoProcessoSeletivoGabaritoRespostaVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final ResultadoProcessoSeletivoGabaritoRespostaVO obj, UsuarioVO usuario) throws Exception;
	public void excluirResultadoProcessoSeletivoGabaritoResposta(Integer gabarito, UsuarioVO usuario) throws Exception;
	public void alterarResultadoGabaritoRespostaVOs(Integer resultadoProcessoSeletivo, List objetos, UsuarioVO usuario) throws Exception;
	public void incluirResultadoProcessoSeletivoGabaritoRespostaVOs(Integer resultadoProcessoSeletivo, List objetos, UsuarioVO usuario) throws Exception;
	public List<ResultadoProcessoSeletivoGabaritoRespostaVO> consultaRapidaPorResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo, UsuarioVO usuarioVO);
	
	
}
