package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ConfiguracaoDiplomaDigitalInterfaceFacade {

	public void incluir(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public void alterar(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public void excluir(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public ConfiguracaoDiplomaDigitalVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public List<ConfiguracaoDiplomaDigitalVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public void persistir(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public void carregarDados(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario) throws Exception;

	public List<ConfiguracaoDiplomaDigitalVO> listarTodasConfiguracoes(Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ConfiguracaoDiplomaDigitalVO consultarConfiguracaoExistente(Integer codigoUnidadeEnsino, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
