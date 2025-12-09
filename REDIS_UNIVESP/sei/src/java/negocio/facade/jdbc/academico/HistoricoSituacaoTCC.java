package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.HistoricoSituacaoTCCVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.SituacaoTCCEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.HistoricoSituacaoTCCInterfaceFacade;

@Repository
@Lazy
@Scope
public class HistoricoSituacaoTCC extends ControleAcesso implements HistoricoSituacaoTCCInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8174957479324967106L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final HistoricoSituacaoTCCVO historicoSituacaoTCCVO) throws Exception {
		historicoSituacaoTCCVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO HistoricoSituacaoTCC ( ");
				sql.append(" dataSituacao, usuario, situacaoTCC, etapaTCC, trabalhoConclusaoCurso, tipoUsuario ");
				sql.append(") VALUES (?,?,?,?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(historicoSituacaoTCCVO.getDataSituacao()));
				if (historicoSituacaoTCCVO.getUsuario().getCodigo().intValue() == 0) {
					ps.setNull(x++, 0);
				} else {
					ps.setInt(x++, historicoSituacaoTCCVO.getUsuario().getCodigo());
				}
				ps.setString(x++, historicoSituacaoTCCVO.getSituacaoTCC().name());
				ps.setString(x++, historicoSituacaoTCCVO.getEtapaTCC().name());
				ps.setInt(x++, historicoSituacaoTCCVO.getTrabalhoConclusaoCurso().getCodigo());
				ps.setString(x++, historicoSituacaoTCCVO.getTipoUsuario());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return 0;
			}
		}));
		historicoSituacaoTCCVO.setNovoObj(false);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoHistoricoSituacaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, String tipoUsuario, UsuarioVO usuarioVO) throws Exception {
		if (tipoUsuario.equals("Orientador")) {
			tipoUsuario = "Avaliador";
		}
		HistoricoSituacaoTCCVO historicoSituacaoTCCVO = new HistoricoSituacaoTCCVO();
		historicoSituacaoTCCVO.getTrabalhoConclusaoCurso().setCodigo(trabalhoConclusaoCursoVO.getCodigo());
		historicoSituacaoTCCVO.getUsuario().setCodigo(usuarioVO.getCodigo());
		historicoSituacaoTCCVO.getUsuario().setNome(usuarioVO.getNome());
		historicoSituacaoTCCVO.setDataSituacao(new Date());
		historicoSituacaoTCCVO.setEtapaTCC(trabalhoConclusaoCursoVO.getEtapaTCC());
		historicoSituacaoTCCVO.setSituacaoTCC(trabalhoConclusaoCursoVO.getSituacaoTCC());
		historicoSituacaoTCCVO.setTipoUsuario(tipoUsuario);
		incluir(historicoSituacaoTCCVO);
		
		
	}

	private String getSelectCompleto() {
		StringBuilder sql = new StringBuilder("SELECT HistoricoSituacaoTCC.*, usuario.nome as \"usuario.nome\", usuario.tipoUsuario as \"usuario.tipoUsuario\" ");
		sql.append(" from  HistoricoSituacaoTCC ");
		sql.append(" left join Usuario on Usuario.codigo = HistoricoSituacaoTCC.usuario ");
		return sql.toString();
	}

	@Override
	public List<HistoricoSituacaoTCCVO> consultarPorTCC(Integer tcc, Integer limit, Integer offset) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append(" where trabalhoConclusaoCurso = ").append(tcc);
		sql.append(" order by HistoricoSituacaoTCC.dataSituacao desc ");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	private List<HistoricoSituacaoTCCVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<HistoricoSituacaoTCCVO> historicoSituacaoTCCVOs = new ArrayList<HistoricoSituacaoTCCVO>(0);
		while (rs.next()) {
			historicoSituacaoTCCVOs.add(montarDados(rs));
		}
		return historicoSituacaoTCCVOs;
	}

	private HistoricoSituacaoTCCVO montarDados(SqlRowSet rs) throws Exception {
		HistoricoSituacaoTCCVO historicoSituacaoTCCVO = new HistoricoSituacaoTCCVO();
		historicoSituacaoTCCVO.setNovoObj(false);
		historicoSituacaoTCCVO.setCodigo(rs.getInt("codigo"));		
		historicoSituacaoTCCVO.setDataSituacao(rs.getDate("dataSituacao"));
		historicoSituacaoTCCVO.setEtapaTCC(EtapaTCCEnum.valueOf(rs.getString("etapaTCC")));
		historicoSituacaoTCCVO.setSituacaoTCC(SituacaoTCCEnum.valueOf(rs.getString("situacaoTCC")));
		historicoSituacaoTCCVO.getTrabalhoConclusaoCurso().setCodigo(rs.getInt("trabalhoConclusaoCurso"));
		historicoSituacaoTCCVO.getUsuario().setCodigo(rs.getInt("usuario"));
		historicoSituacaoTCCVO.getUsuario().setNome(rs.getString("usuario.nome"));
		historicoSituacaoTCCVO.getUsuario().setTipoUsuario(rs.getString("usuario.tipoUsuario"));
		historicoSituacaoTCCVO.setTipoUsuario(rs.getString("tipoUsuario"));
		
		return historicoSituacaoTCCVO;
	}

	@Override
	public Integer consultarTotalRegistroPorTCC(Integer tcc) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(codigo) as qtde from HistoricoSituacaoTCC ");
		sql.append(" where trabalhoConclusaoCurso = ").append(tcc);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

}
