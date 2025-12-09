package negocio.interfaces.contabil;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;

public interface ConfiguracaoContabilRegraPlanoContaInterfaceFacade {

	void persistir(List<ConfiguracaoContabilRegraPlanoContaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConfiguracaoContabilRegraPlanoContaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void montarDadosBasicoConfiguracaoContabilRegraPlanoConta(SqlRowSet dadosSQL, ConfiguracaoContabilRegraVO ccr) throws Exception;

}
