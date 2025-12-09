package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import negocio.comuns.academico.CalendarioRegistroAulaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.AtividadeComplementarMatriculaVO;
import negocio.comuns.secretaria.AtividadeComplementarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.AtividadeComplementarInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class AtividadeComplementar extends ControleAcesso implements AtividadeComplementarInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AtividadeComplementar() {
		super();
		setIdEntidade("AtividadeComplementar");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDados(AtividadeComplementarVO obj) throws Exception {
		if (obj.getListaAtividadeComplementarMatriculaVOs().isEmpty()) {
			throw new Exception("Nenhum aluno foi encontrado com os filtros informados.");
		}
		if (obj.getTurmaVO().getCodigo().equals(0)) {
			throw new Exception("O campo TURMA deve ser informado.");
		}
		if (obj.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
		if (obj.getQtdeHoraComplementar().equals(0)) {
			throw new Exception("O campo HORA COMPLEMENTAR deve ser informado.");
		}
		if (obj.getConteudo().trim().isEmpty()) {
			throw new Exception("O campo CONTEÚDO deve ser informado.");
		}
		Boolean selecionado = Boolean.FALSE;
		for (AtividadeComplementarMatriculaVO atividadeComplementarVO : obj.getListaAtividadeComplementarMatriculaVOs()) {
			if (atividadeComplementarVO.getAlunoSelecionado()) {
				selecionado = Boolean.TRUE;
			}
		}
		if (!selecionado) {
			throw new Exception("Nenhum aluno foi selecionado, por favor selecione para continuar a operação.");
		}

	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			realizarVerificacaoBloqueioLancamentoRegistroAula(obj.getRegistroAulaVO(), usuario);
			final String sql = "INSERT INTO AtividadeComplementar( turma, disciplina, registroAula, qtdeHoraComplementar, dataAtividade, responsavel, ano, semestre, conteudo ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getTurmaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getTurmaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getRegistroAulaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getRegistroAulaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, obj.getQtdeHoraComplementar().intValue());
					sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataAtividade()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getAno());
					sqlInserir.setString(8, obj.getSemestre());
					sqlInserir.setString(9, obj.getConteudo());
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
			getFacadeFactory().getAtividadeComplementarMatriculaFacade().incluirAtividadeComplementarMatricula(obj.getCodigo(), obj.getListaAtividadeComplementarMatriculaVOs(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {

			validarDados(obj);
			realizarVerificacaoBloqueioLancamentoRegistroAula(obj.getRegistroAulaVO(), usuario);
			final String sql = "UPDATE AtividadeComplementar set turma=?, disciplina=?, registroAula=?, qtdeHoraComplementar=?, dataAtividade=?, responsavel=?, ano=?, semestre=?, conteudo = ? where (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getTurmaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getTurmaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getRegistroAulaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getRegistroAulaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, obj.getQtdeHoraComplementar().intValue());
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataAtividade()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getAno());
					sqlAlterar.setString(8, obj.getSemestre());
					sqlAlterar.setString(9, obj.getConteudo());
					sqlAlterar.setInt(10, obj.getCodigo());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getAtividadeComplementarMatriculaFacade().alterarAtividadeComplementarMatricula(obj.getCodigo(), obj.getListaAtividadeComplementarMatriculaVOs(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		AtividadeComplementar.excluir(getIdEntidade(), true, usuario);
		realizarVerificacaoBloqueioLancamentoRegistroAula(obj.getRegistroAulaVO(), usuario);		
		getFacadeFactory().getAtividadeComplementarMatriculaFacade().excluirAtividadeComplementarMatriculaVOs(obj.getCodigo(), new ArrayList<AtividadeComplementarMatriculaVO>(0), usuario);		
		String sql = "DELETE FROM AtividadeComplementar WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);		
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		if (obj.getRegistroAulaVO().getTurma().getTurmaAgrupada()) {
			getFacadeFactory().getRegistroAulaFacade().excluir(Arrays.asList(obj.getRegistroAulaVO()), "Operação -> Exclusão registros Vinculados Atividade Complementar - Visão Administrativa", usuario, Boolean.TRUE);
		} else {
			getFacadeFactory().getRegistroAulaFacade().excluir(obj.getRegistroAulaVO(), usuario, Boolean.TRUE);
		}
		for(AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO: obj.getListaAtividadeComplementarMatriculaVOs()) {
        	if(Uteis.isAtributoPreenchido(atividadeComplementarMatriculaVO.getCodigo())) {
        		getFacadeFactory().getHistoricoFacade().realizarAtualizacaoHoraComplementarHistorico(atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), usuario);
        	}
		}
		
	}

	public void realizarVerificacaoBloqueioLancamentoRegistroAula(RegistroAulaVO registroAulaVO, UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO == null || !usuarioVO.getIsApresentarVisaoProfessor()) {
			return;
		}
		CalendarioRegistroAulaVO calendarioRegistroAulaVO = getFacadeFactory().getCalendarioRegistroAulaFacade().consultarPorCalendarioRegistroAulaUtilizar(registroAulaVO.getTurma().getUnidadeEnsino().getCodigo(), registroAulaVO.getTurma().getCodigo(), registroAulaVO.getTurma().getTurmaAgrupada(), registroAulaVO.getProfessor().getCodigo(), Uteis.getData(registroAulaVO.getData(), "yyyy"), false, usuarioVO);
		if (calendarioRegistroAulaVO == null || calendarioRegistroAulaVO.getCodigo() == null || calendarioRegistroAulaVO.getCodigo() == 0) {
			return;
		}
		Date dataBase = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(new Date());
		if (Uteis.getMesData(registroAulaVO.getData()) == 1 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataJaneiro()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataJaneiro())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 2 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataFevereiro()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataFevereiro())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 3 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataMarco()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataMarco())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 4 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataAbril()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataAbril())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 5 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataMaio()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataMaio())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 6 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataJunho()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataJunho())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 7 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataJulho()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataJulho())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 8 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataAgosto()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataAgosto())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 9 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataSetembro()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataSetembro())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 10 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataOutubro()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataOutubro())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 11 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataNovembro()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataNovembro())));
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 12 && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calendarioRegistroAulaVO.getDataDezembro()).compareTo(dataBase) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoAtividadeComplementarEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataDezembro())));
		}
	}

	public void preencherTodosListaAluno(List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs , Boolean marcarTodos) {
		for (AtividadeComplementarMatriculaVO atividadeComplementarVO : listaAtividadeComplementarMatriculaVOs) {
			atividadeComplementarVO.setAlunoSelecionado(marcarTodos);
		}
	}

	public void desmarcarTodosListaAluno(List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs) {
		for (AtividadeComplementarMatriculaVO atividadeComplementarVO : listaAtividadeComplementarMatriculaVOs) {
			atividadeComplementarVO.setAlunoSelecionado(Boolean.FALSE);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AtividadeComplementarVO obj, Boolean permiteLancamentoAtividadeComplementarFutura, UsuarioVO usuarioVO) throws Exception {
		if (obj.isNovoObj()) {
			incluirAtividadeComplementar(obj, obj.getListaAtividadeComplementarMatriculaVOs(), obj.getQtdeHoraComplementar(), obj.getAno(), obj.getSemestre(), obj.getTurmaVO(), obj.getDisciplinaVO(), obj.getDataAtividade(), permiteLancamentoAtividadeComplementarFutura, usuarioVO);
		} else {
			alterarAtividadeComplementar(obj, obj.getListaAtividadeComplementarMatriculaVOs(), obj.getQtdeHoraComplementar(), obj.getAno(), obj.getSemestre(), obj.getTurmaVO(), obj.getDisciplinaVO(), obj.getDataAtividade(), permiteLancamentoAtividadeComplementarFutura, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAtividadeComplementar(AtividadeComplementarVO obj, List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs, Integer horaComplementar, String ano, String semestre, TurmaVO turmaVO, DisciplinaVO disciplinaVO, Date dataAtividade, Boolean permiteLancamentoAtividadeComplementarFutura, UsuarioVO usuarioVO) throws Exception {
		try {
		validarDados(obj);
		RegistroAulaVO registroAulaVO = realizarCriacaoRegistroAulaAtravesAtividadeComplementar(obj.getConteudo(), horaComplementar, ano, semestre, turmaVO, disciplinaVO, dataAtividade, usuarioVO);
		for (AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO : listaAtividadeComplementarMatriculaVOs) {
			if(!Uteis.isAtributoPreenchido(atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO())) {
				atividadeComplementarMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula_Disciplina_Turma(atividadeComplementarMatriculaVO.getMatriculaVO().getMatricula(), atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), ano, semestre, false, false, usuarioVO);
				if (!Uteis.isAtributoPreenchido(historicoVO)) {				
					throw new Exception("Não foi encontrado o histórico da matrícula "+atividadeComplementarMatriculaVO.getMatriculaVO().getMatricula()+".");
				}
				if (atividadeComplementarMatriculaVO.getAlunoSelecionado()) {
					atividadeComplementarMatriculaVO.setHoraComplementar(horaComplementar.intValue());
				}else {
					atividadeComplementarMatriculaVO.setHoraComplementar(0);
				}
				historicoVO.setMatriculaPeriodoTurmaDisciplina(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorChavePrimariaTrazendoMatricula(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo()));
				atividadeComplementarMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(historicoVO.getMatriculaPeriodoTurmaDisciplina());
			}
			registroAulaVO.getFrequenciaAulaVOs().add(executarCriacaoFrequenciaAulaAtravesAtividadeComplementar(atividadeComplementarMatriculaVO.getMatriculaVO(), atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), atividadeComplementarMatriculaVO.getAlunoSelecionado()));
		}
		getFacadeFactory().getRegistroAulaFacade().incluir(registroAulaVO, permiteLancamentoAtividadeComplementarFutura, usuarioVO, true);
		obj.setRegistroAulaVO(registroAulaVO);
		obj.setResponsavel(usuarioVO);
		incluir(obj, usuarioVO);
		for (AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO : listaAtividadeComplementarMatriculaVOs) {
			getFacadeFactory().getHistoricoFacade().realizarAtualizacaoHoraComplementarHistorico(atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), usuarioVO);
		}
		obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getRegistroAulaVO().setDataOriginal(obj.getDataAtividade());
		obj.setNovoObj(false);
		}catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtividadeComplementar(AtividadeComplementarVO obj, List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs, Integer horaComplementar, String ano, String semestre, TurmaVO turmaVO, DisciplinaVO disciplinaVO, Date dataAtividade, Boolean permiteLancamentoAtividadeComplementarFutura, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		List<RegistroAulaVO> registroAulaVOs = new ArrayList<RegistroAulaVO>(0);
		//obj.getRegistroAulaVO().getFrequenciaAulaVOs().clear();
		//getFacadeFactory().getRegistroAulaFacade().carregarDados(obj.getRegistroAulaVO(), usuarioVO);
		inicializaDadosRegistroAulaAlteracao(obj, obj.getRegistroAulaVO(), usuarioVO);
		for (AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO : listaAtividadeComplementarMatriculaVOs) {
			if (!Uteis.isAtributoPreenchido(atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO())) {
				HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula_Disciplina_Turma(atividadeComplementarMatriculaVO.getMatriculaVO().getMatricula(), disciplinaVO.getCodigo(), turmaVO, ano, semestre, false, false, usuarioVO);
				if (!Uteis.isAtributoPreenchido(historicoVO)) {
					throw new Exception("Não foi encontrado o histórico da matrícula "+atividadeComplementarMatriculaVO.getMatriculaVO().getMatricula()+".");
				}
				historicoVO.setMatriculaPeriodoTurmaDisciplina(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorChavePrimariaTrazendoMatricula(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo()));
				atividadeComplementarMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(historicoVO.getMatriculaPeriodoTurmaDisciplina());
			}
			executarAlteracaoFrequenciaAulaAtraveAtividadeComplementar(obj.getRegistroAulaVO(), atividadeComplementarMatriculaVO.getMatriculaVO(), atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), atividadeComplementarMatriculaVO.getAlunoSelecionado());
			if (atividadeComplementarMatriculaVO.getAlunoSelecionado()) {
				atividadeComplementarMatriculaVO.setHoraComplementar(horaComplementar.intValue());
			} else {
				atividadeComplementarMatriculaVO.setHoraComplementar(0);
			}
		}
//		getFacadeFactory().getRegistroAulaFacade().alterar(obj.getRegistroAulaVO(), permiteLancamentoAtividadeComplementarFutura, usuarioVO);
		registroAulaVOs.add(obj.getRegistroAulaVO());
		getFacadeFactory().getRegistroAulaFacade().persistir(registroAulaVOs, obj.getRegistroAulaVO().getConteudo(), obj.getRegistroAulaVO().getTipoAula(), permiteLancamentoAtividadeComplementarFutura, "RegistroAula", "Alteração pelo Registro Aula Complementar", usuarioVO, true);
		alterar(obj, usuarioVO);
		for (AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO : listaAtividadeComplementarMatriculaVOs) {
			getFacadeFactory().getHistoricoFacade().realizarAtualizacaoHoraComplementarHistorico(atividadeComplementarMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), usuarioVO);
		}
		obj.getRegistroAulaVO().setDataOriginal(obj.getDataAtividade());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RegistroAulaVO realizarCriacaoRegistroAulaAtravesAtividadeComplementar(String conteudo, Integer cargaHoraria, String ano, String semestre, TurmaVO turmaVO, DisciplinaVO disciplinaVO, Date dataAtividade, UsuarioVO usuarioVO) throws Exception {
		RegistroAulaVO registroAulaVO = new RegistroAulaVO();
		registroAulaVO.setAtividadeComplementar(Boolean.TRUE);
		registroAulaVO.setDataRegistroAula(new Date());
		registroAulaVO.setResponsavelRegistroAula(usuarioVO);
		registroAulaVO.setTurma(turmaVO);
		registroAulaVO.setConteudo(conteudo);
		registroAulaVO.setCargaHoraria(cargaHoraria.intValue());
		registroAulaVO.setData(dataAtividade);
		registroAulaVO.setDiaSemana(Uteis.getDiaSemanaData(dataAtividade));
		registroAulaVO.setDisciplina(disciplinaVO);
		registroAulaVO.setTipoAula("P");
		if(!usuarioVO.getIsApresentarVisaoProfessor()) {
			ProfessorTitularDisciplinaTurmaVO professorTitularVO = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(turmaVO, disciplinaVO.getCodigo(), ano, semestre, true, usuarioVO);
			registroAulaVO.setProfessor(professorTitularVO.getProfessor().getPessoa());
		}else {
			registroAulaVO.setProfessor(usuarioVO.getPessoa());
		}
		registroAulaVO.setAno(ano);
		registroAulaVO.setSemestre(semestre);
		return registroAulaVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializaDadosRegistroAulaAlteracao(AtividadeComplementarVO obj, RegistroAulaVO registroAulaVO, UsuarioVO usuarioVO) {
		registroAulaVO.setAtividadeComplementar(Boolean.TRUE);
		registroAulaVO.setDataRegistroAula(new Date());
		registroAulaVO.setResponsavelRegistroAula(usuarioVO);
		registroAulaVO.setTurma(obj.getTurmaVO());
		registroAulaVO.setConteudo(obj.getConteudo());
		registroAulaVO.setCargaHoraria(obj.getQtdeHoraComplementar().intValue());
		registroAulaVO.setData(obj.getDataAtividade());
		registroAulaVO.setDiaSemana(Uteis.getDiaSemanaData(obj.getDataAtividade()));
		registroAulaVO.setDisciplina(obj.getDisciplinaVO());
		registroAulaVO.setTipoAula("P");
		registroAulaVO.setAno(obj.getAno());
		registroAulaVO.setSemestre(obj.getSemestre());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public FrequenciaAulaVO executarCriacaoFrequenciaAulaAtravesAtividadeComplementar(MatriculaVO matriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean presenca) {
		FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
		frequenciaAulaVO.setMatricula(matriculaVO);
		frequenciaAulaVO.setPresente(presenca);
		frequenciaAulaVO.setAbonado(Boolean.FALSE);
		frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
		frequenciaAulaVO.setJustificado(Boolean.FALSE);
		return frequenciaAulaVO;
	}

	public void executarAlteracaoFrequenciaAulaAtraveAtividadeComplementar(RegistroAulaVO obj, MatriculaVO matriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean presenca) {
		for (FrequenciaAulaVO frequenciaAulaVO : obj.getFrequenciaAulaVOs()) {
			if (frequenciaAulaVO.getMatricula().getMatricula().equals(matriculaVO.getMatricula())) {
				frequenciaAulaVO.setPresente(presenca);
				frequenciaAulaVO.setAbonado(Boolean.FALSE);
				frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
				frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
				frequenciaAulaVO.setJustificado(Boolean.FALSE);
				return;
			}
		}
		obj.getFrequenciaAulaVOs().add(executarCriacaoFrequenciaAulaAtravesAtividadeComplementar(matriculaVO, matriculaPeriodoTurmaDisciplinaVO, presenca));
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHoraComplementarAluno(final Integer horasComplementares, final Integer historico, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE historico set horaComplementar=? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setDouble(++i, horasComplementares);
					sqlAlterar.setInt(++i, historico);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public void carregarDados(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((AtividadeComplementarVO) obj, NivelMontarDados.TODOS, usuario);
	}

	/**
	 * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
	 * Completo e faz a consulta de acordo com o nível especificado.
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @author Carlos
	 */
	public void carregarDados(AtividadeComplementarVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((AtividadeComplementarVO) obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((AtividadeComplementarVO) obj, resultado, usuario);			
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT atividadeComplementar.codigo, qtdeHoraComplementar, dataAtividade, atividadeComplementar.ano, atividadeComplementar.semestre, atividadeComplementar.conteudo, ");
		// Dados da Turma
		sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", ");
		// Dados da Disciplina
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		// Dados do Responsavel
		sb.append(" usuario.codigo AS \"responsavel.codigo\", usuario.nome AS \"responsavel.nome\", ");
		// Dados RegistroAula
		sb.append(" registroAula.codigo AS \"registroAula.codigo\" ");
		sb.append(" FROM atividadeComplementar ");
		sb.append(" INNER JOIN turma ON turma.codigo = atividadecomplementar.turma ");
		sb.append(" INNER JOIN disciplina ON disciplina.codigo = atividadeComplementar.disciplina ");
		sb.append(" INNER JOIN registroAula ON registroAula.codigo = atividadeComplementar.registroAula ");
		sb.append(" INNER JOIN usuario ON usuario.codigo = atividadeComplementar.responsavel ");
		return sb;
	}

	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT atividadeComplementar.codigo, qtdeHoraComplementar, dataAtividade, atividadeComplementar.ano, atividadeComplementar.semestre, atividadeComplementar.conteudo,  ");
		// Dados da Turma
		sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", ");
		sb.append(" turma.anual as \"turma.anual\", turma.semestral as \"turma.semestral\", turma.turmaagrupada as \"turma.turmaagrupada\", turma.subturma as \"turma.subturma\", turma.tiposubturma as \"turma.tiposubturma\", ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.nivelEducacional AS \"curso.nivelEducacional\", ");
		sb.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");
		sb.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Disciplina
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		// Dados do Responsavel
		sb.append(" usuario.codigo AS \"responsavel.codigo\", usuario.nome AS \"responsavel.nome\", ");
		// Dados RegistroAula
		sb.append(" registroAula.codigo AS \"registroAula.codigo\" ");
		sb.append(" FROM atividadeComplementar ");
		sb.append(" INNER JOIN turma ON turma.codigo = atividadecomplementar.turma ");
		sb.append(" INNER JOIN disciplina ON disciplina.codigo = atividadeComplementar.disciplina ");
		sb.append(" INNER JOIN registroAula ON registroAula.codigo = atividadeComplementar.registroAula ");
		sb.append(" INNER JOIN usuario ON usuario.codigo = atividadeComplementar.responsavel ");
		sb.append(" LEFT JOIN curso ON curso.codigo = turma.curso ");
		sb.append(" INNER JOIN turno ON turno.codigo = turma.turno ");
		sb.append(" LEFT JOIN periodoLetivo ON periodoletivo.codigo = turma.periodoLetivo ");
		return sb;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (AtividadeComplementar.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (atividadeComplementar.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private void montarDadosBasico(AtividadeComplementarVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setQtdeHoraComplementar(dadosSQL.getInt("qtdeHoraComplementar"));
		obj.setDataAtividade(dadosSQL.getDate("dataAtividade"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setConteudo(dadosSQL.getString("conteudo"));
		// Dados Turma
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		// Dados Disciplina
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
		// Dados RegistroAula
		obj.getRegistroAulaVO().setCodigo(dadosSQL.getInt("registroAula.codigo"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
	}

	private void montarDadosCompleto(AtividadeComplementarVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setQtdeHoraComplementar(dadosSQL.getInt("qtdeHoraComplementar"));
		obj.setDataAtividade(dadosSQL.getDate("dataAtividade"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setConteudo(dadosSQL.getString("conteudo"));
		// Dados Turma
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.getTurmaVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getTurmaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getTurmaVO().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
		obj.getTurmaVO().getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getTurmaVO().getTurno().setNome(dadosSQL.getString("turno.nome"));
		obj.getTurmaVO().setAnual(dadosSQL.getBoolean("turma.anual"));
		obj.getTurmaVO().setSemestral(dadosSQL.getBoolean("turma.semestral"));
		obj.getTurmaVO().setTurmaAgrupada(dadosSQL.getBoolean("turma.turmaAgrupada"));
		obj.getTurmaVO().setSubturma(dadosSQL.getBoolean("turma.subturma"));
		if(obj.getTurmaVO().getSubturma()) {
			obj.getTurmaVO().setTipoSubTurma(TipoSubTurmaEnum.valueOf(dadosSQL.getString("turma.tiposubturma")));
		}
		obj.getTurmaVO().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
		obj.getTurmaVO().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
		// Dados Disciplina
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
		// Dados RegistroAula
		obj.getRegistroAulaVO().setCodigo(dadosSQL.getInt("registroAula.codigo"));
		obj.getRegistroAulaVO().setTurma(obj.getTurmaVO());
		obj.getRegistroAulaVO().setDisciplina(obj.getDisciplinaVO());
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
		obj.setListaAtividadeComplementarMatriculaVOs(getFacadeFactory().getAtividadeComplementarMatriculaFacade().consultarAtividadeComplementarMatriculaPorAtividadeComplementar(obj.getCodigo(), false, usuarioVO));
		getFacadeFactory().getRegistroAulaFacade().carregarDados(obj.getRegistroAulaVO(), NivelMontarDados.TODOS, usuarioVO, false, true, usuarioVO.getIsApresentarVisaoProfessor(), "", "", "", obj.getTurmaVO().getTurmaAgrupada(), false, true);
		obj.getRegistroAulaVO().setDataOriginal(obj.getRegistroAulaVO().getData());
	}

	public List<AtividadeComplementarVO> consultaRapidaPorTurma(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(turma.identificadorTurma)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<AtividadeComplementarVO> consultaRapidaPorProfessor(Integer professor, TurmaVO turma, Integer disciplina, String ano, String semestre , int limit, int offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE registroAula.professor = ").append(professor);
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and registroaula.disciplina = ").append(disciplina);
		}
		if(Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and registroaula.turma = ").append(turma.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and (((turma.anual or turma.semestral) and atividadeComplementar.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and ((turma.semestral and atividadeComplementar.semestre = '").append(semestre).append("') or turma.anual = false or turma.semestral = false) ");
		}
		sqlStr.append(" ORDER BY registroAula.data desc, turma.identificadorTurma ");
		if(limit > 0) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}
	
	@Override
	public Integer consultaTotalPorProfessor(Integer professor, TurmaVO turma, Integer disciplina, String ano, String semestre) throws Exception {		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(distinct atividadeComplementar.codigo) as qtde ");
		sqlStr.append(" FROM atividadeComplementar ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = atividadecomplementar.turma ");
		sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = atividadeComplementar.disciplina ");
		sqlStr.append(" INNER JOIN registroAula ON registroAula.codigo = atividadeComplementar.registroAula ");		
		sqlStr.append(" WHERE registroAula.professor = ").append(professor);
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and registroaula.disciplina = ").append(disciplina);
		}
		if(Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and registroaula.turma = ").append(turma.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and (((turma.anual or turma.semestral) and atividadeComplementar.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and ((turma.semestral and atividadeComplementar.semestre = '").append(semestre).append("') or turma.anual = false or turma.semestral = false) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public List<AtividadeComplementarVO> consultaRapidaPorDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(disciplina.nome)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY disciplina.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<AtividadeComplementarVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<AtividadeComplementarVO> vetResultado = new ArrayList<AtividadeComplementarVO>(0);
		while (tabelaResultado.next()) {
			AtividadeComplementarVO obj = new AtividadeComplementarVO();
			montarDadosBasico(obj, tabelaResultado, usuarioVO);
			vetResultado.add(obj);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return AtividadeComplementar.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		AtividadeComplementar.idEntidade = idEntidade;
	}

	public List<AtividadeComplementarVO> consultar(String campoConsulta, String valorConsulta, String ano, String semestre, int limit, int offset, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuarioVO);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();		
		if(campoConsulta.equals("turma")) {
			sqlStr.append("WHERE sem_acentos(lower(turma.identificadorTurma)) ilike(sem_acentos(?))");
			
		}else {
			sqlStr.append("WHERE sem_acentos(lower(disciplina.nome)) ilike(sem_acentos(?))");
			
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and (((turma.anual or turma.semestral) and atividadeComplementar.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and ((turma.semestral and atividadeComplementar.semestre = '").append(semestre).append("') or turma.anual = false or turma.semestral = false) ");
		}
		if(campoConsulta.equals("turma")) {
			sqlStr.append(" ORDER BY turma.identificadorTurma, registroAula.data desc ");
		}else {
			sqlStr.append(" ORDER BY disciplina.nome, registroAula.data desc ");
		}
		if(limit > 0) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado, usuarioVO);
		
	}
	
	@Override
	public Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, String ano, String semestre) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(distinct atividadeComplementar.codigo) as qtde ");
		sqlStr.append(" FROM atividadeComplementar ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = atividadecomplementar.turma ");
		sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = atividadeComplementar.disciplina ");
		sqlStr.append(" INNER JOIN registroAula ON registroAula.codigo = atividadeComplementar.registroAula ");			
		if(campoConsulta.equals("turma")) {
			sqlStr.append("WHERE sem_acentos(lower(turma.identificadorTurma)) ilike(sem_acentos(?))");
			
		}else {
			sqlStr.append("WHERE sem_acentos(lower(disciplina.nome)) ilike(sem_acentos(?))");
			
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and (((turma.anual or turma.semestral) and atividadeComplementar.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and ((turma.semestral and atividadeComplementar.semestre = '").append(semestre).append("') or turma.anual = false or turma.semestral = false) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
		
	}
}
