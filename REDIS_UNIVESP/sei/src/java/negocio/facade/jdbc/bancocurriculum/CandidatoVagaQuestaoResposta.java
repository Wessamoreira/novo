package negocio.facade.jdbc.bancocurriculum;

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

import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoRespostaVO;
import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.CandidatoVagaQuestaoRespostaInterfaceFacade;

@Repository
@Lazy
public class CandidatoVagaQuestaoResposta extends ControleAcesso implements CandidatoVagaQuestaoRespostaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1881895508358314958L;

	@Override
	public void incluirCandidatoVagaQuestaoResposta(CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception {
		for (CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO : candidatoVagaQuestaoVO.getCandidatoVagaQuestaoRespostaVOs()) {
			candidatoVagaQuestaoRespostaVO.setCandidatoVagaQuestao(candidatoVagaQuestaoVO);
			if (candidatoVagaQuestaoRespostaVO.getSelecionada()) {
				incluir(candidatoVagaQuestaoRespostaVO);
			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO) throws Exception {
		candidatoVagaQuestaoRespostaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO CandidatoVagaQuestaoResposta ");
				sql.append(" (candidatoVagaQuestao, opcaoRespostaVagaQuestao ) ");
				sql.append(" VALUES (?, ?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, candidatoVagaQuestaoRespostaVO.getCandidatoVagaQuestao().getCodigo());
				ps.setInt(x++, candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().getCodigo());
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
		candidatoVagaQuestaoRespostaVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO) throws Exception {

		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE CandidatoVagaQuestaoResposta ");
				sql.append(" SET candidatoVagaQuestao=?, opcaoRespostaVagaQuestao=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, candidatoVagaQuestaoRespostaVO.getCandidatoVagaQuestao().getCodigo());
				ps.setInt(x++, candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().getCodigo());
				ps.setInt(x++, candidatoVagaQuestaoRespostaVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(candidatoVagaQuestaoRespostaVO);
			return;
		}
		candidatoVagaQuestaoRespostaVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirCandidatoVagaQuestaoResposta(CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM CandidatoVagaQuestaoResposta where candidatoVagaQuestao = ").append(candidatoVagaQuestaoVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO : candidatoVagaQuestaoVO.getCandidatoVagaQuestaoRespostaVOs()) {
			if (candidatoVagaQuestaoRespostaVO.getSelecionada()) {
				sql.append(", ").append(candidatoVagaQuestaoRespostaVO.getCodigo());
			}
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public void alteraCandidatoVagaQuestaoResposta(CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception {
		excluirCandidatoVagaQuestaoResposta(candidatoVagaQuestaoVO);
		for (CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO : candidatoVagaQuestaoVO.getCandidatoVagaQuestaoRespostaVOs()) {
			candidatoVagaQuestaoRespostaVO.setCandidatoVagaQuestao(candidatoVagaQuestaoVO);
			if (candidatoVagaQuestaoRespostaVO.getSelecionada()) {
				if (candidatoVagaQuestaoRespostaVO.isNovoObj()) {
					incluir(candidatoVagaQuestaoRespostaVO);
				} else {
					alterar(candidatoVagaQuestaoRespostaVO);
				}
			}
		}
	}

	@Override
	public List<CandidatoVagaQuestaoRespostaVO> consultarPorCandidatoVagaQuestao(Integer candidatoVagaQuestao, Integer vagaQuestao) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select CandidatoVagaQuestaoResposta.codigo, CandidatoVagaQuestaoResposta.CandidatoVagaQuestao,");
		sql.append(" opcaorespostavagaquestao.codigo as opcaorespostavagaquestao_codigo,");
		sql.append(" opcaorespostavagaquestao.vagaQuestao as opcaorespostavagaquestao_vagaQuestao,");
		sql.append(" opcaorespostavagaquestao.opcaoResposta as opcaorespostavagaquestao_opcaoResposta,");
		sql.append(" opcaorespostavagaquestao.ordemApresentacao as opcaorespostavagaquestao_ordemApresentacao");
		sql.append(" from opcaorespostavagaquestao    ");
		sql.append(" left join CandidatoVagaQuestaoResposta on CandidatoVagaQuestaoResposta.CandidatoVagaQuestao = ").append(candidatoVagaQuestao);
		sql.append(" and opcaorespostavagaquestao.codigo = CandidatoVagaQuestaoResposta.opcaorespostavagaquestao");
		sql.append(" where opcaorespostavagaquestao.vagaquestao = ").append(vagaQuestao);
		sql.append(" order by opcaorespostavagaquestao.ordemApresentacao");
		List<CandidatoVagaQuestaoRespostaVO> candidatoVagaQuestaoRespostaVOs = new ArrayList<CandidatoVagaQuestaoRespostaVO>(0);
		CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO = null;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			candidatoVagaQuestaoRespostaVO = new CandidatoVagaQuestaoRespostaVO();
			candidatoVagaQuestaoRespostaVO.setCodigo(rs.getInt("codigo"));
			if (candidatoVagaQuestaoRespostaVO.getCodigo() > 0) {
				candidatoVagaQuestaoRespostaVO.setNovoObj(false);
				candidatoVagaQuestaoRespostaVO.setSelecionada(true);
			}
			candidatoVagaQuestaoRespostaVO.getCandidatoVagaQuestao().setCodigo(rs.getInt("candidatoVagaQuestao"));
			candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().setCodigo(rs.getInt("opcaorespostavagaquestao_codigo"));
			candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().getVagaQuestao().setCodigo(rs.getInt("opcaorespostavagaquestao_vagaQuestao"));
			candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().setOrdemApresentacao(rs.getInt("opcaorespostavagaquestao_ordemApresentacao"));
			candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().setOpcaoResposta(rs.getString("opcaorespostavagaquestao_opcaoResposta"));
			candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().setNovoObj(false);
			
			candidatoVagaQuestaoRespostaVOs.add(candidatoVagaQuestaoRespostaVO);
		}

		return candidatoVagaQuestaoRespostaVOs;
	}

}
