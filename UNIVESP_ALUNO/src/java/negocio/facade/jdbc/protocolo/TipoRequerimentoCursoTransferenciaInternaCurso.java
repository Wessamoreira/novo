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
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoTurmaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.TipoRequerimentoCursoTransferenciaInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class TipoRequerimentoCursoTransferenciaInternaCurso extends ControleAcesso implements  TipoRequerimentoCursoTransferenciaInterfaceFacade{

	
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;
	

	public TipoRequerimentoCursoTransferenciaInternaCurso() throws Exception {
		super();
		setIdEntidade("TipoRequerimentoCursoTransferenciaInternaCurso");
	}
	
	public TipoRequerimentoCursoTransferenciaInternaCursoVO novo() throws Exception {
		TipoRequerimentoCursoTransferenciaInternaCurso.incluir(getIdEntidade());
		TipoRequerimentoCursoTransferenciaInternaCursoVO obj = new TipoRequerimentoCursoTransferenciaInternaCursoVO();
		return obj;
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

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoRequerimentoCursoTransferenciaInternaCursoVO obj, UsuarioVO usuarioVO) throws Exception {
 		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO TipoRequerimentoCursoTransferenciaCurso (tiporequerimentoCurso, curso ) values (?,?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setInt(1, obj.getTipoRequerimentoCursoVO().getCodigo());
				sqlInserir.setInt(2, obj.getCursoVO().getCodigo());
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
	public void alterar(final TipoRequerimentoCursoTransferenciaInternaCursoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TipoRequerimentoCursoTransferenciaCurso SET tiporequerimentoCurso=?, curso=? ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getTipoRequerimentoCursoVO().getCodigo());
					sqlAlterar.setInt(2, obj.getCursoVO().getCodigo());
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
	public void incluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(Integer tipoRequerimentoCurso,
			List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoCursoTransInternaVOs,
			UsuarioVO usuarioVO) throws Exception {
		try {
			for (TipoRequerimentoCursoTransferenciaInternaCursoVO obj : tipoRequerimentoCursoTransInternaVOs) {
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
	public void alterarTipoRequerimentoCursoTransferenciaInternaCursoVOs(Integer tipoRequerimentoCurso,
			List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoCursoTransInternaVOs,
			UsuarioVO usuarioVO) throws Exception {
		try {
			this.excluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(tipoRequerimentoCurso, tipoRequerimentoCursoTransInternaVOs, usuarioVO);
			for (TipoRequerimentoCursoTransferenciaInternaCursoVO obj : tipoRequerimentoCursoTransInternaVOs) {
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

	@Override
	public List<TipoRequerimentoCursoTransferenciaInternaCursoVO> consultarTipoRequerimentoCursoTransferenciaInternaCursoPorTipoRequerimentoCurso(
			Integer tipoRequerimentoCurso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("Select TipoRequerimentoCursoTransferenciaCurso.codigo, TipoRequerimentoCursoTransferenciaCurso.tipoRequerimentoCurso, curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\" from TipoRequerimentoCursoTransferenciaCurso ");
		sql.append(" inner join tipoRequerimentoCurso on tipoRequerimentoCurso.codigo =  TipoRequerimentoCursoTransferenciaCurso.tipoRequerimentoCurso ");
		sql.append(" inner join curso on curso.codigo = TipoRequerimentoCursoTransferenciaCurso.curso ");
		sql.append(" WHERE TipoRequerimentoCursoTransferenciaCurso.tipoRequerimentoCurso = ? ORDER BY codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), tipoRequerimentoCurso);
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(Integer tipoRequerimentoCurso,
			List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoCursoTransInternaVOs,
			UsuarioVO usuarioVO) throws Exception {
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM TipoRequerimentoCursoTransferenciaCurso WHERE (tiporequerimentoCurso = " + tipoRequerimentoCurso);
			sql.append(") and codigo not in(");
			for (TipoRequerimentoCursoTransferenciaInternaCursoVO obj : tipoRequerimentoCursoTransInternaVOs) {
				sql.append(obj.getCodigo()).append(", ");
			}
			sql.append("0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
		
		
	}
	
	public static List<TipoRequerimentoCursoTransferenciaInternaCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<TipoRequerimentoCursoTransferenciaInternaCursoVO> vetResultado = new ArrayList<TipoRequerimentoCursoTransferenciaInternaCursoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}
	
	public static TipoRequerimentoCursoTransferenciaInternaCursoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		TipoRequerimentoCursoTransferenciaInternaCursoVO obj = new TipoRequerimentoCursoTransferenciaInternaCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getTipoRequerimentoCursoVO().setCodigo(new Integer(dadosSQL.getInt("tipoRequerimentoCurso")));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	
}
