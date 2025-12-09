package webservice.nfse.generic;

public enum StatusRPSEnum {
	
	NORMAL((byte)1, "Normal"),
	CANCELADO((byte)2, "Cancelado");
	
	private byte id;
	private String nome;
	
	StatusRPSEnum(byte id, String nome) {
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
