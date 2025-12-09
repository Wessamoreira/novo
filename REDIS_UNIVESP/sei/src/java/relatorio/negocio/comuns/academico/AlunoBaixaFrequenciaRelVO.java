package relatorio.negocio.comuns.academico;

import negocio.comuns.basico.PessoaVO;

public class AlunoBaixaFrequenciaRelVO {
	
	private String nomeUnidadeEnsino;
	private String curso;
	private String turma;
	private String disciplina;
	private String nomeAluno;
	private String matricula;
	private Integer totalFaltas;
	private Integer totalAulasProgramadas;
	private Double percentualFaltas;
	private PessoaVO coordenador;
	
	
	public String getNomeUnidadeEnsino() {
		if(nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}
	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}
	public String getCurso() {
		if(curso == null) {
			curso = "";
		}
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public String getTurma() {
		if(turma == null) {
			turma = "";
		}
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	
	public String getDisciplina() {
		if(disciplina == null) {
			disciplina ="";
		}
		return disciplina;
	}
	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}
	public String getNomeAluno() {
		if(nomeAluno == null) {
			nomeAluno ="";
		}
		return nomeAluno;
	}
	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
	public String getMatricula() {
		if(matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Integer getTotalFaltas() {
		return totalFaltas;
	}
	public void setTotalFaltas(Integer totalFaltas) {
		this.totalFaltas = totalFaltas;
	}
	public Integer getTotalAulasProgramadas() {
		return totalAulasProgramadas;
	}
	public void setTotalAulasProgramadas(Integer totalAulasProgramadas) {
		this.totalAulasProgramadas = totalAulasProgramadas;
	}
	public Double getPercentualFaltas() {
		return percentualFaltas;
	}
	public void setPercentualFaltas(Double percentualFaltas) {
		this.percentualFaltas = percentualFaltas;
	}
	public PessoaVO getCoordenador() {
		if(coordenador == null) {
			coordenador = new PessoaVO();
		}
		return coordenador;
	}
	public void setCoordenador(PessoaVO coordenador) {
		this.coordenador = coordenador;
	}
}
