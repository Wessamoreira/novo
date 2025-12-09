package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ChequeInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ChequeVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ChequeVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ChequeVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Cheque extends ControleAcesso implements ChequeInterfaceFacade {

	protected static String idEntidade;

	public Cheque() throws Exception {
		super();
		setIdEntidade("Cheque");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ChequeVO</code>.
	 */
	public ChequeVO novo() throws Exception {
		Cheque.incluir(getIdEntidade());
		ChequeVO obj = new ChequeVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ChequeVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ChequeVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final ChequeVO obj, UsuarioVO usuario) throws Exception {
		ChequeVO.validarDados(obj);
		// Cheque.incluir(getIdEntidade());
		obj.realizarUpperCaseDados();
		// obj.validarUnicidade(consultarTodos(false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario), obj);
		final String sql = "INSERT INTO Cheque( pessoa, sacado, contacorrente, numero, valor, dataemissao, dataPrevisao, pago, chequeProprio, situacao, recebimento, pagamento, localizacaocheque, banco, agencia, numerocontacorrente, valorusadorecebimento, unidadeEnsino, parceiro, fornecedor, cpf, cnpj, emitentePessoaJuridica) VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getPessoa().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getPessoa().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				sqlInserir.setString(2, obj.getSacado());
				if (obj.getContaCorrente().getCodigo().intValue() != 0) {
					sqlInserir.setInt(3, obj.getContaCorrente().getCodigo().intValue());
				} else {
					sqlInserir.setNull(3, 0);
				}
				sqlInserir.setString(4, obj.getNumero());
				sqlInserir.setDouble(5, obj.getValor().doubleValue());
				sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataEmissao()));
				sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataPrevisao()));
				sqlInserir.setBoolean(8, obj.getPago().booleanValue());
				sqlInserir.setBoolean(9, obj.getChequeProprio().booleanValue());
				sqlInserir.setString(10, obj.getSituacao());
				sqlInserir.setInt(11, obj.getRecebimento().intValue());
				sqlInserir.setInt(12, obj.getPagamento().intValue());
				if (obj.getLocalizacaoCheque().getCodigo().intValue() != 0) {
					sqlInserir.setInt(13, obj.getLocalizacaoCheque().getCodigo().intValue());
				} else {
					sqlInserir.setNull(13, 0);
				}
				sqlInserir.setString(14, obj.getBanco());
				sqlInserir.setString(15, obj.getAgencia());
				sqlInserir.setString(16, obj.getNumeroContaCorrente());
				sqlInserir.setDouble(17, obj.getValorUsadoRecebimento().doubleValue());
				if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
					sqlInserir.setInt(18, obj.getUnidadeEnsino().getCodigo().intValue());
				} else {
					sqlInserir.setNull(18, 0);
				}
				if (obj.getParceiro().getCodigo().intValue() != 0) {
					sqlInserir.setInt(19, obj.getParceiro().getCodigo().intValue());
				} else {
					sqlInserir.setNull(19, 0);
				}
				if (obj.getFornecedor().getCodigo().intValue() != 0) {
					sqlInserir.setInt(20, obj.getFornecedor().getCodigo().intValue());
				} else {
					sqlInserir.setNull(20, 0);
				}
				sqlInserir.setString(21, obj.getCpf());
				sqlInserir.setString(22, obj.getCnpj());
				sqlInserir.setBoolean(23, obj.getEmitentePessoaJuridica());
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));

		obj.setNovoObj(Boolean.FALSE);

	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ChequeVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ChequeVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterar(final ChequeVO obj) throws Exception {
		ChequeVO.validarDados(obj);
		// Cheque.alterar(getIdEntidade());
		obj.realizarUpperCaseDados();
		final String sql = "UPDATE Cheque set pessoa=?, sacado=?, contacorrente=?, numero=?, valor=?, dataemissao=?, dataPrevisao=?, pago=?, chequeProprio=?, situacao=?, recebimento=?, pagamento=?, localizacaocheque=?, banco=?, agencia=?, numerocontacorrente=?, valorusadorecebimento=?, unidadeEnsino=?, parceiro=?, fornecedor = ?, cpf = ?, cnpj=?, emitentePessoaJuridica=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getPessoa().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getPessoa().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setString(2, obj.getSacado());
				if (obj.getContaCorrente().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(3, obj.getContaCorrente().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setString(4, obj.getNumero());
				sqlAlterar.setDouble(5, obj.getValor().doubleValue());
				sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataEmissao()));
				sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataPrevisao()));
				sqlAlterar.setBoolean(8, obj.getPago().booleanValue());
				sqlAlterar.setBoolean(9, obj.getChequeProprio().booleanValue());
				sqlAlterar.setString(10, obj.getSituacao());
				sqlAlterar.setInt(11, obj.getRecebimento().intValue());
				sqlAlterar.setInt(12, obj.getPagamento().intValue());
				if (obj.getLocalizacaoCheque().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(13, obj.getLocalizacaoCheque().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(13, 0);
				}
				sqlAlterar.setString(14, obj.getBanco());
				sqlAlterar.setString(15, obj.getAgencia());
				sqlAlterar.setString(16, obj.getNumeroContaCorrente());
				sqlAlterar.setDouble(17, obj.getValorUsadoRecebimento().doubleValue());
				if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(18, obj.getUnidadeEnsino().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(18, 0);
				}
				if (obj.getParceiro().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(19, obj.getParceiro().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(19, 0);
				}
				if (obj.getFornecedor().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(20, obj.getFornecedor().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(20, 0);
				}
				sqlAlterar.setString(21, obj.getCpf());
				sqlAlterar.setString(22, obj.getCnpj());
				sqlAlterar.setBoolean(23, obj.getEmitentePessoaJuridica());
				sqlAlterar.setInt(24, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarChequeBaixa(final ChequeVO obj) throws Exception {
		final String sql = "UPDATE Cheque set responsavelBaixa=?, dataBaixa=?, dataAntecipacao=?, valorDescontoAntecipacao=?, tarifaAntecipacao=?, situacao=?, pago=?, localizacaocheque=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getResponsavelBaixa().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getResponsavelBaixa().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataBaixa()));
				sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataAntecipacao()));
				sqlAlterar.setDouble(4, new Double(obj.getValorDescontoAntecipacao()));
				sqlAlterar.setDouble(5, new Double(obj.getTarifaAntecipacao()));
				sqlAlterar.setString(6, obj.getSituacao());
				sqlAlterar.setBoolean(7, obj.getPago());
				if (obj.getLocalizacaoCheque().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(8, obj.getLocalizacaoCheque().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(8, 0);
				}
				sqlAlterar.setInt(9, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarChequeReapresentacao(final ChequeVO obj) throws Exception {
		final String sql = "UPDATE Cheque set situacao=?, localizacaocheque=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getSituacao());
				if (obj.getLocalizacaoCheque().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getLocalizacaoCheque().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setInt(3, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void desvincularChequePagamento(final Integer codigo, final String situacaoCheque, final Integer localizacaoCheque) throws Exception {
		final String sql = "UPDATE Cheque set pagamento = ?, situacao = ?, localizacaocheque=? WHERE ((pagamento = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setNull(1, 0);
				sqlAlterar.setString(2, situacaoCheque.toString());
				if (localizacaoCheque.intValue() != 0) {
					sqlAlterar.setInt(3, localizacaoCheque.intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setInt(4, codigo);
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void desvincularChequeRecebimento(final Integer codigo, final String situacaoCheque, final Integer localizacaoCheque) throws Exception {
		final String sql = "UPDATE Cheque set recebimento = ?, situacao=?, localizacaocheque WHERE ((recebimento= ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setNull(1, 0);
				sqlAlterar.setString(2, situacaoCheque.toString());
				if (localizacaoCheque.intValue() != 0) {
					sqlAlterar.setInt(3, localizacaoCheque.intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setInt(4, codigo);
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ChequeVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ChequeVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ChequeVO obj) throws Exception {
		// Cheque.excluir(getIdEntidade());
		try {
			String sql = "DELETE FROM Cheque WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>Date dataPrevisao</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataPrevisao(Date prmIni, Date prmFim, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT cheque.* " + "FROM Cheque " + "left join unidadeensino on Cheque.unidadeEnsino = UnidadeEnsino.codigo " + "WHERE (dataPrevisao between '" + Uteis.getDataJDBC(prmIni) + "' and '" + Uteis.getDataJDBC(prmFim) + "')";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and localizacaocheque = " + localizacao.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ChequeVO> consultarPorDataPrevisaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorDataPrevisaoSituacao(prmIni, prmFim, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorDataPrevisaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE (Cheque.dataPrevisao >= '").append(Uteis.getDataJDBC(prmIni)).append("' and  Cheque.dataPrevisao <= '").append(Uteis.getDataJDBC(prmFim)).append("') ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorDataPrevisaoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(Date prmIni, Date prmFim, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE (Cheque.dataPrevisao between '").append(Uteis.getDataJDBC(prmIni)).append("' and '").append(Uteis.getDataJDBC(prmFim)).append("') ");
		sqlStr.append("and upper (Cheque.situacao) = '").append(situacao.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append("and Cheque.localizacaocheque = ").append(localizacao.intValue()).append(" ");
		}
		sqlStr.append("and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE' ) and mfi.cheque is not null ) ");	
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>Date dataEmissao</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataEmissao(Date prmIni, Date prmFim, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE ((Cheque.dataemissao >= '" + Uteis.getDataJDBC(prmIni) + "') and (Cheque.dataemissao <= '" + Uteis.getDataJDBC(prmFim) + "')) " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ChequeVO> consultarPorDataEmissaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorDataEmissaoSituacao(prmIni, prmFim, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorDataEmissaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE ((Cheque.dataemissao between '").append(Uteis.getDataJDBC(prmIni)).append("' and '").append(Uteis.getDataJDBC(prmFim) + "')) ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorDataEmissaoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(Date prmIni, Date prmFim, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE ((Cheque.dataemissao between '").append(Uteis.getDataJDBC(prmIni)).append("' and '").append(Uteis.getDataJDBC(prmFim) + "')) ");
		sqlStr.append("and upper (Cheque.situacao) = '").append(situacao.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append("and Cheque.localizacaocheque = ").append(localizacao.intValue()).append(" ");
		}
		sqlStr.append("and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE' ) and mfi.cheque is not null ) ");
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>String numero</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNumero(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE upper( Cheque.numero ) like('" + valorConsulta.toUpperCase() + "%') " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorLocalizacaoCheque(Integer localizacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorLocalizacaoSituacaoCheque(Integer localizacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.situacao = '" + situacao + "'";
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public ChequeVO consultarPorNumeroUnico(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE Cheque.numero = '" + valorConsulta.toUpperCase() + "' " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return new ChequeVO();
	}

	public List<ChequeVO> consultarPorNumeroSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNumeroSituacao(valorConsulta, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNumeroSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM  Cheque ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE upper( Cheque.numero ) like upper(?) ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNumeroSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM  Cheque ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE upper( Cheque.numero ) like('").append(valorConsulta.toUpperCase()).append("%') ");
		sqlStr.append("and upper (Cheque.situacao) = '").append(situacao.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append("and Cheque.localizacaocheque = ").append(localizacao.intValue()).append(" ");
		}
		sqlStr.append("and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE' ) and mfi.cheque is not null ) ");
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoPagamento(Integer pagamento, Integer localizacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cheque WHERE pagamento = " + pagamento.intValue() + " ";
		if (localizacao.intValue() != 0) {
			sqlStr += " and localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoRecebimento(Integer rtRLog, Integer localizacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cheque WHERE recebimento = " + rtRLog.intValue() + " ";
		if (localizacao.intValue() != 0) {
			sqlStr += " and localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>numeroAgencia</code> da classe
	 * <code>Agencia</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNumeroAgenciaAgencia(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct Cheque.* FROM Cheque, Agencia, UnidadeEnsino, ContaCorrente WHERE Cheque.contacorrente = ContaCorrente.codigo and ContaCorrente.agencia = agencia.codigo and (upper( agencia.numeroAgencia ) like('" + valorConsulta.toUpperCase() + "%')) or (upper (Cheque.agencia)  like('" + valorConsulta.toUpperCase() + "%'))" + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNumeroAgenciaAgenciaSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNumeroAgenciaAgenciaSituacao(valorConsulta, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNumeroAgenciaAgenciaSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM cheque ");
		sqlStr.append("left join contacorrente on Cheque.contacorrente = ContaCorrente.codigo ");
		sqlStr.append("left join Agencia on ContaCorrente.agencia = agencia.codigo ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE (upper( agencia.numeroAgencia ) like upper(?) ");
		sqlStr.append("or upper (Cheque.agencia) like upper(?)) ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT, valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNumeroAgenciaAgenciaSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM cheque ");
		sqlStr.append("left join contacorrente on Cheque.contacorrente = ContaCorrente.codigo ");
		sqlStr.append("left join Agencia on ContaCorrente.agencia = agencia.codigo ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE (upper( agencia.numeroAgencia ) like('").append(valorConsulta.toUpperCase()).append("%') ");
		sqlStr.append("or upper (Cheque.agencia) like('").append(valorConsulta.toUpperCase()).append("%')) ");
		sqlStr.append("and upper (Cheque.situacao) = '").append(situacao.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append("and Cheque.localizacaocheque = ").append(localizacao.intValue()).append(" ");
		}
		sqlStr.append("and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE' ) and mfi.cheque is not null ) ");
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>nome</code> da classe <code>Banco</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeBanco(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct Cheque.* FROM Cheque, Agencia, Banco, UnidadeEnsino, ContaCorrente WHERE Cheque.contacorrente = ContaCorrente.codigo " + " and ContaCorrente.agencia = agencia.codigo and agencia.banco=Banco.codigo " + " and ((upper( Banco.nome ) like('%" + valorConsulta.toUpperCase() + "%')) or (upper( Cheque.banco ) like('%" + valorConsulta.toUpperCase() + "%'))) " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNomeBancoSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorNomeBancoSituacao(valorConsulta, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNomeBancoSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque ");
		sqlStr.append("left join contacorrente on Cheque.contacorrente = ContaCorrente.codigo ");
		sqlStr.append("left join agencia on ContaCorrente.agencia = agencia.codigo ");
		sqlStr.append("left join banco on agencia.banco=Banco.codigo ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE (upper( Banco.nome ) like upper(?) ");
		sqlStr.append("or upper( Cheque.banco ) like upper(?)) ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT, valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeBancoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque ");
		sqlStr.append("left join contacorrente on Cheque.contacorrente = ContaCorrente.codigo ");
		sqlStr.append("left join agencia on ContaCorrente.agencia = agencia.codigo ");
		sqlStr.append("left join banco on agencia.banco=Banco.codigo ");
		sqlStr.append("left join UnidadeEnsino on Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE (upper( Banco.nome ) like('").append(valorConsulta.toUpperCase()).append("%') ");
		sqlStr.append("or upper( Cheque.banco ) like('").append(valorConsulta.toUpperCase()).append("%')) ");
		sqlStr.append("and upper (Cheque.situacao) = '").append(situacao.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append("and Cheque.localizacaocheque = ").append(localizacao.intValue()).append(" ");
		}
		sqlStr.append("and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE' ) and mfi.cheque is not null ) ");
		sqlStr.append("ORDER BY Cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>String sacado</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSacado(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE upper( sacado ) like('" + valorConsulta.toUpperCase() + "%') " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ChequeVO> consultarPorSacadoSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorSacadoSituacao(valorConsulta, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorSacadoSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE upper(sacado) like upper(?) and Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		sqlStr.append(" ORDER BY Cheque.dataPrevisao");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorSacadoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Cheque.* FROM Cheque, UnidadeEnsino ");
		sqlStr.append("WHERE upper( sacado ) like('").append(valorConsulta.toUpperCase()).append("%') and upper (situacao) = '").append(situacao.toUpperCase()).append("' ");
		sqlStr.append("and Cheque.unidadeEnsino = UnidadeEnsino.codigo ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append("and Cheque.localizacaocheque = ").append(localizacao.intValue()).append(" ");
		}
		sqlStr.append(" and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE' ) and mfi.cheque is not null ) ");
		sqlStr.append("ORDER BY cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>nome</code> da classe <code>Cliente</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT cheque.* FROM Cheque left join Pessoa on Pessoa.codigo = Cheque.pessoa ");
		sqlStr.append(" left join Parceiro on Parceiro.codigo = Cheque.parceiro");
		sqlStr.append(" left join Fornecedor on Fornecedor.codigo = Cheque.fornecedor");
		sqlStr.append(" WHERE ");
		sqlStr.append(" (sem_acentos(upper(Pessoa.nome)) like (sem_acentos(upper('" + valorConsulta.toUpperCase() + "%'))) OR ");
		sqlStr.append(" sem_acentos(upper(Parceiro.nome)) like (sem_acentos(upper('" + valorConsulta.toUpperCase() + "%'))) OR ");
		sqlStr.append(" sem_acentos(upper(Fornecedor.nome)) like (sem_acentos(upper('" + valorConsulta.toUpperCase() + "%'))) ) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Cheque.UnidadeEnsino = " + unidadeEnsino.intValue());
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append(" and cheque.localizacaocheque = " + localizacao.intValue());
		}
		sqlStr.append(" ORDER BY cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNomePessoaSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorNomePessoaSituacao(valorConsulta, situacoes, montarListaCaixas(localizacao), unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<ChequeVO> consultarPorNomePessoaSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		StringBuilder sqlStr = new StringBuilder("SELECT cheque.* FROM Cheque left join Pessoa on Pessoa.codigo = Cheque.pessoa ");
		sqlStr.append(" left join Parceiro on Parceiro.codigo = Cheque.parceiro");
		sqlStr.append(" left join Fornecedor on Fornecedor.codigo = Cheque.fornecedor");
		sqlStr.append(" left join unidadeensino on unidadeensino.codigo = cheque.unidadeensino");
		sqlStr.append(" WHERE ");
		sqlStr.append(" (sem_acentos(upper(Pessoa.nome)) like (sem_acentos(upper(?))) OR ");
		sqlStr.append(" sem_acentos(upper(Parceiro.nome)) like (sem_acentos(upper(?))) OR ");
		sqlStr.append(" sem_acentos(upper(Fornecedor.nome)) like (sem_acentos(upper(?))) ) ");
		montarFiltrosSituacaoCaixasUnidadeEnsino(situacoes, listaCaixas, unidadeEnsino, sqlStr);
		sqlStr.append(" ORDER BY cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT, valorConsulta + PERCENT, valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomePessoaSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT cheque.* FROM Cheque left join Pessoa on Pessoa.codigo = Cheque.pessoa ");
		sqlStr.append(" left join Parceiro on Parceiro.codigo = Cheque.parceiro");
		sqlStr.append(" left join Fornecedor on Fornecedor.codigo = Cheque.fornecedor");
		sqlStr.append(" left join unidadeensino on unidadeensino.codigo = cheque.unidadeensino");
		sqlStr.append(" WHERE ");
		sqlStr.append(" (sem_acentos(upper(Pessoa.nome)) like (sem_acentos(upper('" + valorConsulta.toUpperCase() + "%'))) OR ");
		sqlStr.append(" sem_acentos(upper(Parceiro.nome)) like (sem_acentos(upper('" + valorConsulta.toUpperCase() + "%'))) OR ");
		sqlStr.append(" sem_acentos(upper(Fornecedor.nome)) like (sem_acentos(upper('" + valorConsulta.toUpperCase() + "%'))) ) ");
		sqlStr.append(" and upper (cheque.situacao) = '").append(situacao.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue());
		}
		if (localizacao.intValue() != 0) {
			sqlStr.append(" and cheque.localizacaocheque = ").append(localizacao.intValue());
		}
		sqlStr.append(" and cheque.codigo not in( ");
		sqlStr.append("select distinct mfi.cheque from movimentacaofinanceiraitem mfi ");
		sqlStr.append("inner join movimentacaofinanceira mf on mf.codigo = mfi.movimentacaofinanceira ");
		// sqlStr.append("where (mf.situacao = 'PE' or mf.situacao = 'FI') and mfi.cheque is not null ) ");
		sqlStr.append("where (mf.situacao = 'PE') and mfi.cheque is not null ) ");
		sqlStr.append("ORDER BY cheque.dataPrevisao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaTelaConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoPessoa(Integer valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE pessoa = " + valorConsulta + " " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cheque</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE Cheque.codigo >= " + valorConsulta.intValue() + " " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (localizacao.intValue() != 0) {
			sqlStr += " and Cheque.localizacaocheque = " + localizacao.intValue();
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoSituacao(Integer valorConsulta, String situacao, List listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cheque.* FROM Cheque, UnidadeEnsino WHERE Cheque.codigo >= " + valorConsulta.intValue() + " and upper (Cheque.situacao) = '" + situacao.toUpperCase() + "' " + " and Cheque.unidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		if (listaCaixas != null) {
			if (!listaCaixas.isEmpty()) {
				Iterator i = listaCaixas.iterator();
				while (i.hasNext()) {
					ContaCorrenteVO localizacao = (ContaCorrenteVO) i.next();
					sqlStr += " and Cheque.localizacaocheque = " + localizacao.getCodigo();
				}
			}
		}
		sqlStr += " ORDER BY Cheque.dataPrevisao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoSituacao(Integer valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List lista = new ArrayList();
		ContaCorrenteVO c = new ContaCorrenteVO();
		c.setCodigo(localizacao);
		lista.add(c);
		return this.consultarPorCodigoSituacao(valorConsulta, situacao, lista, unidadeEnsino, controlarAcesso, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de todos <code>Cheque</code> . Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarTodos(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cheque ORDER BY numero";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ChequeVO</code>
	 *         resultantes da consulta.
	 */
	public static List<ChequeVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ChequeVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static List<ChequeVO> montarDadosConsultaTelaConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ChequeVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosTelaConsulta(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ChequeVO</code>.
	 * 
	 * @return O objeto da classe <code>ChequeVO</code> com os dados devidamente
	 *         montados.
	 */
	public static ChequeVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ChequeVO obj = new ChequeVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSacado(dadosSQL.getString("sacado"));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
		obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		obj.getLocalizacaoCheque().setCodigo(new Integer(dadosSQL.getInt("localizacaocheque")));
		obj.setChequeProprio(new Boolean(dadosSQL.getBoolean("chequeProprio")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setValor(new Double(dadosSQL.getDouble("valor")));
		obj.setPago(new Boolean(dadosSQL.getBoolean("pago")));
		obj.setRecebimento(new Integer(dadosSQL.getInt("recebimento")));
		obj.setPagamento(new Integer(dadosSQL.getInt("pagamento")));
		obj.setValorUsadoRecebimento(new Double(dadosSQL.getDouble("valorusadorecebimento")));
		obj.setEmitentePessoaJuridica(dadosSQL.getBoolean("emitentePessoaJuridica"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setCnpj(dadosSQL.getString("cnpj"));
		obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contacorrente")));
		obj.setDataEmissao(dadosSQL.getDate("dataemissao"));
		obj.setDataPrevisao(dadosSQL.getDate("dataPrevisao"));
		obj.setBanco(dadosSQL.getString("banco"));
		obj.setAgencia(dadosSQL.getString("agencia"));
		obj.setNumeroContaCorrente(dadosSQL.getString("numerocontacorrente"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosLocalizacaoCheque(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	public static ChequeVO montarDadosTelaConsulta(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ChequeVO obj = new ChequeVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getLocalizacaoCheque().setCodigo(new Integer(dadosSQL.getInt("localizacaocheque")));
		obj.setChequeProprio(new Boolean(dadosSQL.getBoolean("chequeProprio")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setValor(new Double(dadosSQL.getDouble("valor")));
		obj.setPago(new Boolean(dadosSQL.getBoolean("pago")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setRecebimento(new Integer(dadosSQL.getInt("recebimento")));
		obj.setPagamento(new Integer(dadosSQL.getInt("pagamento")));
		obj.setValorUsadoRecebimento(new Double(dadosSQL.getDouble("valorusadorecebimento")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
		obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
		obj.setSacado(dadosSQL.getString("sacado"));
		obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contacorrente")));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setDataEmissao(dadosSQL.getDate("dataemissao"));
		obj.setDataPrevisao(dadosSQL.getDate("dataPrevisao"));
		obj.setBanco(dadosSQL.getString("banco"));
		obj.setAgencia(dadosSQL.getString("agencia"));
		obj.setNumeroContaCorrente(dadosSQL.getString("numerocontacorrente"));
		obj.setEmitentePessoaJuridica(dadosSQL.getBoolean("emitentePessoaJuridica"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setCnpj(dadosSQL.getString("cnpj"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}

		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosLocalizacaoCheque(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>AgenciaVO</code> relacionado ao objeto <code>ChequeVO</code>. Faz
	 * uso da chave primária da classe <code>AgenciaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosContaCorrente(ChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrente().getCodigo().intValue() == 0) {
			obj.setContaCorrente(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFornecedor(ChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(ChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosLocalizacaoCheque(ChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getLocalizacaoCheque().getCodigo().intValue() == 0) {
			obj.setLocalizacaoCheque(new ContaCorrenteVO());
			return;
		}
		obj.setLocalizacaoCheque(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getLocalizacaoCheque().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosParceiro(ChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ClienteVO</code> relacionado ao objeto <code>ChequeVO</code>. Faz
	 * uso da chave primária da classe <code>ClienteVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoa(ChequeVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>CpRNPtRLogVO</code> contidos em um Hashtable no BD. Faz uso da
	 * operação <code>excluirCpRNPtRLogs</code> e
	 * <code>incluirCpRNPtRLogs</code> disponíveis na classe
	 * <code>CpRNPtRLog</code>.
	 * 
	 * @param chequeVO
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	// public void alterarChequePtR(Integer ptRLog, Integer contaCorrente,
	// ChequeVO objetos) throws Exception {
	// incluirChequePagamento(ptRLog, objetos);
	// }
	public Integer incluirChequePagamento(Integer formaPagamentoNegociacaoPagamentoVO, Integer unidadeEnsino, ChequeVO chequeVO, UsuarioVO usuario) throws Exception {
		chequeVO.setPagamento(formaPagamentoNegociacaoPagamentoVO);

		chequeVO.getUnidadeEnsino().setCodigo(unidadeEnsino);
		chequeVO.getLocalizacaoCheque().setCodigo(0);
		if (chequeVO.getCodigo().intValue() == 0) {
			incluir(chequeVO, usuario);
		} else {
			alterar(chequeVO);
		}
		return chequeVO.getCodigo();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public Integer incluirChequeRecebimento(Integer rtRLogPrm, Integer contaCorrente, Integer unidadeEnsino, ChequeVO objetos, UsuarioVO usuario) throws Exception {
		objetos.setRecebimento(rtRLogPrm);
		objetos.setSituacao("EC");
		objetos.getLocalizacaoCheque().setCodigo(contaCorrente);
		objetos.getUnidadeEnsino().setCodigo(unidadeEnsino);
		if (objetos.getCodigo().intValue() == 0) {
			incluir(objetos, usuario);
		} else {
			alterar(objetos);
		}
		return objetos.getCodigo();
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ChequeVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ChequeVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Cheque WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Cheque ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Cheque.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Cheque.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void inutilizarCheque(Integer codigoCheque) throws Exception {
		String sqlStr = "UPDATE cheque SET localizacaoCheque = null, situacao = 'DS', contacorrente = null WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { codigoCheque });
	}

	@Override
	public List<ChequeVO> consultarChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select cheque.* from cheque");
		sb.append(" inner join extratoMapaLancamentoFuturo on extratoMapaLancamentoFuturo.cheque = cheque.codigo ");
		sb.append(" AND extratoMapaLancamentoFuturo.codigo  = (select max(codigo) from extratoMapaLancamentoFuturo emlf where emlf.cheque = cheque.codigo ");
		if (!contaCorrente.equals(0)) {
			sb.append(" and emlf.contaCorrenteCheque = ").append(contaCorrente);
		}
		sb.append(" and emlf.dataInicioApresentacao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sb.append(" and case when (emlf.chequecompensado is null or emlf.chequecompensado = false) then ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao >= '").append(Uteis.getDataJDBC(dataFim)).append("') else ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao > '").append(Uteis.getDataJDBC(dataFim)).append("') end) ");
		sb.append(" and extratoMapaLancamentoFuturo.tipoMapaLancamentoFuturo = '").append(TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.name()).append("' ");
		sb.append(" order by dataPrevisao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuario);
	}

	@Override
	public Double consultarTotalChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select sum(valor) AS valor from cheque");
		sb.append(" inner join extratoMapaLancamentoFuturo on extratoMapaLancamentoFuturo.cheque = cheque.codigo ");
		sb.append(" AND extratoMapaLancamentoFuturo.codigo  = (select max(codigo) from extratoMapaLancamentoFuturo emlf where emlf.cheque = cheque.codigo ");
		if (!contaCorrente.equals(0)) {
			sb.append(" and emlf.contaCorrenteCheque = ").append(contaCorrente);
		}
		sb.append(" and emlf.dataInicioApresentacao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sb.append(" and case when (emlf.chequecompensado is null or emlf.chequecompensado = false) then ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao >= '").append(Uteis.getDataJDBC(dataFim)).append("') else ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao > '").append(Uteis.getDataJDBC(dataFim)).append("') end) ");
		sb.append(" and extratoMapaLancamentoFuturo.tipoMapaLancamentoFuturo = '").append(TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.name()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		}
		return 0.0;
	}

	@Override
	public List<ChequeVO> consultarChequePendenteCompesacaoRecebimentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select cheque.* from cheque ");
		sb.append(" inner join extratoMapaLancamentoFuturo on extratoMapaLancamentoFuturo.cheque = cheque.codigo ");
		sb.append(" AND extratoMapaLancamentoFuturo.codigo  = (select max(codigo) from extratoMapaLancamentoFuturo emlf where emlf.cheque = cheque.codigo ");
		if (!contaCorrente.equals(0)) {
			sb.append(" and emlf.contaCorrenteCheque = ").append(contaCorrente);
		}
		sb.append(" and emlf.dataInicioApresentacao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sb.append(" and case when (emlf.chequecompensado is null or emlf.chequecompensado = false) then ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao >= '").append(Uteis.getDataJDBC(dataFim)).append("') else ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao > '").append(Uteis.getDataJDBC(dataFim)).append("') end) ");
		sb.append(" and extratoMapaLancamentoFuturo.tipoMapaLancamentoFuturo = '").append(TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.name()).append("' ");
		sb.append(" order by dataPrevisao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuario);
	}

	@Override
	public Double consultarTotalChequePendenteCompesacaoRecebimentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select sum(valor) AS valor from cheque");
		sb.append(" inner join extratoMapaLancamentoFuturo on extratoMapaLancamentoFuturo.cheque = cheque.codigo");
		sb.append(" AND extratoMapaLancamentoFuturo.codigo  = (select max(codigo) from extratoMapaLancamentoFuturo emlf where emlf.cheque = cheque.codigo ");
		if (!contaCorrente.equals(0)) {
			sb.append(" and emlf.contaCorrenteCheque = ").append(contaCorrente);
		}
		sb.append(" and emlf.dataInicioApresentacao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sb.append(" and case when (emlf.chequecompensado is null or emlf.chequecompensado = false) then ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao >= '").append(Uteis.getDataJDBC(dataFim)).append("') else ");
		sb.append(" (emlf.datafimApresentacao is null or emlf.datafimApresentacao > '").append(Uteis.getDataJDBC(dataFim)).append("') end) ");
		sb.append(" and extratoMapaLancamentoFuturo.tipoMapaLancamentoFuturo = '").append(TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.name()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		}
		return 0.0;
	}

	@Override
	public List<ChequeVO> consultarChequeDevolvidoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select distinct cheque.* from cheque ");
		sb.append(" inner join extratoMapaLancamentoFuturo on extratoMapaLancamentoFuturo.cheque = cheque.codigo");
		sb.append(" AND extratoMapaLancamentoFuturo.codigo  = (select max(codigo) from extratoMapaLancamentoFuturo emlf where emlf.cheque = cheque.codigo ");
		if (!contaCorrente.equals(0)) {
			sb.append(" and emlf.contaCorrenteCheque = ").append(contaCorrente);
		}
		// sb.append(" and emlf.dataInicioApresentacao <= '").append(Uteis.getDataJDBC(dataFim)).append("' and (emlf.datafimApresentacao is null or emlf.datafimApresentacao >= '").append(Uteis.getDataJDBC(dataFim)).append("')) ");
		sb.append(" and emlf.dataInicioApresentacao = '").append(Uteis.getDataJDBC(dataFim)).append("') ");
		sb.append(" and extratoMapaLancamentoFuturo.tipoMapaLancamentoFuturo = '").append(TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.name()).append("' ");
		sb.append(" order by dataPrevisao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuario);
	}

	@Override
	public Double consultarTotalChequeDevolvidoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select sum(distinct cheque.valor) AS valor from cheque ");
		sb.append(" inner join extratoMapaLancamentoFuturo on extratoMapaLancamentoFuturo.cheque = cheque.codigo");
		sb.append(" AND extratoMapaLancamentoFuturo.codigo  = (select max(codigo) from extratoMapaLancamentoFuturo emlf where emlf.cheque = cheque.codigo ");
		if (!contaCorrente.equals(0)) {
			sb.append(" and emlf.contaCorrenteCheque = ").append(contaCorrente);
		}
		// sb.append(" and emlf.dataInicioApresentacao <= '").append(Uteis.getDataJDBC(dataFim)).append("' and (emlf.datafimApresentacao is null or emlf.datafimApresentacao >= '").append(Uteis.getDataJDBC(dataFim)).append("')) ");
		sb.append(" and emlf.dataInicioApresentacao = '").append(Uteis.getDataJDBC(dataFim)).append("') ");
		sb.append(" and extratoMapaLancamentoFuturo.tipoMapaLancamentoFuturo = '").append(TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.name()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		}
		return 0.0;
	}

	@Override
	public ChequeVO consultarUltimoChequePorSacadoTipoSacado(String tipoSacado, Integer sacado, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		if (sacado != null && sacado > 0) {
			StringBuilder sqlStr = new StringBuilder("SELECT cheque.* FROM Cheque ");
			if (tipoSacado.equals("FO")) {
				sqlStr.append(" inner join Fornecedor on Fornecedor.codigo = Cheque.fornecedor");
				sqlStr.append(" and Fornecedor.codigo = " + sacado + " ");
			} else if (tipoSacado.equals("PA")) {
				sqlStr.append(" inner join Parceiro on Parceiro.codigo = Cheque.parceiro");
				sqlStr.append(" and Parceiro.codigo = " + sacado + " ");
			} else {
				sqlStr.append(" inner join Pessoa on Pessoa.codigo = Cheque.pessoa  ");
				sqlStr.append(" and Pessoa.codigo = " + sacado + " ");
			}
			if (unidadeEnsino.intValue() != 0) {
				sqlStr.append(" where Cheque.UnidadeEnsino = " + unidadeEnsino.intValue());
			}
			sqlStr.append(" ORDER BY cheque.codigo desc limit 1");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return montarDados(tabelaResultado, nivelMontarDados, usuario);
			}
		}
		return new ChequeVO();
	}

	public void validarUnicidadeCheque(ChequeVO chequeVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT codigo FROM cheque WHERE ");
		sqlStr.append(" banco = '").append(chequeVO.getBanco()).append("' ");
		sqlStr.append(" AND agencia = '").append(chequeVO.getAgencia()).append("' ");
		sqlStr.append(" AND numerocontacorrente = '").append(chequeVO.getNumeroContaCorrente()).append("' ");
		sqlStr.append(" AND numero = '").append(chequeVO.getNumero()).append("' ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			throw new Exception("O cheque de Nr. " + chequeVO.getNumero() + ", está sendo utilizado em outro Recebimento.");
		}
	}

	public List<ChequeVO> consultarPorCodigoSacadoTipoPessoaESituacao(Integer sacado, String tipoSacado, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM cheque ");
		if (tipoSacado.equals("FO")) {
			sqlStr.append(" INNER JOIN fornecedor ON fornecedor.codigo = cheque.fornecedor ");
			sqlStr.append(" AND fornecedor.codigo = ").append(sacado).append(" ");
		} else if (tipoSacado.equals("PA")) {
			sqlStr.append(" INNER JOIN parceiro ON parceiro.codigo = cheque.parceiro ");
			sqlStr.append(" AND parceiro.codigo = ").append(sacado).append(" ");
		} else {
			sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = cheque.pessoa ");
			sqlStr.append(" AND pessoa.codigo = ").append(sacado).append(" ");
		}
		if (!situacao.equals("")) {
			sqlStr.append(" AND cheque.situacao = '").append(situacao).append("' ");
		}
		sqlStr.append(" ORDER BY dataPrevisao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaCheque(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			sqlStr.append(" UPDATE cheque SET pessoa = ").append(contaReceberVO.getPessoa().getCodigo());
		} else {
			sqlStr.append(" UPDATE cheque SET pessoa = ").append(contaReceberVO.getResponsavelFinanceiro().getCodigo());
		}
		sqlStr.append(" WHERE codigo IN ( ");
		sqlStr.append(" 	SELECT cheque FROM formapagamentonegociacaorecebimento WHERE negociacaorecebimento IN ( ");
		sqlStr.append(" 		SELECT negociacaorecebimento FROM contarecebernegociacaorecebimento ");
		sqlStr.append(" 		WHERE contareceber = ").append(contaReceberVO.getCodigo());
		sqlStr.append(" 	) AND cheque IS NOT NULL ");
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE Cheque set pessoa=? WHERE ((pessoa = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}

	@Override
	public ChequeVO consultarChequeDevolvidoPorContaReceberCodOrigemTipoOrigem(String codOrigem,String TipoOrigem) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT ch.* FROM cheque ch ");
		sqlStr.append("inner join devolucaocheque dv on  dv.cheque = ch.codigo ");  
		sqlStr.append("inner join contareceber cr on  CAST (cr.codorigem AS INTEGER) = dv.codigo ");
		sqlStr.append("where cr.codorigem = ").append("'"+codOrigem+"'");
		sqlStr.append(" and cr.tipoorigem =").append("'"+TipoOrigem+"'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
		}
		return new ChequeVO();
	}
	
	private void montarFiltrosSituacaoCaixasUnidadeEnsino(List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(situacoes)) {
			sqlStr.append(situacoes.stream().collect(Collectors.joining("', '", " AND cheque.situacao IN ('", "') ")));
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and UnidadeEnsino.codigo = " + unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(listaCaixas)) {
			sqlStr.append(listaCaixas.stream().map(ContaCorrenteVO::getCodigo).map(String::valueOf)	.collect(Collectors.joining(", ", " AND cheque.localizacaocheque IN (", ") ")));
		}
	}

	private List<ContaCorrenteVO> montarListaCaixas(Integer localizacao) {
		List<ContaCorrenteVO> lista = new ArrayList<>();
		if (Uteis.isAtributoPreenchido(localizacao)) {
			ContaCorrenteVO c = new ContaCorrenteVO();
			c.setCodigo(localizacao);
			lista.add(c);
		}
		return lista;
	}
}
