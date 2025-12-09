package relatorio.negocio.comuns.financeiro;

public class SerasaRelVO {

	private String matriculaAluno;
	private String nomeAluno;
	private Integer numContas;
	private Double valorTotal;

	public SerasaRelVO() {
		setMatriculaAluno("");
		setNomeAluno("");
		setNumContas(0);
		setValorTotal(0.0);
	}

	public String getMatriculaAluno() {
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public Integer getNumContas() {
		return numContas;
	}

	public void setNumContas(Integer numContas) {
		this.numContas = numContas;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

}