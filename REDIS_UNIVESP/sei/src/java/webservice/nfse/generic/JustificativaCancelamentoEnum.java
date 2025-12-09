package webservice.nfse.generic;

public enum JustificativaCancelamentoEnum {
	
	ERRO_NA_EMISSAO(1, "Erro na emissão"),
	SERVICO_NAO_PRESTADO(2, "Serviço não prestado"),
	DUPLICIDADE_DA_NOTA(4, "Duplicidade da nota");
	
	private int id;
	private String nome;
	
	JustificativaCancelamentoEnum(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
}
