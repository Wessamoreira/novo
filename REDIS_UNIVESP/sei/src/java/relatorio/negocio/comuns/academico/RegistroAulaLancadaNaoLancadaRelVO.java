package relatorio.negocio.comuns.academico;

import java.util.Date;

public class RegistroAulaLancadaNaoLancadaRelVO {

	private String unidadeEnsino;
	private String curso;
	private String disciplina;
	private Integer qtdAulasRegistradas;
	private Integer qtdAulasNaoRegistradas;
	private Date dataAula;
	private String usuario;
	private Date dataLancamento;
	private String identificadorTurma;
	private String nomeProfessor;
	private String nomeTurno;

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
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

	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public Date getDataAula() {
		return dataAula;
	}

	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

	public String getUsuario() {
		if (usuario == null) {
			usuario = "";
		}
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
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

	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
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

	public Integer getQtdAulasNaoRegistradas() {
		if (qtdAulasNaoRegistradas == null) {
			qtdAulasNaoRegistradas = 0;
		}
		return qtdAulasNaoRegistradas;
	}

	public void setQtdAulasNaoRegistradas(Integer qtdAulasNaoRegistradas) {
		this.qtdAulasNaoRegistradas = qtdAulasNaoRegistradas;
	}

	public Integer getQtdAulasRegistradas() {
		if (qtdAulasRegistradas == null) {
			qtdAulasRegistradas = 0;
		}
		return qtdAulasRegistradas;
	}

	public void setQtdAulasRegistradas(Integer qtdAulasRegistradas) {
		this.qtdAulasRegistradas = qtdAulasRegistradas;
	}

	public String getOrdenacao() {
		return getCurso() + getNomeTurno() + getIdentificadorTurma() + getDisciplina() + getNomeProfessor() + getDataAula();
	}

}
