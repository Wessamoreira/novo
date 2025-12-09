package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoLayoutComprovanteEnum {
	
	LAYOUT_1, LAYOUT_2;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoLayoutComprovante_" + name());
	}

}
