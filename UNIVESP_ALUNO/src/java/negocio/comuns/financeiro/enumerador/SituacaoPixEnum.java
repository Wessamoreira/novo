package negocio.comuns.financeiro.enumerador;
/*
 */
public enum SituacaoPixEnum {
	NENHUM,
	RECEBIDO,
	CANCELADO,
	NAO_LOCALIZADO_PIX,
	NAO_LOCALIZADO_CONTARECEBER,
	CONTARECEBER_DUPLICIDADE,
	EM_PROCESSAMENTO;
	
	public boolean isNenhum() {
		return name() != null && name().equals(SituacaoPixEnum.NENHUM.name());
	}
	
	public boolean isEmProcessamento() {
		return name() != null && name().equals(SituacaoPixEnum.EM_PROCESSAMENTO.name());
	}
	
	public boolean isRecebido() {
		return name() != null && name().equals(SituacaoPixEnum.RECEBIDO.name());
	}
	
	public boolean isContaReceberDuplicidade() {
		return name() != null && name().equals(SituacaoPixEnum.CONTARECEBER_DUPLICIDADE.name());
	}
	
	public boolean isNaoLocalizadoPix() {
		return name() != null && name().equals(SituacaoPixEnum.NAO_LOCALIZADO_PIX.name());
	}
	
	public boolean isNaoLocalizadoContaReceber() {
		return name() != null && name().equals(SituacaoPixEnum.NAO_LOCALIZADO_CONTARECEBER.name());
	}
	
	
}
