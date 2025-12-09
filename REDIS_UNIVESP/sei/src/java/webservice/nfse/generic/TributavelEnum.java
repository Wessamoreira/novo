package webservice.nfse.generic;

public enum TributavelEnum {
	ITEM_TRIBUTAVEL("S", "Item tributável"), NAO_TRIBUTAVEL("N", "Não tributável");
	private String id;
	private String nome;

	TributavelEnum(String id, String nome) {
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
