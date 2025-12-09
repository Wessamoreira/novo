package negocio.facade.jdbc.financeiro;

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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroResultadoRestricaoUsoVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CentroResultadoRestricaoUsoInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class CentroResultadoRestricaoUso extends ControleAcesso implements CentroResultadoRestricaoUsoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3901263119176799248L;
	private static String idEntidade = "CentroResultadoRestricaoUso";

	public CentroResultadoRestricaoUso() {
		super();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<CentroResultadoRestricaoUsoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (CentroResultadoRestricaoUsoVO obj : lista) {
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
			}

		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CentroResultadoRestricaoUsoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultadoRestricaoUso.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO CentroResultadoRestricaoUso (centroResultado, usuario, perfilacesso ) ");
			sql.append("  VALUES ( ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement ps = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, ps);
					Uteis.setValuePreparedStatement(obj.getUsuarioVO(), ++i, ps);
					Uteis.setValuePreparedStatement(obj.getPerfilAcessoVO(), ++i, ps);
					return ps;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CentroResultadoRestricaoUsoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultadoRestricaoUso.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE CentroResultadoRestricaoUso ");
			sql.append("   SET centroResultado=?, usuario=?, perfilacesso=?  ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, ps);
					Uteis.setValuePreparedStatement(obj.getUsuarioVO(), ++i, ps);
					Uteis.setValuePreparedStatement(obj.getPerfilAcessoVO(), ++i, ps);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, ps);
					return ps;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CentroResultadoRestricaoUso.codigo as \"CentroResultadoRestricaoUso.codigo\",  ");
		sql.append(" centroResultado.codigo as \"centroResultado.codigo\",  ");		
		sql.append(" perfilacesso.codigo as \"perfilacesso.codigo\", perfilacesso.nome as \"perfilacesso.nome\", ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
		sql.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\" ");
		sql.append(" FROM CentroResultadoRestricaoUso ");
		sql.append(" INNER JOIN centroResultado ON centroResultado.codigo = CentroResultadoRestricaoUso.centroResultado ");
		sql.append(" LEFT JOIN perfilacesso ON perfilacesso.codigo = CentroResultadoRestricaoUso.perfilacesso ");
		sql.append(" LEFT JOIN usuario ON usuario.codigo = CentroResultadoRestricaoUso.usuario ");
		sql.append(" LEFT JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sql.append(" LEFT JOIN parceiro ON parceiro.codigo = usuario.parceiro ");
		
		
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoRestricaoUsoVO> consultaRapidaPorCentroResultado(CentroResultadoVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE centroResultado.codigo = ").append(obj.getCodigo());
			sqlStr.append(" ORDER BY CentroResultadoRestricaoUso.codigo ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<CentroResultadoRestricaoUsoVO> vetResultado = new ArrayList<>();
			while (rs.next()) {
				CentroResultadoRestricaoUsoVO crru = new CentroResultadoRestricaoUsoVO();
				montarDadosBasico(crru, rs, nivelMontarDados, usuario);
				crru.setCentroResultadoVO(obj);
				vetResultado.add(crru);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public Boolean consultarSeExisteUsuarioEspecificoPorCentroResultado(Integer codigoCentroResultado, Integer codigoUsuarioEspecifico) {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from CentroResultadoRestricaoUso where centroResultado = ? and usuario = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCentroResultado, codigoUsuarioEspecifico);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public Boolean consultarSeExistePerfilAcessoEspecificoPorCentroResultado(Integer codigoCentroResultado, Integer codigoPerfilAcessoEspecifico) {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from CentroResultadoRestricaoUso where centroResultado = ? and perfilacesso = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCentroResultado, codigoPerfilAcessoEspecifico);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoRestricaoUsoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE centroResultado.codigo = ").append(codigoPrm);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( CentroResultadoRestricaoUsoVO ).");
			}
			CentroResultadoRestricaoUsoVO obj = new CentroResultadoRestricaoUsoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoRestricaoUsoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<CentroResultadoRestricaoUsoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			CentroResultadoRestricaoUsoVO obj = new CentroResultadoRestricaoUsoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(CentroResultadoRestricaoUsoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("CentroResultadoRestricaoUso.codigo"));
			obj.getCentroResultadoVO().setCodigo(dadosSQL.getInt("centroResultado.codigo"));
			obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario.codigo"));
			obj.getUsuarioVO().setNome(dadosSQL.getString("usuario.nome"));
			obj.getUsuarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
			obj.getUsuarioVO().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
			obj.getUsuarioVO().getParceiro().setCodigo(dadosSQL.getInt("parceiro.codigo"));
			obj.getUsuarioVO().getParceiro().setNome(dadosSQL.getString("parceiro.nome"));
			obj.getPerfilAcessoVO().setCodigo(dadosSQL.getInt("perfilacesso.codigo"));
			obj.getPerfilAcessoVO().setNome(dadosSQL.getString("perfilacesso.nome"));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return;
			}

			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
				return;
			}
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
				return;
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return CentroResultadoRestricaoUso.idEntidade;
	}

}
