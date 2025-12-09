package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoDescontoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PlanoDescontoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PlanoDescontoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PlanoDescontoVO
 * @see ControleAcesso
 */

@Repository
@Scope("singleton")
@Lazy
public class PlanoDesconto extends ControleAcesso implements PlanoDescontoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3414060640597645981L;
	protected static String idEntidade;

	public PlanoDesconto() throws Exception {
		super();
		setIdEntidade("PlanoDesconto");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>PlanoDescontoVO</code>.
	 */
	public PlanoDescontoVO novo() throws Exception {
		PlanoDesconto.incluir(getIdEntidade());
		PlanoDescontoVO obj = new PlanoDescontoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>PlanoDescontoVO</code>. Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PlanoDescontoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PlanoDescontoVO.validarDados(obj);
			PlanoDesconto.incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO PlanoDesconto( nome, percDescontoParcela, percDescontoMatricula, requisitos, descricao, "
					+ "somente1PeriodoLetivoParcela, somente1PeriodoLetivoMatricula, tipoDescontoParcela, tipoDescontoMatricula, "
					+ "diasValidadeVencimento, ativo, descontoValidoAteDataVencimento, unidadeEnsino, aplicarSobreValorCheio, "
					+ "utilizarDiaUtil, utilizarDiaFixo, aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto, ordemPrioridadeParaCalculo, categoriaDesconto, bolsaFilantropia, removerDescontoRenovacao, aplicarDescontoApartirParcela, "
					+ "utilizarAvancoDiaUtil ) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setDouble(2, obj.getPercDescontoParcela().doubleValue());
					sqlInserir.setDouble(3, obj.getPercDescontoMatricula().doubleValue());
					sqlInserir.setString(4, obj.getRequisitos());
					sqlInserir.setString(5, obj.getDescricao());
					sqlInserir.setBoolean(6, obj.getSomente1PeriodoLetivoParcela().booleanValue());
					sqlInserir.setBoolean(7, obj.getSomente1PeriodoLetivoMatricula().booleanValue());
					sqlInserir.setString(8, obj.getTipoDescontoParcela());
					sqlInserir.setString(9, obj.getTipoDescontoMatricula());
					sqlInserir.setInt(10, obj.getDiasValidadeVencimento().intValue());
					sqlInserir.setBoolean(11, obj.getAtivo());
					sqlInserir.setBoolean(12, obj.getDescontoValidoAteDataVencimento());
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getUnidadeEnsinoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setBoolean(14, obj.getAplicarSobreValorCheio());
					sqlInserir.setBoolean(15, obj.getUtilizarDiaUtil());
					sqlInserir.setBoolean(16, obj.getUtilizarDiaFixo());

					sqlInserir.setBoolean(17, obj.getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto());
					sqlInserir.setInt(18, obj.getOrdemPrioridadeParaCalculo());
					if (obj.getCategoriaDescontoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(19, obj.getCategoriaDescontoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(19, 0);
					}
					sqlInserir.setBoolean(20, obj.getBolsaFilantropia());
					sqlInserir.setBoolean(21, obj.getRemoverDescontoRenovacao());
					sqlInserir.setInt(22, obj.getAplicarDescontoApartirParcela());
					sqlInserir.setBoolean(23, obj.getUtilizarAvancoDiaUtil());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (DuplicateKeyException e) {
			throw new Exception("O Nome(Plano Desconto) já está cadastrado.");
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>PlanoDescontoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PlanoDescontoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PlanoDescontoVO.validarDados(obj);
			PlanoDesconto.alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE PlanoDesconto set nome=?, percDescontoParcela=?, "
					+ "percDescontoMatricula=?, requisitos=?, descricao=?, somente1PeriodoLetivoParcela=?, "
					+ "somente1PeriodoLetivoMatricula=?, tipoDescontoParcela=?, tipoDescontoMatricula=?, "
					+ "diasValidadeVencimento=?, descontoValidoAteDataVencimento=?, unidadeEnsino=?, "
					+ "aplicarSobreValorCheio=?, utilizarDiaUtil=? , utilizarDiaFixo=?, "
					+ "aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto=?, ordemPrioridadeParaCalculo=?, categoriaDesconto=?, bolsaFilantropia = ?, removerDescontoRenovacao=?, aplicarDescontoApartirParcela = ?, "
					+ "utilizarAvancoDiaUtil=? "
					+ "WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setDouble(2, obj.getPercDescontoParcela().doubleValue());
					sqlAlterar.setDouble(3, obj.getPercDescontoMatricula().doubleValue());
					sqlAlterar.setString(4, obj.getRequisitos());
					sqlAlterar.setString(5, obj.getDescricao());
					sqlAlterar.setBoolean(6, obj.getSomente1PeriodoLetivoParcela().booleanValue());
					sqlAlterar.setBoolean(7, obj.getSomente1PeriodoLetivoMatricula().booleanValue());
					sqlAlterar.setString(8, obj.getTipoDescontoParcela());
					sqlAlterar.setString(9, obj.getTipoDescontoMatricula());
					sqlAlterar.setInt(10, obj.getDiasValidadeVencimento().intValue());
					sqlAlterar.setBoolean(11, obj.getDescontoValidoAteDataVencimento());
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(12, obj.getUnidadeEnsinoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setBoolean(13, obj.getAplicarSobreValorCheio());
					sqlAlterar.setBoolean(14, obj.getUtilizarDiaUtil());
					sqlAlterar.setBoolean(15, obj.getUtilizarDiaFixo());
					sqlAlterar.setBoolean(16, obj.getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto());
					sqlAlterar.setInt(17, obj.getOrdemPrioridadeParaCalculo());
					if (obj.getCategoriaDescontoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(18, obj.getCategoriaDescontoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(18, 0);
					}

					sqlAlterar.setBoolean(19, obj.getBolsaFilantropia());
					sqlAlterar.setBoolean(20, obj.getRemoverDescontoRenovacao());
					sqlAlterar.setInt(21, obj.getAplicarDescontoApartirParcela());
					sqlAlterar.setBoolean(22, obj.getUtilizarAvancoDiaUtil());
					sqlAlterar.setInt(23, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosDescritivos(final PlanoDescontoVO obj) throws Exception {
		try {
			PlanoDescontoVO.validarDados(obj);
			PlanoDesconto.alterar(getIdEntidade());
			final String sql = "UPDATE PlanoDesconto set nome=?, requisitos=?, descricao=?, unidadeEnsino=?, categoriaDesconto=?, bolsaFilantropia = ? , removerDescontoRenovacao = ?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getRequisitos());
					sqlAlterar.setString(3, obj.getDescricao());
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getUnidadeEnsinoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getCategoriaDescontoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getCategoriaDescontoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setBoolean(6, obj.getBolsaFilantropia());
					sqlAlterar.setBoolean(7, obj.getRemoverDescontoRenovacao());
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>PlanoDescontoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PlanoDescontoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PlanoDesconto.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM PlanoDesconto WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>PlanoDesconto</code> através do valor do atributo <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PlanoDescontoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	// public List<PlanoDescontoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
	// return consultarPorNome(valorConsulta, 0, controlarAcesso, usuario, limite, offset);
	// }

	public List<PlanoDescontoVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PlanoDesconto WHERE lower(nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " and (unidadeensino = " + unidadeEnsino.intValue() + " or unidadeEnsino is null) ";
		}
		sqlStr += " ORDER BY nome ";
		if (limite != null && limite > 0) {
			sqlStr += " limit " + limite + " offset " + offset;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public Integer consultarTotalRegistroPorNome(String valorConsulta, Integer unidadeEnsino) throws Exception {
		String sqlStr = "SELECT count(codigo) as qtde FROM PlanoDesconto WHERE lower(nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " and (unidadeensino = " + unidadeEnsino.intValue() + " or unidadeEnsino is null) ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public List<PlanoDescontoVO> consultarPlanoDescontoAtivoNivelComboBox(UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		String sqlStr = "SELECT codigo, nome, ativo FROM PlanoDesconto WHERE (ativo = true) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<PlanoDescontoVO> vetResultado = new ArrayList<PlanoDescontoVO>(0);
		while (tabelaResultado.next()) {
			PlanoDescontoVO obj = new PlanoDescontoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setAtivo(tabelaResultado.getBoolean("ativo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	// Método não poderá ser alterado para não ocorrer pioras na performance.
	public List<PlanoDescontoVO> consultarPlanoDescontoNivelComboBox(UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		String sqlStr = "SELECT codigo, nome, ativo FROM PlanoDesconto where ativo = 't' ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<PlanoDescontoVO> vetResultado = new ArrayList<PlanoDescontoVO>(0);
		while (tabelaResultado.next()) {
			PlanoDescontoVO obj = new PlanoDescontoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setAtivo(tabelaResultado.getBoolean("ativo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por realizar uma consulta de <code>PlanoDesconto</code> através do valor do atributo <code>String nome</code> e do campo ativo que seja true. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PlanoDescontoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PlanoDescontoVO> consultarPorNomeSomenteAtiva(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PlanoDesconto WHERE nome like('" + valorConsulta + "%') AND ativo = true ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List<PlanoDescontoVO> consultarPlanoDescontoDisponivelCondicaoPagamentoPlanoFinanceiroCurso(Integer codCondicaoPagamento, UsuarioVO usuario) throws Exception {
		String sqlStr = "select PlanoDesconto.* from  planoDescontoDisponivelMatricula " + " inner join planoDesconto on planoDesconto.codigo = planoDescontoDisponivelMatricula.planoDesconto " + " where condicaoPagamentoPlanoFinanceiroCurso = " + codCondicaoPagamento + " ORDER BY PlanoDesconto.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>PlanoDesconto</code> através do valor do atributo <code>Boolean ativo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PlanoDescontoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PlanoDescontoVO> consultarPorAtivoOuInativo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		boolean ativo = false;
		if (valorConsulta.equals("ativo")) {
			ativo = true;
		}
		String sqlStr = "SELECT * FROM PlanoDesconto WHERE ativo = " + ativo + " ";
		if (unidadeEnsino > 0) {
			sqlStr += " and (unidadeensino = " + unidadeEnsino.intValue() + " or unidadeEnsino is null) ";
		}
		sqlStr += " ORDER BY nome";
		if (limite != null && limite > 0) {
			sqlStr += " limit " + limite + " offset " + offset;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public Integer consultarTotalRegistroPorAtivoOuInativo(String valorConsulta, Integer unidadeEnsino) throws Exception {

		boolean ativo = false;
		if (valorConsulta.equals("ativo")) {
			ativo = true;
		}
		String sqlStr = "SELECT count(codigo) as qtde FROM PlanoDesconto WHERE ativo = " + ativo + " ";
		if (unidadeEnsino > 0) {
			sqlStr += " and (unidadeensino = " + unidadeEnsino.intValue() + " or unidadeEnsino is null) ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public List<PlanoDescontoVO> consultarPorPlanoFinanceiroAluno(Integer planoFinanceiroAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct planoDesconto.* from planoDesconto ");
		sb.append(" inner join itemPlanoFinanceiroAluno on itemPlanoFinanceiroaluno.planoDesconto = planoDesconto.codigo ");
		if (planoFinanceiroAluno != null && planoFinanceiroAluno > 0) {
			sb.append(" inner join planoFinanceiroAluno on planoFinanceiroAluno.codigo = itemPlanoFinanceiroaluno.planoFinanceiroAluno ");
			sb.append(" where planoFinanceiroAluno.codigo = ");
			sb.append(planoFinanceiroAluno);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List<PlanoDescontoVO> consultarPorContaReceber(Integer contaReceber, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct planoDesconto.* from planoDesconto ");
		sb.append(" inner join planoDescontoContaReceber on planoDescontoContaReceber.planoDesconto = planoDesconto.codigo ");
		sb.append(" where planoDescontoContaReceber.contaReceber = ");
		sb.append(contaReceber);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>PlanoDesconto</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PlanoDescontoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PlanoDescontoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PlanoDesconto WHERE codigo = " + valorConsulta.intValue() + " ";
		if (unidadeEnsino > 0) {
			sqlStr += " and (unidadeensino = " + unidadeEnsino.intValue() + " or unidadeEnsino is null) ";
		}
		sqlStr += " ORDER BY codigo";
		if (limite != null && limite > 0) {
			sqlStr += " limit " + limite + " offset " + offset;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public Integer consultarTotalRegistroPorCodigo(Integer valorConsulta, Integer unidadeEnsino) throws Exception {
		String sqlStr = "SELECT count(codigo) as qtde FROM PlanoDesconto WHERE codigo = " + valorConsulta.intValue() + " ";
		if (unidadeEnsino > 0) {
			sqlStr += " and (unidadeensino = " + unidadeEnsino.intValue() + " or unidadeEnsino is null) ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>PlanoDescontoVO</code> resultantes da consulta.
	 */
	public static List<PlanoDescontoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<PlanoDescontoVO> vetResultado = new ArrayList<PlanoDescontoVO>(0);
		while (tabelaResultado.next()) {
			PlanoDescontoVO obj = new PlanoDescontoVO();
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>PlanoDescontoVO</code>.
	 * 
	 * @return O objeto da classe <code>PlanoDescontoVO</code> com os dados devidamente montados.
	 */
	public static PlanoDescontoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PlanoDescontoVO obj = new PlanoDescontoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNovoObj(Boolean.FALSE);
		obj.setNome(dadosSQL.getString("nome"));
		obj.setPercDescontoParcela(new Double(dadosSQL.getDouble("percDescontoParcela")));
		obj.setPercDescontoMatricula(new Double(dadosSQL.getDouble("percDescontoMatricula")));
		obj.setRequisitos(dadosSQL.getString("requisitos"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSomente1PeriodoLetivoParcela(dadosSQL.getBoolean("somente1PeriodoLetivoParcela"));
		obj.setSomente1PeriodoLetivoMatricula(dadosSQL.getBoolean("somente1PeriodoLetivoMatricula"));
		obj.setTipoDescontoParcela(dadosSQL.getString("tipoDescontoParcela"));
		obj.setTipoDescontoMatricula(dadosSQL.getString("tipoDescontoMatricula"));
		obj.setDiasValidadeVencimento(new Integer(dadosSQL.getInt("diasValidadeVencimento")));
		obj.setAtivo(dadosSQL.getBoolean("ativo"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getCategoriaDescontoVO().setCodigo(dadosSQL.getInt("categoriaDesconto"));
		obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
		obj.setDataInativacao(dadosSQL.getDate("dataInativacao"));
		obj.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("responsavelAtivacao"));
		obj.getResponsavelInativacao().setCodigo(dadosSQL.getInt("responsavelInativacao"));
		obj.setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("descontoValidoAteDataVencimento"));
		obj.setAplicarSobreValorCheio(dadosSQL.getBoolean("aplicarSobreValorCheio"));
		obj.setUtilizarDiaFixo(dadosSQL.getBoolean("utilizarDiaFixo"));
		obj.setUtilizarDiaUtil(dadosSQL.getBoolean("utilizarDiaUtil"));
		obj.setUtilizarAvancoDiaUtil(dadosSQL.getBoolean("utilizarAvancoDiaUtil"));
		obj.setRemoverDescontoRenovacao(dadosSQL.getBoolean("removerDescontoRenovacao"));
		obj.setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(dadosSQL.getBoolean("aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto"));
		obj.setOrdemPrioridadeParaCalculo(dadosSQL.getInt("ordemPrioridadeParaCalculo"));
		obj.setAplicarDescontoApartirParcela(dadosSQL.getInt("aplicarDescontoApartirParcela"));
		obj.setBolsaFilantropia(dadosSQL.getBoolean("bolsaFilantropia"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		// criar montar dados para obj.setCategoriaDescontoVO();
		montarResponsavelAtivacao(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarResponsavelInativacao(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosCategoriaDesconto(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	public static void montarDadosCategoriaDesconto(PlanoDescontoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDescontoVO().getCodigo().intValue() == 0) {
			obj.setCategoriaDescontoVO(new CategoriaDescontoVO());
			return;
		}
		obj.setCategoriaDescontoVO(getFacadeFactory().getCategoriaDescontoFacade().consultarPorChavePrimaria(obj.getCategoriaDescontoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(PlanoDescontoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarResponsavelAtivacao(PlanoDescontoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!obj.getResponsavelAtivacao().getCodigo().equals(0)) {
			obj.setResponsavelAtivacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAtivacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
	}

	public static void montarResponsavelInativacao(PlanoDescontoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!obj.getResponsavelInativacao().getCodigo().equals(0)) {
			obj.setResponsavelInativacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelInativacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>PlanoDescontoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public PlanoDescontoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		String sqlStr = "SELECT * FROM PlanoDesconto WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Plano Desconto).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return PlanoDesconto.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		PlanoDesconto.idEntidade = idEntidade;
	}

	public String realizarAtivacaoInativacao(PlanoDescontoVO planoDescontoVO, UsuarioVO usuario) throws Exception {
		if (planoDescontoVO.getAtivo()) { // se ativo, então estou inativando
			planoDescontoVO.setAtivo(false);
			planoDescontoVO.setDataInativacao(new Date());
			planoDescontoVO.setResponsavelInativacao(usuario);
			realizarAlterarPlanoDescontoParaInativo(planoDescontoVO);
			return "msg_dados_inativado";
		} else { // se inativado, entao estou ativando
			planoDescontoVO.setAtivo(true);
			planoDescontoVO.setDataAtivacao(new Date());
			planoDescontoVO.setResponsavelAtivacao(usuario);
			realizarAlterarPlanoDescontoParaAtivo(planoDescontoVO);
			return "msg_dados_ativado";
		}
	}

	/**
	 * Método responsável por ATIVAR um PlanoDescontoVO. Altera campos ativo, dataAtivacao e responsavelAtivacao
	 * 
	 * @param obj
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlterarPlanoDescontoParaAtivo(final PlanoDescontoVO obj) throws Exception {
		// PlanoDesconto.alterar(getIdEntidade()); VERIFICAR SE É NECESSÁRIO
		// VERIFICAR PERMISSÃO
		final String sql = "UPDATE PlanoDesconto set ativo=?, dataAtivacao=?, responsavelAtivacao=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.getAtivo());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataAtivacao()));
				sqlAlterar.setInt(3, obj.getResponsavelAtivacao().getCodigo());
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Método responsável por INATIVAR um PlanoDescontoVO. Altera campos ativo, dataInativacao e responsavelInativacao
	 * 
	 * @param obj
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlterarPlanoDescontoParaInativo(final PlanoDescontoVO obj) throws Exception {
		// PlanoDesconto.alterar(getIdEntidade()); VERIFICAR SE É NECESSÁRIO
		// VERIFICAR PERMISSÃO
		final String sql = "UPDATE PlanoDesconto set ativo=?, dataInativacao=?, responsavelInativacao=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.getAtivo());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataInativacao()));
				sqlAlterar.setInt(3, obj.getResponsavelInativacao().getCodigo());
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Override
	public void realizarMarcacaoDiaUtil(PlanoDescontoVO planoDescontoVO) {
		planoDescontoVO.setUtilizarDiaFixo(false);
		planoDescontoVO.setUtilizarAvancoDiaUtil(false);
		planoDescontoVO.setDescontoValidoAteDataVencimento(false);
	}

	@Override
	public void realizarMarcacaoDiaFixo(PlanoDescontoVO planoDescontoVO) {
		planoDescontoVO.setUtilizarDiaUtil(false);
		planoDescontoVO.setDescontoValidoAteDataVencimento(false);
	}

	@Override
	public void realizarMarcacaoDescontoValidoAteDataVencimento(PlanoDescontoVO planoDescontoVO) {
		planoDescontoVO.setUtilizarDiaUtil(false);
		planoDescontoVO.setUtilizarDiaFixo(false);
		planoDescontoVO.setUtilizarAvancoDiaUtil(false);
	}

	@Override
	public List<PlanoDescontoVO> consultarPorCategoriaDesconto(Integer categoriaDesconto, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct planoDesconto.* from planoDesconto ");
		sb.append("WHERE categoriaDesconto = ").append(categoriaDesconto);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public List<PlanoDescontoVO> consultarPlanoDescontoFiltrarRenovacaoTurmaNivelCombobox(Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct PlanoDesconto.codigo, PlanoDesconto.descricao from PlanoDesconto ");
		sql.append(" inner join itemplanofinanceiroaluno on itemplanofinanceiroaluno.PlanoDesconto = PlanoDesconto.codigo ");
		sql.append(" inner join planofinanceiroaluno on planofinanceiroaluno.codigo = itemplanofinanceiroaluno.planofinanceiroaluno ");
		sql.append(" inner join matriculaperiodo on planofinanceiroaluno.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sql.append(" where matriculaperiodo.turma = ").append(turma);
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
			if (semestre != null && !semestre.trim().isEmpty()) {
				sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
		}
		if (gradeCurricular != null && gradeCurricular > 0) {
			sql.append(" and matricula.gradeCurricularAtual = ").append(gradeCurricular);
		}
		sql.append(" order by PlanoDesconto.descricao ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
		PlanoDescontoVO obj = null;
		while (rs.next()) {
			obj = new PlanoDescontoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDescricao(rs.getString("descricao"));
			planoDescontoVOs.add(obj);
		}
		return planoDescontoVOs;

	}

	@Override
	public List<PlanoDescontoVO> consultarPlanoDescontoAtivoPorUnidadeEnsinoNivelComboBox(UnidadeEnsinoVO unidadeEnsinoVO, Boolean ativo, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT planodesconto.codigo, planodesconto.nome FROM planodesconto WHERE 1=1 ");
		if (ativo) {
			sqlStr.append(" AND planodesconto.ativo ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
			sqlStr.append(" AND (planodesconto.unidadeensino = ").append(unidadeEnsinoVO.getCodigo().intValue());
			sqlStr.append(" OR planodesconto.unidadeensino IS NULL);");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PlanoDescontoVO> vetResultado = new ArrayList<PlanoDescontoVO>(0);
		while (tabelaResultado.next()) {
			PlanoDescontoVO obj = new PlanoDescontoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<PlanoDescontoVO> consultarPorNomeSomenteAtivaConsiderandoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append( "SELECT * FROM PlanoDesconto WHERE lower (sem_acentos(nome)) like(sem_acentos('").append(valorConsulta).append("%'))");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND (PlanoDesconto.unidadeensino = ").append(unidadeEnsino);
			sqlStr.append(" OR PlanoDesconto.unidadeensino IS NULL)");
		}
		sqlStr.append(" AND ativo = true ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

}
