package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.QuestaoTrabalhoConclusaoCursoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.QuestaoTrabalhoConclusaoCursoInterfaceFacade;

@Repository
@Lazy
public class QuestaoTrabalhoConclusaoCurso extends ControleAcesso implements QuestaoTrabalhoConclusaoCursoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2237620829245633999L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirQuestaoConteudo(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception {
		for (QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO : trabalhoConclusaoCurso.getQuestaoConteudoVOs()) {
			questaoTrbalhoConclusaoCursoVO.setTrabalhoConclusaoCurso(trabalhoConclusaoCurso);			
			incluir(questaoTrbalhoConclusaoCursoVO, "conteudo");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirQuestaoFormatacao(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception {
		for (QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO : trabalhoConclusaoCurso.getQuestaoFormatacaoVOs()) {
			questaoTrbalhoConclusaoCursoVO.setTrabalhoConclusaoCurso(trabalhoConclusaoCurso);			
			incluir(questaoTrbalhoConclusaoCursoVO, "formatacao");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final QuestaoTrabalhoConclusaoCursoVO questaoVO, final String origemQuestao) throws Exception {
		validarDados(questaoVO);
		questaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO QuestaoTrabalhoConclusaoCurso ");
				sql.append(" (enunciado, trabalhoConclusaoCurso, origemQuestao, valor, valorMaximoNotaFormatacao, valorMaximoNotaConteudo ) ");
				sql.append(" VALUES (?, ?, ?, ?, ?, ?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;				
				ps.setString(x++, questaoVO.getEnunciado());
				ps.setInt(x++, questaoVO.getTrabalhoConclusaoCurso().getCodigo());				
				ps.setString(x++, origemQuestao);
				ps.setDouble(x++, questaoVO.getValor().doubleValue());
				ps.setDouble(x++, questaoVO.getValorMaximoNotaFormatacao().doubleValue());
				ps.setDouble(x++, questaoVO.getValorMaximoNotaConteudo().doubleValue());
				
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
	private void alterar(final QuestaoTrabalhoConclusaoCursoVO questaoVO, final String origemQuestao) throws Exception {
		validarDados(questaoVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE QuestaoTrabalhoConclusaoCurso ");
				sql.append(" SET enunciado=?, trabalhoConclusaoCurso=?, origemQuestao=?, valor=?, valorMaximoNotaFormatacao=?, valorMaximoNotaConteudo=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, questaoVO.getEnunciado());
				ps.setInt(x++, questaoVO.getTrabalhoConclusaoCurso().getCodigo());
				ps.setString(x++, origemQuestao);
				if (questaoVO.getValor() == null) {
					ps.setNull(x++, 0);
				} else {
					ps.setDouble(x++, questaoVO.getValor().doubleValue());
				}				
				
				ps.setDouble(x++, questaoVO.getValorMaximoNotaFormatacao().doubleValue());
				ps.setDouble(x++, questaoVO.getValorMaximoNotaConteudo().doubleValue());
				ps.setInt(x++, questaoVO.getCodigo().intValue());
				return ps;
			}
		}) == 0) {
			return;
		}
		questaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirQuestao(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM QuestaoTrabalhoConclusaoCurso where trabalhoConclusaoCurso = ").append(trabalhoConclusaoCurso.getCodigo());
		sql.append(" and codigo not in (0");
		for (QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO : trabalhoConclusaoCurso.getQuestaoConteudoVOs()) {
			sql.append(", ").append(questaoTrbalhoConclusaoCursoVO.getCodigo());
		}
		for (QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO : trabalhoConclusaoCurso.getQuestaoFormatacaoVOs()) {
			sql.append(", ").append(questaoTrbalhoConclusaoCursoVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarQuestaoFormatacao(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception {
		for (QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO : trabalhoConclusaoCurso.getQuestaoFormatacaoVOs()) {
			questaoTrbalhoConclusaoCursoVO.setTrabalhoConclusaoCurso(trabalhoConclusaoCurso);
			if (questaoTrbalhoConclusaoCursoVO.isNovoObj()) {
				incluir(questaoTrbalhoConclusaoCursoVO, "formatacao");
			} else {
				alterar(questaoTrbalhoConclusaoCursoVO, "formatacao");
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarQuestaoConteudo(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) throws Exception {
		for (QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO : trabalhoConclusaoCurso.getQuestaoConteudoVOs()) {
			questaoTrbalhoConclusaoCursoVO.setTrabalhoConclusaoCurso(trabalhoConclusaoCurso);
			if (questaoTrbalhoConclusaoCursoVO.isNovoObj()) {
				incluir(questaoTrbalhoConclusaoCursoVO, "conteudo");
			} else {
				alterar(questaoTrbalhoConclusaoCursoVO, "conteudo");
			}
		}
	}
			
	@Override
	public List<QuestaoTrabalhoConclusaoCursoVO> consultarPorTrabalhoConclusaoCurso(Integer trabalhoConclusaoCurso, String origemQuestao) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM QuestaoTrabalhoConclusaoCurso where trabalhoConclusaoCurso = ").append(trabalhoConclusaoCurso).append(" and origemQuestao = '").append(origemQuestao).append("' order by codigo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<QuestaoTrabalhoConclusaoCursoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<QuestaoTrabalhoConclusaoCursoVO> vagaQuestaoVOs = new ArrayList<QuestaoTrabalhoConclusaoCursoVO>(0);
		while (rs.next()) {
			vagaQuestaoVOs.add(montarDados(rs));
		}
		return vagaQuestaoVOs;
	}

	private QuestaoTrabalhoConclusaoCursoVO montarDados(SqlRowSet rs) throws Exception {
		QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO = new QuestaoTrabalhoConclusaoCursoVO();
		questaoTrbalhoConclusaoCursoVO.setNovoObj(false);
		questaoTrbalhoConclusaoCursoVO.setCodigo(rs.getInt("codigo"));
		questaoTrbalhoConclusaoCursoVO.getTrabalhoConclusaoCurso().setCodigo(rs.getInt("trabalhoConclusaoCurso"));
		if (rs.getObject("valor") == null) {
			questaoTrbalhoConclusaoCursoVO.setValor((Double) rs.getObject("valor"));
		} else {
			questaoTrbalhoConclusaoCursoVO.setValor(rs.getDouble("valor"));
		}
		questaoTrbalhoConclusaoCursoVO.setValorMaximoNotaFormatacao(rs.getDouble("valorMaximoNotaFormatacao"));
		questaoTrbalhoConclusaoCursoVO.setValorMaximoNotaConteudo(rs.getDouble("valorMaximoNotaConteudo"));
		questaoTrbalhoConclusaoCursoVO.setOrigemQuestao(rs.getString("origemQuestao"));
		questaoTrbalhoConclusaoCursoVO.setEnunciado(rs.getString("enunciado"));
		return questaoTrbalhoConclusaoCursoVO;
	}

//	@Override
//	public void alterarOrdemOpcaoRespostaQuestao(QuestaoTrabalhoConclusaoCursoVO vagaQuestaoVO, OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO opc1, OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO opc2) throws Exception {
//		Integer ordem1 = opc1.getOrdemApresentacao();
//		opc1.setOrdemApresentacao(opc2.getOrdemApresentacao());
//		opc2.setOrdemApresentacao(ordem1);
//		opc1.setLetraCorrespondente(null);
//		opc2.setLetraCorrespondente(null);
//		//Ordenacao.ordenarLista(vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs(), "ordemApresentacao");
//	}
//
//	@Override
//	public void adicionarOrdemOpcaoRespostaQuestao(QuestaoTrabalhoConclusaoCursoVO vagaQuestaoVO, OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO opc1, Boolean validarDados)  throws Exception {
//		if(validarDados){
//			getFacadeFactory().getOpcaoRespostaQuestaoFacade().validarDados(opc1);
//		}
//		if (opc1.getOrdemApresentacao() > 0) {
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs().set(opc1.getOrdemApresentacao() - 1, opc1);
//		} else {
//			opc1.setOrdemApresentacao(vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs().size() + 1);
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs().add(opc1);
//		}
//	}

//	@Override
//	public void removerOrdemOpcaoRespostaQuestao(QuestaoTrabalhoConclusaoCursoVO vagaQuestaoVO, OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO opc1)   throws Exception{
//		if (opc1.getOrdemApresentacao() > 0) {
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs().remove(opc1.getOrdemApresentacao() - 1);
//			int x = 1;
//			for (OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO opcaoRespostaQuestaoTrabalhoConclusaoCursoVO : vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs()) {
//				opcaoRespostaQuestaoTrabalhoConclusaoCursoVO.setOrdemApresentacao(x++);
//			}
//		}
//	}
//
	@Override
	public void validarDados(QuestaoTrabalhoConclusaoCursoVO vagaQuestaoVO) throws ConsistirException {
		ConsistirException ce = null;
		if (vagaQuestaoVO.getEnunciado().trim().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_VagaQuestao_enunciado"));
		}
//		if (!vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL) && vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs().isEmpty()) {
//			ce = ce == null ? new ConsistirException() : ce;
//			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_VagaQuestao_opcaoResposta"));
//		}
//		if(vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL)){
//			vagaQuestaoVO.getOpcaoRespostaQuestaoTrabalhoConclusaoCursoVOs().clear();
//		}

		if (ce != null) {
			throw ce;
		}

	}
	
	@Override
	public QuestaoTrabalhoConclusaoCursoVO novo() throws Exception{
		QuestaoTrabalhoConclusaoCursoVO questaoTrbalhoConclusaoCursoVO = new QuestaoTrabalhoConclusaoCursoVO();
		//questaoTrbalhoConclusaoCursoVO.setTipoVagaQuestao(TipoVagaQuestaoEnum.UNICA_ESCOLHA);
		//adicionarOrdemOpcaoRespostaVagaQuestao(vagaQuestaoVO, new OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO(), false);
		//adicionarOrdemOpcaoRespostaVagaQuestao(vagaQuestaoVO, new OpcaoRespostaQuestaoTrabalhoConclusaoCursoVO(), false);
		return questaoTrbalhoConclusaoCursoVO;
	}

}
