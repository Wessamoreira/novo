package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;

public interface ConfiguracaoContabilRegraInterfaceFacade {

	void persistir(List<ConfiguracaoContabilRegraVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConfiguracaoContabilRegraVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void consultaRapidaPorConfiguracaoContabil(ConfiguracaoContabilVO obj, UsuarioVO usuario) throws Exception;

	void addConfiguracaoContabilRegraPlanoConta(ConfiguracaoContabilRegraVO obj, ConfiguracaoContabilRegraPlanoContaVO regraPlanoConta, UsuarioVO usuario) throws Exception;

	void removeConfiguracaoContabilRegraPlanoConta(ConfiguracaoContabilRegraVO obj, ConfiguracaoContabilRegraPlanoContaVO regraPlanoConta, UsuarioVO usuario) throws Exception;

	void consultaRapidaPorConfiguracaoContabilPorTipoRegraContabil(ConfiguracaoContabilVO obj, List<TipoRegraContabilEnum> listaTipoRegraContabil, UsuarioVO usuario) throws Exception;

}
