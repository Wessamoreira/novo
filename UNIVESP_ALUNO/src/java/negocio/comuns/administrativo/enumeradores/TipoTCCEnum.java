package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoTCCEnum {
	ARTIGO, MONOGRAFIA, AMBOS;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoTCCEnum_"+this.name());
	}
	
}
