package relatorio.negocio.comuns.academico;

public class AlunosReprovadosRelVO {

	private String nomeUnidadeEnsino;
	private String nomeCurso;
	private String nomeTurma;
	private String nomeDisciplina;
	private String nomeAluno;
	private Integer codigoAluno;
	private String emailAluno;
	private String matricula;
	private Double nota;
	private String ano;
	private String semestre;
	private Boolean realizouReposicao;

	public AlunosReprovadosRelVO() {

	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
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

	public String getNomeTurma() {
		if (nomeTurma == null) {
			nomeTurma = "";
		}
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
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

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Double getNota() {
		if (nota == null) {
			nota = 0.0;
		}
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
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

	public Boolean getRealizouReposicao() {
		if(realizouReposicao == null){
			realizouReposicao = false;
		}
		return realizouReposicao;
	}

	public void setRealizouReposicao(Boolean realizouReposicao) {
		this.realizouReposicao = realizouReposicao;
	}

	public Integer getCodigoAluno() {
		if(codigoAluno == null){
			codigoAluno = 0;
		}
		return codigoAluno;
	}

	public void setCodigoAluno(Integer codigoAluno) {
		this.codigoAluno = codigoAluno;
	}

	public String getEmailAluno() {
		if(emailAluno == null){
			emailAluno = "";
		}
		return emailAluno;
	}

	public void setEmailAluno(String emailAluno) {
		this.emailAluno = emailAluno;
	}
	
	

}
