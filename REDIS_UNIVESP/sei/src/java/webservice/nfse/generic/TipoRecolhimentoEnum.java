package webservice.nfse.generic;

public enum TipoRecolhimentoEnum {
	A_RECEBER("A", "A Receber"), RETIDO_NA_FONTE("R", "Retido na Fonte");
	private String id;
	private String nome;
	
	TipoRecolhimentoEnum(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
}
