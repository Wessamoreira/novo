package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoPeriodoLetivoEnum {

	TODOS, FAIXA_PERIODO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoPeriodoLetivoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
