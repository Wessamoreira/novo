package webservice.nfse.generic;

public enum TipoRPSEnum {
	
	RECIBO_PROVISORIO_DE_SERVICOS((byte)1, "Recibo Provisório de Serviços"),
	RPS_NOTA_FISCAL_CONJUGADA_MISTA((byte)2, "RPS  Nota Fiscal Conjugada (Mista)"), 
	CUPOM((byte)3, "Cupom");
	
	private byte id;
	private String nome;
	
	TipoRPSEnum(byte id, String nome) {
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
