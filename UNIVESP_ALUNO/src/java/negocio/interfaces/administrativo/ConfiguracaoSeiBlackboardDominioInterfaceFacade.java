package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardDominioVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ConfiguracaoSeiBlackboardDominioInterfaceFacade {

	void persistir(List<ConfiguracaoSeiBlackboardDominioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	void validarDados(ConfiguracaoSeiBlackboardDominioVO obj);

	

}
