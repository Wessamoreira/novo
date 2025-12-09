package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioTurmaDisciplinaProgramadaInterfaceFacade;

/**
 * 
 * @author rodrigo
 */
@SuppressWarnings("static-access")
@Repository
@Scope("singleton")
@Lazy
public class HorarioTurmaDisciplinaProgramada extends ControleAcesso implements HorarioTurmaDisciplinaProgramadaInterfaceFacade {

	private static final long serialVersionUID = -831506333511534467L;

	protected static String idEntidade;
	
	@Override
	public void persistir(HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(horarioTurmaDisciplinaProgramadaVO)) {
			incluir(horarioTurmaDisciplinaProgramadaVO, usuarioVO);
		} else {
			alterar(horarioTurmaDisciplinaProgramadaVO, usuarioVO);
		}
	}
	
	public void validarDados(HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(horarioTurmaDisciplinaProgramadaVO.getConteudo())) {
			throw new ConsistirException("O campo CONTEÚDO deve ser informado !");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioTurmaDisciplinaProgramadaVO obj, UsuarioVO usuario) throws Exception {
		try {
			obj.setUpdated(new Date());
			validarDados(obj, usuario);
			final String sql = "INSERT INTO HorarioTurmaDisciplinaProgramada (disciplina, horarioturma, registraraulaautomaticamente, conteudo) VALUES (?,?,?,?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getCodigoDisciplina());
					sqlInserir.setInt(2, obj.getHorarioTurmaVO().getCodigo());
					sqlInserir.setBoolean(3, obj.getRegistrarAulaAutomaticamente());
					sqlInserir.setString(4, obj.getConteudo());
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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		} finally {

		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioTurmaDisciplinaProgramadaVO obj, UsuarioVO usuario) throws Exception {
		try {
			obj.setUpdated(new Date());
			final String sql = "UPDATE HorarioTurmaDisciplinaProgramada SET disciplina=?, horarioturma = ? , registraraulaautomaticamente = ?, conteudo = ? WHERE ((codigo=?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCodigoDisciplina());
					sqlAlterar.setInt(2, obj.getHorarioTurmaVO().getCodigo());
					sqlAlterar.setBoolean(3, obj.getRegistrarAulaAutomaticamente());
					sqlAlterar.setString(4, obj.getConteudo());
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}
	
	@Override
	public HorarioTurmaDisciplinaProgramadaVO consultarPorDisciplinaHorarioTurma(HorarioTurmaDisciplinaProgramadaVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select * from horarioturmadisciplinaprogramada ");
		sqlStr.append("where disciplina = ").append(obj.getCodigoDisciplina());
		sqlStr.append(" and horarioturma = ").append(obj.getHorarioTurmaVO().getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDados(tabelaResultado, usuario));
	}
	
	public static HorarioTurmaDisciplinaProgramadaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO = new HorarioTurmaDisciplinaProgramadaVO();
		horarioTurmaDisciplinaProgramadaVO.setCodigo(dadosSQL.getInt("codigo"));
		horarioTurmaDisciplinaProgramadaVO.setCodigoDisciplina(dadosSQL.getInt("disciplina"));
		horarioTurmaDisciplinaProgramadaVO.setRegistrarAulaAutomaticamente(dadosSQL.getBoolean("registrarAulaAutomaticamente"));
		horarioTurmaDisciplinaProgramadaVO.setConteudo(dadosSQL.getString("conteudo"));
		horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().setCodigo(dadosSQL.getInt("horarioturma"));
		return horarioTurmaDisciplinaProgramadaVO;
	}
	
	
	
}