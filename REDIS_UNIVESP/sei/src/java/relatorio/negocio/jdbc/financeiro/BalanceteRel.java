package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.BalanceteRelVO;
import relatorio.negocio.interfaces.financeiro.BalanceteRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class BalanceteRel extends SuperRelatorio implements BalanceteRelInterfaceFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.financeiro.BalanceteRelInterfaceFacade#consultarContasPagasRecebidas(relatorio.negocio
	 * .comuns.financeiro.BalanceteRelVO)
	 */
	public List<BalanceteRelVO> consultarContasPagasRecebidas(BalanceteRelVO obj, UsuarioVO usuarioVO) {
		try {
			SqlRowSet rs = consultarIdentificadorCentroReceita();
			SqlRowSet rsd = consultarIdentificadorCategoriaDespesa();
			StringBuilder sqlStr = new StringBuilder("Select sum(valor) as valor, entidade, codigoEntidade, cast (tipo as VARCHAR), extract ( year from (SELECT cast(data as DATE))) as ano , extract ( month from (SELECT cast(data as DATE))) as mes from  (");
			sqlStr.append(" select sum(valorRecebimento) as valor,");
			sqlStr.append(" cast(contaReceberRecebimento.datarecebimento as DATE) as data,");
			sqlStr.append(montarSelectCentroReceita(rs) + " ,");
			sqlStr.append(" cast ('Receita' as VARCHAR) as tipo ");
			sqlStr.append(" from contaReceberRecebimento");
			sqlStr.append(" inner join contaReceber on contaReceberRecebimento.contaReceber = contareceber.codigo");
			sqlStr.append(" inner join centroreceita on centroreceita.codigo = contareceber.centroreceita");
			sqlStr.append(" where datarecebimento>='" + Uteis.getDataJDBC(obj.getDataInicio()) + "'");
			sqlStr.append(montarClausulaWhereCentroReceita(rs));
			sqlStr.append(" and datarecebimento<='" + Uteis.getDataJDBC(obj.getDataFim()) + "'");
			sqlStr.append(" and contaReceberRecebimento.formapagamentonegociacaorecebimento > 0 ");
			sqlStr.append(" and contaReceberRecebimento.tipoRecebimento = 'CR'");
			
			
			if (!(usuarioVO.getUnidadeEnsinoLogado() == null || usuarioVO.getUnidadeEnsinoLogado().getCodigo() == null || usuarioVO.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
				sqlStr.append(" and contaReceber.unidadeEnsino = " + usuarioVO.getUnidadeEnsinoLogado().getCodigo() + "");
			}
			
			sqlStr.append(" group by centroreceita.identificadorCentroReceita, centroreceita.descricao, contaReceberRecebimento.datarecebimento) as tabela");
			sqlStr.append(" where tabela.valor > 0 ");
			sqlStr.append(" group by entidade, codigoEntidade, ano, mes, tipo");

			sqlStr.append(" union all ");

			sqlStr.append(" Select sum(valor) as valor, entidade, codigoEntidade, cast (tipo as VARCHAR), extract ( year from (SELECT cast(data as DATE))) as ano , extract ( month from (SELECT cast(data as DATE))) as mes from  (");
			sqlStr.append(" select sum(valorPago) as valor, ");
			sqlStr.append(" cast(contapagarpagamento.data as DATE) as data, ");
			sqlStr.append(montarSelectCategoriaDespesa(rsd) + " ,");
			sqlStr.append(" cast('Despesa' as VARCHAR) as tipo ");
			sqlStr.append(" from contaPagarPagamento");
			sqlStr.append(" inner join contaPagar on contaPagarPagamento.contaPagar = contapagar.codigo");
			sqlStr.append(" inner join categoriaDespesa on categoriaDespesa.codigo = contapagar.centroDespesa");
			sqlStr.append(" where contaPagarPagamento.data>='" + Uteis.getDataJDBC(obj.getDataInicio()) + "'");
			sqlStr.append(montarClausulaWhereCategoriaDespesa(rsd));
			sqlStr.append(" and contaPagarPagamento.data<='" + Uteis.getDataJDBC(obj.getDataFim()) + "'");
			sqlStr.append(" and contaPagarPagamento.formapagamentonegociacaopagamento > 0");
			sqlStr.append(" and contaPagarPagamento.tipoPagamento = 'CR'");
			sqlStr.append(" group by categoriaDespesa.identificadorcategoriaDespesa, categoriaDespesa.descricao, categoriaDespesa.codigo,  contapagarpagamento.data) as tabela1");
			sqlStr.append(" where tabela1.valor > 0 ");
			sqlStr.append(" group by entidade, codigoEntidade, ano, mes, tipo order by entidade");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsulta(tabelaResultado);
		} catch (Exception e) {
			return new ArrayList<BalanceteRelVO>(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.financeiro.BalanceteRelInterfaceFacade#consultarTodasContas(relatorio.negocio.comuns.
	 * financeiro.BalanceteRelVO)
	 */
	public List<BalanceteRelVO> consultarTodasContas(BalanceteRelVO obj) {
		try {
			SqlRowSet rs = consultarIdentificadorCentroReceita();
			SqlRowSet rsd = consultarIdentificadorCategoriaDespesa();
			StringBuilder sqlStr = new StringBuilder("Select sum(valor) as valor, entidade, codigoEntidade, cast (tipo as VARCHAR), extract ( year from (SELECT cast(data as DATE))) as ano , extract ( month from (SELECT cast(data as DATE))) as mes from  (");
			sqlStr.append(" select sum(valorRecebido) as valor,");
			sqlStr.append(" cast(contaReceber.datavencimento as DATE) as data,");
			sqlStr.append(montarSelectCentroReceita(rs) + " ,");
			sqlStr.append(" cast ('Receita' as VARCHAR) as tipo ");
			sqlStr.append(" from contaReceber");
			sqlStr.append(" inner join centroreceita on centroreceita.codigo = contareceber.centroreceita");
			sqlStr.append(" where datavencimento>='" + Uteis.getDataJDBC(obj.getDataInicio()) + "'");
			sqlStr.append(montarClausulaWhereCentroReceita(rs));
			sqlStr.append(" and datavencimento<='" + Uteis.getDataJDBC(obj.getDataFim()) + "'");
			sqlStr.append(" group by centroreceita.identificadorCentroReceita, centroreceita.descricao, contaReceber.datavencimento) as tabela");
			sqlStr.append(" where tabela.valor > 0 ");
			sqlStr.append(" group by entidade, codigoEntidade, ano, mes, tipo");

			sqlStr.append(" union all ");

			sqlStr.append(" Select sum(valor) as valor, entidade, codigoEntidade, cast (tipo as VARCHAR), extract ( year from (SELECT cast(data as DATE))) as ano , extract ( month from (SELECT cast(data as DATE))) as mes from  (");
			sqlStr.append(" select sum(valorPago) as valor, ");
			sqlStr.append(" cast(contapagar.datavencimento as DATE) as data, ");
			sqlStr.append(montarSelectCategoriaDespesa(rsd) + " ,");
			sqlStr.append(" cast('Despesa' as VARCHAR) as tipo ");
			sqlStr.append(" from contaPagar");
			sqlStr.append(" inner join categoriaDespesa on categoriaDespesa.codigo = contapagar.centroDespesa");
			sqlStr.append(" where contaPagar.datavencimento>='" + Uteis.getDataJDBC(obj.getDataInicio()) + "'");
			sqlStr.append(montarClausulaWhereCategoriaDespesa(rsd));
			sqlStr.append(" and contaPagar.datavencimento<='" + Uteis.getDataJDBC(obj.getDataFim()) + "'");
			sqlStr.append(" group by categoriaDespesa.identificadorcategoriaDespesa, categoriaDespesa.descricao, categoriaDespesa.codigo,  contapagar.datavencimento) as tabela1");
			sqlStr.append(" where tabela1.valor > 0 ");
			sqlStr.append(" group by entidade, codigoEntidade, ano, mes, tipo order by entidade");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsulta(tabelaResultado);
		} catch (Exception e) {
			return new ArrayList<BalanceteRelVO>(0);
		}
	}

	private String montarSelectCategoriaDespesa(SqlRowSet rs) throws Exception {
		String sql = "";
		String sqlElse = " ";
		int i = 0;
		if (rs.next()) {
			rs.beforeFirst();
			// adiciona condiçao select entidade
			while (rs.next()) {
				sql += sqlElse + " case when (  CategoriaDespesa.identificadorCategoriaDespesa like('" + rs.getString("identificadorCategoriaDespesa") + "%')) "
						+ " then (select descricao from CategoriaDespesa where identificadorCategoriaDespesa = '" + rs.getString("identificadorCategoriaDespesa") + "') ";
				sqlElse = " else ";
				i++;
			}
			for (int j = 1; j <= i; j++) {
				sql += " end ";
			}
			sql += " as entidade, ";
			rs.beforeFirst();
			sqlElse = " ";

			// adiciona condiçao select codigoEntidade
			while (rs.next()) {
				sql += sqlElse + " case when (  CategoriaDespesa.identificadorCategoriaDespesa like('" + rs.getString("identificadorCategoriaDespesa") + "%')) "
						+ " then (select codigo from CategoriaDespesa where identificadorCategoriaDespesa = '" + rs.getString("identificadorCategoriaDespesa") + "') ";
				sqlElse = " else ";

			}
			for (int j = 1; j <= i; j++) {
				sql += " end ";
			}
			sql += " as codigoEntidade ";
			rs.beforeFirst();
		} else {
			sql += " CategoriaDespesa.descricao as entidade, CategoriaDespesa.codigo as codigoEntidade ";
		}
		return sql;

	}

	private String montarSelectCentroReceita(SqlRowSet rs) throws Exception {
		String sql = "";
		String sqlElse = " ";
		int i = 0;
		if (rs.next()) {
			rs.beforeFirst();
			// adiciona condiçao select entidade
			while (rs.next()) {
				sql += sqlElse + " case when (  CentroReceita.identificadorCentroReceita like('" + rs.getString("identificadorCentroReceita") + "%')) "
						+ " then (select descricao from CentroReceita where identificadorCentroReceita = '" + rs.getString("identificadorCentroReceita") + "') ";
				sqlElse = " else ";
				i++;
			}
			for (int j = 1; j <= i; j++) {
				sql += " end ";
			}
			sql += " as entidade, ";
			rs.beforeFirst();
			sqlElse = " ";

			// adiciona condiçao select codigoEntidade
			while (rs.next()) {
				sql += sqlElse + " case when (  CentroReceita.identificadorCentroReceita like('" + rs.getString("identificadorCentroReceita") + "%')) "
						+ " then (select codigo from CentroReceita where identificadorCentroReceita = '" + rs.getString("identificadorCentroReceita") + "') ";
				sqlElse = " else ";

			}
			for (int j = 1; j <= i; j++) {
				sql += " end ";
			}
			sql += " as codigoEntidade ";
			rs.beforeFirst();
		} else {
			sql += " CentroReceita.descricao as entidade, CentroReceita.codigo as codigoEntidade ";
		}
		return sql;

	}

	private String montarClausulaWhereCategoriaDespesa(SqlRowSet rs) throws Exception {
		String sql = " ";
		if (rs.next()) {
			rs.beforeFirst();
			String andOr = " and ( ";
			while (rs.next()) {
				sql += andOr + " CategoriaDespesa.identificadorCategoriaDespesa like ('" + rs.getString("identificadorCategoriaDespesa") + "%')";
				andOr = " or ";
			}
			if (andOr.equals(" or ")) {
				sql += " )";
			}
		}
		return sql;
	}

	private String montarClausulaWhereCentroReceita(SqlRowSet rs) throws Exception {
		String sql = " ";
		if (rs.next()) {
			rs.beforeFirst();
			String andOr = " and ( ";
			while (rs.next()) {
				sql += andOr + " CentroReceita.identificadorCentroReceita like ('" + rs.getString("identificadorCentroReceita") + "%')";
				andOr = " or ";
			}
			if (andOr.equals(" or ")) {
				sql += " )";
			}
		}
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.financeiro.BalanceteRelInterfaceFacade#consultarIdentificadorCategoriaDespesa()
	 */
	public SqlRowSet consultarIdentificadorCategoriaDespesa() throws Exception {
		
		String sql = "select identificadorCategoriaDespesa, descricao, codigo from categoriaDespesa where categoriaDespesaPrincipal = 0 or categoriaDespesaPrincipal is null ";
		// Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.FETCH_FORWARD);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return tabelaResultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.financeiro.BalanceteRelInterfaceFacade#consultarIdentificadorCentroReceita()
	 */
	public SqlRowSet consultarIdentificadorCentroReceita() throws Exception {
		
		String sql = "select identificadorcentroreceita, descricao, codigo from centroreceita where centroreceitaprincipal = 0 or centroreceitaprincipal is null ";
		// Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.FETCH_FORWARD);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return tabelaResultado;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ContaReceberVO</code> resultantes da consulta.
	 */
	public static List<BalanceteRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<BalanceteRelVO> vetResultado = new ArrayList<BalanceteRelVO>(0);
		BalanceteRelVO obj = null;
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, obj));
		}
		return vetResultado;
	}

	public static BalanceteRelVO montarDados(SqlRowSet dadosSQL, BalanceteRelVO obj) throws SQLException {
		obj = new BalanceteRelVO();
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setEntidade(dadosSQL.getString("entidade"));
		obj.setCodigoEntidade(dadosSQL.getInt("codigoentidade"));
		obj.setTipo(dadosSQL.getString("tipo"));
		obj.setAno(dadosSQL.getDouble("ano"));
		obj.setMes(dadosSQL.getDouble("mes"));
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("BalanceteSinteticoRel");
	}

	public static String getDesignIReportRelatorioCompleto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeCompleto() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorioCompleto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidadeCompleto() {
		return ("BalanceteRel");
	}
}
