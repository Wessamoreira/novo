package relatorio.negocio.comuns.academico;

public class DeclaracaoAbandonoCursoVO {

	protected String nome;
	protected String rg;
	protected String cpf;
	protected String periodoLetivo;
	protected String curso;
	protected String data;
	protected String matricula;
	protected String unidadeEnsino;

	public DeclaracaoAbandonoCursoVO() {
		setNome("");
		setRg("");
		setCpf("");
		setPeriodoLetivo("");
		setCurso("");
		setData("");
		setMatricula("");
		setUnidadeEnsino("");
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPeriodoLetivo() {
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

}