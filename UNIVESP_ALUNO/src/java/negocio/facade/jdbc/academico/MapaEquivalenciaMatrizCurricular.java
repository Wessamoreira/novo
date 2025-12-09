package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.enumeradores.SituacaoMapaEquivalenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MapaEquivalenciaMatrizCurricularInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class MapaEquivalenciaMatrizCurricular extends ControleAcesso implements MapaEquivalenciaMatrizCurricularInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6293609757718165632L;
	private static String idEntidade = "MapaEquivalenciaMatrizCurricular";


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void clonar(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, UsuarioVO usuarioLogado) throws Exception {
		
		mapaEquivalenciaVO.setNovoObj(true);
		mapaEquivalenciaVO.setCodigo(0);
		mapaEquivalenciaVO.setSituacao(SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO);
		mapaEquivalenciaVO.setData(new Date());
		mapaEquivalenciaVO.getResponsavel().setCodigo(usuarioLogado.getCodigo());
		mapaEquivalenciaVO.getResponsavel().setNome(usuarioLogado.getNome());
		mapaEquivalenciaVO.setDescricao(mapaEquivalenciaVO.getDescricao() + " - Clone");
		for(MapaEquivalenciaDisciplinaVO med: mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs()){
			med.setCodigo(0);
			med.setNovoObj(true);
			med.setSituacao(StatusAtivoInativoEnum.ATIVO);
			med.setDataCadastro(new Date());
			med.getUsuarioCadastro().setCodigo(usuarioLogado.getCodigo());
			med.getUsuarioCadastro().setNome(usuarioLogado.getNome());
			med.setDataInativacao(null);
			med.setUsuarioInativacao(null);
			for(MapaEquivalenciaDisciplinaMatrizCurricularVO medmc:med.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()){
				medmc.setCodigo(0);
				medmc.setNovoObj(true);
			}
			for(MapaEquivalenciaDisciplinaCursadaVO medc:med.getMapaEquivalenciaDisciplinaCursadaVOs()){
				medc.setCodigo(0);
				medc.setNovoObj(true);
			}
		}
		
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {

		if (mapaEquivalenciaVO.isNovoObj()) {
			incluir(mapaEquivalenciaVO, validarAcesso, usuarioLogado);
		} else {
			alterar(mapaEquivalenciaVO, validarAcesso, usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		if (mapaEquivalenciaVO.getSituacao().equals(SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO)) {
			getConexao().getJdbcTemplate().update("DELETE FROM MapaEquivalenciaMatrizCurricular WHERE codigo = " + mapaEquivalenciaVO.getCodigo() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_excluirAtivoInativo"));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		try {
			incluir(getIdEntidade(), validarAcesso, usuarioLogado);
			validarDados(mapaEquivalenciaVO);
			mapaEquivalenciaVO.setData(new Date());
			mapaEquivalenciaVO.getResponsavel().setCodigo(usuarioLogado.getCodigo());
			mapaEquivalenciaVO.getResponsavel().setNome(usuarioLogado.getNome());
			mapaEquivalenciaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO MapaEquivalenciaMatrizCurricular ( ");
					sql.append("curso, gradeCurricular, descricao, data, responsavel, situacao");
					sql.append(") values (?,?,?,?,?,?)");
					sql.append(" returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaVO.getCurso().getCodigo());
					ps.setInt(x++, mapaEquivalenciaVO.getGradeCurricular().getCodigo());
					ps.setString(x++, mapaEquivalenciaVO.getDescricao());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(mapaEquivalenciaVO.getData()));
					ps.setInt(x++, mapaEquivalenciaVO.getResponsavel().getCodigo());
					ps.setString(x++, mapaEquivalenciaVO.getSituacao().name());
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
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().incluirMapaEquivalenciaDisciplinaVOs(mapaEquivalenciaVO, usuarioLogado);
			mapaEquivalenciaVO.setNovoObj(false);
		} catch (Exception e) {
			mapaEquivalenciaVO.setCodigo(0);
			mapaEquivalenciaVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		try {
			alterar(getIdEntidade(), validarAcesso, usuarioLogado);
			validarDados(mapaEquivalenciaVO);
			mapaEquivalenciaVO.setData(new Date());
			mapaEquivalenciaVO.getResponsavel().setCodigo(usuarioLogado.getCodigo());
			mapaEquivalenciaVO.getResponsavel().setNome(usuarioLogado.getNome());
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE MapaEquivalenciaMatrizCurricular SET ");
					sql.append(" curso = ?, gradeCurricular = ?, descricao = ?, data = ?, responsavel = ?, situacao = ? ");
					sql.append(" where codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaVO.getCurso().getCodigo());
					ps.setInt(x++, mapaEquivalenciaVO.getGradeCurricular().getCodigo());
					ps.setString(x++, mapaEquivalenciaVO.getDescricao());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(mapaEquivalenciaVO.getData()));
					ps.setInt(x++, mapaEquivalenciaVO.getResponsavel().getCodigo());
					ps.setString(x++, mapaEquivalenciaVO.getSituacao().name());
					ps.setInt(x++, mapaEquivalenciaVO.getCodigo());
					return ps;
				}
			});
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().alterarMapaEquivalenciaDisciplinaVOs(mapaEquivalenciaVO);
			mapaEquivalenciaVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@Override
	public Map<String, Integer> validarSeExisteEquivalenciaParaReposicaoComTrocaAlteracaoGradeCurricular(Integer gradeCurricular, Integer disciplina, Integer novaDisciplina) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select mapaequivalenciamatrizcurricular.codigo as mapaequivalenciamatrizcurricular, mapaequivalenciadisciplina.codigo as mapaequivalenciadisciplina, "); 
		sql.append(" (");
		sql.append(" select mapaequivalenciadisciplinacursada.codigo"); 
		sql.append(" from mapaequivalenciadisciplinacursada  ");
		sql.append(" where mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo ");
		sql.append(" and mapaequivalenciadisciplinacursada.disciplina = ").append(novaDisciplina);
		sql.append(" ) as mapaequivalenciadisciplinacursada, "); 
		sql.append(" ( ");
		sql.append(" select mapaequivalenciadisciplinamatrizcurricular.codigo "); 
		sql.append(" from mapaequivalenciadisciplinamatrizcurricular ");  
		sql.append(" where mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo ");
		sql.append(" and mapaequivalenciadisciplinamatrizcurricular.disciplina = ").append(disciplina);
		sql.append(" ) as mapaequivalenciadisciplinamatrizcurricular ");
		sql.append(" from mapaequivalenciamatrizcurricular ");
		sql.append(" left join mapaequivalenciadisciplina  on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo ");
		sql.append(" and exists ( ");
		sql.append(" select mapaequivalenciadisciplinacursada.codigo "); 
		sql.append(" from mapaequivalenciadisciplinacursada  ");
		sql.append(" where mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo ");
		sql.append(" and mapaequivalenciadisciplinacursada.disciplina = ").append(novaDisciplina);
		sql.append(" ) ");
		sql.append(" and exists ( ");
		sql.append(" select mapaequivalenciadisciplinamatrizcurricular.codigo "); 
		sql.append(" from mapaequivalenciadisciplinamatrizcurricular  ");
		sql.append(" where mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo  ");
		sql.append(" and mapaequivalenciadisciplinamatrizcurricular.disciplina =  ").append(disciplina);
		sql.append(" )  ");
		sql.append(" and (  ");
		sql.append(" select count(mapaequivalenciadisciplinacursada.codigo)  "); 
		sql.append(" from mapaequivalenciadisciplinacursada   "); 
		sql.append(" where mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo  ");		 			
		sql.append(" ) = 1  ");
		sql.append(" and (  ");
		sql.append(" select count(mapaequivalenciadisciplinamatrizcurricular.codigo)  ");
		sql.append(" from mapaequivalenciadisciplinamatrizcurricular   ");
		sql.append(" where mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo  ");
		sql.append(" ) = 1  ");
		sql.append(" where mapaequivalenciamatrizcurricular.gradecurricular = ").append(gradeCurricular).append("  ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Map<String, Integer> map = new HashMap<>();
		if (rs.next()) {
			map.put("mapaequivalenciamatrizcurricular", rs.getInt("mapaequivalenciamatrizcurricular"));
			map.put("mapaequivalenciadisciplina", rs.getInt("mapaequivalenciadisciplina"));
			map.put("mapaequivalenciadisciplinacursada", rs.getInt("mapaequivalenciadisciplinacursada"));
			map.put("mapaequivalenciadisciplinamatrizcurricular", rs.getInt("mapaequivalenciadisciplinamatrizcurricular"));
		}
		return  map;
	}
	

	private StringBuilder getSqlConsultaBasica() {
		StringBuilder sql = new StringBuilder("SELECT distinct MapaEquivalenciaMatrizCurricular.*, ");
		sql.append(" curso.nome as \"curso.nome\", ");
		sql.append(" gradecurricular.nome as \"gradecurricular.nome\", ");
		sql.append(" gradecurricular.situacao as \"gradecurricular.situacao\", ");
		sql.append(" usuario.nome as \"usuario.nome\" ");
		sql.append(" FROM MapaEquivalenciaMatrizCurricular ");
		sql.append(" INNER JOIN curso on curso.codigo =  MapaEquivalenciaMatrizCurricular.curso ");
		sql.append(" INNER JOIN gradecurricular on gradecurricular.codigo =  MapaEquivalenciaMatrizCurricular.gradecurricular ");
		sql.append(" LEFT JOIN usuario on usuario.codigo =  MapaEquivalenciaMatrizCurricular.responsavel ");
		return sql;
	}

	@Override
	public List<MapaEquivalenciaMatrizCurricularVO> consultar(String campoConsulta, String valorConsulta, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception {
		if (campoConsulta.equals("curso")) {
			return consultarPorCurso(valorConsulta, nivelMontarDados, validarAcesso, usuarioLogado, limite, pagina, campoConsultaSituacao, campoConsultaDisciplina);
		}
		if (campoConsulta.equals("matrizCurricular")) {
			return consultarPorGradeCurricular(valorConsulta, nivelMontarDados, validarAcesso, usuarioLogado, limite, pagina, campoConsultaSituacao, campoConsultaDisciplina);
		}
		if (campoConsulta.equals("descricao")) {
			return consultarPorDescricao(valorConsulta, nivelMontarDados, validarAcesso, usuarioLogado, limite, pagina, campoConsultaSituacao, campoConsultaDisciplina);
		}
		return null;
	}

	@Override
	public Integer consultarTotalRegistroEncontrado(String campoConsulta, String valorConsulta, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception {
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT count(distinct MapaEquivalenciaMatrizCurricular.codigo) as qtde ");
		sql.append(" FROM MapaEquivalenciaMatrizCurricular ");
		sql.append(" INNER JOIN curso on curso.codigo =  MapaEquivalenciaMatrizCurricular.curso ");
		sql.append(" INNER JOIN gradecurricular on gradecurricular.codigo =  MapaEquivalenciaMatrizCurricular.gradecurricular ");
		if (campoConsulta.equals("curso")) {
			sql.append(" WHERE sem_acentos(upper(curso.nome)) ilike sem_acentos(upper(?)) ");
			parametros.add(valorConsulta + PERCENT);
		}
		if (campoConsulta.equals("matrizCurricular")) {
			sql.append(" WHERE sem_acentos(upper(gradecurricular.nome)) ilike sem_acentos(upper(?))  ");
			parametros.add(valorConsulta + PERCENT);
		}
		if (campoConsulta.equals("descricao")) {
			sql.append(" WHERE sem_acentos(upper(MapaEquivalenciaMatrizCurricular.descricao)) ilike sem_acentos(upper(?))  ");
			parametros.add(valorConsulta + PERCENT);
		}
		if(Uteis.isAtributoPreenchido(campoConsultaSituacao)) {
			sql.append(" AND MapaEquivalenciaMatrizCurricular.situacao = ('").append(campoConsultaSituacao).append("') ");
		}
		if(Uteis.isAtributoPreenchido(campoConsultaDisciplina)) {
			sql.append(" AND exists(select m.codigo from mapaequivalenciadisciplina m inner join mapaequivalenciadisciplinacursada m2 on m2.mapaequivalenciadisciplina = m.codigo "
					+ "inner join mapaequivalenciadisciplinamatrizcurricular m3 on m3.mapaequivalenciadisciplina = m.codigo inner join disciplina d on d.codigo = m2.disciplina "
					+ "inner join disciplina d2 on d2.codigo = m3.disciplina where (sem_acentos(d.nome) ilike sem_acentos(?) ");
			sql.append(" or sem_acentos(d2.nome) ilike sem_acentos(?)) ");
			sql.append(" and mapaequivalenciamatrizcurricular.codigo = m.mapaequivalenciamatrizcurricular) ");
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public List<MapaEquivalenciaMatrizCurricularVO> consultarPorCurso(String curso, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception {
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = getSqlConsultaBasica();
		sql.append(" WHERE sem_acentos(upper(curso.nome)) ilike sem_acentos(upper(?)) ");
		parametros.add(PERCENT + curso + PERCENT);
		if(Uteis.isAtributoPreenchido(campoConsultaSituacao)) {
			sql.append(" AND MapaEquivalenciaMatrizCurricular.situacao = ('").append(campoConsultaSituacao).append("') ");
		}
		if(Uteis.isAtributoPreenchido(campoConsultaDisciplina)) {
			sql.append(" AND exists(select m.codigo from mapaequivalenciadisciplina m inner join mapaequivalenciadisciplinacursada m2 on m2.mapaequivalenciadisciplina = m.codigo "
					+ "inner join mapaequivalenciadisciplinamatrizcurricular m3 on m3.mapaequivalenciadisciplina = m.codigo inner join disciplina d on d.codigo = m2.disciplina "
					+ "inner join disciplina d2 on d2.codigo = m3.disciplina where (sem_acentos(d.nome) ilike sem_acentos(?) ");
			sql.append(" or sem_acentos(d2.nome) ilike sem_acentos(?)) ");
			sql.append(" and mapaequivalenciamatrizcurricular.codigo = m.mapaequivalenciamatrizcurricular)");
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
		}
		sql.append(" order by curso.nome, gradecurricular.nome");
		if(Uteis.isAtributoPreenchido(limite)){
			sql.append(" limit ").append(limite).append(" offset ").append(pagina);	
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray()), nivelMontarDados);
	}

	@Override
	public List<MapaEquivalenciaMatrizCurricularVO> consultarPorGradeCurricular(String gradeCurricular, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception {
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = getSqlConsultaBasica();
		sql.append(" WHERE sem_acentos(upper(gradecurricular.nome)) ilike sem_acentos(upper(?)) ");
		parametros.add(PERCENT + gradeCurricular + PERCENT);
		if(Uteis.isAtributoPreenchido(campoConsultaSituacao)) {
			sql.append(" AND MapaEquivalenciaMatrizCurricular.situacao = ('").append(campoConsultaSituacao).append("') ");
		}
		if(Uteis.isAtributoPreenchido(campoConsultaDisciplina)) {
			sql.append(" AND exists(select m.codigo from mapaequivalenciadisciplina m inner join mapaequivalenciadisciplinacursada m2 on m2.mapaequivalenciadisciplina = m.codigo "
					+ "inner join mapaequivalenciadisciplinamatrizcurricular m3 on m3.mapaequivalenciadisciplina = m.codigo inner join disciplina d on d.codigo = m2.disciplina "
					+ "inner join disciplina d2 on d2.codigo = m3.disciplina where (sem_acentos(d.nome) ilike sem_acentos(?) ");
			sql.append(" or sem_acentos(d2.nome) ilike sem_acentos(?)) ");
			sql.append(" and mapaequivalenciamatrizcurricular.codigo = m.mapaequivalenciamatrizcurricular)");
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
		}
		sql.append(" order by gradecurricular.nome, curso.nome" );
		if(Uteis.isAtributoPreenchido(limite)){
			sql.append(" limit ").append(limite).append(" offset ").append(pagina);	
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray()), nivelMontarDados);
	}
        
	@Override
	public List<MapaEquivalenciaMatrizCurricularVO> consultarPorCodigoGradeCurricular(Integer gradeCurricular, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSqlConsultaBasica();
		sql.append(" WHERE (gradecurricular.codigo = ?) order by MapaEquivalenciaMatrizCurricular.descricao ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), gradeCurricular), nivelMontarDados);
	}
        
        
        public List<MapaEquivalenciaMatrizCurricularVO> consultarPorDescricao(String descricao, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception {
    	List<Object> parametros = new ArrayList<>();
    	StringBuilder sql = getSqlConsultaBasica();
		sql.append(" WHERE sem_acentos(upper(MapaEquivalenciaMatrizCurricular.descricao)) ilike sem_acentos(upper(?)) ");
		parametros.add(PERCENT + descricao + PERCENT);
		if(Uteis.isAtributoPreenchido(campoConsultaSituacao)) {
			sql.append(" AND MapaEquivalenciaMatrizCurricular.situacao = ('").append(campoConsultaSituacao).append("') ");
		}
		if(Uteis.isAtributoPreenchido(campoConsultaDisciplina)) {
			sql.append(" AND exists(select m.codigo from mapaequivalenciadisciplina m inner join mapaequivalenciadisciplinacursada m2 on m2.mapaequivalenciadisciplina = m.codigo "
					+ "inner join mapaequivalenciadisciplinamatrizcurricular m3 on m3.mapaequivalenciadisciplina = m.codigo inner join disciplina d on d.codigo = m2.disciplina "
					+ "inner join disciplina d2 on d2.codigo = m3.disciplina where (sem_acentos(d.nome) ilike sem_acentos(?) ");
			sql.append(" or sem_acentos(d2.nome) ilike sem_acentos(?)) ");
			sql.append(" and mapaequivalenciamatrizcurricular.codigo = m.mapaequivalenciamatrizcurricular)");
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
			parametros.add(PERCENT + campoConsultaDisciplina + PERCENT);
		}
		sql.append(" order by MapaEquivalenciaMatrizCurricular.descricao, curso.nome ");
		if(Uteis.isAtributoPreenchido(limite)){
			sql.append(" limit ").append(limite).append(" offset ").append(pagina);	
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray()), nivelMontarDados);
	}

	private List<MapaEquivalenciaMatrizCurricularVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<MapaEquivalenciaMatrizCurricularVO> mapaEquivalenciaMatrizCurricularVOs = new ArrayList<MapaEquivalenciaMatrizCurricularVO>(0);
		while (rs.next()) {
			mapaEquivalenciaMatrizCurricularVOs.add(montarDados(rs, nivelMontarDados));
		}
		return mapaEquivalenciaMatrizCurricularVOs;
	}

	private MapaEquivalenciaMatrizCurricularVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		MapaEquivalenciaMatrizCurricularVO obj = new MapaEquivalenciaMatrizCurricularVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setSituacao(SituacaoMapaEquivalenciaEnum.valueOf(rs.getString("situacao")));
		obj.getCurso().setCodigo(rs.getInt("curso"));
		obj.getCurso().setNome(rs.getString("curso.nome"));
		obj.getGradeCurricular().setCodigo(rs.getInt("gradeCurricular"));
		obj.getGradeCurricular().setNome(rs.getString("gradeCurricular.nome"));
		obj.getGradeCurricular().setSituacao(rs.getString("gradeCurricular.situacao"));
		obj.getResponsavel().setCodigo(rs.getInt("responsavel"));
		obj.getResponsavel().setNome(rs.getString("usuario.nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setMapaEquivalenciaDisciplinaVOs(getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricular(obj.getCodigo(), NivelMontarDados.TODOS));
		return obj;
	}

	@Override
	public MapaEquivalenciaMatrizCurricularVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSqlConsultaBasica();
		sql.append(" WHERE mapaEquivalenciaMatrizCurricular.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
				return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
			} else {
				return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			}
		}
		throw new Exception("Dados não encontrados MAPA EQUIVALENCIA MATRIZ CURRICULAR. ");
	}

	@Override
	public MapaEquivalenciaMatrizCurricularVO consultarPorGradeCurricularSituacaoAtiva(Integer gradeCurricular, NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSqlConsultaBasica();
		sql.append(" WHERE gradecurricular.codigo = ").append(gradeCurricular).append(" and mapaEquivalenciaMatrizCurricular.situacao = '").append(SituacaoMapaEquivalenciaEnum.ATIVO.name()).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
				return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
			} else {
				return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			}
		}
		return null;
	}

	@Override
	public void validarDados(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO) throws ConsistirException {
		ConsistirException consistirException = null;
		if (mapaEquivalenciaVO.getDescricao().trim().isEmpty()) {
			if (consistirException == null) {
				consistirException = new ConsistirException();
			}
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_descricao"));
		}
		if (mapaEquivalenciaVO.getCurso().getCodigo() == 0) {
			if (consistirException == null) {
				consistirException = new ConsistirException();
			}
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_curso"));
		}
		if (mapaEquivalenciaVO.getGradeCurricular().getCodigo() == 0) {
			if (consistirException == null) {
				consistirException = new ConsistirException();
			}
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_gradeCurricular"));
		}
		if (mapaEquivalenciaVO.getSituacao().equals(SituacaoMapaEquivalenciaEnum.ATIVO)) {
			if (mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs().isEmpty()) {
				if (consistirException == null) {
					consistirException = new ConsistirException();
				}
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_disciplina"));
			}
		}
		if (consistirException != null) {
			throw consistirException;
		}
	}

	@Override
	public void realizarAtivacaoMapaEquivalencia(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			mapaEquivalenciaVO.setSituacao(SituacaoMapaEquivalenciaEnum.ATIVO);
			persistir(mapaEquivalenciaVO, true, usuarioVO);
		} catch (Exception e) {
			mapaEquivalenciaVO.setSituacao(SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO);
			throw e;
		}

	}

	@Override
	public void realizarInativacaoMapaEquivalencia(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			mapaEquivalenciaVO.setSituacao(SituacaoMapaEquivalenciaEnum.INATIVO);
			for(MapaEquivalenciaDisciplinaVO equivalenciaDisciplinaVO : mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs()) {
				getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().realizarInativacaoMapaEquivalenciaDisciplina(equivalenciaDisciplinaVO, usuarioVO);
			}
			persistir(mapaEquivalenciaVO, true, usuarioVO);
		} catch (Exception e) {
			mapaEquivalenciaVO.setSituacao(SituacaoMapaEquivalenciaEnum.ATIVO);
			throw e;
		}

	}

	@Override
	public List<DisciplinaVO> realizarVerificacaoDisciplinaNaoRealizadoEquivalencia(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		// trazendo disciplinas normais
		sql.append(" select distinct Disciplina.codigo, Disciplina.nome, Disciplina.abreviatura, 1 as origem, periodoletivo.descricao, gradedisciplina.cargahoraria, gradedisciplina.nrcreditos from gradedisciplina ");
		sql.append(" inner join Disciplina on Disciplina.codigo = GradeDisciplina.Disciplina ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" where periodoletivo.gradecurricular = ").append(mapaEquivalenciaMatrizCurricularVO.getGradeCurricular().getCodigo());
		// trazendo disciplinas grupo optativas
		sql.append(" union ");
		sql.append(" select distinct Disciplina.codigo, Disciplina.nome, Disciplina.abreviatura, 2 as origem, gradecurriculargrupooptativa.descricao, gradecurriculargrupooptativadisciplina.cargahoraria, gradecurriculargrupooptativadisciplina.nrcreditos from gradecurriculargrupooptativa ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sql.append(" inner join Disciplina on Disciplina.codigo = gradecurriculargrupooptativadisciplina.Disciplina ");
		sql.append(" where gradecurriculargrupooptativa.gradecurricular = ").append(mapaEquivalenciaMatrizCurricularVO.getGradeCurricular().getCodigo());
		// trazendo disciplinas filhas de uma composição
		sql.append(" union ");
		sql.append(" select distinct Disciplina.codigo, Disciplina.nome, Disciplina.abreviatura, 3 as origem, periodoletivo.descricao, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrcreditos from gradedisciplinacomposta ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplinacomposta.periodoletivo ");
		sql.append(" inner join Disciplina on Disciplina.codigo = gradedisciplinacomposta.Disciplina ");
		sql.append(" where periodoletivo.gradecurricular = ").append(mapaEquivalenciaMatrizCurricularVO.getGradeCurricular().getCodigo());
		// trazendo disciplinas filhas de uma composição de um grupo optativa
		sql.append(" union ");
		sql.append(" select distinct Disciplina.codigo, Disciplina.nome, Disciplina.abreviatura, 4 as origem, gradecurriculargrupooptativa.descricao, gradecurriculargrupooptativadisciplina.cargahoraria, gradecurriculargrupooptativadisciplina.nrcreditos from gradedisciplinacomposta ");
		sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
		sql.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sql.append(" where gradecurriculargrupooptativa.gradecurricular = ").append(mapaEquivalenciaMatrizCurricularVO.getGradeCurricular().getCodigo());
		// finalizando condicoes do SQL
		sql.append(" order by origem, descricao, nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>(0);
		DisciplinaVO obj = null;
		while (rs.next()) {
			obj = new DisciplinaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNome(rs.getString("nome"));
			obj.setAbreviatura(rs.getString("abreviatura"));
			obj.setCargaHorariaPrevista(rs.getInt("cargahoraria"));
			obj.setNumeroCreditoPrevisto(rs.getInt("nrCreditos"));
			obj.setDescricaoPeriodoLetivo(rs.getString("descricao"));
			disciplinaVOs.add(obj);
		}
		sql = null;
		return disciplinaVOs;
	}

	@Override
	public void adicionarMapaEquivalenciaDisciplina(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().validarDados(mapaEquivalenciaDisciplinaVO);
		for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO2 : mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs()) {
			if (mapaEquivalenciaDisciplinaVO.getSequencia().equals(mapaEquivalenciaDisciplinaVO2.getSequencia())) {
				mapaEquivalenciaDisciplinaVO.setDisciplinaEquivalente(null);
				mapaEquivalenciaDisciplinaVO.setDisciplinaMatrizCurricular(null);
				mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs().set(mapaEquivalenciaDisciplinaVO2.getSequencia() - 1, mapaEquivalenciaDisciplinaVO);
				return;
			}else if(mapaEquivalenciaDisciplinaVO2.equals(mapaEquivalenciaDisciplinaVO)){
				throw new Exception(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_equivalenciaJaExistente").replace("{0}", mapaEquivalenciaDisciplinaVO2.getSequencia().toString()));
			}
		}
		
		
		mapaEquivalenciaDisciplinaVO.setSequencia(mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs().size() + 1);
		mapaEquivalenciaDisciplinaVO.setMapaEquivalenciaMatrizCurricular(mapaEquivalenciaVO);
		mapaEquivalenciaDisciplinaVO.setDisciplinaEquivalente(null);
		mapaEquivalenciaDisciplinaVO.setDisciplinaMatrizCurricular(null);
		mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs().add(mapaEquivalenciaDisciplinaVO);
	}

	@Override
	public void removerMapaEquivalenciaDisciplina(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO2 : mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs()) {
			if (mapaEquivalenciaDisciplinaVO.getSequencia().equals(mapaEquivalenciaDisciplinaVO2.getSequencia())) {
				mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs().remove(mapaEquivalenciaDisciplinaVO2.getSequencia() - 1);
				realizarReorganizacaoSequenciaMapaEquivalenciaDisciplina(mapaEquivalenciaVO);
				return;
			}
		}
	}

	private void realizarReorganizacaoSequenciaMapaEquivalenciaDisciplina(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO) {
		int x = 1;
		for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO : mapaEquivalenciaVO.getMapaEquivalenciaDisciplinaVOs()) {
			mapaEquivalenciaDisciplinaVO.setSequencia(x);
			x++;
		}
	}

	public static String getIdEntidade() {

		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		MapaEquivalenciaMatrizCurricular.idEntidade = idEntidade;
	}

	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String designIReportRelatorio() {

		return (caminhoBaseRelatorio() + "MapaEquivalenciaMatrizCurricularRel.jrxml");

	}

	@Override
	public Boolean realizarVerificacaoDisciplinaEquivalente(Integer matrizCurricular, Integer disciplina) throws Exception{
			StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append("select gradedisciplina.disciplina from gradedisciplina");
			sqlStr.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo");
			sqlStr.append(" inner join gradecurricular on periodoletivo.gradecurricular = gradecurricular.codigo");
			sqlStr.append(" where gradedisciplina.disciplina = (").append(disciplina).append(") "); 
			sqlStr.append(" and gradecurricular.codigo = (").append(matrizCurricular).append(") "); 
			sqlStr.append(" union all");
			sqlStr.append(" select gradedisciplinacomposta.disciplina from gradedisciplinacomposta");
			sqlStr.append(" inner join gradedisciplina on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo");
			sqlStr.append(" inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo");
			sqlStr.append(" inner join gradecurricular on periodoletivo.gradecurricular = gradecurricular.codigo");
			sqlStr.append(" where gradedisciplinacomposta.disciplina = (").append(disciplina).append(") ");
			sqlStr.append(" and gradecurricular.codigo = (").append(matrizCurricular).append(") "); 
			sqlStr.append(" union all");
			sqlStr.append(" select gradecurriculargrupooptativadisciplina.disciplina from gradecurriculargrupooptativadisciplina");
			sqlStr.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
			sqlStr.append(" left join periodoletivo on periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
			sqlStr.append(" inner join gradecurricular on ((periodoletivo.codigo is not null and periodoletivo.gradecurricular = gradecurricular.codigo) or");
			sqlStr.append(" (periodoletivo.codigo is null and gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo))");
			sqlStr.append(" where gradecurriculargrupooptativadisciplina.disciplina = (").append(disciplina).append(") ");
			sqlStr.append(" and gradecurricular.codigo = (").append(matrizCurricular).append(") "); 
			return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}
}
