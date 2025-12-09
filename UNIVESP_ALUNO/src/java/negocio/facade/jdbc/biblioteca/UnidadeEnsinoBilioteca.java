package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.UnidadeEnsinoBibliotecaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoBilioteca extends ControleAcesso implements UnidadeEnsinoBibliotecaInterfaceFacade {

	protected static String idEntidade;

	public UnidadeEnsinoBilioteca() throws Exception {
		super();
		setIdEntidade("Biblioteca");
	}

	public UnidadeEnsinoBibliotecaVO novo() throws Exception {
		UnidadeEnsinoBilioteca.incluir(getIdEntidade());
		UnidadeEnsinoBibliotecaVO obj = new UnidadeEnsinoBibliotecaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoBibliotecaVO obj) throws Exception {

		final String sql = "INSERT INTO UnidadeEnsinoBiblioteca( unidadeEnsino, biblioteca ) VALUES ( ?, ? ) ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo());
				sqlInserir.setDouble(2, obj.getBiblioteca());
				return sqlInserir;
			}
		});
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UnidadeEnsinoBibliotecaVO obj) throws Exception {
		final String sql = "UPDATE UnidadeEnsinoBiblioteca set unidadeEnsino=?, biblioteca=?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo());
				sqlAlterar.setDouble(2, obj.getBiblioteca());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoBibliotecaVO obj) throws Exception {
		UnidadeEnsinoBilioteca.excluir(getIdEntidade());
		String sql = "DELETE FROM UnidadeEnsinoBiblioteca WHERE unidadeEnsino = ? AND biblioteca = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getUnidadeEnsino().getCodigo(), obj.getBiblioteca() });
	}

	public List<UnidadeEnsinoBibliotecaVO> consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ueb.*, ue.nome as nomeunidadeensino, ue.abreviatura, ue.matriz FROM UnidadeEnsinoBiblioteca ueb " + "INNER JOIN unidadeensino ue ON ueb.unidadeensino = ue.codigo " + "WHERE ueb.unidadeEnsino = " + codigoUnidadeEnsino;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoBibliotecaVO> consultarPorBiblioteca(Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ueb.*, ue.nome as nomeunidadeensino, ue.abreviatura, ue.matriz FROM UnidadeEnsinoBiblioteca ueb " + "INNER JOIN unidadeensino ue ON ueb.unidadeensino = ue.codigo " + "WHERE ueb.biblioteca = " + codigoBiblioteca;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List<UnidadeEnsinoBibliotecaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoBibliotecaVO> vetResultado = new ArrayList<UnidadeEnsinoBibliotecaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static UnidadeEnsinoBibliotecaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UnidadeEnsinoBibliotecaVO obj = new UnidadeEnsinoBibliotecaVO();
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("nomeUnidadeEnsino"));
		obj.getUnidadeEnsino().setAbreviatura(dadosSQL.getString("abreviatura"));
		obj.getUnidadeEnsino().setMatriz(dadosSQL.getBoolean("matriz"));
		obj.setBiblioteca(dadosSQL.getInt("biblioteca"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUnidadeEnsinoBiblioteca(List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) throws Exception {
		for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : unidadeEnsinoBibliotecaVOs) {
			excluir(unidadeEnsinoBibliotecaVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUnidadeEnsinoBiblioteca(Integer codigoBiblioteca, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) throws Exception {
		String str = "DELETE FROM UnidadeEnsinoBiblioteca WHERE biblioteca = " + codigoBiblioteca;
//		for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : unidadeEnsinoBibliotecaVOs) {
//			str += " AND unidadeensino <> " + unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getCodigo();
//		}
		getConexao().getJdbcTemplate().update(str);
		for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : unidadeEnsinoBibliotecaVOs) {
			//if (unidadeEnsinoBibliotecaVO.getBiblioteca() == null || unidadeEnsinoBibliotecaVO.getBiblioteca() == 0) {
				unidadeEnsinoBibliotecaVO.setBiblioteca(codigoBiblioteca);
				incluir(unidadeEnsinoBibliotecaVO);
			//}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirUnidadeEnsinoBiblioteca(Integer codigoContaCorrente, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) throws Exception {
		for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : unidadeEnsinoBibliotecaVOs) {
			unidadeEnsinoBibliotecaVO.setBiblioteca(codigoContaCorrente);
			incluir(unidadeEnsinoBibliotecaVO);
		}
	}

	public static String getIdEntidade() {
		return UnidadeEnsinoBilioteca.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		UnidadeEnsinoBilioteca.idEntidade = idEntidade;
	}
}