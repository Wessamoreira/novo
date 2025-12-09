package negocio.comuns.administrativo;

import negocio.comuns.administrativo.enumeradores.OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum;


public class PainelGestorMonitoramentoProcessoSeletivoVO {
	
	private String graficoGeral;
	private String graficoPorCurso;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private Integer qtdeInscritoGeral;
	private Integer qtdeInscritoConfirmado;
	private Integer qtdeInscritoNaoConfirmado;
	private Integer qtdeInscritoPreMatriculado;
	private Integer qtdeInscritoMatriculado;
	private Integer qtdeInscritoAprovadoNaoMatriculado;
	private Integer qtdeInscritoSemResultado;
    private Integer qtdeInscritoNaoCompareceu;	
	private Integer qtdeInscritoAprovado;
	private Integer qtdeInscritoReprovado;
	private OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcoesFiltroPainelGestorMonitoramentoAcademico;
	
	public OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum getOpcoesFiltroPainelGestorMonitoramentoAcademico() {
		if (opcoesFiltroPainelGestorMonitoramentoAcademico == null) {
			opcoesFiltroPainelGestorMonitoramentoAcademico = OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INICIAL;
		}
		return opcoesFiltroPainelGestorMonitoramentoAcademico;
	}

	public void setOpcoesFiltroPainelGestorMonitoramentoAcademico(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcoesFiltroPainelGestorMonitoramentoAcademico) {
		this.opcoesFiltroPainelGestorMonitoramentoAcademico = opcoesFiltroPainelGestorMonitoramentoAcademico;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}
	
	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}
	public String getGraficoGeral() {
		if (graficoGeral == null) {
			graficoGeral = "";
		}
		return graficoGeral;
	}
	public void setGraficoGeral(String graficoGeral) {
		this.graficoGeral = graficoGeral;
	}
	public String getGraficoPorCurso() {
		if (graficoPorCurso == null) {
			graficoPorCurso = "";
		}
		return graficoPorCurso;
	}
	public void setGraficoPorCurso(String graficoPorCurso) {
		this.graficoPorCurso = graficoPorCurso;
	}
	public Integer getQtdeInscritoGeral() {
		if (qtdeInscritoGeral == null) {
			qtdeInscritoGeral = 0;
		}
		return qtdeInscritoGeral;
	}
	public void setQtdeInscritoGeral(Integer qtdeInscritoGeral) {
		this.qtdeInscritoGeral = qtdeInscritoGeral;
	}
	public Integer getQtdeInscritoConfirmado() {
		if (qtdeInscritoConfirmado == null) {
			qtdeInscritoConfirmado = 0;
		}
		return qtdeInscritoConfirmado;
	}
	public void setQtdeInscritoConfirmado(Integer qtdeInscritoConfirmado) {
		this.qtdeInscritoConfirmado = qtdeInscritoConfirmado;
	}
	public Integer getQtdeInscritoNaoConfirmado() {
		if (qtdeInscritoNaoConfirmado == null) {
			qtdeInscritoNaoConfirmado = 0;
		}
		return qtdeInscritoNaoConfirmado;
	}
	public void setQtdeInscritoNaoConfirmado(Integer qtdeInscritoNaoConfirmado) {
		this.qtdeInscritoNaoConfirmado = qtdeInscritoNaoConfirmado;
	}
	public Integer getQtdeInscritoPreMatriculado() {
		if (qtdeInscritoPreMatriculado == null) {
			qtdeInscritoPreMatriculado = 0;
		}
		return qtdeInscritoPreMatriculado;
	}
	public void setQtdeInscritoPreMatriculado(Integer qtdeInscritoPreMatriculado) {
		this.qtdeInscritoPreMatriculado = qtdeInscritoPreMatriculado;
	}
	public Integer getQtdeInscritoMatriculado() {
		if (qtdeInscritoMatriculado == null) {
			qtdeInscritoMatriculado = 0;
		}
		return qtdeInscritoMatriculado;
	}
	public void setQtdeInscritoMatriculado(Integer qtdeInscritoMatriculado) {
		this.qtdeInscritoMatriculado = qtdeInscritoMatriculado;
	}
	public Integer getQtdeInscritoAprovadoNaoMatriculado() {
		if (qtdeInscritoAprovadoNaoMatriculado == null) {
			qtdeInscritoAprovadoNaoMatriculado = 0;
		}
		return qtdeInscritoAprovadoNaoMatriculado;
	}
	public void setQtdeInscritoAprovadoNaoMatriculado(Integer qtdeInscritoAprovadoNaoMatriculado) {
		this.qtdeInscritoAprovadoNaoMatriculado = qtdeInscritoAprovadoNaoMatriculado;
	}
	public Integer getQtdeInscritoSemResultado() {
		if (qtdeInscritoSemResultado == null) {
			qtdeInscritoSemResultado = 0;
		}
		return qtdeInscritoSemResultado;
	}
	public void setQtdeInscritoSemResultado(Integer qtdeInscritoSemResultado) {
		this.qtdeInscritoSemResultado = qtdeInscritoSemResultado;
	}
	public Integer getQtdeInscritoAprovado() {
		if (qtdeInscritoAprovado == null) {
			qtdeInscritoAprovado = 0;
		}
		return qtdeInscritoAprovado;
	}
	public void setQtdeInscritoAprovado(Integer qtdeInscritoAprovado) {
		this.qtdeInscritoAprovado = qtdeInscritoAprovado;
	}
	public Integer getQtdeInscritoReprovado() {
		if (qtdeInscritoReprovado == null) {
			qtdeInscritoReprovado = 0;
		}
		return qtdeInscritoReprovado;
	}
	public void setQtdeInscritoReprovado(Integer qtdeInscritoReprovado) {
		this.qtdeInscritoReprovado = qtdeInscritoReprovado;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
    /**
     * @return the qtdeInscritoNaoCompareceu
     */
    public Integer getQtdeInscritoNaoCompareceu() {
        if (qtdeInscritoNaoCompareceu == null) {
            qtdeInscritoNaoCompareceu = 0;
        }
        return qtdeInscritoNaoCompareceu;
    }

    /**
     * @param qtdeInscritoNaoCompareceu the qtdeInscritoNaoCompareceu to set
     */
    public void setQtdeInscritoNaoCompareceu(Integer qtdeInscritoNaoCompareceu) {
        this.qtdeInscritoNaoCompareceu = qtdeInscritoNaoCompareceu;
    }
}
