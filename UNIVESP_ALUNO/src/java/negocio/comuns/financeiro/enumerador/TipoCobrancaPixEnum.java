package negocio.comuns.financeiro.enumerador;

public enum TipoCobrancaPixEnum {
	
	IMEDIATA,
	VENCIMENTO;
	
	public boolean isImediata() {
		return name() != null && name().equals(TipoCobrancaPixEnum.IMEDIATA.name());
	}
	
	public boolean isVencimento() {
		return name() != null && name().equals(TipoCobrancaPixEnum.VENCIMENTO.name());
	}
	

}
