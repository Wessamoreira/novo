package negocio.facade.jdbc.protocolo;

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
import negocio.comuns.protocolo.TipoRequerimentoTurmaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.TipoRequerimentoTurmaInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class TipoRequerimentoTurma extends ControleAcesso implements TipoRequerimentoTurmaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TipoRequerimentoTurma() throws Exception {
		super();
		setIdEntidade("TipoRequerimentoTurma");
	}

	public TipoRequerimentoTurmaVO novo() throws Exception {
		TipoRequerimentoTurma.incluir(getIdEntidade());
		TipoRequerimentoTurmaVO obj = new TipoRequerimentoTurmaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoRequerimentoTurmaVO obj, UsuarioVO usuarioVO) throws Exception {
 		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO tiporequerimentoTurma (tiporequerimentoCurso, Turma ) values (?,?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setInt(1, obj.getTipoRequerimentoCursoVO().getCodigo());
				sqlInserir.setInt(2, obj.getTurmaVO().getCodigo());
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoRequerimentoTurmaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TipoRequerimentoTurma SET tiporequerimentoCurso=?,turma=? ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getTipoRequerimentoCursoVO().getCodigo());
					sqlAlterar.setInt(2, obj.getTurmaVO().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTipoRequerimentoTurmaVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoTurmaVO> tipoRequerimentoTurmaVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM TipoRequerimentoTurma WHERE (tiporequerimentoCurso = " + tipoRequerimentoCurso);
			sql.append(") and codigo not in(");
			for (TipoRequerimentoTurmaVO obj : tipoRequerimentoTurmaVOs) {
				sql.append(obj.getCodigo()).append(", ");
			}
			sql.append("0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTipoRequerimentoTurmaVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoTurmaVO> tipoRequerimentoTurmaVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (TipoRequerimentoTurmaVO obj : tipoRequerimentoTurmaVOs) {
				obj.getTipoRequerimentoCursoVO().setCodigo(tipoRequerimentoCurso);
				if (obj.getNovoObj()) {
					incluir(obj, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTipoRequerimentoTurmaVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoTurmaVO> tipoRequerimentoTurmaVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			this.excluirTipoRequerimentoTurmaVOs(tipoRequerimentoCurso, tipoRequerimentoTurmaVOs, usuarioVO);
			for (TipoRequerimentoTurmaVO obj : tipoRequerimentoTurmaVOs) {
				obj.getTipoRequerimentoCursoVO().setCodigo(tipoRequerimentoCurso);
				if (obj.getNovoObj()) {
					incluir(obj, usuarioVO);
				} else {
					alterar(obj, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static List<TipoRequerimentoTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<TipoRequerimentoTurmaVO> vetResultado = new ArrayList<TipoRequerimentoTurmaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}
	
	public static TipoRequerimentoTurmaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		TipoRequerimentoTurmaVO obj = new TipoRequerimentoTurmaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getTipoRequerimentoCursoVO().setCodigo(new Integer(dadosSQL.getInt("tipoRequerimentoCurso")));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	@Override
	public List<TipoRequerimentoTurmaVO> consultarTipoRequerimentoTurmaPorTipoRequerimentoCurso(Integer tipoRequerimentoCurso,UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("Select tiporequerimentoTurma.codigo, tiporequerimentoTurma.tipoRequerimentoCurso, turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\" from tiporequerimentoTurma ");
		sql.append(" inner join tipoRequerimentoCurso on tipoRequerimentoCurso.codigo =  tiporequerimentoTurma.tipoRequerimentoCurso ");
		sql.append(" inner join turma on turma.codigo = tiporequerimentoTurma.turma ");
		sql.append(" WHERE tiporequerimentoTurma.tipoRequerimentoCurso = ? ORDER BY codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), tipoRequerimentoCurso);
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TipoRequerimentoTurma.idEntidade = idEntidade;
	}
	

}
