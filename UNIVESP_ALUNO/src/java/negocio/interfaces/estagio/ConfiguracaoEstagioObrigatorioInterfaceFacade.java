package negocio.interfaces.estagio;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioFuncionarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;

public interface ConfiguracaoEstagioObrigatorioInterfaceFacade {

	void persistir(ConfiguracaoEstagioObrigatorioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(ConfiguracaoEstagioObrigatorioVO obj, boolean verificarAcesso, UsuarioVO usuario);

	ConfiguracaoEstagioObrigatorioVO consultarPorConfiguracaoEstagioPadrao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void adicionarConfiguracaoEstagioObrigatorioFuncionarioVO(ConfiguracaoEstagioObrigatorioVO ceo, ConfiguracaoEstagioObrigatorioFuncionarioVO ceoFuncionario, UsuarioVO usuario);

	void removerConfiguracaoEstagioObrigatorioFuncionarioVO(ConfiguracaoEstagioObrigatorioVO ceo, ConfiguracaoEstagioObrigatorioFuncionarioVO ceoFuncionario, UsuarioVO usuario);

	ConfiguracaoEstagioObrigatorioVO consultarPorConfiguracaoEstagioPadraoUnico(boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario);

	

}
