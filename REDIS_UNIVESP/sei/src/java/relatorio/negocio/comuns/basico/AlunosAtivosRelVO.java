package relatorio.negocio.comuns.basico;

import java.io.Serializable;

public class AlunosAtivosRelVO implements Serializable{

	private static final long serialVersionUID = 1L;
	protected Integer unidadeEnsinoId;
	protected String unidadeEnsino;
	protected String matriculaAluno;
	protected String nomeAluno;
	protected String curso;
	protected String turma;
	protected String periodoLetivo;
	protected Integer quantidadeAlunosUnidade;

	public AlunosAtivosRelVO() {}

	public Integer getQuantidadeAlunosUnidade() {
            if (quantidadeAlunosUnidade == null) {
                quantidadeAlunosUnidade = 0;
            }
            return quantidadeAlunosUnidade;
	}

	public void setQuantidadeAlunosUnidade(Integer quantidadeAlunosUnidade) {
		this.quantidadeAlunosUnidade = quantidadeAlunosUnidade;
	}

	public String getCurso() {
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

	public Integer getUnidadeEnsinoId() {
		if (unidadeEnsinoId == null) {
			unidadeEnsinoId = 0;
		}
		return unidadeEnsinoId;
	}

	public void setUnidadeEnsinoId(Integer unidadeEnsinoId) {
		this.unidadeEnsinoId = unidadeEnsinoId;
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

	public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = "";
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
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

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

}
