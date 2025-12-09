package relatorio.negocio.comuns.academico;

public class AlunosAtivosInativosVO {

	private String nomeTurma;
	private String situacao;
	private int nrAlunosTotal;

	public AlunosAtivosInativosVO() {

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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getNrAlunosTotal() {
		return nrAlunosTotal;
	}

	public void setNrAlunosTotal(int nrAlunosTotal) {
		this.nrAlunosTotal = nrAlunosTotal;
	}

}