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
import negocio.comuns.ead.ParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade;

/**
 * 
 * @author Victor Hugo de Paula Costa 23/03/2015
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class ParametrosMonitoramentoAvaliacaoOnline extends ControleAcesso implements ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade {
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
		ParametrosMonitoramentoAvaliacaoOnline.idEntidade = idEntidade;
	}

	public ParametrosMonitoramentoAvaliacaoOnline() throws Exception {
		super();
		setIdEntidade("ParametrosMonitoramentoAvaliacaoOnline");
	}
	
	public void validarDados(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO) throws Exception {
		if(parametrosMonitoramentoAvaliacaoOnlineVO.getDescricao().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_descricao"));
		}
		if(parametrosMonitoramentoAvaliacaoOnlineVO.getItemParametrosMonitoramentoAvaliacaoOnlineVOs().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametrosMonitoramentoAvalaicaoOnline_itensVazio"));
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(parametrosMonitoramentoAvaliacaoOnlineVO);
			ParametrosMonitoramentoAvaliacaoOnline.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO ParametrosMonitoramentoAvaliacaoOnline (descricao) VALUES (?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			parametrosMonitoramentoAvaliacaoOnlineVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, parametrosMonitoramentoAvaliacaoOnlineVO.getDescricao());
					return sqlInserir;
				}

			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						parametrosMonitoramentoAvaliacaoOnlineVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getItemParametrosMonitoramentoAvaliacaoOnlineFacade().persistirItensParametrosMonitoramentoAvaliacaoOnlineVOs(parametrosMonitoramentoAvaliacaoOnlineVO, usuarioVO);
		} catch (Exception e) {
			parametrosMonitoramentoAvaliacaoOnlineVO.setNovoObj(Boolean.TRUE);
			parametrosMonitoramentoAvaliacaoOnlineVO.setCodigo(0);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (parametrosMonitoramentoAvaliacaoOnlineVO.getCodigo().equals(0)) {
			incluir(parametrosMonitoramentoAvaliacaoOnlineVO, verificarAcesso, usuarioVO);
		} else {
			alterar(parametrosMonitoramentoAvaliacaoOnlineVO, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ParametrosMonitoramentoAvaliacaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE ParametrosMonitoramentoAvaliacaoOnline SET descricao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, parametrosMonitoramentoAvaliacaoOnlineVO.getDescricao());
					sqlAlterar.setInt(2, parametrosMonitoramentoAvaliacaoOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getItemParametrosMonitoramentoAvaliacaoOnlineFacade().persistirItensParametrosMonitoramentoAvaliacaoOnlineVOs(parametrosMonitoramentoAvaliacaoOnlineVO, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ParametrosMonitoramentoAvaliacaoOnline.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM ParametrosMonitoramentoAvaliacaoOnline WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, parametrosMonitoramentoAvaliacaoOnlineVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public ParametrosMonitoramentoAvaliacaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ParametrosMonitoramentoAvaliacaoOnlineVO obj = new ParametrosMonitoramentoAvaliacaoOnlineVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setItemParametrosMonitoramentoAvaliacaoOnlineVOs(getFacadeFactory().getItemParametrosMonitoramentoAvaliacaoOnlineFacade().consultarPorCodigoParametrosMonitoramentoAvaliacaOnline(obj.getCodigo(), nivelMontarDados, usuarioLogado));
		}
		return obj;
	}
	
	@Override
	public List<ParametrosMonitoramentoAvaliacaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ParametrosMonitoramentoAvaliacaoOnlineVO> vetResultado = new ArrayList<ParametrosMonitoramentoAvaliacaoOnlineVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Override
	public List<ParametrosMonitoramentoAvaliacaoOnlineVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (valorConsulta.length() < 2) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		if (campoConsulta.equals("descricao")) {
			return consultarPorDescricao(Uteis.removeCaractersEspeciais(valorConsulta), nivelMontarDados, usuarioLogado);
		} 
		return null;
	}
	
	@Override
	public List<ParametrosMonitoramentoAvaliacaoOnlineVO> consultarPorDescricao(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select * from ParametrosMonitoramentoAvaliacaoOnline");
		sqlStr.append(" where upper(descricao) like upper('" + valorConsulta.toUpperCase() + "%')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Override
	public ParametrosMonitoramentoAvaliacaoOnlineVO consultarPorChavePrimaria(Integer codigoParametros, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select * from ParametrosMonitoramentoAvaliacaoOnline");
		sqlStr.append(" where codigo = ").append(codigoParametros);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioLogado);			
		}
		return new ParametrosMonitoramentoAvaliacaoOnlineVO();
	}
	
	@Override
	public List<ParametrosMonitoramentoAvaliacaoOnlineVO> consultarTodosParametros(int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select * from ParametrosMonitoramentoAvaliacaoOnline");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
}
