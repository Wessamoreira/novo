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
import java.util.Set;

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

import negocio.comuns.academico.AbonoFaltaVO;
import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.AbonoFaltaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TurmaVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TurmaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TurmaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AbonoFalta extends ControleAcesso implements AbonoFaltaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AbonoFalta() throws Exception {
		super();
		setIdEntidade("AbonoFalta");
	}

	public AbonoFaltaVO novo() throws Exception {
		AbonoFalta.incluir(getIdEntidade());
		AbonoFaltaVO obj = new AbonoFaltaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/*
			 * Comentado por Wendel Rodrigues 13/03/2015 O sistema informa que
			 * não pode realizar a operação pois o periodo letivo já foi
			 * fachado, neste caso o sistema não deveria fazer esta validação
			 * visto que o aluno só justifica quando retorna a instituição.
			 */
			// getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPeriodoLetivoAtivo(obterUnidadeEnsinoCurso(obj,
			// usuario), obterSemestre(obj), obterAno(obj));
			AbonoFalta.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO AbonoFalta( datafim, datainicio, observacao, matricula, pessoa, tipoAbono, justificativa, abonarFaltaFuturosRegistrosAula, responsavel, tipojustificativafalta ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataFim()));
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlInserir.setString(3, obj.getObservacao());
					sqlInserir.setString(4, obj.getMatricula().getMatricula());
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getPessoa().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getTipoAbono());
					sqlInserir.setString(7, obj.getJustificativa());
					sqlInserir.setBoolean(8, obj.getAbonarFaltaFuturosRegistrosAula());
					sqlInserir.setInt(9, obj.getResponsavel().getCodigo());
					sqlInserir.setInt(10, obj.getTipoJustificativaFaltaVO().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			gravarDisciplinasAbono(obj, usuario);
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarDisciplinasAbono(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		Iterator<DisciplinaAbonoVO> i = obj.getDisciplinaAbonoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaAbonoVO disc = i.next();
			disc.setAbonoFalta(obj.getCodigo());
			if (disc.getFaltaAbonada() || disc.getFaltaJustificada()) {
				if (obj.getTipoAbono().equals("JU")) {
					disc.setFaltaAbonada(Boolean.FALSE);
					disc.setFaltaJustificada(Boolean.TRUE);
				} else {
					disc.setFaltaAbonada(Boolean.TRUE);
					disc.setFaltaJustificada(Boolean.FALSE);
				}
				disc.setMatricula(obj.getMatricula());
				getFacadeFactory().getDisciplinaAbonoFacade().incluir(disc, usuario);
				getFacadeFactory().getFrequenciaAulaFacade().alterarAbonoFalta(disc, usuario);				
				disc.setFaltaAbonada(Boolean.TRUE);
			}
		}
		realizarAtualizacaoFrequenciaHistorico(obj, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoFrequenciaHistorico(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		Iterator<DisciplinaAbonoVO> i = obj.getDisciplinaAbonoVOs().iterator();
		List<Integer> mptdProcessada = new ArrayList<Integer>(0);
		while (i.hasNext()) {
			DisciplinaAbonoVO disc = i.next();
			if (disc.getFaltaAbonada()) {
				if (!obj.getTipoAbono().equals("JU") && !mptdProcessada.contains(disc.getDisciplina().getCodigo())) {
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(disc.getMatricula().getMatricula(), disc.getRegistroAula().getTurma(), disc.getDisciplina().getCodigo(), disc.getRegistroAula().getAno(), disc.getRegistroAula().getSemestre(), null, false, false, usuario);
					if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
						mptdProcessada.add(disc.getDisciplina().getCodigo());
						HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), false, false, usuario);
						if (Uteis.isAtributoPreenchido(historicoVO)) {
							getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(historicoVO, historicoVO.getConfiguracaoAcademico(), usuario);
							if ((historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) && historicoVO.getFreguencia() >= historicoVO.getConfiguracaoAcademico().getPercentualFrequenciaAprovacao()) || (historicoVO.getSituacao().equals(SituacaoHistorico.APROVADO.getValor()) && historicoVO.getFreguencia() < historicoVO.getConfiguracaoAcademico().getPercentualFrequenciaAprovacao())) {
								getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, null, historicoVO.getConfiguracaoAcademico(), 0, TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, true, usuario);
								getFacadeFactory().getHistoricoFacade().gravarLancamentoNota(historicoVO, true, usuario.getVisaoLogar(), TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, usuario);
							} else {
								getFacadeFactory().getHistoricoFacade().alterarFaltaEFrequenciaHistorico(historicoVO, false, usuario);
							}
						}
					}

				}

			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			// AbonoFaltaVO.verificarQtdeFaltasJaAbonada(obj,
			// consultarAbonoFaltaPorPeriodoEMatricula(obj.getMatricula().getMatricula(),
			// obj.getDataInicio(), obj.getDataFim(), false,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			/*
			 * Comentado por Wendel Rodrigues 13/03/2015 O sistema informa que
			 * não pode realizar a operação pois o periodo letivo já foi
			 * fachado, neste caso o sistema não deveria fazer esta validação
			 * visto que o aluno só justifica quando retorna a instituição.
			 */
			// getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPeriodoLetivoAtivo(obterUnidadeEnsinoCurso(obj,
			// usuario), obterSemestre(obj), obterAno(obj));
			AbonoFalta.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE AbonoFalta set datafim=?, datainicio=?, observacao=?, matricula=?, pessoa=?, tipoAbono=?, justificativa=?, abonarFaltaFuturosRegistrosAula=?, responsavel=?, tipojustificativafalta=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataFim()));
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlAlterar.setString(3, obj.getObservacao());
					sqlAlterar.setString(4, obj.getMatricula().getMatricula());
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getTipoAbono());
					sqlAlterar.setString(7, obj.getJustificativa());
					sqlAlterar.setBoolean(8, obj.getAbonarFaltaFuturosRegistrosAula());
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(9, 0);
					}					
					sqlAlterar.setInt(10, obj.getTipoJustificativaFaltaVO().getCodigo());
					sqlAlterar.setInt(11, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			alterarDisciplinasAbono(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public Integer obterUnidadeEnsinoCurso(AbonoFaltaVO obj, UsuarioVO usuario) {
		try {
			List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(obj.getMatricula().getCurso().getCodigo(), obj.getMatricula().getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			if (Uteis.isAtributoPreenchido(unidadeEnsinoCursoVOs)) {
				for (UnidadeEnsinoCursoVO unidade : unidadeEnsinoCursoVOs) {
					return unidade.getCodigo();
				}
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public String obterSemestre(AbonoFaltaVO obj) {
		if (Uteis.isAtributoPreenchido(obj.getDisciplinaAbonoVOs())) {
			return obj.getDisciplinaAbonoVOs().get(0).getRegistroAula().getSemestre();
		}
		return "";
	}

	public String obterAno(AbonoFaltaVO obj) {
		if (Uteis.isAtributoPreenchido(obj.getDisciplinaAbonoVOs())) {
			return obj.getDisciplinaAbonoVOs().get(0).getRegistroAula().getAno();
		}
		return "";
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinasAbono(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		Iterator<DisciplinaAbonoVO> i = obj.getDisciplinaAbonoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaAbonoVO disc = (DisciplinaAbonoVO) i.next();
			disc.setAbonoFalta(obj.getCodigo());
			disc.setMatricula(obj.getMatricula());
			if (disc.getFaltaAbonada() || disc.getFaltaJustificada()) {
				if (obj.getTipoAbono().equals("JU")) {
					disc.setFaltaAbonada(Boolean.FALSE);
					disc.setFaltaJustificada(Boolean.TRUE);					
				} else {
					disc.setFaltaAbonada(Boolean.TRUE);
					disc.setFaltaJustificada(Boolean.FALSE);					
				}
				if (disc.getCodigo().equals(0)) {
					getFacadeFactory().getDisciplinaAbonoFacade().incluir(disc, usuario);
				} else {
					getFacadeFactory().getDisciplinaAbonoFacade().alterar(disc, usuario);
				}
				getFacadeFactory().getFrequenciaAulaFacade().alterarAbonoFalta(disc, usuario);		
				disc.setFaltaAbonada(Boolean.TRUE);				
			} else {
				if (!disc.getCodigo().equals(0)) {
					getFacadeFactory().getDisciplinaAbonoFacade().excluir(disc, usuario);
				}
			}
		}
		realizarAtualizacaoFrequenciaHistorico(obj, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		try {
			AbonoFalta.excluir(getIdEntidade(), true, usuario);
			Iterator<DisciplinaAbonoVO> i = obj.getDisciplinaAbonoVOs().iterator();
			HashMap<String, DisciplinaAbonoVO> hash = new HashMap<String, DisciplinaAbonoVO>();
			while (i.hasNext()) {
				DisciplinaAbonoVO disc = (DisciplinaAbonoVO) i.next();
				if (disc.getFaltaAbonada()) {
					getFacadeFactory().getDisciplinaAbonoFacade().excluir(disc, usuario);
					disc.setFaltaAbonada(Boolean.FALSE);
					disc.setFaltaJustificada(Boolean.FALSE);
					getFacadeFactory().getFrequenciaAulaFacade().alterarAbonoFalta(disc, usuario);
					if (!hash.containsKey(disc.getRegistroAula().getTurma().getCodigo()+";"+disc.getDisciplina().getCodigo())) {
						hash.put(disc.getRegistroAula().getTurma().getCodigo()+";"+disc.getDisciplina().getCodigo(), disc);
					}
				}	
			}
			Set<String> chaves = hash.keySet();
			for (String key : chaves) {
				DisciplinaAbonoVO disc = hash.get(key);
				getFacadeFactory().getTurmaFacade().carregarDados(disc.getRegistroAula().getTurma(), usuario);
				MatriculaPeriodoTurmaDisciplinaVO matPer = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(obj.getMatricula().getMatricula(), disc.getRegistroAula().getTurma(), disc.getDisciplina().getCodigo(), "", "", obj.getMatricula().getGradeCurricularAtual().getCodigo(), false, false, usuario);
				if (obj.getCodigo() > 0 && Uteis.isAtributoPreenchido(matPer)) {
					HistoricoVO hist = getFacadeFactory().getHistoricoFacade().consultaRapidaPorCodigoMatriculaPeriodoTurmaDisciplina(matPer.getCodigo(), null, false, usuario);
					if(Uteis.isAtributoPreenchido(hist)) {
						getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(hist, hist.getConfiguracaoAcademico(), usuario);
						getFacadeFactory().getHistoricoFacade().alterar(hist, usuario);
					}
				}				
			}
			String sql = "DELETE FROM AbonoFalta WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<AbonoFaltaVO> consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AbonoFalta WHERE  matricula = '" + valorConsulta + "' ORDER BY matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.AbonoFaltaInterfaceFacade#
	 * consultarPorNomePessoa(java.lang.String, boolean, int)
	 */
	public List<AbonoFaltaVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "Select * From AbonoFalta, Pessoa WHERE abonofalta.pessoa = pessoa.codigo and lower (Pessoa.nome) like('" + valorConsulta.toLowerCase() + "%')ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.AbonoFaltaInterfaceFacade#
	 * consultarDisciplinaComFalta(java.lang.String, java.util.Date,
	 * java.util.Date, boolean)
	 */
	public List<DisciplinaAbonoVO> consultarDisciplinaComFalta(String matricula, Date dataInicio, Date dataFim, String situacaoAula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct * from ( ");
		sqlStr.append("SELECT distinct frequenciaaula.matricula as matricula, disciplina.nome as disciplina_nome, disciplina.codigo as disciplina_codigo, registroaula.data as registroaula_data, ");
		sqlStr.append("registroaula.cargaHoraria as registroaula_cargaHoraria, registroaula.horario as registroaula_horario, registroaula.codigo as registroaula_codigo, registroaula.ano, registroaula.semestre, registroAula.turmaOrigem AS registroAula_turmaOrigem, registroAula.nrAula AS registroAula_nrAula, registroAula.ano AS registroAula_ano, registroAula.semestre AS registroAula_semestre, ");
		sqlStr.append("turma.codigo as turma_codigo, turma.identificadorturma ");
		sqlStr.append("FROM registroaula ");
		sqlStr.append("inner join frequenciaaula on frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append("inner join turma on turma.codigo = registroaula.turma ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = frequenciaaula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI') ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append("and ((mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma = registroaula.turma) ");
		sqlStr.append("or (mptd.turmapratica is not null and mptd.turmapratica = registroaula.turma) ");
		sqlStr.append("or (mptd.turmateorica is not null and mptd.turmateorica = registroaula.turma)) ");
		sqlStr.append("and mptd.disciplina = registroaula.disciplina ");
		sqlStr.append("and mptd.ano = registroaula.ano ");
		sqlStr.append("and mptd.semestre = registroaula.semestre ");
		sqlStr.append("inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		if (situacaoAula.equals("") || situacaoAula.equals("AF")) {
			sqlStr.append("where frequenciaaula.presente = false ");
		} else {
			sqlStr.append("where frequenciaaula.presente = true ");
		}
		sqlStr.append("and frequenciaaula.matricula = '").append(matricula).append("' ");
		sqlStr.append("AND registroaula.data >= '").append(Uteis.getDataJDBCTimestamp(dataInicio)).append("' ");
		sqlStr.append("AND registroaula.data <= '").append(Uteis.getDataJDBCTimestamp(dataFim)).append("' ");
		sqlStr.append("AND registroaula.codigo not in ( ");
		sqlStr.append("		SELECT distinct registroAula FROM disciplinaAbono ");
		sqlStr.append("		where disciplinaAbono.matricula = frequenciaaula.matricula ");
		sqlStr.append("		and disciplinaAbono.disciplina = disciplina.codigo) ");
		if (situacaoAula.equals("")) {
			sqlStr.append("union ");
			sqlStr.append("SELECT distinct matricula.matricula as matricula, disciplina.nome as disciplina_nome, disciplina.codigo as disciplina_codigo, registroaula.data as registroaula_data, ");
			sqlStr.append("registroaula.cargaHoraria as registroaula_cargaHoraria, registroaula.horario as registroaula_horario, registroaula.codigo as registroaula_codigo, registroaula.ano, registroaula.semestre, registroAula.turmaOrigem AS registroAula_turmaOrigem, registroAula.nrAula AS registroAula_nrAula, registroAula.ano AS registroAula_ano, registroAula.semestre AS registroAula_semestre, ");
			sqlStr.append("turma.codigo as turma_codigo, turma.identificadorturma ");
			sqlStr.append("from matricula ");
			sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI') ");
			sqlStr.append("inner join matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append("left join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
			sqlStr.append("inner join disciplina on disciplina.codigo = mptd.disciplina ");
			sqlStr.append("inner join registroaula on ");
			sqlStr.append("((mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma = registroaula.turma) ");
			sqlStr.append("or (mptd.turmapratica is not null and mptd.turmapratica = registroaula.turma) ");
			sqlStr.append("or (mptd.turmateorica is not null and mptd.turmateorica = registroaula.turma)) ");
			sqlStr.append("and registroaula.disciplina = disciplina.codigo ");
			sqlStr.append("and registroaula.ano = mptd.ano ");
			sqlStr.append("and registroaula.semestre = mptd.semestre ");
			sqlStr.append("inner join turma on turma.codigo = registroaula.turma ");
			sqlStr.append("where matricula.matricula = '").append(matricula).append("' ");
			sqlStr.append("AND registroaula.data >= '").append(Uteis.getDataJDBCTimestamp(dataInicio)).append("' ");
			sqlStr.append("AND registroaula.data <= '").append(Uteis.getDataJDBCTimestamp(dataFim)).append("' ");
			sqlStr.append("AND registroaula.codigo not in ( ");
			sqlStr.append("		SELECT distinct registroAula FROM disciplinaAbono ");
			sqlStr.append("		where disciplinaAbono.matricula = matricula.matricula ");
			sqlStr.append("		and disciplinaAbono.disciplina = disciplina.codigo) ");
			sqlStr.append("and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula  and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false)  ");
			sqlStr.append(" and his.matrizcurricular = matricula.gradecurricularatual limit 1 ");
			sqlStr.append(" )) ");
			// Essa condição OR é responsável por trazer as disciplinas que fazem parte da composição após realizar transferencia 
			// de matriz curricular. Isso porque após a transferência o aluno irá cursar a disciplina na grade antiga mesmo estando 
			// já realizada a transferência para nova grade. O sistema então irá trazer o histórico da matriz antiga caso não possua a mesma disciplina na nova grade.
			sqlStr.append(" or (historico.matrizcurricular = matriculaPeriodo.gradecurricular ");
			sqlStr.append(" and matricula.gradecurricularatual != historico.matrizcurricular ");
			sqlStr.append(" and historico.historicodisciplinafazpartecomposicao ");
			sqlStr.append(" and historico.disciplina not in (");
			sqlStr.append(" select his.disciplina from historico his ");
			sqlStr.append(" where his.matriculaperiodo = historico.matriculaperiodo ");
			sqlStr.append(" and his.disciplina = historico.disciplina ");
			sqlStr.append(" and matricula.gradecurricularatual = his.matrizcurricular))	");
			sqlStr.append(") ");
		}
		sqlStr.append(") as t ");
		sqlStr.append("Group By t.matricula, t.disciplina_nome, t.disciplina_codigo, t.registroaula_data, t.registroaula_cargaHoraria, t.registroaula_horario, t.registroaula_codigo, t.ano, t.semestre, t.turma_codigo, t.identificadorturma, t.registroAula_turmaOrigem, t.registroAula_nrAula, t.registroAula_ano, t.registroAula_semestre ");
		sqlStr.append("ORDER BY t.registroaula_data, t.identificadorturma, t.disciplina_nome, t.registroaula_horario");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		//System.out.println(sqlStr);
		return montarDadosDisciplinaComFalta(tabelaResultado, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.AbonoFaltaInterfaceFacade#
	 * consultarAbonoFaltaPorPeriodoEMatricula(java.lang.String, java.util.Date,
	 * java.util.Date, boolean, int)
	 */
	public List<AbonoFaltaVO> consultarAbonoFaltaPorPeriodoEMatricula(String matricula, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM abonofalta " + "LEFT JOIN disciplinaabono on (disciplinaabono.abonofalta = abonofalta.codigo) " + "LEFT JOIN disciplina on (disciplinaabono.disciplina = disciplina.codigo) " + "WHERE abonofalta.datainicio >= '" + Uteis.getDataJDBCTimestamp(dataInicio) + "' AND abonofalta.datafim <= '" + Uteis.getDataJDBCTimestamp(dataFim) + "' AND abonofalta.matricula = '" + matricula + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.AbonoFaltaInterfaceFacade#
	 * montarDadosDisciplinaComFalta(java.sql.ResultSet)
	 */
	public List<DisciplinaAbonoVO> montarDadosDisciplinaComFalta(SqlRowSet result, UsuarioVO usuario) throws Exception {
		List<DisciplinaAbonoVO> vetResultado = new ArrayList<DisciplinaAbonoVO>(0);
		DisciplinaAbonoVO obj = null;
		while (result.next()) {
			obj = new DisciplinaAbonoVO();
			obj.getDisciplina().setCodigo(result.getInt("disciplina_codigo"));
			obj.getDisciplina().setNome(result.getString("disciplina_nome"));
			obj.getRegistroAula().setCodigo(result.getInt("registroaula_codigo"));
			obj.getRegistroAula().setData(result.getDate("registroaula_data"));
			obj.getRegistroAula().setHorario(result.getString("registroaula_horario"));
			obj.getRegistroAula().setCargaHoraria(result.getInt("registroaula_cargaHoraria"));
			obj.getRegistroAula().setAno(result.getString("ano"));
			obj.getRegistroAula().setSemestre(result.getString("semestre"));
			obj.getRegistroAula().getTurma().setCodigo(result.getInt("turma_codigo"));
			obj.getRegistroAula().getTurma().setIdentificadorTurma(result.getString("identificadorTurma"));
			obj.getRegistroAula().setTurmaOrigem(result.getInt("registroAula_turmaOrigem"));
			obj.getRegistroAula().setNrAula(result.getInt("registroAula_nrAula"));
			obj.getRegistroAula().setAno(result.getString("registroAula_ano"));
			obj.getRegistroAula().setSemestre(result.getString("registroAula_semestre"));
			obj.getRegistroAula().getDisciplina().setCodigo(result.getInt("disciplina_codigo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public static void montarDadosRegistroAula(DisciplinaAbonoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRegistroAula().getCodigo().intValue() == 0) {
			obj.setRegistroAula(new RegistroAulaVO());
			return;
		}
		obj.setRegistroAula(getFacadeFactory().getRegistroAulaFacade().consultarPorChavePrimaria(obj.getRegistroAula().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code>
	 *         resultantes da consulta.
	 */
	public static List<AbonoFaltaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		try {
			List<AbonoFaltaVO> vetResultado = new ArrayList<AbonoFaltaVO>(0);
			while (tabelaResultado.next()) {
				vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
			}
			return vetResultado;
		} catch (Exception e) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TurmaVO</code>.
	 * 
	 * @return O objeto da classe <code>TurmaVO</code> com os dados devidamente
	 *         montados.
	 */
	public static AbonoFaltaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AbonoFaltaVO obj = new AbonoFaltaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataFim(dadosSQL.getDate("datafim"));
		obj.setDataInicio(dadosSQL.getDate("datainicio"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.setTipoAbono(dadosSQL.getString("tipoAbono"));
		obj.setTipoJustificativaFaltaVO(getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorChavePrimeira(dadosSQL.getInt("tipojustificativafalta"), false, nivelMontarDados, usuario)); 
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));		
		obj.setAbonarFaltaFuturosRegistrosAula(dadosSQL.getBoolean("abonarFaltaFuturosRegistrosAula"));
		obj.setDisciplinaAbonoVOs(getFacadeFactory().getDisciplinaAbonoFacade().consultarProAbonoFalta(obj.getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosPessoa(obj, nivelMontarDados, usuario);
		montarDadosResponsavel(obj, nivelMontarDados, usuario);		
		return obj;
	}

	public static void montarDadosMatricula(AbonoFaltaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getMatricula().getMatricula().equals("")) {
			obj.setMatricula(new MatriculaVO());
			return;
		}

		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	public static void montarDadosPessoa(AbonoFaltaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public AbonoFaltaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM AbonoFalta WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavel(AbonoFaltaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return AbonoFalta.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		AbonoFalta.idEntidade = idEntidade;
	}

	public void carregarDados(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((AbonoFaltaVO) obj, NivelMontarDados.TODOS, usuario);
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
	public void carregarDados(AbonoFaltaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((AbonoFaltaVO) obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((AbonoFaltaVO) obj, resultado);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codAbonoFalta, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (abonoFalta.codigo= ").append(codAbonoFalta).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codAbonoFalta, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (abonoFalta.codigo= ").append(codAbonoFalta).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT abonoFalta.codigo, abonoFalta.tipoAbono, abonoFalta.dataInicio, abonoFalta.dataFim, abonoFalta.abonarFaltaFuturosRegistrosAula, matricula.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" , ");
		str.append(" usuario.codigo AS \"abonoFalta.codResponsavel\", ");
		str.append(" usuario.nome AS \"abonoFalta.nomeResponsavel\" ");		
		str.append(" FROM abonoFalta ");
		str.append(" INNER JOIN matricula ON matricula.matricula = abonoFalta.matricula ");
		str.append(" INNER JOIN pessoa ON pessoa.codigo = abonoFalta.pessoa ");
		str.append(" LEFT JOIN usuario ON usuario.codigo = abonoFalta.responsavel ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT DISTINCT abonoFalta.codigo, abonoFalta.tipoAbono, abonoFalta.tipojustificativafalta AS \"tipojustificativafalta\" , abonoFalta.observacao, abonoFalta.dataInicio, abonoFalta.dataFim, abonoFalta.abonarFaltaFuturosRegistrosAula, ");
		str.append(" matricula.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		str.append(" disciplinaAbono.codigo AS \"disciplinaAbono.codigo\", disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		str.append(" m1.matricula AS \"disciplinaAbono.matricula\", turma.codigo as \"turma.codigo\", turma.identificadorturma, turma.subturma as \"turma.subturma\", turma.tiposubturma as \"turma.tiposubturma\", ");
		str.append(" registroAula.codigo AS \"registroAula.codigo\", registroAula.cargaHoraria AS \"registroAula.cargaHoraria\", registroAula.horario AS \"registroAula.horario\", registroAula.data AS \"registroAula.data\", registroAula.turmaOrigem AS \"registroAula.turmaOrigem\", registroAula.nrAula AS \"registroAula.nrAula\", registroAula.ano AS \"registroAula.ano\", registroAula.semestre AS \"registroAula.semestre\", ");
		str.append(" disciplinaAbono.abonoFalta AS \"disciplinaAbono.abonoFalta\", ");
		str.append(" usuario.codigo AS \"abonoFalta.codResponsavel\", ");
		str.append(" usuario.nome AS \"abonoFalta.nomeResponsavel\", ");
		str.append(" disciplinaAbono.faltaAbonada AS \"disciplinaAbono.faltaAbonada\", disciplinaAbono.faltaJustificada AS \"disciplinaAbono.faltaJustificada\" ");
		str.append(" FROM abonoFalta ");
		str.append(" INNER JOIN matricula ON matricula.matricula = abonoFalta.matricula ");
		str.append(" INNER JOIN pessoa ON pessoa.codigo = abonoFalta.pessoa ");
		str.append(" LEFT JOIN disciplinaAbono ON disciplinaAbono.abonoFalta = abonoFalta.codigo ");
		str.append(" LEFT JOIN disciplina ON disciplina.codigo = disciplinaAbono.disciplina ");
		str.append(" LEFT JOIN matricula m1 ON m1.matricula = disciplinaAbono.matricula ");
		str.append(" left JOIN registroAula ON registroAula.codigo = disciplinaAbono.registroAula ");
		str.append(" LEFT JOIN usuario ON usuario.codigo = abonoFalta.responsavel ");
		str.append("left join turma on turma.codigo = registroaula.turma ");
		return str;
	}

	public List<AbonoFaltaVO> consultaRapidaPorMatricula(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(matricula.matricula)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY matricula.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<AbonoFaltaVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(pessoa.nome)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<AbonoFaltaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<AbonoFaltaVO> vetResultado = new ArrayList<AbonoFaltaVO>(0);
		while (tabelaResultado.next()) {
			AbonoFaltaVO obj = new AbonoFaltaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}

	private void montarDadosBasico(AbonoFaltaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do AbonoFalta
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTipoAbono(dadosSQL.getString("tipoAbono"));
		obj.setDataInicio(dadosSQL.getDate("dataInicio"));
		obj.setDataFim(dadosSQL.getDate("dataFim"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.setAbonarFaltaFuturosRegistrosAula(dadosSQL.getBoolean("abonarFaltaFuturosRegistrosAula"));
		// Dados da Pessoa
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa.codigo")));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("abonoFalta.codResponsavel")));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getResponsavel().setNome(dadosSQL.getString("abonoFalta.nomeResponsavel"));		
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
	}

	private void montarDadosCompleto(AbonoFaltaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setTipoAbono(dadosSQL.getString("tipoAbono"));
		obj.getTipoJustificativaFaltaVO().setCodigo(dadosSQL.getInt("tipojustificativafalta"));
		obj.setTipoJustificativaFaltaVO(getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorChavePrimeira(obj.getTipoJustificativaFaltaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO()));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setDataInicio(dadosSQL.getDate("dataInicio"));
		obj.setDataFim(dadosSQL.getDate("dataFim"));
		obj.setAbonarFaltaFuturosRegistrosAula(dadosSQL.getBoolean("abonarFaltaFuturosRegistrosAula"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("abonoFalta.codResponsavel"));
		obj.getResponsavel().setNome(dadosSQL.getString("abonoFalta.nomeResponsavel"));		
		// Dados Pessoa
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));

		DisciplinaAbonoVO disciplinaAbonoVO = null;
		do {
			if (dadosSQL.getInt("disciplinaAbono.codigo") != 0) {
				disciplinaAbonoVO = new DisciplinaAbonoVO();
				disciplinaAbonoVO.setCodigo(dadosSQL.getInt("disciplinaAbono.codigo"));
				disciplinaAbonoVO.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
				disciplinaAbonoVO.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
				disciplinaAbonoVO.getMatricula().setMatricula(dadosSQL.getString("disciplinaAbono.matricula"));
				disciplinaAbonoVO.getRegistroAula().setCodigo(dadosSQL.getInt("registroAula.codigo"));
				disciplinaAbonoVO.getRegistroAula().setCargaHoraria(dadosSQL.getInt("registroAula.cargaHoraria"));
				disciplinaAbonoVO.getRegistroAula().setHorario(dadosSQL.getString("registroAula.horario"));
				disciplinaAbonoVO.getRegistroAula().setData(dadosSQL.getDate("registroAula.data"));
				disciplinaAbonoVO.getRegistroAula().getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
				disciplinaAbonoVO.getRegistroAula().getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
				disciplinaAbonoVO.getRegistroAula().getTurma().setSubturma(dadosSQL.getBoolean("turma.subturma"));
				disciplinaAbonoVO.getRegistroAula().getTurma().setTipoSubTurma(TipoSubTurmaEnum.valueOf(dadosSQL.getString("turma.tipoSubTurma")));
				disciplinaAbonoVO.setAbonoFalta(dadosSQL.getInt("disciplinaAbono.abonoFalta"));
				disciplinaAbonoVO.getRegistroAula().setTurmaOrigem(dadosSQL.getInt("registroAula.turmaOrigem"));
				disciplinaAbonoVO.getRegistroAula().setNrAula(dadosSQL.getInt("registroAula.nrAula"));
				disciplinaAbonoVO.getRegistroAula().setAno(dadosSQL.getString("registroAula.ano"));
				disciplinaAbonoVO.getRegistroAula().setSemestre(dadosSQL.getString("registroAula.semestre"));
				disciplinaAbonoVO.getRegistroAula().getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
				disciplinaAbonoVO.setFaltaAbonada(dadosSQL.getBoolean("disciplinaAbono.faltaAbonada"));
				disciplinaAbonoVO.setFaltaJustificada(dadosSQL.getBoolean("disciplinaAbono.faltaJustificada"));
				obj.getDisciplinaAbonoVOs().add(disciplinaAbonoVO);
			}
			if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("codigo")))) {
				return;
			}
		} while (dadosSQL.next());
	}
	
	public void consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(RegistroAulaVO registroAula) throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("SELECT DISTINCT abonoFalta.codigo, abonoFalta.tipoAbono, abonoFalta.tipojustificativafalta, abonoFalta.observacao, abonoFalta.dataInicio, abonoFalta.dataFim, abonoFalta.abonarFaltaFuturosRegistrosAula, ");
		str.append(" matricula.matricula, pessoa.codigo AS \"pessoa.codigo\" ");
		str.append(" FROM abonoFalta ");
		str.append(" INNER JOIN matricula ON matricula.matricula = abonoFalta.matricula ");
		str.append(" INNER JOIN pessoa ON pessoa.codigo = abonoFalta.pessoa ");
		str.append(" WHERE (abonoFalta.abonarFaltaFuturosRegistrosAula = TRUE) ");
		str.append(" AND ('").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(registroAula.getData())).append("' BETWEEN abonoFalta.dataInicio AND abonoFalta.dataFim) ");
		boolean primeiraInteracao = true;
		for (FrequenciaAulaVO frequenciaAulaVO : registroAula.getFrequenciaAulaVOs()) {
			if ((!Uteis.isAtributoPreenchido(registroAula) || frequenciaAulaVO.isNovoObj()) && !frequenciaAulaVO.getAbonado()) {
				if (primeiraInteracao) {
					str.append(" AND (abonoFalta.matricula IN ( ");
					primeiraInteracao = false;
					str.append("'").append(frequenciaAulaVO.getMatricula().getMatricula()).append("'");
				} else {
					str.append(", '").append(frequenciaAulaVO.getMatricula().getMatricula()).append("' ");
				}
			}
		}
		if (!primeiraInteracao) {
			str.append(" )) ");

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
			while (tabelaResultado.next()) {
				// Montando dados do abono falta
				AbonoFaltaVO abonoFaltaVO = new AbonoFaltaVO();
				abonoFaltaVO.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
				abonoFaltaVO.setTipoAbono(tabelaResultado.getString("tipoAbono"));
				abonoFaltaVO.setTipoJustificativaFaltaVO(getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorChavePrimeira(tabelaResultado.getInt("tipojustificativafalta"), false, Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO()));
				abonoFaltaVO.setDataInicio(tabelaResultado.getDate("dataInicio"));
				abonoFaltaVO.setDataFim(tabelaResultado.getDate("dataFim"));
				abonoFaltaVO.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
				abonoFaltaVO.setAbonarFaltaFuturosRegistrosAula(tabelaResultado.getBoolean("abonarFaltaFuturosRegistrosAula"));
				abonoFaltaVO.getPessoa().setCodigo(new Integer(tabelaResultado.getInt("pessoa.codigo")));

				DisciplinaAbonoVO disciplinaAbonoVO = new DisciplinaAbonoVO();
				disciplinaAbonoVO.setRegistroAula(registroAula);
				disciplinaAbonoVO.setDisciplina(registroAula.getDisciplina());
				disciplinaAbonoVO.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
				if (abonoFaltaVO.getTipoAbono().equals("AB")) {
					disciplinaAbonoVO.setFaltaAbonada(Boolean.TRUE);
					disciplinaAbonoVO.setFaltaJustificada(false);
				} else {
					disciplinaAbonoVO.setFaltaAbonada(false);
					disciplinaAbonoVO.setFaltaJustificada(Boolean.TRUE);
				}
				disciplinaAbonoVO.setAbonoFalta(abonoFaltaVO.getCodigo());
				disciplinaAbonoVO.setDescricaoMotivoAbonoJustificativa(abonoFaltaVO.getDescricaoAbonoFalta());

				carregarAbonoFaltaParaFrequenciasDoRegistroAula(registroAula, disciplinaAbonoVO);
			}
		}
	}

	private void carregarAbonoFaltaParaFrequenciasDoRegistroAula(RegistroAulaVO registroAula, DisciplinaAbonoVO disciplinaAbonoVO) {
		for (FrequenciaAulaVO frequenciaAulaVO : registroAula.getFrequenciaAulaVOs()) {
			if (frequenciaAulaVO.getMatricula().getMatricula().equals(disciplinaAbonoVO.getMatricula().getMatricula())) {
				frequenciaAulaVO.setDisciplinaAbonoVO(disciplinaAbonoVO);
				frequenciaAulaVO.setAbonado(disciplinaAbonoVO.getFaltaAbonada());
				frequenciaAulaVO.setJustificado(disciplinaAbonoVO.getFaltaJustificada());
				frequenciaAulaVO.setPresente(false);
				return;
			}

		}
	}

	/**
	 * Método responsável por realizar a consulta da lista de disciplinaabono
	 * para a geração do abono de registro de aula no ato da transferência de
	 * turma cujo frequencia aula seja abonado ou presente
	 * 
	 * @param matricula
	 * @param dataInicio
	 * @param dataFim
	 * @param turmaDestino
	 * @param disciplina
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<DisciplinaAbonoVO> consultarDisciplinaParaAbonoRegistroAulaTransferenciaTurma(String matricula, Integer qtdeRegistroAbonar, Integer turmaDestino, Integer disciplina, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT disciplina.nome as disciplina_nome, disciplina.codigo as disciplina_codigo, registroaula.data as registroaula_data, registroaula.cargaHoraria as registroaula_cargaHoraria, ");
		sb.append("registroaula.horario as registroaula_horario, registroaula.codigo as registroaula_codigo, ano, semestre, registroAula.turmaOrigem as registroAula_turmaOrigem, ");
		sb.append("registroAula.semestre as registroAula_semestre, registroAula.ano as registroAula_ano, registroAula.nrAula as registroAula_nrAula, turma.codigo as turma_codigo, turma.identificadorturma ");
		sb.append("FROM registroaula ");
		sb.append("inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sb.append("inner join turma on turma.codigo = registroaula.turma ");
		sb.append("WHERE registroaula.turma = ").append(turmaDestino);
		sb.append(" AND disciplina.codigo = ").append(disciplina);
		sb.append(" AND registroaula.ano = '").append(ano).append("'");
		sb.append(" AND registroaula.semestre = '").append(semestre).append("'");
		sb.append(" ORDER BY registroaula_data");
		sb.append(" LIMIT ").append(qtdeRegistroAbonar);		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosDisciplinaComFalta(tabelaResultado, usuario);
	}

	/**
	 * Método responsável por executar a geração de abono do registro de aula de
	 * acordo com a data início e a data fim do registro de aula da turma origem
	 * para a turma de destino.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarGeracaoAbonoRegistroAulaTransferenciaTurma(MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplina, Integer qtdeRegistroAbonar, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		AbonoFaltaVO obj = new AbonoFaltaVO();
		obj.setAbonarFaltaFuturosRegistrosAula(false);
		obj.setDisciplinaAbonoVOs(consultarDisciplinaParaAbonoRegistroAulaTransferenciaTurma(ultimaMatriculaPeriodoAtiva.getMatriculaVO().getMatricula(), qtdeRegistroAbonar, turmaDestino.getCodigo(), disciplina.getCodigo(), ultimaMatriculaPeriodoAtiva.getAno(), ultimaMatriculaPeriodoAtiva.getSemestre(), controlarAcesso, usuarioVO));
		obj.getTipoJustificativaFaltaVO().setCodigo(3);
		obj.setDataInicio(obj.getDisciplinaAbonoVOs().get(0).getRegistroAula().getData());
		obj.setDataFim(obj.getDisciplinaAbonoVOs().get(obj.getDisciplinaAbonoVOs().size() - 1).getRegistroAula().getData());
		obj.setMatricula(ultimaMatriculaPeriodoAtiva.getMatriculaVO());
		obj.setObservacao("Transferência de Turma: " + turmaOrigem.getIdentificadorTurma() + " para " + turmaDestino.getIdentificadorTurma());
		obj.setPessoa(ultimaMatriculaPeriodoAtiva.getMatriculaVO().getAluno());
		obj.setTipoAbono("TT");
		obj.setResponsavel(usuarioVO);
		for (DisciplinaAbonoVO disciplinaAbonoVO : obj.getDisciplinaAbonoVOs()) {
			disciplinaAbonoVO.setMatricula(ultimaMatriculaPeriodoAtiva.getMatriculaVO());
			disciplinaAbonoVO.setFaltaAbonada(true);
			disciplinaAbonoVO.setNovoObj(true);
		}
		incluir(obj, usuarioVO);
	}

	/**
	 * Método responsável por realizar a consulta da data início e data fim para
	 * a geração de abono de registro de aula na transferência de turma.
	 * 
	 * @param matricula
	 * @param turma
	 * @param disciplina
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	private void montarDataInicioDataFimGerarAbonoRegistroAulaTransferenciaTurma(AbonoFaltaVO obj, String matricula, Integer turma, Integer disciplina, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select min(registroaula.data) as dataInicio, max(registroaula.data) as dataFim ");
		sqlStr.append("from registroaula ");
		sqlStr.append("inner join frequenciaaula on frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append("where matricula = '").append(matricula).append("'");
		sqlStr.append(" and turma = ").append(turma);
		sqlStr.append(" and disciplina = ").append(disciplina);
		sqlStr.append(" and ano = '").append(ano).append("'");
		sqlStr.append(" and semestre = '").append(semestre).append("'");
		sqlStr.append(" and (presente or abonado)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			obj.setDataInicio(rs.getDate("dataInicio"));
			obj.setDataFim(rs.getDate("dataFim"));
		}
	}

	/**
	 * Método responsável por realizar a consulta da quantidade de horas de
	 * registro de aula na turma origem para transferência de turma.
	 */
	@Override
	public int verificarQtdeHorasRegistroAulaTransferenciaTurma(String matricula, Integer turma, Integer disciplina, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select count (registroaula.data) as qtdeHoras ");
		sqlStr.append("from registroaula ");
		sqlStr.append("inner join frequenciaaula on frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append("where (presente or abonado)");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" and matricula = '").append(matricula).append("'");
		}
		sqlStr.append(" and turma = ").append(turma);
		sqlStr.append(" and disciplina = ").append(disciplina);
		sqlStr.append(" and ano = '").append(ano).append("'");
		sqlStr.append(" and semestre = '").append(semestre).append("'");
		return getConexao().getJdbcTemplate().queryForInt(sqlStr.toString());
	}

	/**
	 * Método reponsável por verificar se o registro de aula em questão já foi
	 * abonado para a transferência de turma.
	 */
	@Override
	public boolean verificarAbonoRegistroAulaTransferenciaTurma(Integer pessoa, String matricula, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select abonofalta.codigo from abonofalta ");
		sqlStr.append("inner join disciplinaabono on disciplinaabono.abonofalta = abonofalta.codigo ");
		sqlStr.append("where abonofalta.pessoa = ").append(pessoa);
		sqlStr.append(" and abonofalta.matricula = '").append(matricula).append("'");
		sqlStr.append(" and disciplinaabono.disciplina = ").append(disciplina);
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AbonoFaltaVO abonoFaltaVO, UsuarioVO usuarioVO) throws Exception {
		if (abonoFaltaVO.isNovoObj()) {
			incluir(abonoFaltaVO, usuarioVO);
		} else {
			alterar(realizarRemocaoObjetoListaDisciplinaAbono(abonoFaltaVO), usuarioVO);
		}
		
		for (Iterator<DisciplinaAbonoVO> i = abonoFaltaVO.getDisciplinaAbonoVOs().iterator(); i.hasNext();) {
			DisciplinaAbonoVO disc = i.next();
			if (!disc.getFaltaAbonada()) {
				i.remove();
			}
		}
	}

	public void validarDados(AbonoFaltaVO obj) throws ConsistirException {
		if (obj.getMatricula() == null || obj.getMatricula().getMatricula().equals("")) {
			throw new ConsistirException("O campo MATRICULA (Abono Falta) deve ser informado.");
		}
		if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo NOME (Abono Falta) deve ser info	rmado.");
		}
		
		if(!obj.getTipoJustificativaFaltaVO().getTipoSexoJustificativa().equals("AB")){
			if(obj.getTipoJustificativaFaltaVO().getTipoSexoJustificativa().equals("F") && (!obj.getPessoa().getSexo().equals("F"))){
				throw new ConsistirException("Um Abono com esta justificativa só pode ser registrado para uma pessoa do sexo Feminino.");
			}
			if(obj.getTipoJustificativaFaltaVO().getTipoSexoJustificativa().equals("M") && (!obj.getPessoa().getSexo().equals("M"))){
				throw new ConsistirException("Um Abono com esta justificativa só pode ser registrado para uma pessoa do sexo Masculino.");
			}
		}
		
		if (obj.getDataInicio() == null) {
			throw new ConsistirException("O campo DATA INÍCIO (Abono Falta) deve ser informado.");
		}
		if (obj.getDataFim() == null) {
			throw new ConsistirException("O campo DATA FIM (Abono Falta) deve ser informado.");
		}
		if (obj.getTipoAbono().equals("AB")) {
			if (obj.getTipoJustificativaFaltaVO().getCodigo() == null || obj.getTipoJustificativaFaltaVO().getCodigo() == 0) {
				throw new ConsistirException("O campo JUSTIFICATIVA (Abono Falta) deve ser informado.");
			}
		}
		if (obj.getDisciplinaAbonoVOs() == null || obj.getDisciplinaAbonoVOs().isEmpty()) {
			if (!obj.getAbonarFaltaFuturosRegistrosAula()) {
				throw new ConsistirException("Pelo menos uma DISCIPLINA (Abono Falta) deverá ser informada para registrar este Abono. Caso deseje registrar um Abono para futuros registros de aula marque a opção - Abonar Falta Futuros Registros de Aula");
			}
		}
	}
	
	public AbonoFaltaVO realizarRemocaoObjetoListaDisciplinaAbono(AbonoFaltaVO abonoFaltaVO) {
		for (Iterator<DisciplinaAbonoVO> i = abonoFaltaVO.getDisciplinaAbonoVOs().iterator(); i.hasNext();) {
			DisciplinaAbonoVO disc = i.next();
			if (!disc.getFaltaAbonada() &&  disc.getCodigo().equals(0)) {
				i.remove();
			}
		}
		return abonoFaltaVO;
	}
	

}
