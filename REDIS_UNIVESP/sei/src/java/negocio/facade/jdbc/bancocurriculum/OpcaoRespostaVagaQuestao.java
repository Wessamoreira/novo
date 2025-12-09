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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.OpcaoRespostaVagaQuestaoInterfaceFacade;

@Repository
@Lazy
public class OpcaoRespostaVagaQuestao extends ControleAcesso implements OpcaoRespostaVagaQuestaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5230248252176529658L;

	@Override
	public void incluirOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, UsuarioVO usuarioVO) throws Exception {
		for (OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO: vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs()) {
			opcaoRespostaVagaQuestaoVO.setVagaQuestao(vagaQuestaoVO);
			incluir(opcaoRespostaVagaQuestaoVO);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO) throws Exception  {
		validarDados(opcaoRespostaVagaQuestaoVO);
		opcaoRespostaVagaQuestaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO OpcaoRespostaVagaQuestao ");
				sql.append(" (vagaQuestao, opcaoResposta, ordemApresentacao ) ");
				sql.append(" VALUES (?, ?, ?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, opcaoRespostaVagaQuestaoVO.getVagaQuestao().getCodigo());
				ps.setString(x++, opcaoRespostaVagaQuestaoVO.getOpcaoResposta());				
				ps.setInt(x++, opcaoRespostaVagaQuestaoVO.getOrdemApresentacao());
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
		opcaoRespostaVagaQuestaoVO.setNovoObj(false);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO) throws Exception {
		validarDados(opcaoRespostaVagaQuestaoVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE OpcaoRespostaVagaQuestao ");
				sql.append(" SET vagaQuestao = ?, opcaoResposta=?, ordemApresentacao=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, opcaoRespostaVagaQuestaoVO.getVagaQuestao().getCodigo());
				ps.setString(x++, opcaoRespostaVagaQuestaoVO.getOpcaoResposta());
				ps.setInt(x++, opcaoRespostaVagaQuestaoVO.getOrdemApresentacao());
				ps.setInt(x++, opcaoRespostaVagaQuestaoVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(opcaoRespostaVagaQuestaoVO);
			return;
		}
		opcaoRespostaVagaQuestaoVO.setNovoObj(false);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirVagaQuestao(VagaQuestaoVO vagaQuestaoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM OpcaoRespostaVagaQuestao where vagaQuestao = ").append(vagaQuestaoVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO: vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs()) {
			sql.append(", ").append(opcaoRespostaVagaQuestaoVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public void alteraOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, UsuarioVO usuarioVO) throws Exception {
		excluirVagaQuestao(vagaQuestaoVO, usuarioVO);
		for (OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO: vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs()) {
			opcaoRespostaVagaQuestaoVO.setVagaQuestao(vagaQuestaoVO);
			if (opcaoRespostaVagaQuestaoVO.isNovoObj()) {
				incluir(opcaoRespostaVagaQuestaoVO);
			} else {
				alterar(opcaoRespostaVagaQuestaoVO);
			}
		}

	}

	@Override
	public List<OpcaoRespostaVagaQuestaoVO> consultarPorVagaQuestao(Integer vagaQuestao) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM opcaoRespostaVagaQuestao where vagaQuestao = ").append(vagaQuestao).append(" order by ordemApresentacao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	private List<OpcaoRespostaVagaQuestaoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<OpcaoRespostaVagaQuestaoVO> vagaQuestaoVOs = new ArrayList<OpcaoRespostaVagaQuestaoVO>(0);
		while (rs.next()) {
			vagaQuestaoVOs.add(montarDados(rs));
		}
		return vagaQuestaoVOs;
	}

	private OpcaoRespostaVagaQuestaoVO montarDados(SqlRowSet rs) throws Exception {
		OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO = new OpcaoRespostaVagaQuestaoVO();
		opcaoRespostaVagaQuestaoVO.setNovoObj(false);
		opcaoRespostaVagaQuestaoVO.setCodigo(rs.getInt("codigo"));
		opcaoRespostaVagaQuestaoVO.getVagaQuestao().setCodigo(rs.getInt("vagaQuestao"));
		opcaoRespostaVagaQuestaoVO.setOrdemApresentacao(rs.getInt("ordemApresentacao"));
		opcaoRespostaVagaQuestaoVO.setOpcaoResposta(rs.getString("opcaoResposta"));
		return opcaoRespostaVagaQuestaoVO;
	}
	
	@Override
	public void validarDados(OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO) throws ConsistirException {
		ConsistirException ce = null;
		if (Uteis.retiraTags(opcaoRespostaVagaQuestaoVO.getOpcaoResposta()).trim().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_OpcaoRespostaVagaQuestao_enunciado"));
		}
		

		if (ce != null) {
			throw ce;
		}

	}

}
