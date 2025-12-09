package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface ConfiguracaoFinanceiroCartaoRecebimentoInterfaceFacade {

	void incluir(ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistirRebimentoAdministrativoVOs(List<ConfiguracaoFinanceiroCartaoRecebimentoVO> recebimentoAdministrativoVOs, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO, ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO2) throws ConsistirException;

	List<ConfiguracaoFinanceiroCartaoRecebimentoVO> consultarPorCodigoConfiguracaoRecebimentoCartaoOnlineVO(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Tercio Carillo - 17 de mai de 2016 
	 * @param codigoConfiguracaoRecebimentoCartaoOnline
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void excluirPorCodigoConfiguracaoRecebimentoCartaoOnline(int codigoConfiguracaoRecebimentoCartaoOnline, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
