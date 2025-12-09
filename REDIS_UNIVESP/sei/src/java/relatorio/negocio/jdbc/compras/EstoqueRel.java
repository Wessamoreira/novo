/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.compras;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.compras.EstoqueRelVO;
import relatorio.negocio.comuns.compras.UnidadeMedidaEstoqueRelVO;
import relatorio.negocio.interfaces.compras.EstoqueRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Manoel
 */
@Repository
@Scope("singleton")
@Lazy
public class EstoqueRel extends SuperRelatorio implements EstoqueRelInterfaceFacade {

	@Override
	public List<EstoqueRelVO> criarObjeto(Integer codUnidadeEnsino, Integer codProduto, Integer codCategoriaProduto, Date dataEntradaInicial, Date dataEntradaFinal, String estoqueMinimo, Boolean trazerProdutoZerado , Boolean trazerProdutoSaldoeZerado) throws Exception {
		this.validarDados(dataEntradaInicial, dataEntradaFinal);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT UnidadeEnsino.nome as unidadeEnsino, ProdutoServico.nome as produto, estoque.estoqueminimo, estoque.estoquemaximo, estoque.quantidade, estoque.precounitario, ");
		sqlStr.append(" estoque.dataentrada, unidadeMedida.sigla FROM Estoque  inner join ProdutoServico on ProdutoServico.codigo = estoque.produto  ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = estoque.unidadeensino ");
		sqlStr.append(" inner join CategoriaProduto on categoriaproduto.codigo = ProdutoServico.CategoriaProduto ");
		sqlStr.append(" inner join unidadeMedida on ProdutoServico.unidadeMedida = unidadeMedida.codigo ");
		
		if (trazerProdutoZerado == true && trazerProdutoSaldoeZerado == false) {
			sqlStr.append(" where estoque.quantidade = 0");
		}if (trazerProdutoZerado == false && trazerProdutoSaldoeZerado == false) {
				sqlStr.append(" where estoque.quantidade > 0 and estoque.precounitario > 0");
		}
		if (codUnidadeEnsino.intValue() != 0) {
			sqlStr.append(" and unidadeensino.codigo = ").append(codUnidadeEnsino.intValue());
		}

		if (codProduto.intValue() != 0) {
			sqlStr.append(" and ProdutoServico.codigo = ").append(codProduto.intValue());

		}

		if (codCategoriaProduto.intValue() != 0) {
			sqlStr.append(" and categoriaproduto.codigo = ").append(codCategoriaProduto.intValue());

		}

		if (dataEntradaInicial != null && dataEntradaFinal != null) {
			sqlStr.append(" and estoque.dataEntrada  >= " + "'" + Uteis.getData(dataEntradaInicial, "yyyy/MM/dd") + "'" + " and estoque.dataEntrada <= " + "'" + Uteis.getData(dataEntradaFinal, "yyyy/MM/dd") + "'");
		}

		if (dataEntradaInicial != null && dataEntradaFinal == null) {
			sqlStr.append(" and estoque.dataEntrada  >= " + "'" + Uteis.getData(dataEntradaInicial, "yyyy/MM/dd") + "'");

		}

		if (dataEntradaInicial == null && dataEntradaFinal != null) {
			sqlStr.append(" and estoque.dataEntrada <= " + "'" + Uteis.getData(dataEntradaFinal, "yyyy/MM/dd") + "'");

		}

		if (estoqueMinimo.equals("AEM")) {
			sqlStr.append(" and estoque.quantidade < estoque.estoqueminimo ");
		}

		if (estoqueMinimo.equals("ACEM")) {
			sqlStr.append(" and estoque.quantidade > estoque.estoqueminimo  ");

		}

		sqlStr.append(" group by UnidadeEnsino.nome, ProdutoServico.nome, estoque.estoqueminimo, estoque.estoquemaximo, estoque.quantidade, estoque.precounitario, estoque.dataentrada, unidadeMedida.sigla ");
		sqlStr.append(" order by UnidadeEnsino.nome, ProdutoServico.nome, estoque.dataentrada");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<EstoqueRelVO> vetResultado = new ArrayList<EstoqueRelVO>(0);
		while (tabelaResultado.next()) {
			EstoqueRelVO obj = new EstoqueRelVO();
			obj.setUnidadeEnsino(tabelaResultado.getString("unidadeensino"));
			obj.setProduto(tabelaResultado.getString("produto"));
			obj.setEstoqueMinimo(tabelaResultado.getDouble("estoqueminimo"));
			obj.setEstoqueMaximo(tabelaResultado.getDouble("estoquemaximo"));
			obj.setQuantidade(tabelaResultado.getDouble("quantidade"));
			obj.setPrecoUnitario(tabelaResultado.getDouble("precounitario"));
			obj.setCustoTotal(obj.getQuantidade() * obj.getPrecoUnitario());
			obj.setDataEntrada(tabelaResultado.getDate("dataentrada"));
			obj.setUnidade(tabelaResultado.getString("sigla"));

			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<EstoqueRelVO> criarObjetoAnaliticoPorProduto(UnidadeEnsinoVO unidadeEnsinoVO, ProdutoServicoVO produtoServicoVO, CategoriaProdutoVO categoriaProdutoVO, String tipoProduto, String situacaoProduto , Boolean trazerProdutoZerado , Boolean trazerProdutoSaldoeZerado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ProdutoServico.codigo as codigoProduto, ");
		sqlStr.append("ProdutoServico.nome as produto, ");
		sqlStr.append("UnidadeEnsino.nome as unidadeEnsino, ");
		sqlStr.append("CategoriaProdutoPai.nome as categoriaProdutoPai, ");
		sqlStr.append("CategoriaProduto.nome as categoriaProduto, ");
		sqlStr.append("sum (Estoque.quantidade) as quantidadeTotal, ");
		sqlStr.append("sum (Estoque.precounitario * Estoque.quantidade) as valorTotalPago ");
		sqlStr.append("FROM Estoque ");
		sqlStr.append("inner join ProdutoServico on ProdutoServico.codigo = Estoque.produto ");
		sqlStr.append("inner join UnidadeEnsino on UnidadeEnsino.codigo = Estoque.unidadeensino ");
		sqlStr.append("inner join CategoriaProduto on CategoriaProduto.codigo = ProdutoServico.categoriaproduto ");
		sqlStr.append("left join CategoriaProduto as CategoriaProdutoPai on CategoriaProduto.categoriaprodutopai = CategoriaProdutoPai.codigo ");
		sqlStr.append("where 1 = 1 ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
			sqlStr.append("	and  UnidadeEnsino.codigo = ").append(unidadeEnsinoVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(produtoServicoVO.getCodigo())) {
			sqlStr.append(" and ProdutoServico.codigo = ").append(produtoServicoVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(categoriaProdutoVO.getCodigo())) {
			sqlStr.append(" and CategoriaProduto.codigo = ").append(categoriaProdutoVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(tipoProduto)) {
			sqlStr.append(" and ProdutoServico.tipoprodutoservico = '").append(tipoProduto).append("'");
		}
		if (Uteis.isAtributoPreenchido(situacaoProduto)) {
			sqlStr.append(" and ProdutoServico.situacao = '").append(situacaoProduto).append("'");
		}
		sqlStr.append("	group by ProdutoServico.codigo, ProdutoServico.nome, UnidadeEnsino.nome, CategoriaProduto.nome, CategoriaProdutoPai.nome, Estoque.estoqueminimo ");
		
		if (trazerProdutoZerado && !trazerProdutoSaldoeZerado) {
			sqlStr.append(" having sum(quantidade) = 0 ");
		}
		if (!trazerProdutoSaldoeZerado && !trazerProdutoZerado) {
			sqlStr.append(" having sum(quantidade) > 0 ");
		}
		if (trazerProdutoSaldoeZerado && !trazerProdutoZerado) {
			sqlStr.append(" having sum(quantidade) >= 0 ");
		}
		sqlStr.append("	order by UnidadeEnsino.nome, CategoriaProdutoPai.nome, CategoriaProduto.nome, ProdutoServico.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<EstoqueRelVO> vetResultado = new ArrayList<EstoqueRelVO>(0);
		while (tabelaResultado.next()) {
			EstoqueRelVO obj = new EstoqueRelVO();
			obj.setCodigoProduto(tabelaResultado.getInt("codigoProduto"));
			obj.setProduto(tabelaResultado.getString("produto"));
			obj.setUnidadeEnsino(tabelaResultado.getString("unidadeensino"));
			obj.setCategoriaProdutoPai(tabelaResultado.getString("categoriaProdutoPai"));
			obj.setCategoriaProduto(tabelaResultado.getString("categoriaProduto"));
			obj.setQuantidade(tabelaResultado.getDouble("quantidadeTotal"));
			obj.setValorTotalPago(tabelaResultado.getDouble("valorTotalPago"));
			obj.setCustoMedio(tabelaResultado.getDouble("valorTotalPago") / tabelaResultado.getDouble("quantidadeTotal"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List montaListaUnidadeMedidaEstoque(Integer codUnidadeEnsino, Integer codProduto, Integer codCategoriaProduto, Date dataEntradaInicial, Date dataEntradaFinal, String estoqueMinimo) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("  SELECT unidadeMedida.sigla, sum(estoque.quantidade) as qtdTotal FROM Estoque   ");
		sqlStr.append("  inner join ProdutoServico on ProdutoServico.codigo = estoque.produto  inner join unidadeensino on unidadeensino.codigo = estoque.unidadeensino     ");
		sqlStr.append("  inner join CategoriaProduto on categoriaproduto.codigo = ProdutoServico.CategoriaProduto  inner join unidadeMedida on ProdutoServico.unidadeMedida = unidadeMedida.codigo  ");
		sqlStr.append("  where estoque.quantidade > 0 and estoque.precounitario > 0 ");

		if (codUnidadeEnsino.intValue() != 0) {
			sqlStr.append(" and unidadeensino.codigo = ").append(codUnidadeEnsino.intValue());
		}

		if (codProduto.intValue() != 0) {
			sqlStr.append(" and ProdutoServico.codigo = ").append(codProduto.intValue());

		}

		if (codCategoriaProduto.intValue() != 0) {
			sqlStr.append(" and categoriaproduto.codigo = ").append(codCategoriaProduto.intValue());

		}

		if (dataEntradaInicial != null && dataEntradaFinal != null) {
			sqlStr.append(" and estoque.dataEntrada  >= " + "'" + Uteis.getData(dataEntradaInicial, "yyyy/MM/dd") + "'" + " and estoque.dataEntrada <= " + "'" + Uteis.getData(dataEntradaFinal, "yyyy/MM/dd") + "'");
		}

		if (dataEntradaInicial != null && dataEntradaFinal == null) {
			sqlStr.append(" and estoque.dataEntrada  >= " + "'" + Uteis.getData(dataEntradaInicial, "yyyy/MM/dd") + "'");

		}

		if (dataEntradaInicial == null && dataEntradaFinal != null) {
			sqlStr.append(" and estoque.dataEntrada <= " + "'" + Uteis.getData(dataEntradaFinal, "yyyy/MM/dd") + "'");

		}

		if (estoqueMinimo.equals("AEM")) {
			sqlStr.append(" and estoque.quantidade < estoque.estoqueminimo ");
		}

		if (estoqueMinimo.equals("ACEM")) {
			sqlStr.append(" and estoque.quantidade > estoque.estoqueminimo  ");

		}

		sqlStr.append("  group by unidadeMedida.sigla order by unidadeMedida.sigla ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UnidadeMedidaEstoqueRelVO> vetResultado = new ArrayList<UnidadeMedidaEstoqueRelVO>(0);
		while (tabelaResultado.next()) {
			UnidadeMedidaEstoqueRelVO obj = new UnidadeMedidaEstoqueRelVO();
			obj.setSiglaUnidade(tabelaResultado.getString("sigla"));
			obj.setQtdTotal(tabelaResultado.getDouble("qtdTotal"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void validarDados(Date dataEntradaInicial, Date dataEntradaFinal) throws Exception {
		if (dataEntradaInicial != null && dataEntradaFinal != null) {
			if (dataEntradaInicial.getTime() > dataEntradaFinal.getTime()) {
				throw new ConsistirException("Data Inicial não pode ser maior que data Final");
			}
		}

	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator);
	}

	public static String getDesign() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator + "EstoqueRel.jrxml");
	}

	public static String getDesignAnaliticoPorProduto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator + "EstoqueRelAnaliticoPorProduto.jrxml");
	}
	
	public static String getDesignAnaliticoPorProdutoZerado() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator + "EstoqueRelAnaliticoPorProdutoZerado.jrxml");
	}

}
