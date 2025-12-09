package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.PagamentoRelVO;
import relatorio.negocio.interfaces.financeiro.PagamentoResumidoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PagamentoResumidoRel extends SuperRelatorio implements PagamentoResumidoRelInterfaceFacade {

	public PagamentoResumidoRel() {
		
	}

	public List<PagamentoRelVO> criarObjeto(Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, String formaPagamento, Integer filtroTipo) throws Exception {
		List<PagamentoRelVO> pagamentoRelVOs = new ArrayList<PagamentoRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(dataInicio, dataFim, unidadeEnsino, fornecedor, fornecedorNome, fornecedorCpfCnpj,
                funcionarioNome, funcionario, banco, bancoNome, filtroFornecedor, filtroFuncionario, filtroBanco, formaPagamento, filtroTipo);
		while (dadosSQL.next()) {
			pagamentoRelVOs.add(montarDados(dadosSQL));
		}
		return pagamentoRelVOs;
	}

	public PagamentoRelVO montarDados(SqlRowSet dadosSQL) {
		PagamentoRelVO pagamentoRelVO = new PagamentoRelVO();
		pagamentoRelVO.setBanco(dadosSQL.getString("banco_nome"));
		pagamentoRelVO.setDataPagamento(Uteis.getDataJDBC(dadosSQL.getDate("pagamento_data")));
		pagamentoRelVO.setFornecedor(dadosSQL.getString("fornecedor_nome"));
		pagamentoRelVO.setFuncionario(dadosSQL.getString("funcionario_nome"));
		pagamentoRelVO.setNegociacao(dadosSQL.getInt("pagamento_negociacao"));
		pagamentoRelVO.setTipoSacado(dadosSQL.getString("pagamento_tiposacado"));
		pagamentoRelVO.setUnidadeEnsino(dadosSQL.getInt("pagamento_unidadeensino"));
		pagamentoRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		pagamentoRelVO.setValor(dadosSQL.getDouble("pagamento_valor"));
		pagamentoRelVO.setValorPagamento(dadosSQL.getDouble("pagamento_valorpagamento"));
		pagamentoRelVO.setValorTroco(dadosSQL.getDouble("pagamento_troco"));
                pagamentoRelVO.setTipoContaCorrente(dadosSQL.getBoolean("contaCaixa"));
		return pagamentoRelVO;
	}

	public SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, String formaPagamento, Integer filtroTipo) throws Exception {
		String selectStr = "SELECT pagamento.data AS pagamento_data, pagamento.codigo AS pagamento_negociacao, pagamento.valortotal AS pagamento_valor, "
				+ "pagamento.valortotalpagamento AS pagamento_valorpagamento, pagamento.valortroco AS pagamento_troco, pagamento.tiposacado AS pagamento_tiposacado, "
				+ "pagamento.unidadeensino AS pagamento_unidadeensino, pagamento.funcionario AS pagamento_funcionario, pagamento.fornecedor AS pagamento_fornecedor, "
				+ "pagamento.banco AS pagamento_banco, unidadeensino.nome AS unidadeensino_nome, pessoa.nome AS funcionario_nome, fornecedor.nome AS fornecedor_nome, "
				+ "banco.nome AS banco_nome, contacorrente.numero, contaCorrente.contaCaixa "
                                + "FROM negociacaopagamento AS pagamento LEFT JOIN fornecedor ON (pagamento.fornecedor = fornecedor.codigo) "
				+ "LEFT JOIN funcionario ON (pagamento.funcionario = funcionario.codigo) LEFT JOIN pessoa ON (funcionario.pessoa = pessoa.codigo) "
				+ "LEFT JOIN unidadeensino ON (pagamento.unidadeensino = unidadeensino.codigo) LEFT JOIN banco ON (pagamento.banco = banco.codigo) "
                                + "LEFT JOIN formapagamentonegociacaopagamento fpnp on fpnp.negociacaocontapagar = pagamento.codigo "
                                + "LEFT JOIN formapagamento fp on fp.codigo = fpnp.formapagamento "
                                + "LEFT JOIN contacorrente on contacorrente.codigo = fpnp.contacorrente "
                                //+ "INNER JOIN contapagarpagamento as cpp on cpp.contapagar = contapagar.codigo "
                                //+ "INNER JOIN formapagamento as fp on fp.codigo = cpp.formapagamento"
                                ;
		selectStr = montarFiltrosRelatorio(selectStr, dataInicio, dataFim, unidadeEnsino, fornecedor, fornecedorNome, fornecedorCpfCnpj,
                funcionarioNome, funcionario, banco, bancoNome, filtroFornecedor, filtroFuncionario, filtroBanco, formaPagamento, filtroTipo);
		selectStr += " GROUP BY pagamento_unidadeensino, pagamento_data, pagamento_tiposacado, pagamento_negociacao, pagamento_valor, pagamento_valorpagamento, pagamento_troco, "
				+ "unidadeensino_nome, pagamento_funcionario, pagamento_fornecedor, pagamento_banco, funcionario_nome, fornecedor_nome, "
				+ "banco_nome, fp.nome, contacorrente.numero, contaCorrente.contaCaixa "
                                + "ORDER BY pagamento.unidadeensino, pagamento_data, pagamento_tiposacado ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
		return tabelaResultado;
	}

	private String montarFiltrosRelatorio(String selectStr, Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, String formaPagamento, Integer filtroTipo) {
		String filtros = "";
		boolean adicionarAND = true;
		if ((unidadeEnsino != null) && (unidadeEnsino.getCodigo().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.unidadeensino = " + unidadeEnsino.getCodigo().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (filtroFornecedor.intValue() == 1) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'FO' )", adicionarAND);
			adicionarAND = true;
		} else if ((filtroFornecedor.intValue() == 2) && (fornecedor != null) && (fornecedor.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.fornecedor = " + fornecedor.intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (filtroFuncionario.intValue() == 1) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'FU' )", adicionarAND);
		} else if ((filtroFuncionario.intValue() == 2) && (funcionario != null) && (funcionario.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.funcionario = " + funcionario.intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (filtroBanco.intValue() == 1) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.tiposacado <> 'BA' )", adicionarAND);
		} else if ((filtroBanco.intValue() == 2) && (banco != null) && (banco.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( pagamento.banco = " + banco.intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
                if (filtroTipo.intValue() == 1) {
                    filtros = adicionarCondicionalWhere(filtros, "( upper(fp.tipo) = 'NENHUM')", adicionarAND);
                    adicionarAND = true;
                } else if ((filtroTipo.intValue() == 2) && (formaPagamento != null) && (!formaPagamento.equals(""))) {
                    filtros = adicionarCondicionalWhere(filtros, "( upper(fp.tipo) = ('" + formaPagamento.toUpperCase() + "'))", adicionarAND);
                    adicionarAND = true;
                }
		if (dataInicio != null) {
			filtros = adicionarCondicionalWhere(filtros, "(pagamento.data >= '" + Uteis.getDataJDBC(Uteis.getDateTime(dataInicio, 0, 0, 0)) + " ')", adicionarAND);
			adicionarAND = true;
		}
		if (dataFim != null) {
			filtros = adicionarCondicionalWhere(filtros, "(pagamento.data <= '" + Uteis.getDataJDBC(Uteis.getDateTime(dataFim, 23, 59, 59)) + " ')", adicionarAND);
			adicionarAND = true;
		}
		filtros = filtros.replaceFirst("AND", "WHERE");
		selectStr += filtros;
		return selectStr;
	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}
	
    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

	public static String getIdEntidade() {
		return ("PagamentoResumidoRel");
	}
}
