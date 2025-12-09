package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoParcelaNegociarEnum {
	VENCIDAS, A_VENCER, AMBAS;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoParcelaNegociarEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}

	public String getSigla() {
		return UteisJSF.internacionalizar("enum_SiglaTipoParcelaNegociarEnum_" + this.name());
	}
}
