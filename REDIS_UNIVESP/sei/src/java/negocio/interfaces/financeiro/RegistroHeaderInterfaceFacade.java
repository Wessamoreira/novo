package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroHeaderVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface RegistroHeaderInterfaceFacade {

	public RegistroHeaderVO novo() throws Exception;

	public void incluir(RegistroHeaderVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(RegistroHeaderVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(RegistroHeaderVO obj, UsuarioVO usuario) throws Exception;

	public RegistroHeaderVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public Integer verificarHeaderArquivoProcessado(String header, Integer codigoHeader) throws Exception;
}