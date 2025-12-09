package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;

public interface ConfiguracaoContabilInterfaceFacade {

	void persistir(ConfiguracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConfiguracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<ConfiguracaoContabilVO> consultaRapidaPorCodigo(Integer valorConsulta,  boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	List<ConfiguracaoContabilVO> consultaRapidaPorNome(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ConfiguracaoContabilVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	//List<ConfiguracaoContabilVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	public ConfiguracaoContabilVO consultaRapidaPorCodigoUnidadeEnsino(Integer UnidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	public ConfiguracaoContabilVO consultaRapidaPorIntegracaoContabil(IntegracaoContabilVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<ConfiguracaoContabilVO> consultaRapidaPorLayoutIntegracaoContabil(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	void addConfiguracaoContabilRegra(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO configuracaoRegra, UsuarioVO usuario) throws Exception;

	void removeConfiguracaoContabilRegra(ConfiguracaoContabilVO obj, ConfiguracaoContabilRegraVO configuracaoRegra, UsuarioVO usuario) throws Exception;

	
	boolean consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(Integer UnidadeEnsino, UsuarioVO usuario) throws Exception;
	
	boolean consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsinoPorTipoRegraContabil(Integer unidadeEnsino, TipoRegraContabilEnum tipoRegraContabilEnum, UsuarioVO usuario) throws Exception;

}
