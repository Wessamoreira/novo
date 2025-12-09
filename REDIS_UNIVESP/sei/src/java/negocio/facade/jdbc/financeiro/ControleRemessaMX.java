package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import negocio.comuns.financeiro.ControleRemessaMXVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleRemessaMXInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
@SuppressWarnings("unchecked")
public class ControleRemessaMX extends ControleAcesso implements ControleRemessaMXInterfaceFacade {

	private static final long serialVersionUID = -3016443458819546347L;

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ControleRemessaMXVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO controleremessamx ( contaCorrente, incremental, incrementalCP) VALUES (?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getContaCorrenteVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setInt(2, obj.getIncremental());
					sqlInserir.setInt(3, obj.getIncrementalCP());

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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarIncrementalPorContaCorrente(final Integer contaCorrente, final Integer incremental, final Integer incrementalCP, UsuarioVO usuario) throws Exception {
		try {
			if ((incremental != null && incremental > 0) || (incrementalCP != null && incrementalCP > 0)) {
				String sql = "UPDATE controleremessamx SET ";
				Boolean virgula = false;
				if (incremental != null && incremental > 0) {
					sql += "incremental=?";
					virgula = true;
				} 
				if (incrementalCP != null && incrementalCP > 0) {
					if (virgula) {
						sql += ", ";
					}
					sql += "incrementalCP=?";
				}
				sql += " WHERE contacorrente = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				final String sqlFinal = sql;
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
	
					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sqlFinal);
						int i = 0;
						if (incremental != null && incremental > 0) {
							sqlAlterar.setInt(++i, incremental);
						}
						if (incrementalCP != null && incrementalCP > 0) {
							sqlAlterar.setInt(++i, incrementalCP);
						}
						sqlAlterar.setInt(++i, contaCorrente.intValue());
						return sqlAlterar;
					}
				});
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public Integer consultarIncrementalPorContaCorrente(Integer contaCorrente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT incremental FROM controleremessamx WHERE contacorrente = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaCorrente);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("incremental");
		}
		inicializarDadosControleRemessaMX(contaCorrente, usuarioVO);
		return 1;
	}

	public Integer consultarIncrementalCPPorContaCorrente(Integer contaCorrente, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT incrementalcp FROM controleremessamx WHERE contacorrente = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaCorrente);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("incrementalcp");
		}
		inicializarDadosControleRemessaMX(contaCorrente, usuarioVO);
		return 1;
	}

	public void inicializarDadosControleRemessaMX(Integer contaCorrente, UsuarioVO usuarioVO) throws Exception {
		ControleRemessaMXVO obj = new ControleRemessaMXVO();
		obj.getContaCorrenteVO().setCodigo(contaCorrente);
		obj.setIncremental(1);
		incluir(obj);
	}

	public void incluirControleRemessaMXContaCorrente(Integer contaCorrente, ControleRemessaMXVO obj, UsuarioVO usuarioVO) throws Exception {
		obj.getContaCorrenteVO().setCodigo(contaCorrente);
		incluir(obj);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarControleRemessaMXContaCorrente(Integer contaCorrente, ControleRemessaMXVO obj, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj)) {
			obj.getContaCorrenteVO().setCodigo(contaCorrente);
			incluir(obj);
		} else {
			alterarIncrementalPorContaCorrente(contaCorrente, obj.getIncremental(), obj.getIncrementalCP(), usuarioVO);
		}
	}

	public ControleRemessaMXVO consultarPorContaCorrente(Integer contaCorrente, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM controleremessamx WHERE contacorrente = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaCorrente);
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuarioVO);
		}
		return new ControleRemessaMXVO();
	}

	public ControleRemessaMXVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleRemessaMXVO obj = new ControleRemessaMXVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contacorrente"));
		obj.setIncremental(dadosSQL.getInt("incremental"));
		obj.setIncrementalCP(dadosSQL.getInt("incrementalCP"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

}
