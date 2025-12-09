package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CalendarioLancamentoNotaInterfaceFacade;

@Repository
@Lazy
public class CalendarioLancamentoNota extends ControleAcesso implements CalendarioLancamentoNotaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7006155065140841092L;
	protected static String idEntidade;

	public CalendarioLancamentoNota() throws Exception {
		super();
		setIdEntidade("CalendarioLancamentoNota");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, Boolean validarAcesso, UsuarioVO usuarioVO, Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) throws Exception {
		validarDados(calendarioLancamentoNotaVO);
		validarUnicidade(calendarioLancamentoNotaVO, usuarioVO);
		if (calendarioLancamentoNotaVO.getNovoObj()) {
			incluir(calendarioLancamentoNotaVO, validarAcesso, usuarioVO, atualizarCalendarioAtividadeMatriculaComPeriodo);
		} else {
			alterar(calendarioLancamentoNotaVO, validarAcesso, usuarioVO, atualizarCalendarioAtividadeMatriculaComPeriodo);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM CalendarioLancamentoNota where codigo = " + calendarioLancamentoNotaVO.getCodigo() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}

	private void validarUnicidade(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioLancamentoNotaVO obj = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorConfiguracaoAcademicoAnoSemestre(calendarioLancamentoNotaVO.getCodigo(), calendarioLancamentoNotaVO.getUnidadeEnsino().getCodigo(), calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCodigo(), calendarioLancamentoNotaVO.getTurma().getCodigo(), calendarioLancamentoNotaVO.getProfessor().getCodigo(), calendarioLancamentoNotaVO.getDisciplina().getCodigo(), calendarioLancamentoNotaVO.getConfiguracaoAcademico().getCodigo(), calendarioLancamentoNotaVO.getAno(), calendarioLancamentoNotaVO.getSemestre(), false, usuarioVO);
		if (obj != null && obj.getCodigo() > 0 && !obj.getCodigo().equals(calendarioLancamentoNotaVO.getCodigo())) {
			throw new Exception("Já existe um CALENDÁRIO DE LANÇAMENTO DE NOTAS já cadastrado com estas configurações, favor consultar e alterar o mesmo.");
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, Boolean validarAcesso, UsuarioVO usuarioVO, Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) throws Exception {
		incluir(idEntidade, validarAcesso, usuarioVO);
		final StringBuilder sql = new StringBuilder(" INSERT INTO calendariolancamentonota(configuracaoacademico, ano, semestre, datainicionota1,  ");
		sql.append(" dataterminonota1, datainicionota2, dataterminonota2, datainicionota3,  ");
		sql.append(" dataterminonota3, datainicionota4, dataterminonota4, datainicionota5, ");
		sql.append(" dataterminonota5, datainicionota6, dataterminonota6, datainicionota7, ");
		sql.append(" dataterminonota7, datainicionota8, dataterminonota8, datainicionota9, ");
		sql.append(" dataterminonota9, datainicionota10, dataterminonota10, datainicionota11,  ");
		sql.append(" dataterminonota11, datainicionota12, dataterminonota12, datainicionota13, ");
		sql.append(" dataterminonota13, unidadeensino, unidadeensinocurso, turma, disciplina, calendarioPor, periodicidade, ");
		sql.append(" professor, datainiciocalculomediafinal, dataterminocalculomediafinal,  ");
		sql.append(" datainicionota14, datainicionota15, datainicionota16, datainicionota17,  ");
		sql.append(" datainicionota18, datainicionota19, datainicionota20, datainicionota21,  ");
		sql.append(" datainicionota22, datainicionota23, datainicionota24, datainicionota25, ");
		sql.append(" datainicionota26, datainicionota27, datainicionota28, datainicionota29, ");
		sql.append(" datainicionota30, dataterminonota14, dataterminonota15, dataterminonota16, ");
		sql.append(" dataterminonota17, dataterminonota18, dataterminonota19, dataterminonota20,  ");
		sql.append(" dataterminonota21, dataterminonota22, dataterminonota23, dataterminonota24, ");
		sql.append(" dataterminonota25, dataterminonota26, dataterminonota27, dataterminonota28, ");
		sql.append(" dataterminonota29, dataterminonota30, dataliberacaoalunonota1, ");
		sql.append(" dataliberacaoalunonota2, dataliberacaoalunonota3, dataliberacaoalunonota4, ");
		sql.append(" dataliberacaoalunonota5, dataliberacaoalunonota6, dataliberacaoalunonota7, ");
		sql.append(" dataliberacaoalunonota8, dataliberacaoalunonota9, dataliberacaoalunonota10, ");
		sql.append(" dataliberacaoalunonota11, dataliberacaoalunonota12, dataliberacaoalunonota13, ");
		sql.append(" dataliberacaoalunonota14, dataliberacaoalunonota15, dataliberacaoalunonota16, ");
		sql.append(" dataliberacaoalunonota17, dataliberacaoalunonota18, dataliberacaoalunonota19, ");
		sql.append(" dataliberacaoalunonota20, dataliberacaoalunonota21, dataliberacaoalunonota22, ");
		sql.append(" dataliberacaoalunonota23, dataliberacaoalunonota24, dataliberacaoalunonota25, ");
		sql.append(" dataliberacaoalunonota26, dataliberacaoalunonota27, dataliberacaoalunonota28, ");
		sql.append(" dataliberacaoalunonota29, dataliberacaoalunonota30, dataliberacaoalunocalculomediafinal, ");
		
		sql.append(" dataInicioNota31, dataInicioNota32, dataInicioNota33, dataInicioNota34, dataInicioNota35,  ");
		sql.append(" dataInicioNota36, dataInicioNota37, dataInicioNota38, dataInicioNota39, dataInicioNota40,  ");
		sql.append(" dataTerminoNota31, dataTerminoNota32, dataTerminoNota33, dataTerminoNota34, dataTerminoNota35, ");
		sql.append(" dataTerminoNota36, dataTerminoNota37, dataTerminoNota38, dataTerminoNota39, dataTerminoNota40, ");
		sql.append(" dataLiberacaoAlunoNota31, dataLiberacaoAlunoNota32, dataLiberacaoAlunoNota33, dataLiberacaoAlunoNota34, dataLiberacaoAlunoNota35, ");
		sql.append(" dataLiberacaoAlunoNota36, dataLiberacaoAlunoNota37, dataLiberacaoAlunoNota38, dataLiberacaoAlunoNota39, dataLiberacaoAlunoNota40 , atualizarCalendarioAtividadeMatriculaComPeriodo, ");
		sql.append(" calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde, professorExclusivoLancamentoDeNota)");

		sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
		sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
		sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
		sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
		sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
		sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?");
		sql.append(") returning codigo; ");

		calendarioLancamentoNotaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, calendarioLancamentoNotaVO.getConfiguracaoAcademico().getCodigo());
				ps.setString(x++, calendarioLancamentoNotaVO.getAno());
				ps.setString(x++, calendarioLancamentoNotaVO.getSemestre());

				if (calendarioLancamentoNotaVO.getDataInicioNota1() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota1()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota1() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota1()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota2() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota2()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota2() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota2()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota3() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota3()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota3() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota3()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota4() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota4()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota4() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota4()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota5() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota5()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota5() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota5()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota6() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota6()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota6() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota6()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota7() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota7()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota7() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota7()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota8() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota8()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota8() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota8()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota9() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota9()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota9() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota9()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota10() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota10()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota10() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota10()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota11() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota11()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota11() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota11()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota12() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota12()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota12() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota12()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota13() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota13()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota13() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota13()));
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, calendarioLancamentoNotaVO.getUnidadeEnsino().getCodigo());

				if (calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getTurma().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getTurma().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDisciplina().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getDisciplina().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				
				ps.setString(x++, calendarioLancamentoNotaVO.getCalendarioPor());
				ps.setString(x++, calendarioLancamentoNotaVO.getPeriodicidade());

				if (calendarioLancamentoNotaVO.getProfessor().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getProfessor().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota14() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota14()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota15() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota15()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota16() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota16()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota17() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota17()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota18() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota18()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota19() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota19()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota20() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota20()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota21() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota21()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota22() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota22()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota23() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota23()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota24() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota24()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota25() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota25()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota26() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota26()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota27() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota27()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota28() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota28()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota29() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota29()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota30() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota30()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota14() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota14()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota15() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota15()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota16() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota16()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota17() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota17()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota18() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota18()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota19() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota19()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota20() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota20()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota21() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota21()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota22() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota22()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota23() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota23()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota24() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota24()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota25() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota25()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota26() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota26()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota27() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota27()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota28() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota28()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota29() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota29()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota30() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota30()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota1() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota1()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota2() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota2()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota3() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota3()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota4() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota4()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota5() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota5()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota6() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota6()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota7() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota7()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota8() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota8()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota9() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota9()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota10() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota10()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota11() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota11()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota12() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota12()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota13() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota13()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota14() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota14()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota15() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota15()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota16() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota16()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota17() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota17()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota18() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota18()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota19() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota19()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota20() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota20()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota21() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota21()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota22() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota22()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota23() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota23()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota24() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota24()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota25() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota25()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota26() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota26()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota27() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota27()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota28() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota28()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota29() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota29()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota30() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota30()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoCalculoMediaFinal() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoCalculoMediaFinal()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataInicioNota31() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota31()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota32() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota32()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota33() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota33()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota34() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota34()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota35() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota35()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota36() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota36()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota37() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota37()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota38() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota38()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota39() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota39()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota40() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota40()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota31() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota31()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota32() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota32()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota33() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota33()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota34() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota34()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota35() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota35()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota36() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota36()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota37() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota37()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota38() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota38()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota39() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota39()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota40() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota40()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota31() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota31()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota32() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota32()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota33() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota33()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota34() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota34()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota35() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota35()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota36() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota36()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota37() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota37()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota38() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota38()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota39() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota39()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota40() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota40()));
				} else {
					ps.setNull(x++, 0);
				}
			    ps.setBoolean(x++, calendarioLancamentoNotaVO.getAtualizarCalendarioAtividadeMatriculaComPeriodo());
			    
			    if (calendarioLancamentoNotaVO.getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde()));
				} else {
					ps.setNull(x++, 0);
				}
			    
			    if (Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getProfessorExclusivoLancamentoDeNota())) {
			    	ps.setInt(x++, calendarioLancamentoNotaVO.getProfessorExclusivoLancamentoDeNota().getCodigo());
			    } else {
			    	ps.setNull(x++, 0);
			    }

				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
//		getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarEVincularCalendarioAtividadeMatriculaAvaliacaoOnlineSemPeriodoLiberacaoPorDependenciaCalendarioLancamentoNota(calendarioLancamentoNotaVO, usuarioVO, atualizarCalendarioAtividadeMatriculaComPeriodo);
		calendarioLancamentoNotaVO.setNovoObj(Boolean.FALSE);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, Boolean validarAcesso, UsuarioVO usuarioVO, Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) throws Exception {
		alterar(idEntidade, validarAcesso, usuarioVO);
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE calendariolancamentonota SET configuracaoacademico=?, ano=?, semestre=?, datainicionota1=?, ");
		sql.append(" dataterminonota1=?, datainicionota2=?, dataterminonota2=?, datainicionota3=?, ");
		sql.append(" dataterminonota3=?, datainicionota4=?, dataterminonota4=?, datainicionota5=?, ");
		sql.append(" dataterminonota5=?, datainicionota6=?, dataterminonota6=?, datainicionota7=?, ");
		sql.append(" dataterminonota7=?, datainicionota8=?, dataterminonota8=?, datainicionota9=?, ");
		sql.append(" dataterminonota9=?, datainicionota10=?, dataterminonota10=?, ");
		sql.append(" datainicionota11=?, dataterminonota11=?, datainicionota12=?, ");
		sql.append(" dataterminonota12=?, datainicionota13=?, dataterminonota13=?, ");
		sql.append(" unidadeensino=?, unidadeensinocurso=?, turma=?, professor=?, disciplina=?, calendarioPor=?, periodicidade=?, ");
		sql.append(" datainiciocalculomediafinal=?, dataterminocalculomediafinal=?, ");
		sql.append(" datainicionota14=?, datainicionota15=?, datainicionota16=?, datainicionota17=?, ");
		sql.append(" datainicionota18=?, datainicionota19=?, datainicionota20=?, datainicionota21=?, ");
		sql.append(" datainicionota22=?, datainicionota23=?, datainicionota24=?, datainicionota25=?, ");
		sql.append(" datainicionota26=?, datainicionota27=?, datainicionota28=?, datainicionota29=?, ");
		sql.append(" datainicionota30=?, dataterminonota14=?, dataterminonota15=?, ");
		sql.append(" dataterminonota16=?, dataterminonota17=?, dataterminonota18=?, ");
		sql.append(" dataterminonota19=?, dataterminonota20=?, dataterminonota21=?, ");
		sql.append(" dataterminonota22=?, dataterminonota23=?, dataterminonota24=?, ");
		sql.append(" dataterminonota25=?, dataterminonota26=?, dataterminonota27=?, ");
		sql.append(" dataterminonota28=?, dataterminonota29=?, dataterminonota30=?, ");
		sql.append(" dataliberacaoalunonota1=?, dataliberacaoalunonota2=?, dataliberacaoalunonota3=?, ");
		sql.append(" dataliberacaoalunonota4=?, dataliberacaoalunonota5=?, dataliberacaoalunonota6=?, ");
		sql.append(" dataliberacaoalunonota7=?, dataliberacaoalunonota8=?, dataliberacaoalunonota9=?, ");
		sql.append(" dataliberacaoalunonota10=?, dataliberacaoalunonota11=?, dataliberacaoalunonota12=?, ");
		sql.append(" dataliberacaoalunonota13=?, dataliberacaoalunonota14=?, dataliberacaoalunonota15=?, ");
		sql.append(" dataliberacaoalunonota16=?, dataliberacaoalunonota17=?, dataliberacaoalunonota18=?, ");
		sql.append(" dataliberacaoalunonota19=?, dataliberacaoalunonota20=?, dataliberacaoalunonota21=?, ");
		sql.append(" dataliberacaoalunonota22=?, dataliberacaoalunonota23=?, dataliberacaoalunonota24=?, ");
		sql.append(" dataliberacaoalunonota25=?, dataliberacaoalunonota26=?, dataliberacaoalunonota27=?, ");
		sql.append(" dataliberacaoalunonota28=?, dataliberacaoalunonota29=?, dataliberacaoalunonota30=?, ");
		sql.append(" dataliberacaoalunocalculomediafinal=?, ");
		
		sql.append(" dataInicioNota31=?, dataInicioNota32=?, dataInicioNota33=?, dataInicioNota34=?, dataInicioNota35=?,  ");
		sql.append(" dataInicioNota36=?, dataInicioNota37=?, dataInicioNota38=?, dataInicioNota39=?, dataInicioNota40=?,  ");
		sql.append(" dataTerminoNota31=?, dataTerminoNota32=?, dataTerminoNota33=?, dataTerminoNota34=?, dataTerminoNota35=?, ");
		sql.append(" dataTerminoNota36=?, dataTerminoNota37=?, dataTerminoNota38=?, dataTerminoNota39=?, dataTerminoNota40=?, ");
		sql.append(" dataLiberacaoAlunoNota31=?, dataLiberacaoAlunoNota32=?, dataLiberacaoAlunoNota33=?, dataLiberacaoAlunoNota34=?, dataLiberacaoAlunoNota35=?, ");
		sql.append(" dataLiberacaoAlunoNota36=?, dataLiberacaoAlunoNota37=?, dataLiberacaoAlunoNota38=?, dataLiberacaoAlunoNota39=?, dataLiberacaoAlunoNota40=?, ");
		sql.append(" atualizarCalendarioAtividadeMatriculaComPeriodo=?, calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde=?, professorExclusivoLancamentoDeNota=?  ");
		sql.append(" WHERE codigo=?; ");

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, calendarioLancamentoNotaVO.getConfiguracaoAcademico().getCodigo());
				ps.setString(x++, calendarioLancamentoNotaVO.getAno());
				ps.setString(x++, calendarioLancamentoNotaVO.getSemestre());

				if (calendarioLancamentoNotaVO.getDataInicioNota1() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota1()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota1() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota1()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota2() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota2()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota2() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota2()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota3() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota3()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota3() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota3()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota4() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota4()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota4() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota4()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota5() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota5()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota5() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota5()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota6() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota6()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota6() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota6()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota7() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota7()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota7() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota7()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota8() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota8()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota8() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota8()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota9() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota9()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota9() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota9()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota10() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota10()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota10() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota10()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota11() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota11()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota11() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota11()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota12() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota12()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota12() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota12()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota13() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota13()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoNota13() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota13()));
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, calendarioLancamentoNotaVO.getUnidadeEnsino().getCodigo());

				if (calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getTurma().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getTurma().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getProfessor().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getProfessor().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDisciplina().getCodigo() > 0) {
					ps.setInt(x++, calendarioLancamentoNotaVO.getDisciplina().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				
				ps.setString(x++, calendarioLancamentoNotaVO.getCalendarioPor());
				ps.setString(x++, calendarioLancamentoNotaVO.getPeriodicidade());

				if (calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota14() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota14()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota15() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota15()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota16() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota16()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota17() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota17()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota18() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota18()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota19() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota19()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota20() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota20()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota21() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota21()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota22() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota22()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota23() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota23()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota24() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota24()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota25() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota25()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota26() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota26()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota27() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota27()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota28() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota28()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota29() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota29()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioLancamentoNotaVO.getDataInicioNota30() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota30()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota14() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota14()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota15() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota15()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota16() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota16()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota17() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota17()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota18() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota18()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota19() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota19()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota20() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota20()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota21() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota21()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota22() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota22()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota23() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota23()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota24() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota24()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota25() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota25()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota26() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota26()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota27() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota27()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota28() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota28()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota29() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota29()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataTerminoNota30() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota30()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota1() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota1()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota2() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota2()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota3() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota3()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota4() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota4()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota5() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota5()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota6() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota6()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota7() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota7()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota8() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota8()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota9() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota9()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota10() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota10()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota11() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota11()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota12() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota12()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota13() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota13()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota14() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota14()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota15() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota15()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota16() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota16()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota17() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota17()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota18() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota18()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota19() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota19()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota20() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota20()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota21() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota21()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota22() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota22()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota23() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota23()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota24() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota24()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota25() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota25()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota26() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota26()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota27() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota27()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota28() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota28()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota29() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota29()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota30() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota30()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoCalculoMediaFinal() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoCalculoMediaFinal()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataInicioNota31() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota31()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota32() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota32()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota33() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota33()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota34() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota34()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota35() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota35()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota36() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota36()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota37() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota37()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota38() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota38()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota39() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota39()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataInicioNota40() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataInicioNota40()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota31() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota31()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota32() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota32()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota33() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota33()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota34() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota34()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota35() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota35()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota36() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota36()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota37() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota37()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota38() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota38()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota39() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota39()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataTerminoNota40() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataTerminoNota40()));
				} else {
					ps.setNull(x++, 0);
				}
				
				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota31() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota31()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota32() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota32()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota33() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota33()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota34() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota34()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota35() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota35()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota36() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota36()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota37() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota37()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota38() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota38()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota39() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota39()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota40() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getDataLiberacaoAlunoNota40()));
				} else {
					ps.setNull(x++, 0);
				}
			    ps.setBoolean(x++, calendarioLancamentoNotaVO.getAtualizarCalendarioAtividadeMatriculaComPeriodo());
			    if (calendarioLancamentoNotaVO.getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioLancamentoNotaVO.getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde()));
				} else {
					ps.setNull(x++, 0);
				}
			    
			    if (Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getProfessorExclusivoLancamentoDeNota())) {
			    	ps.setInt(x++, calendarioLancamentoNotaVO.getProfessorExclusivoLancamentoDeNota().getCodigo());
			    } else {
			    	ps.setNull(x++, 0);
			    }
				
				ps.setInt(x++, calendarioLancamentoNotaVO.getCodigo());
				return ps;
			}
		});
		calendarioLancamentoNotaVO.setNovoObj(Boolean.FALSE);
//		getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarEVincularCalendarioAtividadeMatriculaAvaliacaoOnlineSemPeriodoLiberacaoPorDependenciaCalendarioLancamentoNota(calendarioLancamentoNotaVO, usuarioVO, calendarioLancamentoNotaVO.getAtualizarCalendarioAtividadeMatriculaComPeriodo());
	}

	@Override
	public void validarDados(CalendarioLancamentoNotaVO obj) throws ConsistirException {
		ConsistirException ex = new ConsistirException();
		if (obj.getUnidadeEnsino().getCodigo() == null || obj.getUnidadeEnsino().getCodigo() == 0) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_unidadeEnsino"));
		}
		if (obj.getConfiguracaoAcademico().getCodigo() == null || obj.getConfiguracaoAcademico().getCodigo() == 0) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_configuracaoAcademico"));
		}
		if (obj.getPeriodicidade().isEmpty()) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_periodicidade"));
		}
		if (obj.getPeriodicidade().equals("AN")) {
			obj.setSemestre("");
		}
		if (obj.getAno() == null || obj.getAno().isEmpty()) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_ano"));
		}
		if(obj.getIsApresentarCampoSemestre() && obj.getSemestre().equals("")) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_semestre"));
		}
		
		if (obj.getCalendarioPor().equals("UE")) {
			obj.setUnidadeEnsinoCurso(new UnidadeEnsinoCursoVO());
			obj.setTurma(new TurmaVO());
			obj.setProfessor(new PessoaVO());
			obj.setDisciplina(new DisciplinaVO());
		} else if (obj.getCalendarioPor().equals("CU")) {
			if (obj.getUnidadeEnsinoCurso().getCodigo() == null || obj.getUnidadeEnsinoCurso().getCodigo() == 0) {
				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_unidadeEnsinoCurso"));
			}
			obj.setTurma(new TurmaVO());
			obj.setProfessor(new PessoaVO());
			obj.setDisciplina(new DisciplinaVO());
		} else if (obj.getCalendarioPor().equals("TU")) {
			if (obj.getTurma().getCodigo() == null || obj.getTurma().getCodigo() == 0) {
				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_turma"));
			}
			obj.setUnidadeEnsinoCurso(new UnidadeEnsinoCursoVO());
		} else if (obj.getCalendarioPor().equals("PR")) {
			if (obj.getProfessor().getCodigo() == null || obj.getProfessor().getCodigo() == 0) {
				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_professor"));
			}
			obj.setUnidadeEnsinoCurso(new UnidadeEnsinoCursoVO());
			obj.setTurma(new TurmaVO());
		}
		
		if (obj.getConfiguracaoAcademico().getNota1TipoLancamento()) {
//			if (obj.getDataInicioNota1() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar1()));
//			}
//			if (obj.getDataTerminoNota1() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar1()));
//			}
		} else {
			obj.setDataInicioNota1(null);
			obj.setDataTerminoNota1(null);
		}
		if (obj.getConfiguracaoAcademico().getNota2TipoLancamento()) {
//			if (obj.getDataInicioNota2() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar2()));
//			}
//			if (obj.getDataTerminoNota2() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar2()));
//			}
		} else {
			obj.setDataInicioNota2(null);
			obj.setDataTerminoNota2(null);
		}
		if (obj.getConfiguracaoAcademico().getNota3TipoLancamento()) {
//			if (obj.getDataInicioNota3() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar3()));
//			}
//			if (obj.getDataTerminoNota3() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar3()));
//			}
		} else {
			obj.setDataInicioNota3(null);
			obj.setDataTerminoNota3(null);
		}
		if (obj.getConfiguracaoAcademico().getNota4TipoLancamento()) {
//			if (obj.getDataInicioNota4() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar4()));
//			}
//			if (obj.getDataTerminoNota4() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar4()));
//			}
		} else {
			obj.setDataInicioNota4(null);
			obj.setDataTerminoNota4(null);
		}
		if (obj.getConfiguracaoAcademico().getNota5TipoLancamento()) {
//			if (obj.getDataInicioNota5() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar5()));
//			}
//			if (obj.getDataTerminoNota5() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar5()));
//			}
		} else {
			obj.setDataInicioNota5(null);
			obj.setDataTerminoNota5(null);
		}
		if (obj.getConfiguracaoAcademico().getNota6TipoLancamento()) {
//			if (obj.getDataInicioNota6() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar6()));
//			}
//			if (obj.getDataTerminoNota6() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar6()));
//			}
		} else {
			obj.setDataInicioNota6(null);
			obj.setDataTerminoNota6(null);
		}
		if (obj.getConfiguracaoAcademico().getNota7TipoLancamento()) {
//			if (obj.getDataInicioNota7() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar7()));
//			}
//			if (obj.getDataTerminoNota7() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar7()));
//			}
		} else {
			obj.setDataInicioNota7(null);
			obj.setDataTerminoNota7(null);
		}
		if (obj.getConfiguracaoAcademico().getNota8TipoLancamento()) {
//			if (obj.getDataInicioNota8() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar8()));
//			}
//			if (obj.getDataTerminoNota8() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar8()));
//			}
		} else {
			obj.setDataInicioNota8(null);
			obj.setDataTerminoNota8(null);
		}
		if (obj.getConfiguracaoAcademico().getNota9TipoLancamento()) {
//			if (obj.getDataInicioNota9() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar9()));
//			}
//			if (obj.getDataTerminoNota9() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar9()));
//			}
		} else {
			obj.setDataInicioNota9(null);
			obj.setDataTerminoNota9(null);
		}
		if (obj.getConfiguracaoAcademico().getNota10TipoLancamento()) {
//			if (obj.getDataInicioNota10() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar10()));
//			}
//			if (obj.getDataTerminoNota10() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar10()));
//			}
		} else {
			obj.setDataInicioNota10(null);
			obj.setDataTerminoNota10(null);
		}
		if (obj.getConfiguracaoAcademico().getNota11TipoLancamento()) {
//			if (obj.getDataInicioNota11() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar11()));
//			}
//			if (obj.getDataTerminoNota11() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar11()));
//			}
		} else {
			obj.setDataInicioNota11(null);
			obj.setDataTerminoNota11(null);
		}
		if (obj.getConfiguracaoAcademico().getNota12TipoLancamento()) {
//			if (obj.getDataInicioNota12() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar12()));
//			}
//			if (obj.getDataTerminoNota12() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar12()));
//			}
		} else {
			obj.setDataInicioNota12(null);
			obj.setDataTerminoNota12(null);
		}
		if (obj.getConfiguracaoAcademico().getNota13TipoLancamento()) {
//			if (obj.getDataInicioNota13() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar13()));
//			}
//			if (obj.getDataTerminoNota13() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar13()));
//			}
		} else {
			obj.setDataInicioNota13(null);
			obj.setDataTerminoNota13(null);
		}
		if (obj.getConfiguracaoAcademico().getNota14TipoLancamento()) {
//			if (obj.getDataInicioNota14() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar14()));
//			}
//			if (obj.getDataTerminoNota14() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar14()));
//			}
		} else {
			obj.setDataInicioNota14(null);
			obj.setDataTerminoNota14(null);
		}
		if (obj.getConfiguracaoAcademico().getNota15TipoLancamento()) {
//			if (obj.getDataInicioNota15() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar15()));
//			}
//			if (obj.getDataTerminoNota15() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar15()));
//			}
		} else {
			obj.setDataInicioNota15(null);
			obj.setDataTerminoNota15(null);
		}
		if (obj.getConfiguracaoAcademico().getNota16TipoLancamento()) {
//			if (obj.getDataInicioNota16() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar16()));
//			}
//			if (obj.getDataTerminoNota16() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar16()));
//			}
		} else {
			obj.setDataInicioNota16(null);
			obj.setDataTerminoNota16(null);
		}
		if (obj.getConfiguracaoAcademico().getNota17TipoLancamento()) {
//			if (obj.getDataInicioNota17() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar17()));
//			}
//			if (obj.getDataTerminoNota17() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar17()));
//			}
		} else {
			obj.setDataInicioNota17(null);
			obj.setDataTerminoNota17(null);
		}
		if (obj.getConfiguracaoAcademico().getNota18TipoLancamento()) {
//			if (obj.getDataInicioNota18() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar18()));
//			}
//			if (obj.getDataTerminoNota18() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar18()));
//			}
		} else {
			obj.setDataInicioNota18(null);
			obj.setDataTerminoNota18(null);
		}
		if (obj.getConfiguracaoAcademico().getNota19TipoLancamento()) {
//			if (obj.getDataInicioNota19() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar19()));
//			}
//			if (obj.getDataTerminoNota19() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar19()));
//			}
		} else {
			obj.setDataInicioNota19(null);
			obj.setDataTerminoNota19(null);
		}
		if (obj.getConfiguracaoAcademico().getNota20TipoLancamento()) {
//			if (obj.getDataInicioNota20() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar20()));
//			}
//			if (obj.getDataTerminoNota20() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar20()));
//			}
		} else {
			obj.setDataInicioNota20(null);
			obj.setDataTerminoNota20(null);
		}
		if (obj.getConfiguracaoAcademico().getNota21TipoLancamento()) {
//			if (obj.getDataInicioNota21() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar21()));
//			}
//			if (obj.getDataTerminoNota21() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar21()));
//			}
		} else {
			obj.setDataInicioNota21(null);
			obj.setDataTerminoNota21(null);
		}
		if (obj.getConfiguracaoAcademico().getNota22TipoLancamento()) {
//			if (obj.getDataInicioNota22() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar22()));
//			}
//			if (obj.getDataTerminoNota22() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar22()));
//			}
		} else {
			obj.setDataInicioNota22(null);
			obj.setDataTerminoNota22(null);
		}
		if (obj.getConfiguracaoAcademico().getNota23TipoLancamento()) {
//			if (obj.getDataInicioNota23() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar23()));
//			}
//			if (obj.getDataTerminoNota23() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar23()));
//			}
		} else {
			obj.setDataInicioNota23(null);
			obj.setDataTerminoNota23(null);
		}
		if (obj.getConfiguracaoAcademico().getNota24TipoLancamento()) {
//			if (obj.getDataInicioNota24() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar24()));
//			}
//			if (obj.getDataTerminoNota24() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar24()));
//			}
		} else {
			obj.setDataInicioNota24(null);
			obj.setDataTerminoNota24(null);
		}
		if (obj.getConfiguracaoAcademico().getNota25TipoLancamento()) {
//			if (obj.getDataInicioNota25() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar25()));
//			}
//			if (obj.getDataTerminoNota25() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar25()));
//			}
		} else {
			obj.setDataInicioNota25(null);
			obj.setDataTerminoNota25(null);
		}
		if (obj.getConfiguracaoAcademico().getNota26TipoLancamento()) {
//			if (obj.getDataInicioNota26() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar26()));
//			}
//			if (obj.getDataTerminoNota26() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar26()));
//			}
		} else {
			obj.setDataInicioNota26(null);
			obj.setDataTerminoNota26(null);
		}
		if (obj.getConfiguracaoAcademico().getNota27TipoLancamento()) {
//			if (obj.getDataInicioNota27() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar27()));
//			}
//			if (obj.getDataTerminoNota27() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar27()));
//			}
		} else {
			obj.setDataInicioNota27(null);
			obj.setDataTerminoNota27(null);
		}
		if (obj.getConfiguracaoAcademico().getNota28TipoLancamento()) {
//			if (obj.getDataInicioNota28() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar28()));
//			}
//			if (obj.getDataTerminoNota28() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar28()));
//			}
		} else {
			obj.setDataInicioNota28(null);
			obj.setDataTerminoNota28(null);
		}
		if (obj.getConfiguracaoAcademico().getNota29TipoLancamento()) {
//			if (obj.getDataInicioNota29() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar29()));
//			}
//			if (obj.getDataTerminoNota29() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar29()));
//			}
		} else {
			obj.setDataInicioNota29(null);
			obj.setDataTerminoNota29(null);
		}
		if (obj.getConfiguracaoAcademico().getNota30TipoLancamento()) {
//			if (obj.getDataInicioNota30() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar30()));
//			}
//			if (obj.getDataTerminoNota30() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar30()));
//			}
		} else {
			obj.setDataInicioNota30(null);
			obj.setDataTerminoNota30(null);
		}

//		if (obj.getDataInicioCalculoMediaFinal() == null) {
//			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataInicioNota").replace("{1}", "Calculo Media Final"));
//		}
//		if (obj.getDataTerminoCalculoMediaFinal() == null) {
//			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataTerminoNota").replace("{1}", "Calculo Media Final"));
//		}

		if (obj.getConfiguracaoAcademico().getUtilizarNota1()) {
//			if (obj.getDataLiberacaoAlunoNota1() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar1()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota1(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota2()) {
//			if (obj.getDataLiberacaoAlunoNota2() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar2()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota2(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota3()) {
//			if (obj.getDataLiberacaoAlunoNota3() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar3()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota3(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota4()) {
//			if (obj.getDataLiberacaoAlunoNota4() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar4()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota4(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota5()) {
//			if (obj.getDataLiberacaoAlunoNota5() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar5()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota5(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota6()) {
//			if (obj.getDataLiberacaoAlunoNota6() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar6()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota6(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota7()) {
//			if (obj.getDataLiberacaoAlunoNota7() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar7()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota7(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota8()) {
//			if (obj.getDataLiberacaoAlunoNota8() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar8()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota8(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota9()) {
//			if (obj.getDataLiberacaoAlunoNota9() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar9()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota9(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota10()) {
//			if (obj.getDataLiberacaoAlunoNota10() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar10()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota10(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota11()) {
//			if (obj.getDataLiberacaoAlunoNota11() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar11()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota11(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota12()) {
//			if (obj.getDataLiberacaoAlunoNota12() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar12()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota12(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota13()) {
//			if (obj.getDataLiberacaoAlunoNota13() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar13()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota13(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota14()) {
//			if (obj.getDataLiberacaoAlunoNota14() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar14()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota14(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota15()) {
//			if (obj.getDataLiberacaoAlunoNota15() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar15()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota15(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota16()) {
//			if (obj.getDataLiberacaoAlunoNota16() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar16()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota16(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota17()) {
//			if (obj.getDataLiberacaoAlunoNota17() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar17()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota17(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota18()) {
//			if (obj.getDataLiberacaoAlunoNota18() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar18()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota18(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota19()) {
//			if (obj.getDataLiberacaoAlunoNota19() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar19()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota19(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota20()) {
//			if (obj.getDataLiberacaoAlunoNota20() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar20()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota20(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota21()) {
//			if (obj.getDataLiberacaoAlunoNota21() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar21()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota21(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota22()) {
//			if (obj.getDataLiberacaoAlunoNota22() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar22()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota22(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota23()) {
//			if (obj.getDataLiberacaoAlunoNota23() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar23()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota23(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota24()) {
//			if (obj.getDataLiberacaoAlunoNota24() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar24()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota24(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota25()) {
//			if (obj.getDataLiberacaoAlunoNota25() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar25()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota25(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota26()) {
//			if (obj.getDataLiberacaoAlunoNota26() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar26()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota26(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota27()) {
//			if (obj.getDataLiberacaoAlunoNota27() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar27()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota27(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota28()) {
//			if (obj.getDataLiberacaoAlunoNota28() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar28()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota28(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota29()) {
//			if (obj.getDataLiberacaoAlunoNota29() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar29()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota29(null);
		}
		if (obj.getConfiguracaoAcademico().getUtilizarNota30()) {
//			if (obj.getDataLiberacaoAlunoNota30() == null) {
//				ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", obj.getConfiguracaoAcademico().getTituloNotaApresentar30()));
//			}
		} else {
			obj.setDataLiberacaoAlunoNota30(null);
		}
		
//		if (obj.getDataLiberacaoAlunoCalculoMediaFinal() == null) {
//			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_dataLiberacaoAluno").replace("{1}", "Calculo Media Final"));
//		}
		
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		} else {
			ex = null;
		}
	}

	public String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT CalendarioLancamentoNota.*, professorexclusivo.codigo \"professorexclusivo.codigo\", professorexclusivo.nome \"professorexclusivo.nome\",  curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.periodicidade as \"curso.periodicidade\", curso2.codigo as \"curso2.codigo\", curso2.nome as \"curso2.nome\", curso2.periodicidade as \"curso2.periodicidade\", turno.nome as \"turno.nome\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sql.append(" turma.anual as \"turma.anual\", turma.semestral as \"turma.semestral\", pessoa.nome as \"pessoa.nome\", unidadeEnsino.nome as \"unidadeEnsino.nome\", disciplina.nome as \"disciplina.nome\", disciplina.disciplinacomposta  ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = CalendarioLancamentoNota.unidadeEnsino ");
		sql.append(" left join unidadeEnsinocurso on unidadeEnsinocurso.codigo = CalendarioLancamentoNota.unidadeEnsinocurso ");
		sql.append(" left join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sql.append(" left join turno on turno.codigo = unidadeEnsinocurso.turno ");
		sql.append(" left join turma on turma.codigo = CalendarioLancamentoNota.turma ");
		sql.append(" left join curso curso2 on curso2.codigo = turma.curso ");		
		sql.append(" left join pessoa on pessoa.codigo = CalendarioLancamentoNota.professor ");
		sql.append(" left join disciplina on disciplina.codigo = CalendarioLancamentoNota.disciplina ");
		sql.append(" left join pessoa as professorexclusivo on professorexclusivo.codigo = CalendarioLancamentoNota.professorExclusivoLancamentoDeNota ");
		return sql.toString();
	}

	@Override
	public CalendarioLancamentoNotaVO consultarPorConfiguracaoAcademicoAnoSemestre(Integer codigoDesconsiderar, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, Integer disciplina, Integer configuracaoAcademica, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_unidadeEnsino"));
		}
		if (!Uteis.isAtributoPreenchido(configuracaoAcademica)) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_configuracaoAcademico"));
		}
		if (!Uteis.isAtributoPreenchido(ano)) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_ano"));
		}
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if(Uteis.isAtributoPreenchido(codigoDesconsiderar)) {
			sql.append(" and CalendarioLancamentoNota.codigo != ").append(codigoDesconsiderar);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoCurso)) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		} else {
			sql.append(" and unidadeEnsinoCurso.codigo is null ");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and turma.codigo = ").append(turma);
		} else {
			sql.append(" and turma.codigo is null ");
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sql.append(" and pessoa.codigo = ").append(professor);
		} else {
			sql.append(" and pessoa.codigo is null ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		} else {
			sql.append(" and disciplina.codigo is null ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return null;
	}

	@Override
	public CalendarioLancamentoNotaVO consultarPorCalendarioAcademicoUtilizar(Integer unidadeEnsino, Integer turma, Boolean turmaAgrupada, Integer professor, Integer disciplina, Integer configuracaoAcademica, String periodicidade, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" where CalendarioLancamentoNota.codigo in ");
		sql.append(" (select codigo from ( ");
		
		// Busca Especifica da Turma e Professor e Disciplina
		sql.append(" SELECT 1 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" inner join turma on turma.codigo = CalendarioLancamentoNota.turma ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and turma = ").append(turma);
		if (professor != null && professor != 0) {
			sql.append(" and professor = ").append(professor);
		} else {
			sql.append(" and professor = 0");
		}
		if (disciplina != null && disciplina != 0) {
			sql.append(" and disciplina = ").append(disciplina);
		} else {
			sql.append(" and disciplina = 0");
		}
		sql.append(" and unidadeEnsinocurso is null ");
		
		// Busca Especifica do Professor e Disciplina
		sql.append(" union all ");
		sql.append(" SELECT 2 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and professor = ").append(professor);
		if (disciplina != null && disciplina != 0) {
			sql.append(" and disciplina = ").append(disciplina);
		} else {
			sql.append(" and disciplina = 0");
		}
		sql.append(" and turma is null ");
		sql.append(" and unidadeEnsinocurso is null ");
		
		// Busca Especifica da Turma e Professor
		sql.append(" union all ");
		sql.append(" SELECT 3 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" inner join turma on turma.codigo = CalendarioLancamentoNota.turma ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and turma = ").append(turma);
		if (professor != null && professor != 0) {
			sql.append(" and professor = ").append(professor);
		} else {
			sql.append(" and professor = 0");
		}
		sql.append(" and unidadeEnsinocurso is null ");
		sql.append(" and disciplina is null ");
		
		// Busca Especifica do Professor
		sql.append(" union all ");
		sql.append(" SELECT 4 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and professor = ").append(professor);
		sql.append(" and disciplina is null ");
		sql.append(" and turma is null ");
		sql.append(" and unidadeEnsinocurso is null ");
		
		// Busca Especifica da Turma e Disciplina
		sql.append(" union all ");
		sql.append(" SELECT 5 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" inner join turma on turma.codigo = CalendarioLancamentoNota.turma ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and turma = ").append(turma);
		if (disciplina != null && disciplina != 0) {
			sql.append(" and disciplina = ").append(disciplina);
		} else {
			sql.append(" and disciplina = 0");
		}
		sql.append(" and unidadeEnsinocurso is null ");
		sql.append(" and professor is null ");
		
		// Busca Especifica da Turma
		sql.append(" union all ");
		sql.append(" SELECT 6 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" inner join turma on turma.codigo = CalendarioLancamentoNota.turma ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and turma = ").append(turma);
		sql.append(" and unidadeEnsinocurso is null ");
		sql.append(" and professor is null ");
		sql.append(" and disciplina is null ");
		
		// Busca Especifica do Curso/Turno
		if (turmaAgrupada == null || !turmaAgrupada) {
			sql.append(" union all ");
			sql.append(" SELECT 7 as ordem, CalendarioLancamentoNota.codigo ");
			sql.append(" FROM CalendarioLancamentoNota  ");
			sql.append(" inner join turma on turma.codigo = ").append(turma);
			sql.append(" inner join unidadeEnsinocurso on unidadeEnsinocurso.codigo = CalendarioLancamentoNota.unidadeEnsinocurso ");
			sql.append(" and turma.curso = unidadeEnsinocurso.curso ");
			sql.append(" and turma.turno = unidadeEnsinocurso.turno ");
			sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
			sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
			sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
			sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
			if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
				sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
			} else {
				sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
			}
			sql.append(" and turma is null ");
			sql.append(" and professor is null ");
			sql.append(" and disciplina is null ");
		}
		
		// Busca Especifica da Unidade Ensino
		sql.append(" union all ");
		sql.append(" SELECT 8 as ordem, CalendarioLancamentoNota.codigo ");
		sql.append(" FROM CalendarioLancamentoNota  ");
		sql.append(" WHERE CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		if (periodicidade.equals("SE") && semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		} else {
			sql.append(" and trim(CalendarioLancamentoNota.semestre) = '' ");
		}
		sql.append(" and unidadeEnsinoCurso is null ");
		sql.append(" and turma is null ");
		sql.append(" and professor is null ");
		sql.append(" and disciplina is null ");
		
		sql.append(" order by ordem limit 1 ) as calendario) ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return null;
	}

	@Override
	public CalendarioLancamentoNotaVO consultarPorCalendarioAcademicoProfessorExcluisoLancamentoNota(Integer unidadeEnsino, Integer turma, Boolean turmaAgrupada, Integer professor, Integer disciplina, Integer configuracaoAcademica, String periodicidade, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		
		sql.append(" WHERE 1 = 1");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and CalendarioLancamentoNota.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademica)) {
			sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		}
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sql.append(" and CalendarioLancamentoNota.periodicidade = '").append(periodicidade).append("' ");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and CalendarioLancamentoNota.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and CalendarioLancamentoNota.disciplina = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sql.append(" and CalendarioLancamentoNota.professorexclusivolancamentodenota = ").append(professor);
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return null;
	}

	@Override
	public List<CalendarioLancamentoNotaVO> consultar(Integer codigoCalendarioLancamentoNota, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, Integer disciplina, Integer configuracaoAcademica, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		
		if(Uteis.isAtributoPreenchido(codigoCalendarioLancamentoNota)) {
			sql.append(" and CalendarioLancamentoNota.codigo = ").append(codigoCalendarioLancamentoNota);
		}
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (configuracaoAcademica != null && configuracaoAcademica > 0) {
			sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		}
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (professor != null && professor > 0) {
			sql.append(" and pessoa.codigo = ").append(professor);
		}
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		sql.append(" order by calendariolancamentonota.codigo desc ");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, usuarioVO);
	}

	@Override
	public Integer consultarTotalRegistro(Integer codigoCalendarioLancamentoNota, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, Integer disciplina, Integer configuracaoAcademica, String ano, String semestre) throws Exception {

		StringBuilder sql = new StringBuilder(" select count(CalendarioLancamentoNota.codigo) as qtde from CalendarioLancamentoNota ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = CalendarioLancamentoNota.unidadeEnsino ");
		sql.append(" left join unidadeEnsinocurso on unidadeEnsinocurso.codigo = CalendarioLancamentoNota.unidadeEnsinocurso ");
		sql.append(" left join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sql.append(" left join turno on turno.codigo = unidadeEnsinocurso.turno ");
		sql.append(" left join turma on turma.codigo = CalendarioLancamentoNota.turma ");
		sql.append(" left join pessoa on pessoa.codigo = CalendarioLancamentoNota.professor ");
		sql.append(" left join disciplina on disciplina.codigo = CalendarioLancamentoNota.disciplina ");
		sql.append(" WHERE 1=1  ");
		
		if(Uteis.isAtributoPreenchido(codigoCalendarioLancamentoNota)) {
			sql.append(" and CalendarioLancamentoNota.codigo = ").append(codigoCalendarioLancamentoNota);
		}		
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (configuracaoAcademica != null && configuracaoAcademica > 0) {
			sql.append(" and CalendarioLancamentoNota.configuracaoAcademico = ").append(configuracaoAcademica);
		}
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CalendarioLancamentoNota.semestre = '").append(semestre).append("' ");
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (professor != null && professor > 0) {
			sql.append(" and pessoa.codigo = ").append(professor);
		}
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	private List<CalendarioLancamentoNotaVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<CalendarioLancamentoNotaVO> calendarioLancamentoNotaVOs = new ArrayList<CalendarioLancamentoNotaVO>(0);
		while (rs.next()) {
			calendarioLancamentoNotaVOs.add(montarDados(rs, usuario));
		}
		return calendarioLancamentoNotaVOs;
	}

	private CalendarioLancamentoNotaVO montarDados(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		CalendarioLancamentoNotaVO obj = new CalendarioLancamentoNotaVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(rs.getInt("configuracaoAcademico"), usuario));
		obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		obj.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		obj.getUnidadeEnsinoCurso().setCodigo(rs.getInt("unidadeEnsinoCurso"));
		if (obj.getUnidadeEnsinoCurso().getCodigo() > 0) {
			obj.getUnidadeEnsinoCurso().getCurso().setCodigo(rs.getInt("curso.codigo"));
			obj.getUnidadeEnsinoCurso().getCurso().setNome(rs.getString("curso.nome"));
			obj.getUnidadeEnsinoCurso().getCurso().setPeriodicidade(rs.getString("curso.periodicidade"));
			obj.getUnidadeEnsinoCurso().getTurno().setNome(rs.getString("turno.nome"));
		}

		obj.getTurma().setCodigo(rs.getInt("turma"));
		if (obj.getTurma().getCodigo() > 0) {
			obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			obj.getUnidadeEnsinoCurso().getCurso().setCodigo(rs.getInt("curso2.codigo"));
			obj.getUnidadeEnsinoCurso().getCurso().setNome(rs.getString("curso2.nome"));
			obj.getUnidadeEnsinoCurso().getCurso().setPeriodicidade(rs.getString("curso2.periodicidade"));
			obj.getTurma().setAnual(rs.getBoolean("turma.anual"));
			obj.getTurma().setSemestral(rs.getBoolean("turma.semestral"));
		}
		obj.getProfessor().setCodigo(rs.getInt("professor"));
		if (obj.getProfessor().getCodigo() > 0) {
			obj.getProfessor().setNome(rs.getString("pessoa.nome"));
		}
		obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
		if (obj.getDisciplina().getCodigo() > 0) {
			obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
			obj.getDisciplina().setDisciplinaComposta(rs.getBoolean("disciplinacomposta"));
		}
		obj.setAno(rs.getString("ano"));
		obj.setSemestre(rs.getString("semestre"));
		obj.setCalendarioPor(rs.getString("calendarioPor"));
		obj.setPeriodicidade(rs.getString("periodicidade"));
		obj.setDataInicioNota1(rs.getDate("dataInicioNota1"));
		obj.setDataInicioNota2(rs.getDate("dataInicioNota2"));
		obj.setDataInicioNota3(rs.getDate("dataInicioNota3"));
		obj.setDataInicioNota4(rs.getDate("dataInicioNota4"));
		obj.setDataInicioNota5(rs.getDate("dataInicioNota5"));
		obj.setDataInicioNota6(rs.getDate("dataInicioNota6"));
		obj.setDataInicioNota7(rs.getDate("dataInicioNota7"));
		obj.setDataInicioNota8(rs.getDate("dataInicioNota8"));
		obj.setDataInicioNota9(rs.getDate("dataInicioNota9"));
		obj.setDataInicioNota10(rs.getDate("dataInicioNota10"));
		obj.setDataInicioNota11(rs.getDate("dataInicioNota11"));
		obj.setDataInicioNota12(rs.getDate("dataInicioNota12"));
		obj.setDataInicioNota13(rs.getDate("dataInicioNota13"));
		obj.setDataInicioNota14(rs.getDate("dataInicioNota14"));
		obj.setDataInicioNota15(rs.getDate("dataInicioNota15"));
		obj.setDataInicioNota16(rs.getDate("dataInicioNota16"));
		obj.setDataInicioNota17(rs.getDate("dataInicioNota17"));
		obj.setDataInicioNota18(rs.getDate("dataInicioNota18"));
		obj.setDataInicioNota19(rs.getDate("dataInicioNota19"));
		obj.setDataInicioNota20(rs.getDate("dataInicioNota20"));
		obj.setDataInicioNota21(rs.getDate("dataInicioNota21"));
		obj.setDataInicioNota22(rs.getDate("dataInicioNota22"));
		obj.setDataInicioNota23(rs.getDate("dataInicioNota23"));
		obj.setDataInicioNota24(rs.getDate("dataInicioNota24"));
		obj.setDataInicioNota25(rs.getDate("dataInicioNota25"));
		obj.setDataInicioNota26(rs.getDate("dataInicioNota26"));
		obj.setDataInicioNota27(rs.getDate("dataInicioNota27"));
		obj.setDataInicioNota28(rs.getDate("dataInicioNota28"));
		obj.setDataInicioNota29(rs.getDate("dataInicioNota29"));
		obj.setDataInicioNota30(rs.getDate("dataInicioNota30"));
		
		obj.setDataInicioNota31(rs.getDate("dataInicioNota31"));
		obj.setDataInicioNota32(rs.getDate("dataInicioNota32"));
		obj.setDataInicioNota33(rs.getDate("dataInicioNota33"));
		obj.setDataInicioNota34(rs.getDate("dataInicioNota34"));
		obj.setDataInicioNota35(rs.getDate("dataInicioNota35"));
		obj.setDataInicioNota36(rs.getDate("dataInicioNota36"));
		obj.setDataInicioNota37(rs.getDate("dataInicioNota37"));
		obj.setDataInicioNota38(rs.getDate("dataInicioNota38"));
		obj.setDataInicioNota39(rs.getDate("dataInicioNota39"));
		obj.setDataInicioNota40(rs.getDate("dataInicioNota40"));
		
		obj.setDataTerminoNota1(rs.getDate("dataTerminoNota1"));
		obj.setDataTerminoNota2(rs.getDate("dataTerminoNota2"));
		obj.setDataTerminoNota3(rs.getDate("dataTerminoNota3"));
		obj.setDataTerminoNota4(rs.getDate("dataTerminoNota4"));
		obj.setDataTerminoNota5(rs.getDate("dataTerminoNota5"));
		obj.setDataTerminoNota6(rs.getDate("dataTerminoNota6"));
		obj.setDataTerminoNota7(rs.getDate("dataTerminoNota7"));
		obj.setDataTerminoNota8(rs.getDate("dataTerminoNota8"));
		obj.setDataTerminoNota9(rs.getDate("dataTerminoNota9"));
		obj.setDataTerminoNota10(rs.getDate("dataTerminoNota10"));
		obj.setDataTerminoNota11(rs.getDate("dataTerminoNota11"));
		obj.setDataTerminoNota12(rs.getDate("dataTerminoNota12"));
		obj.setDataTerminoNota13(rs.getDate("dataTerminoNota13"));
		obj.setDataTerminoNota14(rs.getDate("dataTerminoNota14"));
		obj.setDataTerminoNota15(rs.getDate("dataTerminoNota15"));
		obj.setDataTerminoNota16(rs.getDate("dataTerminoNota16"));
		obj.setDataTerminoNota17(rs.getDate("dataTerminoNota17"));
		obj.setDataTerminoNota18(rs.getDate("dataTerminoNota18"));
		obj.setDataTerminoNota19(rs.getDate("dataTerminoNota19"));
		obj.setDataTerminoNota20(rs.getDate("dataTerminoNota20"));
		obj.setDataTerminoNota21(rs.getDate("dataTerminoNota21"));
		obj.setDataTerminoNota22(rs.getDate("dataTerminoNota22"));
		obj.setDataTerminoNota23(rs.getDate("dataTerminoNota23"));
		obj.setDataTerminoNota24(rs.getDate("dataTerminoNota24"));
		obj.setDataTerminoNota25(rs.getDate("dataTerminoNota25"));
		obj.setDataTerminoNota26(rs.getDate("dataTerminoNota26"));
		obj.setDataTerminoNota27(rs.getDate("dataTerminoNota27"));
		obj.setDataTerminoNota28(rs.getDate("dataTerminoNota28"));
		obj.setDataTerminoNota29(rs.getDate("dataTerminoNota29"));
		obj.setDataTerminoNota30(rs.getDate("dataTerminoNota30"));
		
		obj.setDataTerminoNota31(rs.getDate("dataTerminoNota31"));
		obj.setDataTerminoNota32(rs.getDate("dataTerminoNota32"));
		obj.setDataTerminoNota33(rs.getDate("dataTerminoNota33"));
		obj.setDataTerminoNota34(rs.getDate("dataTerminoNota34"));
		obj.setDataTerminoNota35(rs.getDate("dataTerminoNota35"));
		obj.setDataTerminoNota36(rs.getDate("dataTerminoNota36"));
		obj.setDataTerminoNota37(rs.getDate("dataTerminoNota37"));
		obj.setDataTerminoNota38(rs.getDate("dataTerminoNota38"));
		obj.setDataTerminoNota39(rs.getDate("dataTerminoNota39"));
		obj.setDataTerminoNota40(rs.getDate("dataTerminoNota40"));
		
		obj.setDataLiberacaoAlunoNota1(rs.getDate("dataLiberacaoAlunoNota1"));
		obj.setDataLiberacaoAlunoNota2(rs.getDate("dataLiberacaoAlunoNota2"));
		obj.setDataLiberacaoAlunoNota3(rs.getDate("dataLiberacaoAlunoNota3"));
		obj.setDataLiberacaoAlunoNota4(rs.getDate("dataLiberacaoAlunoNota4"));
		obj.setDataLiberacaoAlunoNota5(rs.getDate("dataLiberacaoAlunoNota5"));
		obj.setDataLiberacaoAlunoNota6(rs.getDate("dataLiberacaoAlunoNota6"));
		obj.setDataLiberacaoAlunoNota7(rs.getDate("dataLiberacaoAlunoNota7"));
		obj.setDataLiberacaoAlunoNota8(rs.getDate("dataLiberacaoAlunoNota8"));
		obj.setDataLiberacaoAlunoNota9(rs.getDate("dataLiberacaoAlunoNota9"));
		obj.setDataLiberacaoAlunoNota10(rs.getDate("dataLiberacaoAlunoNota10"));
		obj.setDataLiberacaoAlunoNota11(rs.getDate("dataLiberacaoAlunoNota11"));
		obj.setDataLiberacaoAlunoNota12(rs.getDate("dataLiberacaoAlunoNota12"));
		obj.setDataLiberacaoAlunoNota13(rs.getDate("dataLiberacaoAlunoNota13"));
		obj.setDataLiberacaoAlunoNota14(rs.getDate("dataLiberacaoAlunoNota14"));
		obj.setDataLiberacaoAlunoNota15(rs.getDate("dataLiberacaoAlunoNota15"));
		obj.setDataLiberacaoAlunoNota16(rs.getDate("dataLiberacaoAlunoNota16"));
		obj.setDataLiberacaoAlunoNota17(rs.getDate("dataLiberacaoAlunoNota17"));
		obj.setDataLiberacaoAlunoNota18(rs.getDate("dataLiberacaoAlunoNota18"));
		obj.setDataLiberacaoAlunoNota19(rs.getDate("dataLiberacaoAlunoNota19"));
		obj.setDataLiberacaoAlunoNota20(rs.getDate("dataLiberacaoAlunoNota20"));
		obj.setDataLiberacaoAlunoNota21(rs.getDate("dataLiberacaoAlunoNota21"));
		obj.setDataLiberacaoAlunoNota22(rs.getDate("dataLiberacaoAlunoNota22"));
		obj.setDataLiberacaoAlunoNota23(rs.getDate("dataLiberacaoAlunoNota23"));
		obj.setDataLiberacaoAlunoNota24(rs.getDate("dataLiberacaoAlunoNota24"));
		obj.setDataLiberacaoAlunoNota25(rs.getDate("dataLiberacaoAlunoNota25"));
		obj.setDataLiberacaoAlunoNota26(rs.getDate("dataLiberacaoAlunoNota26"));
		obj.setDataLiberacaoAlunoNota27(rs.getDate("dataLiberacaoAlunoNota27"));
		obj.setDataLiberacaoAlunoNota28(rs.getDate("dataLiberacaoAlunoNota28"));
		obj.setDataLiberacaoAlunoNota29(rs.getDate("dataLiberacaoAlunoNota29"));
		obj.setDataLiberacaoAlunoNota30(rs.getDate("dataLiberacaoAlunoNota30"));
		
		obj.setDataLiberacaoAlunoNota31(rs.getDate("dataLiberacaoAlunoNota31"));
		obj.setDataLiberacaoAlunoNota32(rs.getDate("dataLiberacaoAlunoNota32"));
		obj.setDataLiberacaoAlunoNota33(rs.getDate("dataLiberacaoAlunoNota33"));
		obj.setDataLiberacaoAlunoNota34(rs.getDate("dataLiberacaoAlunoNota34"));
		obj.setDataLiberacaoAlunoNota35(rs.getDate("dataLiberacaoAlunoNota35"));
		obj.setDataLiberacaoAlunoNota36(rs.getDate("dataLiberacaoAlunoNota36"));
		obj.setDataLiberacaoAlunoNota37(rs.getDate("dataLiberacaoAlunoNota37"));
		obj.setDataLiberacaoAlunoNota38(rs.getDate("dataLiberacaoAlunoNota38"));
		obj.setDataLiberacaoAlunoNota39(rs.getDate("dataLiberacaoAlunoNota39"));
		obj.setDataLiberacaoAlunoNota40(rs.getDate("dataLiberacaoAlunoNota40"));

		obj.setDataInicioCalculoMediaFinal(rs.getDate("dataInicioCalculoMediaFinal"));
		obj.setDataTerminoCalculoMediaFinal(rs.getDate("dataTerminoCalculoMediaFinal"));
		obj.setDataLiberacaoAlunoCalculoMediaFinal(rs.getDate("dataLiberacaoAlunoCalculoMediaFinal"));
		obj.setAtualizarCalendarioAtividadeMatriculaComPeriodo(rs.getBoolean("atualizarCalendarioAtividadeMatriculaComPeriodo"));
		obj.setCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde(rs.getDate("calcularmediaautomaticamentegravarlancamentonotasprofessorparti"));
		obj.getProfessorExclusivoLancamentoDeNota().setCodigo(rs.getInt("professorexclusivo.codigo"));
		obj.getProfessorExclusivoLancamentoDeNota().setNome(rs.getString("professorexclusivo.nome"));
		obj.setNovoObj(false);
		verificarApresentarCampoNotaVisaoAluno(obj);
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CalendarioLancamentoNota.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CalendarioLancamentoNota.idEntidade = idEntidade;
	}

	/**
	 * Método responsável por executar a montagem dos atributos responsáveis por
	 * apresentar as notas na visão do aluno levando em consideração os
	 * parâmetros passados no ato da configuração do Calendário de Lançamento de
	 * Nota.
	 * 
	 * @param calendarioLancamentoNotaVO
	 * @throws Exception
	 */
	@Override
	public void verificarApresentarCampoNotaVisaoAluno(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO) throws Exception {
		for (int x = 1; x <= 40; x++) {
			Date dataLiberacaoAlunoNota = (Date) UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataLiberacaoAlunoNota" + x);
			if (Uteis.isAtributoPreenchido(dataLiberacaoAlunoNota)) {
				UtilReflexao.invocarMetodo(calendarioLancamentoNotaVO, "setApresentarNota" + x + "VisaoAluno", dataLiberacaoAlunoNota.before(new Date()));
			}
		}
		if (Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getDataLiberacaoAlunoCalculoMediaFinal())) {
			calendarioLancamentoNotaVO.setApresentarCalculoMediaFinalVisaoAluno(calendarioLancamentoNotaVO.getDataLiberacaoAlunoCalculoMediaFinal().before(new Date()));
		}
	}

}
