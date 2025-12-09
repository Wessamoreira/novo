package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

/**
 * @author Wellington Rodrigues - 16 de jul de 2015
 */
public class MapaNotaPendenciaAlunoRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String matricula;
	private String nomeCurso;
	private String identificadorTurma;
	private String nomeTurno;
	private String nomeAluno;
	private String disciplinas;
	private List<CrosstabVO> crosstabVOs;
	private List<MapaNotaPendenciaAlunoTurmaRelVO> mapaNotaPendenciaAlunoTurmaRelVOs;

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public String getNomeTurno() {
		if (nomeTurno == null) {
			nomeTurno = "";
		}
		return nomeTurno;
	}

	public void setNomeTurno(String nomeTurno) {
		this.nomeTurno = nomeTurno;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getDisciplinas() {
		if (disciplinas == null) {
			disciplinas = "";
		}
		return disciplinas;
	}

	public void setDisciplinas(String disciplinas) {
		this.disciplinas = disciplinas;
	}

	public List<CrosstabVO> getCrosstabVOs() {
		if (crosstabVOs == null) {
			crosstabVOs = new ArrayList<CrosstabVO>(0);
		}
		return crosstabVOs;
	}

	public void setCrosstabVOs(List<CrosstabVO> crosstabVOs) {
		this.crosstabVOs = crosstabVOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapaNotaPendenciaAlunoRelVO other = (MapaNotaPendenciaAlunoRelVO) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MapaNotaPendenciaAlunoRelVO [matricula=" + matricula + ", nomeCurso=" + nomeCurso + ", identificadorTurma=" + identificadorTurma + ", nomeTurno=" + nomeTurno + ", nomeAluno=" + nomeAluno + ", disciplinas=" + disciplinas + ", crosstabVOs=" + crosstabVOs + "]";
	}

	public List<MapaNotaPendenciaAlunoTurmaRelVO> getMapaNotaPendenciaAlunoTurmaRelVOs() {
		if (mapaNotaPendenciaAlunoTurmaRelVOs == null) {
			mapaNotaPendenciaAlunoTurmaRelVOs = new ArrayList<MapaNotaPendenciaAlunoTurmaRelVO>(0);
		}
		return mapaNotaPendenciaAlunoTurmaRelVOs;
	}

	public void setMapaNotaPendenciaAlunoTurmaRelVOs(List<MapaNotaPendenciaAlunoTurmaRelVO> mapaNotaPendenciaAlunoTurmaRelVOs) {
		this.mapaNotaPendenciaAlunoTurmaRelVOs = mapaNotaPendenciaAlunoTurmaRelVOs;
	}

}
