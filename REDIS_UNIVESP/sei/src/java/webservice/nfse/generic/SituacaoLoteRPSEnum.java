package webservice.nfse.generic;

public enum SituacaoLoteRPSEnum {
	
	NAO_RECEBIDO((byte)1, "Não Recebido"),
	NAO_PROCESSADO((byte)2, "Não Processado"),
	PROCESSADO_COM_ERRO((byte)3, "Processado com Erro"),
	PROCESSADO_COM_SUCESSO((byte)4, "Processado com Sucesso");
	
	private byte id;
	private String nome;
	
	SituacaoLoteRPSEnum(byte id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public byte getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public static SituacaoLoteRPSEnum getById(byte id) {
		SituacaoLoteRPSEnum retorno = null;
		for (SituacaoLoteRPSEnum situacao : SituacaoLoteRPSEnum.values()) {
			if(id == situacao.getId()) {
				retorno = situacao;
				break;
			}
		}
		return retorno;
	}
}
