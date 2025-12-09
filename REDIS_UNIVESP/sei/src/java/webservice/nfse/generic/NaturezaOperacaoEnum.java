package webservice.nfse.generic;

public enum NaturezaOperacaoEnum {
	
	TRIBUTACAO_NO_MUNICIPIO((byte)1, "Tributação no município"),
	TRIBUTACAO_FORA_DO_MUNICIPIO((byte)2, "Tributação fora do município"), 
	ISENCAO((byte)3, "Isenção"),
	IMUNE((byte)4, "Imune"),
	EXIGIBILIDADE_SUSPENSA_POR_DECISAO_JUDICIAL((byte)5, "Exigibilidade suspensa por decisão judicial"),
	EXIGIBILIDADE_SUSPENSA_POR_PROCEDIMENTO_ADMINISTRATIVO((byte)6, "Exigibilidade suspensa por procedimento administrativo");
	
	private byte id;
	private String nome;
	
	NaturezaOperacaoEnum(byte id, String nome) {
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
