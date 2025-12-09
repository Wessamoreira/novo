package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.ChancelaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ChancelaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Chancela extends ControleAcesso implements ChancelaInterfaceFacade {

	protected static String idEntidade;

	public Chancela() throws Exception {
		super();
		setIdEntidade("Chancela");
	}

	public ChancelaVO novo() throws Exception {
		Chancela.incluir(getIdEntidade());
		ChancelaVO obj = new ChancelaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final ChancelaVO obj) throws Exception {

		final String sql = "INSERT INTO Chancela ( instituicaoChanceladora ) VALUES ( ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				sqlInserir.setString(1, obj.getInstituicaoChanceladora());
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
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ChancelaVO obj) throws Exception {
		final String sql = "UPDATE Chancela set instituicaoChanceladora=? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getInstituicaoChanceladora());
				sqlAlterar.setInt(2, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ChancelaVO obj) throws Exception {
		Chancela.excluir(getIdEntidade());
		String sql = "DELETE FROM Chancela WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public ChancelaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Chancela  WHERE codigo = " + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new ChancelaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ChancelaVO consultarPorCodigoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
                sqlStr.append("select chancela.* from chancela ");
                sqlStr.append(" inner join turma on turma.chancela = chancela.codigo ");
                sqlStr.append(" where turma.codigo = ");
                sqlStr.append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ChancelaVO> consultarPorCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Chancela  WHERE codigo >= " + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ChancelaVO> consultarPorInstituicaoChanceladora(String instChanceladora, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Chancela  WHERE LOWER(instituicaoChanceladora) LIKE '" + instChanceladora + "%'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List<ChancelaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ChancelaVO> vetResultado = new ArrayList<ChancelaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static ChancelaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ChancelaVO obj = new ChancelaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setInstituicaoChanceladora(dadosSQL.getString("instituicaoChanceladora"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	public static String getIdEntidade() {
		return Chancela.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Chancela.idEntidade = idEntidade;
	}
}