package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class MatriculaCamposAlteradosVO extends SuperVO{

	private static final long serialVersionUID = 1L;
	
//	Matrícula
	private Boolean matricula;
	private Boolean situacao;
	private Boolean curso;
	private Boolean turno;
	private Boolean unidadeEnsino;
	private Boolean aluno;
	private Boolean formaIngresso;
	private Boolean anoIngresso;
	private Boolean semestreIngresso;
	private Boolean dataInicioCurso;
	private Boolean gradeCurricularAtual;
	
//	MatrículaPeriodo
	private Boolean turma;
	private Boolean gradeCurricular;
	private Boolean situacaoMatriculaPeriodo;
	private Boolean ano;
	private Boolean semestre;
	private Boolean periodoLetivoMatricula;
	private Boolean data;
	
	public Boolean getMatricula() {
		if (matricula == null) {
			matricula = Boolean.FALSE;
		}
		return matricula;
	}
	public void setMatricula(Boolean matricula) {
		this.matricula = matricula;
	}
	public Boolean getSituacao() {
		if (situacao == null) {
			situacao = Boolean.FALSE;
		}
		return situacao;
	}
	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}
	public Boolean getCurso() {
		if (curso == null) {
			curso = Boolean.FALSE;
		}
		return curso;
	}
	public void setCurso(Boolean curso) {
		this.curso = curso;
	}
	public Boolean getTurno() {
		if (turno == null) {
			turno = Boolean.FALSE;
		}
		return turno;
	}
	public void setTurno(Boolean turno) {
		this.turno = turno;
	}
	public Boolean getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = Boolean.FALSE;
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(Boolean unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	public Boolean getAluno() {
		if (aluno == null) {
			aluno = Boolean.FALSE;
		}
		return aluno;
	}
	public void setAluno(Boolean aluno) {
		this.aluno = aluno;
	}
	public Boolean getFormaIngresso() {
		if (formaIngresso == null) {
			formaIngresso = Boolean.FALSE;
		}
		return formaIngresso;
	}
	public void setFormaIngresso(Boolean formaIngresso) {
		this.formaIngresso = formaIngresso;
	}
	public Boolean getAnoIngresso() {
		if (anoIngresso == null) {
			anoIngresso = Boolean.FALSE;
		}
		return anoIngresso;
	}
	public void setAnoIngresso(Boolean anoIngresso) {
		this.anoIngresso = anoIngresso;
	}
	public Boolean getSemestreIngresso() {
		if (semestreIngresso == null) {
			semestreIngresso = Boolean.FALSE;
		}
		return semestreIngresso;
	}
	public void setSemestreIngresso(Boolean semestreIngresso) {
		this.semestreIngresso = semestreIngresso;
	}
	public Boolean getDataInicioCurso() {
		if (dataInicioCurso == null) {
			dataInicioCurso = Boolean.FALSE;
		}
		return dataInicioCurso;
	}
	public void setDataInicioCurso(Boolean dataInicioCurso) {
		this.dataInicioCurso = dataInicioCurso;
	}
	public Boolean getTurma() {
		if (turma == null) {
			turma = Boolean.FALSE;
		}
		return turma;
	}
	public void setTurma(Boolean turma) {
		this.turma = turma;
	}
	public Boolean getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = Boolean.FALSE;
		}
		return gradeCurricular;
	}
	public void setGradeCurricular(Boolean gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}
	public Boolean getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = Boolean.FALSE;
		}
		return situacaoMatriculaPeriodo;
	}
	public void setSituacaoMatriculaPeriodo(Boolean situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}
	public Boolean getAno() {
		if (ano == null) {
			ano = Boolean.FALSE;
		}
		return ano;
	}
	public void setAno(Boolean ano) {
		this.ano = ano;
	}
	public Boolean getSemestre() {
		if (semestre == null) {
			semestre = Boolean.FALSE;
		}
		return semestre;
	}
	public void setSemestre(Boolean semestre) {
		this.semestre = semestre;
	}
	public Boolean getPeriodoLetivoMatricula() {
		if (periodoLetivoMatricula == null) {
			periodoLetivoMatricula = Boolean.FALSE;
		}
		return periodoLetivoMatricula;
	}
	public void setPeriodoLetivoMatricula(Boolean periodoLetivoMatricula) {
		this.periodoLetivoMatricula = periodoLetivoMatricula;
	}
	public Boolean getData() {
		if (data == null) {
			data = Boolean.FALSE;
		}
		return data;
	}
	public void setData(Boolean data) {
		this.data = data;
	}
	public Boolean getGradeCurricularAtual() {
		if (gradeCurricularAtual == null) {
			gradeCurricularAtual = Boolean.FALSE;
		}
		return gradeCurricularAtual;
	}
	public void setGradeCurricularAtual(Boolean gradeCurricularAtual) {
		this.gradeCurricularAtual = gradeCurricularAtual;
	}
}
