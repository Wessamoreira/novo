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
import negocio.comuns.eventos.TrabalhoSubmetidoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.TrabalhoSubmetidoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>TrabalhoSubmetidoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>TrabalhoSubmetidoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TrabalhoSubmetidoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class TrabalhoSubmetido extends ControleAcesso implements TrabalhoSubmetidoInterfaceFacade {

	protected static String idEntidade;

	public TrabalhoSubmetido() throws Exception {
		super();
		setIdEntidade("TrabalhoSubmetido");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TrabalhoSubmetidoVO</code>.
	 */
	public TrabalhoSubmetidoVO novo() throws Exception {
		TrabalhoSubmetido.incluir(getIdEntidade());
		TrabalhoSubmetidoVO obj = new TrabalhoSubmetidoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TrabalhoSubmetidoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TrabalhoSubmetidoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TrabalhoSubmetidoVO obj) throws Exception {
		try {
			TrabalhoSubmetido.incluir(getIdEntidade());
			TrabalhoSubmetidoVO.validarDados(obj);
			final String sql = "INSERT INTO TrabalhoSubmetido( evento, dataSubmissao, titulo, introducao, metodologia, resultado, conclusao, palavrasChave, parecerComissao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getEvento().intValue());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataSubmissao()));
					sqlInserir.setString(3, obj.getTitulo());
					sqlInserir.setString(4, obj.getIntroducao());
					sqlInserir.setString(5, obj.getMetodologia());
					sqlInserir.setString(6, obj.getResultado());
					sqlInserir.setString(7, obj.getConclusao());
					sqlInserir.setString(8, obj.getPalavrasChave());
					sqlInserir.setString(9, obj.getParecerComissao());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TrabalhoSubmetidoVO</code>. Sempre
	 * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TrabalhoSubmetidoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TrabalhoSubmetidoVO obj) throws Exception {
		try {
			TrabalhoSubmetido.alterar(getIdEntidade());
			TrabalhoSubmetidoVO.validarDados(obj);
			final String sql = "UPDATE TrabalhoSubmetido set evento=?, dataSubmissao=?, titulo=?, introducao=?, metodologia=?, resultado=?, conclusao=?, palavrasChave=?, parecerComissao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getEvento().intValue());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataSubmissao()));
					sqlAlterar.setString(3, obj.getTitulo());
					sqlAlterar.setString(4, obj.getIntroducao());
					sqlAlterar.setString(5, obj.getMetodologia());
					sqlAlterar.setString(6, obj.getResultado());
					sqlAlterar.setString(7, obj.getConclusao());
					sqlAlterar.setString(8, obj.getPalavrasChave());
					sqlAlterar.setString(9, obj.getParecerComissao());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TrabalhoSubmetidoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TrabalhoSubmetidoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TrabalhoSubmetidoVO obj) throws Exception {
		try {
			TrabalhoSubmetido.excluir(getIdEntidade());
			String sql = "DELETE FROM TrabalhoSubmetido WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>TrabalhoSubmetido</code> através do valor do atributo
	 * <code>Date dataSubmissao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataSubmissao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TrabalhoSubmetido WHERE ((dataSubmissao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataSubmissao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataSubmissao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TrabalhoSubmetido</code> através do valor do atributo
	 * <code>Integer evento</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorEvento(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TrabalhoSubmetido WHERE evento >= " + valorConsulta.intValue() + " ORDER BY evento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TrabalhoSubmetido</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TrabalhoSubmetido WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TrabalhoSubmetidoVO</code> resultantes da consulta.
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
	 * objeto da classe <code>TrabalhoSubmetidoVO</code>.
	 * 
	 * @return O objeto da classe <code>TrabalhoSubmetidoVO</code> com os dados devidamente montados.
	 */
	public static TrabalhoSubmetidoVO montarDados(SqlRowSet dadosSQL,UsuarioVO usuario) throws Exception {
		TrabalhoSubmetidoVO obj = new TrabalhoSubmetidoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setEvento(new Integer(dadosSQL.getInt("evento")));
		obj.setDataSubmissao(dadosSQL.getDate("dataSubmissao"));
		obj.setTitulo(dadosSQL.getString("titulo"));
		obj.setIntroducao(dadosSQL.getString("introducao"));
		obj.setMetodologia(dadosSQL.getString("metodologia"));
		obj.setResultado(dadosSQL.getString("resultado"));
		obj.setConclusao(dadosSQL.getString("conclusao"));
		obj.setPalavrasChave(dadosSQL.getString("palavrasChave"));
		obj.setParecerComissao(dadosSQL.getString("parecerComissao"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setAutorTrabalhoSubmetidoVOs(AutorTrabalhoSubmetido.consultarAutorTrabalhoSubmetidos(obj.getCodigo(),usuario));
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>TrabalhoSubmetidoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public TrabalhoSubmetidoVO consultarPorChavePrimaria(Integer codigoPrm,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM TrabalhoSubmetido WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
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
		return TrabalhoSubmetido.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TrabalhoSubmetido.idEntidade = idEntidade;
	}
}
