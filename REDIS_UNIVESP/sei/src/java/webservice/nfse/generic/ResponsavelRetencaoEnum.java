package webservice.nfse.generic;

public enum ResponsavelRetencaoEnum {
	
	TOMADOR((byte)1, "Tomador"),
	INTERMEDIARIO ((byte)2, "Intermediário ");
	
	private byte id;
	private String nome;
	
	ResponsavelRetencaoEnum(byte id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public byte getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
}
