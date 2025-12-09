package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoBackgroundEnum {
	SOLIDO, GRADIENTE;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoBackgroundEnum_"+this.name());
	}
	public String getSiglaApresentar() {
		return UteisJSF.internacionalizar("enum_TipoBackgroundEnum_"+this.name()+"_");
	}
}
