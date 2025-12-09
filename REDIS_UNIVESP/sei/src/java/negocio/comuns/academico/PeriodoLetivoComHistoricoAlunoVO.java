/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author EDIGARANTONIO
 */
public class PeriodoLetivoComHistoricoAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Neste atributo ficará a referencia ao periodoLetivo da gradeCurricular o
	 * dado é replicado aqui para ser mantido e ser utilizado em controles e
	 * relatórios. Por exmeplo, para se determinar o o periodoLetivo está
	 * integralizado ou não temos que cruzar o que o aluno estou com o que está
	 * projetado no PeriodoLetivo da Grade.
	 */
	private PeriodoLetivoVO periodoLetivoVO;
	private Integer totalCargaHorariaCursadaAluno;
	private Integer totalCreditosCursadosAluno;

	private Integer totalCargaHorariaAlunoEstaCursandoAtualmente;
	private Integer totalCreditosAlunoEstaCursandoAtualmente;

	private Integer cargaHorariaPendenteAlunoPeriodo;
	private Integer nrCreditosPendentesAlunoPeriodo;

	/**
	 * Reflete a carga horaria que o aluno está pendente com relação a grupo de
	 * optativas. Caso a matriz utilize este tipo de controle.
	 */
	private Integer cargaHorariaPendentePeriodoComRelacaoGrupoOptativa;
	/**
	 * Reflete os creditos que o aluno está pendente com relação a grupo de
	 * optativas. Caso a matriz utilize este tipo de controle.
	 */
	private Integer nrCreditosPendentePeriodoComRelacaoGrupoOptativa;

	private Boolean periodoIntegralizado;
	private Double percentualIntegralizacaoPeriodoPorCargaHoraria;
	private Double percentualIntegralizacaoPeriodoPorCreditos;
	private List<GradeDisciplinaComHistoricoAlunoVO> gradeDisciplinaComHistoricoAlunoVOs;
	private List<GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO> gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;

	private List<HistoricoVO> historicosDisciplinasAprovadasAlunoPeriodoLetivo;
	private List<HistoricoVO> historicosDisciplinasAlunoCursandoPeriodoLetivo;
	/**
	 * Lista que irá manter a lista de todas as disciplinas pendentes do aluno
	 * periodo letivo. A mesma será utiliza para que o aluno, possa visualizar
	 * suas pendencias em cada periodo letivo.
	 */
	private List<GradeDisciplinaVO> disciplinasPendentesAlunoPeriodoLetivo;


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
	 * @return the cargaHorariaPendenteAlunoPeriodo
	 */
	public Integer getCargaHorariaPendenteAlunoPeriodo() {
		if (cargaHorariaPendenteAlunoPeriodo == null) {
			cargaHorariaPendenteAlunoPeriodo = 0;
		}
		return cargaHorariaPendenteAlunoPeriodo;
	}

	/**
	 * @param cargaHorariaPendenteAlunoPeriodo
	 *            the cargaHorariaPendenteAlunoPeriodo to set
	 */
	public void setCargaHorariaPendenteAlunoPeriodo(Integer cargaHorariaPendenteAlunoPeriodo) {
		this.cargaHorariaPendenteAlunoPeriodo = cargaHorariaPendenteAlunoPeriodo;
	}

	/**
	 * @return the nrCreditosPendentesAlunoPeriodo
	 */
	public Integer getNrCreditosPendentesAlunoPeriodo() {
		if (nrCreditosPendentesAlunoPeriodo == null) {
			nrCreditosPendentesAlunoPeriodo = 0;
		}
		return nrCreditosPendentesAlunoPeriodo;
	}

	/**
	 * @param nrCreditosPendentesAlunoPeriodo
	 *            the nrCreditosPendentesAlunoPeriodo to set
	 */
	public void setNrCreditosPendentesAlunoPeriodo(Integer nrCreditosPendentesAlunoPeriodo) {
		this.nrCreditosPendentesAlunoPeriodo = nrCreditosPendentesAlunoPeriodo;
	}

	/**
	 * @return the periodoIntegralizado
	 */
	public Boolean getPeriodoIntegralizado() {
		if (periodoIntegralizado == null) {
			periodoIntegralizado = Boolean.FALSE;
		}
		return periodoIntegralizado;
	}

	/**
	 * @param periodoIntegralizado
	 *            the periodoIntegralizado to set
	 */
	public void setPeriodoIntegralizado(Boolean periodoIntegralizado) {
		this.periodoIntegralizado = periodoIntegralizado;
	}

	/**
	 * @return the percentualIntegralizacaoPeriodoPorCargaHoraria
	 */
	public Double getPercentualIntegralizacaoPeriodoPorCargaHoraria() {
		if (percentualIntegralizacaoPeriodoPorCargaHoraria == null) {
			percentualIntegralizacaoPeriodoPorCargaHoraria = 0.0;
		}
		return percentualIntegralizacaoPeriodoPorCargaHoraria;
	}

	/**
	 * @param percentualIntegralizacaoPeriodoPorCargaHoraria
	 *            the percentualIntegralizacaoPeriodoPorCargaHoraria to set
	 */
	public void setPercentualIntegralizacaoPeriodoPorCargaHoraria(Double percentualIntegralizacaoPeriodoPorCargaHoraria) {
		this.percentualIntegralizacaoPeriodoPorCargaHoraria = percentualIntegralizacaoPeriodoPorCargaHoraria;
	}

	/**
	 * @return the percentualIntegralizacaoPeriodoPorCreditos
	 */
	public Double getPercentualIntegralizacaoPeriodoPorCreditos() {
		if (percentualIntegralizacaoPeriodoPorCreditos == null) {
			percentualIntegralizacaoPeriodoPorCreditos = 0.0;
		}
		return percentualIntegralizacaoPeriodoPorCreditos;
	}

	/**
	 * @param percentualIntegralizacaoPeriodoPorCreditos
	 *            the percentualIntegralizacaoPeriodoPorCreditos to set
	 */
	public void setPercentualIntegralizacaoPeriodoPorCreditos(Double percentualIntegralizacaoPeriodoPorCreditos) {
		this.percentualIntegralizacaoPeriodoPorCreditos = percentualIntegralizacaoPeriodoPorCreditos;
	}

	/**
	 * @return the gradeDisciplinaComHistoricoAlunoVOs
	 */
	public List<GradeDisciplinaComHistoricoAlunoVO> getGradeDisciplinaComHistoricoAlunoVOs() {
		if (gradeDisciplinaComHistoricoAlunoVOs == null) {
			gradeDisciplinaComHistoricoAlunoVOs = new ArrayList<GradeDisciplinaComHistoricoAlunoVO>(0);
		}
		return gradeDisciplinaComHistoricoAlunoVOs;
	}

	/**
	 * @param gradeDisciplinaComHistoricoAlunoVOs
	 *            the gradeDisciplinaComHistoricoAlunoVOs to set
	 */
	public void setGradeDisciplinaComHistoricoAlunoVOs(List<GradeDisciplinaComHistoricoAlunoVO> gradeDisciplinaComHistoricoAlunoVOs) {
		this.gradeDisciplinaComHistoricoAlunoVOs = gradeDisciplinaComHistoricoAlunoVOs;
	}

	/**
	 * @return the gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
	 */
	public List<GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO> getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO() {
		if (gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO == null) {
			gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO = new ArrayList<GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO>(0);
		}
		return gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;
	}

	/**
	 * @param gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
	 *            the gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
	 *            to set
	 */
	public void setGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO(List<GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO> gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO) {
		this.gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO = gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;
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
	 * Método responsável por calcular e atualizar todas as informações sobre a
	 * evolução academica do alnuo no periodo letivo.
	 */
	public void atualizarDadosHistoricoAlunoNoPeriodoLetivo() throws Exception {
		this.setHistoricosDisciplinasAlunoCursandoPeriodoLetivo(null);
		this.setHistoricosDisciplinasAprovadasAlunoPeriodoLetivo(null);
		this.setDisciplinasPendentesAlunoPeriodoLetivo(null);

		this.getPeriodoLetivoVO().atualizarTotalCargaHoraria();
		this.getPeriodoLetivoVO().atualizarTotalCredito();
		totalCargaHorariaCursadaAluno = 0;
		totalCreditosCursadosAluno = 0;
		totalCargaHorariaAlunoEstaCursandoAtualmente = 0;
		totalCreditosAlunoEstaCursandoAtualmente = 0;
		for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico : this.getGradeDisciplinaComHistoricoAlunoVOs()) {
			boolean pendente = true;
			if (gradeDisciplinaComHistorico.getHistoricoAtualAluno().getAprovado()) {
				pendente = false;
				this.getHistoricosDisciplinasAprovadasAlunoPeriodoLetivo().add(gradeDisciplinaComHistorico.getHistoricoAtualAluno());
				totalCargaHorariaCursadaAluno += gradeDisciplinaComHistorico.getGradeDisciplinaVO().getCargaHoraria();
				totalCreditosCursadosAluno += gradeDisciplinaComHistorico.getGradeDisciplinaVO().getNrCreditos();
			}
			if (gradeDisciplinaComHistorico.getHistoricoAtualAluno().getCursando()) {
				pendente = false;
				this.getHistoricosDisciplinasAlunoCursandoPeriodoLetivo().add(gradeDisciplinaComHistorico.getHistoricoAtualAluno());
				totalCargaHorariaAlunoEstaCursandoAtualmente += gradeDisciplinaComHistorico.getGradeDisciplinaVO().getCargaHoraria();
				totalCreditosAlunoEstaCursandoAtualmente += gradeDisciplinaComHistorico.getGradeDisciplinaVO().getNrCreditos();
			}
			if (pendente) {
				this.getDisciplinasPendentesAlunoPeriodoLetivo().add(gradeDisciplinaComHistorico.getGradeDisciplinaVO());
			}
		}

		int totalCargaHorariaCursadaAlunoGrupoOptativa = 0;
		int totalCreditosCursadosAlunoGrupoOptativa = 0;
		int totalCargaHorariaAlunoEstaCursandoAtualmenteGrupoOptativa = 0;
		int totalCreditosAlunoEstaCursandoAtualmenteGrupoOptativa = 0;
		for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeGrupoOptativaComHistorico : this.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
			if (gradeGrupoOptativaComHistorico.getHistoricoAtualAluno().getAprovado()) {
				this.getHistoricosDisciplinasAprovadasAlunoPeriodoLetivo().add(gradeGrupoOptativaComHistorico.getHistoricoAtualAluno());
				totalCargaHorariaCursadaAluno += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
				totalCreditosCursadosAluno += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos();

				totalCargaHorariaCursadaAlunoGrupoOptativa += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
				totalCreditosCursadosAlunoGrupoOptativa += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos();
			}
			if (gradeGrupoOptativaComHistorico.getHistoricoAtualAluno().getCursando()) {
				this.getHistoricosDisciplinasAlunoCursandoPeriodoLetivo().add(gradeGrupoOptativaComHistorico.getHistoricoAtualAluno());
				totalCargaHorariaAlunoEstaCursandoAtualmente += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
				totalCreditosAlunoEstaCursandoAtualmente += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos();

				totalCargaHorariaAlunoEstaCursandoAtualmenteGrupoOptativa += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
				totalCreditosAlunoEstaCursandoAtualmenteGrupoOptativa += gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos();
			}
		}

		cargaHorariaPendentePeriodoComRelacaoGrupoOptativa = 0;
		nrCreditosPendentePeriodoComRelacaoGrupoOptativa = 0;
		if (this.getPeriodoLetivoVO().getControleOptativaGrupo()) {
			if (!this.getPeriodoLetivoVO().getTipoControleGrupoOptativaPorCredito()) {
				cargaHorariaPendentePeriodoComRelacaoGrupoOptativa = this.getPeriodoLetivoVO().getNumeroCargaHorariaOptativa() - totalCargaHorariaCursadaAlunoGrupoOptativa - totalCargaHorariaAlunoEstaCursandoAtualmenteGrupoOptativa;
				if (cargaHorariaPendentePeriodoComRelacaoGrupoOptativa < 0) {
					cargaHorariaPendentePeriodoComRelacaoGrupoOptativa = 0;
				}
			} else {
				nrCreditosPendentePeriodoComRelacaoGrupoOptativa = this.getPeriodoLetivoVO().getNumeroCreditoOptativa() - totalCreditosCursadosAlunoGrupoOptativa - totalCreditosAlunoEstaCursandoAtualmenteGrupoOptativa;
				if (nrCreditosPendentePeriodoComRelacaoGrupoOptativa < 0) {
					nrCreditosPendentePeriodoComRelacaoGrupoOptativa = 0;
				}
			}
		}

		cargaHorariaPendenteAlunoPeriodo = this.getPeriodoLetivoVO().getTotalCargaHoraria() - totalCargaHorariaCursadaAluno;
		if (cargaHorariaPendenteAlunoPeriodo < 0) {
			// pode ser negativa por meio de inclusao de optativas de um grupo,
			// que acabem por ultrapassar
			// a carga horaria prevista para o período. Neste caso defini-se que
			// estamos com cargahoraria pendente zero
			cargaHorariaPendenteAlunoPeriodo = 0;
		}

		nrCreditosPendentesAlunoPeriodo = this.getPeriodoLetivoVO().getTotalCreditos() - totalCreditosCursadosAluno;
		if (nrCreditosPendentesAlunoPeriodo < 0) {
			// pode ser negativa por meio de inclusao de optativas de um grupo,
			// que acabem por ultrapassar
			// os creditos previstos para o período. Neste caso defini-se que
			// estamos com cargahoraria pendente zero
			nrCreditosPendentesAlunoPeriodo = 0;
		}

		if (totalCargaHorariaCursadaAluno > 0 && getPeriodoLetivoVO().getTotalCargaHoraria() > 0) {
			percentualIntegralizacaoPeriodoPorCargaHoraria = Uteis.arrendondarForcando2CadasDecimais((totalCargaHorariaCursadaAluno * 100) / this.getPeriodoLetivoVO().getTotalCargaHoraria());
		} else {
			percentualIntegralizacaoPeriodoPorCargaHoraria = 0.0;
		}

		if (totalCreditosCursadosAluno > 0 && getPeriodoLetivoVO().getTotalCreditos() > 0) {
			percentualIntegralizacaoPeriodoPorCreditos = Uteis.arrendondarForcando2CadasDecimais((totalCreditosCursadosAluno * 100) / this.getPeriodoLetivoVO().getTotalCreditos());
		} else {
			percentualIntegralizacaoPeriodoPorCreditos = 0.0;
		}

		if (percentualIntegralizacaoPeriodoPorCargaHoraria >= 100) {
			periodoIntegralizado = Boolean.TRUE;
		} else {
			periodoIntegralizado = Boolean.FALSE;
		}
	}

	/**
	 * Método responsável por adicionar um histórico ao periodoLetivo já
	 * vinculando ao mesmo a GradeDisciplina adequada e fazendo os devidos
	 * controles de histórico por disciplina. Isto por que uma disciplina pode
	 * ter vários históricos para ela (o aluno pode ter sido reprovado duas
	 * vezes, antes de ser aprovado), o que implicaria em três registros de
	 * histórcio para a mesma disciplina. Este método trabalha com o conceito de
	 * que os históricos estão ordenados por codigo da disciplina e ano e
	 * semestre de estudo, de forma que o último histórcio adicionado é o mais
	 * rescente e portanto reflete a situação atual do aluno para a disciplina.
	 * Um histórico também
	 * 
	 * @param hist
	 */
	public boolean adicionarHistoricoDisciplinaDoPeriodoLetivo(HistoricoVO hist) {
		if (hist.getHistoricoEquivalente() || hist.getHistoricoDisciplinaForaGrade()) {
			// se é equivalente, é por que o aluno estudou em outra matriz
			// curricular de outro
			// com intuito de resolver um mapa de equivalencia.
			return false;
		}
		boolean disciplinaGradeDisciplina = (!hist.getGradeDisciplinaVO().getCodigo().equals(0));
		if (disciplinaGradeDisciplina) {
			for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico : this.getGradeDisciplinaComHistoricoAlunoVOs()) {
				if (gradeDisciplinaComHistorico.getGradeDisciplinaVO().getDisciplina().getCodigo().equals(hist.getDisciplina().getCodigo())) {
					gradeDisciplinaComHistorico.adicionarHistoricoParaGradeDisciplina(hist);
					return true;
				}
			}
		}

		if(hist.getHistoricoDisciplinaFazParteComposicao()){
			for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico : this.getGradeDisciplinaComHistoricoAlunoVOs()) {
				for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaComHistorico.getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs()){
					if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(hist.getDisciplina().getCodigo())) {
						gradeDisciplinaCompostaVO.adicionarHistoricoParaGradeDisciplina(hist);
						return true;
					}
				}
			}
			for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeGrupoOptativaComHistorico : this.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
				for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs()){
					if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().equals(hist.getDisciplina().getCodigo())) {
						gradeDisciplinaCompostaVO.adicionarHistoricoParaGradeDisciplina(hist);
						return true;
					}
				}
			}
		}
		// Se chegarmos aqui é por que o historico refere-se a uma disciplina de
		// um grupo de optativa.
		for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeGrupoOptativaComHistorico : this.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
			if (gradeGrupoOptativaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo().equals(hist.getDisciplina().getCodigo())) {
				gradeGrupoOptativaComHistorico.adicionarHistoricoParaGradeDisciplina(hist);
				return true;
			}
		}
		// Se chegarmos aqui é por que a disciplina nao foi encontrada nem na
		// gradeCurricular, nem no grupo de optativas
		// logo a mesma, deve ser tratada como fora da grade. O método chamado
		// por este trata esta situação
		return false;
	}

	/**
	 * @return the historicosDisciplinasAprovadasAlunoPeriodoLetivo
	 */
	public List<HistoricoVO> getHistoricosDisciplinasAprovadasAlunoPeriodoLetivo() {
		if (historicosDisciplinasAprovadasAlunoPeriodoLetivo == null) {
			historicosDisciplinasAprovadasAlunoPeriodoLetivo = new ArrayList<HistoricoVO>(0);
		}
		return historicosDisciplinasAprovadasAlunoPeriodoLetivo;
	}

	/**
	 * @param historicosDisciplinasAprovadasAlunoPeriodoLetivo
	 *            the historicosDisciplinasAprovadasAlunoPeriodoLetivo to set
	 */
	public void setHistoricosDisciplinasAprovadasAlunoPeriodoLetivo(List<HistoricoVO> historicosDisciplinasAprovadasAlunoPeriodoLetivo) {
		this.historicosDisciplinasAprovadasAlunoPeriodoLetivo = historicosDisciplinasAprovadasAlunoPeriodoLetivo;
	}

	/**
	 * @return the historicosDisciplinasAlunoCursandoPeriodoLetivo
	 */
	public List<HistoricoVO> getHistoricosDisciplinasAlunoCursandoPeriodoLetivo() {
		if (historicosDisciplinasAlunoCursandoPeriodoLetivo == null) {
			historicosDisciplinasAlunoCursandoPeriodoLetivo = new ArrayList<HistoricoVO>(0);
		}
		return historicosDisciplinasAlunoCursandoPeriodoLetivo;
	}

	/**
	 * @param historicosDisciplinasAlunoCursandoPeriodoLetivo
	 *            the historicosDisciplinasAlunoCursandoPeriodoLetivo to set
	 */
	public void setHistoricosDisciplinasAlunoCursandoPeriodoLetivo(List<HistoricoVO> historicosDisciplinasAlunoCursandoPeriodoLetivo) {
		this.historicosDisciplinasAlunoCursandoPeriodoLetivo = historicosDisciplinasAlunoCursandoPeriodoLetivo;
	}

	/**
	 * @return the disciplinasPendentesAlunoPeriodoLetivo
	 */
	public List<GradeDisciplinaVO> getDisciplinasPendentesAlunoPeriodoLetivo() {
		if (disciplinasPendentesAlunoPeriodoLetivo == null) {
			disciplinasPendentesAlunoPeriodoLetivo = new ArrayList<GradeDisciplinaVO>(0);
		}
		return disciplinasPendentesAlunoPeriodoLetivo;
	}

	/**
	 * @param disciplinasPendentesAlunoPeriodoLetivo
	 *            the disciplinasPendentesAlunoPeriodoLetivo to set
	 */
	public void setDisciplinasPendentesAlunoPeriodoLetivo(List<GradeDisciplinaVO> disciplinasPendentesAlunoPeriodoLetivo) {
		this.disciplinasPendentesAlunoPeriodoLetivo = disciplinasPendentesAlunoPeriodoLetivo;
	}

	/**
	 * @return the cargaHorariaPendentePeriodoComRelacaoGrupoOptativa
	 */
	public Integer getCargaHorariaPendentePeriodoComRelacaoGrupoOptativa() {
		return cargaHorariaPendentePeriodoComRelacaoGrupoOptativa;
	}

	/**
	 * @param cargaHorariaPendentePeriodoComRelacaoGrupoOptativa
	 *            the cargaHorariaPendentePeriodoComRelacaoGrupoOptativa to set
	 */
	public void setCargaHorariaPendentePeriodoComRelacaoGrupoOptativa(Integer cargaHorariaPendentePeriodoComRelacaoGrupoOptativa) {
		this.cargaHorariaPendentePeriodoComRelacaoGrupoOptativa = cargaHorariaPendentePeriodoComRelacaoGrupoOptativa;
	}

	/**
	 * @return the nrCreditosPendentePeriodoComRelacaoGrupoOptativa
	 */
	public Integer getNrCreditosPendentePeriodoComRelacaoGrupoOptativa() {
		return nrCreditosPendentePeriodoComRelacaoGrupoOptativa;
	}

	/**
	 * @param nrCreditosPendentePeriodoComRelacaoGrupoOptativa
	 *            the nrCreditosPendentePeriodoComRelacaoGrupoOptativa to set
	 */
	public void setNrCreditosPendentePeriodoComRelacaoGrupoOptativa(Integer nrCreditosPendentePeriodoComRelacaoGrupoOptativa) {
		this.nrCreditosPendentePeriodoComRelacaoGrupoOptativa = nrCreditosPendentePeriodoComRelacaoGrupoOptativa;
	}

	public HistoricoVO obterHistoricoAtualGradeDisciplinaVO(Integer codigoGradeDisciplina) {
		for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico : this.getGradeDisciplinaComHistoricoAlunoVOs()) {
			if (gradeDisciplinaComHistorico.getGradeDisciplinaVO().getCodigo().equals(codigoGradeDisciplina)) {
				return gradeDisciplinaComHistorico.getHistoricoAtualAluno();
			}
		}
		return new HistoricoVO();
	}

	public List<HistoricoVO> obterHistoricosDuplicadosGradeDisciplinaVO(Integer codigoGradeDisciplina) {
		for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistorico : this.getGradeDisciplinaComHistoricoAlunoVOs()) {
			if (gradeDisciplinaComHistorico.getGradeDisciplinaVO().getCodigo().equals(codigoGradeDisciplina) && gradeDisciplinaComHistorico.getHistoricosAluno().size() > 1) {
				return gradeDisciplinaComHistorico.getHistoricosAluno();
			}
		}
		return new ArrayList<HistoricoVO>(0);
	}

	public HistoricoVO obterHistoricoAtualGradeCurricularGrupoOptativaVO(Integer codigoDisciplinaGrupoOptatovaDisciplinaComHistorico) {
		for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptatovaDisciplinaComHistorico : this.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
			if (gradeCurricularGrupoOptatovaDisciplinaComHistorico.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo().equals(codigoDisciplinaGrupoOptatovaDisciplinaComHistorico)) {
				return gradeCurricularGrupoOptatovaDisciplinaComHistorico.getHistoricoAtualAluno();
			}
		}
		return new HistoricoVO();
	}

	/**
	 * Limpa dados relativos a histórico no periodoLetivo.
	 */
	public void limparDadosHistoricos() {
		for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplina : this.getGradeDisciplinaComHistoricoAlunoVOs()) {
			gradeDisciplina.getHistoricosAluno().clear();
			gradeDisciplina.setHistoricoAtualAluno(null);
		}
		for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeGrupo : this.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
			gradeGrupo.getHistoricosAluno().clear();
			gradeGrupo.setHistoricoAtualAluno(null);
		}
		this.getHistoricosDisciplinasAlunoCursandoPeriodoLetivo().clear();
		this.getHistoricosDisciplinasAprovadasAlunoPeriodoLetivo().clear();
	}

	@Override
	public String toString() {
		return "PeriodoLetivoComHistoricoAlunoVO [periodoLetivoVO=" + periodoLetivoVO + ", totalCargaHorariaCursadaAluno=" + totalCargaHorariaCursadaAluno + ", totalCreditosCursadosAluno=" + totalCreditosCursadosAluno + ", totalCargaHorariaAlunoEstaCursandoAtualmente=" + totalCargaHorariaAlunoEstaCursandoAtualmente + ", totalCreditosAlunoEstaCursandoAtualmente=" + totalCreditosAlunoEstaCursandoAtualmente + ", cargaHorariaPendenteAlunoPeriodo=" + cargaHorariaPendenteAlunoPeriodo + ", nrCreditosPendentesAlunoPeriodo=" + nrCreditosPendentesAlunoPeriodo + ", cargaHorariaPendentePeriodoComRelacaoGrupoOptativa=" + cargaHorariaPendentePeriodoComRelacaoGrupoOptativa + ", nrCreditosPendentePeriodoComRelacaoGrupoOptativa=" + nrCreditosPendentePeriodoComRelacaoGrupoOptativa + ", periodoIntegralizado=" + periodoIntegralizado + ", percentualIntegralizacaoPeriodoPorCargaHoraria=" + percentualIntegralizacaoPeriodoPorCargaHoraria + ", percentualIntegralizacaoPeriodoPorCreditos="
				+ percentualIntegralizacaoPeriodoPorCreditos + ", gradeDisciplinaComHistoricoAlunoVOs=" + gradeDisciplinaComHistoricoAlunoVOs + ", gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO=" + gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO + ", historicosDisciplinasAprovadasAlunoPeriodoLetivo=" + historicosDisciplinasAprovadasAlunoPeriodoLetivo + ", historicosDisciplinasAlunoCursandoPeriodoLetivo=" + historicosDisciplinasAlunoCursandoPeriodoLetivo + ", disciplinasPendentesAlunoPeriodoLetivo=" + disciplinasPendentesAlunoPeriodoLetivo + "]";
	}
	
	public PeriodoLetivoComHistoricoAlunoVO clone() throws CloneNotSupportedException {
		PeriodoLetivoComHistoricoAlunoVO clone = (PeriodoLetivoComHistoricoAlunoVO) super.clone();
		return clone;
	}
	
	public PeriodoLetivoComHistoricoAlunoVO() {
		super();
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if(periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}
}
