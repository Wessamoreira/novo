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
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.bancocurriculum.enumeradores.TipoVagaQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.CandidatoVagaQuestaoInterfaceFacade;

@Repository
@Lazy
public class CandidatoVagaQuestao extends ControleAcesso implements CandidatoVagaQuestaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3082910112675753844L;

	@Override
	public void incluirCandidatoVagaQuestao(CandidatosVagasVO candidatosVagasVO) throws Exception {
		validarDados(candidatosVagasVO);
		for (CandidatoVagaQuestaoVO candidatoVagaQuestaoVO : candidatosVagasVO.getCandidatoVagaQuestaoVOs()) {
			candidatoVagaQuestaoVO.setCandidatosVagas(candidatosVagasVO);
			incluir(candidatoVagaQuestaoVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception {
		
		candidatoVagaQuestaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO candidatoVagaQuestao ");
				sql.append(" (candidatosVagas, vagaQuestao, respostaTextual ) ");
				sql.append(" VALUES (?, ?, ?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, candidatoVagaQuestaoVO.getCandidatosVagas().getCodigo());
				ps.setInt(x++, candidatoVagaQuestaoVO.getVagaQuestao().getCodigo());
				ps.setString(x++, candidatoVagaQuestaoVO.getRespostaTextual());
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
		getFacadeFactory().getCandidatoVagaQuestaoRespostaFacade().incluirCandidatoVagaQuestaoResposta(candidatoVagaQuestaoVO);
		candidatoVagaQuestaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception {
		
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE candidatoVagaQuestao ");
				sql.append(" SET candidatosVagas=?, vagaQuestao=?, respostaTextual = ?");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, candidatoVagaQuestaoVO.getCandidatosVagas().getCodigo());
				ps.setInt(x++, candidatoVagaQuestaoVO.getVagaQuestao().getCodigo());
				ps.setString(x++, candidatoVagaQuestaoVO.getRespostaTextual());
				ps.setInt(x++, candidatoVagaQuestaoVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(candidatoVagaQuestaoVO);
			return;
		}
		getFacadeFactory().getCandidatoVagaQuestaoRespostaFacade().alteraCandidatoVagaQuestaoResposta(candidatoVagaQuestaoVO);
		candidatoVagaQuestaoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirVagaQuestao(CandidatosVagasVO candidatosVagasVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM candidatoVagaQuestao where candidatosVagas = ").append(candidatosVagasVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (CandidatoVagaQuestaoVO candidatoVagaQuestaoVO : candidatosVagasVO.getCandidatoVagaQuestaoVOs()) {
			sql.append(", ").append(candidatoVagaQuestaoVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public void alteraCandidatoVagaQuestao(CandidatosVagasVO candidatosVagasVO) throws Exception {
		validarDados(candidatosVagasVO);
		excluirVagaQuestao(candidatosVagasVO);
		for (CandidatoVagaQuestaoVO candidatoVagaQuestaoVO : candidatosVagasVO.getCandidatoVagaQuestaoVOs()) {
			candidatoVagaQuestaoVO.setCandidatosVagas(candidatosVagasVO);
			if (candidatoVagaQuestaoVO.isNovoObj()) {
				incluir(candidatoVagaQuestaoVO);
			} else {
				alterar(candidatoVagaQuestaoVO);
			}
		}

	}

	@Override
	public List<CandidatoVagaQuestaoVO> consultarPorCandidatoVaga(Integer candidatoVaga, Integer vaga) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select candidatoVagaQuestao.codigo, candidatoVagaQuestao.candidatosVagas, candidatoVagaQuestao.respostaTextual, ");
		sql.append(" vagaquestao.codigo as vagaquestao_codigo, vagaquestao.vaga as vagaquestao_vaga, ");
		sql.append(" vagaquestao.enunciado as  vagaquestao_enunciado, vagaquestao.tipoVagaQuestao as  vagaquestao_tipoVagaQuestao,");
		sql.append(" vagaquestao.ordemApresentacao as vagaquestao_ordemApresentacao");
		sql.append(" from vagaquestao  ");
		sql.append(" left join candidatoVagaQuestao on candidatoVagaQuestao.candidatosVagas = ").append(candidatoVaga);
		sql.append(" and vagaquestao.codigo = candidatoVagaQuestao.vagaquestao");
		sql.append(" where vagaquestao.vaga = ").append(vaga);
		sql.append(" order by vagaquestao.ordemApresentacao ");
		List<CandidatoVagaQuestaoVO> candidatoVagaQuestaoVOs = new ArrayList<CandidatoVagaQuestaoVO>(0);
		CandidatoVagaQuestaoVO candidatoVagaQuestaoVO = null;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			candidatoVagaQuestaoVO = new CandidatoVagaQuestaoVO();
			candidatoVagaQuestaoVO.setCodigo(rs.getInt("codigo"));
			candidatoVagaQuestaoVO.setRespostaTextual(rs.getString("respostaTextual"));
			candidatoVagaQuestaoVO.getCandidatosVagas().setCodigo(rs.getInt("candidatosVagas"));
			if (candidatoVagaQuestaoVO.getCodigo() > 0) {
				candidatoVagaQuestaoVO.setNovoObj(false);
			}
			candidatoVagaQuestaoVO.getVagaQuestao().setCodigo(rs.getInt("vagaquestao_codigo"));
			candidatoVagaQuestaoVO.getVagaQuestao().getVaga().setCodigo(rs.getInt("vagaquestao_vaga"));
			candidatoVagaQuestaoVO.getVagaQuestao().setOrdemApresentacao(rs.getInt("vagaquestao_ordemApresentacao"));
			candidatoVagaQuestaoVO.getVagaQuestao().setEnunciado(rs.getString("vagaquestao_enunciado"));
			candidatoVagaQuestaoVO.getVagaQuestao().setTipoVagaQuestao(TipoVagaQuestaoEnum.valueOf(rs.getString("vagaquestao_tipoVagaQuestao")));
			candidatoVagaQuestaoVO.getVagaQuestao().setNovoObj(true);
			candidatoVagaQuestaoVO.setCandidatoVagaQuestaoRespostaVOs(getFacadeFactory().getCandidatoVagaQuestaoRespostaFacade().consultarPorCandidatoVagaQuestao(candidatoVagaQuestaoVO.getCodigo(), candidatoVagaQuestaoVO.getVagaQuestao().getCodigo()));
			candidatoVagaQuestaoVOs.add(candidatoVagaQuestaoVO);
		}
		return candidatoVagaQuestaoVOs;
	}

	@Override
	public void validarDados(CandidatosVagasVO candidatosVagasVO) throws ConsistirException {
		for (CandidatoVagaQuestaoVO candidatoVagaQuestaoVO : candidatosVagasVO.getCandidatoVagaQuestaoVOs()) {
			if (candidatoVagaQuestaoVO.getVagaQuestao().getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL) && candidatoVagaQuestaoVO.getRespostaTextual().trim().isEmpty()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_CandidatoVagaQuestao_respostaObrigatoria").replace("{0}", candidatoVagaQuestaoVO.getVagaQuestao().getOrdemApresentacao().toString()));
			}
			if (!candidatoVagaQuestaoVO.getVagaQuestao().getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL)) {
				boolean selecionou = false;
				for (CandidatoVagaQuestaoRespostaVO resposta : candidatoVagaQuestaoVO.getCandidatoVagaQuestaoRespostaVOs()) {
					if (resposta.getSelecionada()) {
						selecionou = true;
						break;
					}
				}
				if (!selecionou) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_CandidatoVagaQuestao_respostaObrigatoria").replace("{0}", candidatoVagaQuestaoVO.getVagaQuestao().getOrdemApresentacao().toString()));
				}
			}
		}
	}

	@Override
	public void realizarVerificacaoQuestaoUnicaEscolha(CandidatosVagasVO candidatosVagasVO, CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO) {
		if (candidatoVagaQuestaoRespostaVO.getSelecionada()) {
			for (CandidatoVagaQuestaoVO candidatoVagaQuestaoVO : candidatosVagasVO.getCandidatoVagaQuestaoVOs()) {
				if (candidatoVagaQuestaoVO.getVagaQuestao().getCodigo().intValue() == candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().getVagaQuestao().getCodigo().intValue()) {
					if (candidatoVagaQuestaoVO.getVagaQuestao().getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.UNICA_ESCOLHA)) {
						for (CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO2 : candidatoVagaQuestaoVO.getCandidatoVagaQuestaoRespostaVOs()) {
							if (candidatoVagaQuestaoRespostaVO2.getOpcaoRespostaVagaQuestao().getCodigo().intValue() != candidatoVagaQuestaoRespostaVO.getOpcaoRespostaVagaQuestao().getCodigo().intValue()) {
								candidatoVagaQuestaoRespostaVO2.setSelecionada(false);
							}
						}
					}
					return;
				}
			}
		}
	}

}
