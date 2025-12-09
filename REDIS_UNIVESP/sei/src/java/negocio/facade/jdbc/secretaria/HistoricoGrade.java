package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.HistoricoGradeVO;
import negocio.facade.jdbc.academico.DisciplinaEquivalente;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.HistoricoGradeInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class HistoricoGrade extends ControleAcesso implements HistoricoGradeInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public HistoricoGrade() {

	}

	public void validarDados(HistoricoGradeVO obj) throws Exception {

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HistoricoGradeVO obj, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		final String sql = "INSERT INTO HistoricoGrade( transferenciaMatrizCurricular, disciplina, matriculaPeriodo, situacao, mediaFinal, matriculaPeriodoApresentarHistorico, frequencia, disciplinaEquivalente, disciplinasaproveitadas, transferenciaentradadisciplinaaproveitada, mediaFinalNotaConceito, instituicao, cidade, cargaHorariaAproveitamentoDisciplina, cargahorariacursada, anoHistorico, semestreHistorico, isentarMediaFinal ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getTransferenciaMatrizCurricularVO().getCodigo() != 0) {
					sqlInserir.setInt(1, obj.getTransferenciaMatrizCurricularVO().getCodigo());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getDisciplinaVO().getCodigo() != 0) {
					sqlInserir.setInt(2, obj.getDisciplinaVO().getCodigo());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setInt(3, obj.getMatriculaPeriodoVO().getCodigo());
				sqlInserir.setString(4, obj.getSituacao());
				sqlInserir.setDouble(5, obj.getMediaFinal());
				sqlInserir.setInt(6, obj.getMatriculaPeriodoApresentarHistoricoVO().getCodigo());
				sqlInserir.setDouble(7, obj.getFrequencia());
				if (obj.getDisciplinaEquivalenteVO().getCodigo() != 0) {
					sqlInserir.setInt(8, obj.getDisciplinaEquivalenteVO().getCodigo());
				} else {
					sqlInserir.setNull(8, 0);
				}
				if (obj.getDisciplinasAproveitadas() != 0) {
					sqlInserir.setInt(9, obj.getDisciplinasAproveitadas());
				} else {
					sqlInserir.setNull(9, 0);
				}
				if (obj.getTransferenciaEntradaDisciplinaAproveitada() != 0) {
					sqlInserir.setInt(10, obj.getTransferenciaEntradaDisciplinaAproveitada());
				} else {
					sqlInserir.setNull(10, 0);
				}
				if (obj.getMediaFinalNotaConceito().getCodigo() != 0) {
					sqlInserir.setInt(11, obj.getMediaFinalNotaConceito().getCodigo());
				} else {
					sqlInserir.setNull(11, 0);
				}
				sqlInserir.setString(12, obj.getInstituicao());
				if (obj.getCidade().getCodigo() != 0) {
					sqlInserir.setInt(13, obj.getCidade().getCodigo());
				} else {
					sqlInserir.setNull(13, 0);
				}
				sqlInserir.setInt(14, obj.getCargaHorariaAproveitamentoDisciplina());
				sqlInserir.setInt(15, obj.getCargaHorariaCursada());
				sqlInserir.setString(16, obj.getAnoHistorico());
				sqlInserir.setString(17, obj.getSemestreHistorico());
				sqlInserir.setBoolean(18, obj.getIsentarMediaFinal());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HistoricoGradeVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE HistoricoGrade set transferenciaMatrizCurricular=?, disciplina=?, matriculaPeriodo=?, situacao=?, mediaFinal=?, matriculaPeriodoApresentarHistorico=?, frequencia=?, disciplinaEquivalente=?, disciplinasaproveitadas=?, transferenciaentradadisciplinaaproveitada=?, mediaFinalNotaConceito = ?, instituicao=?, cidade=?, cargaHorariaAproveitamentoDisciplina=?, cargahorariacursada = ?, anoHistorico=?, semestreHistorico=?, isentarMediaFinal=?  WHERE codigo = ?";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getTransferenciaMatrizCurricularVO().getCodigo() != 0) {
					sqlAlterar.setInt(1, obj.getTransferenciaMatrizCurricularVO().getCodigo());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (obj.getDisciplinaVO().getCodigo() != 0) {
					sqlAlterar.setInt(2, obj.getDisciplinaVO().getCodigo());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setInt(3, obj.getMatriculaPeriodoVO().getCodigo().intValue());
				sqlAlterar.setString(4, obj.getSituacao());
				sqlAlterar.setDouble(5, obj.getMediaFinal());
				sqlAlterar.setInt(6, obj.getMatriculaPeriodoApresentarHistoricoVO().getCodigo());
				sqlAlterar.setDouble(7, obj.getFrequencia());
				if (obj.getDisciplinaEquivalenteVO().getCodigo() != 0) {
					sqlAlterar.setInt(8, obj.getDisciplinaEquivalenteVO().getCodigo());
				} else {
					sqlAlterar.setNull(8, 0);
				}
				if (obj.getDisciplinasAproveitadas() != 0) {
					sqlAlterar.setInt(9, obj.getDisciplinasAproveitadas());
				} else {
					sqlAlterar.setNull(9, 0);
				}
				if (obj.getTransferenciaEntradaDisciplinaAproveitada() != 0) {
					sqlAlterar.setInt(10, obj.getTransferenciaEntradaDisciplinaAproveitada());
				} else {
					sqlAlterar.setNull(10, 0);
				}
				if (obj.getMediaFinalNotaConceito().getCodigo() != 0) {
					sqlAlterar.setInt(11, obj.getMediaFinalNotaConceito().getCodigo());
				} else {
					sqlAlterar.setNull(11, 0);
				}
				sqlAlterar.setString(12, obj.getInstituicao());
				if (obj.getCidade().getCodigo() != 0) {
					sqlAlterar.setInt(13, obj.getCidade().getCodigo());
				} else {
					sqlAlterar.setNull(13, 0);
				}
				sqlAlterar.setInt(14, obj.getCargaHorariaAproveitamentoDisciplina());
				sqlAlterar.setInt(15, obj.getCargaHorariaCursada());
				sqlAlterar.setString(16, obj.getAnoHistorico());
				sqlAlterar.setString(17, obj.getSemestreHistorico());
				sqlAlterar.setBoolean(18, obj.getIsentarMediaFinal());
				sqlAlterar.setInt(19, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(HistoricoGradeVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		String sql = "DELETE FROM HistoricoGrade WHERE (codigo = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirHistoricoGrade(Integer transferenciaMatrizCurricular, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			HistoricoGradeVO obj = (HistoricoGradeVO) e.next();
			obj.getTransferenciaMatrizCurricularVO().setCodigo(transferenciaMatrizCurricular);
			incluir(obj, usuario);
		}
	}

	public List<HistoricoGradeVO> consultarGradeOrigemAntigaAlunoPorMatricula(String matricula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select historico.disciplina, mptd.disciplinaequivalente, historico.matriculaperiodo, historico.situacao, mediafinal, historico.freguencia, historico.isentarMediaFinal, ");
		sb.append(" (select codigo from matriculaperiodo mp where mp.matricula = historico.matricula order by mp.ano desc, mp.semestre desc, mp.codigo desc limit 1) AS matriculaPeriodoApresentarHistorico, ");
		sb.append(" historico.disciplinasaproveitadas, historico.transferenciaentradadisciplinasaproveitadas as transferenciaentradadisciplinaaproveitada,  ");

		// Busca a instituicaoEnsino de acordo com a origem do historico
		// (Aproveitamento de Disciplina, Transferencia de Entrada ou a Unidade
		// de Ensino da Matrícula)
		sb.append(" case when (transferenciaentrada.codigo is not null and transferenciaentrada.instituicaoOrigem is not null and transferenciaentrada.instituicaoOrigem != '' ) then transferenciaentrada.instituicaoOrigem else ");
		sb.append(" case when (disciplinasaproveitadas.codigo is not null and disciplinasaproveitadas.instituicao is not null and disciplinasaproveitadas.instituicao != '' ) then disciplinasaproveitadas.instituicao else ");
		sb.append(" case when (historico.instituicao = '' or historico.instituicao is null) then  unidadeEnsino.nome else historico.instituicao end end end as instituicao, ");

		// Busca a cidade de acordo com a origem do historico (Aproveitamento de
		// Disciplina, Transferencia de Entrada ou a Unidade de Ensino da
		// Matrícula)
		sb.append(" case when (transferenciaentrada.codigo is not null and transferenciaentrada.instituicaoOrigem is not null and transferenciaentrada.instituicaoOrigem != '' ) then cidadeTrans.codigo else ");
		sb.append(" case when (disciplinasaproveitadas.codigo is not null and disciplinasaproveitadas.instituicao is not null and disciplinasaproveitadas.instituicao != '' ) then cidadeAp.codigo else ");
		sb.append(" case when (historico.situacao in ('AA', 'CC') and historico.cidade is not null) then cidadeHistorico.codigo else cidade.codigo end end end as cidade, ");

		// Define o ano a ser apresentado conforme a origem do historico
		// (Aproveitamento de Disciplina, Transferencia de Entrada ou a Ano da
		// MatrículaPeriodo)
		sb.append(" case when (transferenciaentradadisciplinasaproveitadas.codigo is not null ) then transferenciaentradadisciplinasaproveitadas.anoconclusaodisciplina else ");
		sb.append(" case when (disciplinasaproveitadas.codigo is not null) then disciplinasaproveitadas.ano else ");
		sb.append(" case when historico.anohistorico != null and historico.anohistorico != '' then historico.anohistorico else matriculaperiodo.ano end end end as anohistorico, ");
		// Define o semestre a ser apresentado conforme a origem do historico
		// (Aproveitamento de Disciplina, Transferencia de Entrada ou a semestre
		// da MatrículaPeriodo)
		sb.append(" case when (transferenciaentradadisciplinasaproveitadas.codigo is not null ) then transferenciaentradadisciplinasaproveitadas.semestreconclusaodisciplina else ");
		sb.append(" case when (disciplinasaproveitadas.codigo is not null) then disciplinasaproveitadas.semestre else ");
		sb.append(" case when historico.semestrehistorico != null and historico.semestrehistorico != '' then historico.semestrehistorico else matriculaperiodo.semestre end end end as semestrehistorico, ");

		/**
		 * Define a origem da carga horaria (Disciplinas Aproveitadas,
		 * Transferencia de Entrada ou Historico ou Disciplina)
		 */
		sb.append(" case when (transferenciaentradadisciplinasaproveitadas.codigo is not null ) then transferenciaentradadisciplinasaproveitadas.cargahoraria::INT else ");
		sb.append(" case when (disciplinasaproveitadas.codigo is not null) then disciplinasaproveitadas.cargahoraria else ");
		sb.append(" case when (historico.situacao in ('AA', 'CC') and historico.cargaHorariaAproveitamentoDisciplina is not null and historico.cargaHorariaAproveitamentoDisciplina > 0) then historico.cargaHorariaAproveitamentoDisciplina else gradedisciplina.cargahoraria end end end as cargaHorariaAproveitamentoDisciplina, ");

		/**
		 * Define a origem da carga horaria (Disciplinas Aproveitadas,
		 * Transferencia de Entrada ou Historico ou Disciplina)
		 */
		sb.append(" case when (disciplinasaproveitadas.codigo is not null) then disciplinasaproveitadas.cargahorariacursada else ");
		sb.append(" case when (historico.cargahorariacursada is not null and historico.cargahorariacursada > 0) then historico.cargahorariacursada else ((gradedisciplina.cargahoraria*historico.freguencia)/100)::INT end end as cargaHorariaCursada, ");

		sb.append(" configuracaoacademiconotaconceito.codigo as mediafinalnotaconceito");
		sb.append(" from historico ");
		sb.append(" left join matriculaperiodoturmadisciplina mptd on mptd.codigo = historico.matriculaperiodoturmadisciplina ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sb.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeensino ");
		sb.append(" left join cidade on unidadeEnsino.cidade = cidade.codigo ");
		sb.append(" left join disciplinasaproveitadas on (disciplinasaproveitadas.codigo = historico.disciplinasaproveitadas) ");
		sb.append(" left join transferenciaentradadisciplinasaproveitadas on (transferenciaentradadisciplinasaproveitadas.codigo = historico.transferenciaentradadisciplinasaproveitadas) ");
		sb.append(" left join transferenciaentrada on (transferenciaentrada.codigo = transferenciaentradadisciplinasaproveitadas.transferenciaentrada) ");
		sb.append(" left join configuracaoacademiconotaconceito on (configuracaoacademiconotaconceito.codigo = historico.mediafinalconceito) ");
		sb.append(" left join cidade as cidadeAp on disciplinasaproveitadas.cidade = cidadeAp.codigo ");
		sb.append(" left join cidade as cidadeTrans on transferenciaentrada.cidade = cidadeTrans.codigo ");
		sb.append(" left join cidade as cidadeHistorico on historico.cidade = cidadeHistorico.codigo ");

		sb.append(" where historico.matricula = '").append(matricula).append("' ");
		sb.append(" order by matriculaperiodo.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, true, usuarioVO);
	}

	public List<HistoricoGradeVO> montarDadosConsulta(SqlRowSet tabelaResultado, Boolean usarFrequenciaHistorico, UsuarioVO usuarioVO) {
		List<HistoricoGradeVO> vetResultado = new ArrayList<HistoricoGradeVO>(0);
		while (tabelaResultado.next()) {
			HistoricoGradeVO obj = new HistoricoGradeVO();
			montarDados(obj, usarFrequenciaHistorico, tabelaResultado, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void montarDados(HistoricoGradeVO obj, Boolean usarFrequenciaHistorico, SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo"));
		obj.getMatriculaPeriodoApresentarHistoricoVO().setCodigo(dadosSQL.getInt("matriculaPeriodoApresentarHistorico"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setAnoHistorico(dadosSQL.getString("anoHistorico"));
		obj.setSemestreHistorico(dadosSQL.getString("semestreHistorico"));
		obj.setInstituicao(dadosSQL.getString("instituicao"));
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		obj.setCargaHorariaAproveitamentoDisciplina(dadosSQL.getInt("cargaHorariaAproveitamentoDisciplina"));
		obj.setCargaHorariaCursada(dadosSQL.getInt("cargaHorariaCursada"));
		obj.setMediaFinal(dadosSQL.getDouble("mediaFinal"));
		obj.getDisciplinaEquivalenteVO().setCodigo(dadosSQL.getInt("disciplinaEquivalente"));
		obj.setDisciplinasAproveitadas(dadosSQL.getInt("disciplinasAproveitadas"));
		obj.setTransferenciaEntradaDisciplinaAproveitada(dadosSQL.getInt("transferenciaEntradaDisciplinaAproveitada"));
		obj.getMediaFinalNotaConceito().setCodigo(dadosSQL.getInt("mediafinalnotaconceito"));
		obj.setIsentarMediaFinal(dadosSQL.getBoolean("isentarMediaFinal"));

		if (usarFrequenciaHistorico) {
			obj.setFrequencia(dadosSQL.getDouble("freguencia"));
		} else {
			obj.setFrequencia(dadosSQL.getDouble("frequencia"));
		}
	}

	public List<HistoricoGradeVO> consultarPorMatriculaGradeMigrarAlunoDisciplinasIguaisTransferidasGrade(String matricula, Integer gradeAtual, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select historicograde.* from transferenciamatrizcurricular tr ");
		sb.append(" inner join historicograde on historicograde.transferenciamatrizcurricular = tr.codigo ");
		sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = tr.grademigrar ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sb.append(" where matricula = '").append(matricula).append("' and tr.grademigrar = ").append(gradeAtual);
		sb.append(" and (gradedisciplina.disciplina = historicograde.disciplina or gradedisciplina.disciplina = historicograde.disciplinaequivalente)   ");
		sb.append(" order by disciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, false, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHistoricoGradePorHistoricoGradeAnteriorAlterada(final HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE HistoricoGrade set situacao=?, mediaFinal=?, frequencia=?, anoHistorico=?, semestreHistorico=?, instituicao=?, cidade=?, cargaHorariaCursada=?, matriculaPeriodoApresentarhistorico=?, isentarMediaFinal=? WHERE codigo = ? ";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, obj.getSituacao());
					sqlAlterar.setDouble(++i, obj.getMediaFinal());
					sqlAlterar.setDouble(++i, obj.getFrequencia());
					sqlAlterar.setString(++i, obj.getAno());
					sqlAlterar.setString(++i, obj.getSemestre());
					sqlAlterar.setString(++i, obj.getInstituicao());
					sqlAlterar.setInt(++i, obj.getCidadeVO().getCodigo().intValue());
					sqlAlterar.setInt(++i, obj.getCargaHorariaCursada());
					sqlAlterar.setInt(++i, obj.getMatriculaPeriodoVO().getCodigo());
					sqlAlterar.setBoolean(++i, obj.getIsentarMediaFinal());
					sqlAlterar.setInt(++i, obj.getHistoricoGradeVO().getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM HistoricoGrade WHERE matriculaPeriodo = ").append(matriculaPeriodo);
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}

	public HistoricoVO carregarDadosInclusaoHistorico(HistoricoGradeVO obj) throws Exception {
		HistoricoVO historicoVO = new HistoricoVO();
		historicoVO.setDisciplina(obj.getDisciplinaVO());
		historicoVO.setMatriculaPeriodo(obj.getMatriculaPeriodoVO());
		historicoVO.setSituacao(obj.getSituacao());
		historicoVO.setAnoHistorico(obj.getAnoHistorico());
		historicoVO.setSemestreHistorico(obj.getSemestreHistorico());
		historicoVO.setInstituicao(obj.getInstituicao());
		historicoVO.setCidadeVO(obj.getCidade());
		historicoVO.setCargaHorariaAproveitamentoDisciplina(obj.getCargaHorariaAproveitamentoDisciplina());
		historicoVO.setCargaHorariaCursada(obj.getCargaHorariaCursada());
		historicoVO.setMediaFinal(obj.getMediaFinal());
		// obj.getDisciplinaEquivalenteVO().setCodigo(dadosSQL.getInt("disciplinaEquivalente"));
		historicoVO.setDisciplinasAproveitadas(obj.getDisciplinasAproveitadas());
		historicoVO.setTransferenciaEntradaDisciplinasAproveitadas(obj.getTransferenciaEntradaDisciplinaAproveitada());
		historicoVO.setMediaFinalConceito(obj.getMediaFinalNotaConceito());
		historicoVO.setIsentarMediaFinal(obj.getIsentarMediaFinal());
		historicoVO.setFreguencia(obj.getFrequencia());
		return historicoVO;
	}

}
