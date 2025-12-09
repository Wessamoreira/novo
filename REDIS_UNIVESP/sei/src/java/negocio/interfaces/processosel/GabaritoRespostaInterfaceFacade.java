package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;

public interface GabaritoRespostaInterfaceFacade {
	public void alterarGabaritoRespostaVOs(GabaritoVO gabaritoVO, UsuarioVO usuario) throws Exception;

	public void incluirGabaritoRespostaVOs(GabaritoVO gabaritoVO, UsuarioVO usuario) throws Exception;

	public void excluirGabaritoResposta(Integer gabarito, UsuarioVO usuario) throws Exception;

	public List<GabaritoRespostaVO> consultaRapidaPorGabarito(Integer gabarito, UsuarioVO usuarioVO);

	public void validarDados(GabaritoRespostaVO obj) throws Exception;

}
