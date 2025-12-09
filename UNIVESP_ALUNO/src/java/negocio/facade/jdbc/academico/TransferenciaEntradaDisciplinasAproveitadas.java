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

import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe
 * <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see TransferenciaEntradaDisciplinasAproveitadasVO
 * @see ControleAcesso
 * @see TransferenciaEntrada
 */

@Repository
@Scope("singleton")
@Lazy 
public class TransferenciaEntradaDisciplinasAproveitadas extends ControleAcesso implements TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade {

	protected static String idEntidade;

	public TransferenciaEntradaDisciplinasAproveitadas() throws Exception {
		super();
		setIdEntidade("TransferenciaEntrada");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade#novo()
	 */
	public TransferenciaEntradaDisciplinasAproveitadasVO novo() throws Exception {
		TransferenciaEntradaDisciplinasAproveitadas.incluir(getIdEntidade());
		TransferenciaEntradaDisciplinasAproveitadasVO obj = new TransferenciaEntradaDisciplinasAproveitadasVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #incluir(negocio
	 * .comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransferenciaEntradaDisciplinasAproveitadasVO obj, String periodicidadeCurso) throws Exception {
		try {
			TransferenciaEntradaDisciplinasAproveitadasVO.validarDados(obj, periodicidadeCurso);
			final String sql = "INSERT INTO TransferenciaEntradaDisciplinasAproveitadas( disciplina, nota, frequencia, cargaHoraria, transferenciaEntrada, anoConclusaoDisciplina, semestreConclusaoDisciplina ) VALUES ( ?, ?, ?, ?, ?, ?, ?  ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setDouble(2, obj.getNota().doubleValue());
					sqlInserir.setDouble(3, obj.getFrequencia().doubleValue());
					sqlInserir.setDouble(4, obj.getCargaHoraria().doubleValue());
					if (obj.getTransferenciaEntrada().intValue() != 0) {
						sqlInserir.setInt(5, obj.getTransferenciaEntrada().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getAnoConclusaoDisciplina());
					sqlInserir.setString(7, obj.getSemestreConclusaoDisciplina());
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
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #alterar(negocio
	 * .comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TransferenciaEntradaDisciplinasAproveitadasVO obj, String periodicidadeCurso) throws Exception {
		try {
			TransferenciaEntradaDisciplinasAproveitadasVO.validarDados(obj, periodicidadeCurso);
			final String sql = "UPDATE TransferenciaEntradaDisciplinasAproveitadas set disciplina=?, nota=?, frequencia=?, cargaHoraria=?, transferenciaEntrada=?, anoConclusaoDisciplina=?, semestreConclusaoDisciplina=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getDisciplina().getCodigo().intValue());
					sqlAlterar.setDouble(2, obj.getNota().doubleValue());
					sqlAlterar.setDouble(3, obj.getFrequencia().doubleValue());
					sqlAlterar.setDouble(4, obj.getCargaHoraria().doubleValue());
					if (obj.getTransferenciaEntrada().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getTransferenciaEntrada().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getAnoConclusaoDisciplina());
					sqlAlterar.setString(7, obj.getSemestreConclusaoDisciplina());
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) ;
		} catch (Exception e) {
			throw e;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #excluir(negocio
	 * .comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO)
	 */
	public void excluir(TransferenciaEntradaDisciplinasAproveitadasVO obj) throws Exception {
		try {
			TransferenciaEntradaDisciplinasAproveitadas.excluir(getIdEntidade());
			String sql = "DELETE FROM TransferenciaEntradaDisciplinasAproveitadas WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorDescricaoTransferenciaEntrada(java.lang.String, int)
	 */
	public List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarPorDescricaoTransferenciaEntrada(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT TransferenciaEntradaDisciplinasAproveitadas.* FROM TransferenciaEntradaDisciplinasAproveitadas, TransferenciaEntrada WHERE TransferenciaEntradaDisciplinasAproveitadas.transferenciaEntrada = TransferenciaEntrada.codigo and upper( TransferenciaEntrada.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY TransferenciaEntrada.descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorCargaHoraria(java.lang.Double, boolean, int)
	 */
	public List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarPorCargaHoraria(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE cargaHoraria >= " + valorConsulta.doubleValue() + " ORDER BY cargaHoraria";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorFrequencia(java.lang.Double, boolean, int)
	 */
	public List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarPorFrequencia(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE frequencia >= " + valorConsulta.doubleValue() + " ORDER BY frequencia";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorNota(java.lang.Double, boolean, int)
	 */
	public List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarPorNota(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE nota >= " + valorConsulta.doubleValue() + " ORDER BY nota";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorDisciplinaETransferencia(java.lang.Integer,
	 * java.lang.Integer, boolean, int)
	 */
	public List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarPorDisciplinaETransferencia(Integer valorConsulta, Integer transferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE disciplina = " + valorConsulta.intValue() + " and transferenciaEntrada = " + transferencia + " ORDER BY disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorCodigo(java.lang.Integer, boolean, int)
	 */
	public List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>
	 *         resultantes da consulta.
	 */
	public  List<TransferenciaEntradaDisciplinasAproveitadasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			TransferenciaEntradaDisciplinasAproveitadasVO obj = new TransferenciaEntradaDisciplinasAproveitadasVO();
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
	 * 
	 * @return O objeto da classe
	 *         <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> com os
	 *         dados devidamente montados.
	 */
	public  TransferenciaEntradaDisciplinasAproveitadasVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TransferenciaEntradaDisciplinasAproveitadasVO obj = new TransferenciaEntradaDisciplinasAproveitadasVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setNota(new Double(dadosSQL.getDouble("nota")));
		obj.setFrequencia(new Double(dadosSQL.getDouble("frequencia")));
		obj.setCargaHoraria(new Double(dadosSQL.getDouble("cargaHoraria")));
		obj.setTransferenciaEntrada(new Integer(dadosSQL.getInt("transferenciaEntrada")));
		obj.setAnoConclusaoDisciplina(dadosSQL.getString("anoConclusaoDisciplina"));
		obj.setSemestreConclusaoDisciplina(dadosSQL.getString("semestreConclusaoDisciplina"));		
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosDisciplina(obj, nivelMontarDados, usuario);
		return obj;
	}

	public  void montarDadosDisciplina(TransferenciaEntradaDisciplinasAproveitadasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #excluirTransferenciaEntradaDisciplinasAproveitadass(java.lang.Integer)
	 */
	public void excluirTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada) throws Exception {
		try {
			TransferenciaEntradaDisciplinasAproveitadas.excluir(getIdEntidade());
			String sql = "DELETE FROM TransferenciaEntradaDisciplinasAproveitadas WHERE (transferenciaEntrada = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { transferenciaEntrada });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #excluirTransferenciaEntradaDisciplinasAproveitadass(java.lang.Integer,
	 * java.util.List)
	 */
	public void excluirTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada, String periodicidadeCurso, List objetos) throws Exception {
		try {
			TransferenciaEntradaDisciplinasAproveitadas.excluir(getIdEntidade());
			String sql = "DELETE FROM TransferenciaEntradaDisciplinasAproveitadas WHERE (transferenciaEntrada = ?)";
			Iterator i = objetos.iterator();
			while (i.hasNext()) {
				TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
				if (obj.getCodigo().intValue() != 0) {
					sql += " and codigo != " + obj.getCodigo().intValue();
				}
			}
			getConexao().getJdbcTemplate().update(sql, new Object[] { transferenciaEntrada });
			Iterator e = objetos.iterator();
			while (e.hasNext()) {
				TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) e.next();
				obj.setTransferenciaEntrada(transferenciaEntrada);
				if (obj.getCodigo().intValue() == 0) {
					incluir(obj, periodicidadeCurso);
				} else {
					alterar(obj, periodicidadeCurso);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #alterarTransferenciaEntradaDisciplinasAproveitadass(java.lang.Integer,
	 * java.util.List)
	 */
	public void alterarTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada, String periodicidadeCurso, List objetos) throws Exception {
		try {
			TransferenciaEntradaDisciplinasAproveitadas.excluir(getIdEntidade());
			String sql = "DELETE FROM TransferenciaEntradaDisciplinasAproveitadas WHERE (transferenciaEntrada = ?)";
			Iterator i = objetos.iterator();
			while (i.hasNext()) {
				TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
				if (obj.getCodigo().intValue() != 0) {
					sql += " and codigo != " + obj.getCodigo().intValue();
				}
			}
			getConexao().getJdbcTemplate().update(sql, new Object[] { transferenciaEntrada });
			Iterator e = objetos.iterator();
			while (e.hasNext()) {
				TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) e.next();
				obj.setTransferenciaEntrada(transferenciaEntrada);
				if (obj.getCodigo().intValue() == 0) {
					incluir(obj, periodicidadeCurso);
				} else {
					alterar(obj, periodicidadeCurso);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #incluirTransferenciaEntradaDisciplinasAproveitadass(java.lang.Integer,
	 * java.util.List)
	 */
	public void incluirTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntradaPrm, String periodicidadeCurso, List objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) e.next();
			obj.setTransferenciaEntrada(transferenciaEntradaPrm);
			incluir(obj, periodicidadeCurso);
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> relacionados a
	 * um objeto da classe <code>academico.TransferenciaEntrada</code>.
	 * 
	 * @param transferenciaEntrada
	 *            Atributo de <code>academico.TransferenciaEntrada</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public  List<TransferenciaEntradaDisciplinasAproveitadasVO> consultarTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TransferenciaEntradaDisciplinasAproveitadas.consultar(getIdEntidade());
		List<TransferenciaEntradaDisciplinasAproveitadasVO> objetos = new ArrayList<TransferenciaEntradaDisciplinasAproveitadasVO>(0);
		String sql = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE transferenciaEntrada = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { transferenciaEntrada} );
		while (tabelaResultado.next()) {
			TransferenciaEntradaDisciplinasAproveitadasVO novoObj = new TransferenciaEntradaDisciplinasAproveitadasVO();
			novoObj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public TransferenciaEntradaDisciplinasAproveitadasVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM TransferenciaEntradaDisciplinasAproveitadas WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm} );
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TransferenciaEntradaDisciplinasAproveitadas ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TransferenciaEntradaDisciplinasAproveitadas.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.
	 * TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade
	 * #setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		TransferenciaEntradaDisciplinasAproveitadas.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTransferenciaEntradaDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(final Integer codigo, final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE transferenciaentradadisciplinasaproveitadas set nota=?, frequencia=?, cargaHoraria=?, anoConclusaoDisciplina=?, semestreConclusaoDisciplina=? WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setDouble(++i, obj.getMedia());
					sqlAlterar.setDouble(++i, obj.getFrequencia());
					sqlAlterar.setInt(++i, obj.getCargaHoraria());
					sqlAlterar.setString(++i, obj.getAno());
					sqlAlterar.setString(++i, obj.getSemestre());
					sqlAlterar.setInt(++i, obj.getCodigoOrigem());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
}