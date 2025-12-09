package negocio.interfaces.basico;

import java.util.List;

import controle.arquitetura.ConfiguracaoControleInterface;
import controle.basico.ConfiguracoesControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ConfiguracoesInterfaceFacade {

    public ConfiguracoesVO novo() throws Exception;

    public void incluir(ConfiguracoesVO obj, List<ConfiguracaoControleInterface> configs, UsuarioVO usuarioVO) throws Exception;
    
    public void alterarSomenteConfiguracores(ConfiguracoesVO obj) throws Exception;

    public void alterar(ConfiguracoesVO obj, List<ConfiguracaoControleInterface> configs, ConfiguracoesControle configuracoesControle, UsuarioVO usuarioVO) throws Exception;

    public void excluir(ConfiguracoesVO obj, List<ConfiguracaoControleInterface> configs, ConfiguracoesControle configuracoesControle, UsuarioVO usuarioVO) throws Exception;

    public ConfiguracoesVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public ConfiguracoesVO consultarConfiguracaoASerUsada(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer unidadeEnsino) throws Exception;

    public ConfiguracoesVO consultarConfiguracaoPadrao(boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

    public ConfiguracoesVO consultarConfiguracaoUnidadeEnsino(Integer unidadeEnsinoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public ConfiguracoesVO consultarConfiguracaoPadraoSemControleAcesso() throws Exception;

	List<ConfiguracoesVO> listarTodasConfiguracoes(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
}
