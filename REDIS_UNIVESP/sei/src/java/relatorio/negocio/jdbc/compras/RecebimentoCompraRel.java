/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.compras;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.compras.RecebimentoCompraRelVO;
import relatorio.negocio.comuns.compras.RequisicaoRelVO;
import relatorio.negocio.interfaces.compras.RecebimentoCompraRelInterfaceFacade;
import relatorio.negocio.interfaces.compras.RequisicaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Manoel
 */
@Repository
@Scope("singleton")
@Lazy
public class RecebimentoCompraRel extends SuperRelatorio implements RecebimentoCompraRelInterfaceFacade {

	public List<RecebimentoCompraRelVO> criarObjetoLayoutRecebimentoCompra(UnidadeEnsinoVO unidadeEnsino, Optional<Date> dataInicial, Optional<Date> dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento, String ordenacaoRelatorio) throws Exception {
		List<RecebimentoCompraRelVO> recebimentoCompraRelVOs = new ArrayList<RecebimentoCompraRelVO>(0);
		validarDados(unidadeEnsino, dataInicial, dataFinal);
		SqlRowSet dadosSQL = executarPesquisaParametrizadaLayoutRecebimentoCompra(unidadeEnsino, dataInicial, dataFinal, categoriaDespesa, categoriaProduto, produtoServico, fornecedor, situacaoRecebimento, ordenacaoRelatorio);
		montarDadosLayoutRecebimentoCompra(dadosSQL, recebimentoCompraRelVOs, situacaoRecebimento);
		return recebimentoCompraRelVOs;
	}

	public void validarDados(UnidadeEnsinoVO unidadeEnsino, Optional<Date> dataInicial, Optional<Date> dataFinal) throws Exception {
		if (unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("É necessário informar a UNIDADE DE ENSINO para geração desse relatório.");
		}
		if (dataInicial.isPresent() && dataFinal.isPresent() && dataInicial.get().after(dataFinal.get())) {
			throw new ConsistirException("Data Inicial maior que data Final");
		}
	}

	private void montarDadosLayoutRecebimentoCompra(SqlRowSet dadosSQL, List<RecebimentoCompraRelVO> recebimentoCompraRelVOs, String situacaoRecebimento) throws Exception {
		int linha = 1;
		while (dadosSQL.next()) {
			RecebimentoCompraRelVO obj = new RecebimentoCompraRelVO();
			obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
			obj.setDataRecebimento(dadosSQL.getDate("datarecebimento"));
			obj.setDataCompra(dadosSQL.getDate("datacompra"));
			obj.setSituacaoRecebimento(dadosSQL.getString("situacao"));
			obj.setFornecedor(dadosSQL.getString("fornecedor"));
			obj.setCnpjFornecedor(dadosSQL.getString("cnpjFornecedor"));
			obj.setCpfFornecedor(dadosSQL.getString("cpfFornecedor"));
			obj.setTipoEmpresaFornecedor(dadosSQL.getString("tipoEmpresaFornecedor"));
			obj.setValorTotal(dadosSQL.getDouble("valortotal"));
			obj.setCategoriaDespesa(dadosSQL.getString("categoriadespesa"));
			obj.setProduto(dadosSQL.getString("produto"));
			obj.setCategoriaProduto(dadosSQL.getString("categoriaproduto"));
			obj.setFormaPagamento(dadosSQL.getString("formapagamento"));
			obj.setCondicaoPagamento(dadosSQL.getString("condicaoPagamento"));
			obj.setLinha(linha);
			obj.setCodigoCompra(dadosSQL.getInt("codigocompra"));
			obj.setQuantidade(dadosSQL.getDouble("quantidade"));
			obj.setQuantidadeRecebida(dadosSQL.getDouble("quantidaderecebida"));
			obj.setPrecoUnitario(dadosSQL.getDouble("precounitario"));
			linha++;
			recebimentoCompraRelVOs.add(obj);
		}
	}

	@Override
	public Double montarValorTotalRecebimento(UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento) throws Exception {
		Double valorTotalRecebido = 0.0;
		if (!situacaoRecebimento.equals("TODAS")) {
			SqlRowSet dadosSQL = executarPesquisaValorTotalRecebimento(unidadeEnsino, dataInicial, dataFinal, categoriaDespesa, categoriaProduto, produtoServico, fornecedor, situacaoRecebimento);
			if (dadosSQL.next()) {
				valorTotalRecebido = (dadosSQL.getDouble("valortotalRecebimento"));
			}
		}
		return valorTotalRecebido;
	}

	public Double[] montarTotaisRecebimento(UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento) throws Exception {
		Double valoresTotalRecebido[] = { 0.0, 0.0 };
		if (situacaoRecebimento.equals("TODAS")) {
			SqlRowSet dadosSQL = executarPesquisaValorTotalRecebimento(unidadeEnsino, dataInicial, dataFinal, categoriaDespesa, categoriaProduto, produtoServico, fornecedor, situacaoRecebimento);
			if (dadosSQL.next()) {
				valoresTotalRecebido[0] = (dadosSQL.getDouble("valortotalRecebimentoPrevisao"));
				valoresTotalRecebido[1] = (dadosSQL.getDouble("valortotalRecebimentoEfetivado"));
			}
		}

		return valoresTotalRecebido;
	}

	private SqlRowSet executarPesquisaValorTotalRecebimento(UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento) {
		StringBuilder sqlsb = new StringBuilder();
		boolean where = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if (situacaoRecebimento.equals("TODAS")) {
			sqlsb.append("select sum(recebimentocompra.valortotal) as valortotalRecebimentoPrevisao,  ");
			sqlsb.append("(select sum(recebimentocompra.valortotal) ");
			sqlsb.append("from recebimentocompra ");
			sqlsb.append("inner join unidadeensino on recebimentocompra.unidadeensino = unidadeensino.codigo ");
			sqlsb.append("inner join compra on recebimentocompra.compra = compra.codigo ");
			sqlsb.append("inner join fornecedor on compra.fornecedor = fornecedor.codigo ");
			sqlsb.append("inner join categoriadespesa on categoriadespesa.codigo = compra.categoriadespesa ");
			sqlsb.append("inner join compraitem on compra.codigo = compraitem.compra ");
			sqlsb.append("inner join produtoservico on compraitem.produto = produtoservico.codigo ");
			sqlsb.append("inner join categoriaproduto on produtoservico.categoriaproduto = categoriaproduto.codigo ");
			sqlsb.append("inner join formapagamento on compra.formapagamento = formapagamento.codigo ");
			sqlsb.append("inner join condicaopagamento on compra.condicaopagamento = condicaopagamento.codigo ");
			sqlsb.append("where recebimentocompra.situacao = 'EF' ");

			if (unidadeEnsino.getCodigo() != 0) {
				sqlsb.append("and unidadeensino.codigo = " + unidadeEnsino.getCodigo());
			}

			if ((dataInicial != null) && (dataFinal == null)) {
				sqlsb.append(" and recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'");
			}

			if ((dataFinal != null) && (dataInicial == null)) {
				sqlsb.append(" and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
			}

			if ((dataInicial != null) && (dataFinal != null)) {
				sqlsb.append(" and recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'" + " and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
			}

			if (fornecedor.getCodigo() != 0) {
				sqlsb.append(" and fornecedor.codigo = " + fornecedor.getCodigo());
			}

			if (produtoServico.getCodigo() != 0) {
				sqlsb.append(" and produtoservico.codigo = " + produtoServico.getCodigo());
			}

			if (categoriaDespesa.getCodigo() != 0) {
				sqlsb.append(" and categoriadespesa.codigo = " + categoriaDespesa.getCodigo());
			}
			sqlsb.append(")valortotalRecebimentoEfetivado ");

			// --------
			sqlsb.append("from recebimentocompra ");
			sqlsb.append("inner join unidadeensino on recebimentocompra.unidadeensino = unidadeensino.codigo ");
			sqlsb.append("inner join compra on recebimentocompra.compra = compra.codigo ");
			sqlsb.append("inner join fornecedor on compra.fornecedor = fornecedor.codigo ");
			sqlsb.append("inner join categoriadespesa on categoriadespesa.codigo = compra.categoriadespesa ");
			sqlsb.append("inner join compraitem on compra.codigo = compraitem.compra ");
			sqlsb.append("inner join produtoservico on compraitem.produto = produtoservico.codigo ");
			sqlsb.append("inner join categoriaproduto on produtoservico.categoriaproduto = categoriaproduto.codigo ");
			sqlsb.append("inner join formapagamento on compra.formapagamento = formapagamento.codigo ");
			sqlsb.append("inner join condicaopagamento on compra.condicaopagamento = condicaopagamento.codigo ");
			sqlsb.append("where recebimentocompra.situacao = 'PR' ");

			if (unidadeEnsino.getCodigo() != 0) {
				sqlsb.append("and unidadeensino.codigo = " + unidadeEnsino.getCodigo());
			}

			if ((dataInicial != null) && (dataFinal == null)) {
				sqlsb.append(" and recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'");
			}

			if ((dataFinal != null) && (dataInicial == null)) {
				sqlsb.append(" and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
			}

			if ((dataInicial != null) && (dataFinal != null)) {
				sqlsb.append(" and recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'" + " and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
			}

			if (fornecedor.getCodigo() != 0) {
				sqlsb.append(" and fornecedor.codigo = " + fornecedor.getCodigo());
			}

			if (produtoServico.getCodigo() != 0) {
				sqlsb.append(" and produtoservico.codigo = " + produtoServico.getCodigo());
			}

			if (categoriaDespesa.getCodigo() != 0) {
				sqlsb.append(" and categoriadespesa.codigo = " + categoriaDespesa.getCodigo());
			}

		} else {
			sqlsb.append("select sum(recebimentocompra.valortotal) as valortotalRecebimento from recebimentocompra  ");
			sqlsb.append("inner join unidadeensino on recebimentocompra.unidadeensino = unidadeensino.codigo ");
			sqlsb.append("inner join compra on recebimentocompra.compra = compra.codigo ");
			sqlsb.append("inner join fornecedor on compra.fornecedor = fornecedor.codigo ");
			sqlsb.append("inner join categoriadespesa on categoriadespesa.codigo = compra.categoriadespesa ");
			sqlsb.append("inner join compraitem on compra.codigo = compraitem.compra ");
			sqlsb.append("inner join produtoservico on compraitem.produto = produtoservico.codigo ");
			sqlsb.append("inner join categoriaproduto on produtoservico.categoriaproduto = categoriaproduto.codigo ");
			sqlsb.append("inner join formapagamento on compra.formapagamento = formapagamento.codigo ");
			sqlsb.append("inner join condicaopagamento on compra.condicaopagamento = condicaopagamento.codigo ");
			if (unidadeEnsino.getCodigo() != 0) {
				sqlsb.append("where unidadeensino.codigo = " + unidadeEnsino.getCodigo());
				where = true;
			}

			if ((dataInicial != null) && (dataFinal == null)) {
				if (where) {
					sqlsb.append(" and recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'");
				} else {
					sqlsb.append(" where recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'");
				}
			}

			if ((dataFinal != null) && (dataInicial == null)) {
				if (where) {
					sqlsb.append(" and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
				} else {
					sqlsb.append(" where recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
				}
			}

			if ((dataInicial != null) && (dataFinal != null)) {
				if (where) {
					sqlsb.append(" and recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'" + " and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
				} else {
					sqlsb.append(" where recebimentocompra.data >= " + "'" + sdf.format(dataInicial) + "'" + " and recebimentocompra.data <= " + "'" + sdf.format(dataFinal) + "'");
				}
			}

			if (fornecedor.getCodigo() != 0) {
				if (where) {
					sqlsb.append(" and fornecedor.codigo = " + fornecedor.getCodigo());
				} else {
					sqlsb.append(" where fornecedor.codigo = " + fornecedor.getCodigo());
				}
			}

			if (produtoServico.getCodigo() != 0) {
				if (where) {
					sqlsb.append(" and produtoservico.codigo = " + produtoServico.getCodigo());
				} else {
					sqlsb.append(" where produtoservico.codigo = " + produtoServico.getCodigo());
				}
			}

			if (categoriaDespesa.getCodigo() != 0) {
				if (where) {
					sqlsb.append(" and categoriadespesa.codigo = " + categoriaDespesa.getCodigo());
				} else {
					sqlsb.append(" where categoriadespesa.codigo = " + categoriaDespesa.getCodigo());
				}
			}

			if (situacaoRecebimento.equals("PR")) {
				if (where) {
					sqlsb.append(" and recebimentocompra.situacao = " + "'" + situacaoRecebimento + "'");
				} else {
					sqlsb.append(" where recebimentocompra.situacao = " + "'" + situacaoRecebimento + "'");
				}
			}

			if (situacaoRecebimento.equals("EF")) {
				if (where) {
					sqlsb.append(" and recebimentocompra.situacao = " + "'" + situacaoRecebimento + "'");
				} else {
					sqlsb.append(" where recebimentocompra.situacao = " + "'" + situacaoRecebimento + "'");
				}
			}

		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
		return tabelaResultado;
	}

	private SqlRowSet executarPesquisaParametrizadaLayoutRecebimentoCompra(UnidadeEnsinoVO unidadeEnsino, Optional<Date> dataInicial, Optional<Date> dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento, String ordenacaoRelatorio) {
		StringBuilder sqlsb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		sqlsb.append("select unidadeensino.nome as unidadeensino, recebimentocompra.data as datarecebimento, compra.data as datacompra, recebimentocompra.situacao, ");
		sqlsb.append("fornecedor.nome as fornecedor, fornecedor.cnpj as cnpjFornecedor, fornecedor.cpf as cpfFornecedor, fornecedor.tipoEmpresa as tipoEmpresaFornecedor, produtoservico.nome as produto, ");
		sqlsb.append(" (select array_to_string(array_agg(distinct categoriadespesa.descricao), ', ') from centroresultadoorigem ");
		sqlsb.append(" inner join categoriadespesa on centroresultadoorigem.categoriadespesa = categoriadespesa.codigo ");
		sqlsb.append(" where centroresultadoorigem.TipoCentroResultadoOrigem = 'COMPRA' ");
		sqlsb.append(" and centroresultadoorigem.codorigem =  compra.codigo::varchar ) as categoriadespesa, ");
		sqlsb.append("case when recebimentocompra.situacao = 'PR' then compraitem.quantidade*compraitem.precounitario else compraitem.quantidaderecebida*compraitem.precounitario  end as valorTotal, ");
		sqlsb.append("categoriaproduto.nome as categoriaproduto, formapagamento.nome as formapagamento, condicaopagamento.nome as condicaopagamento, compra.codigo as codigocompra, ");
		sqlsb.append("compraitem.quantidaderecebida as quantidaderecebida, compraitem.precounitario as precounitario, compraitem.quantidade as quantidade ");
		sqlsb.append("from recebimentocompra  ");
		sqlsb.append("inner join unidadeensino on recebimentocompra.unidadeensino = unidadeensino.codigo ");
		sqlsb.append("inner join compra on recebimentocompra.compra = compra.codigo ");
		sqlsb.append("inner join fornecedor on compra.fornecedor = fornecedor.codigo ");
		sqlsb.append("inner join compraitem on compra.codigo = compraitem.compra ");
		sqlsb.append("inner join produtoservico on compraitem.produto = produtoservico.codigo ");
		sqlsb.append("inner join categoriaproduto on produtoservico.categoriaproduto = categoriaproduto.codigo ");
		sqlsb.append("inner join formapagamento on compra.formapagamento = formapagamento.codigo ");
		sqlsb.append("inner join condicaopagamento on compra.condicaopagamento = condicaopagamento.codigo ");
		sqlsb.append(" WHERE 1 = 1 ");

		if (!unidadeEnsino.getCodigo().equals(0)) {
			sqlsb.append("and unidadeensino.codigo = " + unidadeEnsino.getCodigo());
		}
		dataInicial.ifPresent(dt -> sqlsb.append("and recebimentocompra.data >= '").append(sdf.format(dt)).append("' "));
		dataFinal.ifPresent(dt -> sqlsb.append("and recebimentocompra.data <= '").append(sdf.format(dt)).append("' "));

		if (!fornecedor.getCodigo().equals(0)) {
			sqlsb.append(" and fornecedor.codigo = " + fornecedor.getCodigo());
		}
		if (!produtoServico.getCodigo().equals(0)) {
			sqlsb.append(" and produtoservico.codigo = " + produtoServico.getCodigo());
		}
		sqlsb.append(" and exists(select centroresultadoorigem.codigo 	from centroresultadoorigem where centroresultadoorigem.TipoCentroResultadoOrigem = 'COMPRA' ");
		sqlsb.append(" and centroresultadoorigem.codorigem = compra.codigo::varchar ");
		if (!categoriaDespesa.getCodigo().equals(0)) {
			sqlsb.append(" and centroresultadoorigem.categoriadespesa = ").append(categoriaDespesa.getCodigo()).append(" )");
		} else {
			sqlsb.append(")");
		}
		if (!situacaoRecebimento.equals("TODAS")) {
			sqlsb.append(" and recebimentocompra.situacao = '").append(situacaoRecebimento).append("' ");
		}

		if (ordenacaoRelatorio.equals("")) {
			sqlsb.append(" order by codigocompra, produto ");
		} else if (ordenacaoRelatorio.equals("unidadeEnsino")) {
			sqlsb.append(" order by  unidadeensino, codigocompra, produto ");
		} else if (ordenacaoRelatorio.equals("fornecedor")) {
			sqlsb.append(" order by  fornecedor, codigocompra, produto ");
		} else if (ordenacaoRelatorio.equals("categoriaDespesa")) {
			sqlsb.append(" order by  categoriadespesa, codigocompra, produto ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
		return tabelaResultado;
	}

	public String designIReportRelatorio(String layout) {
		return (caminhoBaseRelatorio() + layout + ".jrxml");
	}

	public String designIReportRelatorioExcel(String layout) {
		return (caminhoBaseRelatorio() + layout + "Excel.jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator);
	}

	public static String getIdEntidade() {
		return "RequisicaoRel";
	}

	public static String getIdEntidadeExcel() {
		return "RequisicaoExcelRel";
	}

}
