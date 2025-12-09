package relatorio.negocio.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import relatorio.negocio.comuns.academico.ParametroRelatorioVO;
import relatorio.negocio.interfaces.academico.ParametroRelatorioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public  class ParametroRelatorio extends ControleAcesso implements ParametroRelatorioInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ParametroRelatorio() throws Exception {
		setIdEntidade("ParametroRelatorio");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ParametroRelatorioVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO ParametroRelatorio (entidade, campo, apresentarCampo) VALUES (?, ?, ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getEntidade());
					sqlInserir.setString(2, obj.getCampo());
					sqlInserir.setBoolean(3, obj.getApresentarCampo());
					return sqlInserir;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ParametroRelatorioVO obj) throws Exception {
		try {
			final String sql = "UPDATE ParametroRelatorio set entidade = ?, campo = ?, apresentarCampo = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getEntidade());
					sqlAlterar.setString(2, obj.getCampo());
					sqlAlterar.setBoolean(3, obj.getApresentarCampo());
					sqlAlterar.setInt(4, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ParametroRelatorioVO obj) throws Exception {
		try {
			String sql = "DELETE FROM ParametroRelatorio WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ParametroRelatorioVO consultarPorEntidadeCampo(String entidade, String campo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ParametroRelatorio WHERE  entidade = ? and campo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { entidade, campo });
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado);
		}
		return new ParametroRelatorioVO();
	}

	public static ParametroRelatorioVO montarDados(SqlRowSet dadosSQL) throws Exception {
		ParametroRelatorioVO obj = new ParametroRelatorioVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setEntidade(dadosSQL.getString("entidade"));
		obj.setApresentarCampo(dadosSQL.getBoolean("apresentarCampo"));
		obj.setCampo(dadosSQL.getString("campo"));
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirParametroRelatorio(String entidade, String campo, boolean apresentarCampo, UsuarioVO usuario) throws Exception {
		ParametroRelatorioVO obj = consultarPorEntidadeCampo(entidade, campo, false, usuario);
		if (obj.getEntidade().equals("")) {
			obj.setEntidade(entidade);
			obj.setApresentarCampo(apresentarCampo);
			obj.setCampo(campo);
			incluir(obj);
		} else {
			obj.setEntidade(entidade);
			obj.setApresentarCampo(apresentarCampo);
			obj.setCampo(campo);
			alterar(obj);
		}
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ParametroRelatorio.idEntidade = idEntidade;
	}

	

}
