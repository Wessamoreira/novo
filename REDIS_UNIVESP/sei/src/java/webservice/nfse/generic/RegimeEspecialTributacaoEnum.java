package webservice.nfse.generic;

public enum RegimeEspecialTributacaoEnum {
	
	MICROEMPRESA_MUNICIPAL((byte)1, "Microempresa Municipal"),
	ESTIMATIVA((byte)2, "Estimativa"), 
	SOCIEDADE_DE_PROFISSIONAIS((byte)3, "Sociedade de Profissionais"),
	COOPERATIVA((byte)4, "Cooperativa"),
	MEI_SIMPLES_NACIONAL((byte)5, "MEI - Simples Nacional"),
	ME_EPP_SIMPLES_NACIONAL((byte)6, "ME EPP - Simples Nacional");
	
	private byte id;
	private String nome;
	
	RegimeEspecialTributacaoEnum(byte id, String nome) {
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
