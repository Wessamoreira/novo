package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import negocio.comuns.academico.TurmaDisciplinaVO;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.DiarioFrequenciaAulaVO;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.interfaces.academico.EspelhoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EspelhoRel extends SuperRelatorio implements EspelhoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#validarDados()
	 */
	public void validarDados(TurmaVO turmaVO, String semestre, String ano) throws Exception {
		if (turmaVO == null || turmaVO.getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo TURMA (Diário) deve ser informado.");
		}
		Boolean liberarRegistroAulaEntrePeriodo;
		if (turmaVO.getTurmaAgrupada()) {
			liberarRegistroAulaEntrePeriodo = getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEntrePeriodoConsiderandoTodosCursosTurmaAgrupada(turmaVO);
		} else {
			liberarRegistroAulaEntrePeriodo = turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo();
		}
		if (turmaVO.getSemestral() && !liberarRegistroAulaEntrePeriodo) {
			if (semestre == null || semestre.equals("")) {
				throw new ConsistirException("O campo SEMESTRE (Histórico Turma) deve ser informado");
			}
			if (ano == null || ano.equals("")) {
				throw new ConsistirException("O campo ANO (Histórico Turma) deve ser informado");
			}
		}
		if (turmaVO.getAnual()) {
			if (ano.equals("")) {
				throw new ConsistirException("O campo ANO (Histórico Turma) deve ser informado");
			}
		}
	}

	public void validarDadosRelatorio(TurmaVO turmaVO, String semestre, String ano, Integer disciplina, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		if (unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EspelhoDiarioRel_unidadeEnsino"));
		}
		if (turmaVO == null || turmaVO.getIdentificadorTurma().equals("")) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EspelhoDiarioRel_identificadorTurma"));
		}
		if (turmaVO.getSemestral()) {
			if (semestre == null || semestre.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_EspelhoDiarioRel_semestre"));
			}
			if (ano == null || ano.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_EspelhoDiarioRel_ano"));
			}
		}
		if (turmaVO.getAnual()) {
			if (ano.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_EspelhoDiarioRel_ano"));
			}
		}
		if (disciplina.intValue() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EspelhoDiarioRel_disciplina"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * consultarRegistroAula()
	 */
	public List<DiarioRegistroAulaVO> consultarRegistroAula(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, UsuarioVO usuarioVO) throws Exception {
		EspelhoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		validarDados(turmaVO, semestre, ano);
		String semestrePrm = semestre;
		String anoPrm = ano;
		if (turmaVO.getIntegral()) {
			semestrePrm = "";
			anoPrm = "";
		}
		List<RegistroAulaVO> listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultarPorIdentificadorTurmaProfessorDisciplina(turmaVO, semestrePrm, anoPrm, professor, disciplina, 0, false, Uteis.NIVELMONTARDADOS_TODOS, filtroAcademicoVO, mes, anoMes, usuarioVO);
		return montarListaDiarioVO(listaConsulta, semestre, ano, professor, configuracaoFinanceiroVO, usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * montarListaDiarioVO(java.util.List)
	 */
	public List<DiarioRegistroAulaVO> montarListaDiarioVO(List<RegistroAulaVO> listaConsulta, String semestre, String ano, Integer professor, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<DiarioRegistroAulaVO> listaFinal = new ArrayList<DiarioRegistroAulaVO>(0);
		int index = 1;
		PessoaVO prof = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(professor, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		DiarioRegistroAulaVO diarioRegistroAula = new DiarioRegistroAulaVO();
		Iterator<RegistroAulaVO> i = listaConsulta.iterator();
		while (i.hasNext()) {
			RegistroAulaVO registroAula = (RegistroAulaVO) i.next();
			try {
				String chAula = registroAula.getCargaHoraria().toString();
				if (!chAula.equals("")) {
					if (chAula.length() == 1) {
						chAula = "0" + chAula;
					}
					chAula = " - " + chAula + "h";
				}
				String data = (String) UtilReflexao.invocarMetodoGet(registroAula, "data_Apresentar");
				UtilReflexao.invocarMetodo(diarioRegistroAula, "setData" + index, data + chAula);
				montarListaFrequencia(registroAula, diarioRegistroAula, index, semestre, ano, configuracaoFinanceiroVO, usuarioVO);
				if (index == 1) {
					diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
					diarioRegistroAula.setCargaHorariaStr(Uteis.converterMinutosEmHorasStr(registroAula.getGradeDisciplinaVO().getCargaHoraria().doubleValue()));
					diarioRegistroAula.setObjeto(registroAula.getCodigo());
					diarioRegistroAula.setProfessor(prof);
					diarioRegistroAula.setDisciplina(registroAula.getDisciplina());
					diarioRegistroAula.setTurma(registroAula.getTurma());
					Integer codigoConfiguracao = registroAula.getTurma().getCurso().getConfiguracaoAcademico().getCodigo();
					if (!registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo().equals(0)) {
						codigoConfiguracao = registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo();
					}
					diarioRegistroAula.setConfiguracaoAcademico(consultarConfiguracaoAcademico(codigoConfiguracao));
					verificarNotasUtilizadasConfiguracaoAcademico(diarioRegistroAula);
				} else if (index == 30) {
					Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
					listaFinal.add(diarioRegistroAula);
					index = 0;
					diarioRegistroAula = new DiarioRegistroAulaVO();
				}
				index++;
			} catch (Exception e) {
				throw e;
			}
		}
		if (index > 1 && index < 30) {
			Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
			listaFinal.add(diarioRegistroAula);
		}
		return listaFinal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * montarListaFrequencia(negocio.comuns.academico. RegistroAulaVO,
	 * relatorio.negocio.comuns.academico.DiarioRegistroAulaVO, int)
	 */
	public void montarListaFrequencia(RegistroAulaVO registroAula, DiarioRegistroAulaVO diarioRegistroAula, int index, String semestre, String ano, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		Iterator<FrequenciaAulaVO> i = registroAula.getFrequenciaAulaVOs().iterator();
		while (i.hasNext()) {
			FrequenciaAulaVO frequencia = (FrequenciaAulaVO) i.next();
			DiarioFrequenciaAulaVO diarioFrequencia = diarioRegistroAula.consultarObjFrequenciaAulaVO(frequencia.getMatricula().getMatricula());
			if (diarioFrequencia.getMatricula().getMatricula().equals("")) {
				diarioFrequencia.setMatricula(frequencia.getMatricula());
				diarioFrequencia.setHistorico(consultarHistoricoPorMatricula(frequencia.getMatricula().getMatricula(), semestre, ano, registroAula.getDisciplina().getCodigo(), configuracaoFinanceiroVO, usuarioVO));
			}
			if ((Boolean) UtilReflexao.invocarMetodoGet(frequencia, "presente")) {
				UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "*");
			} else {
				UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "F");
				diarioFrequencia.setTotalFaltas(diarioFrequencia.getTotalFaltas() + registroAula.getCargaHoraria());
			}
			diarioRegistroAula.adicionarObjFrequenciaAulaVOs(diarioFrequencia);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * consultarRegistroAulaEspelho()
	 */
	// public List
	// consultarRegistroAulaEspelho(List<ProfessorMinistrouAulaTurmaVO>
	// listaProfessores) throws Exception {
	// EspelhoRel.emitirRelatorio(getIdEntidade(), true);
	// validarDados();
	// List listaDiarioRelVO = new ArrayList(0);
	// for (ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO :
	// listaProfessores) {
	// List listaConsulta =
	// getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAno(getTurmaVO().getCodigo(),
	// professorMinistrouAulaTurmaVO.getDisciplina().getCodigo(),
	// getTurmaVO().getTurmaAgrupada(), getAno(), getSemestre(), false,
	// Uteis.NIVELMONTARDADOS_TODOS);
	// listaDiarioRelVO.addAll(montarListaEspelhoDiarioVO(listaConsulta,
	// professorMinistrouAulaTurmaVO));
	// }
	// return listaDiarioRelVO;
	// }
	public List<DiarioRegistroAulaVO> consultarRegistroAulaEspelho(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, TurmaVO turmaVO, Integer disciplinaVO, String semestre, String ano, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String tipoLayout, String tipoAluno, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Date dataInicioPeriodoMatricula, Date dataFimPeriodoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, Boolean apresentarDataMatriculaPeriodo) throws Exception {
		EspelhoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		validarDados(turmaVO, semestre, ano);
		List<DiarioRegistroAulaVO> listaDiarioRelVO = new ArrayList<DiarioRegistroAulaVO>(0);
		// <BY THYAGO - FILTRO DE ALUNOS PENDENTES FINANCEIRAMENTE>
		for (ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO : listaProfessores) {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaConsulta = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(turmaVO, disciplinaVO, ano, semestre, "", true, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, tipoLayout, tipoAluno, filtroAcademicoVO, mes, anoMes, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, dataInicioPeriodoMatricula, dataFimPeriodoMatricula, null, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados);
			listaDiarioRelVO.addAll(montarListaEspelhoDiarioVO(listaConsulta, professorTitularDisciplinaTurmaVO, turmaVO, disciplinaVO, apenasAlunosAtivos, configuracaoFinanceiroVO, usuarioVO, tipoLayout, ano, semestre, dataInicio, dataFim, apresentarDataMatriculaPeriodo));
		}
		return listaDiarioRelVO;
	}

	public List<DiarioRegistroAulaVO> consultarRegistroAulaEspelho(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		EspelhoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		validarDados(turmaVO, semestre, ano);
		// <BY THYAGO - FILTRO DE ALUNOS PENDENTES FINANCEIRAMENTE>
		List<MatriculaPeriodoTurmaDisciplinaVO> listaConsulta = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAno(turmaVO, disciplina, ano, semestre, true, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		return montarListaEspelhoDiarioVO(listaConsulta, turmaVO, professor, disciplina, configuracaoFinanceiroVO, usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * montarListaEspelhoDiarioVO(java.util.List)
	 */
	// public List montarListaEspelhoDiarioVO(List listaConsulta,
	// ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO) throws
	// Exception {
	public List<DiarioRegistroAulaVO> montarListaEspelhoDiarioVO(List<MatriculaPeriodoTurmaDisciplinaVO> listaConsulta, ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, TurmaVO turmaVO, Integer disciplinaVO, boolean filtroVisaoProfessor, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String tipoLayout, String ano, String semestre, Date dataInicio, Date dataFim, Boolean apresentarDataMatriculaPeriodo) throws Exception {
		List<DiarioRegistroAulaVO> listaFinal = new ArrayList<DiarioRegistroAulaVO>(0);
		DiarioRegistroAulaVO diarioRegistroAula = new DiarioRegistroAulaVO();
		int index = 1;
		Map<Integer, Boolean> mapCursoBloquearRegistroAulaAnteriorDataMatricula = new HashMap<>(0);
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = listaConsulta.iterator();
		while (i.hasNext()) {
			DiarioFrequenciaAulaVO diarioFrequenciaAula = new DiarioFrequenciaAulaVO();
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurma = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			// Coloca na lista do espelho somente as matrículas que estão
			// ativas.
			if (matriculaPeriodoTurma.getMatriculaObjetoVO().getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor()) && filtroVisaoProfessor) {
				diarioFrequenciaAula.setMatricula(matriculaPeriodoTurma.getMatriculaObjetoVO());
				diarioFrequenciaAula.setTurma(matriculaPeriodoTurma.getTurma().getIdentificadorTurma());
				diarioRegistroAula.adicionarObjFrequenciaAulaVOs(diarioFrequenciaAula);
			} else if (!filtroVisaoProfessor) {
				diarioFrequenciaAula.setMatricula(matriculaPeriodoTurma.getMatriculaObjetoVO());
				diarioFrequenciaAula.setTurma(matriculaPeriodoTurma.getTurma().getIdentificadorTurma());
				diarioRegistroAula.adicionarObjFrequenciaAulaVOs(diarioFrequenciaAula);
			}
			validarDataApresentadaMatriculaPeriodoConformeConfiguracaoAcademicaCurso(diarioFrequenciaAula, matriculaPeriodoTurma, apresentarDataMatriculaPeriodo, mapCursoBloquearRegistroAulaAnteriorDataMatricula, usuarioVO);
		}
		removerAlunosComDataMatriculaPosteriorADataAula(diarioRegistroAula);
		Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
		int x = 1;
		for(DiarioFrequenciaAulaVO diarioFrequenciaAulaVO: diarioRegistroAula.getDiarioFrequenciaVOs()) {
			diarioFrequenciaAulaVO.setOrdemLinha(x++);
		}
		if (tipoLayout.equals("EspelhoDiarioRel") || tipoLayout.equals("EspelhoDiarioRel2") || tipoLayout.equals("EspelhoDiarioRel3") || tipoLayout.equals("EspelhoDiarioNotaRel") || tipoLayout.equals("EspelhoDiarioControleNotaFrequenciaRel")) {
			diarioRegistroAula.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
			diarioRegistroAula.setTurma(turmaVO);
			diarioRegistroAula.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplinaVO, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null));
			if(Uteis.isAtributoPreenchido(turmaVO.getGradeCurricularVO().getCodigo())){
				diarioRegistroAula.getTurma().setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(turmaVO.getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			}
			diarioRegistroAula.setObjeto(diarioRegistroAula.getProfessor().getCodigo());
			diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
			if (tipoLayout.equals("EspelhoDiarioControleNotaFrequenciaRel")){				
				diarioRegistroAula.setCargaHorariaDisciplina(getFacadeFactory().getDiarioRelFacade().obterCargaHorariaDisciplina(turmaVO, disciplinaVO, usuarioVO));
				if(turmaVO.getTurmaAgrupada()){
					StringBuilder sql =  new StringBuilder("");
					sql.append(" select abreviaturaCurso, min(periodoletivo.periodoletivo) as periodoletivo, array_to_string(array_agg(distinct curso.nome order by curso.nome), ', ') as curso from turmaagrupada ");
					sql.append(" inner join turma on turma.codigo = turmaagrupada.turma ");
					sql.append(" left join curso on turma.curso = curso.codigo ");
					sql.append(" left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
					sql.append(" where turmaagrupada.turmaorigem = ").append(turmaVO.getCodigo());
					sql.append(" group by abreviaturaCurso");
					SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
					if(rs.next()){
						turmaVO.getPeridoLetivo().setPeriodoLetivo(rs.getInt("periodoLetivo"));
						if(Uteis.isAtributoPreenchido(turmaVO.getAbreviaturaCurso())){
							turmaVO.getCurso().setNome(turmaVO.getAbreviaturaCurso());							
						}else{
							turmaVO.getCurso().setNome(rs.getString("curso"));	
						}
					}
				}
			}
		} else if (tipoLayout.equals("EspelhoDiarioModRetratoRel")) {
			int indice = 1;
			diarioRegistroAula.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
			diarioRegistroAula.setTurma(turmaVO);
			diarioRegistroAula.setIndice(indice);
			diarioRegistroAula.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplinaVO, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null));
			diarioRegistroAula.setObjeto(diarioRegistroAula.getProfessor().getCodigo());
			diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
			List<HorarioTurmaDiaVO> listaHorarioTurmaDia = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorTurmaDisciplinaPeriodoProfessor(professorTitularDisciplinaTurmaVO.getTurma().getCodigo(), professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo(), 0, ano, semestre, dataInicio, dataFim, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			//Integer numTemp = 4;			
			index = 0;
			List<DiarioFrequenciaAulaVO> diarioFrequenciaAulaVOs =  new ArrayList<DiarioFrequenciaAulaVO>(0);
			for (HorarioTurmaDiaVO obj : listaHorarioTurmaDia) {
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : obj.getHorarioTurmaDiaItemVOs()) {
					for(DiarioFrequenciaAulaVO diarioFrequenciaAulaVO: diarioRegistroAula.getDiarioFrequenciaVOs()) {
						DiarioFrequenciaAulaVO diarioFrequenciaAulaUsar =  null;
						if(index > 0) {
							diarioFrequenciaAulaUsar = diarioFrequenciaAulaVO.clone();
						}else {
							diarioFrequenciaAulaUsar = diarioFrequenciaAulaVO;
						}
						diarioFrequenciaAulaUsar.setLabel1("Data");
						diarioFrequenciaAulaUsar.setLabel2(obj.getData_Apresentar());
						diarioFrequenciaAulaUsar.setLabel3(Uteis.getDiaSemana_Apresentar(horarioTurmaDiaItemVO.getData()));
						diarioFrequenciaAulaUsar.setOrdemColuna(1);
						diarioFrequenciaAulaUsar.setOrdemColuna2(index);
						diarioFrequenciaAulaVOs.add(diarioFrequenciaAulaUsar);
					}
					index++;
//					UtilReflexao.invocarMetodo(diarioRegistroAula, "setData" + index, obj.getData_Apresentar());
//					UtilReflexao.invocarMetodo(diarioRegistroAula, "setDiaSemanaData" + index, Uteis.getDiaSemana_Apresentar(obj.getData()));
//					if (index == numTemp) {
//						Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
//						listaFinal.add(diarioRegistroAula);
//						DiarioRegistroAulaVO regClone = diarioRegistroAula.clone();
//						index = 0;
//						diarioRegistroAula = new DiarioRegistroAulaVO();
//						diarioRegistroAula = regClone;
//						diarioRegistroAula.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
//						diarioRegistroAula.setIndice(++indice);
//						diarioRegistroAula.setTurma(turmaVO);
//						diarioRegistroAula.setDisciplina(regClone.getDisciplina());
//						diarioRegistroAula.setDiarioFrequenciaVOs(regClone.getDiarioFrequenciaVOs());
//						diarioRegistroAula.setObjeto(diarioRegistroAula.getProfessor().getCodigo());
//						diarioRegistroAula.setData1("");
//						diarioRegistroAula.setData2("");
//						diarioRegistroAula.setData3("");
//						diarioRegistroAula.setData4("");
//						diarioRegistroAula.setDiaSemanaData1("");
//						diarioRegistroAula.setDiaSemanaData1("");
//						diarioRegistroAula.setDiaSemanaData1("");
//						diarioRegistroAula.setDiaSemanaData1("");
//						diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
//					}
				}
			}			
			for(DiarioFrequenciaAulaVO diarioFrequenciaAulaVO: diarioRegistroAula.getDiarioFrequenciaVOs()) {
				DiarioFrequenciaAulaVO diarioFrequenciaAulaUsar =  null;
				diarioFrequenciaAulaUsar = diarioFrequenciaAulaVO.clone();				
				diarioFrequenciaAulaUsar.setLabel1("Aproveitamento");
				diarioFrequenciaAulaUsar.setOrdemColuna(2);
				diarioFrequenciaAulaUsar.setLabel2("Total Faltas");
				diarioFrequenciaAulaUsar.setLabel3("");
				diarioFrequenciaAulaUsar.setOrdemColuna2(index);
				diarioFrequenciaAulaVOs.add(diarioFrequenciaAulaUsar);
			}
			index++;
			for(DiarioFrequenciaAulaVO diarioFrequenciaAulaVO: diarioRegistroAula.getDiarioFrequenciaVOs()) {
				DiarioFrequenciaAulaVO diarioFrequenciaAulaUsar =  null;
				diarioFrequenciaAulaUsar = diarioFrequenciaAulaVO.clone();				
				diarioFrequenciaAulaUsar.setLabel1("Aproveitamento");
				diarioFrequenciaAulaUsar.setLabel2("Média");
				diarioFrequenciaAulaUsar.setLabel3("");
				diarioFrequenciaAulaUsar.setOrdemColuna(2);
				diarioFrequenciaAulaUsar.setOrdemColuna2(index);
				diarioFrequenciaAulaVOs.add(diarioFrequenciaAulaUsar);
			}
			index++;
			int diferenca = index % 7;			
			while(diferenca > 0) {
				for(DiarioFrequenciaAulaVO diarioFrequenciaAulaVO: diarioRegistroAula.getDiarioFrequenciaVOs()) {
					DiarioFrequenciaAulaVO diarioFrequenciaAulaUsar =  null;
					diarioFrequenciaAulaUsar = diarioFrequenciaAulaVO.clone();				
					diarioFrequenciaAulaUsar.setLabel1("Aproveitamento");
					diarioFrequenciaAulaUsar.setLabel2("--");
					diarioFrequenciaAulaUsar.setLabel3("--");
					diarioFrequenciaAulaUsar.setFrequencia1("--");
					diarioFrequenciaAulaUsar.setOrdemColuna(2);
					diarioFrequenciaAulaUsar.setOrdemColuna2(index);
					diarioFrequenciaAulaVOs.add(diarioFrequenciaAulaUsar);
				}
				diferenca --;
			}			
			diarioRegistroAula.setDiarioFrequenciaVOs(diarioFrequenciaAulaVOs);
			listaFinal.add(diarioRegistroAula);
			
			listaHorarioTurmaDia = null;
		} else if (tipoLayout.equals("EspelhoDiarioReposicaoRel")) {
			int indice = 1;
			diarioRegistroAula.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
			diarioRegistroAula.setTurma(turmaVO);
			diarioRegistroAula.setIndice(indice);
			diarioRegistroAula.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplinaVO, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null));
			diarioRegistroAula.setObjeto(diarioRegistroAula.getProfessor().getCodigo());
			diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
			List<HorarioTurmaDiaVO> listaHorarioTurmaDia = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorTurmaDisciplinaPeriodoProfessor(professorTitularDisciplinaTurmaVO.getTurma().getCodigo(), professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo(), 0, ano, semestre, dataInicio, dataFim, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			Integer numTemp = 4;			
			index = 0;
			for (HorarioTurmaDiaVO obj : listaHorarioTurmaDia) {
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : obj.getHorarioTurmaDiaItemVOs()) {
					index++;
					UtilReflexao.invocarMetodo(diarioRegistroAula, "setData" + index, obj.getData_Apresentar());
					UtilReflexao.invocarMetodo(diarioRegistroAula, "setDiaSemanaData" + index, Uteis.getDiaSemana_Apresentar(obj.getData()));
					if (index == numTemp) {
						Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
						listaFinal.add(diarioRegistroAula);
						DiarioRegistroAulaVO regClone = diarioRegistroAula.clone();
						index = 0;
						diarioRegistroAula = new DiarioRegistroAulaVO();
						diarioRegistroAula = regClone;
						diarioRegistroAula.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
						diarioRegistroAula.setIndice(++indice);
						diarioRegistroAula.setTurma(turmaVO);
						diarioRegistroAula.setDisciplina(regClone.getDisciplina());
						diarioRegistroAula.setDiarioFrequenciaVOs(regClone.getDiarioFrequenciaVOs());
						diarioRegistroAula.setObjeto(diarioRegistroAula.getProfessor().getCodigo());
						diarioRegistroAula.setData1("");
						diarioRegistroAula.setData2("");
						diarioRegistroAula.setData3("");
						diarioRegistroAula.setData4("");
						diarioRegistroAula.setDiaSemanaData1("");
						diarioRegistroAula.setDiaSemanaData1("");
						diarioRegistroAula.setDiaSemanaData1("");
						diarioRegistroAula.setDiaSemanaData1("");
						diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
					}
				}
			}
			if (index > 0 && index <= 4) {
				listaFinal.add(diarioRegistroAula);
			}
			listaHorarioTurmaDia = null;
		}
		
//		Collections.sort(diarioRegistroAula.getDiarioFrequenciaVOs(), new Uteis.OrdenaDiarioFrequenciaVOPorAluno());
		if (tipoLayout.equals("EspelhoDiarioRel2")) {
			List<HorarioTurmaDiaVO> horarioTurmaDiaVOs = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorTurmaPeriodoUnidadeAnoSemestre(turmaVO.getIdentificadorTurma(), dataInicio, dataFim, ano, semestre, professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo(), turmaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			if (!horarioTurmaDiaVOs.isEmpty()) {
				int cont = 1;
				DiarioRegistroAulaVO diarioRegistroAulaVOClone = diarioRegistroAula.clone();
				for (HorarioTurmaDiaVO htd : horarioTurmaDiaVOs) {
					for (HorarioTurmaDiaItemVO htdi : htd.getHorarioTurmaDiaItemVOs()) {
						UtilReflexao.invocarMetodo(diarioRegistroAulaVOClone, "setData" + cont, Uteis.getData(htd.getData()));
						cont++;
						if (cont == 5) {
							break;
						}
					}
					if (cont == 5) {
						break;
					}					

				}
				listaFinal.add(diarioRegistroAulaVOClone);
			} else {
				listaFinal.add(diarioRegistroAula);
			}
		} else {
			if (tipoLayout.equals("EspelhoDiarioModRetratoRel") || tipoLayout.equals("EspelhoDiarioReposicaoRel")) {
				Ordenacao.ordenarLista(listaFinal, "indice");
			} else {
				listaFinal.add(diarioRegistroAula);
			}
		}
		return listaFinal;
	}

	public void removerAlunosComDataMatriculaPosteriorADataAula(DiarioRegistroAulaVO diarioRegistroAula) throws Exception {
		List<DiarioFrequenciaAulaVO> listaFinal = new ArrayList<DiarioFrequenciaAulaVO>();
		Iterator<DiarioFrequenciaAulaVO> o = diarioRegistroAula.getDiarioFrequenciaVOs().iterator();
		while (o.hasNext()) {
			DiarioFrequenciaAulaVO d = (DiarioFrequenciaAulaVO) o.next();
			if (!diarioRegistroAula.getData1().equals("")) {
				Date data = Uteis.getDate(diarioRegistroAula.getData1());
				if (d.getMatricula().getData().before(data)) {
					listaFinal.add(d);
				}
			} else {
				listaFinal.add(d);
			}
		}
		diarioRegistroAula.setDiarioFrequenciaVOs(listaFinal);
	}

	public List<DiarioRegistroAulaVO> montarListaEspelhoDiarioVO(List<MatriculaPeriodoTurmaDisciplinaVO> listaConsulta, TurmaVO turmaVO, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<DiarioRegistroAulaVO> listaFinal = new ArrayList<DiarioRegistroAulaVO>(0);
		DiarioRegistroAulaVO diarioRegistroAula = new DiarioRegistroAulaVO();
		diarioRegistroAula.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(professor.intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		diarioRegistroAula.setTurma(turmaVO);
		diarioRegistroAula.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = listaConsulta.iterator();
		while (i.hasNext()) {
			DiarioFrequenciaAulaVO diarioFrequenciaAula = new DiarioFrequenciaAulaVO();
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurma = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaPeriodoTurma.getMatricula(), 0, NivelMontarDados.TODOS, usuarioVO);
			// Coloca na lista do espelho somente as matrículas que estão
			// ativas.
			if (obj.getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor())) {
				diarioFrequenciaAula.setMatricula(obj);
				diarioRegistroAula.adicionarObjFrequenciaAulaVOs(diarioFrequenciaAula);
			}
		}
		Collections.sort(diarioRegistroAula.getDiarioFrequenciaVOs(), new Uteis.OrdenaDiarioFrequenciaVOPorAluno());
		listaFinal.add(diarioRegistroAula);
		return listaFinal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * verificarNotasUtilizadasConfiguracaoAcademico(relatorio
	 * .negocio.comuns.academico.DiarioRegistroAulaVO)
	 */
	public void verificarNotasUtilizadasConfiguracaoAcademico(DiarioRegistroAulaVO diarioRegistroAulaVo) {

		for (DiarioFrequenciaAulaVO diarioFrequenciaAulaVo : diarioRegistroAulaVo.getDiarioFrequenciaVOs()) {
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota1()) {
				diarioFrequenciaAulaVo.getHistorico().setNota1(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota2()) {
				diarioFrequenciaAulaVo.getHistorico().setNota2(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota3()) {
				diarioFrequenciaAulaVo.getHistorico().setNota3(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota4()) {
				diarioFrequenciaAulaVo.getHistorico().setNota4(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota5()) {
				diarioFrequenciaAulaVo.getHistorico().setNota5(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota6()) {
				diarioFrequenciaAulaVo.getHistorico().setNota6(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota7()) {
				diarioFrequenciaAulaVo.getHistorico().setNota7(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota8()) {
				diarioFrequenciaAulaVo.getHistorico().setNota8(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota9()) {
				diarioFrequenciaAulaVo.getHistorico().setNota9(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota10()) {
				diarioFrequenciaAulaVo.getHistorico().setNota10(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota11()) {
				diarioFrequenciaAulaVo.getHistorico().setNota11(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota12()) {
				diarioFrequenciaAulaVo.getHistorico().setNota12(null);
			}
			if (!diarioRegistroAulaVo.getConfiguracaoAcademico().getUtilizarNota13()) {
				diarioFrequenciaAulaVo.getHistorico().setNota13(null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * consultarHistoricoPorMatricula(java.lang.String, java.lang.Integer)
	 */
	public HistoricoVO consultarHistoricoPorMatricula(String matricula, String semestre, String ano, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		HistoricoVO historico = getFacadeFactory().getHistoricoFacade().consultarPorMatriula_Disciplina_Semestre_Ano(matricula, disciplina, semestre, ano, false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
		return historico;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.EspelhoRelInterfaceFacade#
	 * consultarConfiguracaoAcademico(java.lang.Integer)
	 */
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademico(Integer codigo) throws Exception {
		ConfiguracaoAcademicoVO obj = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigo, null);
		return obj;
	}

	public static String getDesignIReportRelatorio(String tipoLayout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + tipoLayout + ".jrxml");
	}

	public static String getDesignIReportRelatorioVerso() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVerso() + ".jrxml");
	}

	public static String getDesignIReportRelatorioEspelhoDiario(String tipoLayout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + tipoLayout + ".jrxml");
	}
	
	public static String getDesignIReportRelatorioEspelhoDiarioComBackground() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "EspelhoDiarioControleNotaFrequenciaComBackgroundRel.jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getCaminhoBaseRelatorioVerso() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("EspelhoRel");
	}

	public static String getIdEntidadeVerso() {
		return ("EspelhoRelVerso");
	}

	public static String getDesignIReportRelatorioEspelhoDiarioVersoPos() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "EspelhoDiarioRelVersoPos.jrxml");
	}

	public static String getDesignIReportRelatorioEspelhoDiarioVersoGraduacao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "EspelhoDiarioRelVersoGraduacao.jrxml");
	}

	public static String getDesignIReportRelatorioEspelhoDiarioVersoRel3() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "EspelhoDiarioVersoRel3.jrxml");
	}
	
	private void validarDataApresentadaMatriculaPeriodoConformeConfiguracaoAcademicaCurso(DiarioFrequenciaAulaVO diarioFrequenciaAulaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean apresentarDataMatriculaPeriodo, Map<Integer, Boolean> mapCursoBloquearRegistroAulaAnteriorDataMatricula, UsuarioVO usuarioVO) throws Exception {
		if (apresentarDataMatriculaPeriodo) {
			diarioFrequenciaAulaVO.setDataMatriculaPeriodo(Uteis.getData(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getData()));
			return;
		}
		ConfiguracaoAcademicoVO configuracaoAcademicoVO = null;
		Boolean bloquearRegistroAulaAnteriorDataMatricula = null;
		TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		if (mapCursoBloquearRegistroAulaAnteriorDataMatricula.containsKey(turmaVO.getCurso().getCodigo())) {
			bloquearRegistroAulaAnteriorDataMatricula = mapCursoBloquearRegistroAulaAnteriorDataMatricula.get(turmaVO.getCurso().getCodigo());
		} else {
			configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoCurso(turmaVO.getCurso().getCodigo(), usuarioVO);
			if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {
				mapCursoBloquearRegistroAulaAnteriorDataMatricula.put(turmaVO.getCurso().getCodigo(), configuracaoAcademicoVO.getBloquearRegistroAulaAnteriorDataMatricula());
				bloquearRegistroAulaAnteriorDataMatricula = mapCursoBloquearRegistroAulaAnteriorDataMatricula.get(turmaVO.getCurso().getCodigo());
			}
		}
		if (bloquearRegistroAulaAnteriorDataMatricula != null && bloquearRegistroAulaAnteriorDataMatricula) {
			diarioFrequenciaAulaVO.setDataMatriculaPeriodo(Uteis.getData(matriculaPeriodoTurmaDisciplinaVO.getDataRegistroHistorico()));
		} else {
			diarioFrequenciaAulaVO.setDataMatriculaPeriodo(Uteis.getData(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getData()));
		}
	}
}
