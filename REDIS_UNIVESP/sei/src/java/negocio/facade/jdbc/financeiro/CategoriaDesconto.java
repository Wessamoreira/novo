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

import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CategoriaDescontoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MotivoCancelamentoTrancamentoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>MotivoCancelamentoTrancamentoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see MotivoCancelamentoTrancamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class CategoriaDesconto extends ControleAcesso implements CategoriaDescontoInterfaceFacade {

	protected static String idEntidade;

	public CategoriaDesconto() throws Exception {
		super();
		setIdEntidade("CategoriaDesconto");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>MotivoCancelamentoTrancamentoVO</code>.
	 */
	public CategoriaDescontoVO novo() throws Exception {
		CategoriaDesconto.incluir(getIdEntidade());
		CategoriaDescontoVO obj = new CategoriaDescontoVO();
		return obj;
	}

	public void validarDados(CategoriaDescontoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Categoria Desconto) deve ser informado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CategoriaDescontoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			CategoriaDesconto.incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO CategoriaDesconto( nome ) VALUES ( ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
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
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CategoriaDescontoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			CategoriaDesconto.alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE CategoriaDesconto set nome=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setInt(2, obj.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CategoriaDescontoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			CategoriaDesconto.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM CategoriaDesconto WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDesconto WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDesconto WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	public static CategoriaDescontoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		CategoriaDescontoVO obj = new CategoriaDescontoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public CategoriaDescontoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CategoriaDesconto WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	public static String getIdEntidade() {
		return CategoriaDesconto.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		CategoriaDesconto.idEntidade = idEntidade;
	}

}
