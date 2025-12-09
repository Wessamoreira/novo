package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MapaEquivalenciaDisciplinaMatrizCurricularInterfaceFacade;


@Repository
@Lazy
@Scope("singleton")
public class MapaEquivalenciaDisciplinaMatrizCurricular extends ControleAcesso implements MapaEquivalenciaDisciplinaMatrizCurricularInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4359795785791360363L;

	@Override
	public void incluirMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		for(MapaEquivalenciaDisciplinaMatrizCurricularVO obj: mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()){
			obj.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
			incluir(obj);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws Exception {
		try {
			validarDados(mapaEquivalenciaDisciplinaMatrizCurricularVO);
			mapaEquivalenciaDisciplinaMatrizCurricularVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO mapaEquivalenciaDisciplinaMatrizCurricular ( ");
					sql.append("disciplina, mapaEquivalenciaDisciplina, cargaHoraria, numeroCredito ");					
					sql.append(") values (?, ?, ?, ?)");
					sql.append(" returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getMapaEquivalenciaDisciplina().getCodigo());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getNumeroCredito());
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
			mapaEquivalenciaDisciplinaMatrizCurricularVO.setNovoObj(false);
		} catch (Exception e) {
			mapaEquivalenciaDisciplinaMatrizCurricularVO.setCodigo(0);
			mapaEquivalenciaDisciplinaMatrizCurricularVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws Exception {
		try {

			validarDados(mapaEquivalenciaDisciplinaMatrizCurricularVO);
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE mapaEquivalenciaDisciplinaMatrizCurricular SET ");
					sql.append(" disciplina = ?, mapaEquivalenciaDisciplina = ?, cargaHoraria = ?, numeroCredito = ? ");					
					sql.append(" where codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getMapaEquivalenciaDisciplina().getCodigo());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getNumeroCredito());
					ps.setInt(x++, mapaEquivalenciaDisciplinaMatrizCurricularVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(mapaEquivalenciaDisciplinaMatrizCurricularVO);
				return;
			}
						
		} catch (Exception e) {

			throw e;
		}
	}


	@Override
	public void alterarMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		excluirMapaEquivalenciaDisciplinaMatrizCurricularVOs(mapaEquivalenciaDisciplinaVO, mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs());
		for(MapaEquivalenciaDisciplinaMatrizCurricularVO obj: mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()){
			if(obj.isNovoObj()){
				incluir(obj);
			}else{
				alterar(obj);
			}
		}
	}

	@Override
	public void excluirMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, List<MapaEquivalenciaDisciplinaMatrizCurricularVO> mapaEquivalenciaDisciplinaMatrizCurricularVOs) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM mapaEquivalenciaDisciplinaMatrizCurricular where mapaEquivalenciaDisciplina = ? and codigo not in (0 ");
		for(MapaEquivalenciaDisciplinaMatrizCurricularVO obj: mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()){
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), mapaEquivalenciaDisciplinaVO.getCodigo());
	}

	@Override
	public void validarDados(MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws ConsistirException {
		if(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo() == 0){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplinaMatrizCurricular_disciplina"));
		}

	}
	
	public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql = new StringBuilder("SELECT MapaEquivalenciaDisciplinaMatrizCurricular.*, Disciplina.nome as \"Disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\" ");
		sql.append(" FROM MapaEquivalenciaDisciplinaMatrizCurricular ");
		sql.append(" INNER JOIN Disciplina on Disciplina.codigo = MapaEquivalenciaDisciplinaMatrizCurricular.disciplina ");
		
		return sql;
	}

	@Override
	public MapaEquivalenciaDisciplinaMatrizCurricularVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where MapaEquivalenciaDisciplinaMatrizCurricular.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs);
		}
		throw new Exception("Dados não encontrados DISCIPLINA MATRIZ CURRICULAR.");
	}
	
	public List<MapaEquivalenciaDisciplinaMatrizCurricularVO> montarDadosConsulta(SqlRowSet rs){
		List<MapaEquivalenciaDisciplinaMatrizCurricularVO> mapaEquivalenciaDisciplinaMatrizCurricularVOs = new ArrayList<MapaEquivalenciaDisciplinaMatrizCurricularVO>(0);
		while(rs.next()){
			mapaEquivalenciaDisciplinaMatrizCurricularVOs.add(montarDados(rs));
		}
		return mapaEquivalenciaDisciplinaMatrizCurricularVOs;
	}
	
	public MapaEquivalenciaDisciplinaMatrizCurricularVO montarDados(SqlRowSet rs){
		MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO = new MapaEquivalenciaDisciplinaMatrizCurricularVO();
		mapaEquivalenciaDisciplinaMatrizCurricularVO.setNovoObj(false);
		mapaEquivalenciaDisciplinaMatrizCurricularVO.setCodigo(rs.getInt("codigo"));
		mapaEquivalenciaDisciplinaMatrizCurricularVO.setCargaHoraria(rs.getInt("cargaHoraria"));		
		mapaEquivalenciaDisciplinaMatrizCurricularVO.setNumeroCredito(rs.getInt("numeroCredito"));		
		mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().setAbreviatura(rs.getString("disciplina.abreviatura"));
		mapaEquivalenciaDisciplinaMatrizCurricularVO.getMapaEquivalenciaDisciplina().setCodigo(rs.getInt("MapaEquivalenciaDisciplina"));
		return mapaEquivalenciaDisciplinaMatrizCurricularVO;
	}

	@Override
	public List<MapaEquivalenciaDisciplinaMatrizCurricularVO> consultarPorMapaEquivalenciaDisciplina(Integer mapaEquivalenciaDisciplina) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where MapaEquivalenciaDisciplinaMatrizCurricular.mapaEquivalenciaDisciplina = ").append(mapaEquivalenciaDisciplina).append(" ORDER BY Disciplina.nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	
	

}
