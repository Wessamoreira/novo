package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.ItemCotacaoVO;

public interface ItemCotacaoUnidadeEnsinoInterfaceFacade {

	public void incluir(ItemCotacaoUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ItemCotacaoUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;

	public List consultarItemCotacaoUnidadeEnsinos(Integer itemCotacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarItemCotacaoUnidadeEnsino(ItemCotacaoVO itemCotacao, List<ItemCotacaoUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception;

	public void incluirItemCotacaoUnidadeEnsino(ItemCotacaoVO itemCotacao, List<ItemCotacaoUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception;

	public ItemCotacaoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

}