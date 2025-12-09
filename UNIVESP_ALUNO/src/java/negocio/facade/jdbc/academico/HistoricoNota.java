package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.HistoricoNotaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HistoricoNotaInterfaceFacade;

@Repository
public class HistoricoNota extends ControleAcesso implements HistoricoNotaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2540257013526895814L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HistoricoNotaVO historicoNotaVO) throws Exception {
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE HistoricoNota SET notaRecuperacao = ?, agrupamentoNota = ?, situacaoRecuperacaoNota = ? ");
				sql.append(" where historico= ? and tiponota = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setBoolean(x++, historicoNotaVO.getNotaRecuperacao());
				ps.setString(x++, historicoNotaVO.getAgrupamentoNota().name());
				ps.setString(x++, historicoNotaVO.getSituacaoRecuperacaoNota().name());
				ps.setInt(x++, historicoNotaVO.getHistorico().getCodigo());
				ps.setString(x++, historicoNotaVO.getTipoNota().name());
				return ps;
			}
		}) == 0) {
			incluir(historicoNotaVO);
			return;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HistoricoNotaVO historicoNotaVO) throws Exception {
		historicoNotaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO HistoricoNota  ");
				sql.append(" (notaRecuperacao, agrupamentoNota, situacaoRecuperacaoNota, historico, tiponota ) ");
				sql.append(" values (?, ?, ?, ?, ?) ");
				sql.append(" returning codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setBoolean(x++, historicoNotaVO.getNotaRecuperacao());
				ps.setString(x++, historicoNotaVO.getAgrupamentoNota().name());
				ps.setString(x++, historicoNotaVO.getSituacaoRecuperacaoNota().name());
				ps.setInt(x++, historicoNotaVO.getHistorico().getCodigo());
				ps.setString(x++, historicoNotaVO.getTipoNota().name());				
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
		historicoNotaVO.setNovoObj(false);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirHistoricoNotaVOs(HistoricoVO historicoVO) throws Exception {
		excluirHistoricoNotaVOs(historicoVO);
		for (HistoricoNotaVO historicoNotaVO : historicoVO.getHistoricoNotaVOs()) {
			alterar(historicoNotaVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirHistoricoNotaVOs(HistoricoVO historicoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoNota where historico = " + historicoVO.getCodigo() + " and tipoNota not in ('NOTA_0'");
		for (HistoricoNotaVO historicoNotaVO : historicoVO.getHistoricoNotaVOs()) {
			sql.append(", '").append(historicoNotaVO.getTipoNota().name()).append("'");
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().execute(sql.toString());
	}

	@Override
	public List<HistoricoNotaVO> consultarHistoricoNotaVOs(Integer historico) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM HistoricoNota where historico = ? order by tiponota");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), historico));
	}

	private List<HistoricoNotaVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<HistoricoNotaVO> objs = new ArrayList<HistoricoNotaVO>();
		while (rs.next()) {
			objs.add(montarDados(rs));
		}
		return objs;
	}

	private HistoricoNotaVO montarDados(SqlRowSet rs) throws Exception {
		HistoricoNotaVO obj = new HistoricoNotaVO();
		obj.setNovoObj(false);
		if (rs.getString("agrupamentoNota") != null && !rs.getString("agrupamentoNota").trim().isEmpty()) {
			obj.setAgrupamentoNota(BimestreEnum.valueOf(rs.getString("agrupamentoNota")));
		}
		obj.setCodigo(rs.getInt("codigo"));
		obj.getHistorico().setCodigo(rs.getInt("historico"));
		obj.setNotaRecuperacao(rs.getBoolean("notaRecuperacao"));
		if (rs.getString("situacaoRecuperacaoNota") != null && !rs.getString("situacaoRecuperacaoNota").trim().isEmpty()) {
			obj.setSituacaoRecuperacaoNota(SituacaoRecuperacaoNotaEnum.valueOf(rs.getString("situacaoRecuperacaoNota")));
		}
		obj.setTipoNota(TipoNotaConceitoEnum.valueOf(rs.getString("tipoNota")));

		return obj;
	}

	@Override
	public HistoricoNotaVO consultarHistoricoNotaPorHistoricoTipoNota(Integer historico, TipoNotaConceitoEnum tipoNota) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM HistoricoNota where historico = ? and tiponota = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), historico, tipoNota.name());
		if (rs.next()) {
			return montarDados(rs);
		}
		return null;
	}

	@Override
	public HistoricoNotaVO consultarPorChavePrimaria(Integer historicoNota) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM HistoricoNota where codigo = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), historicoNota);
		if (rs.next()) {
			return montarDados(rs);
		}
		return null;
	}
	
	@Override
	public Integer consultarQtdeDisciplinaEmRecuperacaoPorMatriculaAnoSemestreTipoNota(String matricula, String ano, String semestre, TipoNotaConceitoEnum tipoNota){
		StringBuilder sql = new StringBuilder("select count(historiconota.codigo) as qtde from historiconota ");
		sql.append(" inner join historico on historico.codigo = historiconota.historico ");
		sql.append(" where matricula = '").append(matricula).append("' ");
		if(tipoNota != null){
			sql.append(" and tipoNota = '").append(tipoNota.name()).append("' ");			
		}
		if(ano != null && !ano.trim().isEmpty()){
			sql.append(" and anohistorico = '").append(ano).append("' ");			
		}
		if(semestre != null && !semestre.trim().isEmpty()){
			sql.append(" and semestrehistorico = '").append(semestre).append("' ");			
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

}
