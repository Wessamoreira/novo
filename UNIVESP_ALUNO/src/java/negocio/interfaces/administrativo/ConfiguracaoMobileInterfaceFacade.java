package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoMobileVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
 *
 */
public interface ConfiguracaoMobileInterfaceFacade {

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param configuracaoMobileVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluir(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Victor Hugo de Paula Costa - 3 de nov de 2016 
	 */

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param configuracaoMobileVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistir(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param configuracaoMobileVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterar(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param configuracaoMobileVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void excluir(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param valorConsulta
	 * @param campoConsulta
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConfiguracaoMobileVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param codigo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConfiguracaoMobileVO> consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param valorConsulta
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<ConfiguracaoMobileVO> consultarPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param configuracaoMobileVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterarSituacaoAtivo(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de nov de 2016 
	 * @param configuracaoMobileVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterarSituacaoInativo(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 7 de nov de 2016 
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	ConfiguracaoMobileVO consultarConfiguracaoASerUsada(int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public ConfiguracaoMobileVO consultarConfiguracaoPadrao(int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public List<ConfiguracaoMobileVO> consultarPorNomeAtivo(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public ConfiguracaoMobileVO consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
}
