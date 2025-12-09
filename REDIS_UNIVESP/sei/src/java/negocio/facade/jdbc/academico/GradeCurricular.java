package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LogGradeCurricularVO;
import negocio.comuns.academico.LogImpactoMatrizCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.RegraContagemPeriodoLetivoEnum;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.GradeCurricularInterfaceFacade;
import webservice.servicos.DisciplinaObject;
import webservice.servicos.GradeDisciplinaObject;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>GradeCurricularVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>GradeCurricularVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see GradeCurricularVO
 * @see ControleAcesso
 * @see Curso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class GradeCurricular extends ControleAcesso implements GradeCurricularInterfaceFacade {

	private static final long serialVersionUID = -3873429681300474575L;

	protected static String idEntidade;

	public GradeCurricular() throws Exception {
		super();
		setIdEntidade("Curso");
	}

	public GradeCurricularVO novo() throws Exception {
		GradeCurricular.incluir(getIdEntidade());
		GradeCurricularVO obj = new GradeCurricularVO();
		return obj;
	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception {
		try {
		validarDados(obj, cursoVO, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO GradeCurricular( nome, dataCadastro, situacao, dataFinalVigencia, curso, dataativacao, datadesativacao,");
		sql.append(" responsavelativacao, responsaveldesativacao, cargahoraria, creditos, totalDiaLetivoAno, totalSemanaLetivaAno, ");
		sql.append(" totalCargaHorariaAtividadeComplementar, totalCargaHorariaEstagio, observacaoHistorico, sistemaAvaliacao, competenciaProfissional,");
		sql.append(" resolucao, nrMesesConclusaoMatrizCurricular, habilitacao, quantidadeDisciplinasOptativasMatrizCurricular, percentualPermitirIniciarEstagio, ");
		sql.append(" percentualPermitirIniciarTcc, disciplinaPadraoTcc, totalCargaHorariaDisciplinasObrigatorias, qtdeAnoSemestreParaIntegralizacaoCurso, regraContagemPeriodoLetivoEnum, considerarPeriodoTrancadoParaJubilamento ) ");
		sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ?, ? , ? , ?, ? )");
		sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		montarDadosTotalCargaHorariaDisciplinasObrigatorias(obj);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setString(1, obj.getNome());
				sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataCadastro()));
				sqlInserir.setString(3, obj.getSituacao());
				sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFinalVigencia()));
				if (obj.getCurso().intValue() != 0) {
					sqlInserir.setInt(5, obj.getCurso().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}
				sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataAtivacao()));
				sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataDesativacao()));
				if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
					sqlInserir.setInt(8, obj.getResponsavelAtivacao().getCodigo().intValue());
				} else {
					sqlInserir.setNull(8, 0);
				}
				if (obj.getResponsavelDesativacao().getCodigo().intValue() != 0) {
					sqlInserir.setInt(9, obj.getResponsavelDesativacao().getCodigo().intValue());
				} else {
					sqlInserir.setNull(9, 0);
				}
				sqlInserir.setInt(10, obj.getCargaHoraria().intValue());
				if (obj.getCreditos().intValue() != 0) {
					sqlInserir.setInt(11, obj.getCreditos().intValue());
				} else {
					sqlInserir.setNull(11, 0);
				}
				sqlInserir.setInt(12, obj.getTotalDiaLetivoAno());
				sqlInserir.setInt(13, obj.getTotalSemanaLetivaAno());
				sqlInserir.setInt(14, obj.getTotalCargaHorariaAtividadeComplementar());
				sqlInserir.setInt(15, obj.getTotalCargaHorariaEstagio());
				sqlInserir.setString(16, obj.getObservacaoHistorico());
				sqlInserir.setString(17, obj.getSistemaAvaliacao());
				sqlInserir.setString(18, obj.getCompetenciaProfissional());
				sqlInserir.setString(19, obj.getResolucao());
				sqlInserir.setInt(20, obj.getNrMesesConclusaoMatrizCurricular());
				sqlInserir.setString(21, obj.getHabilitacao());
				sqlInserir.setInt(22, obj.getQuantidadeDisciplinasOptativasMatrizCurricular());
				sqlInserir.setDouble(23, obj.getPercentualPermitirIniciarEstagio());
				sqlInserir.setDouble(24, obj.getPercentualPermitirIniciarTcc());
				int i = 24;
				Uteis.setValuePreparedStatement(obj.getDisciplinaPadraoTcc(), ++i, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(obj.getTotalCargaHorariaDisciplinasObrigatorias(), ++i, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(obj.getQtdeAnoSemestreParaIntegralizacaoCurso(), ++i, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(obj.getRegraContagemPeriodoLetivoEnum(), ++i, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(obj.isConsiderarPeriodoTrancadoParaJubilamento(), ++i, sqlInserir, arg0);
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
		getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().incluirGradeCurricularTipoAtividadeComplementarVOs(obj.getListaGradeCurricularTipoAtividadeComplementarVOs(), obj.getCodigo(), usuario);
		getFacadeFactory().getPeriodoLetivoFacade().incluirPeriodoLetivo(obj.getCodigo(), obj.getPeriodoLetivosVOs(), obj.getSituacao(), usuario);
		getFacadeFactory().getGradeCurricularGrupoOptativaFacade().incluirGradeCurricularGrupoOptativaVOs(obj, obj.getSituacao(), usuario);		
		getFacadeFactory().getGradeCurricularEstagioFacade().persistir(obj.getListaGradeCurricularEstagioVO(), false, usuario);
		} catch (Exception e) {
			obj.setCodigo(0);
			for (PeriodoLetivoVO pl : obj.getPeriodoLetivosVOs()) {
				pl.setCodigo(0);
			}
            throw e;
        }
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception {
		validarDados(obj, cursoVO, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE GradeCurricular set nome=?, dataCadastro=?, situacao=?, dataFinalVigencia=?, curso=? , dataativacao=? , datadesativacao=? ,");
		sql.append(" responsavelativacao=? , responsaveldesativacao=?, cargaHoraria=?, creditos=?, totalDiaLetivoAno=?, totalSemanaLetivaAno=?,");
		sql.append(" totalCargaHorariaAtividadeComplementar=?, totalCargaHorariaEstagio = ?, observacaoHistorico = ?, sistemaAvaliacao = ?,");
		sql.append(" competenciaProfissional = ?, resolucao = ?, nrMesesConclusaoMatrizCurricular=?, habilitacao=?, quantidadeDisciplinasOptativasMatrizCurricular=?, ");
		sql.append(" percentualPermitirIniciarEstagio=?, percentualPermitirIniciarTcc=?, disciplinaPadraoTcc=?, totalCargaHorariaDisciplinasObrigatorias=?, ");
		sql.append(" qtdeAnoSemestreParaIntegralizacaoCurso=?, regraContagemPeriodoLetivoEnum=?, considerarPeriodoTrancadoParaJubilamento=? WHERE ((codigo = ?))");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		montarDadosTotalCargaHorariaDisciplinasObrigatorias(obj);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setString(1, obj.getNome());
				sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataCadastro()));
				sqlAlterar.setString(3, obj.getSituacao());
				sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataFinalVigencia()));
				if (obj.getCurso().intValue() != 0) {
					sqlAlterar.setInt(5, obj.getCurso().intValue());
				} else {
					sqlAlterar.setNull(5, 0);
				}
				sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataAtivacao()));
				sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataDesativacao()));
				if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(8, obj.getResponsavelAtivacao().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(8, 0);
				}
				if (obj.getResponsavelDesativacao().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(9, obj.getResponsavelDesativacao().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(9, 0);
				}
				sqlAlterar.setInt(10, obj.getCargaHoraria().intValue());
				if (obj.getCreditos().intValue() != 0) {
					sqlAlterar.setInt(11, obj.getCreditos().intValue());
				} else {
					sqlAlterar.setNull(11, 0);
				}
				sqlAlterar.setInt(12, obj.getTotalDiaLetivoAno());
				sqlAlterar.setInt(13, obj.getTotalSemanaLetivaAno());
				sqlAlterar.setInt(14, obj.getTotalCargaHorariaAtividadeComplementar());
				sqlAlterar.setInt(15, obj.getTotalCargaHorariaEstagio());
				sqlAlterar.setString(16, obj.getObservacaoHistorico());
				sqlAlterar.setString(17, obj.getSistemaAvaliacao());
				sqlAlterar.setString(18, obj.getCompetenciaProfissional());
				sqlAlterar.setString(19, obj.getResolucao());
				sqlAlterar.setInt(20, obj.getNrMesesConclusaoMatrizCurricular());
				sqlAlterar.setString(21, obj.getHabilitacao());
				sqlAlterar.setInt(22, obj.getQuantidadeDisciplinasOptativasMatrizCurricular());
				sqlAlterar.setDouble(23, obj.getPercentualPermitirIniciarEstagio());
				sqlAlterar.setDouble(24, obj.getPercentualPermitirIniciarTcc());
				int i = 24;
				Uteis.setValuePreparedStatement(obj.getDisciplinaPadraoTcc(), ++i, sqlAlterar, arg0);
				Uteis.setValuePreparedStatement(obj.getTotalCargaHorariaDisciplinasObrigatorias(), ++i, sqlAlterar, arg0);
				Uteis.setValuePreparedStatement(obj.getQtdeAnoSemestreParaIntegralizacaoCurso(), ++i, sqlAlterar, arg0);
				Uteis.setValuePreparedStatement(obj.getRegraContagemPeriodoLetivoEnum(), ++i, sqlAlterar, arg0);
				Uteis.setValuePreparedStatement(obj.isConsiderarPeriodoTrancadoParaJubilamento(), ++i, sqlAlterar, arg0);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar, arg0);
				return sqlAlterar;
			}
		});
		getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().alterarGradeCurricularTipoAtividadeComplementarVOs(obj.getListaGradeCurricularTipoAtividadeComplementarVOs(), obj.getCodigo(), usuario);
		getFacadeFactory().getPeriodoLetivoFacade().alterarPeriodoLetivo(obj.getCodigo(), obj.getPeriodoLetivosVOs(), obj.getSituacao(), usuario);
		getFacadeFactory().getGradeCurricularGrupoOptativaFacade().alterarGradeCurricularGrupoOptativaVOs(obj, obj.getSituacao(), usuario);
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaGradeCurricularEstagioVO(), "gradeCurricularEstagio", "gradeCurricular", obj.getCodigo(), usuario);
		getFacadeFactory().getGradeCurricularEstagioFacade().persistir(obj.getListaGradeCurricularEstagioVO(), false, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		try {
			GradeCurricular.excluir(getIdEntidade());
			Iterator<PeriodoLetivoVO> j = obj.getPeriodoLetivosVOs().iterator();
			while (j.hasNext()) {
				PeriodoLetivoVO periodoLetivo = (PeriodoLetivoVO) j.next();
//				Iterator k = periodoLetivo.getGradeDisciplinaVOs().iterator();
//				while (k.hasNext()) {
//					GradeDisciplinaVO gradeDisciplina = (GradeDisciplinaVO) k.next();
//					getFacadeFactory().getDisciplinaPreRequisitoFacade().excluirDisciplinaPreRequisitos(gradeDisciplina.getCodigo(), usuario);
//				}
				getFacadeFactory().getGradeDisciplinaFacade().excluirGradeDisciplinas(periodoLetivo, usuario);
			}
			getFacadeFactory().getPeriodoLetivoFacade().excluirPeriodoLetivo(obj.getCodigo(), usuario);
			getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().excluirGradeCurricularTipoAtividadeComplementar(obj.getListaGradeCurricularTipoAtividadeComplementarVOs(), obj.getCodigo(), usuario);
			obj.getGradeCurricularGrupoOptativaVOs().clear();
			getFacadeFactory().getGradeCurricularGrupoOptativaFacade().excluirGradeCurricularGrupoOptativaVOs(obj, usuario);
			String sql = "DELETE FROM GradeCurricular WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPeriodosLetivosVOs(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		try {
			Iterator<PeriodoLetivoVO> j = obj.getPeriodoLetivosVOs().iterator();
			while (j.hasNext()) {
				PeriodoLetivoVO periodoLetivo = (PeriodoLetivoVO) j.next();
				Iterator<GradeDisciplinaVO> k = periodoLetivo.getGradeDisciplinaVOs().iterator();
				while (k.hasNext()) {
					GradeDisciplinaVO gradeDisciplina = (GradeDisciplinaVO) k.next();
					getFacadeFactory().getDisciplinaPreRequisitoFacade().excluirDisciplinaPreRequisitos(gradeDisciplina.getCodigo(), usuario);
				}
				getFacadeFactory().getGradeDisciplinaFacade().excluirGradeDisciplinas(periodoLetivo, usuario);
			}
			getFacadeFactory().getPeriodoLetivoFacade().excluirPeriodoLetivo(obj.getCodigo(), usuario);
			// String sql = "DELETE FROM GradeCurricular WHERE ((codigo = ?))";
			// getConexao().getJdbcTemplate().update(sql, new
			// Object[]{obj.getCodigo()});
		} catch (Exception e) {
			throw e;
		}
	}

	public Integer consultarCargaHorariaExigidaGrade(Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		// String sqlStr = "select sum(cargahoraria) " + "from gradedisciplina "
		// +
		// "inner join disciplina on disciplina.codigo = gradedisciplina.disciplina "
		// +
		// "inner join periodoletivo on gradedisciplina.periodoletivo = periodoletivo.codigo "
		// + "where periodoletivo.gradecurricular = " + gradeCurricular;
		String sqlStr = "select cargahoraria from gradecurricular where codigo = " + gradeCurricular;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt(1);
		}
		return null;
	}
	
	public Integer consultarCreditoExigidoGrade(Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		String sqlStr = "select creditos from gradecurricular where codigo = " + gradeCurricular;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt(1);
		}
		return null;
	}

	@Override
	public Integer consultarCargaHorariaExigidaGradePeriodoLetivos(Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		String sqlStr = "select sum(totalcargahoraria) from periodoletivo where gradecurricular = " + gradeCurricular;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt(1);
		}
		return null;
	}

	public List<GradeCurricularVO> consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE curso = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<GradeCurricularVO> consultarPorCodigoCursoCodigoDisciplina(Integer curso, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT distinct GradeCurricular.* FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
		sqlStr.append(" and  gradedisciplina.disciplina = ").append(disciplina);		
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso = ").append(curso.intValue()).append(" ");
		}
		sqlStr.append(" union SELECT distinct GradeCurricular.* FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
		sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
		sqlStr.append(" and  gradedisciplinacomposta.disciplina = ").append(disciplina);
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso = ").append(curso.intValue()).append(" ");
		}
		sqlStr.append(" union SELECT distinct GradeCurricular.* FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sqlStr.append(" and  gradecurriculargrupooptativadisciplina.disciplina = ").append(disciplina);
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso = ").append(curso.intValue()).append(" ");
		}
		sqlStr.append(" union SELECT distinct GradeCurricular.* FROM GradeCurricular ");
		sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sqlStr.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
		sqlStr.append(" and  gradedisciplinacomposta.disciplina = ").append(disciplina);
		sqlStr.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso = ").append(curso.intValue()).append(" ");
		}
		sqlStr.append(" order by situacao, codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<GradeCurricularVO> consultarGradeCurricularAtivaPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM GradeCurricular WHERE situacao = 'AT' and curso = ").append(valorConsulta.intValue()).append(" ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public GradeCurricularVO consultarPorSituacaoGradeCurso(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE curso = " + valorConsulta.intValue() + " and situacao = '" + situacao + "' ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new GradeCurricularVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GradeCurricularVO> consultarPorSituacaoGradeCursoLista(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE curso = " + valorConsulta.intValue() + " and situacao = '" + situacao + "' ORDER BY GradeCurricular.codigo desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GradeCurricularVO> consultarPorCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE curso >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GradeCurricularVO> consultarGradeCurricularCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<GradeCurricularVO> objetos = new ArrayList<>(0);
		String sqlStr = "SELECT GradeCurricular.* FROM GradeCurricular,Curso WHERE GradeCurricular.curso = Curso.codigo and Curso.codigo = " + valorConsulta.intValue() + " ORDER BY GradeCurricular.dataCadastro desc ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (tabelaResultado.next()) {
			objetos.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return objetos;
	}

	public List<GradeCurricularVO> consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE ((dataCadastro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCadastro <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataCadastro";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GradeCurricularVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GradeCurricularVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GradeCurricularVO> consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct gradecurricular.* " + "FROM matriculaperiodo " + "INNER JOIN gradecurricular ON matriculaperiodo.gradecurricular = gradecurricular.codigo " + "INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula " + "WHERE matricula.matricula = '" + matricula + "'" + "ORDER BY gradecurricular.datacadastro";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<GradeCurricularVO> consultarGradeAtualGradeAntigaPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct gradecurricular.* FROM matriculaperiodo ");
		sb.append(" INNER JOIN gradecurricular ON matriculaperiodo.gradecurricular = gradecurricular.codigo ");
		sb.append(" INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" SELECT distinct gradecurricular.* FROM matricula ");
		sb.append(" INNER JOIN gradecurricular ON matricula.gradecurricularatual = gradecurricular.codigo ");		
		sb.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradecurricular.* from transferenciamatrizcurricular tmc ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = tmc.gradeorigem ");
		sb.append(" inner join matricula on matricula.matricula = tmc.matricula ");
		sb.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		sb.append(" union ");
		sb.append(" select distinct gradecurricular.* from historico ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = historico.matrizcurricular ");
		sb.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sb.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public GradeCurricularVO consultarGradeCurricularAtualMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT *  FROM gradecurricular ");
		sqlStr.append(" WHERE  codigo = (select gradecurricularatual from matricula where matricula  = '").append(matricula.toUpperCase()).append("')");		
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new GradeCurricularVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public GradeCurricularVO consultarGradeCurricularDaUltimaMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct gradecurricular.*, (matriculaperiodo.ano || '/' || matriculaperiodo.semestre) as anosemestre, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end as situacaoMatriculaPeriodo, matriculaperiodo.codigo as matriculaperiodo  FROM matriculaperiodo ");
		sqlStr.append(" INNER JOIN gradecurricular ON matriculaperiodo.gradecurricular = gradecurricular.codigo");
		sqlStr.append(" INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula");
		sqlStr.append(" WHERE Upper(matricula.matricula) = '").append(matricula.toUpperCase()).append("'");		
		sqlStr.append(" order by anosemestre desc, situacaoMatriculaPeriodo, matriculaperiodo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new GradeCurricularVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public GradeCurricularVO consultarGradeCurricularPorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct gradecurricular.* FROM matriculaperiodo ");
		sqlStr.append(" INNER JOIN gradecurricular ON matriculaperiodo.gradecurricular = gradecurricular.codigo");
		sqlStr.append(" WHERE matriculaPeriodo.codigo = ").append(matriculaPeriodo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new GradeCurricularVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>GradeCurricularVO</code> resultantes da consulta.
	 */
	public static List<GradeCurricularVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GradeCurricularVO> vetResultado = new ArrayList<GradeCurricularVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>GradeCurricularVO</code>.
	 * 
	 * @return O objeto da classe <code>GradeCurricularVO</code> com os dados
	 *         devidamente montados.
	 */
	public static GradeCurricularVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GradeCurricularVO obj = new GradeCurricularVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargahoraria")));		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setNrMesesConclusaoMatrizCurricular(dadosSQL.getInt("nrMesesConclusaoMatrizCurricular"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.setObservacaoHistorico(dadosSQL.getString("observacaoHistorico"));
		obj.setSistemaAvaliacao(dadosSQL.getString("sistemaAvaliacao"));
		obj.setCompetenciaProfissional(dadosSQL.getString("competenciaProfissional"));
		obj.setResolucao(dadosSQL.getString("resolucao"));
		obj.setDataFinalVigencia(dadosSQL.getDate("dataFinalVigencia"));
		obj.setCurso(new Integer(dadosSQL.getInt("curso")));		
		obj.setTotalCargaHorariaAtividadeComplementar(new Integer(dadosSQL.getInt("totalCargaHorariaAtividadeComplementar")));
		obj.setCreditos(new Integer(dadosSQL.getInt("creditos")));
		obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
		obj.setDataDesativacao(dadosSQL.getDate("dataDesativacao"));
		obj.getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAtivacao")));
		obj.getResponsavelDesativacao().setCodigo(new Integer(dadosSQL.getInt("responsavelDesativacao")));
		obj.setNovoObj(Boolean.FALSE);
		obj.setTotalDiaLetivoAno(dadosSQL.getInt("totalDiaLetivoAno"));
		obj.setTotalSemanaLetivaAno(dadosSQL.getInt("totalSemanaLetivaAno"));
		obj.setQuantidadeDisciplinasOptativasMatrizCurricular(dadosSQL.getInt("quantidadeDisciplinasOptativasMatrizCurricular"));
		obj.setHabilitacao(dadosSQL.getString("habilitacao"));
		obj.setSistemaAvaliacao(dadosSQL.getString("sistemaavaliacao"));
		obj.setTotalCargaHorariaDisciplinasObrigatorias(dadosSQL.getInt("totalCargaHorariaDisciplinasObrigatorias"));
		obj.setQtdeAnoSemestreParaIntegralizacaoCurso(dadosSQL.getInt("qtdeAnoSemestreParaIntegralizacaoCurso"));
		obj.setRegraContagemPeriodoLetivoEnum(RegraContagemPeriodoLetivoEnum.valueOf(dadosSQL.getString("regraContagemPeriodoLetivoEnum")));
		obj.setConsiderarPeriodoTrancadoParaJubilamento(dadosSQL.getBoolean("considerarperiodotrancadoparajubilamento"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			obj.setPeriodoLetivosVOs(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(obj.getCodigo(), false, nivelMontarDados, usuario));
			return obj;
		}
		obj.setPeriodoLetivosVOs(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setListaGradeCurricularTipoAtividadeComplementarVOs(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarPorCodigoGrade(obj.getCodigo(), false, usuario));
		obj.setGradeCurricularGrupoOptativaVOs(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorGradeCurricular(obj.getCodigo(), NivelMontarDados.TODOS, usuario));
		obj.setListaGradeCurricularEstagioVO(getFacadeFactory().getGradeCurricularEstagioFacade().consultarPorGradeCurricularVO(obj, nivelMontarDados, usuario));
		obj.setTotalCargaHorariaEstagio(obj.getListaGradeCurricularEstagioVO().stream().mapToInt(GradeCurricularEstagioVO::getCargaHorarioObrigatorio).reduce(0, (a, b) -> (a + b)));
		obj.setPercentualPermitirIniciarEstagio(dadosSQL.getDouble("percentualPermitirIniciarEstagio"));
		obj.setPercentualPermitirIniciarTcc(dadosSQL.getDouble("percentualPermitirIniciarTcc"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		/**
		 * Utilizado apenas para a geracao do relatorio DisciplinasGradeRel
		 * Layout 1
		 */
		if (obj.getDataAtivacao() != null) {
			obj.setAnoSemestreDataAtivacao(Uteis.getAnoData(obj.getDataAtivacao()) + "/" + Uteis.getSemestreData(obj.getDataAtivacao()));
		}
		if (obj.getDataFinalVigencia() != null) {
			obj.setAnoSemestreDataFinalVigencia(Uteis.getAnoData(obj.getDataFinalVigencia()) + "/" + Uteis.getSemestreData(obj.getDataFinalVigencia()));
		}
		obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuario));
		obj.getDisciplinaPadraoTcc().setCodigo(dadosSQL.getInt("disciplinapadraotcc"));
		if(Uteis.isAtributoPreenchido(obj.getDisciplinaPadraoTcc())) {
			obj.setDisciplinaPadraoTcc(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaPadraoTcc().getCodigo(), nivelMontarDados, usuario));	
		}
		montarDadosResponsavelAtivacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelDesativacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}
	
	private GradeCurricularVO montarDadosEmGradeCurricularExistente(GradeCurricularVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargahoraria")));
        obj.setExisteGradeCurricularEstagio(Uteis.isAtributoPreenchido(dadosSQL.getInt("qtde")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.setObservacaoHistorico(dadosSQL.getString("observacaoHistorico"));
		obj.setSistemaAvaliacao(dadosSQL.getString("sistemaAvaliacao"));
		obj.setCompetenciaProfissional(dadosSQL.getString("competenciaProfissional"));
		obj.setResolucao(dadosSQL.getString("resolucao"));
		obj.setDataFinalVigencia(dadosSQL.getDate("dataFinalVigencia"));
		obj.setCurso(new Integer(dadosSQL.getInt("curso")));
		obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargahoraria")));		
		obj.setTotalCargaHorariaAtividadeComplementar(new Integer(dadosSQL.getInt("totalCargaHorariaAtividadeComplementar")));
		obj.setCreditos(new Integer(dadosSQL.getInt("creditos")));
		obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
		obj.setDataDesativacao(dadosSQL.getDate("dataDesativacao"));
		obj.getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAtivacao")));
		obj.getResponsavelDesativacao().setCodigo(new Integer(dadosSQL.getInt("responsavelDesativacao")));
		obj.setNovoObj(Boolean.FALSE);
		obj.setTotalDiaLetivoAno(dadosSQL.getInt("totalDiaLetivoAno"));
		obj.setTotalSemanaLetivaAno(dadosSQL.getInt("totalSemanaLetivaAno"));
		obj.setNrMesesConclusaoMatrizCurricular(dadosSQL.getInt("nrMesesConclusaoMatrizCurricular"));
		obj.setQuantidadeDisciplinasOptativasMatrizCurricular(dadosSQL.getInt("quantidadeDisciplinasOptativasMatrizCurricular"));
		obj.setHabilitacao(dadosSQL.getString("habilitacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setPeriodoLetivosVOs(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setListaGradeCurricularTipoAtividadeComplementarVOs(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarPorCodigoGrade(obj.getCodigo(), false, usuario));
		obj.setGradeCurricularGrupoOptativaVOs(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorGradeCurricular(obj.getCodigo(), NivelMontarDados.TODOS, usuario));
		obj.setListaGradeCurricularEstagioVO(getFacadeFactory().getGradeCurricularEstagioFacade().consultarPorGradeCurricularVO(obj, nivelMontarDados, usuario));
		obj.setTotalCargaHorariaEstagio(obj.getListaGradeCurricularEstagioVO().stream().mapToInt(GradeCurricularEstagioVO::getCargaHorarioObrigatorio).reduce(0, (a, b) -> (a + b)));
		obj.setPercentualPermitirIniciarEstagio(dadosSQL.getDouble("percentualPermitirIniciarEstagio"));
		obj.setPercentualPermitirIniciarTcc(dadosSQL.getDouble("percentualPermitirIniciarTcc"));
		obj.getDisciplinaPadraoTcc().setCodigo(dadosSQL.getInt("disciplinapadraotcc"));
		if(Uteis.isAtributoPreenchido(obj.getDisciplinaPadraoTcc())) {
			obj.setDisciplinaPadraoTcc(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaPadraoTcc().getCodigo(), nivelMontarDados, usuario));	
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		/**
		 * Utilizado apenas para a geracao do relatorio DisciplinasGradeRel
		 * Layout 1
		 */
		if (obj.getDataAtivacao() != null) {
			obj.setAnoSemestreDataAtivacao(Uteis.getAnoData(obj.getDataAtivacao()) + "/" + Uteis.getSemestreData(obj.getDataAtivacao()));
		}
		if (obj.getDataFinalVigencia() != null) {
			obj.setAnoSemestreDataFinalVigencia(Uteis.getAnoData(obj.getDataFinalVigencia()) + "/" + Uteis.getSemestreData(obj.getDataFinalVigencia()));
		}
		obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuario));
		montarDadosResponsavelAtivacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelDesativacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosResponsavelAtivacao(GradeCurricularVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAtivacao().getCodigo().intValue() == 0) {
			obj.setResponsavelAtivacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelAtivacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAtivacao().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavelDesativacao(GradeCurricularVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelDesativacao().getCodigo().intValue() == 0) {
			obj.setResponsavelDesativacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelDesativacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDesativacao().getCodigo(), nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGradeCurriculars(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		GradeCurricular.excluir(getIdEntidade());
		String sql = "DELETE FROM GradeCurricular WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		Iterator<PeriodoLetivoVO> j = obj.getPeriodoLetivosVOs().iterator();
		while (j.hasNext()) {
			PeriodoLetivoVO periodoLetivo = (PeriodoLetivoVO) j.next();
			Iterator<GradeDisciplinaVO> k = periodoLetivo.getGradeDisciplinaVOs().iterator();
			while (k.hasNext()) {
				GradeDisciplinaVO gradeDisciplina = k.next();
				getFacadeFactory().getDisciplinaPreRequisitoFacade().excluirDisciplinaPreRequisitos(gradeDisciplina.getCodigo(), usuario);
			}
			getFacadeFactory().getGradeDisciplinaFacade().excluirGradeDisciplinas(periodoLetivo, usuario);
		}
		getFacadeFactory().getPeriodoLetivoFacade().excluirPeriodoLetivo(obj.getCodigo(), usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(GradeCurricularVO obj, CursoVO cursoVO, Boolean validarAlteracaoMatrizCurricularAtivaInativa, UsuarioVO usuario) throws Exception {
		if (obj.getCodigo().equals(0)) {
			incluir(obj, cursoVO, usuario);
		} else {
			alterar(obj, cursoVO, usuario);
			realizarCorrecaoImpactoAlteracaoMatrizCurricularAtivaInativa(obj, validarAlteracaoMatrizCurricularAtivaInativa, usuario);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirAtivacaoGradeCurricular(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE GradeCurricular set situacao=?, dataativacao=?, responsavelativacao=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getSituacao(), obj.getDataAtivacao(), obj.getResponsavelAtivacao().getCodigo(), obj.getCodigo() });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirDesativacaoGradeCurricular(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE GradeCurricular set situacao=?, datadesativacao=?, responsaveldesativacao=?, dataFinalVigencia=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getSituacao(), obj.getDataDesativacao(), obj.getResponsavelDesativacao().getCodigo(), obj.getDataFinalVigencia(), obj.getCodigo() });
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>GradeCurricularVO</code> relacionados a um objeto da classe
	 * <code>academico.Curso</code>.
	 * 
	 * @param curso
	 *            Atributo de <code>academico.Curso</code> a ser utilizado para
	 *            localizar os objetos da classe <code>GradeCurricularVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>GradeCurricularVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List<GradeCurricularVO> consultarGradeCurriculars(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<GradeCurricularVO> objetos = new ArrayList<>(0);
		String sql = "SELECT * FROM GradeCurricular WHERE curso = ? ORDER BY dataCadastro desc, dataAtivacao desc";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { curso });
		while (resultado.next()) {
			objetos.add(GradeCurricular.montarDados(resultado, nivelMontarDados, usuario));
		}
		return objetos;
	}
	
	@Override
	public GradeCurricularVO consultarPorChavePrimariaUnica(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT GradeCurricular.*, ( SELECT count(gradecurricularestagio.codigo) QTDE FROM gradecurricularestagio WHERE gradecurricularestagio.gradecurricular = gradecurricular.codigo) FROM GradeCurricular WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( GradeCurricular ).");
		}
		GradeCurricularVO gradeCurricularVO = montarDados(tabelaResultado, nivelMontarDados, usuario);
		gradeCurricularVO.setExisteGradeCurricularEstagio(Uteis.isAtributoPreenchido(tabelaResultado.getInt("qtde")));
		return gradeCurricularVO;
	}

	public GradeCurricularVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getGradeCurricularVO(codigoPrm);
	}
	
	public void realizarMongagemDadosEmGradeCurricularExistente(GradeCurricularVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT GradeCurricular.*, ( SELECT count(gradecurricularestagio.codigo) QTDE FROM gradecurricularestagio WHERE gradecurricularestagio.gradecurricular = gradecurricular.codigo) FROM GradeCurricular WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, obj.getCodigo());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( GradeCurricular ).");
		}
		montarDadosEmGradeCurricularExistente(obj, tabelaResultado, nivelMontarDados, usuario);
	}

	public GradeCurricularVO consultarPorTurmaNivelComboBox(Integer turma, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select gradecurricular.codigo, gradecurricular.nome from turma ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = turma.gradecurricular ");
		sb.append(" where turma.codigo =  ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			GradeCurricularVO obj = new GradeCurricularVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			return obj;
		}
		return new GradeCurricularVO();
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return GradeCurricular.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		GradeCurricular.idEntidade = idEntidade;
	}

	public void ativarGrade(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidade("GradeCurricular_ativarGrade", usuario);
	}

	public void desativarGrade(GradeCurricularVO obj, UsuarioVO usuario) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidade("GradeCurricular_desativarGrade", usuario);
	}

	public void validarDados(GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome() == null || obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Grade Curricular) deve ser informado.");
		}
		if (obj.getDataCadastro() == null) {
			throw new ConsistirException("O campo DATA DE CADASTRO (Grade Curricular) deve ser informado.");
		}
		if (obj.getSituacao() == null || obj.getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Grade Curricular) deve ser informado.");
		}
		if (cursoVO.getRegime().equals("SE")) {
			if (obj.getCargaHoraria().intValue() == 0) {
				throw new ConsistirException("O campo CARGA HORÁRIA (Matriz Curricular) deve ser informado.");
			}
		} else {
			if (obj.getCreditos().intValue() == 0) {
				throw new ConsistirException("O campo CRÉDITOS (Matriz Curricular) deve ser informado.");
			}
		}
		if (obj.getPeriodoLetivosVOs().isEmpty()) {
			throw new ConsistirException("Para prosseguir é necessário adicionar os Períodos Letivos (Definir Períodos Letivos Curso).");
		}
	}

	public Boolean consultarGradeCurricularUltimaMatriculaPeriodo(String matricula, Integer gradeCurricular, UsuarioVO usuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo from gradecurricular where ").append(gradeCurricular).append(" = ");
		sb.append(" (select gradecurricular from matriculaperiodo where matricula = '" + matricula + "' order by ano desc, semestre desc, codigo desc limit 1) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public List<GradeCurricularVO> consultarGradeCurricularAnteriorPorMatricula(String matricula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct gradeCurricular.codigo, gradeCurricular.nome from transferenciamatrizcurricular ");
		sb.append("inner join gradecurricular on gradecurricular.codigo = transferenciamatrizcurricular.gradeorigem ");
		sb.append("where transferenciamatrizcurricular.matricula = '").append(matricula).append("'");
		sb.append(" union ");
		sb.append("select distinct gradeCurricular.codigo, gradeCurricular.nome from gradecurricular ");
		sb.append("inner join historico on historico.matrizcurricular = gradecurricular.codigo ");
		sb.append("inner join matricula on matricula.matricula = historico.matricula ");
		sb.append("where matricula.matricula = '").append(matricula).append("'");
		sb.append(" and historico.matrizcurricular != matricula.gradecurricularatual ");
		sb.append("order by nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<GradeCurricularVO> listaGradeCurricularVOs = new ArrayList<GradeCurricularVO>(0);
		while (tabelaResultado.next()) {
			GradeCurricularVO obj = new GradeCurricularVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaGradeCurricularVOs.add(obj);
		}
		return listaGradeCurricularVOs;
	}

    @Override
    public SqlRowSet consultarDadosGeracaoRelatorioDisciplinasGradeRel(Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct (gradecurricular.nome) as gradeCurricular, curso.codigo as curso, curso.nrregistrointerno as autorizacao, curso.nome as nomeCurso, gradeCurricular.dataAtivacao as dataAtivacao, gradeCurricular.dataFinalVigencia as dataFinalVigencia, gradeCurricular.cargahoraria, gradeCurricular.creditos, gradeCurricular.totalcargahorariaestagio, gradeCurricular.totalcargahorariaatividadecomplementar, ");
        sqlStr.append("autorizacaocurso.nome as reconhecimento, autorizacaocurso.data as dataReconhecimento, curso.datapublicacaodo as dataAutorizacao from gradecurricular ");
        sqlStr.append("inner join curso on curso.codigo = gradecurricular.curso ");
        sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
        sqlStr.append("left join autorizacaocurso  on autorizacaocurso.curso = curso.codigo and autorizacaocurso.codigo = (select codigo from autorizacaocurso ac where ac.curso = curso.codigo order by data desc limit 1 ) ");
        sqlStr.append("left join turma on turma.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("where gradecurricular.codigo = ").append(gradeCurricular);
        if (!unidadeEnsinoCurso.equals(0)) {
            sqlStr.append(" and unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
        }
        if (!curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (!turma.equals(0)) {
            sqlStr.append(" and turma.codigo =  ").append(turma);
        }
        return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    }

    @Override
    public SqlRowSet consultarDadosGeracaoRelatorioDisciplinasGradeDisciplinasRel(Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct gradedisciplina.ordem, disciplina.codigo as disciplina, gradecurricular.codigo as gradeCurricular, periodoletivo.periodoletivo as periodo, periodoletivo.descricao as periodoLetivo, case when disciplinaEixoTematico.codigo is not null then disciplinaEixoTematico.nome else disciplina.nome end as nomeDisciplina, ");
        sqlStr.append("gradedisciplina.cargahoraria as chDisciplina, gradedisciplina.diversificada as disciplinaDiversificada ");
        sqlStr.append("from gradecurricular ");
        sqlStr.append("inner join curso on curso.codigo = gradecurricular.curso ");
        sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sqlStr.append("inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
        sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
        sqlStr.append("left join disciplina as disciplinaEixoTematico on disciplinaEixoTematico.codigo = gradedisciplina.disciplinaEixoTematico ");
        sqlStr.append("left join turma on turma.gradecurricular = gradecurricular.codigo ");
        sqlStr.append("where gradecurricular.codigo = ").append(gradeCurricular);
        if (!unidadeEnsinoCurso.equals(0)) {
            sqlStr.append(" and unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
        }
        if (!curso.equals(0)) {
            sqlStr.append(" and curso.codigo = ").append(curso);
        }
        if (!turma.equals(0)) {
            sqlStr.append(" and turma.codigo =  ").append(turma);
        }
        if (!apresentarDisciplinaComposta) {
            sqlStr.append(" and disciplina.disciplinacomposta = false ");
        }
        sqlStr.append(" order by periodoletivo.periodoletivo, gradedisciplina.ordem, nomeDisciplina");
        return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    }

	@Override
	public void adicionarGradeCurricularGrupoOptativaVO(GradeCurricularVO gradeCurricularVO, GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) throws Exception {
		getFacadeFactory().getGradeCurricularGrupoOptativaFacade().validarDados(gradeCurricularGrupoOptativaVO);
		for (GradeCurricularGrupoOptativaVO obj : gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()) {
			if (obj.getDescricao().trim().equalsIgnoreCase(gradeCurricularGrupoOptativaVO.getDescricao().trim())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_gradeCurricular_grupoOptativaExistente"));
			}
		}
		gradeCurricularVO.getGradeCurricularGrupoOptativaVOs().add(gradeCurricularGrupoOptativaVO);
	}

	@Override
	public void removerGradeCurricularGrupoOptativaVO(GradeCurricularVO gradeCurricularVO, GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) throws Exception {
		int index = 0;
		for (GradeCurricularGrupoOptativaVO obj : gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()) {
			if (obj.getDescricao().trim().equalsIgnoreCase(gradeCurricularGrupoOptativaVO.getDescricao().trim())) {
				if (obj.getCodigo() != null && obj.getCodigo() > 0) {
					for (PeriodoLetivoVO periodoLetivoVO : gradeCurricularVO.getPeriodoLetivosVOs()) {
						if (periodoLetivoVO.getControleOptativaGrupo() && periodoLetivoVO.getGradeCurricularGrupoOptativa().getCodigo().intValue() == obj.getCodigo().intValue()) {
							throw new ConsistirException(UteisJSF.internacionalizar("msg_gradeCurricular_grupoOptativaRemoverVinculo").replace("{0}", periodoLetivoVO.getDescricao()));
						}
					}
				}
				gradeCurricularVO.getGradeCurricularGrupoOptativaVOs().remove(index);
				return;
			}
			index++;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarGradeCurricularEstagio(GradeCurricularVO gradeCurricularVO, GradeCurricularEstagioVO gre, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getGradeCurricularEstagioFacade().validarDados(gre);
		gre.setGradeCurricularVO(gradeCurricularVO);
		gre.setTextoPadraoDeclaracaoVO(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(gre.getTextoPadraoDeclaracaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		gre.setQuestionarioRelatorioFinal(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gre.getQuestionarioRelatorioFinal().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		if(Uteis.isAtributoPreenchido(gre.getQuestionarioAproveitamentoPorDocenteRegular())) {
			gre.setQuestionarioAproveitamentoPorDocenteRegular(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gre.getQuestionarioAproveitamentoPorDocenteRegular().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		if(Uteis.isAtributoPreenchido(gre.getQuestionarioAproveitamentoPorLicenciatura())) {
			gre.setQuestionarioAproveitamentoPorLicenciatura(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gre.getQuestionarioAproveitamentoPorLicenciatura().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		if(Uteis.isAtributoPreenchido(gre.getQuestionarioEquivalencia())) {
			gre.setQuestionarioEquivalencia(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gre.getQuestionarioEquivalencia().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		int index = 0;
		for (GradeCurricularEstagioVO objExistente : gradeCurricularVO.getListaGradeCurricularEstagioVO()) {
			if(objExistente.getNome().equals(gre.getNome())) {
				gre.setCodigo(objExistente.getCodigo());
				gradeCurricularVO.getListaGradeCurricularEstagioVO().set(index, gre);
				return;
			}
			index++;
		}
		gradeCurricularVO.getListaGradeCurricularEstagioVO().add(gre);
		
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerGradeCurricularEstagio(GradeCurricularVO gradeCurricularVO, GradeCurricularEstagioVO gre, UsuarioVO usuarioLogado) {
		gradeCurricularVO.getListaGradeCurricularEstagioVO().removeIf(p-> p.getNome().equals(gre.getNome()));
	}

	public HashMap<Integer, Integer> consultarListaCodigoDisciplinaCodigoPeriodoLetivoMatrizCurricular(Integer matrizCurricular) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select gradeDisciplina.disciplina, periodoletivo.codigo as periodoletivo from gradeDisciplina ");
		sql.append("inner join periodoletivo on (periodoletivo.codigo = gradeDisciplina.periodoLetivo) ");
		sql.append("inner join gradeCurricular on (gradeCurricular.codigo = periodoletivo.gradeCurricular) ");
		sql.append("where gradeCurricular.codigo = ").append(matrizCurricular.intValue());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		HashMap<Integer, Integer> mapa = new HashMap<Integer, Integer>();
		if (rs.next()) {
			mapa.put(rs.getInt("disciplina"), rs.getInt("periodoletivo"));
		}
		return mapa;
	}

	@Override
	public boolean consultarDisciplinaClassificadoComoTCC(Integer matrizCurricular) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(distinct (gradecurricular.codigo)) as QTDE  from gradecurricular ");
		sql.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sql.append(" where gradecurricular.codigo = ").append(matrizCurricular);
		sql.append(" and (gradedisciplina.disciplinatcc = true ");
		sql.append(" or disciplina.classificacaoDisciplina = '").append(ClassificacaoDisciplinaEnum.TCC.name()).append("' ");
		sql.append(" )");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public Integer consultarCargaHorariaTCCPorMatrizCurricular(Integer matrizCurricular) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(gradedisciplina.cargahoraria) as QTDE  from gradecurricular ");
		sql.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sql.append(" where gradecurricular.codigo = ").append(matrizCurricular);
		sql.append(" and (gradedisciplina.disciplinatcc = true ");
		sql.append(" or disciplina.classificacaoDisciplina = '").append(ClassificacaoDisciplinaEnum.TCC.name()).append("' ");
		sql.append(" )");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public Integer consultarCargaHorariaObrigatoriaEstagioPorMatrizCurricular(Integer matrizCurricular) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargahorarioobrigatorio) as totalcargahorariaestagio from gradecurricularestagio where gradecurricular = " + matrizCurricular);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("totalcargahorariaestagio");
		}
		return 0;
	}

	@Override
	public void realizarVerificacaoDisciplinaJaAdicionada(GradeCurricularVO gradeCurricularVO, DisciplinaVO disciplinaVO) throws Exception {

		for (PeriodoLetivoVO periodoLetivoVO : gradeCurricularVO.getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gradeDisciplina : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gradeDisciplina.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
					throw new Exception(UteisJSF.internacionalizar("msg_GradeCurricular_disciplinaExistenteGradeDisciplina").replace("{0}", periodoLetivoVO.getDescricao()));
				}
				if (gradeDisciplina.getDisciplinaComposta()) {
					for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplina.getGradeDisciplinaCompostaVOs()) {
						if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
							throw new Exception(UteisJSF.internacionalizar("msg_GradeCurricular_disciplinaExistenteGradeDisciplinaComposta").replace("{0}", periodoLetivoVO.getDescricao()).replace("{1}", gradeDisciplina.getDisciplina().getNome()));
						}
					}
				}
			}
		}
		
		for (GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO : gradeCurricularVO.getGradeCurricularGrupoOptativaVOs()) {
			for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()) {
				if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
					throw new Exception(UteisJSF.internacionalizar("msg_GradeCurricular_disciplinaExistenteGradeDisciplinaOptativa").replace("{0}", gradeCurricularGrupoOptativaVO.getDescricao()));
				}
				if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaComposta()) {
					for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
						if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
							throw new Exception(UteisJSF.internacionalizar("msg_GradeCurricular_disciplinaExistenteGradeDisciplinaOptativaComposta").replace("{0}", gradeCurricularGrupoOptativaVO.getDescricao()).replace("{1}", gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getNome()));
						}
					}
				}
			}
		}
	}
	
	@Override
	public List<GradeCurricularVO> consultarPorMatriculaGradeCurricularVOsVinculadaHistoricoInclusaoExclusaoDisciplina(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct gradecurricular.* from gradecurricular ");
		sqlStr.append("inner join historico on historico.matrizcurricular = gradecurricular.codigo ");
		sqlStr.append("where historico.matricula = '").append(matricula).append("' ");
		sqlStr.append(" union ");
		sqlStr.append("select distinct gradecurricular.* from matricula ");
		sqlStr.append("inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo ");
		sqlStr.append("where matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
    public List<GradeCurricularVO> consultarGradeCurricularAtualFiltrarRenovacaoTurmaNivelCombobox(Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre) throws Exception{
    	StringBuilder sql  =  new StringBuilder("select distinct gradecurricular.codigo, gradecurricular.nome from matriculaperiodo ");
    	sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
    	sql.append(" inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo ");
    	sql.append(" where 1=1 ");
    	if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);	
    	}
    	if(Uteis.isAtributoPreenchido(curso)) {
    		sql.append(" and matricula.curso = ").append(curso);	
    	}
    	if(Uteis.isAtributoPreenchido(turma)) {
    		sql.append(" and matriculaperiodo.turma = ").append(turma);	
    	}
    	
    	if(ano != null && !ano.trim().isEmpty()){
    		sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
    		if(semestre != null && !semestre.trim().isEmpty()){
    			sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
    		}
    	}    	
    	sql.append(" order by gradecurricular.nome ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	List<GradeCurricularVO> gradeCurricularVOs = new ArrayList<GradeCurricularVO>(0);
    	GradeCurricularVO obj = null;
    	while(rs.next()){
    		obj = new GradeCurricularVO();
    		obj.setCodigo(rs.getInt("codigo"));
    		obj.setNome(rs.getString("nome"));
    		gradeCurricularVOs.add(obj);
    	}
    	return gradeCurricularVOs;
    	
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarTotalCargaHorariaGradeCurricularPorCodigo(Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sum(totalcargahoraria) AS totalcargahoraria from (");
		sb.append(" select distinct periodoletivo.codigo, periodoletivo.totalcargahoraria from periodoletivo where gradecurricular = ").append(gradeCurricular);
		sb.append(" ) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalcargahoraria");
		}
		return 0;
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GradeDisciplinaObject consultarGradeDisciplinaCursoMatriculaOnlineExterna(Integer codigoCurso) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select gradecurricular.codigo as codigogradecurricular, disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria from gradedisciplina");
		sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular");
		sb.append(" inner join curso on curso.codigo = gradecurricular.curso");
		sb.append(" where curso.codigo = ").append(codigoCurso);
		sb.append(" and gradecurricular.situacao = 'AT' ");
		sb.append(" and gradecurricular.codigo = (select gc.codigo from gradecurricular gc where gc.curso = curso.codigo and gc.situacao = 'AT'  order by gc.codigo desc limit 1)");
		sb.append(" order by periodoletivo.periodoletivo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		GradeDisciplinaObject gradeDisciplinaObject = new GradeDisciplinaObject();
		DisciplinaObject disciplinaObject = null;
		while (tabelaResultado.next()) {
			gradeDisciplinaObject.setCodigo(tabelaResultado.getInt("codigogradecurricular"));
			disciplinaObject = new DisciplinaObject();
			disciplinaObject.setCodigo(tabelaResultado.getInt("codigo"));
			disciplinaObject.setNome(tabelaResultado.getString("nome"));
			disciplinaObject.setCargaHoraria(tabelaResultado.getInt("cargahoraria"));
			gradeDisciplinaObject.getDisciplinaObjects().add(disciplinaObject);
		}
		return gradeDisciplinaObject;
	}

	@Override
	public List<GradeCurricularVO> consultarPorCodigoCursoPossuemAtividadeComplementar(Integer valorConsulta, UsuarioVO usuario) throws Exception {		
		String sqlStr = "SELECT * FROM GradeCurricular WHERE curso = " + valorConsulta.intValue();
		sqlStr += " and exists (select codigo from gradecurriculartipoatividadecomplementar where gradecurricular.codigo = gradecurriculartipoatividadecomplementar.gradecurricular  )";
		sqlStr += " ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	@Override
	public Integer consultarTotalDisciplinaObrigatoria(Integer gradeCurricular, boolean desconsiderarDisciplinaEstagioObrigatorio) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sum(gradedisciplina.cargahoraria) as totalcargahoraria from periodoletivo inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  where gradedisciplina.tipodisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = ").append(gradeCurricular);
		if (desconsiderarDisciplinaEstagioObrigatorio) {
			sb.append(" and gradedisciplina.disciplinaestagio = false");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalcargahoraria");
		}
		return 0;
	}	
	
	@Override
	public Integer consultarTotalDisciplinaEstagio(Integer gradeCurricular) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select sum(gradedisciplina.cargahoraria) as totalcargahorariadisciplinaestagio from periodoletivo inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  where gradedisciplina.tipodisciplina in ('OB', 'LG') and periodoletivo.gradecurricular = ").append(gradeCurricular);
		sb.append(" and gradedisciplina.disciplinaestagio = true");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalcargahorariadisciplinaestagio");
		}
		return 0;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCorrecaoImpactoAlteracaoMatrizCurricularAtivaInativa(GradeCurricularVO gradeCurricularVO, Boolean validarAlteracaoMatrizCurricularAtivaInativa, UsuarioVO usuarioVO) throws Exception {
		if (validarAlteracaoMatrizCurricularAtivaInativa) {
			
			GradeCurricularVO gradeCurricularOriginalVO = gradeCurricularVO.getGradeCurricularOriginalVO();
			
			realizarCorrecaoImpactoGradeDisciplina(gradeCurricularVO, gradeCurricularOriginalVO, usuarioVO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCorrecaoImpactoGradeDisciplina(GradeCurricularVO gradeCurricularVO, GradeCurricularVO gradeCurricularOriginalVO, UsuarioVO usuarioVO) throws Exception {
		for (PeriodoLetivoVO periodoLetivoAntigoVO : gradeCurricularOriginalVO.getPeriodoLetivosVOs()) {
			for (PeriodoLetivoVO periodoLetivoVO : gradeCurricularVO.getPeriodoLetivosVOs()) {

				if (periodoLetivoAntigoVO.getCodigo().equals(periodoLetivoVO.getCodigo())) {
					
//					INCLUSÃO GRADE DISCIPLINA
					realizarcorrecaoImpactoInclusaoNovaGradeDisciplina(periodoLetivoAntigoVO, periodoLetivoVO, gradeCurricularVO, usuarioVO);
					
//					ALTERAÇÃO GRADE DISCIPLINA
					realizarCorrecaoImpactoAlteracaoGradeDisciplina(gradeCurricularVO, periodoLetivoAntigoVO, periodoLetivoVO, usuarioVO);
					
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCorrecaoImpactoAlteracaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, PeriodoLetivoVO periodoLetivoAntigoVO, PeriodoLetivoVO periodoLetivoVO, UsuarioVO usuarioVO) throws Exception {
		for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {

			if (!gradeDisciplinaVO.getListaHistoricoImpactoAlteracaoVOs().isEmpty()) {
				GradeDisciplinaVO gradeDisciplinaAntigaVO = periodoLetivoAntigoVO.obterGradeDisciplinaCorrespondentePorCodigo(gradeDisciplinaVO.getCodigo());
				String logAlteracao = gradeDisciplinaVO.validarAlteracaoMatrizCurricularAtivaInativa(gradeDisciplinaAntigaVO);
				if (!logAlteracao.isEmpty()) {
					//CORRIGI OS IMPACTOS DAS ALTERAÇÕES NA GRADE DISCIPLINA
					getFacadeFactory().getGradeDisciplinaFacade().realizarCorrecaoImpactosAlteracaoGradeDisciplina(gradeDisciplinaVO, gradeDisciplinaVO.getListaHistoricoImpactoAlteracaoVOs(), TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA, usuarioVO, gradeCurricularVO);
//					CRIA LOG DA ALTERAÇÃO
					getFacadeFactory().getLogGradeCurricularInterfaceFacade().realizarCriacaoLogMatrizCurricularAlteracaoGradeDisciplina(gradeCurricularVO, periodoLetivoVO, gradeDisciplinaVO, logAlteracao, usuarioVO);
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarcorrecaoImpactoInclusaoNovaGradeDisciplina(PeriodoLetivoVO periodoLetivoAntigoVO, PeriodoLetivoVO periodoLetivoVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception {
		List<GradeDisciplinaVO> listaInclusaoGradeDisciplinaAntigaVOs = periodoLetivoVO.obterInclusaoGradeDisciplina(periodoLetivoAntigoVO);
		if (Uteis.isAtributoPreenchido(listaInclusaoGradeDisciplinaAntigaVOs)) {
			for (GradeDisciplinaVO gradeDisciplinaVO : listaInclusaoGradeDisciplinaAntigaVOs) {
				String logInclusao = gradeDisciplinaVO.getDescricaoCodigoNomeDisciplina();
				if (!logInclusao.isEmpty()) {
					String alteracoes = periodoLetivoVO.getDescricao() + " - Inclusão de disciplina: " + logInclusao;
					LogGradeCurricularVO obj = new LogGradeCurricularVO();
					getFacadeFactory().getLogImpactoMatrizCurricularFacade().inicializarDadosJsonImpactos(gradeDisciplinaVO.getListaLogImpactoGradeDisciplinaVOs());	
					obj.montarDadosPorMatricula(alteracoes, usuarioVO, TipoAlteracaoMatrizCurricularEnum.ADICIONAR_GRADE_DISCIPLINA, gradeCurricularVO.getCodigo(), periodoLetivoVO.getCodigo(), gradeDisciplinaVO.getCodigo());
					getFacadeFactory().getLogGradeCurricularInterfaceFacade().incluir(obj, usuarioVO);
				}
			}
			
		}
	}
	
/*	@Override
	public GradeCurricularVO consultarGradeCurricularDataAtivacaoAtualPorSituacaoGradeCurso(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GradeCurricular WHERE curso = " + valorConsulta.intValue() + " and situacao = '" + situacao + "' ORDER BY dataativacao desc  limit 1 ";
		select gradecurricular.codigo from gradecurricular 
		inner join periodoletivo p on p.gradecurricular = gradecurricular.codigo
		where p.periodoletivo = 4 and curso = 8
		and situacao = 'AT' 
		and exists (select turma.codigo from turma where turma.gradecurricular = gradecurricular.codigo 
		and turma.unidadeensino = 364 and turma.turno = 3 and turma.curso = 8 and turma.situacao =  'AB'
		and turma.periodoletivo = p.codigo)
		order by dataativacao  desc limit 1
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new GradeCurricularVO();
		}
		// unidade ensino 
		// turno 
		// numero period letivo 
		
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}*/

	@Override
	public GradeCurricularVO consultarGradeCurricularDataAtivacaoAtualPorSituacaoGradeCurso(Integer unidadeensino,	Integer curso, Integer turno, Integer periodoLetivo, String situacao, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);			
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from gradecurricular ");
		sql.append(" inner join periodoletivo p on p.gradecurricular = gradecurricular.codigo ");
		sql.append(" WHERE 1=1 ");
		if(Uteis.isAtributoPreenchido(periodoLetivo)) {
			sql.append(" and p.periodoLetivo = ").append(periodoLetivo.intValue()).append(") ");
		}		
		sql.append(" and (curso = ").append(curso.intValue()).append(") ");
		sql.append(" and (situacao =").append("'" + situacao + "'").append(") ");
		sql.append(" and exists (select turma.codigo from turma where turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" and turma.unidadeensino = ").append(unidadeensino.intValue());
		sql.append(" and turma.turno = ").append(turno.intValue());
		sql.append(" and turma.curso = ").append(curso.intValue());
		sql.append(" and turma.situacao =  'AB'  ");		
		sql.append(" and turma.periodoletivo = p.codigo ) ");
		sql.append(" order by gradecurricular.codigo  desc limit 1 ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return new GradeCurricularVO();
		}	
		
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	
	}
	
	private void montarDadosTotalCargaHorariaDisciplinasObrigatorias(GradeCurricularVO obj) {
		if (Uteis.isAtributoPreenchido(obj)) {
			Integer totalCargaHorariaDisciplinasObrigatorias = 0;
			for (PeriodoLetivoVO periodoLetivoVO : obj.getPeriodoLetivosVOs()) {
				for(GradeDisciplinaVO gradeDisciplinaVO: periodoLetivoVO.getGradeDisciplinaVOs()) {
					if(!gradeDisciplinaVO.getIsDisciplinaOptativa()) {
						totalCargaHorariaDisciplinasObrigatorias += gradeDisciplinaVO.getCargaHoraria();			
					}
				}
			}
			obj.setTotalCargaHorariaDisciplinasObrigatorias(totalCargaHorariaDisciplinasObrigatorias);
		}
	}

	@Override
	public String consultarPrazoJubilamento(String matricula) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append("     matricula, ");
		sb.append("     anoSemestreLimiteConclusao ");
		sb.append(" FROM ");
		sb.append("     ( ");
		sb.append("     SELECT ");
		sb.append("         matricula.matricula, ");
		sb.append("         matriculaperiodo.ano, ");
		sb.append("         matriculaperiodo.semestre, ");
		sb.append("         (CASE ");
		sb.append("             WHEN count(DISTINCT matriculaperiodoTrancado.codigo) > 2 THEN 2 ");
		sb.append("             ELSE count(DISTINCT matriculaperiodoTrancado.codigo) ");
		sb.append("         END) AS periodoletivo, ");
		sb.append("         ultimoperiodo.periodoletivo ultimoPeriodo, ");
		sb.append("         maximoPeriodoLetivo, ");
		sb.append("         gradecurricular.qtdeanosemestreparaintegralizacaocurso, ");
		sb.append("         somasemestre(matriculaperiodo.ano, matriculaperiodo.semestre,  ");
		sb.append("         ((gradecurricular.qtdeanosemestreparaintegralizacaocurso - ultimoperiodo.periodoletivo)  ");
		sb.append("    + (CASE WHEN count(DISTINCT matriculaperiodoTrancado.codigo) > 2 THEN 2 ELSE count(DISTINCT matriculaperiodoTrancado.codigo) END))) AS anoSemestreLimiteConclusao ");
		sb.append(" FROM ");
		sb.append("     matricula ");
		sb.append(" INNER JOIN gradecurricular ON ");
		sb.append("     gradecurricular.codigo = matricula.gradecurricularatual ");
		sb.append(" INNER JOIN curso ON ");
		sb.append("     curso.codigo = matricula.curso ");
		sb.append(" LEFT JOIN LATERAL ( ");
		sb.append("     SELECT ");
		sb.append("         periodoletivo.codigo, ");
		sb.append("         periodoletivo.periodoletivo ");
		sb.append("     FROM ");
		sb.append("         periodoletivo ");
		sb.append("     INNER JOIN matriculaperiodo ON ");
		sb.append("         matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sb.append("     WHERE ");
		sb.append("         matriculaperiodo.matricula = matricula.matricula             ");
		sb.append("         AND periodoletivo.gradecurricular = matricula.gradecurricularatual ");
		sb.append("     ORDER by matriculaperiodo.ano desc, matriculaperiodo.semestre desc,            matriculaperiodo.codigo DESC ");
		sb.append("     LIMIT 1) AS ultimoperiodo ON ");
		sb.append("     1 = 1 ");
		sb.append(" INNER JOIN LATERAL( SELECT mp.codigo FROM matriculaperiodo mp WHERE mp.periodoletivomatricula = ultimoperiodo.codigo      ");
		sb.append(" AND mp.matricula = matricula.matricula ORDER BY (COALESCE(mp.ano, '0')|| COALESCE(mp.semestre, '0')) LIMIT 1 ) AS primeiroMPUltimoPeriodo ON ");
		sb.append("     1 = 1 ");
		sb.append(" INNER JOIN matriculaperiodo ON ");
		sb.append("     matriculaperiodo.codigo = primeiroMPUltimoPeriodo.codigo ");
		sb.append(" LEFT JOIN LATERAL( SELECT mp.codigo FROM matriculaperiodo mp WHERE mp.matricula = matricula.matricula  ");
		sb.append(" AND gradecurricular.considerarperiodotrancadoparajubilamento = TRUE AND mp.situacaomatriculaperiodo = 'TR' ) AS matriculaperiodoTrancado ON ");
		sb.append("     1 = 1 ");
		sb.append(" LEFT JOIN LATERAL ( ");
		sb.append("     SELECT ");
		sb.append("         max(periodoletivo) AS maximoPeriodoLetivo ");
		sb.append("     FROM ");
		sb.append("         periodoletivo ");
		sb.append("     WHERE ");
		sb.append("         gradecurricular = matricula.gradecurricularatual) as periodoLetivoMaior ON ");
		sb.append("     1 = 1 ");
		sb.append(" WHERE ");
		sb.append("     matricula.situacao IN ('AT', 'TR', 'AC', 'FI', 'FO', 'PR') ");
		sb.append("     AND matriculaperiodo.situacaomatriculaperiodo IN ('AT', 'TR' , 'AC', 'PR', 'FI', 'FO') ");
		sb.append("     AND gradecurricular.regracontagemperiodoletivoenum = 'ULTIMO_PERIODO' ");
		sb.append("     AND gradecurricular.qtdeanosemestreparaintegralizacaocurso > 0 ");
		sb.append("     AND curso.periodicidade = 'SE' ");
		sb.append("     AND matricula.matricula like ('").append(matricula).append("') ");
		sb.append(" GROUP BY ");
		sb.append("     matricula.matricula, ");
		sb.append("     matriculaperiodo.ano, ");
		sb.append("     matriculaperiodo.semestre, ");
		sb.append("     ultimoperiodo.periodoletivo, ");
		sb.append("     maximoperiodoletivo, ");
		sb.append("     gradecurricular.qtdeanosemestreparaintegralizacaocurso) AS tup ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT ");
		sb.append(" 	matricula, ");
		sb.append(" 	anoSemestreLimiteConclusao ");
		sb.append(" FROM ");
		sb.append(" 	( ");
		sb.append(" 	SELECT ");
		sb.append(" 		matricula.matricula, ");
		sb.append(" 		matricula.anoingresso, ");
		sb.append(" 		matricula.semestreingresso, ");
		sb.append(" 		gradecurricular.qtdeanosemestreparaintegralizacaocurso, ");
		sb.append(" 		(CASE ");
		sb.append(" 			WHEN count(DISTINCT matriculaperiodo.codigo) >2 THEN 2 ");
		sb.append(" 			ELSE count(DISTINCT matriculaperiodo.codigo) ");
		sb.append(" 		END) AS qtdeMatriculaPeriodoTrancado, ");
		sb.append(" 		somasemestre(matricula.anoingresso, matricula.semestreingresso, (gradecurricular.qtdeanosemestreparaintegralizacaocurso + (CASE WHEN count(DISTINCT matriculaperiodo.codigo) >2 THEN 2 ELSE count(DISTINCT matriculaperiodo.codigo) END)) - 1) AS anoSemestreLimiteConclusao ");
		sb.append(" 	FROM ");
		sb.append(" 		matricula ");
		sb.append(" 	INNER JOIN gradecurricular ON ");
		sb.append(" 		gradecurricular.codigo = matricula.gradecurricularatual ");
		sb.append(" 	INNER JOIN curso ON ");
		sb.append(" 		curso.codigo = matricula.curso ");
		sb.append(" 	LEFT JOIN matriculaperiodo ON ");
		sb.append(" 		matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" 		AND (gradecurricular.considerarperiodotrancadoparajubilamento = TRUE ");
		sb.append(" 			AND matriculaperiodo.situacaomatriculaperiodo = 'TR') ");
		sb.append(" 	WHERE ");
		sb.append(" 		matricula.situacao IN ('AT', 'TR', 'AC', 'FI', 'FO', 'PR') ");
		sb.append(" 			AND gradecurricular.regracontagemperiodoletivoenum = 'TODOS_PERIODO_CURSADO' ");
		sb.append(" 			AND gradecurricular.qtdeanosemestreparaintegralizacaocurso > 0 ");
		sb.append(" 			AND curso.periodicidade = 'SE' ");
		sb.append(" 			AND matricula.anoingresso IS NOT NULL ");
		sb.append(" 			AND matricula.anoingresso != '' ");
		sb.append(" 			AND matricula.semestreingresso IS NOT NULL ");
		sb.append(" 			AND matricula.semestreingresso != '' ");
		sb.append(" 			AND matricula.matricula LIKE ('").append(matricula).append("') ");
		sb.append(" 		GROUP BY ");
		sb.append(" 			matricula.matricula, ");
		sb.append(" 			matricula.anoingresso, ");
		sb.append(" 			matricula.semestreingresso, ");
		sb.append(" 			gradecurricular.qtdeanosemestreparaintegralizacaocurso) AS tpc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("anoSemestreLimiteConclusao");
		}
		return "";
	}

}
