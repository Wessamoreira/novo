package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum NivelImportanciaEnum {

	BAIXA, NORMAL, ALTA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_NivelImportanciaEnum_"+ this.name());
	}
}
