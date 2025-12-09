package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

public class EstatisticaMatriculaRelVO {

	private String unidadeEnsino;
	private String curso;
	private List<EstatisticaMatriculaTurnoRelVO> estatisticaMatriculaTurnoRelVOs;
	private Integer totalPorUnidadeEnsino;

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public List<EstatisticaMatriculaTurnoRelVO> getEstatisticaMatriculaTurnoRelVOs() {
		if (estatisticaMatriculaTurnoRelVOs == null) {
			estatisticaMatriculaTurnoRelVOs = new ArrayList<EstatisticaMatriculaTurnoRelVO>(0);
		}
		return estatisticaMatriculaTurnoRelVOs;
	}

	public void setEstatisticaMatriculaTurnoRelVOs(List<EstatisticaMatriculaTurnoRelVO> estatisticaMatriculaTurnoRelVOs) {
		this.estatisticaMatriculaTurnoRelVOs = estatisticaMatriculaTurnoRelVOs;
	}

	public Integer getTotalPorUnidadeEnsino() {
		if (totalPorUnidadeEnsino == null) {
			totalPorUnidadeEnsino = 0;
		}
		return totalPorUnidadeEnsino;
	}

	public void setTotalPorUnidadeEnsino(Integer totalPorUnidadeEnsino) {
		this.totalPorUnidadeEnsino = totalPorUnidadeEnsino;
	}
}
