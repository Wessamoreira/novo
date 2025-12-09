package negocio.comuns.financeiro.enumerador;

public enum SituacaoRegistroDetalheEnum {
	NENHUM, CONFIRMADO, REJEITADO, BAIXADO;
	
	
	public boolean isSituacaoConfirmado() {
	 return name() != null && name().equals(SituacaoRegistroDetalheEnum.CONFIRMADO.name());
	}
	
	public boolean isSituacaoRejeitado() {
		return name() != null && name().equals(SituacaoRegistroDetalheEnum.REJEITADO.name());
	}
	
	public boolean isSituacaoBaixado() {
		return name() != null
				&& !name().equals(SituacaoRegistroDetalheEnum.CONFIRMADO.name()) 
				&& !name().equals(SituacaoRegistroDetalheEnum.REJEITADO.name());
	}
}
