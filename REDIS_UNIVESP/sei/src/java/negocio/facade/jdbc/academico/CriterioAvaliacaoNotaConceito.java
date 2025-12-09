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

import negocio.comuns.academico.CriterioAvaliacaoNotaConceitoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CriterioAvaliacaoNotaConceitoInterfaceFacade;

@Repository
@Lazy
public class CriterioAvaliacaoNotaConceito extends ControleAcesso implements CriterioAvaliacaoNotaConceitoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1049889030109880690L;
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO) throws Exception {
		validarDados(criterioAvaliacaoNotaConceitoVO);
		try {
			criterioAvaliacaoNotaConceitoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO criterioAvaliacaoNotaConceito (");
					sql.append(" criterioAvaliacaoPeriodoLetivo, notaConceitoIndicadorAvaliacao, peso, ordem ) ");
					sql.append(" VALUES (? , ?,  ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getCodigo());
					ps.setDouble(x++, criterioAvaliacaoNotaConceitoVO.getPeso());
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getOrdem());
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
			
			criterioAvaliacaoNotaConceitoVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoNotaConceitoVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO) throws Exception {
		validarDados(criterioAvaliacaoNotaConceitoVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE criterioAvaliacaoNotaConceito SET ");
					sql.append(" criterioAvaliacaoPeriodoLetivo =?, notaConceitoIndicadorAvaliacao =?, peso =?, ordem =? ");
					sql.append(" WHERE codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getCodigo());
					ps.setDouble(x++, criterioAvaliacaoNotaConceitoVO.getPeso());
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getOrdem());
					ps.setInt(x++, criterioAvaliacaoNotaConceitoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(criterioAvaliacaoNotaConceitoVO);
				return;
			}
			
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void incluirCriterioAvaliacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		for(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO: criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()){
			criterioAvaliacaoNotaConceitoVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
			incluir(criterioAvaliacaoNotaConceitoVO);
		}

	}

	@Override
	public void alterarCriterioAvaliacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		excluirCriterioAvaliacaoNotaConceito(criterioAvaliacaoPeriodoLetivoVO);
		for(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO: criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()){
			criterioAvaliacaoNotaConceitoVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
			if(criterioAvaliacaoNotaConceitoVO.getNovoObj()){
				incluir(criterioAvaliacaoNotaConceitoVO);
			}else{
				alterar(criterioAvaliacaoNotaConceitoVO);
			}
		}

	}

	@Override
	public void excluirCriterioAvaliacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM criterioAvaliacaoNotaConceito where criterioAvaliacaoPeriodoLetivo = ? and codigo not in (0 ");
		for(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO: criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()){
			sql.append(", ").append(criterioAvaliacaoNotaConceitoVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoPeriodoLetivoVO.getCodigo());
	}
	
	private StringBuilder getSqlBasico(){
		StringBuilder sql =  new StringBuilder("SELECT criterioAvaliacaoNotaConceito.*,  ");		
		sql.append(" notaConceitoIndicadorAvaliacao.descricao as \"notaConceitoIndicadorAvaliacao.descricao\", notaConceitoIndicadorAvaliacao.nomeArquivo as \"notaConceitoIndicadorAvaliacao.nomeArquivo\", ");
		sql.append(" notaConceitoIndicadorAvaliacao.nomeArquivoApresentar as \"notaConceitoIndicadorAvaliacao.nomeArquivoApresentar\",  notaConceitoIndicadorAvaliacao.pastaBaseArquivo as \"notaConceitoIndicadorAvaliacao.pastaBaseArquivo\" ");
		sql.append(" from criterioAvaliacaoNotaConceito ");
		sql.append(" inner join notaConceitoIndicadorAvaliacao on notaConceitoIndicadorAvaliacao.codigo = criterioAvaliacaoNotaConceito.notaConceitoIndicadorAvaliacao ");
		return sql;		
	}

	@Override
	public List<CriterioAvaliacaoNotaConceitoVO> consultarPorCriterioAvaliacaoPeriodoLetivo(Integer criterioAvaliacaoPeriodoLetivo) throws Exception {
		StringBuilder sql =  getSqlBasico();
		sql.append(" where criterioAvaliacaoNotaConceito.criterioAvaliacaoPeriodoLetivo = ? order by criterioAvaliacaoNotaConceito.ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacaoPeriodoLetivo));
	}
	
	private List<CriterioAvaliacaoNotaConceitoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVOs = new ArrayList<CriterioAvaliacaoNotaConceitoVO>(0);
		while(rs.next()){
			criterioAvaliacaoNotaConceitoVOs.add(montarDados(rs));
		}
		return criterioAvaliacaoNotaConceitoVOs;
	}
	private CriterioAvaliacaoNotaConceitoVO montarDados(SqlRowSet rs) throws Exception{
		CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO = new CriterioAvaliacaoNotaConceitoVO();
		criterioAvaliacaoNotaConceitoVO.setNovoObj(false);
		criterioAvaliacaoNotaConceitoVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoNotaConceitoVO.getCriterioAvaliacaoPeriodoLetivo().setCodigo(rs.getInt("criterioAvaliacaoPeriodoLetivo"));
		criterioAvaliacaoNotaConceitoVO.setOrdem(rs.getInt("ordem"));
		criterioAvaliacaoNotaConceitoVO.setPeso(rs.getDouble("peso"));
		criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setCodigo(rs.getInt("notaConceitoIndicadorAvaliacao"));
		criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setDescricao(rs.getString("notaConceitoIndicadorAvaliacao.descricao"));
		criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setNomeArquivo(rs.getString("notaConceitoIndicadorAvaliacao.nomeArquivo"));
		criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setNomeArquivoApresentar(rs.getString("notaConceitoIndicadorAvaliacao.nomeArquivoApresentar"));
		if(rs.getString("notaConceitoIndicadorAvaliacao.pastaBaseArquivo") != null){
			criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(rs.getString("notaConceitoIndicadorAvaliacao.pastaBaseArquivo")));
		}
		return criterioAvaliacaoNotaConceitoVO;
	}

	@Override
	public void validarDados(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO) throws ConsistirException {
		if(criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getCodigo() == 0){
			throw new ConsistirException("msg_CriterioAvaliacaoNotaConceito_notaConceitoIndicadorAvaliacao");
		}
	}

}
