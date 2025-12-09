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

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.recursoshumanos.CompetenciaFolhaPagamento;
import negocio.interfaces.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe
 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see PeriodoLetivoAtivoUnidadeEnsinoCursoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PeriodoLetivoAtivoUnidadeEnsinoCurso extends ControleAcesso implements PeriodoLetivoAtivoUnidadeEnsinoCursoInterfaceFacade {

//	protected static String idEntidade;

	public PeriodoLetivoAtivoUnidadeEnsinoCurso() throws Exception {
		super();
//		setIdEntidade("PeriodoLetivoAtivoUnidadeEnsinoCurso");
	}

	public static void vefirificaFuncionalidade(String funcionalidade) throws Exception {
//		ControleAcesso.verificarPermissaoUsuarioFuncionalidade(funcionalidade);
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>.
	 */
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO novo() throws Exception {
//		PeriodoLetivoAtivoUnidadeEnsinoCurso.incluir(getIdEntidade());
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, final String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
		try {
//			PeriodoLetivoAtivoUnidadeEnsinoCurso.incluir(getIdEntidade(), true, usuario);
			if (obj.getCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
				obj.setSemestreReferenciaPeriodoLetivo("");
				obj.setAnoReferenciaPeriodoLetivo("");
			} else if (obj.getCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())) {
				obj.setSemestreReferenciaPeriodoLetivo("");
			}
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO.validarDados(obj, nivelProcessoMatricula);
				// if (obj.getDataInicioPeriodoLetivo() == null) {
				// throw new
				// Exception("O campo DATA INÍCIO PERÍODO LETIVO (Aba: Calendário Processo de Matrícula Curso) deve ser informado.");
				// }
				// if (obj.getDataFimPeriodoLetivo() == null) {
				// throw new
				// Exception("O campo DATA FIM PERÍODO LETIVO (Aba: Calendário Processo de Matrícula Curso) deve ser informado.");
				// }
			final String sql = "INSERT INTO PeriodoLetivoAtivoUnidadeEnsinoCurso( situacao, curso,  semestreReferenciaPeriodoLetivo, " + "anoReferenciaPeriodoLetivo, "
					+ "tipoPeriodoLetivo, dataInicioPeriodoLetivo, dataFimPeriodoLetivo, " + "dataInicioPeriodoLetivoPrimeiroBimestre, dataFimPeriodoLetivoPrimeiroBimestre,"
							+ " dataInicioPeriodoLetivoSegundoBimestre, dataFimPeriodoLetivoSegundoBimestre, " + "dataInicioPeriodoLetivoTerceiroBimestre, dataFimPeriodoLetivoTerceiroBimestre, "
									+ "dataInicioPeriodoLetivoQuartoBimestre, dataFimPeriodoLetivoQuartoBimestre, " + "qtdeDiaLetivoPrimeiroBimestre, qtdeSemanaLetivaPrimeiroBimestre, "
											+ "qtdeDiaLetivoSegundoBimestre, qtdeSemanaLetivaSegundoBimestre, qtdeDiaLetivoTerceiroBimestre, " + "qtdeSemanaLetivaTerceiroBimestre, "
													+ "qtdeDiaLetivoQuartoBimestre, qtdeSemanaLetivaQuartoBimestre, totalDiaLetivoAno, totalSemanaLetivaAno, "
					+ "dataAbertura, reponsavelAbertura, dataFechamento, responsavelFechamento, turno, processomatricula ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getSituacao());
					sqlInserir.setInt(2, obj.getCursoVO().getCodigo().intValue());
					sqlInserir.setString(3, obj.getSemestreReferenciaPeriodoLetivo());
					sqlInserir.setString(4, obj.getAnoReferenciaPeriodoLetivo());
					if (obj.getCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
						obj.setTipoPeriodoLetivo(obj.getTipoPeriodoLetivo());
						sqlInserir.setString(5, obj.getTipoPeriodoLetivo());
						sqlInserir.setNull(6, 0);
						sqlInserir.setNull(7, 0);
						sqlInserir.setNull(8, 0);
						sqlInserir.setNull(9, 0);
						sqlInserir.setNull(10, 0);
						sqlInserir.setNull(11, 0);
						sqlInserir.setNull(12, 0);
						sqlInserir.setNull(13, 0);
						sqlInserir.setNull(14, 0);
						sqlInserir.setNull(15, 0);
					} else {
						sqlInserir.setString(5, obj.getTipoPeriodoLetivo());
						sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivo()));
						sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivo()));
						sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoPrimeiroBimestre()));
						sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoPrimeiroBimestre()));
						sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoSegundoBimestre()));
						sqlInserir.setDate(11, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoSegundoBimestre()));
						sqlInserir.setDate(12, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoTerceiroBimestre()));
						sqlInserir.setDate(13, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoTerceiroBimestre()));
						sqlInserir.setDate(14, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoQuartoBimestre()));
						sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoQuartoBimestre()));
					}
					sqlInserir.setInt(16, obj.getQtdeDiaLetivoPrimeiroBimestre());
					sqlInserir.setInt(17, obj.getQtdeSemanaLetivaPrimeiroBimestre());
					sqlInserir.setInt(18, obj.getQtdeDiaLetivoSegundoBimestre());
					sqlInserir.setInt(19, obj.getQtdeSemanaLetivaSegundoBimestre());
					sqlInserir.setInt(20, obj.getQtdeDiaLetivoTerceiroBimestre());
					sqlInserir.setInt(21, obj.getQtdeSemanaLetivaTerceiroBimestre());
					sqlInserir.setInt(22, obj.getQtdeDiaLetivoQuartoBimestre());
					sqlInserir.setInt(23, obj.getQtdeSemanaLetivaQuartoBimestre());
					sqlInserir.setInt(24, obj.getTotalDiaLetivoAno());
					sqlInserir.setInt(25, obj.getTotalSemanaLetivaAno());
					sqlInserir.setDate(26, Uteis.getDataJDBC(obj.getDataAbertura()));
					sqlInserir.setInt(27, obj.getReponsavelAbertura().getCodigo().intValue());
					sqlInserir.setDate(28, Uteis.getDataJDBC(obj.getDataFechamento()));
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(29, obj.getResponsavelFechamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(29, 0);
					}
					sqlInserir.setInt(30, obj.getTurnoVO().getCodigo().intValue());
					if (obj.getProcessoMatriculaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(31, obj.getProcessoMatriculaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(31, 0);
					}
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
//			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade().preencherIncluirProcessoMatriculaLog(obj, nivelProcessoMatricula, "inclusão", usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, final String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
		try {
//			PeriodoLetivoAtivoUnidadeEnsinoCurso.alterar(getIdEntidade(), true, usuario);
			if (obj.getCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
				obj.setSemestreReferenciaPeriodoLetivo("");
				obj.setAnoReferenciaPeriodoLetivo("");
			} else if (obj.getCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())) {
				obj.setSemestreReferenciaPeriodoLetivo("");
			}
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO.validarDados(obj, nivelProcessoMatricula);
			final String sql = "UPDATE PeriodoLetivoAtivoUnidadeEnsinoCurso set situacao=?, curso=?, semestreReferenciaPeriodoLetivo=?, " + "anoReferenciaPeriodoLetivo=?, tipoPeriodoLetivo=?, "
					+ "dataInicioPeriodoLetivo=?, dataFimPeriodoLetivo=?, " + "dataInicioPeriodoLetivoPrimeiroBimestre=?, dataFimPeriodoLetivoPrimeiroBimestre=?, dataInicioPeriodoLetivoSegundoBimestre=?, "
					+ "dataFimPeriodoLetivoSegundoBimestre=?, " + "dataInicioPeriodoLetivoTerceiroBimestre=?, dataFimPeriodoLetivoTerceiroBimestre=?, dataInicioPeriodoLetivoQuartoBimestre=?, "
							+ "dataFimPeriodoLetivoQuartoBimestre=?, " + "qtdeDiaLetivoPrimeiroBimestre=?, qtdeSemanaLetivaPrimeiroBimestre=?, qtdeDiaLetivoSegundoBimestre=?, "
									+ "qtdeSemanaLetivaSegundoBimestre=?, qtdeDiaLetivoTerceiroBimestre=?, " + "qtdeSemanaLetivaTerceiroBimestre=?, qtdeDiaLetivoQuartoBimestre=?, "
											+ "qtdeSemanaLetivaQuartoBimestre=?, totalDiaLetivoAno=?, totalSemanaLetivaAno=?, "
					+ "dataAbertura=?, reponsavelAbertura=?, dataFechamento=?, responsavelFechamento=?, turno = ?, processomatricula = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setInt(2, obj.getCursoVO().getCodigo().intValue());
					sqlAlterar.setString(3, obj.getSemestreReferenciaPeriodoLetivo());
					sqlAlterar.setString(4, obj.getAnoReferenciaPeriodoLetivo());
					if (obj.getCursoVO().getPeriodicidade().equals("IN")) {
						obj.setTipoPeriodoLetivo(obj.getTipoPeriodoLetivo());
						sqlAlterar.setString(5, obj.getTipoPeriodoLetivo());
						sqlAlterar.setNull(6, 0);
						sqlAlterar.setNull(7, 0);
						sqlAlterar.setNull(8, 0);
						sqlAlterar.setNull(9, 0);
						sqlAlterar.setNull(10, 0);
						sqlAlterar.setNull(11, 0);
						sqlAlterar.setNull(12, 0);
						sqlAlterar.setNull(13, 0);
						sqlAlterar.setNull(14, 0);
						sqlAlterar.setNull(15, 0);
					} else {
						sqlAlterar.setString(5, obj.getTipoPeriodoLetivo());
						sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivo()));
						sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivo()));
						sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoPrimeiroBimestre()));
						sqlAlterar.setDate(9, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoPrimeiroBimestre()));
						sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoSegundoBimestre()));
						sqlAlterar.setDate(11, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoSegundoBimestre()));
						sqlAlterar.setDate(12, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoTerceiroBimestre()));
						sqlAlterar.setDate(13, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoTerceiroBimestre()));
						sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoQuartoBimestre()));
						sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoQuartoBimestre()));
					}
					sqlAlterar.setInt(16, obj.getQtdeDiaLetivoPrimeiroBimestre());
					sqlAlterar.setInt(17, obj.getQtdeSemanaLetivaPrimeiroBimestre());
					sqlAlterar.setInt(18, obj.getQtdeDiaLetivoSegundoBimestre());
					sqlAlterar.setInt(19, obj.getQtdeSemanaLetivaSegundoBimestre());
					sqlAlterar.setInt(20, obj.getQtdeDiaLetivoTerceiroBimestre());
					sqlAlterar.setInt(21, obj.getQtdeSemanaLetivaTerceiroBimestre());
					sqlAlterar.setInt(22, obj.getQtdeDiaLetivoQuartoBimestre());
					sqlAlterar.setInt(23, obj.getQtdeSemanaLetivaQuartoBimestre());
					sqlAlterar.setInt(24, obj.getTotalDiaLetivoAno());
					sqlAlterar.setInt(25, obj.getTotalSemanaLetivaAno());
					sqlAlterar.setDate(26, Uteis.getDataJDBC(obj.getDataAbertura()));
					sqlAlterar.setInt(27, obj.getReponsavelAbertura().getCodigo().intValue());
					sqlAlterar.setDate(28, Uteis.getDataJDBC(obj.getDataFechamento()));
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(29, obj.getResponsavelFechamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(29, 0);
					}
					sqlAlterar.setInt(30, obj.getTurnoVO().getCodigo().intValue());
					if (obj.getProcessoMatriculaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(31, obj.getProcessoMatriculaVO().getCodigo().intValue());
					} else {
					sqlAlterar.setNull(31, 0);
				}
					sqlAlterar.setInt(32, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade().preencherIncluirProcessoMatriculaLog(obj, nivelProcessoMatricula, "alteração", usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	public void atualizarSituacaoPeriodoLetivoAtivo(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE periodoletivoativounidadeensinocurso SET situacao = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { periodoLetivoAtivoUnidadeEnsinoCursoVO.getSituacao(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo() });
//		getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade().preencherIncluirProcessoMatriculaLog(periodoLetivoAtivoUnidadeEnsinoCursoVO, "", "alteração", usuario);
	}

	public void alterarDataPeriodoLetivoAtivoParaFinalizacao(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, Date dataAlterar, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE periodoletivoativounidadeensinocurso SET datafimperiodoletivo = ?, situacao = 'AT' WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { dataAlterar, periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo() });
		periodoLetivoAtivoUnidadeEnsinoCursoVO.setSituacao("FI");
		periodoLetivoAtivoUnidadeEnsinoCursoVO.setDataFimPeriodoLetivo(new Date());
//		getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade().preencherIncluirProcessoMatriculaLog(periodoLetivoAtivoUnidadeEnsinoCursoVO, "", "alteração", usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarListaFechamento(List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> listaFechamento, String nivelProcessoMatricula, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer codigoCurso, Integer codigoTurno, Date dataFim, Boolean realizarCalculoMediaFinalFechPeriodo, UsuarioVO usuario, String nivelEducacionalApresentar, ProgressBarVO progressBarVO, String periodicidade) throws Exception {
		for (Iterator<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> iterator = listaFechamento.iterator(); iterator.hasNext();) {
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = (PeriodoLetivoAtivoUnidadeEnsinoCursoVO) iterator.next();
			if (obj.getSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar()) {
				progressBarVO.setStatus("Carregando alunos da turma "+obj.getTurma().getIdentificadorTurma());
				try {
					List<MatriculaPeriodoVO> matriculaPeriodoVOs;
					if (obj.isReabrirPeriodoLetivo()) {
						matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorUnidadeEnsinoCursoTurmaPeriodoLetivoAtivoSituacao(codigoCurso, codigoTurno, unidadeEnsinoVOs, obj.getCodigo(), obj.getTurma().getCodigo(), "FI", false, usuario);
						progressBarVO.setStatus("Realizando Reabertura da Matriculas da Turma "+obj.getTurma().getIdentificadorTurma());
						executarReabrirMatriculaPeridoCurso(obj, matriculaPeriodoVOs, realizarCalculoMediaFinalFechPeriodo, usuario);
						executarReabrirProcessoMatriculaSemMatriculaPeriodoAtiva(obj, unidadeEnsinoVOs, codigoCurso, codigoTurno, dataFim, usuario, nivelEducacionalApresentar, periodicidade);
						getFacadeFactory().getHistoricoFacade().realizarAlteracaoHistoricoCursandoPeriodoLetivoDeAcordoConfiguracaoAcademica(matriculaPeriodoVOs, obj.getAnoReferenciaPeriodoLetivo(), obj.getSemestreReferenciaPeriodoLetivo(), usuario);
					} 
					else {
						matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorUnidadeEnsinoCursoTurmaPeriodoLetivoAtivoSituacao(codigoCurso, codigoTurno, unidadeEnsinoVOs, obj.getCodigo(), obj.getTurma().getCodigo(), "AT", false, usuario);
						progressBarVO.setStatus("Realizando Fechamento da Turma "+obj.getTurma().getIdentificadorTurma());
						executarFinalizarMatriculaPeridoCurso(obj, matriculaPeriodoVOs, realizarCalculoMediaFinalFechPeriodo, usuario);
						executarFinalizacaoProcessoMatriculaSemMatriculaPeriodoAtiva(obj, unidadeEnsinoVOs, codigoCurso, codigoTurno, dataFim, usuario, nivelEducacionalApresentar, periodicidade);						
						getFacadeFactory().getHistoricoFacade().realizarAlteracaoHistoricoReprovadoPeriodoLetivoDeAcordoConfiguracaoAcademica(matriculaPeriodoVOs, obj.getAnoReferenciaPeriodoLetivo(), obj.getSemestreReferenciaPeriodoLetivo(), usuario);
					}
					progressBarVO.incrementar();					
					iterator.remove();
					
				} catch (ConsistirException ex) {
					throw ex;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarReabrirMatriculaPeridoCurso(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, List<MatriculaPeriodoVO> matriculaPeriodoVOs, Boolean realizarCalculoMediaFinalFechPeriodo, UsuarioVO usuario) throws Exception {
		for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.ATIVA.getValor());
				getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, OrigemFechamentoMatriculaPeriodoEnum.FECHAMENTO_PERIODO_LETIVO, periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), new Date());
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, usuario);
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().retirarReservaTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarFinalizarMatriculaPeridoCurso(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, List<MatriculaPeriodoVO> matriculaPeriodoVOs, Boolean realizarCalculoMediaFinalFechPeriodo, UsuarioVO usuario) throws Exception {
		for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor());
				getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, OrigemFechamentoMatriculaPeriodoEnum.FECHAMENTO_PERIODO_LETIVO, periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), new Date());
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, usuario);
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().retirarReservaTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs, usuario);
		}
		try {		
			if (realizarCalculoMediaFinalFechPeriodo) {
				executarDefinirAprovadoReprovadoPorDisciplina(periodoLetivoAtivoUnidadeEnsinoCursoVO, matriculaPeriodoVOs, usuario);					
			}
		} catch (ConsistirException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
			// erro ao gravar alterações matricula
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarReabrirProcessoMatriculaSemMatriculaPeriodoAtiva(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer codigoCurso, Integer codigoTurno, Date dataFim, UsuarioVO usuario, String nivelEducacionalApresentar, String periodicidade) throws Exception {
		List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> listaPeriodoLetivoAtivoUnidadeEnsinoCursoVO = consultarPorUnidadeEnsinoCursoSituacaoDataFechamentoSemMatriculaPeriodoAtiva(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getTurma().getCodigo(), unidadeEnsinoVOs, codigoCurso, codigoTurno, dataFim, periodoLetivoAtivoUnidadeEnsinoCursoVO.getAnoReferenciaPeriodoLetivo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getSemestreReferenciaPeriodoLetivo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getSituacao(), nivelEducacionalApresentar,periodicidade);
		if (listaPeriodoLetivoAtivoUnidadeEnsinoCursoVO.isEmpty()) {
				periodoLetivoAtivoUnidadeEnsinoCursoVO.setSituacao(SituacaoMatriculaPeriodoEnum.ATIVA.getValor());
			
			atualizarSituacaoPeriodoLetivoAtivo(periodoLetivoAtivoUnidadeEnsinoCursoVO, usuario);
			if (!getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarSeAindaExisteProcessoMatriculaCalendarioPorPeriodoLetivoAtivoDistintosComMesmoProcessoMatricula(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)) {
				ProcessoMatriculaCalendarioVO processoMatriculaCalendario = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorPeriodoLetivoAtivoUnidadeEnsinoCurso(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (Uteis.isAtributoPreenchido(processoMatriculaCalendario.getProcessoMatricula())) {
					getFacadeFactory().getProcessoMatriculaFacade().alterarSituacaoProcessoMatricula(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(processoMatriculaCalendario.getProcessoMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario), "AT", usuario);
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarFinalizacaoProcessoMatriculaSemMatriculaPeriodoAtiva(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer codigoCurso, Integer codigoTurno, Date dataFim, UsuarioVO usuario, String nivelEducacionalApresentar, String periodicidade) throws Exception {
		List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> listaPeriodoLetivoAtivoUnidadeEnsinoCursoVO = consultarPorUnidadeEnsinoCursoSituacaoDataFechamentoSemMatriculaPeriodoAtiva(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getTurma().getCodigo(), unidadeEnsinoVOs, codigoCurso, codigoTurno, dataFim, periodoLetivoAtivoUnidadeEnsinoCursoVO.getAnoReferenciaPeriodoLetivo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getSemestreReferenciaPeriodoLetivo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getSituacao(), nivelEducacionalApresentar,periodicidade);
		if (listaPeriodoLetivoAtivoUnidadeEnsinoCursoVO.isEmpty()) {
				periodoLetivoAtivoUnidadeEnsinoCursoVO.setSituacao(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor());
			
			atualizarSituacaoPeriodoLetivoAtivo(periodoLetivoAtivoUnidadeEnsinoCursoVO, usuario);
			if (!getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarSeAindaExisteProcessoMatriculaCalendarioPorPeriodoLetivoAtivoDistintosComMesmoProcessoMatricula(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)) {
				ProcessoMatriculaCalendarioVO processoMatriculaCalendario = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorPeriodoLetivoAtivoUnidadeEnsinoCurso(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (Uteis.isAtributoPreenchido(processoMatriculaCalendario.getProcessoMatricula())) {
					getFacadeFactory().getProcessoMatriculaFacade().alterarSituacaoProcessoMatricula(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(processoMatriculaCalendario.getProcessoMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario), "FI", usuario);
				}
			}
		}
	}

	private void executarDefinirAprovadoReprovadoPorDisciplina(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, List<MatriculaPeriodoVO> matriculaPeriodoVOs,  UsuarioVO usuario) throws Exception {
		List<HistoricoVO> lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaPadraoAnoSemestreSituacaoMatSituacaoHist(0, 0, null, periodoLetivoAtivoUnidadeEnsinoCursoVO.getTurma(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getAnoReferenciaPeriodoLetivo(), periodoLetivoAtivoUnidadeEnsinoCursoVO.getSemestreReferenciaPeriodoLetivo(), "", "", true, false, null, matriculaPeriodoVOs, true, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		Map<Integer, List<HistoricoVO>> mapHistoricoPorDisciplina = new HashMap<Integer, List<HistoricoVO>>(0);
		Map<Integer, List<HistoricoVO>> mapHistoricoPorDisciplinaComposta = new HashMap<Integer, List<HistoricoVO>>(0);
		for(HistoricoVO historicoVO: lista){
			if(!historicoVO.getHistoricoDisciplinaComposta()) {
				if(!mapHistoricoPorDisciplina.containsKey(historicoVO.getDisciplina().getCodigo())){
					mapHistoricoPorDisciplina.put(historicoVO.getDisciplina().getCodigo(), new ArrayList<HistoricoVO>(0));
				}
				mapHistoricoPorDisciplina.get(historicoVO.getDisciplina().getCodigo()).add(historicoVO);
			}else {
				if(!mapHistoricoPorDisciplinaComposta.containsKey(historicoVO.getDisciplina().getCodigo())){
					mapHistoricoPorDisciplinaComposta.put(historicoVO.getDisciplina().getCodigo(), new ArrayList<HistoricoVO>(0));
				}
				mapHistoricoPorDisciplinaComposta.get(historicoVO.getDisciplina().getCodigo()).add(historicoVO);
			}
		}
		if (Uteis.isAtributoPreenchido(lista)) {
			for(List<HistoricoVO> historicoVOs: mapHistoricoPorDisciplina.values()){
				getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicoVOs, true, true, usuario);
			}
			for(List<HistoricoVO> historicoVOs: mapHistoricoPorDisciplinaComposta.values()){
				getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicoVOs, true, true, usuario);
			}
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
		try {
//			PeriodoLetivoAtivoUnidadeEnsinoCurso.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM PeriodoLetivoAtivoUnidadeEnsinoCurso WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
//			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade().preencherIncluirProcessoMatriculaLog(obj, nivelProcessoMatricula, "exclusão", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public void incluirPeriodoLetivoAtivoUnidadeEnsinoCursoLogExclusao(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
		try {
			// String str =
			// "SELECT * FROM PeriodoLetivoAtivoUnidadeEnsinoCurso WHERE (codigo = "
			// + codigo.intValue() + ") and unidadeEnsinoCurso = " +
			// unidadeEnsinoCurso.intValue();
			// SqlRowSet tabelaResultado =
			// getConexao().getJdbcTemplate().queryForRowSet(str);
			// PeriodoLetivoAtivoUnidadeEnsinoCursoVO
			// periodoLetivoAtivoUnidadeEnsinoCursoVO =
			// montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			// usuario);
//			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoLogFacade().preencherIncluirProcessoMatriculaLog(periodoLetivoAtivoUnidadeEnsinoCursoVO, nivelProcessoMatricula, "exclusão", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPorCodigoEUnidadeEnsinoCurso(Integer codigo, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception {
		try {
//			PeriodoLetivoAtivoUnidadeEnsinoCurso.excluir(getIdEntidade());
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			// String sql =
			// "DELETE FROM PeriodoLetivoAtivoUnidadeEnsinoCurso WHERE (codigo = "
			// + codigo.intValue() + " and unidadeEnsinoCurso = " +
			// unidadeEnsinoCurso.intValue() + ")";
			// getConexao().getJdbcTemplate().update(sql);
			excluir(obj, nivelProcessoMatricula, usuario);
//			incluirPeriodoLetivoAtivoUnidadeEnsinoCursoLogExclusao(obj, nivelProcessoMatricula, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCurso</code> através do valor do
	 * atributo <code>Integer reponsavelAbertura</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> resultantes
	 *         da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorReponsavelAbertura(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PeriodoLetivoAtivoUnidadeEnsinoCurso WHERE reponsavelAbertura >= " + valorConsulta.intValue() + " ORDER BY reponsavelAbertura";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPeriodoAtivo(Integer unidadeEnsino, Integer curso, Integer turno, String tipo, String mes, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PeriodoLetivoAtivoUnidadeEnsinoCurso WHERE curso = " + curso.intValue() + " and situacao= 'AT' and turno = "+turno;
		if (tipo.equals("")) {
			return new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		}
		if (tipo.equals("SE")) {
			sqlStr += " and semestreReferenciaPeriodoLetivo = '" + mes + "' and anoReferenciaPeriodoLetivo = '" + ano + "'";
		}
		if (tipo.equals("AN")) {
			sqlStr += " and anoReferenciaPeriodoLetivo = '" + ano + "'";
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr += " and exists (select processomatriculacalendario.curso from processomatriculacalendario where processomatriculacalendario.PeriodoLetivoAtivoUnidadeEnsinoCurso = PeriodoLetivoAtivoUnidadeEnsinoCurso.codigo  ";
			sqlStr += " and exists (select processomatriculaunidadeensino.codigo from processomatriculaunidadeensino where processomatriculacalendario.processomatricula = processomatriculaunidadeensino.processomatricula and processomatriculaunidadeensino.unidadeensino = "+unidadeEnsino;
			sqlStr += " ))  ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> consultarPorUnidadeEnsinoCursoSituacaoDataFechamentoSemMatriculaPeriodoAtiva(Integer periodoLetivoAtivoUnidadeEnsinoCurso, Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turno, Date dataFimPeriodoLetivo, String ano, String semestre, String situacaoFechamentoPeriodoLetivo, String nivelEducacionalApresentar,String periodicidade) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select ");
		sqlStr.append("count(matriculaperiodo.codigo) as qtdeAlunosAtivos,  ");
		sqlStr.append("periodoAtivo.codigo, periodoAtivo.tipoPeriodoLetivo, periodoAtivo.anoReferenciaPeriodoLetivo, periodoAtivo.semestreReferenciaPeriodoLetivo, periodoAtivo.dataInicioPeriodoLetivo, periodoAtivo.dataFimPeriodoLetivo, ");
		sqlStr.append("curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", ");
		sqlStr.append("uec.codigo AS \"unidadeEnsinoCurso.codigo\", processomatricula.codigo as \"processomatricula.codigo\", processomatricula.descricao as \"processomatricula.descricao\", nivelprocessomatricula, ");
		sqlStr.append("matriculaPeriodo.situacaomatriculaperiodo as situacaomatriculaperiodo ");
		sqlStr.append("from matriculaPeriodo ");
		sqlStr.append("inner join unidadeEnsinoCurso uec on uec.codigo =  matriculaPeriodo.unidadeEnsinoCurso ");
		sqlStr.append("inner join curso on curso.codigo = uec.curso ");
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.codigo = uec.unidadeensino ");
		sqlStr.append("inner join turma on turma.codigo = matriculaPeriodo.turma ");
		sqlStr.append("inner join turno on turno.codigo = uec.turno ");
		sqlStr.append("inner join processomatricula on processomatricula.codigo = matriculaPeriodo.processomatricula  ");
		sqlStr.append("inner join processomatriculacalendario on processomatricula.codigo = processomatriculacalendario.processomatricula ");
		sqlStr.append("and processomatriculacalendario.curso = uec.curso and processomatriculacalendario.turno = uec.turno ");
		sqlStr.append("inner join periodoletivoativounidadeensinocurso as periodoAtivo on periodoAtivo.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append(" where 1=1");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
			sqlStr.append(" and ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(unidadeEnsinoVOs, "unidadeensino.codigo"));
		}
		if(Uteis.isAtributoPreenchido(nivelEducacionalApresentar)){
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacionalApresentar).append("'");
		}
		if (periodicidade != null && Uteis.isAtributoPreenchido(periodicidade)) {
			sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("'");
		}
		if (situacaoFechamentoPeriodoLetivo.equals("AT")) {
			sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo = 'AT'");
		} else {
			sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo = 'FI'");
		}
		
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and turno.codigo = ").append(turno);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turma.codigo != ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and periodoAtivo.anoReferenciaPeriodoLetivo = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and periodoAtivo.semestreReferenciaPeriodoLetivo = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(periodoLetivoAtivoUnidadeEnsinoCurso)) {
			sqlStr.append(" and periodoAtivo.codigo = ").append(periodoLetivoAtivoUnidadeEnsinoCurso);
		}
		if (Uteis.isAtributoPreenchido(dataFimPeriodoLetivo)) {
			sqlStr.append(" and PeriodoAtivo.dataFimPeriodoLetivo <= '").append(Uteis.getDataJDBC(dataFimPeriodoLetivo)).append("'");
		}
		sqlStr.append(" group by periodoAtivo.codigo, periodoAtivo.tipoPeriodoLetivo, periodoAtivo.anoReferenciaPeriodoLetivo, periodoAtivo.dataInicioPeriodoLetivo, periodoAtivo.dataFimPeriodoLetivo, matriculaPeriodo.situacaomatriculaperiodo, ");
		sqlStr.append("periodoAtivo.semestreReferenciaPeriodoLetivo, curso.codigo , curso.nome , turno.codigo , turno.nome, turma.codigo , turma.identificadorturma, uec.codigo, matriculaperiodo.processomatricula, ");
		sqlStr.append("processomatricula.codigo, processomatricula.descricao  ");
		sqlStr.append("having count(matriculaperiodo.codigo) > 0 ");
		sqlStr.append("order by turma.codigo, periodoAtivo.anoReferenciaPeriodoLetivo, periodoAtivo.semestreReferenciaPeriodoLetivo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaDiferenciada(tabelaResultado);
	}

	private List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> montarDadosConsultaDiferenciada(SqlRowSet tabelaResultado) throws Exception {
		List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> vetResultado = new ArrayList<PeriodoLetivoAtivoUnidadeEnsinoCursoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosDiferenciada(tabelaResultado));
		}
		return vetResultado;
	}


	private PeriodoLetivoAtivoUnidadeEnsinoCursoVO montarDadosDiferenciada(SqlRowSet dadosSQL) throws Exception {
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTipoPeriodoLetivo(dadosSQL.getString("tipoPeriodoLetivo"));
		obj.setAnoReferenciaPeriodoLetivo(dadosSQL.getString("anoReferenciaPeriodoLetivo"));
		obj.setDataInicioPeriodoLetivo(dadosSQL.getDate("dataInicioPeriodoLetivo"));
		obj.setDataFimPeriodoLetivo(dadosSQL.getDate("dataFimPeriodoLetivo"));
		obj.setSemestreReferenciaPeriodoLetivo(dadosSQL.getString("semestreReferenciaPeriodoLetivo"));
		// Dados do Curso
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		// Dados do Turno
		obj.getTurnoVO().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getTurnoVO().setNome(dadosSQL.getString("turno.nome"));
		// Dados da Turma
		obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));

		// Dados ProcessoMatricula
		obj.getProcessoMatriculaVO().setCodigo(dadosSQL.getInt("processomatricula.codigo"));
		obj.getProcessoMatriculaVO().setDescricao(dadosSQL.getString("processomatricula.descricao"));
		obj.setQtdeAlunosAtivos(dadosSQL.getInt("qtdeAlunosAtivos"));
		//
		// obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
		// obj.getReponsavelAbertura().setCodigo(dadosSQL.getInt("reponsavelAbertura"));
		// obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		// obj.getResponsavelFechamento().setCodigo(dadosSQL.getInt("responsavelFechamento"));
		obj.getProcessoMatriculaVO().setNivelProcessoMatricula(dadosSQL.getString("nivelprocessomatricula"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setSituacao(dadosSQL.getString("situacaomatriculaperiodo"));
		return obj;
	}

	/***
	 *
	 * Método responsável por consultar o periodoletivoativounidadeensinocurso
	 * pelos filtros conforme especificados abaixo
	 *
	 * @param ano
	 * @param semestre
	 * @param codigoUnidadeEnsino
	 * @param codigoTurno
	 * @param codigoCurso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 *
	 */
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorUnidadeEnsinoTurnoCursoAnoSemestre(String ano, String semestre, Integer codigoUnidadeEnsino, Integer codigoTurno, Integer codigoCurso, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT periodoletivoativounidadeensinocurso.* FROM periodoletivoativounidadeensinocurso ");
		sqlStr.append(" INNER JOIN processomatriculacalendario ON (periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso) ");
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			sqlStr.append(" INNER JOIN processomatriculaunidadeensino ON (processomatriculaunidadeensino.processomatricula = processomatriculacalendario.processomatricula) ");
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = '").append(matricula);
			sqlStr.append("' AND matriculaperiodo.ano = periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo ");
			sqlStr.append(" AND matriculaperiodo.semestre = periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo ");
			sqlStr.append(" AND matriculaperiodo.processomatricula = processomatriculacalendario.processomatricula ");
		}
		sqlStr.append(" WHERE 1 = 1 ");
		if (!ano.equals("")) {
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			sqlStr.append("  AND processomatriculaunidadeensino.unidadeensino = ").append(codigoUnidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(codigoTurno)) {
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.turno = ").append(codigoTurno);
		}
		if (Uteis.isAtributoPreenchido(codigoCurso)) {
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.curso = ").append(codigoCurso);
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" ORDER BY CASE WHEN matriculaperiodo.codigo IS NOT NULL THEN 0 ELSE 1 END ");
		}
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivo = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		if (tabelaResultado.next()) {
			periodoLetivo = montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return periodoLetivo;
	}

	public void consultarPeriodoLetivoAtivo(Integer unidadeEnsinoCurso, String semestre, String ano) throws ConsistirException {
		try {
			String sqlStr = "Select periodoletivoativounidadeensinocurso.situacao as situacao, periodoletivoativounidadeensinocurso.datafechamento as data " + "FROM periodoletivoativounidadeensinocurso " + "WHERE periodoletivoativounidadeensinocurso.unidadeensinocurso = " + unidadeEnsinoCurso + " " + "AND periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = '" + semestre + "' " + "AND periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '" + ano + "' " + "AND periodoletivoativounidadeensinocurso.situacao = 'FI' " + "GROUP BY periodoletivoativounidadeensinocurso.datafechamento, periodoletivoativounidadeensinocurso.situacao " + "ORDER BY periodoletivoativounidadeensinocurso.datafechamento";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (tabelaResultado.next()) {
				String data = Uteis.getData(tabelaResultado.getDate("data"));
				throw new ConsistirException("Este Período Letivo já foi fechado no dia " + data + ". Não é possível realizar essa operação.");
			}
		} catch (Exception e) {
			throw new ConsistirException(e.getMessage());
		}
	}

	@Override
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorUltimaMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT periodoletivoativounidadeensinocurso.* FROM processomatricula");
		sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo");
		sqlStr.append(" inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso");	
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.processomatricula = processomatricula.codigo ");
		sqlStr.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = '").append(matricula).append("' and mp.situacao not in ('PC') ORDER BY (mp.ano||'/'||mp.semestre) desc, mp.codigo desc  limit 1  )");
		sqlStr.append(" INNER JOIN unidadeensinocurso ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo ");
		sqlStr.append(" and unidadeensinocurso.curso = periodoletivoativounidadeensinocurso.curso ");
		sqlStr.append(" and unidadeensinocurso.turno = periodoletivoativounidadeensinocurso.turno ");
		sqlStr.append(" WHERE matriculaperiodo.matricula = '").append(matricula).append("'");
		sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo = ('AT') ");
		sqlStr.append(" AND ( ( matriculaPeriodo.ano =   CAST((SELECT EXTRACT (YEAR FROM (SELECT NOW()))) AS TEXT )  ");
		sqlStr.append(" AND matriculaPeriodo.semestre =  CAST((SELECT CASE WHEN ( SELECT EXTRACT (MONTH FROM (SELECT NOW())) > 7 ) THEN '2' ELSE '1' END) as TEXT ) ");
		sqlStr.append(" ) OR (( matriculaPeriodo.ano = '' or matriculaPeriodo.ano is null) and (matriculaPeriodo.semestre = '' or matriculaPeriodo.semestre is null))) ");
		sqlStr.append(" ORDER BY (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) desc, matriculaperiodo.codigo desc  limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> resultantes
	 *         da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>.
	 * 
	 * @return O objeto da classe
	 *         <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static PeriodoLetivoAtivoUnidadeEnsinoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getCursoVO().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.getTurnoVO().setCodigo(new Integer(dadosSQL.getInt("turno")));
		obj.setSemestreReferenciaPeriodoLetivo(dadosSQL.getString("semestreReferenciaPeriodoLetivo"));
		obj.setAnoReferenciaPeriodoLetivo(dadosSQL.getString("anoReferenciaPeriodoLetivo"));
		obj.setTipoPeriodoLetivo(dadosSQL.getString("tipoPeriodoLetivo"));
		obj.setDataInicioPeriodoLetivo(dadosSQL.getDate("dataInicioPeriodoLetivo"));
		obj.setDataFimPeriodoLetivo(dadosSQL.getDate("dataFimPeriodoLetivo"));
		obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
		obj.setDataInicioPeriodoLetivoPrimeiroBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoPrimeiroBimestre"));
		obj.setDataFimPeriodoLetivoPrimeiroBimestre(dadosSQL.getDate("dataFimPeriodoLetivoPrimeiroBimestre"));
		obj.setDataInicioPeriodoLetivoSegundoBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoSegundoBimestre"));
		obj.setDataFimPeriodoLetivoSegundoBimestre(dadosSQL.getDate("dataFimPeriodoLetivoSegundoBimestre"));
		obj.setDataInicioPeriodoLetivoTerceiroBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoTerceiroBimestre"));
		obj.setDataFimPeriodoLetivoTerceiroBimestre(dadosSQL.getDate("dataFimPeriodoLetivoTerceiroBimestre"));
		obj.setDataInicioPeriodoLetivoQuartoBimestre(dadosSQL.getDate("dataInicioPeriodoLetivoQuartoBimestre"));
		obj.setDataFimPeriodoLetivoQuartoBimestre(dadosSQL.getDate("dataFimPeriodoLetivoQuartoBimestre"));
		obj.setQtdeDiaLetivoPrimeiroBimestre(dadosSQL.getInt("qtdeDiaLetivoPrimeiroBimestre"));
		obj.setQtdeSemanaLetivaPrimeiroBimestre(dadosSQL.getInt("qtdeSemanaLetivaPrimeiroBimestre"));
		obj.setQtdeDiaLetivoSegundoBimestre(dadosSQL.getInt("qtdeDiaLetivoSegundoBimestre"));
		obj.setQtdeSemanaLetivaSegundoBimestre(dadosSQL.getInt("qtdeSemanaLetivaSegundoBimestre"));
		obj.setQtdeDiaLetivoTerceiroBimestre(dadosSQL.getInt("qtdeDiaLetivoTerceiroBimestre"));
		obj.setQtdeSemanaLetivaTerceiroBimestre(dadosSQL.getInt("qtdeSemanaLetivaTerceiroBimestre"));
		obj.setQtdeDiaLetivoQuartoBimestre(dadosSQL.getInt("qtdeDiaLetivoQuartoBimestre"));
		obj.setQtdeSemanaLetivaQuartoBimestre(dadosSQL.getInt("qtdeSemanaLetivaQuartoBimestre"));
		obj.setTotalDiaLetivoAno(dadosSQL.getInt("totalDiaLetivoAno"));
		obj.setTotalSemanaLetivaAno(dadosSQL.getInt("totalSemanaLetivaAno"));
		obj.getReponsavelAbertura().setCodigo(new Integer(dadosSQL.getInt("reponsavelAbertura")));
		obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		obj.getResponsavelFechamento().setCodigo(new Integer(dadosSQL.getInt("responsavelFechamento")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsinoCurso(obj, nivelMontarDados, usuario);
		montarDadosResponsavelAbertura(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelFechamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosUnidadeEnsinoCurso(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		 if (obj.getCursoVO().getCodigo().intValue() == 0) {
	            return;
	        }
	        obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
	        if (obj.getTurnoVO().getCodigo().intValue() == 0) {
	            return;
	        }
	        obj.setTurnoVO(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurnoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  usuario));
	}

	public static void montarDadosResponsavelAbertura(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getReponsavelAbertura().getCodigo().intValue() == 0 || obj.getReponsavelAbertura() == null) {
			return;
		}
		obj.setReponsavelAbertura(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getReponsavelAbertura().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavelFechamento(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelFechamento().getCodigo().intValue() == 0 || obj.getResponsavelFechamento() == null) {
			return;
		}
		obj.setResponsavelFechamento(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelFechamento().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM PeriodoLetivoAtivoUnidadeEnsinoCurso WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( PeriodoLetivoAtivoUnidadeEnsinoCurso ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
//
//	/**
//	 * Operação reponsável por retornar o identificador desta classe. Este
//	 * identificar é utilizado para verificar as permissões de acesso as
//	 * operações desta classe.
//	 */
//	public static String getIdEntidade() {
//		return PeriodoLetivoAtivoUnidadeEnsinoCurso.idEntidade;
//	}
//
//	/**
//	 * Operação reponsável por definir um novo valor para o identificador desta
//	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
//	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
//	 * que Como o controle de acesso é realizado com base neste identificador,
//	 */
//	public void setIdEntidade(String idEntidade) {
//		PeriodoLetivoAtivoUnidadeEnsinoCurso.idEntidade = idEntidade;
//	}

	/**
	 * Método responsável por inicializar os dados do calendário do bimestre
	 * trazendo como padrão datas pré-definidas para nível fundamental e médio.
	 */
	@Override
	public void inicializarDadosCalendarioBimestre(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, ProcessoMatriculaVO processoMatriculaVO) throws Exception {
		obj.setDataInicioPeriodoLetivoPrimeiroBimestre(processoMatriculaVO.getDataFinal());
		obj.setDataInicioPeriodoLetivoSegundoBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 90));
		obj.setDataInicioPeriodoLetivoTerceiroBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 180));
		obj.setDataInicioPeriodoLetivoQuartoBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 270));

		obj.setDataFimPeriodoLetivoPrimeiroBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 90));
		obj.setDataFimPeriodoLetivoSegundoBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 180));
		obj.setDataFimPeriodoLetivoTerceiroBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 270));
		obj.setDataFimPeriodoLetivoQuartoBimestre(Uteis.obterDataFutura(processoMatriculaVO.getDataFinal(), 365));
	}

	/**
	 * Método responsável por calcular a quantidade de dias e semanas letivas de
	 * cada bimestre de nível fundamental e médio de acordo com a data
	 * selecionada.
	 * 
	 * @throws Exception
	 */
	@Override
	public void calcularDadosDiaSemanaLetiva(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) throws Exception {
		try {
			calcularQtdeDiaLetivo(obj);
			calcularTotalDiaLetivoAno(obj);
			calcularQtdeSemanaLetiva(obj);
			calcularTotalSemanaLetivaAno(obj);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por calcular a quantidade de dias e semanas letivas de
	 * cada bimestre de nível fundamental e médio de acordo com que os dias é
	 * alterado.
	 * 
	 * @throws Exception
	 */
	@Override
	public void calcularDadosDiaSemanaLetivaQtdeDias(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) throws Exception {
		try {
			calcularTotalDiaLetivoAno(obj);
			calcularQtdeSemanaLetiva(obj);
			calcularTotalSemanaLetivaAno(obj);
		} catch (Exception e) {
			throw e;
		}
	}

	public void calcularQtdeDiaLetivo(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) throws Exception {
		if (obj.getDataInicioPeriodoLetivoPrimeiroBimestre() != null && obj.getDataFimPeriodoLetivoPrimeiroBimestre() != null) {
			obj.setQtdeDiaLetivoPrimeiroBimestre(Uteis.getCalculaDiasUteis(obj.getDataInicioPeriodoLetivoPrimeiroBimestre(), obj.getDataFimPeriodoLetivoPrimeiroBimestre()));
		} else {
			obj.setQtdeDiaLetivoPrimeiroBimestre(0);
		}
		if (obj.getDataInicioPeriodoLetivoSegundoBimestre() != null && obj.getDataFimPeriodoLetivoSegundoBimestre() != null) {
			obj.setQtdeDiaLetivoSegundoBimestre(Uteis.getCalculaDiasUteis(obj.getDataInicioPeriodoLetivoSegundoBimestre(), obj.getDataFimPeriodoLetivoSegundoBimestre()));
		} else {
			obj.setQtdeDiaLetivoSegundoBimestre(0);
		}
		if (obj.getDataInicioPeriodoLetivoTerceiroBimestre() != null && obj.getDataFimPeriodoLetivoTerceiroBimestre() != null) {
			obj.setQtdeDiaLetivoTerceiroBimestre(Uteis.getCalculaDiasUteis(obj.getDataInicioPeriodoLetivoTerceiroBimestre(), obj.getDataFimPeriodoLetivoTerceiroBimestre()));
		} else {
			obj.setQtdeDiaLetivoTerceiroBimestre(0);
		}
		if (obj.getDataInicioPeriodoLetivoQuartoBimestre() != null && obj.getDataFimPeriodoLetivoQuartoBimestre() != null) {
			obj.setQtdeDiaLetivoQuartoBimestre(Uteis.getCalculaDiasUteis(obj.getDataInicioPeriodoLetivoQuartoBimestre(), obj.getDataFimPeriodoLetivoQuartoBimestre()));
		} else {
			obj.setQtdeDiaLetivoQuartoBimestre(0);
		}
	}

	public void calcularQtdeSemanaLetiva(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) {
		obj.setQtdeSemanaLetivaPrimeiroBimestre(Uteis.arredondarDivisaoEntreNumeros(obj.getQtdeDiaLetivoPrimeiroBimestre().doubleValue() / 5));
		obj.setQtdeSemanaLetivaSegundoBimestre(Uteis.arredondarDivisaoEntreNumeros(obj.getQtdeDiaLetivoSegundoBimestre().doubleValue() / 5));
		obj.setQtdeSemanaLetivaTerceiroBimestre(Uteis.arredondarDivisaoEntreNumeros(obj.getQtdeDiaLetivoTerceiroBimestre().doubleValue() / 5));
		obj.setQtdeSemanaLetivaQuartoBimestre(Uteis.arredondarDivisaoEntreNumeros(obj.getQtdeDiaLetivoQuartoBimestre().doubleValue() / 5));
	}

	public void calcularTotalDiaLetivoAno(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) {
		obj.setTotalDiaLetivoAno(obj.getQtdeDiaLetivoPrimeiroBimestre() + obj.getQtdeDiaLetivoSegundoBimestre() + obj.getQtdeDiaLetivoTerceiroBimestre() + obj.getQtdeDiaLetivoQuartoBimestre());
	}

	public void calcularTotalSemanaLetivaAno(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj) {
		obj.setTotalSemanaLetivaAno(obj.getQtdeSemanaLetivaPrimeiroBimestre() + obj.getQtdeSemanaLetivaSegundoBimestre() + obj.getQtdeSemanaLetivaTerceiroBimestre() + obj.getQtdeSemanaLetivaQuartoBimestre());
	}


	public Date consultarDataFimPeriodoLetivoPorTurmaAnoSemestre(Integer turma, String ano, String semestre) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select distinct periodoletivoativounidadeensinocurso.datafimperiodoletivo from periodoletivoativounidadeensinocurso ");
		sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo ");
		sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculacalendario.processomatricula = processomatriculaunidadeensino.processomatricula ");
		sqlStr.append(" inner join turma on turma.curso = periodoletivoativounidadeensinocurso.curso and turma.turno = periodoletivoativounidadeensinocurso.turno ");
		sqlStr.append(" where turma.unidadeensino = processomatriculaunidadeensino.unidadeensino ");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (ano != null && !ano.equals("")) {
			sqlStr.append(" and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append(" and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = '").append(semestre).append("' ");
		}
		sqlStr.append(" order by periodoletivoativounidadeensinocurso.datafimperiodoletivo desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getDate("datafimperiodoletivo");
		}
		return new Date();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataFimPeriodoLetivoAtivoParaFinalizacao(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDataInicioPeriodoLetivo().after(obj.getDataFimPeriodoLetivo())) {
			throw new ConsistirException("A (DATA FIM) deve ser maior que a (DATA INÍCIO) do período letivo.");
		}
		String sqlStr = "UPDATE periodoletivoativounidadeensinocurso SET datafimperiodoletivo = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getDataFimPeriodoLetivo(), obj.getCodigo() });
	}
	
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarDataInicioDataFimPeriodoBimestrePorMatriculaPeriodoUnidadeEnsinoCurso(Integer matriculaPeriodo, Integer unidadeEnsinoCurso, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT periodoLetivoAtivoUnidadeEnsinoCurso.codigo, ");
		sb.append(" periodoLetivoAtivoUnidadeEnsinoCurso.DataInicioPeriodoLetivoPrimeiroBimestre, periodoLetivoAtivoUnidadeEnsinoCurso.DataFimPeriodoLetivoPrimeiroBimestre, ");
		sb.append(" periodoLetivoAtivoUnidadeEnsinoCurso.DataInicioPeriodoLetivoSegundoBimestre, periodoLetivoAtivoUnidadeEnsinoCurso.DataFimPeriodoLetivoSegundoBimestre, ");
		sb.append(" periodoLetivoAtivoUnidadeEnsinoCurso.DataInicioPeriodoLetivoTerceiroBimestre, periodoLetivoAtivoUnidadeEnsinoCurso.DataFimPeriodoLetivoTerceiroBimestre ");
		sb.append(" FROM matriculaperiodo  ");
		sb.append(" INNER JOIN unidadeensinocurso ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo ");
		sb.append(" INNER JOIN processomatriculacalendario ON matriculaperiodo.processomatricula = processomatriculacalendario.processomatricula and unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno  ");
		sb.append(" INNER JOIN periodoLetivoAtivoUnidadeEnsinoCurso on periodoLetivoAtivoUnidadeEnsinoCurso.codigo = processomatriculacalendario.periodoLetivoAtivoUnidadeEnsinoCurso ");
		sb.append(" WHERE matriculaperiodo.codigo = ").append(matriculaPeriodo);
		sb.append(" AND unidadeensinocurso.codigo = ").append(unidadeEnsinoCurso);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDataInicioPeriodoLetivoPrimeiroBimestre(tabelaResultado.getDate("DataInicioPeriodoLetivoPrimeiroBimestre"));
			obj.setDataFimPeriodoLetivoPrimeiroBimestre(tabelaResultado.getDate("DataFimPeriodoLetivoPrimeiroBimestre"));
			
			obj.setDataInicioPeriodoLetivoSegundoBimestre(tabelaResultado.getDate("DataInicioPeriodoLetivoSegundoBimestre"));
			obj.setDataFimPeriodoLetivoSegundoBimestre(tabelaResultado.getDate("DataFimPeriodoLetivoSegundoBimestre"));
			
			obj.setDataInicioPeriodoLetivoTerceiroBimestre(tabelaResultado.getDate("DataInicioPeriodoLetivoTerceiroBimestre"));
			obj.setDataFimPeriodoLetivoTerceiroBimestre(tabelaResultado.getDate("DataFimPeriodoLetivoTerceiroBimestre"));
		}
		return obj;
	}
	
	@Override
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(Integer matriculaperiodoturmadisciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT periodoLetivoAtivoUnidadeEnsinoCurso.codigo, ");
		sb.append(" periodoLetivoAtivoUnidadeEnsinoCurso.dataInicioPeriodoLetivo, periodoLetivoAtivoUnidadeEnsinoCurso.dataFimPeriodoLetivo ");
		sb.append(" FROM matriculaperiodoturmadisciplina ");
		sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" INNER JOIN unidadeensinocurso ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo ");
		sb.append(" INNER JOIN processomatriculacalendario ON matriculaperiodo.processomatricula = processomatriculacalendario.processomatricula and unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno  ");
		sb.append(" INNER JOIN periodoletivoativounidadeensinocurso ON processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo  ");
		sb.append(" WHERE matriculaperiodoturmadisciplina.codigo = ").append(matriculaperiodoturmadisciplina);
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		if (rs.next()) {
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDataInicioPeriodoLetivo(rs.getDate("dataInicioPeriodoLetivo"));
			obj.setDataFimPeriodoLetivo(rs.getDate("dataFimPeriodoLetivo"));	
		}
		return obj;
	}
	
	@Override
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO consultarPorMatriculaAnoSemestre(String matricula, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT periodoletivoativounidadeensinocurso.* FROM processomatricula");
		sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo");
		sqlStr.append(" inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.processomatricula = processomatricula.codigo ");
		sqlStr.append(" INNER JOIN unidadeensinocurso ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo and unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno ");		
		sqlStr.append(" INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" WHERE matriculaperiodo.matricula = ? ");
		sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo != ('PC') ");
		sqlStr.append(" AND ( (curso.periodicidade = 'AN' and matriculaPeriodo.ano =  '"+ano+"')  ");
		sqlStr.append(" or (curso.periodicidade = 'SE' and matriculaPeriodo.ano =  '"+ano+"'  and matriculaPeriodo.semestre =  '"+semestre+"')  ");
		sqlStr.append(" or (curso.periodicidade = 'IN')  ");
		sqlStr.append(" ) ORDER BY (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc  limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
	}

	@Override
	public void alterarDatasPeriodoLetivo(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception {
		CompetenciaFolhaPagamento.alterar(getIdEntidade(), false, usuario);

		alterar(obj, "PeriodoLetivoAtivoUnidadeEnsinoCurso", new AtributoPersistencia()
				.add("dataInicioPeriodoLetivoPrimeiroBimestre", Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoPrimeiroBimestre()))
				.add("dataInicioPeriodoLetivoSegundoBimestre", Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoSegundoBimestre()))
				.add("dataInicioPeriodoLetivoTerceiroBimestre", Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoTerceiroBimestre()))
				.add("dataInicioPeriodoLetivoQuartoBimestre", Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivoQuartoBimestre()))
				.add("dataFimPeriodoLetivoPrimeiroBimestre", Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoPrimeiroBimestre()))
				.add("dataFimPeriodoLetivoSegundoBimestre", Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoSegundoBimestre()))
				.add("dataFimPeriodoLetivoTerceiroBimestre", Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoTerceiroBimestre()))
				.add("dataFimPeriodoLetivoQuartoBimestre", Uteis.getDataJDBC(obj.getDataFimPeriodoLetivoQuartoBimestre()))

				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		
	}
}