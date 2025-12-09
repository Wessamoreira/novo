package webservice.nfse.generic;

public enum ExigibilidadeISSEnum {
	
	EXIGIVEL((byte)1, "Exigível"),
	NAO_INCIDENCIA ((byte)2, "Não incidência"),
	ISENCAO((byte)3, "Isenção"),
	EXPORTACAO ((byte)4, "Exportação"),
	IMUNIDADE((byte)5, "Imunidade"),
	EXIGIBILIDADE_SUSPENSA_POR_DECISAO_JUDICIAL((byte)6, "Exigibilidade Suspensa por Decisão Judicial"),
	EXIGIBILIDADE_SUSPENSA_POR_PROCESSO_ADMINISTRATIVO((byte)7, "Exigibilidade Suspensa por Processo Administrativo");
	
	private byte id;
	private String nome;
	
	ExigibilidadeISSEnum(byte id, String nome) {
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
