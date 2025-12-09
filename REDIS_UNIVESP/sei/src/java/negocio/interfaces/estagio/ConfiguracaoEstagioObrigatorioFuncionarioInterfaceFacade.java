package negocio.interfaces.estagio;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioFuncionarioVO;

public interface ConfiguracaoEstagioObrigatorioFuncionarioInterfaceFacade {

	void persistir(List<ConfiguracaoEstagioObrigatorioFuncionarioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

}
