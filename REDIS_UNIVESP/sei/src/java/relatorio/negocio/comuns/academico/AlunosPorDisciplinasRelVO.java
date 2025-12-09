package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class AlunosPorDisciplinasRelVO {

	private List<NrAlunosMatriculadosVO> turmaDisciplinaVOs;

	private String unidadeEnsino;
	private String curso;
	private String semestre;
	private String ano;
	private String turma;
	private String disciplina;
	private String turno;
	private String gradeCurricular;
	private String situacaoAcademica;

	public AlunosPorDisciplinasRelVO() {
	}

	public JRDataSource getListaTurmaDisciplinaVOs() {
		JRBeanCollectionDataSource jr = new JRBeanCollectionDataSource(getTurmaDisciplinaVOs());
		return jr;
	}

	public List<NrAlunosMatriculadosVO> getTurmaDisciplinaVOs() {
		if (turmaDisciplinaVOs == null) {
			turmaDisciplinaVOs = new ArrayList<NrAlunosMatriculadosVO>(0);
		}
		return turmaDisciplinaVOs;
	}

	public void setTurmaDisciplinaVOs(List<NrAlunosMatriculadosVO> turmaDisciplinaVOs) {
		this.turmaDisciplinaVOs = turmaDisciplinaVOs;
	}

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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getDisciplina() {

		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = "";
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(String gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public String getSituacaoAcademica() {
		if (situacaoAcademica == null) {
			situacaoAcademica = "";
		}
		return situacaoAcademica;
	}

	public void setSituacaoAcademica(String situacaoAcademica) {
		this.situacaoAcademica = situacaoAcademica;
	}

}
