package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.QuestaoTCCInterfaceFacade;

@Repository
@Lazy
public class QuestaoTCC extends ControleAcesso implements QuestaoTCCInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2237620829245633999L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirQuestaoConteudo(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoTCCVO questaoTCCVO : configuracao.getQuestaoConteudoVOs()) {
			questaoTCCVO.setConfiguracao(configuracao);			
			incluir(questaoTCCVO, "conteudo");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirQuestaoFormatacao(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoTCCVO questaoTCCVO : configuracao.getQuestaoFormatacaoVOs()) {
			questaoTCCVO.setConfiguracao(configuracao);			
			incluir(questaoTCCVO, "formatacao");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final QuestaoTCCVO questaoVO, final String origemQuestao) throws Exception {
		validarDados(questaoVO);
		questaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO QuestaoTCC ");
				sql.append(" (enunciado, configuracaotcc, origemQuestao, valorNotaMaximo ) ");
				sql.append(" VALUES (?, ?, ?, ?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;				
				ps.setString(x++, questaoVO.getEnunciado());
				ps.setInt(x++, questaoVO.getConfiguracao().getCodigo());				
				ps.setString(x++, origemQuestao);
				ps.setDouble(x++, questaoVO.getValorNotaMaximo().doubleValue());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		questaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final QuestaoTCCVO questaoVO, final String origemQuestao) throws Exception {
		validarDados(questaoVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE QuestaoTCC ");
				sql.append(" SET enunciado=?, configuracaotcc=?, origemQuestao=?, valorNotaMaximo=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, questaoVO.getEnunciado());
				ps.setInt(x++, questaoVO.getConfiguracao().getCodigo());
				ps.setString(x++, origemQuestao);
				ps.setDouble(x++, questaoVO.getValorNotaMaximo().doubleValue());
				ps.setInt(x++, questaoVO.getCodigo().intValue());
				return ps;
			}
		}) == 0) {
			return;
		}
		questaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirQuestao(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM QuestaoTCC where configuracao = ").append(configuracao.getCodigo());
		sql.append(" and codigo not in (0");
		for (QuestaoTCCVO questaoTCCVO : configuracao.getQuestaoConteudoVOs()) {
			sql.append(", ").append(questaoTCCVO.getCodigo());
		}
		for (QuestaoTCCVO questaoTCCVO : configuracao.getQuestaoFormatacaoVOs()) {
			sql.append(", ").append(questaoTCCVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarQuestaoFormatacao(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoTCCVO questaoTCCVO : configuracao.getQuestaoFormatacaoVOs()) {
			questaoTCCVO.setConfiguracao(configuracao);
			if (questaoTCCVO.isNovoObj()) {
				incluir(questaoTCCVO, "formatacao");
			} else {
				alterar(questaoTCCVO, "formatacao");
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarQuestaoConteudo(ConfiguracaoTCCVO configuracao, UsuarioVO usuarioVO) throws Exception {
		for (QuestaoTCCVO questaoTCCVO : configuracao.getQuestaoConteudoVOs()) {
			questaoTCCVO.setConfiguracao(configuracao);
			if (questaoTCCVO.isNovoObj()) {
				incluir(questaoTCCVO, "conteudo");
			} else {
				alterar(questaoTCCVO, "conteudo");
			}
		}
	}
			
	@Override
	public List<QuestaoTCCVO> consultarPorConfiguracao(Integer configuracao, String origemQuestao) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM QuestaoTCC where configuracaotcc = ").append(configuracao).append(" and origemQuestao = '").append(origemQuestao).append("' order by codigo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<QuestaoTCCVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<QuestaoTCCVO> vagaQuestaoVOs = new ArrayList<QuestaoTCCVO>(0);
		while (rs.next()) {
			vagaQuestaoVOs.add(montarDados(rs));
		}
		return vagaQuestaoVOs;
	}

	private QuestaoTCCVO montarDados(SqlRowSet rs) throws Exception {
		QuestaoTCCVO questaoTCCVO = new QuestaoTCCVO();
		questaoTCCVO.setNovoObj(false);
		questaoTCCVO.setCodigo(rs.getInt("codigo"));
		questaoTCCVO.getConfiguracao().setCodigo(rs.getInt("configuracaotcc"));
		questaoTCCVO.setOrigemQuestao(rs.getString("origemQuestao"));
		questaoTCCVO.setEnunciado(rs.getString("enunciado"));
		questaoTCCVO.setValorNotaMaximo(rs.getDouble("valorNotaMaximo"));
		return questaoTCCVO;
	}

//	@Override
//	public void alterarOrdemOpcaoRespostaQuestao(QuestaoTCCVO vagaQuestaoVO, OpcaoRespostaQuestaoTCCVO opc1, OpcaoRespostaQuestaoTCCVO opc2) throws Exception {
//		Integer ordem1 = opc1.getOrdemApresentacao();
//		opc1.setOrdemApresentacao(opc2.getOrdemApresentacao());
//		opc2.setOrdemApresentacao(ordem1);
//		opc1.setLetraCorrespondente(null);
//		opc2.setLetraCorrespondente(null);
//		//Ordenacao.ordenarLista(vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs(), "ordemApresentacao");
//	}
//
//	@Override
//	public void adicionarOrdemOpcaoRespostaQuestao(QuestaoTCCVO vagaQuestaoVO, OpcaoRespostaQuestaoTCCVO opc1, Boolean validarDados)  throws Exception {
//		if(validarDados){
//			getFacadeFactory().getOpcaoRespostaQuestaoFacade().validarDados(opc1);
//		}
//		if (opc1.getOrdemApresentacao() > 0) {
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs().set(opc1.getOrdemApresentacao() - 1, opc1);
//		} else {
//			opc1.setOrdemApresentacao(vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs().size() + 1);
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs().add(opc1);
//		}
//	}

//	@Override
//	public void removerOrdemOpcaoRespostaQuestao(QuestaoTCCVO vagaQuestaoVO, OpcaoRespostaQuestaoTCCVO opc1)   throws Exception{
//		if (opc1.getOrdemApresentacao() > 0) {
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs().remove(opc1.getOrdemApresentacao() - 1);
//			int x = 1;
//			for (OpcaoRespostaQuestaoTCCVO opcaoRespostaQuestaoTCCVO : vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs()) {
//				opcaoRespostaQuestaoTCCVO.setOrdemApresentacao(x++);
//			}
//		}
//	}
//
	@Override
	public void validarDados(QuestaoTCCVO vagaQuestaoVO) throws ConsistirException {
		ConsistirException ce = null;
		if (vagaQuestaoVO.getEnunciado().trim().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_VagaQuestao_enunciado"));
		}
//		if (!vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL) && vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs().isEmpty()) {
//			ce = ce == null ? new ConsistirException() : ce;
//			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_VagaQuestao_opcaoResposta"));
//		}
//		if(vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL)){
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTCCVOs().clear();
//		}

		if (ce != null) {
			throw ce;
		}

	}
	
	@Override
	public QuestaoTCCVO novo() throws Exception{
		QuestaoTCCVO questaoTCCVO = new QuestaoTCCVO();
		//questaoTCCVO.setTipoVagaQuestao(TipoVagaQuestaoEnum.UNICA_ESCOLHA);
		//adicionarOrdemOpcaoRespostaVagaQuestao(vagaQuestaoVO, new OpcaoRespostaQuestaoTCCVO(), false);
		//adicionarOrdemOpcaoRespostaVagaQuestao(vagaQuestaoVO, new OpcaoRespostaQuestaoTCCVO(), false);
		return questaoTCCVO;
	}

}
