package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberRecebimentoInterfaceFacade;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ContaReceberRecebimentoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ContaReceberRecebimentoVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see ContaReceberRecebimentoVO
 * @see ControleAcesso
 * @see ContaReceber
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaReceberRecebimento extends ControleAcesso implements ContaReceberRecebimentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5038085977374275649L;
	protected static String idEntidade;

	public ContaReceberRecebimento() throws Exception {
		super();
		setIdEntidade("ContaReceber");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ContaReceberRecebimentoVO</code>.
	 */
	public ContaReceberRecebimentoVO novo() throws Exception {
		ContaReceberRecebimento.incluir(getIdEntidade());
		ContaReceberRecebimentoVO obj = new ContaReceberRecebimentoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ContaReceberRecebimentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaReceberRecebimentoVO</code> que
	 *            será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContaReceberRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			// ContaReceberRecebimentoVO.validarDados(obj);
			// obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ContaReceberRecebimento( contaReceber, formaPagamentoNegociacaoRecebimento, valorRecebimento, dataRecebimento, formaPagamento, tipoRecebimento, negociacaoRecebimento, motivo, responsavel,notaFiscalSaidaServico, recebimentoTerceirizado ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					if (obj.getContaReceber().intValue() != 0) {
						sqlInserir.setInt(1, obj.getContaReceber().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getFormaPagamentoNegociacaoRecebimento().intValue() != 0) {
						sqlInserir.setInt(2, obj.getFormaPagamentoNegociacaoRecebimento().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setDouble(3, obj.getValorRecebimento().doubleValue());
					sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataRecebimeto()));
					if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getFormaPagamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getTipoRecebimento());
					sqlInserir.setInt(7, obj.getNegociacaoRecebimento().intValue());
					sqlInserir.setString(8, obj.getMotivo());
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setBoolean(11, obj.getRecebimentoTerceirizado());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ContaReceberRecebimentoVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaReceberRecebimentoVO</code> que
	 *            será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterar(final ContaReceberRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			// obj.realizarUpperCaseDados();
			final String sql = "UPDATE ContaReceberRecebimento set ContaReceber=?, FormaPagamentoNegociacaoRecebimento=?, valorRecebimento=?, dataRecebimento=?, formaPagamento=?, tipoRecebimento=?, negociacaoRecebimento=?, motivo=?, responsavel=?,notaFiscalSaidaServico=?, recebimentoTerceirizado=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					if (obj.getContaReceber().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getContaReceber().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getFormaPagamentoNegociacaoRecebimento().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getFormaPagamentoNegociacaoRecebimento().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setDouble(3, obj.getValorRecebimento().doubleValue());
					sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataRecebimeto()));
					if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getFormaPagamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getTipoRecebimento());
					sqlAlterar.setInt(7, obj.getNegociacaoRecebimento().intValue());
					sqlAlterar.setString(8, obj.getMotivo());
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setBoolean(11, obj.getRecebimentoTerceirizado());
					sqlAlterar.setInt(12, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ContaReceberRecebimentoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaReceberRecebimentoVO</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContaReceberRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		// ContaReceberRecebimento.excluir(getIdEntidade());
		String sql = "DELETE FROM ContaReceberRecebimento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List consultarPorRecebimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaReceberRecebimento WHERE NegociacaoRecebimento = " + valorConsulta.intValue() + " ORDER BY NegociacaoRecebimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ContaReceberRecebimento</code> através do valor do atributo
	 * <code>Integer FormaPagamentoNegociacaoRecebimento</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ContaReceberRecebimentoVO> consultarPorFormaPagamentoNegociacaoRecebimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaReceberRecebimento WHERE FormaPagamentoNegociacaoRecebimento = " + valorConsulta.intValue() + " ORDER BY FormaPagamentoNegociacaoRecebimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoContaReceberCodigoNegociacaoTipoPagamentoPago(Integer valorConsulta, Integer negociacao, String tipoPagamento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), true);
		String sqlStr = "SELECT ContaReceberRecebimento.* FROM ContaReceberRecebimento WHERE contaReceber = " + valorConsulta.intValue() + " and negociacaorecebimento = " + negociacao.intValue() + " ";
		if (tipoPagamento == null) {
			tipoPagamento = "";
		}
		if (!tipoPagamento.equals("")) {
			sqlStr += " and tipoRecebimento = '" + tipoPagamento.toUpperCase() + "' ";
		}
		sqlStr += " ORDER BY codigo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ContaReceberRecebimentoVO> consultarPorContaReceberParaGeracaoLancamentoContabil(Integer contaRereber, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder(" select contareceberrecebimento.valorrecebimento, contareceberrecebimento.datarecebimento, ");
		sb.append(" contareceberrecebimento.formapagamentonegociacaorecebimento,  ");
		
		sb.append(" formapagamento.codigo as \"formapagamento.codigo\", ");
		sb.append(" formapagamento.tipo as \"formapagamento.tipo\", ");
		
		sb.append(" formapagamentonegociacaorecebimento.codigo as \"formapagamentonegociacaorecebimento.codigo\", ");
		sb.append(" formapagamentonegociacaorecebimento.datacredito as \"formapagamentonegociacaorecebimento.datacredito\", ");
		sb.append(" formapagamentonegociacaorecebimento.taxaOperadora as \"formapagamentonegociacaorecebimento.taxaOperadora\", ");
		sb.append(" formapagamentonegociacaorecebimento.taxaAntecipacao as \"formapagamentonegociacaorecebimento.taxaAntecipacao\", ");
		
		sb.append(" operadoraCartao.codigo as \"operadoraCartao.codigo\", ");
		sb.append(" operadoraCartao.nome as \"operadoraCartao.nome\", ");
		sb.append(" operadoraCartao.tipo as \"operadoraCartao.tipo\", ");
		
		sb.append(" contaCorrenteOperadoraCartao.codigo as \"contaCorrenteOperadoraCartao.codigo\", ");
		sb.append(" contaCorrenteOperadoraCartao.utilizaTaxaCartaoDebito as \"contaCorrenteOperadoraCartao.utilizaTaxaCartaoDebito\", ");
		sb.append(" contaCorrenteOperadoraCartao.utilizaTaxaCartaoCredito as \"contaCorrenteOperadoraCartao.utilizaTaxaCartaoCredito\", ");
		
		sb.append(" contaCorrente.codigo as \"contaCorrente.codigo\", ");
		sb.append(" contaCorrente.utilizaTaxaCartaoDebito as \"contaCorrente.utilizaTaxaCartaoDebito\", ");
		sb.append(" contaCorrente.utilizaTaxaCartaoCredito as \"contaCorrente.utilizaTaxaCartaoCredito\" ");
		
		
		sb.append(" from contareceber ");
		sb.append(" inner join contareceberrecebimento on contareceberrecebimento.contareceber = contareceber.codigo ");
		sb.append(" inner join formapagamentonegociacaorecebimento on contareceberrecebimento.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");
		sb.append(" left join operadoraCartao ON operadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.operadoraCartao ");
		sb.append(" inner join formapagamento on formapagamentonegociacaorecebimento.formapagamento = formapagamento.codigo ");
		sb.append(" inner join contaCorrente ON contaCorrente.codigo = formaPagamentoNegociacaoRecebimento.contaCorrente ");
		sb.append(" left join contaCorrente as contaCorrenteOperadoraCartao ON contaCorrenteOperadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.contaCorrenteOperadoraCartao ");
		sb.append(" where contareceber.codigo = ").append(contaRereber);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ContaReceberRecebimentoVO> vetResultado = new ArrayList<ContaReceberRecebimentoVO>(0);
		while (dadosSQL.next()) {
			ContaReceberRecebimentoVO obj = new ContaReceberRecebimentoVO();
			obj.setValorRecebimento((dadosSQL.getDouble("valorrecebimento")));
			obj.setDataRecebimeto(dadosSQL.getDate("datarecebimento"));
			
			obj.getFormaPagamentoNegociacaoRecebimentoVO().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimento.codigo"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().setDataCredito((dadosSQL.getDate("formaPagamentoNegociacaoRecebimento.datacredito")));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeOperacao(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimento.taxaOperadora"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeAntecipacao(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimento.taxaAntecipacao"));
			if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao().equals(0.0)) {
				obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeAntecipacao(obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao());
			}
			
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setCodigo((dadosSQL.getInt("formapagamento.codigo")));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setTipo((dadosSQL.getString("formapagamento.tipo")));

			obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao.codigo"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setNome(dadosSQL.getString("operadoraCartao.nome"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setTipo(dadosSQL.getString("operadoraCartao.tipo"));			
			
			
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().setUtilizaTaxaCartaoDebito(dadosSQL.getBoolean("contaCorrente.utilizaTaxaCartaoDebito"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().setUtilizaTaxaCartaoCredito(dadosSQL.getBoolean("contaCorrente.utilizaTaxaCartaoCredito"));
			
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setCodigo(dadosSQL.getInt("contaCorrenteOperadoraCartao.codigo"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setUtilizaTaxaCartaoDebito(dadosSQL.getBoolean("contaCorrenteOperadoraCartao.utilizaTaxaCartaoDebito"));
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setUtilizaTaxaCartaoCredito(dadosSQL.getBoolean("contaCorrenteOperadoraCartao.utilizaTaxaCartaoCredito"));
			
			
			
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ContaReceberRecebimento</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>ContaReceber</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoContaReceber(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), true);
		String sqlStr = "SELECT * FROM ContaReceberRecebimento WHERE ContaReceber = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ContaReceberRecebimento</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaReceberRecebimento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
	 */
	public static List<ContaReceberRecebimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ContaReceberRecebimentoVO> vetResultado = new ArrayList<ContaReceberRecebimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ContaReceberRecebimentoVO</code>.
	 * 
	 * @return O objeto da classe <code>ContaReceberRecebimentoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static ContaReceberRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContaReceberRecebimentoVO obj = new ContaReceberRecebimentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setContaReceber(new Integer(dadosSQL.getInt("ContaReceber")));
		obj.setFormaPagamentoNegociacaoRecebimento(new Integer(dadosSQL.getInt("FormaPagamentoNegociacaoRecebimento")));
		obj.setValorRecebimento(new Double(dadosSQL.getDouble("valorRecebimento")));
		obj.setDataRecebimeto(dadosSQL.getTimestamp("dataRecebimento"));
		obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formaPagamento")));
		obj.setTipoRecebimento(dadosSQL.getString("tipoRecebimento"));
		obj.setMotivo(dadosSQL.getString("motivo"));
		obj.setNegociacaoRecebimento(new Integer(dadosSQL.getInt("negociacaorecebimento")));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getNotaFiscalSaidaServicoVO().setCodigo(dadosSQL.getInt("notaFiscalSaidaServico"));
		obj.setRecebimentoTerceirizado(dadosSQL.getBoolean("recebimentoTerceirizado"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosNotaFiscal(obj, nivelMontarDados, usuario);
		montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.setFormaPagamentoNegociacaoRecebimentoVO(Uteis.montarDadosVO(dadosSQL.getInt("FormaPagamentoNegociacaoRecebimento"), FormaPagamentoNegociacaoRecebimentoVO.class, p -> getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		return obj;
	}

	public static void montarDadosFormaPagamento(ContaReceberRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0 || obj.getFormaPagamento() == null) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosNotaFiscal(ContaReceberRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue() == 0 || obj.getNotaFiscalSaidaServicoVO() == null) {
			obj.setNotaFiscalSaidaServicoVO(new NotaFiscalSaidaServicoVO());
			return;
		}
		obj.setNotaFiscalSaidaServicoVO(getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarPorChavePrimaria(obj.getNotaFiscalSaidaServicoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavel(ContaReceberRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel() == null || obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>ContaReceberRecebimentoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe
	 * <code>ContaReceberRecebimento</code>.
	 * 
	 * @param <code>ContaReceber</code> campo chave para exclusão dos objetos no
	 *        BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaReceberRecebimentos(Integer contaReceber, UsuarioVO usuario) throws Exception {
		// ContaReceberRecebimento.excluir(getIdEntidade());
		String sql = "DELETE FROM ContaReceberRecebimento WHERE (ContaReceber = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { contaReceber });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaReceberRecebimentos(Integer contaReceber, List objeto, UsuarioVO usuario) throws Exception {
		// CtRPtRLog.excluir(getIdEntidade());
		String sql = "DELETE FROM ContaReceberRecebimento WHERE (ContaReceber = ?) ";
		Iterator i = objeto.iterator();
		while (i.hasNext()) {
			ContaReceberRecebimentoVO obj = (ContaReceberRecebimentoVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sql += " and codigo != " + obj.getCodigo();
			}
		}
		getConexao().getJdbcTemplate().update(sql+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[] { contaReceber });
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>ContaReceberRecebimentoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirContaReceberRecebimentos</code> e
	 * <code>incluirContaReceberRecebimentos</code> disponíveis na classe
	 * <code>ContaReceberRecebimento</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarContaReceberRecebimentos(Integer ContaReceber, Integer notaFiscalSaidaServico, @SuppressWarnings("rawtypes") List objetos, UsuarioVO usuario) throws Exception {
		// excluirContaReceberRecebimentos( ContaReceber );
		incluirContaReceberRecebimentos(ContaReceber, notaFiscalSaidaServico, objetos, usuario);
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>ContaReceberRecebimentoVO</code> no BD. Garantindo o relacionamento
	 * com a entidade principal <code>financeiro.ContaReceber</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirContaReceberRecebimentos(Integer ContaReceberPrm, Integer notaFiscalSaidaServico, List<ContaReceberRecebimentoVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ContaReceberRecebimentoVO obj = (ContaReceberRecebimentoVO) e.next();
			obj.setContaReceber(ContaReceberPrm);
			obj.getNotaFiscalSaidaServicoVO().setCodigo(notaFiscalSaidaServico);
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuario);
			}
			if(Uteis.isAtributoPreenchido(notaFiscalSaidaServico)){
				alterarCodigoNotaFiscalSaidaServico(notaFiscalSaidaServico, obj.getCodigo(), usuario);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>ContaReceberRecebimentoVO</code> relacionados a um objeto da classe
	 * <code>financeiro.ContaReceber</code>.
	 * 
	 * @param ContaReceber
	 *            Atributo de <code>financeiro.ContaReceber</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>ContaReceberRecebimentoVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List consultarContaReceberRecebimentos(Integer contaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ContaReceberRecebimento.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ContaReceberRecebimento WHERE ContaReceber = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { contaReceber });
		while (resultado.next()) {
			ContaReceberRecebimentoVO novoObj = new ContaReceberRecebimentoVO();
			novoObj = ContaReceberRecebimento.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public List<ContaReceberRecebimentoVO> consultarContaReceberPorRecebimentos(Integer contaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ContaReceberRecebimento.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ContaReceberRecebimento WHERE ContaReceber = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { contaReceber });
		while (resultado.next()) {
			ContaReceberRecebimentoVO novoObj = new ContaReceberRecebimentoVO();
			novoObj = ContaReceberRecebimento.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ContaReceberRecebimentoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ContaReceberRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ContaReceberRecebimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContaReceberRecebimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ContaReceberRecebimentoVO consultarPorCodigoContaReceber(Integer codigoContaReceber) throws Exception {
		String sql = "SELECT * FROM ContaReceberRecebimento WHERE contaReceber = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoContaReceber });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContaReceberRecebimento ).");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ContaReceberRecebimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContaReceberRecebimento.idEntidade = idEntidade;
	}

	@Override
	public List<ContaReceberRecebimentoVO> consultarContasRecebidasNotaFiscalSaida(MatriculaVO matriculaVO, Date dataInicio, Date dataFim, CursoVO cursoVO, TurmaVO turmaVO, String tipoValorConsultaContasRecebidas, String tipoDataConsiderar,
			 									FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String situacaoContaReceber,
												ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select sum(crr_valorRecebimento) as crr_valorRecebimento, crr_contaReceber, crr_notaFiscalSaidaServico, nfss_notaFiscalSaida, mensagemretorno, numeroNotaFiscalSaida, numeroRPSNotaFiscalSaida, loteNotaFiscalSaida, situacaoNotaFiscalSaida, ");
		sqlStr.append("crr_negociacaoRecebimento, crr_tipoRecebimento, crr_dataRecebimento, tipopessoa, matricula, tipoOrigem, parcela, pessoa, responsavelfinanceiro, candidato, funcionario, fornecedor, parceiro, emitirNotaFiscalParaBeneficiario, crr_codigo, contareceber_descricaopagamento ");
		sqlStr.append("from ( ");
		sqlStr.append("select distinct contareceber.codigo as contareceber, contareceber.situacao as contareceber_situacao, crr.codigo as crr_codigo, crr.formaPagamento as crr_formaPagamento, crr.notaFiscalSaidaServico as crr_notaFiscalSaidaServico, nfss.notaFiscalSaida as nfss_notaFiscalSaida, notaFiscalSaida.numero as numeroNotaFiscalSaida, notaFiscalSaida.numeroRPS as numeroRPSNotaFiscalSaida, notaFiscalSaida.lote as loteNotaFiscalSaida, notaFiscalSaida.mensagemRetorno as mensagemretorno, ");
		sqlStr.append(" crr.contaReceber as crr_contaReceber, contareceber.matriculaAluno as matricula, contareceber.tipoPessoa as tipopessoa, contareceber.tipoOrigem, contareceber.parcela, ");
		sqlStr.append(" case when contareceber.tipopessoa = 'PA' then matricula.aluno else contareceber.pessoa end as pessoa, ");
		sqlStr.append(" case when contareceber.tipopessoa = 'PA' then (select filiacao.pais from filiacao where  filiacao.aluno = matricula.aluno and filiacao.responsavelFinanceiro order by filiacao.pais  limit 1 ) else contareceber.responsavelfinanceiro end as responsavelfinanceiro, ");
		sqlStr.append(" parceiro.emitirNotaFiscalParaBeneficiario, contareceber.candidato, contareceber.funcionario, contareceber.fornecedor, contareceber.parceiro,");
		sqlStr.append(" crr.formaPagamentoNegociacaoRecebimento as crr_formaPagamentoNegociacaoRecebimento, notaFiscalSaida.situacao as situacaoNotaFiscalSaida, ");
		sqlStr.append(" crr.negociacaoRecebimento as crr_negociacaoRecebimento, crr.tipoRecebimento as crr_tipoRecebimento, crr.dataRecebimento as crr_dataRecebimento, crr.responsavel as crr_responsavel, crr.motivo as crr_motivo, contareceber.descricaopagamento as contareceber_descricaopagamento, ");
		if (tipoValorConsultaContasRecebidas.equals("valorCompensado")) {
			sqlStr.append("(case when fp.tipo = 'CH' and cheque.pago = true then crr.valorrecebimento ");
			sqlStr.append("when fp.tipo = 'CA' then formapagamentonegociacaorecebimentocartaocredito.valorparcela::numeric(20,2) ");
			sqlStr.append("when fp.tipo != 'CH' and fp.tipo != 'CA' then crr.valorrecebimento end) as crr_valorRecebimento, ");
			
			
		}else if (tipoValorConsultaContasRecebidas.equals("valorCheio")) {				
			sqlStr.append("contareceber.valor as crr_valorRecebimento, ");
		} else if(tipoValorConsultaContasRecebidas.equals("valorRecebido")) {
			sqlStr.append("crr.valorrecebimento as crr_valorRecebimento, ");
		}
		if(tipoDataConsiderar.equals("dataCompensacao")){
			sqlStr.append("(case when fp.tipo = 'CH' and cheque.pago = true then cheque.dataprevisao::Date ");
			sqlStr.append("when fp.tipo = 'CA' then contareceber.datavencimento::Date + ('' || configuracaofinanceirocartao.diabasecreditoconta::varchar || 'days''')::interval ");
			sqlStr.append("when fp.tipo = 'BO' then  (case when formapagamentonegociacaorecebimento.datacredito is null then  negociacaorecebimento.data::Date else formapagamentonegociacaorecebimento.datacredito::DATE end)  ");
			sqlStr.append("when fp.tipo != 'CH' and fp.tipo != 'CA' and fp.tipo != 'BO' then  negociacaorecebimento.data::Date end) as dataRecebimento ");
		}else if(tipoDataConsiderar.equals("dataVencimento")){
			sqlStr.append("contareceber.datavencimento::Date as dataRecebimento ");
		}else if(tipoDataConsiderar.equals("dataRecebimento")){
			sqlStr.append("negociacaorecebimento.data::Date as dataRecebimento ");
		}
		sqlStr.append("from contareceberrecebimento crr ");
		sqlStr.append("inner join contareceber on contareceber.codigo = crr.contareceber ");		
		sqlStr.append("inner join contarecebernegociacaorecebimento on contareceber.codigo = contarecebernegociacaorecebimento.contareceber ");
		sqlStr.append("inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sqlStr.append("inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = crr.formapagamentonegociacaorecebimento ");
		sqlStr.append("inner join formapagamento fp on fp.codigo = formapagamentonegociacaorecebimento.formapagamento ");
		sqlStr.append("left join cheque on cheque.codigo = formapagamentonegociacaorecebimento.cheque ");
		sqlStr.append("left join formapagamentonegociacaorecebimentocartaocredito on ((formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null and  formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo) "); 
		sqlStr.append("	or (formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is not null and  formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito)) ");
		sqlStr.append("left join operadoracartao on operadoracartao.codigo = formapagamentonegociacaorecebimento.operadoracartao ");
		sqlStr.append("left join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino ");
		sqlStr.append("left join configuracaofinanceiro on configuracaofinanceiro.configuracoes = unidadeensino.configuracoes ");
		sqlStr.append("left join configuracaofinanceirocartao on configuracaofinanceirocartao.configuracaofinanceiro = configuracaofinanceiro.codigo  and configuracaofinanceirocartao.operadoracartao = operadoracartao.codigo ");
		sqlStr.append("left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sqlStr.append("left join matricula on matricula.matricula = contareceber.matriculaaluno ");
		sqlStr.append("left join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append("left join turma turmareq on turmareq.codigo = contareceber.turma ");
		sqlStr.append("left join curso on curso.codigo = turma.curso ");
		sqlStr.append("left join curso cursoreq on cursoreq.codigo = turmareq.curso ");
		sqlStr.append("left join notaFiscalSaidaServico nfss on nfss.codigo = crr.notaFiscalSaidaServico ");
		sqlStr.append("left join notaFiscalSaida on notaFiscalSaida.codigo = nfss.notaFiscalSaida ");
		sqlStr.append("left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sqlStr.append("where contareceber.unidadeEnsino = ").append(unidadeEnsino);
		sqlStr.append(" and case when notaFiscalSaida.codigo is not null then notaFiscalSaida.situacao in ('', 'RE', 'EN', 'CA') else notaFiscalSaida.codigo is null end and (contareceber.notaFiscalSaidaServico is null or contareceber.notaFiscalSaidaServico = nfss.codigo )");
		if (!matriculaVO.getMatricula().equals("")) {
			sqlStr.append(" and contareceber.matriculaAluno = '").append(matriculaVO.getMatricula()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(cursoVO)) {
			sqlStr.append(" and (curso.codigo = ").append(cursoVO.getCodigo()).append(" or cursoreq.codigo = ").append(cursoVO.getCodigo()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" and (turma.codigo = ").append(turmaVO.getCodigo()).append(" or turmareq.codigo = ").append(turmaVO.getCodigo()).append(" ) ");
		}
		adicionarFiltroTipoOrigem(sqlStr, filtroRelatorioFinanceiroVO);
		sqlStr.append(") as t ");
		sqlStr.append("where contareceber_situacao = 'RE' ");
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" and dataRecebimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" and dataRecebimento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		if (tipoValorConsultaContasRecebidas.equals("valorCompensado")) {
			sqlStr.append(" and crr_valorRecebimento > 0 ");
			
			
		}else if (tipoValorConsultaContasRecebidas.equals("valorCheio")) {				
			sqlStr.append(" and crr_valorRecebimento > 0");
		} else if(tipoValorConsultaContasRecebidas.equals("valorRecebido")) {
			sqlStr.append(" and crr_valorRecebimento is not null and crr_valorRecebimento != 0  ");
		}
		
		sqlStr.append("group by crr_contaReceber, contareceber_situacao, crr_contaReceber, situacaoNotaFiscalSaida, ");
		sqlStr.append("crr_negociacaoRecebimento, crr_tipoRecebimento, crr_dataRecebimento, crr_notaFiscalSaidaServico, nfss_notaFiscalSaida, mensagemretorno, numeroNotaFiscalSaida, numeroRPSNotaFiscalSaida, loteNotaFiscalSaida, tipopessoa, matricula, tipoOrigem, parcela, pessoa, responsavelfinanceiro, candidato, funcionario, fornecedor, parceiro, emitirNotaFiscalParaBeneficiario, contareceber_descricaopagamento, ");
		sqlStr.append("crr_codigo order by crr_contaReceber");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosContasRecebidas(rs, configuracaoFinanceiroVO, unidadeEnsino, usuarioVO);
	}

	public List<ContaReceberRecebimentoVO> montarDadosContasRecebidas(SqlRowSet rs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs = new ArrayList<ContaReceberRecebimentoVO>(0);
		ContaReceberRecebimentoVO contaReceberRecebimentoVO = null;
		while (rs.next()) {
			contaReceberRecebimentoVO = new ContaReceberRecebimentoVO();
			contaReceberRecebimentoVO.setCodigo(rs.getInt("crr_codigo"));
//			contaReceberRecebimentoVO.getFormaPagamento().setCodigo(rs.getInt("crr_formaPagamento"));
			contaReceberRecebimentoVO.setContaReceber(rs.getInt("crr_contaReceber"));
//			contaReceberRecebimentoVO.setFormaPagamentoNegociacaoRecebimento(rs.getInt("crr_formaPagamentoNegociacaoRecebimento"));
			contaReceberRecebimentoVO.setValorRecebimento(rs.getDouble("crr_valorRecebimento"));
//			contaReceberRecebimentoVO.setNegociacaoRecebimento(rs.getInt("crr_negociacaoRecebimento"));
			contaReceberRecebimentoVO.setTipoRecebimento(rs.getString("crr_tipoRecebimento"));
			contaReceberRecebimentoVO.setDataRecebimeto(rs.getDate("crr_dataRecebimento"));
//			contaReceberRecebimentoVO.getResponsavel().setCodigo(rs.getInt("crr_responsavel"));
//			contaReceberRecebimentoVO.setMotivo(rs.getString("crr_motivo"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().setCodigo(rs.getInt("crr_notaFiscalSaidaServico"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setCodigo(rs.getInt("nfss_notaFiscalSaida"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setNumero(rs.getLong("numeroNotaFiscalSaida"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setNumeroRPS(rs.getInt("numeroRPSNotaFiscalSaida"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setLote(rs.getString("loteNotaFiscalSaida"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setSituacao(rs.getString("situacaoNotaFiscalSaida"));
			contaReceberRecebimentoVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setMensagemRetorno(rs.getString("mensagemretorno"));
			contaReceberRecebimentoVO.getContaReceberVO().setCodigo(rs.getInt("crr_contaReceber"));
			contaReceberRecebimentoVO.getContaReceberVO().setTipoPessoa(rs.getString("tipopessoa"));
			contaReceberRecebimentoVO.getContaReceberVO().getMatriculaAluno().setMatricula(rs.getString("matricula"));
			contaReceberRecebimentoVO.getContaReceberVO().setTipoOrigem(rs.getString("tipoOrigem"));
			contaReceberRecebimentoVO.getContaReceberVO().setParcela(rs.getString("parcela"));
			contaReceberRecebimentoVO.getContaReceberVO().getPessoa().setCodigo(rs.getInt("pessoa"));
			contaReceberRecebimentoVO.getContaReceberVO().getResponsavelFinanceiro().setCodigo(rs.getInt("responsavelfinanceiro"));
			contaReceberRecebimentoVO.getContaReceberVO().getCandidato().setCodigo(rs.getInt("candidato"));
			contaReceberRecebimentoVO.getContaReceberVO().getFuncionario().setCodigo(rs.getInt("funcionario"));
			contaReceberRecebimentoVO.getContaReceberVO().getFornecedor().setCodigo(rs.getInt("fornecedor"));
			contaReceberRecebimentoVO.getContaReceberVO().getParceiroVO().setCodigo(rs.getInt("parceiro"));
			contaReceberRecebimentoVO.getContaReceberVO().getParceiroVO().setEmitirNotaFiscalParaBeneficiario(rs.getBoolean("emitirNotaFiscalParaBeneficiario"));
			contaReceberRecebimentoVO.getContaReceberVO().setDescricaoPagamento(rs.getString("contareceber_descricaopagamento"));
//			contaReceberRecebimentoVO.setContaReceberVO(getFacadeFactory().getContaReceberFacade().consultarDadosGerarNotaFiscalSaida(contaReceberRecebimentoVO.getContaReceber(), false, configuracaoFinanceiroVO, unidadeEnsino, usuarioVO));
//			contaReceberRecebimentoVO.setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().consultarPorChavePrimaria(contaReceberRecebimentoVO.getNegociacaoRecebimento(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO));
//			contaReceberRecebimentoVO.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(contaReceberRecebimentoVO.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
			contaReceberRecebimentoVOs.add(contaReceberRecebimentoVO);
		}
		return contaReceberRecebimentoVOs;
	}
	
	
	@Override
	public List<ContaReceberRecebimentoVO> consultarContasRecebidasComFormaPagamento(Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select ");
		sqlStr.append(" formapagamento.nome, formapagamento.tipo, cheque.dataprevisao, cheque.numero,  ");
		sqlStr.append(" (case when (cheque.situacao = 'EC' or cheque.situacao = 'PA' or cheque.situacao = 'PE' or (cheque.situacao = 'BA' and cheque.pago = false)) then 'Aguardando Compensação' ");
		sqlStr.append("       when (cheque.situacao = 'BA' and cheque.pago = true) then 'Compensação'");
		sqlStr.append("       when cheque.situacao = 'DE' then 'Cheque Devolvido'");
		sqlStr.append("       when cheque.situacao = 'DS' then 'Devolvido ao Sacado' else '' end ) as situacao_cheque ");
		sqlStr.append(" from ContaReceberRecebimento ");
		sqlStr.append(" inner join formapagamento on ContaReceberRecebimento.formapagamento = formapagamento.codigo ");
		sqlStr.append(" left join formapagamentonegociacaorecebimento on ContaReceberRecebimento.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");
		sqlStr.append(" left join cheque on formapagamentonegociacaorecebimento.cheque = cheque.codigo ");
		sqlStr.append(" where ContaReceberRecebimento.contareceber = ").append(contaReceber);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs = new ArrayList<ContaReceberRecebimentoVO>(0);
		ContaReceberRecebimentoVO contaReceberRecebimentoVO = null;
		while (rs.next()) {
			contaReceberRecebimentoVO = new ContaReceberRecebimentoVO();
			contaReceberRecebimentoVO.getFormaPagamento().setNome(rs.getString("nome"));
			contaReceberRecebimentoVO.getFormaPagamento().setTipo(rs.getString("tipo"));
			contaReceberRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVO().getCheque().setSituacaoChequeApresentarAluno(rs.getString("situacao_cheque"));
			contaReceberRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVO().getCheque().setDataPrevisao(rs.getDate("dataprevisao"));
			contaReceberRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVO().getCheque().setNumero(rs.getString("numero"));
			contaReceberRecebimentoVOs.add(contaReceberRecebimentoVO);

		}
		return contaReceberRecebimentoVOs;
	}

	public void adicionarFiltroTipoOrigem(StringBuilder sqlStr, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		sqlStr.append(" AND contaReceber.tipoOrigem in (''");
		if (filtroRelatorioFinanceiroVO.getTipoOrigemBiblioteca()) {
			sqlStr.append(", 'BIB'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(", 'BCC'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemContratoReceita()) {
			sqlStr.append(", 'CTR'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(", 'DCH'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(", 'IRE'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(", 'IPS'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemMatricula()) {
			sqlStr.append(", 'MAT'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemMensalidade()) {
			sqlStr.append(", 'MEN'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemNegociacao()) {
			sqlStr.append(", 'NCR'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemOutros()) {
			sqlStr.append(", 'OUT'");
		}
		if (filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento()) {
			sqlStr.append(", 'REQ'");
		}
		sqlStr.append(" ) ");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoNotaFiscalSaidaServico(final Integer notaFiscalSaidaServico, final Integer contaReceberRecebimento, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE ContaReceberRecebimento set notaFiscalSaidaServico = ? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					if(Uteis.isAtributoPreenchido(notaFiscalSaidaServico)){
						sqlAlterar.setInt(1, notaFiscalSaidaServico);
					}else{
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, contaReceberRecebimento);
					return sqlAlterar;
				}
			});
			
			final String sql2 = "update contareceber set notafiscalsaidaservico = ? where codigo in(select distinct contareceber from contareceberrecebimento where codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql2);
					if(Uteis.isAtributoPreenchido(notaFiscalSaidaServico)){
						sqlAlterar.setInt(1, notaFiscalSaidaServico);
					}else{
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, contaReceberRecebimento);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public void realizarMontagemDadosNegociacaoRecebimentoDeRecebimentoTerceirizado(List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		for (ContaReceberRecebimentoVO contaReceberRecebimentoVO : contaReceberRecebimentoVOs) {
			if (contaReceberRecebimentoVO.getRecebimentoTerceirizado() && !contaReceberRecebimentoVO.getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
				contaReceberRecebimentoVO.setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().consultarPorChavePrimaria(contaReceberRecebimentoVO.getNegociacaoRecebimentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, configuracaoFinanceiroVO, usuarioVO));
			}

		}
	}
	
	@Override
	public Integer verificarNotaFiscalSaidaServicoEmitidaContaReceberRecebimento(Integer contaReceberRecebimento) throws Exception {
		String sql = "SELECT notafiscalsaidaServico FROM ContaReceberRecebimento WHERE codigo = ? and notafiscalsaidaServico is not null";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { contaReceberRecebimento });
		if (rs.next()) {
			return rs.getInt("notafiscalsaidaServico");
		}
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void removerVinculoNotaFiscalSaidaServicoContaReceberRecebimento(final Integer notaFiscalSaidaServico, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE ContaReceberRecebimento SET notaFiscalSaidaServico = null WHERE ((notaFiscalSaidaServico = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, notaFiscalSaidaServico);
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public String consultarContaReceberRecebimentoTransacaoCartao(Integer codigoFormaPagamentoNegociacaoRecebimento) throws Exception{
		StringBuilder sql = new StringBuilder("select codigo from contareceberrecebimento where formapagamentoNegociacaoRecebimento = "+codigoFormaPagamentoNegociacaoRecebimento);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		StringBuilder result = new StringBuilder("");
		while(rs.next()){
			result.append("[").append(rs.getInt("codigo")).append("]");
		}
		return result.toString();
	}

}
