package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoLayoutPlanoContaEnum {
	NENHUM, CREDITO, DEBITO;

	public boolean isNenhum() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoLayoutPlanoContaEnum.NENHUM.name());
	}

	public boolean isCredito() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoLayoutPlanoContaEnum.CREDITO.name());
	}

	public boolean isDebito() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoLayoutPlanoContaEnum.DEBITO.name());
	}

}
