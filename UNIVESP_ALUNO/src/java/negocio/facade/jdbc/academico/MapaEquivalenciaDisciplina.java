package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.SituacaoMapaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraAnoSemestreEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraCargaHorariaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraFrequenciaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraNotaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraPeriodoLetivoEnum;
import negocio.comuns.academico.enumeradores.TipoRelacionamentoDisciplinaEquivalenciaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MapaEquivalenciaDisciplinaInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class MapaEquivalenciaDisciplina extends ControleAcesso implements MapaEquivalenciaDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6413135374424065856L;

	@Override
	public void incluirMapaEquivalenciaDisciplinaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, UsuarioVO usuario) throws Exception {
		for (MapaEquivalenciaDisciplinaVO obj : mapaEquivalenciaMatrizCurricularVO.getMapaEquivalenciaDisciplinaVOs()) {
			obj.setMapaEquivalenciaMatrizCurricular(mapaEquivalenciaMatrizCurricularVO);
			obj.getUsuarioCadastro().setCodigo(usuario.getCodigo());
			obj.getUsuarioCadastro().setNome(usuario.getNome());
			incluir(obj);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		try {
			validarDados(mapaEquivalenciaDisciplinaVO);
			mapaEquivalenciaDisciplinaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO mapaEquivalenciaDisciplina ( ");
					sql.append("sequencia, mapaEquivalenciaMatrizCurricular, observacao, tipoRegraNotaEquivalencia, formulaCalculoNota, tipoRegraCargaHorariaEquivalencia, ");
					sql.append("tipoRegraFrequenciaEquivalencia, tipoRegraAnoSemestreEquivalencia, tipoRelacionamentoDisciplinaEquivalencia, tipoRegraPeriodoLetivo, situacao, equivalenciaPorIsencao, dataCadastro, usuarioCadastro ");
					sql.append(") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					sql.append(" returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getSequencia());
					ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaMatrizCurricular().getCodigo());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getObservacao());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraNotaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getFormulaCalculoNota());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraCargaHorariaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraFrequenciaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraAnoSemestreEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRelacionamentoDisciplinaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraPeriodoLetivo().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getSituacao().name());
					ps.setBoolean(x++, mapaEquivalenciaDisciplinaVO.getEquivalenciaPorIsencao());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(mapaEquivalenciaDisciplinaVO.getDataCadastro()));
					if (mapaEquivalenciaDisciplinaVO.getUsuarioCadastro().getCodigo() > 0) {
						ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getUsuarioCadastro().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
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
			getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().incluirMapaEquivalenciaDisciplinaCursadaVOs(mapaEquivalenciaDisciplinaVO);
			getFacadeFactory().getMapaEquivalenciaDisciplinaMatrizCurricularFacade().incluirMapaEquivalenciaDisciplinaMatrizCurricularVOs(mapaEquivalenciaDisciplinaVO);
			mapaEquivalenciaDisciplinaVO.setNovoObj(false);
		} catch (Exception e) {
			mapaEquivalenciaDisciplinaVO.setCodigo(0);
			mapaEquivalenciaDisciplinaVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		try {

			validarDados(mapaEquivalenciaDisciplinaVO);
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE mapaEquivalenciaDisciplina SET ");
					sql.append("sequencia = ?, mapaEquivalenciaMatrizCurricular = ?, observacao = ?, tipoRegraNotaEquivalencia = ?, formulaCalculoNota = ?, tipoRegraCargaHorariaEquivalencia = ?, ");
					sql.append("tipoRegraFrequenciaEquivalencia = ?, tipoRegraAnoSemestreEquivalencia = ?, tipoRelacionamentoDisciplinaEquivalencia = ?, tipoRegraPeriodoLetivo = ?, situacao = ?, equivalenciaPorIsencao = ?, dataInativacao = ?, usuarioInativacao = ? ");
					sql.append(" where codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getSequencia());
					ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaMatrizCurricular().getCodigo());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getObservacao());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraNotaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getFormulaCalculoNota());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraCargaHorariaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraFrequenciaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraAnoSemestreEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRelacionamentoDisciplinaEquivalencia().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getTipoRegraPeriodoLetivo().name());
					ps.setString(x++, mapaEquivalenciaDisciplinaVO.getSituacao().name());
					ps.setBoolean(x++, mapaEquivalenciaDisciplinaVO.getEquivalenciaPorIsencao());
					if (mapaEquivalenciaDisciplinaVO.getSituacao().equals(StatusAtivoInativoEnum.INATIVO)) {
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(mapaEquivalenciaDisciplinaVO.getDataCadastro()));
						if (mapaEquivalenciaDisciplinaVO.getUsuarioInativacao().getCodigo() > 0) {
							ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getUsuarioInativacao().getCodigo());
						} else {
							ps.setNull(x++, 0);
						}
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setInt(x++, mapaEquivalenciaDisciplinaVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(mapaEquivalenciaDisciplinaVO);
				return;
			}

			getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().alterarMapaEquivalenciaDisciplinaCursadaVOs(mapaEquivalenciaDisciplinaVO);
			getFacadeFactory().getMapaEquivalenciaDisciplinaMatrizCurricularFacade().alterarMapaEquivalenciaDisciplinaMatrizCurricularVOs(mapaEquivalenciaDisciplinaVO);
		} catch (Exception e) {

			throw e;
		}
	}

	@Override
	public void realizarInativacaoMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		mapaEquivalenciaDisciplinaVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
		mapaEquivalenciaDisciplinaVO.setDataInativacao(new Date());
		mapaEquivalenciaDisciplinaVO.getUsuarioInativacao().setCodigo(usuarioVO.getCodigo());
		mapaEquivalenciaDisciplinaVO.getUsuarioInativacao().setNome(usuarioVO.getNome());
	}

	@Override
	public void realizarAtivacaoMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		mapaEquivalenciaDisciplinaVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
		mapaEquivalenciaDisciplinaVO.setDataInativacao(null);
		mapaEquivalenciaDisciplinaVO.setUsuarioInativacao(null);
	}

	@Override
	public void alterarMapaEquivalenciaDisciplinaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO) throws Exception {
		excluirMapaEquivalenciaDisciplinaVOs(mapaEquivalenciaMatrizCurricularVO, mapaEquivalenciaMatrizCurricularVO.getMapaEquivalenciaDisciplinaVOs());
		for (MapaEquivalenciaDisciplinaVO obj : mapaEquivalenciaMatrizCurricularVO.getMapaEquivalenciaDisciplinaVOs()) {
			obj.setMapaEquivalenciaMatrizCurricular(mapaEquivalenciaMatrizCurricularVO);
			if (obj.getNovoObj()) {
				incluir(obj);
			} else {
				alterar(obj);
			}
		}

	}

	@Override
	public void excluirMapaEquivalenciaDisciplinaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM MapaEquivalenciaDisciplina WHERE mapaEquivalenciaMatrizCurricular = ").append(mapaEquivalenciaMatrizCurricularVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (MapaEquivalenciaDisciplinaVO obj : mapaEquivalenciaDisciplinaVOs) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public void validarDados(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws ConsistirException {
		ConsistirException consistirException = null;
		if (mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().isEmpty()) {
			if (consistirException == null) {
				consistirException = new ConsistirException();
			}
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_disciplinaMatriz"));
		}

		if (mapaEquivalenciaDisciplinaVO.getTipoRegraNotaEquivalencia().equals(TipoRegraNotaEquivalenciaEnum.FORMULA_CALCULO)) {
			if (mapaEquivalenciaDisciplinaVO.getFormulaCalculoNota().trim().isEmpty()) {
				if (consistirException == null) {
					consistirException = new ConsistirException();
				}
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_formulaCalculo"));
			} else {
				ScriptEngineManager factory = new ScriptEngineManager();

				ScriptEngine engine = factory.getEngineByName("JavaScript");

				String formula = mapaEquivalenciaDisciplinaVO.getFormulaCalculoNota();

				for (MapaEquivalenciaDisciplinaCursadaVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
					if (obj.getVariavelNota().trim().isEmpty()) {
						if (consistirException == null) {
							consistirException = new ConsistirException();
						}
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplinaCursada_variavelNota").replace("{0}", obj.getDisciplinaVO().getNome()));
					} else {
						formula = formula.replaceAll(obj.getVariavelNota(), "0.0");
					}
				}

				formula = formula.replaceAll("e", "&&");
				formula = formula.replaceAll("E", "&&");
				formula = formula.replaceAll("ou", "||");
				formula = formula.replaceAll("OU", "||");
				formula = formula.replaceAll("=", "==");
				formula = formula.replaceAll("====", "==");
				formula = formula.replaceAll(">==", ">=");
				formula = formula.replaceAll("<==", "<=");
				formula = formula.replaceAll("!==", "!=");

				try {
					Object result = engine.eval(formula);
					if (!(result instanceof Number)) {
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_formulaCalculoInvalido"));
					}
				} catch (ScriptException e) {
					if (consistirException == null) {
						consistirException = new ConsistirException();
					}
					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_formulaCalculoInvalido"));
				}

			}
		} else {
			mapaEquivalenciaDisciplinaVO.setFormulaCalculoNota("");
		}

		if (mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().isEmpty()) {
			if (consistirException == null) {
				consistirException = new ConsistirException();
			}
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_disciplinaCursada"));
		}

		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if (mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria() == null || mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria() == 0) {
				if (consistirException == null) {
					consistirException = new ConsistirException();
				} 
				consistirException.adicionarListaMensagemErro("A Carga Horário da Disciplina Cursada "+mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getNome()+" não foi informado." );
			}
		}

		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
				if (mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo()) &&
						mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria().equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria())) {
					consistirException = new ConsistirException();
					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_codigoCargaHorariaDisciplinaIgual").replace("{0}", mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getNome().toUpperCase()).replace("{1}", mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getNome().toUpperCase()));					
				}
			}
		}
		if (consistirException != null) {
			throw consistirException;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarNovoMapaEquivalenciaMatrizCurricularPorAtualizacaoGradeCurricularCursoIntegral(Map<String, Integer> map, CursoVO curso, GradeCurricularVO gradeCurricular, TurmaDisciplinaVO td, TurmaDisciplinaVO tdCorrespodente , UsuarioVO usuarioVO) throws Exception {
		MapaEquivalenciaMatrizCurricularVO memc = new MapaEquivalenciaMatrizCurricularVO();
		MapaEquivalenciaDisciplinaVO med = new MapaEquivalenciaDisciplinaVO();
		if (!map.containsKey("mapaequivalenciamatrizcurricular") || (map.containsKey("mapaequivalenciamatrizcurricular") && !Uteis.isAtributoPreenchido(map.get("mapaequivalenciamatrizcurricular")))) {
			memc.setCurso(curso);
			memc.setGradeCurricular(gradeCurricular);
			memc.setDescricao("MC - " + gradeCurricular.getNome());
			memc.setSituacao(SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO);
			med.setSequencia(1);
		} else {
			memc.setCodigo(map.get("mapaequivalenciamatrizcurricular"));
			med.setSequencia(consultarTotalSequenciaMapaEquivalenciaDisciplinaPorMapaEquivalenciaMatrizCurricular(memc.getCodigo()) + 1);
		}
		MapaEquivalenciaDisciplinaMatrizCurricularVO medmc = new MapaEquivalenciaDisciplinaMatrizCurricularVO();
		medmc.setMapaEquivalenciaDisciplina(med);
		medmc.setDisciplinaVO(td.getDisciplina());
		medmc.setCargaHoraria(td.getDisciplinaReferenteAUmGrupoOptativa() ? td.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() : td.getGradeDisciplinaVO().getCargaHoraria());
		medmc.setNumeroCredito(td.getDisciplinaReferenteAUmGrupoOptativa() ? td.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos() : td.getGradeDisciplinaVO().getNrCreditos());
		
		
		MapaEquivalenciaDisciplinaCursadaVO medc = new MapaEquivalenciaDisciplinaCursadaVO();
		medc.setMapaEquivalenciaDisciplina(med);
		medc.setDisciplinaVO(tdCorrespodente.getDisciplina());
		medc.setCargaHoraria(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa() ? tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() : tdCorrespodente.getGradeDisciplinaVO().getCargaHoraria());
		medc.setNumeroCreditos(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa() ? tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos() : tdCorrespodente.getGradeDisciplinaVO().getNrCreditos());
		
		med.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().add(medmc);
		med.getMapaEquivalenciaDisciplinaCursadaVOs().add(medc);
		med.setMapaEquivalenciaMatrizCurricular(memc);
		med.setDisciplinaEquivalente(null);
		med.setDisciplinaMatrizCurricular(null);
		memc.getMapaEquivalenciaDisciplinaVOs().add(med);
		if(!Uteis.isAtributoPreenchido(memc)){
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().persistir(memc, false, usuarioVO);	
		}else{
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().incluir(med);
		}
		map.put("mapaequivalenciamatrizcurricular", memc.getCodigo());
		map.put("mapaequivalenciadisciplina", med.getCodigo());
		map.put("mapaequivalenciadisciplinacursada", medc.getCodigo());
		map.put("mapaequivalenciadisciplinamatrizcurricular", medmc.getCodigo());

	}

	@Override
	public MapaEquivalenciaDisciplinaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados) throws Exception {
		String sql = getSqlConsultaPadrao() + " WHERE MapaEquivalenciaDisciplina.codigo = " + codigo;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados);
		}
		return null;
	}

	public StringBuilder getSqlConsultaPadrao() {
		StringBuilder sql = new StringBuilder("SELECT distinct MapaEquivalenciaDisciplina.*, usuarioCadastro.nome as \"usuarioCadastro.nome\", usuarioInativacao.nome as \"usuarioInativacao.nome\" ");
		sql.append(" FROM MapaEquivalenciaDisciplina ");
		sql.append(" left join usuario as usuarioCadastro on usuarioCadastro.codigo = MapaEquivalenciaDisciplina.usuarioCadastro");
		sql.append(" left join usuario as usuarioInativacao on usuarioInativacao.codigo = MapaEquivalenciaDisciplina.usuarioInativacao");
		return sql;
	}

	@Override
	public MapaEquivalenciaDisciplinaVO consultarPorMapaEquivalenciaCursada(Integer codigoCursada, NivelMontarDados nivelMontarDados) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(getSqlConsultaPadrao());
		sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.MapaEquivalenciaDisciplina = MapaEquivalenciaDisciplina.codigo ");
		sb.append(" WHERE mapaequivalenciadisciplinacursada.codigo = ").append(codigoCursada);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados);
		}
		return null;
	}

	
	private Integer consultarTotalSequenciaMapaEquivalenciaDisciplinaPorMapaEquivalenciaMatrizCurricular(Integer mapaEquivalenciaMatrizCurricular) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT count(MapaEquivalenciaDisciplina.codigo) as qtd FROM MapaEquivalenciaDisciplina  ");
		sb.append(" WHERE mapaEquivalenciaMatrizCurricular = ").append(mapaEquivalenciaMatrizCurricular);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtd", TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public Integer consultarCodigoPorMapaEquivalenciaCursada(Integer codigoCursada, NivelMontarDados nivelMontarDados) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT MapaEquivalenciaDisciplina.codigo FROM MapaEquivalenciaDisciplina  ");
		sb.append(" inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.MapaEquivalenciaDisciplina = MapaEquivalenciaDisciplina.codigo ");
		sb.append(" WHERE mapaequivalenciadisciplinacursada.codigo = ").append(codigoCursada);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("codigo");
		}
		return null;
	}

	public List<MapaEquivalenciaDisciplinaVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados) throws Exception {
		List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
		while (rs.next()) {
			mapaEquivalenciaDisciplinaVOs.add(montarDados(rs, nivelMontarDados));
		}
		return mapaEquivalenciaDisciplinaVOs;
	}

	public MapaEquivalenciaDisciplinaVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados) throws Exception {
		MapaEquivalenciaDisciplinaVO obj = new MapaEquivalenciaDisciplinaVO();
		obj.setNovoObj(false);
		obj.setNivelMontarDados(nivelMontarDados);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setSequencia(rs.getInt("sequencia"));
		obj.getMapaEquivalenciaMatrizCurricular().setCodigo(rs.getInt("mapaEquivalenciaMatrizCurricular"));
		obj.setObservacao(rs.getString("observacao"));
		obj.setTipoRegraAnoSemestreEquivalencia(TipoRegraAnoSemestreEquivalenciaEnum.valueOf(rs.getString("tipoRegraAnoSemestreEquivalencia")));
		obj.setTipoRegraCargaHorariaEquivalencia(TipoRegraCargaHorariaEquivalenciaEnum.valueOf(rs.getString("tipoRegraCargaHorariaEquivalencia")));
		obj.setTipoRegraFrequenciaEquivalencia(TipoRegraFrequenciaEquivalenciaEnum.valueOf(rs.getString("tipoRegraFrequenciaEquivalencia")));
		obj.setTipoRegraNotaEquivalencia(TipoRegraNotaEquivalenciaEnum.valueOf(rs.getString("tipoRegraNotaEquivalencia")));
		obj.setTipoRegraPeriodoLetivo(TipoRegraPeriodoLetivoEnum.valueOf(rs.getString("tipoRegraPeriodoLetivo")));
		obj.setTipoRelacionamentoDisciplinaEquivalencia(TipoRelacionamentoDisciplinaEquivalenciaEnum.valueOf(rs.getString("tipoRelacionamentoDisciplinaEquivalencia")));
		obj.setFormulaCalculoNota(rs.getString("formulaCalculoNota"));
		obj.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
		obj.setEquivalenciaPorIsencao(rs.getBoolean("equivalenciaPorIsencao"));
		obj.setDataCadastro(rs.getTimestamp("dataCadastro"));
		obj.getUsuarioCadastro().setCodigo(rs.getInt("usuariocadastro"));
		obj.getUsuarioCadastro().setNome(rs.getString("usuarioCadastro.nome"));
		obj.setDataInativacao(rs.getTimestamp("datainativacao"));
		obj.getUsuarioInativacao().setCodigo(rs.getInt("usuarioinativacao"));
		obj.getUsuarioInativacao().setNome(rs.getString("usuarioInativacao.nome"));
		if (nivelMontarDados.equals(NivelMontarDados.BASICO)) {
			return obj;
		}
		obj.setMapaEquivalenciaDisciplinaCursadaVOs(getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorMapaEquivalenciaDisciplina(obj.getCodigo()));
		obj.setMapaEquivalenciaDisciplinaMatrizCurricularVOs(getFacadeFactory().getMapaEquivalenciaDisciplinaMatrizCurricularFacade().consultarPorMapaEquivalenciaDisciplina(obj.getCodigo()));

		return obj;
	}

	@Override
	public void adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO, String tipoOrigemDisciplina, Integer codigoOrigemDisciplina) throws Exception {
		if (tipoOrigemDisciplina != null && !tipoOrigemDisciplina.trim().isEmpty() && codigoOrigemDisciplina != null && codigoOrigemDisciplina != 0) {
			if (tipoOrigemDisciplina.equals("periodoLetivo")) {
				GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinaPorChavePrimariaDadosCargaHorariaNrCreditos(codigoOrigemDisciplina);
				mapaEquivalenciaDisciplinaMatrizCurricularVO.setDisciplinaVO(gradeDisciplinaVO.getDisciplina());
				mapaEquivalenciaDisciplinaMatrizCurricularVO.setCargaHoraria(gradeDisciplinaVO.getCargaHoraria());
				mapaEquivalenciaDisciplinaMatrizCurricularVO.setNumeroCredito(gradeDisciplinaVO.getNrCreditos());
			} else {
				GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(codigoOrigemDisciplina, null);
				mapaEquivalenciaDisciplinaMatrizCurricularVO.setDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina());
				mapaEquivalenciaDisciplinaMatrizCurricularVO.setCargaHoraria(gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
				mapaEquivalenciaDisciplinaMatrizCurricularVO.setNumeroCredito(gradeCurricularGrupoOptativaDisciplinaVO.getNrCreditos());
			}
			adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(mapaEquivalenciaMatrizCurricularVO, mapaEquivalenciaDisciplinaVO, mapaEquivalenciaDisciplinaMatrizCurricularVO);
		}
	}

	@Override
	public void adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws Exception {
		getFacadeFactory().getMapaEquivalenciaDisciplinaMatrizCurricularFacade().validarDados(mapaEquivalenciaDisciplinaMatrizCurricularVO);

		// for (MapaEquivalenciaDisciplinaVO mapaDis :
		// mapaEquivalenciaMatrizCurricularVO.getMapaEquivalenciaDisciplinaVOs())
		// {
		// if (mapaDis.getSequencia().intValue() !=
		// mapaEquivalenciaDisciplinaVO.getSequencia().intValue() &&
		// mapaDis.equals(mapaEquivalenciaDisciplinaVO)){
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_disciplinaGradeJaAdicionada").replace("{0}",
		// mapaDis.getSequencia().toString()));
		// }
		// }

		for (MapaEquivalenciaDisciplinaMatrizCurricularVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo().intValue()) {
				return;
			}
		}
		for (MapaEquivalenciaDisciplinaCursadaVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo().intValue() && obj.getCargaHoraria().equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria())) {
				throw new Exception(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_codigoCargaHorariaDisciplinaIgual").replace("{0}", obj.getDisciplinaVO().getNome().toUpperCase()).replace("{1}", mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getNome().toUpperCase()));
			}
		}
		mapaEquivalenciaDisciplinaMatrizCurricularVO.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().add(mapaEquivalenciaDisciplinaMatrizCurricularVO);
	}

	@Override
	public void removerMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws Exception {
		int index = 0;
		for (MapaEquivalenciaDisciplinaMatrizCurricularVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo().intValue()) {
				mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().remove(index);
				return;
			}
			index++;
		}

	}

	@Override
	public void adicionarMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws Exception {
		getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().validarDados(mapaEquivalenciaDisciplinaCursadaVO);

		// for (MapaEquivalenciaDisciplinaVO mapaDis :
		// mapaEquivalenciaMatrizCurricularVO.getMapaEquivalenciaDisciplinaVOs())
		// {
		// for (MapaEquivalenciaDisciplinaCursadaVO obj :
		// mapaDis.getMapaEquivalenciaDisciplinaCursadaVOs()) {
		// if (mapaDis.getSequencia().intValue() !=
		// mapaEquivalenciaDisciplinaVO.getSequencia().intValue() &&
		// obj.getDisciplinaVO().getCodigo().intValue() ==
		// mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().intValue())
		// {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_MapaEquivalenciaDisciplina_disciplinaGradeJaAdicionada").replace("{0}",
		// mapaDis.getSequencia().toString()));
		// }
		// }
		// }

		for (MapaEquivalenciaDisciplinaCursadaVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().intValue()) {
				return;
			}
		}
		for (MapaEquivalenciaDisciplinaMatrizCurricularVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().intValue() && obj.getCargaHoraria().equals(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria())) {
				throw new Exception(UteisJSF.internacionalizar("msg_MapaEquivalenciaMatrizCurricular_codigoCargaHorariaDisciplinaIgual").replace("{0}", obj.getDisciplinaVO().getNome().toUpperCase()).replace("{1}", mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getNome().toUpperCase()));
			}
		}
		mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().add(mapaEquivalenciaDisciplinaCursadaVO);

	}

	@Override
	public void removerMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws Exception {
		int index = 0;
		for (MapaEquivalenciaDisciplinaCursadaVO obj : mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo().intValue()) {
				mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().remove(index);
				return;
			}
			index++;
		}

	}

	@Override
	public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricular(Integer mapaEquivalenciaMatrizCurricular, NivelMontarDados nivelMontarDados) throws Exception {
		String sql = getSqlConsultaPadrao() + " WHERE MapaEquivalenciaDisciplina.mapaEquivalenciaMatrizCurricular = " + mapaEquivalenciaMatrizCurricular + " order by MapaEquivalenciaDisciplina.sequencia ";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql), nivelMontarDados);
	}

        @Override
	public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
			Integer codigoMatrizCurricular,
                Integer codigoDisplinaCursar,
                Integer cargaHoraria,
                Integer cogigoMapaEquivalenciaMatrizCurricula,
                NivelMontarDados nivelMontarDados) throws Exception {
                StringBuilder sql = new StringBuilder("SELECT DISTINCT MapaEquivalenciaDisciplina.*, usuarioCadastro.nome as \"usuarioCadastro.nome\", usuarioInativacao.nome as \"usuarioInativacao.nome\" ");
		sql.append(" FROM MapaEquivalenciaDisciplina ");
		sql.append(" INNER JOIN MapaEquivalenciaMatrizCurricular on MapaEquivalenciaMatrizCurricular.codigo =  MapaEquivalenciaDisciplina.mapaEquivalenciaMatrizCurricular ");
		sql.append(" INNER JOIN GradeCurricular on GradeCurricular.codigo =  MapaEquivalenciaMatrizCurricular.gradeCurricular ");
                sql.append(" INNER JOIN MapaEquivalenciaDisciplinaCursada on MapaEquivalenciaDisciplina.codigo =  MapaEquivalenciaDisciplinaCursada.MapaEquivalenciaDisciplina ");
                sql.append(" left join usuario as usuarioCadastro on usuarioCadastro.codigo = MapaEquivalenciaDisciplina.usuarioCadastro");
		sql.append(" left join usuario as usuarioInativacao on usuarioInativacao.codigo = MapaEquivalenciaDisciplina.usuarioInativacao");

		sql.append(" WHERE (MapaEquivalenciaMatrizCurricular.gradeCurricular = ").append(codigoMatrizCurricular).append(") ");
                sql.append("   AND (MapaEquivalenciaDisciplina.situacao = 'ATIVO')");
                sql.append("   AND (MapaEquivalenciaMatrizCurricular.situacao = 'ATIVO')");
                sql.append("   AND (MapaEquivalenciaMatrizCurricular.codigo = ").append(cogigoMapaEquivalenciaMatrizCurricula).append(") ");
                sql.append("   AND (MapaEquivalenciaDisciplinaCursada.disciplina = ").append(codigoDisplinaCursar).append(") ");
                sql.append("   AND (MapaEquivalenciaDisciplinaCursada.cargaHoraria = ").append(cargaHoraria).append(") ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
	}

        @Override
	public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
			Integer codigoMatrizCurricular,
                Integer codigoDisplinaCursar,
                Integer cargaHoraria,
                NivelMontarDados nivelMontarDados) throws Exception {
                StringBuilder sql = new StringBuilder("SELECT DISTINCT MapaEquivalenciaDisciplina.*, usuarioCadastro.nome as \"usuarioCadastro.nome\", usuarioInativacao.nome as \"usuarioInativacao.nome\" ");
		sql.append(" FROM MapaEquivalenciaDisciplina ");
		sql.append(" INNER JOIN MapaEquivalenciaMatrizCurricular on MapaEquivalenciaMatrizCurricular.codigo =  MapaEquivalenciaDisciplina.mapaEquivalenciaMatrizCurricular ");
		sql.append(" INNER JOIN GradeCurricular on GradeCurricular.codigo =  MapaEquivalenciaMatrizCurricular.gradeCurricular ");
                sql.append(" INNER JOIN MapaEquivalenciaDisciplinaCursada on MapaEquivalenciaDisciplina.codigo =  MapaEquivalenciaDisciplinaCursada.MapaEquivalenciaDisciplina ");
                sql.append(" left join usuario as usuarioCadastro on usuarioCadastro.codigo = MapaEquivalenciaDisciplina.usuarioCadastro");
		sql.append(" left join usuario as usuarioInativacao on usuarioInativacao.codigo = MapaEquivalenciaDisciplina.usuarioInativacao");

		sql.append(" WHERE (MapaEquivalenciaMatrizCurricular.gradeCurricular = ").append(codigoMatrizCurricular).append(") ");
                sql.append("   AND (MapaEquivalenciaDisciplina.situacao = 'ATIVO')");
                sql.append("   AND (MapaEquivalenciaDisciplinaCursada.disciplina = ").append(codigoDisplinaCursar).append(") ");
                sql.append("   AND (MapaEquivalenciaDisciplinaCursada.cargaHoraria = ").append(cargaHoraria).append(") ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
	}

        
        
        @Override
    	public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(
    			Integer codigoMatrizCurricular,
                    Integer codigoDisplinaMatriz,
                    NivelMontarDados nivelMontarDados, boolean apenasEquivalenciaUmPraUm) throws Exception {         	 
        	return consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatrizConsiderandoSituacao(codigoMatrizCurricular, codigoDisplinaMatriz, nivelMontarDados, apenasEquivalenciaUmPraUm, true);
           
    	}
        
        
    @Override
	public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatrizConsiderandoSituacao(
			Integer codigoMatrizCurricular, Integer codigoDisplinaMatriz, NivelMontarDados nivelMontarDados,
			boolean apenasEquivalenciaUmPraUm,boolean apenasSituacaoAtiva)
			throws Exception {
                StringBuilder sql = new StringBuilder("SELECT DISTINCT MapaEquivalenciaDisciplina.*, usuarioCadastro.nome as \"usuarioCadastro.nome\", usuarioInativacao.nome as \"usuarioInativacao.nome\" ");
		sql.append(" FROM MapaEquivalenciaDisciplina ");
		sql.append(" INNER JOIN MapaEquivalenciaMatrizCurricular on MapaEquivalenciaMatrizCurricular.codigo =  MapaEquivalenciaDisciplina.mapaEquivalenciaMatrizCurricular ");
		sql.append(" INNER JOIN GradeCurricular on GradeCurricular.codigo =  MapaEquivalenciaMatrizCurricular.gradeCurricular ");
		sql.append(" INNER JOIN MapaEquivalenciaDisciplinaMatrizCurricular on MapaEquivalenciaDisciplina.codigo =  MapaEquivalenciaDisciplinaMatrizCurricular.MapaEquivalenciaDisciplina ");
                sql.append(" left join usuario as usuarioCadastro on usuarioCadastro.codigo = MapaEquivalenciaDisciplina.usuarioCadastro");
		sql.append(" left join usuario as usuarioInativacao on usuarioInativacao.codigo = MapaEquivalenciaDisciplina.usuarioInativacao");
		sql.append(" WHERE (MapaEquivalenciaMatrizCurricular.gradeCurricular = ").append(codigoMatrizCurricular).append(") ");
		if(apenasSituacaoAtiva) {
			sql.append("   AND (MapaEquivalenciaMatrizCurricular.situacao = 'ATIVO')");
			sql.append("   AND (MapaEquivalenciaDisciplina.situacao = 'ATIVO') ");
		}		
		sql.append("   AND (MapaEquivalenciaDisciplinaMatrizCurricular.disciplina = ").append(codigoDisplinaMatriz).append(") ");
		if (apenasEquivalenciaUmPraUm) {
			sql.append("   group by MapaEquivalenciaDisciplina.codigo, usuariocadastro.nome, usuarioInativacao.nome  ");
			sql.append("   having count(distinct MapaEquivalenciaDisciplinaMatrizCurricular.codigo) = 1 and (select count(medc.codigo) from mapaequivalenciadisciplinacursada medc where medc.MapaEquivalenciaDisciplina = MapaEquivalenciaDisciplina.codigo)  = 1");
		}
		//sql.append(" order by mapaequivalenciadisciplina.codigo desc ");
		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void carregarHistoricosMapaEquivalenciaParaAvaliacaoEResolucao(String matricula, Integer matrizCurricular, MapaEquivalenciaDisciplinaVO mapaEquivalencia, Integer numeroAgrupamentoEquivalenciaDisciplina, Integer historicoIgnorar, UsuarioVO usuario) throws Exception {

		// MONTANDO OS HISTÓRICOS DAS DISCIPLINAS CURSADAS NO MAPA DE
		// EQUIVALENCIA (EQUIVALE)
		List<Integer> disciplinasCursadas = new ArrayList<Integer>(0);
		for (MapaEquivalenciaDisciplinaCursadaVO cursadas : mapaEquivalencia.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			disciplinasCursadas.add(cursadas.getDisciplinaVO().getCodigo());
		}
		List<SituacaoHistorico> situacoes = SituacaoHistorico.getSituacoesDeAprovacao(); // historicos
																							// de
																							// reprovacao
																							// nao
																							// nos
																							// interessam
		situacoes.add(SituacaoHistorico.CURSANDO); // adiciono esta opcao, para
													// caso o aluno esteja
													// estudando a disciplina,
													// então os dados deste
													// historico sejam
													// carregados para a
													// disciplina
		situacoes.addAll(SituacaoHistorico.getSituacoesDeReprovacao());
		List<HistoricoVO> historicosMapa = getFacadeFactory().getHistoricoFacade().consultaRapidaPorDisciplinaGradeCurricular(matricula, matrizCurricular, disciplinasCursadas, situacoes, false, true, false,  mapaEquivalencia.getCodigo(), numeroAgrupamentoEquivalenciaDisciplina, historicoIgnorar, NivelMontarDados.TODOS, usuario);
		for (MapaEquivalenciaDisciplinaCursadaVO cursadas : mapaEquivalencia.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			for (HistoricoVO historico : historicosMapa) {
				if ((cursadas.getCodigo().equals(historico.getMapaEquivalenciaDisciplinaCursada().getCodigo()))) {
					cursadas.setHistorico(historico);
				}
			}
		}

		// MONTANDO OS HISTÓRICOS DAS DISCIPLINAS A SEREM APROVADAS PELO MAPA DE
		// EQUIVALENCIA
		List<Integer> disciplinasASeremAprovadasPorEviquavalencia = new ArrayList<Integer>();
		for (MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatriz : mapaEquivalencia.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			disciplinasASeremAprovadasPorEviquavalencia.add(disciplinaMatriz.getDisciplinaVO().getCodigo());
		}
		situacoes = SituacaoHistorico.getSituacoesDeAprovacao(); // historicos
																	// de
																	// reprovacao
																	// nao nos
																	// interessam
		situacoes.add(SituacaoHistorico.CURSANDO); // adiciono esta opcao, para
													// caso o aluno esteja
													// estudando a disciplina,
													// então os dados deste
													// historico sejam
													// carregados para a
													// disciplina
		situacoes.add(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA);
		situacoes.addAll(SituacaoHistorico.getSituacoesDeReprovacao());
		List<HistoricoVO> historicosMatriz = getFacadeFactory().getHistoricoFacade().consultaRapidaPorDisciplinaGradeCurricular(matricula, matrizCurricular, disciplinasASeremAprovadasPorEviquavalencia, situacoes, true, false, false, mapaEquivalencia.getCodigo(), numeroAgrupamentoEquivalenciaDisciplina,  historicoIgnorar, NivelMontarDados.TODOS, usuario);
		for (MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatriz : mapaEquivalencia.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			for (HistoricoVO historico : historicosMatriz) {
				if (disciplinaMatriz.getDisciplinaVO().getCodigo().equals(historico.getDisciplina().getCodigo())) {
					disciplinaMatriz.setHistorico(historico);
				}
			}
		}
	}

    @Override
	public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(
			Integer codigoMatrizCurricular,
                Integer codigoDisplinaMatriz,
                Integer codigoMapaEquivalencia,
                NivelMontarDados nivelMontarDados) throws Exception {
                StringBuilder sql = new StringBuilder("SELECT DISTINCT MapaEquivalenciaDisciplina.*, usuarioCadastro.nome as \"usuarioCadastro.nome\", usuarioInativacao.nome as \"usuarioInativacao.nome\" ");
		sql.append(" FROM MapaEquivalenciaDisciplina ");
		sql.append(" INNER JOIN MapaEquivalenciaMatrizCurricular on MapaEquivalenciaMatrizCurricular.codigo =  MapaEquivalenciaDisciplina.mapaEquivalenciaMatrizCurricular ");
		sql.append(" INNER JOIN GradeCurricular on GradeCurricular.codigo =  MapaEquivalenciaMatrizCurricular.gradeCurricular ");
		sql.append(" INNER JOIN MapaEquivalenciaDisciplinaMatrizCurricular on MapaEquivalenciaDisciplina.codigo =  MapaEquivalenciaDisciplinaMatrizCurricular.MapaEquivalenciaDisciplina ");
                sql.append(" left join usuario as usuarioCadastro on usuarioCadastro.codigo = MapaEquivalenciaDisciplina.usuarioCadastro");
		sql.append(" left join usuario as usuarioInativacao on usuarioInativacao.codigo = MapaEquivalenciaDisciplina.usuarioInativacao");

		sql.append(" WHERE (MapaEquivalenciaMatrizCurricular.gradeCurricular = ").append(codigoMatrizCurricular).append(") ");
		sql.append("   AND (MapaEquivalenciaMatrizCurricular.codigo = ").append(codigoMapaEquivalencia).append(") ");
		sql.append("   AND (MapaEquivalenciaMatrizCurricular.situacao = 'ATIVO')");
		sql.append("   AND (MapaEquivalenciaDisciplinaMatrizCurricular.disciplina = ").append(codigoDisplinaMatriz).append(") ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
	}

}
