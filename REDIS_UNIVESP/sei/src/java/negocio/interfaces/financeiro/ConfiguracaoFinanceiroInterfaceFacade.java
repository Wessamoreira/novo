package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ConfiguracaoFinanceiroInterfaceFacade {    
	
    public ConfiguracaoFinanceiroVO novo() throws Exception;

    public void incluir(ConfiguracaoFinanceiroVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void alterar(ConfiguracaoFinanceiroVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void excluir(ConfiguracaoFinanceiroVO obj, UsuarioVO usuario) throws Exception;

    public ConfiguracaoFinanceiroVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public ConfiguracaoFinanceiroVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public ConfiguracaoFinanceiroVO consultarConfiguracaoASerUsada(int nivelMontarDados, Integer unidadeEnsinoMatricula, UsuarioVO usuario) throws Exception;      
    
    public ConfiguracaoFinanceiroVO consultarConfiguracaoASerUsada(int nivelMontarDados, UsuarioVO usuario, Integer codigoUnidadeEnsino) throws Exception;    
    
    public void adicionarObjConfiguracaoFinanceiroCartaoVOs(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception;

    public void excluirObjConfiguracaoFinanceiroCartaoVOs(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception;

    public ConfiguracaoFinanceiroCartaoVO consultarObjConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoFinanceiroCartaoVO obj, UsuarioVO usuario) throws Exception;

    public ConfiguracaoFinanceiroVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception;
    
    public ConfiguracaoFinanceiroVO verificarConfiguracaoUtilizarGeracaoParcela(UnidadeEnsinoVO unidadeEnsinoAlunoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroPadraoVO, UsuarioVO usuarioVO) throws Exception;
    
    public ConfiguracaoFinanceiroVO consultarPorUnidadeEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ConfiguracaoFinanceiroVO consultarPorMatriculaAluno(String matricula, UsuarioVO usuarioVO) throws Exception;

	ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiraComBaseContaReceber(Integer contaReceber) throws Exception;

	ConfiguracaoFinanceiroVO consultarConfiguracaoNivelAplicacaoSerUsada(Integer codigoUnidadeEnsino) throws Exception;

	ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiraComBaseContaReceberCasoExista(Integer contaReceber) throws Exception;

	ConfiguracaoFinanceiroVO consultarPorChavePrimariaUnica(Integer codigoPrm, UsuarioVO usuario) throws Exception;
}
