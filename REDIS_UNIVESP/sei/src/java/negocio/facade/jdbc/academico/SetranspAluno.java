package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.SetranspAlunoVO;
import negocio.comuns.academico.SetranspVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.SetranspAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>SetranspAlunoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>SetranspAlunoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see SetranspAlunoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class SetranspAluno extends ControleAcesso implements SetranspAlunoInterfaceFacade {

	protected static String idEntidade;

	public SetranspAluno() throws Exception {
		super();
		setIdEntidade("SetranspAluno");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#novo()
	 */
	public SetranspAlunoVO novo() throws Exception {
		SetranspAluno.incluir(getIdEntidade());
		SetranspAlunoVO obj = new SetranspAlunoVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#incluir(negocio.comuns.academico.SetranspAlunoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SetranspAlunoVO obj) throws Exception {
		try {
			SetranspAlunoVO.validarDados(obj);
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */ 
			// SetranspAluno.incluir(getIdEntidade());
			obj.realizarUpperCaseDados();

			final String sql = "INSERT INTO SetranspAluno( setransp, matriculaPeriodo ) VALUES ( ?, ? ) returning codigo";

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getSetransp().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getSetransp().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getMatriculaPeriodo() != null && obj.getMatriculaPeriodo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getMatriculaPeriodo().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
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

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#alterar(negocio.comuns.academico.SetranspAlunoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final SetranspAlunoVO obj) throws Exception {
		try {
			SetranspAlunoVO.validarDados(obj);
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */ 
			// SetranspAluno.alterar(getIdEntidade());
			obj.realizarUpperCaseDados();

			final String sql = "UPDATE SetranspAluno set setransp=?, matriculaPeriodo=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getSetransp().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getSetransp().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getMatriculaPeriodo() != null && obj.getMatriculaPeriodo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getMatriculaPeriodo().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#excluir(negocio.comuns.academico.SetranspAlunoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(SetranspAlunoVO obj) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */ 
			// SetranspAluno.excluir(getIdEntidade());
			String sql = "DELETE FROM SetranspAluno WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#consultarPorMatriculaMatricula(java.lang.String,
	 * int)
	 */
	public List consultarPorMatriculaMatricula(String valorConsulta, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT SetranspAluno.* FROM SetranspAluno, Matricula WHERE SetranspAluno.matricula = Matricula.matricula and upper( Matricula.matricula ) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados,configuracaoFinanceiroVO, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#consultarPorCodigoSetransp(java.lang.Integer,
	 * int)
	 */
	public List consultarPorCodigoSetransp(Integer valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT SetranspAluno.* FROM SetranspAluno, Setransp WHERE SetranspAluno.setransp = Setransp.codigo and Setransp.codigo >= " + valorConsulta.intValue()
				+ " ORDER BY Setransp.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados,configuracaoFinanceiroVO, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean,
	 * int)
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM SetranspAluno WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>SetranspAlunoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,configuracaoFinanceiroVO, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>SetranspAlunoVO</code>.
	 * 
	 * @return O objeto da classe <code>SetranspAlunoVO</code> com os dados devidamente montados.
	 */
	public static SetranspAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		SetranspAlunoVO obj = new SetranspAlunoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getSetransp().setCodigo(new Integer(dadosSQL.getInt("setransp")));
		obj.getMatriculaPeriodo().setCodigo(dadosSQL.getInt("matriculaperiodo"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosSetransp(obj, nivelMontarDados, usuario);
		montarDadosMatriculaPeriodo(obj, nivelMontarDados,configuracaoFinanceiroVO, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto
	 * <code>SetranspAlunoVO</code>. Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatriculaPeriodo(SetranspAlunoVO obj, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if ((obj.getMatriculaPeriodo().getCodigo() == null) || (obj.getMatriculaPeriodo().getCodigo().intValue() == 0)) {
			obj.setMatriculaPeriodo(new MatriculaPeriodoVO());
			return;
		}
		obj.setMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodo().getCodigo(), nivelMontarDados,configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>SetranspVO</code> relacionado ao objeto
	 * <code>SetranspAlunoVO</code>. Faz uso da chave primária da classe <code>SetranspVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosSetransp(SetranspAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSetransp().getCodigo().intValue() == 0) {
			obj.setSetransp(new SetranspVO());
			return;
		}
		obj.setSetransp(getFacadeFactory().getSetranspFacade().consultarPorChavePrimaria(obj.getSetransp().getCodigo(), nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#excluirSetranspAluno(java.lang.Integer)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSetranspAluno(Integer setransp) throws Exception {
		SetranspAluno.excluir(getIdEntidade());
		String sql = "DELETE FROM setranspAluno WHERE (setransp = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { setransp });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#alterarSetranspAluno(java.lang.Integer,
	 * java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSetranspAluno(Integer setransp, List objetos) throws Exception {
		String str = "DELETE FROM setranspAluno WHERE setransp = " + setransp;
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			SetranspAlunoVO objeto = (SetranspAlunoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			SetranspAlunoVO objeto = (SetranspAlunoVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				incluir(objeto);
			} else {
				alterar(objeto);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#incluirSetranspAluno(java.lang.Integer,
	 * java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSetranspAluno(Integer setranspPrm, List objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			SetranspAlunoVO obj = (SetranspAlunoVO) e.next();
			obj.getSetransp().setCodigo(setranspPrm);
			incluir(obj);
		}
	}

	public static List consultarSetranspAluno(Integer setransp, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		SetranspAluno.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM setranspAluno WHERE setransp = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { setransp });
		SetranspAlunoVO novoObj = null;
		while (resultado.next()) {
			objetos.add(montarDados(resultado, nivelMontarDados,configuracaoFinanceiroVO, usuario));
		}
		return objetos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public SetranspAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM SetranspAluno WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( SetranspAluno ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados,configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return SetranspAluno.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.SetranspAlunoInterfaceFacade#setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		SetranspAluno.idEntidade = idEntidade;
	}
}