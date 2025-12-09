package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.TaxaOperacaoCartaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ConfiguracaoFinanceiroCartaoInterfaceFacade {

    public void persistir(ConfiguracaoFinanceiroCartaoVO obj) throws Exception;

    public void excluir(ConfiguracaoFinanceiroCartaoVO obj) throws Exception;

    public void validarDados(ConfiguracaoFinanceiroCartaoVO obj) throws ConsistirException;

    public ConfiguracaoFinanceiroCartaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ConfiguracaoFinanceiroCartaoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarConfiguracaoFinanceiroCartao(Integer configuracaoFinanceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoOperadoraCartao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ConfiguracaoFinanceiroCartaoVO> consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro(String valorConsulta, Integer configuracaoFinanceiro, boolean online, Integer codigoFormaPagamento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void excluirPelaConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro, List<ConfiguracaoFinanceiroCartaoVO> listaConfiguracaoFinanceiroCartao) throws Exception;

    public void setIdEntidade(String aIdEntidade);

	Boolean consultarConfiguracaoFinanceiroCartaoVOsPorCodigoConfiguracaoFinanceiroEPermiteRecimentoOnline(Integer codigoConfiguracaoFinanceiro, String campo, Double valorAReceber, UsuarioVO usuario) throws Exception;

	Boolean verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro, Double valorAReceber, String campo) throws Exception;

	Boolean consultarConfiguracaoFinanceiroCartaoVOsPorCodigoConfiguracaoFinanceiroEPermiteRecimentoOnlineUsarMinhasContasVisaoAluno(Integer codigoConfiguracaoFinanceiro, String campo, Double valorAReceber, UsuarioVO usuario) throws Exception;

	List<ConfiguracaoFinanceiroCartaoVO> consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro, Double valorAReceber, String campo, String tipoCartao, UsuarioVO usuario) throws Exception;

	Boolean verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(Integer codigoConfiguracaoFinanceiro) throws Exception;
	
	public void adicionarObjTaxaOperacaoCartaoVOs(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, TaxaOperacaoCartaoVO obj, UsuarioVO usuario) throws Exception;
	
	public void excluirObjTaxaOperacaoCartaoVOs(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, TaxaOperacaoCartaoVO obj, UsuarioVO usuario) throws Exception;
	
	public List<TaxaOperacaoCartaoVO> consultarTaxaOperacaoCartaoConfiguracaoFinanceiroCartao(Integer configuracaoFinanceiroCartao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	ConfiguracaoFinanceiroCartaoVO consultarPorOperadoraCartaoConfiguracaoFinanceiro(OperadoraCartaoVO operadoraCartaoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	ConfiguracaoFinanceiroCartaoVO consultarPorChavePrimariaUnica(Integer codigo, NivelMontarDados nivelMontarDados,
			UsuarioVO usuario) throws Exception;
}
