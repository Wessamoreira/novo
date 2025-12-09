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

import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.QuestaoAssuntoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.QuestaoAssuntoInterfaceFacade;

/**
 * @author Victor Hugo 09/03/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class QuestaoAssunto extends ControleAcesso implements QuestaoAssuntoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public QuestaoAssunto() throws Exception {
		super();	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(QuestaoAssuntoVO questaoAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (questaoAssuntoVO.getCodigo().equals(0)) {
			incluir(questaoAssuntoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(questaoAssuntoVO, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final QuestaoAssuntoVO questaoAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			final String sql = "insert into questaoAssunto(questao, temaassunto) values(?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			questaoAssuntoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, questaoAssuntoVO.getQuestaoVO().getCodigo());
					sqlInserir.setInt(2, questaoAssuntoVO.getTemaAssuntoVO().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						questaoAssuntoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			questaoAssuntoVO.setNovoObj(Boolean.TRUE);
			questaoAssuntoVO.setCodigo(0);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final QuestaoAssuntoVO questaoAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			final String sql = "UPDATE questaoAssunto set questao = ?, temaassunto = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, questaoAssuntoVO.getQuestaoVO().getCodigo());
					sqlAlterar.setInt(2, questaoAssuntoVO.getTemaAssuntoVO().getCodigo());
					sqlAlterar.setInt(3, questaoAssuntoVO.getCodigo());
					return sqlAlterar;
				}
			}) == 0 ) {
				incluir(questaoAssuntoVO, verificarAcesso, usuarioVO);
				return;
			};
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirQuestaoAssuntoVOs(final Integer codigoQuestao, final List<QuestaoAssuntoVO> questaoAssuntoVOs, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {			
			StringBuilder sql = new StringBuilder("DELETE FROM questaoAssunto WHERE questao = ? and codigo not in (0 ");
			for(QuestaoAssuntoVO questaoAssuntoVO: questaoAssuntoVOs) {
				sql.append(", ").append(questaoAssuntoVO.getCodigo());
			}	
			sql.append(") ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), codigoQuestao);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final Integer codigoQuestao, final Integer codigoTemaAssunto, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			String sql = "DELETE FROM questaoAssunto WHERE questao = "+codigoQuestao+" and temaassunto = "+codigoTemaAssunto+" "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void adicionarTemaAssunto(TemaAssuntoVO temaAssuntoVO, QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoAssuntoVO questaoAssuntoVO : questaoVO.getQuestaoAssuntoVOs()) {
			if(questaoAssuntoVO.getTemaAssuntoVO().getCodigo().equals(temaAssuntoVO.getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_TemaAssunto_temaAssuntoJaAdicionado"));				
			}
		}
		QuestaoAssuntoVO questaoAssuntoVO = new QuestaoAssuntoVO();
		questaoAssuntoVO.setTemaAssuntoVO(temaAssuntoVO);
		questaoAssuntoVO.setQuestaoVO(questaoVO);
		questaoVO.getQuestaoAssuntoVOs().add(questaoAssuntoVO);
	}
	
	@Override
	public void removerTemaAssunto(QuestaoAssuntoVO questaoAssuntoVO, List<QuestaoAssuntoVO> questaoAssuntoVOs, UsuarioVO usuarioVO) throws Exception {
		if(!questaoAssuntoVO.getCodigo().equals(0)) {
			excluir(questaoAssuntoVO.getQuestaoVO().getCodigo(), questaoAssuntoVO.getTemaAssuntoVO().getCodigo(), false, usuarioVO);
		} 
		questaoAssuntoVOs.remove(questaoAssuntoVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirQuestaoAssuntoVOs(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoAssuntoVO questaoAssuntoVO : questaoVO.getQuestaoAssuntoVOs()) {
			questaoAssuntoVO.setQuestaoVO(questaoVO);
			persistir(questaoAssuntoVO, false, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<QuestaoAssuntoVO> consultarPorCodigoQuestaoVO(Integer codigoQuestao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM questaoassunto");
		sqlStr.append(" WHERE questao = ").append(codigoQuestao);

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}
	
	@Override
	public List<QuestaoAssuntoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<QuestaoAssuntoVO> vetResultado = new ArrayList<QuestaoAssuntoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}
	
	@Override
	public QuestaoAssuntoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		QuestaoAssuntoVO obj = new QuestaoAssuntoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getQuestaoVO().setCodigo(tabelaResultado.getInt("questao"));
		obj.getTemaAssuntoVO().setCodigo(tabelaResultado.getInt("temaassunto"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setTemaAssuntoVO(getFacadeFactory().getTemaAssuntoFacade().consultarPorChavePrimaria(obj.getTemaAssuntoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			return obj;
		}
		return obj;
	}
}
