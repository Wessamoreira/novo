package negocio.comuns.secretaria.enumeradores;

public enum SituacaoConvocadosEnadeEnum {
	ALUNO_INGRESSANTE("Ingressante"),
	ALUNO_DISPENSADO("Dispensado"),
	ALUNO_CONCLUINTE("Concluinte");
	
	private String descricao;
	
	private SituacaoConvocadosEnadeEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
