package negocio.facade.jdbc.ead;

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
import negocio.comuns.ead.ItemParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.ead.ParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade;

/**
 * 
 * @author Victor Hugo de Paula Costa 23/03/2015
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class ItemParametrosMonitoramentoAvaliacaoOnline extends ControleAcesso implements ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ItemParametrosMonitoramentoAvaliacaoOnline.idEntidade = idEntidade;
	}

	public ItemParametrosMonitoramentoAvaliacaoOnline() throws Exception {
		super();
		setIdEntidade("ItemParametrosMonitoramentoAvaliacaoOnline");
	}

	@Override
	public void persistirItensParametrosMonitoramentoAvaliacaoOnlineVOs(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		for (ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO : parametrosMonitoramentoAvaliacaoOnlineVO.getItemParametrosMonitoramentoAvaliacaoOnlineVOs()) {
			persistir(itemParametrosMonitoramentoAvaliacaoOnlineVO, false, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO itemParametrosMonitoramentoAvaliacaoOnline (ParametrosMonitoramentoAvaliacaoOnline, descricaoparametro, percentualacertosde, percentualacertosate, corgrafico, corLetraGrafico) VALUES (?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			itemParametrosMonitoramentoAvaliacaoOnlineVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, itemParametrosMonitoramentoAvaliacaoOnlineVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo());
					sqlInserir.setString(2, itemParametrosMonitoramentoAvaliacaoOnlineVO.getDescricaoParametro());
					sqlInserir.setDouble(3, itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosDe());
					sqlInserir.setDouble(4, itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosAte());
					sqlInserir.setString(5, itemParametrosMonitoramentoAvaliacaoOnlineVO.getCorGrafico());
					sqlInserir.setString(6, itemParametrosMonitoramentoAvaliacaoOnlineVO.getCorLetraGrafico());
					return sqlInserir;
				}

			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						itemParametrosMonitoramentoAvaliacaoOnlineVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			itemParametrosMonitoramentoAvaliacaoOnlineVO.setNovoObj(Boolean.TRUE);
			itemParametrosMonitoramentoAvaliacaoOnlineVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (itemParametrosMonitoramentoAvaliacaoOnlineVO.getCodigo().equals(0)) {
			incluir(itemParametrosMonitoramentoAvaliacaoOnlineVO, verificarAcesso, usuarioVO);
		} else {
			alterar(itemParametrosMonitoramentoAvaliacaoOnlineVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE itemParametrosMonitoramentoAvaliacaoOnline SET ParametrosMonitoramentoAvaliacaoOnline = ?, descricaoparametro = ?, percentualacertosde = ?, percentualacertosate = ?, corgrafico = ?, corLetraGrafico = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, itemParametrosMonitoramentoAvaliacaoOnlineVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo());
					sqlAlterar.setString(2, itemParametrosMonitoramentoAvaliacaoOnlineVO.getDescricaoParametro());
					sqlAlterar.setDouble(3, itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosDe());
					sqlAlterar.setDouble(4, itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosAte());
					sqlAlterar.setString(5, itemParametrosMonitoramentoAvaliacaoOnlineVO.getCorGrafico());
					sqlAlterar.setString(6, itemParametrosMonitoramentoAvaliacaoOnlineVO.getCorLetraGrafico());
					sqlAlterar.setInt(7, itemParametrosMonitoramentoAvaliacaoOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM itemParametrosMonitoramentoAvaliacaoOnline WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, itemParametrosMonitoramentoAvaliacaoOnlineVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ItemParametrosMonitoramentoAvaliacaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ItemParametrosMonitoramentoAvaliacaoOnlineVO obj = new ItemParametrosMonitoramentoAvaliacaoOnlineVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getParametrosMonitoramentoAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("ParametrosMonitoramentoAvaliacaoOnline"));
		obj.setDescricaoParametro(tabelaResultado.getString("descricaoparametro"));
		obj.setPercentualAcertosDe(tabelaResultado.getDouble("percentualacertosde"));
		obj.setPercentualAcertosAte(tabelaResultado.getDouble("percentualacertosate"));
		obj.setCorGrafico(tabelaResultado.getString("corgrafico"));
		obj.setCorLetraGrafico(tabelaResultado.getString("corLetraGrafico"));
		return obj;
	}

	@Override
	public List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> vetResultado = new ArrayList<ItemParametrosMonitoramentoAvaliacaoOnlineVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	public void adicionarItemParametros(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		for (ItemParametrosMonitoramentoAvaliacaoOnlineVO obj : parametrosMonitoramentoAvaliacaoOnlineVO.getItemParametrosMonitoramentoAvaliacaoOnlineVOs()) {
			validarDados(obj, itemParametrosMonitoramentoAvaliacaoOnlineVO);
		}
		itemParametrosMonitoramentoAvaliacaoOnlineVO.setParametrosMonitoramentoAvaliacaoOnlineVO(parametrosMonitoramentoAvaliacaoOnlineVO);
		parametrosMonitoramentoAvaliacaoOnlineVO.getItemParametrosMonitoramentoAvaliacaoOnlineVOs().add(itemParametrosMonitoramentoAvaliacaoOnlineVO);
	}

	public void validarDados(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO2) throws Exception {
		if(itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosDe() <= itemParametrosMonitoramentoAvaliacaoOnlineVO2.getPercentualAcertosDe()) {
			if(itemParametrosMonitoramentoAvaliacaoOnlineVO2.getPercentualAcertosDe() <= itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosAte()) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_percentualDeEncontraOutroParametro"));
			}
		}
		if(itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosDe() <= itemParametrosMonitoramentoAvaliacaoOnlineVO2.getPercentualAcertosAte()) {
			if(itemParametrosMonitoramentoAvaliacaoOnlineVO2.getPercentualAcertosAte() <= itemParametrosMonitoramentoAvaliacaoOnlineVO.getPercentualAcertosAte()) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_percentualAteEncontraOutroParametroO"));
			}
		}
		if(itemParametrosMonitoramentoAvaliacaoOnlineVO2.getDescricaoParametro().equals(itemParametrosMonitoramentoAvaliacaoOnlineVO.getDescricaoParametro())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_descricaoIgual"));
		}
		if(itemParametrosMonitoramentoAvaliacaoOnlineVO2.getCorGrafico().equals(itemParametrosMonitoramentoAvaliacaoOnlineVO.getCorGrafico())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_corGraficoIgual"));
		}
		if(itemParametrosMonitoramentoAvaliacaoOnlineVO2.getDescricaoParametro().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_descricaoParametro"));
		}
	}
	
	@Override
	public void removerItemParametros(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		if(!itemParametrosMonitoramentoAvaliacaoOnlineVO.getCodigo().equals(0)) {
			excluir(itemParametrosMonitoramentoAvaliacaoOnlineVO, false, usuarioVO);
		}
		parametrosMonitoramentoAvaliacaoOnlineVO.getItemParametrosMonitoramentoAvaliacaoOnlineVOs().remove(itemParametrosMonitoramentoAvaliacaoOnlineVO);
	}
	
	@Override
	public List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> consultarPorCodigoParametrosMonitoramentoAvaliacaOnline(Integer codigoParametros, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		String sql = "select * from ItemParametrosMonitoramentoAvaliacaoOnline where ParametrosMonitoramentoAvaliacaoOnline = "+codigoParametros;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
	
	@Override
	public List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> consultarItemParametrosMonitoramentoAvalaicaoOnlinePorTurmaAnoSemestre(Integer codigoTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select itemparametrosmonitoramentoavaliacaoonline.* from itemparametrosmonitoramentoavaliacaoonline");
		sqlStr.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.parametromonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo");
		sqlStr.append(" inner join turmadisciplinaconteudo on turmadisciplinaconteudo.avaliacaoonline = avaliacaoonline.codigo");
		sqlStr.append(" where turmadisciplinaconteudo.turma = ").append(codigoTurma);
		sqlStr.append(" and turmadisciplinaconteudo.ano ||'/'||turmadisciplinaconteudo.semestre <= '").append(ano).append("/").append(semestre).append("')");
		sqlStr.append(" union");
		sqlStr.append(" (select itemparametrosmonitoramentoavaliacaoonline.* from itemparametrosmonitoramentoavaliacaoonline");
		sqlStr.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.parametromonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo");
		sqlStr.append(" inner join turma on turma.avaliacaoonline = avaliacaoonline.codigo");
		sqlStr.append(" where turma.codigo = ").append(codigoTurma).append(")").append("order by percentualacertosde");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
}
