package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirTipoTotalizadorEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.LayoutRelatorioSeiDecidirCampoInterface;
import relatorio.negocio.comuns.arquitetura.enumeradores.CrossTabEnum;

/**
 * 
 * @author Leonardo Riciolle Data 18/11/2014
 */

@Repository
@Scope("singleton")
@Lazy
public class LayoutRelatorioSeiDecidirCampo extends ControleAcesso implements LayoutRelatorioSeiDecidirCampoInterface {

	private static final long serialVersionUID = 2434590926864070272L;

	protected static String idEntidade;

	public LayoutRelatorioSeiDecidirCampo() throws Exception {
		super();
		setIdEntidade("LayoutRelatorioSeiDecidirCampo");
	}

	public LayoutRelatorioSeiDecidirCampo novo() throws Exception {
		LayoutRelatorioSeiDecidirCampo.incluir(getIdEntidade());
		LayoutRelatorioSeiDecidirCampo obj = new LayoutRelatorioSeiDecidirCampo();
		return obj;
	}

	public static void validarDados(LayoutRelatorioSeiDecidirCampoVO obj) throws Exception {
		if (obj.getTitulo().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSeiDecidirCampo_Titulo"));
		} else if (obj.getCampo().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSeiDecidirCampo_Campo"));
		} else if (obj.getTamanhoColuna().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSeiDecidirCampo_TamanhoColuna"));
		}
	}

	public static void validarDadosConsulta(String valorConsulta) throws Exception {
		if (valorConsulta.equals("")) {
			throw new Exception("Deve ser informado um valor para a consulta.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LayoutRelatorioSeiDecidirCampoVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO layoutrelatorioseidecidircampo(layoutrelatorioseidecidir, titulo, campo, tamanhocoluna, tipocampo, tipototalizador, textototalizador,");
			sql.append(" qtdcasasdecimais, ordemapresentacao, utilizarGroupBy, utilizarOrderBy, utilizarParametroRelatorio, valorCrosstab, ordenarDescrecente)");
			sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());					
						int x = 1;
						sqlInserir.setInt(x++, obj.getLayoutRelatorioSEIDecidirVO().getCodigo());
						sqlInserir.setString(x++, obj.getTitulo());
						sqlInserir.setString(x++, obj.getCampo());
						sqlInserir.setInt(x++, obj.getTamanhoColuna());
						sqlInserir.setString(x++, obj.getTipoCampo().toString());
						sqlInserir.setString(x++, obj.getTipoTotalizador().toString());
						if (!obj.getTipoTotalizador().equals(RelatorioSEIDecidirTipoTotalizadorEnum.TEXTO)) {
							sqlInserir.setNull(x++, 0, "");
						} else {
							sqlInserir.setString(x++, obj.getTextoTotalizador());
						}
						sqlInserir.setInt(x++, obj.getQtdCasasDecimais());
						sqlInserir.setInt(x++, obj.getOrdemApresentacao());
						sqlInserir.setBoolean(x++, obj.getUtilizarGroupBy());
						sqlInserir.setBoolean(x++, obj.getUtilizarOrderBy());
						sqlInserir.setBoolean(x++, obj.getUtilizarParametroRelatorio());
						if (Uteis.isAtributoPreenchido(obj.getValorCrosstab())) {
							sqlInserir.setString(x++, obj.getValorCrosstab().name());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setBoolean(x++, obj.getOrdenarDescrecente());
					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LayoutRelatorioSeiDecidirCampoVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE layoutrelatorioseidecidircampo  SET layoutrelatorioseidecidir=?, titulo=?, campo=?, tamanhocoluna=?, tipocampo=?, tipototalizador=?,");
			sql.append(" textototalizador=?, qtdcasasdecimais=?, ordemApresentacao=?, utilizarGroupBy = ?, utilizarOrderBy = ?, utilizarParametroRelatorio = ?, valorCrosstab=?, ordenarDescrecente =? ");
			sql.append(" WHERE codigo = ?;").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setInt(x++, obj.getLayoutRelatorioSEIDecidirVO().getCodigo());
					sqlAlterar.setString(x++, obj.getTitulo());
					sqlAlterar.setString(x++, obj.getCampo());
					sqlAlterar.setInt(x++, obj.getTamanhoColuna());
					sqlAlterar.setString(x++, obj.getTipoCampo().toString());
					sqlAlterar.setString(x++, obj.getTipoTotalizador().toString());
					sqlAlterar.setString(x++, obj.getTextoTotalizador());
					sqlAlterar.setInt(x++, obj.getQtdCasasDecimais());
					sqlAlterar.setInt(x++, obj.getOrdemApresentacao());
					sqlAlterar.setBoolean(x++, obj.getUtilizarGroupBy());
					sqlAlterar.setBoolean(x++, obj.getUtilizarOrderBy());
					sqlAlterar.setBoolean(x++, obj.getUtilizarParametroRelatorio());
					if (Uteis.isAtributoPreenchido(obj.getValorCrosstab())) {
						sqlAlterar.setString(x++, obj.getValorCrosstab().name());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getOrdenarDescrecente());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0){
				incluir(obj, false, usuarioVO);
				return;
			};
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LayoutRelatorioSeiDecidirCampoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM layoutrelatorioseidecidircampo WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirLayoutRelatorioSEIDecidir(Integer layoutrelatorioseidecidir, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM layoutrelatorioseidecidircampo WHERE (layoutrelatorioseidecidir = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), layoutrelatorioseidecidir);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<LayoutRelatorioSeiDecidirCampoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<LayoutRelatorioSeiDecidirCampoVO> layoutRelatorioSeiDecidirCampoVOs = new ArrayList<LayoutRelatorioSeiDecidirCampoVO>(0);
		while (tabelaResultado.next()) {
			layoutRelatorioSeiDecidirCampoVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return layoutRelatorioSeiDecidirCampoVOs;
	}

	public LayoutRelatorioSeiDecidirCampoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSeiDecidirCampoVO obj = new LayoutRelatorioSeiDecidirCampoVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getLayoutRelatorioSEIDecidirVO().setCodigo(tabelaResultado.getInt("layoutrelatorioseidecidir"));

		obj.setTitulo(tabelaResultado.getString("titulo"));
		obj.setCampo(tabelaResultado.getString("campo"));
		obj.setTamanhoColuna(tabelaResultado.getInt("tamanhocoluna"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipocampo"))) {
			obj.setTipoCampo(TipoCampoEnum.valueOf(tabelaResultado.getString("tipocampo")));
		}
		obj.setQtdCasasDecimais(tabelaResultado.getInt("qtdcasasdecimais"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoTotalizador"))) {
			obj.setTipoTotalizador(RelatorioSEIDecidirTipoTotalizadorEnum.valueOf(tabelaResultado.getString("tipoTotalizador")));
		}
		obj.setTextoTotalizador(tabelaResultado.getString("textototalizador"));
		obj.setOrdemApresentacao(tabelaResultado.getInt("ordemApresentacao"));
		obj.setUtilizarGroupBy(tabelaResultado.getBoolean("utilizarGroupBy"));
		obj.setUtilizarOrderBy(tabelaResultado.getBoolean("utilizarOrderBy"));
		obj.setOrdenarDescrecente(tabelaResultado.getBoolean("ordenarDescrecente"));		
		obj.setUtilizarParametroRelatorio(tabelaResultado.getBoolean("utilizarParametroRelatorio"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("valorcrosstab"))) {
			obj.setValorCrosstab(CrossTabEnum.valueOf(tabelaResultado.getString("valorcrosstab")));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			this.excluirLayoutRelatorioSeiDecidirCampoVOs(layoutRelatorioSEIDecidirVO, verificarAcesso, usuarioVO);
			for (LayoutRelatorioSeiDecidirCampoVO obj : layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs()) {
				obj.setLayoutRelatorioSEIDecidirVO(layoutRelatorioSEIDecidirVO);
				if (obj.getNovoObj()) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM layoutrelatorioseidecidircampo WHERE (layoutrelatorioseidecidir = " + layoutRelatorioSEIDecidirVO.getCodigo());
			sql.append(") and codigo not in(");
			for (LayoutRelatorioSeiDecidirCampoVO obj : layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs()) {
				sql.append(obj.getCodigo()).append(", ");
			}
			sql.append("0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public LayoutRelatorioSeiDecidirCampoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM layoutrelatorioseidecidircampo where codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	/**
	 * Consultar todos {@link LayoutRelatorioSeiDecidirCampoVO} pelo codigo do {@link LayoutRelatorioSEIDecidirVO}.
	 */
	@Override
	public List<LayoutRelatorioSeiDecidirCampoVO> consultarPorLayoutRelatorio(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM layoutrelatorioseidecidircampo WHERE layoutrelatorioseidecidir = ?");
		sqlStr.append(" ORDER BY ordemapresentacao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), layoutRelatorioSEIDecidirVO.getCodigo());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		LayoutRelatorioSeiDecidirCampo.idEntidade = idEntidade;
	}

}
