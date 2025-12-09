package negocio.interfaces.basico;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;

public interface ConfiguracaoGedOrigemInterfaceFacade {

	void incluirConfiguracaoGedOrigemVOs(ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO) throws Exception;
	void alterarConfiguracaoGedOrigemVOs(ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO) throws Exception;	
	void carregarDadosConfiguracaoGedOrigemVOs(ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO) throws Exception;
}
