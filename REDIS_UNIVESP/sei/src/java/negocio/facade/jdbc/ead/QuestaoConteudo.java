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
import negocio.comuns.ead.QuestaoConteudoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.ead.QuestaoConteudoFacade;

/**
 * @author Victor Hugo 08/01/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class QuestaoConteudo extends ControleAcesso implements QuestaoConteudoFacade {

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
		QuestaoConteudo.idEntidade = idEntidade;
	}

	public QuestaoConteudo() throws Exception {
		super();
		setIdEntidade("QuestaoConteudo");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			QuestaoConteudo.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO QuestaoConteudo (questao, conteudo) " + "VALUES (?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			questaoConteudoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, questaoConteudoVO.getQuestaoVO().getCodigo());
					sqlInserir.setInt(2, questaoConteudoVO.getConteudoVO().getCodigo());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						questaoConteudoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			questaoConteudoVO.setNovoObj(Boolean.TRUE);
			questaoConteudoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (questaoConteudoVO.getCodigo() == 0) {
			incluir(questaoConteudoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(questaoConteudoVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			QuestaoConteudo.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE questaoconteudo SET questao = ?, conteudo = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setInt(1, questaoConteudoVO.getQuestaoVO().getCodigo());
					sqlAlterar.setInt(2, questaoConteudoVO.getConteudoVO().getCodigo());
					sqlAlterar.setInt(3, questaoConteudoVO.getCodigo());

					return sqlAlterar;
				}
			}) == 0) {
				incluir(questaoConteudoVO, verificarAcesso, usuarioVO);
				return;
			};
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void persistirQuestaoConteudoVOs(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoConteudoVO questaoConteudoVO : questaoVO.getQuestaoConteudoVOs()) {
			questaoConteudoVO.setQuestaoVO(questaoVO);
			persistir(questaoConteudoVO, false, usuarioVO);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirQuestaoConteudoVOs(final Integer codigoQuestao, final List<QuestaoConteudoVO> questaoConteudoVOs, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {			
			StringBuilder sql = new StringBuilder("DELETE FROM questaoConteudo WHERE questao = ? and codigo not in (0 ");
			for(QuestaoConteudoVO questaoConteudoVO: questaoConteudoVOs) {
				sql.append(", ").append(questaoConteudoVO.getCodigo());
			}			
			sql.append(") ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), codigoQuestao);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<QuestaoConteudoVO> consultarPorCodigoQuestaoVO(Integer codigoQuestao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM questaoconteudo");
		sqlStr.append(" WHERE questao = ").append(codigoQuestao);

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}

	@Override
	public List<QuestaoConteudoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<QuestaoConteudoVO> vetResultado = new ArrayList<QuestaoConteudoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			QuestaoConteudo.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM QuestaoConteudo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, questaoConteudoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public QuestaoConteudoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		QuestaoConteudoVO obj = new QuestaoConteudoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getConteudoVO().setCodigo(tabelaResultado.getInt("conteudo"));
		obj.getQuestaoVO().setCodigo(tabelaResultado.getInt("questao"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(obj.getConteudoVO().getCodigo(), NivelMontarDados.BASICO, false, usuarioLogado));
			return obj;
		}
		return obj;
	}

	@Override
	public void adicionarConteudo(QuestaoVO questaoVO, QuestaoConteudoVO questaoConteudoVO, UsuarioVO usuarioVO) throws Exception {
		if (questaoConteudoVO.getConteudoVO().getCodigo().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_Questao_selecioneUmConteudo"));
		}
		for (QuestaoConteudoVO questaoConteudoVO2 : questaoVO.getQuestaoConteudoVOs()) {
			if (questaoConteudoVO.getConteudoVO().getCodigo().equals(questaoConteudoVO2.getConteudoVO().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_QuestaoControle_conteudoJaAdicionado"));
			}
		}
		questaoConteudoVO.setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(questaoConteudoVO.getConteudoVO().getCodigo(), NivelMontarDados.BASICO, false, usuarioVO));
		questaoVO.getQuestaoConteudoVOs().add(questaoConteudoVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void consultarClonarQuestaoConteudo(Integer codigoNovoConteudo, Integer conteudoAntigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into questaoconteudo (conteudo, questao) (");
		sb.append(" select ").append(codigoNovoConteudo).append(", questao from questaoconteudo where conteudo = ").append(conteudoAntigo);
		sb.append(" and not exists (select qc.codigo from questaoconteudo qc where qc.questao = questaoconteudo.questao and qc.conteudo = ").append(codigoNovoConteudo).append("))");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sb.toString());
	}
}
