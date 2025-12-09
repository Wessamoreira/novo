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

import negocio.comuns.academico.CriterioAvaliacaoDisciplinaEixoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.AvaliarNaoAvaliarEnum;
import negocio.comuns.academico.enumeradores.OrigemCriterioAvaliacaoIndicadorEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CriterioAvaliacaoIndicadorInterfaceFacade;

@Repository
@Lazy
public class CriterioAvaliacaoIndicador extends ControleAcesso implements CriterioAvaliacaoIndicadorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3342420559481316186L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws Exception {
		validarDados(criterioAvaliacaoIndicadorVO);
		try {
			criterioAvaliacaoIndicadorVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO criterioAvaliacaoIndicador (");
					sql.append(" criterioAvaliacaoPeriodoLetivo, criterioAvaliacaoDisciplinaEixoIndicador, avaliarPrimeiroBimestre, avaliarSegundoBimestre, ");
					sql.append(" avaliarTerceiroBimestre, avaliarQuartoBimestre, ordem, origemCriterioAvaliacaoIndicador, descricao, nota1Bimestre, nota2Bimestre, nota3Bimestre, nota4Bimestre ) ");
					sql.append(" VALUES (? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					if (criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo() > 0) {
						ps.setInt(x++, criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoDisciplinaEixoIndicador().getCodigo() > 0) {
						ps.setInt(x++, criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoDisciplinaEixoIndicador().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().name());
					ps.setInt(x++, criterioAvaliacaoIndicadorVO.getOrdem());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getOrigemCriterioAvaliacaoIndicador().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getDescricao());
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota1Bimestre());					
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota2Bimestre());					
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota3Bimestre());					
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota4Bimestre());					
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
			
			criterioAvaliacaoIndicadorVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoIndicadorVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws Exception {
		validarDados(criterioAvaliacaoIndicadorVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE criterioAvaliacaoIndicador SET ");
					sql.append(" criterioAvaliacaoPeriodoLetivo = ?, criterioAvaliacaoDisciplinaEixoIndicador = ?, avaliarPrimeiroBimestre = ?, avaliarSegundoBimestre = ?, ");
					sql.append(" avaliarTerceiroBimestre = ?, avaliarQuartoBimestre = ?, ordem = ?, origemCriterioAvaliacaoIndicador = ?, descricao = ?, nota1Bimestre = ?, nota2Bimestre = ? , nota3Bimestre = ?, nota4Bimestre = ?");
					sql.append(" where codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					if (criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo() > 0) {
						ps.setInt(x++, criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoDisciplinaEixoIndicador().getCodigo() > 0) {
						ps.setInt(x++, criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoDisciplinaEixoIndicador().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().name());
					ps.setInt(x++, criterioAvaliacaoIndicadorVO.getOrdem());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getOrigemCriterioAvaliacaoIndicador().name());
					ps.setString(x++, criterioAvaliacaoIndicadorVO.getDescricao());
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota1Bimestre());	
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota2Bimestre());	
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota3Bimestre());	
					ps.setDouble(x++, criterioAvaliacaoIndicadorVO.getNota4Bimestre());	
					ps.setInt(x++, criterioAvaliacaoIndicadorVO.getCodigo());	
					return ps;
				}
			}) == 0) {
				incluir(criterioAvaliacaoIndicadorVO);
				return;
			}			
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void incluirCriterioAvaliacaoIndicadorPorEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		for(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO:criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()){
			criterioAvaliacaoIndicadorVO.setCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO);
			criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA);
			incluir(criterioAvaliacaoIndicadorVO);
		}
	}

	@Override
	public void alterarCriterioAvaliacaoIndicadorPorEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		excluirCriterioAvaliacaoIndicadorPorEixoIndicadorVO(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		for(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO:criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()){
			criterioAvaliacaoIndicadorVO.setCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO);
			criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA);
			if(criterioAvaliacaoIndicadorVO.getNovoObj()){
			incluir(criterioAvaliacaoIndicadorVO);
			}else{
				alterar(criterioAvaliacaoIndicadorVO);
			}
		}

	}

	@Override
	public void excluirCriterioAvaliacaoIndicadorPorEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		StringBuilder sql = new StringBuilder("delete from CriterioAvaliacaoIndicador where criterioAvaliacaoDisciplinaEixoIndicador = ? and codigo not in (0 ");
		for(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO:criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()){
			sql.append(", ").append(criterioAvaliacaoIndicadorVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoDisciplinaEixoIndicadorVO.getCodigo());
	}

	@Override
	public void incluirCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		for(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO:criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()){
			criterioAvaliacaoIndicadorVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
			criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.GERAL);
			incluir(criterioAvaliacaoIndicadorVO);
		}

	}

	@Override
	public void alterarCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		excluirCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(criterioAvaliacaoPeriodoLetivoVO);
		for(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO:criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()){
			criterioAvaliacaoIndicadorVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
			criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.GERAL);
			if(criterioAvaliacaoIndicadorVO.getNovoObj()){
				incluir(criterioAvaliacaoIndicadorVO);
			}else{
				alterar(criterioAvaliacaoIndicadorVO);
			}
		}

	}

	@Override
	public void excluirCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		StringBuilder sql = new StringBuilder("delete from CriterioAvaliacaoIndicador where criterioAvaliacaoPeriodoLetivo = ? and codigo not in (0 ");
		for(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO:criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()){
			sql.append(", ").append(criterioAvaliacaoIndicadorVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoPeriodoLetivoVO.getCodigo());

	}

	@Override
	public void validarDados(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws ConsistirException {
		if(criterioAvaliacaoIndicadorVO.getDescricao().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoIndicador_descricao"));
		}
	}
	
	public StringBuilder getSqlBasico(){
		StringBuilder sql  = new StringBuilder("Select * from CriterioAvaliacaoIndicador");
		return sql;
	}

	@Override
	public List<CriterioAvaliacaoIndicadorVO> consultarPorCriterioAvalicaoDisciplinaPorEixoIndicador(Integer criterioAvaliacaoDisciplinaEixoIndicador) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" where criterioAvaliacaoDisciplinaEixoIndicador = ? order by ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacaoDisciplinaEixoIndicador));
	}

	@Override
	public List<CriterioAvaliacaoIndicadorVO> consultarPorCriterioAvalicaoDisciplinaPorPeriodoLetivo(Integer criterioAvaliacaoPeriodoLetivo) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" where criterioAvaliacaoPeriodoLetivo = ? order by ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacaoPeriodoLetivo));
	}
	
	private List<CriterioAvaliacaoIndicadorVO> montarDadosConsulta(SqlRowSet rs){
		List<CriterioAvaliacaoIndicadorVO> criterioAvaliacaoIndicadorVOs = new ArrayList<CriterioAvaliacaoIndicadorVO>(0);
		while(rs.next()){
			criterioAvaliacaoIndicadorVOs.add(montarDados(rs));
		}
		return criterioAvaliacaoIndicadorVOs;
	}
	private CriterioAvaliacaoIndicadorVO montarDados(SqlRowSet rs){
		CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO = new CriterioAvaliacaoIndicadorVO();
		criterioAvaliacaoIndicadorVO.setNovoObj(false);
		criterioAvaliacaoIndicadorVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoDisciplinaEixoIndicador().setCodigo(rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador"));
		criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoPeriodoLetivo().setCodigo(rs.getInt("criterioAvaliacaoPeriodoLetivo"));
		criterioAvaliacaoIndicadorVO.setOrdem(rs.getInt("ordem"));
		criterioAvaliacaoIndicadorVO.setAvaliarPrimeiroBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("avaliarPrimeiroBimestre")));
		criterioAvaliacaoIndicadorVO.setAvaliarSegundoBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("avaliarSegundoBimestre")));
		criterioAvaliacaoIndicadorVO.setAvaliarTerceiroBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("avaliarTerceiroBimestre")));
		criterioAvaliacaoIndicadorVO.setAvaliarQuartoBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("avaliarQuartoBimestre")));
		criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("origemCriterioAvaliacaoIndicador")));
		criterioAvaliacaoIndicadorVO.setDescricao(rs.getString("descricao"));
		criterioAvaliacaoIndicadorVO.setNota1Bimestre(rs.getDouble("nota1Bimestre"));
		criterioAvaliacaoIndicadorVO.setNota2Bimestre(rs.getDouble("nota2Bimestre"));
		criterioAvaliacaoIndicadorVO.setNota3Bimestre(rs.getDouble("nota3Bimestre"));
		criterioAvaliacaoIndicadorVO.setNota4Bimestre(rs.getDouble("nota4Bimestre"));
		return criterioAvaliacaoIndicadorVO;
	}

}
