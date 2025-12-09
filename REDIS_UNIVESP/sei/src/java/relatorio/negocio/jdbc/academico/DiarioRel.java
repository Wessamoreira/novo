package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import relatorio.negocio.comuns.academico.DiarioFrequenciaAulaVO;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.interfaces.academico.DiarioRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DiarioRel extends SuperRelatorio implements DiarioRelInterfaceFacade {

	private static final long serialVersionUID = -2456017780674647702L;
	public HashMap<String, String> hashSituacao = new HashMap<String, String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#validarDados()
	 */
	public void validarDados(TurmaVO turmaVO, String semestre, String ano) throws Exception {
		if (turmaVO == null || turmaVO.getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo TURMA (Diário) deve ser informado.");
		}
		if (turmaVO.getSemestral() && (turmaVO.getCurso().getNivelEducacional().equals("PR") && !turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo())) {
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

	public void validarDadosRelatorio(TurmaVO turmaVO, String semestre, String ano, Integer disciplina, UnidadeEnsinoVO unidadeEnsinoVO, boolean possuiDiversidadeConfiguracaoAcademico, Integer configuracaoAcademico) throws Exception {
		if (unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_unidadeEnsino"));
		}
		if (turmaVO == null || turmaVO.getIdentificadorTurma().equals("")) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_identificadorTurma"));
		}
		if (turmaVO.getSemestral() && !turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo()) {
			if (semestre == null || semestre.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_semestre"));
			}
			if (ano == null || ano.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_ano"));
			}
		}
		if (turmaVO.getAnual()) {
			if (ano.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_ano"));
			}
		}
		if (possuiDiversidadeConfiguracaoAcademico) {
			if (!Uteis.isAtributoPreenchido(configuracaoAcademico)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_configuracaoAcademico"));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * consultarRegistroAula()
	 */
	public List<DiarioRegistroAulaVO> consultarRegistroAula(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, Integer disciplina, TurmaVO turmaVO, String semestre, String ano, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoDiario, String tipoAluno, String mes, String anoMes, Integer configuracaoAcademico, Boolean apresentarAulasNaoRegistradas, Boolean apresentarSituacaoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim, List<String> filtroNotasPorBimestre) throws Exception {
		validarDados(turmaVO, semestre, ano);
		if (turmaVO.getIntegral()) {
			semestre = "";
			ano = "";
		}
		List<RegistroAulaVO> listaConsulta;
		ProfessorTitularDisciplinaTurmaVO professorTitular = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().executarObterDadosProfessorTitularDisciplinaTurma(listaProfessores);
		if (tipoDiario.equals("") || tipoDiario.equals("normal")) {
			listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(turmaVO, semestre, ano, null, disciplina, 0, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroVisaoProfessor, filtroTipoCursoAluno, tipoLayout, tipoAluno, false, NivelMontarDados.TODOS, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), mes, anoMes, configuracaoAcademico, usuarioVO, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados, tipoFiltroPeriodo, dataInicio, dataFim);
		} else {
			listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaEReposicao(turmaVO, semestre, ano, null, disciplina, 0, filtroVisaoProfessor, filtroTipoCursoAluno, false, NivelMontarDados.TODOS, null, mes, anoMes, configuracaoAcademico, usuarioVO, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados, tipoFiltroPeriodo, dataInicio, dataFim);
		}
		if (apresentarAulasNaoRegistradas) {
			executarGeracaoRegistroAulaProgramadaNaoRegistrada(listaConsulta, turmaVO, disciplina, ano, semestre, mes, anoMes, usuarioVO, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, tipoAluno, null, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados, tipoFiltroPeriodo, dataInicio, dataFim);
		}
		for(RegistroAulaVO registroAulaVO: listaConsulta) {
			Ordenacao.ordenarLista(registroAulaVO.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
		}
		if (tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequencia") || tipoLayout.equals("DiarioRelNota") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
			return montarDiarioRegistroAula(listaConsulta, turmaVO, semestre, ano, tipoLayout, professorTitular, usuarioVO, apresentarSituacaoMatricula, filtroNotasPorBimestre);
		}
		return montarListaDiarioVO(listaConsulta, null, turmaVO, semestre, ano, tipoLayout, professorTitular, usuarioVO, apresentarSituacaoMatricula);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * consultarRegistroAula()
	 */
	public List<DiarioRegistroAulaVO> consultarRegistroAulaVisaoAdministrativa(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, Integer disciplina, TurmaVO turmaVO, String semestre, String ano, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoDiario, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, Boolean apresentarAulasNaoRegistradas, Boolean apresentarSituacaoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim, List<String> filtroNotasPorBimestre) throws Exception {
		validarDados(turmaVO, semestre, ano);
		if (turmaVO.getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
			ano = "";
			semestre = "";
		}
		List<RegistroAulaVO> listaConsulta;
		ProfessorTitularDisciplinaTurmaVO professorTitular = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().executarObterDadosProfessorTitularDisciplinaTurma(listaProfessores);
		if ((tipoDiario.equals("") || tipoDiario.equals("normal")) && !tipoLayout.equals("DiarioReposicaoRel")) {
			listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaVisaoAdministrativa(turmaVO, semestre, ano, null, disciplina, 0, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroVisaoProfessor, filtroTipoCursoAluno, tipoLayout, tipoAluno, false, NivelMontarDados.TODOS, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), filtroAcademicoVO, mes, anoMes, configuracaoAcademico, null, trazerAlunoTransferencia, tipoFiltroPeriodo, dataInicio, dataFim);
		} else {
			listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaEReposicao(turmaVO, semestre, ano, listaProfessores, disciplina, 0, filtroVisaoProfessor, filtroTipoCursoAluno, false, NivelMontarDados.TODOS, filtroAcademicoVO, mes, anoMes, configuracaoAcademico, null, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados, tipoFiltroPeriodo, dataInicio, dataFim);
		}
		if (apresentarAulasNaoRegistradas) {
			executarGeracaoRegistroAulaProgramadaNaoRegistrada(listaConsulta, turmaVO, disciplina, ano, semestre, mes, anoMes, usuarioVO, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, tipoAluno, filtroAcademicoVO, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados, tipoFiltroPeriodo, dataInicio, dataFim);
		}
		if (tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequencia") || tipoLayout.equals("DiarioRelNota") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
			return montarDiarioRegistroAula(listaConsulta, turmaVO, semestre, ano, tipoLayout, professorTitular, usuarioVO, apresentarSituacaoMatricula, filtroNotasPorBimestre);
		}
		return montarListaDiarioVO(listaConsulta, null, turmaVO, semestre, ano, tipoLayout, professorTitular, usuarioVO, apresentarSituacaoMatricula);
	}

	public List<RegistroAulaVO> consultarRegistroAulaVerso(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, Integer disciplina, TurmaVO turmaVO, String semestre, String ano, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, Boolean apresentarAulasNaoRegistradas, Boolean apresentarSituacaoMatricula, String tipoLayout, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim , boolean apresentarVersoAgrupadoPorData) throws Exception {
		DiarioRel.emitirRelatorio(getIdEntidade(), false, usuarioVO);
		validarDados(turmaVO, semestre, ano);
		if (turmaVO.getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
			semestre = "";
			ano = "";
		}
		List<RegistroAulaVO> listaConsulta = null;
		ProfessorTitularDisciplinaTurmaVO professorTitular = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().executarObterDadosProfessorTitularDisciplinaTurma(listaProfessores);
		if (filtroVisaoProfessor) {
			listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVerso(turmaVO, semestre, ano, null, disciplina, 0, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroVisaoProfessor, filtroTipoCursoAluno, tipoAluno, false, NivelMontarDados.TODOS, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), mes, anoMes, configuracaoAcademico, null, tipoLayout, trazerAlunoTransferencia, tipoFiltroPeriodo, dataInicio, dataFim);
		} else {
			listaConsulta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVersoVisaoAdministrativa(turmaVO, semestre, ano, null, disciplina, 0, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroVisaoProfessor, filtroTipoCursoAluno, tipoAluno, false, NivelMontarDados.TODOS, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), null, filtroRelatorioAcademicoVO, mes, anoMes, configuracaoAcademico, trazerAlunoTransferencia, tipoFiltroPeriodo, dataInicio, dataFim);
		}
		if (apresentarAulasNaoRegistradas) {
			executarGeracaoRegistroAulaProgramadaNaoRegistrada(listaConsulta, turmaVO, disciplina, ano, semestre, mes, anoMes, usuarioVO, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, tipoAluno, filtroRelatorioAcademicoVO, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados, tipoFiltroPeriodo, dataInicio, dataFim);
		}
		return montarListaDiarioVersoVO(listaConsulta, professorTitular, turmaVO, semestre, ano ,apresentarVersoAgrupadoPorData);
	}

	public List<RegistroAulaVO> montarListaDiarioVersoVO(List<RegistroAulaVO> listaConsulta, ProfessorTitularDisciplinaTurmaVO professorTitular, TurmaVO turmaVO, String semestre, String ano , Boolean apresentarVersoAgrupadoPorData) throws Exception {
		List<RegistroAulaVO> listaFinal = new ArrayList<RegistroAulaVO>(0);
		
		int index = 1;
		String nomeDisciplinaApresentarRelatorio = "";
//		listaFinal = listaConsulta;
		boolean possuiAluno = false;
		for (RegistroAulaVO r : listaConsulta) {
			r.setProfessor(professorTitular.getProfessor().getPessoa());
			r.setTurma(turmaVO);			
			if(turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario() && !r.getAtividadeComplementar()){
				r.setCargaHoraria(60);
			}			
			if(!r.getAtividadeComplementar()) {
				r.setCargaHorariaStr(Uteis.converterMinutosEmHorasStr(r.getCargaHoraria().doubleValue()));
			}else {
				String cargaHoraria = Uteis.converterMinutosEmHorasStr(r.getCargaHoraria().doubleValue() * 60);
				r.setCargaHorariaStr(cargaHoraria);
			}
			nomeDisciplinaApresentarRelatorio = r.getDisciplina().getNome();
			possuiAluno = true;
			boolean existeIgual = false;
			for(RegistroAulaVO registroAulaVO: listaFinal){				
								
				if (apresentarVersoAgrupadoPorData) {
					if(r.getData_Apresentar().equals(registroAulaVO.getData_Apresentar()) && r.getConteudo().equalsIgnoreCase(registroAulaVO.getConteudo()) && !r.getAtividadeComplementar()){
						registroAulaVO.setCargaHoraria(registroAulaVO.getCargaHoraria()+r.getCargaHoraria());
						registroAulaVO.setCargaHorariaStr(Uteis.converterMinutosEmHorasStr(registroAulaVO.getCargaHoraria().doubleValue()));
						existeIgual = true;
						break;
					}
				}
				
			}
			if(!existeIgual){
				listaFinal.add(r);
			}
		}
		if (!possuiAluno) {
			listaFinal = new ArrayList<RegistroAulaVO>(0);
		}
		if (listaFinal.size() > 0) {
			int valorMaximo = 24;
			int cont = listaFinal.size();
			while (valorMaximo - cont < 0) {
				valorMaximo = valorMaximo + 24;
			}
			for (cont = listaFinal.size(); cont < valorMaximo; cont++) {
				RegistroAulaVO registroAula = new RegistroAulaVO();
				registroAula.setData(null);
			//	registroAula.setCargaHoraria(null);
				registroAula.setTurma(turmaVO);
				registroAula.setProfessor(professorTitular.getProfessor().getPessoa());
				registroAula.getDisciplina().setNome(nomeDisciplinaApresentarRelatorio);

				listaFinal.add(registroAula);
			}
			if (index > 1 && index < 24) {
				// Ordenacao.ordenarLista(listaFinal, "ordenacao");
			}
		}
		return listaFinal;
	}

	@Override
	public Integer obterCargaHorariaDisciplina(TurmaVO turmaVO, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurmaEDisciplinaTrazendoSala(turmaVO.getCodigo(), disciplina, "", "", false);
		if(Uteis.isAtributoPreenchido(horarioTurmaDisciplinaProgramadaVOs)){
			return horarioTurmaDisciplinaProgramadaVOs.get(0).getChDisciplina();
		}
		return 0;
//		TurmaDisciplinaVO turmaDisciplinaVO = null;
//		if (turmaVO.getTurmaDisciplinaVOs().isEmpty()) {
//			turmaDisciplinaVO = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(turmaVO.getCodigo(), disciplina, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
//		} else {
//			for (TurmaDisciplinaVO turmaDisciplina2 : turmaVO.getTurmaDisciplinaVOs()) {
//				if (turmaDisciplina2 != null && turmaDisciplina2.getDisciplina().getCodigo().equals(disciplina)) {
//					turmaDisciplinaVO = turmaDisciplina2;
//				}
//			}
//		}
//		if (Uteis.isAtributoPreenchido(turmaDisciplinaVO)) {
//			if (Uteis.isAtributoPreenchido(turmaDisciplinaVO.getGradeDisciplinaVO())) {
//				if (!turmaDisciplinaVO.getGradeDisciplinaVO().getCargaHoraria().equals(0)) {
//					return turmaDisciplinaVO.getGradeDisciplinaVO().getCargaHoraria();
//				}
//				return getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaPorChavePrimaria(turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo());
//			} else if (Uteis.isAtributoPreenchido(turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
//				if (!turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria().equals(0)) {
//					return turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
//				}
//				return getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuarioVO).getCargaHoraria();
//			}
//		}
//		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * montarListaDiarioVO (java.util.List)
	 */

	public List<DiarioRegistroAulaVO> montarListaDiarioVO(List<RegistroAulaVO> listaConsulta, Integer codigoPessoaDoProfessor, TurmaVO turmaVO, String semestre, String ano, String tipoLayout, ProfessorTitularDisciplinaTurmaVO professorTitular, UsuarioVO usuarioVO, Boolean apresentarSituacaoMatricula) throws Exception {
		List<DiarioRegistroAulaVO> listaFinal = new ArrayList<DiarioRegistroAulaVO>(0);
		hashSituacao = new HashMap<String, String>();
		int index = 1;
		int qtdeObjeto = 1;
		DiarioRegistroAulaVO diarioRegistroAula = new DiarioRegistroAulaVO();
		if (tipoLayout.equals("DiarioModRetratoRel")) {
			for (RegistroAulaVO registroAula : listaConsulta) {
				registroAula.setTurma(turmaVO);
				registroAula.setProfessor(professorTitular.getProfessor().getPessoa());
				try {									
					String chAula = " 1,0 h";
					if (turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()) {
						chAula = " 1,0 h";
					} else {
						chAula = registroAula.getCargaHoraria().toString();						
					}
					String dataApresentar = (String) UtilReflexao.invocarMetodoGet(registroAula, "data_Apresentar");
					UtilReflexao.invocarMetodo(diarioRegistroAula, "setData" + index, dataApresentar + " " + "-" + " " + chAula);
					montarListaFrequencia(registroAula, diarioRegistroAula, index, turmaVO, semestre, ano, tipoLayout, usuarioVO);
					if (index == 1) {
						diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
						diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						if (!registroAula.getCodigo().equals(0)) {
							diarioRegistroAula.setObjeto(registroAula.getCodigo());
						} else {
							diarioRegistroAula.setObjeto(qtdeObjeto);
						}
						diarioRegistroAula.setProfessor(registroAula.getProfessor());

						diarioRegistroAula.setDisciplina(registroAula.getDisciplina());
						diarioRegistroAula.setTurma(turmaVO);
						diarioRegistroAula.getTurma().setIdentificadorTurma(turmaVO.getIdentificadorTurma());

						// getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(professor,
						// true, ordenarPor)
						Integer codigoConfiguracao = registroAula.getTurma().getCurso().getConfiguracaoAcademico().getCodigo();
						if (!registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo().equals(0)) {
							codigoConfiguracao = registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo();
						}else if (!registroAula.getFrequenciaAulaVOs().isEmpty() && Uteis.isAtributoPreenchido(registroAula.getFrequenciaAulaVOs().get(0).getHistoricoVO())
								&& Uteis.isAtributoPreenchido(registroAula.getFrequenciaAulaVOs().get(0).getHistoricoVO().getConfiguracaoAcademico())) {
							codigoConfiguracao = registroAula.getFrequenciaAulaVOs().get(0).getHistoricoVO().getConfiguracaoAcademico().getCodigo();
						}
						diarioRegistroAula.setConfiguracaoAcademico(consultarConfiguracaoAcademico(codigoConfiguracao));
						verificarNotasUtilizadasConfiguracaoAcademico(diarioRegistroAula);
					} else if (index == 4) {
						Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
						diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						if (!Uteis.isAtributoPreenchido(diarioRegistroAula.getCargaHorariaDisciplina()) && "DiarioModRetratoRel".equals(tipoLayout)) {
							diarioRegistroAula.setCargaHorariaDisciplina(obterCargaHorariaDisciplina(turmaVO, registroAula.getDisciplina().getCodigo(), usuarioVO));
						}
						if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
							diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);							
							listaFinal.add(diarioRegistroAula);					
						}
						index = 0;
						qtdeObjeto++;
						diarioRegistroAula = new DiarioRegistroAulaVO();
					}
					index++;
				} catch (Exception e) {
					throw e;
				}
			}
			if (index > 1 && index < 5) {
				Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
				diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
				diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
				if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
					diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);					
					listaFinal.add(diarioRegistroAula);					
				}
			}
		} else if (tipoLayout.equals("DiarioReposicaoRel")) {

			for (RegistroAulaVO registroAula : listaConsulta) {
				try {
					String chAula = " - 1,0 h";

					// cargaHorariaDada +=
					// registroAula.getCargaHoraria().doubleValue();
					// String chAula =
					// Uteis.converterMinutosEmHorasStr(registroAula.getCargaHoraria().doubleValue());
					// if (!chAula.equals("")) {
					// if (chAula.length() == 1) {
					// chAula = "0" + chAula;
					// }
					// chAula = " - " + chAula + " h";
					// }
					String dataApresentar = (String) UtilReflexao.invocarMetodoGet(registroAula, "data_Apresentar");
					UtilReflexao.invocarMetodo(diarioRegistroAula, "setData" + index, dataApresentar + chAula);
					montarListaFrequencia(registroAula, diarioRegistroAula, index, turmaVO, semestre, ano, tipoLayout, usuarioVO);
					if (index == 1) {
						diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
						diarioRegistroAula.setCargaHorariaStr(Uteis.converterMinutosEmHorasStr(registroAula.getCargaHoraria().doubleValue()));
						diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						diarioRegistroAula.setObjeto(registroAula.getCodigo());
						diarioRegistroAula.setProfessor(registroAula.getProfessor());

						diarioRegistroAula.setDisciplina(registroAula.getDisciplina());
						diarioRegistroAula.setTurma(turmaVO);
						diarioRegistroAula.getTurma().setIdentificadorTurma(turmaVO.getIdentificadorTurma());

						// getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(professor,
						// true, ordenarPor)
						Integer codigoConfiguracao = registroAula.getTurma().getCurso().getConfiguracaoAcademico().getCodigo();
						if (!registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo().equals(0)) {
							codigoConfiguracao = registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo();
						}
						diarioRegistroAula.setConfiguracaoAcademico(consultarConfiguracaoAcademico(codigoConfiguracao));
						verificarNotasUtilizadasConfiguracaoAcademico(diarioRegistroAula);
					} else if (index == 4) {
						Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
						diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
							diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);							
							listaFinal.add(diarioRegistroAula);					
						}
						index = 0;
						diarioRegistroAula = new DiarioRegistroAulaVO();
					}
					index++;
				} catch (Exception e) {
					throw e;
				}
			}
			if (index > 1 && index < 5) {
				Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
				diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
				diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
				if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
					diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);					
					listaFinal.add(diarioRegistroAula);					
				}
			}
		} else if (tipoLayout.equals("DiarioMesMesRel")) {
			Integer mesAula = 0;
			Integer anoAula = 0;
			for (RegistroAulaVO registroAula : listaConsulta) {
				try {
					String newline = System.getProperty("line.separator");
					String chAula = " 1h";
					if (turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()) {
						chAula = " 1h";
					} else {
						if (registroAula.getAtividadeComplementar()) {
							chAula = newline + registroAula.getCargaHoraria() + "h";
						} else {
							 chAula = newline + registroAula.getCargaHoraria();
						}
					}
//					String chAula = String.valueOf(Uteis.arredondar(registroAula.getCargaHoraria().doubleValue() / 60, 2, 0));
//					if (!chAula.equals("")) {
//						if (chAula.length() == 1) {
//							chAula = "0" + chAula;
//						}
//						chAula = " - " + chAula + "h";
//					}

					if (Uteis.getMesData(registroAula.getData()) != mesAula || Uteis.getAnoData(registroAula.getData()) != anoAula) {
						if (mesAula != 0 && index <= 26) {
							Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
							diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
							diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
							if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
								diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);								
								listaFinal.add(diarioRegistroAula);					
							}
							index = 0;
							diarioRegistroAula = new DiarioRegistroAulaVO();
						}
						mesAula = Uteis.getMesData(registroAula.getData());
						anoAula = Uteis.getAnoData(registroAula.getData());
						index = 1;
					}

					String dataApresentar = Uteis.getDiaMesData(registroAula.getData())+""; //(String) UtilReflexao.invocarMetodoGet(registroAula, "data_Apresentar");
					UtilReflexao.invocarMetodo(diarioRegistroAula, "setData" + index, dataApresentar + chAula);
					diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);					
					montarListaFrequencia(registroAula, diarioRegistroAula, index, turmaVO, semestre, ano, tipoLayout, usuarioVO);
					if (index == 1) {
						String agrupador = String.valueOf(mesAula) + String.valueOf(anoAula);
						diarioRegistroAula.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
						diarioRegistroAula.setCargaHoraria(Uteis.arredondar(registroAula.getGradeDisciplinaVO().getCargaHoraria().doubleValue() / 60, 2, 0));
						diarioRegistroAula.setObjeto(Integer.parseInt(agrupador));
						diarioRegistroAula.setProfessor(professorTitular.getProfessor().getPessoa());
						diarioRegistroAula.setDisciplina(registroAula.getDisciplina());
						diarioRegistroAula.setTurma(turmaVO);
						diarioRegistroAula.getTurma().setIdentificadorTurma(turmaVO.getIdentificadorTurma());
						diarioRegistroAula.setMesCorrespondente(MesAnoEnum.getEnum(mesAula + "").getMes()+"/"+anoAula);

						List<Integer> configuracoes = null;
						Integer codigoConfiguracao = registroAula.getTurma().getCurso().getConfiguracaoAcademico().getCodigo();
						if (!registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo().equals(0)) {
							codigoConfiguracao = registroAula.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo();
						}
						if (codigoConfiguracao != 0) {
							diarioRegistroAula.setConfiguracaoAcademico(consultarConfiguracaoAcademico(codigoConfiguracao));
						} else {
							configuracoes = registroAula.getTurma().getTurmaAgrupadaVOs().stream()
									.map(TurmaAgrupadaVO::getTurma)
									.map(TurmaVO::getCurso)
									.map(CursoVO::getConfiguracaoAcademico)
									.map(ConfiguracaoAcademicoVO::getCodigo).collect(Collectors.toList());
							for (Integer conf : configuracoes) {
								if (conf != 0) {
									diarioRegistroAula.setConfiguracaoAcademico(consultarConfiguracaoAcademico(conf));
									break;
								}
							}
						}
						verificarNotasUtilizadasConfiguracaoAcademico(diarioRegistroAula);
					} else if (index == 26) {
						Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
						diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
						if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
							diarioRegistroAula.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);							
							listaFinal.add(diarioRegistroAula);					
						}
						index = 0;
						diarioRegistroAula = new DiarioRegistroAulaVO();
					}
					index++;
				} catch (Exception e) {
					throw e;
				}
			}
			if (mesAula != 0 && index >= 1 && index <= 26) {
				Ordenacao.ordenarLista(diarioRegistroAula.getDiarioFrequenciaVOs(), "ordenacao");
				diarioRegistroAula.setCargaHorariaStr(obterTotalCargaHorariaRegistroAulaStr(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
				diarioRegistroAula.setCargaHoraria(obterTotalCargaHorariaRegistroAula(listaConsulta, turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()));
				if(!diarioRegistroAula.getDiarioFrequenciaVOs().isEmpty()) {
					listaFinal.add(diarioRegistroAula);					
				}
			}
		}
		String situacaoApresentar = "";
		if (!hashSituacao.isEmpty()) {
			situacaoApresentar += "Situação = ";
			Set<String> chaves = hashSituacao.keySet();
			for (String chave : chaves) {
				String situacao = (String)hashSituacao.get(chave);
				situacaoApresentar += situacao;
			}
		}
		if (!listaFinal.isEmpty()) {
			Iterator i = listaFinal.iterator();
			while (i.hasNext()) {
				DiarioRegistroAulaVO dir = (DiarioRegistroAulaVO)i.next();
				dir.setSituacaoApresentar(situacaoApresentar);
			}
		}
		return listaFinal;
	}

	public Double obterTotalCargaHorariaRegistroAula(List<RegistroAulaVO> listaRegistroAula, boolean considerarHoraAulaSessentaMinutosGeracaoDiario) {
		Double totalCargaHoraria = 0.0;
		for (RegistroAulaVO registroAulaVO : listaRegistroAula) {
			if (considerarHoraAulaSessentaMinutosGeracaoDiario) {
				totalCargaHoraria += 60;
			} else {
				if (registroAulaVO.getAtividadeComplementar()) {
					Double cargaHorariaAtividadeComplementar;
					cargaHorariaAtividadeComplementar = Uteis.converterHorasEmMinutos(registroAulaVO.getCargaHoraria().doubleValue());
					totalCargaHoraria += cargaHorariaAtividadeComplementar;
				} else {
					totalCargaHoraria += registroAulaVO.getCargaHoraria();
				}
			}
		}
		return Uteis.converterMinutosEmHoras(totalCargaHoraria);
	}

	public String obterTotalCargaHorariaRegistroAulaStr(List<RegistroAulaVO> listaRegistroAula, boolean considerarHoraAulaSessentaMinutosGeracaoDiario) {
		Double totalCargaHoraria = 0.0;
		for (RegistroAulaVO registroAulaVO : listaRegistroAula) {
			if (considerarHoraAulaSessentaMinutosGeracaoDiario) {
				totalCargaHoraria += 60;
			} else {
				if (registroAulaVO.getAtividadeComplementar()) {
					Double cargaHorariaAtividadeComplementar;
					cargaHorariaAtividadeComplementar = Uteis.converterHorasEmMinutos(registroAulaVO.getCargaHoraria().doubleValue());
					totalCargaHoraria += cargaHorariaAtividadeComplementar;
				} else {
					totalCargaHoraria += registroAulaVO.getCargaHoraria();
				}
			}
		}
		return Uteis.converterMinutosEmHorasStr(totalCargaHoraria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * montarListaFrequencia(negocio.comuns.academico. RegistroAulaVO,
	 * relatorio.negocio.comuns.academico.DiarioRegistroAulaVO, int)
	 */
	public void montarListaFrequencia(RegistroAulaVO registroAula, DiarioRegistroAulaVO diarioRegistroAula, int index, TurmaVO turmaVO, String semestre, String ano, String tipoLayout, UsuarioVO usuarioVO) throws Exception {
		int x = 1;		
		for (FrequenciaAulaVO frequencia : registroAula.getFrequenciaAulaVOs()) {
			if (!Uteis.isAtributoPreenchido(frequencia.getMatriculaPeriodoTurmaDisciplina())) {
				frequencia.setMatriculaPeriodoTurmaDisciplina(frequencia.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(frequencia.getMatriculaPeriodoTurmaDisciplina())) {
				/**
				 * Verificando se a matricula está registrada neste
				 * periodo/turma/disciplina, pois como existe o conceito de
				 * turma agrupada, podemos estar processando um registro de aula
				 * de uma turma agrupada X, para emitir o diario de uma turma A.
				 * Logo, no registro de aula da turma X temos alunos de outras
				 * turmas (que nao estao matriculados na turma A) e que portanto
				 * nao devem ser listados no diario.
				 */
				DiarioFrequenciaAulaVO diarioFrequencia = diarioRegistroAula.consultarObjFrequenciaAulaVO(frequencia.getMatricula().getMatricula());
				if (diarioFrequencia.getMatricula().getMatricula().equals("")) {
					diarioFrequencia.setMatricula(frequencia.getMatricula());
					diarioFrequencia.setHistorico(frequencia.getHistoricoVO());
				}
				if (diarioFrequencia.getHistorico().getCursando()) {
					if (!diarioFrequencia.getHistorico().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FO") && !diarioFrequencia.getHistorico().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI") && !diarioFrequencia.getHistorico().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("AT")) {
						diarioFrequencia.getHistorico().setSituacao(diarioFrequencia.getHistorico().getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
					}
				}
				diarioFrequencia.setTurma(frequencia.getTurma());
				Boolean presente = (Boolean) UtilReflexao.invocarMetodoGet(frequencia, "presente");
				Boolean abonado = (Boolean) UtilReflexao.invocarMetodoGet(frequencia, "abonado");
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO);
				if (frequencia.getHistoricoVO().getSituacao().equals(SituacaoHistorico.TRANCAMENTO.getValor())) {
					Date dataTrancamento = getFacadeFactory().getTrancamentoFacade().consultarDataTrancamentoPorMatricula(frequencia.getMatricula().getMatricula(), usuarioVO, false);
					if (Uteis.getDataJDBC(dataTrancamento).after(Uteis.getDataJDBC(registroAula.getData()))) {
						if (presente != null && presente) {
							UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "*");
						} else if (abonado) {
							if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getSiglaAbonoFalta())) {
								UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, configuracaoGeralSistemaVO.getSiglaAbonoFalta());	
							} else {
								UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "Abonado");								
							}
						} else if (presente != null && !presente && !abonado) {
							UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "F");
							diarioFrequencia.setTotalFaltas(diarioFrequencia.getTotalFaltas() + 1);
							diarioFrequencia.setTotalFaltasStr(diarioFrequencia.getTotalFaltas().toString());
						} else {
							UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "");
						}
					} else {
						UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "");
					}
				} else {
					if (registroAula.getIsAulaNaoRegistrada()) {
						UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "");
					} else if (presente != null && presente) {
						UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "*");
					} else if (abonado) {
						if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getSiglaAbonoFalta())) {
							UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, configuracaoGeralSistemaVO.getSiglaAbonoFalta());	
						} else {
							UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "Abonado");							
						}
					} else if (presente != null && !presente && !abonado) {
						UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "F");
						diarioFrequencia.setTotalFaltas(diarioFrequencia.getTotalFaltas() + 1);
						diarioFrequencia.setTotalFaltasStr(diarioFrequencia.getTotalFaltas().toString());
					} else {
						UtilReflexao.invocarMetodo(diarioFrequencia, "setFrequencia" + index, "");
					}
				}
				if (!frequencia.getFaltasGeral().equals(0)) {
					diarioFrequencia.setFaltasGeralStr(frequencia.getFaltasGeral().toString());
				} else {
					diarioFrequencia.setFaltasGeralStr("0");
				}
				if (x == registroAula.getFrequenciaAulaVOs().size()) {
					diarioFrequencia.setTotalFaltasStr(diarioFrequencia.getTotalFaltas().toString());
				}
				
				if ((frequencia.getHistoricoVO().getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula() 
						&& frequencia.getHistoricoVO().getDataRegistro().before(registroAula.getData())) || (!frequencia.getHistoricoVO().getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula()) 
					|| (!registroAula.getTurma().getCurso().getNivelEducacional().equals(TipoNivelEducacional.POS_GRADUACAO.getValor()) && !registroAula.getTurma().getCurso().getNivelEducacional().equals(TipoNivelEducacional.EXTENSAO.getValor()) && !registroAula.getTurma().getCurso().getNivelEducacional().equals(TipoNivelEducacional.SEQUENCIAL.getValor()))) {
					// Controle feito para pós-graduação
					diarioRegistroAula.adicionarObjFrequenciaAulaVOs(diarioFrequencia);
				}
				if (diarioRegistroAula.getApresentarSituacaoMatricula()) {
					diarioFrequencia.setApresentarSituacaoMatricula(Boolean.TRUE);
					diarioFrequencia.setSituacaoMatriculaPeriodo(frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo());
				}				
				if (tipoLayout.equals("DiarioMesMesRel") || tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelNota") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
					hashSituacao.put(frequencia.getHistoricoVO().getSituacao(), "(" + frequencia.getHistoricoVO().getSituacao() + " = " + frequencia.getHistoricoVO().getSituacao_Apresentar() + ") ");				
				}
				hashSituacao.put(frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo(), "(" + frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo() + " = " + frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar() + ") ");
				x++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * consultarRegistroAulaEspelho()
	 */
	public List<DiarioRegistroAulaVO> consultarRegistroAulaEspelho(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		DiarioRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		validarDados(turmaVO, semestre, ano);
		// <BY THYAGO - FILTRO DE ALUNOS PENDENTES FINANCEIRAMENTE>
		List<MatriculaPeriodoTurmaDisciplinaVO> listaConsulta = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAno(turmaVO, disciplina, ano, semestre, true, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		return montarListaEspelhoDiarioVO(listaConsulta, turmaVO, professor, disciplina, configuracaoFinanceiroVO, usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * montarListaEspelhoDiarioVO(java.util.List)
	 */
	public List<DiarioRegistroAulaVO> montarListaEspelhoDiarioVO(List<MatriculaPeriodoTurmaDisciplinaVO> listaConsulta, TurmaVO turmaVO, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<DiarioRegistroAulaVO> listaFinal = new ArrayList<DiarioRegistroAulaVO>(0);
		DiarioRegistroAulaVO diarioRegistroAula = new DiarioRegistroAulaVO();
		diarioRegistroAula.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(professor.intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		diarioRegistroAula.setTurma(turmaVO);
		diarioRegistroAula.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = listaConsulta.iterator();
		while (i.hasNext()) {
			DiarioFrequenciaAulaVO diarioFrequenciaAula = new DiarioFrequenciaAulaVO();
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurma = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaPeriodoTurma.getMatricula(), 0, NivelMontarDados.TODOS, usuarioVO);
			diarioFrequenciaAula.setMatricula(obj);
			diarioRegistroAula.adicionarObjFrequenciaAulaVOs(diarioFrequenciaAula);
		}

		listaFinal.add(diarioRegistroAula);
		return listaFinal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * verificarNotasUtilizadasConfiguracaoAcademico(relatorio
	 * .negocio.comuns.academico.DiarioRegistroAulaVO)
	 */
	public void verificarNotasUtilizadasConfiguracaoAcademico(DiarioRegistroAulaVO diarioRegistroAulaVo) {

		for (DiarioFrequenciaAulaVO diarioFrequenciaAulaVo : (List<DiarioFrequenciaAulaVO>) diarioRegistroAulaVo.getDiarioFrequenciaVOs()) {
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
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * consultarHistoricoPorMatricula(java.lang.String, java.lang.Integer)
	 */
	public HistoricoVO consultarHistoricoPorMatricula(String matricula, String semestre, String ano, Integer disciplina) throws Exception {
		return getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriula_Disciplina_Semestre_Ano(matricula, disciplina, semestre, ano, false, NivelMontarDados.TODOS, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DiarioRelInterfaceFacade#
	 * consultarConfiguracaoAcademico(java.lang.Integer)
	 */
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademico(Integer codigo) throws Exception {
		return getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigo, null);
	}

	public static String getDesignIReportRelatorio(String tipoLayout) {
		if (tipoLayout.equals("DiarioModRetratoRel")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + tipoLayout + ".jrxml");
		} else if (tipoLayout.equals("DiarioRel")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + tipoLayout + ".jrxml");
		} else if (tipoLayout.equals("DiarioMesMesRel")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "DiarioMesMesRel.jrxml");
		} else if (tipoLayout.equals("DiarioNotaRel")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "DiarioNotaRel.jrxml");
		} else if (tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequencia") || tipoLayout.equals("DiarioRelNota")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "Diario2Rel.jrxml");
		} else if (tipoLayout.equals("DiarioRelFrequenciaNota2")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "Diario2Modelo2Rel.jrxml");
		} else if (tipoLayout.equals("DiarioRelFrequenciaNota3")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "Diario2Modelo3Rel.jrxml");
		}else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + tipoLayout + ".jrxml");
		}
	}

	public static String getDesignIReportRelatorioVerso() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVerso() + ".jrxml");
	}

	public static String getDesignIReportRelatorioVersoPos() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoPos() + ".jrxml");
	}

	public static String getDesignIReportRelatorioEspelhoDiario() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "EspelhoDiarioRel.jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getCaminhoBaseRelatorioVerso() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DiarioRel");
	}

	public static String getIdEntidadeVerso() {
		return ("DiarioRelVerso");
	}

	public static String getIdEntidadeVersoPos() {
		return ("DiarioRelVersoPos");
	}
	
	public static String getDesignIReportRelatorioVersoModeloDois() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoModeloDois() + ".jrxml");
	}
	
	public static String getDesignIReportRelatorioVersoModeloTres() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoModeloTres() + ".jrxml");
	}
	
	public static String getIdEntidadeVersoModeloDois() {
		return ("DiarioRelVerso2");
	}

	public static String getIdEntidadeVersoModeloTres() {
		return ("DiarioRelVerso3");
	}

	public ProfessorTitularDisciplinaTurmaVO consultarProfessorTitularTurma(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcessao, UsuarioVO usuario) throws Exception {
		String semestrePrm = "";
		String anoPrm = "";
		if (turmaVO.getAnual() || turmaVO.getSemestral()) {
			anoPrm = ano;
			semestrePrm = "";
		}
		if (turmaVO.getSemestral()) {
			semestrePrm = semestre;
			anoPrm = ano;
		}
		ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
		p.setAno(anoPrm);
		p.setSemestre(semestrePrm);
		p.setTurma(turmaVO);
		p.getDisciplina().setCodigo(disciplina);
		// p =
		// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		// p =
		// getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		p = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaAgrupadaTitular(p, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), retornarExcessao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, "", usuario);
		return p;
	}

	public List<DiarioRegistroAulaVO> montarDiarioRegistroAula(List<RegistroAulaVO> registroAulaVOs, TurmaVO turmaVO, String semestre, String ano, String tipoLayout, ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, UsuarioVO usuarioVO, Boolean apresentarSituacaoMatricula, List<String> filtroNotasPorBimestre) throws Exception {
		List<DiarioRegistroAulaVO> diarioRegistroAulaVOs = new ArrayList<DiarioRegistroAulaVO>(0);
		if (Uteis.isAtributoPreenchido(registroAulaVOs)) {
			DiarioRegistroAulaVO diarioRegistroAulaVO = new DiarioRegistroAulaVO();
			Map<String, CrosstabVO> totalFaltaMatricula = new HashMap<String, CrosstabVO>(0);
			Map<String, CrosstabVO> totalFaltaMatriculaGeral = new HashMap<String, CrosstabVO>(0);
			Map<String, FrequenciaAulaVO> frequenciaAulaMatricula = new HashMap<String, FrequenciaAulaVO>(0);
			Map<String, Integer> ordemMatricula = new HashMap<String, Integer>();
			montarDiarioRegistroAula(diarioRegistroAulaVO, registroAulaVOs, turmaVO, semestre, ano, tipoLayout, professorTitularDisciplinaTurmaVO, usuarioVO);
			executarCriacaoOrdenacao(registroAulaVOs, ordemMatricula);
			diarioRegistroAulaVO.setApresentarSituacaoMatricula(apresentarSituacaoMatricula);
			if (tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequencia") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
				diarioRegistroAulaVOs.add(montarDiarioRegistroAulaFrequenciaNotaCrosstab(registroAulaVOs, tipoLayout, diarioRegistroAulaVO, totalFaltaMatricula, totalFaltaMatriculaGeral, frequenciaAulaMatricula, ordemMatricula, filtroNotasPorBimestre, usuarioVO));
			} else {
				diarioRegistroAulaVOs.add(montarDiarioRegistroAulaNotaCrosstab(registroAulaVOs, diarioRegistroAulaVO, frequenciaAulaMatricula, ordemMatricula, tipoLayout, filtroNotasPorBimestre));
			}
		}
		return diarioRegistroAulaVOs;
	}

	public DiarioRegistroAulaVO montarDiarioRegistroAulaFrequenciaNotaCrosstab(List<RegistroAulaVO> registroAulaVOs, String tipoLayout, DiarioRegistroAulaVO diarioRegistroAulaVO, Map<String, CrosstabVO> totalFaltaMatricula, Map<String, CrosstabVO> totalFaltaMatriculaGeral, Map<String, FrequenciaAulaVO> frequenciaAulaMatricula, Map<String, Integer> ordemMatricula, List<String> filtroNotasPorBimestre, UsuarioVO usuarioVO) throws Exception {
		int ordemColuna = 0;
		int ordemColunaInt = 0;
		int limitColuna = tipoLayout.equals("DiarioRelFrequenciaNota2") ? 29 : 33;
		HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
			ordemColuna++;
			ordemColunaInt++;
			if (ordemColunaInt == limitColuna) {
				for (CrosstabVO obj : totalFaltaMatricula.values()) {
					obj.setOrdemColuna(ordemColuna);
					diarioRegistroAulaVO.getCrosstabVOs().add(obj);
				}
				ordemColuna++;
				for (CrosstabVO obj : totalFaltaMatriculaGeral.values()) {
					CrosstabVO objClone = (CrosstabVO) obj.clone();
					objClone.setOrdemColuna(ordemColuna);
					diarioRegistroAulaVO.getCrosstabVOs().add(objClone);
				}
				ordemColuna++;
				ordemColunaInt = 1;
				totalFaltaMatricula = new HashMap<String, CrosstabVO>(0);
			}
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO);
			for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
				diarioRegistroAulaVO.getCrosstabVOs().add(montarDadosFrequenciaCrosstab(registroAulaVO, frequenciaAulaVO, ordemColuna, ordemMatricula.get(frequenciaAulaVO.getMatricula().getMatricula()), configuracaoGeralSistemaVO, tipoLayout));
				montarDadosTotalFaltas(frequenciaAulaVO, registroAulaVO, totalFaltaMatricula, "Total Faltas", 0, ordemMatricula.get(frequenciaAulaVO.getMatricula().getMatricula()), usuarioVO);
				montarDadosTotalFaltas(frequenciaAulaVO, registroAulaVO, totalFaltaMatriculaGeral, "Total Geral", 0, ordemMatricula.get(frequenciaAulaVO.getMatricula().getMatricula()), usuarioVO);
				if (!frequenciaAulaMatricula.containsKey(frequenciaAulaVO.getMatricula().getMatricula())) {
					frequenciaAulaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), frequenciaAulaVO);
				}
			}
		}
		if (ordemColunaInt <= limitColuna) {
			ordemColuna++;
			for (CrosstabVO obj : totalFaltaMatricula.values()) {
				obj.setOrdemColuna(ordemColuna);
				diarioRegistroAulaVO.getCrosstabVOs().add(obj);
			}
			ordemColuna++;
			for (CrosstabVO obj : totalFaltaMatriculaGeral.values()) {
				CrosstabVO objClone = (CrosstabVO) obj.clone();
				objClone.setOrdemColuna(ordemColuna);
				diarioRegistroAulaVO.getCrosstabVOs().add(objClone);
			}
//			ordemColuna++;
		}
		if (tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
			
			List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
//			ordemColuna++;
			for (FrequenciaAulaVO aulaVO : frequenciaAulaMatricula.values()) {
				if (!mapConfiguracaoAcademicoVOs.containsKey(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo())) {
					if (Uteis.isAtributoPreenchido(aulaVO.getHistoricoVO().getConfiguracaoAcademico())) {
						diarioRegistroAulaVO.setConfiguracaoAcademico(consultarConfiguracaoAcademico(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo()));
						mapConfiguracaoAcademicoVOs.put(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo(), diarioRegistroAulaVO.getConfiguracaoAcademico());
					}
				} else {
					diarioRegistroAulaVO.setConfiguracaoAcademico(mapConfiguracaoAcademicoVOs.get(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo()));
				}
				aulaVO.getHistoricoVO().setConfiguracaoAcademico(diarioRegistroAulaVO.getConfiguracaoAcademico());
				crosstabVOs = montarDadosConfiguracaoAcademicoCrosstab(diarioRegistroAulaVO.getConfiguracaoAcademico(), aulaVO, ordemColuna, ordemMatricula.get(aulaVO.getMatricula().getMatricula()), filtroNotasPorBimestre);
				diarioRegistroAulaVO.getCrosstabVOs().addAll(crosstabVOs);
				if (tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
					diarioRegistroAulaVO.getCrosstabVOs().add(montarDadosSituacaoHistoricoCrosstab(aulaVO, crosstabVOs.size() + ordemColuna + 1, ordemMatricula.get(aulaVO.getMatricula().getMatricula())));										
				}
			}
			if (tipoLayout.equals("DiarioRelFrequenciaNota2")) {
				ordemColuna += crosstabVOs.size();			
			} else {
				ordemColuna += crosstabVOs.size() + 1;	
			}
		}
		if (diarioRegistroAulaVO.getApresentarSituacaoMatricula() && !tipoLayout.equals("DiarioRelFrequenciaNota2")) {
			ordemColuna += 1;
			for (FrequenciaAulaVO aulaVO : frequenciaAulaMatricula.values()) {
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(aulaVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(aulaVO.getMatricula().getAluno().getNome());
				String situacaoMatriculaPeriodoApresentar = aulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
				if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
					situacaoMatriculaPeriodoApresentar = "Transferido";
				}
				crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
				crosstab.setOrdemColuna(ordemColuna);
				crosstab.setOrdemLinha(ordemMatricula.get(aulaVO.getMatricula().getMatricula()));
				crosstab.setLabelColuna("Situação Mat.");
				if (aulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals("PR")) {
					crosstab.setValorString("PR ");					
				} else {
					crosstab.setValorString(aulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo());
				}				
				diarioRegistroAulaVO.getCrosstabVOs().add(crosstab);				
			}
			
		}		
		for (FrequenciaAulaVO frequencia : frequenciaAulaMatricula.values()) {
			if (tipoLayout.equals("DiarioMesMesRel") || tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelNota") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
				hashSituacao.put(frequencia.getHistoricoVO().getSituacao(), "(" + frequencia.getHistoricoVO().getSituacao() + " = " + frequencia.getHistoricoVO().getSituacao_Apresentar() + ") ");
			}
			hashSituacao.put(frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo(), "(" + frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo() + " = " + frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar() + ") ");
		}
		if (Uteis.isAtributoPreenchido(registroAulaVOs)) {
			preencherColunasFinalPagina(diarioRegistroAulaVO, ordemColuna, registroAulaVOs.get(0).getFrequenciaAulaVOs().get(0), ordemMatricula.get(registroAulaVOs.get(0).getFrequenciaAulaVOs().get(0).getMatricula().getMatricula()), tipoLayout);
		}
		String situacaoApresentar = "";
		if (!hashSituacao.isEmpty()) {
			situacaoApresentar += "Situação = ";
			Set<String> chaves = hashSituacao.keySet();
			for (String chave : chaves) {
				String situacao = (String)hashSituacao.get(chave);
				situacaoApresentar += situacao;
			}
		}
		diarioRegistroAulaVO.setSituacaoApresentar(situacaoApresentar);
		return diarioRegistroAulaVO;
	}

	public DiarioRegistroAulaVO montarDiarioRegistroAulaNotaCrosstab(List<RegistroAulaVO> registroAulaVOs, DiarioRegistroAulaVO diarioRegistroAulaVO, Map<String, FrequenciaAulaVO> frequenciaAulaMatricula, Map<String, Integer> ordemMatricula, String tipoLayout, List<String> filtroNotasPorBimestre) throws Exception {
		int ordemColuna = 0;
		HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
			for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
				if (!frequenciaAulaMatricula.containsKey(frequenciaAulaVO.getMatricula().getMatricula())) {
					frequenciaAulaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), frequenciaAulaVO);
				}
			}
		}
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		for (FrequenciaAulaVO aulaVO : frequenciaAulaMatricula.values()) {
			if (!mapConfiguracaoAcademicoVOs.containsKey(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo())) {
				if (Uteis.isAtributoPreenchido(aulaVO.getHistoricoVO().getConfiguracaoAcademico())) {
					diarioRegistroAulaVO.setConfiguracaoAcademico(consultarConfiguracaoAcademico(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo()));
					mapConfiguracaoAcademicoVOs.put(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo(), diarioRegistroAulaVO.getConfiguracaoAcademico());
				}
			} else {
				diarioRegistroAulaVO.setConfiguracaoAcademico(mapConfiguracaoAcademicoVOs.get(aulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo()));
			}
			aulaVO.getHistoricoVO().setConfiguracaoAcademico(diarioRegistroAulaVO.getConfiguracaoAcademico());
			crosstabVOs = new ArrayList<CrosstabVO>(0);
			crosstabVOs = montarDadosConfiguracaoAcademicoCrosstab(diarioRegistroAulaVO.getConfiguracaoAcademico(), aulaVO, ordemColuna, ordemMatricula.get(aulaVO.getMatricula().getMatricula()), filtroNotasPorBimestre);
			diarioRegistroAulaVO.getCrosstabVOs().addAll(crosstabVOs);
			diarioRegistroAulaVO.getCrosstabVOs().add(montarDadosSituacaoHistoricoCrosstab(aulaVO, crosstabVOs.size() + ordemColuna + 1, ordemMatricula.get(aulaVO.getMatricula().getMatricula())));
		}
		ordemColuna += crosstabVOs.size() + 1;
		if (diarioRegistroAulaVO.getApresentarSituacaoMatricula()) {
			for (FrequenciaAulaVO aulaVO : frequenciaAulaMatricula.values()) {
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(aulaVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(aulaVO.getMatricula().getAluno().getNome());
				String situacaoMatriculaPeriodoApresentar = aulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
				if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
					situacaoMatriculaPeriodoApresentar = "Transferido";
				}
				crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
				crosstab.setOrdemColuna(ordemColuna);
				crosstab.setOrdemLinha(ordemMatricula.get(aulaVO.getMatricula().getMatricula()));
				crosstab.setLabelColuna("Situação Mat.");
				if (aulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals("PR")) {
					crosstab.setValorString("PR ");					
				} else {
					crosstab.setValorString(aulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo());
				}
				diarioRegistroAulaVO.getCrosstabVOs().add(crosstab);				
			}
			ordemColuna += 1;
		}	
		for (FrequenciaAulaVO frequencia : frequenciaAulaMatricula.values()) {
			if (tipoLayout.equals("DiarioMesMesRel") || tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelNota") || tipoLayout.equals("DiarioRelFrequenciaNota2") || tipoLayout.equals("DiarioRelFrequenciaNota3")) {
				hashSituacao.put(frequencia.getHistoricoVO().getSituacao(), "(" + frequencia.getHistoricoVO().getSituacao() + " = " + frequencia.getHistoricoVO().getSituacao_Apresentar() + ") ");
			}
			hashSituacao.put(frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo(), "(" + frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo() + " = " + frequencia.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar() + ") ");
		}
		if (!registroAulaVOs.isEmpty()) {
			preencherColunasFinalPagina(diarioRegistroAulaVO, ordemColuna, registroAulaVOs.get(0).getFrequenciaAulaVOs().get(0), ordemMatricula.get(registroAulaVOs.get(0).getFrequenciaAulaVOs().get(0).getMatricula().getMatricula()), tipoLayout);
		}
		String situacaoApresentar = "";
		if (!hashSituacao.isEmpty()) {
			situacaoApresentar += "Situação = ";
			Set<String> chaves = hashSituacao.keySet();
			for (String chave : chaves) {
				String situacao = (String)hashSituacao.get(chave);
				situacaoApresentar += situacao;
			}
		}		
		diarioRegistroAulaVO.setSituacaoApresentar(situacaoApresentar);
		return diarioRegistroAulaVO;
	}

	public void preencherColunasFinalPagina(DiarioRegistroAulaVO diarioRegistroAulaVO, int ordemColuna, FrequenciaAulaVO frequenciaAulaVO, int ordemLinha, String tipoLayout) {
		int limitColuna = tipoLayout.equals("DiarioRelFrequenciaNota2") ? 30 : 34;
		if ((limitColuna - (ordemColuna % limitColuna)) > 0) {
			limitColuna = (limitColuna - (ordemColuna % limitColuna)); 
			for (int x = 1; x < limitColuna; x++) {
				ordemColuna++;
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(frequenciaAulaVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(frequenciaAulaVO.getMatricula().getAluno().getNome());
				String situacaoMatriculaPeriodoApresentar = frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
				if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
					situacaoMatriculaPeriodoApresentar = "Transferido";
				}
				crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
				crosstab.setOrdemColuna(ordemColuna);				
				crosstab.setOrdemLinha(ordemLinha);
				crosstab.setLabelColuna("");
				crosstab.setLabelColuna2("");
				diarioRegistroAulaVO.getCrosstabVOs().add(crosstab);
			}
		}
	}

	public void montarDiarioRegistroAula(DiarioRegistroAulaVO diarioRegistroAulaVO, List<RegistroAulaVO> registroAulaVOs, TurmaVO turmaVO, String semestre, String ano, String tipoLayout, ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		double cargaHorariaDada = 0.0;
		double cargaHorariaTotal = 0.0;
		Long qtdAulaPrevista = 0L;
		Map<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
			registroAulaVO.setTurma(turmaVO);
			if (!Uteis.isAtributoPreenchido(diarioRegistroAulaVO.getCargaHorariaDisciplina())) {
				diarioRegistroAulaVO.setCargaHorariaDisciplina(obterCargaHorariaDisciplina(turmaVO, registroAulaVO.getDisciplina().getCodigo(), usuarioVO));
			}
			if (turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()) {
				if (!registroAulaVO.getAtividadeComplementar()) {
					cargaHorariaTotal += 60;
				} else {
					cargaHorariaTotal += Uteis.converterHorasEmMinutos(registroAulaVO.getCargaHoraria().doubleValue());
				}
			} else {
				diarioRegistroAulaVO.setCargaHorariaTotal(getFacadeFactory().getRegistroAulaFacade().consultarCargaHorariaTotal(registroAulaVO, turmaVO, semestre, ano) / 60);
			}
			diarioRegistroAulaVO.setDataEmissao(Uteis.getDataAno4Digitos(new Date()));
			if(registroAulaVO.getIsAulaNaoRegistrada().equals(Boolean.FALSE)) {
				if (!registroAulaVO.getAtividadeComplementar()) {
					if (turmaVO.getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()) {
						cargaHorariaDada += 60;
					} else {
						cargaHorariaDada += registroAulaVO.getCargaHoraria();
					}
				} else {
					cargaHorariaDada += Uteis.converterHorasEmMinutos(registroAulaVO.getCargaHoraria().doubleValue());
				}
			}
			
			Integer codigoConfiguracao = registroAulaVO.getTurma().getCurso().getConfiguracaoAcademico().getCodigo();
			if (!registroAulaVO.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo().equals(0)) {
				codigoConfiguracao = registroAulaVO.getGradeDisciplinaVO().getConfiguracaoAcademico().getCodigo();
			}
			if(!turmaVO.getTurmaAgrupada()){
				if (!mapConfiguracaoAcademicoVOs.containsKey(codigoConfiguracao)) {
					diarioRegistroAulaVO.setConfiguracaoAcademico(consultarConfiguracaoAcademico(codigoConfiguracao));
					mapConfiguracaoAcademicoVOs.put(codigoConfiguracao, diarioRegistroAulaVO.getConfiguracaoAcademico());
				} else {
					diarioRegistroAulaVO.setConfiguracaoAcademico(mapConfiguracaoAcademicoVOs.get(codigoConfiguracao));
				}
//				diarioRegistroAulaVO.setConfiguracaoAcademico(consultarConfiguracaoAcademico(codigoConfiguracao));
			}
			diarioRegistroAulaVO.setCargaHoraria(registroAulaVO.getCargaHoraria().doubleValue());
			diarioRegistroAulaVO.setObjeto(registroAulaVO.getCodigo());
			diarioRegistroAulaVO.setDisciplina(registroAulaVO.getDisciplina());
			diarioRegistroAulaVO.setTurma(turmaVO);
		}
		if (cargaHorariaTotal > 0.0) {
			diarioRegistroAulaVO.setCargaHorariaTotal(Uteis.converterMinutosEmHoras(cargaHorariaTotal).intValue());
		}
		diarioRegistroAulaVO.setCargaHorariaStr(String.valueOf(Uteis.converterMinutosEmHoras(cargaHorariaDada).intValue()));
		diarioRegistroAulaVO.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
		diarioRegistroAulaVO.getTurma().setIdentificadorTurma(turmaVO.getIdentificadorTurma());
	//	qtdAulaPrevista = (diarioRegistroAulaVO.getCargaHorariaDisciplina().doubleValue() / 50) * 60;
		qtdAulaPrevista = registroAulaVOs.stream().filter(p -> !p.getAtividadeComplementar()).count();
		diarioRegistroAulaVO.setQtdAulaPrevista((int) qtdAulaPrevista.intValue());
		diarioRegistroAulaVO.setQtdAulaMinistrada((int) registroAulaVOs.stream().filter(ravo -> !ravo.getIsAulaNaoRegistrada()).count());
	}

	public CrosstabVO montarDadosFrequenciaCrosstab(RegistroAulaVO registroAulaVO, FrequenciaAulaVO frequenciaAulaVO, int ordemColuna, int ordemLinha, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String tipoLayout) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		String chAula = "";
		if(tipoLayout.equals("DiarioRelFrequenciaNota3")) {
			chAula = " - " + registroAulaVO.getCargaHoraria().toString() + " min";
		}else {
			if (!registroAulaVO.getAtividadeComplementar()) {
				if (registroAulaVO.getTurma().getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()) {
					if(tipoLayout.equals("DiarioRelFrequenciaNota3")) {
						chAula = " - 60 min";
					}else {
						chAula = " - 1 h";
					}
										
				} else {
					chAula = " - " + registroAulaVO.getCargaHoraria().toString() + " min";
				}
			} else {
				if(tipoLayout.equals("DiarioRelFrequenciaNota3")) {
					chAula = " - " + Uteis.converterHorasEmMinutos(registroAulaVO.getCargaHoraria().doubleValue()).toString() + " min";
				}else {
					chAula = " - " + registroAulaVO.getCargaHoraria().toString() + ",0 h";
				}
			}
		}

		crosstab.setLabelLinha(frequenciaAulaVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(frequenciaAulaVO.getMatricula().getAluno().getNome());	
		String situacaoMatriculaPeriodoApresentar = frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
		if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
			situacaoMatriculaPeriodoApresentar = "Transferido";
		}
		crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelColuna(registroAulaVO.getData_Apresentar() + chAula);
		crosstab.setLabelColuna2("Dias Letivos");
		if (frequenciaAulaVO.getPresente() != null) {
			if (registroAulaVO.getIsAulaNaoRegistrada()) {
				crosstab.setValorString("--");
			} else if (frequenciaAulaVO.getPresente()) {
				crosstab.setValorString("PR");
			} else if (frequenciaAulaVO.getAbonado()) {
				if(tipoLayout.equals("DiarioRelFrequenciaNota3")) {
					crosstab.setValorString("TE");
				}
				else {
					if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getSiglaAbonoFalta())){
						crosstab.setValorString(configuracaoGeralSistemaVO.getSiglaAbonoFalta());	
					} else {
						crosstab.setValorString("AB");
					}
				}

			} else if (frequenciaAulaVO.getJustificado()) {
				if (Uteis.isAtributoPreenchido(frequenciaAulaVO.getAbonoFaltaVO().getTipoJustificativaFaltaVO().getSigla())) {
					crosstab.setValorString(frequenciaAulaVO.getAbonoFaltaVO().getTipoJustificativaFaltaVO().getSigla());
				} else {
					crosstab.setValorString("JU");
				}
			} else {
				crosstab.setValorString("F");
			}
		}else {
			crosstab.setValorString("--");
		}
		return crosstab;
	}

	public void montarDadosTotalFaltas(FrequenciaAulaVO frequenciaAulaVO, RegistroAulaVO registroAulaVO, Map<String, CrosstabVO> totalFaltaMatricula, String labelColuna, int ordemColuna, int ordemMatricula, UsuarioVO usuarioVO) throws Exception {
		if (totalFaltaMatricula.containsKey(frequenciaAulaVO.getMatricula().getMatricula())) {
			int totalFalta = totalFaltaMatricula.get(frequenciaAulaVO.getMatricula().getMatricula()).getValorInteger();
			if (frequenciaAulaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.TRANCAMENTO.getValor())) {
				Date dataTrancamento = getFacadeFactory().getTrancamentoFacade().consultarDataTrancamentoPorMatricula(frequenciaAulaVO.getMatricula().getMatricula(), usuarioVO, false);
				if (Uteis.getDataJDBC(dataTrancamento).after(Uteis.getDataJDBC(registroAulaVO.getData()))) {
					if (!frequenciaAulaVO.getAbonado() && (frequenciaAulaVO.getPresente() != null && !frequenciaAulaVO.getPresente()) && !registroAulaVO.getIsAulaNaoRegistrada()) {
						totalFaltaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), montarDadosTotalFaltasCrosstab(frequenciaAulaVO, labelColuna, totalFalta + 1, ordemColuna, ordemMatricula));
					}
				}
			} else {
				if (frequenciaAulaVO.getPresente() != null && !frequenciaAulaVO.getPresente() && !frequenciaAulaVO.getAbonado() && !registroAulaVO.getIsAulaNaoRegistrada()) {
					totalFaltaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), montarDadosTotalFaltasCrosstab(frequenciaAulaVO, labelColuna, totalFalta + 1, ordemColuna, ordemMatricula));
				}
			}
		} else {
			if (frequenciaAulaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.TRANCAMENTO.getValor())) {
				Date dataTrancamento = getFacadeFactory().getTrancamentoFacade().consultarDataTrancamentoPorMatricula(frequenciaAulaVO.getMatricula().getMatricula(), usuarioVO, false);
				if (Uteis.getDataJDBC(dataTrancamento).after(Uteis.getDataJDBC(registroAulaVO.getData()))) {
					if (!frequenciaAulaVO.getAbonado() && (frequenciaAulaVO.getPresente() != null && !frequenciaAulaVO.getPresente()) && !registroAulaVO.getIsAulaNaoRegistrada()) {
						totalFaltaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), montarDadosTotalFaltasCrosstab(frequenciaAulaVO, labelColuna, 1, ordemColuna, ordemMatricula));
					} else {
						totalFaltaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), montarDadosTotalFaltasCrosstab(frequenciaAulaVO, labelColuna, 0, ordemColuna, ordemMatricula));
					}
				}
			} else {
				if (frequenciaAulaVO.getPresente() != null && !frequenciaAulaVO.getPresente() && !frequenciaAulaVO.getAbonado() && !registroAulaVO.getIsAulaNaoRegistrada()) {
					totalFaltaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), montarDadosTotalFaltasCrosstab(frequenciaAulaVO, labelColuna, 1, ordemColuna, ordemMatricula));
				} else {
					totalFaltaMatricula.put(frequenciaAulaVO.getMatricula().getMatricula(), montarDadosTotalFaltasCrosstab(frequenciaAulaVO, labelColuna, 0, ordemColuna, ordemMatricula));
				}
			}
		}
	}

	public CrosstabVO montarDadosTotalFaltasCrosstab(FrequenciaAulaVO frequenciaAulaVO, String labelColuna, int totalFaltaMatricula, int ordemColuna, int ordemLinha) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setLabelLinha(frequenciaAulaVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(frequenciaAulaVO.getMatricula().getAluno().getNome());
		String situacaoMatriculaPeriodoApresentar = frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
		if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
			situacaoMatriculaPeriodoApresentar = "Transferido";
		}
		crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelColuna(labelColuna);
		crosstab.setLabelColuna2("Dias Letivos");
		crosstab.setValorInteger(totalFaltaMatricula);
		return crosstab;
	}

	public List<CrosstabVO> montarDadosConfiguracaoAcademicoCrosstab(ConfiguracaoAcademicoVO confVO, FrequenciaAulaVO frequenciaAulaVO, int ordemColuna, int ordemLinha, List<String> filtroBimestreSemestre) throws Exception {
//		if (Uteis.isAtributoPreenchido(frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico())) {
//			confVO = consultarConfiguracaoAcademico(frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo());
//		}
		List<String> listaAuxiliar = new ArrayList<String>(0);
		for (String valor : filtroBimestreSemestre) {
			listaAuxiliar.add(valor);
		}
		List<String> bimestresFiltrados = validarFiltroNotasPorBimestre(listaAuxiliar);
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		for (int x = 1; x <= 40; x++) {
			boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(confVO, "utilizarNota" + x);
			boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(confVO, "apresentarNota" + x);
			boolean notaMediaFinal = (Boolean) UtilReflexao.invocarMetodoGet(confVO, "nota" + x + "MediaFinal");
			BimestreEnum bimestreNota = (BimestreEnum) UtilReflexao.invocarMetodoGet(confVO, "bimestreNota" + x);
			Boolean utilizarNotaBimestre = true;
			if (!bimestresFiltrados.isEmpty()) {
				utilizarNotaBimestre = bimestresFiltrados.stream().anyMatch(b -> b.equals(bimestreNota.getValor()));
			}
			ConfiguracaoAcademicoNotaConceitoVO notaConceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(frequenciaAulaVO.getHistoricoVO(), "nota" + x + "Conceito");
			if (utilizarNotaBimestre && ((utilizarNota && apresentarNota) || (utilizarNota && notaMediaFinal))) {
				ordemColuna++;
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(frequenciaAulaVO.getHistoricoVO(), null, confVO, x);
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(frequenciaAulaVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(frequenciaAulaVO.getMatricula().getAluno().getNome());
				String situacaoMatriculaPeriodoApresentar = frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
				if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
					situacaoMatriculaPeriodoApresentar = "Transferido";
				}
				crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
				crosstab.setOrdemColuna(ordemColuna);
				crosstab.setOrdemLinha(ordemLinha);
				crosstab.setLabelColuna((String) UtilReflexao.invocarMetodoGet(confVO, "tituloNotaApresentar" + x));
				crosstab.setLabelColuna2("Avaliações");
				if (Uteis.isAtributoPreenchido(notaConceito)) {
					crosstab.setValorString(notaConceito.getAbreviaturaConceitoNota());
				} else {
					if (UtilReflexao.invocarMetodoGet(frequenciaAulaVO.getHistoricoVO(), "nota" + x) != null) {
						crosstab.setValorDouble(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble((Double) UtilReflexao.invocarMetodoGet(frequenciaAulaVO.getHistoricoVO(), "nota" + x), confVO.getQuantidadeCasasDecimaisPermitirAposVirgula()));
					}
				}
				crosstabVOs.add(crosstab);
			}
		}
		return crosstabVOs;
	}

	public CrosstabVO montarDadosSituacaoHistoricoCrosstab(FrequenciaAulaVO frequenciaAulaVO, int ordemColuna, int ordemLinha) throws Exception {
		if (!frequenciaAulaVO.getHistoricoVO().getSituacao().equals(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo()) && frequenciaAulaVO.getHistoricoVO().getCursando()) {
			if (!frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FO") && !frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI") && !frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("AT")) {
				frequenciaAulaVO.getHistoricoVO().setSituacao(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
			}
		}
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setLabelLinha(frequenciaAulaVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(frequenciaAulaVO.getMatricula().getAluno().getNome());
		String situacaoMatriculaPeriodoApresentar = frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
		if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
			situacaoMatriculaPeriodoApresentar = "Transferido";
		}
		crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelColuna("Situação");
		if (!frequenciaAulaVO.getHistoricoVO().getSituacao().equals("TS")) {
			if (!frequenciaAulaVO.getHistoricoVO().getSituacao().equals(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo()) && frequenciaAulaVO.getHistoricoVO().getCursando()) {
				crosstab.setValorString(SituacaoHistorico.CURSANDO.getValor());
			} else {
				crosstab.setValorString(frequenciaAulaVO.getHistoricoVO().getSituacao());
			}
		} else {
			crosstab.setValorString("TS");
		}
		return crosstab;
	}

	public CrosstabVO montarDadosMediaFinalHistoricoCrosstab(FrequenciaAulaVO frequenciaAulaVO, int ordemColuna, int ordemLinha) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setLabelLinha(frequenciaAulaVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(frequenciaAulaVO.getMatricula().getAluno().getNome());
		String situacaoMatriculaPeriodoApresentar = frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo_Apresentar();
		if (Uteis.isAtributoPreenchido(situacaoMatriculaPeriodoApresentar) && situacaoMatriculaPeriodoApresentar.toLowerCase().contains("transfer") ) {
			situacaoMatriculaPeriodoApresentar = "Transferido";
		}
		crosstab.setLabelLinha3(situacaoMatriculaPeriodoApresentar);
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelColuna("Média Final");
		crosstab.setValorString(frequenciaAulaVO.getHistoricoVO().getMediaFinal_Apresentar());
		return crosstab;
	}
	
	private void executarCriacaoOrdenacao(List<RegistroAulaVO> registroAulaVOs, Map<String, Integer> ordemMatricula) {
		int ordemLinha = 0;
		List<FrequenciaAulaVO> matriculasRealizarOrdenacao = new ArrayList<FrequenciaAulaVO>(0);
		for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
			q: for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
				for (FrequenciaAulaVO faVO : matriculasRealizarOrdenacao) {
					if (faVO.getMatricula().getMatricula().equals(frequenciaAulaVO.getMatricula().getMatricula())) {
						continue q;
					}
				}
				matriculasRealizarOrdenacao.add(frequenciaAulaVO);
			}
		}
		Ordenacao.ordenarLista(matriculasRealizarOrdenacao, "ordenacaoSemAcentuacaoNome");
		for (FrequenciaAulaVO matricula : matriculasRealizarOrdenacao) {
			ordemMatricula.put(matricula.getMatricula().getMatricula(), ++ordemLinha);
		}
	}
	
	/**
	 * Responsável por executar a geração de registros de aula de acordo com a programação de aula, levando em consideração os registros não lançados.
	 * 
	 * @author Wellington - 2 de out de 2015
	 * @param registroAulaVOs
	 * @param turmaVO
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param mes
	 * @param anoMes
	 * @param usuarioVO
	 * @throws Exception
	 */
	private void executarGeracaoRegistroAulaProgramadaNaoRegistrada(List<RegistroAulaVO> registroAulaVOs, TurmaVO turmaVO, Integer disciplina, String ano, String semestre, String mes, String anoMes, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {
		Map<String, FrequenciaAulaVO> frequenciaAulaGerar = new HashMap<String, FrequenciaAulaVO>(0);
		if(!turmaVO.getTurmaAgrupada()) { 
			getFacadeFactory().getCursoFacade().carregarDados(turmaVO.getCurso(), NivelMontarDados.BASICO, usuarioVO);
		}
		executarGeracaoFrequenciaAulaRegistroAula(registroAulaVOs, frequenciaAulaGerar, turmaVO, disciplina, ano, semestre, mes, anoMes, usuarioVO, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, tipoAluno, filtroAcademicoVO);
		executarGeracaoFrequenciaAulaComBaseMatriculaPeriodoTurmaDisciplina(frequenciaAulaGerar, turmaVO, disciplina, ano, semestre, usuarioVO, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, tipoAluno, filtroAcademicoVO, mes, anoMes, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados);
		if (frequenciaAulaGerar.isEmpty()) {
			return;
		}
		executarGeracaoRegistroAulaProgramadaNaoRegistradaComBaseProgramacaoAula(registroAulaVOs, frequenciaAulaGerar, turmaVO, disciplina, ano, semestre, mes, anoMes, usuarioVO, tipoFiltroPeriodo, dataInicio, dataFim);		
	}

	private void executarGeracaoFrequenciaAulaRegistroAula(List<RegistroAulaVO> registroAulaVOs, Map<String, FrequenciaAulaVO> frequenciaAulaGerar, TurmaVO turmaVO, Integer disciplina, String ano, String semestre, String mes, String anoMes, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(registroAulaVOs)) {
			for (RegistroAulaVO reg : registroAulaVOs) {
				for (FrequenciaAulaVO freq : reg.getFrequenciaAulaVOs()) {
					if (!frequenciaAulaGerar.containsKey(freq.getMatricula().getMatricula())) {
						FrequenciaAulaVO clone = (FrequenciaAulaVO) freq.clone();
						clone.setPresente(null);
						frequenciaAulaGerar.put(clone.getMatricula().getMatricula(), clone);
					}
				}
			}
						
		}
	}

	private void executarGeracaoFrequenciaAulaComBaseMatriculaPeriodoTurmaDisciplina(Map<String, FrequenciaAulaVO> frequenciaAulaGerar, TurmaVO turmaVO, Integer disciplina, String ano, String semestre, UsuarioVO usuarioVO, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(turmaVO, disciplina, ano, semestre, "", true, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroTipoCursoAluno, "", tipoAluno, filtroAcademicoVO, mes, anoMes, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, null, null, frequenciaAulaGerar, trazerAlunoTransferencia, permitirRealizarLancamentoAlunosPreMatriculados);
		//List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(null, disciplina, ano, semestre, "", false, false, trazerAlunosPendentesFinanceiramente, "", "", false, usuarioVO, turmaVO);
		for (MatriculaPeriodoTurmaDisciplinaVO mptdVO : matriculaPeriodoTurmaDisciplinaVOs) {
			if (!frequenciaAulaGerar.containsKey(mptdVO.getMatriculaObjetoVO().getMatricula())) {
				FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
				frequenciaAulaVO.setPresente(null);
				frequenciaAulaVO.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(mptdVO.getMatricula(), null, NivelMontarDados.BASICO, usuarioVO));
				frequenciaAulaVO.setHistoricoVO(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(mptdVO.getCodigo(), false, false, usuarioVO));
				frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplina(mptdVO.getCodigo());
				frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(mptdVO);
				frequenciaAulaGerar.put(frequenciaAulaVO.getMatricula().getMatricula(), frequenciaAulaVO);
			}
		}
	}

	private void executarGeracaoRegistroAulaProgramadaNaoRegistradaComBaseProgramacaoAula(List<RegistroAulaVO> registroAulaVOs, Map<String, FrequenciaAulaVO> frequenciaAulaGerar, TurmaVO turmaVO, Integer disciplina, String ano, String semestre, String mes, String anoMes, UsuarioVO usuarioVO, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {
		//UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(turmaVO.getCurso().getCodigo(), turmaVO.getUnidadeEnsino().getCodigo(), turmaVO.getTurno().getCodigo(), usuarioVO);
		SqlRowSet tabelaResultado = getFacadeFactory().getHorarioTurmaFacade().executarConsultaAulasProgramadasNaoRegistradas(turmaVO.getUnidadeEnsino().getCodigo(), turmaVO.getCurso().getCodigo(), turmaVO.getTurno().getCodigo(), turmaVO.getCodigo(), disciplina, 0, ano, semestre, dataInicio, dataFim, mes, anoMes, "", tipoFiltroPeriodo);
		boolean possuiRegistro = false;
		while (tabelaResultado.next()) {
			possuiRegistro = true;
			RegistroAulaVO registroAulaVO = new RegistroAulaVO();
			registroAulaVO.setTurma(turmaVO);
			registroAulaVO.setData(Uteis.getDataJDBC(tabelaResultado.getDate("horarioturmadia_data")));
			registroAulaVO.setNrAula(tabelaResultado.getInt("horarioturmadiaitem_nraula"));
			registroAulaVO.setCargaHoraria(tabelaResultado.getInt("horarioturmadiaitem_duracaoaula"));
			registroAulaVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina_codigo"));
			registroAulaVO.getDisciplina().setNome(tabelaResultado.getString("disciplina_nome"));
			registroAulaVO.getFrequenciaAulaVOs().addAll(frequenciaAulaGerar.values());
			registroAulaVO.setIsAulaNaoRegistrada(Boolean.TRUE);
			Ordenacao.ordenarLista(registroAulaVO.getFrequenciaAulaVOs(), "matricula.aluno.nome");
			registroAulaVOs.add(registroAulaVO);
		}
		if (!possuiRegistro) {			
			if (!registroAulaVOs.isEmpty()) {
				List<FrequenciaAulaVO> listaFreqFalta = new ArrayList<FrequenciaAulaVO>();
				RegistroAulaVO registroAulaVO = (registroAulaVOs.get(0));
				for (String key : frequenciaAulaGerar.keySet() ) {
					FrequenciaAulaVO frequencia = frequenciaAulaGerar.get(key);					
					Iterator i = registroAulaVO.getFrequenciaAulaVOs().iterator();
					boolean achou = false;
					while (i.hasNext()) {
						FrequenciaAulaVO freq = (FrequenciaAulaVO)i.next();
						if (frequencia.getMatricula().getMatricula().equals(freq.getMatricula().getMatricula())) {
							achou = true;
						}
					}
					if (!achou) {
						listaFreqFalta.add(frequencia);
					}
				}				
				Iterator j = listaFreqFalta.iterator();
				while (j.hasNext()) {
					FrequenciaAulaVO f = (FrequenciaAulaVO)j.next();
					registroAulaVO.getFrequenciaAulaVOs().add(f);
				}				
			}
		}
		Ordenacao.ordenarLista(registroAulaVOs, "data");
	}
	
	public void validarDadosImpressaoEtiqueta(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, LayoutEtiquetaVO layoutEtiquetaVO, String tipoLayout) throws Exception{		
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_identificadorTurma"));
		}
		if (turmaVO.getSemestral()) {
			if (semestre == null || semestre.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_semestre"));
			}
			if (ano == null || ano.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_ano"));
			}
		}
		if (turmaVO.getAnual()) {
			if (ano.equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_ano"));
			}
		}
		if (!Uteis.isAtributoPreenchido(disciplinaVO)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DiarioRel_disciplina"));
		}
		
		if (tipoLayout.equals("LayoutImpressaoEtiqueta") && layoutEtiquetaVO.getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_DiarioRel_layoutImpressaoEtiqueta"));
		}
	}
	
	@Override
	public String realizarImpressaoEtiqueta(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, LayoutEtiquetaVO layoutEtiquetaVO, String tipoLayout, Integer numeroCopias, 
			Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception{
		DisciplinaVO disciplinaVO = new DisciplinaVO();
		disciplinaVO.setCodigo(disciplina);
		validarDadosImpressaoEtiqueta(turmaVO, disciplinaVO, ano, semestre, layoutEtiquetaVO, tipoLayout);		
		disciplinaVO = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(disciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(turmaVO, disciplinaVO.getCodigo(), ano, semestre, false, usuarioVO);
		if(!Uteis.isAtributoPreenchido(professorTitularDisciplinaTurmaVO)){
			professorTitularDisciplinaTurmaVO = new ProfessorTitularDisciplinaTurmaVO();
		}
		CronogramaDeAulasRelVO cronogramaDeAulasRelVO = new CronogramaDeAulasRelVO();
		cronogramaDeAulasRelVO.setAno(ano);
		cronogramaDeAulasRelVO.setSemestre(semestre);
		cronogramaDeAulasRelVO.setDisciplina(disciplinaVO.getNome());
		cronogramaDeAulasRelVO.setTurma(turmaVO.getIdentificadorTurma());
		cronogramaDeAulasRelVO.setUnidadeEnsino(turmaVO.getUnidadeEnsino().getNome());
		if(turmaVO.getTurmaAgrupada()){
			StringBuilder sql =  new StringBuilder("");
			sql.append(" select abreviaturaCurso, min(periodoletivo.periodoletivo) as periodoletivo, array_to_string(array_agg(distinct curso.nome order by curso.nome), ', ') as curso from turmaagrupada ");
			sql.append(" inner join turma on turma.codigo = turmaagrupada.turma ");
			sql.append(" left join curso on turma.curso = curso.codigo ");
			sql.append(" left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
			sql.append(" where turmaagrupada.turmaorigem = ").append(turmaVO.getCodigo()).append(" group by abreviaturaCurso ");
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
		cronogramaDeAulasRelVO.setCurso(turmaVO.getCurso().getNome());
		cronogramaDeAulasRelVO.setPeriodo(turmaVO.getPeridoLetivo().getPeriodoLetivo().toString()+"º");	
		cronogramaDeAulasRelVO.setTurno(turmaVO.getTurno().getNome());
		cronogramaDeAulasRelVO.setProfessor(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getNome());
		cronogramaDeAulasRelVO.setGradeCurricular(turmaVO.getGradeCurricularVO().getNome());
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurmaEDisciplinaTrazendoSala(turmaVO.getCodigo(), disciplinaVO.getCodigo(), ano, semestre, false);
		if(!horarioTurmaDisciplinaProgramadaVOs.isEmpty()){
			cronogramaDeAulasRelVO.setCargaHorariaDisciplina(horarioTurmaDisciplinaProgramadaVOs.get(0).getChDisciplina());
			cronogramaDeAulasRelVO.setHoraAulaDisciplina(horarioTurmaDisciplinaProgramadaVOs.get(0).getHoraAula());
			cronogramaDeAulasRelVO.setSala(horarioTurmaDisciplinaProgramadaVOs.get(0).getSala().getSala());
			cronogramaDeAulasRelVO.setLocal(horarioTurmaDisciplinaProgramadaVOs.get(0).getSala().getLocalAula().getLocal());			
		}
		cronogramaDeAulasRelVO.setCodigoBarra(Uteis.preencherComZerosLimitandoTamanho(turmaVO.getCodigo(), 5)+Uteis.preencherComZerosLimitandoTamanho(disciplinaVO.getCodigo(), 6)+Uteis.preencherComZerosLimitandoTamanho(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getCodigo(), 5)+Uteis.preencherComZerosLimitandoTamanho(Uteis.isAtributoPreenchido(ano)?Integer.valueOf(ano):0, 4)+Uteis.preencherComZerosLimitandoTamanho(Uteis.isAtributoPreenchido(semestre)? Integer.valueOf(semestre):0, 1));
		List<CronogramaDeAulasRelVO> coAulasRelVOs = new ArrayList<CronogramaDeAulasRelVO>(0);
		coAulasRelVOs.add(cronogramaDeAulasRelVO);
		return getFacadeFactory().getCronogramaDeAulasRelFacade().realizarImpressaoEtiquetaCronogramaAula(layoutEtiquetaVO, coAulasRelVOs, numeroCopias, linha, coluna, removerEspacoTAGVazia, configuracaoGeralSistemaVO, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String executarAssinaturaDiarios(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, List<PessoaVO> listaProfessor, File fileAssinar, ConfiguracaoGeralSistemaVO config, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, boolean validarAulasRegistradas, boolean permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, UsuarioVO usuarioVO) throws Exception {
		try {
			if(validarAulasRegistradas && getFacadeFactory().getHorarioTurmaFacade().executarConsultaAulasProgramadasNaoRegistradas(turma.getUnidadeEnsino().getCodigo(), turma.getCurso().getCodigo(), 
					turma.getTurno().getCodigo(), turma.getCodigo(), disciplina.getCodigo(), null, ano, semestre, null, null, "", "", turma.getPeriodicidade(), "").next()) {
				throw new Exception("Para assinar digitalmente todas as aulas da turma "+turma.getIdentificadorTurma()+" e disciplina "+disciplina.getNome()+" devem estar registradas.");
			}
			DocumentoAssinadoVO obj = new DocumentoAssinadoVO();			
			obj.setDataRegistro(new Date());
			obj.setUsuario(usuarioVO);
			obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.DIARIO);
			obj.setTurma(turma);
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(obj.getTurma().getUnidadeEnsino().getCodigo(), false, usuarioVO));
			obj.setDisciplina(disciplina);
			obj.setAno(ano);
			obj.setSemestre(semestre);
			obj.getArquivo().setNome(fileAssinar.getName());
			obj.getArquivo().setDescricao(fileAssinar.getName().substring(fileAssinar.getName().lastIndexOf(File.separator) + 1, fileAssinar.getName().lastIndexOf(".")));
			obj.getArquivo().setExtensao(fileAssinar.getName().substring((fileAssinar.getName().lastIndexOf(".") + 1), fileAssinar.getName().length()));
			obj.getArquivo().setResponsavelUpload(usuarioVO);
			obj.getArquivo().setDataUpload(new Date());
			obj.getArquivo().setDataDisponibilizacao(new Date());
			obj.getArquivo().setManterDisponibilizacao(true);
			obj.getArquivo().setControlarDownload(true);
			obj.getArquivo().setDataIndisponibilizacao(null);
			obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
			obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
			obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
			obj.getArquivo().setPessoaVO(usuarioVO.getPessoa());
			obj.getArquivo().setTurma(turma);
			obj.getArquivo().setDisciplina(disciplina);
			obj.getArquivo().setArquivoAssinadoDigitalmente(true);
			obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
			int x = 0;
			for(PessoaVO professor : listaProfessor) {
				obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), professor, TipoPessoa.PROFESSOR, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, x == 0 ? 1 : 0, "", "", null, professor.getTipoAssinaturaDocumentoEnum()));
				x++;
			}
			if(usuarioVO.getIsApresentarVisaoCoordenador()){
				obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), usuarioVO.getPessoa(), TipoPessoa.COORDENADOR_CURSO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, "", "", null, usuarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
			}else{
				Integer cursoFiltro = 0;
				if (!obj.getTurma().getTurmaAgrupada()) {
					cursoFiltro = obj.getTurma().getCurso().getCodigo();
				}
				List<PessoaVO> listaCoordenador = getFacadeFactory().getPessoaFacade().consultarCoordenadorCursoTurmaUnidadeEnsino(cursoFiltro, obj.getTurma().getCodigo(), obj.getTurma().getUnidadeEnsino().getCodigo(), usuarioVO);
				x = 0;
				for (PessoaVO coordenador : listaCoordenador) {			
					obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), coordenador, TipoPessoa.COORDENADOR_CURSO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, x == 0 ? 2 : 0, "", "", null, coordenador.getTipoAssinaturaDocumentoEnum()));
					x++;
				}	
			}
			getFacadeFactory().getDocumentoAssinadoFacade().incluir(obj, false, usuarioVO, config);
			File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome());
			getFacadeFactory().getArquivoHelper().copiar(fileAssinar, fileDiretorioCorreto);
			ConfiguracaoGEDVO configGEDVO =  getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(obj.getTurma().getUnidadeEnsino().getCodigo(), usuarioVO);
			if(Uteis.isAtributoPreenchido(configGEDVO)) {
				getFacadeFactory().getDocumentoAssinadoFacade().adicionarSeloPDF(obj, fileAssinar.getAbsolutePath(), config, configGEDVO);
				getFacadeFactory().getDocumentoAssinadoFacade().adicionarQRCodePDF(obj, fileAssinar.getAbsolutePath(), config, configGEDVO);
				ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGEDVO.getConfiguracaoGedOrigemVO(obj.getTipoOrigemDocumentoAssinadoEnum());
				ProvedorDeAssinaturaEnum provedorAssinaturaEnum = obj.getProvedorAssinaturaVisaoAdm() != null ? obj.getProvedorAssinaturaVisaoAdm() : configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum();		
				if(configuracaoGedOrigemVO.getAssinarDocumento() && (provedorAssinaturaEnum.isProvedorCertisign() || provedorAssinaturaEnum.isProvedorImprensaOficial())){
					obj.setProvedorDeAssinaturaEnum(provedorAssinaturaEnum);
					return getFacadeFactory().getDocumentoAssinadoFacade().executarAssinaturaProvedorCertiSign(obj, fileDiretorioCorreto.getAbsolutePath(), configGEDVO, config, usuarioVO);
				}else if(configuracaoGedOrigemVO.getAssinarDocumento()) {
					getFacadeFactory().getDocumentoAssinadoFacade().preencherAssinadorDigitalDocumentoPdf(fileDiretorioCorreto.getAbsolutePath(), configGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), obj, null, "#000000", Float.valueOf(configGEDVO.getConfiguracaoGedDiarioVO().getAlturaAssinaturaUnidadeEnsino()), Float.valueOf(configGEDVO.getConfiguracaoGedDiarioVO().getLarguraAssinaturaUnidadeEnsino()), 8f, configGEDVO.getConfiguracaoGedDiarioVO().getPosicaoXAssinaturaUnidadeEnsino(), configGEDVO.getConfiguracaoGedDiarioVO().getPosicaoYAssinaturaUnidadeEnsino(), 0, 0, configGEDVO.getConfiguracaoGedDiarioVO().isUltimaPaginaAssinaturaUnidadeEnsino(), configGEDVO.getConfiguracaoGedDiarioVO().getApresentarAssinaturaUnidadeEnsino(), config, false, false);
				}
				return getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarDocumento(obj, fileDiretorioCorreto, config, permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, false, usuarioVO);
			}else {
				return getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarDocumento(obj, fileDiretorioCorreto, config, permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, false, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private List<String> validarFiltroNotasPorBimestre(List<String> filtroNotasPorBimestre) {
		List<String> listaApenasComBimestresApresentados = new ArrayList<String>(0);
		if (filtroNotasPorBimestre.stream().anyMatch(b -> b.equals(BimestreEnum.SEMESTRE_1.getValor()))) {
			listaApenasComBimestresApresentados.add(BimestreEnum.BIMESTRE_01.getValor());
			listaApenasComBimestresApresentados.add(BimestreEnum.BIMESTRE_02.getValor());
		}
		if (filtroNotasPorBimestre.stream().anyMatch(b -> b.equals(BimestreEnum.SEMESTRE_2.getValor()))) {
			listaApenasComBimestresApresentados.add(BimestreEnum.BIMESTRE_03.getValor());
			listaApenasComBimestresApresentados.add(BimestreEnum.BIMESTRE_04.getValor());
		}
		filtroNotasPorBimestre.removeIf(b -> b.equals(BimestreEnum.SEMESTRE_1.getValor()));
		filtroNotasPorBimestre.removeIf(b -> b.equals(BimestreEnum.SEMESTRE_2.getValor()));
		filtroNotasPorBimestre.removeIf(b -> b.equals(BimestreEnum.NAO_CONTROLA.getValor()));
		listaApenasComBimestresApresentados.addAll(filtroNotasPorBimestre);
		Stream<String> listaFinal = listaApenasComBimestresApresentados.stream().distinct().sorted();
		return listaFinal.collect(Collectors.toList());
	}
	
	
}
