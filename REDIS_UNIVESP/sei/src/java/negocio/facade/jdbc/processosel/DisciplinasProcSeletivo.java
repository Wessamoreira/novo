package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.DisciplinasProcSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>DisciplinasProcSeletivoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>DisciplinasProcSeletivoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see DisciplinasProcSeletivoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class DisciplinasProcSeletivo extends ControleAcesso implements DisciplinasProcSeletivoInterfaceFacade {

	protected static String idEntidade;

	public DisciplinasProcSeletivo() throws Exception {
		super();
		setIdEntidade("DisciplinasProcSeletivo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>DisciplinasProcSeletivoVO</code>.
	 */
	public DisciplinasProcSeletivoVO novo(UsuarioVO usuario) throws Exception {
		DisciplinasProcSeletivo.incluir(getIdEntidade());
		DisciplinasProcSeletivoVO obj = new DisciplinasProcSeletivoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>DisciplinasProcSeletivoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>DisciplinasProcSeletivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			DisciplinasProcSeletivoVO.validarDados(obj);
			DisciplinasProcSeletivo.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO DisciplinasProcSeletivo( nome, tipoDisciplina, descricao, requisitos, bibliografia, disciplinaIdioma ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getTipoDisciplina());
					sqlInserir.setString(3, obj.getDescricao());
					sqlInserir.setString(4, obj.getRequisitos());
					sqlInserir.setString(5, obj.getBibliografia());
					sqlInserir.setBoolean(6, obj.getDisciplinaIdioma());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>DisciplinasProcSeletivoVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>DisciplinasProcSeletivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			DisciplinasProcSeletivoVO.validarDados(obj);
			DisciplinasProcSeletivo.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE DisciplinasProcSeletivo set nome=?, tipoDisciplina=?, descricao=?, requisitos=?, bibliografia=?, disciplinaIdioma=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getTipoDisciplina());
					sqlAlterar.setString(3, obj.getDescricao());
					sqlAlterar.setString(4, obj.getRequisitos());
					sqlAlterar.setString(5, obj.getBibliografia());
					sqlAlterar.setBoolean(6, obj.getDisciplinaIdioma());
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>DisciplinasProcSeletivoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>DisciplinasProcSeletivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			DisciplinasProcSeletivo.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM DisciplinasProcSeletivo WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>DisciplinasProcSeletivo</code> através do valor do atributo <code>String tipoDisciplina</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DisciplinasProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DisciplinasProcSeletivoVO> consultarPorTipoDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisciplinasProcSeletivo WHERE lower (tipoDisciplina) like lower(?) ORDER BY tipoDisciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr,valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado));
	}

	public List<DisciplinasProcSeletivoVO> consultarPorDisciplinasIdioma(boolean tipoDisciplinasLinguaEstrangeira, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisciplinasProcSeletivo WHERE disciplinaIdioma = " + tipoDisciplinasLinguaEstrangeira + " ORDER BY tipoDisciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado));
	}

	/**
	 * Responsável por realizar uma consulta de <code>DisciplinasProcSeletivo</code> através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DisciplinasProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DisciplinasProcSeletivoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisciplinasProcSeletivo WHERE lower  (nome) like lower(?) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado));
	}

	/**
	 * Responsável por realizar uma consulta de <code>DisciplinasProcSeletivo</code> através do valor do atributo <code>Integer codigo</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>DisciplinasProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<DisciplinasProcSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisciplinasProcSeletivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>DisciplinasProcSeletivoVO</code> resultantes da consulta.
	 */
	private List<DisciplinasProcSeletivoVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<DisciplinasProcSeletivoVO> vetResultado = new ArrayList<DisciplinasProcSeletivoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisciplinasProcSeletivoVO</code>.
	 *
	 * @return O objeto da classe <code>DisciplinasProcSeletivoVO</code> com os dados devidamente montados.
	 */
	public static DisciplinasProcSeletivoVO montarDados(SqlRowSet dadosSQL) throws Exception {
		DisciplinasProcSeletivoVO obj = new DisciplinasProcSeletivoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setTipoDisciplina(dadosSQL.getString("tipoDisciplina"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setRequisitos(dadosSQL.getString("requisitos"));
		obj.setBibliografia(dadosSQL.getString("bibliografia"));
		obj.setDisciplinaIdioma(new Boolean(dadosSQL.getBoolean("disciplinaIdioma")));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>DisciplinasProcSeletivoVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public DisciplinasProcSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DisciplinasProcSeletivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return DisciplinasProcSeletivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		DisciplinasProcSeletivo.idEntidade = idEntidade;
	}
	
	@Override
	public List<DisciplinasProcSeletivoVO> consultarPorGrupoDisciplinaProcSeletivo(Integer grupoDisciplinaProcSeletivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select disciplinasProcSeletivo.* from disciplinasProcSeletivo ");
		sqlStr.append("inner join disciplinasGrupoDisciplinaProcSeletivo on  disciplinasProcSeletivo.codigo = disciplinasGrupoDisciplinaProcSeletivo.disciplinasProcSeletivo ");
		sqlStr.append("inner join grupoDisciplinaProcSeletivo on  grupoDisciplinaProcSeletivo.codigo = disciplinasGrupoDisciplinaProcSeletivo.grupoDisciplinaProcSeletivo ");
		sqlStr.append("where grupoDisciplinaProcSeletivo.codigo = ").append(grupoDisciplinaProcSeletivo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado));
	}
	
	
	@Override
	public List<DisciplinasProcSeletivoVO> consultarPorProcSeletivo(Integer procSeletivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct disciplinasprocseletivo.* from procseletivo   ");
		sqlStr.append("inner join procseletivounidadeensino on procseletivounidadeensino.procseletivo = procseletivo.codigo ");
		sqlStr.append("inner join procseletivocurso on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ");
		sqlStr.append("inner join grupodisciplinaprocseletivo on grupodisciplinaprocseletivo.codigo = procseletivocurso.grupodisciplinaprocseletivo ");
		sqlStr.append("inner join disciplinasgrupodisciplinaprocseletivo on grupodisciplinaprocseletivo.codigo = disciplinasgrupodisciplinaprocseletivo.grupodisciplinaprocseletivo ");
		sqlStr.append("inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = disciplinasgrupodisciplinaprocseletivo.disciplinasprocseletivo ");
		sqlStr.append("where procseletivo.codigo = ").append(procSeletivo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado));
	}
	
	
}
