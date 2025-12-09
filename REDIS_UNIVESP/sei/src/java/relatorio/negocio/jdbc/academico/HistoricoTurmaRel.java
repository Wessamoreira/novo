package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.HistoricoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.interfaces.academico.HistoricoTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class HistoricoTurmaRel extends SuperRelatorio implements HistoricoTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public HistoricoTurmaRel() {
	}

	public List<HistoricoTurmaRelVO> criarObjeto(TurmaVO turmaVO, String semestre, String ano, Integer unidadeEnsino, Integer disciplina, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiramente, Boolean trazerDisciplinaAproveitadas, UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno,boolean ApresentarAlunoTurmaOrigem, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		List<HistoricoVO> listaHistorico = criarObjetoTurmaNaoAgrupada(turmaVO, semestre, ano, unidadeEnsino, disciplina, filtroVisaoProfessor, trazerAlunoPendenteFinanceiramente, trazerDisciplinaAproveitadas, usuarioVO, filtroRelatorioAcademicoVO, tipoAluno, permitirRealizarLancamentoAlunosPreMatriculados);
		DisciplinaVO disciplinaVO = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		return realizarGeracaoDadosApresentarRelatorio(listaHistorico, turmaVO, disciplinaVO, ano, semestre,usuarioVO,ApresentarAlunoTurmaOrigem);
	}

	private List<HistoricoVO> criarObjetoTurmaNaoAgrupada(TurmaVO turmaVO, String semestre, String ano, Integer unidadeEnsino, Integer disciplina, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiramente, Boolean trazerDisciplinaAproveitadas, UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		String situacaoHistorico = trazerDisciplinaAproveitadas == null || trazerDisciplinaAproveitadas ? "" : "'AA', 'CC', 'CH', 'IS'";
		if (filtroVisaoProfessor) {
			return getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessor(unidadeEnsino, turmaVO.getCurso().getCodigo(), disciplina, turmaVO, ano, semestre, "AT", situacaoHistorico, true, filtroVisaoProfessor, trazerAlunoPendenteFinanceiramente, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);
		} else {
			FiltroRelatorioAcademicoVO filtro = new FiltroRelatorioAcademicoVO();
			filtro.setPendenteFinanceiro(Boolean.TRUE);
			return getFacadeFactory().getHistoricoFacade().consultaRapidaPorTurmaUnidadeEnsinoCursoDisciplinaAnoSemestreDataHistoricoOrdenarDisciplina(unidadeEnsino, turmaVO.getCurso().getCodigo(), disciplina, turmaVO, ano, semestre, "", situacaoHistorico, true, filtroVisaoProfessor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, filtroRelatorioAcademicoVO, tipoAluno, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);
		}
	}

	public static void validarDados(TurmaVO turmaVO, String semestre, String ano, Integer unidadeEnsino, Integer disciplina) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO (Histórico Turma) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			throw new ConsistirException("O campo TURMA (Histórico Turma) deve ser informado.");
		}
		if (turmaVO.getSemestral() || turmaVO.getAnual()) {
			if (!Uteis.isAtributoPreenchido(ano)) {
				throw new ConsistirException("O campo ANO (Histórico Turma) deve ser informado.");
			}
			if (turmaVO.getSemestral()) {
				if (!Uteis.isAtributoPreenchido(semestre)) {
					throw new ConsistirException("O campo SEMESTRE (Histórico Turma) deve ser informado.");
				}
			}
		}
		if (!Uteis.isAtributoPreenchido(disciplina)) {
			throw new ConsistirException("O campo DISCIPLINA (Histórico Turma) deve ser informado.");
		}
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade(false) + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade(Boolean isTurmaAgrupada) {
		if (isTurmaAgrupada) {
			return "HistoricoTurmaAgrupadaRel";
		}
		return "HistoricoTurmaRel";
	}

	@Override
	public List<HistoricoTurmaRelVO> realizarGeracaoDadosApresentarRelatorio(List<HistoricoVO> historicoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, UsuarioVO usuario, boolean ApresentarAlunoTurmaOrigem) throws Exception {
		if (ApresentarAlunoTurmaOrigem && turmaVO.getTurmaAgrupada()) {
			return montarDadosTurmaAgrupadaApresentarRelatorio(historicoVOs, turmaVO, disciplinaVO, ano, semestre, usuario);
		} else {
			return montarDadosTurmaApresentarRelatorio(historicoVOs, turmaVO, disciplinaVO, ano, semestre, usuario);
		}
	}

	public void preencherColunasFinalPagina(HistoricoTurmaRelVO historicoTurmaRelVO) {
		int ordemColuna = 3;
		int x = 1;
		for (x = 1; x <= 40; x++) {
			boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoTurmaRelVO.getConfiguracaoAcademicoVO(), "utilizarNota" + x);
			boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoTurmaRelVO.getConfiguracaoAcademicoVO(), "apresentarNota" + x);
			if (utilizarNota && apresentarNota) {
				++ordemColuna;
			}
		}
		if (ordemColuna % 18 == 0) {
			return;
		}
		if ((18 - (ordemColuna % 18)) > 0) {
			for (x = 1; x < (18 - (ordemColuna % 18)); x++) {
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(historicoTurmaRelVO.getCrosstabVOs().get(0).getLabelLinha());
				crosstab.setLabelLinha2(historicoTurmaRelVO.getCrosstabVOs().get(0).getLabelLinha2());
				crosstab.setLabelLinha3(historicoTurmaRelVO.getCrosstabVOs().get(0).getLabelLinha3());
				crosstab.setLabelLinha4(historicoTurmaRelVO.getCrosstabVOs().get(0).getLabelLinha4());
				crosstab.setOrdemColuna(ordemColuna + x);
				crosstab.setOrdemLinha(1);
				crosstab.setLabelColuna("");
				historicoTurmaRelVO.getCrosstabVOs().add(crosstab);
			}
		}
	}

	public List<CrosstabVO> montarDadosConfiguracaoAcademicoCrosstab(HistoricoTurmaRelVO historicoTurmaRelVO, HistoricoVO historicoVO, int ordemColuna) throws Exception {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		int x = 1;
		for (x = 1; x <= 40; x++) {
			boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoTurmaRelVO.getConfiguracaoAcademicoVO(), "utilizarNota" + x);
			boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoTurmaRelVO.getConfiguracaoAcademicoVO(), "apresentarNota" + x);
			if (utilizarNota && apresentarNota) {
				ConfiguracaoAcademicoNotaConceitoVO notaConceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x + "Conceito");
				ordemColuna++;
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
				crosstab.setLabelLinha3(historicoVO.getMatricula().getCurso().getNome());
				crosstab.setLabelLinha4(historicoVO.getMatriculaPeriodo().getPeriodoLetivo().getDescricao());
				crosstab.setOrdemColuna(ordemColuna);
				crosstab.setOrdemLinha(historicoTurmaRelVO.getOrdemLinhaAtual());
				crosstab.setLabelColuna((String) UtilReflexao.invocarMetodoGet(historicoTurmaRelVO.getConfiguracaoAcademicoVO(), "tituloNotaApresentar" + x));
				if (Uteis.isAtributoPreenchido(notaConceito)) {
					crosstab.setValorString(notaConceito.getAbreviaturaConceitoNota());
				} else {
					if (UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x) != null) {
						crosstab.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr((Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula(), true));
					}
				}
				crosstabVOs.add(crosstab);
			}
		}		
		ordemColuna++;
		CrosstabVO crosstabVO = new CrosstabVO();
		crosstabVO.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstabVO.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstabVO.setLabelLinha3(historicoVO.getMatricula().getCurso().getNome());
		crosstabVO.setLabelLinha4(historicoVO.getMatriculaPeriodo().getPeriodoLetivo().getDescricao());
		crosstabVO.setOrdemColuna(ordemColuna);
		crosstabVO.setOrdemLinha(historicoTurmaRelVO.getOrdemLinhaAtual());
		crosstabVO.setLabelColuna("MF");				
		if(historicoVO.getMediaFinal() == null){
			crosstabVO.setValorString("--");
		}else{
			if (historicoVO.getMediaFinalConceito().getCodigo() > 0) {
				crosstabVO.setValorString(historicoVO.getMediaFinalConceito().getAbreviaturaConceitoNota());
	        }else if(historicoVO.getUtilizaNotaFinalConceito()){
	        	crosstabVO.setValorString(historicoVO.getNotaFinalConceito());
	        } else{
	        	crosstabVO.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(historicoVO.getMediaFinal(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
	        }       	      		
		}
		crosstabVOs.add(crosstabVO);
		return crosstabVOs;
	}

	public CrosstabVO montarDadosSituacaoHistoricoCrosstab(HistoricoVO historicoVO, int ordemColuna, Integer ordemLinha) throws Exception {
		if (historicoVO.getSituacao().equals("CS")) {
			if (!historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FO") && !historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI") && !historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("AT")) {
				historicoVO.setSituacao(historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
			}
		}
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstab.setLabelLinha3(historicoVO.getMatricula().getCurso().getNome());
		crosstab.setLabelLinha4(historicoVO.getMatriculaPeriodo().getPeriodoLetivo().getDescricao());
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelColuna("Situação");
		crosstab.setValorString(historicoVO.getSituacao());
		return crosstab;
	}

	public CrosstabVO montarDadosFrequenciaHistoricoCrosstab(HistoricoVO historicoVO, int ordemColuna, Integer ordemLinha) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstab.setLabelLinha3(historicoVO.getMatricula().getCurso().getNome());
		crosstab.setLabelLinha4(historicoVO.getMatriculaPeriodo().getPeriodoLetivo().getDescricao());
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelColuna("Freq.");
		crosstab.setValorString(historicoVO.getFrequencia_Apresentar());
		return crosstab;
	}
	
	public List<HistoricoTurmaRelVO> montarDadosTurmaAgrupadaApresentarRelatorio(List<HistoricoVO> historicoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, UsuarioVO usuario) throws Exception {
		Map<Integer, Map<Integer, HistoricoTurmaRelVO>> historicoTurmaRelVOs = new HashMap<Integer, Map<Integer, HistoricoTurmaRelVO>>(0);

		for (HistoricoVO historicoVO : historicoVOs) {
			
			HistoricoTurmaRelVO historicoTurmaRelVO = null;
			if (!historicoTurmaRelVOs.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
				historicoTurmaRelVOs.put(historicoVO.getConfiguracaoAcademico().getCodigo(), new HashMap<Integer, HistoricoTurmaRelVO>());
			}

			if (!historicoTurmaRelVOs.get(historicoVO.getConfiguracaoAcademico().getCodigo()).containsKey(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo())) {
				historicoTurmaRelVO = new HistoricoTurmaRelVO();
				historicoTurmaRelVO.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
				historicoTurmaRelVO.setDisciplinaVO(disciplinaVO);
				historicoTurmaRelVO.setCargaHorariaDisciplina(historicoVO.getCargaHorariaDisciplina());
				historicoTurmaRelVO.setNomeProfessor(historicoVO.getNomeProfessor());
				historicoTurmaRelVO.setAno(ano);
				historicoTurmaRelVO.setSemestre(semestre);
				Map<String, Date> periodoAula = getFacadeFactory().getHorarioTurmaFacade().consultarPeriodoAulaDisciplinaPorTurmaDisciplinaAnoSemestre(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), disciplinaVO.getCodigo(), ano, semestre);
				if (periodoAula != null) {
					historicoTurmaRelVO.setDataPrimeiraAula(periodoAula.get("DATA_INICIO"));
					historicoTurmaRelVO.setDataUltimaAula(periodoAula.get("DATA_TERMINO"));
				}
				historicoTurmaRelVO.setConfiguracaoAcademicoVO(historicoVO.getConfiguracaoAcademico());
				historicoTurmaRelVOs.get(historicoVO.getConfiguracaoAcademico().getCodigo()).put(historicoTurmaRelVO.getTurmaVO().getCodigo(), historicoTurmaRelVO);
			}
			Integer ordemColuna = 1;
			historicoTurmaRelVO = historicoTurmaRelVOs.get(historicoVO.getConfiguracaoAcademico().getCodigo()).get(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
			historicoTurmaRelVO.setTotalAlunos(historicoTurmaRelVO.getTotalAlunos() + 1);
			historicoTurmaRelVO.setOrdemLinhaAtual(historicoTurmaRelVO.getOrdemLinhaAtual() + 1);
			historicoTurmaRelVO.getCrosstabVOs().add(montarDadosSituacaoHistoricoCrosstab(historicoVO, ordemColuna, historicoTurmaRelVO.getOrdemLinhaAtual()));
			ordemColuna++;
			historicoTurmaRelVO.getCrosstabVOs().add(montarDadosFrequenciaHistoricoCrosstab(historicoVO, ordemColuna, historicoTurmaRelVO.getOrdemLinhaAtual()));
			historicoTurmaRelVO.getCrosstabVOs().addAll(montarDadosConfiguracaoAcademicoCrosstab(historicoTurmaRelVO, historicoVO, ordemColuna));
			
		}
       
		List<HistoricoTurmaRelVO> listaFinal = new ArrayList<HistoricoTurmaRelVO>(0);
		for (Integer conf : historicoTurmaRelVOs.keySet()) {
			listaFinal.addAll(historicoTurmaRelVOs.get(conf).values());
		}
		for (HistoricoTurmaRelVO historicoTurmaRelVO : listaFinal) {
			preencherColunasFinalPagina(historicoTurmaRelVO);
		}
		return listaFinal;
	}
	
	public List<HistoricoTurmaRelVO> montarDadosTurmaApresentarRelatorio(List<HistoricoVO> historicoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, UsuarioVO usuario) throws Exception {
		Map<Integer, HistoricoTurmaRelVO> historicoTurmaRelVOs = new HashMap<Integer, HistoricoTurmaRelVO>(0);
		for (HistoricoVO historicoVO : historicoVOs) {
			HistoricoTurmaRelVO historicoTurmaRelVO = null;
			if (!historicoTurmaRelVOs.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
				historicoTurmaRelVO = new HistoricoTurmaRelVO();
				historicoTurmaRelVO.setTurmaVO(turmaVO);
				historicoTurmaRelVO.setDisciplinaVO(disciplinaVO);
				historicoTurmaRelVO.setCargaHorariaDisciplina(historicoVO.getCargaHorariaDisciplina());
				historicoTurmaRelVO.setAno(ano);
				historicoTurmaRelVO.setSemestre(semestre);
				historicoTurmaRelVO.setNomeProfessor(getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarProfessorTitularTurma(historicoTurmaRelVO));
				if (!Uteis.isAtributoPreenchido(historicoTurmaRelVO.getNomeProfessor())) {
					historicoTurmaRelVO.setNomeProfessor(historicoVO.getNomeProfessor());
				}
				Map<String, Date> periodoAula = getFacadeFactory().getHorarioTurmaFacade().consultarPeriodoAulaDisciplinaPorTurmaDisciplinaAnoSemestre(turmaVO.getCodigo(), disciplinaVO.getCodigo(), ano, semestre);
				if (periodoAula != null) {
					historicoTurmaRelVO.setDataPrimeiraAula(periodoAula.get("DATA_INICIO"));
					historicoTurmaRelVO.setDataUltimaAula(periodoAula.get("DATA_TERMINO"));
				}
				historicoTurmaRelVO.setConfiguracaoAcademicoVO(historicoVO.getConfiguracaoAcademico());
				historicoTurmaRelVOs.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoTurmaRelVO);
			}
			Integer ordemColuna = 1;
			historicoTurmaRelVO = historicoTurmaRelVOs.get(historicoVO.getConfiguracaoAcademico().getCodigo());
			historicoTurmaRelVO.setTotalAlunos(historicoTurmaRelVO.getTotalAlunos() + 1);
			historicoTurmaRelVO.setOrdemLinhaAtual(historicoTurmaRelVO.getOrdemLinhaAtual() + 1);
			historicoTurmaRelVO.getCrosstabVOs().add(montarDadosSituacaoHistoricoCrosstab(historicoVO, ordemColuna, historicoTurmaRelVO.getOrdemLinhaAtual()));
			ordemColuna++;
			historicoTurmaRelVO.getCrosstabVOs().add(montarDadosFrequenciaHistoricoCrosstab(historicoVO, ordemColuna, historicoTurmaRelVO.getOrdemLinhaAtual()));
			historicoTurmaRelVO.getCrosstabVOs().addAll(montarDadosConfiguracaoAcademicoCrosstab(historicoTurmaRelVO, historicoVO, ordemColuna));

		}
		List<HistoricoTurmaRelVO> listaFinal = new ArrayList<HistoricoTurmaRelVO>(0);
		listaFinal.addAll(historicoTurmaRelVOs.values());
		for (HistoricoTurmaRelVO historicoTurmaRelVO : listaFinal) {
			preencherColunasFinalPagina(historicoTurmaRelVO);
		}
		return listaFinal;
	}
	
	 

}
