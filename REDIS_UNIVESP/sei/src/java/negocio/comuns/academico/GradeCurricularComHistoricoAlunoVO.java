/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;

/**
 * Classe trasient a ser utilizada para controle e emissao do historico de um
 * aluno, tendo todas as informacoes sobre o aluno em uma determinada grade
 * curricular Como por exmeplo: aluno ja integralizou (pode formar)?, está no
 * último periodo?, é um possível formando?, percentual de creditos / carga
 * horaria cursada?, etc
 * 
 * @author EDIGARANTONIO
 */
public class GradeCurricularComHistoricoAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GradeCurricularVO gradeCurricularVO;
	private List<PeriodoLetivoComHistoricoAlunoVO> periodoLetivoComHistoricoAlunoVOs;
	private List<HistoricoVO> todosHistoricosAlunoGradeCurricular;
	/**
	 * No SEI é possível registrar no histórico do aluno um aproveitamento
	 * academico de uma disciplina que não consta na gradeCurricular do curso
	 * vigente. Contudo, mesmo neste caso os históricos destas disciplinas ficam
	 * vinculadas a gradeCurricular do aluno (no momento de sua inclusao). Isto
	 * irá permitir isolar a vida acadêmica deste aluno nesta matriz, mesmo após
	 * o mesmo ser transferido para outra unidade/curso/matriz.
	 */
	private List<HistoricoVO> historicosDisciplinasForaGradeCurricular;
	private List<HistoricoVO> historicosDisciplinasAprovadasAlunoGradeCurricular;
	private List<HistoricoVO> historicosDisciplinasAlunoCursandoGradeCurricular;
	private List<HistoricoVO> historicosDisciplinasAlunoReprovouGradeCurricular;	
	private Integer totalCargaHorariaCursadaAluno;
	private Integer totalCreditosCursadosAluno;
	private Integer totalCargaHorariaCursadaForaGradeAluno;
	private Integer totalCreditosCursadosForaGradeAluno;
	private Integer totalCargaHorariaAlunoEstaCursandoAtualmente;
	private Integer totalCreditosAlunoEstaCursandoAtualmente;
	/**
	 * Irá determinar o periodoLetivo em que o aluno está academicamente
	 * matriculado. Ou seja, mesmo que o aluno já tenha renovado sua matrícula
	 * por diversas vezes (por exemplo, 05 vezes), este período iré indicar em
	 * qual período o mesmo está acadêmicamente (por cumprimento de carga
	 * horária /credito). Este campo será definido com base em parametros da
	 * configuração academica.
	 */
	private PeriodoLetivoVO periodoLetivoEvolucaoAcademicaAluno;
	/**
	 * Totaliza a cargaHoraria que ainda está pendente para o aluno cursar para
	 * integralizar o curso. Mesmo que o aluno esteja cursando uma disciplina
	 * ela ainda será tratada como pendente. Até que a mesma estaja registrada
	 * como aprovada.
	 */
	private Integer cargaHorariaPendente;
	/**
	 * Totaliza os creditos que ainda estão pendentes para o aluno cursar para
	 * integralizar o curso. Mesmo que o aluno esteja cursando uma disciplina
	 * ela ainda será tratada como pendente. Até que a mesma estaja registrada
	 * como aprovada.
	 */
	private Integer nrCreditosPendentes;
	/**
	 * Indica de o aluno já integralizou sua matriz curricular ou seja, já
	 * cumpriu todas as disciplinas obrigatorias e optativas determinadas para
	 * ele, em todos os períodos
	 */
	private Boolean gradeIntegralizada;
	/**
	 * Indica se o aluno está cursando o último período do curso on não.
	 */
	private Boolean cursandoUltimoPeriodo;
	/**
	 * Indica se o aluno é um possível formando, do ponto de vista da matriz
	 * curricular. Ou seja, se ele já estudou todas as disciplinas obrigatorias
	 * (e optativas) de todos os períodos do curso. E as que faltam o mesmo já
	 * está cursando as mesmas. O mesmo ainda não pode ser definido como apto
	 * para formar com base nesta variável, pois o mesmo depende ainda de
	 * cumprir atividades extracurriculares obrigatórios como ENADE, Atividades
	 * complementares e estágio.
	 */
	private Boolean possivelFormandoSobOticaMatrizCurricular;
	private Double percentualIntegralizacaoPorCargaHoraria;
	private Double percentualIntegralizacaoPorCreditos;

	@Override
	public String toString() {
		return "GradeCurricularComHistoricoAlunoVO [gradeCurricularVO=" + gradeCurricularVO + ", periodoLetivoComHistoricoAlunoVOs=" + periodoLetivoComHistoricoAlunoVOs + ", todosHistoricosAlunoGradeCurricular=" + todosHistoricosAlunoGradeCurricular + ", historicosDisciplinasForaGradeCurricular=" + historicosDisciplinasForaGradeCurricular + ", historicosDisciplinasAprovadasAlunoGradeCurricular=" + historicosDisciplinasAprovadasAlunoGradeCurricular + ", historicosDisciplinasAlunoCursandoGradeCurricular=" + historicosDisciplinasAlunoCursandoGradeCurricular + ", totalCargaHorariaCursadaAluno=" + totalCargaHorariaCursadaAluno + ", totalCreditosCursadosAluno=" + totalCreditosCursadosAluno + ", totalCargaHorariaCursadaForaGradeAluno=" + totalCargaHorariaCursadaForaGradeAluno + ", totalCreditosCursadosForaGradeAluno=" + totalCreditosCursadosForaGradeAluno + ", totalCargaHorariaAlunoEstaCursandoAtualmente=" + totalCargaHorariaAlunoEstaCursandoAtualmente
				+ ", totalCreditosAlunoEstaCursandoAtualmente=" + totalCreditosAlunoEstaCursandoAtualmente + ", periodoLetivoEvolucaoAcademicaAluno=" + periodoLetivoEvolucaoAcademicaAluno + ", cargaHorariaPendente=" + cargaHorariaPendente + ", nrCreditosPendentes=" + nrCreditosPendentes + ", gradeIntegralizada=" + gradeIntegralizada + ", cursandoUltimoPeriodo=" + cursandoUltimoPeriodo + ", possivelFormandoSobOticaMatrizCurricular=" + possivelFormandoSobOticaMatrizCurricular + ", percentualIntegralizacaoPorCargaHoraria=" + percentualIntegralizacaoPorCargaHoraria + ", percentualIntegralizacaoPorCreditos=" + percentualIntegralizacaoPorCreditos + "]";
	}

	public Integer getTotalCargaHorariaGradeCurricular() {
		return this.getGradeCurricularVO().getTotalCargaHoraria();
	}

	public Integer getTotalCreditosGradeCurricular() {
		return this.getGradeCurricularVO().getTotalCreditos();
	}

	/**
	 * @return the totalCargaHorariaCursadaAluno
	 */
	public Integer getTotalCargaHorariaCursadaAluno() {
		if (totalCargaHorariaCursadaAluno == null) {
			totalCargaHorariaCursadaAluno = 0;
		}
		return totalCargaHorariaCursadaAluno;
	}

	/**
	 * @param totalCargaHorariaCursadaAluno
	 *            the totalCargaHorariaCursadaAluno to set
	 */
	public void setTotalCargaHorariaCursadaAluno(Integer totalCargaHorariaCursadaAluno) {
		this.totalCargaHorariaCursadaAluno = totalCargaHorariaCursadaAluno;
	}

	/**
	 * @return the totalCreditosCursadosAluno
	 */
	public Integer getTotalCreditosCursadosAluno() {
		if (totalCreditosCursadosAluno == null) {
			totalCreditosCursadosAluno = 0;
		}
		return totalCreditosCursadosAluno;
	}

	/**
	 * @param totalCreditosCursadosAluno
	 *            the totalCreditosCursadosAluno to set
	 */
	public void setTotalCreditosCursadosAluno(Integer totalCreditosCursadosAluno) {
		this.totalCreditosCursadosAluno = totalCreditosCursadosAluno;
	}

	/**
	 * @return the cargaHorariaPendente
	 */
	public Integer getCargaHorariaPendente() {
		if (cargaHorariaPendente == null) {
			cargaHorariaPendente = 0;
		}
		return cargaHorariaPendente;
	}

	/**
	 * @param cargaHorariaPendente
	 *            the cargaHorariaPendente to set
	 */
	public void setCargaHorariaPendente(Integer cargaHorariaPendente) {
		this.cargaHorariaPendente = cargaHorariaPendente;
	}

	/**
	 * @return the nrCreditosPendentes
	 */
	public Integer getNrCreditosPendentes() {
		if (nrCreditosPendentes == null) {
			nrCreditosPendentes = 0;
		}
		return nrCreditosPendentes;
	}

	/**
	 * @param nrCreditosPendentes
	 *            the nrCreditosPendentes to set
	 */
	public void setNrCreditosPendentes(Integer nrCreditosPendentes) {
		this.nrCreditosPendentes = nrCreditosPendentes;
	}

	/**
	 * @return the gradeIntegralizada
	 */
	public Boolean getGradeIntegralizada() {
		if (gradeIntegralizada == null) {
			gradeIntegralizada = Boolean.FALSE;
		}
		return gradeIntegralizada;
	}

	/**
	 * @param gradeIntegralizada
	 *            the gradeIntegralizada to set
	 */
	public void setGradeIntegralizada(Boolean gradeIntegralizada) {
		this.gradeIntegralizada = gradeIntegralizada;
	}

	/**
	 * @return the cursandoUltimoPeriodo
	 */
	public Boolean getCursandoUltimoPeriodo() {
		if (cursandoUltimoPeriodo == null) {
			cursandoUltimoPeriodo = Boolean.FALSE;
		}
		return cursandoUltimoPeriodo;
	}

	/**
	 * @param cursandoUltimoPeriodo
	 *            the cursandoUltimoPeriodo to set
	 */
	public void setCursandoUltimoPeriodo(Boolean cursandoUltimoPeriodo) {
		this.cursandoUltimoPeriodo = cursandoUltimoPeriodo;
	}

	/**
	 * @return the possivelFormandoSobOticaMatrizCurricular
	 */
	public Boolean getPossivelFormandoSobOticaMatrizCurricular() {
		if (possivelFormandoSobOticaMatrizCurricular == null) {
			possivelFormandoSobOticaMatrizCurricular = Boolean.FALSE;
		}
		return possivelFormandoSobOticaMatrizCurricular;
	}

	/**
	 * @param possivelFormandoSobOticaMatrizCurricular
	 *            the possivelFormandoSobOticaMatrizCurricular to set
	 */
	public void setPossivelFormandoSobOticaMatrizCurricular(Boolean possivelFormandoSobOticaMatrizCurricular) {
		this.possivelFormandoSobOticaMatrizCurricular = possivelFormandoSobOticaMatrizCurricular;
	}

	/**
	 * @return the percentualIntegralizacaoPorCargaHoraria
	 */
	public Double getPercentualIntegralizacaoPorCargaHoraria() {
		if (percentualIntegralizacaoPorCargaHoraria == null) {
			percentualIntegralizacaoPorCargaHoraria = 0.0;
		}
		return percentualIntegralizacaoPorCargaHoraria;
	}

	/**
	 * @param percentualIntegralizacaoPorCargaHoraria
	 *            the percentualIntegralizacaoPorCargaHoraria to set
	 */
	public void setPercentualIntegralizacaoPorCargaHoraria(Double percentualIntegralizacaoPorCargaHoraria) {
		this.percentualIntegralizacaoPorCargaHoraria = percentualIntegralizacaoPorCargaHoraria;
	}

	/**
	 * @return the percentualIntegralizacaoPorCreditos
	 */
	public Double getPercentualIntegralizacaoPorCreditos() {
		if (percentualIntegralizacaoPorCreditos == null) {
			percentualIntegralizacaoPorCreditos = 0.0;
		}
		return percentualIntegralizacaoPorCreditos;
	}

	/**
	 * @param percentualIntegralizacaoPorCreditos
	 *            the percentualIntegralizacaoPorCreditos to set
	 */
	public void setPercentualIntegralizacaoPorCreditos(Double percentualIntegralizacaoPorCreditos) {
		this.percentualIntegralizacaoPorCreditos = percentualIntegralizacaoPorCreditos;
	}

	/**
	 * @return the periodoLetivoComHistoricoAlunoVOs
	 */
	public List<PeriodoLetivoComHistoricoAlunoVO> getPeriodoLetivoComHistoricoAlunoVOs() {
		if (periodoLetivoComHistoricoAlunoVOs == null) {
			periodoLetivoComHistoricoAlunoVOs = new ArrayList<PeriodoLetivoComHistoricoAlunoVO>(0);
		}
		return periodoLetivoComHistoricoAlunoVOs;
	}

	/**
	 * @param periodoLetivoComHistoricoAlunoVOs
	 *            the periodoLetivoComHistoricoAlunoVOs to set
	 */
	public void setPeriodoLetivoComHistoricoAlunoVOs(List<PeriodoLetivoComHistoricoAlunoVO> periodoLetivoComHistoricoAlunoVOs) {
		this.periodoLetivoComHistoricoAlunoVOs = periodoLetivoComHistoricoAlunoVOs;
	}

	/**
	 * @return the gradeCurricularVO
	 */
	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	/**
	 * @param gradeCurricularVO
	 *            the gradeCurricularVO to set
	 */
	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	/**
	 * @return the todosHistoricosAlunoGradeCurricular
	 */
	public List<HistoricoVO> getTodosHistoricosAlunoGradeCurricular() {
		if (todosHistoricosAlunoGradeCurricular == null) {
			todosHistoricosAlunoGradeCurricular = new ArrayList<HistoricoVO>(0);
		}
		return todosHistoricosAlunoGradeCurricular;
	}

	/**
	 * @param todosHistoricosAlunoGradeCurricular
	 *            the todosHistoricosAlunoGradeCurricular to set
	 */
	public void setTodosHistoricosAlunoGradeCurricular(List<HistoricoVO> todosHistoricosAlunoGradeCurricular) {
		this.todosHistoricosAlunoGradeCurricular = todosHistoricosAlunoGradeCurricular;
	}

	/**
	 * @return the historicosDisciplinasAprovadasAlunoGradeCurricular
	 */
	public List<HistoricoVO> getHistoricosDisciplinasAprovadasAlunoGradeCurricular() {
		if (historicosDisciplinasAprovadasAlunoGradeCurricular == null) {
			historicosDisciplinasAprovadasAlunoGradeCurricular = new ArrayList<HistoricoVO>(0);
		}
		return historicosDisciplinasAprovadasAlunoGradeCurricular;
	}

	/**
	 * @param historicosDisciplinasAprovadasAlunoGradeCurricular
	 *            the historicosDisciplinasAprovadasAlunoGradeCurricular to set
	 */
	public void setHistoricosDisciplinasAprovadasAlunoGradeCurricular(List<HistoricoVO> historicosDisciplinasAprovadasAlunoGradeCurricular) {
		this.historicosDisciplinasAprovadasAlunoGradeCurricular = historicosDisciplinasAprovadasAlunoGradeCurricular;
	}

	/**
	 * @return the historicoDisciplinasForaGradeCurricular
	 */
	public List<HistoricoVO> getHistoricosDisciplinasForaGradeCurricular() {
		if (historicosDisciplinasForaGradeCurricular == null) {
			historicosDisciplinasForaGradeCurricular = new ArrayList<HistoricoVO>(0);
		}
		return historicosDisciplinasForaGradeCurricular;
	}

	/**
	 * @param historicoDisciplinasForaGradeCurricular
	 *            the historicoDisciplinasForaGradeCurricular to set
	 */
	public void setHistoricosDisciplinasForaGradeCurricular(List<HistoricoVO> historicosDisciplinasForaGradeCurricular) {
		this.historicosDisciplinasForaGradeCurricular = historicosDisciplinasForaGradeCurricular;
	}

	/**
	 * @return the totalCargaHorariaAlunoEstaCursandoAtualmente
	 */
	public Integer getTotalCargaHorariaAlunoEstaCursandoAtualmente() {
		if (totalCargaHorariaAlunoEstaCursandoAtualmente == null) {
			totalCargaHorariaAlunoEstaCursandoAtualmente = 0;
		}
		return totalCargaHorariaAlunoEstaCursandoAtualmente;
	}

	/**
	 * @param totalCargaHorariaAlunoEstaCursandoAtualmente
	 *            the totalCargaHorariaAlunoEstaCursandoAtualmente to set
	 */
	public void setTotalCargaHorariaAlunoEstaCursandoAtualmente(Integer totalCargaHorariaAlunoEstaCursandoAtualmente) {
		this.totalCargaHorariaAlunoEstaCursandoAtualmente = totalCargaHorariaAlunoEstaCursandoAtualmente;
	}

	/**
	 * @return the totalCreditosAlunoEstaCursandoAtualmente
	 */
	public Integer getTotalCreditosAlunoEstaCursandoAtualmente() {
		if (totalCreditosAlunoEstaCursandoAtualmente == null) {
			totalCreditosAlunoEstaCursandoAtualmente = 0;
		}
		return totalCreditosAlunoEstaCursandoAtualmente;
	}

	/**
	 * @param totalCreditosAlunoEstaCursandoAtualmente
	 *            the totalCreditosAlunoEstaCursandoAtualmente to set
	 */
	public void setTotalCreditosAlunoEstaCursandoAtualmente(Integer totalCreditosAlunoEstaCursandoAtualmente) {
		this.totalCreditosAlunoEstaCursandoAtualmente = totalCreditosAlunoEstaCursandoAtualmente;
	}

	/**
	 * @return the periodoLetivoEvolucaoAcademicaAluno
	 */
	public PeriodoLetivoVO getPeriodoLetivoEvolucaoAcademicaAluno() {
		if (periodoLetivoEvolucaoAcademicaAluno == null) {
			periodoLetivoEvolucaoAcademicaAluno = new PeriodoLetivoVO();
		}
		return periodoLetivoEvolucaoAcademicaAluno;
	}

	/**
	 * @param periodoLetivoEvolucaoAcademicaAluno
	 *            the periodoLetivoEvolucaoAcademicaAluno to set
	 */
	public void setPeriodoLetivoEvolucaoAcademicaAluno(PeriodoLetivoVO periodoLetivoEvolucaoAcademicaAluno) {
		this.periodoLetivoEvolucaoAcademicaAluno = periodoLetivoEvolucaoAcademicaAluno;
	}

	/**
	 * Método que obtem os dados completos do PeriodoLetivo da GradeCurricular
	 * (que estão todos já montados)
	 */
	public PeriodoLetivoVO obterDadosCompletosPeriodoLetivoComBaseGradeCurricularCurso(Integer periodoAtual) {
		PeriodoLetivoVO periodoVO = new PeriodoLetivoVO();
		for (PeriodoLetivoVO obj : this.getGradeCurricularVO().getPeriodoLetivosVOs()) {
			if (obj.getPeriodoLetivo().equals(periodoAtual)) {
				periodoVO = obj;
				break;
			}
		}
		return periodoVO;
	}

	public void adicionarHistoricoComoDisciplinaForaGrade(HistoricoVO hist) {
		if (!hist.getHistoricoDisciplinaFazParteComposicao()) {
			this.getHistoricosDisciplinasForaGradeCurricular().add(hist);
		}
	}
	
	public Boolean verificarExistenciaHistoricoForaGradeMatrizDestino(Integer disciplina, Integer cargaHoraria) {
		for (HistoricoVO historicoForaGradeVO : getHistoricosDisciplinasForaGradeCurricular()) {
			if (historicoForaGradeVO.getDisciplina().getCodigo().equals(disciplina)
					&& historicoForaGradeVO.getCargaHorariaDisciplina().equals(cargaHoraria)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Inicializar todos os PeriodosLetivosComHistorico com base nos
	 * periodosLetivos da grade
	 */
	public void inicializarPeriodoLetivoComHistoricoAlunoVOComPeriodoLetivoVOGrade(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		if (this.getPeriodoLetivoComHistoricoAlunoVOs().size() > 0) {
			this.getPeriodoLetivoComHistoricoAlunoVOs().clear();
			this.setPeriodoLetivoComHistoricoAlunoVOs(null);
		}
		for (PeriodoLetivoVO periodo : this.getGradeCurricularVO().getPeriodoLetivosVOs()) {
			PeriodoLetivoComHistoricoAlunoVO periodoComHistorico = new PeriodoLetivoComHistoricoAlunoVO();
			periodoComHistorico.setPeriodoLetivoVO(periodo);
			for (GradeDisciplinaVO gradeDisciplinaVO : periodo.getGradeDisciplinaVOs()) {
//				if((gradeDisciplinaVO.getTipoDisciplina().equals("OP") && configuracaoAcademicoVO.getPermitirAproveitamentoDisciplinasOptativas()) || !gradeDisciplinaVO.getTipoDisciplina().equals("OP")) {
					GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico = new GradeDisciplinaComHistoricoAlunoVO();
					gradeDisciplinaComHistorico.setGradeDisciplinaVO(gradeDisciplinaVO);
					periodoComHistorico.getGradeDisciplinaComHistoricoAlunoVOs().add(gradeDisciplinaComHistorico);
//				}
			}
			for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodo.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
				GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeGrupoOptativaComHistorico = new GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO();
				periodoComHistorico.setPeriodoLetivoVO(periodo);
				gradeGrupoOptativaComHistorico.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO);
				periodoComHistorico.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO().add(gradeGrupoOptativaComHistorico);
			}
			this.getPeriodoLetivoComHistoricoAlunoVOs().add(periodoComHistorico);
		}
	}

	public PeriodoLetivoComHistoricoAlunoVO obterPeriodoLetivoComHistoricoAlunoVO(Integer codigoPeriodoLetivo) {
		PeriodoLetivoComHistoricoAlunoVO periodoRetornar = null;
		for (PeriodoLetivoComHistoricoAlunoVO periodo : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodo.getPeriodoLetivoVO().getCodigo().equals(codigoPeriodoLetivo)) {
				periodoRetornar = periodo;
				break;
			}
		}
		return periodoRetornar;
	}

	/**
	 * Método responsável por distribuir os historicos do aluno para cada
	 * GradeDisciplina. Ou seja, iremos associar os historicos de cada
	 * disciplina com a sua respectiva GradeDisciplinaVO, já definindo também
	 * históricoAtual a ser considerado para a disciplina (um aluno pode ter
	 * vários históricos para uma disciplina)
	 */
	public void montarGradeDisciplinaComHistoricoAlunoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		inicializarPeriodoLetivoComHistoricoAlunoVOComPeriodoLetivoVOGrade(configuracaoAcademicoVO);
		PeriodoLetivoComHistoricoAlunoVO periodoAtualComHistorico = null;
		for (HistoricoVO hist : this.getTodosHistoricosAlunoGradeCurricular()) {
			
			if (hist.getAprovado()) {
				this.getHistoricosDisciplinasAprovadasAlunoGradeCurricular().add(hist);
			}
			if (hist.getReprovado()) {
				this.getHistoricosDisciplinasAlunoReprovouGradeCurricular().add(hist);
			}
			if (hist.getCursando()) {
				this.getHistoricosDisciplinasAlunoCursandoGradeCurricular().add(hist);
			}
			Integer codigoPeriodoLetivoDisciplina = 0;
			if (!hist.getGradeDisciplinaVO().getCodigo().equals(0)) {
				// Se trata-se de um histórico de uma GradeDisciplina temos que
				// encaixar o histórico
				// no período da matriz curricular da Gradedisciplina. E não o
				// período letivo do histórico.
				// Isto dará maior garantir que cada histórico será agrupadado
				// no período letivo correto.
				// Encontramos por que encontramos históricos cujo
				// periodoLetivoMatriz não batia com o período
				// no qual a disciplina de fato estava na matriz
				codigoPeriodoLetivoDisciplina = hist.getGradeDisciplinaVO().getPeriodoLetivoVO().getCodigo();
			} else if(hist.getHistoricoDisciplinaFazParteComposicao()) {
				boolean encontrado = false;
				for(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO:getPeriodoLetivoComHistoricoAlunoVOs()){
					if(encontrado){
						break;
					}
					for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico : periodoLetivoComHistoricoAlunoVO.getGradeDisciplinaComHistoricoAlunoVOs()) {
						if(encontrado){
							break;
						}
						for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaComHistorico.getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs()){
							if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(hist.getDisciplina().getCodigo())) {
								gradeDisciplinaCompostaVO.adicionarHistoricoParaGradeDisciplina(hist);
								encontrado = true;
								break;
							}
						}
					}
					if(encontrado){
						break;
					}
					for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeGrupoOptativaComHistorico : periodoLetivoComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
						if(encontrado){
							break;
						}
						for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs()){
							if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(hist.getDisciplina().getCodigo())) {
								gradeDisciplinaCompostaVO.adicionarHistoricoParaGradeDisciplina(hist);
								encontrado = true;
								break;
							}
						}
					}
				}
			} else {
				codigoPeriodoLetivoDisciplina = hist.getPeriodoLetivoMatrizCurricular().getCodigo();
			}
			if ((codigoPeriodoLetivoDisciplina != null) && (!codigoPeriodoLetivoDisciplina.equals(0))) {
				periodoAtualComHistorico = obterPeriodoLetivoComHistoricoAlunoVO(codigoPeriodoLetivoDisciplina);
				if ((periodoAtualComHistorico != null) && (!hist.getHistoricoDisciplinaForaGrade())) {
					// Se entrarmos aqui é por que encontramos o periodo no qual
					// o historico deve ser
					// associado e também o historico nao refere-se a uma
					// inclusao fora da grade
					boolean historicoAdicionado = periodoAtualComHistorico.adicionarHistoricoDisciplinaDoPeriodoLetivo(hist);
					if (!historicoAdicionado) {
						// se o historico nao foi vinculado a nenhuma
						// gradeDisciplinaVO e nem a uma
						// gradeCurriculaGrupoOptativaDisciplinaVO
						// entao o mesmo é adicionado como disciplina fora da
						// grade.
						adicionarHistoricoComoDisciplinaForaGrade(hist);
					}
				} else if (!hist.getHistoricoEquivalente()) {
					// Se entrarmos aqui é por que encontramos uma disciplina
					// que esta vinculada
					// a um periodo letivo que nao pertence a matriz curricular,
					// por isto
					// a mesma será tratada como foraDaGrade (se entrar aqui é
					// em função de
					// alguma inconsistencia no historico do aluno).
					adicionarHistoricoComoDisciplinaForaGrade(hist);
				}
			} else {
				// Se entrarmos aqui é por que encontramos uma disciplina fora
				// da gradeCurricular
				adicionarHistoricoComoDisciplinaForaGrade(hist);
			}
		}
		atualizarDadosHistoricoAlunoNaGradeCurricular(configuracaoAcademicoVO);
	}

	public Boolean verificarAlunoPossivelFormandoSobOticaMatrizCurricular() {
		Integer totalCargaHorariaAluno = totalCargaHorariaCursadaAluno + totalCargaHorariaAlunoEstaCursandoAtualmente;
		if (totalCargaHorariaAluno.compareTo(getGradeCurricularVO().getTotalCargaHoraria()) >= 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Método responsável por calcular os totais e informacoes relativas a
	 * evolucao academica do aluno na gradeCurricular de seu curso.
	 */
	public void atualizarDadosHistoricoAlunoNaGradeCurricular(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		totalCargaHorariaCursadaAluno = 0;
		totalCreditosCursadosAluno = 0;
		totalCargaHorariaAlunoEstaCursandoAtualmente = 0;
		totalCreditosAlunoEstaCursandoAtualmente = 0;

		for (PeriodoLetivoComHistoricoAlunoVO periodoAtualComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			periodoAtualComHistorico.atualizarDadosHistoricoAlunoNoPeriodoLetivo();
			totalCargaHorariaCursadaAluno += periodoAtualComHistorico.getTotalCargaHorariaCursadaAluno();
			totalCreditosCursadosAluno += periodoAtualComHistorico.getTotalCreditosCursadosAluno();

			totalCargaHorariaAlunoEstaCursandoAtualmente += periodoAtualComHistorico.getTotalCargaHorariaAlunoEstaCursandoAtualmente();
			totalCreditosAlunoEstaCursandoAtualmente += periodoAtualComHistorico.getTotalCreditosAlunoEstaCursandoAtualmente();
		}
		getGradeCurricularVO().atualizarTotalCargaHorariaETotalCreditoMatrizCurricular(false);

		Integer totalCargaHorariaExigida = getGradeCurricularVO().getTotalCargaHorariaDisciplinasObrigatorias()+getGradeCurricularVO().getTotalCargaHorariaOptativaExigida();
//		cargaHorariaPendente = getGradeCurricularVO().getTotalCargaHoraria() - totalCargaHorariaCursadaAluno;
		cargaHorariaPendente = totalCargaHorariaExigida - totalCargaHorariaCursadaAluno;
		if (cargaHorariaPendente < 0) {
			// pode ficar negativa caso o aluno tenha feito inclusoes /
			// aproveitamentos que vao além da matriz curricular
			cargaHorariaPendente = 0;
		}
		nrCreditosPendentes = getGradeCurricularVO().getTotalCreditos() - totalCreditosCursadosAluno;
		if (nrCreditosPendentes < 0) {
			// pode ficar negativa caso o aluno tenha feito inclusoes /
			// aproveitamentos que vao além da matriz curricular
			nrCreditosPendentes = 0;
		}

		percentualIntegralizacaoPorCargaHoraria = 0.0;
		percentualIntegralizacaoPorCreditos = 0.0;

		if (totalCargaHorariaCursadaAluno > 0) {
			percentualIntegralizacaoPorCargaHoraria = Uteis.arrendondarForcando2CadasDecimais((totalCargaHorariaCursadaAluno * 100) / totalCargaHorariaExigida);
		} else {
			percentualIntegralizacaoPorCargaHoraria = 0.0;
		}

		if (totalCreditosCursadosAluno > 0 && this.getGradeCurricularVO().getTotalCreditos() > 0) {
			percentualIntegralizacaoPorCreditos = Uteis.arrendondarForcando2CadasDecimais((totalCreditosCursadosAluno * 100) / this.getGradeCurricularVO().getTotalCreditos());
		} else {
			percentualIntegralizacaoPorCreditos = 0.0;
		}

		gradeIntegralizada = (getGradeCurricularVO().getTotalCargaHorariaDisciplinasObrigatorias() - getTotalCargaHorariaDisciplinaObrigatoriaCumprida()) <= 0 &&
				(getGradeCurricularVO().getTotalCargaHorariaOptativaExigida() - getTotalCargaHorariaDisciplinaOptativaCumprida()) <= 0;

		totalCargaHorariaCursadaForaGradeAluno = 0;
		totalCreditosCursadosForaGradeAluno = 0;
		for (HistoricoVO historicoForaGrade : this.getHistoricosDisciplinasForaGradeCurricular()) {
			totalCargaHorariaCursadaForaGradeAluno += historicoForaGrade.getCargaHorariaDisciplina();
			totalCreditosCursadosForaGradeAluno += historicoForaGrade.getCreditoDisciplina();
		}

		possivelFormandoSobOticaMatrizCurricular = verificarAlunoPossivelFormandoSobOticaMatrizCurricular();
		periodoLetivoEvolucaoAcademicaAluno = definirPeriodoLetivoEvolucaoAcademicaAluno(configuracaoAcademicoVO);
	}

	public PeriodoLetivoComHistoricoAlunoVO consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(Integer periodoLetivo) throws Exception {
		Iterator<PeriodoLetivoComHistoricoAlunoVO> i = getPeriodoLetivoComHistoricoAlunoVOs().iterator();
		while (i.hasNext()) {
			PeriodoLetivoComHistoricoAlunoVO objExistente = (PeriodoLetivoComHistoricoAlunoVO) i.next();
			if (objExistente.getPeriodoLetivoVO().getPeriodoLetivo().equals(periodoLetivo)) {
				return objExistente;
			}
		}
		return null;
	}

	/*
     * 
     */
	public PeriodoLetivoVO definirPeriodoLetivoEvolucaoAcademicaAluno(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		int qtdCHCreditosCumpridosPeloAluno = 0;
		if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH() && configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("CH")) {
			qtdCHCreditosCumpridosPeloAluno = this.getTotalCargaHorariaCursadaAluno();
		} else {
			qtdCHCreditosCumpridosPeloAluno = this.getTotalCreditosCursadosAluno();
		}
		PeriodoLetivoComHistoricoAlunoVO periodoIntegralizadoPeloAlunoSobOticaCHCreditos = null;
		if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH() && configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("QT")
				&& configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo() > 0.0) {
			int qtdePendente = 0;
			for (PeriodoLetivoComHistoricoAlunoVO periodoComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
				qtdePendente += periodoComHistorico.getDisciplinasPendentesAlunoPeriodoLetivo().size();
				if(qtdePendente >= configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo()
						|| (periodoComHistorico.getDisciplinasPendentesAlunoPeriodoLetivo().size() == periodoComHistorico.getPeriodoLetivoVO().getGradeDisciplinaVOs().size())) {
					periodoIntegralizadoPeloAlunoSobOticaCHCreditos = periodoComHistorico;
					break;
				}
			}
			if((periodoIntegralizadoPeloAlunoSobOticaCHCreditos == null || !Uteis.isAtributoPreenchido(periodoIntegralizadoPeloAlunoSobOticaCHCreditos.getPeriodoLetivoVO())) && qtdePendente < configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo()) {
				periodoIntegralizadoPeloAlunoSobOticaCHCreditos = this.getPeriodoLetivoComHistoricoAlunoVOs().get(this.getPeriodoLetivoComHistoricoAlunoVOs().size()-1);
			}
		}else {
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			int qtdCHCreditoPeriodo = periodoComHistorico.getPeriodoLetivoVO().getTotalCargaHoraria();
			int saldoCHCreditoAposConsiderarPeriodo = qtdCHCreditosCumpridosPeloAluno - qtdCHCreditoPeriodo;
			if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH() && configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("CR")) {
				// se calculo deve ser baseado em creditos variaveis sao
				// reiniciadas com creditos do periodo
				qtdCHCreditoPeriodo = periodoComHistorico.getPeriodoLetivoVO().getTotalCreditos();
				// se calculo por creditos recalculados o saldo com base em
				// creditos
				saldoCHCreditoAposConsiderarPeriodo = qtdCHCreditosCumpridosPeloAluno - qtdCHCreditoPeriodo;
			}
			if (saldoCHCreditoAposConsiderarPeriodo >= 0) {
				// Se positivo significa que a carga horaria/creditos cursados
				// pelo aluno, atende 100%
				// a carga horaria exigida pelo periodo letivo, logo, podemos
				// considerar que o
				// evoluiu academicamente por este período.
				periodoIntegralizadoPeloAlunoSobOticaCHCreditos = periodoComHistorico;
				// Deduzimos da qtd de CH/Creditos do periodo para verificarmos
				// na proxima interação
				// Se a qtd de horas restantes é suficiente para cumprir o
				// próximo período.
				qtdCHCreditosCumpridosPeloAluno = qtdCHCreditosCumpridosPeloAluno - qtdCHCreditoPeriodo;
			} else {
				// Tornando o saldo positivo para efeitos de calculo.
				saldoCHCreditoAposConsiderarPeriodo = saldoCHCreditoAposConsiderarPeriodo * -1;
				if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH() && !configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("QT")) {
					Double percentualCumprirPeriodoConsideraloCumprido = new Double(configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo().doubleValue());					
					Double percCumpridoPeriodoAlunoCHCreditoPeriodo = Uteis.arrendondarForcando2CadasDecimais((qtdCHCreditosCumpridosPeloAluno * 100) / qtdCHCreditoPeriodo);
					
					if (percCumpridoPeriodoAlunoCHCreditoPeriodo.compareTo(percentualCumprirPeriodoConsideraloCumprido) >= 0) {
						// se o aluno conseguiu cumprir a carga horaria mínima
						// deste periodo, então o mesmo
						// pode ser matricular até o próximo período. Pois,
						// mesmo matriculado-se no próximo
						// período, ainda cabe ao mesmo, entrar no período
						// seguinte e recuperar o carga
						// horária perdida deste período (pois está dentro de um
						// percentual aceitável)
						int periodoSeguinte = periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo() + 1;
						if (periodoSeguinte > this.getGradeCurricularVO().obterNumeroMaiorPeriodoLetivo()) {
							periodoSeguinte = this.getGradeCurricularVO().obterNumeroMaiorPeriodoLetivo();
						}
						periodoIntegralizadoPeloAlunoSobOticaCHCreditos = this.consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(periodoSeguinte);
						// periodoIntegralizadoPeloAlunoSobOticaCHCreditos =
						// periodoComHistorico;
						break;
					} else {
						// Se o aluno nao consegiu cumprir a carga horaria
						// mínima para considerar o periodo
						// letivo cursado, então deve ser assumido o periodo em
						// questão.
						// int periodoAnterior =
						// periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo()
						// - 1;
						// if (periodoAnterior < 1) {
						// periodoAnterior = 1;
						// }
						// periodoIntegralizadoPeloAlunoSobOticaCHCreditos =
						// this.consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(periodoAnterior);
						periodoIntegralizadoPeloAlunoSobOticaCHCreditos = periodoComHistorico;
						break;
					}
				} else {
					// como não está sendo controlado a evolucao academica do
					// aluno, não temos
					// que verificar se o aluno cumpriu determinado percentual
					// deste período letivo
					// Como ele não o fez de forma integral (100%) entao o sei
					// irá considerar que
					// academicamente ele está no período anterior.
					int periodoAnterior = periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo() - 1;
					if (periodoAnterior < 1) {
						periodoAnterior = 1;
					}
					for (int i = periodoAnterior; i > 0; i--) {
						periodoIntegralizadoPeloAlunoSobOticaCHCreditos = this.consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(i);
						if (Uteis.isAtributoPreenchido(periodoIntegralizadoPeloAlunoSobOticaCHCreditos)) {
							break;
						}
					}
					break;
				}
			}		
		}
		}
		if (periodoIntegralizadoPeloAlunoSobOticaCHCreditos == null) {
			return new PeriodoLetivoVO();
		}
		return periodoIntegralizadoPeloAlunoSobOticaCHCreditos.getPeriodoLetivoVO();
	}
	
	
	/*
     * 
     */
	public boolean validarAlunoAptoAvancarProximoPeriodoLetivo(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
		 
		int qtdCHCreditosCumpridosPeloAluno = 0;
		if (configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("CH")) {
			qtdCHCreditosCumpridosPeloAluno = this.getTotalCargaHorariaCursadaAluno();
		} else {
			qtdCHCreditosCumpridosPeloAluno = this.getTotalCreditosCursadosAluno();
		}
		PeriodoLetivoComHistoricoAlunoVO periodoIntegralizadoPeloAlunoSobOticaCHCreditos = null;
		if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH() && configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("QT")
				&& configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo() > 0.0) {
			int qtdePendente = 0;
			for (PeriodoLetivoComHistoricoAlunoVO periodoComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
				qtdePendente += periodoComHistorico.getDisciplinasPendentesAlunoPeriodoLetivo().size();
				if(qtdePendente >= configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo()) {
					return false;
				}
			}
			if(qtdePendente == 0) {
				return true;
			}
		}else {
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			int qtdCHCreditoPeriodo = periodoComHistorico.getPeriodoLetivoVO().getTotalCargaHoraria();
			int saldoCHCreditoAposConsiderarPeriodo = qtdCHCreditosCumpridosPeloAluno - qtdCHCreditoPeriodo;
			if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH() && configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("CR")) {
				// se calculo deve ser baseado em creditos variaveis sao
				// reiniciadas com creditos do periodo
				qtdCHCreditoPeriodo = periodoComHistorico.getPeriodoLetivoVO().getTotalCreditos();
				// se calculo por creditos recalculados o saldo com base em
				// creditos
				saldoCHCreditoAposConsiderarPeriodo = qtdCHCreditosCumpridosPeloAluno - qtdCHCreditoPeriodo;
			}
			if (saldoCHCreditoAposConsiderarPeriodo >= 0) {
				// Se positivo significa que a carga horaria/creditos cursados
				// pelo aluno, atende 100%
				// a carga horaria exigida pelo periodo letivo, logo, podemos
				// considerar que o
				// evoluiu academicamente por este período.
				periodoIntegralizadoPeloAlunoSobOticaCHCreditos = periodoComHistorico;
				// Deduzimos da qtd de CH/Creditos do periodo para verificarmos
				// na proxima interação
				// Se a qtd de horas restantes é suficiente para cumprir o
				// próximo período.
				qtdCHCreditosCumpridosPeloAluno = qtdCHCreditosCumpridosPeloAluno - qtdCHCreditoPeriodo;
			} else {
				// Tornando o saldo positivo para efeitos de calculo.
				saldoCHCreditoAposConsiderarPeriodo = saldoCHCreditoAposConsiderarPeriodo * -1;
				if (configuracaoAcademicoVO.getControlarAvancoPeriodoPorCreditoOuCH()  && !configuracaoAcademicoVO.getTipoControleAvancoPeriodoPorCreditoOuCH().equals("QT")) {
					Double percentualCumprirPeriodoConsideraloCumprido = new Double(configuracaoAcademicoVO.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo().doubleValue());					
					Double percCumpridoPeriodoAlunoCHCreditoPeriodo = Uteis.arrendondarForcando2CadasDecimais((qtdCHCreditosCumpridosPeloAluno * 100) / qtdCHCreditoPeriodo);
					
					if (percCumpridoPeriodoAlunoCHCreditoPeriodo.compareTo(percentualCumprirPeriodoConsideraloCumprido) >= 0) {
						// se o aluno conseguiu cumprir a carga horaria mínima
						// deste periodo, então o mesmo
						// pode ser matricular até o próximo período. Pois,
						// mesmo matriculado-se no próximo
						// período, ainda cabe ao mesmo, entrar no período
						// seguinte e recuperar o carga
						// horária perdida deste período (pois está dentro de um
						// percentual aceitável)
						int periodoSeguinte = periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo() + 1;
						if (periodoSeguinte > this.getGradeCurricularVO().obterNumeroMaiorPeriodoLetivo()) {
							periodoSeguinte = this.getGradeCurricularVO().obterNumeroMaiorPeriodoLetivo();
						}
						periodoIntegralizadoPeloAlunoSobOticaCHCreditos = this.consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(periodoSeguinte);
						// periodoIntegralizadoPeloAlunoSobOticaCHCreditos =
						// periodoComHistorico;
						break;
					} else {
						// Se o aluno nao consegiu cumprir a carga horaria
						// mínima para considerar o periodo
						// letivo cursado, então deve ser assumido o periodo em
						// questão.
						// int periodoAnterior =
						// periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo()
						// - 1;
						// if (periodoAnterior < 1) {
						// periodoAnterior = 1;
						// }
						// periodoIntegralizadoPeloAlunoSobOticaCHCreditos =
						// this.consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(periodoAnterior);
						periodoIntegralizadoPeloAlunoSobOticaCHCreditos = periodoComHistorico;
						return false;
					}
				} else {
					// como não está sendo controlado a evolucao academica do
					// aluno, não temos
					// que verificar se o aluno cumpriu determinado percentual
					// deste período letivo
					// Como ele não o fez de forma integral (100%) entao o sei
					// irá considerar que
					// academicamente ele está no período anterior.
					int periodoAnterior = periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo() - 1;
					if (periodoAnterior < 1) {
						periodoAnterior = 1;
					}
					for (int i = periodoAnterior; i > 0; i--) {
						periodoIntegralizadoPeloAlunoSobOticaCHCreditos = this.consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(i);
						if (Uteis.isAtributoPreenchido(periodoIntegralizadoPeloAlunoSobOticaCHCreditos)) {
							break;
						}
					}
					break;
				}
			}	
		}
		}		
		return true;
	}


	/**
	 * @return the totalCargaHorariaCursadaForaGradeAluno
	 */
	public Integer getTotalCargaHorariaCursadaForaGradeAluno() {
		if (totalCargaHorariaCursadaForaGradeAluno == null) {
			totalCargaHorariaCursadaForaGradeAluno = 0;
		}
		return totalCargaHorariaCursadaForaGradeAluno;
	}

	/**
	 * @param totalCargaHorariaCursadaForaGradeAluno
	 *            the totalCargaHorariaCursadaForaGradeAluno to set
	 */
	public void setTotalCargaHorariaCursadaForaGradeAluno(Integer totalCargaHorariaCursadaForaGradeAluno) {
		this.totalCargaHorariaCursadaForaGradeAluno = totalCargaHorariaCursadaForaGradeAluno;
	}

	/**
	 * @return the totalCreditosCursadosForaGradeAluno
	 */
	public Integer getTotalCreditosCursadosForaGradeAluno() {
		if (totalCreditosCursadosForaGradeAluno == null) {
			totalCreditosCursadosForaGradeAluno = 0;
		}
		return totalCreditosCursadosForaGradeAluno;
	}

	/**
	 * @param totalCreditosCursadosForaGradeAluno
	 *            the totalCreditosCursadosForaGradeAluno to set
	 */
	public void setTotalCreditosCursadosForaGradeAluno(Integer totalCreditosCursadosForaGradeAluno) {
		this.totalCreditosCursadosForaGradeAluno = totalCreditosCursadosForaGradeAluno;
	}

	/**
	 * Verifica se um alnuo está reprovado em uma disciplina. Para fazer esta
	 * verificacao, primeiro, verificamos se o aluno não está aprovado na
	 * disciplina. Pois, pode ocorrer do mesmo ter sido reprovado no passado,
	 * mas posteriormente ter sido aprovado em um novo periodo letivo. Caso
	 * contrário, verificamos se existe um ou mais histórico que registra esta
	 * reprovação na disciplina. Estes historicos de reprovacao sao retornados
	 * pelo método. Se a lista for nulla é por que o aluno nunca reprovou na
	 * disciplina.
	 */
	public List<HistoricoVO> verificarAlunoReprovadoDisciplina(Integer codigoDisciplina) {
		HistoricoVO hist = verificarAlunoAprovadoDisciplina(codigoDisciplina);
		if (hist != null) {
			return null;
		}
		List<HistoricoVO> listaHistoricosReprovacao = new ArrayList<HistoricoVO>(0);
		for (HistoricoVO hist2 : this.getTodosHistoricosAlunoGradeCurricular()) {
			if (hist2.getDisciplina().getCodigo().equals(codigoDisciplina)) {
				if (hist2.getReprovado()) {
					listaHistoricosReprovacao.add(hist2);
				}
			}
		}
		if (listaHistoricosReprovacao.isEmpty()) {
			return null;
		}
		return listaHistoricosReprovacao;
	}

	/**
	 * Método utilizado no controle de disciplina em Regime Especial. Um aluno
	 * pode estudar uma disciplina em regime especial, quando já cursou uma
	 * disciplina e foi reprovado por nota (e nao por falta ou abandono). Neste
	 * caso permiti-se o mesmo estudá-la novamente em um regime especial.
	 * 
	 * @param codigoDisciplina
	 * @return
	 */
	public Boolean verificarAlunoJaCursouDisciplinaEEstaReprovadoPorNota(Integer codigoDisciplina) {
		List<HistoricoVO> listaHistReprovacao = verificarAlunoReprovadoDisciplina(codigoDisciplina);
		if ((listaHistReprovacao != null) && (!listaHistReprovacao.isEmpty())) {
			for (HistoricoVO hist : listaHistReprovacao) {
				if (hist.getAlunoReprovadoPorNotaENaoPorFaltaOuAbandono()) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public HistoricoVO verificarAlunoCursandoDisciplina(Integer codigoDisciplina) {
		for (HistoricoVO hist : this.getHistoricosDisciplinasAlunoCursandoGradeCurricular()) {
			if (hist.getDisciplina().getCodigo().equals(codigoDisciplina)) {
				return hist;
			}
		}
		return null;
	}

	public HistoricoVO verificarAlunoAprovadoDisciplina(Integer codigoDisciplina) {
		for (HistoricoVO hist : this.getHistoricosDisciplinasAprovadasAlunoGradeCurricular()) {
			if (hist.getDisciplina().getCodigo().equals(codigoDisciplina)) {
				return hist;
			}
		}
		for (HistoricoVO hist : this.getHistoricosDisciplinasForaGradeCurricular()) {
			if (hist.getDisciplina().getCodigo().equals(codigoDisciplina)) {
				if (hist.getAprovado()) {
					return hist;
				}
			}
		}
		return null;
	}

	/**
	 * @return the historicosDisciplinasAlunoCursandoGradeCurricular
	 */
	public List<HistoricoVO> getHistoricosDisciplinasAlunoCursandoGradeCurricular() {
		if (historicosDisciplinasAlunoCursandoGradeCurricular == null) {
			historicosDisciplinasAlunoCursandoGradeCurricular = new ArrayList<HistoricoVO>(0);
		}
		return historicosDisciplinasAlunoCursandoGradeCurricular;
	}

	/**
	 * @param historicosDisciplinasAlunoCursandoGradeCurricular
	 *            the historicosDisciplinasAlunoCursandoGradeCurricular to set
	 */
	public void setHistoricosDisciplinasAlunoCursandoGradeCurricular(List<HistoricoVO> historicosDisciplinasAlunoCursandoGradeCurricular) {
		this.historicosDisciplinasAlunoCursandoGradeCurricular = historicosDisciplinasAlunoCursandoGradeCurricular;
	}

	/**
	 * Método responsável por determinar a carga horaria ou créditos que um
	 * aluno deve até determinado período, no que tange a Grupo de Optativas. Ou
	 * seja, se no primeiro período por exemplo, o aluno tinha que ter cursado
	 * 40 horas em disciplinas optativas, mas o mesmo não o fez. Então este
	 * método deverá identificar esta carga horária pendente. Possibilitando
	 * assim que o usuário possa incluir disciplinas de grupo de optativas do
	 * passado do mesmo.
	 */
	public Integer calcularTotalCargaHorariaPendenteAlunoGrupoOptativaAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean considerarDisciplinaCursandoComoRealizada) {
		if (numeroPeriodoLetivo == null || numeroPeriodoLetivo.equals(0)) {
			return 0;
		}
		int totalPendente = 0;

		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			totalPendente += periodoComHist.getPeriodoLetivoVO().getNumeroCargaHorariaOptativa();
			if (periodoComHist.getPeriodoLetivoVO().getPeriodoLetivo().equals(numeroPeriodoLetivo)) {
				break;
			}
		}
		return totalPendente;
	}

	public Integer calcularTotalCargaHorariaCreditoPendenteAlunoAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean considerarDisciplinaCursandoComoRealizada,  String calculoPorCreditoOuCargaHoraria) {
		if (numeroPeriodoLetivo == null || numeroPeriodoLetivo.equals(0)) {
			return 0;
		}
		if (calculoPorCreditoOuCargaHoraria == null || calculoPorCreditoOuCargaHoraria.equals("")) {
			calculoPorCreditoOuCargaHoraria = "CH";
		}
		boolean calcularPorCreditos = (calculoPorCreditoOuCargaHoraria.equals("CR"));
		int totalPendente = 0;
		int totalCHCreditoPeriodo = 0;
		int totalAprovadoPeriodo = 0;
		int totalCursadoPeriodo = 0;
		int totalPendentePeriodo = 0;

		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (!calcularPorCreditos) {
				totalCHCreditoPeriodo = periodoComHist.getPeriodoLetivoVO().getTotalCargaHoraria();
				totalAprovadoPeriodo = periodoComHist.getTotalCargaHorariaCursadaAluno();
				totalCursadoPeriodo = periodoComHist.getTotalCargaHorariaAlunoEstaCursandoAtualmente();
				if (considerarDisciplinaCursandoComoRealizada) {
					totalAprovadoPeriodo = totalAprovadoPeriodo + totalCursadoPeriodo;
				}
				totalPendentePeriodo = totalCHCreditoPeriodo - totalAprovadoPeriodo;
			} else {
				totalCHCreditoPeriodo = periodoComHist.getPeriodoLetivoVO().getTotalCreditos();
				totalAprovadoPeriodo = periodoComHist.getTotalCreditosCursadosAluno();
				totalCursadoPeriodo = periodoComHist.getTotalCreditosAlunoEstaCursandoAtualmente();
				if (considerarDisciplinaCursandoComoRealizada) {
					totalAprovadoPeriodo = totalAprovadoPeriodo + totalCursadoPeriodo;
				}
				totalPendentePeriodo = totalCHCreditoPeriodo - totalAprovadoPeriodo;
			}
			totalPendente = totalPendente + totalPendentePeriodo;
			if (periodoComHist.getPeriodoLetivoVO().getPeriodoLetivo().equals(numeroPeriodoLetivo)) {
				break;
			}
		}
		return totalPendente;
	}

	public Integer calcularNrDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa) {
		if (numeroPeriodoLetivo == null || numeroPeriodoLetivo.equals(0)) {
			return 0;
		}
		int nrDisciplinasPendentes = 0;
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				int nrPendentes = 0;
				if(desconsiderarDisciplinaOptativa != null && desconsiderarDisciplinaOptativa){
					for(GradeDisciplinaVO gradeDisciplinaVO:periodoComHist.getDisciplinasPendentesAlunoPeriodoLetivo()){
						if(!gradeDisciplinaVO.getIsDisciplinaOptativa()){
							nrPendentes += 1;
						}
					}
				}else{
					nrPendentes = periodoComHist.getDisciplinasPendentesAlunoPeriodoLetivo().size();
				}
				nrDisciplinasPendentes = nrDisciplinasPendentes + nrPendentes;
//                break;
            }else{
            	break;
            }
		}
		return nrDisciplinasPendentes;
	}
        
	public void atualizarSituacaoMapaEquivalenciaDisciplinaCursadaVOAluno(MapaEquivalenciaDisciplinaCursadaVO disciplinaCursar) {
		HistoricoVO hist = this.verificarAlunoAprovadoDisciplina(disciplinaCursar.getDisciplinaVO().getCodigo());
		if (hist != null) {
			// entao disciplina aprovada
			disciplinaCursar.setHistorico(hist);
			return;
		} else {
			// se ao menos uma disciplina o aluno nao está aprovado, entao o
			// mapa não está cumprido
			hist = this.verificarAlunoCursandoDisciplina(disciplinaCursar.getDisciplinaVO().getCodigo());
			if (hist != null) {
				disciplinaCursar.setHistorico(hist);
				return;
			} else {
				hist = new HistoricoVO();
				hist.setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
				disciplinaCursar.setHistorico(hist);
			}
		}
	}

	public void atualizarSituacaoMapaEquivalenciaDisciplinaAluno(MapaEquivalenciaDisciplinaVO mapaEquivalencia, Integer numeroAgrupamentoEquivalenciaDisciplina, List<MatriculaPeriodoTurmaDisciplinaVO> disciplinasAlunoCursando) {
		// Atualizando a situacao das disciplinas cursadas.
		for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursar : mapaEquivalencia.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			HistoricoVO hist = null;
			for(HistoricoVO historicoVO : getTodosHistoricosAlunoGradeCurricular()){
				if(historicoVO.getDisciplina().getCodigo().equals(disciplinaCursar.getDisciplinaVO().getCodigo()) &&
						historicoVO.getMapaEquivalenciaDisciplina().getCodigo().equals(disciplinaCursar.getMapaEquivalenciaDisciplina().getCodigo())
						&& historicoVO.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(historicoVO.getMapaEquivalenciaDisciplinaCursada().getCodigo())
						&& historicoVO.getNumeroAgrupamentoEquivalenciaDisciplina().equals(numeroAgrupamentoEquivalenciaDisciplina)){
					hist = historicoVO;
					break;
				}
			}
//			HistoricoVO hist = this.verificarAlunoAprovadoDisciplina(disciplinaCursar.getDisciplinaVO().getCodigo());
			if (Uteis.isAtributoPreenchido(hist)) {
				// entao disciplina aprovada
				disciplinaCursar.setHistorico(hist);
			} else {
				// se ao menos uma disciplina o aluno nao está aprovado, entao o
				// mapa não está cumprido
//				hist = this.verificarAlunoCursandoDisciplina(disciplinaCursar.getDisciplinaVO().getCodigo());
//				if (hist != null) {
//					disciplinaCursar.setHistorico(hist);
//				} else {
					// se nao encontrou no historico do aluno, verificar se a
					// disciplina não está na própria lista de disciplnias
					// da matricula periodo.
				if (Uteis.isAtributoPreenchido(disciplinasAlunoCursando)) {
					for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplinaVOAtual : disciplinasAlunoCursando) {
						if (matriculaPeriodoTumaDisciplinaVOAtual.getDisciplina().getCodigo().equals(disciplinaCursar.getDisciplinaVO().getCodigo())) {
							hist = new HistoricoVO();
							hist.setSituacao(SituacaoHistorico.CURSANDO.getValor());
							disciplinaCursar.setHistorico(hist);
						}
					}
				}
					if (disciplinaCursar.getHistorico() == null) {
						for (HistoricoVO historicoVO : this.getTodosHistoricosAlunoGradeCurricular()) {
							if (historicoVO.getDisciplina().getCodigo().equals(disciplinaCursar.getDisciplinaVO().getCodigo()) && historicoVO.getReprovado()) {
								disciplinaCursar.setHistorico(historicoVO);
								break;
							}
						}
						if (disciplinaCursar.getHistorico() == null) {
							hist = new HistoricoVO();
							hist.setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
							disciplinaCursar.setHistorico(hist);
						}
					}
				}
//			}
		}

		for (MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatriz : mapaEquivalencia.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			HistoricoVO hist = null;
			for(HistoricoVO historicoVO : getTodosHistoricosAlunoGradeCurricular()){
				if(historicoVO.getDisciplina().getCodigo().equals(disciplinaMatriz.getDisciplinaVO().getCodigo()) &&
						historicoVO.getMapaEquivalenciaDisciplina().getCodigo().equals(disciplinaMatriz.getMapaEquivalenciaDisciplina().getCodigo())
						&& historicoVO.getMapaEquivalenciaDisciplinaMatrizCurricular().getCodigo().equals(historicoVO.getMapaEquivalenciaDisciplinaMatrizCurricular().getCodigo())
						&& historicoVO.getNumeroAgrupamentoEquivalenciaDisciplina().equals(numeroAgrupamentoEquivalenciaDisciplina)){
					hist = historicoVO;
					break;
				}
			}
//			HistoricoVO hist = this.verificarAlunoAprovadoDisciplina(disciplinaMatriz.getDisciplinaVO().getCodigo());
			if (Uteis.isAtributoPreenchido(hist)) {
				disciplinaMatriz.setHistorico(hist);
			} else {
//				for (HistoricoVO historicoVO : this.getTodosHistoricosAlunoGradeCurricular()) {
//					if (historicoVO.getDisciplina().getCodigo().equals(disciplinaMatriz.getDisciplinaVO().getCodigo()) && historicoVO.getReprovado() && historicoVO.getHistoricoPorEquivalencia()) {
//						disciplinaMatriz.setHistorico(historicoVO);
//						break;
//					}
//				}
				if (!Uteis.isAtributoPreenchido(disciplinaMatriz.getHistorico())) {
					hist = new HistoricoVO();
					hist.setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
					disciplinaMatriz.setHistorico(hist);
				}
			}
		}
	}

	public Double getPercenutalCargaHorariaCursada() {
		Double percentual = 0.0;
		Integer total = this.getGradeCurricularVO().getTotalCargaHoraria();
		Integer parteCalcular = this.getTotalCargaHorariaCursadaAluno();
		if (total <= 0) {
			return 0.0;
		} else {
			percentual = ((parteCalcular.doubleValue() * 100) / total.doubleValue());
		}
		return Uteis.arrendondarForcando2CadasDecimais(percentual);
	}

	public Double getPercenutalCargaHorariaAlunoEstaCursandoAtualmente() {
		Double percentual = 0.0;
		Integer total = this.getGradeCurricularVO().getTotalCargaHoraria();
		Integer parteCalcular = this.getTotalCargaHorariaAlunoEstaCursandoAtualmente();
		if (total <= 0) {
			return 0.0;
		} else {
			percentual = ((parteCalcular.doubleValue() * 100) / total.doubleValue());
		}
		return Uteis.arrendondarForcando2CadasDecimais(percentual);
	}

	public List<GradeDisciplinaVO> getListaGradeDisciplinaVOsPendentesGradeCurricular(Integer ateDeterminadoPeriodoLetivo) throws Exception {
		List<GradeDisciplinaVO> listaGradeDisciplinaPendencia = new ArrayList<GradeDisciplinaVO>();
		for (PeriodoLetivoComHistoricoAlunoVO periodo : getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodo.getPeriodoLetivoVO().getPeriodoLetivo().compareTo(ateDeterminadoPeriodoLetivo) <= 0) {
				for (GradeDisciplinaComHistoricoAlunoVO gradeHist : periodo.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if ((!gradeHist.getHistoricoAtualAluno().getAprovado()) && (!gradeHist.getHistoricoAtualAluno().getCursando())) {
						listaGradeDisciplinaPendencia.add(gradeHist.getGradeDisciplinaVO());
					}
				}
			} else {
				break;
			}
		}
		return listaGradeDisciplinaPendencia.stream().sorted(Comparator.comparing(p-> p.getPeriodoLetivoVO().getPeriodoLetivo())).collect(Collectors.toList());
	}

	public List<GradeDisciplinaVO> getListaGradeDisciplinaVOsPendentesGradeCurricular() throws Exception {
		List<GradeDisciplinaVO> listaGradeDisciplinaPendencia = new ArrayList<GradeDisciplinaVO>();
		for (PeriodoLetivoComHistoricoAlunoVO periodo : getPeriodoLetivoComHistoricoAlunoVOs()) {
			for (GradeDisciplinaComHistoricoAlunoVO gradeHist : periodo.getGradeDisciplinaComHistoricoAlunoVOs()) {
				if ((!gradeHist.getHistoricoAtualAluno().getAprovado()) && (!gradeHist.getHistoricoAtualAluno().getCursando())) {
					listaGradeDisciplinaPendencia.add(gradeHist.getGradeDisciplinaVO());
				}
			}
		}
		return listaGradeDisciplinaPendencia.stream().sorted(Comparator.comparing(p-> p.getPeriodoLetivoVO().getPeriodoLetivo())).collect(Collectors.toList());
	}

	public List<GradeDisciplinaComHistoricoAlunoVO> getDisciplinasPendentesGradeCurricular() throws Exception {
		List<GradeDisciplinaComHistoricoAlunoVO> listaGradeDisciplinaPendencia = new ArrayList<GradeDisciplinaComHistoricoAlunoVO>();
		for (PeriodoLetivoComHistoricoAlunoVO periodo : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			for (GradeDisciplinaComHistoricoAlunoVO gradeHist : periodo.getGradeDisciplinaComHistoricoAlunoVOs()) {
				if ((!gradeHist.getHistoricoAtualAluno().getAprovado()) && (!gradeHist.getHistoricoAtualAluno().getCursando())) {
					listaGradeDisciplinaPendencia.add(gradeHist);
				}
			}
		}
		return listaGradeDisciplinaPendencia;
	}

	public PeriodoLetivoComHistoricoAlunoVO getPeriodoLetivoComHistoricoAlunoVOPorCodigo(Integer codigoPeriodoLetivo) {
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHistorico.getPeriodoLetivoVO().getCodigo().equals(codigoPeriodoLetivo)) {
				return periodoComHistorico;
			}
		}
		return null;
	}

	public PeriodoLetivoComHistoricoAlunoVO getPeriodoLetivoComHistoricoAlunoVOPorNumeroPeriodo(Integer periodoLetivo) {
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHistorico : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHistorico.getPeriodoLetivoVO().getPeriodoLetivo().equals(periodoLetivo)) {
				return periodoComHistorico;
			}
		}
		return null;
	}

	/**
	 * Método responsável por obter um histórico para uma GradeDisciplina,
	 * navegando por todos períodos letivos.
	 * 
	 * @param codigoDisciplina
	 * @param cargaHoraria
	 * @return
	 */
	public HistoricoVO obterHistoricoAtualGradeDisciplinaVO(Integer codigoDisciplina, Integer cargaHoraria) {
		for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			for (GradeDisciplinaComHistoricoAlunoVO disciplinaGrade : periodoLetivoComHistoricoAlunoVO.getGradeDisciplinaComHistoricoAlunoVOs()) {
				if ((disciplinaGrade.getGradeDisciplinaVO().getDisciplina().getCodigo().equals(codigoDisciplina)) && (disciplinaGrade.getGradeDisciplinaVO().getCargaHoraria().equals(cargaHoraria))) {
					HistoricoVO historicoVO = disciplinaGrade.getHistoricoAtualAluno();
					if (!historicoVO.getCodigo().equals(0)) {
						return historicoVO;
					}
				}
			}
		}
		return new HistoricoVO();
	}

	/**
	 * Método responsável por obter um histórico para uma disciplina de um grupo
	 * optativa. Navegando por todos períodos letivos. Pois uma optativa que o
	 * aluno cursou no 10o período, por exemplo, também pode estar vinculada ao
	 * 1o período (pois usa-se o mesmo grupo de optativa), portanto, temos que
	 * visualizar que a mesma já foi cursada (ou está sendo cursada) em outro
	 * período.
	 * 
	 * @param codigoDisciplina
	 * @param cargaHoraria
	 * @return
	 */
	public HistoricoVO obterHistoricoAtualGradeCurricularGrupoOptativaVO(Integer codigoDisciplina, Integer cargaHoraria) {
		for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO disciplinaGrupoOptativa : periodoLetivoComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
				if ((disciplinaGrupoOptativa.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo().equals(codigoDisciplina)) && (disciplinaGrupoOptativa.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria().equals(cargaHoraria))) {
					HistoricoVO historicoVO = disciplinaGrupoOptativa.getHistoricoAtualAluno();
					if (!historicoVO.getCodigo().equals(0)) {
						return historicoVO;
					}
				}
			}
		}
		return new HistoricoVO();
	}

	public HistoricoVO obterHistoricoAtualPorDisciplinaCargaHoraria(Integer codigoDisciplina, Integer cargaHoraria) {
		for (HistoricoVO historicoVO : this.getTodosHistoricosAlunoGradeCurricular()) {
			if (historicoVO.getDisciplina().getCodigo().equals(codigoDisciplina)) {
				if (!historicoVO.getCodigo().equals(0)) {
					return historicoVO;
				}
			}
		}
		return new HistoricoVO();
	}

	/**
	 * Limpa dados relativos a histórico no periodoLetivo.
	 */
	public void limparDadosHistoricos() {
		for (PeriodoLetivoComHistoricoAlunoVO periodo : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			periodo.limparDadosHistoricos();
		}
		this.getHistoricosDisciplinasAlunoCursandoGradeCurricular().clear();
		this.getHistoricosDisciplinasAprovadasAlunoGradeCurricular().clear();
		this.getHistoricosDisciplinasAlunoReprovouGradeCurricular().clear();
		this.getHistoricosDisciplinasForaGradeCurricular().clear();
		this.getTodosHistoricosAlunoGradeCurricular().clear();
	}

	public void removerDisciplinaListaDisciplinasAlunoCursando(GradeDisciplinaVO gradeDisciplinaVO) {
		for (HistoricoVO historicoVO : this.getHistoricosDisciplinasAlunoCursandoGradeCurricular()) {
			if (historicoVO.getGradeDisciplinaVO().getCodigo().equals(gradeDisciplinaVO.getCodigo())) {
				this.getHistoricosDisciplinasAlunoCursandoGradeCurricular().remove(historicoVO);
				break;
			}
		}
	}
	
	public GradeCurricularComHistoricoAlunoVO() {
		super();
	}

	
	public void verificaExistenciarDisciplinaPendenteAlunoAteDeterminadoPeriodoMatrizCurricularPodeSerIncluida(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa, Integer maximaCursarPeriodoLetivo, String ano, String semestre, Boolean considerarNumeroMaximoCredito, Integer percentualMinimoIncluir) throws Exception {
		if (numeroPeriodoLetivo == null || numeroPeriodoLetivo.equals(0)) {
			return;
		}
		Integer chDisciplinaIncluida = 0;
		Integer chCrTotalDependencia = 0;
		if(considerarNumeroMaximoCredito){
			chCrTotalDependencia = calcularNrCreditoDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(numeroPeriodoLetivo, desconsiderarDisciplinaOptativa);
			chCrTotalDependencia += calcularNrCreditoIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricularJaGravada(numeroPeriodoLetivo, desconsiderarDisciplinaOptativa, ano, semestre);
			chDisciplinaIncluida = calcularNrCreditoIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(numeroPeriodoLetivo, desconsiderarDisciplinaOptativa, ano, semestre);
		}else{
			chCrTotalDependencia = calcularNrCargaHorariaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(numeroPeriodoLetivo, desconsiderarDisciplinaOptativa);
			chCrTotalDependencia += calcularNrCargaHorariaIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricularJaGravada(numeroPeriodoLetivo, desconsiderarDisciplinaOptativa, ano, semestre);
			chDisciplinaIncluida = calcularNrCargaHorariaIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(numeroPeriodoLetivo, desconsiderarDisciplinaOptativa, ano, semestre);
		}		
		/**
		 *
		 * A Regra abaixo valida a quantidade mínima de disciplinas em crédito ou carga horária que um aluno de dependencia
		 * levando em consideração o campo PorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia definido na configuração acadêmica,
		 * Exemplo se o aluno deve 60 créditos e no campo PorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia está defindo 50%
		 * então o aluno terá que incluir no mínimo 30 créditos/carga horária de disciplinas de dependencia, caso este valor seja 
		 * maior que o total máximo permitido no periodo letivo então será considerado  maximaCursarPeriodoLetivo, caso contrario
		 * será considerado o valor mínimo calculado.
		 * 
		 */
		if(percentualMinimoIncluir > 0){
			Integer totalDependenciaMinimaIncluir = (chCrTotalDependencia*percentualMinimoIncluir)/100;
			if(totalDependenciaMinimaIncluir < maximaCursarPeriodoLetivo){
				maximaCursarPeriodoLetivo = totalDependenciaMinimaIncluir;
			}
		}
		
		/**
		 * O for abaixo valida se ainda existe possibilidade de incluir alguma disciplina na matrícula do aluno considerando a carga horária máxima do periodo letivo da matriz:
		 * Ex: se a carga horária máxima é de 400 e o aluno já incluiu 360 em carga horária de disciplinas do passo existem ainda 40 CH para ser preenchida, então é percorrido a lista de disciplinas
		 * em busca da existencia de alguma disciplina que poderá preencher esta carga horária pois se todas as pendentes for de 60 horas então não poderá incluir mais nada.
		 */
		Integer chPendendente = maximaCursarPeriodoLetivo - chDisciplinaIncluida;
		if (chPendendente > 0) {
			for (PeriodoLetivoComHistoricoAlunoVO periodoComHist : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
				if (periodoComHist.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
					for (GradeDisciplinaVO gradeDisciplinaVO : periodoComHist.getDisciplinasPendentesAlunoPeriodoLetivo()) {
						if ((!gradeDisciplinaVO.getIsDisciplinaOptativa() || desconsiderarDisciplinaOptativa)  
							&& ((gradeDisciplinaVO.getCargaHoraria() <= chPendendente && !considerarNumeroMaximoCredito) || (gradeDisciplinaVO.getNrCreditos() <= chPendendente && considerarNumeroMaximoCredito)) 
							&& (!gradeDisciplinaVO.getJaIncluidaRenovacao()
							&& (!Uteis.isAtributoPreenchido(gradeDisciplinaVO.getHistoricoAtualAluno().getCodigo())
								 || (Uteis.isAtributoPreenchido(gradeDisciplinaVO.getHistoricoAtualAluno().getCodigo()) 
								 && !SituacaoHistorico.getEnum(gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao()).getHistoricoCursando()
								 && !SituacaoHistorico.getEnum(gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao()).getHistoricoAprovado())))
							) {
							String crDis = considerarNumeroMaximoCredito ? "crédito(s)" : "carga horárias";
							throw new Exception("É obrigatório que sejam incluídas pelo menos mais " + chPendendente +" em "+crDis+" de disciplina(s) pendente(s) dos períodos anteriores ao "+(numeroPeriodoLetivo+1)+"º período. Talvez você tenha que remover disciplinas do "+(numeroPeriodoLetivo+1)+"º período para permitir a inclusão de disciplinas anteriores.");
						}
					}
				} else {
					break;
				}
			}			
		}
		return;

	}
	
	public Integer calcularNrCreditoIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa, String ano, String semestre) {		
		Integer crDisciplinaIncluida = 0;
		List<Integer> disciplinaContabilizada = new ArrayList<Integer>(0);
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que acabaram de ser incluidas na matrícula e ainda não foi gravado
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoComHist2.getDisciplinasPendentesAlunoPeriodoLetivo()) {
					if ((!gradeDisciplinaVO.getIsDisciplinaOptativa() || desconsiderarDisciplinaOptativa) 
							&& (gradeDisciplinaVO.getJaIncluidaRenovacao() || (Uteis.isAtributoPreenchido(gradeDisciplinaVO.getHistoricoAtualAluno().getCodigo()) 
									&& (gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CS") || gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CO") 
											|| gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CE"))))) {
						disciplinaContabilizada.add(gradeDisciplinaVO.getDisciplina().getCodigo());
						crDisciplinaIncluida += gradeDisciplinaVO.getNrCreditos();
					}					
				}
			}
		}
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que já estavam vinculado a matrícula e o usuário está apenas repassando pela matrícula. 
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaComHistoricoAlunoVO historicoVO : periodoComHist2.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (!historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaFazParteComposicao() && !historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaForaGrade() && !historicoVO.getHistoricoAtualAluno().getHistoricoEquivalente() 
							&& !disciplinaContabilizada.contains(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo()) && historicoVO.getHistoricoAtualAluno().getAnoHistorico().equals(ano) && historicoVO.getHistoricoAtualAluno().getSemestreHistorico().equals(semestre)) {
						disciplinaContabilizada.add(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo());
						crDisciplinaIncluida += historicoVO.getGradeDisciplinaVO().getNrCreditos();
					}
				}
			}
		}
		return crDisciplinaIncluida;
	}
	
	public Integer calcularNrCreditoIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricularJaGravada(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa, String ano, String semestre) {		
		Integer crDisciplinaIncluida = 0;
		List<Integer> disciplinaContabilizada = new ArrayList<Integer>(0);
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que acabaram de ser incluidas na matrícula e ainda não foi gravado
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoComHist2.getDisciplinasPendentesAlunoPeriodoLetivo()) {
					if ((!gradeDisciplinaVO.getIsDisciplinaOptativa() || desconsiderarDisciplinaOptativa) 
							&& (gradeDisciplinaVO.getJaIncluidaRenovacao() || (Uteis.isAtributoPreenchido(gradeDisciplinaVO.getHistoricoAtualAluno().getCodigo()) 
									&& (gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CS") || gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CO") 
											|| gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CE"))))) {
						disciplinaContabilizada.add(gradeDisciplinaVO.getDisciplina().getCodigo());						
					}					
				}
			}
		}
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que já estavam vinculado a matrícula e o usuário está apenas repassando pela matrícula. 
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaComHistoricoAlunoVO historicoVO : periodoComHist2.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (!historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaFazParteComposicao() && !historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaForaGrade() && !historicoVO.getHistoricoAtualAluno().getHistoricoEquivalente() 
							&& !disciplinaContabilizada.contains(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo()) && historicoVO.getHistoricoAtualAluno().getAnoHistorico().equals(ano) && historicoVO.getHistoricoAtualAluno().getSemestreHistorico().equals(semestre)) {
						disciplinaContabilizada.add(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo());
						crDisciplinaIncluida += historicoVO.getGradeDisciplinaVO().getNrCreditos();
					}
				}
			}
		}
		return crDisciplinaIncluida;
	}
	
	public Integer calcularNrCargaHorariaIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa, String ano, String semestre) {		
		Integer chDisciplinaIncluida = 0;
		List<Integer> disciplinaContabilizada = new ArrayList<Integer>(0);
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que acabaram de ser incluidas na matrícula e ainda não foi gravado
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoComHist2.getDisciplinasPendentesAlunoPeriodoLetivo()) {
					if ((!gradeDisciplinaVO.getIsDisciplinaOptativa() || desconsiderarDisciplinaOptativa) 
							&& (gradeDisciplinaVO.getJaIncluidaRenovacao() || (Uteis.isAtributoPreenchido(gradeDisciplinaVO.getHistoricoAtualAluno().getCodigo()) 
									&& (gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CS") || gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CO") 
											|| gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CE"))))) {
						disciplinaContabilizada.add(gradeDisciplinaVO.getDisciplina().getCodigo());
						chDisciplinaIncluida += gradeDisciplinaVO.getCargaHoraria();
					}					
				}
			}
		}
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que já estavam vinculado a matrícula e o usuário está apenas repassando pela matrícula. 
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaComHistoricoAlunoVO historicoVO : periodoComHist2.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (!historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaFazParteComposicao() && !historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaForaGrade() && !historicoVO.getHistoricoAtualAluno().getHistoricoEquivalente() 
							&& !disciplinaContabilizada.contains(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo()) && historicoVO.getHistoricoAtualAluno().getAnoHistorico().equals(ano) && historicoVO.getHistoricoAtualAluno().getSemestreHistorico().equals(semestre)) {
						disciplinaContabilizada.add(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo());
						chDisciplinaIncluida += historicoVO.getGradeDisciplinaVO().getCargaHoraria();
					}
				}
			}
		}
		return chDisciplinaIncluida;
	}
	
	public Integer calcularNrCargaHorariaIncluidaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricularJaGravada(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa, String ano, String semestre) {		
		Integer chDisciplinaIncluida = 0;
		List<Integer> disciplinaContabilizada = new ArrayList<Integer>(0);
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que acabaram de ser incluidas na matrícula e ainda não foi gravado
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoComHist2.getDisciplinasPendentesAlunoPeriodoLetivo()) {
					if ((!gradeDisciplinaVO.getIsDisciplinaOptativa() || desconsiderarDisciplinaOptativa) 
							&& (gradeDisciplinaVO.getJaIncluidaRenovacao() || (Uteis.isAtributoPreenchido(gradeDisciplinaVO.getHistoricoAtualAluno().getCodigo()) 
									&& (gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CS") || gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CO") 
											|| gradeDisciplinaVO.getHistoricoAtualAluno().getSituacao().equals("CE"))))) {
						disciplinaContabilizada.add(gradeDisciplinaVO.getDisciplina().getCodigo());						
					}					
				}
			}
		}
		/**
		 * o for abaixo tem a finalidade de contabilizar as disciplinas que já estavam vinculado a matrícula e o usuário está apenas repassando pela matrícula. 
		 */
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist2 : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist2.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				for (GradeDisciplinaComHistoricoAlunoVO historicoVO : periodoComHist2.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (!historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaFazParteComposicao() && !historicoVO.getHistoricoAtualAluno().getHistoricoDisciplinaForaGrade() && !historicoVO.getHistoricoAtualAluno().getHistoricoEquivalente() 
							&& !disciplinaContabilizada.contains(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo()) && historicoVO.getHistoricoAtualAluno().getAnoHistorico().equals(ano) && historicoVO.getHistoricoAtualAluno().getSemestreHistorico().equals(semestre)) {
						disciplinaContabilizada.add(historicoVO.getHistoricoAtualAluno().getDisciplina().getCodigo());
						chDisciplinaIncluida += historicoVO.getGradeDisciplinaVO().getCargaHoraria();
					}
				}
			}
		}
		return chDisciplinaIncluida;
	}
	
	
	
	public Integer calcularNrCreditoDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa) {
		if (numeroPeriodoLetivo == null || numeroPeriodoLetivo.equals(0)) {
			return 0;
		}
		int nrCreditoDisciplinasPendentes = 0;
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				int nrPendentes = 0;
				
					for(GradeDisciplinaVO gradeDisciplinaVO:periodoComHist.getDisciplinasPendentesAlunoPeriodoLetivo()){
						if(!gradeDisciplinaVO.getIsDisciplinaOptativa() || (desconsiderarDisciplinaOptativa != null && !desconsiderarDisciplinaOptativa)){
							nrPendentes += gradeDisciplinaVO.getNrCreditos();
						}
					}				
					nrCreditoDisciplinasPendentes = nrCreditoDisciplinasPendentes + nrPendentes;

            }else{
            	break;
            }
		}
		return nrCreditoDisciplinasPendentes;
	}
	
	public Integer calcularNrCargaHorariaDisciplinasPendentesAlunoAteDeterminadoPeriodoMatrizCurricular(Integer numeroPeriodoLetivo, Boolean desconsiderarDisciplinaOptativa) {
		if (numeroPeriodoLetivo == null || numeroPeriodoLetivo.equals(0)) {
			return 0;
		}
		int nrCargaHorariaDisciplinasPendentes = 0;
		for (PeriodoLetivoComHistoricoAlunoVO periodoComHist : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodoComHist.getPeriodoLetivoVO().getPeriodoLetivo() <= (numeroPeriodoLetivo)) {
				int nrPendentes = 0;
				
					for(GradeDisciplinaVO gradeDisciplinaVO:periodoComHist.getDisciplinasPendentesAlunoPeriodoLetivo()){
						if(!gradeDisciplinaVO.getIsDisciplinaOptativa() || (desconsiderarDisciplinaOptativa != null && !desconsiderarDisciplinaOptativa)){
							nrPendentes += gradeDisciplinaVO.getCargaHoraria();
						}
					}				
					nrCargaHorariaDisciplinasPendentes = nrCargaHorariaDisciplinasPendentes + nrPendentes;
            }else{
            	break;
            }
		}
		return nrCargaHorariaDisciplinasPendentes;
	}

	public List<HistoricoVO> getHistoricosDisciplinasAlunoReprovouGradeCurricular() {
		if (historicosDisciplinasAlunoReprovouGradeCurricular == null) {
			historicosDisciplinasAlunoReprovouGradeCurricular = new ArrayList<HistoricoVO>(0);
		}
		return historicosDisciplinasAlunoReprovouGradeCurricular;
	}

	public void setHistoricosDisciplinasAlunoReprovouGradeCurricular(List<HistoricoVO> historicosDisciplinasAlunoReprovouGradeCurricular) {
		this.historicosDisciplinasAlunoReprovouGradeCurricular = historicosDisciplinasAlunoReprovouGradeCurricular;
	}


	public Integer totalCargaHorariaDisciplinaObrigatoriaCumprida;

	public Integer getTotalCargaHorariaDisciplinaObrigatoriaCumprida() throws Exception {
		if (totalCargaHorariaDisciplinaObrigatoriaCumprida == null) {
			totalCargaHorariaDisciplinaObrigatoriaCumprida = 0;
			for (PeriodoLetivoComHistoricoAlunoVO periodo : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
				for (GradeDisciplinaComHistoricoAlunoVO gradeHist : periodo.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (!gradeHist.getGradeDisciplinaVO().getIsDisciplinaOptativa() && Uteis.isAtributoPreenchido(gradeHist.getHistoricoAtualAluno()) && gradeHist.getHistoricoAtualAluno().getAprovado()) {
						totalCargaHorariaDisciplinaObrigatoriaCumprida += gradeHist.getGradeDisciplinaVO().getCargaHoraria();
					}
				}
			}
		}
		return totalCargaHorariaDisciplinaObrigatoriaCumprida;
	}

	public Integer totalCargaHorariaDisciplinaOptativaCumprida;

	public Integer getTotalCargaHorariaDisciplinaOptativaCumprida() throws Exception {
		if (totalCargaHorariaDisciplinaOptativaCumprida == null) {
			totalCargaHorariaDisciplinaOptativaCumprida = 0;
			for (PeriodoLetivoComHistoricoAlunoVO periodo : this.getPeriodoLetivoComHistoricoAlunoVOs()) {
				for (GradeDisciplinaComHistoricoAlunoVO gradeHist : periodo.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (gradeHist.getGradeDisciplinaVO().getIsDisciplinaOptativa() && Uteis.isAtributoPreenchido(gradeHist.getHistoricoAtualAluno()) && gradeHist.getHistoricoAtualAluno().getAprovado()) {
						totalCargaHorariaDisciplinaOptativaCumprida += gradeHist.getGradeDisciplinaVO().getCargaHoraria();
					}
				}
				for(GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaVO : periodo.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
					if (Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno()) && gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getAprovado()) {
						totalCargaHorariaDisciplinaOptativaCumprida += gradeCurricularGrupoOptativaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
					}
				}
			}
			
		}
		return totalCargaHorariaDisciplinaOptativaCumprida;
	}
	
	public List<GradeDisciplinaVO> 	getListaGradeDisciplinaVOsPendentesGradeCurricularValidandoDisciplinaAprovadoNaGradeAntigaReferenteAtransferenciaInternaMatricula(Integer ateDeterminadoPeriodoLetivo) throws Exception {
		List<GradeDisciplinaVO> listaGradeDisciplinaPendencia = new ArrayList<GradeDisciplinaVO>();
		for (PeriodoLetivoComHistoricoAlunoVO periodo : getPeriodoLetivoComHistoricoAlunoVOs()) {
			if (periodo.getPeriodoLetivoVO().getPeriodoLetivo().compareTo(ateDeterminadoPeriodoLetivo) <= 0) {
				for (GradeDisciplinaComHistoricoAlunoVO gradeHist : periodo.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if ((!gradeHist.getHistoricoAtualAluno().getAprovado()) && (!gradeHist.getHistoricoAtualAluno().getCursando())  &&  !Uteis.isAtributoPreenchido(verificarAlunoAprovadoDisciplina(gradeHist.getGradeDisciplinaVO().getDisciplina().getCodigo()))) {
						listaGradeDisciplinaPendencia.add(gradeHist.getGradeDisciplinaVO());
					}
				}
			} else {
				break;
			}	
		}
		return listaGradeDisciplinaPendencia.stream().sorted(Comparator.comparing(p-> p.getPeriodoLetivoVO().getPeriodoLetivo())).collect(Collectors.toList());
	}
	
	public Integer getTotalCargaHorariaDisciplinaGrupoOptativaCumprida(Integer gradeCurricularGrupoOptativa) throws Exception {
		
		Integer	totalCargaHorariaDisciplinaOptativaCumprida = 0;
			for (PeriodoLetivoComHistoricoAlunoVO periodo : this.getPeriodoLetivoComHistoricoAlunoVOs()) {				
				for(GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaVO : periodo.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
					if (Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno()) && gradeCurricularGrupoOptativa.equals(gradeCurricularGrupoOptativaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getCodigo()) && gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getAprovado()) {
						totalCargaHorariaDisciplinaOptativaCumprida += gradeCurricularGrupoOptativaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
					}
				}
			}
			
		
		return totalCargaHorariaDisciplinaOptativaCumprida;
	}
	
}
