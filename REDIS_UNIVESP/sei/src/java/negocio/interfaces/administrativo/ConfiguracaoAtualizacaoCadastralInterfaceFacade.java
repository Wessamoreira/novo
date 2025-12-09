package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoAtualizacaoCadastralVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ConfiguracaoAtualizacaoCadastralInterfaceFacade {
	

    public ConfiguracaoAtualizacaoCadastralVO novo() throws Exception;
    public void incluir(ConfiguracaoAtualizacaoCadastralVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(ConfiguracaoAtualizacaoCadastralVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ConfiguracaoAtualizacaoCadastralVO obj, UsuarioVO usuarioVO) throws Exception;
    public ConfiguracaoAtualizacaoCadastralVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ConfiguracaoAtualizacaoCadastralVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public ConfiguracaoAtualizacaoCadastralVO consultarPorConfiguracaoGeralSistema(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
	void aplicarRegraRenderizacaoCamposAtualizacaoCadastral(ConfiguracaoAtualizacaoCadastralVO configuracaoAtualizacaoCadastralVO);    
}