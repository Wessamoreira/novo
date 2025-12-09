package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.ItemCotacaoVO;

public interface ItemCotacaoInterfaceFacade {

	public void incluir(ItemCotacaoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ItemCotacaoVO obj, UsuarioVO usuario) throws Exception;

	public Double consultarUltimoPrecoProdutoFornecedor(Integer fornecedor, Integer produto) throws Exception;

	public List<ItemCotacaoVO> consultarItemCotacaos(Integer cotacaoFornecedor, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarItemCotacao(CotacaoFornecedorVO cotacaoFornecedor, List<ItemCotacaoVO> objetos, UsuarioVO usuario) throws Exception;

	public void incluirItemCotacao(CotacaoFornecedorVO cotacaoFornecedor, List<ItemCotacaoVO> objetos, UsuarioVO usuario) throws Exception;

	public ItemCotacaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

}