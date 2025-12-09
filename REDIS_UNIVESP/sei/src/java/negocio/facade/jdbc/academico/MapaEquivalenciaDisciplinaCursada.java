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

import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MapaEquivalenciaDisciplinaCursadaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class MapaEquivalenciaDisciplinaCursada extends ControleAcesso implements MapaEquivalenciaDisciplinaCursadaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6422618444788908848L;

	@Override
	public void incluirMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		for(MapaEquivalenciaDisciplinaCursadaVO obj: mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()){
			obj.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
			incluir(obj);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws Exception {
		try {
			validarDados(mapaEquivalenciaDisciplinaCursadaVO);
			mapaEquivalenciaDisciplinaCursadaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO mapaEquivalenciaDisciplinaCursada ( ");
					sql.append("disciplina, mapaEquivalenciaDisciplina, variavelNota, cargaHoraria, numeroCreditos ");					
					sql.append(") values (?, ?, ?, ?, ?)");
					sql.append(" returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo());
					ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getMapaEquivalenciaDisciplina().getCodigo());
					ps.setString(x++, mapaEquivalenciaDisciplinaCursadaVO.getVariavelNota());
                                        ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria());
                                        ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getNumeroCreditos());
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
			mapaEquivalenciaDisciplinaCursadaVO.setNovoObj(false);
		} catch (Exception e) {
			mapaEquivalenciaDisciplinaCursadaVO.setCodigo(0);
			mapaEquivalenciaDisciplinaCursadaVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws Exception {
		try {

			validarDados(mapaEquivalenciaDisciplinaCursadaVO);
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE mapaEquivalenciaDisciplinaCursada SET ");
					sql.append(" disciplina = ?, mapaEquivalenciaDisciplina = ?, variavelNota = ?, cargaHoraria=?, numeroCreditos=? ");					
					sql.append(" where codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo());
					ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getMapaEquivalenciaDisciplina().getCodigo());
					ps.setString(x++, mapaEquivalenciaDisciplinaCursadaVO.getVariavelNota());
                                        ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria());
                                        ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getNumeroCreditos());
					ps.setInt(x++, mapaEquivalenciaDisciplinaCursadaVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(mapaEquivalenciaDisciplinaCursadaVO);
				return;
			}
						
		} catch (Exception e) {

			throw e;
		}
	}


	@Override
	public void alterarMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		excluirMapaEquivalenciaDisciplinaCursadaVOs(mapaEquivalenciaDisciplinaVO, mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs());
		for(MapaEquivalenciaDisciplinaCursadaVO obj: mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()){
			if(obj.isNovoObj()){
				incluir(obj);
			}else{
				alterar(obj);
			}
		}
	}

	@Override
	public void excluirMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, List<MapaEquivalenciaDisciplinaCursadaVO> mapaEquivalenciaDisciplinaCursadaVOs) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM mapaEquivalenciaDisciplinaCursada where mapaEquivalenciaDisciplina = ? and codigo not in (0 ");
		for(MapaEquivalenciaDisciplinaCursadaVO obj: mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()){
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), mapaEquivalenciaDisciplinaVO.getCodigo());
	}

	@Override
	public void validarDados(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws ConsistirException {
		if(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo() == 0){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplinaCursada_disciplina"));
		}

	}
	
	public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql = new StringBuilder("SELECT MapaEquivalenciaDisciplinaCursada.*, Disciplina.nome as \"Disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\" ");
		sql.append(" FROM MapaEquivalenciaDisciplinaCursada ");
		sql.append(" INNER JOIN Disciplina on Disciplina.codigo = MapaEquivalenciaDisciplinaCursada.disciplina ");
		
		return sql;
	}

	@Override
	public MapaEquivalenciaDisciplinaCursadaVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where MapaEquivalenciaDisciplinaCursada.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs);
		}
		throw new Exception("Dados não encontrados DISCIPLINA a CURSAR por equivalencia.");
	}
	
	public List<MapaEquivalenciaDisciplinaCursadaVO> montarDadosConsulta(SqlRowSet rs){
		List<MapaEquivalenciaDisciplinaCursadaVO> mapaEquivalenciaDisciplinaCursadaVOs = new ArrayList<MapaEquivalenciaDisciplinaCursadaVO>(0);
		while(rs.next()){
			mapaEquivalenciaDisciplinaCursadaVOs.add(montarDados(rs));
		}
		return mapaEquivalenciaDisciplinaCursadaVOs;
	}
	
	public MapaEquivalenciaDisciplinaCursadaVO montarDados(SqlRowSet rs){
		MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = new MapaEquivalenciaDisciplinaCursadaVO();
		mapaEquivalenciaDisciplinaCursadaVO.setNovoObj(false);
		mapaEquivalenciaDisciplinaCursadaVO.setCodigo(rs.getInt("codigo"));
		mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().setAbreviatura(rs.getString("disciplina.abreviatura"));
		mapaEquivalenciaDisciplinaCursadaVO.setVariavelNota(rs.getString("variavelNota"));
		mapaEquivalenciaDisciplinaCursadaVO.setCargaHoraria(rs.getInt("cargaHoraria"));
                mapaEquivalenciaDisciplinaCursadaVO.setNumeroCreditos(rs.getInt("numeroCreditos"));
                
		mapaEquivalenciaDisciplinaCursadaVO.getMapaEquivalenciaDisciplina().setCodigo(rs.getInt("MapaEquivalenciaDisciplina"));
		return mapaEquivalenciaDisciplinaCursadaVO;
	}

	@Override
	public List<MapaEquivalenciaDisciplinaCursadaVO> consultarPorMapaEquivalenciaDisciplina(Integer mapaEquivalenciaDisciplina) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where MapaEquivalenciaDisciplinaCursada.mapaEquivalenciaDisciplina = ").append(mapaEquivalenciaDisciplina).append(" ORDER BY Disciplina.nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	@Override
	public MapaEquivalenciaDisciplinaCursadaVO consultarPorMapaEquivalenciaDisciplinaEDisciplina(Integer mapaEquivalenciaDisciplina, Integer disciplina) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where MapaEquivalenciaDisciplinaCursada.mapaEquivalenciaDisciplina = ").append(mapaEquivalenciaDisciplina).append(" ORDER BY Disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado);
		}
		return null;
	}
}
