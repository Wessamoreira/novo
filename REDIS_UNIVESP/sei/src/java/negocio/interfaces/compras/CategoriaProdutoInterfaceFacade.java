package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface CategoriaProdutoInterfaceFacade {

	public CategoriaProdutoVO novo() throws Exception;

	public void incluir(CategoriaProdutoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(CategoriaProdutoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(CategoriaProdutoVO obj, UsuarioVO usuarioVO) throws Exception;

	public CategoriaProdutoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaProdutoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaProdutoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<CategoriaProdutoVO> consultarCategoriaProdutoPassandoDescricaoCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<CategoriaProdutoVO> consultaComHierarquia(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	CategoriaProdutoVO consultarPorCodigo(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List consultarCategoriaProdutoPassandoCodigoFornecedor(Integer fornecedor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}