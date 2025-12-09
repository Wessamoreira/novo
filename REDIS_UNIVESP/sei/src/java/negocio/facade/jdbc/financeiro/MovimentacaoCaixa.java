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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoCaixaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.MovimentacaoCaixaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MovimentacaoCaixaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>MovimentacaoCaixaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see MovimentacaoCaixaVO
 * @see ControleAcesso
 * @see FluxoCaixa
 */
@Repository
@Scope("singleton")
@Lazy
public class MovimentacaoCaixa extends ControleAcesso implements MovimentacaoCaixaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4452468824772467599L;
	protected static String idEntidade;

	public MovimentacaoCaixa() throws Exception {
		super();
		setIdEntidade("FluxoCaixa");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>MovimentacaoCaixaVO</code>.
	 */
	public MovimentacaoCaixaVO novo() throws Exception {
		MovimentacaoCaixa.incluir(getIdEntidade());
		MovimentacaoCaixaVO obj = new MovimentacaoCaixaVO();
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<MovimentacaoCaixaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (MovimentacaoCaixaVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>MovimentacaoCaixaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MovimentacaoCaixaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MovimentacaoCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			MovimentacaoCaixaVO.validarDados(obj);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO MovimentacaoCaixa( data, responsavel, tipoMovimentacao, codigoOrigem, tipoOrigem, formaPagamento, valor, fluxoCaixa, pessoa, fornecedor, banco, parceiro, numeroCheque, cheque, agenciaCheque, contaCorrenteCheque, emitenteCheque, sacadoCheque, cpfCnpjCheque, dataPrevisaoCheque, pessoaJuridicaCheque, sacadobanco, operadoracartao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					sqlInserir.setString(3, obj.getTipoMovimentacao());
					sqlInserir.setInt(4, obj.getCodigoOrigem().intValue());
					sqlInserir.setString(5, obj.getTipoOrigem());
					if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getFormaPagamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setDouble(7, obj.getValor().doubleValue());
					if (obj.getFluxoCaixa().intValue() != 0) {
						sqlInserir.setInt(8, obj.getFluxoCaixa().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getPessoa().getCodigo());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getFornecedor().getCodigo());
					} else {
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setString(11, obj.getBanco());
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getParceiro().getCodigo());
					} else {
						sqlInserir.setNull(12, 0);
					}
					if (obj.getCheque() != null && obj.getCheque() > 0) {
						sqlInserir.setString(13, obj.getNumeroCheque());
						sqlInserir.setInt(14, obj.getCheque());
						sqlInserir.setString(15, obj.getAgenciaCheque());
						sqlInserir.setString(16, obj.getContaCorrenteCheque());
						sqlInserir.setString(17, obj.getEmitenteCheque());
						sqlInserir.setString(18, obj.getSacadoCheque());
						sqlInserir.setString(19, obj.getCpfCnpjCheque());
						sqlInserir.setDate(20, Uteis.getDataJDBC(obj.getDataPrevisaoCheque()));
						sqlInserir.setBoolean(21, obj.getPessoaJuridicaCheque());
					}else{
						sqlInserir.setNull(13, 0);
						sqlInserir.setNull(14, 0);
						sqlInserir.setNull(15, 0);
						sqlInserir.setNull(16, 0);
						sqlInserir.setNull(17, 0);
						sqlInserir.setNull(18, 0);
						sqlInserir.setNull(19, 0);
						sqlInserir.setNull(20, 0);
						sqlInserir.setNull(21, 0);
					}
					if (obj.getBancoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(22, obj.getBancoVO().getCodigo());
					} else {
						sqlInserir.setNull(22, 0);
					}
					if (obj.getOperadoraCartaoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(23, obj.getOperadoraCartaoVO().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>MovimentacaoCaixaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MovimentacaoCaixaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MovimentacaoCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {

			MovimentacaoCaixaVO.validarDados(obj);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE MovimentacaoCaixa set data=?, responsavel=?, tipoMovimentacao=?, codigoOrigem=?, tipoOrigem=?, formaPagamento=?, valor=?, fluxoCaixa=?, pessoa=?, fornecedor=?, banco = ?, parceiro = ?, numeroCheque=?, cheque = ?, agenciaCheque = ?, contaCorrenteCheque = ?, emitenteCheque = ?, sacadoCheque = ?, cpfCnpjCheque = ?, dataPrevisaoCheque = ?, pessoaJuridicaCheque = ?, sacadobanco =?, operadoracartao =? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setString(3, obj.getTipoMovimentacao());
					sqlAlterar.setInt(4, obj.getCodigoOrigem().intValue());
					sqlAlterar.setString(5, obj.getTipoOrigem());
					if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getFormaPagamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setDouble(7, obj.getValor().doubleValue());
					if (obj.getFluxoCaixa().intValue() != 0) {
						sqlAlterar.setInt(8, obj.getFluxoCaixa().intValue());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getPessoa().getCodigo());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getFornecedor().getCodigo());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setString(11, obj.getBanco());
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(12, obj.getParceiro().getCodigo());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					
					if (obj.getCheque() != null && obj.getCheque() > 0) {
						sqlAlterar.setString(13, obj.getNumeroCheque());
						sqlAlterar.setInt(14, obj.getCheque());
						sqlAlterar.setString(15, obj.getAgenciaCheque());
						sqlAlterar.setString(16, obj.getContaCorrenteCheque());
						sqlAlterar.setString(17, obj.getEmitenteCheque());
						sqlAlterar.setString(18, obj.getSacadoCheque());
						sqlAlterar.setString(19, obj.getCpfCnpjCheque());
						sqlAlterar.setDate(20, Uteis.getDataJDBC(obj.getDataPrevisaoCheque()));
						sqlAlterar.setBoolean(21, obj.getPessoaJuridicaCheque());
					}else{
						sqlAlterar.setNull(13, 0);
						sqlAlterar.setNull(14, 0);
						sqlAlterar.setNull(15, 0);
						sqlAlterar.setNull(16, 0);
						sqlAlterar.setNull(17, 0);
						sqlAlterar.setNull(18, 0);
						sqlAlterar.setNull(19, 0);
						sqlAlterar.setNull(20, 0);
						sqlAlterar.setNull(21, 0);
					}
					if (obj.getBancoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(22, obj.getBancoVO().getCodigo());
					} else {
						sqlAlterar.setNull(22, 0);
					}
					if (obj.getOperadoraCartaoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(23, obj.getOperadoraCartaoVO().getCodigo());
					} else {
						sqlAlterar.setNull(23, 0);
					}
					sqlAlterar.setInt(24, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>MovimentacaoCaixaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MovimentacaoCaixaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MovimentacaoCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM MovimentacaoCaixa WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>codigo</code> da classe
	 * <code>FluxoCaixa</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoFluxoCaixa(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT MovimentacaoCaixa.* FROM MovimentacaoCaixa, FluxoCaixa WHERE MovimentacaoCaixa.fluxoCaixa = FluxoCaixa.codigo and FluxoCaixa.codigo >= " + valorConsulta.intValue() + " ORDER BY FluxoCaixa.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>FormaPagamento</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeFormaPagamento(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT MovimentacaoCaixa.* FROM MovimentacaoCaixa, FormaPagamento WHERE MovimentacaoCaixa.formaPagamento = FormaPagamento.codigo and upper( FormaPagamento.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY FormaPagamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>String tipoOrigem</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoOrigem(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM MovimentacaoCaixa WHERE upper( tipoOrigem ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoOrigem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>Integer codigoOrigem</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<MovimentacaoCaixaVO> consultarPorCodigoTipoOrigem(Integer codigoOrigem, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM MovimentacaoCaixa WHERE codigoOrigem = " + codigoOrigem.intValue() + " and tipoorigem = '" + tipoOrigem + "' ORDER BY codigoOrigem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>String tipoMovimentacao</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoMovimentacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MovimentacaoCaixa WHERE upper( tipoMovimentacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoMovimentacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>Integer responsavel</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MovimentacaoCaixa WHERE responsavel >= " + valorConsulta.intValue() + " ORDER BY responsavel";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MovimentacaoCaixa WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>MovimentacaoCaixa</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MovimentacaoCaixa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 */
	public List<MovimentacaoCaixaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<MovimentacaoCaixaVO> vetResultado = new ArrayList<MovimentacaoCaixaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>MovimentacaoCaixaVO</code>.
	 * 
	 * @return O objeto da classe <code>MovimentacaoCaixaVO</code> com os dados
	 *         devidamente montados.
	 */
	public MovimentacaoCaixaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MovimentacaoCaixaVO obj = new MovimentacaoCaixaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.setTipoMovimentacao(dadosSQL.getString("tipoMovimentacao"));
		obj.setCodigoOrigem(new Integer(dadosSQL.getInt("codigoOrigem")));
		obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
		obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formaPagamento")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
		
		obj.setValor(new Double(dadosSQL.getDouble("valor")));
		obj.setFluxoCaixa(new Integer(dadosSQL.getInt("fluxoCaixa")));
		obj.getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
		obj.setBanco(dadosSQL.getString("banco"));
		obj.setNumeroCheque(dadosSQL.getString("numeroCheque"));
		obj.setCheque(dadosSQL.getInt("cheque"));
		obj.setAgenciaCheque(dadosSQL.getString("agenciaCheque"));
		obj.setContaCorrenteCheque(dadosSQL.getString("contaCorrenteCheque"));
		obj.setEmitenteCheque(dadosSQL.getString("emitenteCheque"));
		obj.setSacadoCheque(dadosSQL.getString("sacadoCheque"));
		obj.setCpfCnpjCheque(dadosSQL.getString("cpfCnpjCheque"));
		obj.setDataPrevisaoCheque(dadosSQL.getDate("dataPrevisaoCheque"));
		obj.setPessoaJuridicaCheque(dadosSQL.getBoolean("pessoaJuridicaCheque"));
		obj.getBancoVO().setCodigo(dadosSQL.getInt("sacadobanco"));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoracartao"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
		montarDadosFornecedor(obj, nivelMontarDados, usuario);
		montarDadosPessoa(obj, nivelMontarDados, usuario);
		montarDadosParceiro(obj, nivelMontarDados, usuario);
		montarDadosBanco(obj, nivelMontarDados, usuario);
		montarDadosOperadoraCartao(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FormaPagamentoVO</code> relacionado ao objeto
	 * <code>MovimentacaoCaixaVO</code>. Faz uso da chave primária da classe
	 * <code>FormaPagamentoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFormaPagamento(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFornecedor(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosPessoa(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavel(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>MovimentacaoCaixaVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>MovimentacaoCaixa</code>.
	 * 
	 * @param <code>fluxoCaixa</code> campo chave para exclusão dos objetos no
	 *        BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMovimentacaoCaixas(Integer fluxoCaixa, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM MovimentacaoCaixa WHERE (fluxoCaixa = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { fluxoCaixa.intValue() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer excluirMovimentacaoCaixasPorAlteracaoMapaPendenciaCartaoCredito(Integer negociacaorecebimento, Integer formaPagamento, Integer operadoraCartao, UsuarioVO usuario) throws Exception{
		StringBuilder sqlStr = new StringBuilder(" delete from movimentacaocaixa  ");
		sqlStr.append(" where codigoorigem = '").append(negociacaorecebimento).append("' ");
		sqlStr.append(" and formapagamento = ").append(formaPagamento).append(" ");
		sqlStr.append(" and (operadoracartao = ").append(operadoraCartao).append("  or operadoracartao is null )");
		sqlStr.append(" and tipoorigem = 'RE'");
		sqlStr.append(" and tipomovimentacao = 'EN' ");
		sqlStr.append(" returning fluxocaixa ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()) {
			return rs.getInt("fluxocaixa");
		}		
		return 0 ;
	} 

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>MovimentacaoCaixaVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirMovimentacaoCaixas</code> e
	 * <code>incluirMovimentacaoCaixas</code> disponíveis na classe
	 * <code>MovimentacaoCaixa</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMovimentacaoCaixas(Integer fluxoCaixa, List objetos, UsuarioVO usuario) throws Exception {
		excluirMovimentacaoCaixas(fluxoCaixa, usuario);
		incluirMovimentacaoCaixas(fluxoCaixa, objetos, usuario);
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>MovimentacaoCaixaVO</code> no BD. Garantindo o relacionamento com a
	 * entidade principal <code>financeiro.FluxoCaixa</code> através do atributo
	 * de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMovimentacaoCaixas(Integer fluxoCaixaPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MovimentacaoCaixaVO obj = (MovimentacaoCaixaVO) e.next();
			obj.setFluxoCaixa(fluxoCaixaPrm);
			incluir(obj, usuario);
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>MovimentacaoCaixaVO</code> relacionados a um objeto da classe
	 * <code>financeiro.FluxoCaixa</code>.
	 * 
	 * @param fluxoCaixa
	 *            Atributo de <code>financeiro.FluxoCaixa</code> a ser utilizado
	 *            para localizar os objetos da classe
	 *            <code>MovimentacaoCaixaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>MovimentacaoCaixaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List consultarMovimentacaoCaixas(Integer fluxoCaixa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// MovimentacaoCaixa.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM MovimentacaoCaixa WHERE fluxoCaixa = " + fluxoCaixa.intValue() + " order by data";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (resultado.next()) {
			MovimentacaoCaixaVO novoObj = new MovimentacaoCaixaVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;

	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>MovimentacaoCaixaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public MovimentacaoCaixaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM MovimentacaoCaixa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( MovimentacaoCaixa ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return MovimentacaoCaixa.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		MovimentacaoCaixa.idEntidade = idEntidade;
	}
	
	@Override
	public List<MovimentacaoCaixaVO> consultarChequeFluxoCaixaPelaMovimentacaoCaixa(Integer contaCaixa, Date dataBase) throws Exception{
		StringBuilder sql  =  new StringBuilder("");
		sql.append("select distinct movimentacaocaixa.cheque, movimentacaocaixa.valor, movimentacaocaixa.banco, movimentacaocaixa.numeroCheque,   ");
		sql.append("       movimentacaocaixa.agenciaCheque, movimentacaocaixa.contaCorrenteCheque,  movimentacaocaixa.emitenteCheque,  ");
		sql.append("       movimentacaocaixa.sacadoCheque, movimentacaocaixa.cpfCnpjCheque, movimentacaocaixa.dataPrevisaoCheque, ");
		sql.append("       movimentacaocaixa.pessoaJuridicaCheque, case when cheque.codigo is null then 'DS' else cheque.situacao end as situacaoCheque ");
		sql.append(" from movimentacaocaixa  ");
		sql.append(" left join cheque on cheque.codigo = movimentacaocaixa.cheque ");
		sql.append(" where movimentacaocaixa.codigo in (");
		sql.append(" 	select max(mc.codigo) from fluxocaixa fc");
		sql.append(" 		inner join movimentacaocaixa mc on mc.fluxocaixa = fc.codigo  ");
		sql.append(" 		inner join formapagamento pf on pf.codigo = mc.formapagamento  and pf.tipo = 'CH'  ");
		sql.append(" 		where fc.contacaixa = ").append(contaCaixa).append(" and fc.dataabertura <= '").append(Uteis.getDataJDBCTimestamp(dataBase)).append("'  group by cheque ");
		sql.append(" 		having sum(case when mc.tipomovimentacao = 'EN' then 1 else -1 end ) > 0");
		sql.append(" 	)");
		sql.append(" group by movimentacaocaixa.numeroCheque, movimentacaocaixa.cheque, movimentacaocaixa.valor, movimentacaocaixa.banco,   ");
		sql.append("       movimentacaocaixa.agenciaCheque, movimentacaocaixa.contaCorrenteCheque,  movimentacaocaixa.emitenteCheque,  ");
		sql.append("       movimentacaocaixa.sacadoCheque, movimentacaocaixa.cpfCnpjCheque, movimentacaocaixa.dataPrevisaoCheque, ");
		sql.append("       movimentacaocaixa.pessoaJuridicaCheque, cheque.situacao, cheque.codigo ");
		sql.append(" having sum(case when movimentacaocaixa.tipomovimentacao = 'EN' then 1 else -1 end ) > 0 order by emitenteCheque ");
		SqlRowSet dadosSQL  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MovimentacaoCaixaVO> movimentacaoCaixaVOs = new ArrayList<MovimentacaoCaixaVO>(0);
		while(dadosSQL.next()){
			MovimentacaoCaixaVO obj  = new MovimentacaoCaixaVO();
			obj.setValor(new Double(dadosSQL.getDouble("valor")));
			obj.setBanco(dadosSQL.getString("banco"));
			obj.setNumeroCheque(dadosSQL.getString("numeroCheque"));
			obj.setCheque(dadosSQL.getInt("cheque"));
			obj.setAgenciaCheque(dadosSQL.getString("agenciaCheque"));
			obj.setContaCorrenteCheque(dadosSQL.getString("contaCorrenteCheque"));
			obj.setEmitenteCheque(dadosSQL.getString("emitenteCheque"));
			obj.setSacadoCheque(dadosSQL.getString("sacadoCheque"));			
			obj.setCpfCnpjCheque(dadosSQL.getString("cpfCnpjCheque"));
			obj.setDataPrevisaoCheque(dadosSQL.getDate("dataPrevisaoCheque"));
			obj.setPessoaJuridicaCheque(dadosSQL.getBoolean("pessoaJuridicaCheque"));
			if(dadosSQL.getString("situacaoCheque") != null && !dadosSQL.getString("situacaoCheque").trim().isEmpty()){
				obj.setSituacaoCheque(SituacaoCheque.getEnum(dadosSQL.getString("situacaoCheque")).getDescricao());
			}
			movimentacaoCaixaVOs.add(obj);
		}
		return movimentacaoCaixaVOs;
	} 
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaMovimentacaoCaixa(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			sqlStr.append(" UPDATE movimentacaocaixa SET pessoa = ").append(contaReceberVO.getPessoa().getCodigo());
		} else {
			sqlStr.append(" UPDATE movimentacaocaixa SET pessoa = ").append(contaReceberVO.getResponsavelFinanceiro().getCodigo());
		}
		sqlStr.append(" WHERE codigo IN ( ");
		sqlStr.append(" 	SELECT movimentacaocaixa.codigo FROM contarecebernegociacaorecebimento ");
		sqlStr.append(" 	INNER JOIN movimentacaocaixa ON tipoOrigem = 'RE' AND codigoorigem = negociacaorecebimento ");
		sqlStr.append(" 	WHERE contareceber = ").append(contaReceberVO.getCodigo());
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	public static void montarDadosParceiro(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0 ) {
			obj.setParceiro(new ParceiroVO());
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	@Override
	public MovimentacaoCaixaVO executarGeracaoMovimentacaoCaixa(ChequeVO chequeVO, FluxoCaixaVO fluxoCaixaVO, Integer pessoa, Integer parceiro, Integer fornecedor, Integer responsavel, Integer operadoraCartao, Integer banco, Integer codigoOrigem, Integer formaPagamento, Double valor, String tipoOrigem, String tipoMovimentacao) throws Exception {
		MovimentacaoCaixaVO obj = new MovimentacaoCaixaVO();
		obj.setFluxoCaixa(fluxoCaixaVO.getCodigo());
		obj.getFormaPagamento().setCodigo(formaPagamento);
		obj.setCodigoOrigem(codigoOrigem);
		obj.getResponsavel().setCodigo(responsavel);
		obj.setTipoMovimentacao(tipoMovimentacao);
		obj.setTipoOrigem(tipoOrigem);
		obj.setValor(valor);
		obj.getPessoa().setCodigo(pessoa);
		obj.getFornecedor().setCodigo(fornecedor);
		obj.getParceiro().setCodigo(parceiro);	
		obj.getOperadoraCartaoVO().setCodigo(operadoraCartao);
		obj.getBancoVO().setCodigo(banco);
		if (Uteis.isAtributoPreenchido(chequeVO)) {
			if (SituacaoCheque.BANCO.getValor().equals(chequeVO.getSituacao())) {
				obj.setTipoOrigem(TipoOrigemMovimentacaoCaixa.ESTORNO_CHEQUE.getValor());
			}
			obj.setBanco(chequeVO.getBanco());
			obj.setNumeroCheque(chequeVO.getNumero());
			obj.setCheque(chequeVO.getCodigo());
			obj.setAgenciaCheque(chequeVO.getAgencia());
			obj.setContaCorrenteCheque(chequeVO.getNumeroContaCorrente());
			obj.setEmitenteCheque(chequeVO.getSacado());
			if (chequeVO.getPessoa().getCodigo() > 0) {
				obj.setSacadoCheque(chequeVO.getPessoa().getNome());
			} else if (chequeVO.getFornecedor().getCodigo() > 0) {
				obj.setSacadoCheque(chequeVO.getFornecedor().getNome());
			} else if (chequeVO.getParceiro().getCodigo() > 0) {
				obj.setSacadoCheque(chequeVO.getParceiro().getNome());
			}
			obj.setDataPrevisaoCheque(chequeVO.getDataPrevisao());
			obj.setPessoaJuridicaCheque(chequeVO.getEmitentePessoaJuridica());
			if (chequeVO.getEmitentePessoaJuridica()) {
				obj.setCpfCnpjCheque(chequeVO.getCnpj());
			} else {
				obj.setCpfCnpjCheque(chequeVO.getCpf());
			}
		}
		return obj;
	}
	
	@Override
	public List<MovimentacaoCaixaVO> consultarPorFluxoCaixaFormaPagamentoDinheiroECheque(Integer fluxoCaixa, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		MovimentacaoCaixa.consultar(idEntidade, verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT MovimentacaoCaixa.* FROM MovimentacaoCaixa ");
		sqlStr.append("INNER JOIN FormaPagamento on FormaPagamento.codigo = MovimentacaoCaixa.formaPagamento ");
		sqlStr.append("WHERE MovimentacaoCaixa.fluxoCaixa = ").append(fluxoCaixa);
		sqlStr.append(" AND FormaPagamento.tipo in ('DI', 'CH')");
		sqlStr.append(" order by MovimentacaoCaixa.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public static void montarDadosBanco(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBancoVO().getCodigo().intValue() == 0 ) {			
			return;
		}
		obj.setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBancoVO().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	public static void montarDadosOperadoraCartao(MovimentacaoCaixaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getOperadoraCartaoVO().getCodigo().intValue() == 0 ) {			
			return;
		}
		obj.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartaoVO().getCodigo(), nivelMontarDados, usuario));
	}
	
}
