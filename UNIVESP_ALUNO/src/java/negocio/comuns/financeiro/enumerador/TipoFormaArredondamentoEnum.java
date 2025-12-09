package negocio.comuns.financeiro.enumerador;

public enum TipoFormaArredondamentoEnum {
	ARREDONDADO, TRUNCADO;

	public boolean isArredondado() {
		return this.name() != null && this.name().equals(TipoFormaArredondamentoEnum.ARREDONDADO.name());
	}
	
	public boolean isTruncado() {
		return this.name() != null && this.name().equals(TipoFormaArredondamentoEnum.TRUNCADO.name());
	}

}
