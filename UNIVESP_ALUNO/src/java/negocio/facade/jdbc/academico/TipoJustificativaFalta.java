package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TipoJustificativaFaltaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TipoJustificativaFaltaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TipoJustificativaFalta extends ControleAcesso implements TipoJustificativaFaltaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public TipoJustificativaFalta() throws Exception {
		super();
		setIdEntidade("TipoJustificativaFalta");
	}

	@Override
	public void setIdEntidade(String idEntidade) {
		TipoJustificativaFalta.idEntidade = idEntidade;
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void incluir(final TipoJustificativaFaltaVO tipoJustificativaFaltaVO, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			if (validarUnicidade(tipoJustificativaFaltaVO)) {
				incluir(getIdEntidade(), verificarAcesso, usuario);
				final String sql = "INSERT INTO tipojustificativafalta(descricao, tipoSexoJustificativa, sigla) VALUES ( ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

				tipoJustificativaFaltaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlInserir = arg0.prepareStatement(sql);
						sqlInserir.setString(1, tipoJustificativaFaltaVO.getDescricao());
						sqlInserir.setString(2, tipoJustificativaFaltaVO.getTipoSexoJustificativa());
						sqlInserir.setString(3, tipoJustificativaFaltaVO.getSigla());
						return sqlInserir;
					}
				}, new ResultSetExtractor() {

					public Object extractData(ResultSet rs) throws SQLException {
						if (rs.next()) {
							tipoJustificativaFaltaVO.setNovoObj(Boolean.FALSE);
							return rs.getInt("codigo");
						}
						return null;
					}
				}));

				tipoJustificativaFaltaVO.setNovoObj(Boolean.FALSE);
			} else {
				throw new ConsistirException("J\u00e1 existe um Tipo de Justificativa de Falta com esta descri\u00e7\u00e3o.");
			}
		} catch (Exception e) {
			tipoJustificativaFaltaVO.setNovoObj(true);
			throw e;
		}

	}

	@Override
	public void alterar(final TipoJustificativaFaltaVO tipoJustificativaFaltaVO, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			final String sql = "UPDATE tipojustificativafalta set descricao=?, tipoSexoJustificativa=?, sigla=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, tipoJustificativaFaltaVO.getDescricao());
					sqlAlterar.setString(2, tipoJustificativaFaltaVO.getTipoSexoJustificativa());
					sqlAlterar.setString(3, tipoJustificativaFaltaVO.getSigla());
					sqlAlterar.setInt(4, tipoJustificativaFaltaVO.getCodigo().intValue());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(TipoJustificativaFaltaVO tipoJustificativaFaltaVO, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM tipojustificativafalta WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { tipoJustificativaFaltaVO.getCodigo() });
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public TipoJustificativaFaltaVO consultarPorChavePrimeira(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM tipojustificativafalta WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return new TipoJustificativaFaltaVO();
	}
	
	public static List<TipoJustificativaFaltaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<TipoJustificativaFaltaVO> vetResultado = new ArrayList<TipoJustificativaFaltaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public static TipoJustificativaFaltaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		TipoJustificativaFaltaVO obj = new TipoJustificativaFaltaVO();

		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setTipoSexoJustificativa(dadosSQL.getString("tipoSexoJustificativa"));
		obj.setSigla(dadosSQL.getString("sigla"));
		return obj;
	}
	
	@Override
	public List<TipoJustificativaFaltaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM tipojustificativafalta WHERE upper(sem_acentos( descricao )) ilike(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public Boolean validarUnicidade(TipoJustificativaFaltaVO obj) {

		StringBuilder sql = new StringBuilder("SELECT count(codigo) as codigo from tipojustificativafalta");
		sql.append(" where descricao = '").append(obj.getDescricao()).append("'");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("codigo") == 0 ? true : false;
		}

		return false;
	}
	
}