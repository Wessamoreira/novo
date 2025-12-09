package negocio.facade.jdbc.eventos;

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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.eventos.CursosEventoVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.CursosEventoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>CursosEventoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>CursosEventoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CursosEventoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CursosEvento extends ControleAcesso implements CursosEventoInterfaceFacade {

	protected static String idEntidade;

	public CursosEvento() throws Exception {
		super();
		setIdEntidade("CursosEvento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>CursosEventoVO</code>.
	 */
	public CursosEventoVO novo() throws Exception {
		CursosEvento.incluir(getIdEntidade());
		CursosEventoVO obj = new CursosEventoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>CursosEventoVO</code>. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CursosEventoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CursosEventoVO obj) throws Exception {
		try {
			CursosEvento.incluir(getIdEntidade());
			CursosEventoVO.validarDados(obj);
			final String sql = "INSERT INTO CursosEvento( titulo, data, hora, evento, ministrante, miniCurriculoMinistrante, tipoMinicurso, descricaoMinicurso, conteudoProgramatico, nrVagasCurso, nrMaximoVagasExcedentes, localCurso, duracao, valorAluno, valorProfessor, valorFuncionario, valorComunidade ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getTitulo());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(3, obj.getHora());
					sqlInserir.setInt(4, obj.getEvento().getCodigo().intValue());
					sqlInserir.setString(5, obj.getMinistrante());
					sqlInserir.setString(6, obj.getMiniCurriculoMinistrante());
					sqlInserir.setString(7, obj.getTipoMinicurso());
					sqlInserir.setString(8, obj.getDescricaoMinicurso());
					sqlInserir.setString(9, obj.getConteudoProgramatico());
					sqlInserir.setInt(10, obj.getNrVagasCurso().intValue());
					sqlInserir.setInt(11, obj.getNrMaximoVagasExcedentes().intValue());
					sqlInserir.setString(12, obj.getLocalCurso());
					sqlInserir.setString(13, obj.getDuracao());
					sqlInserir.setDouble(14, obj.getValorAluno().doubleValue());
					sqlInserir.setDouble(15, obj.getValorProfessor().doubleValue());
					sqlInserir.setDouble(16, obj.getValorFuncionario().doubleValue());
					sqlInserir.setDouble(17, obj.getValorComunidade().doubleValue());
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
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursosEventoVO</code>. Sempre
	 * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CursosEventoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CursosEventoVO obj) throws Exception {
		try {
			CursosEvento.alterar(getIdEntidade());
			CursosEventoVO.validarDados(obj);
			final String sql = "UPDATE CursosEvento set titulo=?, data=?, hora=?, evento=?, ministrante=?, miniCurriculoMinistrante=?, tipoMinicurso=?, descricaoMinicurso=?, conteudoProgramatico=?, nrVagasCurso=?, nrMaximoVagasExcedentes=?, localCurso=?, duracao=?, valorAluno=?, valorProfessor=?, valorFuncionario=?, valorComunidade=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTitulo());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(3, obj.getHora());
					sqlAlterar.setInt(4, obj.getEvento().getCodigo().intValue());
					sqlAlterar.setString(5, obj.getMinistrante());
					sqlAlterar.setString(6, obj.getMiniCurriculoMinistrante());
					sqlAlterar.setString(7, obj.getTipoMinicurso());
					sqlAlterar.setString(8, obj.getDescricaoMinicurso());
					sqlAlterar.setString(9, obj.getConteudoProgramatico());
					sqlAlterar.setInt(10, obj.getNrVagasCurso().intValue());
					sqlAlterar.setInt(11, obj.getNrMaximoVagasExcedentes().intValue());
					sqlAlterar.setString(12, obj.getLocalCurso());
					sqlAlterar.setString(13, obj.getDuracao());
					sqlAlterar.setDouble(14, obj.getValorAluno().doubleValue());
					sqlAlterar.setDouble(15, obj.getValorProfessor().doubleValue());
					sqlAlterar.setDouble(16, obj.getValorFuncionario().doubleValue());
					sqlAlterar.setDouble(17, obj.getValorComunidade().doubleValue());
					sqlAlterar.setInt(18, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>CursosEventoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CursosEventoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CursosEventoVO obj) throws Exception {
		try {
			CursosEvento.excluir(getIdEntidade());
			String sql = "DELETE FROM CursosEvento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Double valorComunidade</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorComunidade(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE valorComunidade >= " + valorConsulta.doubleValue() + " ORDER BY valorComunidade";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Double valorFuncionario</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorFuncionario(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE valorFuncionario >= " + valorConsulta.doubleValue() + " ORDER BY valorFuncionario";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Double valorProfessor</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorProfessor(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE valorProfessor >= " + valorConsulta.doubleValue() + " ORDER BY valorProfessor";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Double valorAluno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorAluno(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE valorAluno >= " + valorConsulta.doubleValue() + " ORDER BY valorAluno";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>String localCurso</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorLocalCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE localCurso like('" + valorConsulta + "%') ORDER BY localCurso";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Integer nrMaximoVagasExcedentes</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrMaximoVagasExcedentes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE nrMaximoVagasExcedentes >= " + valorConsulta.intValue() + " ORDER BY nrMaximoVagasExcedentes";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Integer nrVagasCurso</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrVagasCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE nrVagasCurso >= " + valorConsulta.intValue() + " ORDER BY nrVagasCurso";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>String tipoMinicurso</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoMinicurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE tipoMinicurso like('" + valorConsulta + "%') ORDER BY tipoMinicurso";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>String titulo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTitulo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE titulo like('" + valorConsulta + "%') ORDER BY titulo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CursosEvento</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CursosEvento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>CursosEventoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>CursosEventoVO</code>.
	 * 
	 * @return O objeto da classe <code>CursosEventoVO</code> com os dados devidamente montados.
	 */
	public static CursosEventoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		CursosEventoVO obj = new CursosEventoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTitulo(dadosSQL.getString("titulo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setHora(dadosSQL.getString("hora"));
		obj.getEvento().setCodigo(new Integer(dadosSQL.getInt("evento")));
		obj.setMinistrante(dadosSQL.getString("ministrante"));
		obj.setMiniCurriculoMinistrante(dadosSQL.getString("miniCurriculoMinistrante"));
		obj.setTipoMinicurso(dadosSQL.getString("tipoMinicurso"));
		obj.setDescricaoMinicurso(dadosSQL.getString("descricaoMinicurso"));
		obj.setConteudoProgramatico(dadosSQL.getString("conteudoProgramatico"));
		obj.setNrVagasCurso(new Integer(dadosSQL.getInt("nrVagasCurso")));
		obj.setNrMaximoVagasExcedentes(new Integer(dadosSQL.getInt("nrMaximoVagasExcedentes")));
		obj.setLocalCurso(dadosSQL.getString("localCurso"));
		obj.setDuracao(dadosSQL.getString("duracao"));
		obj.setValorAluno(new Double(dadosSQL.getDouble("valorAluno")));
		obj.setValorProfessor(new Double(dadosSQL.getDouble("valorProfessor")));
		obj.setValorFuncionario(new Double(dadosSQL.getDouble("valorFuncionario")));
		obj.setValorComunidade(new Double(dadosSQL.getDouble("valorComunidade")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosEvento(obj, nivelMontarDados,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>EventoVO</code> relacionado ao objeto
	 * <code>CursosEventoVO</code>. Faz uso da chave primária da classe <code>EventoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosEvento(CursosEventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getEvento().getCodigo().intValue() == 0) {
			obj.setEvento(new EventoVO());
			return;
		}
		obj.setEvento(getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(obj.getEvento().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>CursosEventoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public CursosEventoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM CursosEvento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return CursosEvento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CursosEvento.idEntidade = idEntidade;
	}
}
