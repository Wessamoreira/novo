package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.DisciplinaAbonoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TurmaVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TurmaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TurmaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DisciplinaAbono extends ControleAcesso implements DisciplinaAbonoInterfaceFacade {

	protected static String idEntidade;

	public DisciplinaAbono() throws Exception {
		super();
		setIdEntidade("AbonoFalta");
	}

	public DisciplinaAbonoVO novo() throws Exception {
		DisciplinaAbono.incluir(getIdEntidade());
		DisciplinaAbonoVO obj = new DisciplinaAbonoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DisciplinaAbonoVO obj, UsuarioVO usuario) throws Exception {
		try {
			DisciplinaAbonoVO.validarDados(obj);
			/**
			 * @author Leonardo Riciolle Comentado 27/10/2014 Classe onde e
			 *         chamada já faz a verificacao de permissao do UsuarioVO
			 */
			// DisciplinaAbono.incluir(getIdEntidade());
			final String sql = "INSERT INTO DisciplinaAbono( faltaAbonada, matricula, disciplina, abonoFalta, registroAula, faltaJustificada ) VALUES ( ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setBoolean(1, obj.getFaltaAbonada());
					sqlInserir.setString(2, obj.getMatricula().getMatricula());
					if (obj.getDisciplina().getCodigo() != 0) {
						sqlInserir.setInt(3, obj.getDisciplina().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getAbonoFalta() != 0) {
						sqlInserir.setInt(4, obj.getAbonoFalta());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getRegistroAula().getCodigo() != 0) {
						sqlInserir.setInt(5, obj.getRegistroAula().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setBoolean(6, obj.getFaltaJustificada());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DisciplinaAbonoVO obj, UsuarioVO usuario) throws Exception {
		try {
			DisciplinaAbonoVO.validarDados(obj);
			/**
			 * @author Leonardo Riciolle Comentado 27/10/2014 Classe onde e
			 *         chamada já faz a verificacao de permissao do UsuarioVO
			 */
			// DisciplinaAbono.alterar(getIdEntidade());
			final String sql = "UPDATE DisciplinaAbono set faltaAbonada=?, matricula=?, disciplina=?, abonoFalta=?, registroAula=?, faltaJustificada=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getFaltaAbonada().booleanValue());
					sqlAlterar.setString(2, obj.getMatricula().getMatricula());
					if (obj.getDisciplina().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getDisciplina().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (obj.getAbonoFalta().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getAbonoFalta());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getRegistroAula().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getRegistroAula().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setBoolean(6, obj.getFaltaJustificada().booleanValue());
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DisciplinaAbonoVO obj, UsuarioVO usuario) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle Comentado 27/10/2014 Classe onde e
			 *         chamada já faz a verificacao de permissao do UsuarioVO
			 */
			// DisciplinaAbono.excluir(getIdEntidade());
			String sql = "DELETE FROM DisciplinaAbono WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<DisciplinaAbonoVO> consultarProAbonoFalta(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws SQLException {
		try {
			String sqlStr = "SELECT * FROM DisciplinaAbono WHERE abonofalta = " + valorConsulta + " ORDER BY abonofalta";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			return new ArrayList(0);
		}
	}

	public List consultarPorDisciplinaRegistroAulaMatricula(Integer disciplina, Integer registroAula, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sqlStr = "SELECT * FROM DisciplinaAbono WHERE disciplina = ? AND registroaula = ? AND matricula = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { disciplina, registroAula, matricula });
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			return new ArrayList(0);
		}
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TurmaVO</code>.
	 * 
	 * @return O objeto da classe <code>TurmaVO</code> com os dados devidamente
	 *         montados.
	 */
	public static DisciplinaAbonoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		DisciplinaAbonoVO obj = new DisciplinaAbonoVO();
		obj.setFaltaAbonada(dadosSQL.getBoolean("faltaAbonada"));
		obj.setFaltaJustificada(dadosSQL.getBoolean("faltaJustificada"));
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina"));
		obj.setAbonoFalta(dadosSQL.getInt("abonofalta"));
		;
		obj.getRegistroAula().setCodigo(dadosSQL.getInt("registroAula"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosDisciplina(obj, nivelMontarDados, usuario);
		montarDadosRegistroAula(obj, nivelMontarDados, usuario);
		return obj;
	}

	public static void montarDadosMatricula(DisciplinaAbonoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getMatricula().getMatricula().equals("")) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	public static void montarDadosDisciplina(DisciplinaAbonoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosRegistroAula(DisciplinaAbonoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRegistroAula().getCodigo().intValue() == 0) {
			obj.setRegistroAula(new RegistroAulaVO());
			return;
		}
		obj.setRegistroAula(getFacadeFactory().getRegistroAulaFacade().consultarPorChavePrimaria(obj.getRegistroAula().getCodigo(), nivelMontarDados, usuario));
	}

	public DisciplinaAbonoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DisciplinaAbono WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return DisciplinaAbono.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		DisciplinaAbono.idEntidade = idEntidade;
	}
	
	public DisciplinaAbonoVO consultarDisciplinaAbonoPorRegistroAula(Integer registroAula, String matricula, String horario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(); 
		sqlStr.append("select da.* from registroaula ra ");
		sqlStr.append("inner join disciplinaabono da on da.registroaula = ra.codigo ");
		sqlStr.append("left join frequenciaaula fa on fa.registroaula = ra.codigo ");
		sqlStr.append("where ra.codigo = ? and fa.matricula = ? and ra.horario = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), registroAula, matricula, horario);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		} else {
			return new DisciplinaAbonoVO();
		}
	};
	
}
