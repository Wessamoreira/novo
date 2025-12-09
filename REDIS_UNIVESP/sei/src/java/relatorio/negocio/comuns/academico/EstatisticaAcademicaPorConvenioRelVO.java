package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.basico.PessoaVO;

public class EstatisticaAcademicaPorConvenioRelVO {

//	private String matricula;
	private MatriculaVO matricula;
	private PessoaVO aluno;
	private String curso;
	private String identificadorTurma;
	private String turno;
	private Integer qtdeDisciplinasAprovadas;
	private Integer qtdeDisciplinasCursando;
	private Integer qtdeDisciplinasReprovadas;
	private Double mediaFrequencia;
	private Double mediaNotas;
	private Double totalNotas;
	private Double mediaAproveitamento;
	private String ano;
	private String semestre;
	private String convenio;
	private Boolean selecionado;
	private Boolean conclusaoCurso;


	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
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

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
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

	public Integer getQtdeDisciplinasAprovadas() {
		if (qtdeDisciplinasAprovadas == null) {
			qtdeDisciplinasAprovadas = 0;
		}
		return qtdeDisciplinasAprovadas;
	}

	public void setQtdeDisciplinasAprovadas(Integer qtdeDisciplinasAprovadas) {
		this.qtdeDisciplinasAprovadas = qtdeDisciplinasAprovadas;
	}

	public Integer getQtdeDisciplinasCursando() {
		if (qtdeDisciplinasCursando == null) {
			qtdeDisciplinasCursando = 0;
		}
		return qtdeDisciplinasCursando;
	}

	public void setQtdeDisciplinasCursando(Integer qtdeDisciplinasCursando) {
		this.qtdeDisciplinasCursando = qtdeDisciplinasCursando;
	}

	public Integer getQtdeDisciplinasReprovadas() {
		if (qtdeDisciplinasReprovadas == null) {
			qtdeDisciplinasReprovadas = 0;
		}
		return qtdeDisciplinasReprovadas;
	}

	public void setQtdeDisciplinasReprovadas(Integer qtdeDisciplinasReprovadas) {
		this.qtdeDisciplinasReprovadas = qtdeDisciplinasReprovadas;
	}

	public Double getMediaFrequencia() {
		if (mediaFrequencia == null) {
			mediaFrequencia = 0.0;
		}
		return mediaFrequencia;
	}

	public void setMediaFrequencia(Double mediaFrequencia) {
		this.mediaFrequencia = mediaFrequencia;
	}

	public Double getMediaNotas() {
		if (mediaNotas == null) {
			mediaNotas = 0.0;
		}
		return mediaNotas;
	}

	public void setMediaNotas(Double mediaNotas) {
		this.mediaNotas = mediaNotas;
	}

	public Double getMediaAproveitamento() {
		if (mediaAproveitamento == null) {
			mediaAproveitamento = 0.0;
		}
		return mediaAproveitamento;
	}

	public void setMediaAproveitamento(Double mediaAproveitamento) {
		this.mediaAproveitamento = mediaAproveitamento;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getConvenio() {
		if (convenio == null) {
			convenio = "";
		}
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
	}

	public Double getTotalNotas() {
		if (totalNotas == null) {
			totalNotas = 0.0;
		}
		return totalNotas;
	}

	public void setTotalNotas(Double totalNotas) {
		this.totalNotas = totalNotas;
	}

	public Boolean getConclusaoCurso() {
		if (conclusaoCurso == null) {
			conclusaoCurso = false;
		}
		return conclusaoCurso;
	}

	public void setConclusaoCurso(Boolean conclusaoCurso) {
		this.conclusaoCurso = conclusaoCurso;
	}
	
	

}
