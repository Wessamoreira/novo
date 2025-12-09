package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

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

import controle.arquitetura.ControleConcorrencia;
import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.ChoqueHorarioVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisponibilidadeHorarioTurmaProfessorVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaSemanalVO;
import negocio.comuns.academico.HorarioTurmaProfessorDisciplinaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ProgramacaoAulaResumoDisciplinaSemanaVO;
import negocio.comuns.academico.ProgramacaoAulaResumoSemanaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.academico.enumeradores.TipoValidacaoChoqueHorarioEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.ControleConcorrenciaHorarioTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioTurmaInterfaceFacade;

/**
 * 
 * @author rodrigo
 */
@SuppressWarnings("static-access")
@Repository
@Scope("singleton")
@Lazy
public class HorarioTurma extends ControleAcesso implements HorarioTurmaInterfaceFacade {

	private static final long serialVersionUID = -831506333511534467L;

	protected static String idEntidade;

	public HorarioTurma() throws Exception {
		super();
		setIdEntidade("ProgramacaoAula");
	}

	public HorarioTurmaVO novo() throws Exception {
		Cancelamento.incluir(getIdEntidade());
		HorarioTurmaVO obj = new HorarioTurmaVO();

		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioTurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			obj.setUpdated(new Date());
			HorarioTurma.incluir(getIdEntidade(), true, usuario);
			validarDados(obj, usuario);
			final String sql = "INSERT INTO HorarioTurma (turma,semestrevigente,anovigente, observacao, updated, datacriacao) VALUES (?,?,?,?,?,?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getTurma().getCodigo().intValue());
					sqlInserir.setString(2, obj.getSemestreVigente());
					sqlInserir.setString(3, obj.getAnoVigente());
					sqlInserir.setString(4, obj.getObservacao());
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getUpdated()));
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(new Date()));
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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		} finally {

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void adicionarProfessorManipularControleConcorrencia(HorarioTurmaVO horarioTurmaVO, PessoaVO professor, String acaoRealizada, UsuarioVO usuario) throws Exception {
		ControleConcorrenciaHorarioTurmaVO concorrenciaHorarioTurmaVO = new ControleConcorrenciaHorarioTurmaVO();
		concorrenciaHorarioTurmaVO.setCodigoProfessor(professor.getCodigo());
		concorrenciaHorarioTurmaVO.setNomeProfessor(professor.getNome());
		concorrenciaHorarioTurmaVO.setNomeMetodo(acaoRealizada);
		concorrenciaHorarioTurmaVO.setUsuarioVO(usuario);
		concorrenciaHorarioTurmaVO.setData(new Date());
		ControleConcorrencia.adicionarProfessorListaProfessorHorario(concorrenciaHorarioTurmaVO);
		horarioTurmaVO.getControleConcorrenciaHorarioTurmaVOs().add(concorrenciaHorarioTurmaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerProfessorManipularControleConcorrencia(HorarioTurmaVO horarioTurmaVO) throws Exception {
		ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioTurmaVO obj, List<HorarioProfessorVO> horarioProfessorVOs, boolean zerarListaConcorrencia, UsuarioVO usuario) throws Exception {
		try {
			obj.setUpdated(new Date());
			HorarioTurma.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE HorarioTurma SET turma=?, semestrevigente = ? , anovigente = ?, observacao = ?, updated = ? WHERE ((codigo=?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getTurma().getCodigo().intValue());
					sqlAlterar.setString(2, obj.getSemestreVigente());
					sqlAlterar.setString(3, obj.getAnoVigente());
					sqlAlterar.setString(4, obj.getObservacao());
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getUpdated()));
					sqlAlterar.setInt(6, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarObservacao(final HorarioTurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			obj.setUpdated(new Date());
			final String sql = "UPDATE HorarioTurma SET observacao = ?, updated = ? WHERE (codigo=?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getObservacao());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getUpdated()));
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSeNecessarioProfessoresTitularDisciplinaTurma(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws Exception {
		for (HorarioTurmaProfessorDisciplinaVO obj : horarioTurmaVO.getListaProfessorDisciplina()) {
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().incluirProfessorTitularDisciplinaTurma(obj.getProfessor(), obj.getDisciplina(), horarioTurmaVO.getTurma(), horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDataFinalizacaoEConfirmacaoTurma(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws Exception {
		Date dataAbertura = null;
		Date dataFinal = null;
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			if (horarioTurmaDiaVO.getIsAulaProgramada()) {
				if (dataAbertura == null || dataAbertura.compareTo(horarioTurmaDiaVO.getData()) > 0) {
					dataAbertura = horarioTurmaDiaVO.getData();
				}
				if (dataFinal == null || dataFinal.compareTo(horarioTurmaDiaVO.getData()) < 0) {
					dataFinal = horarioTurmaDiaVO.getData();
				}
			}
		}
		TurmaAberturaVO turmaAberturaVO = new TurmaAberturaVO();
		turmaAberturaVO.setData(dataAbertura);
		turmaAberturaVO.getTurma().setCodigo(horarioTurmaVO.getTurma().getCodigo());
		turmaAberturaVO.setUsuario(usuario);
		TurmaAberturaVO ultimaTurmaAbertura = getFacadeFactory().getTurmaAberturaFacade().consultarUltimaTurmaAberturaPorTurma(horarioTurmaVO.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (dataAbertura != null && dataFinal != null) {
			if (ultimaTurmaAbertura == null || ultimaTurmaAbertura.isNovoObj()) {
				turmaAberturaVO.setSituacao("CO");
				getFacadeFactory().getTurmaAberturaFacade().incluir(turmaAberturaVO);
			} else if (!ultimaTurmaAbertura.getData_Apresentar().equals(Uteis.getData(dataAbertura)) && ultimaTurmaAbertura.getData().after(dataAbertura)) {
				if (ultimaTurmaAbertura.getSituacao().equals("CO")) {
					ultimaTurmaAbertura.setDataAdiada(dataAbertura);
					getFacadeFactory().getTurmaAberturaFacade().alterar(ultimaTurmaAbertura);
					turmaAberturaVO.setSituacao("AD");
				} else {
					turmaAberturaVO.setSituacao("CO");
				}
				getFacadeFactory().getTurmaAberturaFacade().incluir(turmaAberturaVO);
			} else if (!ultimaTurmaAbertura.getData_Apresentar().equals(Uteis.getData(dataAbertura)) && ultimaTurmaAbertura.getData().before(dataAbertura)) {
				if (ultimaTurmaAbertura.getSituacao().equals("CO")) {
					ultimaTurmaAbertura.setDataAdiada(dataAbertura);
					getFacadeFactory().getTurmaAberturaFacade().alterar(ultimaTurmaAbertura);
					turmaAberturaVO.setSituacao("AN");
				} else {
					turmaAberturaVO.setSituacao("CO");
				}
				getFacadeFactory().getTurmaAberturaFacade().incluir(turmaAberturaVO);
			}
		} else {
			if (ultimaTurmaAbertura == null || ultimaTurmaAbertura.isNovoObj() || !ultimaTurmaAbertura.getSituacao().equals("AC")) {
				turmaAberturaVO.setSituacao("AC");
				getFacadeFactory().getTurmaAberturaFacade().incluir(turmaAberturaVO);
			}
		}
		getFacadeFactory().getTurmaFacade().alterarDataPrevisaoFinalizacao(horarioTurmaVO.getTurma().getCodigo(), dataAbertura, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(HorarioTurmaVO obj, UsuarioVO usuario) throws Exception {
		List<PessoaVO> professores = null;
		try {
			HorarioTurma.excluir(getIdEntidade(), true, usuario);
			if (!obj.getHorarioTurmaDiaVOs().isEmpty()) {
				List<RegistroAulaVO> registroAulaVOs = getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorTurmaDisciplinaPeriodo(obj.getTurma().getCodigo(), 0, obj.getHorarioTurmaDiaVOs().get(0).getData(), obj.getHorarioTurmaDiaVOs().get(obj.getHorarioTurmaDiaVOs().size() - 1).getData(), obj.getAnoVigente(), obj.getSemestreVigente(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, Boolean.FALSE);
				if (!registroAulaVOs.isEmpty()) {
					throw new Exception("Não é possível excluir essa programação de aula, pois existe(m) registro(s) de aula(s) cadastrado(s) para a turma, a alteração ou exclusão da programação de aula pode ocasionar erros futuros! Caso seja necessário realizar a operação é necessário excluir todos os registros de aula dessa turma!");
				}
				Date dataInicio = obj.getHorarioTurmaDiaVOs().get(0).getData();
				Date dataFim = obj.getHorarioTurmaDiaVOs().get(obj.getHorarioTurmaDiaVOs().size() - 1).getData();
				professores = getFacadeFactory().getHorarioTurmaFacade().executarMontagemListaProfessorComAulaProgramadaTurma(obj);
				for (PessoaVO professor : professores) {
					adicionarProfessorManipularControleConcorrencia(obj, professor, "Exclusão Horario Turma", usuario);
					List<DisciplinaVO> disciplinaVOs = realizarObtencaoDisciplinaLecionadaProfessor(obj, professor.getCodigo());
					for (DisciplinaVO disciplinaVO : disciplinaVOs) {
						getFacadeFactory().getHorarioProfessorFacade().realizarExclusaoHorarioProfessor(obj, professor.getCodigo(), disciplinaVO.getCodigo(), dataInicio, dataFim, 0, true, registroAulaVOs, usuario, false);
					}
				}
			}
			String sql = "DELETE FROM HorarioTurma WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getHorarioTurmaProfessorDisciplinaFacade().excluirHorarioTurmaProfessorDisciplinas(obj.getCodigo());
			getFacadeFactory().getHorarioTurmaDiaFacade().excluirHorarioTurmaDias(obj.getCodigo(), usuario);
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluirTodosRegistrosTurmaComBaseNaProgramacaoAula(obj.getTurma().getCodigo(), obj.getSemestreVigente(), obj.getAnoVigente(), usuario);
			getFacadeFactory().getHorarioTurmaLogFacade().executarLogExclusaoProgramacaoAula(obj, usuario);
			obj.getHorarioTurmaDiaVOs().clear();
			incluirDataFinalizacaoEConfirmacaoTurma(obj, usuario);
		} catch (Exception e) {
			throw e;
		} finally {
			professores = null;

		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirHorarioTurmaPorAlteracaoGradeCurricularCursoIntegral(TurmaVO turma, DisciplinaVO disciplina, UsuarioVO usuario) throws Exception {		
		try {
			if(getFacadeFactory().getRegistroAulaFacade().existeRegistroAula(turma, disciplina)){
				throw new Exception("Não é possível excluir essa disciplina "+disciplina.getNome()+" da grade, pois a mesma tem aula registrada para essa turma.");
			}
			if(consultarSeExisteHorarioTurmaPorTurmaPorDisciplinaDiferente(turma.getCodigo(), disciplina.getCodigo(), "", "", false, usuario)){
				getFacadeFactory().getHorarioProfessorDiaItemFacade().excluirPorTurmaPorDisciplina(turma.getCodigo(), disciplina.getCodigo(), usuario);
				getFacadeFactory().getHorarioTurmaDiaItemFacade().excluirPorTurmaPorDisciplina(turma.getCodigo(), disciplina.getCodigo(), usuario);
			}else{
				HorarioTurmaVO obj = consultarPorCodigoTurmaUnico(turma.getCodigo(), "", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if(Uteis.isAtributoPreenchido(obj)){
					inicializarDadosHorarioTurmaPorHorarioTurma(obj, false, usuario);
					excluir(obj, usuario);	
				}
			}
		} catch (Exception e) {
			throw e;
		} 
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHorarioTurmaPorAlteracaoGradeCurricularCursoIntegral(TurmaVO turma, TurmaDisciplinaVO turmaDisciplina, UsuarioVO usuario) throws Exception {		
		try {
			getFacadeFactory().getHorarioProfessorDiaItemFacade().atualizarNovaDisciplinaHorarioProfessorDiaItemPorAlteracaoGradeCurricularIntegral(turma.getCodigo(), turmaDisciplina.getDisciplina().getCodigo(), turmaDisciplina.getOperacaoMatrizCurricular(), usuario);
			getFacadeFactory().getHorarioTurmaDiaItemFacade().atualizarNovaDisciplinaHorarioTurmaDiaItemPorAlteracaoGradeCurricularIntegral(turma.getCodigo(), turmaDisciplina.getDisciplina().getCodigo(), turmaDisciplina.getOperacaoMatrizCurricular(), usuario);
			getFacadeFactory().getRegistroAulaFacade().atualizarNovaDisciplinaRegistroAulaPorAlteracaoGradeCurricularIntegral(turma.getCodigo(), turmaDisciplina.getDisciplina().getCodigo(), turmaDisciplina.getOperacaoMatrizCurricular(), usuario);
		} catch (Exception e) {
			throw e;
		} 
	}
	
	private Boolean consultarSeExisteHorarioTurmaPorTurmaPorDisciplinaDiferente(Integer turma, Integer disciplina, String semestreVigente, String anoVigente, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT count(horarioturma.codigo) as qtd ");
		sqlStr.append("FROM HorarioTurma ");
		sqlStr.append("INNER JOIN Turma ON HorarioTurma.Turma = turma.codigo ");
		sqlStr.append("INNER JOIN HorarioTurmadia ON HorarioTurmadia.HorarioTurma = HorarioTurma.codigo ");
		sqlStr.append("INNER JOIN HorarioTurmadiaitem ON HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo ");
		sqlStr.append("WHERE turma.codigo = ").append(turma).append(" ");
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append("AND HorarioTurmadiaitem.disciplina != ").append(disciplina).append(" ");
		}
		if(Uteis.isAtributoPreenchido(anoVigente) &&  Uteis.isAtributoPreenchido(semestreVigente)){
			sqlStr.append(" AND ((turma.anual and anovigente = '").append(anoVigente).append("') ");
			sqlStr.append(" or (turma.semestral and anovigente = '").append(anoVigente).append("' and  semestrevigente = '").append(semestreVigente).append("') ");
			sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO); 
	}

	public List<HorarioTurmaVO> consultarHorarioTurmaPeloCodigoTurmaTrazendoTurmaAgrupada(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		// StringBuilder sqlStr = new
		// StringBuilder(" select horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		StringBuilder sqlStr = new StringBuilder(" select horarioturma.codigo, horarioturma.turma");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		sqlStr.append(" from Turma left join horarioturma on horarioturma.turma = turma.codigo");
		// sqlStr.append(" where  turma.codigo = ").append(turma).append(" group by horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		sqlStr.append(" where  turma.codigo = ").append(turma).append(" group by horarioturma.codigo, horarioturma.turma");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		sqlStr.append(" union all");
		// sqlStr.append(" select horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		sqlStr.append(" select horarioturma.codigo, horarioturma.turma");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		sqlStr.append(" from horarioturma inner join Turma as ta on horarioturma.turma = ta.codigo");
		sqlStr.append(" inner join TurmaAgrupada on ta.codigo = TurmaAgrupada.turmaOrigem");
		sqlStr.append(" inner join Turma as tu on tu.codigo = TurmaAgrupada.turma");
		sqlStr.append(" where tu.codigo = ").append(turma);
		// sqlStr.append(" group by horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		sqlStr.append(" group by horarioturma.codigo, horarioturma.turma");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List<HorarioTurmaVO> consultarHorarioTurmaPelaMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		// StringBuilder sqlStr = new
		// StringBuilder("select horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		StringBuilder sqlStr = new StringBuilder("select horarioturma.codigo, horarioturma.turma, horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.observacao");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		sqlStr.append(" from matricula inner join matriculaPeriodo on matriculaPeriodo.matricula =  matricula.matricula");
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo");
		sqlStr.append(" inner join Turma on matriculaPeriodoTurmaDisciplina.turma = turma.codigo");
		sqlStr.append(" inner join horarioturma on horarioturma.turma = turma.codigo");
		// sqlStr.append(" where  matricula.matricula = '").append(matricula).append("' and matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' group by horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		sqlStr.append(" where  matricula.matricula = '").append(matricula).append("' and matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ");
		sqlStr.append(" AND ( ( horarioturma.anovigente =   CAST((SELECT EXTRACT (YEAR FROM (SELECT NOW()))) AS TEXT ) ");
		sqlStr.append(" AND horarioturma.semestrevigente =  CAST((SELECT CASE WHEN ( SELECT EXTRACT (MONTH FROM (SELECT NOW())) > 7 ) THEN '2' ELSE '1' END) as TEXT) ) ");
		sqlStr.append(" OR ( (horarioturma.anovigente = '' or  horarioturma.anovigente is null) and (horarioturma.semestrevigente = '' or horarioturma.semestrevigente is null) ) ) ");
		sqlStr.append(" GROUP BY horarioturma.codigo, horarioturma.turma, horarioturma.anovigente,horarioturma.semestrevigente, horarioturma.observacao  ");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		sqlStr.append(" union all");
		// sqlStr.append(" select horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		sqlStr.append(" select horarioturma.codigo, horarioturma.turma, horarioturma.anovigente,horarioturma.semestrevigente, horarioturma.observacao ");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		sqlStr.append(" from horarioturma inner join Turma as ta on horarioturma.turma = ta.codigo");
		sqlStr.append(" inner join TurmaAgrupada on ta.codigo = TurmaAgrupada.turmaOrigem");
		sqlStr.append(" inner join Turma as tu on tu.codigo = TurmaAgrupada.turma");
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on tu.codigo =  matriculaPeriodoTurmaDisciplina.turma  ");
		sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo  and matriculaPeriodo.situacaoMatriculaPeriodo = 'AT'");
		sqlStr.append(" inner join matricula on matriculaPeriodo.matricula =  matricula.matricula and matricula.matricula = '").append(matricula).append("'	");
		// sqlStr.append(" group by horarioturma.codigo, horarioturma.turma, horarioturma.segunda, horarioturma.terca, horarioturma.quarta, horarioturma.quinta,");
		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append(" AND ( ( matriculaPeriodo.ano =   CAST((SELECT EXTRACT (YEAR FROM (SELECT NOW()))) AS TEXT )  ");
		sqlStr.append(" AND matriculaPeriodo.semestre =  CAST((SELECT CASE WHEN ( SELECT EXTRACT (MONTH FROM (SELECT NOW())) > 7 ) THEN '2' ELSE '1' END) as TEXT) ) ");
		sqlStr.append(" OR ( matriculaPeriodo.ano = '' and matriculaPeriodo.semestre = '')  ) ");
		sqlStr.append(" AND ( (horarioturma.anovigente =   CAST((SELECT EXTRACT (YEAR FROM (SELECT NOW()))) AS TEXT ) ");
		sqlStr.append(" AND horarioturma.semestrevigente =  CAST((SELECT CASE WHEN ( SELECT EXTRACT (MONTH FROM (SELECT NOW())) > 7 ) THEN '2' ELSE '1' END) as TEXT) ");
		sqlStr.append(" ) OR ( (horarioturma.anovigente = '' or  horarioturma.anovigente is null) and (horarioturma.semestrevigente = '' or horarioturma.semestrevigente is null)) ) ");
		sqlStr.append(" group by horarioturma.codigo, horarioturma.turma, horarioturma.anovigente,horarioturma.semestrevigente,  horarioturma.observacao ");
		// sqlStr.append(" horarioturma.sexta, horarioturma.sabado, horarioturma.domingo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<HorarioTurmaVO> consultarPorCodigoTurma(Integer codigoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurma.* FROM HorarioTurma, Turma WHERE HorarioTurma.Turma = turma.codigo  and turma.codigo  = " + codigoTurma.intValue() + " ORDER BY  Turma.identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Consulta afim de verificar a existï¿½ncia de um horario de turma para
	 * mesma turma
	 * 
	 * @param codigoTurma
	 * @param semestreVigente
	 * @param anoVigente
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * 
	 * @return HorarioTurmaVO
	 * 
	 * @author Danilo
	 * 
	 * @since 14.02.2011
	 */
	public HorarioTurmaVO consultarPorCodigoTurmaUnico(Integer codigoTurma, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT horarioturma.codigo,horarioturma.turma, horarioturma.anovigente,horarioturma.observacao, horarioturma.semestrevigente, Turma.identificadorTurma, horarioturma.updated FROM HorarioTurma INNER JOIN Turma ON  HorarioTurma.Turma = turma.codigo ");
		sqlStr.append(" WHERE turma.codigo = ").append(codigoTurma.intValue());
		sqlStr.append(" AND ((turma.anual and anovigente = '").append(anoVigente).append("') ");
		sqlStr.append(" or (turma.semestral and anovigente = '").append(anoVigente).append("' and  semestrevigente = '").append(semestreVigente).append("') ");
		sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
		sqlStr.append(" ORDER BY Turma.identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new HorarioTurmaVO();
		}
		sqlStr = null;
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public HorarioTurmaVO consultarPorCodigoTurmaAgrupadaDisciplinaUnico(Integer turma, Integer disciplina, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT horarioturma.codigo,horarioturma.turma, horarioturma.anovigente,horarioturma.observacao, ");
		sqlStr.append("horarioturma.semestrevigente, Turma.identificadorTurma, horarioturma.updated ");
		sqlStr.append("FROM HorarioTurma ");
		sqlStr.append("INNER JOIN Turma ON HorarioTurma.Turma = turma.codigo ");
		sqlStr.append("INNER JOIN HorarioTurmadia ON HorarioTurmadia.HorarioTurma = HorarioTurma.codigo ");
		sqlStr.append("INNER JOIN HorarioTurmadiaitem ON HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo ");
		sqlStr.append("WHERE turma.codigo in (SELECT ta.turmaorigem FROM turmaagrupada ta ");
		sqlStr.append("INNER JOIN turmadisciplina td ON td.turma = ta.turma ");
		sqlStr.append("WHERE ta.turma = ").append(turma.intValue()).append(" ");
		if (disciplina != null && !disciplina.equals(0)) {
			sqlStr.append("AND td.disciplina = ").append(disciplina.intValue()).append(" ");
		}
		sqlStr.append(" )");
		if (disciplina != null && !disciplina.equals(0)) {
			sqlStr.append("AND HorarioTurmadiaitem.disciplina = ").append(disciplina.intValue()).append(" ");
		}
		sqlStr.append(" AND ((turma.anual and anovigente = '").append(anoVigente).append("') ");
		sqlStr.append(" or (turma.semestral and anovigente = '").append(anoVigente).append("' and  semestrevigente = '").append(semestreVigente).append("') ");
		sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");				
		sqlStr.append("ORDER BY Turma.identificadorTurma limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new HorarioTurmaVO();
		}
		sqlStr = null;
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Mï¿½todo responsï¿½vel por trazer o HorarioTurma da turma citada
	 * 
	 * @param valorConsulta
	 *            Nome identificador da turma
	 * @param unidadeEnsino
	 *            Unidade de Ensino da turma
	 * @param semestreVigente
	 *            Semestre de vigï¿½ncia do horario turma
	 * @param anoVigente
	 *            Ano de vigï¿½ncia do horario turma
	 * @param nivelMontarDados
	 *            Nivel de montagem dos dados
	 * 
	 * @return List HorarioTurma
	 * @author Danilo
	 * 
	 * @since 11.02.2011
	 * 
	 */
	public List<HorarioTurmaVO> consultarPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" SELECT DISTINCT HorarioTurma.codigo,HorarioTurma.turma,Turma.identificadorTurma,anovigente,semestrevigente, HorarioTurma.observacao, horarioturma.updated  ");
		sqlStr.append(" FROM horarioturma ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = horarioturma.turma ");
		sqlStr.append(" WHERE UPPER(turma.identificadorTurma)  like('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (!semestreVigente.isEmpty()) {
			sqlStr.append(" AND semestrevigente = '").append(semestreVigente).append("' ");
		}
		if (!anoVigente.isEmpty()) {
			sqlStr.append(" AND anovigente = '").append(anoVigente).append("' ");
		}
		sqlStr.append(" ORDER BY Turma.identificadorTurma ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List<HorarioTurmaVO> consultarPorIdentificadorTurmaTurmaMontagemCompleta(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" SELECT DISTINCT HorarioTurma.codigo,HorarioTurma.turma,Turma.identificadorTurma,anovigente,semestrevigente, HorarioTurma.observacao, horarioturma.updated  ");
		sqlStr.append(" FROM horarioturma ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = horarioturma.turma ");
		sqlStr.append(" WHERE UPPER(turma.identificadorTurma)  like('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (!semestreVigente.isEmpty()) {
			sqlStr.append(" AND semestrevigente = '").append(semestreVigente).append("' ");
		}
		if (!anoVigente.isEmpty()) {
			sqlStr.append(" AND anovigente = '").append(anoVigente).append("' ");
		}
		sqlStr.append(" ORDER BY Turma.identificadorTurma ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public HorarioTurmaVO consultarPorHorarioTurmaPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurma.* FROM HorarioTurma inner join Turma on HorarioTurma.turma = Turma.codigo WHERE upper(Turma.identificadorTurma) = '" + valorConsulta.toUpperCase() + "' ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY Turma.identificadorTurma limit 1 ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new HorarioTurmaVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Mï¿½todo responsï¿½vel por buscar um horï¿½rio para turma baseado no ano
	 * e semestre
	 * 
	 * @param valorConsulta
	 *            - Identificador da turma
	 * @param unidadeEnsino
	 * @param semestreVigente
	 * @param anoVigente
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * 
	 * @return HorarioTurmaVO
	 * 
	 * @author Danilo
	 * @since 14.02.2011
	 * 
	 */
	public HorarioTurmaVO consultarPorHorarioTurmaPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT HorarioTurma.turma, HorarioTurma.codigo, HorarioTurma.anovigente, HorarioTurma.semestrevigente, HorarioTurma.observacao, HorarioTurma.updated ");
		sqlStr.append(" FROM HorarioTurma inner join Turma on HorarioTurma.turma = Turma.codigo ");
		sqlStr.append(" WHERE upper(Turma.identificadorTurma) =  '").append(valorConsulta.toUpperCase()).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" AND semestreVigente = '").append(semestreVigente).append("' ");
		sqlStr.append(" AND anoVigente = '").append(anoVigente).append("' ");
		sqlStr.append(" ORDER BY Turma.identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new HorarioTurmaVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<HorarioTurmaVO> consultarPorCodigo(Integer codigoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM HorarioTurma WHERE codigo  >= " + codigoTurma.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public static List<HorarioTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<HorarioTurmaVO> vetResultado = new ArrayList<HorarioTurmaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsï¿½vel por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisponibilidadeHorarioVO</code>.
	 * 
	 * @return O objeto da classe <code>DisponibilidadeHorarioVO</code> com os
	 *         dados devidamente montados.
	 */
	public static HorarioTurmaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioTurmaVO horarioTurma = new HorarioTurmaVO();
		horarioTurma.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		horarioTurma.getTurma().setCodigo(new Integer(tabelaResultado.getInt("turma")));
		horarioTurma.setAnoVigente(tabelaResultado.getString("anovigente"));
		horarioTurma.setSemestreVigente(tabelaResultado.getString("semestrevigente"));
		horarioTurma.setObservacao(tabelaResultado.getString("observacao"));
		horarioTurma.setNovoObj(false);
		horarioTurma.setUpdated(tabelaResultado.getTimestamp("updated"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return horarioTurma;
		}
		montarDadosTurma(horarioTurma, nivelMontarDados, usuario);
		horarioTurma.setTurno(horarioTurma.getTurma().getTurno());
		montarDadosTurno(horarioTurma, Uteis.NIVELMONTARDADOS_TODOS, usuario);

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return horarioTurma;
		}
		getFacadeFactory().getHorarioTurmaFacade().carregarDadosHorarioTurma(horarioTurma, null, null, usuario);

		return horarioTurma;
	}

	public static void montarDadosTurma(HorarioTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), usuario);
	}

	public static void montarDadosTurno(HorarioTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurma().getTurno().getCodigo(), nivelMontarDados, usuario));
	}

	public HorarioTurmaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM HorarioTurma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (HorarioTurma).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public HorarioTurmaVO consultarHorarioTurmaPorCodigo(Integer codigoPrm, boolean acesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), acesso, usuario);
		String sqlStr = "SELECT * FROM HorarioTurma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (HorarioTurma).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public void setIdEntidade(String idEntidade) {
		HorarioTurma.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return HorarioTurma.idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public HorarioProfessorVO executarAtualizacaoDisponibilidadeHorarioProfessorProgramacaoAulaDiaADiaAvulso(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws ConsistirException, Exception {
		validarDadosProgramacaoHorario(horarioTurmaVO, true);
		return executarAtualizacaoDisponibilidadeHorarioProfessor(horarioTurmaVO, true, usuario);
	}

	public static void validarDadosProgramacaoHorario(HorarioTurmaVO obj, Boolean avulso) throws ConsistirException {
		if (obj.getDisciplina() == null || obj.getDisciplina().getCodigo() == null || obj.getDisciplina().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo DISCIPLINA (Programação Aula) deve ser informado.");
		}

		if (obj.getProfessor() == null || obj.getProfessor().getCodigo() == null || obj.getProfessor().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo PROFESSOR (Programação Aula) deve ser informado.");
		}
		if ((avulso)) {
			if (obj.getDia() == null && avulso) {
				throw new ConsistirException("O campo DATA AULA (Programação Aula) deve ser informado.");
			}
			if (getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(obj.getTurno(), Uteis.getDiaSemanaEnum(obj.getDia())) == 0 && avulso) {
				throw new ConsistirException("Não está definido no cadastro do turno aulas para " + Uteis.getDiaSemanaEnum(obj.getDia()).getDescricao());
			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public HorarioProfessorVO executarAtualizacaoDisponibilidadeHorarioProfessor(HorarioTurmaVO horarioTurmaVO, Boolean avulso, UsuarioVO usuario) throws Exception {
		HorarioProfessorVO horarioProfessorVO = getFacadeFactory().getHorarioProfessorFacade().consultarRapidaHorarioProfessorTurno(horarioTurmaVO.getProfessor().getCodigo(), horarioTurmaVO.getTurno().getCodigo(), usuario);
		if (horarioProfessorVO.getCodigo().intValue() == 0) {
			horarioProfessorVO = getFacadeFactory().getHorarioProfessorFacade().executarCriacaoHorarioProfessorTurno(horarioTurmaVO.getProfessor(), horarioTurmaVO.getTurno(), usuario);
			getFacadeFactory().getHorarioProfessorFacade().incluir(horarioProfessorVO, usuario);
		}
		if (horarioProfessorVO.getTurno().getCodigo().intValue() == horarioTurmaVO.getTurno().getCodigo().intValue()) {
			if (!avulso) {
				getFacadeFactory().getHorarioProfessorFacade().executarCriacaoDisponibilidadeHorarioProfessor(horarioProfessorVO, horarioTurmaVO.getTurno(), true, usuario);
			} else if (avulso) {
				getFacadeFactory().getHorarioProfessorFacade().executarCriacaoDisponibilidadeHorarioProfessorDiaSemana(horarioProfessorVO, horarioTurmaVO.getTurno(), Uteis.getDiaSemanaEnum(horarioTurmaVO.getDia()), true, usuario);
				montarAulaJaCadastradasProfessorDia(horarioTurmaVO, horarioProfessorVO);
				horarioProfessorVO.montarAulaJaCadastradasProfessorDia(horarioTurmaVO.getDia());
			}
		}
		return horarioProfessorVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void montarAulaJaCadastradasProfessorDia(HorarioTurmaVO horarioTurmaVO, HorarioProfessorVO horarioProfessorVO) throws Exception {
		List<DisponibilidadeHorarioTurmaProfessorVO> objs = horarioProfessorVO.consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(horarioTurmaVO.getDia()));
		for (HorarioTurmaDiaVO obj : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			if (Uteis.getDateHoraFinalDia(obj.getData()).compareTo(Uteis.getDateHoraFinalDia(horarioTurmaVO.getDia())) == 0) {
				for (DisponibilidadeHorarioTurmaProfessorVO item : objs) {
					if (item.getHorarioLivre()) {
						if (obj.getCodigoDisciplina(item.getNrAula()) > 0) {
							item.setHorarioLivre(false);
							item.setProgramarAula(false);
						}
					}
				}
			}
		}
		horarioProfessorVO.adicionarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(horarioTurmaVO.getDia()), objs);
	}

	public void inicializarDadosProgramarAulaDiaDiaAvulso(HorarioTurmaVO horarioTurmaVO) throws ConsistirException {
		// horarioTurmaVO.setTipoHorarioTurma(TipoHorarioTurma.DIARIO);
		validarDadosProgramacaoHorario(horarioTurmaVO, false);
		horarioTurmaVO.setDia(null);
	}

	public HorarioProfessorVO inicializarDadosProgramarAulaDiaDiaPeriodo(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws ConsistirException, Exception {
		// horarioTurmaVO.setTipoHorarioTurma(TipoHorarioTurma.DIARIO);
		validarDadosProgramacaoHorario(horarioTurmaVO, false);
		horarioTurmaVO.setDia(null);
		return executarAtualizacaoDisponibilidadeHorarioProfessor(horarioTurmaVO, false, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> executarAdicionarDadosHorarioTurmaProfessor(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, Boolean avulso, HorarioProfessorVO horarioProfessorVO, Boolean validarHorarioDiaPeriodo, UsuarioVO usuario, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido) throws Exception {
		boolean horarioTurmaIncluida = false;
		Date dataInicio = null;
		Date dataFim = null;
		Date diaBase = null;
		List<FeriadoVO> feriadoVOs = null;
		List<Date> datas = new ArrayList<Date>();
		List<DisponibilidadeHorarioTurmaProfessorVO> disponibilidadeHorarioTurmaProfessorVOs = null;
		Map<String, Integer> qtdeAulaDiaProgramadaProfessor = null;		
		try {
			adicionarProfessorManipularControleConcorrencia(horarioTurmaVO, horarioProfessorVO.getProfessor(), "Inclusão do Professor no Horário da Turma", usuario);
			if (Uteis.isAtributoPreenchido(horarioTurmaVO.getDisciplina().getCodigo())) {
				getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(horarioTurmaVO.getTurma(), horarioTurmaVO.getDisciplina().getCodigo(), horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), true);
			}
			horarioProfessorVO.getHorarioProfessorDiaVOs().clear();
			horarioTurmaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(horarioTurmaVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			if (Uteis.isAtributoPreenchido(horarioTurmaVO.getSala().getCodigo())) {
				horarioTurmaVO.setSala(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(horarioTurmaVO.getSala().getCodigo()));
				horarioTurmaVO.getSala().setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(horarioTurmaVO.getSala().getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
			if (validarHorarioDiaPeriodo) {
				Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = executarVerificarDisponibilidadeHorarioTurmaEHorarioProfessor(horarioTurmaVO, avulso, horarioProfessorVO, usuario, validarHorarioDiaPeriodo, controlaNumeroMaximoAulaProgramadaProfessorDia, numeroAulaMaximaProgramarProfessorDia, liberarProgramacaoAulaProfessorAcimaPermitido);
				if (validacoes != null && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {
					return validacoes;
				}
			}
			if (horarioTurmaVO.isNovoObj()) {
				incluir(horarioTurmaVO, usuario);
				horarioTurmaIncluida = true;
			}
			if (avulso) {
				dataInicio = horarioTurmaVO.getDia();
				dataFim = horarioTurmaVO.getDia();
				diaBase = Uteis.getDateHoraFinalDia(horarioTurmaVO.getDia());
			} else {
				dataInicio = horarioTurmaVO.getDiaInicio();
				dataFim = horarioTurmaVO.getDiaFim();
				diaBase = Uteis.getDateHoraFinalDia(horarioTurmaVO.getDiaInicio());

			}
			feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(Uteis.getDate("01/" + Uteis.getMesData(dataInicio) + "/" + Uteis.getAnoData(dataInicio)), Uteis.getDate(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(dataFim)) + "/" + Uteis.getMesData(dataFim) + "/" + Uteis.getAnoData(dataFim)), horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			while (Uteis.getDateHoraFinalDia(diaBase).compareTo(Uteis.getDateHoraFinalDia(dataFim)) <= 0) {
				FeriadoVO feriadoVO = getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, diaBase);
				if (feriadoVO == null || feriadoVO.getCodigo().intValue() == 0) {
					disponibilidadeHorarioTurmaProfessorVOs = horarioProfessorVO.consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(diaBase));
					for (DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioProfessorVO : disponibilidadeHorarioTurmaProfessorVOs) {
						diaBase = Uteis.getDateHoraFinalDia(diaBase);
						if (disponibilidadeHorarioProfessorVO.getProgramarAula() && disponibilidadeHorarioProfessorVO.getHorarioLivre()) {
							datas.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(diaBase));
							break;
						}
					}
				}
				diaBase = Uteis.getDateHoraFinalDia(Uteis.obterDataFutura(diaBase, 1));
			}
			if(datas.isEmpty() && avulso){
				throw new Exception("Não existe data disponível para programação de aula avulsa. Por Favor verifique os feriados ou a disponibilidade do professor.");
			}else if(datas.isEmpty() && !avulso){
				throw new Exception("Não existe datas disponíveis para programação de aula por período. Por Favor verifique os feriados ou a disponibilidade do professor.");
			}
			if (controlaNumeroMaximoAulaProgramadaProfessorDia && numeroAulaMaximaProgramarProfessorDia > 0 && !liberarProgramacaoAulaProfessorAcimaPermitido) {
				qtdeAulaDiaProgramadaProfessor = getFacadeFactory().getHorarioProfessorDiaFacade().consultarQuantidadeAulaProgramadaProfessorPorData(datas, horarioProfessorVO.getProfessor().getCodigo());
			}
			if (horarioProfessorVO.getHorarioProfessorDiaVOs().isEmpty()) {
				horarioProfessorVO.setHorarioProfessorDiaVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(null, null, horarioTurmaVO.getProfessor().getCodigo(), null, null, dataInicio, dataFim, null, datas, null, null, null));
			}
			StringBuilder resultadoAcao = new StringBuilder();
			// Stopwatch stopWatch = new Stopwatch();
			// stopWatch.start();
			for (Date data : datas) {
				disponibilidadeHorarioTurmaProfessorVOs = horarioProfessorVO.consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(data)));
				FeriadoVO feriadoVO = getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, data);
				if (feriadoVO == null || feriadoVO.getCodigo().intValue() == 0) {
					for (DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioProfessorVO : disponibilidadeHorarioTurmaProfessorVOs) {
						if (!disponibilidadeHorarioProfessorVO.getProgramarAula() && !disponibilidadeHorarioProfessorVO.getPossuiChoqueSala() && disponibilidadeHorarioProfessorVO.getNaoPossueChoqueHorario() && disponibilidadeHorarioProfessorVO.getPossuiChoqueAulaExcesso() && liberarProgramacaoAulaProfessorAcimaPermitido) {
							disponibilidadeHorarioProfessorVO.setProgramarAula(true);
						}
						if (disponibilidadeHorarioProfessorVO.getNaoPossueChoqueHorario() && disponibilidadeHorarioProfessorVO.getProgramarAula() && disponibilidadeHorarioProfessorVO.getHorarioLivre() && (!disponibilidadeHorarioProfessorVO.getPossuiChoqueAulaExcesso() || liberarProgramacaoAulaProfessorAcimaPermitido) && !disponibilidadeHorarioProfessorVO.getPossuiChoqueSala()) {
							if (executarVerificarDisponibilidadeHorarioMarcadoTurmaPorDia(horarioTurmaVO, data, disponibilidadeHorarioProfessorVO, false)) {
								if (adicionarAulaHorarioProfessor(horarioTurmaVO, horarioTurmaVO.getDisciplina(), data, disponibilidadeHorarioProfessorVO.getNrAula(), disponibilidadeHorarioProfessorVO.getHorarioInicio(), disponibilidadeHorarioProfessorVO.getHorarioTermino(), horarioProfessorVO, horarioTurmaVO.getSala(), usuario, false, controlaNumeroMaximoAulaProgramadaProfessorDia, numeroAulaMaximaProgramarProfessorDia, liberarProgramacaoAulaProfessorAcimaPermitido, qtdeAulaDiaProgramadaProfessor, true)) {
									resultadoAcao.append(adicionarDadosHorarioTurmaPorDia(horarioTurmaVO, data, disponibilidadeHorarioProfessorVO, feriadoVO, liberarProgramacaoAulaProfessorAcimaPermitido));
								}
							}
						}
					}
				}
			}
			if(horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty() && avulso){
				throw new Exception("Não existe horário disponível para programação de aula avulsa. Por Favor verifique a Disponibilidade de Horário da aula.");
			}else if(horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty() && !avulso){
				throw new Exception("Não existe horários disponíveis para programação de aula por período. Por Favor verifique a Disponibilidade de Horário da aula.");
			}
			boolean alteroHorarioTurma = false;
			for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
				if (horarioTurmaDiaVO2.getHorarioAlterado()) {
					horarioTurmaDiaVO2.setFuncionarioCargoVO(horarioTurmaDiaVO.getFuncionarioCargoVO());
					horarioTurmaDiaVO2.setAulaReposicao(horarioTurmaDiaVO.getAulaReposicao());
					horarioTurmaDiaVO2.setGerarEventoAulaOnLineGoogleMeet(horarioTurmaVO.getGerarEventoAulaOnLineGoogleMeet());
					alteroHorarioTurma = true;
					horarioTurmaDiaVO2.setHorarioAlterado(false);					
					horarioTurmaDiaVO2.setHorarioTurma(horarioTurmaVO);
					if (horarioTurmaDiaVO2.getCodigo() > 0) {
						if(horarioTurmaDiaVO2.getIsAulaProgramada()) {							
							getFacadeFactory().getHorarioTurmaDiaFacade().alterar(horarioTurmaDiaVO2, usuario);
						}else {
							getFacadeFactory().getHorarioTurmaDiaFacade().excluir(horarioTurmaDiaVO2, usuario);
						}
					} else {
						if(horarioTurmaDiaVO2.getIsAulaProgramada()) {			
							getFacadeFactory().getHorarioTurmaDiaFacade().incluir(horarioTurmaDiaVO2, usuario);
						}
					}
				}
			}
			// stopWatch.stop();
			// stopWatch.getElapsed();
			for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
				if (horarioProfessorDiaVO.getHorarioAlterado()) {
					for (HorarioProfessorDiaItemVO hpdiVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
						q: for (HorarioTurmaDiaVO htdVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
							for (HorarioTurmaDiaItemVO htdiVO : htdVO.getHorarioTurmaDiaItemVOs()) {
								if (hpdiVO.getTurmaVO().getCodigo().equals(horarioTurmaVO.getTurma().getCodigo())
										&& hpdiVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplina().getCodigo()) 
										&& hpdiVO.getData_Apresentar().equals(htdVO.getData_Apresentar()) 
										&& hpdiVO.getNrAula().equals(htdiVO.getNrAula())
										&& !Uteis.isAtributoPreenchido(hpdiVO.getHorarioTurmaDiaItemVO().getCodigo())) {
									hpdiVO.setHorarioTurmaDiaItemVO(htdiVO);
									break q;
								}
							}
						}
					}
					horarioProfessorDiaVO.getHorarioProfessor().setCodigo(horarioProfessorVO.getCodigo());
					if (horarioProfessorDiaVO.getCodigo() > 0) {
						getFacadeFactory().getHorarioProfessorDiaFacade().alterar(horarioProfessorDiaVO, usuario);
					} else {
						getFacadeFactory().getHorarioProfessorDiaFacade().incluir(horarioProfessorDiaVO, usuario);
					}
				}
			}
			if (alteroHorarioTurma) {
				if (!resultadoAcao.toString().isEmpty()) {
					getFacadeFactory().getHorarioTurmaLogFacade().realizarCriacaoLogHorarioTurma(horarioTurmaVO, usuario, "Inclusão da Disciplina e Professor no Horario da Turma e no Horario do Professor", resultadoAcao.toString());
				}
				incluirDataFinalizacaoEConfirmacaoTurma(horarioTurmaVO, usuario);
				horarioTurmaVO.montarDadosHorarioDisciplinaProfessorDia();
				getFacadeFactory().getHorarioTurmaProfessorDisciplinaFacade().alterarHorarioTurmaProfessorDisciplinas(horarioTurmaVO.getCodigo(), horarioTurmaVO.getListaProfessorDisciplina());
				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().incluirProfessorTitularDisciplinaTurma(horarioProfessorVO.getProfessor(), horarioTurmaVO.getDisciplina(), horarioTurmaVO.getTurma(), horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), usuario);
				Ordenacao.ordenarLista(horarioTurmaVO.getHorarioTurmaDiaVOs(), "data");
				realizarCriacaoCalendarioHorarioTurma(horarioTurmaVO, usuario);
				if(Uteis.isAtributoPreenchido(horarioProfessorVO.getOperacaoFuncionalidadeVO().getResponsavel())){
					horarioProfessorVO.getOperacaoFuncionalidadeVO().setCodigoOrigem(horarioProfessorVO.getCodigo().toString());
					getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(horarioProfessorVO.getOperacaoFuncionalidadeVO());	
				}
				
			}
			
			alterarUpdatedPorCodigo(horarioTurmaVO, false, usuario);
			return null;
		} catch (Exception e) {
			if (horarioTurmaIncluida) {
				horarioTurmaVO.setNovoObj(true);
				horarioTurmaVO.setCodigo(0);
			}
			throw e;
		} finally {
			dataInicio = null;
			dataFim = null;
			diaBase = null;
			Uteis.liberarListaMemoria(feriadoVOs);
			Uteis.liberarListaMemoria(datas);
			// Uteis.liberarListaMemoria(disponibilidadeHorarioTurmaProfessorVOs);
			qtdeAulaDiaProgramadaProfessor = null;
		}
	}

	/**
	 * Este mï¿½todo ï¿½ chamado quando o Horarï¿½rio da Turma For baseado no
	 * DIA.
	 * 
	 * @param obj
	 *            = Objeto que contï¿½m os horï¿½rios selecionados na tela pelo
	 *            usuario que define qual deles deverï¿½ ser utilizado pelo
	 *            Horï¿½rio da Turma
	 */
	public String adicionarDadosHorarioTurmaPorDia(HorarioTurmaVO horarioTurmaVO, Date dataBase, DisponibilidadeHorarioTurmaProfessorVO obj, FeriadoVO feriadoVO, boolean liberarAulaAcimaLimite) throws Exception {
		if (obj.getProgramarAula() && obj.getNaoPossueChoqueHorario() && !obj.getPossuiChoqueSala() && (!obj.getPossuiChoqueAulaExcesso() || liberarAulaAcimaLimite)) {
			HorarioTurmaDiaVO item = getFacadeFactory().getHorarioTurmaDiaFacade().consultarHorarioTurmaPorDiaPorDia(horarioTurmaVO, dataBase);
			if (feriadoVO == null || feriadoVO.getCodigo().intValue() == 0) {
				if (item.adicinarProfessorEDisciplina(obj.getNrAula(), horarioTurmaVO.getDisciplina(), horarioTurmaVO.getProfessor(), horarioTurmaVO.getSala(), false, obj.getHorarioLivre())) {
					horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(item, true, feriadoVO);
					item.setHorarioAlterado(true);
					horarioTurmaVO.montarLogResultadoAcao("Incluído", obj.getNrAula(), obj.getHorario(), dataBase, horarioTurmaVO.getDisciplina(), horarioTurmaVO.getProfessor());
					return "Inclusão da " + obj.getNrAula() + "º (" + obj.getHorario() + ") do dia " + Uteis.getData(dataBase) + " da disciplina " + horarioTurmaVO.getDisciplina().getNome() + " e professor " + horarioTurmaVO.getProfessor() + ".\n";
				}
			}

		}
		return "";
	}

	/***
	 * Mï¿½todo responsï¿½vel por verificar a disponibilidade do horario a ser
	 * marcado de acordo com a turma.
	 * 
	 * @param horarioTurmaVO
	 * @param disponibilidadeHorarioTurmaProfessor
	 * @throws Exception
	 */
	@Override
	public Boolean executarVerificarDisponibilidadeHorarioMarcadoTurmaPorDia(HorarioTurmaVO horarioTurmaVO, Date dataBase, DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioTurmaProfessor, boolean retornarExcecao) throws Exception {
		HorarioTurmaDiaVO item = getFacadeFactory().getHorarioTurmaDiaFacade().consultarHorarioTurmaPorDiaPorDia(horarioTurmaVO, dataBase);
		if (disponibilidadeHorarioTurmaProfessor.getProgramarAula()) {
			return getFacadeFactory().getHorarioTurmaDiaFacade().executarVerificarDisponibilidadeProfessorEDisciplinaAula(horarioTurmaVO, item, disponibilidadeHorarioTurmaProfessor.getNrAula(), disponibilidadeHorarioTurmaProfessor.getHorario(), horarioTurmaVO.getDisciplina(), horarioTurmaVO.getProfessor(), retornarExcecao);
		}
		return false;
	}

	public List<HorarioTurmaDiaVO> inicializarDadosHorarioTurmaDiaVOPorProfessor(Integer professor, Integer disciplina, HorarioTurmaVO horarioTurmaVO) throws Exception {
		return getFacadeFactory().getHorarioTurmaDiaFacade().consultarHorarioTurmaDias(horarioTurmaVO.getCodigo(), null, null, null, null, professor, disciplina, null, null, null, null);
	}

	public List<PessoaVO> executarMontagemListaProfessorComAulaProgramadaTurma(HorarioTurmaVO horarioTurmaVO) {
		List<PessoaVO> professores = new ArrayList<PessoaVO>(0);
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
				professores = adicionarProfessorNaListaProfessorComAulaProgramadaNaTurma(horarioTurmaDiaItemVO.getFuncionarioVO(), professores);
			}
		}
		return professores;
	}

	public List<PessoaVO> adicionarProfessorNaListaProfessorComAulaProgramadaNaTurma(PessoaVO professor, List<PessoaVO> professores) {
		for (PessoaVO pessoa : professores) {
			if (pessoa.getCodigo().intValue() == professor.getCodigo()) {
				return professores;
			}
		}
		professores.add(professor);
		return professores;
	}

	/***
	 * consulta os horarioturma baseados na data informada, ou seja, a data
	 * irï¿½ ter aula jï¿½ programada
	 * 
	 * @param data
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 */
	public List<HorarioTurmaVO> consultarPorData(Date data, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT horarioturma.turma, horarioturma.codigo, horarioturma.semestrevigente, horarioturma.observacao, horarioturma.anovigente, horarioturma.segunda, ");
		sqlStr.append(" horarioturma.terca, horarioturma.quarta, horarioturma.quinta, horarioturma.sexta, horarioturma.sabado, horarioturma.domingo, updated ");
		sqlStr.append(" FROM HORARIOTURMA ");
		sqlStr.append(" INNER JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" WHERE 1=1 ");
		if (data != null) {
			sqlStr.append(" AND horarioturmadia.data = '").append(Uteis.getDataJDBC(data)).append("' ");
		}
		sqlStr.append(" AND horarioturmadia.data >= current_date ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	/***
	 * consulta todos os horarioturma
	 * 
	 * @param data
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 */
	public List<HorarioTurmaVO> consultarTodos(int nivelMontarDados, UsuarioVO usuario) throws Exception {

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT horarioturma.turma,horarioturma.codigo,horarioturma.semestrevigente, horarioturma.anovigente, horarioturma.segunda,horarioturma.terca,horarioturma.quarta, horarioturma.quinta,horarioturma.sexta,horarioturma.sabado,horarioturma.domingo, updated ");
		sqlStr.append(" FROM horarioturma ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	@Override
	public Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> executarVerificarDisponibilidadeHorarioTurmaEHorarioProfessor(final HorarioTurmaVO horarioTurmaVO, Boolean avulso, final HorarioProfessorVO horarioProfessorVO, final UsuarioVO usuario, final Boolean retornarExcecao, final boolean controlaNumeroMaximoAulaProgramadaProfessorDia, final Integer numeroAulaMaximaProgramarProfessorDia, final boolean liberarProgramacaoAulaProfessorAcimaPermitido) throws Exception {
		final List<ChoqueHorarioVO> listaErros = new ArrayList<ChoqueHorarioVO>();
		final List<ChoqueHorarioVO> listaSucesso = new ArrayList<ChoqueHorarioVO>();
		Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = new HashMap<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>>(0);
		final Map<String, Integer> qtdeAulaDiaProgramadaProfessor = new HashMap<String, Integer>(0);
		final List<Date> datas = new ArrayList<Date>();
		Date dataInicio = null;
		Date dataFim = null;
		Date diaBase = null;
		if (avulso) {
			dataInicio = horarioTurmaVO.getDia();
			dataFim = horarioTurmaVO.getDia();
			diaBase = Uteis.getDateHoraFinalDia(horarioTurmaVO.getDia());
		} else {
			dataInicio = horarioTurmaVO.getDiaInicio();
			dataFim = horarioTurmaVO.getDiaFim();
			diaBase = Uteis.getDateHoraFinalDia(horarioTurmaVO.getDiaInicio());

		}
		final List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(Uteis.getDate("01/" + Uteis.getMesData(dataInicio) + "/" + Uteis.getAnoData(dataInicio)), Uteis.getDate(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(dataFim)) + "/" + Uteis.getMesData(dataFim) + "/" + Uteis.getAnoData(dataFim)), horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		List<DisponibilidadeHorarioTurmaProfessorVO> disponibilidadeHorarioTurmaProfessorVOs = null;
		while (Uteis.getDateHoraFinalDia(diaBase).compareTo(Uteis.getDateHoraFinalDia(dataFim)) <= 0) {
			disponibilidadeHorarioTurmaProfessorVOs = horarioProfessorVO.consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(diaBase));
			boolean diaAdicionado = false;
			for (DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioProfessorVO : disponibilidadeHorarioTurmaProfessorVOs) {
				diaBase = Uteis.getDateHoraFinalDia(diaBase);
				if (disponibilidadeHorarioProfessorVO.getProgramarAula() && disponibilidadeHorarioProfessorVO.getHorarioLivre()) {
					FeriadoVO feriadoVO = getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, diaBase);
					if (feriadoVO == null || feriadoVO.getCodigo().intValue() == 0) {
						if (!diaAdicionado) {
							datas.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(diaBase));
							diaAdicionado = true;
						}
					}
				}
			}
			diaBase = Uteis.getDateHoraFinalDia(Uteis.obterDataFutura(diaBase, 1));
		}
		horarioProfessorVO.setHorarioProfessorDiaVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(null, null, horarioProfessorVO.getProfessor().getCodigo(), null, null, dataInicio, dataFim, null, datas, null, null, null));
		if (controlaNumeroMaximoAulaProgramadaProfessorDia && numeroAulaMaximaProgramarProfessorDia > 0 && !liberarProgramacaoAulaProfessorAcimaPermitido) {
			qtdeAulaDiaProgramadaProfessor.putAll(getFacadeFactory().getHorarioProfessorDiaFacade().consultarQuantidadeAulaProgramadaProfessorPorData(datas, horarioProfessorVO.getProfessor().getCodigo()));
		}
		// if (!datas.isEmpty()) {
		final ConsistirException ex = new ConsistirException();
		ProcessarParalelismo.executar(0, datas.size(), ex, new ProcessarParalelismo.Processo() {
			@Override
			public void run(int i) {
				Date data = datas.get(i);
				List<DisponibilidadeHorarioTurmaProfessorVO> disponibilidadeHorarioTurmaProfessorVOs = horarioProfessorVO.consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(data));
				Integer qtdeAulaProgramadaDia = qtdeAulaDiaProgramadaProfessor != null && qtdeAulaDiaProgramadaProfessor.containsKey(Uteis.getData(data)) ? qtdeAulaDiaProgramadaProfessor.get(Uteis.getData(data)) : 0;
				for (DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioProfessorVO : disponibilidadeHorarioTurmaProfessorVOs) {
					FeriadoVO feriadoVO = getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, data);
					if (feriadoVO == null || feriadoVO.getCodigo().intValue() == 0) {
						if (disponibilidadeHorarioProfessorVO.getProgramarAula() && disponibilidadeHorarioProfessorVO.getHorarioLivre()) {
							try {
								if (getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoChoqueHorarioTurmaDiaItemComHorarioTurmaOutroAnoSemestre(horarioTurmaVO, horarioTurmaVO.getDisciplina(), data, disponibilidadeHorarioProfessorVO.getNrAula(), usuario, retornarExcecao)
										&& getFacadeFactory().getHorarioTurmaFacade().executarVerificarDisponibilidadeHorarioMarcadoTurmaPorDia(horarioTurmaVO, data, disponibilidadeHorarioProfessorVO, retornarExcecao) 
										&& !getFacadeFactory().getHorarioTurmaDiaItemFacade().realizarVerificacaoChoqueHorarioSalaAula(horarioTurmaVO.getCodigo(), horarioTurmaVO.getSala().getCodigo(), horarioTurmaVO.getSala().getControlarChoqueSala(), data, disponibilidadeHorarioProfessorVO.getHorarioInicio(), disponibilidadeHorarioProfessorVO.getHorarioTermino(), retornarExcecao)) {
									if (controlaNumeroMaximoAulaProgramadaProfessorDia && numeroAulaMaximaProgramarProfessorDia > 0 && !liberarProgramacaoAulaProfessorAcimaPermitido) {
										qtdeAulaProgramadaDia++;
										if (qtdeAulaProgramadaDia > numeroAulaMaximaProgramarProfessorDia) {
											ChoqueHorarioVO choqueHorarioVO = new ChoqueHorarioVO();
											choqueHorarioVO.setProfessor(horarioProfessorVO.getProfessor().getNome());
											choqueHorarioVO.setData(data);
											choqueHorarioVO.setHorario(disponibilidadeHorarioProfessorVO.getNrAula() + "ª (" + disponibilidadeHorarioProfessorVO.getHorarioInicio() + " até " + disponibilidadeHorarioProfessorVO.getHorarioTermino() + ") ");
											choqueHorarioVO.setChoqueHorarioAulaExcesso(true);
											throw choqueHorarioVO;
										}
									}
									disponibilidadeHorarioProfessorVO.setNaoPossueChoqueHorario(true);
									disponibilidadeHorarioProfessorVO.setNaoPossueChoqueHorario(getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoChoqueHorarioProfessorPorDisponibilidadeHorarioTurmaProfessorVO(horarioTurmaVO, horarioTurmaVO.getDisciplina(), data, disponibilidadeHorarioProfessorVO, horarioProfessorVO, usuario, retornarExcecao));
									disponibilidadeHorarioProfessorVO.setPossuiChoqueSala(false);
									disponibilidadeHorarioProfessorVO.setPossuiChoqueAulaExcesso(false);
									listaSucesso.add(new ChoqueHorarioVO(data, disponibilidadeHorarioProfessorVO.getNrAula(), disponibilidadeHorarioProfessorVO.getHorario(), horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioTurmaVO.getDisciplina().getNome(), horarioTurmaVO.getProfessor().getNome(), horarioTurmaVO.getTurma().getTurno().getNome(), "", false));
								}
							} catch (ChoqueHorarioVO e) {
								disponibilidadeHorarioProfessorVO.setPossuiChoqueSala(e.getChoqueHorarioSala());
								disponibilidadeHorarioProfessorVO.setPossuiChoqueAulaExcesso(e.getChoqueHorarioAulaExcesso());
								if (!disponibilidadeHorarioProfessorVO.getPossuiChoqueAulaExcesso() && !disponibilidadeHorarioProfessorVO.getPossuiChoqueSala()) {
									disponibilidadeHorarioProfessorVO.setNaoPossueChoqueHorario(false);
								}
								listaErros.add(e);
							} catch (Exception e) {
								ex.adicionarListaMensagemErro(e.getMessage());
							}
						}
					}
				}

			}
		});
		validacoes.put(TipoValidacaoChoqueHorarioEnum.ERRO, listaErros);
		validacoes.put(TipoValidacaoChoqueHorarioEnum.SUCESSO, listaSucesso);
		if(!ex.getListaMensagemErro().isEmpty()) {
			throw new Exception(ex.getToStringMensagemErro());
		}
		// }
		return validacoes;
	}

	/**
	 * Este mï¿½todo e utilizado para verificar choque de horario com o horario
	 * do professor quando estï¿½ adicionando ao horario da turma uma nova aula.
	 * 
	 * @param horarioTurmaVO
	 * @param disciplina
	 * @param dataBase
	 * @param disponibilidadeHorarioTurmaProfessorVO
	 * @param horarioProfessorDiaVOs
	 * @param horarioProfessorVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public Boolean realizarVerificacaoChoqueHorarioProfessorPorDisponibilidadeHorarioTurmaProfessorVO(HorarioTurmaVO horarioTurmaVO, DisciplinaVO disciplina, Date dataBase, DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioTurmaProfessorVO, HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario, boolean retornarExececao) throws Exception {
		Integer minutoInicioAulaTurma = (((Uteis.getHoraDaString(disponibilidadeHorarioTurmaProfessorVO.getHorarioInicio()) * 60) + Uteis.getMinutosDaString(disponibilidadeHorarioTurmaProfessorVO.getHorarioInicio())));
		Integer minutoTerminoAulaTurma = (Uteis.getHoraDaString(disponibilidadeHorarioTurmaProfessorVO.getHorarioTermino()) * 60) + Uteis.getMinutosDaString(disponibilidadeHorarioTurmaProfessorVO.getHorarioTermino());
		String horarioBase = disponibilidadeHorarioTurmaProfessorVO.getHorarioInicio() + " " + UteisJSF.internacionalizar("prt_a") + " " + disponibilidadeHorarioTurmaProfessorVO.getHorarioTermino();
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			if (horarioProfessorDiaVO.getData_Apresentar().equals(Uteis.getData(dataBase))) {
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
					if (!horarioProfessorDiaItemVO.getDisciplinaLivre() && !horarioProfessorDiaItemVO.getTurmaLivre()) {
						Integer minutoInicioAulaProfessor = (((Uteis.getHoraDaString(horarioProfessorDiaItemVO.getHorarioInicio()) * 60) + Uteis.getMinutosDaString(horarioProfessorDiaItemVO.getHorarioInicio())));
						Integer minutoTerminoAulaProfessor = (Uteis.getHoraDaString(horarioProfessorDiaItemVO.getHorarioTermino()) * 60) + Uteis.getMinutosDaString(horarioProfessorDiaItemVO.getHorarioTermino());
						if (!validarDadosChoqueHorarioTurmaComHorarioProfessor(minutoInicioAulaTurma, minutoInicioAulaProfessor, minutoTerminoAulaProfessor, minutoTerminoAulaTurma, horarioProfessorDiaItemVO, horarioTurmaVO, dataBase, disponibilidadeHorarioTurmaProfessorVO.getNrAula(), horarioBase, horarioProfessorVO.getProfessor(), disciplina, usuario, retornarExececao)) {
							return false;
						}
					}
				}

			}
		}
		return true;
	}

	public Boolean realizarVerificacaoProfessorLecionaAlgumaDisciplina(Integer pessoa) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT codigo from HorarioTurmadiaitem where professor = ").append(pessoa).append(" limit 1");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	/***
	 * Mï¿½todo responsï¿½vel por buscar horï¿½rio da turma para determinada
	 * disciplina
	 * 
	 * @param codigoDisciplina
	 * @return
	 * @throws Exception
	 */
	public List<HorarioTurmaVO> consultarPorTurmaDisciplinaAnoSemestre(int codigoTurma, int codigoDisciplina, String anoVigente, String semestreVigente, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSqlConsultaRapida();
		sqlStr.append(" WHERE turma.codigo = ").append(codigoTurma);
		sqlStr.append(" AND ((turma.anual and horarioturma.anovigente = '").append(anoVigente).append("') ");
		sqlStr.append(" or (turma.semestral and horarioturma.anovigente = '").append(anoVigente).append("' and horarioturma.semestrevigente = '").append(semestreVigente).append("') ");
		sqlStr.append(" or (turma.semestral = false and turma.anual =  false )) ");		
		sqlStr.append(" and exists (SELECT horarioturmadiaitem.codigo from HorarioTurmaDia inner join horarioturmadiaitem on horarioturmadiaitem.HorarioTurmaDia = HorarioTurmaDia.codigo where HorarioTurmaDia.horarioturma = horarioturma.codigo and horarioturmadiaitem.disciplina = ").append(codigoDisciplina).append(" limit 1) ");
		sqlStr.append(" order by HorarioTurma.turma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<HorarioTurmaVO> listaHorarioTurma = montarDadosConsultaRapida(tabelaResultado);
		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurma) {			
			horarioTurmaVO.setHorarioTurmaDiaVOs(getFacadeFactory().getHorarioTurmaDiaFacade().consultarHorarioTurmaDias(horarioTurmaVO.getCodigo(), codigoTurma, null, anoVigente, semestreVigente, null, codigoDisciplina, null, null, null, usuario));
		}
		return listaHorarioTurma;
	}

	public void inicializarDadosCalendario(HorarioTurmaVO horarioTurmaVO, boolean montarComTodosOsDias, List<FeriadoVO> feriadoVOs) throws Exception {
		horarioTurmaVO.setCalendarioHorarioAulaVOs(new ArrayList<CalendarioHorarioAulaVO<HorarioTurmaDiaVO>>());
		if (!horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty()) {
			for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
				adicionarHorarioTurmaDiaEmCalendarioHorarioTurma(horarioTurmaVO, horarioTurmaDiaVO, montarComTodosOsDias);
			}
			if (montarComTodosOsDias) {
				executarCriacaoDiasMes(horarioTurmaVO, feriadoVOs);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void adicionarHorarioTurmaDiaEmCalendarioHorarioTurma(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, boolean montarComTodosOsDias) {
		for (CalendarioHorarioAulaVO calendarioHorarioAulaVO : horarioTurmaVO.getCalendarioHorarioAulaVOs()) {
			if (Integer.parseInt(calendarioHorarioAulaVO.getAno()) == (Uteis.getAnoData(horarioTurmaDiaVO.getData())) && calendarioHorarioAulaVO.getMesAno().getMesData(horarioTurmaDiaVO.getData()).equals(calendarioHorarioAulaVO.getMesAno())) {
				adicionarDiaComAulaProgramadaCalendario(calendarioHorarioAulaVO, horarioTurmaDiaVO);
				return;
			}
		}
		CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO = new CalendarioHorarioAulaVO<HorarioTurmaDiaVO>();
		calendarioHorarioAulaVO.setAno("" + (Uteis.getAnoData(horarioTurmaDiaVO.getData())));
		calendarioHorarioAulaVO.setMesAno(calendarioHorarioAulaVO.getMesAno().getMesData(horarioTurmaDiaVO.getData()));
		adicionarDiaComAulaProgramadaCalendario(calendarioHorarioAulaVO, horarioTurmaDiaVO);
		horarioTurmaVO.getCalendarioHorarioAulaVOs().add(calendarioHorarioAulaVO);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executarCriacaoDiasMes(HorarioTurmaVO horarioTurmaVO, List<FeriadoVO> feriadoVOs) throws Exception {
		if (!horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty()) {

			String diaString;
			String diaSemana;
			for (CalendarioHorarioAulaVO calendarioHorarioAulaVO : horarioTurmaVO.getCalendarioHorarioAulaVOs()) {
				int dia = 1;
				int ultimoDia = Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(Uteis.getDate("01/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
				while (dia <= ultimoDia) {
					if (dia < 10) {
						diaString = "0" + dia;
					} else {
						diaString = "" + dia;
					}
					diaSemana = Uteis.getDiaSemanaData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
					if (diaSemana.equals(DiaSemana.DOMINGO.getValor())) {
						adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					} else if (diaSemana.equals(DiaSemana.SEGUNGA.getValor())) {
						adicionarDiaCalendarioSegunda(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					} else if (diaSemana.equals(DiaSemana.TERCA.getValor())) {
						adicionarDiaCalendarioTerca(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					} else if (diaSemana.equals(DiaSemana.QUARTA.getValor())) {
						adicionarDiaCalendarioQuarta(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					} else if (diaSemana.equals(DiaSemana.QUINTA.getValor())) {
						adicionarDiaCalendarioQuinta(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					} else if (diaSemana.equals(DiaSemana.SEXTA.getValor())) {
						adicionarDiaCalendarioSexta(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					} else if (diaSemana.equals(DiaSemana.SABADO.getValor())) {
						adicionarDiaCalendarioSabado(horarioTurmaVO, calendarioHorarioAulaVO, diaString, getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
					}
					dia++;
				}
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado(), "dia");
			}
		}
	}

	public void adicionarDiaCalendarioDomingo(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}
	}

	public void adicionarDiaCalendarioSegunda(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01")) {
			adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
		}
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}

	}

	public void adicionarDiaCalendarioTerca(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01")) {
			adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioSegunda(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
		}
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}
	}

	public void adicionarDiaCalendarioQuarta(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01")) {
			adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioSegunda(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioTerca(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
		}
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}
	}

	public void adicionarDiaCalendarioQuinta(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01")) {
			adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioSegunda(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioTerca(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioQuarta(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
		}
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}
	}

	public void adicionarDiaCalendarioSexta(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01")) {
			adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioSegunda(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioTerca(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioQuarta(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioQuinta(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
		}
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}
	}

	public void adicionarDiaCalendarioSabado(HorarioTurmaVO horarioTurmaVO, CalendarioHorarioAulaVO<HorarioTurmaDiaVO> calendarioHorarioAulaVO, String dia, FeriadoVO feriadoVO) throws Exception {
		if (dia.equals("01")) {
			adicionarDiaCalendarioDomingo(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioSegunda(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioTerca(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioQuarta(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioQuinta(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
			adicionarDiaCalendarioSexta(horarioTurmaVO, calendarioHorarioAulaVO, "", null);
		}
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado()) {
			if (horarioTurmaDiaVO.getDia().equals(dia)) {
				if(!horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().isEmpty()) {					
					return;
				}else {
					horarioTurmaDiaVO.setFeriado(feriadoVO);
					return;
				}
			}
		}
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		if (dia.isEmpty()) {
			horarioTurmaDiaVO.setData(null);
		} else {
			horarioTurmaDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado().add(horarioTurmaDiaVO);
		if (horarioTurmaDiaVO.getData() != null) {
			horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, false, feriadoVO);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarDiaComAulaProgramadaCalendario(CalendarioHorarioAulaVO calendarioHorarioAulaVO, HorarioTurmaDiaVO horarioTurmaDiaVO) {
		if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.DOMINGO.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo().add(horarioTurmaDiaVO);
		} else if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.SEGUNGA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda().add(horarioTurmaDiaVO);
		} else if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.TERCA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca().add(horarioTurmaDiaVO);
		} else if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.QUARTA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta().add(horarioTurmaDiaVO);
		} else if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.QUINTA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta().add(horarioTurmaDiaVO);
		} else if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.SEXTA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta().add(horarioTurmaDiaVO);
		} else if (horarioTurmaDiaVO.getDiaSemana().equals(DiaSemana.SABADO.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado().add(horarioTurmaDiaVO);
		}
	}

	/*
	 * Este mï¿½todo inicializa os dados do horï¿½rio da turma, montando uma
	 * lista com todos os professores e disciplinas envolvidas em seguida
	 * inicializa os dados do horario do tipo dia a dia e em seguida tipo
	 * semanal.
	 */
	public void inicializarDadosHorarioTurmaPorHorarioTurma(HorarioTurmaVO horarioTurmaVO, boolean montarDataCalendarioSemProgramacaoAula, UsuarioVO usuario) throws Exception {
		carregarDadosHorarioTurma(horarioTurmaVO, null, null, usuario);
		montarDadosTurno(horarioTurmaVO, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		horarioTurmaVO.getTurma().setTurno(horarioTurmaVO.getTurno());
		realizarCriacaoCalendarioHorarioTurma(horarioTurmaVO, usuario);

	}

	/**
	 * Este mï¿½todo ï¿½ chamado quando ï¿½ realizado uma consulta doo horario
	 * pela turma e pelo ano e semestre, Ele verifica a existencia de um
	 * horï¿½rio jï¿½ cadastrado caso sim ele chama o mï¿½todo
	 * <inicializarDadosHorarioTurmaPorHorarioTurma> caso nï¿½o ele
	 * disponibiliza um cadastro limpo do horario baseado no ano e semestre.
	 * 
	 * @param identificadorTurma
	 * @param montarDataCalendarioSemProgramacaoAula
	 * @param semestreVigente
	 * @param anoVigente
	 * @return HorarioTurmaVO
	 * @throws Exception
	 */
	public void inicializarDadosHorarioTurmaPorTurma(HorarioTurmaVO horarioTurmaVO, boolean montarDataCalendarioSemProgramacaoAula, UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuario) throws Exception {
		horarioTurmaVO.setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(horarioTurmaVO.getTurma().getIdentificadorTurma(), unidadeEnsino.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	    //adicionado para preencher o campo de curso caso  a turma consultada seja agrupada  e nao tem curso ;
		if(!Uteis.isAtributoPreenchido(horarioTurmaVO.getTurma().getCurso()) && horarioTurmaVO.getTurma().getTurmaAgrupada()) {
	    	CursoVO primeiroCursoTurmaAgrupada  = TurmaAgrupada.consultarTurmaAgrupadas(horarioTurmaVO.getTurma().getCodigo(), false, 0, usuario).get(0).getTurma().getCurso();
	    	if(Uteis.isAtributoPreenchido(primeiroCursoTurmaAgrupada)) {
	    		horarioTurmaVO.getTurma().setCurso(primeiroCursoTurmaAgrupada);
	    	}   	
	    }		
		if (horarioTurmaVO.getTurma().getCodigo() > 0) {
			if (horarioTurmaVO.getTurma().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				horarioTurmaVO.setAnoVigente("");
				horarioTurmaVO.setSemestreVigente("");
			}
			if (horarioTurmaVO.getTurma().getSemestral() || horarioTurmaVO.getTurma().getAnual()) {
				if (horarioTurmaVO.getAnoVigente().trim().isEmpty()) {
//					throw new ConsistirException(UteisJSF.internacionalizar("msg_HorarioTurma_ano"));
					StringBuilder sql  = new StringBuilder("select horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.updated from horarioturma ");
					sql.append(" where horarioturma.turma = ").append(horarioTurmaVO.getTurma().getCodigo());
					sql.append(" order by (horarioturma.anovigente||'/'||horarioturma.semestrevigente) desc limit 1 ");
					SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
					if(rs.next()){
						horarioTurmaVO.setAnoVigente(rs.getString("anovigente"));
						horarioTurmaVO.setUpdated(rs.getTimestamp("updated"));
						horarioTurmaVO.setSemestreVigente(rs.getString("semestrevigente"));					
					}else{
						horarioTurmaVO.setAnoVigente(Uteis.getAnoDataAtual4Digitos());
						if (horarioTurmaVO.getTurma().getSemestral() && horarioTurmaVO.getSemestreVigente().trim().isEmpty()) {
							horarioTurmaVO.setSemestreVigente(Uteis.getSemestreAtual());
						}
					}
					
				}
				if (horarioTurmaVO.getAnoVigente().trim().length() != 4) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_HorarioTurma_anoInvalido"));
				}
			}
			if (horarioTurmaVO.getTurma().getSemestral()) {
				if (horarioTurmaVO.getSemestreVigente().trim().isEmpty()) {
//					throw new ConsistirException(UteisJSF.internacionalizar("msg_HorarioTurma_semestre"));
					return;
				}
				if (!horarioTurmaVO.getSemestreVigente().trim().equals("1") && !horarioTurmaVO.getSemestreVigente().trim().equals("2")) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_HorarioTurma_semestreInvalido"));
				}
			}
			HorarioTurmaVO horarioTurmaVO2 = consultarPorHorarioTurmaPorIdentificadorTurmaTurma(horarioTurmaVO.getTurma().getIdentificadorTurma(), usuario.getUnidadeEnsinoLogado().getCodigo(), horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			horarioTurmaVO.setCodigo(horarioTurmaVO2.getCodigo());
			horarioTurmaVO.setNovoObj(horarioTurmaVO.getCodigo().equals(0));
			horarioTurmaVO.setTurno(horarioTurmaVO.getTurma().getTurno());
			horarioTurmaVO.setUpdated(horarioTurmaVO2.getUpdated());

			if (usuario.getUnidadeEnsinoLogado().getCodigo() > 0) {
				horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().setCodigo(usuario.getUnidadeEnsinoLogado().getCidade().getCodigo());
			} else {
				horarioTurmaVO.getTurma().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(horarioTurmaVO.getTurma().getUnidadeEnsino().getCodigo(), false, usuario));
			}
			if (horarioTurmaVO.getCodigo().intValue() > 0) {
				inicializarDadosHorarioTurmaPorHorarioTurma(horarioTurmaVO, montarDataCalendarioSemProgramacaoAula, usuario);
				horarioTurmaVO.setNovoObj(false);
			} else {
				inicializarDadosHorarioTurmaPorHorarioTurma(horarioTurmaVO, montarDataCalendarioSemProgramacaoAula, usuario);

			}
		}
	}

	public List<Integer> consultarProfessorLecionaDisciplina(HorarioTurmaVO horarioTurmaVO, Integer codigoDisciplina) throws Exception {
		return consultarProfessorLecionaDisciplinaPorDia(horarioTurmaVO, codigoDisciplina);
	}

	private List<Integer> consultarProfessorLecionaDisciplinaPorDia(HorarioTurmaVO horarioTurmaVO, Integer codigoDisciplina) {
		Integer nrAulas = 0;
		List<Integer> professores = new ArrayList<Integer>();
		Integer professor = null;
		for (HorarioTurmaDiaVO item : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			nrAulas = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioTurmaVO.getTurno(), item.getDiaSemanaEnum());
			professor = item.consultarProfessorLecionaDisciplina(nrAulas, codigoDisciplina);
			if (professor.intValue() > 0 && !professores.contains(professor)) {
				professores.add(professor);
			}
		}
		return professores;
	}

	public void validarDados(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws Exception {
		HorarioTurmaVO horarioTurmaBanco = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(horarioTurmaVO.getTurma().getCodigo(), horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (horarioTurmaVO.getTurma().getIdentificadorTurma().equals("")) {
			throw new ConsistirException("O campo TURMA (Programação Aula) deve ser informado !");
		}
		if (horarioTurmaBanco != null && horarioTurmaBanco.getTurma().getCodigo() != 0) {
			throw new ConsistirException("A TURMA " + horarioTurmaBanco.getTurma().getIdentificadorTurma() + " já possui uma programação de aula.");
		}
		if (horarioTurmaVO.getTurma().getSemestral() || horarioTurmaVO.getTurma().getAnual()) {
			if (horarioTurmaVO.getAnoVigente().isEmpty()) {
				throw new ConsistirException("O campo Ano (Programação Aula) deve ser informado !");
			}
			if (horarioTurmaVO.getTurma().getSemestral() && horarioTurmaVO.getSemestreVigente().isEmpty()) {
				throw new ConsistirException("O campo Semestre (Programação Aula) deve ser informado !");
			}
		}
		if (horarioTurmaVO.getDisciplina().getCodigo() == 0) {
			throw new ConsistirException("O campo DISCIPLINA (Programação Aula) deve ser informado !");
		}
		if (horarioTurmaVO.getProfessor().getNome().equals("")) {
			throw new ConsistirException("O campo PROFESSOR (Programação Aula) deve ser informado !");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarMigracaoDados(UsuarioVO usuario) throws Exception {

		List<HorarioTurmaVO> listaHorarioTurma = getFacadeFactory().getHorarioTurmaFacade().consultarTodos(Uteis.NIVELMONTARDADOS_TODOS, usuario);
		Set<HorarioTurmaVO> listaHorarioTurma20111 = new HashSet<HorarioTurmaVO>(0);
		Set<HorarioTurmaVO> listaHorarioTurma20102 = new HashSet<HorarioTurmaVO>(0);
		Set<HorarioTurmaVO> listaHorarioTurma20101 = new HashSet<HorarioTurmaVO>(0);
		Set<HorarioTurmaVO> listaHorarioTurmasPosGraduacao = new HashSet<HorarioTurmaVO>(0);
		Set<Integer> listaCodigosJaAdicionados20111 = new HashSet<Integer>(0);
		Set<Integer> listaCodigosJaAdicionados20102 = new HashSet<Integer>(0);
		Set<Integer> listaCodigosJaAdicionados20101 = new HashSet<Integer>(0);
		Set<Integer> listaCodigosJaAdicionadosPos = new HashSet<Integer>(0);

		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurma) {

			if (horarioTurmaVO.getTurma().getSemestral() || horarioTurmaVO.getTurma().getAnual()) {
				for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {

					// 2011 | 1
					if ((Uteis.getAnoData(horarioTurmaDiaVO.getData()) == 2011) && (Uteis.getMesData(horarioTurmaDiaVO.getData()) >= 1) && (!listaCodigosJaAdicionados20111.contains(horarioTurmaVO.getCodigo()))) {
						HorarioTurmaVO horarioTurmaASerInserido = new HorarioTurmaVO();
						horarioTurmaASerInserido.setTurma(horarioTurmaVO.getTurma());
						horarioTurmaASerInserido.setAnoVigente("2011");
						horarioTurmaASerInserido.setSemestreVigente("1");
						horarioTurmaASerInserido.setProfessor(horarioTurmaVO.getProfessor());
						horarioTurmaASerInserido.setCalendarioHorarioAulaVOs(horarioTurmaVO.getCalendarioHorarioAulaVOs());
						horarioTurmaASerInserido.setDia(horarioTurmaVO.getDia());
						horarioTurmaASerInserido.setDisciplina(null);
						horarioTurmaASerInserido.setDiaFim(horarioTurmaVO.getDiaFim());
						horarioTurmaASerInserido.setDiaInicio(horarioTurmaVO.getDiaInicio());
						horarioTurmaASerInserido.setDisciplina(horarioTurmaVO.getDisciplina());
						horarioTurmaASerInserido.setDisciplinaAtual(horarioTurmaVO.getDisciplinaAtual());
						horarioTurmaASerInserido.setDisciplinaSubstituta(horarioTurmaVO.getDisciplinaSubstituta());
						horarioTurmaASerInserido.setListaCodigoProfessor(horarioTurmaVO.getListaCodigoProfessor());
						for (HorarioTurmaDiaVO horarioTDVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
							if ((Uteis.getAnoData(horarioTDVO.getData()) == 2011) && (Uteis.getMesData(horarioTDVO.getData()) >= 1)) {
								HorarioTurmaDiaVO hTDVO = new HorarioTurmaDiaVO();
								hTDVO.setData(horarioTDVO.getData());
								hTDVO.setHorarioTurmaDiaItemVOs(horarioTDVO.getHorarioTurmaDiaItemVOs());
								hTDVO.setHorarioTurma(horarioTurmaASerInserido);
								horarioTurmaASerInserido.getHorarioTurmaDiaVOs().add(hTDVO);
							}
						}
						listaHorarioTurma20111.add(horarioTurmaASerInserido);
						listaCodigosJaAdicionados20111.add(horarioTurmaVO.getCodigo());

						// 2010 | 2
					} else if ((Uteis.getAnoData(horarioTurmaDiaVO.getData()) == 2010) && (Uteis.getMesData(horarioTurmaDiaVO.getData()) > 7) && (!listaCodigosJaAdicionados20102.contains(horarioTurmaVO.getCodigo()))) {
						HorarioTurmaVO horarioTurmaASerInserido = new HorarioTurmaVO();
						horarioTurmaASerInserido.setTurma(horarioTurmaVO.getTurma());
						horarioTurmaASerInserido.setAnoVigente("2010");
						horarioTurmaASerInserido.setSemestreVigente("2");
						horarioTurmaASerInserido.setProfessor(horarioTurmaVO.getProfessor());
						horarioTurmaASerInserido.setCalendarioHorarioAulaVOs(horarioTurmaVO.getCalendarioHorarioAulaVOs());
						horarioTurmaASerInserido.setDia(horarioTurmaVO.getDia());
						horarioTurmaASerInserido.setDiaFim(horarioTurmaVO.getDiaFim());
						horarioTurmaASerInserido.setDiaInicio(horarioTurmaVO.getDiaInicio());
						horarioTurmaASerInserido.setDisciplina(horarioTurmaVO.getDisciplina());
						horarioTurmaASerInserido.setDisciplinaAtual(horarioTurmaVO.getDisciplinaAtual());
						horarioTurmaASerInserido.setDisciplinaSubstituta(horarioTurmaVO.getDisciplinaSubstituta());
						horarioTurmaASerInserido.setListaCodigoProfessor(horarioTurmaVO.getListaCodigoProfessor());
						for (HorarioTurmaDiaVO horarioTDVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
							if ((Uteis.getAnoData(horarioTDVO.getData()) == 2011) && (Uteis.getMesData(horarioTDVO.getData()) >= 1)) {
								HorarioTurmaDiaVO hTDVO = new HorarioTurmaDiaVO();
								hTDVO.setData(horarioTDVO.getData());
								hTDVO.setHorarioTurmaDiaItemVOs(horarioTDVO.getHorarioTurmaDiaItemVOs());
								hTDVO.setHorarioTurma(horarioTurmaASerInserido);
								horarioTurmaASerInserido.getHorarioTurmaDiaVOs().add(hTDVO);
							}
						}
						listaHorarioTurma20102.add(horarioTurmaASerInserido);
						listaCodigosJaAdicionados20102.add(horarioTurmaVO.getCodigo());
						// 2010 | 1
					} else if ((Uteis.getAnoData(horarioTurmaDiaVO.getData()) == 2010) && (Uteis.getMesData(horarioTurmaDiaVO.getData()) <= 6) && (!listaCodigosJaAdicionados20101.contains(horarioTurmaVO.getCodigo()))) {
						HorarioTurmaVO horarioTurmaASerInserido = new HorarioTurmaVO();
						horarioTurmaASerInserido.setTurma(horarioTurmaVO.getTurma());
						horarioTurmaASerInserido.setAnoVigente("2010");
						horarioTurmaASerInserido.setSemestreVigente("1");
						horarioTurmaASerInserido.setProfessor(horarioTurmaVO.getProfessor());
						horarioTurmaASerInserido.setCalendarioHorarioAulaVOs(horarioTurmaVO.getCalendarioHorarioAulaVOs());
						horarioTurmaASerInserido.setDia(horarioTurmaVO.getDia());
						horarioTurmaASerInserido.setDiaFim(horarioTurmaVO.getDiaFim());
						horarioTurmaASerInserido.setDiaInicio(horarioTurmaVO.getDiaInicio());
						horarioTurmaASerInserido.setDisciplina(horarioTurmaVO.getDisciplina());
						horarioTurmaASerInserido.setDisciplinaAtual(horarioTurmaVO.getDisciplinaAtual());
						horarioTurmaASerInserido.setDisciplinaSubstituta(horarioTurmaVO.getDisciplinaSubstituta());
						horarioTurmaASerInserido.setListaCodigoProfessor(horarioTurmaVO.getListaCodigoProfessor());
						for (HorarioTurmaDiaVO horarioTDVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
							if ((Uteis.getAnoData(horarioTDVO.getData()) == 2011) && (Uteis.getMesData(horarioTDVO.getData()) >= 1)) {
								HorarioTurmaDiaVO hTDVO = new HorarioTurmaDiaVO();
								hTDVO.setData(horarioTDVO.getData());
								hTDVO.setHorarioTurmaDiaItemVOs(horarioTDVO.getHorarioTurmaDiaItemVOs());
								hTDVO.setHorarioTurma(horarioTurmaASerInserido);
								horarioTurmaASerInserido.getHorarioTurmaDiaVOs().add(hTDVO);
							}
						}
						listaHorarioTurma20101.add(horarioTurmaASerInserido);
						listaCodigosJaAdicionados20101.add(horarioTurmaVO.getCodigo());
					}
				}

				// TURMAS DE POS GRADUACAO NAO CONTEM ANO E SEMESTRE
			} else {
				HorarioTurmaVO horarioTurmaASerInserido = new HorarioTurmaVO();
				horarioTurmaASerInserido.setTurma(horarioTurmaVO.getTurma());
				horarioTurmaASerInserido.setAnoVigente("");
				horarioTurmaASerInserido.setSemestreVigente("");
				horarioTurmaASerInserido.setProfessor(horarioTurmaVO.getProfessor());
				horarioTurmaASerInserido.setCalendarioHorarioAulaVOs(horarioTurmaVO.getCalendarioHorarioAulaVOs());
				horarioTurmaASerInserido.setDia(horarioTurmaVO.getDia());
				horarioTurmaASerInserido.setDiaFim(horarioTurmaVO.getDiaFim());
				horarioTurmaASerInserido.setDiaInicio(horarioTurmaVO.getDiaInicio());
				horarioTurmaASerInserido.setDisciplina(horarioTurmaVO.getDisciplina());
				horarioTurmaASerInserido.setDisciplinaAtual(horarioTurmaVO.getDisciplinaAtual());
				horarioTurmaASerInserido.setDisciplinaSubstituta(horarioTurmaVO.getDisciplinaSubstituta());
				horarioTurmaASerInserido.setListaCodigoProfessor(horarioTurmaVO.getListaCodigoProfessor());
				horarioTurmaASerInserido.getHorarioTurmaDiaVOs().addAll(horarioTurmaVO.getHorarioTurmaDiaVOs());
				listaHorarioTurmasPosGraduacao.add(horarioTurmaASerInserido);
				listaCodigosJaAdicionadosPos.add(horarioTurmaVO.getCodigo());

			}
		}

		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurma) {
			excluir(horarioTurmaVO, usuario);
		}
		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurma20101) {
			incluir(horarioTurmaVO, usuario);
		}
		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurma20102) {
			incluir(horarioTurmaVO, usuario);
		}
		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurma20111) {
			incluir(horarioTurmaVO, usuario);
		}
		for (HorarioTurmaVO horarioTurmaVO : listaHorarioTurmasPosGraduacao) {
			incluir(horarioTurmaVO, usuario);
		}

	}

	public StringBuilder getSqlConsultaRapida() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT  HorarioTurma.codigo,HorarioTurma.turma,Turma.identificadorTurma, Turma.anual, Turma.semestral, Turma.subturma, Turma.turmaagrupada, anovigente,semestrevigente,HorarioTurma.observacao, ");
		sqlStr.append(" curso.nome as  \"curso.nome\", curso.niveleducacional as  \"curso.niveleducacional\", turno.nome as \"turno.nome\",curso.codigo as  \"curso.codigo\", turno.codigo as \"turno.codigo\",");
		sqlStr.append(" unidadeensino.codigo as  \"unidadeensino.codigo\", ");
		sqlStr.append(" unidadeensino.nome as  \"unidadeensino.nome\", HorarioTurma.updated ");
		sqlStr.append(" FROM horarioturma ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = horarioturma.turma ");
		sqlStr.append(" left JOIN unidadeensino ON turma.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" Left JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append(" Left JOIN turno ON turno.codigo = turma.turno ");
		return sqlStr;
	}

	public List<HorarioTurmaVO> consultaRapidaPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, String semestreVigente, String anoVigente, String situacaoTurma, String tipoTurma, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaRapida();
		sqlStr.append(" left join turma as turmaprincipal on turmaprincipal.codigo = turma.turmaprincipal ");
		sqlStr.append(" WHERE (sem_acentos(turma.identificadorTurma)  ilike(sem_acentos(?)) or (turma.subturma and sem_acentos(turmaprincipal.identificadorTurma)  ilike(sem_acentos(?)) ) ) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (!semestreVigente.isEmpty()) {
			sqlStr.append(" AND HorarioTurma.semestrevigente = '").append(semestreVigente).append("' ");
		}
		if (!anoVigente.isEmpty()) {
			sqlStr.append(" AND HorarioTurma.anovigente = '").append(anoVigente).append("' ");
		}
		if (!situacaoTurma.isEmpty()) {
			sqlStr.append(" and turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (!tipoTurma.isEmpty()) {
			if (tipoTurma.equals("agrupada")) {
				sqlStr.append(" and turma.turmaagrupada = true ");
			}
			if (tipoTurma.equals("subturma")) {
				sqlStr.append(" and turma.subturma = true ");
			}
			if (tipoTurma.equals("normal")) {
				sqlStr.append(" and turma.subturma = false and turma.turmaagrupada = false ");
			}
		}
		sqlStr.append(" order by case when turma.subturma then turmaPrincipal.identificadorTurma::VARCHAR||'ZZZZ'||turma.identificadorTurma else turma.identificadorTurma end, turma.identificadorTurma, turma.codigo ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%", valorConsulta+"%");

		return montarDadosConsultaRapida(tabelaResultado);

	}

	public static List<HorarioTurmaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<HorarioTurmaVO> vetResultado = new ArrayList<HorarioTurmaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosRapida(tabelaResultado));
		}
		return vetResultado;
	}

	public static HorarioTurmaVO montarDadosRapida(SqlRowSet tabelaResultado) throws Exception {
		HorarioTurmaVO horarioTurma = new HorarioTurmaVO();
		horarioTurma.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		horarioTurma.getTurma().setCodigo(new Integer(tabelaResultado.getInt("turma")));
		horarioTurma.setAnoVigente(tabelaResultado.getString("anovigente"));
		horarioTurma.setSemestreVigente(tabelaResultado.getString("semestrevigente"));
		horarioTurma.setObservacao(tabelaResultado.getString("observacao"));
		horarioTurma.setNovoObj(false);
		horarioTurma.setUpdated(tabelaResultado.getTimestamp("updated"));
		horarioTurma.getTurma().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		horarioTurma.getTurma().setAnual(tabelaResultado.getBoolean("anual"));
		horarioTurma.getTurma().setSemestral(tabelaResultado.getBoolean("semestral"));
		horarioTurma.getTurma().setSubturma(tabelaResultado.getBoolean("subturma"));
		horarioTurma.getTurma().setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
		horarioTurma.getTurma().getCurso().setNome(tabelaResultado.getString("curso.nome"));
		horarioTurma.getTurma().getCurso().setNivelEducacional(tabelaResultado.getString("curso.niveleducacional"));
		horarioTurma.getTurma().getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
		horarioTurma.getTurma().getTurno().setNome(tabelaResultado.getString("turno.nome"));
		horarioTurma.getTurma().getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
		horarioTurma.getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
		horarioTurma.getTurno().setNome(tabelaResultado.getString("turno.nome"));
		horarioTurma.getTurma().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
		horarioTurma.getTurma().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino.nome"));
		return horarioTurma;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEnvioEmailAlunosTurmaPadrao(HorarioTurmaVO horarioTurmaVO, PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean enviarPorEmail) throws Exception {
		if (msg.isNovoObj() || msg.getDesabilitarEnvioMensagemAutomatica()) {
			throw new Exception("Não existe uma MENSAGEM PERSONALIZADA cadastrada ou habilidade no sistema.");
		}		
		StringBuilder listaHorariosAlterados = horarioTurmaVO.getTextoAlteracaoHorarioTurmaLog();
		List<PessoaVO> pessoaVOs = consultarDestinatariosDestinatarios(horarioTurmaVO, msg.getTemplateMensagemAutomaticaEnum() , usuarioVO, null);
		for (PessoaVO pessoaVO : pessoaVOs) {
			ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
			if (usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
				comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
			} else {
				comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
			}
			comunicacaoInternaVO.setEnviarEmail(enviarPorEmail);
			comunicacaoInternaVO.setEnviarEmailInstitucional(msg.getEnviarEmailInstitucional());
			comunicacaoInternaVO.setTipoDestinatario("AL");
			comunicacaoInternaVO.setTipoMarketing(false);
			comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
			comunicacaoInternaVO.setDigitarMensagem(true);
			comunicacaoInternaVO.setRemoverCaixaSaida(false);
			comunicacaoInternaVO.setAluno(pessoaVO);
			String mensagem = msg.getMensagem();
			mensagem = mensagem.replaceAll("TURMA", horarioTurmaVO.getTurma().getIdentificadorTurma());
			mensagem = mensagem.replaceAll("NOME_ALUNO", pessoaVO.getNome());
			mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.LISTA_PROGRAMACAO_AULA_DISCIPLINA_PERIODO.name(), Matcher.quoteReplacement(listaHorariosAlterados.toString()));			
			comunicacaoInternaVO.setAssunto(msg.getAssunto());
			comunicacaoInternaVO.setMensagem(mensagem);
			ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
			comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
			comunicadoInternoDestinatarioVO.setDataLeitura(null);
			comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
			comunicadoInternoDestinatarioVO.setCiJaLida(false);
			comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
			comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
			comunicadoInternoDestinatarioVO.setDestinatario(pessoaVO);
			comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
			if (pessoaVO.getCodigo() > 0) {
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
			}
		}
	}

	public List<PessoaVO> consultarDestinatariosDestinatarios(HorarioTurmaVO horarioTurmaVO,TemplateMensagemAutomaticaEnum templateMensagemAutomatica ,  UsuarioVO usuarioVO, List<Integer> listaDisciplinasAlteradasCodigo) throws Exception {
		try {
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (horarioTurmaVO.getTurma().getCodigo().intValue() != 0) {
				if (templateMensagemAutomatica.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_PROGRAMACAO_AULA)) {
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunosPorCodigoTurmaPadraoSituacao(horarioTurmaVO.getTurma().getCodigo(), "AT", Uteis.getAnoDataAtual4Digitos(), Uteis.getSemestreAtual(), false, usuarioVO);
					if (!objs.isEmpty()) {
						objs.add(usuarioVO.getPessoa());
					}
				}
				if(templateMensagemAutomatica.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_REPOSICAO_AULA)){
					objs = getFacadeFactory().getPessoaFacade().consultaAlunosAtivosParaReposicaoDisciplinaEnvioEmail(horarioTurmaVO.getTurma().getCodigo(), Uteis.getAnoDataAtual4Digitos(), Uteis.getSemestreAtual(), listaDisciplinasAlteradasCodigo, false, usuarioVO);
					if (!objs.isEmpty()) {
						objs.add(usuarioVO.getPessoa());
					}
				}				
				if(templateMensagemAutomatica.equals(templateMensagemAutomatica.MENSAGEM_NOTIFICACAO_COORDENADOR_PROGRAMACAO_AULA)) {
					objs  = getFacadeFactory().getPessoaFacade().consultarCoordenadoresCursoTurmaNotificacaoCronogramaAula(horarioTurmaVO.getTurma().getCodigo(),  usuarioVO);
				}		

			}
			return objs;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEnvioEmailAlunosInclusaoDisciplina(HorarioTurmaVO horarioTurmaVO, PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean enviarPorEmail, Map<Integer, String> hashDisciplinasAlteradas) throws Exception {
		if (msg.isNovoObj() || msg.getDesabilitarEnvioMensagemAutomatica()) {
			throw new Exception("Não existe uma MENSAGEM PERSONALIZADA cadastrada ou habilidade no sistema.");
		}
		StringBuilder listaHorariosAlterados = horarioTurmaVO.getTextoAlteracaoHorarioTurmaLog();
		if (!hashDisciplinasAlteradas.isEmpty()) {
			List<Integer> disciplinas = new ArrayList<Integer>(0);
			String disciplinasAlteradas = "";
			for (Integer i : hashDisciplinasAlteradas.keySet()) {
				if (!disciplinasAlteradas.equals("")) {
					disciplinasAlteradas += ", " + hashDisciplinasAlteradas.get(i);
				} else {
					disciplinasAlteradas = hashDisciplinasAlteradas.get(i);
				}
				disciplinas.add(i);
			}
			List<PessoaVO> pessoaVOs = consultarDestinatariosDestinatarios(horarioTurmaVO, msg.getTemplateMensagemAutomaticaEnum() , usuarioVO, disciplinas);
			for (PessoaVO pessoaVO : pessoaVOs) {
				if (!pessoaVO.getDisciplinas().trim().isEmpty()) {
					ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
					if (usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
						comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
					} else {
						comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
					}
					comunicacaoInternaVO.setEnviarEmail(enviarPorEmail);
					comunicacaoInternaVO.setEnviarEmailInstitucional(msg.getEnviarEmailInstitucional());
					comunicacaoInternaVO.setTipoDestinatario("AL");
					comunicacaoInternaVO.setTipoMarketing(false);
					comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
					comunicacaoInternaVO.setDigitarMensagem(true);
					comunicacaoInternaVO.setRemoverCaixaSaida(false);
					comunicacaoInternaVO.setAluno(pessoaVO);
					String mensagem = msg.getMensagem();
					mensagem = mensagem.replaceAll("TURMA", horarioTurmaVO.getTurma().getIdentificadorTurma());
					mensagem = mensagem.replaceAll("NOME_UNIDADE_ENSINO", horarioTurmaVO.getTurma().getUnidadeEnsino().getNome());
					mensagem = mensagem.replaceAll("NOME_ALUNO", pessoaVO.getNome());
					mensagem = mensagem.replaceAll("NOME_DISCIPLINA", pessoaVO.getDisciplinas());
					mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.LISTA_PROGRAMACAO_AULA_DISCIPLINA_PERIODO.name(), Matcher.quoteReplacement(listaHorariosAlterados.toString()));
					comunicacaoInternaVO.setAssunto(msg.getAssunto());
					comunicacaoInternaVO.setMensagem(mensagem);
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
					comunicadoInternoDestinatarioVO.setDataLeitura(null);
					comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
					comunicadoInternoDestinatarioVO.setCiJaLida(false);
					comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
					comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
					comunicadoInternoDestinatarioVO.setDestinatario(pessoaVO);
					comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
					try {
						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
					} catch (Exception e) {
						System.out.print("Erro ao enviar!");
					}
				}
			}
		}
	}

	@Override
	public Date consultarPrimeiroDiaAulaTurmaDisciplina(int turma, Integer disciplina, String ano, String semeste) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select min(data) as data from ");
		if (disciplina != null && disciplina > 0) {
			sql.append(" horarioturmadetalhado(" + turma + ", null, " + disciplina + ",null) as t ");
		} else {
			sql.append(" horarioturmadetalhado(" + turma + ", null, null,null) as t ");
		}
		sql.append(" inner join horarioturma on horarioturma.turma = t.turma ");
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and horarioturma.anovigente = '" + ano + "'");
		}
		if (semeste != null && !semeste.trim().isEmpty()) {
			sql.append(" and horarioturma.semestrevigente = '" + semeste + "'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getDate("data");
		}
		return null;
	}

	public Date consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(Integer codMptd) throws Exception {
		// StringBuilder sqlStr = new
		// StringBuilder("select max(htd.data) as data from matriculaperiodoturmadisciplina ");
		// sqlStr.append(" inner join turma on matriculaperiodoturmadisciplina.turma = turma.codigo");
		// sqlStr.append(" INNER JOIN horarioturma ht ON ht.turma = turma.codigo");
		// sqlStr.append(" INNER JOIN horarioturmadia htd ON htd.horarioturma = ht.codigo");
		// sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = (select matricula from matriculaperiodoturmadisciplina where codigo = ").append(codMptd).append(")");
		StringBuilder sqlStr = new StringBuilder(" select max(t.data) as data from horarioturmadetalhado(null, null, null, null, null,null,null,null) as t inner join matriculaperiodoturmadisciplina on  ");
		sqlStr.append(" (matriculaperiodoturmadisciplina.turma = t.turma or t.turma in (select turmaorigem from turmaagrupada  where turma = matriculaperiodoturmadisciplina.turma)) ");
		sqlStr.append(" and (matriculaperiodoturmadisciplina.disciplina = t.disciplina or t.disciplina in  (select disciplina from gradedisciplinacomposta where gradedisciplinacomposta.gradedisciplina = matriculaperiodoturmadisciplina.gradedisciplina ) ");
		sqlStr.append(" or t.disciplina in  (select disciplina from gradedisciplinacomposta where gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina )) ");
		sqlStr.append(" where matricula =  (select matricula from matriculaperiodoturmadisciplina where codigo = ").append(codMptd).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return tabelaResultado.getDate("data");
		}
		return null;
	}

	public Date consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(String matricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select  max((select datatermino from periodoauladisciplinaaluno(historico.codigo)) ) as data from matricula ");
		sqlStr.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = ( ");
		sqlStr.append(" select mp.codigo from matriculaperiodo as mp where  mp.situacaomatriculaperiodo != 'PC' ");
		sqlStr.append(" and mp.matricula = matricula.matricula ");
		sqlStr.append(" order by mp.ano desc, mp.semestre desc,  case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ");
		sqlStr.append(" ) ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" where matricula.matricula =  ? ");
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		while (tabelaResultado.next()) {
			return tabelaResultado.getDate("data");
		}
		return null;
	}

	public List<HorarioTurmaDisciplinaSemanalVO> consultarHorarioTurmaSemanalPorTurmaDisciplina(Integer turma, Integer disciplina, String ano, String semestre, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT distinct horarioturma.anovigente, horarioturma.semestrevigente, ");
		sb.append(" min(horarioturmadiaitem.data) as inicio, ");
		sb.append(" max(horarioturmadiaitem.data) as termino, ");
		sb.append(" cast(extract(dow from horarioturmadiaitem.data) as integer) diasemana, horarioturmadiaitem.nraula ");
		sb.append(" FROM horarioturma ");
		sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sb.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
		sb.append(" inner join turma on horarioturma.turma = turma.codigo ");
		sb.append(" where disciplina.codigo =  ").append(disciplina);
		sb.append(" and  (turma.turmaagrupada = false and turma.codigo =  ").append(turma).append(") ");
		sb.append(" or  (turma.turmaagrupada and turma.codigo in (select tagr.turma  from turmaagrupada where tagr.turmaorigem =  ").append(turma).append("))) ");
		sb.append(" and ((turma.anual and anovigente = '").append(ano).append("') ");
		sb.append(" or (turma.semestral and anovigente = '").append(ano).append("' and semestrevigente = '").append(semestre).append("') ");
		sb.append(" or (turma.semestral = false and turma.anual = false )) ");
		sb.append(" group by horarioturma.anovigente, horarioturma.semestrevigente, cast(extract(dow from horarioturmadiaitem.data) as integer) diasemana, horarioturmadiaitem.nraula ");		
		sb.append(" order by diasemana, nraula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<HorarioTurmaDisciplinaSemanalVO> listaHorarioTurmaDisciplinaSemanalVOs = new ArrayList<HorarioTurmaDisciplinaSemanalVO>(0);
		for (int i = 0; i <= 6; i++) {
			HorarioTurmaDisciplinaSemanalVO obj = new HorarioTurmaDisciplinaSemanalVO();
			obj.setDiaSemana(Uteis.getDiaSemana(i));
			obj.setNrDiaSemana(i);
			listaHorarioTurmaDisciplinaSemanalVOs.add(obj);
		}
		while (tabelaResultado.next()) {
			for (HorarioTurmaDisciplinaSemanalVO horarioTurmaDisciplinaSemanalVO : listaHorarioTurmaDisciplinaSemanalVOs) {
				horarioTurmaDisciplinaSemanalVO.setDataInicio(tabelaResultado.getDate("inicio"));
				horarioTurmaDisciplinaSemanalVO.setDataTermino(tabelaResultado.getDate("termino"));
				if (horarioTurmaDisciplinaSemanalVO.getNrDiaSemana().equals(tabelaResultado.getInt("diasemana"))) {
					horarioTurmaDisciplinaSemanalVO.getListaHorario().add(tabelaResultado.getString("nraula") + "º Horário");
					break;
				}
			}
		}
		return listaHorarioTurmaDisciplinaSemanalVOs;
	}

	@Override
	public void inicializarListaHorarioTurmaDisciplinaProgramada(HorarioTurmaVO horarioTurmaVO, boolean permitirProgramacaoAulaComClassroom, boolean permitirProgramacaoAulaComBlackboard, UsuarioVO usuarioVO) throws Exception {
		Map<Integer, Integer> mapDisciplinaProgramada = new HashMap<Integer, Integer>(0);
		Map<Integer, String> mapDisciplinaProfessor = new HashMap<Integer, String>(0);
		try {
			horarioTurmaVO.setHorarioTurmaDisciplinaProgramadaVOs(this.consultarHorarioTurmaDisciplinaProgramadaPorTurma(horarioTurmaVO.getTurma().getCodigo(), false, true, horarioTurmaVO.getCodigo()));
			for (HorarioTurmaDiaVO htd : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
				for (HorarioTurmaDiaItemVO htdi : htd.getHorarioTurmaDiaItemVOs()) {
					Integer duracaoAula = htdi.getDuracaoAula();
					if (horarioTurmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario().booleanValue()) {
						duracaoAula = 60;
					}
					if (htdi.getIsHorarioOcupado()) {
						if (mapDisciplinaProgramada.containsKey(htdi.getDisciplinaVO().getCodigo())) {
							mapDisciplinaProgramada.put(htdi.getDisciplinaVO().getCodigo(), mapDisciplinaProgramada.get(htdi.getDisciplinaVO().getCodigo()) + duracaoAula);
							if (!mapDisciplinaProfessor.get(htdi.getDisciplinaVO().getCodigo()).contains(htdi.getFuncionarioVO().getNome())) {
								mapDisciplinaProfessor.put(htdi.getDisciplinaVO().getCodigo(), mapDisciplinaProfessor.get(htdi.getDisciplinaVO().getCodigo()) + ", " + htdi.getFuncionarioVO().getNome());
							}
						} else {
							mapDisciplinaProgramada.put(htdi.getDisciplinaVO().getCodigo(), duracaoAula);
							mapDisciplinaProfessor.put(htdi.getDisciplinaVO().getCodigo(), htdi.getFuncionarioVO().getNome());
						}
					}
				}
			}

			for (HorarioTurmaDisciplinaProgramadaVO htdp : horarioTurmaVO.getHorarioTurmaDisciplinaProgramadaVOs()) {
				Integer chProgramada = 0;
				if (mapDisciplinaProgramada.containsKey(htdp.getCodigoDisciplina())) {
					chProgramada = mapDisciplinaProgramada.get(htdp.getCodigoDisciplina());
					htdp.setChProgramada(mapDisciplinaProgramada.get(htdp.getCodigoDisciplina()) / 60);
					String professores = (mapDisciplinaProfessor.get(htdp.getCodigoDisciplina()));
					htdp.setProfessores(professores);
					for(String prof: professores.split(",")){
						htdp.getProfessorVOs().add(prof.trim());
					}
				}
				htdp.setTurmaAulaProgramadaVOs(realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(horarioTurmaVO.getTurma(), htdp.getCodigoDisciplina(), horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), false));
				for(HorarioTurmaDisciplinaProgramadaVO horarioTurmaVO2: htdp.getTurmaAulaProgramadaVOs()){
//					chProgramada += horarioTurmaVO2.getChProgramada();
					horarioTurmaVO2.setChProgramada(horarioTurmaVO2.getChProgramada()/60);
					for(String prof: horarioTurmaVO2.getProfessores().split(",")){
						if(!htdp.getProfessorVOs().contains(prof)){
							htdp.setProfessores(htdp.getProfessores()+ (htdp.getProfessores().isEmpty()? prof: ", "+prof));
							htdp.getProfessorVOs().add(prof.trim());
						}
					}
				}
				
				htdp.setChProgramada(chProgramada / 60);
				realizarInicializacaoSalaAulaIntegracao(horarioTurmaVO, htdp, permitirProgramacaoAulaComClassroom, permitirProgramacaoAulaComBlackboard, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			mapDisciplinaProgramada = null;
			mapDisciplinaProfessor = null;
		}
	}

	private void realizarInicializacaoSalaAulaIntegracao(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDisciplinaProgramadaVO htdp, boolean permitirProgramacaoAulaComClassroom, boolean permitirProgramacaoAulaComBlackboard, UsuarioVO usuarioVO) throws Exception {
		if(permitirProgramacaoAulaComClassroom) {
			htdp.setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().consultarSeExisteClassroom(horarioTurmaVO.getTurma().getCodigo(), htdp.getCodigoDisciplina(), horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), null));
			if(!Uteis.isAtributoPreenchido(htdp.getClassroomGoogleVO())) {
				DisciplinaVO disc = new DisciplinaVO();
				disc.setCodigo(htdp.getCodigoDisciplina());
				disc.setNome(htdp.getNomeDisciplina());
				htdp.setClassroomGoogleVO(new ClassroomGoogleVO(horarioTurmaVO.getTurma(), disc, horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente()));
			}	
		}
		if(permitirProgramacaoAulaComBlackboard) {
			htdp.setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSeExisteSalaAulaBlackboard(TipoSalaAulaBlackboardEnum.DISCIPLINA, horarioTurmaVO.getTurma().getCurso().getCodigo(), horarioTurmaVO.getTurma().getCodigo(), htdp.getCodigoDisciplina(), horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), null, null, null, usuarioVO));
			if(!Uteis.isAtributoPreenchido(htdp.getSalaAulaBlackboardVO())) {
				DisciplinaVO disc = new DisciplinaVO();
				disc.setCodigo(htdp.getCodigoDisciplina());
				disc.setNome(htdp.getNomeDisciplina());
				htdp.setSalaAulaBlackboardVO(new SalaAulaBlackboardVO(horarioTurmaVO.getTurma().getCurso(), horarioTurmaVO.getTurma(), disc, horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente()));
				htdp.getSalaAulaBlackboardVO().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(htdp.getSalaAulaBlackboardVO().getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioVO));
			}	
		}
		
	}

	@Override
	public List<HorarioTurmaDisciplinaProgramadaVO> consultarHorarioTurmaDisciplinaProgramadaPorTurma(Integer turma, boolean trazerDisciplinaCompostaPrincipal, Boolean permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica, Integer horarioTurma) throws Exception {

		StringBuilder sqlStr = new StringBuilder("");
		// Tras as disciplina compostas da grade disciplina de uma turma normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append("left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union ");
		// Tras as disciplina da grade disciplina de uma turma normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
		sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union ");
		// Tras as disciplina compostas da grade disciplina de uma turma
		// composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradedisciplinacomposta.cargahoraria) as cargahoraria, max(gradedisciplinacomposta.nrCreditos) as nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina2.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union");
		// Tras as disciplina da grade disciplina de uma turma composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradedisciplina.cargahoraria) as cargahoraria, max(gradedisciplina.nrCreditos) as nrCreditos, max(gradedisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
		sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union ");
		// Tras as disciplina compostas de um grupo de optativas de uma turma
		// normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradedisciplinacomposta.cargahoraria) as cargahoraria, max(gradedisciplinacomposta.nrCreditos) as nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union ");
		// Tras as disciplina de um grupo de optativas de uma turma normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union ");
		// Tras as disciplina compostas de um grupo de optativas de uma turma
		// composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradedisciplinacomposta.cargahoraria) as cargahoraria, max(gradedisciplinacomposta.nrCreditos) as nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina2.codigo ");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		sqlStr.append(" union ");
		// Tras as disciplina de um grupo de optativas de uma turma composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradeCurricularGrupoOptativaDisciplina.cargahoraria) as cargahoraria, max(gradeCurricularGrupoOptativaDisciplina.nrCreditos) as nrCreditos, max(gradeCurricularGrupoOptativaDisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo, horarioturmadisciplinaprogramada.conteudo from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
			sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");

		if (trazerDisciplinaCompostaPrincipal) {
			// Tras as disciplina compostas da grade disciplina de uma turma
			// normal
			sqlStr.append(" union");
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradedisciplina.cargahoraria) as cargahoraria, max(gradedisciplina.nrCreditos) as nrCreditos, max(gradedisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
			sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
				sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
			sqlStr.append(" union");
			// Tras as disciplina compostas da grade disciplina de uma turma
			// composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradedisciplina.cargahoraria) as cargahoraria, max(gradedisciplina.nrCreditos) as nrCreditos, max(gradedisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
			sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
				sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
			sqlStr.append(" union");
			// Tras as disciplina compostas de um grupo de optativas de uma
			// turma
			// normal
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradeCurricularGrupoOptativaDisciplina.cargahoraria) as cargahoraria, max(gradeCurricularGrupoOptativaDisciplina.nrCreditos) as nrCreditos, max(gradeCurricularGrupoOptativaDisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
				sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
			sqlStr.append(" union");
			// Tras as disciplina compostas de um grupo de optativas de uma
			// turma
			// composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, max(gradeCurricularGrupoOptativaDisciplina.cargahoraria) as cargahoraria, max(gradeCurricularGrupoOptativaDisciplina.nrCreditos) as nrCreditos, max(gradeCurricularGrupoOptativaDisciplina.horaaula) as horaaula, turmadisciplina.modalidadedisciplina as modalidadeDisciplina, turmadisciplina.definicoestutoriaonline as definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.codigo as \"horarioturmadisciplinaprogramada.codigo\", horarioturmadisciplinaprogramada.conteudo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" left join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = ").append(horarioTurma).append(" and disciplina.codigo = horarioturmadisciplinaprogramada.disciplina");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if(!permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica) {
				sqlStr.append(" and turmadisciplina.definicoestutoriaonline <> 'DINAMICA' ");
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, turmadisciplina.modalidadedisciplina, turmadisciplina.definicoestutoriaonline, horarioturmadisciplinaprogramada.registraraulaautomaticamente, horarioturmadisciplinaprogramada.conteudo, horarioturmadisciplinaprogramada.codigo ");
		}
		sqlStr.append(" order by nome ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = new ArrayList<HorarioTurmaDisciplinaProgramadaVO>(0);
		HorarioTurmaDisciplinaProgramadaVO obj = null;
		while (rs.next()) {
			obj = new HorarioTurmaDisciplinaProgramadaVO();
			obj.setCodigo(rs.getInt("horarioturmadisciplinaprogramada.codigo"));
			obj.setCodigoDisciplina(rs.getInt("codigo"));
			obj.setNomeDisciplina(rs.getString("nome"));
			obj.setChDisciplina(rs.getInt("cargahoraria"));
			obj.setCreditoDisciplina(rs.getInt("nrCreditos"));
			obj.setHoraAula(rs.getInt("horaAula"));
			obj.setModalidadeDisciplina(rs.getString("modalidadeDisciplina"));
			obj.setDefinicoesTutoriaOnline(rs.getString("definicoestutoriaonline"));
			obj.setRegistrarAulaAutomaticamente(rs.getBoolean("registrarAulaAutomaticamente"));
			obj.setConteudo(rs.getString("conteudo"));
			horarioTurmaDisciplinaProgramadaVOs.add(obj);
		}
		return horarioTurmaDisciplinaProgramadaVOs;
	}

	/**
	 * Este mï¿½todo realizar uma consulta utilizando a view
	 * horarioturmadetalhado que utiliza como base os parametros abaixo
	 * 
	 * @param horarioTurma
	 *            - se 0 serï¿½ considerado null
	 * @param turma
	 *            - se 0 serï¿½ considerado null
	 * @param ano
	 *            - se "" irï¿½ buscar os horarios das turmas onde o campo
	 *            anoVigente for igual a "" (para ignorar o filtro utilize null
	 *            como parï¿½metro)
	 * @param semestre
	 *            - se "" irï¿½ buscar os horarios das turmas onde o campo
	 *            semestreVigente for igual a "" (para ignorar o filtro utilize
	 *            null como parï¿½metro)
	 * @param professor
	 *            - se 0 irï¿½ buscar os horarios das turmas onde o horï¿½rio
	 *            estiver livre (para ignorar o filtro utilize null como
	 *            parï¿½metro)
	 * @param disciplina
	 *            - se 0 irï¿½ buscar os horarios das turmas onde o horï¿½rio
	 *            estiver livre (para ignorar o filtro utilize null como
	 *            parï¿½metro)
	 * @param dataInicio
	 *            && @param dataFim - Este dois filtros possue a seguinte regra
	 *            de usu: 1 - se dataInicio && dataFim = null entï¿½o o filtro
	 *            serï¿½ ignorado 2 - se dataInicio != null && dataFim = null
	 *            entï¿½o buscar o horï¿½rio do dia informado na dataInicio 3 -
	 *            se dataInicio == null && dataFim != null entï¿½o buscar o
	 *            horï¿½rio do dia informado na dataFim 4 - se dataInicio !=
	 *            null && dataFim != null entï¿½o buscar o horï¿½rio no
	 *            perï¿½odo informado
	 * @return Para ignorar um dos filtros da busca deverï¿½ ser passado null
	 *         como parï¿½metro ou 0 para os filtros horarioTurma, turma
	 * 
	 *         OBS = UTILIZAR PARA AS CONSULTAS O ORDER BY order by
	 *         horarioturma, data, nraula
	 */
	@Override
	public StringBuilder getSqlConsultaCompleta(Integer horarioTurma, Integer turma, String identificadorTurma, String ano, String semestre, Integer professor, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino) {
		StringBuilder sql = new StringBuilder("");		
			sql.append(" select distinct horarioturma.codigo as horarioturma,  ");
			sql.append(" horarioturma.turma, turma.identificadorturma, ");
			sql.append(" horarioturmadia.codigo as horarioturmadia, horarioturmadia.usuarioResp, horarioturmadia.data,  horarioturmadiaitem.codigo as horarioturmadiaitem, ");
			sql.append(" horarioturmadiaitem.disciplina, disciplina.nome as \"disciplina.nome\", horarioturmadiaitem.professor, ");
			sql.append("  horarioturmadiaitem.nraula, horarioturmadiaitem.duracaoaula, horarioturmadiaitem.aulareposicao, ");
			sql.append(" horarioturmadiaitem.horarioinicio as horarioinicioaula,  horarioturmadiaitem.horariotermino as horariofinalaula, ('0'::VARCHAR(1)||TO_CHAR(horarioturmadia.data, 'D'))::VARCHAR(2) as diasemana, ");
			sql.append(" turno.codigo as turno, horarioturmadia.ocultarDataAula, unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");
			sql.append(" cidade.codigo as \"cidade.codigo\", cidade.nome as \"cidade.nome\", estado.codigo as \"estado.codigo\", estado.sigla as \"estado.sigla\", ");
			sql.append(" googlemeet.codigo as \"googlemeet.codigo\",  ");
			sql.append(" professor.nome as \"professor.nome\", professor.email as \"professor.email\", ");
			sql.append(" horarioturma.anovigente, horarioturma.semestrevigente, horarioturma.observacao,");
			sql.append(" turno.nome as \"turno.nome\", registroaula.codigo is not null possuiAulaRegistrada, ");
			sql.append(" horarioturmadiaitem.sala, sala.sala as \"sala.sala\", localaula.local as \"localaula.local\", localaula.codigo as \"localaula.codigo\", horarioturma.updated ");
			sql.append(" from horarioturma ");
			sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
			if (Uteis.isAtributoPreenchido(horarioTurma)) {
				sql.append(" and horarioTurma.codigo = ").append(horarioTurma).append(" ");
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sql.append(" and horarioTurma.anovigente = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" and horarioTurma.semestrevigente = '").append(semestre).append("' ");
			}
			if (Uteis.isAtributoPreenchido(dataInicio) || Uteis.isAtributoPreenchido(dataFim)) {
				sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "data", false));
			}
			sql.append(" left join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
			sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
			if (Uteis.isAtributoPreenchido(turma)) {
				sql.append(" and turma.codigo = ").append(turma).append(" ");
			}
			if (turma == null && identificadorTurma != null && !identificadorTurma.trim().isEmpty()) {
				sql.append(" and turma.identificadorTurma ilike ('").append(identificadorTurma).append("')");
			}
			sql.append(" inner join turno on turno.codigo = turma.turno ");
			sql.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
			}
			sql.append(" left join googlemeet on googlemeet.codigo = horarioturmadiaitem.googlemeet ");
			sql.append(" left join salalocalaula as sala on sala.codigo = horarioturmadiaitem.sala ");
			sql.append(" left join localaula on localaula.codigo = sala.localaula ");
			sql.append(" left join registroaula on turma.codigo = registroaula.turma and horarioturmadiaitem.disciplina = registroaula.disciplina  and registroaula.data::DATE = horarioturmadia.data::DATE and registroaula.nrAula = horarioturmadiaitem.nrAula ");
			sql.append(" left join cidade on unidadeensino.cidade = cidade.codigo");
			sql.append(" left join estado on cidade.estado = estado.codigo");
			if (Uteis.isAtributoPreenchido(professor)) {
				sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
				sql.append(" and professor.codigo = ").append(professor).append(" ");
			} else {
				sql.append(" left join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
			}
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
				sql.append(" and disciplina.codigo = ").append(disciplina).append(" ");
			} else {
				sql.append(" left join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
			}
		
		return sql;
	}

	@Override
	public String getSqlOrdenarConsultaCompleta() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" order by horarioturma, data, nraula");
		return sql.toString();
	}

	@Override
	public void carregarDadosHorarioTurma(HorarioTurmaVO horarioTurmaVO, Integer professor, Integer disciplina, UsuarioVO usuario) throws Exception {
		if (horarioTurmaVO.getCodigo() > 0) {
			StringBuilder sql = getSqlConsultaCompleta(horarioTurmaVO.getCodigo(), null, null, null, null, professor, disciplina, null, null, null);
			sql.append(getSqlOrdenarConsultaCompleta());
			montarDadosConsultaCompleta(horarioTurmaVO, getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), usuario);
			montarDadosIsLancadoRegistro(horarioTurmaVO);
		} else {
			horarioTurmaVO.getHorarioTurmaDiaVOs().clear();
		}
	}

	private void montarDadosConsultaCompleta(HorarioTurmaVO horarioTurmaVO, SqlRowSet rs, UsuarioVO usuario) throws Exception {
		Integer horarioTurma = 0;
		Integer horarioTurmaDia = null;
		HorarioTurmaDiaVO horarioTurmaDiaVO = null;
		horarioTurmaVO.getHorarioTurmaDiaVOs().clear();
		while (rs.next()) {
			if (horarioTurma == null || horarioTurma == 0 || !horarioTurma.equals(horarioTurmaVO.getCodigo())) {
				horarioTurmaVO.setCodigo(rs.getInt("horarioturma"));
				horarioTurmaVO.setNovoObj(false);
				horarioTurmaVO.setUpdated(rs.getTimestamp("updated"));
				horarioTurmaVO.setAnoVigente(rs.getString("anoVigente"));
				horarioTurmaVO.setSemestreVigente(rs.getString("semestrevigente"));
				horarioTurmaVO.setObservacao(rs.getString("observacao"));
				horarioTurmaVO.getTurma().setCodigo(rs.getInt("turma"));
				horarioTurmaVO.getTurma().setIdentificadorTurma(rs.getString("identificadorTurma"));
				horarioTurmaVO.getTurma().getTurno().setCodigo(rs.getInt("turno"));
				horarioTurmaVO.getTurma().getTurno().setNome(rs.getString("turno.nome"));
				horarioTurmaVO.getTurno().setCodigo(rs.getInt("turno"));
				horarioTurmaVO.getTurno().setNome(rs.getString("turno.nome"));
				horarioTurmaVO.getTurma().getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
				horarioTurmaVO.getTurma().getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
				horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().setCodigo(rs.getInt("cidade.codigo"));
				horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().setNome(rs.getString("cidade.nome"));
				horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().getEstado().setCodigo(rs.getInt("estado.codigo"));
				horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().getEstado().setSigla(rs.getString("estado.sigla"));
				horarioTurma = horarioTurmaVO.getCodigo();
			}
			if (horarioTurmaDia == null || !horarioTurmaDia.equals(rs.getInt("horarioturmadia"))) {
				horarioTurmaDia = rs.getInt("horarioturmadia");
				horarioTurmaDiaVO = getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosConsultaCompletaHorarioTurmaDia(rs, usuario);
				horarioTurmaVO.getHorarioTurmaDiaVOs().add(horarioTurmaDiaVO);
			}
			HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = new HorarioTurmaDiaItemVO();
			horarioTurmaDiaItemVO = getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosConsultaCompletaHorarioTurmaDiaItem(rs, usuario);
			if (Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getCodigo())) {
				horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().add(horarioTurmaDiaItemVO);
			}
		}

	}

	/**
	 * Este mï¿½todo ï¿½ responsavel por alterar o horario da turma e o horario
	 * do professor quando estï¿½ realizando uma substituiï¿½ï¿½o do professor
	 * ou de uma disciplina Segue as regras verificadas 1 - Chama um metodo que
	 * verifica se estï¿½ alterando apenas a disciplina pois neste caso nï¿½o
	 * ï¿½ realizada a verificaï¿½ï¿½o do choque de horario 2 - Chama um metodo
	 * que verifica se estï¿½ alterando o professor pois neste caso ï¿½
	 * realizada a verificaï¿½ï¿½o do choque de horario 3 - Chama um mï¿½todo
	 * que altera o horario da turma 4 - Chama um mï¿½todo que cria novamente o
	 * Calendario do Horario da Turma
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVO
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> executarAlteracaoAulaHorarioTurmaProfessorDisciplina(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, int numeroAula, boolean alterarTodasAulas, UsuarioVO usuario, boolean retornarExecaoRegistroAulaLancada, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarAulaAcimaLimite, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		HorarioTurma.alterar(getIdEntidade(), true, usuario);
		HorarioTurmaVO.validarDadosAlteracaoHorario(horarioTurmaVO);
		boolean alterou = false;
		List<RegistroAulaVO> registroAulaVOs = null;
		List<HorarioTurmaDiaVO> horarioTurmaDiaVOs = null;
		List<Date> datas = new ArrayList<Date>();
		Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = null;
		Date dataInicio = null;
		Date dataFim = null;
		try {			
			if(Uteis.isAtributoPreenchido(horarioTurmaVO.getProfessorSubstituto())) {
				adicionarProfessorManipularControleConcorrencia(horarioTurmaVO, horarioTurmaVO.getProfessorSubstituto(), "Alterar Disciplina do Professor no Horario da Turma", usuario);
				if (!horarioTurmaVO.getProfessorAtual().getCodigo().equals(horarioTurmaVO.getProfessorSubstituto().getCodigo())) {
					adicionarProfessorManipularControleConcorrencia(horarioTurmaVO, horarioTurmaVO.getProfessorAtual(), "Alterar Disciplina do Professor no Horario da Turma", usuario);
				}
			}
			HorarioTurmaVO.validarDadosAlteracaoHorario(horarioTurmaVO);
			if (!horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty() && alterarTodasAulas) {
				dataInicio = horarioTurmaVO.getHorarioTurmaDiaVOs().get(0).getData();
				dataFim = horarioTurmaVO.getHorarioTurmaDiaVOs().get(horarioTurmaVO.getHorarioTurmaDiaVOs().size() - 1).getData();
			}
			if (horarioTurmaDiaVO != null && horarioTurmaDiaVO.getData() != null && !alterarTodasAulas) {
				dataInicio = horarioTurmaDiaVO.getData();
				dataFim = horarioTurmaDiaVO.getData();
			}
			horarioTurmaVO.setDisciplinaSubstituta(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(horarioTurmaVO.getDisciplinaSubstituta().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			if (Uteis.isAtributoPreenchido(horarioTurmaVO.getSalaSubstituta().getCodigo())) {
				horarioTurmaVO.setSalaSubstituta(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(horarioTurmaVO.getSalaSubstituta().getCodigo()));
				horarioTurmaVO.getSalaSubstituta().setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(horarioTurmaVO.getSalaSubstituta().getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
			registroAulaVOs = getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorTurmaDisciplinaPeriodo(horarioTurmaVO.getTurma().getCodigo(), horarioTurmaVO.getDisciplinaAtual().getCodigo(), dataInicio, dataFim, horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, Boolean.FALSE);
			horarioTurmaDiaVOs = realizarObtencaoHorarioTurmaDiaSeremAlteradoOuExcluido(horarioTurmaVO, horarioTurmaDiaVO, alterarTodasAulas, horarioTurmaVO.getProfessorAtual().getCodigo(), horarioTurmaVO.getDisciplinaAtual().getCodigo(), numeroAula, registroAulaVOs);
			Ordenacao.ordenarLista(horarioTurmaDiaVOs, "data");
			for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaDiaVOs) {
				datas.add(horarioTurmaDiaVO2.getData());
			}
			for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaDiaVOs) {
				if (verificarPermissaoUsuarioExcluirHorarioTurmaDia(usuario)) {
					if (horarioTurmaDiaVO2.getUsuarioResp().getCodigo().intValue() != usuario.getCodigo().intValue()) {
						throw new Exception("Não é possível realizar a alteração da aula selecionada! Somente o usuário " + horarioTurmaDiaVO2.getUsuarioResp().getNome() + " tem permissão para alterar este registro,ele é o responsável pelo cadastro! ");
					}
				}
			}
			if (retornarExecaoRegistroAulaLancada) {
				validacoes = new HashMap<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>>();
				validacoes.put(TipoValidacaoChoqueHorarioEnum.ERRO, new ArrayList<ChoqueHorarioVO>());
				validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).addAll(realizarValidacaoExclusaoHorarioTurmaPorProfessorDisciplina(horarioTurmaVO, registroAulaVOs));
				if (!horarioTurmaVO.getSalaAtual().getCodigo().equals(horarioTurmaVO.getSalaSubstituta().getCodigo()) && horarioTurmaVO.getSalaSubstituta().getControlarChoqueSala()) {
					validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).addAll(realizarValidacaoChoqueHorarioTrocaSala(horarioTurmaVO, horarioTurmaDiaVOs, numeroAula, alterarTodasAulas));
				}
				validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).addAll(realizarValidacaoNumeroAulaProgramaProfessorNaTrocaProfessor(horarioTurmaVO, horarioTurmaDiaVOs, datas, numeroAula, alterarTodasAulas, controlaNumeroMaximoAulaProgramadaProfessorDia, numeroAulaMaximaProgramarProfessorDia, liberarAulaAcimaLimite));
				if (validacoes.containsKey(TipoValidacaoChoqueHorarioEnum.ERRO) && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {
					return validacoes;
				}
			}
			realizarAlteracaoDisciplinaHorarioProfessorSubstitutoIgualProfessorSusbtituido(horarioTurmaVO, horarioTurmaDiaVOs, datas, numeroAula, alterarTodasAulas, registroAulaVOs, usuario);
			realizarAlteracaoDisciplinaHorarioProfessorSubstitutoDiferenteProfessorSusbtituido(horarioTurmaVO, horarioTurmaDiaVOs, datas, numeroAula, alterarTodasAulas, registroAulaVOs, usuario, controlaNumeroMaximoAulaProgramadaProfessorDia, numeroAulaMaximaProgramarProfessorDia, liberarAulaAcimaLimite, !horarioTurmaVO.getSalaAtual().getCodigo().equals(horarioTurmaVO.getSalaSubstituta().getCodigo()) && horarioTurmaVO.getSalaSubstituta().getControlarChoqueSala());
			realizarAlteracaoProfessorDisciplinaHorarioTurma(horarioTurmaVO, horarioTurmaDiaVOs, numeroAula, alterarTodasAulas, registroAulaVOs, usuario, true, liberarAulaAcimaLimite, funcionarioCargoVO);
			alterou = true;
			incluirDataFinalizacaoEConfirmacaoTurma(horarioTurmaVO, usuario);
			horarioTurmaVO.montarDadosHorarioDisciplinaProfessorDia();
			getFacadeFactory().getHorarioTurmaProfessorDisciplinaFacade().alterarHorarioTurmaProfessorDisciplinas(horarioTurmaVO.getCodigo(), horarioTurmaVO.getListaProfessorDisciplina());
			realizarCriacaoCalendarioHorarioTurma(horarioTurmaVO, usuario);
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().incluirProfessorTitularDisciplinaTurma(horarioTurmaVO.getProfessorSubstituto(), horarioTurmaVO.getDisciplinaSubstituta(), horarioTurmaVO.getTurma(), horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), usuario);
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluirComBaseNaProgramacaoAula(horarioTurmaVO.getTurma().getCodigo(), horarioTurmaVO.getProfessorAtual().getCodigo(), horarioTurmaVO.getDisciplinaAtual().getCodigo(), horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), usuario);
			
			horarioTurmaVO.getDisciplinaAtual().setCodigo(horarioTurmaVO.getDisciplinaSubstituta().getCodigo());
			horarioTurmaVO.getDisciplinaAtual().setNome(horarioTurmaVO.getDisciplinaSubstituta().getNome());
			horarioTurmaVO.setDisciplinaSubstituta(null);
			horarioTurmaVO.getProfessorAtual().setCodigo(horarioTurmaVO.getProfessorSubstituto().getCodigo());
			horarioTurmaVO.getProfessorAtual().setNome(horarioTurmaVO.getProfessorSubstituto().getNome());
			horarioTurmaVO.getProfessorAtual().setEmail(horarioTurmaVO.getProfessorSubstituto().getEmail());
			horarioTurmaVO.getProfessorAtual().setEmail2(horarioTurmaVO.getProfessorSubstituto().getEmail2());
			horarioTurmaVO.setProfessorSubstituto(null);
			horarioTurmaVO.setSalaAtual(horarioTurmaVO.getSalaSubstituta().clone());
			horarioTurmaVO.setSalaSubstituta(null);
			alterarUpdatedPorCodigo(horarioTurmaVO, false, usuario);
		} catch (Exception e) {
			if (alterou && !horarioTurmaDiaVOs.isEmpty()) {
				getFacadeFactory().getHorarioTurmaDiaFacade().realizarCarregamentoDados(horarioTurmaDiaVOs, usuario);
			}
			throw e;
		} finally {
			dataInicio = null;
			dataFim = null;
			Uteis.liberarListaMemoria(registroAulaVOs);
			Uteis.liberarListaMemoria(horarioTurmaDiaVOs);
		}
		return validacoes;

	}

	@Override
	public void realizarCriacaoCalendarioHorarioTurma(HorarioTurmaVO horarioTurmaVO, UsuarioVO usuario) throws Exception {
		if (!horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty()) {
			horarioTurmaVO.getCalendarioHorarioAulaVOs().clear();
			Date menorData = horarioTurmaVO.getHorarioTurmaDiaVOs().get(0).getData();
			Date maiorData = horarioTurmaVO.getHorarioTurmaDiaVOs().get(horarioTurmaVO.getHorarioTurmaDiaVOs().size() - 1).getData();
			List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(Uteis.getDate("01/" + Uteis.getMesData(menorData) + "/" + Uteis.getAnoData(menorData)), Uteis.getDate(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(maiorData)) + "/" + Uteis.getMesData(maiorData) + "/" + Uteis.getAnoData(maiorData)), horarioTurmaVO.getTurma().getUnidadeEnsino().getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			inicializarDadosCalendario(horarioTurmaVO, true, feriadoVOs);
		} else {
			horarioTurmaVO.setCalendarioHorarioAulaVOs(null);
		}
	}

	/**
	 * Este metodo ï¿½ responsavel por alterar a disciplina no horario do
	 * professor quando nesta alteracao nï¿½o esta sendo alterado tambem o
	 * professor, onde neste caso nao e nescessario verificar choque de horario
	 * pois o mesmo so altera um horario ja cadastrado, porï¿½m ele sï¿½ irï¿½
	 * alterar os dias de aula que ainda nï¿½o foram registrado aula.
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVO
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarAlteracaoDisciplinaHorarioProfessorSubstitutoIgualProfessorSusbtituido(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, List<Date> datas, int numeroAula, boolean alterarTodasAulas, List<RegistroAulaVO> registroAulaVOs, UsuarioVO usuario) throws Exception {
		if (horarioTurmaVO.getProfessorAtual().getCodigo().equals(horarioTurmaVO.getProfessorSubstituto().getCodigo())) {
			Date dataInicio = null;
			Date dataFim = null;
			List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = null;
			StringBuilder resultadoAcao = new StringBuilder();
			try {				
				if (!horarioTurmaDiaVOs.isEmpty() && alterarTodasAulas) {
					dataInicio = horarioTurmaDiaVOs.get(0).getData();
					dataFim = horarioTurmaDiaVOs.get(horarioTurmaDiaVOs.size() - 1).getData();
				}
				horarioProfessorDiaVOs = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(null, horarioTurmaVO.getTurno().getCodigo(), horarioTurmaVO.getProfessorAtual().getCodigo(), null, null, dataInicio, dataFim, null, datas, null, null, null);
				for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorDiaVOs) {
					boolean existeAulaRegistrada = false;
					boolean alterouAula = false;
					for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
						if (registroAulaVO.getData_Apresentar().equals(horarioProfessorDiaVO.getData_Apresentar()) && ((numeroAula == 0) || (registroAulaVO.getNrAula().equals(numeroAula)))) {
							existeAulaRegistrada = true;
							break;
						}
					}
					if (!existeAulaRegistrada) {
						HorarioTurmaDiaVO htd = realizadaObtencaoHorarioTurmaDiaPorData(horarioTurmaDiaVOs, horarioProfessorDiaVO.getData());
						for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
							if (horarioProfessorDiaItemVO.getTurmaVO().getCodigo().equals(horarioTurmaVO.getTurma().getCodigo()) && horarioProfessorDiaItemVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplinaAtual().getCodigo())) {
								HorarioTurmaDiaItemVO htdi = null;
								if (htd != null) {
									htdi = realizadaObtencaoHorarioTurmaDiaItemPorNrAula(htd, horarioProfessorDiaItemVO.getNrAula());
								}
								if (htdi == null || !htdi.getPossuiChoqueSala()) {
									if (alterarTodasAulas) {
										resultadoAcao.append("Alterou a aula do horário do professor no dia " + horarioProfessorDiaVO.getData_Apresentar() + " de nrº " + horarioProfessorDiaItemVO.getNrAula() + "(" + horarioProfessorDiaItemVO.getHorario() + ") da disciplina " + horarioTurmaVO.getDisciplinaAtual().getNome() + " para a disciplina " + horarioTurmaVO.getDisciplinaSubstituta().getNome() + ".\n");
										//horarioTurmaVO.montarLogResultadoAcao("Alterado", horarioProfessorDiaItemVO.getNrAula(), horarioProfessorDiaItemVO.getHorario(), horarioProfessorDiaVO.getData(), horarioTurmaVO.getDisciplinaSubstituta(), null);
										horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(horarioTurmaVO.getDisciplinaSubstituta().getCodigo());
										horarioProfessorDiaItemVO.getDisciplinaVO().setNome(horarioTurmaVO.getDisciplinaSubstituta().getNome());
										horarioProfessorDiaItemVO.getSala().setSala(horarioTurmaVO.getSalaSubstituta().getSala());
										horarioProfessorDiaItemVO.getSala().setCodigo(horarioTurmaVO.getSalaSubstituta().getCodigo());
										horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal(horarioTurmaVO.getSalaSubstituta().getLocalAula().getLocal());
										alterouAula = true;
									} else if (horarioProfessorDiaItemVO.getNrAula().intValue() == numeroAula) {
										resultadoAcao.append("Alterou a aula do horário do professor no dia " + horarioProfessorDiaVO.getData_Apresentar() + " de nrº " + horarioProfessorDiaItemVO.getNrAula() + "(" + horarioProfessorDiaItemVO.getHorario() + ")  da disciplina " + horarioTurmaVO.getDisciplinaAtual().getNome() + " para a disciplina " + horarioTurmaVO.getDisciplinaSubstituta().getNome() + ".\n");
										//horarioTurmaVO.montarLogResultadoAcao("Alterado", horarioProfessorDiaItemVO.getNrAula(), horarioProfessorDiaItemVO.getHorario(), horarioProfessorDiaVO.getData(), horarioTurmaVO.getDisciplinaSubstituta(), null);
										horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(horarioTurmaVO.getDisciplinaSubstituta().getCodigo());
										horarioProfessorDiaItemVO.getDisciplinaVO().setNome(horarioTurmaVO.getDisciplinaSubstituta().getNome());
										horarioProfessorDiaItemVO.getSala().setSala(horarioTurmaVO.getSalaSubstituta().getSala());
										horarioProfessorDiaItemVO.getSala().setCodigo(horarioTurmaVO.getSalaSubstituta().getCodigo());
										horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal(horarioTurmaVO.getSalaSubstituta().getLocalAula().getLocal());
										alterouAula = true;
										break;
									}
								}
							}
						}
						if (alterouAula) {
							getFacadeFactory().getHorarioProfessorDiaFacade().alterar(horarioProfessorDiaVO, usuario);
						}
					}
				}
				if (resultadoAcao.toString().isEmpty()) {
					getFacadeFactory().getHorarioTurmaLogFacade().realizarCriacaoLogHorarioTurma(horarioTurmaVO, usuario, "Alteração Disciplina Horario Professor " + horarioTurmaVO.getProfessorAtual().getNome(), resultadoAcao.toString());
				}
			} catch (Exception e) {
				throw e;
			} finally {
				dataInicio = null;
				dataFim = null;
				Uteis.liberarListaMemoria(datas);
				Uteis.liberarListaMemoria(horarioProfessorDiaVOs);
				resultadoAcao = null;
			}
		}
	}

	/**
	 * Este mï¿½todo ï¿½ responsavel por alterar e grava do Professor e da
	 * Disciplina atraves da aï¿½ï¿½o de substituiï¿½ï¿½o do Professor /
	 * Disciplina
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVO
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param usuario
	 * @param alterarEGravar
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarAlteracaoProfessorDisciplinaHorarioTurma(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, int numeroAula, boolean alterarTodasAulas, List<RegistroAulaVO> registroAulaVOs, UsuarioVO usuario, boolean alterarEGravar, boolean liberarAulaAcimaLimite, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		StringBuilder resultadoAcao = new StringBuilder();
		try {
			for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaDiaVOs) {
				boolean existeAulaRegistrada = false;
				for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
					if (registroAulaVO.getData_Apresentar().equals(horarioTurmaDiaVO2.getData_Apresentar()) && ((numeroAula == 0) || (registroAulaVO.getNrAula().equals(numeroAula)))) {
						existeAulaRegistrada = true;
						break;
					}
				}
				if (!existeAulaRegistrada) {
					boolean alterouDia = false;
					for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO2.getHorarioTurmaDiaItemVOs()) {
						if (horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplinaAtual().getCodigo()) && horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(horarioTurmaVO.getProfessorAtual().getCodigo()) && (alterarTodasAulas || (!alterarTodasAulas && horarioTurmaDiaItemVO.getNrAula().intValue() == numeroAula)) && !horarioTurmaDiaItemVO.getPossuiChoqueSala() && (!horarioTurmaDiaItemVO.getPossuiChoqueAulaExcesso() || liberarAulaAcimaLimite)) {
							DisciplinaVO dis =horarioTurmaVO.getDisciplinaAtual();
							PessoaVO prof = horarioTurmaVO.getProfessorAtual();
							resultadoAcao.append("Alterou a aula do horário da turma no dia " + horarioTurmaDiaVO2.getData_Apresentar() + " de nrº " + horarioTurmaDiaItemVO.getNrAula() + "(" + horarioTurmaDiaItemVO.getHorario() + ") ");
							
							if (!horarioTurmaVO.getDisciplinaAtual().getCodigo().equals(horarioTurmaVO.getDisciplinaSubstituta().getCodigo())) {
								resultadoAcao.append(" da disciplina " + horarioTurmaVO.getDisciplinaAtual().getNome() + " para a disciplina " + horarioTurmaVO.getDisciplinaSubstituta().getNome());
								dis = horarioTurmaVO.getDisciplinaSubstituta();
							}
							if (!horarioTurmaVO.getProfessorAtual().getCodigo().equals(horarioTurmaVO.getProfessorSubstituto().getCodigo())) {
								resultadoAcao.append(" do(a) professor(a) " + horarioTurmaVO.getProfessorAtual().getNome() + " para o(a) professor(a) " + horarioTurmaVO.getProfessorSubstituto().getNome());
								prof=horarioTurmaVO.getProfessorSubstituto();
							}
							resultadoAcao.append(".\n");
							horarioTurmaDiaItemVO.setDisciplinaVO(new DisciplinaVO());
							horarioTurmaDiaItemVO.getDisciplinaVO().setCodigo(horarioTurmaVO.getDisciplinaSubstituta().getCodigo());
							horarioTurmaDiaItemVO.getDisciplinaVO().setNome(horarioTurmaVO.getDisciplinaSubstituta().getNome());
							horarioTurmaDiaItemVO.setFuncionarioVO(new PessoaVO());
							horarioTurmaDiaItemVO.getFuncionarioVO().setCodigo(horarioTurmaVO.getProfessorSubstituto().getCodigo());
							horarioTurmaDiaItemVO.getFuncionarioVO().setNome(horarioTurmaVO.getProfessorSubstituto().getNome());
							horarioTurmaDiaItemVO.setSala(new SalaLocalAulaVO());
							horarioTurmaDiaItemVO.getSala().setCodigo(horarioTurmaVO.getSalaSubstituta().getCodigo());
							horarioTurmaDiaItemVO.getSala().setSala(horarioTurmaVO.getSalaSubstituta().getSala());
							horarioTurmaDiaItemVO.getSala().getLocalAula().setLocal(horarioTurmaVO.getSalaSubstituta().getLocalAula().getLocal());
							horarioTurmaDiaItemVO.getSala().getLocalAula().setCodigo(horarioTurmaVO.getSalaSubstituta().getLocalAula().getCodigo());
							horarioTurmaVO.montarLogResultadoAcao("Alteração ", horarioTurmaDiaItemVO.getNrAula(), horarioTurmaDiaItemVO.getHorario(), horarioTurmaDiaVO2.getData(), dis, prof);
							horarioTurmaDiaItemVO.setAulaReposicao(horarioTurmaDiaVO2.getAulaReposicao());

							if (!Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioCargoVO())) {
								horarioTurmaDiaItemVO.setFuncionarioCargoVO(funcionarioCargoVO);
							}
							if (Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getGoogleMeetVO())) {
								horarioTurmaVO.getHorarioTurmaDiaItemGoogleMeet().add(horarioTurmaDiaItemVO);
							}
							alterouDia = true;
							if (!alterarTodasAulas) {
								break;
							}
						}
					}
//					horarioTurmaDiaVO2.montarDadosHorarioDisciplinaProfessor();
					horarioTurmaDiaVO2.setUsuarioResp(usuario);
					if (alterouDia && alterarEGravar) {
						if (horarioTurmaDiaVO2.getIsAulaProgramada()) {
							horarioTurmaDiaVO2.setHorarioAlterado(false);
							horarioTurmaDiaVO2.setHorarioTurma(horarioTurmaVO);
							if (horarioTurmaDiaVO2.getNovoObj()) {
								getFacadeFactory().getHorarioTurmaDiaFacade().incluir(horarioTurmaDiaVO2, usuario);
							} else {
								getFacadeFactory().getHorarioTurmaDiaFacade().alterar(horarioTurmaDiaVO2, usuario);
							}
						} else {
							horarioTurmaDiaVO2.setHorarioAlterado(false);
							getFacadeFactory().getHorarioTurmaDiaFacade().excluir(horarioTurmaDiaVO2, usuario);
							horarioTurmaDiaVO2.setCodigo(0);
							horarioTurmaDiaVO2.setNovoObj(true);
						}

					}
					if (!alterarTodasAulas) {
						break;
					}
				}
			}
			if (resultadoAcao.toString().isEmpty()) {
				getFacadeFactory().getHorarioTurmaLogFacade().realizarCriacaoLogHorarioTurma(horarioTurmaVO, usuario, "Alterar Disciplina e Professor Horario Turma", resultadoAcao.toString());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			resultadoAcao = null;
			Uteis.liberarListaMemoria(horarioTurmaDiaVOs);
		}
	}

	/**
	 * Este mï¿½todo ï¿½ responsavel em separar os Dias de Aulas que estï¿½o
	 * sendo programadas para um determinado Professor Segue a regra 1 - Caso o
	 * parametro alterarTodasAulas = true entï¿½o separa todos os dias de aula
	 * do professor / disciplina se false retorno apenas 1 registro na
	 * List<HorarioTurmaDiaVO> contendo o parametro horarioTurmaDiaVO 2 - Este
	 * verifica se existe Aula registrada pois se houver as data com aula
	 * registra nï¿½o ï¿½ retornada na lista List<HorarioTurmaDiaVO>
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVO
	 * @param alterarTodasAulas
	 * @param professor
	 * @param disciplina
	 *            - Campo disciplina ï¿½ opcional utilizado apenas para exclusao
	 *            de um horario ou alteraï¿½ï¿½o de um horario
	 * @param registroAulaVOs
	 * @return
	 */
	private List<HorarioTurmaDiaVO> realizarObtencaoHorarioTurmaDiaSeremAlteradoOuExcluido(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, boolean alterarTodasAulas, int professor, Integer disciplina, Integer nrAula, List<RegistroAulaVO> registroAulaVOs) {
		List<HorarioTurmaDiaVO> horarioTurmaDiaVOs = new ArrayList<HorarioTurmaDiaVO>();
		q: for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			boolean existeAulaRegistrada = false;
			for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
				if (registroAulaVO.getData_Apresentar().equals(horarioTurmaDiaVO2.getData_Apresentar()) && ((nrAula == null || nrAula == 0 || alterarTodasAulas) || (registroAulaVO.getNrAula().equals(nrAula)))) {
					existeAulaRegistrada = true;
					break;
				}
			}

			if (!existeAulaRegistrada) {
				if (alterarTodasAulas) {
					for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO2.getHorarioTurmaDiaItemVOs()) {
						if ((horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(disciplina)) && horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(professor)) {
							horarioTurmaDiaVOs.add(horarioTurmaDiaVO2);
							continue q;
						}
					}
				} else {
					if (horarioTurmaDiaVO2.getData_Apresentar().equals(horarioTurmaDiaVO.getData_Apresentar())) {
						horarioTurmaDiaVOs.add(horarioTurmaDiaVO2);
						break;
					}
				}
			}
		}

		return horarioTurmaDiaVOs;
	}

	/**
	 * Este mï¿½todo ï¿½ responsavel em separar as Datas Aulas que estï¿½o sendo
	 * programadas para um determinado Professor Segue a regra 1 - Caso o
	 * parametro alterarTodasAulas = true entï¿½o separa todos os dias de aula
	 * do professor / disciplina se false retorno apenas 1 registro na
	 * List<Date> contendo o parametro horarioTurmaDiaVO 2 - Este verifica se
	 * existe Aula registrada pois se houver as data com aula registra nï¿½o ï¿½
	 * retornada na lista List<Date>
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVO
	 * @param alterarTodasAulas
	 * @param professor
	 * @param disciplina
	 *            - Campo disciplina ï¿½ opcional utilizado apenas para exclusao
	 *            de um horario ou alteraï¿½ï¿½o de um horario
	 * @param registroAulaVOs
	 * @return
	 */
	@Override
	public List<Date> realizarObtencaoDataSeremAlteradoExcluido(HorarioTurmaVO horarioTurmaVO, Date dataBase, boolean alterarTodasAulas, int professor, Integer disciplina, List<RegistroAulaVO> registroAulaVOs) {
		List<Date> horarioTurmaDiaVOs = new ArrayList<Date>();
		q: for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			boolean existeAulaRegistrada = false;
			for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
				if (registroAulaVO.getData_Apresentar().equals(horarioTurmaDiaVO2.getData_Apresentar())) {
					existeAulaRegistrada = true;
					break;
				}
			}
			if (!existeAulaRegistrada) {
				if (alterarTodasAulas) {
					for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO2.getHorarioTurmaDiaItemVOs()) {
						if ((horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(disciplina)) && horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(professor)) {
							horarioTurmaDiaVOs.add(horarioTurmaDiaVO2.getData());
							continue q;
						}
					}
				} else {
					if (horarioTurmaDiaVO2.getData_Apresentar().equals(Uteis.getData(dataBase))) {
						horarioTurmaDiaVOs.add(horarioTurmaDiaVO2.getData());
						break;
					}
				}
			}
		}
		return horarioTurmaDiaVOs;
	}

	/**
	 * Este mï¿½doto altera o horario da turma quando nesta alteraï¿½ï¿½o tambem
	 * estï¿½ sendo alterado o professor. Segue a regra de execucao 1 - Separa
	 * as datas que deverao ser substituidas estas datas servem para otimizar a
	 * consulta do horario do professor 2 - Separa os Horarios Turma Dia que
	 * deverï¿½ ser alterado utilizado para otimizar a alteracao do horario do
	 * professor 3 - Consulta o Horario do Professor Substituto do Turno da
	 * Programacao de Aula (Caso no exista este ï¿½ criado) 4 - Consulta as
	 * aulas programadas para o professor substituto independente do turno a fim
	 * de verificar o choque de horario 4 - Realiza a Inclusï¿½o do Horario do
	 * Professor 4.1 - Realiza a verificacao do choque de horario 4.2 - Adiciona
	 * o horario do professor 4.3 - Grava o Horario do Professor 5 - Alterar o
	 * horario do professor substituido
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVO
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param registroAulaVOs
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarAlteracaoDisciplinaHorarioProfessorSubstitutoDiferenteProfessorSusbtituido(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, List<Date> datas, int numeroAula, boolean alterarTodasAulas, List<RegistroAulaVO> registroAulaVOs, UsuarioVO usuario, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, Boolean liberarAulaAcimaLimite, Boolean validarChoqueSala) throws Exception {
		if (!horarioTurmaVO.getProfessorAtual().getCodigo().equals(horarioTurmaVO.getProfessorSubstituto().getCodigo())) {
			Date dataInicio = null;
			Date dataFim = null;
			HorarioProfessorVO horarioProfessorVO = null;
			try {				
				if (!horarioTurmaDiaVOs.isEmpty() && alterarTodasAulas) {
					dataInicio = horarioTurmaVO.getHorarioTurmaDiaVOs().get(0).getData();
					dataFim = horarioTurmaVO.getHorarioTurmaDiaVOs().get(horarioTurmaVO.getHorarioTurmaDiaVOs().size() - 1).getData();
				}else if(datas != null && !datas.isEmpty()){
					dataInicio = datas.get(0);
					dataFim = datas.get(datas.size() - 1);
				}
				if (datas != null && !datas.isEmpty()) {
					horarioProfessorVO = getFacadeFactory().getHorarioProfessorFacade().consultarRapidaHorarioProfessorTurno(horarioTurmaVO.getProfessorSubstituto().getCodigo(), horarioTurmaVO.getTurma().getTurno().getCodigo(), usuario);
					if (horarioProfessorVO.isNovoObj()) {
						horarioProfessorVO.setProfessor(horarioTurmaVO.getProfessorSubstituto());
						horarioProfessorVO.setTurno(horarioTurmaVO.getTurno());
						getFacadeFactory().getHorarioProfessorFacade().incluir(horarioProfessorVO, usuario);
					}
					horarioProfessorVO.setHorarioProfessorDiaVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(null, null, horarioTurmaVO.getProfessorSubstituto().getCodigo(), null, null, dataInicio, dataFim, null, datas, null, null, null));
					executarGravacaoAulaHorarioProfessorSubstituto(horarioTurmaVO, horarioTurmaDiaVOs, numeroAula, alterarTodasAulas, horarioProfessorVO, usuario, liberarAulaAcimaLimite, validarChoqueSala);
					getFacadeFactory().getHorarioProfessorFacade().realizarExclusaoHorarioProfessor(horarioTurmaVO, horarioTurmaVO.getProfessorAtual().getCodigo(), horarioTurmaVO.getDisciplinaAtual().getCodigo(), dataInicio, dataFim, numeroAula, alterarTodasAulas, registroAulaVOs, usuario, liberarAulaAcimaLimite);

				}
			} catch (Exception e) {
				throw e;
			} finally {
				dataInicio = null;
				dataFim = null;
				horarioProfessorVO = null;
			}
		}
	}

	private List<ChoqueHorarioVO> realizarValidacaoExclusaoHorarioTurmaPorProfessorDisciplina(HorarioTurmaVO horarioTurmaVO, List<RegistroAulaVO> registroAulaVOs) throws Exception {
		List<ChoqueHorarioVO> listaErros = new ArrayList<ChoqueHorarioVO>(0);
		if (!registroAulaVOs.isEmpty()) {
			for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
				listaErros.add(new ChoqueHorarioVO(registroAulaVO.getData(), registroAulaVO.getNrAula(), registroAulaVO.getHorario(), horarioTurmaVO.getTurma().getIdentificadorTurma(), registroAulaVO.getDisciplina().getNome(), registroAulaVO.getProfessor().getNome(), horarioTurmaVO.getTurma().getTurno().getNome(), "", true));
			}
		}
		return listaErros;
	}

	/**
	 * Este método tem a função de validar se a sala de aula que está sendo
	 * substituida possui choque de horário com outra turma
	 *
	 * Caso ocorra choque de sala o campo transitente no Horario Turma Dia Item
	 * setPossuiChoqueSala é marcado como true e também é adicionado na lista de
	 * erro.
	 * 
	 * @author Rodrigo Wind - 15/09/2015
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVOs
	 *            - Lista dos horarios da turma que serão alterados
	 * @param numeroAula
	 *            - Corresponde ao numero da aula especifica que será realizado
	 *            a validação;
	 * @param alterarTodasAulas
	 *            - Defini se a alteração do professor está sendo feita em uma
	 *            aula específica ou tem todas as aulas programadas para o
	 *            professor
	 * @param controlaNumeroMaximoAulaProgramadaProfessorDia
	 *            - Caso true significa que foi habilitado o controle na
	 *            configuração geral do sistema
	 * @param numeroAulaMaximaProgramarProfessorDia
	 *            - Corresponde ao número máximo de aula permitida para o
	 *            professor por dia independente da unidade de
	 *            ensino/curso/turno/turma/
	 * @param liberarProgramacaoAulaProfessorAcimaPermitido
	 *            - Caso true não sigifica que o usuário liberou a inclusão da
	 *            aula para o professor acima do limite permitido
	 * @param qtdeAulaDiaProgramadaProfessor
	 *            - Mantem a lista com as aulas programadas para o professor dia
	 *            a dia (Ex: Dia 01/01/2015 3 aulas programadas)
	 * @return
	 * @throws Exception
	 */
	private List<ChoqueHorarioVO> realizarValidacaoChoqueHorarioTrocaSala(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, Integer numeroAula, boolean alterarTodasAulas) throws Exception {
		List<ChoqueHorarioVO> listaErros = new ArrayList<ChoqueHorarioVO>(0);
		if (Uteis.isAtributoPreenchido(horarioTurmaVO.getSalaSubstituta().getCodigo()) && !horarioTurmaVO.getSalaAtual().getCodigo().equals(horarioTurmaVO.getSalaSubstituta().getCodigo())) {
			for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaDiaVOs) {
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
					try {
						if (horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplinaAtual().getCodigo()) && horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(horarioTurmaVO.getProfessorAtual().getCodigo()) && (alterarTodasAulas || (!alterarTodasAulas && (numeroAula.equals(horarioTurmaDiaItemVO.getNrAula()) || numeroAula == 0)))) {
							getFacadeFactory().getHorarioTurmaDiaItemFacade().realizarVerificacaoChoqueHorarioSalaAula(horarioTurmaVO.getCodigo(), horarioTurmaVO.getSalaSubstituta().getCodigo(), horarioTurmaVO.getSalaSubstituta().getControlarChoqueSala(), horarioTurmaDiaVO.getData(), horarioTurmaDiaItemVO.getHorarioInicio(), horarioTurmaDiaItemVO.getHorarioTermino(), true);
						}
					} catch (ChoqueHorarioVO ex) {
						horarioTurmaDiaItemVO.setPossuiChoqueSala(true);
						listaErros.add(ex);
						if (!alterarTodasAulas) {
							break;
						}
					}
				}
			}
		} else {
			for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaDiaVOs) {
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
					horarioTurmaDiaItemVO.setPossuiChoqueSala(false);
				}
			}
		}
		return listaErros;
	}

	/**
	 * Este método tem a função de validar se o professor que está sendo
	 * substituido não atingiu o limite máximo de aula programada para o mesmo
	 * nos dias em que estão sendo alterados as aulas, esta função só é
	 * habilitada se na configuração geral do sistema esta marcada a opção de
	 * controlarNumeroAulaProgramadoProfessorPorDia está marcada e
	 * quantidadeAulaMaximaProgramarProfessorDia seja maior que 0 e o professor
	 * atual seja diferente do professor substituto. Caso ocorra problemas com a
	 * validação o campo transitente no Horario Turma Dia Item
	 * setPossuiChoqueAulaExcesso é marcado como true e também é adicionado na
	 * lista de erro a execção.
	 * 
	 * @author Rodrigo Wind - 15/09/2015
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVOs
	 *            - Lista dos horarios da turma que serão alterados
	 * @param numeroAula
	 *            - Corresponde ao numero da aula especifica que será realizado
	 *            a validação;
	 * @param alterarTodasAulas
	 *            - Defini se a alteração do professor está sendo feita em uma
	 *            aula específica ou tem todas as aulas programadas para o
	 *            professor
	 * @param controlaNumeroMaximoAulaProgramadaProfessorDia
	 *            - Caso true significa que foi habilitado o controle na
	 *            configuração geral do sistema
	 * @param numeroAulaMaximaProgramarProfessorDia
	 *            - Corresponde ao número máximo de aula permitida para o
	 *            professor por dia independente da unidade de
	 *            ensino/curso/turno/turma/
	 * @param liberarProgramacaoAulaProfessorAcimaPermitido
	 *            - Caso true não sigifica que o usuário liberou a inclusão da
	 *            aula para o professor acima do limite permitido
	 * @param qtdeAulaDiaProgramadaProfessor
	 *            - Mantem a lista com as aulas programadas para o professor dia
	 *            a dia (Ex: Dia 01/01/2015 3 aulas programadas)
	 * @return
	 * @throws Exception
	 */
	private List<ChoqueHorarioVO> realizarValidacaoNumeroAulaProgramaProfessorNaTrocaProfessor(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, List<Date> datas, Integer numeroAula, boolean alterarTodasAulas, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido) throws Exception {
		List<ChoqueHorarioVO> listaErros = new ArrayList<ChoqueHorarioVO>(0);
		if (!horarioTurmaVO.getProfessorAtual().getCodigo().equals(horarioTurmaVO.getProfessorSubstituto().getCodigo()) && controlaNumeroMaximoAulaProgramadaProfessorDia && numeroAulaMaximaProgramarProfessorDia > 0 && !liberarProgramacaoAulaProfessorAcimaPermitido) {
			Map<String, Integer> qtdeAulaDiaProgramadaProfessor = getFacadeFactory().getHorarioProfessorDiaFacade().consultarQuantidadeAulaProgramadaProfessorPorData(datas, horarioTurmaVO.getProfessorSubstituto().getCodigo());
			for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaDiaVOs) {
				Integer qtdeAulaProgramadaDia = qtdeAulaDiaProgramadaProfessor != null && qtdeAulaDiaProgramadaProfessor.containsKey(Uteis.getData(horarioTurmaDiaVO.getData())) ? qtdeAulaDiaProgramadaProfessor.get(Uteis.getData(horarioTurmaDiaVO.getData())) : 0;
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
					horarioTurmaDiaItemVO.setPossuiChoqueSala(false);
					horarioTurmaDiaItemVO.setPossuiChoqueAulaExcesso(false);
					if (horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplinaAtual().getCodigo()) && horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(horarioTurmaVO.getProfessorAtual().getCodigo()) && (alterarTodasAulas || (!alterarTodasAulas && (numeroAula.equals(horarioTurmaDiaItemVO.getNrAula()) || numeroAula == 0)))) {
						qtdeAulaProgramadaDia++;
						if (qtdeAulaProgramadaDia > numeroAulaMaximaProgramarProfessorDia) {
							horarioTurmaDiaItemVO.setPossuiChoqueAulaExcesso(true);
							ChoqueHorarioVO choqueHorarioVO = new ChoqueHorarioVO();
							choqueHorarioVO.setProfessor(horarioTurmaVO.getProfessorSubstituto().getNome());
							choqueHorarioVO.setData(horarioTurmaDiaVO.getData());
							choqueHorarioVO.setHorario(horarioTurmaDiaItemVO.getNrAula() + "ª (" + horarioTurmaDiaItemVO.getHorarioInicio() + " até " + horarioTurmaDiaItemVO.getHorarioTermino() + ") ");
							choqueHorarioVO.setChoqueHorarioAulaExcesso(true);
							listaErros.add(choqueHorarioVO);
						}
						if (!alterarTodasAulas) {
							break;
						}
					}
				}
			}
			qtdeAulaDiaProgramadaProfessor = null;
		} else {
			for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaDiaVOs) {
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
					horarioTurmaDiaItemVO.setPossuiChoqueAulaExcesso(false);
				}
			}
		}
		return listaErros;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> realizarExclusaoHorarioTurmaPorProfessorDisciplina(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, Integer professor, Integer disciplina, int numeroAula, boolean alterarTodasAulas, UsuarioVO usuario, boolean retornarExecaoRegistroAulaLancada) throws Exception {
		try {
			HorarioTurma.excluir(getIdEntidade(), true, usuario);
			if (professor == 0) {
				throw new Exception("O campo PROFESSOR deve ser informado.");
			}
			if (disciplina == 0) {
				throw new Exception("O campo DISCIPLINA deve ser informado.");
			}
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(professor, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			adicionarProfessorManipularControleConcorrencia(horarioTurmaVO, pessoaVO, "Exclusão do Professor no Horario da Turma", usuario);
			Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = new HashMap<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>>(0);
			Date dataInicio = null;
			Date dataFim = null;
			if (!horarioTurmaVO.getHorarioTurmaDiaVOs().isEmpty() && alterarTodasAulas) {
				dataInicio = horarioTurmaVO.getHorarioTurmaDiaVOs().get(0).getData();
				dataFim = horarioTurmaVO.getHorarioTurmaDiaVOs().get(horarioTurmaVO.getHorarioTurmaDiaVOs().size() - 1).getData();
			}
			if (horarioTurmaDiaVO != null && horarioTurmaDiaVO.getData() != null && !alterarTodasAulas) {
				dataInicio = horarioTurmaDiaVO.getData();
				dataFim = horarioTurmaDiaVO.getData();
			}

			List<RegistroAulaVO> registroAulaVOs = (getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorTurmaDisciplinaPeriodo(horarioTurmaVO.getTurma().getCodigo(), disciplina, dataInicio, dataFim, horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, Boolean.FALSE));
			List<HorarioTurmaDiaVO> horarioTurmaDiaVOs = (realizarObtencaoHorarioTurmaDiaSeremAlteradoOuExcluido(horarioTurmaVO, horarioTurmaDiaVO, alterarTodasAulas, professor, disciplina, numeroAula, registroAulaVOs));
			if (!registroAulaVOs.isEmpty() && retornarExecaoRegistroAulaLancada) {
				validacoes.put(TipoValidacaoChoqueHorarioEnum.ERRO, new ArrayList<ChoqueHorarioVO>());
				validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).addAll(realizarValidacaoExclusaoHorarioTurmaPorProfessorDisciplina(horarioTurmaVO, registroAulaVOs));
				if (validacoes.containsKey(TipoValidacaoChoqueHorarioEnum.ERRO) && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {
					return validacoes;
				}
			}
			for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaDiaVOs) {
				if (verificarPermissaoUsuarioExcluirHorarioTurmaDia(usuario)) {
					if (horarioTurmaDiaVO2.getUsuarioResp().getCodigo().intValue() > 0 && horarioTurmaDiaVO2.getUsuarioResp().getCodigo().intValue() != usuario.getCodigo().intValue()) {
						throw new Exception("Não é possível realizar a exclusão da aula selecionada! Somente o usuário " + horarioTurmaDiaVO2.getUsuarioResp().getNome() + " tem permissão para excluir este registro,ele é o responsável pelo cadastro! ");
					}
				}
			}
			getFacadeFactory().getHorarioProfessorFacade().realizarExclusaoHorarioProfessor(horarioTurmaVO, professor, disciplina, dataInicio, dataFim, numeroAula, alterarTodasAulas, registroAulaVOs, usuario, false);
			StringBuilder resultadoAcao = new StringBuilder();
			for (HorarioTurmaDiaVO horarioTurmaDiaVO2 : horarioTurmaDiaVOs) {
				boolean existeAulaRegistrada = false;
				for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
					if (registroAulaVO.getData_Apresentar().equals(horarioTurmaDiaVO2.getData_Apresentar()) && ((numeroAula == 0) || (registroAulaVO.getNrAula().equals(numeroAula)))) {
						existeAulaRegistrada = true;
						break;
					}
				}
				if (!existeAulaRegistrada) {
					boolean alterouDia = false;
					for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO2.getHorarioTurmaDiaItemVOs()) {
						if ((horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(disciplina)) && horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(professor) && (alterarTodasAulas || (!alterarTodasAulas && (numeroAula == horarioTurmaDiaItemVO.getNrAula() || numeroAula == 0))) && !horarioTurmaDiaItemVO.getPossuiChoqueSala()) {
							//devido a posicao de ponteiro do java entao e necessario criar as instancia de Pesssoa e Disciplina pois os campos serao limpados e assim perdendo a informação que esta na lista de log.
							PessoaVO prof = new PessoaVO();
							prof.setCodigo(horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo());
							prof.setNome(horarioTurmaDiaItemVO.getFuncionarioVO().getNome());
							DisciplinaVO disc = new DisciplinaVO();
							disc.setCodigo(horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo());
							disc.setNome(horarioTurmaDiaItemVO.getDisciplinaVO().getNome());
							horarioTurmaVO.montarLogResultadoAcao("Excluído", horarioTurmaDiaItemVO.getNrAula(),  horarioTurmaDiaItemVO.getNrAula() + "º (" + horarioTurmaDiaItemVO.getHorario() + ")", horarioTurmaDiaVO2.getData(), disc, prof);
							resultadoAcao.append("Removeu a aula do horário da turma no dia " + horarioTurmaDiaVO2.getData_Apresentar() + " de nrº " + horarioTurmaDiaItemVO.getNrAula() + "(" + horarioTurmaDiaItemVO.getHorario() + ") da disciplina " + horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo() + " do professor " + horarioTurmaDiaItemVO.getFuncionarioVO().getNome() + ".\n");
							horarioTurmaDiaItemVO.getDisciplinaVO().setCodigo(0);
							horarioTurmaDiaItemVO.getDisciplinaVO().setNome("");
							horarioTurmaDiaItemVO.getFuncionarioVO().setCodigo(0);
							horarioTurmaDiaItemVO.getFuncionarioVO().setNome("");
							horarioTurmaDiaItemVO.getSala().setCodigo(0);
							horarioTurmaDiaItemVO.getSala().setSala("");
							horarioTurmaDiaItemVO.getSala().getLocalAula().setLocal("");
							horarioTurmaDiaItemVO.getSala().getLocalAula().setCodigo(0);
							alterouDia = true;

							if (Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getGoogleMeetVO())) {
								horarioTurmaVO.getHorarioTurmaDiaItemGoogleMeet().add(horarioTurmaDiaItemVO);
							}
							if (!alterarTodasAulas && numeroAula > 0) {
								break;
							}
						}
					}
//					horarioTurmaDiaVO2.montarDadosHorarioDisciplinaProfessor();
					if (alterouDia) {
						if (horarioTurmaDiaVO2.getIsAulaProgramada()) {
							horarioTurmaDiaVO2.setHorarioTurma(horarioTurmaVO);
							if (horarioTurmaDiaVO2.getNovoObj()) {
								getFacadeFactory().getHorarioTurmaDiaFacade().incluir(horarioTurmaDiaVO2, usuario);
							} else {
								getFacadeFactory().getHorarioTurmaDiaFacade().alterar(horarioTurmaDiaVO2, usuario);
							}
						} else {
							getFacadeFactory().getHorarioTurmaDiaFacade().excluir(horarioTurmaDiaVO2, usuario);
							horarioTurmaDiaVO2.setCodigo(0);
							horarioTurmaDiaVO2.setNovoObj(true);
						}
					}
					if (!alterarTodasAulas) {
						break;
					}
				}
			}
			incluirDataFinalizacaoEConfirmacaoTurma(horarioTurmaVO, usuario);
			horarioTurmaVO.montarDadosHorarioDisciplinaProfessorDia();
			getFacadeFactory().getHorarioTurmaProfessorDisciplinaFacade().alterarHorarioTurmaProfessorDisciplinas(horarioTurmaVO.getCodigo(), horarioTurmaVO.getListaProfessorDisciplina());
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluirComBaseNaProgramacaoAula(horarioTurmaVO.getTurma().getCodigo(), professor, disciplina, horarioTurmaVO.getSemestreVigente(), horarioTurmaVO.getAnoVigente(), usuario);
			if (!resultadoAcao.toString().isEmpty()) {
				getFacadeFactory().getHorarioTurmaLogFacade().realizarCriacaoLogHorarioTurma(horarioTurmaVO, usuario, "Exclusão Disciplina e Professor Horario Turma", resultadoAcao.toString());
			}
			realizarCriacaoCalendarioHorarioTurma(horarioTurmaVO, usuario);
			alterarUpdatedPorCodigo(horarioTurmaVO, false, usuario);			
			return validacoes;
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}	
	
	public Boolean verificarPermissaoUsuarioExcluirHorarioTurmaDia(UsuarioVO usuario) {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario", usuario);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	/**
	 * Este mï¿½todo ï¿½ chamado pelo mï¿½todo
	 * realizarAlteracaoProfessorDisciplinaHorarioProfessor responsavel em
	 * alterar no horario do professor substituto Segue as regras do metodo 1 -
	 * Percorre a lista horarioTurmaDiaVOs em busca das aulas em que o professor
	 * que estï¿½ sendo substituido ministra a aula 2 - Chama um metodo que
	 * adiciona no horario do professor substituto a aula correspondente
	 * validando choque de horï¿½rio. 3 - Altera o horario do professor
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVOs
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param horarioProfessorDiaVOs
	 * @param horarioProfessorVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private Boolean executarGravacaoAulaHorarioProfessorSubstituto(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, int numeroAula, boolean alterarTodasAulas, HorarioProfessorVO horarioProfessorVO, UsuarioVO usuarioVO, boolean liberarProgramacaoAulaProfessorAcimaPermitido, Boolean validarChoqueSala) throws Exception {
		StringBuilder resultadoAcao = realizarInclusaoDisciplinaNoHorarioProfessorSubstituto(horarioTurmaVO, horarioTurmaDiaVOs, numeroAula, alterarTodasAulas, horarioProfessorVO, usuarioVO, liberarProgramacaoAulaProfessorAcimaPermitido, validarChoqueSala);
		boolean houveAlteracao = false;
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			if (horarioProfessorDiaVO.getHorarioAlterado()) {
				houveAlteracao = true;
				horarioProfessorDiaVO.setHorarioAlterado(false);
				horarioProfessorDiaVO.getHorarioProfessor().setCodigo(horarioProfessorVO.getCodigo());
				if (horarioProfessorDiaVO.getCodigo() > 0) {
					getFacadeFactory().getHorarioProfessorDiaFacade().alterar(horarioProfessorDiaVO, usuarioVO);
				} else {
					getFacadeFactory().getHorarioProfessorDiaFacade().incluir(horarioProfessorDiaVO, usuarioVO);
				}
			}
		}
		if (!resultadoAcao.toString().isEmpty()) {
			getFacadeFactory().getHorarioTurmaLogFacade().realizarCriacaoLogHorarioTurma(horarioTurmaVO, usuarioVO, "Inclusão da Disciplina Substituta no Horario do Professor", resultadoAcao.toString());
		}
		return houveAlteracao;
	}

	/**
	 * Este metodo altera o horario do professor incluindo o horario que estï¿½
	 * sendo programado no turma,
	 * 
	 * @param horarioTurmaVO
	 * @param disciplina
	 *            = Disciplina que deve ser incluida no horario do professor
	 *            (Esta disciplina ï¿½ passada como parametro porque o metodo de
	 *            substituicao do professor ou disciplina tambï¿½m utiliza este
	 *            metodo)
	 * @param dataBase
	 *            = Data utilizada para filtar o horario do professor;
	 * @param horarioTurmaDiaItemVO
	 *            = Corresponde a aula do horario da turma que deve ser
	 *            atribuida ao professor
	 * @param horarioProfessorDiaVOs
	 *            = Corresponde a lista dos dias que o professor possui aula
	 *            registrada independente se as aulas sï¿½o do turno em que
	 *            estï¿½ ocorrendo a programacao de aula deve vir todos esses
	 *            dias para realizar a validaï¿½ï¿½o do choque de horario.
	 * @param horarioProfessorVO
	 *            = Corresponde ao horario do professor do turno na qual estï¿½
	 *            sendo programada aula.
	 * @param usuario
	 * @throws Exception
	 *             Este mï¿½todo segue a seguinte regra 1 - Defini o horario de
	 *             inicio e termino da aula em que estï¿½ ocorrendo a
	 *             programacao em minutos 2 - Verifica se existe alguma aula
	 *             programada para o professor no dia base independente do
	 *             turno, onde compara os horarios de inicio e termino da aula
	 *             do professor com o horario inicio e termino da aula que
	 *             estï¿½ sendo programada. 3 - Caso exista um choque de
	 *             horï¿½rio ï¿½ retornado uma execeï¿½ï¿½o 4 - Caso nï¿½o
	 *             ocorra o choque de horï¿½rio se o dia do horario do professor
	 *             corresonde ao dia em que se deve realizar a inclusï¿½o do
	 *             horario sendo assim a aula da turma ï¿½ adicionada no horario
	 *             do professor. 5 - Caso nï¿½o encontre aula programada para o
	 *             professor na data base entï¿½o ï¿½ criado um novo registro no
	 *             horario do professor, adiciona o horario correspondente.
	 * 
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean adicionarAulaHorarioProfessor(HorarioTurmaVO horarioTurmaVO, DisciplinaVO disciplina, Date dataBase, int nrAula, String horarioInicio, String horarioTermino, HorarioProfessorVO horarioProfessorVO, SalaLocalAulaVO sala, UsuarioVO usuario, Boolean retornarExcecao, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido, Map<String, Integer> aulaProgramadaPorDiaProfessor, Boolean validarChoqueSala) throws Exception {
		Boolean naoPossueChoqueHorario = realizarVerificacaoChoqueHorarioTurmaDiaItemComHorarioProfessor(horarioTurmaVO, disciplina, dataBase, nrAula, horarioInicio, horarioTermino, horarioProfessorVO, sala, usuario, retornarExcecao, controlaNumeroMaximoAulaProgramadaProfessorDia, numeroAulaMaximaProgramarProfessorDia, liberarProgramacaoAulaProfessorAcimaPermitido, aulaProgramadaPorDiaProfessor, validarChoqueSala);
		
		if (naoPossueChoqueHorario) {
			
			for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
				if (horarioProfessorDiaVO.getData_Apresentar().equals(Uteis.getData(dataBase)) && horarioProfessorDiaVO.getHorarioProfessor().getCodigo().intValue() == horarioProfessorVO.getCodigo().intValue()) {
					getFacadeFactory().getHorarioProfessorDiaFacade().realizarCriacaoHorarioProfessorDiaItemVOs(horarioProfessorDiaVO, horarioProfessorVO, usuario);
					for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
						if (horarioProfessorDiaItemVO.getNrAula().equals(nrAula)) {
							if (horarioProfessorDiaItemVO.getDisciplinaLivre() && horarioProfessorDiaItemVO.getTurmaLivre()) {
								horarioProfessorDiaItemVO.getTurmaVO().setCodigo(horarioTurmaVO.getTurma().getCodigo());
								horarioProfessorDiaItemVO.getTurmaVO().setIdentificadorTurma(horarioTurmaVO.getTurma().getIdentificadorTurma());
								horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(disciplina.getCodigo());
								horarioProfessorDiaItemVO.getDisciplinaVO().setNome(disciplina.getNome());
								horarioProfessorDiaItemVO.getSala().setCodigo(sala.getCodigo());
								horarioProfessorDiaItemVO.getSala().setSala(sala.getSala());
								horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal(sala.getLocalAula().getLocal());
								if(horarioProfessorDiaItemVO.getDuracaoAula().equals(0)){
									TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(horarioProfessorVO.getTurno(), horarioProfessorDiaVO.getDiaSemanaEnum(), nrAula);
									horarioProfessorDiaItemVO.setDuracaoAula(turnoHorarioVO.getDuracaoAula());
								}
								horarioProfessorDiaVO.setHorarioAlterado(true);
								return true;
							}
						}
					}
					return false;
				}
			}
			HorarioProfessorDiaVO horarioProfessorDiaVO = new HorarioProfessorDiaVO();
			horarioProfessorDiaVO.setData(dataBase);
			horarioProfessorDiaVO.getHorarioProfessor().setCodigo(horarioProfessorVO.getCodigo());
			horarioProfessorDiaVO.setHorarioAlterado(true);
			
			getFacadeFactory().getHorarioProfessorDiaFacade().realizarCriacaoHorarioProfessorDiaItemVOs(horarioProfessorDiaVO, horarioProfessorVO, usuario);
			horarioProfessorDiaVO.adicinarTurmaEDisciplina(nrAula, disciplina, horarioTurmaVO.getTurma(), sala, false);
			horarioProfessorVO.getHorarioProfessorDiaVOs().add(horarioProfessorDiaVO);
			return true;
		}
		return false;

	}

	/**
	 * Este mï¿½todo realiza a inclusï¿½o da disciplina substitutiva no horario
	 * do professor substituto Caso exista algum choque de horario no horario do
	 * professor substituto ï¿½ realizada uma execeï¿½ï¿½o
	 * 
	 * @param horarioTurmaVO
	 * @param horarioTurmaDiaVOs
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param horarioProfessorVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private StringBuilder realizarInclusaoDisciplinaNoHorarioProfessorSubstituto(HorarioTurmaVO horarioTurmaVO, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, int numeroAula, boolean alterarTodasAulas, HorarioProfessorVO horarioProfessorVO, UsuarioVO usuarioVO, Boolean liberarProgramacaoAulaProfessorAcimaPermitido, Boolean validarChoqueSala) throws Exception {
		StringBuilder resultadoAcao = new StringBuilder();
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaDiaVOs) {
			for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
				if (horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(horarioTurmaVO.getProfessorAtual().getCodigo()) && horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplinaAtual().getCodigo())) {
					try {
						if (alterarTodasAulas) {
							if (!horarioTurmaDiaItemVO.getPossuiChoqueSala() && (!horarioTurmaDiaItemVO.getPossuiChoqueAulaExcesso() || liberarProgramacaoAulaProfessorAcimaPermitido)) {
								if (adicionarAulaHorarioProfessor(horarioTurmaVO, horarioTurmaVO.getDisciplinaSubstituta(), horarioTurmaDiaVO.getData(), horarioTurmaDiaItemVO.getNrAula(), horarioTurmaDiaItemVO.getHorarioInicio(), horarioTurmaDiaItemVO.getHorarioTermino(), horarioProfessorVO, horarioTurmaVO.getSalaSubstituta(), usuarioVO, true, false, 0, false, null, validarChoqueSala)) {
									//horarioTurmaVO.montarLogResultadoAcao("Incluído", horarioTurmaDiaItemVO.getNrAula(), horarioTurmaDiaItemVO.getHorario(), horarioTurmaDiaVO.getData(), horarioTurmaVO.getDisciplinaSubstituta(), horarioTurmaVO.getProfessorSubstituto());
									resultadoAcao.append("Inclusão da Disciplina " + horarioTurmaVO.getDisciplinaSubstituta().getNome() + " no dia " + horarioTurmaDiaVO.getData_Apresentar() + " e horário " + horarioTurmaDiaItemVO.getNrAula().toString() + "º (" + horarioTurmaDiaItemVO.getHorario() + ") do professor " + horarioTurmaVO.getProfessorSubstituto().getNome() + ".\n");
								}
							}
						} else if (horarioTurmaDiaItemVO.getNrAula().intValue() == numeroAula || numeroAula == 0) {
							if (!horarioTurmaDiaItemVO.getPossuiChoqueSala() && (!horarioTurmaDiaItemVO.getPossuiChoqueAulaExcesso() || liberarProgramacaoAulaProfessorAcimaPermitido)) {
								if (adicionarAulaHorarioProfessor(horarioTurmaVO, horarioTurmaVO.getDisciplinaSubstituta(), horarioTurmaDiaVO.getData(), horarioTurmaDiaItemVO.getNrAula(), horarioTurmaDiaItemVO.getHorarioInicio(), horarioTurmaDiaItemVO.getHorarioTermino(), horarioProfessorVO, horarioTurmaVO.getSalaSubstituta(), usuarioVO, true, false, 0, false, null, validarChoqueSala)) {
									//horarioTurmaVO.montarLogResultadoAcao("Incluído", horarioTurmaDiaItemVO.getNrAula(), horarioTurmaDiaItemVO.getHorario(), horarioTurmaDiaVO.getData(), horarioTurmaVO.getDisciplinaSubstituta(), horarioTurmaVO.getProfessorSubstituto());
									resultadoAcao.append("Inclusão da Disciplina " + horarioTurmaVO.getDisciplinaSubstituta().getNome() + " no dia " + horarioTurmaDiaVO.getData_Apresentar() + " e horário " + horarioTurmaDiaItemVO.getNrAula().toString() + "º (" + horarioTurmaDiaItemVO.getHorario() + ") do professor " + horarioTurmaVO.getProfessorSubstituto().getNome() + ".\n");
								}
							}
							if (numeroAula > 0) {
								break;
							}
						}
					} catch (Exception e) {
						throw e;
					}
				}
			}
			if (!alterarTodasAulas && numeroAula == 0) {
				break;
			}
		}
		
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			if (horarioProfessorDiaVO.getHorarioAlterado() || horarioProfessorDiaVO.isNovoObj()) {
				for (HorarioProfessorDiaItemVO hpdiVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
					q: for (HorarioTurmaDiaVO htdVO : horarioTurmaDiaVOs) {
						for (HorarioTurmaDiaItemVO htdiVO : htdVO.getHorarioTurmaDiaItemVOs()) {
							if (hpdiVO.getTurmaVO().getCodigo().equals(horarioTurmaVO.getTurma().getCodigo()) 
									&& hpdiVO.getDisciplinaVO().getCodigo().equals(horarioTurmaVO.getDisciplinaSubstituta().getCodigo()) 
									&& hpdiVO.getData_Apresentar().equals(htdVO.getData_Apresentar()) 
									&& hpdiVO.getNrAula().equals(htdiVO.getNrAula())
									&& !Uteis.isAtributoPreenchido(hpdiVO.getHorarioTurmaDiaItemVO().getCodigo())) {
								hpdiVO.setHorarioTurmaDiaItemVO(htdiVO);
								break q;
							}
						}
					}
				}				
			}
		}

		return resultadoAcao;
	}

	/**
	 * Verifica a existencia de choque de horario de uma turma com ela mesma
	 * porém em ano/semestre diferente, ou seja em outra programação de aula
	 * 
	 * @param horarioTurmaVO
	 * @param disciplinaBase
	 * @param dataBase
	 * @param numeroAula
	 * @param usuario
	 * @param retornarExcecao
	 * @return
	 * @throws Exception
	 */
	@Override
	public Boolean realizarVerificacaoChoqueHorarioTurmaDiaItemComHorarioTurmaOutroAnoSemestre(HorarioTurmaVO horarioTurmaVO, DisciplinaVO disciplinaBase, Date dataBase, int numeroAula, UsuarioVO usuario, boolean retornarExcecao) throws Exception {
		StringBuilder sql = new StringBuilder("select t.horarioturma, horarioturma.anovigente, horarioturma.semestrevigente, ");
		sql.append(" t.horarioinicioaula, t.horariofinalaula, disciplina.nome as disciplina, pessoa.nome as professor  ");
		sql.append(" from horarioturmadetalhado(null, ");
		sql.append(horarioTurmaVO.getTurma().getCodigo()).append(",null,null, null, null, ");
		sql.append("'").append(Uteis.getDataJDBC(dataBase)).append("', '").append(Uteis.getDataJDBC(dataBase)).append("') as t ");
		sql.append(" inner join horarioturma on horarioturma.codigo = t.horarioturma ");
		sql.append(" inner join disciplina on disciplina.codigo = t.disciplina ");
		sql.append(" inner join pessoa on pessoa.codigo = t.professor ");
		sql.append(" where t.nraula = ").append(numeroAula).append(" and t.horarioturma != ").append(horarioTurmaVO.getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			if (retornarExcecao) {
				String horario = rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula") + " (" + rs.getString("anovigente") + "/" + rs.getString("semestrevigente") + ")";
				String horarioBase = rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula") + " (" + horarioTurmaVO.getAnoVigente() + "/" + horarioTurmaVO.getSemestreVigente() + ")";
				throw new ChoqueHorarioVO(dataBase, numeroAula, numeroAula, horario, horarioBase, horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioTurmaVO.getTurma().getIdentificadorTurma(), rs.getString("disciplina"), disciplinaBase.getNome(), rs.getString("professor"), horarioTurmaVO.getProfessor().getNome(), horarioTurmaVO.getTurma().getTurno().getNome(), horarioTurmaVO.getTurno().getNome(), "", false, true);
			}
			return false;
		}
		return true;

	}

	public Boolean realizarVerificacaoChoqueHorarioTurmaDiaItemComHorarioProfessor(HorarioTurmaVO horarioTurmaVO, DisciplinaVO disciplina, Date dataBase, int numeroAula, String horarioInicio, String horarioTermino, HorarioProfessorVO horarioProfessorVO, SalaLocalAulaVO sala, UsuarioVO usuario, boolean retornarExcecao, boolean controlaNumeroMaximoAulaProgramadaProfessorDia, Integer numeroAulaMaximaProgramarProfessorDia, boolean liberarProgramacaoAulaProfessorAcimaPermitido, Map<String, Integer> aulaProgramadaPorDiaProfessor, Boolean validarChoqueSala) throws Exception {
		Integer minutoInicioAulaTurma = (((Uteis.getHoraDaString(horarioInicio) * 60) + Uteis.getMinutosDaString(horarioInicio)));
		Integer minutoTerminoAulaTurma = (Uteis.getHoraDaString(horarioTermino) * 60) + Uteis.getMinutosDaString(horarioTermino);
		String horarioBase = horarioInicio + " " + UteisJSF.internacionalizar("prt_a") + " " + horarioTermino;
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			if (horarioProfessorDiaVO.getData_Apresentar().equals(Uteis.getData(dataBase))) {
				Integer nrAulaProgramacaoProfessorDia = 0;
				if (aulaProgramadaPorDiaProfessor != null && controlaNumeroMaximoAulaProgramadaProfessorDia && !liberarProgramacaoAulaProfessorAcimaPermitido && numeroAulaMaximaProgramarProfessorDia > 0) {
					nrAulaProgramacaoProfessorDia = aulaProgramadaPorDiaProfessor.containsKey(Uteis.getData(dataBase)) ? aulaProgramadaPorDiaProfessor.get(Uteis.getData(dataBase)) : 0;
				}
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
					if (!horarioProfessorDiaItemVO.getDisciplinaLivre() && !horarioProfessorDiaItemVO.getTurmaLivre()) {
						Integer minutoInicioAulaProfessor = (((Uteis.getHoraDaString(horarioProfessorDiaItemVO.getHorarioInicio()) * 60) + Uteis.getMinutosDaString(horarioProfessorDiaItemVO.getHorarioInicio())));
						Integer minutoTerminoAulaProfessor = (Uteis.getHoraDaString(horarioProfessorDiaItemVO.getHorarioTermino()) * 60) + Uteis.getMinutosDaString(horarioProfessorDiaItemVO.getHorarioTermino());
						if (!validarDadosChoqueHorarioTurmaComHorarioProfessor(minutoInicioAulaTurma, minutoInicioAulaProfessor, minutoTerminoAulaProfessor, minutoTerminoAulaTurma, horarioProfessorDiaItemVO, horarioTurmaVO, dataBase, numeroAula, horarioBase, horarioProfessorVO.getProfessor(), disciplina, usuario, retornarExcecao) || (validarChoqueSala && getFacadeFactory().getHorarioTurmaDiaItemFacade().realizarVerificacaoChoqueHorarioSalaAula(horarioTurmaVO.getCodigo(), sala.getCodigo(), sala.getControlarChoqueSala(), dataBase, horarioInicio, horarioTermino, retornarExcecao)) || (controlaNumeroMaximoAulaProgramadaProfessorDia && !liberarProgramacaoAulaProfessorAcimaPermitido && numeroAulaMaximaProgramarProfessorDia > 0 && (nrAulaProgramacaoProfessorDia + 1) > numeroAulaMaximaProgramarProfessorDia)) {
							return true;
						}
						if (controlaNumeroMaximoAulaProgramadaProfessorDia && !liberarProgramacaoAulaProfessorAcimaPermitido && numeroAulaMaximaProgramarProfessorDia > 0) {
							nrAulaProgramacaoProfessorDia++;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Este mï¿½todo valida choque de horario do professor onde se o parametro
	 * retornarExcecao estiver como true irï¿½ retornar uma excecao caso
	 * contrario irï¿½ retornar true(Horario Disponivel) ou false(Horario
	 * Indisponivel)
	 * 
	 * @param minutosInicioTurnoADarAula
	 * @param minutosInicioTurno
	 * @param minutosFinalTurno
	 * @param minutosFinalTurnoADarAula
	 * @param horarioProfessorDiaItemVO
	 * @param horarioTurmaVO
	 * @param dataBase
	 * @param numeroAulaBase
	 * @param usuario
	 * @param retornarExcecao
	 * @return
	 * @throws Exception
	 */
	@Override
	public Boolean validarDadosChoqueHorarioTurmaComHorarioProfessor(int minutosInicioTurnoADarAula, int minutosInicioTurno, int minutosFinalTurno, int minutosFinalTurnoADarAula, HorarioProfessorDiaItemVO horarioProfessorDiaItemVO, HorarioTurmaVO horarioTurmaVO, Date dataBase, Integer numeroAulaBase, String horarioBase, PessoaVO professorBase, DisciplinaVO disciplinaBase, UsuarioVO usuario, boolean retornarExcecao) throws Exception {

		if (minutosInicioTurnoADarAula == minutosInicioTurno) {
			if (retornarExcecao) {
				throw new ChoqueHorarioVO(dataBase, horarioProfessorDiaItemVO.getNrAula(), numeroAulaBase, horarioProfessorDiaItemVO.getHorario(), horarioBase, horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma(), horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioProfessorDiaItemVO.getDisciplinaVO().getNome(), disciplinaBase.getNome(), professorBase.getNome(), professorBase.getNome(), horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome(), horarioTurmaVO.getTurno().getNome(), "", false, true);
				// throw new
				// Exception(UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorario").replace("{0}",
				// numeroAulaBase.toString() + "(" + horarioBase +
				// ")").replace("{1}", Uteis.getData(dataBase,
				// "dd/MM/yyyy")).replace("{2}",
				// horarioTurmaVO.getTurma().getIdentificadorTurma()).replace("{3}",
				// Uteis.getData(dataBase, "dd/MM/yyyy")).replace("{4}",
				// horarioProfessorDiaItemVO.getNrAula().toString() + "(" +
				// horarioProfessorDiaItemVO.getHorario() + ")").replace("{5}",
				// horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma()).replace("{6}",
				// horarioProfessorDiaItemVO.getDisciplinaVO().getNome()).replace("{7}",
				// horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome()));
			}
			return false;
		} else {
			if ((minutosInicioTurnoADarAula > minutosInicioTurno) && (minutosInicioTurnoADarAula < minutosFinalTurno)) {
				if (retornarExcecao) {
					throw new ChoqueHorarioVO(dataBase, horarioProfessorDiaItemVO.getNrAula(), numeroAulaBase, horarioProfessorDiaItemVO.getHorario(), horarioBase, horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma(), horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioProfessorDiaItemVO.getDisciplinaVO().getNome(), disciplinaBase.getNome(), professorBase.getNome(), professorBase.getNome(), horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome(), horarioTurmaVO.getTurno().getNome(), "", false, true);
					// throw new
					// Exception(UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorario").replace("{0}",
					// numeroAulaBase.toString() + "(" + horarioBase +
					// ")").replace("{1}", Uteis.getData(dataBase,
					// "dd/MM/yyyy")).replace("{2}",
					// horarioTurmaVO.getTurma().getIdentificadorTurma()).replace("{3}",
					// Uteis.getData(dataBase, "dd/MM/yyyy")).replace("{4}",
					// horarioProfessorDiaItemVO.getNrAula().toString() + "(" +
					// horarioProfessorDiaItemVO.getHorario() +
					// ")").replace("{5}",
					// horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma()).replace("{6}",
					// horarioProfessorDiaItemVO.getDisciplinaVO().getNome()).replace("{7}",
					// horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome()));
				}
				return false;
			} else {
				if ((minutosFinalTurnoADarAula > minutosInicioTurno) && (minutosFinalTurnoADarAula < minutosFinalTurno)) {
					if (retornarExcecao) {
						throw new ChoqueHorarioVO(dataBase, horarioProfessorDiaItemVO.getNrAula(), numeroAulaBase, horarioProfessorDiaItemVO.getHorario(), horarioBase, horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma(), horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioProfessorDiaItemVO.getDisciplinaVO().getNome(), disciplinaBase.getNome(), professorBase.getNome(), professorBase.getNome(), horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome(), horarioTurmaVO.getTurno().getNome(), "", false, true);
						// throw new
						// Exception(UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorario").replace("{0}",
						// numeroAulaBase.toString() + "(" + horarioBase +
						// ")").replace("{1}", Uteis.getData(dataBase,
						// "dd/MM/yyyy")).replace("{2}",
						// horarioTurmaVO.getTurma().getIdentificadorTurma()).replace("{3}",
						// Uteis.getData(dataBase, "dd/MM/yyyy")).replace("{4}",
						// horarioProfessorDiaItemVO.getNrAula().toString() +
						// "(" + horarioProfessorDiaItemVO.getHorario() +
						// ")").replace("{5}",
						// horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma()).replace("{6}",
						// horarioProfessorDiaItemVO.getDisciplinaVO().getNome()).replace("{7}",
						// horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome()));
					}
					return false;
				} else {
					if ((minutosInicioTurno > minutosInicioTurnoADarAula) && (minutosInicioTurno < minutosFinalTurnoADarAula)) {
						if (retornarExcecao) {
							throw new ChoqueHorarioVO(dataBase, horarioProfessorDiaItemVO.getNrAula(), numeroAulaBase, horarioProfessorDiaItemVO.getHorario(), horarioBase, horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma(), horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioProfessorDiaItemVO.getDisciplinaVO().getNome(), disciplinaBase.getNome(), professorBase.getNome(), professorBase.getNome(), horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome(), horarioTurmaVO.getTurno().getNome(), "", false, true);
							// throw new
							// Exception(UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorario").replace("{0}",
							// numeroAulaBase.toString() + "(" + horarioBase +
							// ")").replace("{1}", Uteis.getData(dataBase,
							// "dd/MM/yyyy")).replace("{2}",
							// horarioTurmaVO.getTurma().getIdentificadorTurma()).replace("{3}",
							// Uteis.getData(dataBase,
							// "dd/MM/yyyy")).replace("{4}",
							// horarioProfessorDiaItemVO.getNrAula().toString()
							// + "(" + horarioProfessorDiaItemVO.getHorario() +
							// ")").replace("{5}",
							// horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma()).replace("{6}",
							// horarioProfessorDiaItemVO.getDisciplinaVO().getNome()).replace("{7}",
							// horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome()));
						}
						return false;
					} else {

						if ((minutosFinalTurno > minutosInicioTurnoADarAula) && (minutosFinalTurno < minutosFinalTurnoADarAula)) {
							if (retornarExcecao) {
								throw new ChoqueHorarioVO(dataBase, horarioProfessorDiaItemVO.getNrAula(), numeroAulaBase, horarioProfessorDiaItemVO.getHorario(), horarioBase, horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma(), horarioTurmaVO.getTurma().getIdentificadorTurma(), horarioProfessorDiaItemVO.getDisciplinaVO().getNome(), disciplinaBase.getNome(), professorBase.getNome(), professorBase.getNome(), horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome(), horarioTurmaVO.getTurno().getNome(), "", false, true);
								// throw new
								// Exception(UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorario").replace("{0}",
								// numeroAulaBase.toString() + "(" + horarioBase
								// + ")").replace("{1}", Uteis.getData(dataBase,
								// "dd/MM/yyyy")).replace("{2}",
								// horarioTurmaVO.getTurma().getIdentificadorTurma()).replace("{3}",
								// Uteis.getData(dataBase,
								// "dd/MM/yyyy")).replace("{4}",
								// horarioProfessorDiaItemVO.getNrAula().toString()
								// + "(" +
								// horarioProfessorDiaItemVO.getHorario() +
								// ")").replace("{5}",
								// horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma()).replace("{6}",
								// horarioProfessorDiaItemVO.getDisciplinaVO().getNome()).replace("{7}",
								// horarioProfessorDiaItemVO.getTurmaVO().getTurno().getNome()));
							}
							return false;
						}
					}

				}

			}
		}
		return true;

	}

	@Override
	public List<DisciplinaVO> realizarObtencaoDisciplinaLecionadaProfessor(HorarioTurmaVO horarioTurmaVO, Integer professor) {
		Map<Integer, DisciplinaVO> disciplinas = new HashMap<Integer, DisciplinaVO>(0);
		if (professor != null && professor > 0) {
			for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
					if (horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(professor)) {
						if (!disciplinas.containsKey(horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo())) {
							disciplinas.put(horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo(), horarioTurmaDiaItemVO.getDisciplinaVO());
						}
					}
				}
			}
		}
		List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>();
		disciplinaVOs.addAll(disciplinas.values());
		return disciplinaVOs;
	}

	@Override
	public Map<String, Date> consultarPeriodoAulaDisciplinaPorTurmaDisciplinaAnoSemestre(Integer turma, Integer disciplina, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select min(horarioturmadia.data) as dataInicio, max(horarioturmadia.data) as dataTermino from horarioturma ");
		sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sql.append(" inner join turma on turma.codigo = horarioturma.turma");
		sql.append(" where turma.codigo in (");
		sql.append(" 		select codigo from turma where codigo = ").append(turma);
		sql.append(" 		union ");
		sql.append(" 		select turmaorigem from turmaagrupada   where turma = ").append(turma);
		sql.append(" 		union ");
		sql.append(" 		select codigo from turma   where subturma and turmaprincipal = ").append(turma);
		sql.append(" )	");
		sql.append(" and horarioturmadiaitem.disciplina = ").append(disciplina);
		sql.append(" and ((turma.anual and horarioturma.anovigente = '").append(ano).append("') ");
		sql.append(" or (turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("') ");
		sql.append(" or (turma.semestral = false  and turma.anual =  false ))");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			Map<String, Date> map = new HashMap<String, Date>(0);
			map.put("DATA_INICIO", rs.getDate("dataInicio"));
			map.put("DATA_TERMINO", rs.getDate("dataTermino"));
		}
		return null;
	}

	/*
	 * Caso exista programação de aula para a tuma/turma agrupada, disciplina e
	 * ano/semestre passado como parâmetro em outra turma/turma agrupada no
	 * mesmo ano/semestre, o sistema irá lançar uma exceção impedindo o usuário
	 * de continuar a operação.
	 * 
	 * @author Wendel Rodrigues
	 * 
	 * @version 5.0.3
	 * 
	 * @since 2015-02-26
	 */
	public List<HorarioTurmaDisciplinaProgramadaVO> realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcecao) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select horarioturma.codigo, turma.identificadorturma, turma.turmaagrupada, turma.subturma, turma.tipoSubturma, turma.situacao  ");
		if (!retornarExcecao) {
			sqlStr.append(" , sum(duracaoaula) as chProgramada, array_to_string(array_agg( distinct pessoa.nome order by pessoa.nome), ', ') as professores ");
		}
		sqlStr.append(" from horarioturma ");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" where ((turma.anual and horarioturma.anovigente = '").append(ano).append("') ");
		sqlStr.append(" or (turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("') ");
		sqlStr.append(" or (turma.semestral = false and turma.anual = false)) and turma.codigo != ").append(turmaVO.getCodigo());
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" and (disciplina.codigo = ").append(disciplina).append(" or disciplina.codigo in (select equivalente from disciplinaequivalente  where disciplina = ").append(disciplina).append(")) ");
			sqlStr.append(" and horarioturma.turma in ( ");
			sqlStr.append(" 	select turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo());
			sqlStr.append(" 	union ");
			sqlStr.append(" 	select distinct turmaorigem from turmaagrupada where turmaorigem != ").append(turmaVO.getCodigo()).append(" and turma in (select turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo()).append(") ");
			sqlStr.append(" ) ");
		} else if (turmaVO.getSubturma()) {
			if (turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" and (disciplina.codigo = ").append(disciplina).append(" or disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(")) ");
			} else {
				sqlStr.append(" and (disciplina.codigo = ").append(disciplina).append(" or disciplina.codigo in (select equivalente from disciplinaequivalente  where disciplina = ").append(disciplina).append(")) ");
			}
			sqlStr.append(" and horarioturma.turma in ( ");
			sqlStr.append(" 	select turmaprincipal from turma where subturma = true and codigo = ").append(turmaVO.getCodigo());
			if (turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" union ");
				sqlStr.append(" select turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo());
				sqlStr.append(" union ");
				sqlStr.append(" select distinct turmaorigem from turmaagrupada inner join turma t on t.codigo = turmaagrupada.turmaorigem where t.subturma = false and turmaorigem != ").append(turmaVO.getCodigo()).append(" and turma in (select turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo()).append(") ");
			}
			sqlStr.append(" ) ");
		} else {
			sqlStr.append(" and (disciplina.codigo = ").append(disciplina).append(" or disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(")) ");
			sqlStr.append(" and horarioturma.turma in ( ");
			sqlStr.append(" 	select turmaorigem from turmaagrupada where turma = ").append(turmaVO.getCodigo());
			sqlStr.append(" 	union ");
			sqlStr.append(" 	select codigo from turma where subturma and turmaprincipal = ").append(turmaVO.getCodigo());
			sqlStr.append(" ) ");
		}
		if (!retornarExcecao) {
			sqlStr.append(" group by horarioturma.codigo, turma.identificadorturma, turma.turmaagrupada, turma.subturma, turma.tipoSubturma, turma.situacao ");
		}
		if (retornarExcecao) {
			sqlStr.append(" limit 1");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaVOs = new ArrayList<HorarioTurmaDisciplinaProgramadaVO>(0);
		while (rs.next()) {
			if (retornarExcecao) {
				if (turmaVO.getTurmaAgrupada() && rs.getBoolean("turmaagrupada")) {
					throw new Exception(UteisJSF.internacionalizar("msg_HorarioTurma_existeAulaProgramadaParaDisciplinaParaTurmaAgrupadaETurmaAgrupada").replace("{0}", rs.getString("identificadorturma")).replace("{1}", turmaVO.getIdentificadorTurma()));
				} else if (turmaVO.getTurmaAgrupada() && !rs.getBoolean("turmaagrupada") && !rs.getBoolean("subturma")) {
					throw new Exception(UteisJSF.internacionalizar("msg_HorarioTurma_existeAulaProgramadaParaDisciplinaParaTurmaAgrupadaETurma").replace("{0}", rs.getString("identificadorturma")).replace("{1}", turmaVO.getIdentificadorTurma()));
				} else if (rs.getBoolean("subturma")) {
					throw new Exception(UteisJSF.internacionalizar("msg_HorarioTurma_existeAulaProgramadaParaDisciplinaParaTurmaAgrupadaETurma").replace("{0}", rs.getString("identificadorturma")).replace("{1}", turmaVO.getIdentificadorTurma()));
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_HorarioTurma_existeAulaProgramadaParaDisciplinaParaTurma").replace("{0}", rs.getString("identificadorturma")));
				}
			}
			HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO = new HorarioTurmaDisciplinaProgramadaVO();
			horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().setCodigo(rs.getInt("codigo"));
			horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().getTurma().setIdentificadorTurma(rs.getString("identificadorturma"));
			horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().getTurma().setTurmaAgrupada(rs.getBoolean("turmaagrupada"));
			horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().getTurma().setSubturma(rs.getBoolean("subturma"));
			horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().getTurma().setSituacao(rs.getString("situacao"));
			horarioTurmaDisciplinaProgramadaVO.setChProgramada(rs.getInt("chProgramada"));			
			horarioTurmaDisciplinaProgramadaVO.setProfessores(rs.getString("professores"));
			for(String prof: rs.getString("professores").split(",")){
				horarioTurmaDisciplinaProgramadaVO.getProfessorVOs().add(prof.trim());
			}
			if(horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().getTurma().getSubturma() && !rs.getString("tipoSubturma").isEmpty()){
				horarioTurmaDisciplinaProgramadaVO.getHorarioTurmaVO().getTurma().setTipoSubTurma(TipoSubTurmaEnum.valueOf(rs.getString("tipoSubturma")));
			}
			horarioTurmaVOs.add(horarioTurmaDisciplinaProgramadaVO);
		}
		return horarioTurmaVOs;
	}

	public Boolean realizadaValidacaoChoqueSalaJaDetecdata(List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, Date data, Integer nrAula) {
		HorarioTurmaDiaVO horarioTurmaDiaVO = realizadaObtencaoHorarioTurmaDiaPorData(horarioTurmaDiaVOs, data);
		if (horarioTurmaDiaVO != null) {
			HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = realizadaObtencaoHorarioTurmaDiaItemPorNrAula(horarioTurmaDiaVO, nrAula);
			if (horarioTurmaDiaItemVO != null) {
				return horarioTurmaDiaItemVO.getPossuiChoqueSala();
			}
		}
		return false;
	}

	@Override
	public HorarioTurmaDiaVO realizadaObtencaoHorarioTurmaDiaPorData(List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, Date data) {
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaDiaVOs) {
			if (Uteis.getData(horarioTurmaDiaVO.getData()).equals(Uteis.getData(data))) {
				return horarioTurmaDiaVO;
			}
		}
		return null;
	}

	@Override
	public HorarioTurmaDiaItemVO realizadaObtencaoHorarioTurmaDiaItemPorNrAula(HorarioTurmaDiaVO horarioTurmaDiaVO, Integer nrAula) {
		for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
			if (horarioTurmaDiaItemVO.getNrAula().equals(nrAula)) {
				return horarioTurmaDiaItemVO;
			}
		}
		return null;
	}

	@Override
	public List<ProgramacaoAulaResumoSemanaVO> realizarMontagemListaProgramacaoAulaResumoSemanaVO(HorarioTurmaVO horarioTurmaVO) {
		Map<String, ProgramacaoAulaResumoSemanaVO> mapProgramacaoAulaResumoSemanaVO = new HashMap<String, ProgramacaoAulaResumoSemanaVO>();
		ProgramacaoAulaResumoSemanaVO programacaoAulaResumoSemanaVO = null;
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
			Date dataInicio = UteisData.getPrimeiroDiaSemana(horarioTurmaDiaVO.getData());
			Date dataTermino = UteisData.getUltimoDiaSemana(horarioTurmaDiaVO.getData());
			String key = Uteis.getData(dataInicio) + "-" + Uteis.getData(dataTermino);
			if (!mapProgramacaoAulaResumoSemanaVO.containsKey(key)) {
				programacaoAulaResumoSemanaVO = new ProgramacaoAulaResumoSemanaVO();
				programacaoAulaResumoSemanaVO.setDataInicio(dataInicio);
				programacaoAulaResumoSemanaVO.setDataTermino(dataTermino);

			} else {
				programacaoAulaResumoSemanaVO = mapProgramacaoAulaResumoSemanaVO.get(key);
			}
			boolean possuiAula = false;
			q: for (HorarioTurmaDiaItemVO diaItem : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
				if (!diaItem.getDisciplinaLivre()) {
					possuiAula = true;
					for (ProgramacaoAulaResumoDisciplinaSemanaVO item : programacaoAulaResumoSemanaVO.getProgramacaoAulaResumoDisciplinaSemanaVOs()) {
						if (item.getDisciplinaVO().getCodigo().equals(diaItem.getDisciplinaVO().getCodigo())) {
							item.setNumeroAulaProgramada(item.getNumeroAulaProgramada() + 1);
							item.setCargaHorariaProgramada(item.getCargaHorariaProgramada() + diaItem.getDuracaoAula());
							item.setHorario(item.getHorario() + "<tr class=\"rich-table-row rich-table-firstrow\"><th class=\"rich-table-cell\" width=\"50%\" style=\"text-align:center\">" + Uteis.getData(horarioTurmaDiaVO.getData()) + "</th><th class=\"rich-table-cell\" width=\"50%\" style=\"text-align:center\">" + diaItem.getNrAula() + "ª (" + diaItem.getHorarioInicio() + " - " + diaItem.getHorarioTermino() + ")</th></tr>");
							item.getListaDataDisciplina().add(Uteis.getData(horarioTurmaDiaVO.getData()));
							item.getListaHorarioDisciplina().add(diaItem.getNrAula() + "ª (" + diaItem.getHorarioInicio() + " - " + diaItem.getHorarioTermino());
							if (Uteis.isAtributoPreenchido(diaItem.getSala().getCodigo()) && !item.getSala().contains(diaItem.getSala().getLocalSala())) {
								if (!item.getSala().trim().isEmpty()) {
									item.setSala(item.getSala() + "\n" + diaItem.getSala().getLocalSala());
								} else {
									item.setSala(diaItem.getSala().getLocalSala());
								}
							}
							if (!item.getProfessor().contains(diaItem.getFuncionarioVO().getNome())) {
								item.setProfessor(item.getProfessor() + "\n" + diaItem.getFuncionarioVO().getNome());
							}
							continue q;
						}
					}
					ProgramacaoAulaResumoDisciplinaSemanaVO item = new ProgramacaoAulaResumoDisciplinaSemanaVO();
					item.setDisciplinaVO(diaItem.getDisciplinaVO());
					item.setNumeroAulaProgramada(1);
					item.setCargaHorariaProgramada(diaItem.getDuracaoAula());
					item.setSala(diaItem.getSala().getLocalSala());
					item.setHorario("<tr  style=\"border:none;background-color:#999999\"><th class=\"rich-mpnl-text rich-table-header\" width=\"50%\">Data</th><th class=\"rich-mpnl-text rich-table-header\" width=\"50%\">Aula</th></tr><tr class=\"rich-table-row rich-table-firstrow\" ><th class=\"rich-table-cell\" width=\"50%\" style=\"text-align:center\">" + Uteis.getData(horarioTurmaDiaVO.getData()) + "</th><th class=\"rich-table-cell\" width=\"50%\" style=\"text-align:center\">" + diaItem.getNrAula() + "ª (" + diaItem.getHorarioInicio() + " - " + diaItem.getHorarioTermino() + ")</th></tr>");
					item.getListaDataDisciplina().add(Uteis.getData(horarioTurmaDiaVO.getData()));
					item.getListaHorarioDisciplina().add(diaItem.getNrAula() + "ª (" + diaItem.getHorarioInicio() + " - " + diaItem.getHorarioTermino());
					item.setProfessor(diaItem.getFuncionarioVO().getNome());
					programacaoAulaResumoSemanaVO.getProgramacaoAulaResumoDisciplinaSemanaVOs().add(item);
				}
			}
			if (!mapProgramacaoAulaResumoSemanaVO.containsKey(key) && possuiAula) {
				mapProgramacaoAulaResumoSemanaVO.put(key, programacaoAulaResumoSemanaVO);
			}
		}
		List<ProgramacaoAulaResumoSemanaVO> programacaoAulaResumoSemanaVOs = new ArrayList<ProgramacaoAulaResumoSemanaVO>();
		programacaoAulaResumoSemanaVOs.addAll(mapProgramacaoAulaResumoSemanaVO.values());
		Ordenacao.ordenarLista(programacaoAulaResumoSemanaVOs, "dataInicio");
		for (ProgramacaoAulaResumoSemanaVO obj : programacaoAulaResumoSemanaVOs) {
			Ordenacao.ordenarLista(obj.getProgramacaoAulaResumoDisciplinaSemanaVOs(), "ordenacao");
		}
		return programacaoAulaResumoSemanaVOs;
	}

	@Override
	public SqlRowSet executarConsultaAulasProgramadasNaoRegistradas(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String mes, String anoMes, String periodicidade, String tipoFiltroPeriodo) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select horarioturmadia_data, horarioturmadiaitem_nraula, horarioturmadiaitem_duracaoaula, unidadeensino_nome, curso_nome, turno_nome, identificadorturma, disciplina_codigo, disciplina_nome, professor_nome, ");
		sqlStr.append("count(distinct horarioturmadiaitem_nraula) as qtdAulasNaoRegistradas ");
		sqlStr.append("from (");
		sqlStr.append("select distinct horarioturmadia.data as horarioturmadia_data, horarioturmadiaitem.nraula as horarioturmadiaitem_nraula, horarioturmadiaitem.duracaoaula as horarioturmadiaitem_duracaoaula, ");
		sqlStr.append("unidadeensino.nome as unidadeensino_nome, curso.nome as curso_nome, turno.nome as turno_nome, turma.identificadorturma, disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, professor.nome as professor_nome ");
		sqlStr.append("from horarioturma ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append("inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append("inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append("left join registroaula on registroaula.turma = turma.codigo ");
		sqlStr.append("and registroaula.disciplina = horarioturmadiaitem.disciplina ");
		sqlStr.append("and registroaula.data::DATE = horarioturmadia.data::DATE ");
		sqlStr.append("and registroaula.nrAula = horarioturmadiaitem.nrAula ");
		sqlStr.append("inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ");
		sqlStr.append("left join turmaagrupada on turmaagrupada.turmaOrigem = turma.codigo ");
		sqlStr.append("left join turma turma2 on turma2.codigo = turmaagrupada.turma ");
		sqlStr.append("inner join curso on curso.codigo in (case when turma.turmaagrupada then turma2.curso else turma.curso end) ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.codigo = unidadeensinocurso.unidadeEnsino ");
		sqlStr.append("where unidadeensino.codigo = ").append(unidadeEnsino);
		if (Uteis.isAtributoPreenchido(curso) && !Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and turno.codigo = ").append(turno);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and (turma.codigo = ").append(turma).append(" or turma2.codigo = ").append(turma).append(") ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sqlStr.append(" and professor.codigo = ").append(professor);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and anovigente = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and semestrevigente = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(mes) && Uteis.isAtributoPreenchido(anoMes) && (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && !tipoFiltroPeriodo.equals("porPeriodoData"))) {
			sqlStr.append(" and extract(month from horarioturmadia.data)::INT = ").append(mes);
			sqlStr.append(" and extract(year from horarioturmadia.data)::INT = ").append(anoMes);
		}

		if (Uteis.isAtributoPreenchido(dataInicio) && (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && tipoFiltroPeriodo.equals("porPeriodoData"))) {
			sqlStr.append(" and horarioturmadia.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataFim) && (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && tipoFiltroPeriodo.equals("porPeriodoData"))) {
			sqlStr.append(" and horarioturmadia.data <= '").append(Uteis.getDataJDBC(dataFim)).append("'");
		}
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("'");
		}
		sqlStr.append(" and registroaula is null");
		sqlStr.append(") as t ");
		sqlStr.append("group by horarioturmadia_data, horarioturmadiaitem_nraula, horarioturmadiaitem_duracaoaula, unidadeensino_nome, curso_nome, turno_nome, identificadorturma, disciplina_codigo, disciplina_nome, professor_nome ");
		sqlStr.append("order by horarioturmadia_data, horarioturmadiaitem_nraula");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	@Override
	public SqlRowSet executarConsultaAulasProgramadasNaoRegistradasRegistroAulaLancadaNaoLancadaRel(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String mes, String anoMes, String periodicidade) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select horarioturmadia_data, unidadeensino_nome, curso_nome, turno_nome, identificadorturma, disciplina_codigo, disciplina_nome, professor_nome, ");
		sqlStr.append("count(distinct horarioturmadiaitem_nraula) as qtdAulasNaoRegistradas ");
		sqlStr.append("from (");
		sqlStr.append("select distinct horarioturmadia.data as horarioturmadia_data, horarioturmadiaitem.nraula as horarioturmadiaitem_nraula, ");
		sqlStr.append("unidadeensino.nome as unidadeensino_nome, turno.nome as turno_nome, turma.identificadorturma, disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, professor.nome as professor_nome, ");
		sqlStr.append("case when turma.turmaagrupada then array_to_string(array(select distinct c.nome from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma inner join curso as c on c.codigo = t.curso where turmaagrupada.turmaorigem = turma.codigo order by c.nome), ', ') else curso.nome end as curso_nome ");
		sqlStr.append("from horarioturma ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append("inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append("inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append("left join registroaula on registroaula.turma = turma.codigo ");
		sqlStr.append("and registroaula.disciplina = horarioturmadiaitem.disciplina ");
		sqlStr.append("and registroaula.data::DATE = horarioturmadia.data::DATE ");
		sqlStr.append("and registroaula.nrAula = horarioturmadiaitem.nrAula ");
		sqlStr.append("inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ");
		sqlStr.append("left join turmaagrupada on turmaagrupada.turmaOrigem = turma.codigo ");
		sqlStr.append("left join turma turma2 on turma2.codigo = turmaagrupada.turma ");
		sqlStr.append("inner join curso on curso.codigo in (case when turma.turmaagrupada then turma2.curso else turma.curso end) ");
		sqlStr.append("inner join turno on turno.codigo = turma.turno ");
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.codigo in (case when turma.turmaagrupada then turma2.unidadeensino else turma.unidadeensino end) ");
		sqlStr.append("where unidadeensino.codigo = ").append(unidadeEnsino);
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and turno.codigo = ").append(turno);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and (turma.codigo = ").append(turma).append(" or turma2.codigo = ").append(turma).append(") ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sqlStr.append(" and professor.codigo = ").append(professor);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and anovigente = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and semestrevigente = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(mes) && Uteis.isAtributoPreenchido(anoMes)) {
			sqlStr.append(" and extract(month from horarioturmadia.data)::INT = ").append(mes);
			sqlStr.append(" and extract(year from horarioturmadia.data)::INT = ").append(anoMes);
		}
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" and horarioturmadia.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" and horarioturmadia.data <= '").append(Uteis.getDataJDBC(dataFim)).append("'");
		}
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("'");
		}
		sqlStr.append(" and registroaula is null");
		sqlStr.append(") as t ");
		sqlStr.append("group by unidadeensino_nome, curso_nome, turno_nome, identificadorturma, disciplina_codigo, disciplina_nome, professor_nome, horarioturmadia_data ");
		sqlStr.append("order by curso_nome, turno_nome, identificadorturma, disciplina_nome, professor_nome, horarioturmadia_data");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	@Override
	public Date consultarUpdatedPorTurmaAnoSemestre(Integer turma, String ano, String semestre, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		HorarioTurma.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		String sql = "SELECT updated FROM HorarioTurma WHERE turma = ? and anovigente = ? and semestrevigente = ?";
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { turma, ano, semestre });
		Date updated = null;
		if (rowSet.next()) {
			updated = rowSet.getTimestamp("updated");
		}
		return updated;
	}

	public HorarioTurmaVO consultarPorTurmaDisciplina(Integer turma, Integer disciplina) throws Exception {
		String sql = "select min(horarioturmadiaitem.codigo) as codigomodulo, horarioturma.turma as codigoturma, disciplina.nome as nomeDisciplina, disciplina.codigo as codigoDisciplina, min(horarioturmadiaitem.data) as dataInicio, max(horarioturmadiaitem.data) as datatermino, pessoa.nome as nomeProfessor "
				+ " from horarioturmadiaitem inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia "
				+ " inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina "
				+ " inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor "
				+ " where horarioturma.turma = ? and disciplina.codigo = ? "
				+ " group by disciplina.nome, disciplina.codigo, pessoa.nome, horarioturma.turma ";
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { turma, disciplina });
		HorarioTurmaVO horario = new HorarioTurmaVO();
		if (rowSet.next()) {
			horario.setCodigo(rowSet.getInt("codigomodulo"));
			horario.getTurma().setCodigo(rowSet.getInt("codigoturma"));
			horario.getDisciplina().setNome(rowSet.getString("nomeDisciplina"));
			horario.getDisciplina().setCodigo(rowSet.getInt("codigoDisciplina"));
			horario.setDiaInicio(Uteis.getDataJDBC(rowSet.getDate("datainicio")));
			horario.setDiaFim(Uteis.getDataJDBC(rowSet.getDate("datatermino")));
			horario.getProfessor().setNome(rowSet.getString("nomeProfessor"));
		}
		return horario;
	}

	public List<HorarioTurmaVO> consultarPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		String sql = "select min(horarioturmadiaitem.codigo) as codigomodulo, horarioturma.turma as codigoturma, disciplina.nome as nomeDisciplina, disciplina.codigo as codigoDisciplina, min(horarioturmadiaitem.data) as dataInicio, max(horarioturmadiaitem.data) as datatermino, pessoa.nome as nomeProfessor "
				+ " from horarioturmadiaitem inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia "
				+ " inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina "
				+ " inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor "
				+ " inner join turma on turma.codigo = horarioturma.turma "
				+ " where turma.unidadeensino = ? and turma.situacao = 'AB' "
				+ " group by disciplina.nome, disciplina.codigo, pessoa.nome, horarioturma.turma ";
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { unidadeEnsino });
		List<HorarioTurmaVO> lista = new ArrayList<HorarioTurmaVO>();		
		//if (rowSet.next()) {
		while (rowSet.next()) {
			HorarioTurmaVO horario = new HorarioTurmaVO();
			horario.setCodigo(rowSet.getInt("codigomodulo"));
			horario.getTurma().setCodigo(rowSet.getInt("codigoturma"));
			horario.getDisciplina().setNome(rowSet.getString("nomeDisciplina"));
			horario.getDisciplina().setCodigo(rowSet.getInt("codigoDisciplina"));
			horario.setDiaInicio(Uteis.getDataJDBC(rowSet.getDate("datainicio")));
			horario.setDiaFim(Uteis.getDataJDBC(rowSet.getDate("datatermino")));
			horario.getProfessor().setNome(rowSet.getString("nomeProfessor"));
			lista.add(horario);
		}
		return lista;
	}
	
	@Override
	public void alterarUpdatedPorCodigo(final HorarioTurmaVO horarioTurma, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		HorarioTurma.consultar(idEntidade, verificarAcesso, usuarioVO);
		horarioTurma.setUpdated(new Date());
		final String sql = "UPDATE HorarioTurma SET updated = ? WHERE (codigo=?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(horarioTurma.getUpdated()));
				sqlAlterar.setInt(2, horarioTurma.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Override
	public List<HorarioTurmaDisciplinaProgramadaVO> consultarHorarioTurmaDisciplinaProgramadaPorTurmaEDisciplinaTrazendoSala(Integer turma, Integer disciplina, String ano, String semestre, boolean trazerDisciplinaCompostaPrincipal) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select t.*, sala.sala, local.local ");
		sqlStr.append(" from ( ");
		// Tras as disciplina compostas da grade disciplina de uma turma normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
		sqlStr.append(" union ");
		// Tras as disciplina da grade disciplina de uma turma normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
		sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos ");
		sqlStr.append(" union ");
		// Tras as disciplina compostas da grade disciplina de uma turma
		// composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina2.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
		sqlStr.append(" union");
		// Tras as disciplina da grade disciplina de uma turma composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
		sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" where gradedisciplina.disciplinacomposta = false and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos ");
		sqlStr.append(" union ");
		// Tras as disciplina compostas de um grupo de optativas de uma turma
		// normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
		sqlStr.append(" union ");
		// Tras as disciplina de um grupo de optativas de uma turma normal
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada =  false");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula ");
		sqlStr.append(" union ");
		// Tras as disciplina compostas de um grupo de optativas de uma turma
		// composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos, max(gradedisciplinacomposta.horaaula) as horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" left join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina2.codigo ");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join gradedisciplinacomposta on (case when turmadisciplinacomposta.codigo is not null then gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta else gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina.codigo end)");
		sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrCreditos ");
		sqlStr.append(" union ");
		// Tras as disciplina de um grupo de optativas de uma turma composta
		sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
		sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
		sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
		sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
		sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta = false and turma.turmaagrupada");
		sqlStr.append(" and turma.codigo = ").append(turma);
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, gradeCurricularGrupoOptativaDisciplina.horaaula ");

		if (trazerDisciplinaCompostaPrincipal) {
			// Tras as disciplina compostas da grade disciplina de uma turma
			// normal
			sqlStr.append(" union");
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" and disciplina.codigo = ").append(disciplina);
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos ");
			sqlStr.append(" union");
			// Tras as disciplina compostas da grade disciplina de uma turma
			// composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos, max(gradedisciplina.horaaula) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina2.gradedisciplina");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradedisciplina.disciplinacomposta and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" and disciplina.codigo = ").append(disciplina);
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradedisciplina.cargahoraria, gradedisciplina.nrCreditos ");
			sqlStr.append(" union");
			// Tras as disciplina compostas de um grupo de optativas de uma
			// turma
			// normal
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, max(gradeCurricularGrupoOptativaDisciplina.cargahoraria) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada =  false");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" and disciplina.codigo = ").append(disciplina);
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos ");
			sqlStr.append(" union");
			// Tras as disciplina compostas de um grupo de optativas de uma
			// turma
			// composta
			sqlStr.append(" select distinct disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos, max(gradeCurricularGrupoOptativaDisciplina.cargahoraria) as horaaula from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as turma2 on turma2.codigo = turmaagrupada.turma");
			sqlStr.append(" inner join turmadisciplina as turmadisciplina2 on turmadisciplina2.turma = turma2.codigo");
			sqlStr.append(" and turmadisciplina2.disciplina = turmadisciplina.disciplina");
			sqlStr.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmadisciplina2.gradeCurricularGrupoOptativaDisciplina");
			sqlStr.append(" inner join disciplina on gradeCurricularGrupoOptativaDisciplina.disciplina = disciplina.codigo");
			sqlStr.append(" where gradeCurricularGrupoOptativaDisciplina.disciplinacomposta and turma.turmaagrupada");
			sqlStr.append(" and turma.codigo = ").append(turma);
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" and disciplina.codigo = ").append(disciplina);
			}
			sqlStr.append(" group by disciplina.codigo, disciplina.nome, gradeCurricularGrupoOptativaDisciplina.cargahoraria, gradeCurricularGrupoOptativaDisciplina.nrCreditos ");
		}
		sqlStr.append(" ) as t ");
		sqlStr.append(" left join salalocalaula as sala on sala.codigo = ( ");
		sqlStr.append(" select sala from horarioturma ");
		sqlStr.append(" inner join   horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append(" where horarioturmadiaitem.disciplina = t.codigo ");
		sqlStr.append(" and horarioturma.turma = ").append(turma);
		sqlStr.append(" and horarioturmadiaitem.sala is not null ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and anovigente ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and semestrevigente = '").append(semestre).append("'  ");
		}
		sqlStr.append(" limit 1 ) ");
		sqlStr.append(" left join localaula as local on sala.localaula = local.codigo ");
		sqlStr.append(" order by nome ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = new ArrayList<HorarioTurmaDisciplinaProgramadaVO>(0);
		HorarioTurmaDisciplinaProgramadaVO obj = null;
		if (rs.next()) {
			obj = new HorarioTurmaDisciplinaProgramadaVO();
			obj.setCodigoDisciplina(rs.getInt("codigo"));
			obj.setNomeDisciplina(rs.getString("nome"));
			obj.setChDisciplina(rs.getInt("cargahoraria"));
			obj.setCreditoDisciplina(rs.getInt("nrCreditos"));
			obj.setHoraAula(rs.getInt("horaAula"));
			obj.getSala().setSala(rs.getString("sala"));
			obj.getSala().getLocalAula().setLocal(rs.getString("local"));
			horarioTurmaDisciplinaProgramadaVOs.add(obj);
		}
		return horarioTurmaDisciplinaProgramadaVOs;
	}

		
	public StringBuilder getSqlBaseConsultarCursosQueSofreramAlteracao() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT NULL::int as tutoriaonline, NULL::date as datainicioaula_ead, null::date as dataterminoaula_ead,  curso.codigo as curso_codigo, curso.modalidadecurso, curso.nome as curso_nome, ")
		
		
		.append("case when curso.niveleducacional = 'IN' then 1  ") 
		.append("when curso.niveleducacional = 'BA' then 2  ") 
		.append("when curso.niveleducacional = 'ME' then 3  ") 
		.append("when curso.niveleducacional = 'SE' then 4  ") 
		.append("when curso.niveleducacional = 'GT' then 5  ") 
		.append("when curso.niveleducacional = 'SU' then 6  ") 
		.append("when curso.niveleducacional = 'PO' then 7  ") 
		.append("when curso.niveleducacional = 'MT' then 8  ") 
		.append("when curso.niveleducacional = 'PR' then 9  ") 
		.append("when curso.niveleducacional = 'EX' then 10 else 11 ") 
		.append("end as modalidade_codigo, ")
		
		.append("case when curso.niveleducacional = 'IN' then 'Educação Infantil'  ") 
		.append("when curso.niveleducacional = 'BA' then 'Ensino Fundamental' ")
		.append("when curso.niveleducacional = 'ME' then 'Ensino Médio' ")
		.append("when curso.niveleducacional = 'SE' then 'Sequencial'  ") 
		.append("when curso.niveleducacional = 'GT' then 'Graduação Tecnológica' ") 
		.append("when curso.niveleducacional = 'SU' then 'Graduação' ") 
		.append("when curso.niveleducacional = 'PO' then 'Pós-graduação' ") 
		.append("when curso.niveleducacional = 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' ") 
		.append("when curso.niveleducacional = 'PR' then 'Técnico/Profissionalizante' ")
		.append("when curso.niveleducacional = 'EX' then 'Cursos de Aperfeiçoamento Profissional' else 'Outros' ")
		.append("end as modalidade_nome,  ")		
		.append("turma.codigo as turma_codigo, ")
		.append("turma.identificadorturma as turma_nome, ")
		.append("disciplina.codigo as modulo_codigo, ")
		.append("disciplina.nome as modulo_nome, ")
		.append("turma.datacriacao as turma_inicio, null::date as turma_fim, ")
		.append("min(horarioturmadiaitem.data) as modulo_inicio, ")
		.append("max(horarioturmadiaitem.data) as modulo_fim, ")
		.append("turmadisciplina.modalidadeDisciplina as modalidade_disciplina, ")
		.append("turmadisciplina.permiteApoioPresencial as tipomodalidade_disciplina, ")
		.append("coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ")
		.append("horarioturma.anovigente as modulo_ano, ")
		.append("horarioturma.semestrevigente as modulo_semestre ")
		.append("FROM turma ")
		.append(" INNER JOIN turmadisciplina 	 ON turmadisciplina.turma	 	 = turma.codigo ")
		.append(" INNER JOIN disciplina 		 ON disciplina.codigo 			 = turmadisciplina.disciplina ")
		.append(" INNER JOIN curso 				 ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo IN (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo))) ")
		.append(" INNER JOIN horarioturma 		 ON horarioturma.turma 			 = turma.codigo ")
		.append(" INNER JOIN horarioturmadia 	 ON horarioturmadia.horarioturma = horarioturma.codigo ")
		.append(" INNER JOIN horarioturmadiaitem ON horarioturmadia.codigo 		 = horarioturmadiaitem.horarioturmadia and horarioturmadiaitem.disciplina = turmadisciplina.disciplina ")
		.append(" INNER JOIN pessoa as professor ON professor.codigo 			 = horarioturmadiaitem.professor  ")
		.append(" LEFT JOIN periodoletivo 		 ON periodoletivo.codigo 	 	 = turma.periodoletivo  ");
		return sb;
	}
	
	public StringBuilder getSqlBaseConsultarCursosEADQueSofreramAlteracao() {
		StringBuilder sb = new StringBuilder();
		sb.append("select curso.codigo as curso_codigo, curso.modalidadecurso, curso.nome as curso_nome, ")
		
		.append("case when curso.niveleducacional = 'IN' then 1  ") 
		.append("when curso.niveleducacional = 'BA' then 2  ") 
		.append("when curso.niveleducacional = 'ME' then 3  ") 
		.append("when curso.niveleducacional = 'SE' then 4  ") 
		.append("when curso.niveleducacional = 'GT' then 5  ") 
		.append("when curso.niveleducacional = 'SU' then 6  ") 
		.append("when curso.niveleducacional = 'PO' then 7  ") 
		.append("when curso.niveleducacional = 'MT' then 8  ") 
		.append("when curso.niveleducacional = 'PR' then 9  ") 
		.append("when curso.niveleducacional = 'EX' then 10 else 11 ") 
		.append("end as modalidade_codigo, ")
		
		.append("case when curso.niveleducacional = 'IN' then 'Educação Infantil'  ") 
		.append("when curso.niveleducacional = 'BA' then 'Ensino Fundamental' ")
		.append("when curso.niveleducacional = 'ME' then 'Ensino Médio' ")
		.append("when curso.niveleducacional = 'SE' then 'Sequencial'  ") 
		.append("when curso.niveleducacional = 'GT' then 'Graduação Tecnológica' ") 
		.append("when curso.niveleducacional = 'SU' then 'Graduação' ") 
		.append("when curso.niveleducacional = 'PO' then 'Pós-graduação' ") 
		.append("when curso.niveleducacional = 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' ") 
		.append("when curso.niveleducacional = 'PR' then 'Técnico/Profissionalizante' ")
		.append("when curso.niveleducacional = 'EX' then 'Cursos de Aperfeiçoamento Profissional' else 'Outros' ")
		.append("end as modalidade_nome,  ")
		.append("turma.codigo as turma_codigo, ")
		.append("turma.identificadorturma as turma_nome, ")
		.append("disciplina.codigo as modulo_codigo, ")
		.append("disciplina.nome as modulo_nome, ")
		.append("turma.datacriacao as turma_inicio, null::date as turma_fim, ")
		.append("min(horarioturmadiaitem.data) as modulo_inicio, ")
		.append("max(horarioturmadiaitem.data) as modulo_fim, ")
		.append("turmadisciplina.modalidadeDisciplina as modalidade_disciplina, ")
		.append("turmadisciplina.permiteApoioPresencial as tipomodalidade_disciplina ")
		.append("from turma ")
		.append("inner join turmadisciplina on turmadisciplina.turma = turma.codigo ")
		.append("inner join curso on curso.codigo = turma.curso ")
		.append("left join horarioturma on horarioturma.turma = turma.codigo ")
		.append("left join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ")
		.append("left join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia and horarioturmadiaitem.disciplina = turmadisciplina.disciplina ")
		.append("inner join disciplina on disciplina.codigo = turmadisciplina.disciplina ")
		.append("left join pessoa as professor on professor.codigo = horarioturmadiaitem.professor  ");
		return sb;
	}
	/**
	 * Realiza a consulta dos cursos que tiveram alteracao em seus registros nas ultimas horas @param
	 * @param horas
	 * @return
	 */
		
	@Override
	public SqlRowSet consultarCursosQueSofreramAlteracao(Integer horas) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append(getSqlBaseConsultarCursosQueSofreramAlteracao());		
		sb.append(" WHERE horarioturmadiaitem.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)  ");
		sb.append("GROUP BY tutoriaonline , datainicioaula_ead , dataterminoaula_ead, curso.codigo , curso.nome,  curso.niveleducacional, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, turmadisciplina.modalidadeDisciplina, turmadisciplina.permiteApoioPresencial, ");
		sb.append("periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente ");
		
		sb.append(" UNION ");
		
		sb.append(getSqlBaseConsultarCursosQueSofreramAlteracao());		
		sb.append(" WHERE curso.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)  ");
		sb.append(" and	horarioturmadiaitem.dataultimaalteracao >= (now() - '6 months'::interval) ");
		sb.append("GROUP BY tutoriaonline , datainicioaula_ead , dataterminoaula_ead, curso.codigo , curso.nome,  curso.niveleducacional, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, turmadisciplina.modalidadeDisciplina, turmadisciplina.permiteApoioPresencial, ");
		sb.append("periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente ");
		
		sb.append(" UNION ");
		
		sb.append(getSqlBaseConsultarCursosQueSofreramAlteracao());		
		sb.append(" WHERE turma.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)  ");
		sb.append(" and	horarioturmadiaitem.dataultimaalteracao >= (now() - '6 months'::interval) ");
		sb.append("GROUP BY tutoriaonline , datainicioaula_ead , dataterminoaula_ead, curso.codigo , curso.nome,  curso.niveleducacional, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, turmadisciplina.modalidadeDisciplina, turmadisciplina.permiteApoioPresencial, ");
		sb.append("periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente ");
		
		sb.append(" UNION ");
		
		sb.append("SELECT DISTINCT programacaotutoriaonline.codigo as tutoriaonline, programacaotutoriaonline.datainicioaula as datainicioaula_ead, programacaotutoriaonline.dataterminoaula as dataterminoaula_ead,  curso.codigo as curso_codigo, curso.modalidadecurso, curso.nome as curso_nome, ");
		sb.append("case when curso.niveleducacional = 'IN' then 1  ") ;
		sb.append("when curso.niveleducacional = 'BA' then 2  ") ;
		sb.append("when curso.niveleducacional = 'ME' then 3  "); 
		sb.append("when curso.niveleducacional = 'SE' then 4  "); 
		sb.append("when curso.niveleducacional = 'GT' then 5  "); 
		sb.append("when curso.niveleducacional = 'SU' then 6  "); 
		sb.append("when curso.niveleducacional = 'PO' then 7  "); 
		sb.append("when curso.niveleducacional = 'MT' then 8  ") ;
		sb.append("when curso.niveleducacional = 'PR' then 9  "); 
		sb.append("when curso.niveleducacional = 'EX' then 10 else 11 ") ;
		sb.append("end as modalidade_codigo, ");
		
		sb.append("case when curso.niveleducacional = 'IN' then 'Educação Infantil'  ") ;
		sb.append("when curso.niveleducacional = 'BA' then 'Ensino Fundamental' ");
		sb.append("when curso.niveleducacional = 'ME' then 'Ensino Médio' ");
		sb.append("when curso.niveleducacional = 'SE' then 'Sequencial'  ") ;
		sb.append("when curso.niveleducacional = 'GT' then 'Graduação Tecnológica' ") ;
		sb.append("when curso.niveleducacional = 'SU' then 'Graduação' ") ;
		sb.append("when curso.niveleducacional = 'PO' then 'Pós-graduação' ") ;
		sb.append("when curso.niveleducacional = 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' ");
		sb.append("when curso.niveleducacional = 'PR' then 'Técnico/Profissionalizante' ");
		sb.append("when curso.niveleducacional = 'EX' then 'Cursos de Aperfeiçoamento Profissional' else 'Outros' ");
		sb.append("end as modalidade_nome,  ")	;	
		sb.append("turma.codigo as turma_codigo, ");
		sb.append("turma.identificadorturma as turma_nome, ");
		sb.append("disciplina.codigo as modulo_codigo, ");
		sb.append("disciplina.nome as modulo_nome, ");
		sb.append("turma.datacriacao as turma_inicio, null::date as turma_fim, ");
		sb.append("null::date as modulo_inicio, ");
		sb.append("null::date as modulo_fim, ");
		sb.append("turmadisciplina.modalidadeDisciplina as modalidade_disciplina, ");
		sb.append("turmadisciplina.permiteApoioPresencial as tipomodalidade_disciplina, ");
		sb.append("coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ");
		sb.append("programacaotutoriaonline.ano as modulo_ano, ");
		sb.append("programacaotutoriaonline.semestre as modulo_semestre ");
		sb.append("from turma ");
		sb.append(" INNER JOIN turmadisciplina 	 	   			  ON turmadisciplina.turma 		      = turma.codigo ");
		sb.append(" INNER JOIN disciplina 		 	   			  ON disciplina.codigo 			      = turmadisciplina.disciplina ");
		sb.append(" INNER JOIN curso 				 	   		  ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo IN (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo))) ");
		sb.append(" INNER JOIN programacaotutoriaonline           ON programacaotutoriaonline.turma   = turma.codigo AND programacaotutoriaonline.disciplina = disciplina.codigo ");
		sb.append(" INNER JOIN programacaotutoriaonlineprofessor  ON programacaotutoriaonline.codigo  = programacaotutoriaonlineprofessor.programacaotutoriaonline");
		sb.append(" INNER JOIN pessoa as professor 	    		  ON professor.codigo 				  = programacaotutoriaonlineprofessor.professor  ");
		sb.append(" LEFT JOIN periodoletivo 		 			  ON periodoletivo.codigo 	 	 	  = turma.periodoletivo  ");
		sb.append(" WHERE (  ");
		sb.append("  (turma.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)) OR ");
		sb.append("  ((curso.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval))) )");
		sb.append("GROUP BY tutoriaonline , datainicioaula_ead , dataterminoaula_ead, curso.codigo , curso.nome,  curso.niveleducacional, turma.codigo, turma.identificadorturma, disciplina.codigo, disciplina.nome, turmadisciplina.modalidadeDisciplina, turmadisciplina.permiteApoioPresencial, ");
		sb.append("periodoletivo.periodoletivo, programacaotutoriaonline.ano, programacaotutoriaonline.semestre ");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}
	
	public void alterarCursosSetandoDataAlteracaoInicial() {
		boolean repetir = true;
		while (repetir) {
			try {
				repetir = false;
				StringBuilder sb = new StringBuilder();
				sb.append("select curso.codigo from curso ")
				.append(" where curso.dataultimaalteracao is null ")
				.append(" order by curso.codigo desc limit 10 ");
				
				String codigosAlterar = "";
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
				while (tabelaResultado.next()) {
					repetir = true;
					codigosAlterar += tabelaResultado.getInt("codigo") + ", ";
				}
				if (repetir) {
					alterarCursosSetandoDataAlteracaoInicial(codigosAlterar);
					Thread.sleep(300000);
				}
			} catch (Exception e) {
				repetir = false;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarCursosSetandoDataAlteracaoInicial(String codigosAlterar) {
		String sql = "UPDATE curso set dataultimaalteracao=now() where codigo in (" + codigosAlterar + " 0);";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				return sqlAlterar;

			}
		});
	}
	
	public void alterarMatriculasSetandoDataAlteracaoInicial() {
		boolean repetir = true;
		while (repetir) {
			try {
				repetir = false;
				StringBuilder sb = new StringBuilder();
				sb.append("select matriculaperiodoturmadisciplina.codigo from matriculaperiodoturmadisciplina ")
				.append(" where matriculaperiodoturmadisciplina.dataultimaalteracao is null ")
				.append(" order by matriculaperiodoturmadisciplina.codigo desc limit 2000 ");
				
				String codigosAlterar = "";
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
				while (tabelaResultado.next()) {
					repetir = true;
					codigosAlterar += tabelaResultado.getInt("codigo") + ", ";
				}
				if (repetir) {
					alterarMatriculasSetandoDataAlteracaoInicial(codigosAlterar);
					Thread.sleep(300000);
				}
			} catch (Exception e) {
				repetir = false;
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarMatriculasSetandoDataAlteracaoInicial(String codigosAlterar) {
		String sql = "UPDATE matriculaperiodoturmadisciplina set dataultimaalteracao=now() where codigo in (" + codigosAlterar + " 0);";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				return sqlAlterar;
				
			}
		});
	}
	
	public void alterarProfessoresSetandoDataAlteracaoInicial() {
		boolean repetir = true;
		while (repetir) {
			try {
				repetir = false;
				StringBuilder sb = new StringBuilder();
				sb.append("select horarioturmadiaitem.codigo from horarioturmadiaitem ")
				.append(" where horarioturmadiaitem.dataultimaalteracao is null ")
				.append(" order by horarioturmadiaitem.codigo desc limit 2000 ");
				
				String codigosAlterar = "";
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
				while (tabelaResultado.next()) {
					repetir = true;
					codigosAlterar += tabelaResultado.getInt("codigo") + ", ";
				}
				if (repetir) {
					alterarProfessoresSetandoDataAlteracaoInicial(codigosAlterar);
					Thread.sleep(300000);
				}
			} catch (Exception e) {
				repetir = false;
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarProfessoresSetandoDataAlteracaoInicial(String codigosAlterar) {
		String sql = "UPDATE horarioturmadiaitem set dataultimaalteracao=now() where codigo in (" + codigosAlterar + " 0);";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				return sqlAlterar;
				
			}
		});
	}

	
	/**
	 * Consulta as matriculas dos alunos as quais algum dado foi alterado no intervalo de horas informado
	 * @param horas
	 * @return
	 */
	public StringBuilder getSqlBaseConsultarVinculacaoAlunoAlterado(String joinMatriculaPeriodoTurmaDisciplina) {
		StringBuilder sb = new StringBuilder();
//		sb.append("select aluno.cpf, curso.codigo as curso_codigo, ") 
		sb.append("select aluno.cpf, cursorep.codigo as curso_codigo, matricula.curso as curso_codigoorigem, ") 
		.append("case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 2 else " + 
				"case when matricula.situacao = 'AC' then 1 else " + 
				"case when matricula.situacao = 'PR' then 2 else " + 
				"case when matricula.situacao = 'DE' then 3 else " + 
				"case when matricula.situacao = 'IN' then 4 else " + 
				"case when matricula.situacao = 'AT' then 5 else " + 
				"case when matricula.situacao = 'CA' then 6 else " + 
				"case when matricula.situacao = 'CF' then 7 else " + 
				"case when matricula.situacao = 'JU' then 8 else " + 
				"case when matricula.situacao = 'TS' then 9 else " + 
				"case when matricula.situacao = 'TR' then 10 else " + 
				"case when matricula.situacao = 'FI' then 11 else " + 
				"case when matricula.situacao = 'PF' then 12 else " + 
				"case when matricula.situacao = 'FO' then 13 else " + 
				"case when matricula.situacao = 'TI' then 14 else " + 
				"case when matricula.situacao = 'ER' then 15 else 0 end end end end end end end end end end end end end end end end as codigoSituacao," + 
				"case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else " +
				"case when matricula.situacao = 'AC' then 'Abandono de Curso' else " + 
				"case when matricula.situacao = 'PR' then 'Pré-matrícula' else " + 
				"case when matricula.situacao = 'DE' then 'Desligado' else " + 
				"case when matricula.situacao = 'IN' then 'Inativa' else " + 
				"case when matricula.situacao = 'AT' then 'Ativa' else " + 
				"case when matricula.situacao = 'CA' then 'Cancelada' else " + 
				"case when matricula.situacao = 'CF' then 'Cancelada Financeiro' else " + 
				"case when matricula.situacao = 'JU' then 'Jubilado' else " + 
				"case when matricula.situacao = 'TS' then 'Transferida' else " + 
				"case when matricula.situacao = 'TR' then 'Trancada' else " + 
				"case when matricula.situacao = 'FI' then 'Finalizada' else " + 
				"case when matricula.situacao = 'PF' then 'Provável Formando' else " + 
				"case when matricula.situacao = 'FO' then 'Formado' else " + 
				"case when matricula.situacao = 'TI' then 'Transferida Internamente' else " + 
				"case when matricula.situacao = 'ER' then 'Problema ao Tentar Definir Situação - Problema Importação' else '' " + 
				"end end end end end end end end end end end end end end end end as situacaoMatricula, ") 
		.append("turma.codigo as turma_codigo, ") 
		.append("disciplina.codigo as modulo_codigo, ") 
		//.append("min(horarioturmadiaitem.data) as vinculo_inicio, ") 
		.append("matricula.data as vinculo_inicio, ") 
		.append("case when negociacaorecebimento.data is not null then negociacaorecebimento.data else matricula.data end data_confirmacao, ")
		.append("case when matricula.situacao = 'AT' then null else case when matricula.situacao <> 'AT' then current_date else max(horarioturmadiaitem.data) end end as vinculo_fim, case when matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma then 1 else 0 end as codigo_reposicao, ") 
		.append("coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ")
		.append("horarioturma.anovigente as vinculo_ano, ")
		.append("horarioturma.semestrevigente as vinculo_semestre ")
		.append("from horarioturma ") 
		.append("inner join turma on turma.codigo = horarioturma.turma ") 
		.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ") 
		.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ") 
		.append("inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ")  
		.append("left join turmaagrupada ta on ta.turmaorigem = turma.codigo ")  
		.append("left join turma t on ta.turma  = t.codigo ")  
		.append(joinMatriculaPeriodoTurmaDisciplina)
		.append(" AND matriculaperiodoturmadisciplina.ano = horarioturma.anovigente and matriculaperiodoturmadisciplina.semestre = horarioturma.semestrevigente  ") 
		.append(" AND (( matriculaperiodoturmadisciplina.turma  = turma.codigo) OR (matriculaperiodoturmadisciplina.turma = t.codigo)) ") 
		.append("inner join turma as turmarep on matriculaperiodoturmadisciplina.turma = turmarep.codigo ") 
		.append("inner join curso as cursorep on cursorep.codigo = turmarep.curso ")
		.append("inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ")
		.append(" INNER JOIN curso ON (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = t.curso) ")
		.append("inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ") 
		.append("inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ") 
		.append("inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ") 
		.append("inner join pessoa as aluno on matricula.aluno = aluno.codigo ") 
		.append("left join contareceber on contareceber.matriculaaluno = matricula.matricula and contareceber.tipoorigem = 'MAT'  ") 
		.append("and contareceber.matriculaperiodo = matriculaperiodoturmadisciplina.matriculaperiodo and contareceber.situacao = 'RE' ") 
		.append("left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ") 
		.append("left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ")
		.append("left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		return sb;

	}

	public StringBuilder getSqlBaseConsultarVinculacaoAlunoAlteradoPartindoMatricula() {
		StringBuilder sb = new StringBuilder();
//		sb.append("select aluno.cpf, curso.codigo as curso_codigo, ") 
		sb.append("select aluno.cpf, case when turma.turmaagrupada then matricula.curso else curso.codigo end as curso_codigo, matricula.curso as curso_codigoorigem, ") 
		.append("case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 2 else " +
				"case when matricula.situacao = 'AC' then 1 else " + 
				"case when matricula.situacao = 'PR' then 2 else " + 
				"case when matricula.situacao = 'DE' then 3 else " + 
				"case when matricula.situacao = 'IN' then 4 else " + 
				"case when matricula.situacao = 'AT' then 5 else " + 
				"case when matricula.situacao = 'CA' then 6 else " + 
				"case when matricula.situacao = 'CF' then 7 else " + 
				"case when matricula.situacao = 'JU' then 8 else " + 
				"case when matricula.situacao = 'TS' then 9 else " + 
				"case when matricula.situacao = 'TR' then 10 else " + 
				"case when matricula.situacao = 'FI' then 11 else " + 
				"case when matricula.situacao = 'PF' then 12 else " + 
				"case when matricula.situacao = 'FO' then 13 else " + 
				"case when matricula.situacao = 'TI' then 14 else " + 
				"case when matricula.situacao = 'ER' then 15 else 0 end end end end end end end end end end end end end end end end as codigoSituacao," + 
				"case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else " +
				"case when matricula.situacao = 'AC' then 'Abandono de Curso' else " + 
				"case when matricula.situacao = 'PR' then 'Pré-matrícula' else " + 
				"case when matricula.situacao = 'DE' then 'Desligado' else " + 
				"case when matricula.situacao = 'IN' then 'Inativa' else " + 
				"case when matricula.situacao = 'AT' then 'Ativa' else " + 
				"case when matricula.situacao = 'CA' then 'Cancelada' else " + 
				"case when matricula.situacao = 'CF' then 'Cancelada Financeiro' else " + 
				"case when matricula.situacao = 'JU' then 'Jubilado' else " + 
				"case when matricula.situacao = 'TS' then 'Transferida' else " + 
				"case when matricula.situacao = 'TR' then 'Trancada' else " + 
				"case when matricula.situacao = 'FI' then 'Finalizada' else " + 
				"case when matricula.situacao = 'PF' then 'Provável Formando' else " + 
				"case when matricula.situacao = 'FO' then 'Formado' else " + 
				"case when matricula.situacao = 'TI' then 'Transferida Internamente' else " + 
				"case when matricula.situacao = 'ER' then 'Problema ao Tentar Definir Situação - Problema Importação' else '' " + 
				"end end end end end end end end end end end end end end end end as situacaoMatricula, ") 
		.append("turma.codigo as turma_codigo, ") 
		.append("horarioturmadiaitem.disciplina as modulo_codigo, ") 
		//.append("min(horarioturmadiaitem.data) as vinculo_inicio, ") 
		.append("matricula.data as vinculo_inicio, ") 
		.append("case when negociacaorecebimento.data is not null then negociacaorecebimento.data else matricula.data end data_confirmacao, ")
		.append("case when matricula.situacao = 'AT' then null else case when matricula.situacao <> 'AT' then current_date else max(horarioturmadiaitem.data) end end as vinculo_fim, case when matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma then 1 else 0 end as codigo_reposicao, ") 
		.append("coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ")
		.append("horarioturma.anovigente as vinculo_ano, ")
		.append("horarioturma.semestrevigente as vinculo_semestre ")
		.append(" FROM matricula ")
		.append("  INNER JOIN matriculaperiodo 					ON	matriculaperiodo.matricula = matricula.matricula ")
		.append("  INNER JOIN matriculaperiodoturmadisciplina 	ON 	matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ")
		.append("  INNER JOIN historico 						ON  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ")
		.append("  INNER JOIN disciplina 						ON	disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ")
		.append("  INNER JOIN horarioturma 						ON  matriculaperiodoturmadisciplina.ano = horarioturma.anovigente	and matriculaperiodoturmadisciplina.semestre = horarioturma.semestrevigente ")
		.append("   AND (( horarioturma.turma  = matriculaperiodoturmadisciplina.turma) OR ( horarioturma.turma  IN (SELECT turmaagrupada.turmaorigem FROM turmaagrupada WHERE turmaagrupada.turma = matriculaperiodoturmadisciplina.turma))) ")
		.append("  INNER JOIN turma 							ON	turma.codigo = horarioturma.turma ")
		.append("  LEFT JOIN turmaagrupada ta ON ta.turmaorigem = turma.codigo ")
		.append("  LEFT JOIN turma t ON ta.turma = t.codigo ")
		.append("  INNER JOIN curso 							ON  ((turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = t.curso) ) ")
		.append("  INNER JOIN horarioturmadia 					ON	horarioturmadia.horarioturma = horarioturma.codigo ")
		.append("  INNER JOIN horarioturmadiaitem 				ON	horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia AND (horarioturmadiaitem.disciplina  = matriculaperiodoturmadisciplina.disciplina ")
		.append(" or (turma.turmaagrupada and matriculaperiodoturmadisciplina.disciplina in ")
		.append(" (select de.disciplina from disciplinaequivalente de  where de.equivalente = horarioturmadiaitem.disciplina union select ")
		.append(" de2.equivalente from disciplinaequivalente de2 where de2.disciplina = horarioturmadiaitem.disciplina))) ")
		.append("  INNER JOIN pessoa as professor 				ON	professor.codigo = horarioturmadiaitem.professor ")
		.append("  INNER JOIN pessoa as aluno 					ON	matricula.aluno = aluno.codigo ")
		.append("  LEFT JOIN contareceber 						ON  contareceber.matriculaaluno = matricula.matricula	and contareceber.tipoorigem = 'MAT'	and contareceber.matriculaperiodo = matriculaperiodo.codigo	and contareceber.situacao = 'RE' ")
		.append("  LEFT JOIN contarecebernegociacaorecebimento  ON	contarecebernegociacaorecebimento.contareceber = contareceber.codigo ")
		.append("  LEFT JOIN negociacaorecebimento 				ON	contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ")
		.append("  LEFT JOIN periodoletivo 						ON periodoletivo.codigo = turma.periodoletivo ");
		return sb;
		
	}

	public StringBuilder getSqlBaseConsultarVinculacaoAlunoBlended() {
		StringBuilder sb = new StringBuilder();
//		sb.append("select aluno.cpf, curso.codigo as curso_codigo, ") 
		sb.append("select aluno.cpf, cursorep.codigo as curso_codigo, matricula.curso as curso_codigoorigem, ") 
		.append("case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 2 else " +
				"case when matricula.situacao = 'AC' then 1 else " + 
				"case when matricula.situacao = 'PR' then 2 else " + 
				"case when matricula.situacao = 'DE' then 3 else " + 
				"case when matricula.situacao = 'IN' then 4 else " + 
				"case when matricula.situacao = 'AT' then 5 else " + 
				"case when matricula.situacao = 'CA' then 6 else " + 
				"case when matricula.situacao = 'CF' then 7 else " + 
				"case when matricula.situacao = 'JU' then 8 else " + 
				"case when matricula.situacao = 'TS' then 9 else " + 
				"case when matricula.situacao = 'TR' then 10 else " + 
				"case when matricula.situacao = 'FI' then 11 else " + 
				"case when matricula.situacao = 'PF' then 12 else " + 
				"case when matricula.situacao = 'FO' then 13 else " + 
				"case when matricula.situacao = 'TI' then 14 else " + 
				"case when matricula.situacao = 'ER' then 15 else 0 end end end end end end end end end end end end end end end end as codigoSituacao," + 
				"case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else " +
				"case when matricula.situacao = 'AC' then 'Abandono de Curso' else " + 
				"case when matricula.situacao = 'PR' then 'Pré-matrícula' else " + 
				"case when matricula.situacao = 'DE' then 'Desligado' else " + 
				"case when matricula.situacao = 'IN' then 'Inativa' else " + 
				"case when matricula.situacao = 'AT' then 'Ativa' else " + 
				"case when matricula.situacao = 'CA' then 'Cancelada' else " + 
				"case when matricula.situacao = 'CF' then 'Cancelada Financeiro' else " + 
				"case when matricula.situacao = 'JU' then 'Jubilado' else " + 
				"case when matricula.situacao = 'TS' then 'Transferida' else " + 
				"case when matricula.situacao = 'TR' then 'Trancada' else " + 
				"case when matricula.situacao = 'FI' then 'Finalizada' else " + 
				"case when matricula.situacao = 'PF' then 'Provável Formando' else " + 
				"case when matricula.situacao = 'FO' then 'Formado' else " + 
				"case when matricula.situacao = 'TI' then 'Transferida Internamente' else " + 
				"case when matricula.situacao = 'ER' then 'Problema ao Tentar Definir Situação - Problema Importação' else '' " + 
				"end end end end end end end end end end end end end end end end as situacaoMatricula, ") 
		.append("turma.codigo as turma_codigo, ") 
		.append("programacaotutoriaonline.disciplina as modulo_codigo, ") 
		.append("matricula.data as vinculo_inicio, ") 
		.append("case when negociacaorecebimento.data is not null then negociacaorecebimento.data else matricula.data end data_confirmacao, ")
		.append("case when matricula.situacao = 'AT' then null else case when matricula.situacao <> 'AT' then current_date else current_date end end as vinculo_fim, case when matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma then 1 else 0 end as codigo_reposicao, ") 
		.append("coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ")
		.append("programacaotutoriaonline.ano as vinculo_ano, ")
		.append("programacaotutoriaonline.semestre as vinculo_semestre ")
		.append("from matriculaperiodo ") 
		.append("inner join matricula on matricula.matricula = matriculaperiodo.matricula ") 
		.append("inner join turma on turma.codigo = matriculaperiodo.turma ") 
		.append("inner join curso on curso.codigo = turma.curso ") 
		.append("inner join turmadisciplina on turmadisciplina.turma = turma.codigo  and turmadisciplina.definicoesTutoriaOnline = 'DINAMICA'") 
		.append("LEFT JOIN turmaagrupada ta ON ta.turma = turma.codigo ")
		.append("LEFT JOIN turma turmaorigem ON ta.turmaorigem = turmaorigem.codigo ")
		.append("INNER JOIN LATERAL ( ")
		.append(" 	SELECT turmadisciplina.disciplina codigo ")
		.append("	UNION SELECT de.equivalente codigo FROM disciplinaequivalente de where turmaorigem.codigo IS NOT NULL AND de.disciplina = turmadisciplina.disciplina ")
		.append("	UNION SELECT de.disciplina codigo FROM disciplinaequivalente de where turmaorigem.codigo IS NOT NULL AND de.equivalente = turmadisciplina.disciplina ")
		.append(") disciplinas ON 1 = 1 ")
		.append("inner join programacaotutoriaonline on (programacaotutoriaonline.turma = turma.codigo or programacaotutoriaonline.turma = turmaorigem.codigo) ") 
		.append("and programacaotutoriaonline.disciplina = disciplinas.codigo and programacaotutoriaonline.definirPeriodoAulaOnline  ") 
		
		.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.turma = turma.codigo  ") 
		.append("and matriculaperiodoturmadisciplina.disciplina = turmadisciplina.disciplina and matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ") 
		.append("inner join turma as turmarep on matriculaperiodoturmadisciplina.turma = turmarep.codigo ") 
		.append("inner join curso as cursorep on cursorep.codigo = turmarep.curso ") 
		.append("inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ") 
		.append("inner join pessoa as professor on professor.codigo = programacaotutoriaonlineprofessor.professor ") 
		.append("inner join pessoa as aluno on matricula.aluno = aluno.codigo ") 
		.append("inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ") 
		.append("left join contareceber on contareceber.matriculaaluno = matricula.matricula and contareceber.tipoorigem = 'MAT'  ") 
		.append("and contareceber.matriculaperiodo = matriculaperiodoturmadisciplina.matriculaperiodo and contareceber.situacao = 'RE' ") 
		.append("left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ") 
		.append("left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ")
		.append("left join periodoletivo on periodoletivo.codigo  = turma.periodoletivo ");
		return sb;
	}
	
	@Override
	public SqlRowSet consultarVinculacaoAlunoAlterado(Integer horas) throws Exception {
		StringBuilder sql  =  new StringBuilder("");		
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlterado(" INNER JOIN matriculaperiodoturmadisciplina ON  matriculaperiodoturmadisciplina.disciplina = disciplina.codigo "));
		sql.append(" where (horarioturmadiaitem.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, disciplina.codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");		
		sql.append(" UNION ");
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlterado(" INNER JOIN LATERAL (SELECT CASE WHEN turma.turmaagrupada THEN disciplinaequivalente.equivalente ELSE 0 END codigo FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = disciplina.codigo ) AS disciplinas_equivalente1 ON 1 = 1 INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.disciplina = disciplinas_equivalente1.codigo "));
		sql.append(" where (horarioturmadiaitem.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, disciplina.codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");		
		sql.append(" UNION ");
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlterado(" INNER JOIN LATERAL (SELECT CASE WHEN turma.turmaagrupada THEN disciplinaequivalente.disciplina ELSE 0 END codigo FROM disciplinaequivalente WHERE disciplinaequivalente.equivalente = disciplina.codigo) AS disciplinas_equivalente2 ON 1 = 1 INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.disciplina = disciplinas_equivalente2.codigo "));
		sql.append(" where (horarioturmadiaitem.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, disciplina.codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");		
		sql.append(" UNION ");
		
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlterado(" INNER JOIN matriculaperiodoturmadisciplina ON  matriculaperiodoturmadisciplina.disciplina = disciplina.codigo "));
		sql.append(" where matriculaperiodoturmadisciplina.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, disciplina.codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");
		sql.append(" UNION ");
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlterado(" INNER JOIN LATERAL (SELECT CASE WHEN turma.turmaagrupada THEN disciplinaequivalente.equivalente ELSE 0 END codigo FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = disciplina.codigo ) AS disciplinas_equivalente1 ON 1 = 1 INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.disciplina = disciplinas_equivalente1.codigo "));
		sql.append(" where matriculaperiodoturmadisciplina.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, disciplina.codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");
		sql.append(" UNION ");
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlterado(" INNER JOIN LATERAL (SELECT CASE WHEN turma.turmaagrupada THEN disciplinaequivalente.disciplina ELSE 0 END codigo FROM disciplinaequivalente WHERE disciplinaequivalente.equivalente = disciplina.codigo) AS disciplinas_equivalente2 ON 1 = 1 INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.disciplina = disciplinas_equivalente2.codigo "));
		sql.append(" where matriculaperiodoturmadisciplina.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, disciplina.codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");
		sql.append(" UNION ");
		
		sql.append(getSqlBaseConsultarVinculacaoAlunoAlteradoPartindoMatricula());
		sql.append(" where matricula.updated >= (now() - '").append(horas).append(" hour'::interval) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, curso_codigo, matricula.curso, turma.codigo, horarioturmadiaitem.disciplina, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente, matriculaperiodo.situacaomatriculaperiodo ");
		// pega alunos blended
		sql.append(" UNION ");
		sql.append(getSqlBaseConsultarVinculacaoAlunoBlended());
		sql.append(" where matricula.updated >= (now() - '").append(horas).append(" hour'::interval) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sql.append(" and curso.modalidadecurso = 'HIBRIDO' and case when curso.niveleducacional = 'SU' then matriculaperiodo.situacaomatriculaperiodo = 'AT' else 1=1 end ");		
		sql.append(" group by negociacaorecebimento.data, matricula.data, matricula.situacao, aluno.cpf, cursorep.codigo , matricula.curso, turma.codigo, programacaotutoriaonline.disciplina, matriculaperiodoturmadisciplina.turma, matriculaperiodo.turma, periodoletivo.periodoletivo, programacaotutoriaonline.ano, programacaotutoriaonline.semestre, matriculaperiodo.situacaomatriculaperiodo ");
		sql.append(" UNION ");
		
		sql.append(" select aluno.cpf, curso.codigo as curso_codigo, matricula.curso as curso_codigoorigem, case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 2 else case when matricula.situacao = 'AC' then 1 else case when matricula.situacao = 'PR' then 2 else case when matricula.situacao = 'DE' then 3 else case when matricula.situacao = 'IN' then 4 else case when matricula.situacao = 'AT' then 5 else case when matricula.situacao = 'CA' then 6 else case when matricula.situacao = 'CF' then 7 else case when matricula.situacao = 'JU' then 8 else case when matricula.situacao = 'TS' then 9 else case when matricula.situacao = 'TR' then 10 else case when matricula.situacao = 'FI' then 11 else case when matricula.situacao = 'PF' then 12 else case when matricula.situacao = 'FO' then 13 else case when matricula.situacao = 'TI' then 14 else case when matricula.situacao = 'ER' then 15 else 0 end end end end end end end end end end end end end end end end as codigoSituacao, ");		
		sql.append(" case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else case when matricula.situacao = 'AC' then 'Abandono de Curso' else case when matricula.situacao = 'PR' then 'Pré-matrícula' else case when matricula.situacao = 'DE' then 'Desligado' else case when matricula.situacao = 'IN' then 'Inativa' else case when matricula.situacao = 'AT' then 'Ativa' else case when matricula.situacao = 'CA' then 'Cancelada' else case when matricula.situacao = 'CF' then 'Cancelada Financeiro' else case when matricula.situacao = 'JU' then 'Jubilado' else case when matricula.situacao = 'TS' then 'Transferida' else case when matricula.situacao = 'TR' then 'Trancada' else case when matricula.situacao = 'FI' then 'Finalizada' else case when matricula.situacao = 'PF' then 'Provável Formando' else case when matricula.situacao = 'FO' then 'Formado' else case when matricula.situacao = 'TI' then 'Transferida Internamente' else case when matricula.situacao = 'ER' then 'Problema ao Tentar Definir Situação - Problema Importação' else '' end end end end end end end end end end end end end end end end as situacaoMatricula, turma.codigo as turma_codigo, disciplina.codigo as modulo_codigo, datainicioaula as vinculo_inicio,  ");		
		sql.append(" case when negociacaorecebimento.data is not null then negociacaorecebimento.data else matricula.data end data_confirmacao, case when matricula.situacao = 'AT' then null else case when matricula.situacao <> 'AT' then current_date else dataterminoaula end end as vinculo_fim, case  when matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma then 1 else 0 end as codigo_reposicao,  ");		
		sql.append(" coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ");
		sql.append(" matriculaperiodoturmadisciplina.ano as vinculo_ano, ");
		sql.append(" matriculaperiodoturmadisciplina.semestre as vinculo_semestre ");
		sql.append(" FROM matriculaperiodoturmadisciplina  ");	
		sql.append(" INNER JOIN matriculaperiodo 			ON matriculaperiodo.codigo  = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sql.append(" INNER JOIN matricula 					ON matricula.matricula 		= matriculaperiodo.matricula ");		
		sql.append(" INNER JOIN pessoa as aluno 			ON aluno.codigo 			= matricula.aluno   ");	
		sql.append(" INNER JOIN LATERAL (   ");
		sql.append(" SELECT  turmanormal.*    ");
		sql.append(" FROM turma as turmanormal   ");
		sql.append("  WHERE (turmanormal.turmaagrupada = false and turmanormal.codigo = matriculaperiodoturmadisciplina.turma)     ");
		sql.append(" UNION ALL   ");
		sql.append(" SELECT  agrupada.*   ");
		sql.append(" FROM turma AS agrupada   ");
		sql.append("  WHERE (agrupada.turmaagrupada  AND agrupada.codigo  IN (SELECT turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodoturmadisciplina.turma))  ");
		sql.append(" ) AS turma ON 1=1    ");
		sql.append(" LEFT JOIN turmaagrupada ta ON ta.turmaorigem = turma.codigo   ");
		sql.append(" LEFT JOIN turma t ON ta.turma = t.codigo AND t.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" INNER JOIN curso ON ((turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = t.curso))   ");
		sql.append(" INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" INNER JOIN programacaotutoriaonline on turma.codigo = programacaotutoriaonline.turma ");		
		sql.append(" AND programacaotutoriaonline.disciplina IN (select disciplina.codigo UNION select de.equivalente from disciplinaequivalente de where de.disciplina = disciplina.codigo UNION select de.disciplina from disciplinaequivalente de where de.equivalente = disciplina.codigo ) ");		
		sql.append(" left join contareceber on contareceber.matriculaaluno = matricula.matricula and contareceber.tipoorigem = 'MAT'  and contareceber.matriculaperiodo = matriculaperiodoturmadisciplina.matriculaperiodo and contareceber.situacao = 'RE'  ");		
		sql.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo   ");		
		sql.append(" left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sql.append(" where (matriculaperiodoturmadisciplina.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ) ");
		sql.append(" and (curso.modalidadecurso = 'ON_LINE' or matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE') ");
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		// alunos com disciplinas excluidas para perda de acesso ao moodle.
		
		sql.append(" UNION ");
		sql.append(" select aluno.cpf, curso.codigo as curso_codigo, matricula.curso as curso_codigoorigem, case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 2 else case when matricula.situacao = 'AC' then 1 else case when matricula.situacao = 'PR' then 2 else case when matricula.situacao = 'DE' then 3 else case when matricula.situacao = 'IN' then 4 else case when matricula.situacao = 'AT' then 5 else case when matricula.situacao = 'CA' then 6 else case when matricula.situacao = 'CF' then 7 else case when matricula.situacao = 'JU' then 8 else case when matricula.situacao = 'TS' then 9 else case when matricula.situacao = 'TR' then 10 else case when matricula.situacao = 'FI' then 11 else case when matricula.situacao = 'PF' then 12 else case when matricula.situacao = 'FO' then 13 else case when matricula.situacao = 'TI' then 14 else case when matricula.situacao = 'ER' then 15 else 0 end end end end end end end end end end end end end end end end as codigoSituacao, ");		
		sql.append(" case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else case when matricula.situacao = 'AC' then 'Abandono de Curso' else case when matricula.situacao = 'PR' then 'Pré-matrícula' else case when matricula.situacao = 'DE' then 'Desligado' else case when matricula.situacao = 'IN' then 'Inativa' else case when matricula.situacao = 'AT' then 'Ativa' else case when matricula.situacao = 'CA' then 'Cancelada' else case when matricula.situacao = 'CF' then 'Cancelada Financeiro' else case when matricula.situacao = 'JU' then 'Jubilado' else case when matricula.situacao = 'TS' then 'Transferida' else case when matricula.situacao = 'TR' then 'Trancada' else case when matricula.situacao = 'FI' then 'Finalizada' else case when matricula.situacao = 'PF' then 'Provável Formando' else case when matricula.situacao = 'FO' then 'Formado' else case when matricula.situacao = 'TI' then 'Transferida Internamente' else case when matricula.situacao = 'ER' then 'Problema ao Tentar Definir Situação - Problema Importação' else '' end end end end end end end end end end end end end end end end as situacaoMatricula, turma.codigo as turma_codigo, disciplina.codigo as modulo_codigo, datainicioaula as vinculo_inicio,  ");		
		sql.append(" case when negociacaorecebimento.data is not null then negociacaorecebimento.data else matricula.data end data_confirmacao, case when matricula.situacao = 'AT' then null else case when matricula.situacao <> 'AT' then current_date else dataterminoaula end end as vinculo_fim, case  when matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma then 1 else 0 end as codigo_reposicao,  ");		
		sql.append(" coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ");
		sql.append(" matriculaperiodoturmadisciplina.ano as vinculo_ano, ");
		sql.append(" matriculaperiodoturmadisciplina.semestre as vinculo_semestre ");
		sql.append(" FROM matriculaperiodoturmadisciplina  ");	
		sql.append(" INNER JOIN matriculaperiodo 			ON matriculaperiodo.codigo  = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sql.append(" INNER JOIN matricula 					ON matricula.matricula 		= matriculaperiodo.matricula ");		
		sql.append(" INNER JOIN pessoa as aluno 			ON aluno.codigo 			= matricula.aluno   ");	
		sql.append(" INNER JOIN LATERAL (   ");
		sql.append(" SELECT  turmanormal.*    ");
		sql.append(" FROM turma as turmanormal   ");
		sql.append("  WHERE (turmanormal.turmaagrupada = false and turmanormal.codigo = matriculaperiodoturmadisciplina.turma)     ");
		sql.append(" UNION ALL   ");
		sql.append(" SELECT  agrupada.*   ");
		sql.append(" FROM turma AS agrupada   ");
		sql.append("  WHERE (agrupada.turmaagrupada  AND agrupada.codigo  IN (SELECT turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodoturmadisciplina.turma))  ");
		sql.append(" ) AS turma ON 1=1    ");
		sql.append(" LEFT JOIN turmaagrupada ta ON ta.turmaorigem = turma.codigo   ");
		sql.append(" LEFT JOIN turma t ON ta.turma = t.codigo AND t.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" INNER JOIN curso ON ((turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = t.curso))   ");
		sql.append(" INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" INNER JOIN programacaotutoriaonline on turma.codigo = programacaotutoriaonline.turma ");		
		sql.append(" AND programacaotutoriaonline.disciplina IN (select disciplina.codigo UNION select de.equivalente from disciplinaequivalente de where de.disciplina = disciplina.codigo UNION select de.disciplina from disciplinaequivalente de where de.equivalente = disciplina.codigo ) ");		
		sql.append(" left join contareceber on contareceber.matriculaaluno = matricula.matricula and contareceber.tipoorigem = 'MAT'  and contareceber.matriculaperiodo = matriculaperiodoturmadisciplina.matriculaperiodo and contareceber.situacao = 'RE'  ");		
		sql.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo   ");		
		sql.append(" left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		sql.append(" where (matricula.updated >= (now() - '").append(horas).append(" hour'::interval ) ) ");
		sql.append(" and (curso.modalidadecurso = 'ON_LINE' or matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE') ");
		sql.append(" and case when curso.niveleducacional = 'SU' then ((matricula.situacao  = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT') or matricula.situacao in ('TR', 'AC', 'CA', 'TI', 'TS', 'JU', 'FO')) else 1=1 end ");
		
		sql.append(" UNION ");
		sql.append("  SELECT DISTINCT aluno.cpf, turmabase.curso as curso_codigo, matricula.curso as curso_codigoorigem, case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 2 else case when matricula.situacao = 'AC' then 1 else case when matricula.situacao = 'PR' then 2 else case when matricula.situacao = 'DE' then 3 else case when matricula.situacao = 'IN' then 4 else case when matricula.situacao = 'AT' then 5 else case when matricula.situacao = 'CA' then 6 else case when matricula.situacao = 'CF' then 7 else case when matricula.situacao = 'JU' then 8 else case when matricula.situacao = 'TS' then 9 else case when matricula.situacao = 'TR' then 10 else case when matricula.situacao = 'FI' then 11 else case when matricula.situacao = 'PF' then 12 else case when matricula.situacao = 'FO' then 13 else case when matricula.situacao = 'TI' then 14 else case when matricula.situacao = 'ER' then 15 else 0 end end end end end end end end end end end end end end end end as codigoSituacao,  ");		
		sql.append(" case when matriculaperiodo.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else case when matricula.situacao = 'AC' then 'Abandono de Curso' else case when matricula.situacao = 'PR' then 'Pré-matrícula' else case when matricula.situacao = 'DE' then 'Desligado' else case when matricula.situacao = 'IN' then 'Inativa' else case when matricula.situacao = 'AT' then 'Ativa' else case when matricula.situacao = 'CA' then 'Cancelada' else case when matricula.situacao = 'CF' then 'Cancelada Financeiro' else case when matricula.situacao = 'JU' then 'Jubilado' else case when matricula.situacao = 'TS' then 'Transferida' else case when matricula.situacao = 'TR' then 'Trancada' else case when matricula.situacao = 'FI' then 'Finalizada' else case when matricula.situacao = 'PF' then 'Provável Formando' else case when matricula.situacao = 'FO' then 'Formado' else case when matricula.situacao = 'TI' then 'Transferida Internamente' else case when matricula.situacao = 'ER' then 'Problema ao Tentar Definir Situação - Problema Importação' else '' end end end end end end end end end end end end end end end end as situacaoMatricula, turma.codigo as turma_codigo, horarioturmadiaitem.disciplina as modulo_codigo,  ");		
		sql.append(" now() as vinculo_inicio,   now() as data_confirmacao,  now() as vinculo_fim, case  when emptd.turma <> matriculaperiodo.turma then 1 else 0 end as codigo_reposicao,  ");		
		sql.append(" coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ");
		sql.append(" horarioturma.anovigente as vinculo_ano, ");
		sql.append(" horarioturma.semestrevigente as vinculo_semestre ");
		sql.append(" FROM exclusaomatriculaperiodoturmadisciplina  emptd ");
		sql.append(" INNER JOIN LATERAL (   ");	
		sql.append("  SELECT  turmanormal.*  ");	
		sql.append(" FROM turma as turmanormal    ");	
		sql.append("   WHERE (turmanormal.turmaagrupada = false and turmanormal.codigo = emptd.turma) ");	
		sql.append("  UNION ");	
		sql.append("  SELECT  agrupada.* ");	
		sql.append(" FROM turma AS agrupada  ");	
		sql.append(" WHERE (agrupada.turmaagrupada  AND agrupada.codigo  IN (SELECT turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turma = emptd.turma)) ");	
		sql.append(" ) AS turma ON 1=1  ");
		sql.append(" inner join lateral (  ");
		sql.append(" 	SELECT emptd.disciplina codigo ");
		sql.append(" 	UNION SELECT de.equivalente codigo FROM disciplinaequivalente de where turma.codigo IS NOT NULL AND turma.turmaagrupada AND de.disciplina = emptd.disciplina ");
		sql.append(" 	UNION SELECT de.disciplina codigo FROM disciplinaequivalente de where turma.codigo IS NOT NULL AND turma.turmaagrupada AND de.equivalente = emptd.disciplina ");
		sql.append(" ) disciplinas on 1 = 1 ");
		sql.append(" INNER JOIN horarioturma  			ON horarioturma.turma 		= turma.codigo  ");	
		sql.append(" INNER JOIN horarioturmadia 		ON horarioturma.codigo 		= horarioturmadia.horarioturma  ");	
		sql.append(" INNER JOIN horarioturmadiaitem 	ON horarioturmadia.codigo 	= horarioturmadiaitem.horarioturmadia  AND horarioturmadiaitem.disciplina = disciplinas.codigo ");	
		sql.append(" LEFT JOIN matriculaperiodoturmadisciplina mptd on mptd.turma = emptd.turma and mptd.disciplina = emptd.disciplina and mptd.matriculaperiodo = emptd.matriculaperiodo ");		
		sql.append(" INNER JOIN matriculaperiodo 		ON matriculaperiodo.codigo = emptd.matriculaperiodo ");		
		sql.append(" INNER JOIN matricula 			    ON matricula.matricula = matriculaperiodo.matricula ");		
		sql.append(" INNER JOIN pessoa as aluno  		ON aluno.codigo = matricula.aluno ");		
		sql.append(" INNER JOIN turma turmabase ON mptd.turma = turmabase.codigo ");		
		sql.append(" LEFT JOIN periodoletivo on periodoletivo.codigo = turma.periodoletivo ");		
		sql.append(" WHERE mptd.codigo is null AND emptd.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	
	/**
	 * Retorna todos os agendamentos de professores os quais algum dado foi alterado no intervalo de horas informado
	 * 
	 * @param horas
	 * @return
	 */
	@Override
	public SqlRowSet consultarVinculacaoProfessorAlterado(Integer horas) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select professor.cpf, curso.codigo as curso_codigo, ")
		.append("turma.codigo as turma_codigo, ")
		.append("disciplina.codigo as modulo_codigo, ")
		//.append("min(horarioturmadiaitem.data) as vinculo_inicio, ")
		.append("horarioturma.datacriacao as vinculo_inicio, ")
		//.append("max(horarioturmadiaitem.data) as vinculo_fim ")
		.append(" (select max(htd.data)::date + 60 from horarioturmadiaitem as htditem ") 
		.append(" inner join horarioturmadia as htd on htd.codigo = htditem.horarioturmadia   ") 
		.append(" inner join horarioturma ht on ht.codigo = htd.horarioturma and htditem.disciplina = disciplina.codigo ") 
		.append(" where ht.turma = turma.codigo)::timestamp as vinculo_fim, ")
		.append(" coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ")
		.append(" horarioturma.anovigente as vinculo_ano, ")
		.append(" horarioturma.semestrevigente as vinculo_semestre ")
		.append("from horarioturma ")
		.append("inner join turma on turma.codigo = horarioturma.turma ")
		.append(" INNER JOIN curso 			ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) )  ")
		.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ")
		.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ")
		.append("inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ")
		.append("inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ")
		.append("left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ")
		.append("where horarioturmadiaitem.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ")
		.append("group by horarioturma.datacriacao, professor.cpf, curso.codigo , turma.codigo, disciplina.codigo, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente ");
		
		sb.append(" union ");
		sb.append(" select pessoa.cpf, curso.codigo as curso_codigo, programacaotutoriaonline.turma as turma_codigo, programacaotutoriaonline.disciplina as modulo_codigo,  ");		
		sb.append(" (current_date::date - 1) as vinculo_inicio, (programacaotutoriaonline.dataterminoaula::date + 60) as vinculo_fim, ");
		sb.append(" coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ");
		sb.append(" programacaotutoriaonline.ano as vinculo_ano, ");
		sb.append(" programacaotutoriaonline.semestre as vinculo_semestre ");
		sb.append(" FROM programacaotutoriaonlineprofessor ");
		sb.append(" INNER JOIN programacaotutoriaonline on programacaotutoriaonline.codigo = programacaotutoriaonlineprofessor.programacaotutoriaonline ");		
		sb.append(" INNER JOIN pessoa on pessoa.codigo = programacaotutoriaonlineprofessor.professor ");
		sb.append(" INNER JOIN turma on turma.codigo = programacaotutoriaonline.turma ");
		sb.append(" INNER JOIN curso 			ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) )  ");		
		sb.append(" LEFT JOIN periodoletivo ON periodoletivo.codigo = turma.periodoletivo ");
		sb.append(" WHERE programacaotutoriaonline.dataalteracao >= (now() - '").append(horas).append(" hour'::interval) ");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	@Override
	public SqlRowSet consultarVinculacaoProfessorRemover(Integer horas) {
		StringBuilder sb = new StringBuilder();
		sb.append("select pessoa.cpf, curso.codigo as curso_codigo, horarioturmadiaitemlog.turma as turma_codigo, horarioturmadiaitemlog.disciplina as modulo_codigo, ")
		.append("current_date as vinculo_inicio, current_date as vinculo_fim ")
		.append("from horarioturmadiaitemlog   ")
		.append("from horarioturma ")
		.append("left join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadiaitemlog.horarioturmadia  ")
		.append("and horarioturmadiaitemlog.professor = horarioturmadiaitem.professor and horarioturmadiaitemlog.disciplina = horarioturmadiaitem.disciplina  ")
		.append("inner join pessoa on pessoa.codigo = horarioturmadiaitemlog.professor ")
		.append("inner join turma on turma.codigo = horarioturmadiaitemlog.turma ")
		.append("inner join curso on curso.codigo = turma.curso ")
		.append("where horarioturmadiaitem.codigo is null ")
		.append("and horarioturmadiaitemlog.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval) ");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	
	
	/**
	 * Retorna todas as vinculacoes de coordenadores as quais algum dado foi alterado no intervalo de horas informado
	 * @param horas
	 * @return
	 */
	public StringBuilder getSqlBaseConsultarVinculacaoCoordenador()  {
		StringBuilder sb = new StringBuilder();
		sb.append("select coordenador.cpf, curso.codigo as curso_codigo,  turma.codigo as turma_codigo, disciplina.codigo as modulo_codigo, ");
		sb.append("cursocoordenador.datacriacao as vinculo_inicio, max(horarioturmadiaitem.data) as vinculo_fim, ");
		sb.append(" coalesce(periodoletivo.periodoletivo, 0) as periodoletivo_periodoletivo, ");
		sb.append(" horarioturma.anovigente as vinculo_ano, ");
		sb.append(" horarioturma.semestrevigente as vinculo_semestre ");
		sb.append("FROM turma ");
		sb.append(" INNER JOIN curso 			ON ( (turma.turmaagrupada = false AND curso.codigo = turma.curso) OR (turma.turmaagrupada AND curso.codigo = (SELECT t.curso  FROM turmaagrupada INNER JOIN turma t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo LIMIT 1  )) ) ");
		sb.append(" INNER JOIN turmadisciplina  ON turma.codigo = turmadisciplina.turma ");
		sb.append("left join horarioturma on horarioturma.turma = turma.codigo ");
		sb.append("left join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sb.append("left join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sb.append("inner join disciplina on disciplina.codigo = turmadisciplina.disciplina ");
		sb.append("inner join cursocoordenador on cursocoordenador.unidadeensino = turma.unidadeensino ");
		sb.append("and cursocoordenador.curso = curso.codigo and case when cursocoordenador.turma is not null then cursocoordenador.turma = turma.codigo else 1=1 end ");
		sb.append("inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sb.append("inner join pessoa as coordenador on coordenador.codigo = funcionario.pessoa ");
		sb.append("left join periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		return sb;
	}
	
	@Override
	public SqlRowSet consultarVinculacaoCoordenador(Integer horas) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( ");
		sb.append(getSqlBaseConsultarVinculacaoCoordenador());		
		sb.append("where ((horarioturmadiaitem.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval))) "); 		
		sb.append("group by cursocoordenador.datacriacao, coordenador.cpf, curso.codigo , turma.codigo, disciplina.codigo, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente ");
		sb.append(" union ");
		sb.append(getSqlBaseConsultarVinculacaoCoordenador());		
		sb.append("where  (cursocoordenador.datacriacao >= (now() - '").append(horas).append(" hour'::interval)) ") ;
		sb.append("group by cursocoordenador.datacriacao, coordenador.cpf, curso.codigo , turma.codigo, disciplina.codigo, periodoletivo.periodoletivo, horarioturma.anovigente, horarioturma.semestrevigente ");
		sb.append(" ) as t ");
		sb.append("where t.vinculo_fim > current_date or t.vinculo_fim is null  "); 
		
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}	
	
	private void montarDadosIsLancadoRegistro(HorarioTurmaVO horarioTurmaVO) throws Exception {
		horarioTurmaVO.getHorarioTurmaDiaVOs()
			.forEach(h -> h.setIsLancadoRegistro(h.getHorarioTurmaDiaItemVOs().stream().map(HorarioTurmaDiaItemVO::getPossuiAulaRegistrada).reduce(Boolean::logicalAnd).orElse(Boolean.FALSE)));
	}

	@Override
	public void realizarEnvioEmailNotificacaoAlteracaoCronogramaAula(HorarioTurmaVO horarioTurmaVO, TemplateMensagemAutomaticaEnum templateMensagenAutomatica, UsuarioVO usuarioLogado,
			ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema, Boolean enviarComunicadoPorEmail,Map<Integer, String> hashDisciplinasAlteradas) throws Exception {
		 if(!horarioTurmaVO.getListaTemporariaHorarioTurmaDiaItemVOLog().isEmpty()) {		
		    PersonalizacaoMensagemAutomaticaVO msgNotificacao = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagenAutomatica, false, horarioTurmaVO.getTurma().getUnidadeEnsino().getCodigo(), usuarioLogado);
		   if (!Uteis.isAtributoPreenchido(msgNotificacao)  || msgNotificacao.getDesabilitarEnvioMensagemAutomatica()) {
			 throw new Exception("Não existe uma MENSAGEM PERSONALIZADA cadastrada ou habilitada no sistema.");
		   }	  
		   List<ComunicacaoInternaVO> comunicacaoInternaVOs = new ArrayList<ComunicacaoInternaVO>(0);		   
			if(templateMensagenAutomatica.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROFESSOR_PROGRAMACAO_AULA)) {
				PessoaVO professor = new PessoaVO();				
				HorarioTurmaDiaItemVO horarioTurmaDiaItemVO	 = horarioTurmaVO.getListaTemporariaHorarioTurmaDiaItemVOLog().get(0);
				professor  = horarioTurmaDiaItemVO.getFuncionarioVO() ; 
				professor.setDisciplinas(horarioTurmaDiaItemVO.getDisciplinaVO().getNome());							
				if(Uteis.isAtributoPreenchido(professor)) {
				 ComunicacaoInternaVO comunicadoInternoProfessor =  realizarMontagemComunicadoInternoNotificacaoAlteracaoCronogramaAulaPorTipoDestinatario(horarioTurmaVO, "PR", professor , null,   enviarComunicadoPorEmail, msgNotificacao ,configuracaoGeralPadraoSistema ,usuarioLogado );
				 comunicacaoInternaVOs.add(comunicadoInternoProfessor);				
				}
			}			
			if(templateMensagenAutomatica.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_COORDENADOR_PROGRAMACAO_AULA)) {
				List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
				pessoaVOs = consultarDestinatariosDestinatarios(horarioTurmaVO,TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_COORDENADOR_PROGRAMACAO_AULA ,  usuarioLogado, null);
				if(!pessoaVOs.isEmpty()) {
					for (PessoaVO pessoaVO : pessoaVOs) {					 
						 ComunicacaoInternaVO comunicadoInternoCoordenador =  realizarMontagemComunicadoInternoNotificacaoAlteracaoCronogramaAulaPorTipoDestinatario(horarioTurmaVO, "CO", pessoaVO ,null ,   enviarComunicadoPorEmail, msgNotificacao ,configuracaoGeralPadraoSistema ,usuarioLogado );
						 comunicacaoInternaVOs.add(comunicadoInternoCoordenador);
					 }
				}
			}
			if(templateMensagenAutomatica.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_USUARIO_PROGRAMACAO_AULA)) {	
				if(!horarioTurmaVO.getResponsaveisInternoAptoReceberNotificacaoCronogramaAula().isEmpty()) {
					for(FuncionarioVO funcionario : horarioTurmaVO.getResponsaveisInternoAptoReceberNotificacaoCronogramaAula()) {
						if(Uteis.isAtributoPreenchido(funcionario)) {		  
						      ComunicacaoInternaVO comunicadoInternoUsuario =  realizarMontagemComunicadoInternoNotificacaoAlteracaoCronogramaAulaPorTipoDestinatario(horarioTurmaVO, "FU", null ,funcionario,   enviarComunicadoPorEmail, msgNotificacao ,configuracaoGeralPadraoSistema ,usuarioLogado );
						      comunicacaoInternaVOs.add(comunicadoInternoUsuario);
						  }
					}	
				}
			}			
			if(templateMensagenAutomatica.equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_RESPONSAVEL_UNIDADE_PROGRAMACAO_AULA)) {				
			   FuncionarioVO responsavel = ((UnidadeEnsinoVO) getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaResponsavelNotificacaoAlteracaoCronogramaAulaPorCodigoUnidadeEnsino(horarioTurmaVO.getTurma().getUnidadeEnsino().getCodigo())).getResponsavelNotificacaoAlteracaoCronogramaAula();
			  if(Uteis.isAtributoPreenchido(responsavel)) {		  
			      ComunicacaoInternaVO comunicadoInternoResponsavelUnidade =  realizarMontagemComunicadoInternoNotificacaoAlteracaoCronogramaAulaPorTipoDestinatario(horarioTurmaVO, "FU", null ,responsavel,   enviarComunicadoPorEmail, msgNotificacao ,configuracaoGeralPadraoSistema ,usuarioLogado );
			      comunicacaoInternaVOs.add(comunicadoInternoResponsavelUnidade);
			  }
			}		
			for(ComunicacaoInternaVO comunicadoInterno : comunicacaoInternaVOs) {				
				try {
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicadoInterno, false, usuarioLogado, configuracaoGeralPadraoSistema,null);
				} catch (Exception e) {
					System.out.print("Erro ao enviar!");
				}
			}
           		
		 }
		
	}

	private ComunicacaoInternaVO realizarMontagemComunicadoInternoNotificacaoAlteracaoCronogramaAulaPorTipoDestinatario(HorarioTurmaVO horarioTurmaVO, String tipoDestinatario, PessoaVO pessoa, FuncionarioVO funcionario,
			Boolean enviarComunicadoPorEmail, PersonalizacaoMensagemAutomaticaVO msgNotificacao,ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema, UsuarioVO usuarioLogado) {
		
		 StringBuilder listaHorariosAlterados = horarioTurmaVO.getTextoAlteracaoHorarioTurmaLog();
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		if (usuarioLogado.getPessoa() == null || usuarioLogado.getPessoa().getCodigo().equals(0)) {
			comunicacaoInternaVO.setResponsavel(configuracaoGeralPadraoSistema.getResponsavelPadraoComunicadoInterno());
		} else {
			comunicacaoInternaVO.setResponsavel(usuarioLogado.getPessoa());
		}
		comunicacaoInternaVO.setEnviarEmail(enviarComunicadoPorEmail);
		comunicacaoInternaVO.setEnviarEmailInstitucional(msgNotificacao.getEnviarEmailInstitucional());
		comunicacaoInternaVO.setTipoMarketing(false);
		comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
		comunicacaoInternaVO.setDigitarMensagem(true);
		comunicacaoInternaVO.setRemoverCaixaSaida(false);
		comunicacaoInternaVO.setAssunto(msgNotificacao.getAssunto());	
		
		String mensagem = msgNotificacao.getMensagem();				
	    PessoaVO pessoaFinal = new PessoaVO();
		if(tipoDestinatario.equals("PR")) {	
			comunicacaoInternaVO.setTipoDestinatario("PR");			
			comunicacaoInternaVO.setProfessor(pessoa);
			comunicacaoInternaVO.setProfessorNome(pessoa.getNome());
			mensagem = mensagem.replaceAll("NOME_PROFESSOR", pessoa.getNome());
			mensagem = mensagem.replaceAll("NOME_DISCIPLINA", pessoa.getDisciplinas());
			mensagem = mensagem.replaceAll("TURMA", horarioTurmaVO.getTurma().getIdentificadorTurma());
			mensagem = mensagem.replaceAll("DATA", Uteis.getData(horarioTurmaVO.getDia()));		
			pessoaFinal = pessoa ;
		}
		if(tipoDestinatario.equals("CO")) {		
			comunicacaoInternaVO.setTipoDestinatario("CO");			
			comunicacaoInternaVO.setCoordenador(pessoa);
			comunicacaoInternaVO.setCoordenadorNome(pessoa.getNome());
			mensagem = mensagem.replaceAll("NOME_COORDENADOR", pessoa.getNome());		
			mensagem = mensagem.replaceAll("TURMA", horarioTurmaVO.getTurma().getIdentificadorTurma());
			mensagem = mensagem.replaceAll("CURSO", horarioTurmaVO.getTurma().getCurso().getNome());
			mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.LISTA_PROGRAMACAO_AULA_DISCIPLINA_PERIODO.name(), Matcher.quoteReplacement(listaHorariosAlterados.toString()));
			pessoaFinal = pessoa ;
		}
		if(tipoDestinatario.equals("FU")) {		
			 comunicacaoInternaVO.setTipoDestinatario("FU");
			 comunicacaoInternaVO.setFuncionario(funcionario);
			 comunicacaoInternaVO.setFuncionarioNome(funcionario.getPessoa().getNome());				
			 mensagem = mensagem.replaceAll("TURMA", horarioTurmaVO.getTurma().getIdentificadorTurma());
			 mensagem = mensagem.replaceAll("CURSO", horarioTurmaVO.getTurma().getCurso().getNome());
			 mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.LISTA_PROGRAMACAO_AULA_DISCIPLINA_PERIODO.name(), Matcher.quoteReplacement(listaHorariosAlterados.toString()));
			 pessoaFinal = funcionario.getPessoa() ;
		}
		    comunicacaoInternaVO.setMensagem(mensagem);
			ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
			comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			comunicadoInternoDestinatarioVO.setDataLeitura(null);
			comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
			comunicadoInternoDestinatarioVO.setCiJaLida(false);
			comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
			comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
			comunicadoInternoDestinatarioVO.setDestinatario(pessoaFinal);
			comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
		return comunicacaoInternaVO ;
	}		
	
}