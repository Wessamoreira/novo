package negocio.facade.jdbc.extensao;

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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.extensao.InscricaoCursoExtensaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.extensao.InscricaoCursoExtensaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>InscricaoCursoExtensaoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>InscricaoCursoExtensaoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see InscricaoCursoExtensaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class InscricaoCursoExtensao extends ControleAcesso implements InscricaoCursoExtensaoInterfaceFacade {

	protected static String idEntidade;

	public InscricaoCursoExtensao() throws Exception {
		super();
		setIdEntidade("InscricaoCursoExtensao");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>InscricaoCursoExtensaoVO</code>.
	 */
	public InscricaoCursoExtensaoVO novo() throws Exception {
		InscricaoCursoExtensao.incluir(getIdEntidade());
		InscricaoCursoExtensaoVO obj = new InscricaoCursoExtensaoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>InscricaoCursoExtensaoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoCursoExtensaoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InscricaoCursoExtensaoVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle
			 * Comentado 28/10/2014
			 * Classe Subordinada
			 */
			// InscricaoCursoExtensao.incluir(getIdEntidade());
			InscricaoCursoExtensaoVO.validarDados(obj);
			final String sql = "INSERT INTO InscricaoCursoExtensao( cursoExtensao, data, hora, valorTotal, tipoInscricao, pessoaInscricaoCursoExtensao ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setNrInscricao((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getCursoExtensao().intValue());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(3, obj.getHora());
					sqlInserir.setDouble(4, obj.getValorTotal().doubleValue());
					sqlInserir.setString(5, obj.getTipoInscricao());
					sqlInserir.setInt(6, obj.getPessoaInscricaoCursoExtensao().getCodigo().intValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>InscricaoCursoExtensaoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoCursoExtensaoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InscricaoCursoExtensaoVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle
			 * Comentado 28/10/2014
			 * Classe Subordinada
			 */
			// InscricaoCursoExtensao.alterar(getIdEntidade());
			InscricaoCursoExtensaoVO.validarDados(obj);
			final String sql = "UPDATE InscricaoCursoExtensao set cursoExtensao=?, data=?, hora=?, valorTotal=?, tipoInscricao=?, pessoaInscricaoCursoExtensao=? WHERE ((nrInscricao = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCursoExtensao().intValue());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(3, obj.getHora());
					sqlAlterar.setDouble(4, obj.getValorTotal().doubleValue());
					sqlAlterar.setString(5, obj.getTipoInscricao());
					sqlAlterar.setInt(6, obj.getPessoaInscricaoCursoExtensao().getCodigo().intValue());
					sqlAlterar.setInt(7, obj.getNrInscricao().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>InscricaoCursoExtensaoVO</code>. Sempre localiza
	 * o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoCursoExtensaoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InscricaoCursoExtensaoVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle
			 * Comentado 28/10/2014
			 * Classe Subordinada
			 */
			// InscricaoCursoExtensao.excluir(getIdEntidade());
			String sql = "DELETE FROM InscricaoCursoExtensao WHERE ((nrInscricao = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getNrInscricao() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoExtensao</code> através do valor do atributo
	 * <code>String tipoInscricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoInscricao(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoExtensao WHERE tipoInscricao like('" + valorConsulta + "%') ORDER BY tipoInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoExtensao</code> através do valor do atributo
	 * <code>Double valorTotal</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorTotal(Double valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoExtensao WHERE valorTotal >= " + valorConsulta.doubleValue() + " ORDER BY valorTotal";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoExtensao</code> através do valor do atributo
	 * <code>String hora</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorHora(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoExtensao WHERE hora like('" + valorConsulta + "%') ORDER BY hora";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoExtensao</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoExtensao WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoExtensao</code> através do valor do atributo
	 * <code>Integer cursoExtensao</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCursoExtensao(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoExtensao WHERE cursoExtensao >= " + valorConsulta.intValue() + " ORDER BY cursoExtensao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoExtensao</code> através do valor do atributo
	 * <code>Integer nrInscricao</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrInscricao(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoExtensao WHERE nrInscricao >= " + valorConsulta.intValue() + " ORDER BY nrInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoExtensaoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado,usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>InscricaoCursoExtensaoVO</code>.
	 * 
	 * @return O objeto da classe <code>InscricaoCursoExtensaoVO</code> com os dados devidamente montados.
	 */
	public static InscricaoCursoExtensaoVO montarDados(SqlRowSet dadosSQL,UsuarioVO usuario) throws Exception {
		InscricaoCursoExtensaoVO obj = new InscricaoCursoExtensaoVO();
		obj.setNrInscricao(new Integer(dadosSQL.getInt("nrInscricao")));
		obj.setCursoExtensao(new Integer(dadosSQL.getInt("cursoExtensao")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setHora(dadosSQL.getString("hora"));
		obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
		obj.setTipoInscricao(dadosSQL.getString("tipoInscricao"));
		obj.getPessoaInscricaoCursoExtensao().setCodigo(new Integer(dadosSQL.getInt("pessoaInscricaoCursoExtensao")));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosPessoaInscricaoCursoExtensao(obj,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>InscricaoCursoExtensaoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoaInscricaoCursoExtensao(InscricaoCursoExtensaoVO obj,UsuarioVO usuario) throws Exception {
		if (obj.getPessoaInscricaoCursoExtensao().getCodigo().intValue() == 0) {
			obj.setPessoaInscricaoCursoExtensao(new PessoaVO());
			return;
		}
		obj.setPessoaInscricaoCursoExtensao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoaInscricaoCursoExtensao().getCodigo(), false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>InscricaoCursoExtensaoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public InscricaoCursoExtensaoVO consultarPorChavePrimaria(Integer nrInscricaoPrm,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM InscricaoCursoExtensao WHERE nrInscricao = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { nrInscricaoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado,usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return InscricaoCursoExtensao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		InscricaoCursoExtensao.idEntidade = idEntidade;
	}
}
