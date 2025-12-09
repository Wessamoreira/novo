/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import relatorio.negocio.comuns.compras.EstoqueRelVO;

/**
 * 
 * @author Manoel
 */
public interface EstoqueRelInterfaceFacade {

	public List<EstoqueRelVO> criarObjeto(Integer codUnidadeEnsino, Integer codProduto, Integer codCategoriaProduto, Date dataEntradaInicial, Date dataEntradaFinal, String estoqueMinimo , Boolean trazerProdutoZerado , Boolean trazerProdutoSaldoeZerado) throws Exception;

	public List montaListaUnidadeMedidaEstoque(Integer codUnidadeEnsino, Integer codProduto, Integer codCategoriaProduto, Date dataEntradaInicial, Date dataEntradaFinal, String estoqueMinimo) throws Exception;

	/**
	 * @author Leonardo Riciolle - 22/06/2015
	 * @param unidadeEnsinoVO
	 * @param produtoServicoVO
	 * @param categoriaProdutoVO
	 * @return
	 * @throws Exception
	 */
	List<EstoqueRelVO> criarObjetoAnaliticoPorProduto(UnidadeEnsinoVO unidadeEnsinoVO, ProdutoServicoVO produtoServicoVO, CategoriaProdutoVO categoriaProdutoVO, String tipoProduto, String situacaoProduto , Boolean trazerProdutoZerado, Boolean trazerProdutoSaldoeZerado) throws Exception;

}
