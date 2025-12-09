package relatorio.negocio.comuns.academico;

public class AlunosMatriculadosGeralSituacaoMatriculaRelVO {

	private String descricao;
	private Integer total;

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getTotal() {
		if (total == null) {
			total = 0;
		}
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
