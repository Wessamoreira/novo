package negocio.facade.jdbc.biblioteca;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.MotivoEmprestimoPorHoraVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.MotivoEmprestimoPorHoraInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>EditoraVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>EditoraVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see EditoraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MotivoEmprestimoPorHora extends ControleAcesso implements MotivoEmprestimoPorHoraInterfaceFacade {

	protected static String idEntidade;

	public MotivoEmprestimoPorHora() throws Exception {
		super();
		setIdEntidade("MotivoEmprestimoPorHora");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		MotivoEmprestimoPorHora.idEntidade = idEntidade;
	}

	@Override
	public void incluir(final MotivoEmprestimoPorHoraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (validarUnicidade(obj)) {
				MotivoEmprestimoPorHora.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
				final String sql = "INSERT INTO motivoemprestimoporhora(descricao) VALUES ( ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

				obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlInserir = arg0.prepareStatement(sql);
						sqlInserir.setString(1, obj.getDescricao());
						return sqlInserir;
					}
				}, new ResultSetExtractor() {

					public Object extractData(ResultSet rs) throws SQLException {
						if (rs.next()) {
							obj.setNovoObj(Boolean.FALSE);
							return rs.getInt("codigo");
						}
						return null;
					}
				}));

				obj.setNovoObj(Boolean.FALSE);
			}else{
				throw new ConsistirException("J\u00e1 existe um motivo com esta descri\u00e7\u00e3o.");
			}
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}

	}

	@Override
	public void alterar(final MotivoEmprestimoPorHoraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			MotivoEmprestimoPorHora.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE motivoemprestimoporhora set descricao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(MotivoEmprestimoPorHoraVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			MotivoEmprestimoPorHora.excluir(getIdEntidade(), usuarioVO);
			String sql = "DELETE FROM motivoemprestimoporhora WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public static void validarDados(MotivoEmprestimoPorHoraVO obj) throws ConsistirException {
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo Descrição deve ser informado.");
		}
	}

	@Override
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM motivoemprestimoporhora WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public static MotivoEmprestimoPorHoraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		MotivoEmprestimoPorHoraVO obj = new MotivoEmprestimoPorHoraVO();

		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));

		return obj;
	}

	@Override
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM motivoemprestimoporhora WHERE upper(sem_acentos( descricao )) ilike(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public Boolean validarUnicidade(MotivoEmprestimoPorHoraVO obj) {

		StringBuilder sql = new StringBuilder("SELECT count(codigo) as codigo from motivoemprestimoporhora");
		sql.append(" where descricao = '").append(obj.getDescricao()).append("'");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("codigo") == 0 ? true : false;
		}

		return false;
	}
}