package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Manoel
 */
public class AtividadeComplementarRelVO {
	private String matricula;
	private String nomeAluno;
	private String curso;
	private String turma;
	private Integer qtdHorasExigidas;
	private Integer qtdHorasRealizadas;
	private Integer qtdHorasConsideradas;
	private Integer gradeCurricular;
	private Integer totalHorasConsideradas;
	private Integer qtdAlunoCumpriuAtividadeComplementar;
	private Integer qtdAlunoNaoCumpriuAtividadeComplementar;
	private List<EventoAtividadeComplementarVO> listaEventoAtividadeComplementar;

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
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

	public Integer getQtdHorasExigidas() {
		if (qtdHorasExigidas == null) {
			qtdHorasExigidas = 0;
		}
		return qtdHorasExigidas;
	}

	public void setQtdHorasExigidas(Integer qtdHorasExigidas) {
		this.qtdHorasExigidas = qtdHorasExigidas;
	}

	public Integer getQtdHorasRealizadas() {
		if (qtdHorasRealizadas == null) {
			qtdHorasRealizadas = 0;
		}
		return qtdHorasRealizadas;
	}

	public void setQtdHorasRealizadas(Integer qtdHorasRealizadas) {
		this.qtdHorasRealizadas = qtdHorasRealizadas;
	}

	public Integer getQtdHorasConsideradas() {
		if (qtdHorasConsideradas == null) {
			qtdHorasConsideradas = 0;
		}
		return qtdHorasConsideradas;
	}

	public void setQtdHorasConsideradas(Integer qtdHorasConsideradas) {
		this.qtdHorasConsideradas = qtdHorasConsideradas;
	}

	public Integer getQtdHorasPendentes() {
		if (getTotalHorasConsideradas() < getQtdHorasExigidas()) {
			return getQtdHorasExigidas() - getTotalHorasConsideradas();
		}
		return 0;
	}

	public boolean getExistePendencia() {
		return getQtdHorasPendentes() > 0;
	}

	public Integer getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = 0;
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(Integer gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public Integer getTotalHorasConsideradas() {
		if (totalHorasConsideradas == null) {
			totalHorasConsideradas = 0;
		}
		return totalHorasConsideradas;
	}

	public void setTotalHorasConsideradas(Integer totalHorasConsideradas) {
		this.totalHorasConsideradas = totalHorasConsideradas;
	}

	public Integer getQtdAlunoCumpriuAtividadeComplementar() {
		if (qtdAlunoCumpriuAtividadeComplementar == null) {
			qtdAlunoCumpriuAtividadeComplementar = 0;
		}

		return qtdAlunoCumpriuAtividadeComplementar;
	}

	public void setQtdAlunoCumpriuAtividadeComplementar(Integer qtdAlunoCumpriuAtividadeComplementar) {
		this.qtdAlunoCumpriuAtividadeComplementar = qtdAlunoCumpriuAtividadeComplementar;
	}

	public Integer getQtdAlunoNaoCumpriuAtividadeComplementar() {
		if (qtdAlunoNaoCumpriuAtividadeComplementar == null) {
			qtdAlunoNaoCumpriuAtividadeComplementar = 0;
		}
		return qtdAlunoNaoCumpriuAtividadeComplementar;
	}

	public void setQtdAlunoNaoCumpriuAtividadeComplementar(Integer qtdAlunoNaoCumpriuAtividadeComplementar) {
		this.qtdAlunoNaoCumpriuAtividadeComplementar = qtdAlunoNaoCumpriuAtividadeComplementar;
	}

	public List<EventoAtividadeComplementarVO> getListaEventoAtividadeComplementar() {
		if (listaEventoAtividadeComplementar == null) {
			listaEventoAtividadeComplementar = new ArrayList<EventoAtividadeComplementarVO>(0);
		}
		return listaEventoAtividadeComplementar;
	}

	public void setListaEventoAtividadeComplementar(List<EventoAtividadeComplementarVO> listaEventoAtividadeComplementar) {
		this.listaEventoAtividadeComplementar = listaEventoAtividadeComplementar;
	}

	public String getSituacao() {
		if (this.getQtdHorasExigidas() > this.getTotalHorasConsideradas()) {
			return "Pendente";
		}
		return "Concluído";
	}

}
