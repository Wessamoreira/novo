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

import negocio.comuns.academico.EixoCursoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.EixoCursoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class EixoCurso extends ControleAcesso implements EixoCursoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public EixoCursoVO novo() throws Exception {
		EixoCurso.incluir(getIdEntidade());
		EixoCursoVO eixoCurso = new EixoCursoVO();
		return eixoCurso;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final EixoCursoVO eixoCursoVO) throws Exception {
		try {
			validarDados(eixoCursoVO);
			verificarUnicidade(eixoCursoVO);
			final String sql = "insert into eixocurso (nome) values (?) returning codigo";
			eixoCursoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, eixoCursoVO.getNome());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						eixoCursoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(EixoCursoVO eixoCursoVO) throws Exception {
		try {
			verificarUnicidade(eixoCursoVO);
			final String sql = "update eixocurso set nome=? where codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, eixoCursoVO.getNome());
					sqlAlterar.setInt(2, eixoCursoVO.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			eixoCursoVO.setNovoObj(false);
			throw e;
		}

	}

	@Override
	public void excluir(EixoCursoVO eixoCursoVO) throws Exception {
		String sql = "delete from eixocurso where codigo=?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { eixoCursoVO.getCodigo() });

	}

	@Override
	public EixoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
		String sqlStr = "select * from eixocurso where codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, codigo);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
		}
		return null;
	}

	@Override
	public List<EixoCursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados)
			throws Exception {
		String sqlStr = "select * from eixocurso where codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
		
	}

	@Override
	public List<EixoCursoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados)
			throws Exception {
		String sqlStr = "select * from eixocurso where sem_acentos(nome) ilike sem_acentos(?)  ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr,
				PERCENT + valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}

	public void validarDados(EixoCursoVO eixoCursoVO) throws Exception {

		if (!Uteis.isAtributoPreenchido(eixoCursoVO.getNome())) {
			throw new Exception("O campo NOME (Eixo de Curso) deve ser informado.");
		}
	}
	
	public void verificarUnicidade(EixoCursoVO eixoCursoVO) throws Exception {
		StringBuilder sql = new StringBuilder("select codigo from eixocurso where trim(sem_acentos(nome)) ilike trim(sem_acentos(?)) and codigo != ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), eixoCursoVO.getNome(), eixoCursoVO.getCodigo());
		if(rs.next()) {
			throw new Exception("Já existe um cadastro como o nome: "+eixoCursoVO.getNome()+"!");
		}
	}

	public void persistir(EixoCursoVO eixoCursoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(eixoCursoVO.getCodigo())) {
			alterar(eixoCursoVO);
		} else {
			incluir(eixoCursoVO);
		}
	}

	public static EixoCursoVO montarDados(SqlRowSet dadosSql, int nivelMontarDados) throws Exception {
		EixoCursoVO obj = new EixoCursoVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSql.getInt("codigo")));
		obj.setNome(dadosSql.getString("nome"));
		return obj;
	}

	public List<EixoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<EixoCursoVO> eixoCursoVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			eixoCursoVOs.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return eixoCursoVOs;
	}

	@Override
	public List<EixoCursoVO> consultarTodos(int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT * FROM eixocurso ORDER BY nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	public EixoCursoVO consultarNomePorCodigo(String eixoCurso, int nivelMontarDados)
			throws Exception {
		String sqlStr = "select * from eixocurso where sem_acentos(nome) ilike sem_acentos(?)";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr,
				eixoCurso );
		if(tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados);	
		}
		throw new Exception("Não foi encontrado um EIXO CURSO com o nome "+eixoCurso+".");
	}

}
